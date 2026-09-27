package cn.handyplus.guild.listener.gui;

import cn.handyplus.guild.constants.GuiTypeEnum;
import cn.handyplus.guild.enter.GuildInfo;
import cn.handyplus.guild.enter.GuildLogo;
import cn.handyplus.guild.enter.GuildLogoShop;
import cn.handyplus.guild.inventory.GuildLogoShopGui;
import cn.handyplus.guild.inventory.GuildSettingGui;
import cn.handyplus.guild.lib.HandyInventory;
import cn.handyplus.guild.lib.HandyInventoryUtil;
import cn.handyplus.guild.lib.IHandyClickEvent;
import cn.handyplus.guild.lib.MessageUtil;
import cn.handyplus.guild.service.GuildInfoService;
import cn.handyplus.guild.service.GuildLogoService;
import cn.handyplus.guild.service.GuildLogoShopService;
import cn.handyplus.guild.util.ConfigUtil;
import java.util.Map;
import java.util.Optional;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class GuildLogoShopClickEvent
implements IHandyClickEvent {
    @Override
    public boolean isAsync() {
        return true;
    }

    @Override
    public String guiType() {
        return GuiTypeEnum.GUILD_LOGO_SHOP.getType();
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
                GuildLogoShopGui.getInstance().setInventoryDate(gui);
            }
            return;
        }
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.LOGO_SHOP_CONFIG, "nextPage")) {
            if (pageNum + 1 <= pageCount) {
                HandyInventory nextPageGui = gui;
                nextPageGui.setPageNum(nextPageGui.getPageNum() + 1);
                GuildLogoShopGui.getInstance().setInventoryDate(gui);
            }
            return;
        }
        Integer logoShopId = map.get(rawSlot);
        if (logoShopId == null) {
            return;
        }
        Optional<GuildLogoShop> optional = GuildLogoShopService.getInstance().findById(logoShopId);
        if (!optional.isPresent()) {
            return;
        }
        GuildLogoShop guildLogoShop = optional.get();
        Integer price = guildLogoShop.getPrice();
        Optional<GuildInfo> guildInfoOptional = GuildInfoService.getInstance().findById(guildInfoId);
        if (!guildInfoOptional.isPresent()) {
            return;
        }
        GuildInfo guildInfo = guildInfoOptional.get();
        if (guildLogoShop.getLevel() > guildInfo.getLevel()) {
            String levelMessage = ConfigUtil.LOGO_SHOP_CONFIG.getString("levelButton", "${level}").replace("${level}", guildLogoShop.getLevel().toString());
            MessageUtil.sendMessage(player, levelMessage);
            return;
        }
        if (guildInfo.getMoney() < price) {
            MessageUtil.sendMessage(player, ConfigUtil.LOGO_SHOP_CONFIG.getString("noButton"));
            return;
        }
        GuildInfoService.getInstance().subtractMoneyById(guildInfoId, price);
        GuildLogo guildLogo = new GuildLogo();
        guildLogo.setGuildInfoId(guildInfoId);
        guildLogo.setLogoId(guildLogoShop.getId());
        guildLogo.setLogoName(guildLogoShop.getItemStack());
        guildLogo.setUseFlag(false);
        GuildLogoService.getInstance().add(guildLogo);
        MessageUtil.sendMessage(player, ConfigUtil.LOGO_SHOP_CONFIG.getString("buyMsg"));
        GuildLogoShopGui.getInstance().setInventoryDate(gui);
    }
}
