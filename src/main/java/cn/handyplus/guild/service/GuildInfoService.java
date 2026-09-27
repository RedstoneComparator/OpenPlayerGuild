package cn.handyplus.guild.service;

import cn.handyplus.guild.constants.GuildSortEnum;
import cn.handyplus.guild.core.score.ScorePvpReward;
import cn.handyplus.guild.enter.GuildInfo;
import cn.handyplus.guild.lib.BeanUtil;
import cn.handyplus.guild.lib.Compare;
import cn.handyplus.guild.lib.Db;
import cn.handyplus.guild.lib.Page;
import cn.handyplus.guild.lib.p;
import cn.handyplus.guild.service.GuildApplyService;
import cn.handyplus.guild.service.GuildBuffService;
import cn.handyplus.guild.service.GuildLogoService;
import cn.handyplus.guild.service.GuildNoticeReadService;
import cn.handyplus.guild.service.GuildNoticeService;
import cn.handyplus.guild.service.GuildPlayerPickaxeService;
import cn.handyplus.guild.service.GuildPlayerService;
import cn.handyplus.guild.service.GuildTaskDemandService;
import cn.handyplus.guild.service.GuildTaskService;
import java.lang.invoke.SerializedLambda;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;

public class GuildInfoService {
    public void addMoneyById(Integer id, Integer money) {
        Db<GuildInfo> db = Db.use(GuildInfo.class);
        db.update().add(GuildInfo::getMoney, GuildInfo::getMoney, money);
        db.execution().updateById(id);
    }

    public void batchSubtractProsperityDegree(@NotNull Map<Integer, List<Integer>> degreeMap, @NotNull List<Integer> zeroIds) {
        Iterator<Map.Entry<Integer, List<Integer>>> entryIterator = degreeMap.entrySet().iterator();
        while (entryIterator.hasNext()) {
            Map.Entry<Integer, List<Integer>> entry = entryIterator.next();
            Integer amount = entry.getKey();
            List<Integer> targetIds = entry.getValue();
            if (amount == null) continue;
            if (amount <= 0) {
                continue;
            }
            Db<GuildInfo> db = Db.use(GuildInfo.class);
            db.update().subtract(GuildInfo::getProsperityDegree, GuildInfo::getProsperityDegree, amount);
            db.where().in(GuildInfo::getId, (List<?>)targetIds);
            db.execution().update();
        }
        Db<GuildInfo> db = Db.use(GuildInfo.class);
        db.update().set(GuildInfo::getProsperityDegree, 0);
        db.where().in(GuildInfo::getId, zeroIds);
        db.execution().update();
    }

    public void subtractProsperityDegreeById(Integer id, Integer degree) {
        Db<GuildInfo> db = Db.use(GuildInfo.class);
        db.update().subtract(GuildInfo::getProsperityDegree, GuildInfo::getProsperityDegree, degree);
        db.execution().updateById(id);
    }

    public void setLuckPermsGroup(Integer id, String group) {
        Db<GuildInfo> db = Db.use(GuildInfo.class);
        db.update().set(GuildInfo::getLuckPermsGroup, group);
        db.execution().updateById(id);
    }

    public List<String> findNameAll() {
        return this.findAll().stream().map(GuildInfo::getGuildName).collect(Collectors.toList());
    }

    public void addMemberCount(Integer id, Integer count) {
        Db<GuildInfo> db = Db.use(GuildInfo.class);
        db.update().add(GuildInfo::getMemberCount, GuildInfo::getMemberCount, count);
        db.execution().updateById(id);
    }

    public void editguildDescription(Integer id, String description, Integer money) {
        Db<GuildInfo> db = Db.use(GuildInfo.class);
        db.update().set(GuildInfo::getDescription, description).subtract(GuildInfo::getMoney, GuildInfo::getMoney, money);
        db.execution().updateById(id);
    }

    public int updateSpawnLocation(Integer id, String spawnLocation) {
        Db<GuildInfo> db = Db.use(GuildInfo.class);
        db.update().set(GuildInfo::getSpawnLocation, spawnLocation);
        return db.execution().updateById(id);
    }

    public Optional<GuildInfo> findByWorldName(String worldName) {
        Db<GuildInfo> db = Db.use(GuildInfo.class);
        db.where().eq(GuildInfo::getWorldName, worldName).limit(1, 1);
        return db.execution().selectOne();
    }

    public static GuildInfoService getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void subtractOreById(Integer id, Integer ore) {
        Db<GuildInfo> db = Db.use(GuildInfo.class);
        db.update().subtract(GuildInfo::getOre, GuildInfo::getOre, ore);
        db.execution().updateById(id);
    }

    public int clearMonthProsperityDegree() {
        Db<GuildInfo> db = Db.use(GuildInfo.class);
        db.update().set(GuildInfo::getMonthProsperityDegree, 0);
        return db.execution().update();
    }

    public void addTaskRewardById(Integer id, int money, int prosperityDegree, int ore) {
        Db<GuildInfo> db = Db.use(GuildInfo.class);
        db.update().add(GuildInfo::getMoney, GuildInfo::getMoney, money).add(GuildInfo::getProsperityDegree, GuildInfo::getProsperityDegree, prosperityDegree).add(GuildInfo::getMonthProsperityDegree, GuildInfo::getMonthProsperityDegree, prosperityDegree).add(GuildInfo::getOre, GuildInfo::getOre, ore);
        db.execution().updateById(id);
    }

    public Page<GuildInfo> page(Integer pageNum, Integer pageSize, String sortName) {
        Db<GuildInfo> db;
        Db<GuildInfo> db2 = Db.use(GuildInfo.class);
        Compare<GuildInfo> compare = db2.where();
        compare.limit((Integer)pageNum, pageSize);
        GuildSortEnum guildSortEnum = GuildSortEnum.getEnum(sortName);
        switch (p.pPpppp[guildSortEnum.ordinal()]) {
            case 1: {
                compare.orderByDesc(GuildInfo::getLevel);
                db = db2;
                return db.execution().page();
            }
            case 2: {
                compare.orderByDesc(GuildInfo::getMoney);
                db = db2;
                return db.execution().page();
            }
            case 3: {
                compare.orderByDesc(GuildInfo::getCreateTime);
                db = db2;
                return db.execution().page();
            }
            case 4: {
                compare.orderByDesc(GuildInfo::getMemberCount);
                db = db2;
                return db.execution().page();
            }
            case 5: {
                compare.orderByDesc(GuildInfo::getMonthProsperityDegree);
                db = db2;
                return db.execution().page();
            }
        }
        compare.orderByDesc(GuildInfo::getProsperityDegree);
        db = db2;
        return db.execution().page();
    }

    public List<GuildInfo> findByIds(List<Integer> ids) {
        return Db.use(GuildInfo.class).execution().selectBatchIds(ids);
    }

    public void addProsperityDegreeById(Integer id, Integer degree) {
        Db<GuildInfo> db = Db.use(GuildInfo.class);
        db.update().add(GuildInfo::getProsperityDegree, GuildInfo::getProsperityDegree, degree).add(GuildInfo::getMonthProsperityDegree, GuildInfo::getMonthProsperityDegree, degree);
        db.execution().updateById(id);
    }

    public List<GuildInfo> findAll() {
        return Db.use(GuildInfo.class).execution().list();
    }

    private GuildInfoService() {
    }

    public void updateOre() {
        Db<GuildInfo> db = Db.use(GuildInfo.class);
        db.update().set(GuildInfo::getOre, 0);
        db.where().isNull(GuildInfo::getOre);
        db.execution().update();
    }

    public boolean addMoneyByName(String guildName, Integer money) {
        Db<GuildInfo> db = Db.use(GuildInfo.class);
        db.update().add(GuildInfo::getMoney, GuildInfo::getMoney, money);
        db.where().eq(GuildInfo::getGuildName, guildName);
        if (db.execution().update() > 0) {
            return true;
        }
        return false;
    }

    public void batchAddProsperityDegree(@NotNull Map<Integer, List<Integer>> degreeMap) {
        Iterator<Map.Entry<Integer, List<Integer>>> entryIterator = degreeMap.entrySet().iterator();
        while (entryIterator.hasNext()) {
            Map.Entry<Integer, List<Integer>> entry = entryIterator.next();
            Integer amount = entry.getKey();
            List<Integer> targetIds = entry.getValue();
            if (amount == null) continue;
            if (amount <= 0) {
                continue;
            }
            Db<GuildInfo> db = Db.use(GuildInfo.class);
            db.update().add(GuildInfo::getProsperityDegree, GuildInfo::getProsperityDegree, amount).add(GuildInfo::getMonthProsperityDegree, GuildInfo::getMonthProsperityDegree, amount);
            db.where().in(GuildInfo::getId, (List<?>)targetIds);
            db.execution().update();
        }
    }

    public void setProsperityDegreeById(Integer id, Integer degree) {
        Db<GuildInfo> db = Db.use(GuildInfo.class);
        db.update().set(GuildInfo::getProsperityDegree, degree);
        db.execution().updateById(id);
    }

    public void delByGuildId(Integer guildId) {
        Db.use(GuildInfo.class).execution().deleteById(guildId);
        GuildApplyService.getInstance().delByGuildId(guildId);
        GuildLogoService.getInstance().delByGuildId(guildId);
        GuildPlayerService.getInstance().delByGuildId(guildId);
        GuildTaskService.getInstance().delByGuildId(guildId);
        GuildTaskDemandService.getInstance().delByGuildId(guildId);
        GuildNoticeService.getInstance().delByGuildId(guildId);
        GuildNoticeReadService.getInstance().delByGuildId(guildId);
        GuildPlayerPickaxeService.getInstance().delByGuildId(guildId);
        GuildBuffService.getInstance().delByGuildId(guildId);
    }

    public void editJoinMode(Integer id, boolean joinMode, Integer money) {
        Db<GuildInfo> db = Db.use(GuildInfo.class);
        db.update().set(GuildInfo::getJoinMode, joinMode).subtract(GuildInfo::getMoney, GuildInfo::getMoney, money);
        db.execution().updateById(id);
    }

    public boolean subtractMoneyByName(String guildName, Integer money) {
        Db<GuildInfo> db = Db.use(GuildInfo.class);
        db.update().subtract(GuildInfo::getMoney, GuildInfo::getMoney, money);
        db.where().eq(GuildInfo::getGuildName, guildName);
        if (db.execution().update() > 0) {
            return true;
        }
        return false;
    }

    public void setMemberCount(Integer id, Integer memberCount, Integer memberMaxCount) {
        Db<GuildInfo> db = Db.use(GuildInfo.class);
        db.update().set(GuildInfo::getMemberCount, memberCount).set(GuildInfo::getMemberMaxCount, memberMaxCount);
        db.execution().updateById(id);
    }

    public Optional<GuildInfo> findById(Integer id) {
        return Db.use(GuildInfo.class).execution().selectById(id);
    }

    public void updateLogoByLogoId(Integer logoId, String logoName) {
        Db<GuildInfo> db = Db.use(GuildInfo.class);
        db.update().set(GuildInfo::getLogoName, logoName);
        db.where().eq(GuildInfo::getLogoId, logoId);
        db.execution().update();
    }

    public void updateWorldName(Integer id, String worldName) {
        Db<GuildInfo> db = Db.use(GuildInfo.class);
        db.update().set(GuildInfo::getWorldName, worldName);
        db.execution().updateById(id);
    }

    public boolean subtractProsperityDegreeByName(String guildName, Integer degree) {
        Db<GuildInfo> db = Db.use(GuildInfo.class);
        db.update().subtract(GuildInfo::getProsperityDegree, GuildInfo::getProsperityDegree, degree);
        db.where().eq(GuildInfo::getGuildName, guildName);
        if (db.execution().update() > 0) {
            return true;
        }
        return false;
    }

    public void setLevel(Integer id, Integer level, Integer memberMaxCount) {
        Db<GuildInfo> db = Db.use(GuildInfo.class);
        db.update().set(level != null, GuildInfo::getLevel, level).set(memberMaxCount != null, GuildInfo::getMemberMaxCount, memberMaxCount);
        db.execution().updateById(id);
    }

    public List<GuildInfo> list() {
        return Db.use(GuildInfo.class).execution().list();
    }

    public int add(GuildInfo guildInfo) {
        return Db.use(GuildInfo.class).execution().insert(guildInfo);
    }

    public boolean addOreByName(String guildName, Integer ore) {
        Db<GuildInfo> db = Db.use(GuildInfo.class);
        db.update().add(GuildInfo::getOre, GuildInfo::getOre, ore);
        db.where().eq(GuildInfo::getGuildName, guildName);
        if (db.execution().update() > 0) {
            return true;
        }
        return false;
    }

    public void subtractMemberCount(Integer id, Integer count) {
        Db<GuildInfo> db = Db.use(GuildInfo.class);
        db.update().subtract(GuildInfo::getMemberCount, GuildInfo::getMemberCount, count);
        db.execution().updateById(id);
    }

    public void updateTitleIdById(Integer id, Integer titleId) {
        Db<GuildInfo> db = Db.use(GuildInfo.class);
        db.update().set(GuildInfo::getTitleId, titleId);
        db.execution().updateById(id);
    }

    public boolean setProsperityDegreeByName(String guildName, Integer degree) {
        Db<GuildInfo> db = Db.use(GuildInfo.class);
        db.update().set(GuildInfo::getProsperityDegree, degree);
        db.where().eq(GuildInfo::getGuildName, guildName);
        if (db.execution().update() > 0) {
            return true;
        }
        return false;
    }

    public void addSacredStoneLevelById(Integer id, Integer level) {
        Db<GuildInfo> db = Db.use(GuildInfo.class);
        db.update().add(GuildInfo::getSacredStoneLevel, GuildInfo::getSacredStoneLevel, level);
        db.execution().updateById(id);
    }

    public boolean setOreByName(String guildName, Integer ore) {
        Db<GuildInfo> db = Db.use(GuildInfo.class);
        db.update().set(GuildInfo::getOre, ore);
        db.where().eq(GuildInfo::getGuildName, guildName);
        if (db.execution().update() > 0) {
            return true;
        }
        return false;
    }

    public void setMoneyById(Integer id, Integer money) {
        Db<GuildInfo> db = Db.use(GuildInfo.class);
        db.update().set(GuildInfo::getMoney, money);
        db.execution().updateById(id);
    }

    public Map<String, Integer> findAllWorldName() {
        Db<GuildInfo> db = Db.use(GuildInfo.class);
        db.where().isNotNull(GuildInfo::getWorldName);
        return db.execution().list().stream().collect(Collectors.toMap(GuildInfo::getWorldName, GuildInfo::getId));
    }

    public void editPlayerPvpStatus(Integer id, boolean pvpStatus, Integer money) {
        Db<GuildInfo> db = Db.use(GuildInfo.class);
        db.update().set(GuildInfo::getPvpStatus, pvpStatus).subtract(GuildInfo::getMoney, GuildInfo::getMoney, money);
        db.execution().updateById(id);
        GuildPlayerService.getInstance().editPlayerPvpStatus(id, pvpStatus);
    }

    public void editGuildName(Integer id, String guildName, Integer money) {
        Db<GuildInfo> db = Db.use(GuildInfo.class);
        db.update().set(GuildInfo::getGuildName, guildName).subtract(GuildInfo::getMoney, GuildInfo::getMoney, money);
        db.execution().updateById(id);
        GuildPlayerService.getInstance().editPlayerGuildName(id, guildName);
    }

    public Integer findCount() {
        return Db.use(GuildInfo.class).execution().count();
    }

    public boolean subtractOreByName(String guildName, Integer ore) {
        Db<GuildInfo> db = Db.use(GuildInfo.class);
        db.update().subtract(GuildInfo::getOre, GuildInfo::getOre, ore);
        db.where().eq(GuildInfo::getGuildName, guildName);
        if (db.execution().update() > 0) {
            return true;
        }
        return false;
    }

    public Optional<GuildInfo> findByGuildName(String guildName) {
        Db<GuildInfo> db = Db.use(GuildInfo.class);
        db.where().eq(GuildInfo::getGuildName, guildName);
        return db.execution().selectOne();
    }

    public void setSeasonRank(Integer id, Integer seasonRank) {
        Db<GuildInfo> db = Db.use(GuildInfo.class);
        db.update().set(GuildInfo::getSeasonRank, seasonRank);
        db.execution().updateById(id);
    }

    public void setMonthProsperityDegreeById(Integer id, Integer degree) {
        Db<GuildInfo> db = Db.use(GuildInfo.class);
        db.update().set(GuildInfo::getMonthProsperityDegree, degree);
        db.execution().updateById(id);
    }

    public void updateLogoById(Integer id, Integer logoId, String logoName) {
        Db<GuildInfo> db = Db.use(GuildInfo.class);
        db.update().set(GuildInfo::getLogoId, logoId).set(GuildInfo::getLogoName, logoName);
        db.execution().updateById(id);
    }

    public void subtractMoneyById(Integer id, Integer money) {
        Db<GuildInfo> db = Db.use(GuildInfo.class);
        db.update().subtract(GuildInfo::getMoney, GuildInfo::getMoney, money);
        db.execution().updateById(id);
    }

    public void batchSubtractMoney(@NotNull Map<Integer, List<Integer>> moneyMap, @NotNull List<Integer> zeroIds) {
        Iterator<Map.Entry<Integer, List<Integer>>> entryIterator = moneyMap.entrySet().iterator();
        while (entryIterator.hasNext()) {
            Map.Entry<Integer, List<Integer>> entry = entryIterator.next();
            Integer amount = entry.getKey();
            List<Integer> targetIds = entry.getValue();
            if (amount == null) continue;
            if (amount <= 0) {
                continue;
            }
            Db<GuildInfo> db = Db.use(GuildInfo.class);
            db.update().subtract(GuildInfo::getMoney, GuildInfo::getMoney, amount);
            db.where().in(GuildInfo::getId, (List<?>)targetIds);
            db.execution().update();
        }
        Db<GuildInfo> db = Db.use(GuildInfo.class);
        db.update().set(GuildInfo::getMoney, 0);
        db.where().in(GuildInfo::getId, zeroIds);
        db.execution().update();
    }

    public void addOreById(Integer id, Integer ore) {
        Db<GuildInfo> db = Db.use(GuildInfo.class);
        db.update().add(GuildInfo::getOre, GuildInfo::getOre, ore);
        db.execution().updateById(id);
    }

    public Map<Integer, GuildInfo> findMapByIds(@NotNull List<Integer> ids) {
        return this.findByIds(ids).stream().collect(Collectors.toMap(GuildInfo::getId, guildInfo -> guildInfo));
    }

    public boolean setMoneyByName(String guildName, Integer money) {
        Db<GuildInfo> db = Db.use(GuildInfo.class);
        db.update().set(GuildInfo::getMoney, money);
        db.where().eq(GuildInfo::getGuildName, guildName);
        if (db.execution().update() > 0) {
            return true;
        }
        return false;
    }

    public boolean addProsperityDegreeByName(String guildName, Integer degree) {
        Db<GuildInfo> db = Db.use(GuildInfo.class);
        db.update().add(GuildInfo::getProsperityDegree, GuildInfo::getProsperityDegree, degree).add(GuildInfo::getMonthProsperityDegree, GuildInfo::getMonthProsperityDegree, degree);
        db.where().eq(GuildInfo::getGuildName, guildName);
        if (db.execution().update() > 0) {
            return true;
        }
        return false;
    }

    private static class SingletonHolder {
        private static final GuildInfoService INSTANCE = new GuildInfoService();
    }
}
