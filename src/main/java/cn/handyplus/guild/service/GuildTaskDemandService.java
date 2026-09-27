package cn.handyplus.guild.service;

import cn.handyplus.guild.enter.GuildTaskDemand;
import cn.handyplus.guild.lib.CollUtil;
import cn.handyplus.guild.lib.DateUtil;
import cn.handyplus.guild.lib.Db;
import cn.handyplus.guild.lib.HandyInventoryUtil;
import cn.handyplus.guild.lib.MapUtil;
import cn.handyplus.guild.util.NoticeUtil;
import java.lang.invoke.SerializedLambda;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class GuildTaskDemandService {
    private GuildTaskDemandService() {
    }

    public void addCompletionAmountBatch(Map<Integer, List<Integer>> completionMap) {
        if (MapUtil.isEmpty(completionMap)) {
            return;
        }
        Iterator<Map.Entry<Integer, List<Integer>>> completionEntryIterator = completionMap.entrySet().iterator();
        while (completionEntryIterator.hasNext()) {
            Map.Entry<Integer, List<Integer>> entry = completionEntryIterator.next();
            Integer amount = entry.getKey();
            List<Integer> demandIds = entry.getValue();
            if (amount == null) continue;
            if (amount == 0) {
                continue;
            }
            Db<GuildTaskDemand> db = Db.use(GuildTaskDemand.class);
            db.update().add(GuildTaskDemand::getCompletionAmount, GuildTaskDemand::getCompletionAmount, amount);
            db.where().in(GuildTaskDemand::getId, (List<?>)demandIds);
            db.execution().update();
        }
    }

    public void addCompletionAmount(Integer id, Integer amount) {
        Db<GuildTaskDemand> db = Db.use(GuildTaskDemand.class);
        db.update().add(GuildTaskDemand::getCompletionAmount, GuildTaskDemand::getCompletionAmount, amount);
        db.execution().updateById(id);
    }

    public void delete() {
        Db.use(GuildTaskDemand.class).execution().delete();
    }

    public static GuildTaskDemandService getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public List<GuildTaskDemand> findByGuildTaskId(Integer guildTaskId) {
        Db<GuildTaskDemand> db = Db.use(GuildTaskDemand.class);
        db.where().eq(GuildTaskDemand::getGuildTaskId, guildTaskId);
        return db.execution().list();
    }

    public void addBatch(List<GuildTaskDemand> guildTaskDemands) {
        if (CollUtil.isEmpty(guildTaskDemands)) {
            return;
        }
        Db.use(GuildTaskDemand.class).execution().insertBatch(guildTaskDemands);
    }

    public List<GuildTaskDemand> findAll() {
        return Db.use(GuildTaskDemand.class).execution().list();
    }

    public List<GuildTaskDemand> findByGuildId(Integer guildId) {
        Db<GuildTaskDemand> db = Db.use(GuildTaskDemand.class);
        db.where().eq(GuildTaskDemand::getGuildInfoId, guildId).lt(GuildTaskDemand::getCompletionAmount, GuildTaskDemand::getAmount).ge(GuildTaskDemand::getStartDate, DateUtil.getMonday()).le(GuildTaskDemand::getEndDate, DateUtil.getSunday());
        return db.execution().list();
    }

    public List<GuildTaskDemand> findByGuildIdAndContent(Integer guildId, String itemStack, String type) {
        Db<GuildTaskDemand> db = Db.use(GuildTaskDemand.class);
        db.where().eq(GuildTaskDemand::getGuildInfoId, guildId).eq(GuildTaskDemand::getItemStack, itemStack).eq(GuildTaskDemand::getType, type).lt(GuildTaskDemand::getCompletionAmount, GuildTaskDemand::getAmount).ge(GuildTaskDemand::getStartDate, DateUtil.getMonday()).le(GuildTaskDemand::getEndDate, DateUtil.getSunday());
        return db.execution().list();
    }

    public Boolean findDoneCountByGuildTaskId(Integer guildTaskId) {
        Db<GuildTaskDemand> db = Db.use(GuildTaskDemand.class);
        db.where().eq(GuildTaskDemand::getGuildTaskId, guildTaskId).lt(GuildTaskDemand::getCompletionAmount, GuildTaskDemand::getAmount);
        return db.execution().count() > 0;
    }

    public List<GuildTaskDemand> findByGuildTaskIds(List<Integer> guildTaskIds) {
        Db<GuildTaskDemand> db = Db.use(GuildTaskDemand.class);
        db.where().in(GuildTaskDemand::getGuildTaskId, guildTaskIds);
        return db.execution().list();
    }

    public static String pPPppP(String value) {
        int length = value.length();
        int index = length - 1;
        char[] chars = new char[length];
        int xorKeyOne = 52;
        int remaining = index;
        int xorKeyTwo = 54;
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

    public void delByGuildId(Integer guildId) {
        Db<GuildTaskDemand> db = Db.use(GuildTaskDemand.class);
        db.where().eq(GuildTaskDemand::getGuildInfoId, guildId);
        db.execution().delete();
    }

    public void add(GuildTaskDemand guildTaskDemand) {
        Db.use(GuildTaskDemand.class).execution().insert(guildTaskDemand);
    }

    private static class SingletonHolder {
        private static final GuildTaskDemandService INSTANCE = new GuildTaskDemandService();
    }
}
