package cn.handyplus.guild.inventory;

import cn.handyplus.guild.constants.GuiTypeEnum;
import cn.handyplus.guild.constants.GuildRoleEnum;
import cn.handyplus.guild.enter.GuildPlayer;
import cn.handyplus.guild.hook.PlaceholderApiUtil;
import cn.handyplus.guild.lib.BaseUtil;
import cn.handyplus.guild.lib.DateUtil;
import cn.handyplus.guild.lib.HandyInventory;
import cn.handyplus.guild.lib.HandyInventoryUtil;
import cn.handyplus.guild.lib.ItemStackUtil;
import cn.handyplus.guild.lib.MapUtil;
import cn.handyplus.guild.lib.Page;
import cn.handyplus.guild.lib.StrUtil;
import cn.handyplus.guild.service.GuildPlayerService;
import cn.handyplus.guild.util.ConfigUtil;
import cn.handyplus.guild.util.GuildUtil;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class GuildMemberGui {
    private final static GuildMemberGui INSTANCE = new GuildMemberGui();

    public void setInventoryDate(HandyInventory inventory) {
        HandyInventory handyInventory = inventory;
        inventory.setGuiType(GuiTypeEnum.GUILD_MEMBER.getType());
        HandyInventoryUtil.refreshInventory(handyInventory.getInventory());
        this.J(inventory);
        this.setPageButtons(handyInventory);
        GuildUtil.setHead(handyInventory.getInventory());
    }

    public static long getOfflineDay(GuildPlayer guildPlayer) {
        if (guildPlayer.getLastJoinTime() != null && guildPlayer.getLastQuitTime() != null && guildPlayer.getLastJoinTime().getTime() > guildPlayer.getLastQuitTime().getTime()) {
            return 0L;
        }
        if (guildPlayer.getLastQuitTime() == null) {
            return 0L;
        }
        if (guildPlayer.getLastJoinTime() != null) {
            return DateUtil.between(guildPlayer.getLastQuitTime(), new Date(), ChronoUnit.DAYS);
        }
        return 0L;
    }

    private void J(HandyInventory guiInventory) {
        HandyInventory handyInventory = guiInventory;
        Inventory inventory = handyInventory.getInventory();
        Map<Integer, Integer> intMap = handyInventory.getIntMap();
        Map<Integer, Object> objMap = handyInventory.getObjMap();
        List<Integer> slots = StrUtil.strToIntList(ConfigUtil.MEMBER_CONFIG.getString("member.index", "20,21,22,23,24,29,30,31,32,33"));
        handyInventory.setPageSize(slots.size());
        Page<GuildPlayer> page = GuildPlayerService.getInstance().page(guiInventory.getId(), guiInventory.getPageNum(), guiInventory.getPageSize());
        handyInventory.setPageCount(page.getTotal());
        if (page.getTotal() < 1) {
            return;
        }
        List<GuildPlayer> records = page.getRecords();
        String nameFormat = ConfigUtil.MEMBER_CONFIG.getString("member.name", "");
        String material = ConfigUtil.MEMBER_CONFIG.getString("member.material");
        List<String> lore = ConfigUtil.MEMBER_CONFIG.getStringList("member.lore");
        boolean isEnchant = ConfigUtil.MEMBER_CONFIG.getBoolean("member.isEnchant", false);
        int customModelData = ConfigUtil.MEMBER_CONFIG.getInt("member.custom-model-data");
        boolean hideFlag = ConfigUtil.MEMBER_CONFIG.getBoolean("member.hideFlag", true);
        LinkedHashMap<Integer, ItemStack> itemMap = new LinkedHashMap<Integer, ItemStack>();
        LinkedHashMap<Integer, String> playerNameMap = new LinkedHashMap<Integer, String>();
        for (int i = 0; i < records.size(); ++i) {
            GuildPlayer guildPlayer = records.get(i);
            String roleName = GuildRoleEnum.getRoleName(guildPlayer.getRoleId());
            String name = nameFormat.replace("${player_name}", guildPlayer.getPlayerName()).replace("${player_role}", roleName);
            String index = String.valueOf(1 + (guiInventory.getPageNum() - 1) * slots.size());
            ItemStack itemStack = ItemStackUtil.getItemStack(material, name, lore, isEnchant, customModelData, hideFlag, this.getMemberPlaceholders(guildPlayer, index));
            itemMap.put(guildPlayer.getId(), itemStack);
            playerNameMap.put(guildPlayer.getId(), guildPlayer.getPlayerName());
        }
        int index2 = 0;
        for (Integer playerId : itemMap.keySet()) {
            Integer slot = slots.get(index2);
            ++index2;
            inventory.setItem(slot.intValue(), itemMap.get(playerId));
            intMap.put(slot, playerId);
            objMap.put(slot, playerNameMap.get(playerId));
        }
    }

    private Map<String, String> getImpeachmentPlaceholders(GuildPlayer guildPlayer) {
        long offlineDays = GuildMemberGui.getOfflineDay(guildPlayer);
        int impeachmentDay = ConfigUtil.MEMBER_CONFIG.getInt("impeachment.day", 14);
        boolean impeached = offlineDays > (long)impeachmentDay;
        String status = impeached ? BaseUtil.getLangMsg("impeachmentStatus") : BaseUtil.getLangMsg("impeachmentActiveStatus");
        String button = impeached ? BaseUtil.getLangMsg("impeachmentYesButton") : BaseUtil.getLangMsg("impeachmentNoButton");
        HashMap<String, String> placeholders = MapUtil.newHashMapWithExpectedSize(4);
        String previousValue = placeholders.put("impeachment_status", StrUtil.replace(status, "impeachment_day", String.valueOf(impeachmentDay)));
        HashMap<String, String> resultMap = placeholders;
        placeholders.put("offline_day", String.valueOf(offlineDays));
        resultMap.put("impeachment_day", String.valueOf(impeachmentDay));
        placeholders.put("button", button);
        return resultMap;
    }

    public static GuildMemberGui getInstance() {
        return INSTANCE;
    }

    public Inventory createGui(Player player, Integer guildId) {
        Object title = ConfigUtil.MEMBER_CONFIG.getString("title");
        String sound = ConfigUtil.MEMBER_CONFIG.getString("sound");
        title = PlaceholderApiUtil.set(player, (String)title);
        int size = ConfigUtil.MEMBER_CONFIG.getInt("size", 54);
        Object handyInventory = title = new HandyInventory(GuiTypeEnum.GUILD_MEMBER.getType(), (String)title, size, sound);
        ((HandyInventory)title).setPlayer(player);
        ((HandyInventory)handyInventory).setId(guildId);
        this.setInventoryDate((HandyInventory)handyInventory);
        return ((HandyInventory)handyInventory).getInventory();
    }

    private void setPageButtons(HandyInventory inventory) {
        Map<String, String> pageMap = HandyInventoryUtil.replacePageMap(inventory);
        HandyInventoryUtil.setButton(ConfigUtil.MEMBER_CONFIG, inventory, "nextPage", pageMap);
        HandyInventoryUtil.setButton(ConfigUtil.MEMBER_CONFIG, inventory, "previousPage", pageMap);
        HandyInventoryUtil.setButton(ConfigUtil.MEMBER_CONFIG, inventory, "back");
        HandyInventoryUtil.setButton(ConfigUtil.MEMBER_CONFIG, inventory, "pane");
        GuildPlayerService.getInstance().findOneRoleByGuildId(inventory.getId()).ifPresent(guildPlayer -> HandyInventoryUtil.setButton(ConfigUtil.MEMBER_CONFIG, inventory, "impeachment", this.getImpeachmentPlaceholders((GuildPlayer)guildPlayer)));
    }

    private Map<String, String> getMemberPlaceholders(GuildPlayer guildPlayer, String ranking) {
        HashMap<String, String> placeholders;
        HashMap<String, String> resultMap = placeholders = MapUtil.newHashMapWithExpectedSize(11);
        placeholders.put("player_name", guildPlayer.getPlayerName());
        resultMap.put("player_ranking", ranking);
        placeholders.put("player_role", GuildRoleEnum.getRoleName(guildPlayer.getRoleId()));
        placeholders.put("player_money", guildPlayer.getMoney().toString());
        placeholders.put("player_total_money", guildPlayer.getTotalMoney().toString());
        placeholders.put("player_week_money", guildPlayer.getWeekMoney().toString());
        placeholders.put("player_guild_money", guildPlayer.getGuildMoney().toString());
        resultMap.put("total_ore", guildPlayer.getTotalOre() != null ? guildPlayer.getTotalOre().toString() : "0");
        placeholders.put("player_join_time", DateUtil.format(guildPlayer.getJoinTime(), "yyyy-MM-dd HH:mm:ss"));
        placeholders.put("last_join_time", guildPlayer.getLastJoinTime() != null ? DateUtil.format(guildPlayer.getLastJoinTime(), "yyyy-MM-dd HH:mm:ss") : "");
        placeholders.put("last_quit_time", guildPlayer.getLastQuitTime() != null ? DateUtil.format(guildPlayer.getLastQuitTime(), "yyyy-MM-dd HH:mm:ss") : "");
        ranking = ConfigUtil.MEMBER_CONFIG.getString("member.online", "");
        String offlineText = ConfigUtil.MEMBER_CONFIG.getString("member.offline", "");
        if (guildPlayer.getLastJoinTime() != null && guildPlayer.getLastQuitTime() != null && guildPlayer.getLastJoinTime().getTime() > guildPlayer.getLastQuitTime().getTime()) {
            placeholders.put("player_status", ranking);
        }
        if (guildPlayer.getLastQuitTime() == null) {
            placeholders.put("player_status", ranking);
        }
        if (guildPlayer.getLastJoinTime() != null && guildPlayer.getLastQuitTime() != null && guildPlayer.getLastJoinTime().getTime() <= guildPlayer.getLastQuitTime().getTime()) {
            long offlineDays = DateUtil.between(guildPlayer.getLastQuitTime(), new Date(), ChronoUnit.DAYS);
            placeholders.put("player_status", offlineText.replace("${day}", offlineDays + ""));
        }
        return placeholders;
    }

    private GuildMemberGui() {
    }
}
