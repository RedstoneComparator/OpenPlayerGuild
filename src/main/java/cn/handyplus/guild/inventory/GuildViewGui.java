package cn.handyplus.guild.inventory;

import cn.handyplus.guild.constants.GuiTypeEnum;
import cn.handyplus.guild.constants.GuildRoleEnum;
import cn.handyplus.guild.enter.GuildInfo;
import cn.handyplus.guild.enter.GuildPlayer;
import cn.handyplus.guild.hook.PlaceholderApiUtil;
import cn.handyplus.guild.lib.BaseConstants;
import cn.handyplus.guild.lib.CollUtil;
import cn.handyplus.guild.lib.HandyInventory;
import cn.handyplus.guild.lib.HandyInventoryUtil;
import cn.handyplus.guild.lib.ItemMetaUtil;
import cn.handyplus.guild.lib.ItemStackUtil;
import cn.handyplus.guild.lib.MapUtil;
import cn.handyplus.guild.lib.StrUtil;
import cn.handyplus.guild.service.GuildInfoService;
import cn.handyplus.guild.service.GuildPlayerService;
import cn.handyplus.guild.util.ConfigUtil;
import cn.handyplus.guild.util.GuildUtil;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GuildViewGui {
    private final static GuildViewGui INSTANCE = new GuildViewGui();

    private GuildViewGui() {
    }

    private void fillGui(HandyInventory gui) {
        Player player = gui.getPlayer();
        Optional<GuildInfo> optional = GuildInfoService.getInstance().findById(gui.getId());
        if (!optional.isPresent()) {
            return;
        }
        GuildInfo guildInfo = optional.get();
        Optional<GuildPlayer> optional2 = GuildPlayerService.getInstance().findByPlayerUuid(player.getUniqueId());
        if (!optional2.isPresent()) {
            return;
        }
        GuildPlayer guildPlayer = optional2.get();
        if (ConfigUtil.VIEW_CONFIG.getBoolean("menu.enable")) {
            String name = BaseConstants.CONFIG.getString(new StringBuilder().insert(0, "guildSetting.").append(guildInfo.getLevel()).append(".nameColor").toString(), "");
            name = ConfigUtil.VIEW_CONFIG.getString("menu.name", new StringBuilder().insert(0, name).append(guildInfo.getGuildName()).toString());
            List<String> list = ConfigUtil.VIEW_CONFIG.getStringList("menu.lore");
            ItemStack itemStack = ItemStackUtil.itemStackDeserialize(guildInfo.getLogoName());
            ItemMeta itemMeta = ItemStackUtil.getItemMeta(itemStack);
            ItemMetaUtil.setDisplayName(itemMeta, name);
            if (CollUtil.isEmpty(list)) {
                list = StrUtil.strToStrList(guildInfo.getDescription());
            }
            list = PlaceholderApiUtil.set(player, list);
            list = ItemStackUtil.loreBatchReplaceMap(list, GuildUtil.getDescriptionMap(guildInfo.getDescription()), null);
            ItemMeta itemMeta2 = itemMeta;
            ItemMetaUtil.setLore(itemMeta2, list);
            ItemMetaUtil.hideAttributes(itemMeta2);
            ItemMetaUtil.hideEnchant(itemMeta2);
            itemStack.setItemMeta(itemMeta2);
            for (Integer slot : StrUtil.strToIntList(ConfigUtil.VIEW_CONFIG.getString("menu.index"))) {
                gui.getInventory().setItem(slot.intValue(), itemStack);
            }
        }
        HandyInventoryUtil.setButton(ConfigUtil.VIEW_CONFIG, gui, "notice");
        HandyInventoryUtil.setButton(ConfigUtil.VIEW_CONFIG, gui, "member");
        HandyInventoryUtil.setButton(ConfigUtil.VIEW_CONFIG, gui, "spawn");
        HandyInventoryUtil.setButton(ConfigUtil.VIEW_CONFIG, gui, "task");
        HandyInventoryUtil.setButton(ConfigUtil.VIEW_CONFIG, gui, "shop", this.buildShopButtonMap(guildPlayer));
        HandyInventoryUtil.setButton(ConfigUtil.VIEW_CONFIG, gui, "bank");
        HandyInventoryUtil.setButton(ConfigUtil.VIEW_CONFIG, gui, "signin");
        HandyInventoryUtil.setButton(ConfigUtil.VIEW_CONFIG, gui, "buff");
        HandyInventoryUtil.setButton(ConfigUtil.VIEW_CONFIG, gui, "sacredStone");
        HandyInventoryUtil.setButton(ConfigUtil.VIEW_CONFIG, gui, "activity");
        HandyInventoryUtil.setButton(ConfigUtil.VIEW_CONFIG, gui, "donate");
        HandyInventoryUtil.setButton(ConfigUtil.VIEW_CONFIG, gui, "back");
        HandyInventoryUtil.setButton(ConfigUtil.VIEW_CONFIG, gui, "exit");
        HandyInventoryUtil.setButton(ConfigUtil.VIEW_CONFIG, gui, "pane");
        if (guildPlayer.getRoleId() < GuildRoleEnum.THREE.getRoleId()) {
            HandyInventoryUtil.setButton(ConfigUtil.VIEW_CONFIG, gui, "admin");
        }
        HandyInventoryUtil.setCustomButton(ConfigUtil.VIEW_CONFIG, gui, "custom");
    }

    public static GuildViewGui getInstance() {
        return INSTANCE;
    }

    private void setGuiTypeAndFill(HandyInventory gui) {
        HandyInventory inventory = gui;
        inventory.setGuiType(GuiTypeEnum.GUILD_VIEW.getType());
        this.fillGui(inventory);
    }

    public Inventory createGui(Player player, Integer guildId) {
        Object guiValue = ConfigUtil.VIEW_CONFIG.getString("title");
        String sound = ConfigUtil.VIEW_CONFIG.getString("sound");
        guiValue = PlaceholderApiUtil.set(player, (String)guiValue);
        int size = ConfigUtil.VIEW_CONFIG.getInt("size", 54);
        Object handyInventory = guiValue = new HandyInventory(GuiTypeEnum.GUILD_VIEW.getType(), (String)guiValue, size, sound);
        ((HandyInventory)guiValue).setPlayer(player);
        ((HandyInventory)handyInventory).setId(guildId);
        this.setGuiTypeAndFill((HandyInventory)handyInventory);
        return ((HandyInventory)handyInventory).getInventory();
    }

    private Map<String, String> buildShopButtonMap(GuildPlayer guildPlayer) {
        int buyRoleLimit = ConfigUtil.VIEW_CONFIG.getInt("shop.buyRoleLimit", 4);
        String buttonText = ConfigUtil.VIEW_CONFIG.getString("shop.yesButton");
        if (guildPlayer.getRoleId() > 1) {
            buttonText = ConfigUtil.VIEW_CONFIG.getString("shop.noRoleButton");
        }
        return MapUtil.of("button", buttonText);
    }
}
