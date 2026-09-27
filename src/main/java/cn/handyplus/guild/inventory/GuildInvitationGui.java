package cn.handyplus.guild.inventory;

import cn.handyplus.guild.constants.GuiTypeEnum;
import cn.handyplus.guild.constants.GuildConstants;
import cn.handyplus.guild.enter.GuildInfo;
import cn.handyplus.guild.enter.GuildPlayer;
import cn.handyplus.guild.hook.PlaceholderApiUtil;
import cn.handyplus.guild.lib.BaseUtil;
import cn.handyplus.guild.lib.CollUtil;
import cn.handyplus.guild.lib.HandyInventory;
import cn.handyplus.guild.lib.HandyInventoryUtil;
import cn.handyplus.guild.lib.ItemStackUtil;
import cn.handyplus.guild.lib.MapUtil;
import cn.handyplus.guild.lib.StrUtil;
import cn.handyplus.guild.service.GuildInfoService;
import cn.handyplus.guild.service.GuildPlayerService;
import cn.handyplus.guild.util.ConfigUtil;
import cn.handyplus.guild.util.GuildCoolingUtil;
import cn.handyplus.guild.util.GuildUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class GuildInvitationGui {
    private final static GuildInvitationGui INSTANCE = new GuildInvitationGui();

    private List<UUID> getInvitablePlayers(HandyInventory inventory) {
        Integer pageNum = inventory.getPageNum();
        Integer pageSize = inventory.getPageSize();
        ArrayList<UUID> result = new ArrayList<UUID>();
        Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
        inventory.setPageCount(onlinePlayers.size());
        if (CollUtil.isEmpty(onlinePlayers)) {
            return result;
        }
        List<UUID> uuidList = onlinePlayers.stream().map(OfflinePlayer::getUniqueId).collect(Collectors.toList());
        Map<UUID, GuildPlayer> map = GuildPlayerService.getInstance().findByPlayerUuidList(uuidList).stream().collect(Collectors.toMap(GuildPlayer::getPlayerUuid, guildPlayer -> guildPlayer));
        for (UUID uuid : uuidList) {
            if (map.get(uuid) != null) {
                continue;
            }
            result.add(uuid);
        }
        if (pageNum == null) {
            return result;
        }
        return result.stream().skip((long)(pageNum - 1) * (long)pageSize.intValue()).limit(pageSize.intValue()).collect(Collectors.toList());
    }

    private Map<String, String> getMemberPlaceholders(UUID playerUuid, String playerName, GuildInfo guildInfo) {
        HashMap<String, String> placeholders = MapUtil.newHashMapWithExpectedSize(3);
        Integer money = guildInfo.getMoney();
        int price = ConfigUtil.INVITATION_CONFIG.getInt("member.price");
        String button = ConfigUtil.INVITATION_CONFIG.getString("member.noButton");
        if (money >= price) {
            button = ConfigUtil.INVITATION_CONFIG.getString("member.yesButton");
        }
        if (GuildCoolingUtil.getJoinCoolingRemainHours(playerUuid) > 0L) {
            button = GuildCoolingUtil.getInvitationCoolingButton(playerUuid);
        }
        List<Integer> invitationList = GuildConstants.INVITATION_PLAYER_MAP.get(playerName);
        if (CollUtil.isNotEmpty(invitationList) && invitationList.contains(guildInfo.getId())) {
            button = ConfigUtil.INVITATION_CONFIG.getString("member.haveButton");
        }
        placeholders.put("price", price + "");
        placeholders.put("money", money.toString());
        placeholders.put("button", button);
        return placeholders;
    }

    private GuildInvitationGui() {
    }

    public static GuildInvitationGui getInstance() {
        return INSTANCE;
    }

    private void J(HandyInventory inventory) {
        Inventory guiInventory = inventory.getInventory();
        List<Integer> slots = StrUtil.strToIntList(ConfigUtil.INVITATION_CONFIG.getString("member.index", "20,21,22,23,24,29,30,31,32,33"));
        inventory.setPageSize(slots.size());
        Map<Integer, String> playerNameMap = inventory.getStrMap();
        List<UUID> uuidList = this.getInvitablePlayers(inventory);
        if (CollUtil.isEmpty(uuidList)) {
            return;
        }
        java.util.Optional<GuildInfo> optionalGuildInfo = GuildInfoService.getInstance().findById(inventory.getId());
        if (!optionalGuildInfo.isPresent()) {
            return;
        }
        GuildInfo guildInfo = optionalGuildInfo.get();
        String nameFormat = ConfigUtil.INVITATION_CONFIG.getString("member.name", "${player_name}");
        String material = ConfigUtil.INVITATION_CONFIG.getString("member.material");
        List<String> lore = ConfigUtil.INVITATION_CONFIG.getStringList("member.lore");
        boolean isEnchant = ConfigUtil.INVITATION_CONFIG.getBoolean("member.isEnchant", false);
        int customModelData = ConfigUtil.INVITATION_CONFIG.getInt("member.custom-model-data");
        boolean hideFlag = ConfigUtil.INVITATION_CONFIG.getBoolean("member.hideFlag", true);
        LinkedHashMap<String, ItemStack> itemMap = new LinkedHashMap<String, ItemStack>();
        for (UUID uuid : uuidList) {
            OfflinePlayer offlinePlayer = BaseUtil.getOfflinePlayer(uuid);
            if (StrUtil.isEmpty(offlinePlayer.getName())) {
                continue;
            }
            String playerName = offlinePlayer.getName();
            String itemName = nameFormat.replace("${player_name}", playerName);
            lore = PlaceholderApiUtil.set(uuid, lore);
            ItemStack itemStack = ItemStackUtil.getItemStack(material, itemName, lore, isEnchant, customModelData, hideFlag, this.getMemberPlaceholders(uuid, playerName, guildInfo));
            itemMap.put(playerName, itemStack);
        }
        int index = 0;
        for (String name : itemMap.keySet()) {
            Integer slot = slots.get(index);
            ++index;
            guiInventory.setItem(slot.intValue(), itemMap.get(name));
            playerNameMap.put(slot, name);
        }
    }

    private void setPageButtons(HandyInventory inventory) {
        Map<String, String> pageMap = HandyInventoryUtil.replacePageMap(inventory);
        HandyInventoryUtil.setButton(ConfigUtil.INVITATION_CONFIG, inventory, "nextPage", pageMap);
        HandyInventoryUtil.setButton(ConfigUtil.INVITATION_CONFIG, inventory, "previousPage", pageMap);
        HandyInventoryUtil.setButton(ConfigUtil.INVITATION_CONFIG, inventory, "back");
        HandyInventoryUtil.setButton(ConfigUtil.INVITATION_CONFIG, inventory, "pane");
    }

    public void setInventoryDate(HandyInventory inventory) {
        HandyInventory handyInventory = inventory;
        inventory.setGuiType(GuiTypeEnum.GUILD_INVITATION.getType());
        HandyInventoryUtil.refreshInventory(handyInventory.getInventory());
        this.J(inventory);
        this.setPageButtons(handyInventory);
        GuildUtil.setHead(handyInventory.getInventory());
    }

    public Inventory createGui(Player player, Integer guildId) {
        String title = ConfigUtil.INVITATION_CONFIG.getString("title");
        String sound = ConfigUtil.INVITATION_CONFIG.getString("sound");
        title = PlaceholderApiUtil.set(player, title);
        int size = ConfigUtil.INVITATION_CONFIG.getInt("size", 54);
        HandyInventory handyInventory = new HandyInventory(GuiTypeEnum.GUILD_INVITATION.getType(), title, size, sound);
        handyInventory.setPlayer(player);
        handyInventory.setId(guildId);
        this.setInventoryDate(handyInventory);
        return handyInventory.getInventory();
    }
}
