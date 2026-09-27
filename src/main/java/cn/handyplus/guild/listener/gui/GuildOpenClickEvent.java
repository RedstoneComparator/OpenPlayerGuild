package cn.handyplus.guild.listener.gui;

import cn.handyplus.guild.command.player.JoinCommand;
import cn.handyplus.guild.constants.GuiTypeEnum;
import cn.handyplus.guild.constants.GuildConstants;
import cn.handyplus.guild.constants.GuildSortEnum;
import cn.handyplus.guild.enter.GuildPlayer;
import cn.handyplus.guild.inventory.GuildOpenGui;
import cn.handyplus.guild.inventory.GuildViewGui;
import cn.handyplus.guild.lib.HandyInventory;
import cn.handyplus.guild.lib.HandyInventoryUtil;
import cn.handyplus.guild.lib.IHandyClickEvent;
import cn.handyplus.guild.lib.MessageUtil;
import cn.handyplus.guild.lib.PlayerSchedulerUtil;
import cn.handyplus.guild.lib.StrUtil;
import cn.handyplus.guild.service.GuildPlayerService;
import cn.handyplus.guild.util.ConfigUtil;
import cn.handyplus.guild.util.GuildCoolingUtil;
import java.util.Map;
import java.util.Optional;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class GuildOpenClickEvent
implements IHandyClickEvent {
    @Override
    public void rawSlotClick(HandyInventory gui, InventoryClickEvent clickEvent) {
        int rawSlot = clickEvent.getRawSlot();
        HandyInventory inventoryGui = gui;
        Integer pageNum = inventoryGui.getPageNum();
        Integer pageCount = inventoryGui.getPageCount();
        Player player = inventoryGui.getPlayer();
        Map<Integer, Integer> map = inventoryGui.getIntMap();
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.OPEN_CONFIG, "previousPage")) {
            if (pageNum > 1) {
                HandyInventory pageGui = gui;
                pageGui.setPageNum(pageGui.getPageNum() - 1);
                GuildOpenGui.getInstance().setInventoryDate(gui);
            }
            return;
        }
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.OPEN_CONFIG, "nextPage")) {
            if (pageNum + 1 <= pageCount) {
                HandyInventory nextPageGui = gui;
                nextPageGui.setPageNum(nextPageGui.getPageNum() + 1);
                GuildOpenGui.getInstance().setInventoryDate(gui);
            }
            return;
        }
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.OPEN_CONFIG, "search")) {
            HandyInventory searchGui = gui;
            GuildSortEnum guildSortEnum = GuildSortEnum.getNextEnum(searchGui.getSearchType());
            searchGui.setSearchType(guildSortEnum.getName());
            GuildOpenGui.getInstance().setInventoryDate(gui);
            return;
        }
        Map<Integer, String> customButtonMap = HandyInventoryUtil.getCustomButton(ConfigUtil.OPEN_CONFIG, "custom");
        String command = customButtonMap.get(rawSlot);
        if (StrUtil.isNotEmpty(command)) {
            PlayerSchedulerUtil.performReplaceCommand(player, command);
            return;
        }
        Integer guildId = map.get(rawSlot);
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.OPEN_CONFIG, "me") && guildId != null) {
            Inventory inventory = GuildViewGui.getInstance().createGui(player, map.get(rawSlot));
            gui.syncOpen(inventory);
            return;
        }
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.OPEN_CONFIG, "create") && guildId == null) {
            if (GuildCoolingUtil.checkJoinCooling(player)) {
                return;
            }
            GuildConstants.CREATE_GUILD_INFO_MAP.put(player.getUniqueId(), "add");
            MessageUtil.sendMessage(player, ConfigUtil.OPEN_CONFIG.getString("create.nameMsg"));
            gui.syncClose();
            return;
        }
        String guildIndex = ConfigUtil.OPEN_CONFIG.getString("guild.index", "20,21,22,23,24,29,30,31,32,33");
        if (StrUtil.strToIntList(guildIndex).contains(rawSlot)) {
            Optional<GuildPlayer> optional = GuildPlayerService.getInstance().findByPlayerUuid(player.getUniqueId());
            if (!optional.isPresent()) {
                JoinCommand.join(player, guildId);
                GuildOpenGui.getInstance().setInventoryDate(gui);
                return;
            }
            if (map.get(rawSlot).equals(optional.get().getGuildInfoId())) {
                Inventory inventory2 = GuildViewGui.getInstance().createGui(player, map.get(rawSlot));
                gui.syncOpen(inventory2);
            }
        }
    }

    @Override
    public String guiType() {
        return GuiTypeEnum.GUILD_OPEN.getType();
    }

    @Override
    public boolean isAsync() {
        return true;
    }
}
