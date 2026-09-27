package cn.handyplus.guild.listener.gui;

import cn.handyplus.guild.constants.GuiTypeEnum;
import cn.handyplus.guild.inventory.GuildViewGui;
import cn.handyplus.guild.lib.HandyInventory;
import cn.handyplus.guild.lib.HandyInventoryUtil;
import cn.handyplus.guild.lib.IHandyClickEvent;
import cn.handyplus.guild.lib.PlayerSchedulerUtil;
import cn.handyplus.guild.lib.StrUtil;
import cn.handyplus.guild.util.ConfigUtil;
import java.util.Map;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class GuildTitleClickEvent
implements IHandyClickEvent {
    @Override
    public void rawSlotClick(HandyInventory handyInventory, InventoryClickEvent clickEvent) {
        int rawSlot = clickEvent.getRawSlot();
        Player player = handyInventory.getPlayer();
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.TITLE_CONFIG, "back")) {
            Inventory inventory = GuildViewGui.getInstance().createGui(player, handyInventory.getId());
            handyInventory.syncOpen(inventory);
        }
        Map<Integer, String> customButtons = HandyInventoryUtil.getCustomButton(ConfigUtil.TITLE_CONFIG, "custom");
        String command = customButtons.get(rawSlot);
        if (StrUtil.isNotEmpty(command)) {
            PlayerSchedulerUtil.performReplaceCommand(player, command);
        }
    }

    @Override
    public boolean isAsync() {
        return true;
    }

    @Override
    public String guiType() {
        return GuiTypeEnum.GUILD_TITLE.getType();
    }
}
