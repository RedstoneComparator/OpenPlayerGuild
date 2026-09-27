package cn.handyplus.guild.hook;

import cn.handyplus.guild.PlayerGuild;
import cn.handyplus.guild.enter.GuildTask;
import cn.handyplus.guild.enter.GuildTaskDemand;
import cn.handyplus.guild.inventory.GuildTaskGui;
import cn.handyplus.guild.lib.ItemStackUtil;
import cn.handyplus.guild.lib.MapUtil;
import cn.handyplus.guild.lib.Pair;
import cn.handyplus.guild.lib.StrUtil;
import cn.handyplus.guild.service.GuildTaskDemandService;
import cn.handyplus.guild.service.GuildTaskService;
import cn.handyplus.guild.util.ConfigUtil;
import cn.handyplus.scoreboard.api.PlayerScoreboardApi;
import cn.handyplus.scoreboard.param.ExternalLine;
import com.handy.playertask.api.PlayerTaskApi;
import com.handy.playertask.entity.TaskPlayerDemand;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class PlayerScoreboardUtil {
    public static void addLine(List<Player> players, Integer guildId) {
        List<String> lines;
        String title;
        boolean enabled = ConfigUtil.SCOREBOARD_CONFIG.getBoolean("enable");
        if (!enabled || !PlayerGuild.USE_PSB) {
            return;
        }
        String key = ConfigUtil.SCOREBOARD_CONFIG.getString("key", "guild");
        int priority = ConfigUtil.SCOREBOARD_CONFIG.getInt("existPriority");
        Optional<GuildTask> taskOptional = PlayerScoreboardUtil.findCurrentUndoneTask(guildId);
        if (taskOptional.isPresent()) {
            Pair<String, List<String>> taskPair = PlayerScoreboardUtil.buildTaskLines(taskOptional.get());
            title = taskPair.getKey();
            lines = taskPair.getValue();
        } else {
            title = null;
            lines = ConfigUtil.SCOREBOARD_CONFIG.getStringList("noTaskLines");
        }
        for (Player player : players) {
            PlayerScoreboardApi.setPriority((Plugin)PlayerGuild.INSTANCE, (Player)player, (String)key, (int)priority);
            if (title != null) {
                PlayerScoreboardApi.setTitle((Plugin)PlayerGuild.INSTANCE, (Player)player, (String)key, (String)title);
            }
            PlayerScoreboardApi.addLines((Plugin)PlayerGuild.INSTANCE, (Player)player, (String)key, (ExternalLine)ExternalLine.append(lines));
        }
    }

    private static Optional<GuildTask> findCurrentUndoneTask(Integer guildId) {
        Optional<GuildTask> taskOptional = GuildTaskService.getInstance().findFirstUndone(guildId);
        if (!taskOptional.isPresent()) {
            return Optional.empty();
        }
        GuildTask guildTask = taskOptional.get();
        List<GuildTaskDemand> demands = GuildTaskDemandService.getInstance().findByGuildTaskId(guildTask.getId());
        guildTask.setGuildTaskDemandList(demands);
        return Optional.of(guildTask);
    }

    private static Pair<String, List<String>> buildTaskLines(GuildTask guildTask) {
        String name = ConfigUtil.TASK_CONFIG.getString("guildTask." + guildTask.getTaskId() + ".name", "${taskName}");
        name = StrUtil.replace(name, "taskName", guildTask.getTaskName());
        List<String> lore = ConfigUtil.TASK_CONFIG.getStringList("guildTask." + guildTask.getTaskId() + ".lore");
        lore.removeIf(line -> line.contains("${status}"));
        List<TaskPlayerDemand> demands = GuildTaskGui.toTaskPlayerDemands(guildTask.getGuildTaskDemandList());
        Map<String, String> replaceMap = GuildTaskGui.replaceMap(guildTask);
        lore = ItemStackUtil.loreReplaceMap(lore, replaceMap);
        HashMap<String, List<String>> batchMap = MapUtil.of("taskSchedule", PlayerTaskApi.taskScheduleReplaceMap(demands));
        lore = ItemStackUtil.loreBatchReplaceMap(lore, batchMap, "");
        return Pair.of(name, lore);
    }
}
