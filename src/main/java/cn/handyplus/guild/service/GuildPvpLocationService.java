package cn.handyplus.guild.service;

import cn.handyplus.guild.enter.GuildPvpLocation;
import cn.handyplus.guild.lib.CollUtil;
import cn.handyplus.guild.lib.Db;
import cn.handyplus.guild.lib.Page;
import cn.handyplus.guild.lib.StrUtil;
import cn.handyplus.guild.service.GuildSpawnPvpLocationService;
import java.lang.invoke.SerializedLambda;
import java.util.List;
import java.util.Optional;

public class GuildPvpLocationService {
    public List<GuildPvpLocation> listByNotIds(String type, List<Integer> excludeIds) {
        Db<GuildPvpLocation> db = Db.use(GuildPvpLocation.class);
        db.where().eq(GuildPvpLocation::getType, type).notIn(CollUtil.isNotEmpty(excludeIds), GuildPvpLocation::getId, excludeIds);
        return db.execution().list();
    }

    public boolean existType(String type) {
        Db<GuildPvpLocation> db = Db.use(GuildPvpLocation.class);
        db.where().eq(GuildPvpLocation::getType, type).limit(1, 1);
        return db.execution().selectOne().isPresent();
    }

    public List<GuildPvpLocation> findAll() {
        return Db.use(GuildPvpLocation.class).execution().list();
    }

    public Optional<GuildPvpLocation> findById(Integer id) {
        return Db.use(GuildPvpLocation.class).execution().selectById(id);
    }

    public Optional<GuildPvpLocation> findById(String name) {
        Db<GuildPvpLocation> db = Db.use(GuildPvpLocation.class);
        db.where().eq(GuildPvpLocation::getName, name);
        return db.execution().selectOne();
    }

    public void delById(Integer id) {
        Db.use(GuildPvpLocation.class).execution().deleteById(id);
        GuildSpawnPvpLocationService.getInstance().delByGuildPvpLocationId(id);
    }

    public List<GuildPvpLocation> findByType(String type) {
        Db<GuildPvpLocation> db = Db.use(GuildPvpLocation.class);
        db.where().eq(GuildPvpLocation::getType, type);
        return db.execution().list();
    }

    public Optional<GuildPvpLocation> findByNameAndType(String name, String type) {
        Db<GuildPvpLocation> db = Db.use(GuildPvpLocation.class);
        db.where().eq(GuildPvpLocation::getName, name).eq(GuildPvpLocation::getType, type);
        return db.execution().selectOne();
    }

    private GuildPvpLocationService() {
    }

    public int add(GuildPvpLocation guildPvpLocation) {
        return Db.use(GuildPvpLocation.class).execution().insert(guildPvpLocation);
    }

    public static GuildPvpLocationService getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public Page<GuildPvpLocation> page(String type, Integer pageNum, Integer pageSize) {
        Db<GuildPvpLocation> db = Db.use(GuildPvpLocation.class);
        db.where().eq(StrUtil.isNotEmpty(type), GuildPvpLocation::getType, type).orderByDesc(GuildPvpLocation::getId).limit(pageNum, pageSize);
        return db.execution().page();
    }

    private static class SingletonHolder {
        private static final GuildPvpLocationService INSTANCE = new GuildPvpLocationService();
    }
}
