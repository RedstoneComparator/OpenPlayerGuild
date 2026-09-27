package cn.handyplus.guild.service;

import cn.handyplus.guild.enter.GuildBuff;
import cn.handyplus.guild.lib.CollUtil;
import cn.handyplus.guild.lib.Db;
import cn.handyplus.guild.lib.InputField;
import cn.handyplus.guild.lib.MapUtil;
import cn.handyplus.guild.lib.Page;
import java.lang.invoke.SerializedLambda;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GuildBuffService {
    public void delByGuildId(Integer guildInfoId) {
        Db<GuildBuff> db = Db.use(GuildBuff.class);
        db.where().eq(GuildBuff::getGuildInfoId, guildInfoId);
        db.execution().delete();
    }

    public void add(GuildBuff buff) {
        Db.use(GuildBuff.class).execution().insert(buff);
    }

    private GuildBuffService() {
    }

    public List<GuildBuff> findAll() {
        return Db.use(GuildBuff.class).execution().list();
    }

    public Map<String, GuildBuff> findByGuildId(Integer guildInfoId) {
        Db<GuildBuff> db = Db.use(GuildBuff.class);
        db.where().eq(GuildBuff::getGuildInfoId, guildInfoId);
        List<GuildBuff> buffs = db.execution().list();
        if (CollUtil.isEmpty(buffs)) {
            return MapUtil.of();
        }
        return buffs.stream().collect(Collectors.toMap(GuildBuff::getBuffType, buff -> buff));
    }

    public static GuildBuffService getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void addLevelById(Integer id, Integer addLevel) {
        Db<GuildBuff> db = Db.use(GuildBuff.class);
        db.update().add(GuildBuff::getLevel, GuildBuff::getLevel, addLevel);
        db.execution().updateById(id);
    }

    private static class SingletonHolder {
        private static final GuildBuffService INSTANCE = new GuildBuffService();
    }
}
