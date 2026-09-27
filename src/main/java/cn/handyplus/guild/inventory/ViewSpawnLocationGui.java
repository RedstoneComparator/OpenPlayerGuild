package cn.handyplus.guild.inventory;

import cn.handyplus.guild.constants.GuiTypeEnum;
import cn.handyplus.guild.enter.GuildSpawnPvpLocation;
import cn.handyplus.guild.hook.GuildWorldUtil;
import cn.handyplus.guild.hook.PlaceholderApiUtil;
import cn.handyplus.guild.lib.CollUtil;
import cn.handyplus.guild.lib.HandyInventory;
import cn.handyplus.guild.lib.HandyInventoryUtil;
import cn.handyplus.guild.lib.ItemMetaUtil;
import cn.handyplus.guild.lib.ItemStackUtil;
import cn.handyplus.guild.lib.Page;
import cn.handyplus.guild.lib.StrUtil;
import cn.handyplus.guild.service.GuildSpawnPvpLocationService;
import cn.handyplus.guild.util.ConfigUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ViewSpawnLocationGui {
    private final static ViewSpawnLocationGui INSTANCE = new ViewSpawnLocationGui();

    private void fillLocationItems(HandyInventory gui) {
        HandyInventory inventoryGui = gui;
        Inventory inventory = gui.getInventory();
        Integer searchTypeId = Integer.valueOf(gui.getSearchType());
        Map<Integer, Integer> map = gui.getIntMap();
        List<Integer> list = StrUtil.strToIntList(ConfigUtil.PVP_LOCATION_CONFIG.getString("index", "20,21,22,23,24,29,30,31,32,33"));
        inventoryGui.setPageSize(list.size());
        Page<GuildSpawnPvpLocation> page = GuildSpawnPvpLocationService.getInstance().page(searchTypeId, gui.getPageNum(), gui.getPageSize());
        inventoryGui.setPageCount(page.getTotal());
        if (page.getTotal() < 1) {
            return;
        }
        List<GuildSpawnPvpLocation> records = page.getRecords();
        int index = 0;
        String deleteButton = ConfigUtil.PVP_LOCATION_CONFIG.getString("delButton");
        for (GuildSpawnPvpLocation guildSpawnPvpLocation : records) {
            Integer slot = list.get(index);
            ++index;
            ItemStack itemStack = new ItemStack(ItemStackUtil.getMaterial(Material.ARROW.name()));
            ItemMeta itemMeta = ItemStackUtil.getItemMeta(itemStack);
            ItemMetaUtil.setDisplayName(itemMeta, guildSpawnPvpLocation.getSpawnName());
            List<String> lore = itemMeta.getLore();
            if (CollUtil.isEmpty(lore)) {
                lore = new ArrayList<String>();
            }
            lore.add("");
            lore.add("&8▪ &7world: &a" + GuildWorldUtil.getWorldAlias(guildSpawnPvpLocation.getWorld()));
            lore.add("&8▪ &7X: &a" + (int)guildSpawnPvpLocation.getX());
            lore.add("&8▪ &7Y: &a" + (int)guildSpawnPvpLocation.getY());
            lore.add("&8▪ &7Z: &a" + (int)guildSpawnPvpLocation.getZ());
            lore.add("");
            lore.add(deleteButton);
            ItemMetaUtil.setLore(itemMeta, lore);
            itemStack.setItemMeta(itemMeta);
            inventory.setItem(slot.intValue(), itemStack);
            map.put(slot, guildSpawnPvpLocation.getId());
        }
    }

    public static ViewSpawnLocationGui getInstance() {
        return INSTANCE;
    }

    public void setInventoryDate(HandyInventory gui) {
        HandyInventory inventory = gui;
        inventory.setGuiType(GuiTypeEnum.VIEW_SPAWN_LOCATION.getType());
        HandyInventoryUtil.refreshInventory(inventory.getInventory());
        ViewSpawnLocationGui instance = this;
        instance.fillLocationItems(gui);
        instance.addNavigationButtons(inventory);
    }

    private void addNavigationButtons(HandyInventory gui) {
        Map<String, String> map = HandyInventoryUtil.replacePageMap(gui);
        HandyInventoryUtil.setButton(ConfigUtil.PVP_LOCATION_CONFIG, gui, "nextPage", map);
        HandyInventoryUtil.setButton(ConfigUtil.PVP_LOCATION_CONFIG, gui, "previousPage", map);
        HandyInventoryUtil.setButton(ConfigUtil.PVP_LOCATION_CONFIG, gui, "back");
        HandyInventoryUtil.setButton(ConfigUtil.PVP_LOCATION_CONFIG, gui, "pane");
    }

    private ViewSpawnLocationGui() {
    }

    public Inventory createGui(Player player, Integer guildId) {
        String title = ConfigUtil.PVP_LOCATION_CONFIG.getString("title");
        String sound = ConfigUtil.PVP_LOCATION_CONFIG.getString("sound");
        title = PlaceholderApiUtil.set(player, title);
        int size = ConfigUtil.PVP_LOCATION_CONFIG.getInt("size", 54);
        HandyInventory handyInventory = new HandyInventory(GuiTypeEnum.VIEW_SPAWN_LOCATION.getType(), title, size, sound);
        handyInventory.setPlayer(player);
        handyInventory.setSearchType(guildId.toString());
        this.setInventoryDate(handyInventory);
        return handyInventory.getInventory();
    }
}
