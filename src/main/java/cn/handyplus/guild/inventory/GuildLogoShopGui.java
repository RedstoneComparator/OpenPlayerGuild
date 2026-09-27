package cn.handyplus.guild.inventory;

import cn.handyplus.guild.constants.GuiTypeEnum;
import cn.handyplus.guild.enter.GuildInfo;
import cn.handyplus.guild.enter.GuildLogo;
import cn.handyplus.guild.enter.GuildLogoShop;
import cn.handyplus.guild.hook.PlaceholderApiUtil;
import cn.handyplus.guild.lib.HandyInventory;
import cn.handyplus.guild.lib.HandyInventoryUtil;
import cn.handyplus.guild.lib.ItemMetaUtil;
import cn.handyplus.guild.lib.ItemStackUtil;
import cn.handyplus.guild.lib.MapUtil;
import cn.handyplus.guild.lib.Page;
import cn.handyplus.guild.lib.StrUtil;
import cn.handyplus.guild.service.GuildInfoService;
import cn.handyplus.guild.service.GuildLogoService;
import cn.handyplus.guild.service.GuildLogoShopService;
import cn.handyplus.guild.util.ConfigUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GuildLogoShopGui {
    private final static GuildLogoShopGui INSTANCE = new GuildLogoShopGui();

    public static GuildLogoShopGui getInstance() {
        return INSTANCE;
    }

    public void setInventoryDate(HandyInventory inventory) {
        HandyInventory handyInventory = inventory;
        handyInventory.setGuiType(GuiTypeEnum.GUILD_LOGO_SHOP.getType());
        HandyInventoryUtil.refreshInventory(handyInventory.getInventory());
        GuildLogoShopGui guildLogoShopGui = this;
        guildLogoShopGui.fillShopItems(inventory);
        guildLogoShopGui.setPageButtons(handyInventory);
    }

    public Inventory createGui(Player player, Integer guildId) {
        String title = ConfigUtil.LOGO_SHOP_CONFIG.getString("title");
        String sound = ConfigUtil.LOGO_SHOP_CONFIG.getString("sound");
        title = PlaceholderApiUtil.set(player, title);
        int size = ConfigUtil.LOGO_SHOP_CONFIG.getInt("size", 54);
        HandyInventory handyInventory = new HandyInventory(GuiTypeEnum.GUILD_LOGO_SHOP.getType(), title, size, sound);
        handyInventory.setPlayer(player);
        handyInventory.setId(guildId);
        this.setInventoryDate(handyInventory);
        return handyInventory.getInventory();
    }

    private void fillShopItems(HandyInventory guiInventory) {
        HandyInventory handyInventory = guiInventory;
        Inventory inventory = guiInventory.getInventory();
        Integer guildId = guiInventory.getId();
        Map<Integer, Integer> intMap = guiInventory.getIntMap();
        List<Integer> slots = StrUtil.strToIntList(ConfigUtil.LOGO_SHOP_CONFIG.getString("logo.index", "20,21,22,23,24,29,30,31,32,33"));
        handyInventory.setPageSize(slots.size());
        Optional<GuildInfo> guildInfoOptional = GuildInfoService.getInstance().findById(guildId);
        if (!guildInfoOptional.isPresent()) {
            return;
        }
        GuildInfo guildInfo = guildInfoOptional.get();
        Integer money = guildInfo.getMoney();
        List<Integer> logoIds = GuildLogoService.getInstance().list(guildId).stream().map(GuildLogo::getLogoId).collect(Collectors.toList());
        Page<GuildLogoShop> page = GuildLogoShopService.getInstance().page(logoIds, guiInventory.getPageNum(), guiInventory.getPageSize(), false);
        handyInventory.setPageCount(page.getTotal());
        if (page.getTotal() < 1) {
            return;
        }
        List<GuildLogoShop> records = page.getRecords();
        int index = 0;
        List<String> loreTemplate = ConfigUtil.LOGO_SHOP_CONFIG.getStringList("logo.guild");
        for (GuildLogoShop guildLogoShop : records) {
            Integer slot = slots.get(index);
            ++index;
            ItemStack itemStack = ItemStackUtil.itemStackDeserialize(guildLogoShop.getItemStack());
            List<String> lore = ItemStackUtil.loreReplaceMap(loreTemplate, this.getPriceMap(money, guildLogoShop, guildInfo));
            ItemMeta itemMeta = ItemStackUtil.getItemMeta(itemStack);
            ItemMetaUtil.setLore(itemMeta, lore);
            ItemMetaUtil.hideAttributes(itemMeta);
            ItemMetaUtil.hideEnchant(itemMeta);
            itemStack.setItemMeta(itemMeta);
            inventory.setItem(slot.intValue(), itemStack);
            intMap.put(slot, guildLogoShop.getId());
        }
    }

    private GuildLogoShopGui() {
    }

    private void setPageButtons(HandyInventory inventory) {
        Map<String, String> pageMap = HandyInventoryUtil.replacePageMap(inventory);
        HandyInventoryUtil.setButton(ConfigUtil.LOGO_SHOP_CONFIG, inventory, "nextPage", pageMap);
        HandyInventoryUtil.setButton(ConfigUtil.LOGO_SHOP_CONFIG, inventory, "previousPage", pageMap);
        HandyInventoryUtil.setButton(ConfigUtil.LOGO_SHOP_CONFIG, inventory, "back");
        HandyInventoryUtil.setButton(ConfigUtil.LOGO_SHOP_CONFIG, inventory, "pane");
    }

    private Map<String, String> getPriceMap(Integer money, GuildLogoShop logoShop, GuildInfo guildInfo) {
        HashMap<String, String> placeholders = MapUtil.newHashMapWithExpectedSize(4);
        Integer price = logoShop.getPrice();
        Integer level = logoShop.getLevel();
        String button = ConfigUtil.LOGO_SHOP_CONFIG.getString("noButton");
        if (money >= price) {
            button = ConfigUtil.LOGO_SHOP_CONFIG.getString("yesButton");
        }
        if (level > guildInfo.getLevel()) {
            button = ConfigUtil.LOGO_SHOP_CONFIG.getString("levelButton", "${level}").replace("${level}", level.toString());
        }
        placeholders.put("price", price.toString());
        placeholders.put("level", level.toString());
        placeholders.put("money", money.toString());
        placeholders.put("button", button);
        return placeholders;
    }
}
