package cn.handyplus.guild.inventory;

import cn.handyplus.guild.constants.ApplyTypeEnum;
import cn.handyplus.guild.constants.GuiTypeEnum;
import cn.handyplus.guild.constants.GuildSortEnum;
import cn.handyplus.guild.enter.GuildApply;
import cn.handyplus.guild.enter.GuildInfo;
import cn.handyplus.guild.enter.GuildPlayer;
import cn.handyplus.guild.hook.PlaceholderApiUtil;
import cn.handyplus.guild.lib.BaseConstants;
import cn.handyplus.guild.lib.CollUtil;
import cn.handyplus.guild.lib.DateUtil;
import cn.handyplus.guild.lib.HandyInventory;
import cn.handyplus.guild.lib.HandyInventoryUtil;
import cn.handyplus.guild.lib.ItemMetaUtil;
import cn.handyplus.guild.lib.ItemStackUtil;
import cn.handyplus.guild.lib.MapUtil;
import cn.handyplus.guild.lib.MessageUtil;
import cn.handyplus.guild.lib.Page;
import cn.handyplus.guild.lib.StrUtil;
import cn.handyplus.guild.service.GuildApplyService;
import cn.handyplus.guild.service.GuildInfoService;
import cn.handyplus.guild.service.GuildPlayerService;
import cn.handyplus.guild.util.ConfigUtil;
import cn.handyplus.guild.util.GuildUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class GuildOpenGui {
    private final static GuildOpenGui INSTANCE = new GuildOpenGui();

    public Inventory createGui(Player player, String searchType) {
        String title = ConfigUtil.OPEN_CONFIG.getString("title");
        String sound = ConfigUtil.OPEN_CONFIG.getString("sound");
        title = PlaceholderApiUtil.set(player, title);
        int size = ConfigUtil.OPEN_CONFIG.getInt("size", 54);
        HandyInventory handyInventory = new HandyInventory(GuiTypeEnum.GUILD_OPEN.getType(), title, size, sound);
        handyInventory.setPlayer(player);
        handyInventory.setSearchType(searchType);
        this.setInventoryDate(handyInventory);
        return handyInventory.getInventory();
    }

    public void setInventoryDate(HandyInventory gui) {
        gui.setGuiType(GuiTypeEnum.GUILD_OPEN.getType());
        HandyInventoryUtil.refreshInventory(gui.getInventory());
        this.fillGuildItems(gui);
        this.addNavigationButtons(gui);
    }

    public static GuildOpenGui getInstance() {
        return INSTANCE;
    }

    private Map<String, String> buildGuildPlaceholders(GuildInfo guildInfo, GuildApply guildApply, GuildPlayer guildPlayer, GuildPlayer owner) {
        HashMap<String, String> placeholders = MapUtil.newHashMapWithExpectedSize(13);
        placeholders.put("prosperity_degree", guildInfo.getProsperityDegree().toString());
        placeholders.put("month_prosperity_degree", guildInfo.getMonthProsperityDegree().toString());
        placeholders.put("guild_name", guildInfo.getGuildName());
        placeholders.put("guild_owner", owner.getPlayerName());
        placeholders.put("guild_creator", guildInfo.getCreator());
        placeholders.put("guild_money", guildInfo.getMoney().toString());
        placeholders.put("guild_level", guildInfo.getLevel().toString());
        placeholders.put("guild_member_count", guildInfo.getMemberCount().toString());
        placeholders.put("guild_max_member_count", guildInfo.getMemberMaxCount().toString());
        placeholders.put("guild_creation_time", DateUtil.format(guildInfo.getCreateTime(), "yyyy-MM-dd"));
        String joinMode = ConfigUtil.SETTING_CONFIG.getString("joinMode.apply");
        if (guildInfo.getJoinMode() != null && !guildInfo.getJoinMode().booleanValue()) {
            joinMode = ConfigUtil.SETTING_CONFIG.getString("joinMode.auto");
        }
        placeholders.put("join_mode", joinMode);
        String viewButton = ConfigUtil.OPEN_CONFIG.getString("view");
        String noViewButton = ConfigUtil.OPEN_CONFIG.getString("noView");
        String applyButton = ConfigUtil.OPEN_CONFIG.getString("apply");
        String buttonText = ConfigUtil.OPEN_CONFIG.getString("join");
        if (guildApply != null && guildApply.getGuildInfoId().equals(guildInfo.getId())) {
            buttonText = applyButton;
        } else if (guildPlayer != null) {
            if (guildPlayer.getGuildInfoId().equals(guildInfo.getId())) {
                buttonText = viewButton;
            } else {
                buttonText = noViewButton;
            }
        }
        placeholders.put("button", buttonText);
        placeholders.put("guild_season_rank", guildInfo.getSeasonRank().toString());
        return placeholders;
    }

    private void fillGuildItems(HandyInventory gui) {
        Inventory inventory = gui.getInventory();
        Player player = gui.getPlayer();
        Map<Integer, Integer> intMap = gui.getIntMap();
        List<Integer> slotList = StrUtil.strToIntList(
                ConfigUtil.OPEN_CONFIG.getString("guild.index",
                        "20,21,22,23,24,29,30,31,32,33"));
        gui.setPageSize(slotList.size());
        Page page = GuildInfoService.getInstance().page(gui.getPageNum(), gui.getPageSize(), gui.getSearchType());
        gui.setPageCount(page.getTotal());
        List<GuildInfo> records = page.getRecords();
        if (CollUtil.isEmpty(records)) {
            return;
        }
        Map<Integer, GuildPlayer> roleMap = GuildPlayerService.getInstance()
                .findRoleByGuildInfoIds(records.stream().map(GuildInfo::getId).collect(Collectors.toList()))
                .stream()
                .collect(Collectors.groupingBy(GuildPlayer::getGuildInfoId,
                        Collectors.collectingAndThen(Collectors.toList(), (List<GuildPlayer> list) -> list.get(0))));
        GuildPlayer guildPlayer = GuildPlayerService.getInstance().findByPlayerUuid(player.getUniqueId()).orElse(null);
        GuildApply guildApply = null;
        if (guildPlayer == null) {
            guildApply = GuildApplyService.getInstance()
                    .findByPlayerUuid(player.getUniqueId(), ApplyTypeEnum.PENDING.getId()).orElse(null);
        }
        String nameFormat = ConfigUtil.OPEN_CONFIG.getString("guild.name", "");
        boolean enchant = ConfigUtil.OPEN_CONFIG.getBoolean("guild.isEnchant");
        nameFormat = StrUtil.isNotEmpty(nameFormat) ? nameFormat : "";
        List<String> loreTemplate = ConfigUtil.OPEN_CONFIG.getStringList("guild.lore");
        String tooltipStyle = ConfigUtil.OPEN_CONFIG.getString("guild.tooltipStyle");
        String itemModel = ConfigUtil.OPEN_CONFIG.getString("guild.itemModel");
        LinkedHashMap<Integer, ItemStack> itemMap = new LinkedHashMap<Integer, ItemStack>();
        for (GuildInfo guildInfo : records) {
            List<String> lore = new ArrayList<String>();
            String name = nameFormat.replace("${guild_name}", guildInfo.getGuildName())
                    .replace("${guild_level}", guildInfo.getLevel().toString());
            GuildPlayer member = roleMap.get(guildInfo.getId());
            if (member == null) {
                MessageUtil.sendConsoleMessage(
                        "出现异常公会 " + guildInfo.getGuildName()
                                + "ID:" + guildInfo.getId()
                                + "异常原因:没有会长");
                continue;
            }
            Map<String, String> placeholderMap = this.buildGuildPlaceholders(guildInfo, guildApply, guildPlayer, member);
            for (String template : loreTemplate) {
                for (String key : placeholderMap.keySet()) {
                    template = template.replace("${" + key + "}",
                            placeholderMap.get(key));
                }
                lore.add(template);
            }
            lore = ItemStackUtil.loreBatchReplaceMap(lore, GuildUtil.getDescriptionMap(guildInfo.getDescription()), null);
            ItemStack itemStack = ItemStackUtil.itemStackDeserialize(guildInfo.getLogoName());
            ItemMeta itemMeta = ItemStackUtil.getItemMeta(itemStack);
            ItemMetaUtil.setDisplayName(itemMeta, name);
            lore = PlaceholderApiUtil.set(member.getPlayerUuid(), lore);
            ItemMetaUtil.setLore(itemMeta, lore);
            if (enchant) {
                ItemMetaUtil.setEnchant(itemMeta);
            }
            if (itemMeta instanceof SkullMeta) {
                if (BaseConstants.CONFIG.getBoolean("setDefaultHead", true)) {
                    ItemMetaUtil.setOwner((SkullMeta)itemMeta, member.getPlayerName());
                }
            }
            ItemMetaUtil.hideAttributes(itemMeta);
            ItemMetaUtil.hideEnchant(itemMeta);
            ItemMetaUtil.setTooltipStyle(itemMeta, tooltipStyle);
            ItemMetaUtil.setItemModel(itemMeta, itemModel);
            itemStack.setItemMeta(itemMeta);
            itemMap.put(guildInfo.getId(), itemStack);
        }
        int index = 0;
        for (Integer guildInfoId : itemMap.keySet()) {
            Integer slot = slotList.get(index);
            ++index;
            inventory.setItem(slot.intValue(), itemMap.get(guildInfoId));
            intMap.put(slot, guildInfoId);
        }
    }

    private Map<String, String> buildSortButtonMap(HandyInventory gui) {
        String searchType = gui.getSearchType();
        GuildSortEnum guildSortEnum = GuildSortEnum.getEnum(searchType);
        GuildSortEnum guildSortEnum2 = GuildSortEnum.getNextEnum(searchType);
        HashMap<String, String> placeholders = MapUtil.newHashMapWithExpectedSize(2);
        placeholders.put("type", GuildSortEnum.getTypeName(guildSortEnum));
        placeholders.put("nextType", GuildSortEnum.getTypeName(guildSortEnum2));
        return placeholders;
    }

    private GuildOpenGui() {
    }

    private void addNavigationButtons(HandyInventory gui) {
        Player player = gui.getPlayer();
        Map<Integer, Integer> intMap = gui.getIntMap();
        Map<String, String> pageMap = HandyInventoryUtil.replacePageMap(gui);
        HandyInventoryUtil.setButton(ConfigUtil.OPEN_CONFIG, gui, "nextPage", pageMap);
        HandyInventoryUtil.setButton(ConfigUtil.OPEN_CONFIG, gui, "previousPage", pageMap);
        HandyInventoryUtil.setButton(ConfigUtil.OPEN_CONFIG, gui, "info");
        GuildPlayer guildPlayer = GuildPlayerService.getInstance()
                .findByPlayerUuid(gui.getPlayer().getUniqueId()).orElse(null);
        if (guildPlayer != null) {
            if (ConfigUtil.OPEN_CONFIG.getBoolean("me.enable")) {
                HandyInventoryUtil.setButton(ConfigUtil.OPEN_CONFIG, gui, "me");
                for (Integer slot : StrUtil.strToIntList(
                        ConfigUtil.OPEN_CONFIG.getString("me.index"))) {
                    intMap.put(slot, guildPlayer.getGuildInfoId());
                }
            }
        } else if (player.hasPermission("playerGuild.create")) {
            HandyInventoryUtil.setButton(ConfigUtil.OPEN_CONFIG, gui, "create");
        }
        HandyInventoryUtil.setButton(ConfigUtil.OPEN_CONFIG, gui, "search", this.buildSortButtonMap(gui));
        HandyInventoryUtil.setCustomButton(ConfigUtil.OPEN_CONFIG, gui, "custom");
    }
}
