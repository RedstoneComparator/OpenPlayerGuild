package cn.handyplus.guild.lib;

import cn.handyplus.guild.util.LocalLanguageResources;
import java.util.Optional;
import java.util.logging.Level;
import org.bukkit.entity.Player;

public final class HandyHttpUtil {
    private static final String M = "";
    private static final String F = "/api/public/getIp";
    private static final String vmPhTw = "";

    private HandyHttpUtil() {
    }

    private static Optional<String> PppPPp() {
        return Optional.empty();
    }

    public static void checkVersion(Player player) {

    }

    private static void xxXxXx(Player player) {

    }

    public static void getCloudI18nJson(LanguageTypeEnum language) {
        if (InitApi.PLUGIN != null) {
            LocalLanguageResources.loadDictionaries(InitApi.PLUGIN, language == null ? "zh_cn" : language.getValue());
        }
    }

    public static void getCloudItem(LanguageTypeEnum language) {
        if (InitApi.PLUGIN != null) {
            LocalLanguageResources.loadDictionaries(InitApi.PLUGIN, language == null ? "zh_cn" : language.getValue());
        }
    }

    public static String getIp() {
        try {
            return HttpUtil.get(SignService.getInstance().get() + F);
        } catch (Exception error) {
            InitApi.PLUGIN.getLogger().log(Level.SEVERE, "Unable to query the public IP address", error);
            return "";
        }
    }
}
