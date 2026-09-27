package cn.handyplus.guild.util;

import cn.handyplus.guild.constants.GuildRoleEnum;
import cn.handyplus.guild.enter.GuildInfo;
import cn.handyplus.guild.enter.GuildNotice;
import cn.handyplus.guild.enter.GuildNoticeRead;
import cn.handyplus.guild.enter.GuildPlayer;
import cn.handyplus.guild.lib.BaseUtil;
import cn.handyplus.guild.lib.MessageUtil;
import cn.handyplus.guild.lib.RgbTextUtil;
import cn.handyplus.guild.lib.StrUtil;
import cn.handyplus.guild.service.GuildInfoService;
import cn.handyplus.guild.service.GuildNoticeReadService;
import cn.handyplus.guild.service.GuildNoticeService;
import cn.handyplus.guild.service.GuildPlayerService;
import cn.handyplus.guild.util.ConfigUtil;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class NoticeUtil {
    private NoticeUtil() {
    }

    public static void sendNotice(Player player, String notice) {
        NoticeUtil.sendNotice(player, notice, true);
    }

    private static boolean validateNotice(@NotNull Player player, @NotNull String content, @NotNull GuildPlayer guildPlayer) {
        int maxFont = ConfigUtil.NOTICE_CONFIG.getInt("send.maxFont");
        if (BaseUtil.stripColor(content).length() > maxFont) {
            String lengthFailureMsg = ConfigUtil.NOTICE_CONFIG.getString("lengthFailureMsg", "").replace("${length}", maxFont + "");
            MessageUtil.sendMessage(player, lengthFailureMsg);
            return true;
        }
        List<String> contentLines = StrUtil.strToStrList(content);
        int maxLine = ConfigUtil.NOTICE_CONFIG.getInt("send.maxLine");
        for (String line : contentLines) {
            if (BaseUtil.stripColor(line).length() <= maxLine) continue;
            String lineFailureMsg = ConfigUtil.NOTICE_CONFIG.getString("lengthFailureMsg", "").replace("${length}", maxLine + "");
            MessageUtil.sendMessage(player, lineFailureMsg);
            return true;
        }
        Optional<GuildInfo> optional = GuildInfoService.getInstance().findById(guildPlayer.getGuildInfoId());
        if (!optional.isPresent()) {
            return true;
        }
        GuildInfo guildInfo = optional.get();
        if (guildPlayer.getRoleId() > GuildRoleEnum.ONE.getRoleId()) {
            MessageUtil.sendMessage(player, BaseUtil.getLangMsg("noPermission"));
            return true;
        }
        int price = ConfigUtil.NOTICE_CONFIG.getInt("send.price");
        if (guildInfo.getMoney() < price) {
            MessageUtil.sendMessage(player, ConfigUtil.NOTICE_CONFIG.getString("send.noButton"));
            return true;
        }
        GuildInfoService.getInstance().subtractMoneyById(guildInfo.getId(), price);
        return false;
    }

    private static void broadcastNotice(@NotNull String title, @NotNull GuildPlayer guildPlayer) {
        List<UUID> memberUuids = GuildPlayerService.getInstance().findByGuildId(guildPlayer.getGuildInfoId()).stream().map(GuildPlayer::getPlayerUuid).filter(memberUuid -> NoticeUtil.isOtherPlayer(guildPlayer, memberUuid)).collect(Collectors.toList());
        String noticeMessage = ConfigUtil.NOTICE_CONFIG.getString("sendNoticeMsg", "").replace("${title}", title);
        String lookButton = ConfigUtil.NOTICE_CONFIG.getString("lookNotice");
        RgbTextUtil.init(noticeMessage).addExtra(RgbTextUtil.init(lookButton).addClickCommand("/plg me notice")).send(memberUuids);
    }

    private static boolean isOtherPlayer(GuildPlayer guildPlayer, UUID playerUuid) {
        if (!playerUuid.equals(guildPlayer.getPlayerUuid())) {
            return true;
        }
        return false;
    }

    public static String pPPppP(String value) {
        int length = value.length();
        int index = length - 1;
        char[] chars = new char[length];
        int xorKeyOne = 47;
        int remaining = index;
        int xorKeyTwo = 118;
        while (remaining >= 0) {
            int posOne = index--;
            chars[posOne] = (char)(value.charAt(posOne) ^ xorKeyTwo);
            if (index < 0) break;
            int posTwo = index--;
            chars[posTwo] = (char)(value.charAt(posTwo) ^ xorKeyOne);
            remaining = index;
        }
        return new String(chars);
    }

    public static void sendNotice(@NotNull Player player, @NotNull String notice, boolean check) {
        String[] parts = notice.split("#");
        if (parts.length != 2) {
            MessageUtil.sendMessage(player, ConfigUtil.NOTICE_CONFIG.getString("send.contentError"));
            return;
        }
        String title = parts[0];
        String content = parts[1];
        Optional<GuildPlayer> optional = GuildPlayerService.getInstance().findByPlayerUuid(player.getUniqueId());
        if (!optional.isPresent()) {
            return;
        }
        GuildPlayer guildPlayer = optional.get();
        if (check && NoticeUtil.validateNotice(player, content, guildPlayer)) {
            return;
        }
        GuildNotice guildNotice = new GuildNotice();
        guildNotice.setGuildInfoId(guildPlayer.getGuildInfoId());
        guildNotice.setGuildInfoName(guildPlayer.getGuildInfoName());
        guildNotice.setTitle(title);
        guildNotice.setContent(content);
        guildNotice.setSendPlayerName(player.getName());
        guildNotice.setSendPlayerUuid(player.getUniqueId());
        guildNotice.setSendPlayerRoleId(guildPlayer.getRoleId());
        guildNotice.setCreateTime(new Date());
        int noticeId = GuildNoticeService.getInstance().add(guildNotice);
        MessageUtil.sendMessage(player, ConfigUtil.NOTICE_CONFIG.getString("send.okButton"));
        GuildNoticeRead guildNoticeRead = new GuildNoticeRead();
        guildNoticeRead.setGuildInfoId(guildPlayer.getGuildInfoId());
        guildNoticeRead.setGuildNoticeId(noticeId);
        guildNoticeRead.setReadPlayerName(player.getName());
        guildNoticeRead.setReadPlayerUuid(player.getUniqueId());
        guildNoticeRead.setReadTime(new Date());
        GuildNoticeReadService.getInstance().add(guildNoticeRead);
        NoticeUtil.broadcastNotice(title, guildPlayer);
    }
}
