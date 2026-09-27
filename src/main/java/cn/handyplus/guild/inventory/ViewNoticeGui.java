package cn.handyplus.guild.inventory;

import cn.handyplus.guild.constants.GuiTypeEnum;
import cn.handyplus.guild.constants.GuildRoleEnum;
import cn.handyplus.guild.enter.GuildNotice;
import cn.handyplus.guild.hook.PlaceholderApiUtil;
import cn.handyplus.guild.lib.DateUtil;
import cn.handyplus.guild.lib.HandyInventory;
import cn.handyplus.guild.lib.HandyInventoryUtil;
import cn.handyplus.guild.lib.ItemStackUtil;
import cn.handyplus.guild.lib.MapUtil;
import cn.handyplus.guild.lib.Page;
import cn.handyplus.guild.lib.StrUtil;
import cn.handyplus.guild.service.GuildNoticeService;
import cn.handyplus.guild.util.ConfigUtil;
import cn.handyplus.guild.util.GuildUtil;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ViewNoticeGui {
    private final static ViewNoticeGui INSTANCE = new ViewNoticeGui();

    public Inventory createGui(Player player) {
        String title = ConfigUtil.NOTICE_CONFIG.getString("adminTitle");
        String sound = ConfigUtil.NOTICE_CONFIG.getString("sound");
        title = PlaceholderApiUtil.set(player, title);
        int size = ConfigUtil.NOTICE_CONFIG.getInt("size", 54);
        HandyInventory handyInventory = new HandyInventory(GuiTypeEnum.VIEW_NOTICE.getType(), title, size, sound);
        handyInventory.setPlayer(player);
        this.setInventoryDate(handyInventory);
        return handyInventory.getInventory();
    }

    private void fillNoticeItems(HandyInventory gui) {
        HandyInventory inventoryGui = gui;
        Inventory inventory = gui.getInventory();
        Map<Integer, Integer> map = gui.getIntMap();
        List<Integer> list = StrUtil.strToIntList(ConfigUtil.NOTICE_CONFIG.getString("notice.index", "20,21,22,23,24,29,30,31,32,33"));
        inventoryGui.setPageSize(list.size());
        Page<GuildNotice> page = GuildNoticeService.getInstance().page(gui.getPageNum(), gui.getPageSize());
        inventoryGui.setPageCount(page.getTotal());
        if (page.getTotal() < 1) {
            return;
        }
        List<GuildNotice> records = page.getRecords();
        String nameFormat = ConfigUtil.NOTICE_CONFIG.getString("adminNotice.name", "");
        String itemType = ConfigUtil.NOTICE_CONFIG.getString("adminNotice.material");
        List<String> loreTemplate = ConfigUtil.NOTICE_CONFIG.getStringList("adminNotice.lore");
        int customModel = ConfigUtil.NOTICE_CONFIG.getInt("adminNotice.custom-model-data");
        boolean hideEnchant = ConfigUtil.NOTICE_CONFIG.getBoolean("notice.hideFlag", true);
        LinkedHashMap<Integer, ItemStack> itemMap = new LinkedHashMap<Integer, ItemStack>();
        for (GuildNotice guildNotice : records) {
            String title = nameFormat.replace("${title}", guildNotice.getTitle());
            List<String> lore = ItemStackUtil.loreBatchReplaceMap(loreTemplate, GuildUtil.getDescriptionMap(guildNotice.getContent()), null);
            ItemStack itemStack = ItemStackUtil.getItemStack(itemType, title, lore, false, customModel, hideEnchant, this.buildNoticePlaceholders(guildNotice));
            itemMap.put(guildNotice.getId(), itemStack);
        }
        int index = 0;
        for (Integer noticeId : itemMap.keySet()) {
            Integer slot = list.get(index);
            ++index;
            inventory.setItem(slot.intValue(), itemMap.get(noticeId));
            map.put(slot, noticeId);
        }
    }

    public static ViewNoticeGui getInstance() {
        return INSTANCE;
    }

    private Map<String, String> buildNoticePlaceholders(GuildNotice guildNotice) {
        HashMap<String, String> placeholders = MapUtil.newHashMapWithExpectedSize(4);
        placeholders.put("player_name", guildNotice.getSendPlayerName());
        placeholders.put("guild_name", guildNotice.getGuildInfoName());
        placeholders.put("player_role", GuildRoleEnum.getRoleName(guildNotice.getSendPlayerRoleId()));
        placeholders.put("create_time", DateUtil.format(guildNotice.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
        return placeholders;
    }

    private void addNavigationButtons(HandyInventory gui) {
        Map<String, String> map = HandyInventoryUtil.replacePageMap(gui);
        HandyInventoryUtil.setButton(ConfigUtil.NOTICE_CONFIG, gui, "nextPage", map);
        HandyInventoryUtil.setButton(ConfigUtil.NOTICE_CONFIG, gui, "previousPage", map);
        HandyInventoryUtil.setButton(ConfigUtil.NOTICE_CONFIG, gui, "back");
        HandyInventoryUtil.setButton(ConfigUtil.NOTICE_CONFIG, gui, "pane");
    }

    private ViewNoticeGui() {
    }

    public void setInventoryDate(HandyInventory gui) {
        HandyInventory inventory = gui;
        inventory.setGuiType(GuiTypeEnum.VIEW_NOTICE.getType());
        HandyInventoryUtil.refreshInventory(inventory.getInventory());
        ViewNoticeGui instance = this;
        instance.fillNoticeItems(gui);
        instance.addNavigationButtons(inventory);
    }
}
