package cn.handyplus.guild.hook;

import cn.handyplus.guild.PlayerGuild;
import cn.handyplus.guild.enter.GuildInfo;
import cn.handyplus.guild.lib.MessageUtil;
import cn.handyplus.guild.lib.StrUtil;
import cn.handyplus.guild.util.ConfigUtil;
import com.wimbli.WorldBorder.Config;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class WorldBorderUtil {
    private WorldBorderUtil() {
    }

    public static void setGuildBorder(GuildInfo guildInfo) {
        String worldName = guildInfo.getWorldName();
        if (StrUtil.isEmpty(worldName)) {
            return;
        }
        World world = Bukkit.getWorld((String)worldName);
        if (world == null) {
            return;
        }
        Location spawnLocation = world.getSpawnLocation();
        int borderX = ConfigUtil.WORLD_CONFIG.getInt("guildSetting." + guildInfo.getLevel() + ".x");
        int borderZ = ConfigUtil.WORLD_CONFIG.getInt("guildSetting." + guildInfo.getLevel() + ".z");
        if (borderX == 0 || borderZ == 0) {
            MessageUtil.sendConsoleDebugMessage("&a\u8d85\u8fc7\u7b49\u7ea7\u4e0a\u9650\uff0c\u9ed8\u8ba4\u4e0d\u8bbe\u7f6e");
            return;
        }
        WorldBorderUtil.setBorder(spawnLocation, borderX, borderZ);
    }

    public static void setBorder(Location location, int borderX, int borderZ) {
        World world = location.getWorld();
        if (world == null) {
            return;
        }
        String worldName = world.getName();
        String borderType = ConfigUtil.WORLD_CONFIG.getString("borderType", "WorldBorder");
        if ("WorldBorder".equalsIgnoreCase(borderType) && PlayerGuild.USE_WORLD_BORDER) {
            Config.setBorder((String)worldName, (int)borderX, (int)borderZ, (double)location.getX(), (double)location.getZ(), (Boolean)false);
        } else {
            world.getWorldBorder().setCenter(location);
            world.getWorldBorder().setSize((double)borderX);
        }
    }
}
