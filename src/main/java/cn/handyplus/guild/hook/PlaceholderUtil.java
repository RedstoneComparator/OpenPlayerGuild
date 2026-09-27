package cn.handyplus.guild.hook;

import cn.handyplus.guild.PlayerGuild;
import cn.handyplus.guild.constants.PapiHandleEnum;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class PlaceholderUtil
extends PlaceholderExpansion {
    private final PlayerGuild plugin;

    public PlaceholderUtil(PlayerGuild plugin) {
        this.plugin = plugin;
    }

    @NotNull
    public String getIdentifier() {
        return "playerGuild";
    }

    public String onRequest(OfflinePlayer offlinePlayer, @NotNull String params) {
        if (offlinePlayer == null) {
            return null;
        }
        return PapiHandleEnum.execution(offlinePlayer, params);
    }

    public boolean persist() {
        return true;
    }

    public boolean canRegister() {
        return true;
    }

    @NotNull
    public String getAuthor() {
        return this.plugin.getDescription().getAuthors().toString();
    }

    @NotNull
    public String getVersion() {
        return this.plugin.getDescription().getVersion();
    }
}
