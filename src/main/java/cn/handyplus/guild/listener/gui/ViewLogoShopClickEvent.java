package cn.handyplus.guild.listener.gui;

import cn.handyplus.guild.constants.GuiTypeEnum;
import cn.handyplus.guild.inventory.ViewLogoShopGui;
import cn.handyplus.guild.lib.HandyInventory;
import cn.handyplus.guild.lib.HandyInventoryUtil;
import cn.handyplus.guild.lib.IHandyClickEvent;
import cn.handyplus.guild.service.GuildLogoService;
import cn.handyplus.guild.service.GuildLogoShopService;
import cn.handyplus.guild.util.ConfigUtil;
import java.util.Map;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ViewLogoShopClickEvent
implements IHandyClickEvent {
    @Override
    public void rawSlotClick(HandyInventory clickedInventory, InventoryClickEvent clickEvent) {
        int rawSlot = clickEvent.getRawSlot();
        HandyInventory handyInventory = clickedInventory;
        Integer pageNum = handyInventory.getPageNum();
        Integer pageCount = handyInventory.getPageCount();
        Map<Integer, Integer> intMap = handyInventory.getIntMap();
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.LOGO_SHOP_CONFIG, "back")) {
            clickedInventory.syncClose();
            return;
        }
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.LOGO_SHOP_CONFIG, "previousPage")) {
            if (pageNum > 1) {
                HandyInventory handyInventory2 = clickedInventory;
                handyInventory2.setPageNum(handyInventory2.getPageNum() - 1);
                ViewLogoShopGui.getInstance().setInventoryDate(clickedInventory);
            }
            return;
        }
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.LOGO_SHOP_CONFIG, "nextPage")) {
            if (pageNum + 1 <= pageCount) {
                HandyInventory handyInventory3 = clickedInventory;
                handyInventory3.setPageNum(handyInventory3.getPageNum() + 1);
                ViewLogoShopGui.getInstance().setInventoryDate(clickedInventory);
            }
            return;
        }
        Integer logoShopId = intMap.get(rawSlot);
        if (logoShopId != null) {
            GuildLogoShopService.getInstance().delById(logoShopId);
            GuildLogoService.getInstance().delByLogoId(logoShopId);
            ViewLogoShopGui.getInstance().setInventoryDate(clickedInventory);
        }
    }

    @Override
    public String guiType() {
        return GuiTypeEnum.VIEW_LOGO_SHOP.getType();
    }

    @Override
    public boolean isAsync() {
        return true;
    }
}
