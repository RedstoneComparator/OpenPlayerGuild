package cn.handyplus.guild.listener.gui;

import cn.handyplus.guild.constants.GuiTypeEnum;
import cn.handyplus.guild.constants.GuildConstants;
import cn.handyplus.guild.enter.GuildPlayer;
import cn.handyplus.guild.inventory.GuildBankGui;
import cn.handyplus.guild.inventory.GuildViewGui;
import cn.handyplus.guild.lib.HandyInventory;
import cn.handyplus.guild.lib.HandyInventoryUtil;
import cn.handyplus.guild.lib.IHandyClickEvent;
import cn.handyplus.guild.lib.MessageUtil;
import cn.handyplus.guild.lib.PlayerSchedulerUtil;
import cn.handyplus.guild.lib.StrUtil;
import cn.handyplus.guild.service.GuildInfoService;
import cn.handyplus.guild.service.GuildPlayerService;
import cn.handyplus.guild.util.ConfigUtil;
import cn.handyplus.guild.util.GuildCacheUtil;
import java.util.Map;
import java.util.Optional;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class GuildBankClickEvent
implements IHandyClickEvent {
    @Override
    public void rawSlotClick(HandyInventory gui, InventoryClickEvent clickEvent) {
        int rawSlot = clickEvent.getRawSlot();
        Player player = gui.getPlayer();
        Integer guildInfoId = gui.getId();
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.BANK_CONFIG, "back")) {
            Inventory inventory = GuildViewGui.getInstance().createGui(player, gui.getId());
            gui.syncOpen(inventory);
            return;
        }
        Map<Integer, String> map = HandyInventoryUtil.getCustomButton(ConfigUtil.BANK_CONFIG, "bankCustom");
        String command = map.get(rawSlot);
        if (StrUtil.isNotEmpty(command)) {
            PlayerSchedulerUtil.performReplaceCommand(player, command);
            return;
        }
        Optional<GuildPlayer> optional = GuildPlayerService.getInstance().findByPlayerUuid(player.getUniqueId());
        if (!optional.isPresent()) {
            return;
        }
        GuildPlayer guildPlayer = optional.get();
        Integer money = guildPlayer.getMoney();
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.BANK_CONFIG, "small")) {
            int smallPrice = ConfigUtil.BANK_CONFIG.getInt("small.price");
            if (money < smallPrice) {
                MessageUtil.sendMessage(player, ConfigUtil.BANK_CONFIG.getString("noButton"));
                return;
            }
            GuildPlayerService.getInstance().subtractMoneyById(guildPlayer.getId(), smallPrice);
            GuildPlayerService.getInstance().addGuildMoneyById(guildPlayer.getId(), smallPrice);
            GuildInfoService.getInstance().addMoneyById(guildInfoId, smallPrice);
            MessageUtil.sendMessage(player, ConfigUtil.BANK_CONFIG.getString("succeedMsg"));
            GuildBankGui.getInstance().setInventoryDate(gui);
            GuildCacheUtil.refresh(guildInfoId);
            return;
        }
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.BANK_CONFIG, "big")) {
            int bigPrice = ConfigUtil.BANK_CONFIG.getInt("big.price");
            if (money < bigPrice) {
                MessageUtil.sendMessage(player, ConfigUtil.BANK_CONFIG.getString("noButton"));
                return;
            }
            GuildPlayerService.getInstance().subtractMoneyById(guildPlayer.getId(), bigPrice);
            GuildPlayerService.getInstance().addGuildMoneyById(guildPlayer.getId(), bigPrice);
            GuildInfoService.getInstance().addMoneyById(guildInfoId, bigPrice);
            MessageUtil.sendMessage(player, ConfigUtil.BANK_CONFIG.getString("succeedMsg"));
            GuildBankGui.getInstance().setInventoryDate(gui);
            GuildCacheUtil.refresh(guildInfoId);
            return;
        }
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.BANK_CONFIG, "all")) {
            if (money < 1) {
                MessageUtil.sendMessage(player, ConfigUtil.BANK_CONFIG.getString("noButton"));
                return;
            }
            GuildPlayerService.getInstance().subtractMoneyById(guildPlayer.getId(), money);
            GuildPlayerService.getInstance().addGuildMoneyById(guildPlayer.getId(), money);
            GuildInfoService.getInstance().addMoneyById(guildInfoId, money);
            MessageUtil.sendMessage(player, ConfigUtil.BANK_CONFIG.getString("succeedMsg"));
            GuildBankGui.getInstance().setInventoryDate(gui);
            GuildCacheUtil.refresh(guildInfoId);
            return;
        }
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.BANK_CONFIG, "custom")) {
            if (money < 1) {
                MessageUtil.sendMessage(player, ConfigUtil.BANK_CONFIG.getString("noButton"));
                return;
            }
            GuildConstants.CREATE_GUILD_INFO_MAP.put(player.getUniqueId(), "custom");
            MessageUtil.sendMessage(player, ConfigUtil.BANK_CONFIG.getString("custom.customMsg"));
            gui.syncClose();
        }
    }

    @Override
    public boolean isAsync() {
        return true;
    }

    @Override
    public String guiType() {
        return GuiTypeEnum.GUILD_BANK.getType();
    }
}
