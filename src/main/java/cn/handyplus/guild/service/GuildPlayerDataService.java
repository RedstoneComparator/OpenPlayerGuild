package cn.handyplus.guild.service;

import cn.handyplus.guild.core.stronghold.StrongholdPvpView;
import cn.handyplus.guild.enter.GuildPlayerData;
import cn.handyplus.guild.lib.Db;
import cn.handyplus.guild.lib.DbFunction;
import cn.handyplus.guild.lib.RgbTextUtil;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.SerializedLambda;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class GuildPlayerDataService {
    public Optional<GuildPlayerData> findByPlayerUuid(UUID playerUuid) {
        Db<GuildPlayerData> db = Db.use(GuildPlayerData.class);
        db.where().eq(GuildPlayerData::getPlayerUuid, playerUuid);
        return db.execution().selectOne();
    }

    public void add(GuildPlayerData guildPlayerData) {
        Db.use(GuildPlayerData.class).execution().insert(guildPlayerData);
    }

    public void deleteByPlayerUuId(UUID playerUuid) {
        Db<GuildPlayerData> db = Db.use(GuildPlayerData.class);
        db.where().eq(GuildPlayerData::getPlayerUuid, playerUuid);
        db.execution().delete();
    }

    public void addBatch(List<GuildPlayerData> guildPlayerDataList) {
        Db.use(GuildPlayerData.class).execution().insertBatch(guildPlayerDataList);
    }

    public static GuildPlayerDataService getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private GuildPlayerDataService() {
    }

    private static class SingletonHolder {
        private static final GuildPlayerDataService INSTANCE = new GuildPlayerDataService();
    }
}
