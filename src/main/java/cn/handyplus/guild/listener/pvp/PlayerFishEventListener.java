package cn.handyplus.guild.listener.pvp;

import cn.handyplus.guild.lib.HandyListener;
import cn.handyplus.guild.listener.pvp.PvpCheckUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

@HandyListener
public class PlayerFishEventListener
implements Listener {
    @EventHandler(ignoreCancelled=true)
    public void onPlayerFishing(PlayerFishEvent event) {
        if (!(event.getCaught() instanceof Player)) {
            return;
        }
        PlayerFishEvent playerFishEvent = event;
        Player fisher = playerFishEvent.getPlayer();
        Player caughtPlayer = (Player)playerFishEvent.getCaught();
        event.setCancelled(PvpCheckUtil.check(fisher, caughtPlayer));
    }
}
