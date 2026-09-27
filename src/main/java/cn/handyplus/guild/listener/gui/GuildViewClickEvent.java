package cn.handyplus.guild.listener.gui;

import cn.handyplus.guild.constants.GuiTypeEnum;
import cn.handyplus.guild.constants.GuildRoleEnum;
import cn.handyplus.guild.constants.GuildSortEnum;
import cn.handyplus.guild.constants.ShopSearchEnum;
import cn.handyplus.guild.enter.GuildPlayer;
import cn.handyplus.guild.inventory.ConfirmGui;
import cn.handyplus.guild.inventory.GuildActivityGui;
import cn.handyplus.guild.inventory.GuildBankGui;
import cn.handyplus.guild.inventory.GuildMemberGui;
import cn.handyplus.guild.inventory.GuildNoticeGui;
import cn.handyplus.guild.inventory.GuildOpenGui;
import cn.handyplus.guild.inventory.GuildSacredStoneGui;
import cn.handyplus.guild.inventory.GuildSettingGui;
import cn.handyplus.guild.inventory.GuildShopGui;
import cn.handyplus.guild.inventory.GuildSignInGui;
import cn.handyplus.guild.inventory.GuildTaskGui;
import cn.handyplus.guild.inventory.GuildTitleGui;
import cn.handyplus.guild.lib.HandyInventory;
import cn.handyplus.guild.lib.HandyInventoryUtil;
import cn.handyplus.guild.lib.IHandyClickEvent;
import cn.handyplus.guild.lib.PlayerSchedulerUtil;
import cn.handyplus.guild.lib.StrUtil;
import cn.handyplus.guild.service.GuildPlayerService;
import cn.handyplus.guild.util.ConfigUtil;
import java.util.Map;
import java.util.Optional;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class GuildViewClickEvent
implements IHandyClickEvent {
    @Override
    public void rawSlotClick(HandyInventory handyInventory, InventoryClickEvent clickEvent) {
        int rawSlot = clickEvent.getRawSlot();
        Player player = handyInventory.getPlayer();
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.VIEW_CONFIG, "notice")) {
            Inventory inventory = GuildNoticeGui.getInstance().createGui(player, handyInventory.getId(), "all");
            handyInventory.syncOpen(inventory);
            return;
        }
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.VIEW_CONFIG, "member")) {
            Inventory inventory = GuildMemberGui.getInstance().createGui(player, handyInventory.getId());
            handyInventory.syncOpen(inventory);
            return;
        }
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.VIEW_CONFIG, "spawn")) {
            handyInventory.syncClose();
            PlayerSchedulerUtil.performCommand(player, "plg spawn");
            return;
        }
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.VIEW_CONFIG, "task")) {
            Inventory inventory = GuildTaskGui.getInstance().createGui(player, handyInventory.getId());
            handyInventory.syncOpen(inventory);
            return;
        }
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.VIEW_CONFIG, "shop")) {
            GuildPlayer guildPlayer = GuildPlayerService.getInstance().findByPlayerUuid(player.getUniqueId()).orElse(null);
            int buyRoleLimit = ConfigUtil.VIEW_CONFIG.getInt("shop.buyRoleLimit", 4);
            if (guildPlayer != null && guildPlayer.getRoleId() > buyRoleLimit) {
                return;
            }
            Inventory inventory = GuildShopGui.getInstance().createGui(player, handyInventory.getId(), ShopSearchEnum.ALL.getType());
            handyInventory.syncOpen(inventory);
            return;
        }
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.VIEW_CONFIG, "bank")) {
            Inventory inventory = GuildBankGui.getInstance().createGui(player, handyInventory.getId());
            handyInventory.syncOpen(inventory);
            return;
        }
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.VIEW_CONFIG, "signin")) {
            Inventory inventory = GuildSignInGui.getInstance().createGui(player, handyInventory.getId());
            handyInventory.syncOpen(inventory);
            return;
        }
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.VIEW_CONFIG, "buff")) {
            Inventory inventory = GuildTitleGui.getInstance().createGui(player, handyInventory.getId());
            handyInventory.syncOpen(inventory);
            return;
        }
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.VIEW_CONFIG, "sacredStone")) {
            Inventory inventory = GuildSacredStoneGui.getInstance().createGui(player, handyInventory.getId());
            handyInventory.syncOpen(inventory);
            return;
        }
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.VIEW_CONFIG, "activity")) {
            Inventory inventory = GuildActivityGui.getInstance().createGui(player, handyInventory.getId());
            handyInventory.syncOpen(inventory);
            return;
        }
        Map<Integer, String> customButtons = HandyInventoryUtil.getCustomButton(ConfigUtil.VIEW_CONFIG, "custom");
        String command = customButtons.get(rawSlot);
        if (StrUtil.isNotEmpty(command)) {
            PlayerSchedulerUtil.performReplaceCommand(player, command);
            return;
        }
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.VIEW_CONFIG, "exit")) {
            Inventory inventory2 = ConfirmGui.getInstance().createGui(player, handyInventory.getId(), "exit", null);
            handyInventory.syncOpen(inventory2);
            return;
        }
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.VIEW_CONFIG, "admin")) {
            Optional<GuildPlayer> guildPlayerOptional = GuildPlayerService.getInstance().findByPlayerUuid(player.getUniqueId());
            if (!guildPlayerOptional.isPresent()) {
                return;
            }
            if (guildPlayerOptional.get().getRoleId() > GuildRoleEnum.TWO.getRoleId()) {
                return;
            }
            Inventory inventory = GuildSettingGui.getInstance().createGui(player, handyInventory.getId());
            handyInventory.syncOpen(inventory);
            return;
        }
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.VIEW_CONFIG, "back")) {
            Inventory inventory3 = GuildOpenGui.getInstance().createGui(player, GuildSortEnum.PROSPERITY_DEGREE.getName());
            handyInventory.syncOpen(inventory3);
        }
    }

    @Override
    public String guiType() {
        return GuiTypeEnum.GUILD_VIEW.getType();
    }

    @Override
    public boolean isAsync() {
        return true;
    }
}
