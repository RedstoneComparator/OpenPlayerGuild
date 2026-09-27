package cn.handyplus.guild.inventory;

import cn.handyplus.guild.constants.GuiTypeEnum;
import cn.handyplus.guild.enter.GuildBuff;
import cn.handyplus.guild.enter.GuildInfo;
import cn.handyplus.guild.enter.GuildPlayer;
import cn.handyplus.guild.hook.PlaceholderApiUtil;
import cn.handyplus.guild.lib.CollUtil;
import cn.handyplus.guild.lib.DateUtil;
import cn.handyplus.guild.lib.FormulaUtil;
import cn.handyplus.guild.lib.HandyConfigUtil;
import cn.handyplus.guild.lib.HandyInventory;
import cn.handyplus.guild.lib.HandyInventoryUtil;
import cn.handyplus.guild.lib.ItemStackUtil;
import cn.handyplus.guild.lib.MapUtil;
import cn.handyplus.guild.lib.StrUtil;
import cn.handyplus.guild.service.GuildBuffService;
import cn.handyplus.guild.service.GuildInfoService;
import cn.handyplus.guild.service.GuildPlayerService;
import cn.handyplus.guild.util.ConfigUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class GuildSacredStoneGui {
    private final static GuildSacredStoneGui INSTANCE = new GuildSacredStoneGui();

    public Inventory createGui(Player player, Integer guildId) {
        String title = ConfigUtil.SACRED_STONE_CONFIG.getString("title");
        String sound = ConfigUtil.SACRED_STONE_CONFIG.getString("sound");
        title = PlaceholderApiUtil.set(player, title);
        int size = ConfigUtil.SACRED_STONE_CONFIG.getInt("size", 54);
        HandyInventory handyInventory = new HandyInventory(GuiTypeEnum.GUILD_SACRED_STONE.getType(), title, size, sound);
        handyInventory.setPlayer(player);
        handyInventory.setId(guildId);
        this.setInventoryDate(handyInventory);
        return handyInventory.getInventory();
    }

    public void setInventoryDate(HandyInventory gui) {
        HandyInventory inventory = gui;
        inventory.setGuiType(GuiTypeEnum.GUILD_SACRED_STONE.getType());
        HandyInventoryUtil.refreshInventory(inventory.getInventory());
        this.fillGui(inventory);
    }

    private GuildSacredStoneGui() {
    }

    private Map<String, String> buildInfoPlaceholders(GuildInfo info) {
        HashMap<String, String> placeholders = MapUtil.newHashMapWithExpectedSize(9);
        GuildInfo guildInfo = info;
        Integer sacredStoneLevel = guildInfo.getSacredStoneLevel();
        Integer money = guildInfo.getMoney();
        String buttonText = ConfigUtil.SACRED_STONE_CONFIG.getString("noButton");
        double nowValue = ConfigUtil.SACRED_STONE_CONFIG.getDouble(sacredStoneLevel + ".value", 1.0);
        int nextPrice = ConfigUtil.SACRED_STONE_CONFIG.getInt(sacredStoneLevel + 1 + ".price", -1);
        double nextValue = ConfigUtil.SACRED_STONE_CONFIG.getDouble(sacredStoneLevel + 1 + ".value", nowValue);
        if (nextPrice == -1) {
            buttonText = ConfigUtil.SACRED_STONE_CONFIG.getString("maxButton");
        } else if (money >= nextPrice) {
            buttonText = ConfigUtil.SACRED_STONE_CONFIG.getString("yesButton");
        }
        placeholders.put("price", String.valueOf(nextPrice));
        placeholders.put("money", money.toString());
        placeholders.put("nowLevel", sacredStoneLevel.toString());
        placeholders.put("nextLevel", String.valueOf(sacredStoneLevel + 1));
        placeholders.put("nowValue", String.valueOf(nowValue));
        placeholders.put("nextValue", String.valueOf(nextValue));
        placeholders.put("button", buttonText);
        placeholders.put("time", ConfigUtil.SACRED_STONE_CONFIG.getString("time"));
        String stoneName = ConfigUtil.SACRED_STONE_CONFIG.getString(sacredStoneLevel + ".name", "");
        placeholders.put("sacred_stone_name", stoneName);
        return placeholders;
    }

    private Map<String, String> buildBuffButtonMap(Player player, String key, GuildInfo guildInfo, Map<String, GuildBuff> buffMap) {
        HashMap<String, String> placeholders = MapUtil.newHashMapWithExpectedSize(4);
        String buttonText = ConfigUtil.SACRED_STONE_CONFIG.getString("lock", "");
        String oreAmount = guildInfo.getOre() != null ? guildInfo.getOre().toString() : "0";
        String priceFormula = ConfigUtil.SACRED_STONE_CONFIG.getString(new StringBuilder().insert(0, "buff.").append(key).append(".price").toString(), "0");
        int level = buffMap.get(key) != null ? buffMap.get(key).getLevel() : 0;
        int maxLevel = ConfigUtil.SACRED_STONE_CONFIG.getInt(new StringBuilder().insert(0, "buff.").append(key).append(".maxLevel").toString());
        if (level >= maxLevel) {
            buttonText = ConfigUtil.SACRED_STONE_CONFIG.getString("maxLevelMsg", "");
        }
        HashMap<String, String> levelPlaceholders = MapUtil.newHashMapWithExpectedSize(4);
        levelPlaceholders.put("level", String.valueOf(level + 1));
        Integer useOre = FormulaUtil.evaluateFormulaToInt(PlaceholderApiUtil.set(player, priceFormula), levelPlaceholders);
        buttonText = buttonText.replace("${guild_ore}", oreAmount).replace("${use_ore}", useOre.toString());
        placeholders.put("level", String.valueOf(level));
        placeholders.put("maxLevel", String.valueOf(maxLevel));
        placeholders.put("button", buttonText);
        return placeholders;
    }

    private Map<String, String> buildPickaxeButtonMap(String pickaxeKey, GuildPlayer guildPlayer) {
        HashMap<String, String> placeholders = MapUtil.newHashMapWithExpectedSize(3);
        int price = ConfigUtil.SACRED_STONE_CONFIG.getInt(new StringBuilder().insert(0, pickaxeKey).append(".price").toString(), 0);
        placeholders.put("price", String.valueOf(price));
        placeholders.put("money", guildPlayer.getMoney().toString());
        String buttonText = ConfigUtil.SACRED_STONE_CONFIG.getString("notExcavated");
        String notMoneyText = ConfigUtil.SACRED_STONE_CONFIG.getString("notMoneyExcavated");
        if (guildPlayer.getMoney() < price) {
            buttonText = notMoneyText;
        }
        if (guildPlayer.getPickaxeTime() != null) {
            String lastTime = DateUtil.format(guildPlayer.getPickaxeTime(), "yyyy-MM-dd");
            if (DateUtil.format(new Date(), "yyyy-MM-dd").equalsIgnoreCase(lastTime)) {
                buttonText = ConfigUtil.SACRED_STONE_CONFIG.getString("excavated", "").replace("${ore}", guildPlayer.getOre().toString());
            }
        }
        placeholders.put("button", buttonText);
        return placeholders;
    }

    private void fillGui(HandyInventory gui) {
        Inventory inventory = gui.getInventory();
        Player player = gui.getPlayer();
        Map<Integer, String> soundMap = gui.getSoundMap();
        Optional<GuildInfo> optional = GuildInfoService.getInstance().findById(gui.getId());
        if (!optional.isPresent()) {
            return;
        }
        GuildInfo guildInfo = optional.get();
        Integer sacredStoneLevel = guildInfo.getSacredStoneLevel();
        String stoneName = ConfigUtil.SACRED_STONE_CONFIG.getString(sacredStoneLevel + ".name", "");
        List<Integer> slotList = StrUtil.strToIntList(ConfigUtil.SACRED_STONE_CONFIG.getString("info.index"));
        String material = ConfigUtil.SACRED_STONE_CONFIG.getString("info.material");
        String sound = ConfigUtil.SACRED_STONE_CONFIG.getString("info.sound");
        boolean enchant = ConfigUtil.SACRED_STONE_CONFIG.getBoolean("info.isEnchant");
        stoneName = ConfigUtil.SACRED_STONE_CONFIG.getString("info.name", "${sacred_stone_name}").replace("${sacred_stone_name}", stoneName);
        int customModel = ConfigUtil.SACRED_STONE_CONFIG.getInt("info.custom-model-data");
        List<String> loreTemplate = ConfigUtil.SACRED_STONE_CONFIG.getStringList("info.lore");
        boolean hideEnchant = ConfigUtil.SACRED_STONE_CONFIG.getBoolean("info.hideFlag", true);
        String tooltipStyle = ConfigUtil.SACRED_STONE_CONFIG.getString("info.tooltipStyle");
        String itemModel = ConfigUtil.SACRED_STONE_CONFIG.getString("info.itemModel");
        ItemStack itemStack = ItemStackUtil.getItemStack(material, stoneName, loreTemplate, enchant, customModel, hideEnchant, this.buildInfoPlaceholders(guildInfo), false, null, tooltipStyle, itemModel);
        slotList.forEach(slot -> GuildSacredStoneGui.setItemAndSound(inventory, itemStack, soundMap, sound, slot));
        HandyInventoryUtil.setButton(ConfigUtil.SACRED_STONE_CONFIG, gui, "back");
        Optional<GuildPlayer> optionalGuildPlayer = GuildPlayerService.getInstance().findByPlayerUuid(player.getUniqueId());
        if (!optionalGuildPlayer.isPresent()) {
            return;
        }
        GuildPlayer guildPlayer = optionalGuildPlayer.get();
        HandyInventoryUtil.setButton(ConfigUtil.SACRED_STONE_CONFIG, gui, "woodenPickaxe", this.buildPickaxeButtonMap("woodenPickaxe", guildPlayer));
        HandyInventoryUtil.setButton(ConfigUtil.SACRED_STONE_CONFIG, gui, "ironPickaxe", this.buildPickaxeButtonMap("ironPickaxe", guildPlayer));
        HandyInventoryUtil.setButton(ConfigUtil.SACRED_STONE_CONFIG, gui, "goldenPickaxe", this.buildPickaxeButtonMap("goldenPickaxe", guildPlayer));
        HandyInventoryUtil.setButton(ConfigUtil.SACRED_STONE_CONFIG, gui, "diamondPickaxe", this.buildPickaxeButtonMap("diamondPickaxe", guildPlayer));
        Map<String, Object> childMap = HandyConfigUtil.getChildMap(ConfigUtil.SACRED_STONE_CONFIG, "buff");
        Map<String, GuildBuff> buffMap = GuildBuffService.getInstance().findByGuildId(guildInfo.getId());
        for (String key : childMap.keySet()) {
            Map<String, String> buffPlaceholders = this.buildBuffButtonMap(player, key, guildInfo, buffMap);
            Map<String, List<String>> buffLoreMap = this.buildBuffLoreMap(player, key, buffMap);
            HandyInventoryUtil.setButton(ConfigUtil.SACRED_STONE_CONFIG, gui, "buff." + key, buffPlaceholders, buffLoreMap);
        }
        HandyInventoryUtil.setCustomButton(ConfigUtil.SACRED_STONE_CONFIG, gui, "custom");
    }

    private Map<String, List<String>> buildBuffLoreMap(Player player, String key, Map<String, GuildBuff> buffMap) {
        HashMap<String, List<String>> loreMap = MapUtil.newHashMapWithExpectedSize(2);
        List<String> loreTemplate = ConfigUtil.SACRED_STONE_CONFIG.getStringList(new StringBuilder().insert(0, "buff.").append(key).append(".display").toString());
        if (CollUtil.isEmpty(loreTemplate)) {
            loreTemplate = ConfigUtil.SACRED_STONE_CONFIG.getStringList(new StringBuilder().insert(0, "buff.").append(key).append(".attribute").toString());
        }
        if (CollUtil.isEmpty(loreTemplate)) {
            return loreMap;
        }
        int level = buffMap.get(key) != null ? buffMap.get(key).getLevel() : 0;
        ArrayList<String> displayLore = new ArrayList<String>();
        for (String template : loreTemplate) {
            String line = FormulaUtil.evaluateFormula(PlaceholderApiUtil.set(player, template), MapUtil.of("level", String.valueOf(level)));
            displayLore.add(line);
        }
        loreMap.put("buff", displayLore);
        int nextLevel = buffMap.get(key) != null ? buffMap.get(key).getLevel() + 1 : 1;
        ArrayList<String> nextLore = new ArrayList<String>();
        for (String template : loreTemplate) {
            String nextLine = FormulaUtil.evaluateFormula(PlaceholderApiUtil.set(player, template), MapUtil.of("level", String.valueOf(nextLevel)));
            nextLore.add(nextLine);
        }
        loreMap.put("subBuff", nextLore);
        int maxLevel = ConfigUtil.SACRED_STONE_CONFIG.getInt(new StringBuilder().insert(0, "buff.").append(key).append(".maxLevel").toString());
        if (level >= maxLevel) {
            String maxLevelText = ConfigUtil.SACRED_STONE_CONFIG.getString("maxLevel", "");
            loreMap.put("subBuff", Collections.singletonList(maxLevelText));
        }
        return loreMap;
    }

    public static GuildSacredStoneGui getInstance() {
        return INSTANCE;
    }

    private static void setItemAndSound(Inventory inventory, ItemStack itemStack, Map soundMap, String sound, Integer slot) {
        Integer slotIndex = slot;
        inventory.setItem(slotIndex.intValue(), itemStack);
        soundMap.put(slotIndex, sound);
    }
}
