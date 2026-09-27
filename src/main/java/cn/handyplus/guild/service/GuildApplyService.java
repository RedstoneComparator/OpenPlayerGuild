package cn.handyplus.guild.service;

import cn.handyplus.guild.constants.ApplyTypeEnum;
import cn.handyplus.guild.enter.GuildApply;
import cn.handyplus.guild.lib.Db;
import cn.handyplus.guild.lib.ItemMetaUtil;
import cn.handyplus.guild.lib.Page;
import java.lang.invoke.SerializedLambda;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class GuildApplyService {
    public Optional<GuildApply> findByPlayerUuid(UUID playerUuid, Integer applyResult) {
        Db<GuildApply> db = Db.use(GuildApply.class);
        db.where().eq(playerUuid != null, GuildApply::getPlayerUuid, playerUuid).eq(applyResult != null, GuildApply::getApplyResult, applyResult);
        return db.execution().selectOne();
    }

    public static GuildApplyService getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void updateApplyResultByPlayerUuid(UUID playerUuid, Integer applyResult) {
        Db<GuildApply> db = Db.use(GuildApply.class);
        db.update().set(GuildApply::getApplyResult, applyResult);
        db.where().eq(GuildApply::getApplyResult, ApplyTypeEnum.PENDING.getId()).eq(GuildApply::getPlayerUuid, playerUuid);
        db.execution().update();
    }

    public Page<GuildApply> page(Integer guildInfoId, Integer pageNum, Integer pageSize, Integer applyResult) {
        Db<GuildApply> db = Db.use(GuildApply.class);
        db.where().eq(GuildApply::getGuildInfoId, guildInfoId).eq(GuildApply::getApplyResult, applyResult).limit(pageNum, pageSize).orderByDesc(GuildApply::getApplyTime);
        return db.execution().page();
    }

    public void updateById(GuildApply apply) {
        Db<GuildApply> db = Db.use(GuildApply.class);
        db.update().set(GuildApply::getApplyResult, apply.getApplyResult()).set(GuildApply::getApplyPlayerName, apply.getApplyPlayerName()).set(GuildApply::getApplyPlayerUuid, apply.getApplyPlayerUuid());
        db.execution().updateById(apply.getId());
    }

    public void delByGuildId(Integer guildInfoId) {
        Db<GuildApply> db = Db.use(GuildApply.class);
        db.where().eq(GuildApply::getGuildInfoId, guildInfoId);
        db.execution().delete();
    }

    public List<GuildApply> findByGuildId(Integer guildInfoId, Integer applyResult) {
        Db<GuildApply> db = Db.use(GuildApply.class);
        db.where().eq(GuildApply::getGuildInfoId, guildInfoId).eq(applyResult != null, GuildApply::getApplyResult, applyResult);
        return db.execution().list();
    }

    public int add(GuildApply apply) {
        return Db.use(GuildApply.class).execution().insert(apply);
    }

    private GuildApplyService() {
    }

    public List<GuildApply> findAll() {
        return Db.use(GuildApply.class).execution().list();
    }

    public Optional<GuildApply> findById(Integer id) {
        return Db.use(GuildApply.class).execution().selectById(id);
    }

    private static class SingletonHolder {
        private static final GuildApplyService INSTANCE = new GuildApplyService();
    }
}
