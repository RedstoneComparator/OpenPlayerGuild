package cn.handyplus.guild.listener.guild;

import cn.handyplus.guild.event.GuildSetSpawnEvent;
import cn.handyplus.guild.lib.BaseConstants;
import cn.handyplus.guild.lib.BaseUtil;
import cn.handyplus.guild.lib.HandyListener;
import cn.handyplus.guild.lib.JsonUtil;
import cn.handyplus.guild.lib.MessageUtil;
import cn.handyplus.guild.param.GuildSpawn;
import cn.handyplus.guild.service.GuildInfoService;
import java.util.Objects;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@HandyListener
public class GuildSetSpawnEventListener
implements Listener {
    @EventHandler
    public void onEvent(GuildSetSpawnEvent event) {
        if (event.isCancelled()) {
            return;
        }
        Location location = event.getLocation();
        String worldName = Objects.requireNonNull(location.getWorld()).getName();
        String serverName = BaseConstants.CONFIG.getString("serverName", "server");
        GuildSpawn guildSpawn = new GuildSpawn();
        guildSpawn.setServerName(serverName);
        guildSpawn.setX(location.getX());
        guildSpawn.setY(location.getY());
        guildSpawn.setZ(location.getZ());
        guildSpawn.setYaw(location.getYaw());
        guildSpawn.setPitch(location.getPitch());
        guildSpawn.setWorld(worldName);
        String json = JsonUtil.toJson(guildSpawn);
        int updatedRows = GuildInfoService.getInstance().updateSpawnLocation(event.getGuildInfoId(), json);
        MessageUtil.sendMessage(event.getPlayer(), BaseUtil.getLangMsg(updatedRows > 0 ? "succeedMsg" : "failureMsg"));
    }
}
