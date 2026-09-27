package cn.handyplus.guild.listener.gui;

import cn.handyplus.guild.constants.GuiTypeEnum;
import cn.handyplus.guild.event.GuildCreateEvent;
import cn.handyplus.guild.hook.PlayerCurrencyUtil;
import cn.handyplus.guild.hook.PlayerPointsUtil;
import cn.handyplus.guild.hook.VaultUtil;
import cn.handyplus.guild.lib.HandyInventory;
import cn.handyplus.guild.lib.HandyInventoryUtil;
import cn.handyplus.guild.lib.IHandyClickEvent;
import cn.handyplus.guild.lib.MessageUtil;
import cn.handyplus.guild.service.GuildInfoService;
import cn.handyplus.guild.util.ConfigUtil;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class GuildCreateClickEvent
implements IHandyClickEvent {
    @Override
    public String guiType() {
        return GuiTypeEnum.GUILD_CREATE.getType();
    }

    @Override
    public boolean isAsync() {
        return true;
    }

    @Override
    public void rawSlotClick(HandyInventory gui, InventoryClickEvent clickEvent) {
        int rawSlot = clickEvent.getRawSlot();
        Player player = gui.getPlayer();
        String guildName = gui.getSearchType().trim();
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.CREATE_CONFIG, "back")) {
            gui.syncClose();
            return;
        }
        if (!(HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.CREATE_CONFIG, "point") || HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.CREATE_CONFIG, "vault") || HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.CREATE_CONFIG, "ply"))) {
            return;
        }
        gui.syncClose();
        if (GuildInfoService.getInstance().findByGuildName(guildName).isPresent()) {
            MessageUtil.sendMessage(player, ConfigUtil.CREATE_CONFIG.getString("duplicateName"));
            return;
        }
        boolean success = false;
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.CREATE_CONFIG, "point")) {
            int pointPrice = ConfigUtil.CREATE_CONFIG.getInt("point.price");
            success = PlayerPointsUtil.take(player, pointPrice);
        }
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.CREATE_CONFIG, "vault")) {
            double vaultPrice = ConfigUtil.CREATE_CONFIG.getDouble("vault.price");
            success = VaultUtil.take((OfflinePlayer)player, vaultPrice);
        }
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.CREATE_CONFIG, "ply")) {
            long plyPrice = ConfigUtil.CREATE_CONFIG.getLong("ply.price");
            String currencyType = ConfigUtil.CREATE_CONFIG.getString("ply.type");
            String title = ConfigUtil.CREATE_CONFIG.getString("title");
            success = PlayerCurrencyUtil.take(player, currencyType, plyPrice, title);
        }
        if (!success) {
            MessageUtil.sendMessage(player, ConfigUtil.CREATE_CONFIG.getString("notMoneyFailureMsg"));
            return;
        }
        GuildCreateEvent.callEvent(player, guildName);
    }
}
