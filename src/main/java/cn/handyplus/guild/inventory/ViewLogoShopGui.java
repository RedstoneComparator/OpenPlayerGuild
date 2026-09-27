package cn.handyplus.guild.inventory;

import cn.handyplus.guild.constants.GuiTypeEnum;
import cn.handyplus.guild.enter.GuildLogoShop;
import cn.handyplus.guild.hook.PlaceholderApiUtil;
import cn.handyplus.guild.lib.HandyInventory;
import cn.handyplus.guild.lib.HandyInventoryUtil;
import cn.handyplus.guild.lib.ItemMetaUtil;
import cn.handyplus.guild.lib.ItemStackUtil;
import cn.handyplus.guild.lib.MapUtil;
import cn.handyplus.guild.lib.NumberUtil;
import cn.handyplus.guild.lib.Page;
import cn.handyplus.guild.lib.StrUtil;
import cn.handyplus.guild.service.GuildLogoShopService;
import cn.handyplus.guild.util.ConfigUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ViewLogoShopGui {
    private final static ViewLogoShopGui INSTANCE = new ViewLogoShopGui();

    public static ViewLogoShopGui getInstance() {
        return INSTANCE;
    }

    private ViewLogoShopGui() {
    }

    private Map<String, String> buildLogoPlaceholders(GuildLogoShop guildLogoShop) {
        HashMap<String, String> placeholders = MapUtil.newHashMapWithExpectedSize(4);
        String previousValue = placeholders.put("id", guildLogoShop.getId().toString());
        HashMap<String, String> placeholdersRef = placeholders;
        placeholders.put("price", NumberUtil.format(guildLogoShop.getPrice()));
        placeholdersRef.put("level", String.valueOf(guildLogoShop.getLevel()));
        placeholders.put("defaultLogo", String.valueOf(guildLogoShop.getDefaultLogoFlag()));
        placeholders.put("button", ConfigUtil.LOGO_SHOP_CONFIG.getString("delButton"));
        return placeholdersRef;
    }

    public void setInventoryDate(HandyInventory gui) {
        HandyInventory inventory = gui;
        inventory.setGuiType(GuiTypeEnum.VIEW_LOGO_SHOP.getType());
        HandyInventoryUtil.refreshInventory(inventory.getInventory());
        ViewLogoShopGui instance = this;
        instance.fillLogoItems(gui);
        instance.addNavigationButtons(inventory);
    }

    private void addNavigationButtons(HandyInventory gui) {
        Map<String, String> map = HandyInventoryUtil.replacePageMap(gui);
        HandyInventoryUtil.setButton(ConfigUtil.LOGO_SHOP_CONFIG, gui, "nextPage", map);
        HandyInventoryUtil.setButton(ConfigUtil.LOGO_SHOP_CONFIG, gui, "previousPage", map);
        HandyInventoryUtil.setButton(ConfigUtil.LOGO_SHOP_CONFIG, gui, "back");
        HandyInventoryUtil.setButton(ConfigUtil.LOGO_SHOP_CONFIG, gui, "pane");
    }

    public Inventory createGui(Player player) {
        Object guiValue = ConfigUtil.LOGO_SHOP_CONFIG.getString("title");
        String sound = ConfigUtil.LOGO_SHOP_CONFIG.getString("sound");
        guiValue = PlaceholderApiUtil.set(player, (String)guiValue);
        int size = ConfigUtil.LOGO_SHOP_CONFIG.getInt("size", 54);
        Object handyInventory = guiValue = new HandyInventory(GuiTypeEnum.VIEW_LOGO_SHOP.getType(), (String)guiValue, size, sound);
        ((HandyInventory)guiValue).setPageNum(1);
        ((HandyInventory)handyInventory).setPlayer(player);
        this.setInventoryDate((HandyInventory)handyInventory);
        return ((HandyInventory)handyInventory).getInventory();
    }

    private void fillLogoItems(HandyInventory gui) {
        HandyInventory inventoryGui = gui;
        Inventory inventory = inventoryGui.getInventory();
        Map<Integer, Integer> map = inventoryGui.getIntMap();
        List<Integer> list = StrUtil.strToIntList(ConfigUtil.LOGO_SHOP_CONFIG.getString("logo.index", "20,21,22,23,24,29,30,31,32,33"));
        inventoryGui.setPageSize(list.size());
        Page<GuildLogoShop> page = GuildLogoShopService.getInstance().page(null, inventoryGui.getPageNum(), inventoryGui.getPageSize(), null);
        inventoryGui.setPageCount(page.getTotal());
        if (page.getTotal() < 1) {
            return;
        }
        List<GuildLogoShop> records = page.getRecords();
        int index = 0;
        List<String> loreTemplate = ConfigUtil.LOGO_SHOP_CONFIG.getStringList("logo.view");
        for (GuildLogoShop guildLogoShop : records) {
            Integer slot = list.get(index);
            ++index;
            ItemStack itemStack = ItemStackUtil.itemStackDeserialize(guildLogoShop.getItemStack());
            List<String> lore = ItemStackUtil.loreReplaceMap(loreTemplate, this.buildLogoPlaceholders(guildLogoShop));
            ItemStack itemStackRef = itemStack;
            ItemMeta itemMeta = ItemStackUtil.getItemMeta(itemStackRef);
            ItemMetaUtil.setLore(itemMeta, lore);
            ItemMetaUtil.hideAttributes(itemMeta);
            ItemMetaUtil.hideEnchant(itemMeta);
            itemStackRef.setItemMeta(itemMeta);
            Integer slotRef = slot;
            inventory.setItem(slotRef.intValue(), itemStack);
            map.put(slotRef, guildLogoShop.getId());
        }
    }
}
