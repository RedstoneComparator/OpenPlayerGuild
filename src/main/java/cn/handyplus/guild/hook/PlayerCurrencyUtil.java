package cn.handyplus.guild.hook;

import cn.handyplus.currency.api.PlayerCurrencyApi;
import cn.handyplus.guild.PlayerGuild;
import cn.handyplus.guild.lib.StrUtil;
import java.util.UUID;
import org.bukkit.entity.Player;

public class PlayerCurrencyUtil {
    private PlayerCurrencyUtil() {
    }

    public static boolean take(Player player, String currencyType, long amount, String reason) {
        return PlayerCurrencyUtil.take(player.getUniqueId(), currencyType, amount, reason);
    }

    public static boolean take(UUID playerUuid, String currencyType, long amount, String reason) {
        if (!PlayerGuild.USE_PLY) {
            return false;
        }
        return PlayerCurrencyApi.take((UUID)playerUuid, (String)currencyType, (Long)amount, (String)"PlayerGuild", (String)reason);
    }

    public static Long look(Player player, String currencyType) {
        if (!PlayerGuild.USE_PLY || player == null || StrUtil.isEmpty(currencyType)) {
            return 0L;
        }
        return PlayerCurrencyApi.look((UUID)player.getUniqueId(), (String)currencyType);
    }

    public static String getDesc(String currencyType) {
        if (!PlayerGuild.USE_PLY || StrUtil.isEmpty(currencyType)) {
            return currencyType;
        }
        return PlayerCurrencyApi.getDesc((String)currencyType);
    }
}
