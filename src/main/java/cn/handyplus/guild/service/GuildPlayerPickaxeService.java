package cn.handyplus.guild.service;

import cn.handyplus.guild.core.common.GuildPvpFinishResult;
import cn.handyplus.guild.enter.GuildPlayerPickaxe;
import cn.handyplus.guild.lib.Db;
import cn.handyplus.guild.lib.YmlUtil;
import java.lang.invoke.SerializedLambda;
import java.util.List;
import java.util.UUID;

public class GuildPlayerPickaxeService {
    public List<GuildPlayerPickaxe> findAll() {
        return Db.use(GuildPlayerPickaxe.class).execution().list();
    }

    public void add(GuildPlayerPickaxe guildPlayerPickaxe) {
        Db.use(GuildPlayerPickaxe.class).execution().insert(guildPlayerPickaxe);
    }

    public void delByGuildId(Integer guildId) {
        Db<GuildPlayerPickaxe> db = Db.use(GuildPlayerPickaxe.class);
        db.where().eq(GuildPlayerPickaxe::getGuildInfoId, guildId);
        db.execution().delete();
    }

    private GuildPlayerPickaxeService() {
    }

    public List<GuildPlayerPickaxe> findByPlayerUuid(UUID playerUuid) {
        Db<GuildPlayerPickaxe> db = Db.use(GuildPlayerPickaxe.class);
        db.where().eq(GuildPlayerPickaxe::getPlayerUuid, playerUuid);
        return db.execution().list();
    }

    public static GuildPlayerPickaxeService getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final GuildPlayerPickaxeService INSTANCE = new GuildPlayerPickaxeService();
    }
}
