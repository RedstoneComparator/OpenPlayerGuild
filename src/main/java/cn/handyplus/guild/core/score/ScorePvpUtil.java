package cn.handyplus.guild.core.score;

import cn.handyplus.guild.constants.GuildPvpBattleTypeEnum;
import cn.handyplus.guild.constants.GuildPvpRoomStatusEnum;
import cn.handyplus.guild.constants.ScoreConstants;
import cn.handyplus.guild.core.common.GuildPvpTimeUtil;
import cn.handyplus.guild.core.match.MatchPvpManager;
import cn.handyplus.guild.core.score.ScorePvpData;
import cn.handyplus.guild.param.GuildPvpMatchRoom;
import cn.handyplus.guild.param.GuildPvpPlayerParam;
import cn.handyplus.guild.util.ConfigUtil;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import org.bukkit.Location;

public final class ScorePvpUtil {
    public static synchronized boolean isOpen() {
        if (ScoreConstants.ACCEPTING && ScorePvpUtil.isDateOpen() && ScorePvpUtil.isTimeOpen()) {
            return true;
        }
        return false;
    }

    public static int getMinute(String timeText) {
        return GuildPvpTimeUtil.getMinute(timeText);
    }

    public static synchronized int getScore(Integer guildId) {
        ScorePvpData scorePvpData = ScoreConstants.SCORE_DATA_MAP.get(guildId);
        if (scorePvpData == null) {
            return ConfigUtil.ACTIVITY_CONFIG.getInt("scoreMatePvp.defaultScore", 100);
        }
        return scorePvpData.getScore();
    }

    public static boolean isScoreMatch(Integer guildId) {
        return ScorePvpUtil.isScoreRoom(MatchPvpManager.getRoomByGuildId(guildId));
    }

    public static synchronized boolean isRunning(Integer guildId) {
        GuildPvpMatchRoom guildPvpMatchRoom = MatchPvpManager.getRoomByGuildId(guildId);
        if (ScorePvpUtil.isScoreRoom(guildPvpMatchRoom) && GuildPvpRoomStatusEnum.RUNNING.equals((Object)guildPvpMatchRoom.getStatus())) {
            return true;
        }
        return false;
    }

    public static boolean isDateOpen() {
        return GuildPvpTimeUtil.isDateOpen("scoreMatePvp.startDate");
    }

    public static int getStartMinute() {
        return GuildPvpTimeUtil.getStartMinute("scoreMatePvp.startTime", "19:00~20:00");
    }

    public static synchronized boolean isRunning() {
        for (GuildPvpMatchRoom guildPvpMatchRoom : MatchPvpManager.listRooms()) {
            if (!ScorePvpUtil.isScoreRoom(guildPvpMatchRoom) || !GuildPvpRoomStatusEnum.RUNNING.equals((Object)guildPvpMatchRoom.getStatus())) continue;
            return true;
        }
        return false;
    }

    public static synchronized boolean isPreparing(Integer guildId) {
        GuildPvpMatchRoom guildPvpMatchRoom = MatchPvpManager.getRoomByGuildId(guildId);
        if (ScorePvpUtil.isScoreRoom(guildPvpMatchRoom) && GuildPvpRoomStatusEnum.PREPARING.equals((Object)guildPvpMatchRoom.getStatus())) {
            return true;
        }
        return false;
    }

    private ScorePvpUtil() {
    }

    public static boolean isAfterEndTime() {
        return GuildPvpTimeUtil.isAfterEndTime("scoreMatePvp.startTime", "19:00~20:00");
    }

    private static boolean isScoreRoom(GuildPvpMatchRoom matchRoom) {
        if (matchRoom != null && GuildPvpBattleTypeEnum.SCORE_MATCH.equals((Object)matchRoom.getBattleType())) {
            return true;
        }
        return false;
    }

    public static synchronized boolean isQueueing(Integer guildId) {
        return ScoreConstants.QUEUE_MAP.containsKey(guildId);
    }

    public static synchronized Integer getGuildIdByPlayer(UUID playerUuid) {
        for (GuildPvpMatchRoom guildPvpMatchRoom : MatchPvpManager.listRooms()) {
            if (!ScorePvpUtil.isScoreRoom(guildPvpMatchRoom) || !GuildPvpRoomStatusEnum.RUNNING.equals((Object)guildPvpMatchRoom.getStatus())) continue;
            for (Map.Entry<Integer, List<GuildPvpPlayerParam>> entry : guildPvpMatchRoom.getStartPlayerMap().entrySet()) {
                for (GuildPvpPlayerParam guildPvpPlayerParam : entry.getValue()) {
                    if (!guildPvpPlayerParam.getPlayerUuid().equals(playerUuid)) continue;
                    return entry.getKey();
                }
            }
        }
        return null;
    }

    public static String getRankPercent(Integer guildId) {
        ScorePvpData scorePvpData = ScoreConstants.SCORE_DATA_MAP.get(guildId);
        if (!ScoreConstants.ACTIVITY_RUNNING || scorePvpData == null || !Boolean.TRUE.equals(scorePvpData.getJoined())) {
            return "-";
        }
        List<ScorePvpData> ranked = ScorePvpUtil.rankedData();
        int index = 0;
        while (index < ranked.size()) {
            if (guildId.equals(ranked.get(index).getGuildId())) {
                return (int)Math.ceil((double)(index + 1) * 100.0 / (double)ranked.size()) + "%";
            }
            ++index;
        }
        return "-";
    }

    public static String getRank(Integer guildId) {
        ScorePvpData scorePvpData = ScoreConstants.SCORE_DATA_MAP.get(guildId);
        if (!ScoreConstants.ACTIVITY_RUNNING || scorePvpData == null || !Boolean.TRUE.equals(scorePvpData.getJoined())) {
            return "-";
        }
        List<ScorePvpData> ranked = ScorePvpUtil.rankedData();
        int index = 0;
        while (index < ranked.size()) {
            if (guildId.equals(ranked.get(index).getGuildId())) {
                return String.valueOf(index + 1);
            }
            ++index;
        }
        return "-";
    }

    public static boolean isTimeOpen() {
        return GuildPvpTimeUtil.isTimeOpen("scoreMatePvp.startTime", "19:00~20:00", false);
    }

    public static Location getLookLocation(Integer guildId) {
        GuildPvpMatchRoom guildPvpMatchRoom = MatchPvpManager.getRoomByGuildId(guildId);
        if (ScorePvpUtil.isScoreRoom(guildPvpMatchRoom)) {
            return guildPvpMatchRoom.getLookLocation();
        }
        return null;
    }

    public static synchronized boolean isStarting(Integer guildId) {
        GuildPvpMatchRoom guildPvpMatchRoom = MatchPvpManager.getRoomByGuildId(guildId);
        if (ScorePvpUtil.isScoreRoom(guildPvpMatchRoom) && GuildPvpRoomStatusEnum.STARTING.equals((Object)guildPvpMatchRoom.getStatus())) {
            return true;
        }
        return false;
    }

    public static synchronized boolean isActive(Integer guildId) {
        if (ScoreConstants.RUNNING_GUILD_SET.contains(guildId) || ScorePvpUtil.isQueueing(guildId) || ScorePvpUtil.isPreparing(guildId) || ScorePvpUtil.isStarting(guildId) || ScorePvpUtil.isRunning(guildId)) {
            return true;
        }
        return false;
    }

    private static boolean hasJoined(ScorePvpData data) {
        return Boolean.TRUE.equals(data.getJoined());
    }

    private static List<ScorePvpData> rankedData() {
        return ScoreConstants.SCORE_DATA_MAP.values().stream().filter(ScorePvpUtil::hasJoined).sorted(Comparator.comparing(ScorePvpData::getScore).reversed().thenComparing(ScorePvpData::getGuildId)).collect(Collectors.toList());
    }

    public static int getEndMinute() {
        return GuildPvpTimeUtil.getEndMinute("scoreMatePvp.startTime", "19:00~20:00");
    }

    public static synchronized boolean isRunning(UUID playerUuid) {
        if (ScorePvpUtil.getGuildIdByPlayer(playerUuid) != null) {
            return true;
        }
        return false;
    }
}
