package cn.handyplus.guild.hook;

import cn.handyplus.guild.PlayerGuild;
import cn.handyplus.guild.enter.GuildInfo;
import cn.handyplus.guild.hook.MultiverseLegacyUtil;
import cn.handyplus.guild.hook.MultiverseUtil;
import cn.handyplus.guild.hook.WorldBorderUtil;
import cn.handyplus.guild.hook.WorldRuleUtil;
import cn.handyplus.guild.hook.WorldsUtil;
import java.util.List;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class GuildWorldUtil {
    private GuildWorldUtil() {
    }

    public static void createWorld(Player player, Integer guildInfoId, String worldName, String alias) {
        if (PlayerGuild.USE_WORLDS) {
            WorldsUtil.createWorld(player, guildInfoId, worldName, alias);
            return;
        }
        if (PlayerGuild.USE_MV_5) {
            MultiverseUtil.createWorld(player, guildInfoId, worldName, alias);
            return;
        }
        if (PlayerGuild.USE_MV_4) {
            MultiverseLegacyUtil.createWorld(player, guildInfoId, worldName, alias);
        }
    }

    public static Location getSpawnLocation(String worldName) {
        if (PlayerGuild.USE_WORLDS) {
            return WorldsUtil.getSpawnLocation(worldName);
        }
        if (PlayerGuild.USE_MV_5) {
            return MultiverseUtil.getSpawnLocation(worldName);
        }
        if (PlayerGuild.USE_MV_4) {
            return MultiverseLegacyUtil.getSpawnLocation(worldName);
        }
        return null;
    }

    public static void removeWorld(String worldName, List<UUID> playerUuidList) {
        if (PlayerGuild.USE_WORLDS) {
            WorldsUtil.removeWorld(worldName, playerUuidList);
            return;
        }
        if (PlayerGuild.USE_MV_5) {
            MultiverseUtil.removeWorld(worldName, playerUuidList);
            return;
        }
        if (PlayerGuild.USE_MV_4) {
            MultiverseLegacyUtil.removeWorld(worldName, playerUuidList);
        }
    }

    public static void removePlayerFromWorld(UUID playerUuid, String worldName) {
        if (PlayerGuild.USE_WORLDS) {
            WorldsUtil.removePlayerFromWorld(playerUuid, worldName);
            return;
        }
        if (PlayerGuild.USE_MV_5) {
            MultiverseUtil.removePlayerFromWorld(playerUuid, worldName);
            return;
        }
        if (PlayerGuild.USE_MV_4) {
            MultiverseLegacyUtil.removePlayerFromWorld(playerUuid, worldName);
        }
    }

    public static void setGuildBorder(GuildInfo guildInfo) {
        WorldBorderUtil.setGuildBorder(guildInfo);
    }

    public static void setGameRule(String worldName) {
        WorldRuleUtil.setGameRule(worldName);
    }

    public static void setWorldAlias(String worldName, String alias) {
        if (PlayerGuild.USE_WORLDS) {
            return;
        }
        if (PlayerGuild.USE_MV_5) {
            MultiverseUtil.setWorldAlias(worldName, alias);
            return;
        }
        if (PlayerGuild.USE_MV_4) {
            MultiverseLegacyUtil.setWorldAlias(worldName, alias);
        }
    }

    public static String getWorldAlias(String worldName) {
        if (PlayerGuild.USE_WORLDS) {
            return worldName;
        }
        if (PlayerGuild.USE_MV_5) {
            return MultiverseUtil.getWorldAlias(worldName);
        }
        if (PlayerGuild.USE_MV_4) {
            return MultiverseLegacyUtil.getWorldAlias(worldName);
        }
        return worldName;
    }
}
