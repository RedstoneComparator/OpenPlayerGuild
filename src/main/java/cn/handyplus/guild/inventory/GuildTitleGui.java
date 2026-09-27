package cn.handyplus.guild.inventory;

import cn.handyplus.guild.PlayerGuild;
import cn.handyplus.guild.constants.GuiTypeEnum;
import cn.handyplus.guild.enter.GuildInfo;
import cn.handyplus.guild.hook.PlaceholderApiUtil;
import cn.handyplus.guild.lib.CollUtil;
import cn.handyplus.guild.lib.HandyInventory;
import cn.handyplus.guild.lib.HandyInventoryUtil;
import cn.handyplus.guild.lib.ItemStackUtil;
import cn.handyplus.guild.lib.StrUtil;
import cn.handyplus.guild.service.GuildInfoService;
import cn.handyplus.guild.util.ConfigUtil;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class GuildTitleGui {
    private final static GuildTitleGui INSTANCE = new GuildTitleGui();

    private static void setSlotItemAndSound(Inventory inventory, ItemStack itemStack, String soundKey, Map soundMap, Integer slot) {
        inventory.setItem(slot.intValue(), itemStack);
        if (StrUtil.isNotEmpty(soundKey)) {
            soundMap.put(slot, soundKey);
        }
    }

    public static GuildTitleGui getInstance() {
        return INSTANCE;
    }

    public Inventory createGui(Player player, Integer guildId) {
        String title = ConfigUtil.TITLE_CONFIG.getString("title");
        String sound = ConfigUtil.TITLE_CONFIG.getString("sound");
        title = PlaceholderApiUtil.set(player, title);
        int size = ConfigUtil.TITLE_CONFIG.getInt("size", 27);
        HandyInventory handyInventory = new HandyInventory(GuiTypeEnum.GUILD_TITLE.getType(), title, size, sound);
        handyInventory.setPlayer(player);
        handyInventory.setId(guildId);
        this.setInventoryDate(handyInventory);
        return handyInventory.getInventory();
    }

    private GuildTitleGui() {
    }

    public void setInventoryDate(HandyInventory gui) {
        HandyInventory inventory = gui;
        inventory.setGuiType(GuiTypeEnum.GUILD_TITLE.getType());
        HandyInventoryUtil.refreshInventory(inventory.getInventory());
        this.fillGui(inventory);
    }

    private void fillGui(HandyInventory gui) {
        Inventory inventory = gui.getInventory();
        Player player = gui.getPlayer();
        Integer guildInfoId = gui.getId();
        Map<Integer, String> soundMap = gui.getSoundMap();
        HandyInventoryUtil.setButton(ConfigUtil.TITLE_CONFIG, gui, "back");
        HandyInventoryUtil.setCustomButton(ConfigUtil.TITLE_CONFIG, gui, "custom");
        if (!PlayerGuild.USE_TITLE) {
            return;
        }
        Optional<GuildInfo> optional = GuildInfoService.getInstance().findById(guildInfoId);
        if (!optional.isPresent()) {
            return;
        }
        GuildInfo guildInfo = optional.get();
        Integer level = guildInfo.getLevel();
        String nowTitle = ConfigUtil.TITLE_CONFIG.getString(level + ".title", "");
        String nextTitle = ConfigUtil.TITLE_CONFIG.getString(level + 1 + ".title", nowTitle);
        nowTitle = nowTitle.replace("${guild_name}", guildInfo.getGuildName());
        nextTitle = nextTitle.replace("${guild_name}", guildInfo.getGuildName());
        List<Integer> slotList = StrUtil.strToIntList(ConfigUtil.TITLE_CONFIG.getString("now.index"));
        nowTitle = ConfigUtil.TITLE_CONFIG.getString("now.name", "${nowTitleName}").replace("${nowTitleName}", nowTitle);
        nowTitle = PlaceholderApiUtil.set(player, nowTitle);
        String itemType = ConfigUtil.TITLE_CONFIG.getString("now.material");
        String soundKey = ConfigUtil.TITLE_CONFIG.getString("now.sound");
        List<String> lore = ConfigUtil.TITLE_CONFIG.getStringList(level + ".buff");
        List<String> displayLore = ConfigUtil.TITLE_CONFIG.getStringList(level + ".display");
        if (CollUtil.isNotEmpty(displayLore)) {
            lore = displayLore;
        }
        int customModel = ConfigUtil.TITLE_CONFIG.getInt("now.custom-model-data");
        ItemStack itemStack = ItemStackUtil.getItemStack(itemType, nowTitle, lore, false, customModel);
        slotList.forEach(slot -> GuildTitleGui.setItemAndSound(inventory, itemStack, soundKey, soundMap, slot));
        List<Integer> nextSlotList = StrUtil.strToIntList(ConfigUtil.TITLE_CONFIG.getString("next.index"));
        nowTitle = ConfigUtil.TITLE_CONFIG.getString("next.name", " ${nextTitleName}").replace("${nextTitleName}", nextTitle);
        nowTitle = PlaceholderApiUtil.set(player, nowTitle);
        String nextItemType = ConfigUtil.TITLE_CONFIG.getString("next.material");
        String nextSoundKey = ConfigUtil.TITLE_CONFIG.getString("next.sound");
        List<String> nextLore = ConfigUtil.TITLE_CONFIG.getStringList(level + 1 + ".buff");
        List<String> nextDisplayLore = ConfigUtil.TITLE_CONFIG.getStringList(level + 1 + ".display");
        if (CollUtil.isNotEmpty(nextDisplayLore)) {
            nextLore = nextDisplayLore;
        }
        if (CollUtil.isEmpty(nextLore)) {
            nextLore = lore;
        }
        int nextCustomModel = ConfigUtil.TITLE_CONFIG.getInt("next.custom-model-data");
        ItemStack nextItemStack = ItemStackUtil.getItemStack(nextItemType, nowTitle, nextLore, false, nextCustomModel);
        nextSlotList.forEach(slot -> GuildTitleGui.setSlotItemAndSound(inventory, nextItemStack, nextSoundKey, soundMap, slot));
    }

    private static void setItemAndSound(Inventory inventory, ItemStack itemStack, String soundKey, Map soundMap, Integer slot) {
        inventory.setItem(slot.intValue(), itemStack);
        if (StrUtil.isNotEmpty(soundKey)) {
            soundMap.put(slot, soundKey);
        }
    }
}
