package cn.handyplus.guild.core.score;

import cn.handyplus.guild.constants.GuildActivityType;
import cn.handyplus.guild.constants.GuildPvpRoomStatusEnum;
import cn.handyplus.guild.constants.ScoreConstants;
import cn.handyplus.guild.core.common.GuildPvpFinishUtil;
import cn.handyplus.guild.core.match.MatchPvpManager;
import cn.handyplus.guild.core.match.MatchPvpReward;
import cn.handyplus.guild.core.match.MatchPvpUtil;
import cn.handyplus.guild.core.score.ScorePvpData;
import cn.handyplus.guild.core.score.ScorePvpManager;
import cn.handyplus.guild.core.score.ScorePvpUtil;
import cn.handyplus.guild.enter.GuildPlayer;
import cn.handyplus.guild.event.GuildPvpFinishEvent;
import cn.handyplus.guild.lib.CollUtil;
import cn.handyplus.guild.lib.MessageUtil;
import cn.handyplus.guild.lib.PlayerSchedulerUtil;
import cn.handyplus.guild.lib.StrUtil;
import cn.handyplus.guild.param.GuildPvpMatchRoom;
import cn.handyplus.guild.param.GuildPvpPlayerParam;
import cn.handyplus.guild.service.GuildInfoService;
import cn.handyplus.guild.service.GuildPlayerService;
import cn.handyplus.guild.util.ConfigUtil;
import cn.handyplus.guild.util.GuildCacheUtil;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

public class ScorePvpReward {
    public static void issueReward(@NotNull Integer loseGuildId, @NotNull Integer winGuildId) {
        GuildPvpFinishUtil.finishPlayers(loseGuildId, winGuildId);
        ScorePvpReward.applyBattleResult(loseGuildId, winGuildId);
        Integer[] guildIdArray = new Integer[2];
        guildIdArray[0] = loseGuildId;
        guildIdArray[1] = winGuildId;
        MatchPvpReward.finishMatch(CollUtil.of(guildIdArray));
    }

    private static void J(Integer guildId) {
        int contribution = ConfigUtil.ACTIVITY_CONFIG.getInt("scoreMatePvp.failContribution", 30);
        ScorePvpReward.sendPlayerReward(guildId, contribution, ConfigUtil.ACTIVITY_CONFIG.getString("scoreMatePvpFailRewardMsg", ""), new ArrayList<String>());
    }

    public static void ooOoOO() {
        if (!ScoreConstants.ACTIVITY_RUNNING || ScoreConstants.ACCEPTING || ScoreConstants.REWARDED || ScorePvpReward.hasFinishedScoreRoom()) {
            return;
        }
        ScoreConstants.REWARDED = true;
        List<ScorePvpData> rankedData = ScoreConstants.SCORE_DATA_MAP.values().stream().filter(data -> Boolean.TRUE.equals(data.getJoined())).filter(data -> {
            if (data.getMatchCount() > 0) {
                return true;
            }
            return false;
        }).sorted(Comparator.comparing(ScorePvpData::getScore).reversed()).collect(Collectors.toList());
        if (CollUtil.isEmpty(rankedData)) {
            ScorePvpManager.J();
            return;
        }
        ConfigurationSection rewardSection = ConfigUtil.ACTIVITY_CONFIG.getConfigurationSection("scoreMatePvpReward");
        ConfigurationSection percentRewardSection = ConfigUtil.ACTIVITY_CONFIG.getConfigurationSection("scoreMatePvpPercentReward");
        if (rewardSection == null && percentRewardSection == null) {
            MessageUtil.sendAllMessage(ConfigUtil.ACTIVITY_CONFIG.getString("scoreMatePvpFinalMsg", ""));
            ScorePvpManager.J();
            return;
        }
        HashSet<Integer> rewardedGuildIds = new HashSet<Integer>();
        List<ScorePvpData> rankedList = rankedData;
        ScorePvpReward.J(rankedList, rewardSection, rewardedGuildIds);
        ScorePvpReward.applyPercentReward(rankedList, percentRewardSection, rewardedGuildIds);
        MessageUtil.sendAllMessage(ConfigUtil.ACTIVITY_CONFIG.getString("scoreMatePvpFinalMsg", ""));
        ScorePvpManager.J();
    }

    private static void pPppPp(ScorePvpData winData, ScorePvpData failData, int score) {
        String message = ConfigUtil.ACTIVITY_CONFIG.getString("scoreMatePvpFinishAllMsg", "");
        message = message.replace("${win}", winData.getGuildName()).replace("${fail}", failData.getGuildName()).replace("${score}", String.valueOf(score)).replace("${winScore}", String.valueOf(winData.getScore())).replace("${failScore}", String.valueOf(failData.getScore()));
        MessageUtil.sendAllMessage(message);
        GuildPvpFinishEvent.callEvent(GuildActivityType.SCORE_MATCH, winData.getGuildId(), failData.getGuildId(), winData.getGuildName(), failData.getGuildName());
    }

    private static void applyGuildReward(ScorePvpData data, int rank, ConfigurationSection section, Set<Integer> rewardedGuildIds) {
        if (section == null || rewardedGuildIds.contains(data.getGuildId())) {
            return;
        }
        int guildMoney = section.getInt("guildMoney");
        int guildActive = section.getInt("guildActive");
        int playerMoney = section.getInt("playerMoney");
        List<String> commands = section.getStringList("command");
        if (guildMoney == 0 && guildActive == 0 && playerMoney == 0 && CollUtil.isEmpty(commands)) {
            return;
        }
        if (guildMoney != 0) {
            GuildInfoService.getInstance().addMoneyById(data.getGuildId(), guildMoney);
        }
        if (guildActive != 0) {
            GuildInfoService.getInstance().addProsperityDegreeById(data.getGuildId(), guildActive);
        }
        Optional<GuildPlayer> owner = GuildPlayerService.getInstance().findOneRoleByGuildId(data.getGuildId());
        if (owner.isPresent()) {
            GuildPlayer guildPlayer = owner.get();
            if (playerMoney != 0) {
                GuildPlayerService.getInstance().addMoneyById(guildPlayer.getId(), playerMoney);
            }
            ScorePvpReward.pPppPp(commands, guildPlayer.getPlayerName(), data.getGuildName(), rank, data.getScore(), guildMoney, guildActive, playerMoney);
        }
        GuildCacheUtil.refresh(data.getGuildId());
        rewardedGuildIds.add(data.getGuildId());
    }

    public static String pPPppP(String text) {
        int length = text.length();
        int index = length - 1;
        char[] chars = new char[length];
        int keySecond = 38;
        int cursor = index;
        int keyFirst = 79;
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

    private static void pPppPp(List<String> commands, String playerName, String guildName, int rank, int score, int guildMoney, int guildActive, int playerMoney) {
        Iterator<String> commandIterator;
        Iterator<String> loopIterator = commandIterator = commands.iterator();
        while (loopIterator.hasNext()) {
            String command = commandIterator.next();
            if (StrUtil.isEmpty(command)) {
                loopIterator = commandIterator;
                continue;
            }
            command = command.replace("${player}", playerName).replace("${guildName}", guildName).replace("${rank}", String.valueOf(rank)).replace("${score}", String.valueOf(score)).replace("${guildMoney}", String.valueOf(guildMoney)).replace("${guildActive}", String.valueOf(guildActive)).replace("${playerMoney}", String.valueOf(playerMoney)).replace("${player}", playerName);
            loopIterator = commandIterator;
            PlayerSchedulerUtil.dispatchCommand(command);
        }
    }

    private static void J(List<ScorePvpData> rankedData, ConfigurationSection section, Set<Integer> rewardedGuildIds) {
        Iterator keyIterator;
        if (section == null) {
            return;
        }
        Iterator loopIterator = keyIterator = section.getKeys(false).stream().filter(key -> {
            block3: {
                try {
                    if (Integer.parseInt(key) <= 0) break block3;
                    return true;
                }
                catch (NumberFormatException numberFormatException) {
                    return false;
                }
            }
            boolean invalid = false;
            return invalid;
        }).sorted(Comparator.comparingInt(Integer::parseInt)).collect(Collectors.toList()).iterator();
        while (loopIterator.hasNext()) {
            String rankText = (String)keyIterator.next();
            int rank = Integer.parseInt(rankText);
            if (rankedData.size() < rank) {
                loopIterator = keyIterator;
                continue;
            }
            ScorePvpReward.applyGuildReward(rankedData.get(rank - 1), rank, ScorePvpReward.getSection(section, rankText), rewardedGuildIds);
            loopIterator = keyIterator;
        }
    }

    private static boolean hasFinishedScoreRoom() {
        GuildPvpMatchRoom matchRoom;
        Iterator<GuildPvpMatchRoom> roomIterator = MatchPvpManager.listRooms().iterator();
        block0: do {
            Iterator<GuildPvpMatchRoom> loopIterator = roomIterator;
            while (true) {
                if (!loopIterator.hasNext()) {
                    return false;
                }
                matchRoom = roomIterator.next();
                if (!GuildPvpRoomStatusEnum.FINISHED.equals((Object)matchRoom.getStatus())) continue block0;
                loopIterator = roomIterator;
            }
        } while (!ScorePvpUtil.isScoreMatch(matchRoom.getGuildIdList().get(0)));
        return true;
    }

    private static void sendPlayerReward(Integer guildId, int money, String messageTemplate, List<String> commands) {
        List<GuildPvpPlayerParam> startPlayers = MatchPvpUtil.getStartPlayers(guildId);
        if (CollUtil.isEmpty(startPlayers)) {
            return;
        }
        List<GuildPlayer> players = GuildPlayerService.getInstance().findByPlayerUuidList(startPlayers.stream().map(GuildPvpPlayerParam::getPlayerUuid).collect(Collectors.toList()));
        if (money > 0 && CollUtil.isNotEmpty(players)) {
            List<Integer> playerIds = players.stream().map(GuildPlayer::getId).collect(Collectors.toList());
            GuildPlayerService.getInstance().addMoneyByIds(playerIds, money);
        }
        for (GuildPlayer guildPlayer : players) {
            if (money > 0) {
                MessageUtil.sendMessage(guildPlayer.getPlayerUuid(), messageTemplate.replace("${money}", String.valueOf(money)));
            }
            ScorePvpReward.pPppPp(commands, guildPlayer.getPlayerName(), guildPlayer.getGuildInfoName(), 0, ScorePvpUtil.getScore(guildId), 0, 0, money);
        }
    }

    private static ConfigurationSection getSection(ConfigurationSection section, String key) {
        Object value = section.getValues(false).get(key);
        if (value instanceof ConfigurationSection) {
            return (ConfigurationSection)value;
        }
        return null;
    }

    private static void sendWinReward(Integer guildId) {
        int contribution = ConfigUtil.ACTIVITY_CONFIG.getInt("scoreMatePvp.winContribution", 0);
        List commands = ConfigUtil.ACTIVITY_CONFIG.getStringList("scoreMatePvp.winCommand");
        ScorePvpReward.sendPlayerReward(guildId, contribution, ConfigUtil.ACTIVITY_CONFIG.getString("scoreMatePvpWinRewardMsg", ""), commands);
    }

    private static synchronized void applyBattleResult(Integer loseGuildId, Integer winGuildId) {
        ScorePvpData winData = ScoreConstants.SCORE_DATA_MAP.get(winGuildId);
        ScorePvpData loseData = ScoreConstants.SCORE_DATA_MAP.get(loseGuildId);
        if (winData == null || loseData == null) {
            return;
        }
        int transferScore = ConfigUtil.ACTIVITY_CONFIG.getInt("scoreMatePvp.transferPercent", 10);
        transferScore = Math.max(1, loseData.getScore() * transferScore / 100);
        transferScore = Math.min(transferScore, loseData.getScore());
        ScorePvpData winData2 = winData;
        winData2.setScore(winData2.getScore() + transferScore);
        ScorePvpData loseData2 = loseData;
        loseData2.setScore(Math.max(0, loseData2.getScore() - transferScore));
        int participationScore = Math.max(0, ConfigUtil.ACTIVITY_CONFIG.getInt("scoreMatePvp.participationScore", 0));
        if (participationScore > 0) {
            ScorePvpData loseData3 = loseData;
            ScorePvpData winData3 = winData;
            winData3.setScore(winData3.getScore() + participationScore);
            loseData3.setScore(loseData3.getScore() + participationScore);
        }
        ScorePvpData loseData4 = loseData;
        ScorePvpData winData4 = winData;
        winData4.setWinCount(winData.getWinCount() + 1);
        loseData4.setFailCount(loseData4.getFailCount() + 1);
        winData.setMatchCount(winData4.getMatchCount() + 1);
        ScorePvpData loseData5 = loseData;
        loseData5.setMatchCount(loseData5.getMatchCount() + 1);
        ScoreConstants.RUNNING_GUILD_SET.remove(winGuildId);
        ScoreConstants.RUNNING_GUILD_SET.remove(loseGuildId);
        ScorePvpReward.sendWinReward(winGuildId);
        ScorePvpReward.J(loseGuildId);
        ScorePvpReward.pPppPp(winData, loseData, transferScore);
        ScorePvpReward.ooOoOO();
    }

    private ScorePvpReward() {
    }

    private static void applyPercentReward(List<ScorePvpData> rankedData, ConfigurationSection section, Set<Integer> rewardedGuildIds) {
        if (section == null) {
            return;
        }
        for (String rankText : section.getKeys(false).stream().filter(key -> {
            block3: {
                try {
                    int percent = Integer.parseInt(key);
                    if (percent <= 0) break block3;
                    if (percent > 100) break block3;
                    return true;
                }
                catch (NumberFormatException numberFormatException) {
                    return false;
                }
            }
            boolean invalid = false;
            return invalid;
        }).sorted(Comparator.comparingInt(Integer::parseInt)).collect(Collectors.toList())) {
            int rewardCount = Integer.parseInt(rankText);
            rewardCount = Math.max(1, (int)Math.ceil((double)(rankedData.size() * rewardCount) / 100.0));
            ConfigurationSection rewardSection = ScorePvpReward.getSection(section, rankText);
            for (int index = 0; index < rewardCount && index < rankedData.size(); ++index) {
                ScorePvpReward.applyGuildReward(rankedData.get(index), index + 1, rewardSection, rewardedGuildIds);
            }
        }
    }
}
