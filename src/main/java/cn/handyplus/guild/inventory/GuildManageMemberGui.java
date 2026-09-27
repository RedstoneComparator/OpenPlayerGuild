package cn.handyplus.guild.inventory;

import cn.handyplus.guild.constants.GuiTypeEnum;
import cn.handyplus.guild.constants.GuildRoleEnum;
import cn.handyplus.guild.enter.GuildPlayer;
import cn.handyplus.guild.hook.PlaceholderApiUtil;
import cn.handyplus.guild.lib.HandyInventory;
import cn.handyplus.guild.lib.HandyInventoryUtil;
import cn.handyplus.guild.lib.MapUtil;
import cn.handyplus.guild.service.GuildPlayerService;
import cn.handyplus.guild.util.ConfigUtil;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class GuildManageMemberGui {
    private final static GuildManageMemberGui INSTANCE = new GuildManageMemberGui();

    private Map<String, String> getRolePlaceholders(GuildPlayer guildPlayer) {
        HashMap<String, String> placeholders = MapUtil.newHashMapWithExpectedSize(3);
        String previousValue = placeholders.put("role", GuildRoleEnum.getRoleName(guildPlayer.getRoleId()));
        HashMap<String, String> resultMap = placeholders;
        placeholders.put("upRole", GuildRoleEnum.getRoleName(guildPlayer.getRoleId() - 1));
        resultMap.put("downRole", GuildRoleEnum.getRoleName(guildPlayer.getRoleId() + 1));
        return resultMap;
    }

    public static GuildManageMemberGui getInstance() {
        return INSTANCE;
    }

    private GuildManageMemberGui() {
    }

    private void setButtons(HandyInventory inventory) {
        HandyInventory handyInventory = inventory;
        Player player = handyInventory.getPlayer();
        Integer guildPlayerId = Integer.valueOf(handyInventory.getObj().toString());
        Optional<GuildPlayer> optional = GuildPlayerService.getInstance().findById(guildPlayerId);
        if (!optional.isPresent()) {
            return;
        }
        GuildPlayer guildPlayer = optional.get();
        Optional<GuildPlayer> optional2 = GuildPlayerService.getInstance().findByPlayerUuid(player.getUniqueId());
        if (!optional2.isPresent()) {
            return;
        }
        boolean canManage = !optional2.get().getRoleId().equals(GuildRoleEnum.TWO.getRoleId()) || !guildPlayer.getRoleId().equals(GuildRoleEnum.THREE.getRoleId());
        if (canManage) {
            HandyInventoryUtil.setButton(ConfigUtil.MANAGE_MEMBER_CONFIG, inventory, "up", this.getRolePlaceholders(guildPlayer));
        }
        HandyInventoryUtil.setButton(ConfigUtil.MANAGE_MEMBER_CONFIG, inventory, "down", this.getRolePlaceholders(guildPlayer));
        HandyInventoryUtil.setButton(ConfigUtil.MANAGE_MEMBER_CONFIG, inventory, "ban");
        HandyInventoryUtil.setButton(ConfigUtil.MANAGE_MEMBER_CONFIG, inventory, "back");
    }

    public void setInventoryDate(HandyInventory inventory) {
        HandyInventory handyInventory = inventory;
        handyInventory.setGuiType(GuiTypeEnum.GUILD_MANAGE_MEMBER_CREATE.getType());
        HandyInventoryUtil.refreshInventory(handyInventory.getInventory());
        this.setButtons(handyInventory);
    }

    public Inventory createGui(Player player, Integer guildId, Integer guildPlayerId) {
        Object title = ConfigUtil.MANAGE_MEMBER_CONFIG.getString("title");
        String sound = ConfigUtil.MANAGE_MEMBER_CONFIG.getString("sound");
        title = PlaceholderApiUtil.set(player, (String)title);
        int size = ConfigUtil.MANAGE_MEMBER_CONFIG.getInt("size", 27);
        Object handyInventory = title = new HandyInventory(GuiTypeEnum.GUILD_MANAGE_MEMBER_CREATE.getType(), (String)title, size, sound);
        Object inventoryRef = title;
        ((HandyInventory)inventoryRef).setPlayer(player);
        ((HandyInventory)inventoryRef).setId(guildId);
        ((HandyInventory)handyInventory).setObj(guildPlayerId);
        this.setInventoryDate((HandyInventory)handyInventory);
        return ((HandyInventory)handyInventory).getInventory();
    }
}
