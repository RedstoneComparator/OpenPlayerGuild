package cn.handyplus.guild.command.player;

import cn.handyplus.guild.constants.GuildConstants;
import cn.handyplus.guild.enter.GuildInfo;
import cn.handyplus.guild.enter.GuildPlayer;
import cn.handyplus.guild.lib.AssertUtil;
import cn.handyplus.guild.lib.BaseConstants;
import cn.handyplus.guild.lib.BaseUtil;
import cn.handyplus.guild.lib.HandySchedulerUtil;
import cn.handyplus.guild.lib.IHandyCommandEvent;
import cn.handyplus.guild.lib.JsonUtil;
import cn.handyplus.guild.lib.MessageUtil;
import cn.handyplus.guild.lib.StrUtil;
import cn.handyplus.guild.param.GuildSpawn;
import cn.handyplus.guild.runnable.SpawnTeleportRunnable;
import cn.handyplus.guild.service.GuildInfoService;
import cn.handyplus.guild.service.GuildPlayerService;
import java.util.Optional;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCommand
implements IHandyCommandEvent {
    @Override
    public String command() {
        return "spawn";
    }

    @Override
    public boolean isAsync() {
        return true;
    }

    @Override
    public void onCommand(CommandSender sender, Command command, String label, String[] args) {
        long timeValue;
        Player player = AssertUtil.notPlayer(sender, BaseUtil.getLangMsg("noPlayerFailureMsg"));
        Optional<GuildPlayer> guildPlayerOptional = GuildPlayerService.getInstance().findByPlayerUuid(player.getUniqueId());
        if (!guildPlayerOptional.isPresent()) {
            MessageUtil.sendMessage(sender, BaseUtil.getLangMsg("noGuildMsg"));
            return;
        }
        GuildPlayer guildPlayer = guildPlayerOptional.get();
        Optional<GuildInfo> guildInfoOptional = GuildInfoService.getInstance().findById(guildPlayer.getGuildInfoId());
        if (!guildInfoOptional.isPresent()) {
            return;
        }
        GuildInfo guildInfo = guildInfoOptional.get();
        String spawnLocation = guildInfo.getSpawnLocation();
        if (StrUtil.isEmpty(spawnLocation)) {
            MessageUtil.sendMessage(sender, BaseUtil.getLangMsg("noGuildSpawnLocationMsg"));
            return;
        }
        GuildSpawn guildSpawn = JsonUtil.toBean(spawnLocation, GuildSpawn.class);
        if (StrUtil.isNotEmpty(BaseConstants.CONFIG.getString("serverName")) && StrUtil.isEmpty(guildSpawn.getServerName())) {
            MessageUtil.sendMessage(player, BaseUtil.getLangMsg("noGuildSpawnLocationMsg"));
            return;
        }
        long waitTime = BaseConstants.CONFIG.getLong("waitTime");
        if (GuildConstants.SPAWN_WAIT_TIME.containsKey(player.getUniqueId()) && (timeValue = (System.currentTimeMillis() - GuildConstants.SPAWN_WAIT_TIME.get(player.getUniqueId())) / 1000L) < waitTime) {
            String message = BaseUtil.getLangMsg("waitTime").replace("${waitTime}", waitTime - timeValue + "");
            MessageUtil.sendMessage(player, message);
            return;
        }
        timeValue = BaseConstants.CONFIG.getLong("delayTime");
        if (timeValue > 0L) {
            String delayMessage = BaseUtil.getLangMsg("delayTime").replace("${delayTime}", timeValue + "");
            MessageUtil.sendMessage(player, delayMessage);
        }
        GuildConstants.SPAWN_TP_WAIT_TIME.put(player.getUniqueId(), false);
        SpawnTeleportRunnable runnable = new SpawnTeleportRunnable(player, guildSpawn, guildInfo);
        HandySchedulerUtil.runTaskLater(runnable, timeValue * 20L);
    }

    @Override
    public String permission() {
        return "playerGuild.spawn";
    }
}
