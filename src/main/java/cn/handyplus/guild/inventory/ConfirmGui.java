package cn.handyplus.guild.inventory;

import cn.handyplus.guild.constants.GuiTypeEnum;
import cn.handyplus.guild.hook.PlaceholderApiUtil;
import cn.handyplus.guild.lib.HandyInventory;
import cn.handyplus.guild.lib.HandyInventoryUtil;
import cn.handyplus.guild.util.ConfigUtil;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class ConfirmGui {
    private final static ConfirmGui INSTANCE = new ConfirmGui();

    private void initGui(HandyInventory inventory) {
        HandyInventory handyInventory = inventory;
        handyInventory.setGuiType(GuiTypeEnum.CONFIRM.getType());
        HandyInventoryUtil.refreshInventory(handyInventory.getInventory());
        this.setButtons(handyInventory);
    }

    public static ConfirmGui getInstance() {
        return INSTANCE;
    }

    private void setButtons(HandyInventory inventory) {
        HandyInventoryUtil.setButton(ConfigUtil.CONFIRM_CONFIG, inventory, "confirm");
        HandyInventoryUtil.setButton(ConfigUtil.CONFIRM_CONFIG, inventory, "cancel");
        HandyInventoryUtil.setButton(ConfigUtil.CONFIRM_CONFIG, inventory, "back");
        HandyInventoryUtil.setButton(ConfigUtil.CONFIRM_CONFIG, inventory, "pane");
    }

    public Inventory createGui(Player player, Integer guildId, String searchType, Object obj) {
        Object title = ConfigUtil.CONFIRM_CONFIG.getString("title");
        String sound = ConfigUtil.CONFIRM_CONFIG.getString("sound");
        int size = ConfigUtil.CONFIRM_CONFIG.getInt("size", 27);
        title = PlaceholderApiUtil.set(player, (String)title);
        Object handyInventory = title = new HandyInventory(GuiTypeEnum.CONFIRM.getType(), (String)title, size, sound);
        Object inventoryRef = title;
        ((HandyInventory)title).setPlayer(player);
        ((HandyInventory)inventoryRef).setId(guildId);
        ((HandyInventory)inventoryRef).setSearchType(searchType);
        ((HandyInventory)handyInventory).setObj(obj);
        this.initGui((HandyInventory)handyInventory);
        return ((HandyInventory)handyInventory).getInventory();
    }

    private ConfirmGui() {
    }
}
