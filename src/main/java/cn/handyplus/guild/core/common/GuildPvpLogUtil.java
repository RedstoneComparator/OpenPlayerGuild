package cn.handyplus.guild.core.common;

import cn.handyplus.guild.constants.GuildActivityType;
import cn.handyplus.guild.core.match.MatchPvpUtil;
import cn.handyplus.guild.core.score.ScorePvpUtil;
import cn.handyplus.guild.core.season.SeasonPvpPlayer;
import cn.handyplus.guild.core.stronghold.StrongholdPvpPlayer;
import cn.handyplus.guild.core.stronghold.StrongholdPvpRoom;
import cn.handyplus.guild.enter.GuildInfo;
import cn.handyplus.guild.enter.GuildPvpLog;
import cn.handyplus.guild.enter.GuildPvpPlayerLog;
import cn.handyplus.guild.lib.HandySchedulerUtil;
import cn.handyplus.guild.param.GuildPvpPlayerParam;
import cn.handyplus.guild.service.GuildInfoService;
import cn.handyplus.guild.service.GuildPlayerService;
import cn.handyplus.guild.service.GuildPvpLogService;
import cn.handyplus.guild.service.GuildPvpPlayerLogService;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GuildPvpLogUtil {
    private static void recordSeasonLog(@NotNull Integer guildId, @NotNull Integer rank, @NotNull Integer season, @Nullable Date startTime, @NotNull List<SeasonPvpPlayer> seasonPlayers) {
        GuildPvpLog guildPvpLog;
        Date date = startTime == null ? new Date() : startTime;
        startTime = new Date();
        Optional<GuildInfo> guildInfoOptional = GuildInfoService.getInstance().findById(guildId);
        if (!guildInfoOptional.isPresent()) {
            return;
        }
        GuildInfo guildInfo = guildInfoOptional.get();
        GuildPvpLog guildPvpLog2 = guildPvpLog = new GuildPvpLog();
        GuildPvpLog guildPvpLog3 = guildPvpLog;
        GuildPvpLog guildPvpLog4 = guildPvpLog;
        guildPvpLog.setType(GuildActivityType.SEASON.getType());
        guildPvpLog4.setGuildInfoId(guildId);
        guildPvpLog4.setGuildInfoName(guildInfo.getGuildName());
        guildPvpLog3.setStartTime(date);
        guildPvpLog3.setSeason(season);
        guildPvpLog2.setRank(rank);
        guildPvpLog2.setEndTime(startTime);
        List<GuildPvpPlayerLog> playerLogs = new ArrayList<GuildPvpPlayerLog>();
        for (SeasonPvpPlayer seasonPvpPlayer : seasonPlayers) {
            GuildPvpPlayerLog guildPvpPlayerLog = new GuildPvpPlayerLog();
            guildPvpPlayerLog.setGuildInfoId(guildId);
            guildPvpPlayerLog.setGuildInfoName(guildInfo.getGuildName());
            guildPvpPlayerLog.setPlayerName(seasonPvpPlayer.getPlayerName());
            guildPvpPlayerLog.setPlayerUuid(seasonPvpPlayer.getPlayerUuid());
            guildPvpPlayerLog.setKill(seasonPvpPlayer.getKill());
            guildPvpPlayerLog.setDie(seasonPvpPlayer.getIsDeath().booleanValue() ? 1 : 0);
            guildPvpPlayerLog.setStartTime(date);
            guildPvpPlayerLog.setEndTime(startTime);
            playerLogs.add(guildPvpPlayerLog);
        }
        GuildPvpLogUtil.savePvpLog(guildPvpLog, playerLogs);
        GuildInfoService.getInstance().setSeasonRank(guildId, rank);
    }

    private static void recordMatchLog(@NotNull Integer guildId, @NotNull List<GuildPvpPlayerParam> players, boolean win) {
        GuildPvpLog guildPvpLog;
        Date date = MatchPvpUtil.getStartTime(guildId);
        Date date2 = new Date();
        Optional<GuildInfo> guildInfoOptional = GuildInfoService.getInstance().findById(guildId);
        if (!guildInfoOptional.isPresent()) {
            return;
        }
        GuildInfo guildInfo = guildInfoOptional.get();
        GuildPvpLog guildPvpLog2 = guildPvpLog = new GuildPvpLog();
        GuildPvpLog guildPvpLog3 = guildPvpLog;
        GuildPvpLog guildPvpLog4 = guildPvpLog;
        guildPvpLog4.setType(GuildPvpLogUtil.getActivityType(guildId));
        guildPvpLog4.setGuildInfoId(guildId);
        guildPvpLog3.setGuildInfoName(guildInfo.getGuildName());
        guildPvpLog3.setStartTime(date);
        guildPvpLog2.setResult(win);
        guildPvpLog2.setEndTime(date2);
        ArrayList<GuildPvpPlayerLog> playerLogs = new ArrayList<GuildPvpPlayerLog>();
        HashMap<String, List<UUID>> killDieMap = new HashMap<String, List<UUID>>();
        for (GuildPvpPlayerParam guildPvpPlayerParam : players) {
            int isDeath = guildPvpPlayerParam.getIsDeath().booleanValue() ? 1 : 0;
            GuildPvpPlayerLog guildPvpPlayerLog = new GuildPvpPlayerLog();
            guildPvpPlayerLog.setGuildInfoId(guildId);
            guildPvpPlayerLog.setGuildInfoName(guildInfo.getGuildName());
            guildPvpPlayerLog.setPlayerName(guildPvpPlayerParam.getPlayerName());
            guildPvpPlayerLog.setPlayerUuid(guildPvpPlayerParam.getPlayerUuid());
            guildPvpPlayerLog.setResult(win);
            guildPvpPlayerLog.setKill(guildPvpPlayerParam.getKill());
            guildPvpPlayerLog.setDie(isDeath);
            guildPvpPlayerLog.setStartTime(date);
            guildPvpPlayerLog.setEndTime(date2);
            playerLogs.add(guildPvpPlayerLog);
            killDieMap.computeIfAbsent(guildPvpPlayerParam.getKill() + "," + isDeath, key -> new ArrayList()).add(guildPvpPlayerParam.getPlayerUuid());
        }
        GuildPlayerService.getInstance().batchAddKillAndDie(killDieMap);
        GuildPvpLogUtil.savePvpLog(guildPvpLog, playerLogs);
    }

    public static void asyncRecordPvpLog(@NotNull Integer loserGuildId, @NotNull Integer winnerGuildId) {
        if (Bukkit.isPrimaryThread()) {
            HandySchedulerUtil.runTaskAsynchronously(() -> GuildPvpLogUtil.asyncRecordPvpLog(loserGuildId, winnerGuildId));
            return;
        }
        Integer winnerId = winnerGuildId;
        GuildPvpLogUtil.recordMatchLog(winnerId, MatchPvpUtil.getStartPlayers(winnerId), true);
        Integer loserId = loserGuildId;
        GuildPvpLogUtil.recordMatchLog(loserId, MatchPvpUtil.getStartPlayers(loserId), false);
    }

    private static void savePvpLog(@NotNull GuildPvpLog guildPvpLog, @NotNull List<GuildPvpPlayerLog> playerLogs) {
        Iterator<GuildPvpPlayerLog> playerIterator;
        int logId = GuildPvpLogService.getInstance().add(guildPvpLog);
        Iterator<GuildPvpPlayerLog> playerIteratorRef = playerIterator = playerLogs.iterator();
        while (playerIteratorRef.hasNext()) {
            playerIterator.next().setGuildPvpLogId(0);
            playerIteratorRef = playerIterator;
        }
        GuildPvpPlayerLogService.getInstance().addBatch(playerLogs);
    }

    public static void asyncRecordPvpLog(@NotNull Integer guildId, @NotNull Integer rank, @NotNull Integer season, @Nullable Date startTime, @NotNull List<SeasonPvpPlayer> seasonPlayers) {
        if (Bukkit.isPrimaryThread()) {
            HandySchedulerUtil.runTaskAsynchronously(() -> GuildPvpLogUtil.asyncRecordPvpLog(guildId, rank, season, startTime, seasonPlayers));
            return;
        }
        GuildPvpLogUtil.recordSeasonLog(guildId, rank, season, startTime, seasonPlayers);
    }

    private static String getActivityType(@NotNull Integer guildId) {
        if (ScorePvpUtil.isScoreMatch(guildId)) {
            return GuildActivityType.SCORE_MATCH.getType();
        }
        return GuildActivityType.MATCH.getType();
    }

    public static void asyncRecordPvpLog(@NotNull StrongholdPvpRoom room, @Nullable Integer winnerGuildId) {
        if (Bukkit.isPrimaryThread()) {
            HandySchedulerUtil.runTaskAsynchronously(() -> GuildPvpLogUtil.asyncRecordPvpLog(room, winnerGuildId));
            return;
        }
        GuildPvpLogUtil.recordStrongholdLog(room, winnerGuildId);
    }

    private GuildPvpLogUtil() {
    }

    private static void recordStrongholdLog(@NotNull StrongholdPvpRoom room, @Nullable Integer winnerGuildId) {
        Iterator<Integer> guildIdIterator;
        Date date = new Date();
        Map<Integer, GuildInfo> guildInfoMap = GuildInfoService.getInstance().findMapByIds(room.getGuildIdList());
        HashMap<String, List<UUID>> killDieMap = new HashMap<String, List<UUID>>();
        Iterator<Integer> guildIdIteratorRef = guildIdIterator = room.getGuildIdList().iterator();
        while (guildIdIteratorRef.hasNext()) {
            GuildPvpLog guildPvpLog;
            Integer guildId = guildIdIterator.next();
            GuildInfo guildInfo = guildInfoMap.get(guildId);
            if (guildInfo == null) {
                guildIdIteratorRef = guildIdIterator;
                continue;
            }
            GuildPvpLog guildPvpLog2 = guildPvpLog = new GuildPvpLog();
            GuildPvpLog guildPvpLog3 = guildPvpLog;
            GuildPvpLog guildPvpLog4 = guildPvpLog;
            guildPvpLog4.setType(GuildActivityType.STRONGHOLD.getType());
            guildPvpLog4.setGuildInfoId(guildId);
            guildPvpLog3.setGuildInfoName(guildInfo.getGuildName());
            guildPvpLog3.setStartTime(room.getStartTime());
            guildPvpLog2.setResult(guildId.equals(winnerGuildId));
            guildPvpLog2.setEndTime(date);
            ArrayList<GuildPvpPlayerLog> playerLogs = new ArrayList<GuildPvpPlayerLog>();
            for (StrongholdPvpPlayer strongholdPvpPlayer : room.getPlayerMap().values()) {
                if (!guildId.equals(strongholdPvpPlayer.getGuildId())) {
                    continue;
                }
                GuildPvpPlayerLog guildPvpPlayerLog = new GuildPvpPlayerLog();
                guildPvpPlayerLog.setGuildInfoId(guildId);
                guildPvpPlayerLog.setGuildInfoName(guildInfo.getGuildName());
                guildPvpPlayerLog.setPlayerName(strongholdPvpPlayer.getPlayerName());
                guildPvpPlayerLog.setPlayerUuid(strongholdPvpPlayer.getPlayerUuid());
                guildPvpPlayerLog.setResult(guildId.equals(winnerGuildId));
                guildPvpPlayerLog.setKill(strongholdPvpPlayer.getKill());
                guildPvpPlayerLog.setDie(strongholdPvpPlayer.getDie());
                guildPvpPlayerLog.setStartTime(room.getStartTime());
                guildPvpPlayerLog.setEndTime(date);
                playerLogs.add(guildPvpPlayerLog);
                killDieMap.computeIfAbsent(strongholdPvpPlayer.getKill() + "," + strongholdPvpPlayer.getDie(), key -> new ArrayList()).add(strongholdPvpPlayer.getPlayerUuid());
            }
            GuildPvpLogUtil.savePvpLog(guildPvpLog, playerLogs);
            guildIdIteratorRef = guildIdIterator;
        }
        GuildPlayerService.getInstance().batchAddKillAndDie(killDieMap);
    }
}
