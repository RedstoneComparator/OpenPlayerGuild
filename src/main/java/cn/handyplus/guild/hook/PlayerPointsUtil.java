package cn.handyplus.guild.hook;

import cn.handyplus.guild.PlayerGuild;
import java.util.UUID;
import org.bukkit.entity.Player;

public class PlayerPointsUtil {
    private PlayerPointsUtil() {
    }

    public static boolean take(Player player, int amount) {
        return PlayerPointsUtil.take(player.getUniqueId(), amount);
    }

    public static boolean take(UUID playerUuid, int amount) {
        if (PlayerGuild.PLAYER_POINTS == null) {
            return false;
        }
        return PlayerGuild.PLAYER_POINTS.getAPI().take(playerUuid, amount);
    }

    public static int look(Player player) {
        if (PlayerGuild.PLAYER_POINTS == null || player == null) {
            return 0;
        }
        return PlayerGuild.PLAYER_POINTS.getAPI().look(player.getUniqueId());
    }
}
