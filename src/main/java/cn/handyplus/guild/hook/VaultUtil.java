package cn.handyplus.guild.hook;

import cn.handyplus.guild.PlayerGuild;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class VaultUtil {
    private VaultUtil() {
    }

    public static boolean take(OfflinePlayer player, double amount) {
        if (PlayerGuild.ECON == null) {
            return false;
        }
        if (!PlayerGuild.ECON.has(player, amount)) {
            return false;
        }
        EconomyResponse response = PlayerGuild.ECON.withdrawPlayer(player, amount);
        return response != null && response.transactionSuccess();
    }

    public static double look(Player player) {
        if (PlayerGuild.ECON == null || player == null) {
            return 0.0;
        }
        return PlayerGuild.ECON.getBalance((OfflinePlayer)player);
    }
}
