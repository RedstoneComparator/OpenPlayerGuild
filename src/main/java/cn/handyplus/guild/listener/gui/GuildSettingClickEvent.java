package cn.handyplus.guild.listener.gui;

import cn.handyplus.guild.command.function.UpCommand;
import cn.handyplus.guild.command.player.SetSpawnCommand;
import cn.handyplus.guild.constants.GuiTypeEnum;
import cn.handyplus.guild.constants.GuildConstants;
import cn.handyplus.guild.constants.GuildRoleEnum;
import cn.handyplus.guild.enter.GuildInfo;
import cn.handyplus.guild.enter.GuildPlayer;
import cn.handyplus.guild.hook.GuildWorldUtil;
import cn.handyplus.guild.hook.ResidenceUtil;
import cn.handyplus.guild.inventory.ConfirmGui;
import cn.handyplus.guild.inventory.GuildApplyGui;
import cn.handyplus.guild.inventory.GuildCreateWorldGui;
import cn.handyplus.guild.inventory.GuildInvitationGui;
import cn.handyplus.guild.inventory.GuildLogoOpenGui;
import cn.handyplus.guild.inventory.GuildLogoShopGui;
import cn.handyplus.guild.inventory.GuildSettingGui;
import cn.handyplus.guild.inventory.GuildViewGui;
import cn.handyplus.guild.lib.BaseUtil;
import cn.handyplus.guild.lib.EntitySchedulerUtil;
import cn.handyplus.guild.lib.HandyInventory;
import cn.handyplus.guild.lib.HandyInventoryUtil;
import cn.handyplus.guild.lib.IHandyClickEvent;
import cn.handyplus.guild.lib.MessageUtil;
import cn.handyplus.guild.lib.PlayerSchedulerUtil;
import cn.handyplus.guild.lib.StrUtil;
import cn.handyplus.guild.service.GuildInfoService;
import cn.handyplus.guild.service.GuildPlayerService;
import cn.handyplus.guild.util.ConfigUtil;
import java.util.Optional;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class GuildSettingClickEvent
implements IHandyClickEvent {
    @Override
    public String guiType() {
        return GuiTypeEnum.GUILD_SETTING.getType();
    }

    @Override
    public void rawSlotClick(HandyInventory handyInventory, InventoryClickEvent clickEvent) {
        int rawSlot = clickEvent.getRawSlot();
        Player player = handyInventory.getPlayer();
        Integer guildId = handyInventory.getId();
        Object value = HandyInventoryUtil.getCustomButton(ConfigUtil.SETTING_CONFIG, "custom").get(rawSlot);
        if (StrUtil.isNotEmpty((CharSequence)value)) {
            PlayerSchedulerUtil.performReplaceCommand(player, (String)value);
            return;
        }
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.SETTING_CONFIG, "up")) {
            UpCommand.up(player, success -> {
                if (success.booleanValue()) {
                    GuildSettingGui.getInstance().setInventoryDate(handyInventory);
                }
            });
            return;
        }
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.SETTING_CONFIG, "editName")) {
            value = GuildInfoService.getInstance().findById(guildId);
            if (!((Optional)value).isPresent()) {
                return;
            }
            GuildInfo guildInfo = (GuildInfo)((Optional)value).get();
            int editNamePrice = ConfigUtil.SETTING_CONFIG.getInt("editName.price");
            if (guildInfo.getMoney() < editNamePrice) {
                MessageUtil.sendMessage(player, ConfigUtil.SETTING_CONFIG.getString("editName.noButton"));
                return;
            }
            GuildConstants.CREATE_GUILD_INFO_MAP.put(player.getUniqueId(), "edit");
            handyInventory.syncClose();
            MessageUtil.sendMessage(player, ConfigUtil.OPEN_CONFIG.getString("create.nameMsg"));
            return;
        }
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.SETTING_CONFIG, "invitation")) {
            value = GuildInvitationGui.getInstance().createGui(player, guildId);
            handyInventory.syncOpen((Inventory)value);
            return;
        }
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.SETTING_CONFIG, "logoShop")) {
            value = GuildLogoShopGui.getInstance().createGui(player, guildId);
            handyInventory.syncOpen((Inventory)value);
            return;
        }
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.SETTING_CONFIG, "logoWarehouse")) {
            value = GuildLogoOpenGui.getInstance().createGui(player, guildId);
            handyInventory.syncOpen((Inventory)value);
            return;
        }
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.SETTING_CONFIG, "apply")) {
            value = GuildApplyGui.getInstance().createGui(player, guildId);
            handyInventory.syncOpen((Inventory)value);
            return;
        }
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.SETTING_CONFIG, "pvp")) {
            int chargedPrice;
            boolean newPvpStatus;
            value = GuildInfoService.getInstance().findById(guildId);
            if (!((Optional)value).isPresent()) {
                return;
            }
            GuildInfo guildInfo = (GuildInfo)((Optional)value).get();
            int pvpPrice = ConfigUtil.SETTING_CONFIG.getInt("pvp.price");
            if (guildInfo.getMoney() < pvpPrice) {
                MessageUtil.sendMessage(player, ConfigUtil.SETTING_CONFIG.getString("editName.noButton"));
                return;
            }
            GuildInfoService guildInfoService = GuildInfoService.getInstance();
            if (!guildInfo.getPvpStatus().booleanValue()) {
                newPvpStatus = true;
                chargedPrice = pvpPrice;
            } else {
                newPvpStatus = false;
                chargedPrice = pvpPrice;
            }
            guildInfoService.editPlayerPvpStatus(guildId, newPvpStatus, chargedPrice);
            GuildSettingGui.getInstance().setInventoryDate(handyInventory);
            return;
        }
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.SETTING_CONFIG, "setSpawn")) {
            SetSpawnCommand.setSpawn(player);
            GuildSettingGui.getInstance().setInventoryDate(handyInventory);
            return;
        }
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.SETTING_CONFIG, "createGuildWorld")) {
            value = GuildInfoService.getInstance().findById(guildId);
            if (!((Optional)value).isPresent()) {
                return;
            }
            GuildInfo guildInfo = (GuildInfo)((Optional)value).get();
            Optional<GuildPlayer> guildPlayerOptional = GuildPlayerService.getInstance().findByPlayerUuid(player.getUniqueId());
            if (!guildPlayerOptional.isPresent()) {
                return;
            }
            GuildPlayer guildPlayer = guildPlayerOptional.get();
            if (StrUtil.isNotEmpty(guildInfo.getWorldName())) {
                World world = Bukkit.getWorld((String)guildInfo.getWorldName());
                if (world == null) {
                    MessageUtil.sendMessage(player, BaseUtil.getLangMsg("noGuildWorld"));
                    return;
                }
                Location location = GuildWorldUtil.getSpawnLocation(guildInfo.getWorldName());
                if (location != null) {
                    EntitySchedulerUtil.syncTeleport((Entity)player, location);
                    if (GuildRoleEnum.ONE.getRoleId().equals(guildPlayer.getRoleId())) {
                        ResidenceUtil.init(player, guildInfo, location);
                    }
                }
                return;
            }
            if (!GuildRoleEnum.ONE.getRoleId().equals(guildPlayer.getRoleId())) {
                MessageUtil.sendMessage(player, BaseUtil.getLangMsg("noPermission"));
                return;
            }
            Inventory inventory = GuildCreateWorldGui.getInstance().createGui(player, guildId);
            handyInventory.syncOpen(inventory);
            return;
        }
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.SETTING_CONFIG, "dissolution")) {
            value = ConfirmGui.getInstance().createGui(player, guildId, "dissolution", null);
            handyInventory.syncOpen((Inventory)value);
            return;
        }
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.SETTING_CONFIG, "description")) {
            value = GuildInfoService.getInstance().findById(guildId);
            if (!((Optional)value).isPresent()) {
                return;
            }
            GuildInfo guildInfo = (GuildInfo)((Optional)value).get();
            int descriptionPrice = ConfigUtil.SETTING_CONFIG.getInt("description.price");
            if (guildInfo.getMoney() < descriptionPrice) {
                MessageUtil.sendMessage(player, ConfigUtil.SETTING_CONFIG.getString("description.noButton"));
                return;
            }
            GuildConstants.CREATE_GUILD_INFO_MAP.put(player.getUniqueId(), "editDescription");
            handyInventory.syncClose();
            MessageUtil.sendMessage(player, ConfigUtil.SETTING_CONFIG.getString("description.changDescription"));
            return;
        }
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.SETTING_CONFIG, "joinMode")) {
            value = GuildInfoService.getInstance().findById(guildId);
            if (!((Optional)value).isPresent()) {
                return;
            }
            GuildInfo guildInfo = (GuildInfo)((Optional)value).get();
            int joinModePrice = ConfigUtil.SETTING_CONFIG.getInt("joinMode.price");
            if (guildInfo.getMoney() < joinModePrice) {
                MessageUtil.sendMessage(player, ConfigUtil.SETTING_CONFIG.getString("editName.noButton"));
                return;
            }
            boolean newJoinMode = guildInfo.getJoinMode() != null && !guildInfo.getJoinMode().booleanValue();
            GuildInfoService.getInstance().editJoinMode(guildId, newJoinMode, joinModePrice);
            GuildSettingGui.getInstance().setInventoryDate(handyInventory);
            return;
        }
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.SETTING_CONFIG, "back")) {
            value = GuildViewGui.getInstance().createGui(player, guildId);
            handyInventory.syncOpen((Inventory)value);
        }
    }

    @Override
    public boolean isAsync() {
        return true;
    }
}
