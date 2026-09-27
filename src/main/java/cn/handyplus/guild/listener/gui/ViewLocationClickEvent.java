package cn.handyplus.guild.listener.gui;

import cn.handyplus.guild.constants.GuiTypeEnum;
import cn.handyplus.guild.inventory.ViewLocationGui;
import cn.handyplus.guild.inventory.ViewSpawnLocationGui;
import cn.handyplus.guild.lib.HandyInventory;
import cn.handyplus.guild.lib.HandyInventoryUtil;
import cn.handyplus.guild.lib.IHandyClickEvent;
import cn.handyplus.guild.service.GuildPvpLocationService;
import cn.handyplus.guild.util.ConfigUtil;
import java.util.Map;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class ViewLocationClickEvent
implements IHandyClickEvent {
    @Override
    public boolean isAsync() {
        return true;
    }

    @Override
    public String guiType() {
        return GuiTypeEnum.VIEW_LOCATION.getType();
    }

    @Override
    public void rawSlotClick(HandyInventory clickedInventory, InventoryClickEvent clickEvent) {
        int rawSlot = clickEvent.getRawSlot();
        HandyInventory handyInventory = clickedInventory;
        Integer pageNum = handyInventory.getPageNum();
        Integer pageCount = handyInventory.getPageCount();
        Player player = handyInventory.getPlayer();
        Map<Integer, Integer> intMap = handyInventory.getIntMap();
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.PVP_LOCATION_CONFIG, "previousPage")) {
            if (pageNum > 1) {
                HandyInventory handyInventory2 = clickedInventory;
                handyInventory2.setPageNum(handyInventory2.getPageNum() - 1);
                ViewLocationGui.getInstance().setInventoryDate(clickedInventory);
            }
            return;
        }
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.PVP_LOCATION_CONFIG, "nextPage")) {
            if (2 <= pageCount) {
                HandyInventory handyInventory3 = clickedInventory;
                handyInventory3.setPageNum(handyInventory3.getPageNum() + 1);
                ViewLocationGui.getInstance().setInventoryDate(clickedInventory);
            }
            return;
        }
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.PVP_LOCATION_CONFIG, "back")) {
            clickedInventory.syncClose();
            return;
        }
        Integer locationId = intMap.get(rawSlot);
        if (locationId == null) {
            return;
        }
        if (ClickType.RIGHT.equals((Object)clickEvent.getClick())) {
            GuildPvpLocationService.getInstance().delById(locationId);
            ViewLocationGui.getInstance().setInventoryDate(clickedInventory);
            return;
        }
        Inventory inventory = ViewSpawnLocationGui.getInstance().createGui(player, locationId);
        clickedInventory.syncOpen(inventory);
    }
}
