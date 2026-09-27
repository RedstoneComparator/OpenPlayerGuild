package cn.handyplus.guild.listener.gui;

import cn.handyplus.guild.constants.GuiTypeEnum;
import cn.handyplus.guild.enter.GuildLogo;
import cn.handyplus.guild.inventory.GuildLogoOpenGui;
import cn.handyplus.guild.inventory.GuildSettingGui;
import cn.handyplus.guild.lib.HandyInventory;
import cn.handyplus.guild.lib.HandyInventoryUtil;
import cn.handyplus.guild.lib.IHandyClickEvent;
import cn.handyplus.guild.lib.MessageUtil;
import cn.handyplus.guild.service.GuildInfoService;
import cn.handyplus.guild.service.GuildLogoService;
import cn.handyplus.guild.util.ConfigUtil;
import java.util.Map;
import java.util.Optional;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class GuildLogoOpenClickEvent
implements IHandyClickEvent {
    @Override
    public boolean isAsync() {
        return true;
    }

    @Override
    public void rawSlotClick(HandyInventory gui, InventoryClickEvent clickEvent) {
        int rawSlot = clickEvent.getRawSlot();
        HandyInventory inventoryGui = gui;
        Integer pageNum = gui.getPageNum();
        Integer pageCount = inventoryGui.getPageCount();
        Player player = inventoryGui.getPlayer();
        Map<Integer, Integer> map = inventoryGui.getIntMap();
        Integer guildInfoId = inventoryGui.getId();
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.LOGO_SHOP_CONFIG, "back")) {
            Inventory inventory = GuildSettingGui.getInstance().createGui(player, gui.getId());
            gui.syncOpen(inventory);
            return;
        }
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.LOGO_SHOP_CONFIG, "previousPage")) {
            if (pageNum > 1) {
                HandyInventory pageGui = gui;
                pageGui.setPageNum(pageGui.getPageNum() - 1);
                GuildLogoOpenGui.getInstance().setInventoryDate(gui);
            }
            return;
        }
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.LOGO_SHOP_CONFIG, "nextPage")) {
            if (pageNum + 1 <= pageCount) {
                HandyInventory nextPageGui = gui;
                nextPageGui.setPageNum(nextPageGui.getPageNum() + 1);
                GuildLogoOpenGui.getInstance().setInventoryDate(gui);
            }
            return;
        }
        Integer logoId = map.get(rawSlot);
        if (logoId == null) {
            return;
        }
        Optional<GuildLogo> optional = GuildLogoService.getInstance().findById(logoId);
        if (!optional.isPresent()) {
            return;
        }
        GuildLogo guildLogo = optional.get();
        if (guildLogo.getUseFlag().booleanValue()) {
            return;
        }
        GuildLogoService.getInstance().updateUseToFalse(guildInfoId);
        GuildLogoService.getInstance().updateUseById(logoId);
        GuildInfoService.getInstance().updateLogoById(guildInfoId, guildLogo.getLogoId(), guildLogo.getLogoName());
        MessageUtil.sendMessage(player, ConfigUtil.LOGO_SHOP_CONFIG.getString("useMsg"));
        GuildLogoOpenGui.getInstance().setInventoryDate(gui);
    }

    @Override
    public String guiType() {
        return GuiTypeEnum.GUILD_LOGO_OPEN.getType();
    }
}
