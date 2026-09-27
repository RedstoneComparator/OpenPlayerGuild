package cn.handyplus.guild.listener;

import cn.handyplus.guild.constants.GuildConstants;
import cn.handyplus.guild.lib.BaseUtil;
import cn.handyplus.guild.lib.HandyListener;
import cn.handyplus.guild.lib.MessageUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

@HandyListener
public class PlayerMoveEventListener
implements Listener {
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.isCancelled()) {
            return;
        }
        Player player = event.getPlayer();
        if (GuildConstants.SPAWN_TP_WAIT_TIME.get(player.getUniqueId()) == null || GuildConstants.SPAWN_TP_WAIT_TIME.get(player.getUniqueId()).booleanValue()) {
            return;
        }
        Location to = event.getTo();
        Location from = event.getFrom();
        if (to == null) {
            return;
        }
        if (to.getWorld() != from.getWorld() || !to.getBlock().equals(from.getBlock())) {
            GuildConstants.SPAWN_TP_WAIT_TIME.put(player.getUniqueId(), true);
            MessageUtil.sendMessage(player, BaseUtil.getLangMsg("spawnMove"));
        }
    }
}
