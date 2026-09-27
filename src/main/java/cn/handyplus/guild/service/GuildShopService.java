package cn.handyplus.guild.service;

import cn.handyplus.guild.enter.GuildShop;
import cn.handyplus.guild.lib.Db;
import cn.handyplus.guild.lib.LegacyUtil;
import cn.handyplus.guild.lib.Page;
import cn.handyplus.guild.service.GuildEquipmentService;
import cn.handyplus.guild.service.GuildShopLogService;
import java.lang.invoke.SerializedLambda;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class GuildShopService {
    public void delByPlayerUuid(UUID playerUuid) {
        Db<GuildShop> db = Db.use(GuildShop.class);
        db.where().eq(GuildShop::getPlayerUuid, playerUuid);
        db.execution().delete();
    }

    private GuildShopService() {
    }

    public void subtractNumberById(Integer id, int number) {
        Db<GuildShop> db = Db.use(GuildShop.class);
        db.update().subtract(GuildShop::getNumber, GuildShop::getNumber, number);
        db.execution().updateById(id);
    }

    public Page<GuildShop> page(Integer pageNum, Integer pageSize, List<Integer> guildIds) {
        Db<GuildShop> db = Db.use(GuildShop.class);
        db.where().in(GuildShop::getGuildInfoId, guildIds).limit(pageNum, pageSize).orderByDesc(GuildShop::getId);
        return db.execution().page();
    }

    public List<GuildShop> list(Integer guildId) {
        Db<GuildShop> db = Db.use(GuildShop.class);
        db.where().eq(GuildShop::getGuildInfoId, guildId).orderByDesc(GuildShop::getId);
        return db.execution().list();
    }

    public static GuildShopService getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public int add(GuildShop guildShop) {
        return Db.use(GuildShop.class).execution().insert(guildShop);
    }

    public void delById(Integer id) {
        Db.use(GuildShop.class).execution().deleteById(id);
        GuildShopLogService.getInstance().delByGuildShopId(id);
    }

    public List<GuildShop> findAll() {
        return Db.use(GuildShop.class).execution().list();
    }

    public Optional<GuildShop> findById(Integer id) {
        return Db.use(GuildShop.class).execution().selectById(id);
    }

    private static class SingletonHolder {
        private static final GuildShopService INSTANCE = new GuildShopService();
    }
}
