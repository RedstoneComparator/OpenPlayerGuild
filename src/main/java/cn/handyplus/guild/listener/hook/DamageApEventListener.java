package cn.handyplus.guild.listener.hook;

import cn.handyplus.guild.lib.HandyListener;
import cn.handyplus.guild.listener.pvp.PvpCheckUtil;
import me.monsterxz.monsterapi.attributeskill.api.event.DamageAPEvent;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

@HandyListener
public class DamageApEventListener
implements Listener {
    @EventHandler(priority=EventPriority.LOW)
    public void onEvent(DamageAPEvent.Attack event) {
        LivingEntity attacker = event.getAttacker();
        if (!(attacker instanceof Player)) {
            return;
        }
        attacker = (Player)attacker;
        LivingEntity target = event.getEntity();
        if (!(target instanceof Player)) {
            return;
        }
        target = (Player)target;
        event.setCancelled(PvpCheckUtil.check((Player)attacker, (Player)target));
    }
}
