package cn.handyplus.guild.service;

import cn.handyplus.guild.enter.GuildPvp;
import cn.handyplus.guild.lib.Db;
import cn.handyplus.guild.service.GuildNoticeService;
import java.lang.invoke.SerializedLambda;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class GuildPvpService {
    public static GuildPvpService getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void update(GuildPvp guildPvp) {
        Db<GuildPvp> db = Db.use(GuildPvp.class);
        db.update().set(GuildPvp::getIsStart, guildPvp.getIsStart()).set(GuildPvp::getStartTime, guildPvp.getStartTime());
        db.execution().updateById(guildPvp.getId());
    }

    private GuildPvpService() {
    }

    private Integer findMaxSeason() {
        Db<GuildPvp> db = Db.use(GuildPvp.class);
        db.where().orderByDesc(GuildPvp::getSeason).limit(1, 1);
        Optional<GuildPvp> optional = db.execution().selectOne();
        return optional.isPresent() ? optional.get().getSeason() : 0;
    }

    public Optional<GuildPvp> findByGuildInfoId(Integer guildId) {
        Db<GuildPvp> db = Db.use(GuildPvp.class);
        db.where().eq(GuildPvp::getGuildInfoId, guildId).eq(GuildPvp::getSeason, this.findSeason());
        return db.execution().selectOne();
    }

    public Integer findSeason() {
        Db<GuildPvp> db = Db.use(GuildPvp.class);
        db.where().orderByDesc(GuildPvp::getSeason).eq(GuildPvp::getIsStart, false).limit(1, 1);
        Optional<GuildPvp> optional = db.execution().selectOne();
        if (optional.isPresent()) {
            return optional.get().getSeason();
        }
        return this.findMaxSeason() + 1;
    }

    public List<GuildPvp> findBySeason() {
        Db<GuildPvp> db = Db.use(GuildPvp.class);
        db.where().eq(GuildPvp::getSeason, this.findSeason()).eq(GuildPvp::getIsJoin, true).eq(GuildPvp::getIsStart, false);
        return db.execution().list();
    }

    public void add(GuildPvp guildPvp) {
        Db.use(GuildPvp.class).execution().insert(guildPvp);
    }

    public void updateJoinById(Integer id, Boolean isJoin) {
        Db<GuildPvp> db = Db.use(GuildPvp.class);
        db.update().set(GuildPvp::getIsJoin, isJoin);
        db.execution().updateById(id);
    }

    public List<GuildPvp> findAll() {
        return Db.use(GuildPvp.class).execution().list();
    }

    public static String pPPppP(String value) {
        int length = value.length();
        int index = length - 1;
        char[] chars = new char[length];
        int xorKeyOne = 37;
        int remaining = index;
        int xorKeyTwo = 92;
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

    public void batchUpdateStart(List<Integer> ids, Date startTime) {
        Db<GuildPvp> db = Db.use(GuildPvp.class);
        db.update().set(GuildPvp::getIsStart, true).set(GuildPvp::getStartTime, startTime);
        db.where().in(GuildPvp::getId, ids);
        db.execution().update();
    }

    public void batchAdd(List<GuildPvp> guildPvps) {
        Db.use(GuildPvp.class).execution().insertBatch(guildPvps);
    }

    private static class SingletonHolder {
        private static final GuildPvpService INSTANCE = new GuildPvpService();
    }
}
