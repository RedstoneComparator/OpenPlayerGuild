package cn.handyplus.guild.core.stronghold;

import cn.handyplus.guild.constants.GuildPvpRoomStatusEnum;
import cn.handyplus.guild.core.common.GuildPvpTimeUtil;
import cn.handyplus.guild.core.stronghold.StrongholdPvpConstants;
import cn.handyplus.guild.core.stronghold.StrongholdPvpManager;
import cn.handyplus.guild.core.stronghold.StrongholdPvpPlayer;
import cn.handyplus.guild.core.stronghold.StrongholdPvpRoom;
import cn.handyplus.guild.param.GuildPvpMatchQueue;
import cn.handyplus.guild.util.ConfigUtil;
import java.util.List;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StrongholdPvpUtil {
    public static boolean isRunning() {
        for (StrongholdPvpRoom room : StrongholdPvpConstants.ROOM_MAP.values()) {
            if (!GuildPvpRoomStatusEnum.RUNNING.equals((Object)room.getStatus())) continue;
            return true;
        }
        return false;
    }

    @Nullable
    public static Integer getWaitSecond(@NotNull Integer guildId) {
        GuildPvpMatchQueue guildPvpMatchQueue = StrongholdPvpManager.pPpppP(guildId);
        if (guildPvpMatchQueue == null) {
            return null;
        }
        return guildPvpMatchQueue.getWaitSecond();
    }

    private StrongholdPvpUtil() {
    }

    public static boolean isRunning(@NotNull UUID playerUuid) {
        StrongholdPvpPlayer pvpPlayer = StrongholdPvpManager.getPlayer(playerUuid);
        if (pvpPlayer != null && StrongholdPvpUtil.isRunning(pvpPlayer.getGuildId())) {
            return true;
        }
        return false;
    }

    public static boolean isOpen() {
        if (ConfigUtil.ACTIVITY_CONFIG.getBoolean("strongholdPvp.enable") && GuildPvpTimeUtil.isDateOpen("strongholdPvp.startDate")) {
            if (GuildPvpTimeUtil.isTimeOpen("strongholdPvp.startTime", true)) {
                return true;
            }
        }
        return false;
    }

    @NotNull
    public static String getScoreText(@NotNull Integer guildId) {
        StrongholdPvpRoom room = StrongholdPvpManager.getRoomByGuildId(guildId);
        if (room == null) {
            return "0:0";
        }
        List<Integer> guildIdList = room.getGuildIdList();
        if (guildIdList.size() < 2) {
            return "0:0";
        }
        Integer firstGuildId = guildIdList.get(0);
        Integer secondGuildId = guildIdList.get(1);
        return room.getScoreMap().getOrDefault(firstGuildId, 0) + ":" + room.getScoreMap().getOrDefault(secondGuildId, 0);
    }

    public static boolean isRunning(@NotNull Integer guildId) {
        StrongholdPvpRoom room = StrongholdPvpManager.getRoomByGuildId(guildId);
        if (room != null && GuildPvpRoomStatusEnum.RUNNING.equals((Object)room.getStatus())) {
            return true;
        }
        return false;
    }

    public static boolean isPreparing(@NotNull Integer guildId) {
        StrongholdPvpRoom room = StrongholdPvpManager.getRoomByGuildId(guildId);
        if (room != null && GuildPvpRoomStatusEnum.PREPARING.equals((Object)room.getStatus())) {
            return true;
        }
        return false;
    }

    public static boolean isStarting(@NotNull Integer guildId) {
        StrongholdPvpRoom room = StrongholdPvpManager.getRoomByGuildId(guildId);
        if (room != null && GuildPvpRoomStatusEnum.STARTING.equals((Object)room.getStatus())) {
            return true;
        }
        return false;
    }

    public static boolean isQueueing(@NotNull Integer guildId) {
        if (StrongholdPvpManager.pPpppP(guildId) != null) {
            return true;
        }
        return false;
    }

    public static boolean isPlaying(@NotNull UUID playerUuid) {
        if (StrongholdPvpManager.getPlayer(playerUuid) != null) {
            return true;
        }
        return false;
    }

    public static boolean isActive(@NotNull Integer guildId) {
        if (StrongholdPvpUtil.isQueueing(guildId) || StrongholdPvpUtil.isPreparing(guildId) || StrongholdPvpUtil.isStarting(guildId) || StrongholdPvpUtil.isRunning(guildId)) {
            return true;
        }
        return false;
    }
}
