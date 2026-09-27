package cn.handyplus.guild.inventory;

import cn.handyplus.guild.constants.GuiTypeEnum;
import cn.handyplus.guild.enter.GuildLogo;
import cn.handyplus.guild.hook.PlaceholderApiUtil;
import cn.handyplus.guild.lib.CollUtil;
import cn.handyplus.guild.lib.HandyInventory;
import cn.handyplus.guild.lib.HandyInventoryUtil;
import cn.handyplus.guild.lib.ItemMetaUtil;
import cn.handyplus.guild.lib.ItemStackUtil;
import cn.handyplus.guild.lib.Page;
import cn.handyplus.guild.lib.StrUtil;
import cn.handyplus.guild.service.GuildLogoService;
import cn.handyplus.guild.util.ConfigUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GuildLogoOpenGui {
    private final static GuildLogoOpenGui INSTANCE = new GuildLogoOpenGui();

    public static GuildLogoOpenGui getInstance() {
        return INSTANCE;
    }

    public void setInventoryDate(HandyInventory inventory) {
        HandyInventory handyInventory = inventory;
        handyInventory.setGuiType(GuiTypeEnum.GUILD_LOGO_OPEN.getType());
        HandyInventoryUtil.refreshInventory(handyInventory.getInventory());
        GuildLogoOpenGui guildLogoOpenGui = this;
        guildLogoOpenGui.fillLogoItems(inventory);
        guildLogoOpenGui.setPageButtons(handyInventory);
    }

    private GuildLogoOpenGui() {
    }

    public Inventory createGui(Player player, Integer guildId) {
        Object title = ConfigUtil.LOGO_SHOP_CONFIG.getString("openTitle");
        String sound = ConfigUtil.LOGO_SHOP_CONFIG.getString("sound");
        title = PlaceholderApiUtil.set(player, (String)title);
        int size = ConfigUtil.LOGO_SHOP_CONFIG.getInt("size", 54);
        Object handyInventory = title = new HandyInventory(GuiTypeEnum.GUILD_LOGO_OPEN.getType(), (String)title, size, sound);
        ((HandyInventory)title).setPlayer(player);
        ((HandyInventory)handyInventory).setId(guildId);
        this.setInventoryDate((HandyInventory)handyInventory);
        return ((HandyInventory)handyInventory).getInventory();
    }

    private void setPageButtons(HandyInventory inventory) {
        Map<String, String> pageMap = HandyInventoryUtil.replacePageMap(inventory);
        HandyInventoryUtil.setButton(ConfigUtil.LOGO_SHOP_CONFIG, inventory, "nextPage", pageMap);
        HandyInventoryUtil.setButton(ConfigUtil.LOGO_SHOP_CONFIG, inventory, "previousPage", pageMap);
        HandyInventoryUtil.setButton(ConfigUtil.LOGO_SHOP_CONFIG, inventory, "back");
        HandyInventoryUtil.setButton(ConfigUtil.LOGO_SHOP_CONFIG, inventory, "pane");
    }

    private void fillLogoItems(HandyInventory guiInventory) {
        HandyInventory handyInventory = guiInventory;
        Inventory inventory = handyInventory.getInventory();
        Integer guildId = handyInventory.getId();
        Map<Integer, Integer> intMap = handyInventory.getIntMap();
        List<Integer> slots = StrUtil.strToIntList(ConfigUtil.LOGO_SHOP_CONFIG.getString("logo.index", "20,21,22,23,24,29,30,31,32,33"));
        handyInventory.setPageSize(slots.size());
        Page<GuildLogo> page = GuildLogoService.getInstance().page(guildId, guiInventory.getPageNum(), guiInventory.getPageSize());
        handyInventory.setPageCount(page.getTotal());
        if (page.getTotal() < 1) {
            return;
        }
        int index = 0;
        String selectUseButton = ConfigUtil.LOGO_SHOP_CONFIG.getString("selUseButton");
        String useButton = ConfigUtil.LOGO_SHOP_CONFIG.getString("useButton");
        for (GuildLogo guildLogo : page.getRecords()) {
            Integer slot = slots.get(index);
            ++index;
            ItemStack itemStack = ItemStackUtil.itemStackDeserialize(guildLogo.getLogoName());
            ItemMeta itemMeta = ItemStackUtil.getItemMeta(itemStack);
            List<String> lore = itemMeta.getLore();
            if (CollUtil.isEmpty(lore)) {
                lore = new ArrayList<String>();
            }
            if (guildLogo.getUseFlag() != null && guildLogo.getUseFlag().booleanValue()) {
                lore.add(useButton);
                ItemMetaUtil.setEnchant(itemMeta);
                ItemMetaUtil.hideEnchant(itemMeta);
            } else {
                lore.add(selectUseButton);
            }
            ItemMetaUtil.hideAttributes(itemMeta);
            ItemMetaUtil.setLore(itemMeta, lore);
            itemStack.setItemMeta(itemMeta);
            inventory.setItem(slot.intValue(), itemStack);
            intMap.put(slot, guildLogo.getId());
        }
    }
}
