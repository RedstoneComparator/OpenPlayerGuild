package cn.handyplus.guild.inventory;

import cn.handyplus.guild.constants.GuiTypeEnum;
import cn.handyplus.guild.constants.GuildRoleEnum;
import cn.handyplus.guild.enter.GuildInfo;
import cn.handyplus.guild.enter.GuildPlayer;
import cn.handyplus.guild.hook.GuildWorldUtil;
import cn.handyplus.guild.hook.PlaceholderApiUtil;
import cn.handyplus.guild.lib.BaseConstants;
import cn.handyplus.guild.lib.BaseUtil;
import cn.handyplus.guild.lib.CollUtil;
import cn.handyplus.guild.lib.HandyInventory;
import cn.handyplus.guild.lib.HandyInventoryUtil;
import cn.handyplus.guild.lib.ItemMetaUtil;
import cn.handyplus.guild.lib.ItemStackUtil;
import cn.handyplus.guild.lib.JsonUtil;
import cn.handyplus.guild.lib.MapUtil;
import cn.handyplus.guild.lib.StrUtil;
import cn.handyplus.guild.param.GuildSpawn;
import cn.handyplus.guild.service.GuildInfoService;
import cn.handyplus.guild.service.GuildPlayerService;
import cn.handyplus.guild.util.ConfigUtil;
import cn.handyplus.guild.util.GuildUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GuildSettingGui {
    private final static GuildSettingGui INSTANCE = new GuildSettingGui();

    private void fillGui(HandyInventory gui) {
        GuildPlayer member;
        Player player = gui.getPlayer();
        Optional<GuildInfo> optional = GuildInfoService.getInstance().findById(gui.getId());
        if (!optional.isPresent()) {
            return;
        }
        GuildInfo guildInfo = optional.get();
        GuildPlayer currentGuildPlayer = GuildPlayerService.getInstance().findByPlayerUuid(gui.getPlayer().getUniqueId()).orElse(null);
        if (ConfigUtil.SETTING_CONFIG.getBoolean("menu.enable")) {
            List<String> list = ConfigUtil.SETTING_CONFIG.getStringList("menu.lore");
            ItemStack itemStack = ItemStackUtil.itemStackDeserialize(guildInfo.getLogoName());
            ItemMeta itemMeta = ItemStackUtil.getItemMeta(itemStack);
            ItemMetaUtil.setDisplayName(itemMeta, guildInfo.getGuildName());
            if (CollUtil.isEmpty(list)) {
                list = StrUtil.strToStrList(guildInfo.getDescription());
            }
            list = PlaceholderApiUtil.set(player, list);
            list = ItemStackUtil.loreBatchReplaceMap(list, GuildUtil.getDescriptionMap(guildInfo.getDescription()), null);
            ItemMeta itemMeta2 = itemMeta;
            ItemMetaUtil.setLore(itemMeta2, list);
            itemStack.setItemMeta(itemMeta2);
            gui.getInventory().setItem(ConfigUtil.SETTING_CONFIG.getInt("menu.index"), itemStack);
        }
        HandyInventoryUtil.setButton(ConfigUtil.SETTING_CONFIG, gui, "up", this.buildUpButtonMap(guildInfo));
        HandyInventoryUtil.setButton(ConfigUtil.SETTING_CONFIG, gui, "editName", this.buildEditNameButtonMap(guildInfo));
        HandyInventoryUtil.setButton(ConfigUtil.SETTING_CONFIG, gui, "invitation");
        HandyInventoryUtil.setButton(ConfigUtil.SETTING_CONFIG, gui, "logoShop");
        HandyInventoryUtil.setButton(ConfigUtil.SETTING_CONFIG, gui, "logoWarehouse");
        HandyInventoryUtil.setButton(ConfigUtil.SETTING_CONFIG, gui, "apply");
        HandyInventoryUtil.setButton(ConfigUtil.SETTING_CONFIG, gui, "pvp", this.buildPvpButtonMap(guildInfo));
        HandyInventoryUtil.setButton(ConfigUtil.SETTING_CONFIG, gui, "setSpawn", this.buildSpawnPlaceholders(guildInfo));
        if (StrUtil.isEmpty(guildInfo.getWorldName())) {
            HandyInventoryUtil.setButton(ConfigUtil.SETTING_CONFIG, gui, "createGuildWorld", this.buildCreateWorldButtonMap(guildInfo));
            member = currentGuildPlayer;
        } else {
            HandyInventoryUtil.setButton(ConfigUtil.SETTING_CONFIG, gui, "tpGuildWorld");
            member = currentGuildPlayer;
        }
        if (member != null && currentGuildPlayer.getRoleId() < GuildRoleEnum.TWO.getRoleId()) {
            HandyInventoryUtil.setButton(ConfigUtil.SETTING_CONFIG, gui, "dissolution");
        }
        HandyInventoryUtil.setButton(ConfigUtil.SETTING_CONFIG, gui, "description", this.buildDescriptionButtonMap(guildInfo), GuildUtil.getDescriptionMap(guildInfo.getDescription()));
        HandyInventoryUtil.setButton(ConfigUtil.SETTING_CONFIG, gui, "back");
        HandyInventoryUtil.setButton(ConfigUtil.SETTING_CONFIG, gui, "joinMode", this.xxXxXx(guildInfo));
        HandyInventoryUtil.setButton(ConfigUtil.SETTING_CONFIG, gui, "pane");
        HandyInventoryUtil.setCustomButton(ConfigUtil.SETTING_CONFIG, gui, "custom");
    }

    public static GuildSettingGui getInstance() {
        return INSTANCE;
    }

    private Map<String, String> buildUpButtonMap(GuildInfo info) {
        HashMap<String, String> placeholders = MapUtil.newHashMapWithExpectedSize(12);
        GuildInfo guildInfo = info;
        Integer money = guildInfo.getMoney();
        Integer prosperity = guildInfo.getProsperityDegree();
        Integer level = guildInfo.getLevel();
        String buttonText = ConfigUtil.SETTING_CONFIG.getString("up.noButton");
        int upMoney = BaseConstants.CONFIG.getInt(new StringBuilder().insert(0, "guildSetting.").append(level).append(".upMoney").toString());
        int upProsperity = BaseConstants.CONFIG.getInt(new StringBuilder().insert(0, "guildSetting.").append(level).append(".upProsperityDegree").toString());
        int upMember = BaseConstants.CONFIG.getInt(new StringBuilder().insert(0, "guildSetting.").append(level).append(".upMember").toString(), 0);
        if (prosperity >= upProsperity && money >= upMoney && info.getMemberCount() >= upMember) {
            buttonText = ConfigUtil.SETTING_CONFIG.getString("up.yesButton");
        }
        if (upMoney == 0 && upProsperity == 0 && upMember == 0) {
            buttonText = ConfigUtil.SETTING_CONFIG.getString("up.maxLevel");
        }
        HashMap<String, String> placeholdersRef = placeholders;
        placeholdersRef.put("level", level.toString());
        placeholders.put("button", buttonText);
        placeholders.put("guildMoney", money.toString());
        placeholders.put("guildProsperityDegree", prosperity.toString());
        placeholders.put("upMoney", String.valueOf(upMoney));
        placeholders.put("upProsperityDegree", String.valueOf(upProsperity));
        int maintenanceMoney = BaseConstants.CONFIG.getInt(new StringBuilder().insert(0, "guildSetting.").append(level).append(".maintenanceMoney").toString(), 0);
        int upMaintenanceMoney = BaseConstants.CONFIG.getInt(new StringBuilder().insert(0, "guildSetting.").append(level + 1).append(".maintenanceMoney").toString(), 0);
        placeholders.put("maintenanceMoney", String.valueOf(maintenanceMoney));
        placeholders.put("upMaintenanceMoney", String.valueOf(upMaintenanceMoney));
        maintenanceMoney = BaseConstants.CONFIG.getInt(new StringBuilder().insert(0, "guildSetting.").append(level).append(".maintenanceActive").toString(), 0);
        upMaintenanceMoney = BaseConstants.CONFIG.getInt(new StringBuilder().insert(0, "guildSetting.").append(level + 1).append(".maintenanceActive").toString(), 0);
        placeholders.put("maintenanceActive", String.valueOf(maintenanceMoney));
        placeholders.put("upMaintenanceActive", String.valueOf(upMaintenanceMoney));
        placeholders.put("upMember", String.valueOf(upMember));
        placeholders.put("guildMember", String.valueOf(info.getMemberCount()));
        return placeholdersRef;
    }

    public void setInventoryDate(HandyInventory gui) {
        HandyInventory inventory = gui;
        inventory.setGuiType(GuiTypeEnum.GUILD_SETTING.getType());
        this.fillGui(inventory);
    }

    private Map<String, String> buildCreateWorldButtonMap(GuildInfo guildInfo) {
        String buttonText = ConfigUtil.SETTING_CONFIG.getString("createGuildWorld.createButton");
        if (StrUtil.isNotEmpty(guildInfo.getWorldName())) {
            buttonText = ConfigUtil.SETTING_CONFIG.getString("createGuildWorld.haveButton");
        }
        return MapUtil.of("button", buttonText);
    }

    private GuildSettingGui() {
    }

    private Map<String, String> buildPvpButtonMap(GuildInfo guildInfo) {
        HashMap<String, String> placeholders = MapUtil.newHashMapWithExpectedSize(3);
        String pvpStatusText = ConfigUtil.SETTING_CONFIG.getString("pvp.yesPvp");
        if (guildInfo.getPvpStatus() != null && guildInfo.getPvpStatus().booleanValue()) {
            pvpStatusText = ConfigUtil.SETTING_CONFIG.getString("pvp.noPvp");
        }
        int price = ConfigUtil.SETTING_CONFIG.getInt("pvp.price");
        String previousValue = placeholders.put("price", String.valueOf(price));
        HashMap<String, String> placeholdersRef = placeholders;
        placeholders.put("money", String.valueOf(guildInfo.getMoney()));
        placeholdersRef.put("pvpStatus", pvpStatusText);
        return placeholdersRef;
    }

    public Inventory createGui(Player player, Integer guildId) {
        String title = ConfigUtil.SETTING_CONFIG.getString("title");
        String sound = ConfigUtil.SETTING_CONFIG.getString("sound");
        title = PlaceholderApiUtil.set(player, title);
        int size = ConfigUtil.SETTING_CONFIG.getInt("size", 54);
        HandyInventory handyInventory = new HandyInventory(GuiTypeEnum.GUILD_SETTING.getType(), title, size, sound);
        handyInventory.setPlayer(player);
        handyInventory.setId(guildId);
        this.setInventoryDate(handyInventory);
        return handyInventory.getInventory();
    }

    private Map<String, String> buildDescriptionButtonMap(GuildInfo guildInfo) {
        HashMap<String, String> placeholders = MapUtil.newHashMapWithExpectedSize(3);
        Integer money = guildInfo.getMoney();
        String buttonText = ConfigUtil.SETTING_CONFIG.getString("description.noButton");
        int price = ConfigUtil.SETTING_CONFIG.getInt("description.price");
        if (money >= price) {
            buttonText = ConfigUtil.SETTING_CONFIG.getString("description.yesButton");
        }
        placeholders.put("button", buttonText);
        placeholders.put("guildMoney", money.toString());
        placeholders.put("price", String.valueOf(price));
        return placeholders;
    }

    private Map<String, String> buildEditNameButtonMap(GuildInfo guildInfo) {
        HashMap<String, String> placeholders = MapUtil.newHashMapWithExpectedSize(3);
        Integer money = guildInfo.getMoney();
        String buttonText = ConfigUtil.SETTING_CONFIG.getString("editName.noButton");
        int price = ConfigUtil.SETTING_CONFIG.getInt("editName.price");
        if (money >= price) {
            buttonText = ConfigUtil.SETTING_CONFIG.getString("editName.yesButton");
        }
        placeholders.put("button", buttonText);
        placeholders.put("guildMoney", money.toString());
        placeholders.put("price", String.valueOf(price));
        return placeholders;
    }

    private Map<String, String> buildSpawnPlaceholders(GuildInfo guildInfo) {
        GuildSpawn guildSpawn = new GuildSpawn();
        if (StrUtil.isNotEmpty(guildInfo.getSpawnLocation())) {
            guildSpawn = JsonUtil.toBean(guildInfo.getSpawnLocation(), GuildSpawn.class);
        } else {
            guildSpawn.setWorld(BaseUtil.getLangMsg("notSet", "未设置"));
            guildSpawn.setX(0.0);
            guildSpawn.setY(0.0);
            guildSpawn.setZ(0.0);
        }
        Map<String, String> placeholders = MapUtil.newHashMapWithExpectedSize(4);
        placeholders.put("world", GuildWorldUtil.getWorldAlias(guildSpawn.getWorld()));
        placeholders.put("x", (int)guildSpawn.getX() + "");
        placeholders.put("y", (int)guildSpawn.getY() + "");
        placeholders.put("z", (int)guildSpawn.getZ() + "");
        return placeholders;
    }

    private Map<String, String> xxXxXx(GuildInfo guildInfo) {
        HashMap<String, String> placeholders = MapUtil.newHashMapWithExpectedSize(3);
        String joinStatus = ConfigUtil.SETTING_CONFIG.getString("joinMode.apply");
        if (guildInfo.getJoinMode() != null && !guildInfo.getJoinMode().booleanValue()) {
            joinStatus = ConfigUtil.SETTING_CONFIG.getString("joinMode.auto");
        }
        int price = ConfigUtil.SETTING_CONFIG.getInt("joinMode.price");
        String previousValue = placeholders.put("price", String.valueOf(price));
        HashMap<String, String> placeholdersRef = placeholders;
        placeholders.put("money", String.valueOf(guildInfo.getMoney()));
        placeholdersRef.put("joinStatus", joinStatus);
        return placeholdersRef;
    }
}
