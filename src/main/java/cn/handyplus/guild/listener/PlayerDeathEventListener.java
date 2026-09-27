package cn.handyplus.guild.listener;

import cn.handyplus.guild.core.match.MatchPvpManager;
import cn.handyplus.guild.core.season.SeasonPvpManager;
import cn.handyplus.guild.core.stronghold.StrongholdPvpManager;
import cn.handyplus.guild.lib.HandyListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

@HandyListener
public class PlayerDeathEventListener
implements Listener {
    private boolean handleStrongholdDeath(Player player) {
        return StrongholdPvpManager.handleDeath(player);
    }

    private void handleSeasonDeath(Player player) {
        SeasonPvpManager.handleDeath(player);
    }

    @EventHandler
    public void onEvent(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (this.handleStrongholdDeath(player)) {
            return;
        }
        PlayerDeathEventListener listener = this;
        listener.handleMatchDeath(player);
        listener.handleSeasonDeath(player);
    }

    private void handleMatchDeath(Player player) {
        MatchPvpManager.handleDeath(player);
    }
}
