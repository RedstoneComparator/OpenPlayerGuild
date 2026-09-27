package cn.handyplus.guild.core.match;

import cn.handyplus.guild.constants.GuildActivityType;
import cn.handyplus.guild.constants.GuildRoleEnum;
import cn.handyplus.guild.core.common.GuildPvpFinishResult;
import cn.handyplus.guild.core.common.GuildPvpFinishUtil;
import cn.handyplus.guild.core.common.GuildPvpMsgUtil;
import cn.handyplus.guild.core.match.MatchPvpManager;
import cn.handyplus.guild.enter.GuildPlayer;
import cn.handyplus.guild.event.GuildPvpFinishEvent;
import cn.handyplus.guild.lib.BaseUtil;
import cn.handyplus.guild.lib.CollUtil;
import cn.handyplus.guild.lib.DateUtil;
import cn.handyplus.guild.lib.MessageUtil;
import cn.handyplus.guild.lib.PlayerSchedulerUtil;
import cn.handyplus.guild.lib.StrUtil;
import cn.handyplus.guild.service.GuildPlayerService;
import cn.handyplus.guild.service.GuildPvpLogService;
import cn.handyplus.guild.service.GuildPvpPlayerLogService;
import cn.handyplus.guild.util.ConfigUtil;
import cn.handyplus.guild.util.GuildCacheUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;

public class MatchPvpReward {
    private MatchPvpReward() {
    }

    public static void issueReward(@NotNull Integer loseGuildId, @NotNull Integer winGuildId) {
        GuildPvpFinishResult guildPvpFinishResult = GuildPvpFinishUtil.finishPlayers(loseGuildId, winGuildId);
        Map<Integer, List<GuildPlayer>> guildPlayersByGuildId = GuildPlayerService.getInstance().findByPlayerUuidList(guildPvpFinishResult.getPlayerUuidList()).stream().collect(Collectors.groupingBy(GuildPlayer::getGuildInfoId));
        boolean winRewarded = MatchPvpReward.applyReward(winGuildId, guildPlayersByGuildId, true);
        MatchPvpReward.applyReward(loseGuildId, guildPlayersByGuildId, false);
        GuildPvpMsgUtil.sendMatchFinishAll(winRewarded, guildPvpFinishResult.getWinGuildName(), guildPvpFinishResult.getFailGuildName());
        GuildPvpFinishEvent.callEvent(GuildActivityType.MATCH, winGuildId, loseGuildId, guildPvpFinishResult.getWinGuildName(), guildPvpFinishResult.getFailGuildName());
        Integer[] guildIdArray = new Integer[2];
        guildIdArray[0] = loseGuildId;
        guildIdArray[1] = winGuildId;
        MatchPvpReward.finishMatch(CollUtil.of(guildIdArray));
    }

    private static boolean applyReward(Integer guildId, Map<Integer, List<GuildPlayer>> guildPlayerMap, boolean isWin) {
        int todayCount = GuildPvpLogService.getInstance().findByGuildInfoIdAndStartTime(guildId, DateUtil.getToday());
        int maxNumber = ConfigUtil.ACTIVITY_CONFIG.getInt("matePvp.number");
        if (todayCount >= maxNumber) {
            return false;
        }
        int rewardMoney = ConfigUtil.ACTIVITY_CONFIG.getInt(isWin ? "matePvpWin" : "matePvpFail");
        int ownerExtraReward = ConfigUtil.ACTIVITY_CONFIG.getInt("matePvpOwnerExtraReward", 0);
        String rewardMessage = ConfigUtil.ACTIVITY_CONFIG.getString(isWin ? "matePvpWinMsg" : "matePvpFailMsg", "");
        List<String> commands = ConfigUtil.ACTIVITY_CONFIG.getStringList(isWin ? "matePvpWinCommand" : "matePvpFailCommand");
        List<GuildPlayer> players = guildPlayerMap.getOrDefault(guildId, new ArrayList<>());
        if (CollUtil.isEmpty(players)) {
            return true;
        }
        List<UUID> playerUuids = players.stream().map(GuildPlayer::getPlayerUuid).collect(Collectors.toList());
        Map<UUID, Integer> counts = GuildPvpPlayerLogService.getInstance().findCountMapByPlayerUuids(playerUuids, DateUtil.getToday());
        HashMap<Integer, List<Integer>> playerIdsByMoney = new HashMap<Integer, List<Integer>>();
        ArrayList<GuildPlayer> rewardedPlayers = new ArrayList<GuildPlayer>();
        ArrayList<Integer> moneyList = new ArrayList<Integer>();
        for (GuildPlayer guildPlayer : players) {
            int playerCount = counts.getOrDefault(guildPlayer.getPlayerUuid(), 0);
            if (playerCount >= maxNumber) {
                MessageUtil.sendMessage(guildPlayer.getPlayerUuid(), BaseUtil.getLangMsg("matePvpRewardLimitMsg"));
                continue;
            }
            int money = rewardMoney;
            if (GuildRoleEnum.ONE.getRoleId().equals(guildPlayer.getRoleId())) {
                money += ownerExtraReward;
            }
            playerIdsByMoney.computeIfAbsent(money, amount -> new ArrayList<>()).add(guildPlayer.getId());
            rewardedPlayers.add(guildPlayer);
            moneyList.add(money);
        }
        for (Map.Entry<Integer, List<Integer>> entry : playerIdsByMoney.entrySet()) {
            GuildPlayerService.getInstance().addMoneyByIds(entry.getValue(), entry.getKey());
        }
        for (int index = 0; index < rewardedPlayers.size(); ++index) {
            GuildPlayer guildPlayer = rewardedPlayers.get(index);
            int money = moneyList.get(index);
            String playerMessage = rewardMessage.replace("${money}", String.valueOf(money));
            MessageUtil.sendMessage(guildPlayer.getPlayerUuid(), playerMessage);
            if (!CollUtil.isEmpty(commands)) {
                for (String command : commands) {
                    if (StrUtil.isEmpty(command)) {
                        continue;
                    }
                    command = StrUtil.replace(command, "${player}", guildPlayer.getPlayerName());
                    PlayerSchedulerUtil.dispatchCommand(command);
                }
            }
        }
        return true;
    }

    public static void finishMatch(List<Integer> guildIds) {
        guildIds.forEach(GuildCacheUtil::refresh);
        MatchPvpManager.finishRoom(guildIds);
    }
}
