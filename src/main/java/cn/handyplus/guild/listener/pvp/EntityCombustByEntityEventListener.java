package cn.handyplus.guild.listener.pvp;

import cn.handyplus.guild.lib.HandyListener;
import cn.handyplus.guild.listener.pvp.PvpCheckUtil;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustByEntityEvent;

@HandyListener
public class EntityCombustByEntityEventListener
implements Listener {
    @EventHandler(ignoreCancelled=true)
    public void onEntityComBustByEntity(EntityCombustByEntityEvent event) {
        Arrow arrow;
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        if (event.getCombuster() instanceof Arrow && (arrow = (Arrow)event.getCombuster()).getShooter() instanceof Player) {
            Player shooter = (Player)arrow.getShooter();
            Player player = (Player)event.getEntity();
            event.setCancelled(PvpCheckUtil.check(shooter, player));
        }
    }
}
