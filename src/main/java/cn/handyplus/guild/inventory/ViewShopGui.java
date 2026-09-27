package cn.handyplus.guild.inventory;

import cn.handyplus.guild.constants.GuiTypeEnum;
import cn.handyplus.guild.constants.ShopBuyLimitTypeEnum;
import cn.handyplus.guild.enter.GuildShop;
import cn.handyplus.guild.hook.PlaceholderApiUtil;
import cn.handyplus.guild.lib.CollUtil;
import cn.handyplus.guild.lib.HandyInventory;
import cn.handyplus.guild.lib.HandyInventoryUtil;
import cn.handyplus.guild.lib.ItemMetaUtil;
import cn.handyplus.guild.lib.ItemStackUtil;
import cn.handyplus.guild.lib.MapUtil;
import cn.handyplus.guild.lib.Page;
import cn.handyplus.guild.lib.StrUtil;
import cn.handyplus.guild.service.GuildShopService;
import cn.handyplus.guild.util.ConfigUtil;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ViewShopGui {
    private final static ViewShopGui INSTANCE = new ViewShopGui();

    private ViewShopGui() {
    }

    public Inventory createGui(Player player) {
        String title = ConfigUtil.SHOP_CONFIG.getString("title");
        String sound = ConfigUtil.SHOP_CONFIG.getString("sound");
        title = PlaceholderApiUtil.set(player, title);
        int size = ConfigUtil.SHOP_CONFIG.getInt("size", 54);
        HandyInventory handyInventory = new HandyInventory(GuiTypeEnum.VIEW_SHOP.getType(), title, size, sound);
        handyInventory.setPlayer(player);
        this.setInventoryDate(handyInventory);
        return handyInventory.getInventory();
    }

    public void setInventoryDate(HandyInventory gui) {
        HandyInventory inventory = gui;
        inventory.setGuiType(GuiTypeEnum.VIEW_SHOP.getType());
        HandyInventoryUtil.refreshInventory(inventory.getInventory());
        ViewShopGui instance = this;
        instance.fillShopItems(gui);
        instance.addNavigationButtons(inventory);
    }

    private Map<String, String> buildShopPlaceholders(GuildShop guildShop) {
        HashMap<String, String> placeholders = MapUtil.newHashMapWithExpectedSize(5);
        Integer number = guildShop.getNumber();
        String unlimitedText = ConfigUtil.SHOP_CONFIG.getString("unlimited", "&a无限制");
        placeholders.put("number", number == 0 ? unlimitedText : number.toString());
        placeholders.put("price", guildShop.getPrice().toString());
        placeholders.put("level", guildShop.getLevel().toString());
        placeholders.put("button", ConfigUtil.SHOP_CONFIG.getString("delButton"));
        ShopBuyLimitTypeEnum shopBuyLimitTypeEnum = ShopBuyLimitTypeEnum.getEnumDefaultOnly(guildShop.getType());
        placeholders.put("buyType", ShopBuyLimitTypeEnum.getTypeName(shopBuyLimitTypeEnum));
        return placeholders;
    }

    public static ViewShopGui getInstance() {
        return INSTANCE;
    }

    private void addNavigationButtons(HandyInventory gui) {
        Map<String, String> map = HandyInventoryUtil.replacePageMap(gui);
        HandyInventoryUtil.setButton(ConfigUtil.SHOP_CONFIG, gui, "nextPage", map);
        HandyInventoryUtil.setButton(ConfigUtil.SHOP_CONFIG, gui, "previousPage", map);
        HandyInventoryUtil.setButton(ConfigUtil.SHOP_CONFIG, gui, "back");
        HandyInventoryUtil.setButton(ConfigUtil.SHOP_CONFIG, gui, "pane");
    }

    private void fillShopItems(HandyInventory gui) {
        HandyInventory inventoryGui = gui;
        Inventory inventory = gui.getInventory();
        Map<Integer, Integer> map = gui.getIntMap();
        List<Integer> list = StrUtil.strToIntList(ConfigUtil.SHOP_CONFIG.getString("logo.index", "20,21,22,23,24,29,30,31,32,33"));
        inventoryGui.setPageSize(list.size());
        Page<GuildShop> page = GuildShopService.getInstance().page(gui.getPageNum(), gui.getPageSize(), Collections.singletonList(0));
        inventoryGui.setPageCount(page.getTotal());
        if (page.getTotal() < 1) {
            return;
        }
        List<GuildShop> records = page.getRecords();
        int index = 0;
        for (GuildShop guildShop : records) {
            Integer slot = list.get(index);
            ++index;
            ItemStack itemStack = ItemStackUtil.itemStackDeserialize(guildShop.getItemStack());
            List<String> lore = ConfigUtil.SHOP_CONFIG.getStringList("logo.view");
            List<String> oldLore = ItemStackUtil.getItemMeta(itemStack).getLore();
            if (CollUtil.isNotEmpty(oldLore)) {
                oldLore.addAll(lore);
                lore = oldLore;
            }
            lore = ItemStackUtil.loreReplaceMap(lore, this.buildShopPlaceholders(guildShop));
            ItemMeta itemMeta = ItemStackUtil.getItemMeta(itemStack);
            ItemMetaUtil.setLore(itemMeta, lore);
            itemStack.setItemMeta(itemMeta);
            inventory.setItem(slot.intValue(), itemStack);
            map.put(slot, guildShop.getId());
        }
    }
}
