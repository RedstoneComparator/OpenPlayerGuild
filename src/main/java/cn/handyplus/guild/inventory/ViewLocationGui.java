package cn.handyplus.guild.inventory;

import cn.handyplus.guild.constants.GuiTypeEnum;
import cn.handyplus.guild.constants.GuildActivityType;
import cn.handyplus.guild.enter.GuildPvpLocation;
import cn.handyplus.guild.hook.PlaceholderApiUtil;
import cn.handyplus.guild.lib.HandyInventory;
import cn.handyplus.guild.lib.HandyInventoryUtil;
import cn.handyplus.guild.lib.ItemStackUtil;
import cn.handyplus.guild.lib.Page;
import cn.handyplus.guild.lib.StrUtil;
import cn.handyplus.guild.service.GuildPvpLocationService;
import cn.handyplus.guild.util.ConfigUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ViewLocationGui {
    private final static ViewLocationGui INSTANCE = new ViewLocationGui();

    public static ViewLocationGui getInstance() {
        return INSTANCE;
    }

    public Inventory createGui(Player player) {
        String title = ConfigUtil.PVP_LOCATION_CONFIG.getString("title");
        String sound = ConfigUtil.PVP_LOCATION_CONFIG.getString("sound");
        title = PlaceholderApiUtil.set(player, title);
        int size = ConfigUtil.PVP_LOCATION_CONFIG.getInt("size", 54);
        HandyInventory handyInventory = new HandyInventory(GuiTypeEnum.VIEW_LOCATION.getType(), title, size, sound);
        handyInventory.setPlayer(player);
        this.setInventoryDate(handyInventory);
        return handyInventory.getInventory();
    }

    private void fillLocationItems(HandyInventory gui) {
        HandyInventory inventoryGui = gui;
        Inventory inventory = gui.getInventory();
        Map<Integer, Integer> map = gui.getIntMap();
        List<Integer> list = StrUtil.strToIntList(ConfigUtil.PVP_LOCATION_CONFIG.getString("index", "20,21,22,23,24,29,30,31,32,33"));
        inventoryGui.setPageSize(list.size());
        Page<GuildPvpLocation> page = GuildPvpLocationService.getInstance().page(null, gui.getPageNum(), gui.getPageSize());
        inventoryGui.setPageCount(page.getTotal());
        if (page.getTotal() < 1) {
            return;
        }
        List<GuildPvpLocation> records = page.getRecords();
        int index = 0;
        String buttonText = ConfigUtil.PVP_LOCATION_CONFIG.getString("button");
        for (GuildPvpLocation guildPvpLocation : records) {
            Integer slot = list.get(index);
            ++index;
            GuildActivityType guildActivityType = GuildActivityType.getTypeThrow(guildPvpLocation.getType());
            String name = guildPvpLocation.getName();
            if (guildActivityType != null) {
                name = new StringBuilder().insert(0, name).append(" ▪ ").append(GuildActivityType.getDesc(guildActivityType)).toString();
            }
            List<String> lore = new ArrayList<String>();
            lore.add(buttonText);
            ItemStack itemStack = ItemStackUtil.getItemStack("DIAMOND_BOOTS", name, lore);
            inventory.setItem(slot.intValue(), itemStack);
            map.put(slot, guildPvpLocation.getId());
        }
    }

    public void setInventoryDate(HandyInventory gui) {
        HandyInventory inventory = gui;
        inventory.setGuiType(GuiTypeEnum.VIEW_LOCATION.getType());
        HandyInventoryUtil.refreshInventory(inventory.getInventory());
        ViewLocationGui instance = this;
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

    private ViewLocationGui() {
    }
}
