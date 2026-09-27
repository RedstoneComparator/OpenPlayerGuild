package cn.handyplus.guild.core.match;

import cn.handyplus.guild.param.GuildPvpMatchQueue;
import cn.handyplus.guild.param.GuildPvpMatchRoom;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MatchPvpConstants {
    public final static Map<Integer, GuildPvpMatchRoom> A;
    public final static Map<Integer, GuildPvpMatchQueue> e;
    public final static int d = 31;
    public final static List<Integer> M;
    public final static Map<Integer, Integer> F;
    public final static List<Integer> XxXxxX;

    private MatchPvpConstants() {
    }

    static {
        e = new ConcurrentHashMap<Integer, GuildPvpMatchQueue>();
        A = new ConcurrentHashMap<Integer, GuildPvpMatchRoom>();
        F = new ConcurrentHashMap<Integer, Integer>();
        Integer[] messageSeconds = new Integer[5];
        messageSeconds[0] = 1;
        messageSeconds[1] = 6;
        messageSeconds[2] = 11;
        messageSeconds[3] = 21;
        messageSeconds[4] = 30;
        M = Arrays.asList(messageSeconds);
        Integer[] titleSeconds = new Integer[5];
        titleSeconds[0] = 11;
        titleSeconds[1] = 21;
        titleSeconds[2] = 28;
        titleSeconds[3] = 29;
        titleSeconds[4] = 30;
        XxXxxX = Arrays.asList(titleSeconds);
    }
}
