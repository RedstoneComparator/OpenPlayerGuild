package cn.handyplus.guild.hook;

import cn.handyplus.guild.PlayerGuild;
import cn.handyplus.guild.enter.GuildTask;
import cn.handyplus.guild.enter.GuildTaskDemand;
import cn.handyplus.guild.lib.CollUtil;
import cn.handyplus.guild.lib.DateUtil;
import cn.handyplus.guild.service.GuildTaskDemandService;
import cn.handyplus.guild.service.GuildTaskService;
import cn.handyplus.guild.util.ConfigUtil;
import cn.handyplus.guild.util.GuildUtil;
import com.handy.playertask.api.PlayerTaskApi;
import com.handy.playertask.entity.TaskDemand;
import com.handy.playertask.entity.TaskList;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PlayerTaskUtil {
    private PlayerTaskUtil() {
    }

    public static void createTask(Integer guildId) {
        if (!PlayerGuild.USE_TASK || !ConfigUtil.TASK_CONFIG.getBoolean("enable", true)) {
            return;
        }
        List<Integer> taskIds = ConfigUtil.TASK_CONFIG.getIntegerList("taskId");
        if (CollUtil.isEmpty(taskIds)) {
            return;
        }
        Date startDate = DateUtil.getMonday();
        Date endDate = DateUtil.getSunday();
        for (Integer taskId : taskIds) {
            TaskList taskList;
            if (taskId == 0 || (taskList = PlayerTaskApi.getInstance().findDetailByTaskId(taskId)) == null || CollUtil.isEmpty(taskList.getTaskDemands())) continue;
            GuildTask guildTask = new GuildTask();
            guildTask.setGuildInfoId(guildId);
            guildTask.setTaskId(taskId);
            guildTask.setTaskName(taskList.getTaskName());
            guildTask.setStatus(false);
            guildTask.setDemandSuccess(false);
            guildTask.setStartDate(startDate);
            guildTask.setEndDate(endDate);
            int guildTaskId = GuildTaskService.getInstance().add(guildTask);
            ArrayList<GuildTaskDemand> demands = new ArrayList<GuildTaskDemand>(taskList.getTaskDemands().size());
            for (TaskDemand demand : taskList.getTaskDemands()) {
                GuildTaskDemand guildTaskDemand = new GuildTaskDemand();
                guildTaskDemand.setGuildInfoId(guildId);
                guildTaskDemand.setTaskId(taskId);
                guildTaskDemand.setGuildTaskId(guildTaskId);
                guildTaskDemand.setType(demand.getType());
                guildTaskDemand.setCompletionAmount(0);
                guildTaskDemand.setAmount(demand.getAmount());
                guildTaskDemand.setItemStack(demand.getItemStack());
                guildTaskDemand.setStartDate(startDate);
                guildTaskDemand.setEndDate(endDate);
                demands.add(guildTaskDemand);
            }
            GuildTaskDemandService.getInstance().addBatch(demands);
        }
        GuildUtil.cacheGuildTask(guildId);
    }
}
