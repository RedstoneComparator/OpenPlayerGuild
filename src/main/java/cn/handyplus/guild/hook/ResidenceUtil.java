package cn.handyplus.guild.hook;

import cn.handyplus.guild.PlayerGuild;
import cn.handyplus.guild.enter.GuildInfo;
import cn.handyplus.guild.lib.BaseConstants;
import cn.handyplus.guild.lib.HandySchedulerUtil;
import cn.handyplus.guild.lib.MessageUtil;
import cn.handyplus.guild.lib.VersionCheckEnum;
import cn.handyplus.guild.util.ConfigUtil;
import com.bekvon.bukkit.residence.api.ResidenceApi;
import com.bekvon.bukkit.residence.api.ResidenceInterface;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import com.bekvon.bukkit.residence.protection.CuboidArea;
import com.wimbli.WorldBorder.BorderData;
import com.wimbli.WorldBorder.Config;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ResidenceUtil {
    private final static String RESIDENCE_NAME_PREFIX = "world_guild_";
    private final static String MAIN_AREA_NAME = "main";

    private ResidenceUtil() {
    }

    public static synchronized void init(@NotNull Player player, @NotNull GuildInfo guildInfo, @NotNull Location location) {
        if (!PlayerGuild.USE_RES || guildInfo.getId() == null || location.getWorld() == null) {
            return;
        }
        if (!Bukkit.isPrimaryThread()) {
            HandySchedulerUtil.runTask(() -> ResidenceUtil.init(player, guildInfo, location));
            return;
        }
        ResidenceInterface residenceManager = ResidenceApi.getResidenceManager();
        if (residenceManager == null) {
            return;
        }
        String residenceName = ResidenceUtil.getResidenceName(guildInfo.getId());
        World world = location.getWorld();
        Location[] corners = ResidenceUtil.getBorderCorners(world, guildInfo, location);
        Location corner1 = corners[0];
        Location corner2 = corners[1];
        CuboidArea cuboidArea = new CuboidArea(corner1, corner2);
        ClaimedResidence residence = residenceManager.getByName(residenceName);
        boolean success = residence == null ? residenceManager.addResidence(player, residenceName, corner1, corner2, true) : ResidenceUtil.updateResidenceArea(residence, player, cuboidArea);
        if (!success) {
            MessageUtil.sendConsoleMessage("\u540c\u6b65\u516c\u4f1aResidence\u9886\u5730\u5931\u8d25:" + residenceName);
        }
    }

    public static void remove(@NotNull GuildInfo guildInfo) {
        if (!PlayerGuild.USE_RES || guildInfo.getId() == null) {
            return;
        }
        ResidenceInterface residenceManager = ResidenceApi.getResidenceManager();
        if (residenceManager == null) {
            return;
        }
        ClaimedResidence residence = residenceManager.getByName(ResidenceUtil.getResidenceName(guildInfo.getId()));
        if (residence == null) {
            return;
        }
        residence.remove();
    }

    private static boolean updateResidenceArea(@NotNull ClaimedResidence residence, @NotNull Player player, @NotNull CuboidArea area) {
        String mainAreaName = ResidenceUtil.getMainAreaName(residence);
        CuboidArea mainArea = residence.getCuboidAreabyName(mainAreaName);
        boolean success = true;
        if (!ResidenceUtil.isSameCuboidArea(mainArea, area)) {
            success = residence.replaceArea(player, area, mainAreaName, true);
        }
        if (!residence.getPermissions().getOwnerUUID().equals(player.getUniqueId())) {
            residence.getPermissions().setOwner(player.getName(), false);
        }
        return success;
    }

    private static boolean isSameCuboidArea(CuboidArea area, @NotNull CuboidArea otherArea) {
        if (area == null) {
            return false;
        }
        if (!area.getWorldName().equals(otherArea.getWorldName())) {
            return false;
        }
        if (!area.getLowVector().equals((Object)otherArea.getLowVector())) {
            return false;
        }
        return area.getHighVector().equals((Object)otherArea.getHighVector());
    }

    private static String getMainAreaName(@NotNull ClaimedResidence residence) {
        CuboidArea mainArea = residence.getMainArea();
        for (Map.Entry entry : residence.getAreaMap().entrySet()) {
            if (entry.getValue() != mainArea) continue;
            return (String)entry.getKey();
        }
        String[] areaNames = residence.getAreaList();
        if (areaNames.length > 0) {
            return areaNames[0];
        }
        return MAIN_AREA_NAME;
    }

    private static String getResidenceName(@NotNull Integer guildId) {
        return RESIDENCE_NAME_PREFIX + guildId;
    }

    private static Location[] getBorderCorners(@NotNull World world, @NotNull GuildInfo guildInfo, @NotNull Location location) {
        String borderType = ConfigUtil.WORLD_CONFIG.getString("borderType", "WorldBorder");
        if ("WorldBorder".equalsIgnoreCase(borderType) && PlayerGuild.USE_WORLD_BORDER) {
            BorderData borderData = Config.Border((String)world.getName());
            if (borderData != null) {
                return ResidenceUtil.createCorners(world, borderData.getX(), borderData.getZ(), borderData.getRadiusX(), borderData.getRadiusZ());
            }
            int borderX = ConfigUtil.WORLD_CONFIG.getInt("guildSetting." + guildInfo.getLevel() + ".x");
            int borderZ = ConfigUtil.WORLD_CONFIG.getInt("guildSetting." + guildInfo.getLevel() + ".z");
            return ResidenceUtil.createCorners(world, location.getX(), location.getZ(), borderX, borderZ);
        }
        WorldBorder bukkitBorder = world.getWorldBorder();
        Location center = bukkitBorder.getCenter();
        double size = bukkitBorder.getSize() / 2.0;
        return ResidenceUtil.createCorners(world, center.getX(), center.getZ(), size, size);
    }

    private static Location[] createCorners(@NotNull World world, double centerX, double centerZ, double radiusX, double radiusZ) {
        int minHeight = 0;
        int maxHeight = 256;
        if (BaseConstants.VERSION_ID > VersionCheckEnum.V_1_17.getVersionId()) {
            minHeight = world.getMinHeight();
            maxHeight = world.getMaxHeight();
        }
        Location cornerLow = new Location(world, centerX - radiusX, (double)minHeight, centerZ - radiusZ);
        Location cornerHigh = new Location(world, centerX + radiusX, (double)(maxHeight - 1), centerZ + radiusZ);
        return new Location[]{cornerLow, cornerHigh};
    }
}
