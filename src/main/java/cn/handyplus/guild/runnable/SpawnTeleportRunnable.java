package cn.handyplus.guild.runnable;

import cn.handyplus.guild.constants.GuildConstants;
import cn.handyplus.guild.enter.GuildChannel;
import cn.handyplus.guild.enter.GuildInfo;
import cn.handyplus.guild.lib.BaseConstants;
import cn.handyplus.guild.lib.BcUtil;
import cn.handyplus.guild.lib.HandyRunnable;
import cn.handyplus.guild.lib.JsonUtil;
import cn.handyplus.guild.lib.StrUtil;
import cn.handyplus.guild.param.GuildSpawn;
import cn.handyplus.guild.service.GuildChannelService;
import cn.handyplus.guild.util.GuildUtil;
import java.util.Date;
import lombok.Generated;
import org.bukkit.entity.Player;

public class SpawnTeleportRunnable
extends HandyRunnable {
    private final Player M;
    private final GuildInfo guildInfo;
    private final GuildSpawn iiiIii;

    @Override
    public void run() {
        if (GuildConstants.SPAWN_TP_WAIT_TIME.get(this.M.getUniqueId()) != null && GuildConstants.SPAWN_TP_WAIT_TIME.get(this.M.getUniqueId()).booleanValue()) {
            this.cancel();
            return;
        }
        GuildConstants.SPAWN_TP_WAIT_TIME.remove(this.M.getUniqueId());
        SpawnTeleportRunnable spawnTeleportRunnable = this;
        SpawnTeleportRunnable spawnTeleportRunnable2 = this;
        spawnTeleportRunnable2.doTeleport(spawnTeleportRunnable.M, spawnTeleportRunnable.iiiIii, spawnTeleportRunnable2.guildInfo.getGuildName());
    }

    private void doTeleport(Player player, GuildSpawn guildSpawn, String guildName) {
        Object serverOrChannel = BaseConstants.CONFIG.getString("serverName", "server");
        if (StrUtil.isEmpty(guildSpawn.getServerName()) || ((String)serverOrChannel).equalsIgnoreCase(guildSpawn.getServerName())) {
            GuildUtil.teleport(player, guildSpawn, guildName);
            return;
        }
        serverOrChannel = new GuildChannel();
        GuildSpawn targetSpawn = guildSpawn;
        Object serverOrChannel2 = serverOrChannel;
        Object serverOrChannel3 = serverOrChannel;
        ((GuildChannel)serverOrChannel3).setPlayerName(player.getName());
        ((GuildChannel)serverOrChannel3).setPlayerUuid(player.getUniqueId());
        ((GuildChannel)serverOrChannel2).setGuildName(guildName);
        Object serverOrChannel4 = serverOrChannel;
        ((GuildChannel)serverOrChannel2).setTpTime(new Date());
        ((GuildChannel)serverOrChannel2).setServerName(guildSpawn.getServerName());
        ((GuildChannel)serverOrChannel).setSpawnLocation(JsonUtil.toJson(targetSpawn));
        GuildChannelService.getInstance().add((GuildChannel)serverOrChannel);
        BcUtil.tpConnect(player, targetSpawn.getServerName());
    }

    @Generated
    public SpawnTeleportRunnable(Player player, GuildSpawn guildSpawn, GuildInfo guildInfo) {
        SpawnTeleportRunnable spawnTeleportRunnable = this;
        this.M = player;
        this.iiiIii = guildSpawn;
        this.guildInfo = guildInfo;
    }
}
