package cn.handyplus.guild.core.common;

import cn.handyplus.guild.constants.GuildActivityType;
import cn.handyplus.guild.constants.GuildConstants;
import cn.handyplus.guild.core.match.MatchPvpUtil;
import cn.handyplus.guild.core.score.ScorePvpUtil;
import cn.handyplus.guild.core.season.SeasonPvpUtil;
import cn.handyplus.guild.core.stronghold.StrongholdPvpUtil;
import cn.handyplus.guild.lib.MapUtil;
import cn.handyplus.guild.lib.MessageUtil;
import cn.handyplus.guild.param.GuildPvpMatchQueue;
import cn.handyplus.guild.service.GuildPvpLocationService;
import cn.handyplus.guild.util.ConfigUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GuildPvpUtil {
    private GuildPvpUtil() {
    }

    @NotNull
    public static Map<Integer, List<UUID>> initPreparationPlayerMap(@NotNull List<Integer> guildIds) {
        Iterator<Integer> guildIdIterator;
        List<Integer> guildIdList = guildIds;
        HashMap<Integer, List<UUID>> playerMap = MapUtil.newHashMapWithExpectedSize(guildIdList.size());
        Iterator<Integer> loopIterator = guildIdIterator = guildIdList.iterator();
        while (loopIterator.hasNext()) {
            Integer guildId = guildIdIterator.next();
            loopIterator = guildIdIterator;
            playerMap.put(guildId, new ArrayList());
        }
        return playerMap;
    }

    public static boolean isClickCooling(@NotNull Player player, int waitSeconds) {
        long elapsedSeconds;
        if (GuildConstants.MATE_PVP_CLICK_WAIT_TIME.containsKey(player.getUniqueId()) && (elapsedSeconds = (System.currentTimeMillis() - GuildConstants.MATE_PVP_CLICK_WAIT_TIME.get(player.getUniqueId())) / 1000L) < (long)waitSeconds) {
            String message = ConfigUtil.ACTIVITY_CONFIG.getString("matePvpClickMsg", "").replace("${time}", (long)waitSeconds - elapsedSeconds + "");
            MessageUtil.sendMessage(player, message);
            return true;
        }
        GuildConstants.MATE_PVP_CLICK_WAIT_TIME.put(player.getUniqueId(), System.currentTimeMillis());
        return false;
    }

    public static boolean isPvpLocationMissing(@NotNull Player player, @NotNull GuildActivityType activityType) {
        if (!GuildPvpLocationService.getInstance().existType(activityType.getType())) {
            MessageUtil.sendMessage(player, ConfigUtil.PVP_LOCATION_CONFIG.getString("noLocation"));
            return true;
        }
        return false;
    }

    @NotNull
    public static String getGuildPvpActiveName(@NotNull Integer guildId) {
        if (MatchPvpUtil.isActive(guildId)) {
            return GuildActivityType.getDesc(GuildActivityType.MATCH);
        }
        if (ScorePvpUtil.isActive(guildId)) {
            return GuildActivityType.getDesc(GuildActivityType.SCORE_MATCH);
        }
        if (StrongholdPvpUtil.isActive(guildId)) {
            return GuildActivityType.getDesc(GuildActivityType.STRONGHOLD);
        }
        if (SeasonPvpUtil.isActive(guildId)) {
            return GuildActivityType.getDesc(GuildActivityType.SEASON);
        }
        return "";
    }

    @NotNull
    public static List<GuildPvpMatchQueue> takeTwoQueue(@NotNull List<GuildPvpMatchQueue> queueList, @NotNull Map<Integer, GuildPvpMatchQueue> queueMap) {
        ArrayList<GuildPvpMatchQueue> pair;
        ArrayList<GuildPvpMatchQueue> shuffled = new ArrayList<GuildPvpMatchQueue>(queueList);
        Collections.shuffle(shuffled);
        ArrayList<GuildPvpMatchQueue> result = new ArrayList<GuildPvpMatchQueue>(2);
        Iterator queueIterator = shuffled.iterator();
        do {
            GuildPvpMatchQueue queue;
            Iterator scanIterator = queueIterator;
            while (true) {
                if (!scanIterator.hasNext()) {
                    return result;
                }
                queue = (GuildPvpMatchQueue)queueIterator.next();
                if ((queue = queueMap.remove(queue.getGuildId())) != null) break;
                scanIterator = queueIterator;
            }
            pair = result;
            pair.add(queue);
        } while (pair.size() != 2);
        return result;
    }
}
