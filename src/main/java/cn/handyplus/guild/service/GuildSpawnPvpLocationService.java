package cn.handyplus.guild.service;

import cn.handyplus.guild.core.common.GuildPvpTpUtil;
import cn.handyplus.guild.enter.GuildSpawnPvpLocation;
import cn.handyplus.guild.lib.Db;
import cn.handyplus.guild.lib.Page;
import java.lang.invoke.SerializedLambda;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class GuildSpawnPvpLocationService {
    public void add(GuildSpawnPvpLocation guildSpawnPvpLocation) {
        Db.use(GuildSpawnPvpLocation.class).execution().insert(guildSpawnPvpLocation);
    }

    public Map<Integer, List<GuildSpawnPvpLocation>> listByLocationIds(List<Integer> locationIds) {
        Db<GuildSpawnPvpLocation> db = Db.use(GuildSpawnPvpLocation.class);
        db.where().in(GuildSpawnPvpLocation::getGuildPvpLocationId, (List<?>)locationIds).orderByAsc(GuildSpawnPvpLocation::getId);
        List<GuildSpawnPvpLocation> spawnPvpLocations = db.execution().list();
        HashMap<Integer, List<GuildSpawnPvpLocation>> resultMap = new HashMap<Integer, List<GuildSpawnPvpLocation>>();
        Iterator<GuildSpawnPvpLocation> locationIterator = spawnPvpLocations.iterator();
        while (locationIterator.hasNext()) {
            GuildSpawnPvpLocation guildSpawnPvpLocation = locationIterator.next();
            resultMap.computeIfAbsent(guildSpawnPvpLocation.getGuildPvpLocationId(), locationId -> new ArrayList<GuildSpawnPvpLocation>()).add(guildSpawnPvpLocation);
        }
        return resultMap;
    }

    public static String pPPppP(String value) {
        int length = value.length();
        int index = length - 1;
        char[] chars = new char[length];
        int xorKeyOne = 29;
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

    public void delById(Integer id) {
        Db.use(GuildSpawnPvpLocation.class).execution().deleteById(id);
    }

    private GuildSpawnPvpLocationService() {
    }

    public Page<GuildSpawnPvpLocation> page(Integer locationId, Integer pageNum, Integer pageSize) {
        Db<GuildSpawnPvpLocation> db = Db.use(GuildSpawnPvpLocation.class);
        db.where().eq(GuildSpawnPvpLocation::getGuildPvpLocationId, locationId).orderByAsc(GuildSpawnPvpLocation::getId).limit(pageNum, pageSize);
        return db.execution().page();
    }

    public List<GuildSpawnPvpLocation> findAll() {
        return Db.use(GuildSpawnPvpLocation.class).execution().list();
    }

    public List<GuildSpawnPvpLocation> list(Integer locationId) {
        Db<GuildSpawnPvpLocation> db = Db.use(GuildSpawnPvpLocation.class);
        db.where().eq(GuildSpawnPvpLocation::getGuildPvpLocationId, locationId).orderByAsc(GuildSpawnPvpLocation::getId);
        return db.execution().list();
    }

    public static GuildSpawnPvpLocationService getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void delByGuildPvpLocationId(Integer guildPvpLocationId) {
        Db<GuildSpawnPvpLocation> db = Db.use(GuildSpawnPvpLocation.class);
        db.where().eq(GuildSpawnPvpLocation::getGuildPvpLocationId, guildPvpLocationId);
        db.execution().delete();
    }

    private static class SingletonHolder {
        private static final GuildSpawnPvpLocationService INSTANCE = new GuildSpawnPvpLocationService();
    }
}
