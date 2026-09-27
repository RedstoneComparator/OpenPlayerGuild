package cn.handyplus.guild.listener.pvp;

import cn.handyplus.guild.lib.HandyListener;
import cn.handyplus.guild.listener.pvp.PvpCheckUtil;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

@HandyListener
public class EntityDamageByEntityEventListener
implements Listener {
    @EventHandler(ignoreCancelled=true)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        ThrownPotion thrownPotion;
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        Player victim = (Player)event.getEntity();
        Player damager = null;
        if (event.getDamager() instanceof Player) {
            damager = (Player)event.getDamager();
        }
        Projectile projectile = null;
        if (event.getDamager() instanceof Projectile && (projectile = (Projectile)event.getDamager()).getShooter() instanceof Player) {
            damager = (Player)projectile.getShooter();
        }
        if (event.getDamager() instanceof ThrownPotion && (thrownPotion = (ThrownPotion)event.getDamager()).getShooter() instanceof Player) {
            damager = (Player)thrownPotion.getShooter();
        }
        if (damager == null) {
            return;
        }
        boolean shouldCancel = PvpCheckUtil.check(damager, victim);
        if (shouldCancel && projectile instanceof Arrow) {
            projectile.remove();
        }
        event.setCancelled(shouldCancel);
    }
}
