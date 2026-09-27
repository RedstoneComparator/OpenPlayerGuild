package cn.handyplus.guild.listener;

import cn.handyplus.guild.api.PlayerGuildApi;
import cn.handyplus.guild.lib.HandyListener;
import cn.handyplus.guild.lib.MessageUtil;
import cn.handyplus.guild.util.ConfigUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

@HandyListener
public class PlayerDropItemEventListener
implements Listener {
    @EventHandler(priority=EventPriority.HIGHEST)
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if (event.isCancelled()) {
            return;
        }
        Player player = event.getPlayer();
        if (!PlayerGuildApi.isPvp(player)) {
            return;
        }
        event.setCancelled(true);
        MessageUtil.sendMessage(player, ConfigUtil.ACTIVITY_CONFIG.getString("playerDropMsg"));
    }
}
