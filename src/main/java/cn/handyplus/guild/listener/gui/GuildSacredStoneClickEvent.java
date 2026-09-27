package cn.handyplus.guild.listener.gui;

import cn.handyplus.guild.constants.GuiTypeEnum;
import cn.handyplus.guild.constants.GuildRoleEnum;
import cn.handyplus.guild.enter.GuildBuff;
import cn.handyplus.guild.enter.GuildInfo;
import cn.handyplus.guild.enter.GuildPlayer;
import cn.handyplus.guild.enter.GuildPlayerPickaxe;
import cn.handyplus.guild.hook.PlaceholderApiUtil;
import cn.handyplus.guild.inventory.GuildSacredStoneGui;
import cn.handyplus.guild.inventory.GuildViewGui;
import cn.handyplus.guild.lib.BaseUtil;
import cn.handyplus.guild.lib.DateUtil;
import cn.handyplus.guild.lib.FormulaUtil;
import cn.handyplus.guild.lib.HandyConfigUtil;
import cn.handyplus.guild.lib.HandyInventory;
import cn.handyplus.guild.lib.HandyInventoryUtil;
import cn.handyplus.guild.lib.IHandyClickEvent;
import cn.handyplus.guild.lib.MapUtil;
import cn.handyplus.guild.lib.MessageUtil;
import cn.handyplus.guild.lib.PlayerSchedulerUtil;
import cn.handyplus.guild.lib.StrUtil;
import cn.handyplus.guild.service.GuildBuffService;
import cn.handyplus.guild.service.GuildInfoService;
import cn.handyplus.guild.service.GuildPlayerPickaxeService;
import cn.handyplus.guild.service.GuildPlayerService;
import cn.handyplus.guild.util.BuffManageUtil;
import cn.handyplus.guild.util.ConfigUtil;
import cn.handyplus.guild.util.GuildCacheUtil;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class GuildSacredStoneClickEvent
implements IHandyClickEvent {
    private synchronized void buyPickaxe(String pickaxeType, HandyInventory inventory) {
        Player player = inventory.getPlayer();
        Optional<GuildPlayer> guildPlayerOptional = GuildPlayerService.getInstance().findByPlayerUuid(player.getUniqueId());
        if (!guildPlayerOptional.isPresent()) {
            return;
        }
        GuildPlayer guildPlayer = guildPlayerOptional.get();
        if (guildPlayer.getPickaxeTime() != null) {
            String lastPickaxeDate = DateUtil.format(guildPlayer.getPickaxeTime(), "yyyy-MM-dd");
            if (DateUtil.format(new Date(), "yyyy-MM-dd").equalsIgnoreCase(lastPickaxeDate)) {
                return;
            }
        }
        int price = ConfigUtil.SACRED_STONE_CONFIG.getInt(new StringBuilder().insert(0, pickaxeType).append(".price").toString(), 0);
        if (guildPlayer.getMoney() < price) {
            String notMoneyMessage = ConfigUtil.SACRED_STONE_CONFIG.getString("notMoneyExcavated");
            MessageUtil.sendMessage(player, notMoneyMessage);
            return;
        }
        GuildPlayerService.getInstance().subtractMoneyById(guildPlayer.getId(), price);
        this.giveExcavationReward(pickaxeType, guildPlayer, player);
        GuildSacredStoneGui.getInstance().setInventoryDate(inventory);
    }

    private synchronized void upgradeSacredStoneLevel(HandyInventory clickedInventory) {
        HandyInventory handyInventory = clickedInventory;
        Integer guildId = handyInventory.getId();
        Player player = handyInventory.getPlayer();
        Optional<GuildPlayer> guildPlayerOptional = GuildPlayerService.getInstance().findByPlayerUuid(player.getUniqueId());
        if (!guildPlayerOptional.isPresent()) {
            return;
        }
        if (guildPlayerOptional.get().getRoleId() > GuildRoleEnum.TWO.getRoleId()) {
            MessageUtil.sendMessage(player, BaseUtil.getLangMsg("noPermission"));
            return;
        }
        Optional<GuildInfo> guildInfoOptional = GuildInfoService.getInstance().findById(guildId);
        if (!guildInfoOptional.isPresent()) {
            return;
        }
        GuildInfo guildInfo = guildInfoOptional.get();
        Integer sacredStoneLevel = guildInfo.getSacredStoneLevel();
        Integer guildMoney = guildInfo.getMoney();
        int nextLevelPrice = ConfigUtil.SACRED_STONE_CONFIG.getInt(sacredStoneLevel + 1 + ".price", -1);
        if (nextLevelPrice == -1) {
            MessageUtil.sendMessage(player, ConfigUtil.SACRED_STONE_CONFIG.getString("maxButton"));
            return;
        }
        if (guildMoney < nextLevelPrice) {
            MessageUtil.sendMessage(player, ConfigUtil.SACRED_STONE_CONFIG.getString("noButton"));
            return;
        }
        GuildInfoService.getInstance().subtractMoneyById(guildId, nextLevelPrice);
        GuildInfoService.getInstance().addSacredStoneLevelById(guildId, 1);
        MessageUtil.sendMessage(player, ConfigUtil.SACRED_STONE_CONFIG.getString("succeedMsg"));
        GuildSacredStoneGui.getInstance().setInventoryDate(clickedInventory);
    }

    @Override
    public boolean isAsync() {
        return true;
    }

    private synchronized void upgradeBuff(String buffType, HandyInventory clickedInventory) {
        HandyInventory handyInventory = clickedInventory;
        Integer guildId = handyInventory.getId();
        Player player = handyInventory.getPlayer();
        Optional<GuildPlayer> guildPlayerOptional = GuildPlayerService.getInstance().findByPlayerUuid(player.getUniqueId());
        if (!guildPlayerOptional.isPresent()) {
            return;
        }
        if (guildPlayerOptional.get().getRoleId() > GuildRoleEnum.ONE.getRoleId()) {
            MessageUtil.sendMessage(player, BaseUtil.getLangMsg("noPermission"));
            return;
        }
        Optional<GuildInfo> guildInfoOptional = GuildInfoService.getInstance().findById(guildId);
        if (!guildInfoOptional.isPresent()) {
            return;
        }
        GuildInfo guildInfo = guildInfoOptional.get();
        Map<String, GuildBuff> buffMap = GuildBuffService.getInstance().findByGuildId(guildId);
        int nextLevel = buffMap.get(buffType) != null ? buffMap.get(buffType).getLevel() + 1 : 1;
        int maxLevel = ConfigUtil.SACRED_STONE_CONFIG.getInt(new StringBuilder().insert(0, "buff.").append(buffType).append(".maxLevel").toString());
        if (nextLevel > maxLevel) {
            MessageUtil.sendMessage(player, ConfigUtil.SACRED_STONE_CONFIG.getString("maxLevelMsg"));
            return;
        }
        String priceFormula = ConfigUtil.SACRED_STONE_CONFIG.getString(new StringBuilder().insert(0, "buff.").append(buffType).append(".price").toString());
        priceFormula = PlaceholderApiUtil.set(player, priceFormula);
        Integer price = FormulaUtil.evaluateFormulaToInt(priceFormula, MapUtil.of("level", String.valueOf(nextLevel)));
        if (guildInfo.getOre() < price) {
            MessageUtil.sendMessage(player, ConfigUtil.SACRED_STONE_CONFIG.getString("noOre"));
            return;
        }
        GuildBuff currentBuff = buffMap.get(buffType);
        if (currentBuff == null) {
            GuildBuff newBuff = new GuildBuff();
            newBuff.setGuildInfoId(guildId);
            newBuff.setBuffType(buffType);
            newBuff.setLevel(1);
            GuildBuffService.getInstance().add(newBuff);
        } else {
            GuildBuffService.getInstance().addLevelById(currentBuff.getId(), 1);
        }
        GuildInfoService.getInstance().subtractOreById(guildId, price);
        GuildSacredStoneGui.getInstance().setInventoryDate(clickedInventory);
        BuffManageUtil.refreshCallBuffEvent(guildId);
    }

    @Override
    public void rawSlotClick(HandyInventory handyInventory, InventoryClickEvent clickEvent) {
        int rawSlot = clickEvent.getRawSlot();
        Player player = handyInventory.getPlayer();
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.SACRED_STONE_CONFIG, "back")) {
            Inventory inventory = GuildViewGui.getInstance().createGui(player, handyInventory.getId());
            handyInventory.syncOpen(inventory);
            return;
        }
        Map<Integer, String> customButtons = HandyInventoryUtil.getCustomButton(ConfigUtil.SACRED_STONE_CONFIG, "custom");
        String command = customButtons.get(rawSlot);
        if (StrUtil.isNotEmpty(command)) {
            PlayerSchedulerUtil.performReplaceCommand(player, command);
            return;
        }
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.SACRED_STONE_CONFIG, "info")) {
            this.upgradeSacredStoneLevel(handyInventory);
            return;
        }
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.SACRED_STONE_CONFIG, "woodenPickaxe")) {
            this.buyPickaxe("woodenPickaxe", handyInventory);
            return;
        }
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.SACRED_STONE_CONFIG, "ironPickaxe")) {
            this.buyPickaxe("ironPickaxe", handyInventory);
            return;
        }
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.SACRED_STONE_CONFIG, "goldenPickaxe")) {
            this.buyPickaxe("goldenPickaxe", handyInventory);
            return;
        }
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.SACRED_STONE_CONFIG, "diamondPickaxe")) {
            this.buyPickaxe("diamondPickaxe", handyInventory);
            return;
        }
        for (String buffType : HandyConfigUtil.getChildMap(ConfigUtil.SACRED_STONE_CONFIG, "buff").keySet()) {
            if (!StrUtil.strToIntList(ConfigUtil.SACRED_STONE_CONFIG.getString(new StringBuilder().insert(0, "buff.").append(buffType).append(".index").toString())).contains(rawSlot)) continue;
            this.upgradeBuff(buffType, handyInventory);
            return;
        }
    }

    private void giveExcavationReward(String pickaxeType, GuildPlayer guildPlayer, Player player) {
        Date date = new Date();
        List<Integer> oreAmounts = StrUtil.strToIntList(ConfigUtil.SACRED_STONE_CONFIG.getString(new StringBuilder().insert(0, pickaxeType).append(".ore").toString(), ""));
        if (oreAmounts.size() < 2) {
            return;
        }
        int minAmount = Math.min(oreAmounts.get(0), oreAmounts.get(1));
        int randomAmount = Math.max(oreAmounts.get(0), oreAmounts.get(1));
        randomAmount = ThreadLocalRandom.current().nextInt(randomAmount - minAmount + 1) + minAmount;
        GuildPlayerService.getInstance().setPickaxeTime(guildPlayer.getPlayerUuid(), date, randomAmount);
        GuildPlayerPickaxe guildPlayerPickaxe = new GuildPlayerPickaxe();
        guildPlayerPickaxe.setGuildInfoId(guildPlayer.getGuildInfoId());
        guildPlayerPickaxe.setPlayerName(guildPlayer.getPlayerName());
        guildPlayerPickaxe.setPlayerUuid(guildPlayer.getPlayerUuid());
        guildPlayerPickaxe.setPickaxeTime(date);
        guildPlayerPickaxe.setType(pickaxeType);
        guildPlayerPickaxe.setRewards(randomAmount);
        GuildPlayerPickaxeService.getInstance().add(guildPlayerPickaxe);
        GuildInfoService.getInstance().addOreById(guildPlayer.getGuildInfoId(), randomAmount);
        pickaxeType = ConfigUtil.SACRED_STONE_CONFIG.getString("excavationSuccess", "");
        pickaxeType = pickaxeType.replace("${ore}", String.valueOf(randomAmount));
        MessageUtil.sendMessage(player, pickaxeType);
        GuildCacheUtil.refresh(guildPlayer.getPlayerUuid());
    }

    @Override
    public String guiType() {
        return GuiTypeEnum.GUILD_SACRED_STONE.getType();
    }
}
