package cn.handyplus.guild.service;

import cn.handyplus.guild.enter.GuildPlayerCooling;
import cn.handyplus.guild.lib.Db;
import cn.handyplus.guild.lib.DbFunction;
import cn.handyplus.guild.lib.HandyInventoryUtil;
import cn.handyplus.guild.service.GuildPvpLogService;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.SerializedLambda;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class GuildPlayerCoolingService {
    private GuildPlayerCoolingService() {
    }

    public List<GuildPlayerCooling> findAll() {
        return Db.use(GuildPlayerCooling.class).execution().list();
    }

    public Optional<GuildPlayerCooling> findByPlayerUuid(UUID playerUuid) {
        Db<GuildPlayerCooling> db = Db.use(GuildPlayerCooling.class);
        db.where().eq(GuildPlayerCooling::getPlayerUuid, playerUuid);
        return db.execution().selectOne();
    }

    public static GuildPlayerCoolingService getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void delByPlayerUuid(UUID playerUuid) {
        Db<GuildPlayerCooling> db = Db.use(GuildPlayerCooling.class);
        db.where().eq(GuildPlayerCooling::getPlayerUuid, playerUuid);
        db.execution().delete();
    }

    public void add(GuildPlayerCooling guildPlayerCooling) {
        Db.use(GuildPlayerCooling.class).execution().insert(guildPlayerCooling);
    }

    private static class SingletonHolder {
        private static final GuildPlayerCoolingService INSTANCE = new GuildPlayerCoolingService();
    }
}
