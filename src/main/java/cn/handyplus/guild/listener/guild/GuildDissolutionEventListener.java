package cn.handyplus.guild.listener.guild;

import cn.handyplus.guild.enter.GuildInfo;
import cn.handyplus.guild.enter.GuildPlayer;
import cn.handyplus.guild.event.GuildDissolutionEvent;
import cn.handyplus.guild.hook.GuildWorldUtil;
import cn.handyplus.guild.hook.LuckPermsUtil;
import cn.handyplus.guild.hook.PlayerChatUtil;
import cn.handyplus.guild.hook.PlayerTitleUtil;
import cn.handyplus.guild.hook.ResidenceUtil;
import cn.handyplus.guild.lib.BaseUtil;
import cn.handyplus.guild.lib.HandyListener;
import cn.handyplus.guild.lib.MessageUtil;
import cn.handyplus.guild.listener.guild.PlayerLeaveGuildEventListener;
import cn.handyplus.guild.service.GuildInfoService;
import cn.handyplus.guild.service.GuildPlayerService;
import cn.handyplus.guild.util.BuffManageUtil;
import cn.handyplus.guild.util.ConfigUtil;
import cn.handyplus.guild.util.GuildCacheUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@HandyListener
public class GuildDissolutionEventListener
implements Listener {
    @EventHandler
    public void onGuildDissolutionEvent(GuildDissolutionEvent event) {
        Integer guildId = event.getGuildId();
        List<GuildPlayer> guildPlayers = GuildPlayerService.getInstance().findByGuildId(guildId);
        ArrayList<UUID> playerUuids = new ArrayList<UUID>();
        for (GuildPlayer guildPlayer : guildPlayers) {
            playerUuids.add(guildPlayer.getPlayerUuid());
            Optional<Player> onlineByName = BaseUtil.getOnlinePlayer(guildPlayer.getPlayerName());
            onlineByName.ifPresent(BuffManageUtil::removeBuff);
            MessageUtil.sendMessage(guildPlayer.getPlayerName(), ConfigUtil.CONFIRM_CONFIG.getString("dissolutionMsg"));
            Optional<Player> onlineByUuid = BaseUtil.getOnlinePlayer(guildPlayer.getPlayerUuid());
            if (onlineByUuid.isPresent()) {
                Player player = onlineByUuid.get();
                PlayerChatUtil.unRegPlayerChannel(player, guildId);
                PlayerChatUtil.setPlayerChannelToDefault(player);
            }
            PlayerLeaveGuildEventListener.savePlayerData(guildPlayer);
        }
        Optional<GuildInfo> guildInfoOptional = GuildInfoService.getInstance().findById(guildId);
        if (!guildInfoOptional.isPresent()) {
            return;
        }
        GuildInfo guildInfo = guildInfoOptional.get();
        PlayerTitleUtil.removeTitle(guildInfo.getTitleId());
        ResidenceUtil.remove(guildInfo);
        GuildWorldUtil.removeWorld(guildInfo.getWorldName(), playerUuids);
        PlayerChatUtil.unRegChannel(guildId);
        GuildInfoService.getInstance().delByGuildId(guildId);
        LuckPermsUtil.deleteGroup(guildInfo.getLuckPermsGroup());
        for (GuildPlayer guildPlayer : guildPlayers) {
            LuckPermsUtil.removeUserFromGroup(guildPlayer.getPlayerUuid(), guildInfo.getLuckPermsGroup());
        }
        GuildCacheUtil.refresh(playerUuids);
    }
}
