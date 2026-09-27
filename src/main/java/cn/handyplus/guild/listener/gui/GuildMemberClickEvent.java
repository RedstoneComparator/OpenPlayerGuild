package cn.handyplus.guild.listener.gui;

import cn.handyplus.guild.constants.GuiTypeEnum;
import cn.handyplus.guild.constants.GuildRoleEnum;
import cn.handyplus.guild.enter.GuildPlayer;
import cn.handyplus.guild.inventory.GuildManageMemberGui;
import cn.handyplus.guild.inventory.GuildMemberGui;
import cn.handyplus.guild.inventory.GuildViewGui;
import cn.handyplus.guild.lib.BaseUtil;
import cn.handyplus.guild.lib.HandyInventory;
import cn.handyplus.guild.lib.HandyInventoryUtil;
import cn.handyplus.guild.lib.IHandyClickEvent;
import cn.handyplus.guild.lib.MessageUtil;
import cn.handyplus.guild.lib.StrUtil;
import cn.handyplus.guild.listener.gui.ConfirmClickEvent;
import cn.handyplus.guild.service.GuildPlayerService;
import cn.handyplus.guild.util.ConfigUtil;
import cn.handyplus.guild.util.NoticeUtil;
import java.util.Map;
import java.util.Optional;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class GuildMemberClickEvent
implements IHandyClickEvent {
    @Override
    public String guiType() {
        return GuiTypeEnum.GUILD_MEMBER.getType();
    }

    @Override
    public void rawSlotClick(HandyInventory gui, InventoryClickEvent clickEvent) {
        int rawSlot = clickEvent.getRawSlot();
        HandyInventory inventoryGui = gui;
        Player player = inventoryGui.getPlayer();
        Integer pageNum = inventoryGui.getPageNum();
        Integer pageCount = inventoryGui.getPageCount();
        Map<Integer, Integer> map = inventoryGui.getIntMap();
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.MEMBER_CONFIG, "back")) {
            Inventory inventory = GuildViewGui.getInstance().createGui(player, gui.getId());
            gui.syncOpen(inventory);
            return;
        }
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.MEMBER_CONFIG, "previousPage")) {
            if (pageNum > 1) {
                HandyInventory pageGui = gui;
                pageGui.setPageNum(pageGui.getPageNum() - 1);
                GuildMemberGui.getInstance().setInventoryDate(gui);
            }
            return;
        }
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.MEMBER_CONFIG, "nextPage")) {
            if (pageNum + 1 <= pageCount) {
                HandyInventory nextPageGui = gui;
                nextPageGui.setPageNum(nextPageGui.getPageNum() + 1);
                GuildMemberGui.getInstance().setInventoryDate(gui);
            }
            return;
        }
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.MEMBER_CONFIG, "impeachment")) {
            GuildMemberClickEvent.impeachGuildMaster(gui);
            return;
        }
        Integer guildPlayerId = map.get(rawSlot);
        if (guildPlayerId == null) {
            return;
        }
        Optional<GuildPlayer> optional = GuildPlayerService.getInstance().findById(guildPlayerId);
        if (!optional.isPresent()) {
            GuildMemberGui.getInstance().setInventoryDate(gui);
            return;
        }
        GuildPlayer guildPlayer = optional.get();
        Optional<GuildPlayer> optional2 = GuildPlayerService.getInstance().findByPlayerUuid(player.getUniqueId());
        if (!optional2.isPresent()) {
            return;
        }
        GuildPlayer operator = optional2.get();
        if (operator.getRoleId() >= guildPlayer.getRoleId() || operator.getRoleId() > GuildRoleEnum.TWO.getRoleId()) {
            return;
        }
        Inventory inventory2 = GuildManageMemberGui.getInstance().createGui(player, gui.getId(), guildPlayerId);
        gui.syncOpen(inventory2);
    }

    private static void impeachGuildMaster(HandyInventory gui) {
        Player player = gui.getPlayer();
        Optional<GuildPlayer> optional = GuildPlayerService.getInstance().findOneRoleByGuildId(gui.getId());
        if (!optional.isPresent()) {
            GuildMemberGui.getInstance().setInventoryDate(gui);
            return;
        }
        GuildPlayer guildPlayer = optional.get();
        long offlineDay = GuildMemberGui.getOfflineDay(guildPlayer);
        int impeachmentDay = ConfigUtil.MEMBER_CONFIG.getInt("impeachment.day", 14);
        if (offlineDay <= (long)impeachmentDay) {
            String offlineMessage = BaseUtil.getLangMsg("impeachmentOneFailMsg");
            offlineMessage = StrUtil.replace(offlineMessage, "offline_day", String.valueOf(offlineDay));
            offlineMessage = StrUtil.replace(offlineMessage, "impeachment_day", String.valueOf(impeachmentDay));
            MessageUtil.sendMessage(player, offlineMessage);
            GuildMemberGui.getInstance().setInventoryDate(gui);
            return;
        }
        Optional<GuildPlayer> optional2 = GuildPlayerService.getInstance().findOneTotalMoneyByGuildId(gui.getId());
        if (!optional2.isPresent()) {
            GuildMemberGui.getInstance().setInventoryDate(gui);
            return;
        }
        GuildPlayer successor = optional2.get();
        if (guildPlayer.getId().equals(successor.getId())) {
            MessageUtil.sendMessage(player, BaseUtil.getLangMsg("impeachmentFailMsg"));
            return;
        }
        ConfirmClickEvent.upRole(successor, guildPlayer);
        GuildMemberGui.getInstance().setInventoryDate(gui);
        String langMessage = BaseUtil.getLangMsg("impeachmentSucceedMsg");
        MessageUtil.sendMessage(player, StrUtil.replace(langMessage, "playerName", successor.getPlayerName()));
        langMessage = BaseUtil.getLangMsg("impeachmentSucceedNotice");
        if (StrUtil.isNotEmpty(langMessage)) {
            langMessage = StrUtil.replace(langMessage, "one", guildPlayer.getPlayerName());
            langMessage = StrUtil.replace(langMessage, "player", player.getName());
            langMessage = StrUtil.replace(langMessage, "successor", successor.getPlayerName());
            NoticeUtil.sendNotice(player, langMessage, false);
        }
    }

    @Override
    public boolean isAsync() {
        return true;
    }
}
