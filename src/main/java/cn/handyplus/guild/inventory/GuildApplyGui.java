package cn.handyplus.guild.inventory;

import cn.handyplus.guild.constants.ApplyTypeEnum;
import cn.handyplus.guild.constants.GuiTypeEnum;
import cn.handyplus.guild.enter.GuildApply;
import cn.handyplus.guild.hook.PlaceholderApiUtil;
import cn.handyplus.guild.lib.DateUtil;
import cn.handyplus.guild.lib.HandyInventory;
import cn.handyplus.guild.lib.HandyInventoryUtil;
import cn.handyplus.guild.lib.ItemStackUtil;
import cn.handyplus.guild.lib.MapUtil;
import cn.handyplus.guild.lib.Page;
import cn.handyplus.guild.lib.StrUtil;
import cn.handyplus.guild.service.GuildApplyService;
import cn.handyplus.guild.util.ConfigUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class GuildApplyGui {
    private final static GuildApplyGui INSTANCE = new GuildApplyGui();

    private GuildApplyGui() {
    }

    public Inventory createGui(Player player, Integer guildId) {
        String title = ConfigUtil.APPLY_CONFIG.getString("title");
        String sound = ConfigUtil.APPLY_CONFIG.getString("sound");
        int size = ConfigUtil.APPLY_CONFIG.getInt("size", 54);
        title = PlaceholderApiUtil.set(player, title);
        HandyInventory handyInventory = new HandyInventory(GuiTypeEnum.GUILD_APPLY.getType(), title, size, sound);
        handyInventory.setPlayer(player);
        handyInventory.setId(guildId);
        this.setInventoryDate(handyInventory);
        return handyInventory.getInventory();
    }

    private void setPageButtons(HandyInventory inventory) {
        Map<String, String> pageMap = HandyInventoryUtil.replacePageMap(inventory);
        HandyInventoryUtil.setButton(ConfigUtil.APPLY_CONFIG, inventory, "nextPage", pageMap);
        HandyInventoryUtil.setButton(ConfigUtil.APPLY_CONFIG, inventory, "previousPage", pageMap);
        HandyInventoryUtil.setButton(ConfigUtil.APPLY_CONFIG, inventory, "back");
        HandyInventoryUtil.setButton(ConfigUtil.APPLY_CONFIG, inventory, "pane");
    }

    public void setInventoryDate(HandyInventory inventory) {
        HandyInventory handyInventory = inventory;
        handyInventory.setGuiType(GuiTypeEnum.GUILD_APPLY.getType());
        HandyInventoryUtil.refreshInventory(handyInventory.getInventory());
        GuildApplyGui guildApplyGui = this;
        guildApplyGui.fillMemberItems(inventory);
        guildApplyGui.setPageButtons(handyInventory);
    }

    private void fillMemberItems(HandyInventory guiInventory) {
        HandyInventory handyInventory = guiInventory;
        Inventory inventory = guiInventory.getInventory();
        Map<Integer, Integer> intMap = guiInventory.getIntMap();
        List<Integer> slots = StrUtil.strToIntList(ConfigUtil.APPLY_CONFIG.getString("member.index", "20,21,22,23,24,29,30,31,32,33"));
        handyInventory.setPageSize(slots.size());
        Page<GuildApply> page = GuildApplyService.getInstance().page(guiInventory.getId(), guiInventory.getPageNum(), guiInventory.getPageSize(), ApplyTypeEnum.PENDING.getId());
        handyInventory.setPageCount(page.getTotal());
        if (page.getTotal() < 1) {
            return;
        }
        List<GuildApply> records = page.getRecords();
        String nameFormat = ConfigUtil.APPLY_CONFIG.getString("member.name", "");
        String material = ConfigUtil.APPLY_CONFIG.getString("member.material");
        int customModelData = ConfigUtil.APPLY_CONFIG.getInt("member.custom-model-data");
        List<String> lore = ConfigUtil.APPLY_CONFIG.getStringList("member.lore");
        boolean hideFlag = ConfigUtil.APPLY_CONFIG.getBoolean("member.hideFlag", true);
        Map<Integer, ItemStack> itemMap = MapUtil.newHashMapWithExpectedSize(records.size());
        for (GuildApply guildApply : records) {
            String itemName = nameFormat.replace("${player_name}", guildApply.getPlayerName());
            lore = PlaceholderApiUtil.set(Bukkit.getOfflinePlayer(guildApply.getPlayerUuid()), lore);
            ItemStack itemStack = ItemStackUtil.getItemStack(material, itemName, lore, true, customModelData, hideFlag, this.getPlaceholderMap(guildApply));
            itemMap.put(guildApply.getId(), itemStack);
        }
        int index = 0;
        for (Integer applyId : itemMap.keySet()) {
            Integer slot = slots.get(index);
            ++index;
            inventory.setItem(slot.intValue(), itemMap.get(applyId));
            intMap.put(slot, applyId);
        }
    }

    public static GuildApplyGui getInstance() {
        return INSTANCE;
    }

    private Map<String, String> getPlaceholderMap(GuildApply guildApply) {
        HashMap<String, String> placeholders = MapUtil.newHashMapWithExpectedSize(2);
        placeholders.put("player_name", guildApply.getPlayerName());
        placeholders.put("apply_Time", DateUtil.format(guildApply.getApplyTime(), "yyyy-MM-dd HH:mm:ss"));
        return placeholders;
    }
}
