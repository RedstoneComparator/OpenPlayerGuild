package cn.handyplus.guild.hook;

import cn.handyplus.chat.api.PlayerChatApi;
import cn.handyplus.guild.PlayerGuild;
import cn.handyplus.guild.enter.GuildInfo;
import cn.handyplus.guild.enter.GuildPlayer;
import cn.handyplus.guild.lib.BaseUtil;
import cn.handyplus.guild.lib.CollUtil;
import cn.handyplus.guild.lib.MessageUtil;
import cn.handyplus.guild.service.GuildInfoService;
import cn.handyplus.guild.service.GuildPlayerService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class PlayerChatUtil {
    private PlayerChatUtil() {
    }

    public static void regChannel() {
        if (!PlayerGuild.USE_CHAT) {
            return;
        }
        List<GuildInfo> guilds = GuildInfoService.getInstance().findAll();
        if (CollUtil.isEmpty(guilds)) {
            return;
        }
        List guildIds = guilds.stream().map(guildInfo -> String.valueOf(guildInfo.getId())).collect(Collectors.toList());
        PlayerChatApi.getInstance().regChannel((Plugin)PlayerGuild.INSTANCE, guildIds);
    }

    public static void regChannel(Integer guildId) {
        if (!PlayerGuild.USE_CHAT) {
            return;
        }
        PlayerChatApi.getInstance().regChannel((Plugin)PlayerGuild.INSTANCE, guildId.toString());
    }

    public static void unRegChannel() {
        if (!PlayerGuild.USE_CHAT) {
            return;
        }
        PlayerChatApi.getInstance().unRegChannel((Plugin)PlayerGuild.INSTANCE);
    }

    public static void unRegChannel(Integer guildId) {
        if (!PlayerGuild.USE_CHAT) {
            return;
        }
        PlayerChatApi.getInstance().unRegChannel((Plugin)PlayerGuild.INSTANCE, guildId.toString());
    }

    public static void regPlayerChannel(Player player, Integer guildId) {
        if (!PlayerGuild.USE_CHAT || player == null) {
            return;
        }
        PlayerChatApi.getInstance().regPlayerChannel((Plugin)PlayerGuild.INSTANCE, guildId.toString(), player);
    }

    public static void unRegPlayerChannel(Player player, Integer guildId) {
        if (!PlayerGuild.USE_CHAT || player == null) {
            return;
        }
        PlayerChatApi.getInstance().unRegPlayerChannel((Plugin)PlayerGuild.INSTANCE, guildId.toString(), player);
    }

    public static void setPlayerChannel(Player player) {
        if (!PlayerGuild.USE_CHAT) {
            return;
        }
        Optional<GuildPlayer> guildPlayerOptional = GuildPlayerService.getInstance().findByPlayerUuid(player.getUniqueId());
        if (!guildPlayerOptional.isPresent()) {
            MessageUtil.sendMessage(player, BaseUtil.getLangMsg("noGuildMsg"));
            return;
        }
        PlayerChatApi.getInstance().setPlayerChannel((Plugin)PlayerGuild.INSTANCE, guildPlayerOptional.get().getGuildInfoId().toString(), player);
        MessageUtil.sendMessage(player, BaseUtil.getLangMsg("joinChannel"));
    }

    public static void setPlayerChannelToDefault(Player player) {
        if (!PlayerGuild.USE_CHAT || player == null) {
            return;
        }
        PlayerChatApi.getInstance().setPlayerChannelToDefault(player);
    }
}
