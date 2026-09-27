package cn.handyplus.guild.constants;

import cn.handyplus.guild.core.score.ScorePvpData;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class ScoreConstants {
    volatile public static boolean ACCEPTING;
    volatile public static String ACTIVITY_DAY;
    public final static Map<Integer, ScorePvpData> SCORE_DATA_MAP;
    volatile public static boolean ACTIVITY_RUNNING;
    volatile public static boolean MATCH_BATCH_RUNNING;
    public final static Map<Integer, Long> QUEUE_MAP;
    volatile public static String LAST_MATCH_MINUTE;
    volatile public static boolean REWARDED;
    public final static Set<Integer> RUNNING_GUILD_SET;

    private ScoreConstants() {
    }

    static {
        SCORE_DATA_MAP = new ConcurrentHashMap<Integer, ScorePvpData>();
        QUEUE_MAP = new ConcurrentHashMap<Integer, Long>();
        RUNNING_GUILD_SET = ConcurrentHashMap.newKeySet();
        ACTIVITY_RUNNING = false;
        ACCEPTING = false;
        REWARDED = false;
        ACTIVITY_DAY = "";
        LAST_MATCH_MINUTE = "";
        MATCH_BATCH_RUNNING = false;
    }
}
