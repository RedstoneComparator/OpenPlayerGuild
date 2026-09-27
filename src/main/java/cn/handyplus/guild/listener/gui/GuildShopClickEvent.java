package cn.handyplus.guild.listener.gui;

import cn.handyplus.guild.api.PlayerGuildApi;
import cn.handyplus.guild.constants.GuiTypeEnum;
import cn.handyplus.guild.constants.ShopBuyLimitTypeEnum;
import cn.handyplus.guild.constants.ShopSearchEnum;
import cn.handyplus.guild.enter.GuildInfo;
import cn.handyplus.guild.enter.GuildPlayer;
import cn.handyplus.guild.enter.GuildShop;
import cn.handyplus.guild.enter.GuildShopLog;
import cn.handyplus.guild.inventory.GuildShopGui;
import cn.handyplus.guild.inventory.GuildViewGui;
import cn.handyplus.guild.lib.BaseUtil;
import cn.handyplus.guild.lib.DateUtil;
import cn.handyplus.guild.lib.HandyInventory;
import cn.handyplus.guild.lib.HandyInventoryUtil;
import cn.handyplus.guild.lib.IHandyClickEvent;
import cn.handyplus.guild.lib.ItemStackUtil;
import cn.handyplus.guild.lib.MessageUtil;
import cn.handyplus.guild.lib.PlayerSchedulerUtil;
import cn.handyplus.guild.lib.StrUtil;
import cn.handyplus.guild.service.GuildInfoService;
import cn.handyplus.guild.service.GuildPlayerService;
import cn.handyplus.guild.service.GuildShopLogService;
import cn.handyplus.guild.service.GuildShopService;
import cn.handyplus.guild.util.ConfigUtil;
import cn.handyplus.guild.util.GuildCacheUtil;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class GuildShopClickEvent
implements IHandyClickEvent {
    private static boolean checkBuyLimit(GuildShop guildShop, GuildInfo guildInfo, Player buyer) {
        if (guildShop.getNumber() == 0 || StrUtil.isNotEmpty(guildShop.getPlayerName())) {
            return false;
        }
        ShopBuyLimitTypeEnum shopBuyLimitTypeEnum = ShopBuyLimitTypeEnum.getEnumDefaultOnly(guildShop.getType());
        switch (shopBuyLimitTypeEnum) {
            case ONLY: {
                GuildShopLog guildShopLog;
                GuildShopLogService guildShopLogService = GuildShopLogService.getInstance();
                Optional<GuildShopLog> shopLogOptional = guildShopLogService.findByPlayerUuid(buyer.getUniqueId(), shopBuyLimitTypeEnum.getName(), guildShop.getId());
                if (shopLogOptional.isPresent()) {
                    GuildShopLog guildShopLog2 = shopLogOptional.get();
                    if (guildShop.getNumber() <= guildShopLog2.getNumber()) {
                        return true;
                    }
                    GuildShopLogService.getInstance().addNumber(guildShopLog2.getId(), 1);
                    break;
                }
                GuildShopLog guildShopLog3 = guildShopLog = new GuildShopLog();
                GuildShopLog guildShopLog4 = guildShopLog;
                Player player = buyer;
                GuildShopLog guildShopLog5 = guildShopLog;
                guildShopLog5.setGuildInfoId(guildInfo.getId());
                guildShopLog5.setGuildInfoName(guildInfo.getGuildName());
                guildShopLog.setPlayerName(player.getName());
                guildShopLog4.setPlayerUuid(player.getUniqueId());
                guildShopLog4.setGuildShopId(guildShop.getId());
                guildShopLog3.setNumber(1);
                guildShopLog3.setType(shopBuyLimitTypeEnum.getName());
                GuildShopLog guildShopLog6 = guildShopLog;
                guildShopLog3.setBuyTime(new Date());
                GuildShopLogService.getInstance().add(guildShopLog);
                break;
            }
            case DAY: {
                if (!GuildShopClickEvent.recordAndCheckPeriodLimit(guildShop, guildInfo, buyer, shopBuyLimitTypeEnum, DateUtil.getToday(), DateUtil.getTodayEnd())) break;
                return true;
            }
            case WEEK: {
                if (!GuildShopClickEvent.recordAndCheckPeriodLimit(guildShop, guildInfo, buyer, shopBuyLimitTypeEnum, DateUtil.getMonday(), DateUtil.getSunday())) break;
                return true;
            }
            case MONTH: {
                if (!GuildShopClickEvent.recordAndCheckPeriodLimit(guildShop, guildInfo, buyer, shopBuyLimitTypeEnum, DateUtil.getFirstDayOfMonth(), DateUtil.getLastDayOfMonth())) break;
                return true;
            }
        }
        return false;
    }

    private static synchronized void buyShopItem(Integer shopId, Player player, HandyInventory handyInventory) {
        Optional<GuildShop> shopOptional = GuildShopService.getInstance().findById(shopId);
        if (!shopOptional.isPresent()) {
            GuildShopGui.getInstance().setInventoryDate(handyInventory);
            return;
        }
        GuildShop guildShop = shopOptional.get();
        if (StrUtil.isNotEmpty(guildShop.getPlayerName()) && player.getName().equals(guildShop.getPlayerName())) {
            GuildShopService.getInstance().delById(guildShop.getId());
            ItemStack itemStack = ItemStackUtil.itemStackDeserialize(guildShop.getItemStack());
            ItemStackUtil.addItem(player, itemStack, guildShop.getNumber());
            MessageUtil.sendMessage(player, ConfigUtil.SHOP_CONFIG.getString("noSellMsg"));
            GuildShopGui.getInstance().setInventoryDate(handyInventory);
            return;
        }
        Optional<GuildPlayer> guildPlayerOptional = GuildPlayerService.getInstance().findByPlayerUuid(player.getUniqueId());
        if (!guildPlayerOptional.isPresent()) {
            return;
        }
        GuildPlayer guildPlayer = guildPlayerOptional.get();
        Optional<GuildInfo> guildInfoOptional = GuildInfoService.getInstance().findById(handyInventory.getId());
        if (!guildInfoOptional.isPresent()) {
            return;
        }
        GuildInfo guildInfo = guildInfoOptional.get();
        if (guildShop.getLevel() > guildInfo.getLevel()) {
            String message = ConfigUtil.SHOP_CONFIG.getString("levelButton", "${level}").replace("${level}", guildShop.getLevel().toString());
            MessageUtil.sendMessage(player, message);
            return;
        }
        if (guildPlayer.getMoney() < guildShop.getPrice()) {
            MessageUtil.sendMessage(player, ConfigUtil.SHOP_CONFIG.getString("noButton"));
            return;
        }
        if (GuildShopClickEvent.checkBuyLimit(guildShop, guildInfo, player)) {
            String message = ConfigUtil.SHOP_CONFIG.getString("noLimitButton", "");
            ShopBuyLimitTypeEnum shopBuyLimitTypeEnum = ShopBuyLimitTypeEnum.getEnumDefaultOnly(guildShop.getType());
            MessageUtil.sendMessage(player, message.replace("${buyType}", ShopBuyLimitTypeEnum.getTypeName(shopBuyLimitTypeEnum)));
            return;
        }
        GuildPlayerService.getInstance().subtractMoneyById(guildPlayer.getId(), guildShop.getPrice());
        ItemStack itemStack = ItemStackUtil.itemStackDeserialize(guildShop.getItemStack());
        ItemStackUtil.addItem(player, itemStack, itemStack.getAmount());
        MessageUtil.sendMessage(player, ConfigUtil.SHOP_CONFIG.getString("buyMsg"));
        if (StrUtil.isNotEmpty(guildShop.getPlayerName())) {
            if (guildShop.getNumber() - 1 > 0) {
                GuildShopService.getInstance().subtractNumberById(guildShop.getId(), 1);
            } else {
                GuildShopService.getInstance().delById(guildShop.getId());
            }
            GuildPlayerService.getInstance().addMoneyByPlayerUuid(guildShop.getPlayerUuid(), guildShop.getPrice());
            String message = ConfigUtil.SHOP_CONFIG.getString("sellBuyMsg", "");
            message = message.replace("${player}", guildPlayer.getPlayerName()).replace("${item}", BaseUtil.getDisplayName(itemStack)).replace("${money}", guildShop.getPrice().toString());
            MessageUtil.sendMessage(guildShop.getPlayerName(), message);
        }
        GuildShopGui.getInstance().setInventoryDate(handyInventory);
        GuildCacheUtil.refresh(guildPlayer.getPlayerUuid());
        GuildCacheUtil.refresh(guildShop.getPlayerUuid());
    }

    @Override
    public void rawSlotClick(HandyInventory clickedInventory, InventoryClickEvent clickEvent) {
        int rawSlot = clickEvent.getRawSlot();
        HandyInventory handyInventory = clickedInventory;
        Integer pageNum = handyInventory.getPageNum();
        Integer pageCount = handyInventory.getPageCount();
        Player player = handyInventory.getPlayer();
        Map<Integer, Integer> intMap = handyInventory.getIntMap();
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.SHOP_CONFIG, "back")) {
            Inventory inventory = GuildViewGui.getInstance().createGui(player, clickedInventory.getId());
            clickedInventory.syncOpen(inventory);
            return;
        }
        Map<Integer, String> customButtons = HandyInventoryUtil.getCustomButton(ConfigUtil.SHOP_CONFIG, "custom");
        String command = customButtons.get(rawSlot);
        if (StrUtil.isNotEmpty(command)) {
            PlayerSchedulerUtil.performReplaceCommand(player, command);
            return;
        }
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.SHOP_CONFIG, "search")) {
            String searchType = null;
            if (ShopSearchEnum.ALL.getType().equals(clickedInventory.getSearchType())) {
                searchType = ShopSearchEnum.SYSTEM.getType();
            }
            if (ShopSearchEnum.SYSTEM.getType().equals(clickedInventory.getSearchType())) {
                searchType = ShopSearchEnum.PLAYER.getType();
            }
            if (ShopSearchEnum.PLAYER.getType().equals(clickedInventory.getSearchType())) {
                searchType = ShopSearchEnum.ALL.getType();
            }
            Inventory inventory = GuildShopGui.getInstance().createGui(player, clickedInventory.getId(), searchType);
            clickedInventory.syncOpen(inventory);
            return;
        }
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.SHOP_CONFIG, "sell")) {
            PlayerGuildApi.startGuildShopSell(player);
            clickedInventory.syncClose();
            return;
        }
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.SHOP_CONFIG, "previousPage")) {
            if (pageNum > 1) {
                HandyInventory handyInventory2 = clickedInventory;
                handyInventory2.setPageNum(handyInventory2.getPageNum() - 1);
                GuildShopGui.getInstance().setInventoryDate(clickedInventory);
            }
            return;
        }
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.SHOP_CONFIG, "nextPage")) {
            if (pageNum + 1 <= pageCount) {
                HandyInventory handyInventory3 = clickedInventory;
                handyInventory3.setPageNum(handyInventory3.getPageNum() + 1);
                GuildShopGui.getInstance().setInventoryDate(clickedInventory);
            }
            return;
        }
        Integer shopId = intMap.get(rawSlot);
        if (shopId == null) {
            return;
        }
        GuildShopClickEvent.buyShopItem(shopId, player, clickedInventory);
    }

    @Override
    public String guiType() {
        return GuiTypeEnum.GUILD_SHOP.getType();
    }

    @Override
    public boolean isAsync() {
        return true;
    }

    private static boolean recordAndCheckPeriodLimit(GuildShop guildShop, GuildInfo guildInfo, Player player, ShopBuyLimitTypeEnum limitType, Date startDate, Date endDate) {
        int count = GuildShopLogService.getInstance().findCountByPlayerUuid(player.getUniqueId(), limitType.getName(), guildShop.getId(), startDate, endDate);
        if (guildShop.getNumber() <= count) {
            return true;
        }
        GuildShopLog guildShopLog = new GuildShopLog();
        guildShopLog.setGuildInfoId(guildInfo.getId());
        guildShopLog.setGuildInfoName(guildInfo.getGuildName());
        guildShopLog.setPlayerName(player.getName());
        guildShopLog.setPlayerUuid(player.getUniqueId());
        guildShopLog.setGuildShopId(guildShop.getId());
        guildShopLog.setNumber(1);
        guildShopLog.setType(limitType.getName());
        guildShopLog.setBuyTime(new Date());
        GuildShopLogService.getInstance().add(guildShopLog);
        return false;
    }
}
