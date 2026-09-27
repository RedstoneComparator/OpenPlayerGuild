package cn.handyplus.guild.listener.gui;

import cn.handyplus.guild.constants.GuiTypeEnum;
import cn.handyplus.guild.enter.GuildInfo;
import cn.handyplus.guild.hook.PlayerCurrencyUtil;
import cn.handyplus.guild.hook.PlayerPointsUtil;
import cn.handyplus.guild.hook.VaultUtil;
import cn.handyplus.guild.inventory.GuildSettingGui;
import cn.handyplus.guild.lib.HandyInventory;
import cn.handyplus.guild.lib.HandyInventoryUtil;
import cn.handyplus.guild.lib.IHandyClickEvent;
import cn.handyplus.guild.lib.MessageUtil;
import cn.handyplus.guild.lib.PlayerSchedulerUtil;
import cn.handyplus.guild.service.GuildInfoService;
import cn.handyplus.guild.util.ConfigUtil;
import java.util.Optional;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class GuildCreateWorldClickEvent
implements IHandyClickEvent {
    @Override
    public void rawSlotClick(HandyInventory gui, InventoryClickEvent clickEvent) {
        int rawSlot = clickEvent.getRawSlot();
        Player player = gui.getPlayer();
        Integer guildId = gui.getId();
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.CREATE_WORLD_CONFIG, "back")) {
            Inventory inventory = GuildSettingGui.getInstance().createGui(player, gui.getId());
            gui.syncOpen(inventory);
            return;
        }
        if (!(HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.CREATE_WORLD_CONFIG, "point")
                || HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.CREATE_WORLD_CONFIG, "vault")
                || HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.CREATE_WORLD_CONFIG, "money")
                || HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.CREATE_WORLD_CONFIG, "ply"))) {
            return;
        }
        boolean success = false;
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.CREATE_WORLD_CONFIG, "point")) {
            int point = ConfigUtil.CREATE_WORLD_CONFIG.getInt("point.price");
            success = PlayerPointsUtil.take(player, point);
        }
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.CREATE_WORLD_CONFIG, "vault")) {
            double vault = ConfigUtil.CREATE_WORLD_CONFIG.getDouble("vault.price");
            success = VaultUtil.take((org.bukkit.OfflinePlayer)player, vault);
        }
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.CREATE_WORLD_CONFIG, "money")) {
            int money = ConfigUtil.CREATE_WORLD_CONFIG.getInt("money.price");
            Optional<GuildInfo> optional = GuildInfoService.getInstance().findById(guildId);
            if (!optional.isPresent()) {
                return;
            }
            GuildInfo guildInfo = optional.get();
            Integer guildMoney = guildInfo.getMoney();
            if (guildMoney.intValue() < money) {
                success = false;
            } else {
                GuildInfoService.getInstance().subtractMoneyById(guildId, Integer.valueOf(money));
                success = true;
            }
        }
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.CREATE_WORLD_CONFIG, "ply")) {
            long ply = ConfigUtil.CREATE_WORLD_CONFIG.getLong("ply.price");
            String currencyType = ConfigUtil.CREATE_WORLD_CONFIG.getString("ply.type");
            String cause = ConfigUtil.CREATE_WORLD_CONFIG.getString("title");
            success = PlayerCurrencyUtil.take(player, currencyType, ply, cause);
        }
        if (!success) {
            MessageUtil.sendMessage(player, ConfigUtil.CREATE_WORLD_CONFIG.getString("notMoneyFailureMsg"));
            return;
        }
        gui.syncClose();
        PlayerSchedulerUtil.performCommand(player, "plg createWorld");
    }

    @Override
    public boolean isAsync() {
        return true;
    }

    @Override
    public String guiType() {
        return GuiTypeEnum.GUILD_WORLD_CREATE.getType();
    }
}
