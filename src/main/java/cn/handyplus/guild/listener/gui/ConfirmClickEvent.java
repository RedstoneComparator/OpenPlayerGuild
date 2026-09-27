package cn.handyplus.guild.listener.gui;

import cn.handyplus.guild.constants.GuiTypeEnum;
import cn.handyplus.guild.constants.GuildRoleEnum;
import cn.handyplus.guild.enter.GuildInfo;
import cn.handyplus.guild.enter.GuildPlayer;
import cn.handyplus.guild.event.GuildDissolutionEvent;
import cn.handyplus.guild.inventory.GuildManageMemberGui;
import cn.handyplus.guild.inventory.GuildSettingGui;
import cn.handyplus.guild.inventory.GuildViewGui;
import cn.handyplus.guild.lib.HandyInventory;
import cn.handyplus.guild.lib.HandyInventoryUtil;
import cn.handyplus.guild.lib.HandySchedulerUtil;
import cn.handyplus.guild.lib.IHandyClickEvent;
import cn.handyplus.guild.lib.PlayerSchedulerUtil;
import cn.handyplus.guild.service.GuildInfoService;
import cn.handyplus.guild.service.GuildPlayerService;
import cn.handyplus.guild.util.ConfigUtil;
import cn.handyplus.guild.util.GuildCacheUtil;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class ConfirmClickEvent
implements IHandyClickEvent {
    @Override
    public boolean isAsync() {
        return true;
    }

    private void confirmUpRole(HandyInventory gui, int rawSlot) {
        HandyInventory inventoryGui = gui;
        Player player = inventoryGui.getPlayer();
        Integer guildPlayerId = Integer.valueOf(inventoryGui.getObj().toString());
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.CONFIRM_CONFIG, "back")) {
            Inventory inventory = GuildManageMemberGui.getInstance().createGui(player, gui.getId(), guildPlayerId);
            gui.syncOpen(inventory);
            return;
        }
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.CONFIRM_CONFIG, "cancel")) {
            Inventory inventory = GuildManageMemberGui.getInstance().createGui(player, gui.getId(), guildPlayerId);
            gui.syncOpen(inventory);
            return;
        }
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.CONFIRM_CONFIG, "confirm")) {
            Optional<GuildPlayer> optional = GuildPlayerService.getInstance().findById(guildPlayerId);
            if (!optional.isPresent()) {
                return;
            }
            Optional<GuildPlayer> optional2 = GuildPlayerService.getInstance().findByPlayerUuid(player.getUniqueId());
            if (!optional2.isPresent()) {
                return;
            }
            GuildPlayer guildPlayer = optional2.get();
            ConfirmClickEvent.upRole(optional.get(), guildPlayer);
            Inventory inventory = GuildViewGui.getInstance().createGui(player, gui.getId());
            gui.syncOpen(inventory);
        }
    }

    private void confirmDissolution(HandyInventory gui, int slotIndex) {
        HandyInventory inventoryGui = gui;
        Player player = inventoryGui.getPlayer();
        Integer guildInfoId = inventoryGui.getId();
        if (HandyInventoryUtil.isIndex(slotIndex, ConfigUtil.CONFIRM_CONFIG, "back")) {
            Inventory inventory = GuildSettingGui.getInstance().createGui(player, gui.getId());
            gui.syncOpen(inventory);
            return;
        }
        if (HandyInventoryUtil.isIndex(slotIndex, ConfigUtil.CONFIRM_CONFIG, "cancel")) {
            Inventory inventory = GuildSettingGui.getInstance().createGui(player, gui.getId());
            gui.syncOpen(inventory);
            return;
        }
        if (HandyInventoryUtil.isIndex(slotIndex, ConfigUtil.CONFIRM_CONFIG, "confirm")) {
            gui.syncClose();
            Optional<GuildInfo> optional = GuildInfoService.getInstance().findById(guildInfoId);
            optional.ifPresent(guildInfo -> HandySchedulerUtil.runTask(() -> Bukkit.getServer().getPluginManager().callEvent((Event)new GuildDissolutionEvent(player, guildInfo.getId(), guildInfo.getGuildName()))));
        }
    }

    private void confirmExit(HandyInventory gui, int slotIndex) {
        Player player = gui.getPlayer();
        if (HandyInventoryUtil.isIndex(slotIndex, ConfigUtil.CONFIRM_CONFIG, "back")) {
            Inventory inventory = GuildViewGui.getInstance().createGui(player, gui.getId());
            gui.syncOpen(inventory);
            return;
        }
        if (HandyInventoryUtil.isIndex(slotIndex, ConfigUtil.CONFIRM_CONFIG, "cancel")) {
            Inventory inventory = GuildViewGui.getInstance().createGui(player, gui.getId());
            gui.syncOpen(inventory);
            return;
        }
        if (HandyInventoryUtil.isIndex(slotIndex, ConfigUtil.CONFIRM_CONFIG, "confirm")) {
            gui.syncClose();
            PlayerSchedulerUtil.performCommand(player, "plg leave");
        }
    }

    public static void upRole(GuildPlayer newOwner, GuildPlayer oldOwner) {
        GuildPlayerService.getInstance().updateRoleIdById(newOwner.getId(), GuildRoleEnum.ONE.getRoleId());
        GuildPlayerService.getInstance().updateRoleIdById(oldOwner.getId(), GuildRoleEnum.TWO.getRoleId());
        UUID[] playerUuids = new UUID[2];
        playerUuids[0] = newOwner.getPlayerUuid();
        playerUuids[1] = oldOwner.getPlayerUuid();
        GuildCacheUtil.refresh(Arrays.asList(playerUuids));
    }

    @Override
    public void rawSlotClick(HandyInventory gui, InventoryClickEvent clickEvent) {
        int rawSlot = clickEvent.getRawSlot();
        String searchType = gui.getSearchType();
        int switchType = -1;
        switch (searchType.hashCode()) {
            case 1327522823: {
                if (!searchType.equals("dissolution")) break;
                switchType = 0;
                break;
            }
            case 3739: {
                if (!searchType.equals("up")) break;
                switchType = 1;
                break;
            }
            case 3127582: {
                if (!searchType.equals("exit")) break;
                switchType = 2;
            }
        }
        switch (switchType) {
            case 0: {
                this.confirmDissolution(gui, rawSlot);
                return;
            }
            case 1: {
                this.confirmUpRole(gui, rawSlot);
                return;
            }
            case 2: {
                this.confirmExit(gui, rawSlot);
                return;
            }
        }
    }

    @Override
    public String guiType() {
        return GuiTypeEnum.CONFIRM.getType();
    }
}
