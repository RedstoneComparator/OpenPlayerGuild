package cn.handyplus.guild.listener;

import cn.handyplus.guild.api.PlayerGuildApi;
import cn.handyplus.guild.lib.EntitySchedulerUtil;
import cn.handyplus.guild.lib.HandyListener;
import cn.handyplus.guild.lib.HandySchedulerUtil;
import cn.handyplus.guild.util.BuffManageUtil;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityResurrectEvent;

@HandyListener
public class PlayerTotemBuffEventListener
implements Listener {
    private static void J(Player player) {
        Player targetPlayer = player;
        EntitySchedulerUtil.runSafeOnEntityScheduler((LivingEntity)targetPlayer, () -> BuffManageUtil.callBuffEvent(targetPlayer));
    }

    @EventHandler
    public void onEntityResurrect(EntityResurrectEvent event) {
        if (event.isCancelled()) {
            return;
        }
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        Player player = (Player)event.getEntity();
        if (PlayerGuildApi.isPvp(player)) {
            return;
        }
        HandySchedulerUtil.runTaskLater(() -> PlayerTotemBuffEventListener.J(player), 20L);
    }
}
