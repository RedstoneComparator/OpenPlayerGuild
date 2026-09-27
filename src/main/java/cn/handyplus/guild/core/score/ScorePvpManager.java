package cn.handyplus.guild.core.score;

import cn.handyplus.guild.constants.GuildActivityType;
import cn.handyplus.guild.constants.ScoreConstants;
import cn.handyplus.guild.core.common.GuildPvpUtil;
import cn.handyplus.guild.core.match.MatchPvpManager;
import cn.handyplus.guild.core.score.ScorePvpData;
import cn.handyplus.guild.core.score.ScorePvpReward;
import cn.handyplus.guild.core.score.ScorePvpUtil;
import cn.handyplus.guild.enter.GuildInfo;
import cn.handyplus.guild.enter.GuildPlayer;
import cn.handyplus.guild.lib.BaseUtil;
import cn.handyplus.guild.lib.CollUtil;
import cn.handyplus.guild.lib.DateUtil;
import cn.handyplus.guild.lib.MapUtil;
import cn.handyplus.guild.lib.MessageUtil;
import cn.handyplus.guild.lib.StrUtil;
import cn.handyplus.guild.param.GuildPvpMatchRoom;
import cn.handyplus.guild.param.GuildPvpPlayerParam;
import cn.handyplus.guild.service.GuildInfoService;
import cn.handyplus.guild.service.GuildPlayerService;
import cn.handyplus.guild.util.ConfigUtil;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public final class ScorePvpManager {
    public static void autoAcceptPreparation(GuildPvpMatchRoom matchRoom) {
        for (Map.Entry<Integer, List<GuildPlayer>> entry : matchRoom.getGuildPlayerMap().entrySet()) {
            List<UUID> preparationPlayers = matchRoom.getPreparationPlayerMap().get(entry.getKey());
            if (preparationPlayers == null) {
                continue;
            }
            for (GuildPlayer guildPlayer : entry.getValue()) {
                Optional<Player> optional = BaseUtil.getOnlinePlayer(guildPlayer.getPlayerUuid());
                if (!optional.isPresent()) {
                    continue;
                }
                Player player = optional.get();
                if (player.isDead()) {
                    continue;
                }
                preparationPlayers.add(player.getUniqueId());
            }
        }
    }

    private static ScorePvpData createData(Integer guildId) {
        ScorePvpData data;
        ScorePvpData created = data = new ScorePvpData();
        created.setGuildId(guildId);
        created.setGuildName(GuildInfoService.getInstance().findById(guildId).map(GuildInfo::getGuildName).orElse(""));
        ScorePvpData result = data;
        ScorePvpData initialized = data;
        data.setScore(ConfigUtil.ACTIVITY_CONFIG.getInt("scoreMatePvp.defaultScore", 100));
        initialized.setJoined(false);
        initialized.setMatchCount(0);
        result.setWinCount(0);
        result.setFailCount(0);
        ScoreConstants.SCORE_DATA_MAP.put(guildId, data);
        return result;
    }

    public static synchronized void toggleQueue(Player player, Integer guildId) {
        if (ScoreConstants.QUEUE_MAP.remove(guildId) != null) {
            MessageUtil.sendMessage(player, ConfigUtil.ACTIVITY_CONFIG.getString("scoreMatePvpCancelMsg", ""));
            return;
        }
        if (!ScorePvpUtil.isOpen()) {
            MessageUtil.sendMessage(player, ConfigUtil.ACTIVITY_CONFIG.getString("scoreMatePvpStopMsg", ""));
            return;
        }
        if (GuildPvpUtil.isClickCooling(player, ConfigUtil.ACTIVITY_CONFIG.getInt("scoreMatePvp.clickWait", 30))) {
            return;
        }
        if (GuildPvpUtil.isPvpLocationMissing(player, GuildActivityType.MATCH)) {
            return;
        }
        Object value = ScoreConstants.SCORE_DATA_MAP.get(guildId);
        if (value == null) {
            value = ScorePvpManager.createData(guildId);
        }
        ((ScorePvpData)value).setJoined(true);
        ScoreConstants.QUEUE_MAP.put(guildId, System.currentTimeMillis());
        value = ConfigUtil.ACTIVITY_CONFIG.getString("scoreMatePvpJoinMsg", "");
        value = ((String)value).replace("${score}", String.valueOf(ScorePvpUtil.getScore(guildId)));
        MessageUtil.sendMessage(player, (String)value);
    }

    private static String formatNextTime(int currentMinute, int intervalMinutes) {
        intervalMinutes = Math.min(currentMinute + intervalMinutes, ScorePvpUtil.getEndMinute());
        Object[] timeParts = new Object[2];
        timeParts[0] = intervalMinutes / 60;
        timeParts[1] = intervalMinutes % 60;
        return String.format("%02d:%02d", timeParts);
    }

    public static synchronized void init() {
        if (!ConfigUtil.ACTIVITY_CONFIG.getBoolean("scoreMatePvp.enable", false) || !ScorePvpUtil.isDateOpen()) {
            ScorePvpManager.J();
            return;
        }
        if (ScorePvpUtil.isTimeOpen()) {
            if (!ScoreConstants.ACTIVITY_RUNNING) {
                ScorePvpManager.startActivity();
            }
            ScoreConstants.ACCEPTING = true;
            ScorePvpManager.tickMatch();
            return;
        }
        if (ScoreConstants.ACTIVITY_RUNNING && ScorePvpUtil.isAfterEndTime()) {
            ScoreConstants.ACCEPTING = false;
            ScoreConstants.QUEUE_MAP.clear();
            ScorePvpReward.ooOoOO();
        }
    }

    public static boolean handleNoPlayerForfeit(GuildPvpMatchRoom matchRoom, Map<Integer, List<GuildPvpPlayerParam>> startPlayerMap) {
        List<GuildPvpPlayerParam> firstPlayers = startPlayerMap.get(matchRoom.getGuildIdList().get(0));
        List<GuildPvpPlayerParam> secondPlayers = startPlayerMap.get(matchRoom.getGuildIdList().get(1));
        boolean firstEmpty = CollUtil.isEmpty(firstPlayers);
        boolean secondEmpty = CollUtil.isEmpty(secondPlayers);
        if (!firstEmpty && !secondEmpty) {
            return false;
        }
        if (firstEmpty && secondEmpty) {
            ScorePvpManager.releaseRunning(matchRoom.getGuildIdList());
            MatchPvpManager.cancelNoPlayerRoom(matchRoom);
            return true;
        }
        Integer loseGuildId = firstEmpty ? matchRoom.getGuildIdList().get(0) : matchRoom.getGuildIdList().get(1);
        Integer winGuildId = firstEmpty ? matchRoom.getGuildIdList().get(1) : matchRoom.getGuildIdList().get(0);
        MatchPvpManager.finishBattle(loseGuildId, winGuildId);
        return true;
    }

    private static void tickMatch() {
        if (!ScoreConstants.MATCH_BATCH_RUNNING) {
            String currentMinuteText = DateUtil.format(new Date(), "HH:mm");
            if (currentMinuteText.equals(ScoreConstants.LAST_MATCH_MINUTE)) {
                return;
            }
            int currentMinute = ScorePvpUtil.getMinute(currentMinuteText);
            int startMinute = ScorePvpUtil.getStartMinute();
            int intervalMinutes = Math.max(1, ConfigUtil.ACTIVITY_CONFIG.getInt("scoreMatePvp.matchIntervalMinutes", 2));
            if ((currentMinute - 4) % intervalMinutes != 0 || MapUtil.isEmpty(ScoreConstants.QUEUE_MAP)) {
                return;
            }
            ScoreConstants.LAST_MATCH_MINUTE = currentMinuteText;
            if (ScoreConstants.QUEUE_MAP.size() < 2) {
                ScorePvpManager.notifyWaitNext(new ArrayList<Integer>(ScoreConstants.QUEUE_MAP.keySet()), currentMinute, intervalMinutes);
                return;
            }
            ScoreConstants.MATCH_BATCH_RUNNING = true;
        }
        if (!ScorePvpManager.startBatch()) {
            ScoreConstants.MATCH_BATCH_RUNNING = false;
        }
    }

    private static Comparator<Integer> scoreComparator() {
        return (firstGuildId, secondGuildId) -> {
            ScorePvpData firstData = ScoreConstants.SCORE_DATA_MAP.get(firstGuildId);
            ScorePvpData secondData = ScoreConstants.SCORE_DATA_MAP.get(secondGuildId);
            int firstScore = firstData == null ? 0 : firstData.getScore().intValue();
            int secondScore = secondData == null ? 0 : secondData.getScore().intValue();
            return Integer.compare(secondScore, firstScore);
        };
    }

    public static void J() {
        if (!ScoreConstants.ACTIVITY_RUNNING) {
            return;
        }
        ScoreConstants.SCORE_DATA_MAP.clear();
        ScoreConstants.QUEUE_MAP.clear();
        ScoreConstants.RUNNING_GUILD_SET.clear();
        ScoreConstants.ACTIVITY_RUNNING = false;
        ScoreConstants.ACCEPTING = false;
        ScoreConstants.REWARDED = false;
        ScoreConstants.ACTIVITY_DAY = "";
        ScoreConstants.LAST_MATCH_MINUTE = "";
        ScoreConstants.MATCH_BATCH_RUNNING = false;
    }

    private static boolean hasOnlinePlayer(@Nullable List<GuildPlayer> players) {
        Optional<Player> onlinePlayer;
        if (CollUtil.isEmpty(players)) {
            return false;
        }
        Iterator<GuildPlayer> playerIterator = players.iterator();
        block0: do {
            Iterator<GuildPlayer> loopIterator = playerIterator;
            while (true) {
                if (!loopIterator.hasNext()) {
                    return false;
                }
                onlinePlayer = BaseUtil.getOnlinePlayer(playerIterator.next().getPlayerUuid());
                if (onlinePlayer.isPresent()) continue block0;
                loopIterator = playerIterator;
            }
        } while (onlinePlayer.get().isDead());
        return true;
    }

    private static boolean startBatch() {
        List<Integer> queuedGuildIds = ScoreConstants.QUEUE_MAP.keySet().stream().filter(guildId -> {
            if (!ScoreConstants.RUNNING_GUILD_SET.contains(guildId)) {
                return true;
            }
            return false;
        }).filter(guildId -> {
            if (!ScorePvpUtil.isPreparing(guildId) && !ScorePvpUtil.isRunning(guildId)) {
                return true;
            }
            return false;
        }).collect(Collectors.toList());
        if (queuedGuildIds.size() < 2) {
            return false;
        }
        Map<Integer, List<GuildPlayer>> guildPlayersMap = GuildPlayerService.getInstance().findMapByGuildInfoIds(queuedGuildIds);
        List<Integer> rankedGuildIds = queuedGuildIds.stream().filter(candidateGuildId -> ScorePvpManager.hasOnlinePlayer(guildPlayersMap.get(candidateGuildId))).sorted(ScorePvpManager.scoreComparator()).collect(Collectors.toList());
        if (rankedGuildIds.size() < 2) {
            ScorePvpManager.notifyWaitNext(rankedGuildIds, ScorePvpUtil.getMinute(DateUtil.format(new Date(), "HH:mm")), Math.max(1, ConfigUtil.ACTIVITY_CONFIG.getInt("scoreMatePvp.matchIntervalMinutes", 5)));
            return false;
        }
        Integer firstGuildId = rankedGuildIds.get(0);
        Integer secondGuildId = rankedGuildIds.get(1);
        ArrayList<Integer> selectedGuildIds = new ArrayList<Integer>(2);
        selectedGuildIds.add(firstGuildId);
        selectedGuildIds.add(secondGuildId);
        if (!MatchPvpManager.createScoreRoom(selectedGuildIds)) {
            return false;
        }
        ScoreConstants.RUNNING_GUILD_SET.add(firstGuildId);
        ScoreConstants.RUNNING_GUILD_SET.add(secondGuildId);
        return true;
    }

    public static void releaseRunning(List<Integer> guildIds) {
        if (CollUtil.isEmpty(guildIds)) {
            return;
        }
        guildIds.forEach(ScoreConstants.RUNNING_GUILD_SET::remove);
    }

    private static void notifyWaitNext(List<Integer> guildIds, int currentMinute, int intervalMinutes) {
        if (CollUtil.isEmpty(guildIds)) {
            return;
        }
        String message = ConfigUtil.ACTIVITY_CONFIG.getString("scoreMatePvpWaitNextMsg", "");
        if (StrUtil.isEmpty(message)) {
            return;
        }
        String nextTimeText = ScorePvpManager.formatNextTime(currentMinute, intervalMinutes);
        Map<Integer, List<GuildPlayer>> guildPlayersMap = GuildPlayerService.getInstance().findMapByGuildInfoIds(guildIds);
        for (Integer guildId : guildIds) {
            List<GuildPlayer> players = guildPlayersMap.get(guildId);
            if (CollUtil.isEmpty(players)) {
                continue;
            }
            for (GuildPlayer guildPlayer : players) {
                Optional<Player> onlinePlayer = BaseUtil.getOnlinePlayer(guildPlayer.getPlayerUuid());
                if (!onlinePlayer.isPresent()) {
                    continue;
                }
                MessageUtil.sendMessage(onlinePlayer.get(), message.replace("${time}", nextTimeText));
            }
        }
    }

    private static void startActivity() {
        ScoreConstants.SCORE_DATA_MAP.clear();
        ScoreConstants.QUEUE_MAP.clear();
        ScoreConstants.RUNNING_GUILD_SET.clear();
        ScoreConstants.ACTIVITY_RUNNING = true;
        ScoreConstants.ACCEPTING = true;
        ScoreConstants.REWARDED = false;
        ScoreConstants.ACTIVITY_DAY = DateUtil.format(new Date(), "yyyy-MM-dd");
        ScoreConstants.LAST_MATCH_MINUTE = "";
        ScoreConstants.MATCH_BATCH_RUNNING = false;
        MessageUtil.sendAllMessage(ConfigUtil.ACTIVITY_CONFIG.getString("scoreMatePvpStartMsg", ""));
    }

    private ScorePvpManager() {
    }
}
