package cn.handyplus.guild.inventory;

import cn.handyplus.guild.constants.GuiTypeEnum;
import cn.handyplus.guild.enter.GuildTask;
import cn.handyplus.guild.enter.GuildTaskDemand;
import cn.handyplus.guild.hook.PlaceholderApiUtil;
import cn.handyplus.guild.lib.HandyInventory;
import cn.handyplus.guild.lib.HandyInventoryUtil;
import cn.handyplus.guild.lib.ItemStackUtil;
import cn.handyplus.guild.lib.MapUtil;
import cn.handyplus.guild.lib.Page;
import cn.handyplus.guild.lib.StrUtil;
import cn.handyplus.guild.service.GuildTaskDemandService;
import cn.handyplus.guild.service.GuildTaskService;
import cn.handyplus.guild.util.ConfigUtil;
import com.handy.playertask.api.PlayerTaskApi;
import com.handy.playertask.entity.TaskPlayerDemand;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class GuildTaskGui {
    private final static GuildTaskGui INSTANCE = new GuildTaskGui();

    public Inventory createGui(Player player, Integer guildId) {
        Object guiValue = ConfigUtil.TASK_CONFIG.getString("title");
        String sound = ConfigUtil.TASK_CONFIG.getString("sound");
        guiValue = PlaceholderApiUtil.set(player, (String)guiValue);
        int size = ConfigUtil.TASK_CONFIG.getInt("size", 54);
        Object handyInventory = guiValue = new HandyInventory(GuiTypeEnum.GUILD_TASK.getType(), (String)guiValue, size, sound);
        ((HandyInventory)guiValue).setPlayer(player);
        ((HandyInventory)handyInventory).setId(guildId);
        this.setInventoryDate((HandyInventory)handyInventory);
        return ((HandyInventory)handyInventory).getInventory();
    }

    private void addNavigationButtons(HandyInventory gui) {
        HandyInventoryUtil.setButton(ConfigUtil.TASK_CONFIG, gui, "info");
        Map<String, String> map = HandyInventoryUtil.replacePageMap(gui);
        HandyInventoryUtil.setButton(ConfigUtil.TASK_CONFIG, gui, "nextPage", map);
        HandyInventoryUtil.setButton(ConfigUtil.TASK_CONFIG, gui, "previousPage", map);
        HandyInventoryUtil.setButton(ConfigUtil.TASK_CONFIG, gui, "back");
        HandyInventoryUtil.setButton(ConfigUtil.TASK_CONFIG, gui, "pane");
    }

    public static List<TaskPlayerDemand> toTaskPlayerDemands(List<GuildTaskDemand> taskDemands) {
        Iterator<GuildTaskDemand> demandIterator;
        ArrayList<TaskPlayerDemand> demands = new ArrayList<TaskPlayerDemand>();
        if (taskDemands == null) {
            return demands;
        }
        Iterator<GuildTaskDemand> taskDemandIterator = demandIterator = taskDemands.iterator();
        while (taskDemandIterator.hasNext()) {
            GuildTaskDemand guildTaskDemand = demandIterator.next();
            TaskPlayerDemand taskDemand = new TaskPlayerDemand();
            taskDemandIterator = demandIterator;
            TaskPlayerDemand taskDemandRef = taskDemand;
            GuildTaskDemand guildTaskDemandRef = guildTaskDemand;
            taskDemand.setType(guildTaskDemand.getType());
            taskDemand.setCompletionAmount(guildTaskDemandRef.getCompletionAmount());
            taskDemandRef.setAmount(guildTaskDemandRef.getAmount());
            taskDemandRef.setItemStack(guildTaskDemand.getItemStack());
            demands.add(taskDemandRef);
        }
        return demands;
    }

    private void fillTaskItems(HandyInventory gui) {
        Inventory inventory = gui.getInventory();
        Map<Integer, Integer> map = gui.getIntMap();
        List<Integer> list = StrUtil.strToIntList(ConfigUtil.TASK_CONFIG.getString("index", "20,21,22,23,24,29,30,31,32,33"));
        gui.setPageSize(list.size());
        Page<GuildTask> page = GuildTaskService.getInstance().page(gui.getId(), gui.getPageNum(), gui.getPageSize());
        gui.setPageCount(page.getTotal());
        if (page.getTotal() < 1) {
            return;
        }
        List<GuildTask> records = page.getRecords();
        List<Integer> taskIds = records.stream().map(GuildTask::getId).collect(Collectors.toList());
        Map<Integer, List<GuildTaskDemand>> demandMap = GuildTaskDemandService.getInstance().findByGuildTaskIds(taskIds).stream().collect(Collectors.groupingBy(GuildTaskDemand::getGuildTaskId));
        for (GuildTask guildTask : records) {
            if (demandMap.get(guildTask.getId()) == null) continue;
            guildTask.setGuildTaskDemandList(demandMap.get(guildTask.getId()));
        }
        int index = 0;
        for (GuildTask taskItem : records) {
            Integer slot = list.get(index);
            ++index;
            List<TaskPlayerDemand> taskDemands = GuildTaskGui.toTaskPlayerDemands(taskItem.getGuildTaskDemandList());
            String material = ConfigUtil.TASK_CONFIG.getString(new StringBuilder().insert(0, "guildTask.").append(taskItem.getTaskId()).append(".material").toString());
            String name = ConfigUtil.TASK_CONFIG.getString(new StringBuilder().insert(0, "guildTask.").append(taskItem.getTaskId()).append(".name").toString(), "${taskName}").replace("${taskName}", taskItem.getTaskName());
            int customModel = ConfigUtil.TASK_CONFIG.getInt(new StringBuilder().insert(0, "guildTask.").append(taskItem.getTaskId()).append(".custom-model-data").toString());
            List<String> loreTemplate = ConfigUtil.TASK_CONFIG.getStringList(new StringBuilder().insert(0, "guildTask.").append(taskItem.getTaskId()).append(".lore").toString());
            Map<String, String> placeholders = GuildTaskGui.replaceMap(taskItem);
            Map<String, List<String>> scheduleMap = MapUtil.of("taskSchedule", PlayerTaskApi.taskScheduleReplaceMap(taskDemands));
            List<String> lore = ItemStackUtil.loreReplaceMap(loreTemplate, placeholders);
            lore = ItemStackUtil.loreBatchReplaceMap(lore, scheduleMap, "");
            ItemStack itemStack = ItemStackUtil.getItemStack(material, name, lore, false, customModel);
            inventory.setItem(slot.intValue(), itemStack);
            map.put(slot, taskItem.getId());
        }
    }

    public static Map<String, String> replaceMap(GuildTask guildTask) {
        if (guildTask.getStatus().booleanValue()) {
            return MapUtil.of("status", ConfigUtil.TASK_CONFIG.getString("settled"));
        }
        if (guildTask.getDemandSuccess().booleanValue()) {
            String statusText = ConfigUtil.TASK_CONFIG.getString("pendingSettlement");
            String startDate = ConfigUtil.TASK_CONFIG.getString("startDate");
            String startTime = ConfigUtil.TASK_CONFIG.getString("startTime");
            statusText = StrUtil.replace(statusText, "day", startDate);
            statusText = StrUtil.replace(statusText, "time", startTime);
            return MapUtil.of("status", statusText);
        }
        return MapUtil.of("status", ConfigUtil.TASK_CONFIG.getString("unfinished"));
    }

    private GuildTaskGui() {
    }

    public void setInventoryDate(HandyInventory gui) {
        HandyInventory inventory = gui;
        inventory.setGuiType(GuiTypeEnum.GUILD_TASK.getType());
        HandyInventoryUtil.refreshInventory(inventory.getInventory());
        GuildTaskGui instance = this;
        instance.fillTaskItems(gui);
        instance.addNavigationButtons(inventory);
    }

    public static GuildTaskGui getInstance() {
        return INSTANCE;
    }
}
