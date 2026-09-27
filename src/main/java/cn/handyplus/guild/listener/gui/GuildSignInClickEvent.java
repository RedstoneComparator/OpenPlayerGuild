package cn.handyplus.guild.listener.gui;

import cn.handyplus.guild.constants.GuiTypeEnum;
import cn.handyplus.guild.enter.GuildPlayer;
import cn.handyplus.guild.enter.GuildPlayerSignIn;
import cn.handyplus.guild.hook.PlaceholderApiUtil;
import cn.handyplus.guild.hook.PlayerCurrencyUtil;
import cn.handyplus.guild.hook.PlayerPointsUtil;
import cn.handyplus.guild.hook.VaultUtil;
import cn.handyplus.guild.inventory.GuildSignInGui;
import cn.handyplus.guild.inventory.GuildViewGui;
import cn.handyplus.guild.lib.CollUtil;
import cn.handyplus.guild.lib.HandyInventory;
import cn.handyplus.guild.lib.HandyInventoryUtil;
import cn.handyplus.guild.lib.HandyPermissionUtil;
import cn.handyplus.guild.lib.IHandyClickEvent;
import cn.handyplus.guild.lib.MessageUtil;
import cn.handyplus.guild.lib.PlayerSchedulerUtil;
import cn.handyplus.guild.lib.StrUtil;
import cn.handyplus.guild.service.GuildPlayerService;
import cn.handyplus.guild.service.GuildPlayerSignInService;
import cn.handyplus.guild.util.ConfigUtil;
import cn.handyplus.guild.util.GuildCacheUtil;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class GuildSignInClickEvent
implements IHandyClickEvent {
    @Override
    public void rawSlotClick(HandyInventory handyInventory, InventoryClickEvent clickEvent) {
        int rawSlot = clickEvent.getRawSlot();
        Player player = handyInventory.getPlayer();
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.SIGN_IN_CONFIG, "back")) {
            Inventory inventory = GuildViewGui.getInstance().createGui(player, handyInventory.getId());
            handyInventory.syncOpen(inventory);
            return;
        }
        Map<Integer, String> customButtons = HandyInventoryUtil.getCustomButton(ConfigUtil.SIGN_IN_CONFIG, "custom");
        String command = customButtons.get(rawSlot);
        if (StrUtil.isNotEmpty(command)) {
            PlayerSchedulerUtil.performReplaceCommand(player, command);
            return;
        }
        GuildSignInClickEvent.performSignIn(handyInventory, rawSlot);
    }

    @Override
    public String guiType() {
        return GuiTypeEnum.GUILD_SIGN_IN.getType();
    }

    private static synchronized void performSignIn(HandyInventory handyInventory, int rawSlot) {
        GuildPlayerSignIn guildPlayerSignIn;
        String text;
        Player player = handyInventory.getPlayer();
        Optional<GuildPlayer> guildPlayerOptional = GuildPlayerService.getInstance().findByPlayerUuid(player.getUniqueId());
        if (!guildPlayerOptional.isPresent()) {
            return;
        }
        GuildPlayer guildPlayer = guildPlayerOptional.get();
        if (GuildPlayerSignInService.getInstance().findByPlayerUuid(player.getUniqueId()).isPresent()) {
            MessageUtil.sendMessage(player, ConfigUtil.SIGN_IN_CONFIG.getString("signedInButton"));
            return;
        }
        int money = 0;
        boolean taken = false;
        String signInType = "";
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.SIGN_IN_CONFIG, "point")) {
            int pointPrice = ConfigUtil.SIGN_IN_CONFIG.getInt("point.price");
            taken = PlayerPointsUtil.take(player, pointPrice);
            money = ConfigUtil.SIGN_IN_CONFIG.getInt("point.money");
            signInType = "point";
        }
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.SIGN_IN_CONFIG, "vault")) {
            double vaultPrice = ConfigUtil.SIGN_IN_CONFIG.getDouble("vault.price");
            taken = VaultUtil.take((OfflinePlayer)player, vaultPrice);
            money = ConfigUtil.SIGN_IN_CONFIG.getInt("vault.money");
            signInType = "vault";
        }
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.SIGN_IN_CONFIG, "ply")) {
            long currencyPrice = ConfigUtil.SIGN_IN_CONFIG.getLong("ply.price");
            String currencyName = ConfigUtil.SIGN_IN_CONFIG.getString("ply.type");
            text = ConfigUtil.SIGN_IN_CONFIG.getString("title");
            taken = PlayerCurrencyUtil.take(player, currencyName, currencyPrice, text);
            money = ConfigUtil.SIGN_IN_CONFIG.getInt("ply.money");
            signInType = "ply";
        }
        if (!taken) {
            MessageUtil.sendMessage(player, ConfigUtil.SIGN_IN_CONFIG.getString("notMoneyFailureMsg"));
            return;
        }
        GuildPlayerSignIn guildPlayerSignIn2 = guildPlayerSignIn = new GuildPlayerSignIn();
        guildPlayerSignIn2.setPlayerName(player.getName());
        guildPlayerSignIn2.setPlayerUuid(player.getUniqueId());
        GuildPlayerSignIn guildPlayerSignIn3 = guildPlayerSignIn;
        guildPlayerSignIn2.setSignInTime(new Date());
        Player player2 = player;
        if (GuildPlayerSignInService.getInstance().add(guildPlayerSignIn) > 0) {
            Double multiplier = HandyPermissionUtil.getDoubleNumber(player2, ConfigUtil.SIGN_IN_CONFIG, "signIn");
            money = (int)((double)money * multiplier);
            text = ConfigUtil.SIGN_IN_CONFIG.getString("succeedMsg", "").replace("${money}", money + "");
            Player player3 = player;
            MessageUtil.sendMessage(player3, text);
            GuildPlayerService.getInstance().addMoneyById(guildPlayer.getId(), money);
            GuildSignInGui.getInstance().setInventoryDate(handyInventory);
            GuildSignInClickEvent.runSignInCommands(signInType, player3, String.valueOf(money));
            GuildCacheUtil.refresh(player3.getUniqueId());
            return;
        }
        MessageUtil.sendMessage(player2, ConfigUtil.SIGN_IN_CONFIG.getString("failureMsg"));
    }

    @Override
    public boolean isAsync() {
        return true;
    }

    private static void runSignInCommands(String signInType, Player player, String money) {
        List<String> signInCommands = ConfigUtil.SIGN_IN_CONFIG.getStringList(new StringBuilder().insert(0, signInType).append(".command").toString());
        if (CollUtil.isNotEmpty(signInCommands)) {
            for (String signInCommand : signInCommands) {
                if (!StrUtil.isNotEmpty(signInCommand)) continue;
                signInCommand = StrUtil.replace(signInCommand, "money", money);
                signInCommand = PlaceholderApiUtil.set(player, signInCommand);
                PlayerSchedulerUtil.performReplaceCommand(player, signInCommand);
            }
        }
    }
}
