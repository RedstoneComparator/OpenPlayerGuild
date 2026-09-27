package cn.handyplus.guild.inventory;

import cn.handyplus.guild.constants.GuiTypeEnum;
import cn.handyplus.guild.constants.ShopBuyLimitTypeEnum;
import cn.handyplus.guild.constants.ShopSearchEnum;
import cn.handyplus.guild.enter.GuildInfo;
import cn.handyplus.guild.enter.GuildPlayer;
import cn.handyplus.guild.enter.GuildShop;
import cn.handyplus.guild.enter.GuildShopLog;
import cn.handyplus.guild.hook.PlaceholderApiUtil;
import cn.handyplus.guild.lib.CollUtil;
import cn.handyplus.guild.lib.DateUtil;
import cn.handyplus.guild.lib.HandyInventory;
import cn.handyplus.guild.lib.HandyInventoryUtil;
import cn.handyplus.guild.lib.ItemMetaUtil;
import cn.handyplus.guild.lib.ItemStackUtil;
import cn.handyplus.guild.lib.MapUtil;
import cn.handyplus.guild.lib.Page;
import cn.handyplus.guild.lib.StrUtil;
import cn.handyplus.guild.service.GuildInfoService;
import cn.handyplus.guild.service.GuildPlayerService;
import cn.handyplus.guild.service.GuildShopLogService;
import cn.handyplus.guild.service.GuildShopService;
import cn.handyplus.guild.util.ConfigUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GuildShopGui {
    private final static GuildShopGui INSTANCE = new GuildShopGui();

    private void fillShopItems(HandyInventory gui) {
        Inventory inventory = gui.getInventory();
        Player player = gui.getPlayer();
        Map<Integer, Integer> intMap = gui.getIntMap();
        String searchType = gui.getSearchType();
        List<Integer> slotList = StrUtil.strToIntList(
                ConfigUtil.SHOP_CONFIG.getString("logo.index",
                        "20,21,22,23,24,29,30,31,32,33"));
        gui.setPageSize(slotList.size());
        Optional<GuildPlayer> optional = GuildPlayerService.getInstance().findByPlayerUuid(player.getUniqueId());
        if (!optional.isPresent()) {
            return;
        }
        GuildPlayer guildPlayer = optional.get();
        gui.setObj(guildPlayer);
        ArrayList<Integer> guildInfoIds = new ArrayList<Integer>();
        if (ShopSearchEnum.ALL.getType().equals(searchType)) {
            guildInfoIds.add(0);
            guildInfoIds.add(gui.getId());
        }
        if (ShopSearchEnum.SYSTEM.getType().equals(searchType)) {
            guildInfoIds.add(0);
        }
        if (ShopSearchEnum.PLAYER.getType().equals(searchType)) {
            guildInfoIds.add(gui.getId());
        }
        Page page = GuildShopService.getInstance().page(gui.getPageNum(), gui.getPageSize(), guildInfoIds);
        gui.setPageCount(page.getTotal());
        if (page.getTotal() < 1) {
            return;
        }
        Optional<GuildInfo> optional2 = GuildInfoService.getInstance().findById(gui.getId());
        if (!optional2.isPresent()) {
            return;
        }
        Integer guildLevel = optional2.get().getLevel();
        Integer money = guildPlayer.getMoney();
        List<GuildShop> records = page.getRecords();
        int index = 0;
        for (GuildShop guildShop : records) {
            Integer slot = slotList.get(index);
            ++index;
            ItemStack itemStack = ItemStackUtil.itemStackDeserialize(guildShop.getItemStack());
            List<String> lore = ConfigUtil.SHOP_CONFIG.getStringList("logo.guild");
            List<String> oldLore = ItemStackUtil.getItemMeta(itemStack).getLore();
            if (CollUtil.isNotEmpty(oldLore)) {
                oldLore.addAll(lore);
                lore = oldLore;
            }
            if (StrUtil.isNotEmpty(guildShop.getPlayerName())) {
                List<String> extraLore = ConfigUtil.SHOP_CONFIG.getStringList("logo.sell");
                lore.addAll(extraLore);
            }
            List<String> replacedLore = ItemStackUtil.loreReplaceMap(lore,
                    this.buildShopPlaceholders(money, guildShop, guildLevel, player));
            ItemMeta itemMeta = ItemStackUtil.getItemMeta(itemStack);
            ItemMetaUtil.setLore(itemMeta, replacedLore);
            itemStack.setItemMeta(itemMeta);
            inventory.setItem(slot.intValue(), itemStack);
            intMap.put(slot, guildShop.getId());
        }
    }

    public Inventory createGui(Player player, Integer guildId, String searchType) {
        String title = ConfigUtil.SHOP_CONFIG.getString("title");
        String sound = ConfigUtil.SHOP_CONFIG.getString("sound");
        if (ShopSearchEnum.SYSTEM.getType().equals(searchType)) {
            title = ConfigUtil.SHOP_CONFIG.getString("systemTitle");
        }
        if (ShopSearchEnum.PLAYER.getType().equals(searchType)) {
            title = ConfigUtil.SHOP_CONFIG.getString("playerTitle");
        }
        title = PlaceholderApiUtil.set(player, title);
        int size = ConfigUtil.SHOP_CONFIG.getInt("size", 54);
        HandyInventory handyInventory = new HandyInventory(GuiTypeEnum.GUILD_SHOP.getType(), title, size, sound);
        handyInventory.setPlayer(player);
        handyInventory.setId(guildId);
        handyInventory.setSearchType(searchType);
        this.setInventoryDate(handyInventory);
        return handyInventory.getInventory();
    }

    private Map<String, String> buildSearchButtonMap(HandyInventory gui) {
        HashMap<String, String> placeholders = MapUtil.newHashMapWithExpectedSize(2);
        String searchType = gui.getSearchType();
        String systemText = ConfigUtil.SHOP_CONFIG.getString("search.system");
        String playerText = ConfigUtil.SHOP_CONFIG.getString("search.player");
        String allText = ConfigUtil.SHOP_CONFIG.getString("search.all");
        if (ShopSearchEnum.ALL.getType().equals(searchType)) {
            placeholders.put("type", allText);
            placeholders.put("nextType", systemText);
        }
        if (ShopSearchEnum.SYSTEM.getType().equals(searchType)) {
            placeholders.put("type", systemText);
            placeholders.put("nextType", playerText);
        }
        if (ShopSearchEnum.PLAYER.getType().equals(searchType)) {
            placeholders.put("type", playerText);
            placeholders.put("nextType", allText);
        }
        return placeholders;
    }

    private Map<String, String> buildShopPlaceholders(Integer money, GuildShop guildShop, Integer guildLevel, Player player) {
        Integer price = guildShop.getPrice();
        Integer level = guildShop.getLevel();
        Integer number = guildShop.getNumber();
        HashMap<String, String> placeholders = MapUtil.newHashMapWithExpectedSize(6);
        String buttonText = ConfigUtil.SHOP_CONFIG.getString("noButton");
        if (money >= price) {
            if (number == 0) {
                buttonText = ConfigUtil.SHOP_CONFIG.getString("yesButton");
            } else {
                buttonText = this.buildBuyLimitText(guildShop, player);
            }
        }
        if (level > guildLevel) {
            buttonText = ConfigUtil.SHOP_CONFIG.getString("levelButton",
                    "${level}")
                    .replace("${level}", level.toString());
        }
        if (StrUtil.isNotEmpty(guildShop.getPlayerName()) && player.getName().equals(guildShop.getPlayerName())) {
            buttonText = ConfigUtil.SHOP_CONFIG.getString("noSell", "");
        }
        String buyLimit = ConfigUtil.SHOP_CONFIG.getString("unlimited",
                "&a无限制");
        placeholders.put("price", price.toString());
        placeholders.put("level", level.toString());
        placeholders.put("money", money.toString());
        placeholders.put("number", number == 0 ? buyLimit : number.toString());
        placeholders.put("button", buttonText);
        placeholders.put("playerName", guildShop.getPlayerName());
        ShopBuyLimitTypeEnum shopBuyLimitTypeEnum = ShopBuyLimitTypeEnum.getEnumDefaultOnly(guildShop.getType());
        placeholders.put("buyType", ShopBuyLimitTypeEnum.getTypeName(shopBuyLimitTypeEnum));
        return placeholders;
    }

    private String buildBuyLimitText(GuildShop guildShop, Player player) {
        int bought = 0;
        ShopBuyLimitTypeEnum shopBuyLimitTypeEnum = ShopBuyLimitTypeEnum.getEnumDefaultOnly(guildShop.getType());
        switch (shopBuyLimitTypeEnum) {
            case ONLY: {
                Optional<GuildShopLog> optional = GuildShopLogService.getInstance()
                        .findByPlayerUuid(player.getUniqueId(), shopBuyLimitTypeEnum.getName(), guildShop.getId());
                bought = optional.isPresent() ? optional.get().getNumber() : 0;
                break;
            }
            case DAY: {
                bought = GuildShopLogService.getInstance().findCountByPlayerUuid(player.getUniqueId(),
                        shopBuyLimitTypeEnum.getName(), guildShop.getId(), DateUtil.getToday(), DateUtil.getTodayEnd());
                break;
            }
            case WEEK: {
                bought = GuildShopLogService.getInstance().findCountByPlayerUuid(player.getUniqueId(),
                        shopBuyLimitTypeEnum.getName(), guildShop.getId(), DateUtil.getMonday(), DateUtil.getSunday());
                break;
            }
            case MONTH: {
                bought = GuildShopLogService.getInstance().findCountByPlayerUuid(player.getUniqueId(),
                        shopBuyLimitTypeEnum.getName(), guildShop.getId(), DateUtil.getFirstDayOfMonth(),
                        DateUtil.getLastDayOfMonth());
                break;
            }
        }
        if (guildShop.getNumber() > bought) {
            return ConfigUtil.SHOP_CONFIG.getString("limitButton", "")
                    .replace("${number}", guildShop.getNumber() - bought + "")
                    .replace("${buyType}",
                            ShopBuyLimitTypeEnum.getTypeName(shopBuyLimitTypeEnum));
        }
        return ConfigUtil.SHOP_CONFIG.getString("noLimitButton", "")
                .replace("${buyType}",
                        ShopBuyLimitTypeEnum.getTypeName(shopBuyLimitTypeEnum));
    }

    private void addNavigationButtons(HandyInventory gui) {
        Object guiObj = gui.getObj();
        if (!(guiObj instanceof GuildPlayer)) {
            return;
        }
        GuildPlayer guildPlayer = (GuildPlayer)guiObj;
        Map<String, String> pageMap = HandyInventoryUtil.replacePageMap(gui);
        HandyInventoryUtil.setButton(ConfigUtil.SHOP_CONFIG, gui, "nextPage", pageMap);
        HandyInventoryUtil.setButton(ConfigUtil.SHOP_CONFIG, gui, "previousPage", pageMap);
        HandyInventoryUtil.setButton(ConfigUtil.SHOP_CONFIG, gui, "back");
        HandyInventoryUtil.setButton(ConfigUtil.SHOP_CONFIG, gui, "pane");
        HandyInventoryUtil.setButton(ConfigUtil.SHOP_CONFIG, gui, "search",
                this.buildSearchButtonMap(gui));
        int roleId = ConfigUtil.SHOP_CONFIG.getInt("sell.roleLimit", 2);
        if (guildPlayer.getRoleId() <= roleId) {
            HandyInventoryUtil.setButton(ConfigUtil.SHOP_CONFIG, gui, "sell");
        }
        HandyInventoryUtil.setCustomButton(ConfigUtil.SHOP_CONFIG, gui, "custom");
    }

    private GuildShopGui() {
    }

    public static GuildShopGui getInstance() {
        return INSTANCE;
    }

    public void setInventoryDate(HandyInventory gui) {
        gui.setGuiType(GuiTypeEnum.GUILD_SHOP.getType());
        HandyInventoryUtil.refreshInventory(gui.getInventory());
        this.fillShopItems(gui);
        this.addNavigationButtons(gui);
    }
}
