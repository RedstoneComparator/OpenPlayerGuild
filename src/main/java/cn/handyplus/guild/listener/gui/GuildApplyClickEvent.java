package cn.handyplus.guild.listener.gui;

import cn.handyplus.guild.command.function.AcceptCommand;
import cn.handyplus.guild.command.function.DenyCommand;
import cn.handyplus.guild.constants.GuiTypeEnum;
import cn.handyplus.guild.inventory.GuildApplyGui;
import cn.handyplus.guild.inventory.GuildSettingGui;
import cn.handyplus.guild.lib.HandyInventory;
import cn.handyplus.guild.lib.HandyInventoryUtil;
import cn.handyplus.guild.lib.IHandyClickEvent;
import cn.handyplus.guild.util.ConfigUtil;
import java.util.Map;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class GuildApplyClickEvent
implements IHandyClickEvent {
    @Override
    public String guiType() {
        return GuiTypeEnum.GUILD_APPLY.getType();
    }

    @Override
    public boolean isAsync() {
        return true;
    }

    @Override
    public void rawSlotClick(HandyInventory gui, InventoryClickEvent clickEvent) {
        int rawSlot = clickEvent.getRawSlot();
        HandyInventory inventoryGui = gui;
        Integer pageNum = inventoryGui.getPageNum();
        Integer pageCount = inventoryGui.getPageCount();
        Player player = inventoryGui.getPlayer();
        Map<Integer, Integer> map = inventoryGui.getIntMap();
        int backIndex = ConfigUtil.APPLY_CONFIG.getInt("back.index");
        if (0 == backIndex) {
            Inventory inventory = GuildSettingGui.getInstance().createGui(player, gui.getId());
            gui.syncOpen(inventory);
            return;
        }
        if (HandyInventoryUtil.isIndex(0, ConfigUtil.APPLY_CONFIG, "previousPage")) {
            if (pageNum > 1) {
                HandyInventory pageGui = gui;
                pageGui.setPageNum(pageGui.getPageNum() - 1);
                GuildApplyGui.getInstance().setInventoryDate(gui);
            }
            return;
        }
        if (HandyInventoryUtil.isIndex(0, ConfigUtil.APPLY_CONFIG, "nextPage")) {
            if (pageNum + 1 <= pageCount) {
                HandyInventory nextPageGui = gui;
                nextPageGui.setPageNum(nextPageGui.getPageNum() + 1);
                GuildApplyGui.getInstance().setInventoryDate(gui);
            }
            return;
        }
        Integer applyId = map.get(0);
        if (applyId == null) {
            return;
        }
        Player targetPlayer = player;
        if (ClickType.RIGHT.equals((Object)clickEvent.getClick())) {
            DenyCommand.deny(targetPlayer, applyId);
        } else {
            AcceptCommand.accept(targetPlayer, applyId);
        }
        GuildApplyGui.getInstance().setInventoryDate(gui);
    }
}
