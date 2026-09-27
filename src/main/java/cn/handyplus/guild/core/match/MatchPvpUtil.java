package cn.handyplus.guild.core.match;

import cn.handyplus.guild.constants.GuildPvpBattleTypeEnum;
import cn.handyplus.guild.constants.GuildPvpRoomStatusEnum;
import cn.handyplus.guild.core.common.GuildPvpTimeUtil;
import cn.handyplus.guild.core.match.MatchPvpManager;
import cn.handyplus.guild.param.GuildPvpMatchQueue;
import cn.handyplus.guild.param.GuildPvpMatchRoom;
import cn.handyplus.guild.param.GuildPvpPlayerParam;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public class MatchPvpUtil {
    public static boolean isMatchRunning(UUID playerUuid) {
        for (GuildPvpMatchRoom guildPvpMatchRoom : MatchPvpManager.listRooms()) {
            if (!MatchPvpUtil.isMatchRoom(guildPvpMatchRoom)) continue;
            if (!GuildPvpRoomStatusEnum.RUNNING.equals((Object)guildPvpMatchRoom.getStatus())) continue;
            for (Map.Entry<Integer, List<GuildPvpPlayerParam>> entry : guildPvpMatchRoom.getStartPlayerMap().entrySet()) {
                for (GuildPvpPlayerParam guildPvpPlayerParam : entry.getValue()) {
                    if (!guildPvpPlayerParam.getPlayerUuid().equals(playerUuid)) continue;
                    return true;
                }
            }
        }
        return false;
    }

    public static Integer getMatchGuildIdByPlayer(UUID playerUuid) {
        for (GuildPvpMatchRoom guildPvpMatchRoom : MatchPvpManager.listRooms()) {
            if (!MatchPvpUtil.isMatchRoom(guildPvpMatchRoom)) continue;
            if (!GuildPvpRoomStatusEnum.RUNNING.equals((Object)guildPvpMatchRoom.getStatus())) continue;
            for (Map.Entry<Integer, List<GuildPvpPlayerParam>> entry : guildPvpMatchRoom.getStartPlayerMap().entrySet()) {
                for (GuildPvpPlayerParam guildPvpPlayerParam : entry.getValue()) {
                    if (!guildPvpPlayerParam.getPlayerUuid().equals(playerUuid)) continue;
                    return entry.getKey();
                }
            }
        }
        return null;
    }

    public static Set<Integer> getUsedLocationIds() {
        Iterator<GuildPvpMatchRoom> roomIterator;
        HashSet<Integer> locationIds = new HashSet<Integer>();
        Iterator<GuildPvpMatchRoom> loopIterator = roomIterator = MatchPvpManager.listRooms().iterator();
        while (loopIterator.hasNext()) {
            GuildPvpMatchRoom guildPvpMatchRoom = roomIterator.next();
            loopIterator = roomIterator;
            locationIds.add(guildPvpMatchRoom.getLocationId());
        }
        return locationIds;
    }

    public static List<UUID> getPreparationPlayers(Integer guildId) {
        GuildPvpMatchRoom guildPvpMatchRoom = MatchPvpManager.getRoomByGuildId(guildId);
        if (guildPvpMatchRoom == null) {
            return new ArrayList<UUID>();
        }
        return new ArrayList<UUID>(guildPvpMatchRoom.getPreparationPlayerMap().getOrDefault(guildId, new ArrayList()));
    }

    private static boolean isMatchRoom(GuildPvpMatchRoom matchRoom) {
        if (matchRoom != null && GuildPvpBattleTypeEnum.MATCH.equals((Object)matchRoom.getBattleType())) {
            return true;
        }
        return false;
    }

    public static List<GuildPvpPlayerParam> getStartPlayers(Integer guildId) {
        GuildPvpMatchRoom guildPvpMatchRoom = MatchPvpManager.getRoomByGuildId(guildId);
        if (guildPvpMatchRoom == null) {
            return new ArrayList<GuildPvpPlayerParam>();
        }
        return new ArrayList<GuildPvpPlayerParam>(guildPvpMatchRoom.getStartPlayerMap().getOrDefault(guildId, new ArrayList()));
    }

    public static boolean isMatching(Integer guildId) {
        if (MatchPvpManager.pPpppP(guildId) != null) {
            return true;
        }
        return false;
    }

    public static Location getOriginLocation(@NotNull UUID playerUuid) {
        Iterator<GuildPvpMatchRoom> roomIterator = MatchPvpManager.listRooms().iterator();
        while (roomIterator.hasNext()) {
            Location location = roomIterator.next().getOriginLocationMap().get(playerUuid);
            if (location == null) continue;
            return location;
        }
        return null;
    }

    public static Location getLookLocation(@NotNull Integer guildId) {
        GuildPvpMatchRoom guildPvpMatchRoom = MatchPvpManager.getRoomByGuildId(guildId);
        if (guildPvpMatchRoom == null) {
            return null;
        }
        return guildPvpMatchRoom.getLookLocation();
    }

    public static Date getStartTime(Integer guildId) {
        GuildPvpMatchRoom guildPvpMatchRoom = MatchPvpManager.getRoomByGuildId(guildId);
        if (guildPvpMatchRoom == null) {
            return new Date();
        }
        return guildPvpMatchRoom.getStartTime();
    }

    public static Integer getGuildIdByPlayer(UUID playerUuid) {
        for (GuildPvpMatchRoom guildPvpMatchRoom : MatchPvpManager.listRooms()) {
            if (!GuildPvpRoomStatusEnum.RUNNING.equals((Object)guildPvpMatchRoom.getStatus())) continue;
            for (Map.Entry<Integer, List<GuildPvpPlayerParam>> entry : guildPvpMatchRoom.getStartPlayerMap().entrySet()) {
                for (GuildPvpPlayerParam guildPvpPlayerParam : entry.getValue()) {
                    if (!guildPvpPlayerParam.getPlayerUuid().equals(playerUuid)) continue;
                    return entry.getKey();
                }
            }
        }
        return null;
    }

    public static boolean isRunning(Integer guildId) {
        GuildPvpMatchRoom guildPvpMatchRoom = MatchPvpManager.getRoomByGuildId(guildId);
        if (MatchPvpUtil.isMatchRoom(guildPvpMatchRoom) && GuildPvpRoomStatusEnum.RUNNING.equals((Object)guildPvpMatchRoom.getStatus())) {
            return true;
        }
        return false;
    }

    public static boolean isPreparing(UUID playerUuid) {
        for (GuildPvpMatchRoom guildPvpMatchRoom : MatchPvpManager.listRooms()) {
            if (!GuildPvpRoomStatusEnum.PREPARING.equals((Object)guildPvpMatchRoom.getStatus())) continue;
            for (List<UUID> preparationPlayerList : guildPvpMatchRoom.getPreparationPlayerMap().values()) {
                if (preparationPlayerList.contains(playerUuid)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isStarting(Integer guildId) {
        GuildPvpMatchRoom guildPvpMatchRoom = MatchPvpManager.getRoomByGuildId(guildId);
        if (MatchPvpUtil.isMatchRoom(guildPvpMatchRoom) && GuildPvpRoomStatusEnum.STARTING.equals((Object)guildPvpMatchRoom.getStatus())) {
            return true;
        }
        return false;
    }

    public static boolean isOpen() {
        if (GuildPvpTimeUtil.isDateOpen("matePvp.startDate")) {
            if (GuildPvpTimeUtil.isTimeOpen("matePvp.startTime", true)) {
                return true;
            }
        }
        return false;
    }

    public static GuildPvpMatchRoom getRoomByGuildId(Integer guildId) {
        return MatchPvpManager.getRoomByGuildId(guildId);
    }

    public static boolean isPreparing(Integer guildId) {
        GuildPvpMatchRoom guildPvpMatchRoom = MatchPvpManager.getRoomByGuildId(guildId);
        if (MatchPvpUtil.isMatchRoom(guildPvpMatchRoom) && GuildPvpRoomStatusEnum.PREPARING.equals((Object)guildPvpMatchRoom.getStatus())) {
            return true;
        }
        return false;
    }

    public static boolean isActive(Integer guildId) {
        if (MatchPvpUtil.isMatching(guildId) || MatchPvpUtil.isPreparing(guildId) || MatchPvpUtil.isStarting(guildId) || MatchPvpUtil.isRunning(guildId)) {
            return true;
        }
        return false;
    }

    public static boolean isRunning() {
        for (GuildPvpMatchRoom guildPvpMatchRoom : MatchPvpManager.listRooms()) {
            if (!MatchPvpUtil.isMatchRoom(guildPvpMatchRoom) || !GuildPvpRoomStatusEnum.RUNNING.equals((Object)guildPvpMatchRoom.getStatus())) continue;
            return true;
        }
        return false;
    }

    public static List<GuildPvpPlayerParam> getAllStartPlayers(Integer guildId) {
        GuildPvpMatchRoom guildPvpMatchRoom = MatchPvpManager.getRoomByGuildId(guildId);
        if (guildPvpMatchRoom == null) {
            return new ArrayList<GuildPvpPlayerParam>();
        }
        ArrayList<GuildPvpPlayerParam> startPlayers = new ArrayList<GuildPvpPlayerParam>();
        for (List<GuildPvpPlayerParam> startPlayerList : guildPvpMatchRoom.getStartPlayerMap().values()) {
            startPlayers.addAll(startPlayerList);
        }
        return startPlayers;
    }

    public static String pPPppP(String text) {
        int length = text.length();
        int index = length - 1;
        char[] chars = new char[length];
        int keySecond = 73;
        int cursor = index;
        int keyFirst = 29;
        while (cursor >= 0) {
            int charIndex = index--;
            chars[charIndex] = (char)(text.charAt(charIndex) ^ keyFirst);
            if (index < 0) break;
            int nextCharIndex = index--;
            chars[nextCharIndex] = (char)(text.charAt(nextCharIndex) ^ keySecond);
            cursor = index;
        }
        return new String(chars);
    }

    public static Integer getWaitSecond(Integer guildId) {
        GuildPvpMatchQueue guildPvpMatchQueue = MatchPvpManager.pPpppP(guildId);
        if (guildPvpMatchQueue == null) {
            return null;
        }
        return guildPvpMatchQueue.getWaitSecond();
    }

    public static boolean isRunning(UUID playerUuid) {
        if (MatchPvpUtil.getGuildIdByPlayer(playerUuid) != null) {
            return true;
        }
        return false;
    }

    private MatchPvpUtil() {
    }
}
