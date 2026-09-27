package cn.handyplus.guild.hook;

import cn.handyplus.guild.PlayerGuild;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class PlaceholderApiUtil {
    private PlaceholderApiUtil() {
    }

    public static String set(Player player, String text) {
        if (!PlayerGuild.USE_PAPI || player == null) {
            return text;
        }
        if (PlaceholderAPI.containsPlaceholders((String)text)) {
            return PlaceholderAPI.setPlaceholders((Player)player, (String)text);
        }
        return text;
    }

    public static String set(OfflinePlayer player, String text) {
        if (!PlayerGuild.USE_PAPI || player == null) {
            return text;
        }
        if (PlaceholderAPI.containsPlaceholders((String)text)) {
            return PlaceholderAPI.setPlaceholders((OfflinePlayer)player, (String)text);
        }
        return text;
    }

    public static List<String> set(OfflinePlayer player, List<String> texts) {
        if (!PlayerGuild.USE_PAPI || player == null) {
            return texts;
        }
        return PlaceholderAPI.setPlaceholders((OfflinePlayer)player, texts);
    }

    public static List<String> set(UUID playerUuid, List<String> texts) {
        ArrayList<String> result = new ArrayList<String>();
        for (String text : texts) {
            result.add(PlaceholderApiUtil.set(playerUuid, text));
        }
        return result;
    }

    public static String set(UUID playerUuid, String text) {
        if (!PlayerGuild.USE_PAPI || playerUuid == null) {
            return text;
        }
        if (PlaceholderAPI.containsPlaceholders((String)text)) {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer((UUID)playerUuid);
            return PlaceholderAPI.setPlaceholders((OfflinePlayer)offlinePlayer, (String)text);
        }
        return text;
    }

    public static List<String> set(Player player, List<String> texts) {
        if (!PlayerGuild.USE_PAPI || player == null) {
            return texts;
        }
        return PlaceholderAPI.setPlaceholders((Player)player, texts);
    }
}
