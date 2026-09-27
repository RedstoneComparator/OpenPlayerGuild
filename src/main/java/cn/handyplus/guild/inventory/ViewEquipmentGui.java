package cn.handyplus.guild.inventory;

import cn.handyplus.guild.constants.GuiTypeEnum;
import cn.handyplus.guild.enter.GuildEquipment;
import cn.handyplus.guild.hook.PlaceholderApiUtil;
import cn.handyplus.guild.lib.CollUtil;
import cn.handyplus.guild.lib.HandyInventory;
import cn.handyplus.guild.lib.HandyInventoryUtil;
import cn.handyplus.guild.lib.ItemMetaUtil;
import cn.handyplus.guild.lib.ItemStackUtil;
import cn.handyplus.guild.lib.Page;
import cn.handyplus.guild.lib.StrUtil;
import cn.handyplus.guild.service.GuildEquipmentService;
import cn.handyplus.guild.util.ConfigUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ViewEquipmentGui {
    private final static ViewEquipmentGui INSTANCE = new ViewEquipmentGui();

    private void addNavigationButtons(HandyInventory gui) {
        Map<String, String> map = HandyInventoryUtil.replacePageMap(gui);
        HandyInventoryUtil.setButton(ConfigUtil.EQUIPMENT_CONFIG, gui, "nextPage", map);
        HandyInventoryUtil.setButton(ConfigUtil.EQUIPMENT_CONFIG, gui, "previousPage", map);
        HandyInventoryUtil.setButton(ConfigUtil.EQUIPMENT_CONFIG, gui, "back");
        HandyInventoryUtil.setButton(ConfigUtil.EQUIPMENT_CONFIG, gui, "pane");
    }

    private void fillEquipmentItems(HandyInventory gui) {
        HandyInventory inventoryGui = gui;
        Inventory inventory = gui.getInventory();
        Map<Integer, Integer> map = gui.getIntMap();
        List<Integer> list = StrUtil.strToIntList(ConfigUtil.EQUIPMENT_CONFIG.getString("logo.index", "20,21,22,23,24,29,30,31,32,33"));
        inventoryGui.setPageSize(list.size());
        Page<GuildEquipment> page = GuildEquipmentService.getInstance().page(gui.getPageNum(), gui.getPageSize());
        inventoryGui.setPageCount(page.getTotal());
        if (page.getTotal() < 1) {
            return;
        }
        List<GuildEquipment> records = page.getRecords();
        int index = 0;
        String deleteButton = ConfigUtil.EQUIPMENT_CONFIG.getString("delButton");
        for (GuildEquipment guildEquipment : records) {
            Integer slot = list.get(index);
            ++index;
            ItemStack itemStack = ItemStackUtil.itemStackDeserialize(guildEquipment.getItemStack());
            List<String> lore = ItemStackUtil.getItemMeta(itemStack).getLore();
            if (CollUtil.isNotEmpty(lore)) {
                lore.add(deleteButton);
            } else {
                lore = new ArrayList<String>();
                lore.add(deleteButton);
            }
            ItemMeta itemMeta = ItemStackUtil.getItemMeta(itemStack);
            ItemMetaUtil.setLore(itemMeta, lore);
            itemStack.setItemMeta(itemMeta);
            inventory.setItem(slot.intValue(), itemStack);
            map.put(slot, guildEquipment.getId());
        }
    }

    public void setInventoryDate(HandyInventory gui) {
        HandyInventory inventory = gui;
        inventory.setGuiType(GuiTypeEnum.GUILD_EQUIPMENT.getType());
        HandyInventoryUtil.refreshInventory(inventory.getInventory());
        ViewEquipmentGui instance = this;
        instance.fillEquipmentItems(gui);
        instance.addNavigationButtons(inventory);
    }

    private ViewEquipmentGui() {
    }

    public static ViewEquipmentGui getInstance() {
        return INSTANCE;
    }

    public Inventory createGui(Player player) {
        String title = ConfigUtil.EQUIPMENT_CONFIG.getString("title");
        String sound = ConfigUtil.EQUIPMENT_CONFIG.getString("sound");
        title = PlaceholderApiUtil.set(player, title);
        int size = ConfigUtil.EQUIPMENT_CONFIG.getInt("size", 54);
        HandyInventory handyInventory = new HandyInventory(GuiTypeEnum.GUILD_EQUIPMENT.getType(), title, size, sound);
        handyInventory.setPlayer(player);
        this.setInventoryDate(handyInventory);
        return handyInventory.getInventory();
    }
}
