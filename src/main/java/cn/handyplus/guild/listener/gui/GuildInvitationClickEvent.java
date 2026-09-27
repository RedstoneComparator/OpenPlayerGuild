package cn.handyplus.guild.listener.gui;

import cn.handyplus.guild.command.player.InvitationCommand;
import cn.handyplus.guild.constants.GuiTypeEnum;
import cn.handyplus.guild.inventory.GuildInvitationGui;
import cn.handyplus.guild.inventory.GuildSettingGui;
import cn.handyplus.guild.lib.HandyInventory;
import cn.handyplus.guild.lib.HandyInventoryUtil;
import cn.handyplus.guild.lib.IHandyClickEvent;
import cn.handyplus.guild.lib.StrUtil;
import cn.handyplus.guild.util.ConfigUtil;
import java.util.Map;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class GuildInvitationClickEvent
implements IHandyClickEvent {
    @Override
    public String guiType() {
        return GuiTypeEnum.GUILD_INVITATION.getType();
    }

    @Override
    public void rawSlotClick(HandyInventory gui, InventoryClickEvent clickEvent) {
        int rawSlot = clickEvent.getRawSlot();
        HandyInventory inventoryGui = gui;
        Player player = gui.getPlayer();
        Integer pageNum = inventoryGui.getPageNum();
        Integer pageCount = inventoryGui.getPageCount();
        Map<Integer, String> map = inventoryGui.getStrMap();
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.INVITATION_CONFIG, "back")) {
            Inventory inventory = GuildSettingGui.getInstance().createGui(player, gui.getId());
            gui.syncOpen(inventory);
            return;
        }
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.INVITATION_CONFIG, "previousPage")) {
            if (pageNum > 1) {
                HandyInventory pageGui = gui;
                pageGui.setPageNum(pageGui.getPageNum() - 1);
                GuildInvitationGui.getInstance().setInventoryDate(gui);
            }
            return;
        }
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.INVITATION_CONFIG, "nextPage")) {
            if (1 <= pageCount) {
                HandyInventory nextPageGui = gui;
                nextPageGui.setPageNum(nextPageGui.getPageNum() + 1);
                GuildInvitationGui.getInstance().setInventoryDate(gui);
            }
            return;
        }
        String playerName = map.get(rawSlot);
        if (StrUtil.isEmpty(playerName)) {
            return;
        }
        InvitationCommand.invitation(player, playerName);
        GuildInvitationGui.getInstance().setInventoryDate(gui);
    }

    @Override
    public boolean isAsync() {
        return true;
    }
}
