package cn.handyplus.guild.listener.guild;

import cn.handyplus.guild.event.GuildBuffEvent;
import cn.handyplus.guild.lib.HandyListener;
import cn.handyplus.guild.util.BuffManageUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@HandyListener
public class GuildBuffEventListener
implements Listener {
    @EventHandler
    public void onEvent(GuildBuffEvent event) {
        BuffManageUtil.setBuff(event.getPlayer(), event.getBuffList());
    }
}
