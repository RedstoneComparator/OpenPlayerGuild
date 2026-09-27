package cn.handyplus.guild.listener;

import cn.handyplus.guild.constants.GuildConstants;
import cn.handyplus.guild.core.match.MatchPvpManager;
import cn.handyplus.guild.core.season.SeasonPvpManager;
import cn.handyplus.guild.core.stronghold.StrongholdPvpManager;
import cn.handyplus.guild.lib.HandyListener;
import cn.handyplus.guild.lib.HandySchedulerUtil;
import cn.handyplus.guild.lib.SignService;
import cn.handyplus.guild.service.GuildPlayerService;
import cn.handyplus.guild.util.BuffManageUtil;
import cn.handyplus.guild.util.GuildCacheUtil;
import java.util.Date;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@HandyListener
public class PlayerQuitEventListener
implements Listener {
    private void J(Player player) {

        GuildConstants.SPAWN_WAIT_TIME.remove(player.getUniqueId());
        GuildConstants.SPAWN_TP_WAIT_TIME.remove(player.getUniqueId());
        GuildConstants.CREATE_GUILD_INFO_MAP.remove(player.getUniqueId());
        GuildConstants.INVITATION_PLAYER_MAP.remove(player.getName());
        GuildConstants.PLAYER_API_PVP_MAP.remove(player.getUniqueId());
        Player targetPlayer = player;
        StrongholdPvpManager.handleQuit(targetPlayer);
        MatchPvpManager.handleDeath(targetPlayer);
        SeasonPvpManager.handleDeath(targetPlayer);
        BuffManageUtil.removeBuff(targetPlayer);
        HandySchedulerUtil.runTaskAsynchronously(() -> GuildPlayerService.getInstance().updateLastQuitTimeByPlayerUuid(player.getUniqueId(), new Date()));
        GuildCacheUtil.remove(player.getUniqueId());
    }

    @EventHandler
    public void onKick(PlayerKickEvent event) {
        this.J(event.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        this.J(event.getPlayer());
    }
}
