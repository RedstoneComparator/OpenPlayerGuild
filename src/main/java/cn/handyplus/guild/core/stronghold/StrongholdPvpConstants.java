package cn.handyplus.guild.core.stronghold;

import cn.handyplus.guild.core.stronghold.StrongholdPvpRoom;
import cn.handyplus.guild.param.GuildPvpMatchQueue;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class StrongholdPvpConstants {
    public final static List<Integer> COUNT_DOWN_SECONDS;
    public final static List<Integer> PREPARE_NOTICE_SECONDS;
    public final static int PREPARE_TOTAL_SECONDS = 31;
    public final static Map<Integer, GuildPvpMatchQueue> QUEUE_MAP;
    public final static Map<Integer, Integer> GUILD_ROOM_MAP;
    public final static Map<Integer, StrongholdPvpRoom> ROOM_MAP;
    public final static int SCORE_MESSAGE_INTERVAL_SECONDS = 60;
    public final static Map<UUID, Integer> PLAYER_ROOM_MAP;

    private StrongholdPvpConstants() {
    }

    static {
        QUEUE_MAP = new ConcurrentHashMap<Integer, GuildPvpMatchQueue>();
        ROOM_MAP = new ConcurrentHashMap<Integer, StrongholdPvpRoom>();
        GUILD_ROOM_MAP = new ConcurrentHashMap<Integer, Integer>();
        PLAYER_ROOM_MAP = new ConcurrentHashMap<UUID, Integer>();
        Integer[] prepareNoticeArray = new Integer[5];
        prepareNoticeArray[0] = 1;
        prepareNoticeArray[1] = 6;
        prepareNoticeArray[2] = 11;
        prepareNoticeArray[3] = 21;
        prepareNoticeArray[4] = 30;
        PREPARE_NOTICE_SECONDS = Arrays.asList(prepareNoticeArray);
        Integer[] countDownArray = new Integer[5];
        countDownArray[0] = 11;
        countDownArray[1] = 21;
        countDownArray[2] = 28;
        countDownArray[3] = 29;
        countDownArray[4] = 30;
        COUNT_DOWN_SECONDS = Arrays.asList(countDownArray);
    }
}
