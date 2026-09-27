package cn.handyplus.guild.listener.gui;

import cn.handyplus.guild.constants.GuiTypeEnum;
import cn.handyplus.guild.constants.GuildRoleEnum;
import cn.handyplus.guild.enter.GuildPlayer;
import cn.handyplus.guild.event.PlayerLeaveGuildEvent;
import cn.handyplus.guild.inventory.ConfirmGui;
import cn.handyplus.guild.inventory.GuildManageMemberGui;
import cn.handyplus.guild.inventory.GuildMemberGui;
import cn.handyplus.guild.inventory.GuildViewGui;
import cn.handyplus.guild.lib.HandyInventory;
import cn.handyplus.guild.lib.HandyInventoryUtil;
import cn.handyplus.guild.lib.IHandyClickEvent;
import cn.handyplus.guild.service.GuildPlayerService;
import cn.handyplus.guild.util.ConfigUtil;
import cn.handyplus.guild.util.GuildCacheUtil;
import java.util.Optional;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class GuildManageMemberClickEvent
implements IHandyClickEvent {
    @Override
    public void rawSlotClick(HandyInventory gui, InventoryClickEvent clickEvent) {
        int rawSlot = clickEvent.getRawSlot();
        HandyInventory inventoryGui = gui;
        Player player = inventoryGui.getPlayer();
        Integer guildInfoId = inventoryGui.getId();
        Integer guildPlayerId = Integer.valueOf(inventoryGui.getObj().toString());
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.MANAGE_MEMBER_CONFIG, "back")) {
            Inventory inventory = GuildMemberGui.getInstance().createGui(player, gui.getId());
            gui.syncOpen(inventory);
            return;
        }
        Optional<GuildPlayer> optional = GuildPlayerService.getInstance().findById(guildPlayerId);
        if (!optional.isPresent()) {
            return;
        }
        GuildPlayer guildPlayer = optional.get();
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.MANAGE_MEMBER_CONFIG, "ban")) {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(guildPlayer.getPlayerUuid());
            PlayerLeaveGuildEvent.callEvent(offlinePlayer, guildPlayerId);
            Inventory inventory = GuildViewGui.getInstance().createGui(player, gui.getId());
            gui.syncOpen(inventory);
            return;
        }
        Optional<GuildPlayer> operatorOptional = GuildPlayerService.getInstance().findByPlayerUuid(player.getUniqueId());
        if (!operatorOptional.isPresent()) {
            return;
        }
        GuildPlayer operator = operatorOptional.get();
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.MANAGE_MEMBER_CONFIG, "up")) {
            if (operator.getRoleId() >= guildPlayer.getRoleId() || operator.getRoleId() > GuildRoleEnum.TWO.getRoleId()) {
                return;
            }
            int newRoleId = guildPlayer.getRoleId() - 1;
            if (GuildRoleEnum.ONE.getRoleId() >= newRoleId) {
                Inventory inventory = ConfirmGui.getInstance().createGui(player, guildInfoId, "up", guildPlayerId);
                gui.syncOpen(inventory);
                return;
            }
            GuildManageMemberClickEvent.setCache(guildPlayer, newRoleId);
            GuildManageMemberGui.getInstance().setInventoryDate(gui);
            return;
        }
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.MANAGE_MEMBER_CONFIG, "down")) {
            int newRoleId = guildPlayer.getRoleId() + 1;
            if (GuildRoleEnum.FOUR.getRoleId() < newRoleId) {
                return;
            }
            GuildManageMemberClickEvent.setCache(guildPlayer, newRoleId);
            GuildManageMemberGui.getInstance().setInventoryDate(gui);
        }
    }

    @Override
    public String guiType() {
        return GuiTypeEnum.GUILD_MANAGE_MEMBER_CREATE.getType();
    }

    public static void setCache(GuildPlayer guildPlayer, int roleId) {
        GuildPlayerService.getInstance().updateRoleIdById(guildPlayer.getId(), roleId);
        GuildCacheUtil.refresh(guildPlayer.getPlayerUuid());
    }

    @Override
    public boolean isAsync() {
        return true;
    }
}
