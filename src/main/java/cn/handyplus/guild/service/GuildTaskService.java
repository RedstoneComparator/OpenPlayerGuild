package cn.handyplus.guild.service;

import cn.handyplus.guild.enter.GuildTask;
import cn.handyplus.guild.lib.DateUtil;
import cn.handyplus.guild.lib.Db;
import cn.handyplus.guild.lib.LegacyUtil;
import cn.handyplus.guild.lib.Page;
import cn.handyplus.guild.service.GuildPlayerSignInService;
import java.lang.invoke.SerializedLambda;
import java.util.List;
import java.util.Optional;

public class GuildTaskService {

    public Optional<GuildTask> findRewardTaskById(Integer id) {
        Db<GuildTask> db = Db.use(GuildTask.class);
        db.where().eq(GuildTask::getId, id).eq(GuildTask::getStatus, false).eq(GuildTask::getDemandSuccess, true);
        return db.execution().selectOne();
    }

    public Integer findCount(Integer guildId) {
        Db<GuildTask> db = Db.use(GuildTask.class);
        db.where().eq(GuildTask::getGuildInfoId, guildId).ge(GuildTask::getStartDate, DateUtil.getMonday()).le(GuildTask::getEndDate, DateUtil.getSunday());
        return db.execution().count();
    }

    public int delete() {
        return Db.use(GuildTask.class).execution().delete();
    }

    public List<GuildTask> list(Integer guildId) {
        Db<GuildTask> db = Db.use(GuildTask.class);
        db.where().eq(GuildTask::getGuildInfoId, guildId).ge(GuildTask::getStartDate, DateUtil.getMonday()).le(GuildTask::getEndDate, DateUtil.getSunday()).orderByDesc(GuildTask::getId);
        return db.execution().list();
    }

    public List<GuildTask> findSeasonTask() {
        Db<GuildTask> db = Db.use(GuildTask.class);
        db.where().eq(GuildTask::getStatus, false).eq(GuildTask::getDemandSuccess, true).ge(GuildTask::getStartDate, DateUtil.getMonday()).le(GuildTask::getEndDate, DateUtil.getSunday());
        return db.execution().list();
    }

    public static String pPPppP(String value) {
        int length = value.length();
        int index = length - 1;
        char[] chars = new char[length];
        int xorKeyOne = 48;
        int remaining = index;
        int xorKeyTwo = 26;
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

    public List<GuildTask> findRewardTask() {
        Db<GuildTask> db = Db.use(GuildTask.class);
        db.where().eq(GuildTask::getStatus, false).eq(GuildTask::getDemandSuccess, true);
        return db.execution().list();
    }

    public Page<GuildTask> page(Integer guildId, Integer pageNum, Integer pageSize) {
        Db<GuildTask> db = Db.use(GuildTask.class);
        db.where().eq(GuildTask::getGuildInfoId, guildId).ge(GuildTask::getStartDate, DateUtil.getMonday()).le(GuildTask::getEndDate, DateUtil.getSunday()).limit(pageNum, pageSize).orderByDesc(GuildTask::getId);
        return db.execution().page();
    }

    public void updateDemandSuccessByIds(List<Integer> ids, boolean demandSuccess) {
        Db<GuildTask> db = Db.use(GuildTask.class);
        db.update().set(GuildTask::getDemandSuccess, demandSuccess);
        db.where().in(GuildTask::getId, ids);
        db.execution().update();
    }

    public void updateDemandSuccess(Integer id, boolean demandSuccess) {
        Db<GuildTask> db = Db.use(GuildTask.class);
        db.update().set(GuildTask::getDemandSuccess, demandSuccess);
        db.execution().updateById(id);
    }

    private GuildTaskService() {
    }

    public int add(GuildTask guildTask) {
        return Db.use(GuildTask.class).execution().insert(guildTask);
    }

    public void updateStatusById(List<Integer> ids, boolean status) {
        Db<GuildTask> db = Db.use(GuildTask.class);
        db.update().set(GuildTask::getStatus, status);
        db.where().in(GuildTask::getId, ids);
        db.execution().update();
    }

    public void delByGuildId(Integer guildId) {
        Db<GuildTask> db = Db.use(GuildTask.class);
        db.where().eq(GuildTask::getGuildInfoId, guildId);
        db.execution().delete();
    }

    public Optional<GuildTask> findFirstUndone(Integer guildId) {
        Db<GuildTask> db = Db.use(GuildTask.class);
        db.where().eq(GuildTask::getGuildInfoId, guildId).eq(GuildTask::getStatus, false).eq(GuildTask::getDemandSuccess, false).ge(GuildTask::getStartDate, DateUtil.getMonday()).le(GuildTask::getEndDate, DateUtil.getSunday()).orderByAsc(GuildTask::getId).limit(1, 1);
        return db.execution().selectOne();
    }

    public List<GuildTask> findAll() {
        return Db.use(GuildTask.class).execution().list();
    }

    public static GuildTaskService getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public List<GuildTask> findRewardTaskByIds(List<Integer> ids) {
        Db<GuildTask> db = Db.use(GuildTask.class);
        db.where().in(GuildTask::getId, ids).eq(GuildTask::getStatus, false).eq(GuildTask::getDemandSuccess, true);
        return db.execution().list();
    }

    private static class SingletonHolder {
        private static final GuildTaskService INSTANCE = new GuildTaskService();
    }
}
