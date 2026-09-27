package cn.handyplus.guild.command.function;

import cn.handyplus.guild.enter.GuildPlayer;
import cn.handyplus.guild.enter.GuildPlayerSignIn;
import cn.handyplus.guild.hook.PlayerCurrencyUtil;
import cn.handyplus.guild.hook.PlayerPointsUtil;
import cn.handyplus.guild.hook.VaultUtil;
import cn.handyplus.guild.lib.AssertUtil;
import cn.handyplus.guild.lib.BaseUtil;
import cn.handyplus.guild.lib.IHandyCommandEvent;
import cn.handyplus.guild.lib.MessageUtil;
import cn.handyplus.guild.service.GuildPlayerService;
import cn.handyplus.guild.service.GuildPlayerSignInService;
import cn.handyplus.guild.util.ConfigUtil;
import cn.handyplus.guild.util.GuildCacheUtil;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class SignInCommand
implements IHandyCommandEvent {
    @Override
    public boolean isAsync() {
        return true;
    }

    @Override
    public String command() {
        return "signIn";
    }

    public static boolean signIn(@NotNull CommandSender sender, @NotNull OfflinePlayer offlinePlayer, @NotNull String type) {
        boolean paid = false;
        int money = 0;
        Optional<GuildPlayer> guildPlayerOptional = GuildPlayerService.getInstance().findByPlayerUuid(offlinePlayer.getUniqueId());
        if (!guildPlayerOptional.isPresent()) {
            MessageUtil.sendMessage(sender, BaseUtil.getLangMsg("noGuildMsg"));
            return false;
        }
        GuildPlayer guildPlayer = guildPlayerOptional.get();
        if (SignInCommand.isSignedIn(offlinePlayer.getUniqueId())) {
            MessageUtil.sendMessage(sender, ConfigUtil.SIGN_IN_CONFIG.getString("signedInButton"));
            return false;
        }
        int switchIndex = -1;
        switch (type.hashCode()) {
            case 111981106: {
                if (!type.equals("vault")) break;
                switchIndex = 0;
                break;
            }
            case 106845584: {
                if (!type.equals("point")) break;
                switchIndex = 1;
                break;
            }
            case 111101: {
                if (!type.equals("ply")) break;
                switchIndex = 2;
                break;
            }
        }
        switch (switchIndex) {
            case 0: {
                double vaultPrice = ConfigUtil.SIGN_IN_CONFIG.getDouble("vault.price");
                paid = VaultUtil.take(offlinePlayer, vaultPrice);
                money = ConfigUtil.SIGN_IN_CONFIG.getInt("vault.money");
                break;
            }
            case 1: {
                int pointPrice = ConfigUtil.SIGN_IN_CONFIG.getInt("point.price");
                paid = PlayerPointsUtil.take(offlinePlayer.getUniqueId(), pointPrice);
                money = ConfigUtil.SIGN_IN_CONFIG.getInt("point.money");
                break;
            }
            case 2: {
                long plyPrice = ConfigUtil.SIGN_IN_CONFIG.getLong("ply.price");
                String plyType = ConfigUtil.SIGN_IN_CONFIG.getString("ply.type");
                String title = ConfigUtil.SIGN_IN_CONFIG.getString("title");
                paid = PlayerCurrencyUtil.take(offlinePlayer.getUniqueId(), plyType, plyPrice, title);
                money = ConfigUtil.SIGN_IN_CONFIG.getInt("ply.money");
                break;
            }
        }
        if (!paid) {
            MessageUtil.sendMessage(sender, ConfigUtil.SIGN_IN_CONFIG.getString("notMoneyFailureMsg"));
            return false;
        }
        GuildPlayerSignIn guildPlayerSignIn = new GuildPlayerSignIn();
        guildPlayerSignIn.setPlayerName(offlinePlayer.getName());
        guildPlayerSignIn.setPlayerUuid(offlinePlayer.getUniqueId());
        guildPlayerSignIn.setSignInTime(new Date());
        int signInId = GuildPlayerSignInService.getInstance().add(guildPlayerSignIn);
        if (signInId > 0) {
            String succeedMsg = ConfigUtil.SIGN_IN_CONFIG.getString("succeedMsg", "").replace("${money}", money + "");
            MessageUtil.sendMessage(sender, succeedMsg);
            GuildPlayerService.getInstance().addMoneyById(guildPlayer.getId(), money);
            GuildCacheUtil.refresh(guildPlayer.getPlayerUuid());
            return true;
        }
        MessageUtil.sendMessage(sender, ConfigUtil.SIGN_IN_CONFIG.getString("failureMsg"));
        return false;
    }

    @Override
    public String permission() {
        return "playerGuild.signIn";
    }

    public static boolean isSignedIn(@NotNull UUID playerUuid) {
        return GuildPlayerSignInService.getInstance().findByPlayerUuid(playerUuid).isPresent();
    }

    @Override
    public void onCommand(CommandSender sender, Command command, String label, String[] args) {
        AssertUtil.notTrue(args.length < 2, BaseUtil.getLangMsg("paramFailureMsg"));
        OfflinePlayer offlinePlayer;
        if (args.length == 3) {
            offlinePlayer = BaseUtil.getOfflinePlayer(args[2]);
            if (offlinePlayer == null) {
                MessageUtil.sendMessage(sender, BaseUtil.getLangMsg("noPlayerFailureMsg"));
                return;
            }
        } else {
            offlinePlayer = AssertUtil.notPlayer(sender, BaseUtil.getLangMsg("noPlayerFailureMsg"));
        }
        SignInCommand.signIn(sender, offlinePlayer, args[1]);
    }
}
