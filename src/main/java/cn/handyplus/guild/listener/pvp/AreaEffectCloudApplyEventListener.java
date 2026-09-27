package cn.handyplus.guild.listener.pvp;

import cn.handyplus.guild.lib.HandyListener;
import cn.handyplus.guild.lib.VersionCheckEnum;
import cn.handyplus.guild.listener.pvp.PvpCheckUtil;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.AreaEffectCloudApplyEvent;
import org.bukkit.projectiles.ProjectileSource;

@HandyListener(version=VersionCheckEnum.V_1_13)
public class AreaEffectCloudApplyEventListener
implements Listener {
    @EventHandler(ignoreCancelled=true)
    public void onCloudEffects(AreaEffectCloudApplyEvent event) {
        ProjectileSource projectileSource = event.getEntity().getSource();
        if (!(projectileSource instanceof Player)) {
            return;
        }
        Player shooter = (Player)projectileSource;
        for (LivingEntity livingEntity : event.getAffectedEntities()) {
            if (!(livingEntity instanceof Player)) {
                continue;
            }
            Player affectedPlayer = (Player)livingEntity;
            event.setCancelled(PvpCheckUtil.check(shooter, affectedPlayer));
            return;
        }
    }
}
