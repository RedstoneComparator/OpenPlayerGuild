package cn.handyplus.guild.listener.gui;

import cn.handyplus.guild.constants.GuiTypeEnum;
import cn.handyplus.guild.inventory.ViewShopGui;
import cn.handyplus.guild.lib.HandyInventory;
import cn.handyplus.guild.lib.HandyInventoryUtil;
import cn.handyplus.guild.lib.IHandyClickEvent;
import cn.handyplus.guild.service.GuildShopService;
import cn.handyplus.guild.util.ConfigUtil;
import java.util.Map;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ViewShopClickEvent
implements IHandyClickEvent {
    @Override
    public String guiType() {
        return GuiTypeEnum.VIEW_SHOP.getType();
    }

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
        handyInventory.getPlayer();
        Map<Integer, Integer> intMap = handyInventory.getIntMap();
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.SHOP_CONFIG, "back")) {
            clickedInventory.syncClose();
            return;
        }
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.SHOP_CONFIG, "previousPage")) {
            if (pageNum > 1) {
                HandyInventory handyInventory2 = clickedInventory;
                handyInventory2.setPageNum(handyInventory2.getPageNum() - 1);
                ViewShopGui.getInstance().setInventoryDate(clickedInventory);
            }
            return;
        }
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.SHOP_CONFIG, "nextPage")) {
            if (pageNum + 1 <= pageCount) {
                HandyInventory handyInventory3 = clickedInventory;
                handyInventory3.setPageNum(handyInventory3.getPageNum() + 1);
                ViewShopGui.getInstance().setInventoryDate(clickedInventory);
            }
            return;
        }
        Integer shopId = intMap.get(rawSlot);
        if (shopId != null) {
            GuildShopService.getInstance().delById(shopId);
            ViewShopGui.getInstance().setInventoryDate(clickedInventory);
        }
    }
}
