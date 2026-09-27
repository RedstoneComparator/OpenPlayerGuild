package cn.handyplus.guild.listener.pvp;

import cn.handyplus.guild.lib.HandyListener;
import cn.handyplus.guild.listener.pvp.PvpCheckUtil;
import java.util.Iterator;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PotionSplashEvent;

@HandyListener
public class PotionSplashEventListener
implements Listener {
    @EventHandler(ignoreCancelled=true)
    public void onPotionSplash(PotionSplashEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        if (event.getPotion().getShooter() instanceof Player) {
            Iterator affectedIterator = event.getAffectedEntities().iterator();
            while (affectedIterator.hasNext()) {
                if (!((LivingEntity)affectedIterator.next() instanceof Player)) continue;
                Player shooter = (Player)event.getPotion().getShooter();
                Player targetPlayer = (Player)event.getEntity();
                event.setCancelled(PvpCheckUtil.check(shooter, targetPlayer));
                return;
            }
        }
    }
}
