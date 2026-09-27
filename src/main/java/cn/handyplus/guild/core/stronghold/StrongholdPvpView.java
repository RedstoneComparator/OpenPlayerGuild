package cn.handyplus.guild.core.stronghold;

import cn.handyplus.guild.constants.GuildPvpRoomStatusEnum;
import cn.handyplus.guild.core.stronghold.StrongholdPvpPoint;
import cn.handyplus.guild.core.stronghold.StrongholdPvpRoom;
import cn.handyplus.guild.lib.CollUtil;
import cn.handyplus.guild.lib.hologram.HdUtil;
import cn.handyplus.guild.util.ConfigUtil;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StrongholdPvpView {
    private StrongholdPvpView() {
    }

    @NotNull
    private static List<String> buildRankLines(@NotNull StrongholdPvpRoom room, @NotNull StrongholdPvpPoint point) {
        List<Integer> guildIdList = new ArrayList<Integer>(room.getGuildIdList());
        guildIdList.sort(Comparator.comparing((Integer guildId) -> point.getScoreMap().getOrDefault(guildId, 0)).reversed().thenComparing(Comparator.comparing((Integer guildId) -> point.getOccupySecondMap().getOrDefault(guildId, 0)).reversed()));
        ArrayList<String> rankLines = new ArrayList<String>();
        String lineTemplate = ConfigUtil.ACTIVITY_CONFIG.getString("strongholdPvp.hologram.rankLine", "");
        for (Integer rankGuildId : guildIdList) {
            rankLines.add(lineTemplate.replace("${guild}", StrongholdPvpView.resolveGuildName(room, rankGuildId)).replace("${time}", String.valueOf(point.getOccupySecondMap().getOrDefault(rankGuildId, 0))).replace("${score}", String.valueOf(point.getScoreMap().getOrDefault(rankGuildId, 0))));
        }
        return rankLines;
    }

    public static void render(@NotNull StrongholdPvpRoom room) {
        if (!GuildPvpRoomStatusEnum.RUNNING.equals((Object)room.getStatus())) {
            return;
        }
        room.getPointList().forEach(point -> {
            double unused;
            Location location = point.getLocation();
            if (location.getWorld() == null) {
                return;
            }
            World world = location.getWorld();
            int radius = ConfigUtil.ACTIVITY_CONFIG.getInt("strongholdPvp.radius");
            radius = Math.max(1, radius);
            double yOffset = ConfigUtil.ACTIVITY_CONFIG.getDouble("strongholdPvp.particle.yOffset");
            double stepDegree = ConfigUtil.ACTIVITY_CONFIG.getDouble("strongholdPvp.particle.stepDegree");
            stepDegree = Math.max(5.0, stepDegree);
            Particle particle = Particle.valueOf((String)ConfigUtil.ACTIVITY_CONFIG.getString("strongholdPvp.particle.type"));
            double degree = 0.0;
            while (degree < 360.0) {
                double angleZ = Math.toRadians(degree);
                Location center = location;
                double xCoord = center.getX() + Math.cos(angleZ) * radius;
                angleZ = center.getZ() + Math.sin(angleZ) * radius;
                world.spawnParticle(particle, xCoord, location.getY() + yOffset, angleZ, 1, 0.0, 0.0, 0.0, 0.0);
                degree = degree + stepDegree;
            }
        });
    }

    public static boolean shouldRender(int tick) {
        if (!ConfigUtil.ACTIVITY_CONFIG.getBoolean("strongholdPvp.particle.enable")) {
            return false;
        }
        int interval = ConfigUtil.ACTIVITY_CONFIG.getInt("strongholdPvp.particle.interval");
        if (tick % (interval = Math.max(1, interval)) == 0) {
            return true;
        }
        return false;
    }

    @NotNull
    public static Location getHologramLocation(@NotNull Location location) {
        double yOffset = ConfigUtil.ACTIVITY_CONFIG.getDouble("strongholdPvp.hologram.yOffset");
        return location.clone().add(0.0, yOffset, 0.0);
    }

    @NotNull
    private static String resolveGuildName(@NotNull StrongholdPvpRoom room, @Nullable Integer guildId) {
        if (guildId == null) {
            return ConfigUtil.ACTIVITY_CONFIG.getString("strongholdPvp.hologram.noOwner", "无");
        }
        Integer ownerGuildId = guildId;
        return room.getGuildNameMap().getOrDefault(ownerGuildId, String.valueOf(ownerGuildId));
    }

    public static void upsertHologram(@NotNull StrongholdPvpRoom room) {
        if (!ConfigUtil.ACTIVITY_CONFIG.getBoolean("strongholdPvp.hologram.enable")) {
            return;
        }
        room.getPointList().forEach(point -> HdUtil.upsert(point.getHologramLocation(), StrongholdPvpView.buildHologramLines(room, point)));
    }

    public static void deleteHologram(@Nullable StrongholdPvpRoom room) {
        if (room == null || CollUtil.isEmpty(room.getPointList())) {
            return;
        }
        room.getPointList().forEach(point -> HdUtil.delete(point.getHologramLocation()));
    }

    @NotNull
    private static List<String> buildHologramLines(@NotNull StrongholdPvpRoom room, @NotNull StrongholdPvpPoint point) {
        ArrayList<String> lines = new ArrayList<String>();
        List<String> configLines = ConfigUtil.ACTIVITY_CONFIG.getStringList("strongholdPvp.hologram.lines");
        if (CollUtil.isEmpty(configLines)) {
            return lines;
        }
        int remainingSecond = ConfigUtil.ACTIVITY_CONFIG.getInt("strongholdPvp.time");
        remainingSecond = Math.max(0, Math.max(1, remainingSecond) - room.getStartSecond());
        for (String line : configLines) {
            if ("${rank}".equals(line)) {
                lines.addAll(StrongholdPvpView.buildRankLines(room, point));
                continue;
            }
            lines.add(line.replace("${point}", point.getName()).replace("${owner}", StrongholdPvpView.resolveGuildName(room, point.getOwnerGuildId())).replace("${time}", String.valueOf(remainingSecond)));
        }
        return lines;
    }

    public static String pPPppP(String value) {
        int n2 = value.length();
        int n3 = n2 - 1;
        char[] cArray = new char[n2];
        int n4 = 11;
        int n5 = n3;
        int n6 = 110;
        while (n5 >= 0) {
            int n7 = n3--;
            cArray[n7] = (char)(value.charAt(n7) ^ n6);
            if (n3 < 0) break;
            int n8 = n3--;
            cArray[n8] = (char)(value.charAt(n8) ^ n4);
            n5 = n3;
        }
        return new String(cArray);
    }
}
