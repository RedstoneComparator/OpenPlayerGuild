package cn.handyplus.guild.listener.hook;

import cn.handyplus.guild.command.admin.RewardCommand;
import cn.handyplus.guild.constants.GuildConstants;
import cn.handyplus.guild.enter.GuildPlayer;
import cn.handyplus.guild.enter.GuildTask;
import cn.handyplus.guild.enter.GuildTaskDemand;
import cn.handyplus.guild.lib.CollUtil;
import cn.handyplus.guild.lib.HandyListener;
import cn.handyplus.guild.lib.HandySchedulerUtil;
import cn.handyplus.guild.lib.ItemStackUtil;
import cn.handyplus.guild.lib.StrUtil;
import cn.handyplus.guild.service.GuildTaskDemandService;
import cn.handyplus.guild.service.GuildTaskService;
import cn.handyplus.guild.util.ConfigUtil;
import cn.handyplus.guild.util.GuildUtil;
import com.handy.playertask.event.PlayerTaskScheduleEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@HandyListener
public class PlayerTaskScheduleEventListener
implements Listener {
    private boolean hasMatchingDemand(GuildPlayer guildPlayer, String itemStack, String demandType) {
        List<GuildTaskDemand> demands = GuildConstants.GUILD_TASK_DEMAND_MAP.get(guildPlayer.getGuildInfoId());
        if (CollUtil.isEmpty(demands)) {
            return true;
        }
        boolean found = false;
        for (GuildTaskDemand guildTaskDemand : demands) {
            if (!guildTaskDemand.getItemStack().equals(itemStack)) continue;
            if (!guildTaskDemand.getType().equals(demandType)) continue;
            found = true;
            break;
        }
        if (!found) {
            return true;
        }
        return false;
    }

    private void recordTaskProgress(GuildPlayer guildPlayer, String itemStack, String demandType, Integer amount) {
        this.addTaskProgress(guildPlayer, itemStack, demandType, amount);
    }

    private void addTaskProgress(GuildPlayer guildPlayer, String itemStack, String demandType, Integer amount) {
        List<GuildTaskDemand> demands = GuildTaskDemandService.getInstance().findByGuildIdAndContent(guildPlayer.getGuildInfoId(), itemStack, demandType);
        if (CollUtil.isEmpty(demands)) {
            return;
        }
        LinkedHashMap<Integer, Integer> amountByDemandId = new LinkedHashMap<Integer, Integer>();
        LinkedHashSet<Integer> guildTaskIds = new LinkedHashSet<Integer>();
        for (GuildTaskDemand guildTaskDemand : demands) {
            amountByDemandId.merge(guildTaskDemand.getId(), amount, Integer::sum);
            guildTaskIds.add(guildTaskDemand.getGuildTaskId());
        }
        HashMap<Integer, List<Integer>> demandIdsByAmount = new HashMap<Integer, List<Integer>>();
        for (Map.Entry<Integer, Integer> entry : amountByDemandId.entrySet()) {
            demandIdsByAmount.computeIfAbsent(entry.getValue(), amountKey -> new ArrayList()).add(entry.getKey());
        }
        GuildTaskDemandService.getInstance().addCompletionAmountBatch(demandIdsByAmount);
        List<Integer> guildTaskIdList = new ArrayList<Integer>(guildTaskIds);
        List<GuildTaskDemand> allDemands = GuildTaskDemandService.getInstance().findByGuildTaskIds(guildTaskIdList);
        HashMap<Integer, List<GuildTaskDemand>> demandsByTaskId = new HashMap<Integer, List<GuildTaskDemand>>();
        for (GuildTaskDemand guildTaskDemand2 : allDemands) {
            demandsByTaskId.computeIfAbsent(guildTaskDemand2.getGuildTaskId(), taskIdKey -> new ArrayList()).add(guildTaskDemand2);
        }
        ArrayList<Integer> completedTaskIds = new ArrayList<Integer>();
        for (Integer guildTaskId : guildTaskIdList) {
            List<GuildTaskDemand> taskDemands = demandsByTaskId.get(guildTaskId);
            if (CollUtil.isEmpty(taskDemands)) continue;
            boolean allCompleted = true;
            for (GuildTaskDemand guildTaskDemand3 : taskDemands) {
                if (guildTaskDemand3.getCompletionAmount() >= guildTaskDemand3.getAmount()) continue;
                allCompleted = false;
                break;
            }
            if (!allCompleted) continue;
            completedTaskIds.add(guildTaskId);
        }
        if (CollUtil.isNotEmpty(completedTaskIds)) {
            GuildTaskService.getInstance().updateDemandSuccessByIds(completedTaskIds, true);
            if (ConfigUtil.TASK_CONFIG.getBoolean("autoReward", false)) {
                List<GuildTask> rewardTasks = GuildTaskService.getInstance().findRewardTaskByIds(completedTaskIds);
                if (CollUtil.isNotEmpty(rewardTasks)) {
                    RewardCommand.rewardTask(rewardTasks);
                }
            }
        }
        GuildUtil.cacheGuildTask(guildPlayer.getGuildInfoId());
    }

    @EventHandler
    public void onPlayerTaskSchedule(PlayerTaskScheduleEvent event) {
        if (!ConfigUtil.TASK_CONFIG.getBoolean("enable", true)) {
            return;
        }
        Player player = event.getPlayer();
        Integer amount = event.getAmount();
        String content = event.getContent();
        String demandType = event.getDemandType();
        if (StrUtil.isEmpty(content) && event.getItemStack() != null) {
            content = ItemStackUtil.itemStackSerialize(event.getItemStack());
        }
        String finalContent = content;
        GuildPlayer guildPlayer;
        if ((guildPlayer = GuildConstants.PLAYER_GUILD_MAP.get(player.getUniqueId())) == null) {
            return;
        }
        if (this.hasMatchingDemand(guildPlayer, finalContent, demandType)) {
            return;
        }
        HandySchedulerUtil.runTaskAsynchronously(() -> this.recordTaskProgress(guildPlayer, finalContent, demandType, amount));
    }
}
