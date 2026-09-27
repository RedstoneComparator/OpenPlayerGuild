package cn.handyplus.guild.inventory;

import cn.handyplus.guild.constants.GuiTypeEnum;
import cn.handyplus.guild.constants.GuildRoleEnum;
import cn.handyplus.guild.enter.GuildInfo;
import cn.handyplus.guild.enter.GuildNotice;
import cn.handyplus.guild.enter.GuildPlayer;
import cn.handyplus.guild.hook.PlaceholderApiUtil;
import cn.handyplus.guild.lib.DateUtil;
import cn.handyplus.guild.lib.HandyInventory;
import cn.handyplus.guild.lib.HandyInventoryUtil;
import cn.handyplus.guild.lib.ItemStackUtil;
import cn.handyplus.guild.lib.MapUtil;
import cn.handyplus.guild.lib.Page;
import cn.handyplus.guild.lib.StrUtil;
import cn.handyplus.guild.service.GuildInfoService;
import cn.handyplus.guild.service.GuildNoticeReadService;
import cn.handyplus.guild.service.GuildNoticeService;
import cn.handyplus.guild.service.GuildPlayerService;
import cn.handyplus.guild.util.ConfigUtil;
import cn.handyplus.guild.util.GuildUtil;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class GuildNoticeGui {
    private final static GuildNoticeGui INSTANCE = new GuildNoticeGui();

    private Map<String, String> buildNoticePlaceholders(GuildPlayer guildPlayer, GuildNotice guildNotice, List<Integer> readIds, Map<Integer, Integer> readMap) {
        HashMap<String, String> placeholders = MapUtil.newHashMapWithExpectedSize(5);
        placeholders.put("player_name", guildNotice.getSendPlayerName());
        placeholders.put("player_role", GuildRoleEnum.getRoleName(guildNotice.getSendPlayerRoleId()));
        placeholders.put("create_time", DateUtil.format(guildNotice.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
        String buttonText = ConfigUtil.NOTICE_CONFIG.getString("noRead", "");
        if (readIds.contains(guildNotice.getId())) {
            buttonText = ConfigUtil.NOTICE_CONFIG.getString("read", "");
        }
        if (guildPlayer != null && guildPlayer.getRoleId() < GuildRoleEnum.TWO.getRoleId()) {
            buttonText = ConfigUtil.NOTICE_CONFIG.getString("del", "");
        }
        placeholders.put("button", buttonText);
        Integer readNumber = readMap.get(guildNotice.getId());
        placeholders.put("read_number", readNumber == null ? "0" : readNumber.toString());
        return placeholders;
    }

    private GuildNoticeGui() {
    }

    public Inventory createGui(Player player, Integer guildId, String searchType) {
        String title = ConfigUtil.NOTICE_CONFIG.getString("title");
        String sound = ConfigUtil.NOTICE_CONFIG.getString("sound");
        if ("noRead".equals(searchType)) {
            title = ConfigUtil.NOTICE_CONFIG.getString("noReadTitle");
        }
        title = PlaceholderApiUtil.set(player, title);
        int size = ConfigUtil.NOTICE_CONFIG.getInt("size", 54);
        HandyInventory handyInventory = new HandyInventory(GuiTypeEnum.GUILD_NOTICE.getType(), title, size, sound);
        handyInventory.setPlayer(player);
        handyInventory.setSearchType(searchType);
        handyInventory.setId(guildId);
        this.setInventoryDate(handyInventory);
        return handyInventory.getInventory();
    }

    public static GuildNoticeGui getInstance() {
        return INSTANCE;
    }

    public void setInventoryDate(HandyInventory gui) {
        HandyInventory inventory = gui;
        inventory.setGuiType(GuiTypeEnum.GUILD_NOTICE.getType());
        HandyInventoryUtil.refreshInventory(inventory.getInventory());
        GuildNoticeGui instance = this;
        instance.fillNoticeItems(gui);
        instance.addNavigationButtons(inventory);
    }

    private Map<String, String> buildSendButtonMap(GuildInfo guildInfo) {
        HashMap<String, String> placeholders = MapUtil.newHashMapWithExpectedSize(3);
        Integer money = guildInfo.getMoney();
        String buttonText = ConfigUtil.NOTICE_CONFIG.getString("send.noButton");
        int price = ConfigUtil.NOTICE_CONFIG.getInt("send.price");
        if (money >= price) {
            buttonText = ConfigUtil.NOTICE_CONFIG.getString("send.yesButton");
        }
        placeholders.put("button", buttonText);
        placeholders.put("guildMoney", money.toString());
        placeholders.put("price", String.valueOf(price));
        return placeholders;
    }

    private Map<String, String> buildSearchButtonMap(HandyInventory gui) {
        HashMap<String, String> placeholders = MapUtil.newHashMapWithExpectedSize(2);
        String searchType = gui.getSearchType();
        String noRead = ConfigUtil.NOTICE_CONFIG.getString("search.noRead");
        String all = ConfigUtil.NOTICE_CONFIG.getString("search.all");
        if ("all".equals(searchType)) {
            placeholders.put("type", all);
            placeholders.put("nextType", noRead);
        }
        if ("noRead".equals(searchType)) {
            placeholders.put("type", noRead);
            placeholders.put("nextType", all);
        }
        return placeholders;
    }

    private void addNavigationButtons(HandyInventory gui) {
        GuildPlayer guildPlayer = GuildPlayerService.getInstance().findByPlayerUuid(gui.getPlayer().getUniqueId()).orElse(null);
        Map<String, String> map = HandyInventoryUtil.replacePageMap(gui);
        HandyInventoryUtil.setButton(ConfigUtil.NOTICE_CONFIG, gui, "nextPage", map);
        HandyInventoryUtil.setButton(ConfigUtil.NOTICE_CONFIG, gui, "previousPage", map);
        HandyInventoryUtil.setButton(ConfigUtil.NOTICE_CONFIG, gui, "back");
        if (guildPlayer != null && guildPlayer.getRoleId() < GuildRoleEnum.THREE.getRoleId()) {
            GuildInfoService.getInstance().findById(gui.getId()).ifPresent(guildInfo -> HandyInventoryUtil.setButton(ConfigUtil.NOTICE_CONFIG, gui, "send", this.buildSendButtonMap(guildInfo)));
        }
        HandyInventoryUtil.setButton(ConfigUtil.NOTICE_CONFIG, gui, "pane");
        HandyInventory handyInventory = gui;
        HandyInventoryUtil.setButton(ConfigUtil.NOTICE_CONFIG, handyInventory, "search", this.buildSearchButtonMap(handyInventory));
    }

    private void fillNoticeItems(HandyInventory gui) {
        Inventory inventory = gui.getInventory();
        Player player = gui.getPlayer();
        Map<Integer, Integer> intMap = gui.getIntMap();
        Integer guildInfoId = gui.getId();
        String searchType = gui.getSearchType();
        List<Integer> slotList = StrUtil.strToIntList(ConfigUtil.NOTICE_CONFIG.getString("notice.index", "20,21,22,23,24,29,30,31,32,33"));
        gui.setPageSize(slotList.size());
        List<Integer> readIds = GuildNoticeReadService.getInstance().findGuildNoticeId(guildInfoId, player.getUniqueId());
        Page<GuildNotice> page = GuildNoticeService.getInstance().page(guildInfoId, gui.getPageNum(), gui.getPageSize(), readIds, searchType);
        gui.setPageCount(page.getTotal());
        if (page.getTotal() < 1) {
            return;
        }
        List<GuildNotice> records = page.getRecords();
        List<Integer> noticeIds = records.stream().map(GuildNotice::getId).collect(Collectors.toList());
        Map<Integer, Integer> readMap = GuildNoticeReadService.getInstance().findMapByGuildNoticeIds(noticeIds);
        GuildPlayer guildPlayer = GuildPlayerService.getInstance().findByPlayerUuid(player.getUniqueId()).orElse(null);
        String titleFormat = ConfigUtil.NOTICE_CONFIG.getString("notice.name", "");
        String itemType = ConfigUtil.NOTICE_CONFIG.getString("notice.material");
        List<String> loreList = ConfigUtil.NOTICE_CONFIG.getStringList("notice.lore");
        int customModel = ConfigUtil.NOTICE_CONFIG.getInt("notice.custom-model-data");
        boolean hideEnchant = ConfigUtil.NOTICE_CONFIG.getBoolean("notice.hideFlag", true);
        LinkedHashMap<Integer, ItemStack> itemMap = new LinkedHashMap<Integer, ItemStack>();
        for (GuildNotice guildNotice : records) {
            String title = titleFormat.replace("${title}", guildNotice.getTitle());
            List<String> lore = ItemStackUtil.loreBatchReplaceMap(loreList, GuildUtil.getDescriptionMap(guildNotice.getContent()), null);
            ItemStack itemStack = ItemStackUtil.getItemStack(itemType, title, lore, !readIds.contains(guildNotice.getId()), customModel, hideEnchant, this.buildNoticePlaceholders(guildPlayer, guildNotice, readIds, readMap));
            itemMap.put(guildNotice.getId(), itemStack);
        }
        int index = 0;
        for (Integer noticeId : itemMap.keySet()) {
            Integer slot = slotList.get(index);
            ++index;
            inventory.setItem(slot.intValue(), itemMap.get(noticeId));
            intMap.put(slot, noticeId);
        }
    }
}
