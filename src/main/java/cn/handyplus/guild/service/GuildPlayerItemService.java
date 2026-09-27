package cn.handyplus.guild.service;

import cn.handyplus.guild.enter.GuildPlayerItem;
import cn.handyplus.guild.lib.CollUtil;
import cn.handyplus.guild.lib.Db;
import cn.handyplus.guild.lib.DbFunction;
import cn.handyplus.guild.lib.HandyRunnable;
import cn.handyplus.guild.service.GuildShopLogService;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.SerializedLambda;
import java.util.List;
import java.util.UUID;

public class GuildPlayerItemService {
    public List<GuildPlayerItem> findAll() {
        return Db.use(GuildPlayerItem.class).execution().list();
    }

    private GuildPlayerItemService() {
    }

    public static GuildPlayerItemService getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public int addAll(List<GuildPlayerItem> guildPlayerItems) {
        if (CollUtil.isEmpty(guildPlayerItems)) {
            return 0;
        }
        Db.use(GuildPlayerItem.class).execution().insertBatch(guildPlayerItems);
        return guildPlayerItems.size();
    }

    public List<GuildPlayerItem> findByPlayerUuid(UUID playerUuid) {
        Db<GuildPlayerItem> db = Db.use(GuildPlayerItem.class);
        db.where().eq(GuildPlayerItem::getPlayerUuid, playerUuid);
        return db.execution().list();
    }

    public void delByPlayerUuid(UUID playerUuid) {
        Db<GuildPlayerItem> db = Db.use(GuildPlayerItem.class);
        db.where().eq(GuildPlayerItem::getPlayerUuid, playerUuid);
        db.execution().delete();
    }

    private static class SingletonHolder {
        private static final GuildPlayerItemService INSTANCE = new GuildPlayerItemService();
    }
}
