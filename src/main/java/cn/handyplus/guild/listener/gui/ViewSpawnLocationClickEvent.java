package cn.handyplus.guild.listener.gui;

import cn.handyplus.guild.constants.GuiTypeEnum;
import cn.handyplus.guild.inventory.ViewLocationGui;
import cn.handyplus.guild.inventory.ViewSpawnLocationGui;
import cn.handyplus.guild.lib.HandyInventory;
import cn.handyplus.guild.lib.HandyInventoryUtil;
import cn.handyplus.guild.lib.IHandyClickEvent;
import cn.handyplus.guild.service.GuildSpawnPvpLocationService;
import cn.handyplus.guild.util.ConfigUtil;
import java.util.Map;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class ViewSpawnLocationClickEvent
implements IHandyClickEvent {
    @Override
    public boolean isAsync() {
        return true;
    }

    @Override
    public void rawSlotClick(HandyInventory clickedInventory, InventoryClickEvent clickEvent) {
        int rawSlot = clickEvent.getRawSlot();
        HandyInventory handyInventory = clickedInventory;
        Integer pageNum = handyInventory.getPageNum();
        Integer pageCount = handyInventory.getPageCount();
        Player player = handyInventory.getPlayer();
        Map<Integer, Integer> intMap = handyInventory.getIntMap();
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.PVP_LOCATION_CONFIG, "back")) {
            Inventory inventory = ViewLocationGui.getInstance().createGui(player);
            clickedInventory.syncOpen(inventory);
            return;
        }
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.PVP_LOCATION_CONFIG, "previousPage")) {
            if (pageNum > 1) {
                HandyInventory handyInventory2 = clickedInventory;
                handyInventory2.setPageNum(handyInventory2.getPageNum() - 1);
                ViewSpawnLocationGui.getInstance().setInventoryDate(clickedInventory);
            }
            return;
        }
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.PVP_LOCATION_CONFIG, "nextPage")) {
            if (pageNum + 1 <= pageCount) {
                HandyInventory handyInventory3 = clickedInventory;
                handyInventory3.setPageNum(handyInventory3.getPageNum() + 1);
                ViewSpawnLocationGui.getInstance().setInventoryDate(clickedInventory);
            }
            return;
        }
        Integer locationId = intMap.get(rawSlot);
        if (locationId == null) {
            return;
        }
        GuildSpawnPvpLocationService.getInstance().delById(locationId);
        ViewSpawnLocationGui.getInstance().setInventoryDate(clickedInventory);
    }

    @Override
    public String guiType() {
        return GuiTypeEnum.VIEW_SPAWN_LOCATION.getType();
    }
}
