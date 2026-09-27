package cn.handyplus.guild.listener.gui;

import cn.handyplus.guild.command.admin.RewardCommand;
import cn.handyplus.guild.constants.GuiTypeEnum;
import cn.handyplus.guild.enter.GuildTask;
import cn.handyplus.guild.enter.GuildTaskDemand;
import cn.handyplus.guild.inventory.GuildTaskGui;
import cn.handyplus.guild.inventory.GuildViewGui;
import cn.handyplus.guild.lib.BaseUtil;
import cn.handyplus.guild.lib.CollUtil;
import cn.handyplus.guild.lib.HandyInventory;
import cn.handyplus.guild.lib.HandyInventoryUtil;
import cn.handyplus.guild.lib.IHandyClickEvent;
import cn.handyplus.guild.lib.ItemStackUtil;
import cn.handyplus.guild.lib.MapUtil;
import cn.handyplus.guild.lib.MessageUtil;
import cn.handyplus.guild.service.GuildTaskDemandService;
import cn.handyplus.guild.service.GuildTaskService;
import cn.handyplus.guild.util.ConfigUtil;
import cn.handyplus.guild.util.GuildUtil;
import com.handy.playertask.constants.DemandTypeEnum;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class GuildTaskClickEvent
implements IHandyClickEvent {
    private void submitTaskDemands(Player player, List<GuildTaskDemand> demands) {
        if (CollUtil.isEmpty(demands)) {
            return;
        }
        Integer guildInfoId = demands.get(0).getGuildInfoId();
        HashMap<Integer, Integer> submittedAmounts = new HashMap<Integer, Integer>();
        for (GuildTaskDemand guildTaskDemand : demands) {
            if (!DemandTypeEnum.SUBMIT.getType().equals(guildTaskDemand.getType())) continue;
            int remainingAmount = guildTaskDemand.getAmount() - guildTaskDemand.getCompletionAmount();
            if (remainingAmount <= 0) continue;
            ItemStack itemStack = ItemStackUtil.itemStackDeserialize(guildTaskDemand.getItemStack());
            int ownedAmount = ItemStackUtil.getItemAmount(player, itemStack);
            if (ownedAmount <= 0) continue;
            int submitAmount = Math.min(ownedAmount, remainingAmount);
            ItemStackUtil.removeItem(player, itemStack, submitAmount, false);
            submittedAmounts.put(guildTaskDemand.getId(), submitAmount);
            String message = BaseUtil.getLangMsg("submitSucceedMsg", MapUtil.of("${item}", BaseUtil.getDisplayName(itemStack), "${number}", submitAmount + ""));
            MessageUtil.sendMessage(player, message);
        }
        if (MapUtil.isEmpty(submittedAmounts)) {
            GuildUtil.cacheGuildTask(guildInfoId);
            return;
        }
        HashMap<Integer, List<Integer>> demandIdsByAmount = new HashMap<Integer, List<Integer>>();
        for (Map.Entry<Integer, Integer> entry : submittedAmounts.entrySet()) {
            demandIdsByAmount.computeIfAbsent(entry.getValue(), amountKey -> new ArrayList()).add(entry.getKey());
        }
        GuildTaskDemandService.getInstance().addCompletionAmountBatch(demandIdsByAmount);
        Integer guildTaskId = demands.get(0).getGuildTaskId();
        boolean allCompleted = true;
        for (GuildTaskDemand guildTaskDemand2 : demands) {
            int completedAmount = guildTaskDemand2.getCompletionAmount() + submittedAmounts.getOrDefault(guildTaskDemand2.getId(), 0);
            if (completedAmount >= guildTaskDemand2.getAmount()) continue;
            allCompleted = false;
            break;
        }
        if (allCompleted) {
            GuildTaskService.getInstance().updateDemandSuccess(guildTaskId, true);
            if (ConfigUtil.TASK_CONFIG.getBoolean("autoReward", false)) {
                Optional<GuildTask> rewardTaskOptional = GuildTaskService.getInstance().findRewardTaskById(guildTaskId);
                rewardTaskOptional.ifPresent(rewardTask -> RewardCommand.rewardTask(Collections.singletonList(rewardTask)));
            }
        }
        GuildUtil.cacheGuildTask(guildInfoId);
    }

    @Override
    public boolean isAsync() {
        return true;
    }

    @Override
    public void rawSlotClick(HandyInventory handyInventory, InventoryClickEvent clickEvent) {
        int rawSlot = clickEvent.getRawSlot();
        Integer pageNum = handyInventory.getPageNum();
        Integer pageCount = handyInventory.getPageCount();
        Player player = handyInventory.getPlayer();
        Map<Integer, Integer> intMap = handyInventory.getIntMap();
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.TASK_CONFIG, "back")) {
            Inventory inventory = GuildViewGui.getInstance().createGui(player, handyInventory.getId());
            handyInventory.syncOpen(inventory);
            return;
        }
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.TASK_CONFIG, "previousPage")) {
            if (pageNum > 1) {
                handyInventory.setPageNum(handyInventory.getPageNum() - 1);
                GuildTaskGui.getInstance().setInventoryDate(handyInventory);
            }
            return;
        }
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.TASK_CONFIG, "nextPage")) {
            if (pageNum + 1 <= pageCount) {
                handyInventory.setPageNum(handyInventory.getPageNum() + 1);
                GuildTaskGui.getInstance().setInventoryDate(handyInventory);
            }
        }
        Integer guildTaskId;
        if ((guildTaskId = intMap.get(rawSlot)) == null) {
            return;
        }
        List<GuildTaskDemand> guildTaskDemands = GuildTaskDemandService.getInstance().findByGuildTaskId(guildTaskId);
        this.submitTaskDemands(player, guildTaskDemands);
        GuildTaskGui.getInstance().setInventoryDate(handyInventory);
    }

    @Override
    public String guiType() {
        return GuiTypeEnum.GUILD_TASK.getType();
    }
}
