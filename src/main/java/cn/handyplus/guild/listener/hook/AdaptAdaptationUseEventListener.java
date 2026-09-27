package cn.handyplus.guild.listener.hook;

import cn.handyplus.guild.api.PlayerGuildApi;
import cn.handyplus.guild.lib.HandyListener;
import cn.handyplus.guild.util.ConfigUtil;
import com.volmit.adapt.content.event.AdaptAdaptationUseEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@HandyListener
public class AdaptAdaptationUseEventListener
implements Listener {
    @EventHandler
    public void onEvent(AdaptAdaptationUseEvent event) {
        if (!ConfigUtil.KIT_PVP_CONFIG.getBoolean("adapt", false)) {
            return;
        }
        if (PlayerGuildApi.isPvp(event.getPlayer().getPlayer())) {
            event.setCancelled(true);
        }
    }
}
