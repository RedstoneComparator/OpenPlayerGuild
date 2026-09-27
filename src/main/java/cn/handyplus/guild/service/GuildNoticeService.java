package cn.handyplus.guild.service;

import cn.handyplus.guild.enter.GuildNotice;
import cn.handyplus.guild.lib.CollUtil;
import cn.handyplus.guild.lib.Db;
import cn.handyplus.guild.lib.Page;
import cn.handyplus.guild.service.GuildNoticeReadService;
import java.util.List;
import java.util.Optional;

public class GuildNoticeService {
    public List<GuildNotice> findAll() {
        return Db.use(GuildNotice.class).execution().list();
    }

    public void delByGuildId(Integer guildId) {
        Db<GuildNotice> db = Db.use(GuildNotice.class);
        db.where().eq(GuildNotice::getGuildInfoId, guildId);
        db.execution().delete();
    }

    public Optional<GuildNotice> findById(Integer id) {
        return Db.use(GuildNotice.class).execution().selectById(id);
    }

    public void delById(Integer id) {
        Db.use(GuildNotice.class).execution().deleteById(id);
        GuildNoticeReadService.getInstance().delByGuildNoticeId(id);
    }

    public List<GuildNotice> list(Integer guildId, List<Integer> excludeIds) {
        Db<GuildNotice> db = Db.use(GuildNotice.class);
        db.where().eq(GuildNotice::getGuildInfoId, guildId).notIn(CollUtil.isNotEmpty(excludeIds), GuildNotice::getId, excludeIds).orderByDesc(GuildNotice::getCreateTime);
        return db.execution().list();
    }

    public int add(GuildNotice guildNotice) {
        return Db.use(GuildNotice.class).execution().insert(guildNotice);
    }

    public Page<GuildNotice> page(Integer guildId, Integer pageNum, Integer pageSize, List<Integer> excludeIds, String type) {
        Db<GuildNotice> db = Db.use(GuildNotice.class);
        db.where().eq(GuildNotice::getGuildInfoId, guildId).notIn(CollUtil.isNotEmpty(excludeIds) && "noRead".equals(type), GuildNotice::getId, excludeIds).limit(pageNum, pageSize).orderByDesc(GuildNotice::getCreateTime);
        return db.execution().page();
    }

    public static GuildNoticeService getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private GuildNoticeService() {
    }

    public Page<GuildNotice> page(Integer pageNum, Integer pageSize) {
        Db<GuildNotice> db = Db.use(GuildNotice.class);
        db.where().limit(pageNum, pageSize).orderByDesc(GuildNotice::getCreateTime);
        return db.execution().page();
    }

    public static String pPPppP(String value) {
        int length = value.length();
        int index = length - 1;
        char[] chars = new char[length];
        int xorKeyOne = 110;
        int remaining = index;
        int xorKeyTwo = 18;
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
        private static final GuildNoticeService INSTANCE = new GuildNoticeService();
    }
}
