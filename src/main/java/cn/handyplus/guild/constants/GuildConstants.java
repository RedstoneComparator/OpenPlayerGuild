package cn.handyplus.guild.constants;

import cn.handyplus.guild.enter.GuildPlayer;
import cn.handyplus.guild.enter.GuildTaskDemand;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class GuildConstants {
    public final static String PLY = "ply";
    public final static Map<UUID, Boolean> SPAWN_TP_WAIT_TIME;
    public final static String GUILD_NAME_VAR = "${guildName}";
    public final static String GUILD_LEAVE = "plg leave";
    public final static Map<Integer, List<GuildTaskDemand>> GUILD_TASK_DEMAND_MAP;
    public final static String GUILD_SPAWN = "plg spawn";
    public final static String SIGN_IN = "plg me signIn";
    public final static String NOTICE = "plg me notice";
    public final static String ACCEPT_INVITATION = "plg acceptInvitation ";
    public final static String GUILD_TP_SPAWN = "spawn";
    public final static String ADAPT = "adapt";
    public final static String PLG_ME_ACTIVITY = "plg me activity";
    public final static Map<UUID, Long> MATE_PVP_CLICK_WAIT_TIME;
    public final static String DENY_PVP = "plg denyPvp";
    public final static String MONEY = "money";
    public final static Map<UUID, Boolean> PLAYER_API_PVP_MAP;
    public final static Map<UUID, String> CREATE_GUILD_INFO_MAP;
    public final static String DENY = "plg deny ";
    public final static Map<String, Integer> GUILD_WORLD_MAP;
    public final static String ACCEPT_PVP = "plg acceptPvp";
    public final static Map<UUID, GuildPlayer> PLAYER_GUILD_MAP;
    public final static String POINT = "point";
    public final static Map<UUID, GuildPlayer> PLAYER_GUILD_OFFLINE_CACHE_MAP;
    public final static String BAR = "/";
    public final static Map<String, List<Integer>> INVITATION_PLAYER_MAP;
    public final static String ACTIVITY = "plg me activity ";
    public final static Map<UUID, Long> SPAWN_WAIT_TIME;
    public final static String OPEN = "plg open";
    public final static String DENY_INVITATION = "plg denyInvitation ";
    public final static String ACCEPT = "plg accept ";
    public final static String GUILD_CREATE_WORLD = "plg createWorld";
    public final static String VAULT = "vault";

    private GuildConstants() {
    }

    static {
        CREATE_GUILD_INFO_MAP = new ConcurrentHashMap<UUID, String>();
        SPAWN_WAIT_TIME = new ConcurrentHashMap<UUID, Long>();
        SPAWN_TP_WAIT_TIME = new ConcurrentHashMap<UUID, Boolean>();
        PLAYER_GUILD_MAP = new ConcurrentHashMap<UUID, GuildPlayer>();
        GUILD_WORLD_MAP = new ConcurrentHashMap<String, Integer>();
        GUILD_TASK_DEMAND_MAP = new ConcurrentHashMap<Integer, List<GuildTaskDemand>>();
        INVITATION_PLAYER_MAP = new ConcurrentHashMap<String, List<Integer>>();
        PLAYER_API_PVP_MAP = new ConcurrentHashMap<UUID, Boolean>();
        PLAYER_GUILD_OFFLINE_CACHE_MAP = new ConcurrentHashMap<UUID, GuildPlayer>();
        MATE_PVP_CLICK_WAIT_TIME = new ConcurrentHashMap<UUID, Long>();
    }
}
