package cn.handyplus.guild.service;

import cn.handyplus.guild.constants.GuildActivityType;
import cn.handyplus.guild.enter.GuildPvpLog;
import cn.handyplus.guild.lib.Db;
import cn.handyplus.guild.util.ConfigUtil;
import java.util.Date;
import java.util.List;

public class GuildPvpLogService {
    public static GuildPvpLogService getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public int add(GuildPvpLog guildPvpLog) {
        return Db.use(GuildPvpLog.class).execution().insert(guildPvpLog);
    }

    public int findByGuildInfoIdAndStartTime(Integer guildId, Date startTime) {
        boolean failNotCount = ConfigUtil.ACTIVITY_CONFIG.getBoolean("matePvp.failNotCount", false);
        Db<GuildPvpLog> db = Db.use(GuildPvpLog.class);
        db.where().eq(GuildPvpLog::getGuildInfoId, guildId).eq(GuildPvpLog::getType, GuildActivityType.MATCH.getType()).ge(GuildPvpLog::getStartTime, startTime).eq(failNotCount, GuildPvpLog::getResult, true);
        return db.execution().count();
    }

    private GuildPvpLogService() {
    }

    public static String pPPppP(String value) {
        int length = value.length();
        int index = length - 1;
        char[] chars = new char[length];
        int xorKeyOne = 81;
        int remaining = index;
        int xorKeyTwo = 74;
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

    public List<GuildPvpLog> findAll() {
        return Db.use(GuildPvpLog.class).execution().list();
    }

    private static class SingletonHolder {
        private static final GuildPvpLogService INSTANCE = new GuildPvpLogService();
    }
}
