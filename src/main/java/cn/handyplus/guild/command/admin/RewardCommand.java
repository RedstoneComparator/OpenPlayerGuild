package cn.handyplus.guild.command.admin;

import cn.handyplus.guild.enter.GuildPlayer;
import cn.handyplus.guild.enter.GuildTask;
import cn.handyplus.guild.lib.BaseUtil;
import cn.handyplus.guild.lib.CollUtil;
import cn.handyplus.guild.lib.HandySchedulerUtil;
import cn.handyplus.guild.lib.HandyTab;
import cn.handyplus.guild.lib.IHandyCommandEvent;
import cn.handyplus.guild.lib.MapUtil;
import cn.handyplus.guild.lib.MessageUtil;
import cn.handyplus.guild.service.GuildInfoService;
import cn.handyplus.guild.service.GuildPlayerService;
import cn.handyplus.guild.service.GuildTaskService;
import cn.handyplus.guild.util.ConfigUtil;
import cn.handyplus.guild.util.GuildUtil;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class RewardCommand
implements IHandyCommandEvent {
    @Override
    public void tab(HandyTab handyTab) {
        handyTab.next("task");
    }

    public static synchronized int rewardTask(List<GuildTask> tasks) {
        if (CollUtil.isEmpty(tasks)) {
            return 0;
        }
        List<Integer> taskIds = tasks.stream().map(GuildTask::getId).collect(Collectors.toList());
        List<GuildTask> rewardTasks = GuildTaskService.getInstance().findRewardTaskByIds(taskIds);
        if (CollUtil.isEmpty(rewardTasks)) {
            return 0;
        }
        List<Integer> guildIds = rewardTasks.stream().map(GuildTask::getGuildInfoId).distinct().collect(Collectors.toList());
        Map<Integer, List<GuildPlayer>> playersByGuildId = GuildPlayerService.getInstance().findMapByGuildInfoIds(guildIds);
        String rewardMsg = ConfigUtil.TASK_CONFIG.getString("rewardMsg", "");
        ArrayList<Integer> rewardedTaskIds = new ArrayList<Integer>();
        HashSet<Integer> guildIdsToCache = new HashSet<Integer>();
        for (GuildTask guildTask : rewardTasks) {
            RewardCommand.sendTaskReward(guildTask, playersByGuildId.getOrDefault(guildTask.getGuildInfoId(), new ArrayList<GuildPlayer>()), rewardMsg);
            rewardedTaskIds.add(guildTask.getId());
            guildIdsToCache.add(guildTask.getGuildInfoId());
        }
        GuildTaskService.getInstance().updateStatusById(rewardedTaskIds, true);
        HandySchedulerUtil.runTask(() -> guildIdsToCache.forEach(GuildUtil::cacheGuildTask));
        return rewardedTaskIds.size();
    }

    private static void sendTaskReward(GuildTask guildTask, List<GuildPlayer> guildPlayers, String rewardMsg) {
        int guildMoney = ConfigUtil.TASK_CONFIG.getInt(new StringBuilder().insert(0, "guildTask.").append(guildTask.getTaskId()).append(".reward.guildMoney").toString());
        int guildProsperityDegree = ConfigUtil.TASK_CONFIG.getInt(new StringBuilder().insert(0, "guildTask.").append(guildTask.getTaskId()).append(".reward.guildProsperityDegree").toString());
        int guildOre = ConfigUtil.TASK_CONFIG.getInt(new StringBuilder().insert(0, "guildTask.").append(guildTask.getTaskId()).append(".reward.guildOre").toString());
        GuildInfoService.getInstance().addTaskRewardById(guildTask.getGuildInfoId(), guildMoney, guildProsperityDegree, guildOre);
        rewardMsg = rewardMsg.replace("${taskName}", guildTask.getTaskName()).replace("${guildMoney}", String.valueOf(guildMoney)).replace("${guildProsperityDegree}", String.valueOf(guildProsperityDegree)).replace("${guildOre}", String.valueOf(guildOre));
        for (GuildPlayer guildPlayer : guildPlayers) {
            MessageUtil.sendMessage(guildPlayer.getPlayerName(), rewardMsg);
        }
        MessageUtil.sendConsoleMessage(new StringBuilder().insert(0, rewardMsg).append(" 公会ID:").append(guildTask.getGuildInfoId()).toString());
    }

    @Override
    public String command() {
        return "reward";
    }

    @Override
    public boolean isAsync() {
        return true;
    }

    @Override
    public String permission() {
        return "playerGuild.reward";
    }

    private void rewardAllTasks(CommandSender sender) {
        List<GuildTask> rewardTasks = GuildTaskService.getInstance().findRewardTask();
        if (CollUtil.isEmpty(rewardTasks)) {
            MessageUtil.sendMessage(sender, BaseUtil.getLangMsg("rewardTaskEmptyMsg"));
            return;
        }
        int rewardedCount = RewardCommand.rewardTask(rewardTasks);
        MessageUtil.sendMessage(sender, BaseUtil.getLangMsg("rewardTaskSucceedMsg", MapUtil.of("${delNum}", String.valueOf(rewardedCount))));
    }

    @Override
    public void onCommand(CommandSender sender, Command command, String label, String[] args) {
        switch (this.getArg(args, 1, BaseUtil.getLangMsg("paramFailureMsg")).toLowerCase(Locale.ROOT)) {
            case "task": {
                this.rewardAllTasks(sender);
                return;
            }
        }
        MessageUtil.sendMessage(sender, BaseUtil.getLangMsg("paramFailureMsg"));
    }
}
