package cn.handyplus.guild.service;

import cn.handyplus.guild.core.season.SeasonPvpPlayer;
import cn.handyplus.guild.enter.GuildShopLog;
import cn.handyplus.guild.lib.Db;
import cn.handyplus.guild.service.GuildPvpPlayerLogService;
import java.lang.invoke.SerializedLambda;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class GuildShopLogService {

    public Integer findCountByPlayerUuid(UUID playerUuid, String type, Integer guildShopId, Date startTime, Date endTime) {
        Db<GuildShopLog> db = Db.use(GuildShopLog.class);
        db.where().eq(GuildShopLog::getType, type).eq(GuildShopLog::getGuildShopId, guildShopId).eq(GuildShopLog::getPlayerUuid, playerUuid).ge(GuildShopLog::getBuyTime, startTime).le(GuildShopLog::getBuyTime, endTime);
        return db.execution().count();
    }

    public static String pPPppP(String value) {
        int length = value.length();
        int index = length - 1;
        char[] chars = new char[length];
        int xorKeyOne = 52;
        int remaining = index;
        int xorKeyTwo = 101;
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

    public void add(GuildShopLog guildShopLog) {
        Db.use(GuildShopLog.class).execution().insert(guildShopLog);
    }

    public static GuildShopLogService getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private GuildShopLogService() {
    }

    public Optional<GuildShopLog> findByPlayerUuid(UUID playerUuid, String type, Integer guildShopId) {
        Db<GuildShopLog> db = Db.use(GuildShopLog.class);
        db.where().eq(GuildShopLog::getType, type).eq(GuildShopLog::getGuildShopId, guildShopId).eq(GuildShopLog::getPlayerUuid, playerUuid);
        return db.execution().selectOne();
    }

    public void delByGuildShopId(Integer guildShopId) {
        Db<GuildShopLog> db = Db.use(GuildShopLog.class);
        db.where().eq(GuildShopLog::getGuildShopId, guildShopId);
        db.execution().delete();
    }

    public void addNumber(Integer id, Integer number) {
        Db<GuildShopLog> db = Db.use(GuildShopLog.class);
        db.update().add(GuildShopLog::getNumber, GuildShopLog::getNumber, number).set(GuildShopLog::getBuyTime, new Date());
        db.execution().updateById(id);
    }

    public List<GuildShopLog> findAll() {
        return Db.use(GuildShopLog.class).execution().list();
    }

    private static class SingletonHolder {
        private static final GuildShopLogService INSTANCE = new GuildShopLogService();
    }
}
