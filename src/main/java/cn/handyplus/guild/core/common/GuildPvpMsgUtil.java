package cn.handyplus.guild.core.common;

import cn.handyplus.guild.PlayerGuild;
import cn.handyplus.guild.constants.GuildActivityType;
import cn.handyplus.guild.enter.GuildInfo;
import cn.handyplus.guild.enter.GuildPlayer;
import cn.handyplus.guild.lib.BaseUtil;
import cn.handyplus.guild.lib.BcUtil;
import cn.handyplus.guild.lib.CollUtil;
import cn.handyplus.guild.lib.MapUtil;
import cn.handyplus.guild.lib.MessageUtil;
import cn.handyplus.guild.lib.RgbTextUtil;
import cn.handyplus.guild.lib.StrUtil;
import cn.handyplus.guild.service.GuildInfoService;
import cn.handyplus.guild.util.ConfigUtil;
import com.google.common.collect.Iterables;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GuildPvpMsgUtil {
    private final static String RANGE_NOT = "not";
    private final static String RANGE_ALL = "all";
    private final static String RANGE_OP = "op";

    public static void sendPrepareTitle(@NotNull Map<Integer, List<UUID>> playerUuidMap, int prepareSeconds) {
        for (List<UUID> uuids : playerUuidMap.values()) {
            for (UUID uuid : uuids) {
                Optional<Player> optional = BaseUtil.getOnlinePlayer(uuid);
                if (!optional.isPresent()) {
                    continue;
                }
                Player player = optional.get();
                String title = ConfigUtil.ACTIVITY_CONFIG.getString("matePvpTitleMsg");
                String subTitle;
                if (prepareSeconds > 0) {
                    subTitle = ConfigUtil.ACTIVITY_CONFIG.getString("matePvpSubTitleMsg", "${time}").replace("${time}", String.valueOf(prepareSeconds));
                } else {
                    subTitle = ConfigUtil.ACTIVITY_CONFIG.getString("matePvpSubTitleStartMsg");
                }
                MessageUtil.sendTitle(player, title, subTitle);
            }
        }
    }

    private static void broadcastMessage(@Nullable Player player, @Nullable String message) {
        BcUtil.BcMessageParam bcMessageParam;
        if (player == null) {
            player = (Player)Iterables.getFirst((Iterable)Bukkit.getOnlinePlayers(), null);
        }
        if (player == null || StrUtil.isEmpty(message)) {
            return;
        }
        BcUtil.BcMessageParam bcMessageParam2 = bcMessageParam = new BcUtil.BcMessageParam();
        bcMessageParam.setPluginName(PlayerGuild.INSTANCE.getName());
        bcMessageParam2.setType("guild_match");
        bcMessageParam2.setMessage(message);
        BcUtil.sendParamForward(player, bcMessageParam2);
    }

    public static void sendPrepareMessage(@NotNull Map<Integer, List<GuildPlayer>> guildPlayerMap, @NotNull Map<Integer, List<UUID>> fightingUuidMap, @NotNull String message) {
        String acceptText = ConfigUtil.ACTIVITY_CONFIG.getString("matePvpPrepareAccept");
        String refuseText = ConfigUtil.ACTIVITY_CONFIG.getString("matePvpPrepareRefuse");
        for (List<GuildPlayer> guildPlayers : guildPlayerMap.values()) {
            for (GuildPlayer guildPlayer : guildPlayers) {
                Optional<Player> optional = BaseUtil.getOnlinePlayer(guildPlayer.getPlayerUuid());
                if (!optional.isPresent()) {
                    continue;
                }
                Player player = optional.get();
                List<UUID> uuids = fightingUuidMap.get(guildPlayer.getGuildInfoId());
                if (uuids != null && uuids.contains(player.getUniqueId())) {
                    continue;
                }
                MessageUtil.sendMessage(player, message);
                RgbTextUtil.init(acceptText).addClickCommand("/plg acceptPvp").addExtra(RgbTextUtil.init(refuseText).addClickCommand("/plg denyPvp")).send((CommandSender)player);
            }
        }
    }

    public static void sendMatchFinishAll(boolean hasWinner, String winGuildName, String failGuildName) {
        String message = ConfigUtil.ACTIVITY_CONFIG.getString(hasWinner ? "matePvpFinishAllMsg" : "matePvpUnlimitedFinishAllMsg", "");
        message = message.replace("${win}", winGuildName).replace("${fail}", failGuildName);
        GuildPvpMsgUtil.broadcastMessage(null, message);
    }

    private GuildPvpMsgUtil() {
    }

    public static void sendStartNotice(@NotNull List<Integer> guildIds) {
        String range = ConfigUtil.ACTIVITY_CONFIG.getString("notice.range", RANGE_NOT);
        if (RANGE_NOT.equalsIgnoreCase(range)) {
            return;
        }
        String message = ConfigUtil.ACTIVITY_CONFIG.getString("notice.msg", "");
        if (StrUtil.isEmpty(message)) {
            return;
        }
        List<GuildInfo> guildInfos = GuildInfoService.getInstance().findByIds(guildIds);
        if (CollUtil.isEmpty(guildInfos) || guildInfos.size() < 2) {
            return;
        }
        GuildInfo guildInfo = guildInfos.get(0);
        GuildInfo guildInfo2 = guildInfos.get(1);
        String notice = message.replace("${one}", guildInfo.getGuildName()).replace("${two}", guildInfo2.getGuildName());
        if (RANGE_OP.equalsIgnoreCase(range)) {
            Bukkit.getOperators().forEach(operator -> MessageUtil.sendMessage(operator.getUniqueId(), notice));
            return;
        }
        if (RANGE_ALL.equalsIgnoreCase(range)) {
            GuildPvpMsgUtil.broadcastMessage(null, notice);
        }
    }

    public static void sendMatchNotice(@NotNull Player player, @NotNull Integer guildId, @NotNull GuildActivityType activityType) {
        String range = ConfigUtil.ACTIVITY_CONFIG.getString("mateNotice.range", RANGE_NOT);
        if (RANGE_NOT.equalsIgnoreCase(range)) {
            return;
        }
        Optional<GuildInfo> optional = GuildInfoService.getInstance().findById(guildId);
        if (!optional.isPresent()) {
            return;
        }
        GuildInfo guildInfo = optional.get();
        Map<String, String> placeholders = MapUtil.of("${guildName}", guildInfo.getGuildName(), "${type}", GuildActivityType.getDesc(activityType));
        String message = BaseUtil.getLangMsg(ConfigUtil.ACTIVITY_CONFIG, "mateNotice.msg", placeholders);
        if (RANGE_OP.equalsIgnoreCase(range)) {
            Bukkit.getOperators().forEach(operator -> GuildPvpMsgUtil.sendMessageToPlayer(message, operator));
            return;
        }
        if (RANGE_ALL.equalsIgnoreCase(range)) {
            RgbTextUtil.init(message).addClickCommand("/plg me activity").sendAll();
            GuildPvpMsgUtil.broadcastMessage(player, message);
        }
    }

    public static void sendFinishTitle(@NotNull Player player, @NotNull String winText, @NotNull String failText) {
        String title = ConfigUtil.ACTIVITY_CONFIG.getString("matePvpTitleMsg");
        String subTitle = ConfigUtil.ACTIVITY_CONFIG.getString("matePvpFinishSubTitleMsg", "${win}");
        subTitle = subTitle.replace("${win}", winText).replace("${fail}", failText);
        MessageUtil.sendTitle(player, title, subTitle);
    }

    private static void sendMessageToPlayer(String message, OfflinePlayer player) {
        MessageUtil.sendMessage(player.getUniqueId(), message);
    }
}
