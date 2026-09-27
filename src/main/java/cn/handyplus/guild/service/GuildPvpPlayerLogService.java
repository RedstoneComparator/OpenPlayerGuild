package cn.handyplus.guild.service;

import cn.handyplus.guild.enter.GuildPvpPlayerLog;
import cn.handyplus.guild.lib.Db;
import cn.handyplus.guild.lib.LegacyUtil;
import cn.handyplus.guild.util.ConfigUtil;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;

public class GuildPvpPlayerLogService {
    public List<GuildPvpPlayerLog> findAll() {
        return Db.use(GuildPvpPlayerLog.class).execution().list();
    }

    public int findByPlayerUuidAndStartTime(@NotNull UUID playerUuid, @NotNull Date startTime) {
        boolean failNotCount = ConfigUtil.ACTIVITY_CONFIG.getBoolean("matePvp.failNotCount", false);
        Db<GuildPvpPlayerLog> db = Db.use(GuildPvpPlayerLog.class);
        db.where().eq(GuildPvpPlayerLog::getPlayerUuid, playerUuid).ge(GuildPvpPlayerLog::getStartTime, startTime).eq(failNotCount, GuildPvpPlayerLog::getResult, true);
        return db.execution().count();
    }

    public Map<UUID, Integer> findCountMapByPlayerUuids(@NotNull List<UUID> playerUuids, @NotNull Date startTime) {
        boolean failNotCount = ConfigUtil.ACTIVITY_CONFIG.getBoolean(LegacyUtil.J("\u000e)\u0017-3>\u0013f\u0005)\n$-'\u0017\u000b\f=\r<"), false);
        Db<GuildPvpPlayerLog> db = Db.use(GuildPvpPlayerLog.class);
        db.where().in(GuildPvpPlayerLog::getPlayerUuid, (List<?>)playerUuids).ge(GuildPvpPlayerLog::getStartTime, startTime).eq(failNotCount, GuildPvpPlayerLog::getResult, true);
        List<GuildPvpPlayerLog> guildPvpPlayerLogs = db.execution().list();
        HashMap<UUID, Integer> countMap = new HashMap<UUID, Integer>();
        Iterator<GuildPvpPlayerLog> logIterator = guildPvpPlayerLogs.iterator();
        while (logIterator.hasNext()) {
            GuildPvpPlayerLog guildPvpPlayerLog = logIterator.next();
            countMap.merge(guildPvpPlayerLog.getPlayerUuid(), 1, Integer::sum);
        }
        return countMap;
    }

    public void addBatch(@NotNull List<GuildPvpPlayerLog> guildPvpPlayerLogs) {
        Db.use(GuildPvpPlayerLog.class).execution().insertBatch(guildPvpPlayerLogs);
    }

    private GuildPvpPlayerLogService() {
    }

    public static GuildPvpPlayerLogService getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public static String pPPppP(String value) {
        int length = value.length();
        int index = length - 1;
        char[] chars = new char[length];
        int xorKeyOne = 4;
        int remaining = index;
        int xorKeyTwo = 88;
        while (remaining >= 0) {
            int posOne = index--;
            chars[posOne] = (char)(value.charAt(posOne) ^ xorKeyTwo);
            if (index < 0) break;
            int posTwo = index--;
            chars[posTwo] = (char)(value.charAt(posTwo) ^ xorKeyOne);
            remaining = index;
        }
        return new String(chars);
    }

    private static class SingletonHolder {
        private static final GuildPvpPlayerLogService INSTANCE = new GuildPvpPlayerLogService();
    }
}
