package cn.handyplus.guild.service;

import cn.handyplus.guild.enter.GuildNoticeRead;
import cn.handyplus.guild.lib.Db;
import cn.handyplus.guild.lib.ItemStackUtil;
import cn.handyplus.guild.util.GuildCoolingUtil;
import java.lang.invoke.SerializedLambda;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class GuildNoticeReadService {
    public Map<Integer, Integer> findMapByGuildNoticeIds(List<Integer> guildNoticeIds) {
        Db<GuildNoticeRead> db = Db.use(GuildNoticeRead.class);
        db.where().in(GuildNoticeRead::getGuildNoticeId, guildNoticeIds);
        return db.execution().list().stream().collect(Collectors.groupingBy(GuildNoticeRead::getGuildNoticeId, Collectors.collectingAndThen(Collectors.toList(), List::size)));
    }

    public static GuildNoticeReadService getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void add(GuildNoticeRead guildNoticeRead) {
        Db.use(GuildNoticeRead.class).execution().insert(guildNoticeRead);
    }

    public List<GuildNoticeRead> findByGuildIdAndPlayerUuid(Integer guildId, UUID playerUuid) {
        Db<GuildNoticeRead> db = Db.use(GuildNoticeRead.class);
        db.where().eq(GuildNoticeRead::getGuildInfoId, guildId).eq(GuildNoticeRead::getReadPlayerUuid, playerUuid);
        return db.execution().list();
    }

    public List<Integer> findGuildNoticeId(Integer guildId, UUID playerUuid) {
        return this.findByGuildIdAndPlayerUuid(guildId, playerUuid).stream().map(GuildNoticeRead::getGuildNoticeId).collect(Collectors.toList());
    }

    private GuildNoticeReadService() {
    }

    public List<GuildNoticeRead> findAll() {
        return Db.use(GuildNoticeRead.class).execution().list();
    }

    public void delByGuildNoticeId(Integer guildNoticeId) {
        Db<GuildNoticeRead> db = Db.use(GuildNoticeRead.class);
        db.where().eq(GuildNoticeRead::getGuildNoticeId, guildNoticeId);
        db.execution().delete();
    }

    public void delByGuildId(Integer guildId) {
        Db<GuildNoticeRead> db = Db.use(GuildNoticeRead.class);
        db.where().eq(GuildNoticeRead::getGuildInfoId, guildId);
        db.execution().delete();
    }

    private static class SingletonHolder {
        private static final GuildNoticeReadService INSTANCE = new GuildNoticeReadService();
    }
}
