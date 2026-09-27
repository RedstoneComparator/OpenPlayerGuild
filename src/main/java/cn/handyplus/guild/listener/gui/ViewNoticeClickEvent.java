package cn.handyplus.guild.listener.gui;

import cn.handyplus.guild.constants.GuiTypeEnum;
import cn.handyplus.guild.inventory.ViewNoticeGui;
import cn.handyplus.guild.lib.HandyInventory;
import cn.handyplus.guild.lib.HandyInventoryUtil;
import cn.handyplus.guild.lib.IHandyClickEvent;
import cn.handyplus.guild.service.GuildNoticeService;
import cn.handyplus.guild.util.ConfigUtil;
import java.util.Map;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ViewNoticeClickEvent
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
        Object playerObject = handyInventory.getPlayer();
        Map<Integer, Integer> intMap = handyInventory.getIntMap();
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.NOTICE_CONFIG, "back")) {
            clickedInventory.syncClose();
            return;
        }
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.NOTICE_CONFIG, "previousPage")) {
            if (pageNum > 1) {
                HandyInventory handyInventory2 = clickedInventory;
                handyInventory2.setPageNum(handyInventory2.getPageNum() - 1);
                ViewNoticeGui.getInstance().setInventoryDate(clickedInventory);
            }
            return;
        }
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.NOTICE_CONFIG, "nextPage")) {
            if (pageNum + 1 <= pageCount) {
                HandyInventory handyInventory3 = clickedInventory;
                handyInventory3.setPageNum(handyInventory3.getPageNum() + 1);
                ViewNoticeGui.getInstance().setInventoryDate(clickedInventory);
            }
            return;
        }
        Integer noticeId = intMap.get(rawSlot);
        if (noticeId != null) {
            GuildNoticeService.getInstance().delById(noticeId);
            ViewNoticeGui.getInstance().setInventoryDate(clickedInventory);
        }
    }

    @Override
    public String guiType() {
        return GuiTypeEnum.VIEW_NOTICE.getType();
    }
}
