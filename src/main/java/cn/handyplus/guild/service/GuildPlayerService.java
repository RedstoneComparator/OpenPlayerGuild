package cn.handyplus.guild.service;

import cn.handyplus.guild.constants.GuildRoleEnum;
import cn.handyplus.guild.enter.GuildInfo;
import cn.handyplus.guild.enter.GuildPlayer;
import cn.handyplus.guild.event.PlayerJoinGuildEvent;
import cn.handyplus.guild.lib.CollUtil;
import cn.handyplus.guild.lib.Db;
import cn.handyplus.guild.lib.MapUtil;
import cn.handyplus.guild.lib.Page;
import cn.handyplus.guild.service.GuildInfoService;
import cn.handyplus.guild.util.GuildCacheUtil;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GuildPlayerService {
    public void addGuildMoneyById(Integer id, int money) {
        Db<GuildPlayer> db = Db.use(GuildPlayer.class);
        db.update().add(GuildPlayer::getGuildMoney, GuildPlayer::getGuildMoney, money);
        db.execution().updateById(id);
    }

    public void editPlayerPvpStatus(Integer guildId, boolean pvpStatus) {
        Db<GuildPlayer> db = Db.use(GuildPlayer.class);
        db.update().set(GuildPlayer::getPvpStatus, pvpStatus);
        db.where().eq(GuildPlayer::getGuildInfoId, guildId);
        db.execution().update();
        GuildCacheUtil.refresh(guildId);
    }

    public void updateLastJoinTimeByPlayer(Player player) {
        Db<GuildPlayer> db = Db.use(GuildPlayer.class);
        db.update().set(GuildPlayer::getLastJoinTime, new Date()).set(GuildPlayer::getPlayerName, player.getName());
        db.where().eq(GuildPlayer::getPlayerUuid, player.getUniqueId());
        db.execution().update();
    }

    public Optional<GuildPlayer> findById(Integer id) {
        return Db.use(GuildPlayer.class).execution().selectById(id);
    }

    public boolean addMoneyByPlayerName(String playerName, int money) {
        Db<GuildPlayer> db = Db.use(GuildPlayer.class);
        db.update().add(GuildPlayer::getMoney, GuildPlayer::getMoney, money).add(GuildPlayer::getWeekMoney, GuildPlayer::getWeekMoney, money).add(GuildPlayer::getTotalMoney, GuildPlayer::getTotalMoney, money);
        db.where().eq(GuildPlayer::getPlayerName, playerName);
        if (db.execution().update() > 0) {
            return true;
        }
        return false;
    }

    private void applyGuildInfoOnNameResult(Optional playerOptional, GuildInfo guildInfo) {
        this.copyGuildInfoToPlayer((GuildPlayer)playerOptional.get(), guildInfo);
    }

    public List<GuildPlayer> findAll() {
        return Db.use(GuildPlayer.class).execution().list();
    }

    public void deleteById(Integer id) {
        Db.use(GuildPlayer.class).execution().deleteById(id);
    }

    public void add(GuildPlayer guildPlayerData) {
        GuildPlayer guildPlayer = guildPlayerData;
        GuildPlayer guildPlayer2 = guildPlayerData;
        GuildPlayer guildPlayer3 = guildPlayerData;
        GuildPlayer guildPlayer4 = guildPlayerData;
        guildPlayer4.setMoney(0);
        guildPlayer4.setWeekMoney(0);
        guildPlayer3.setTotalMoney(0);
        guildPlayer3.setGuildMoney(0);
        guildPlayer2.setOre(0);
        guildPlayer2.setTotalOre(0);
        guildPlayer2.setJoinTime(new Date());
        GuildPlayer guildPlayer5 = guildPlayerData;
        guildPlayerData.setLastJoinTime(new Date());
        guildPlayer.setKill(0);
        guildPlayer.setDie(0);
        if (Db.use(GuildPlayer.class).execution().insert(guildPlayerData) > 0) {
            PlayerJoinGuildEvent.callEvent(guildPlayerData.getPlayerName(), guildPlayerData.getPlayerUuid(), guildPlayerData.getGuildInfoId());
        }
    }

    public Optional<GuildPlayer> findByPlayerUuidAndGuildInfo(UUID playerUuid) {
        Optional<GuildPlayer> optional = this.findByPlayerUuid(playerUuid);
        if (!optional.isPresent()) {
            return optional;
        }
        GuildInfoService.getInstance().findById(optional.get().getGuildInfoId()).ifPresent(guildInfo -> this.applyGuildInfoOnUuidResult(optional, guildInfo));
        return optional;
    }

    public Map<Integer, List<GuildPlayer>> findMapByGuildInfoIds(List<Integer> guildInfoIds) {
        return this.findByGuildInfoIds(guildInfoIds).stream().collect(Collectors.groupingBy(GuildPlayer::getGuildInfoId));
    }

    public void setPickaxeTime(UUID playerUuid, Date pickaxeTime, Integer ore) {
        Db<GuildPlayer> db = Db.use(GuildPlayer.class);
        db.update().set(GuildPlayer::getPickaxeTime, pickaxeTime).set(GuildPlayer::getOre, ore).add(GuildPlayer::getTotalOre, GuildPlayer::getTotalOre, ore);
        db.where().eq(GuildPlayer::getPlayerUuid, playerUuid);
        db.execution().update();
    }

    public int clearWeekMoney() {
        Db<GuildPlayer> db = Db.use(GuildPlayer.class);
        db.update().set(GuildPlayer::getWeekMoney, 0);
        return db.execution().update();
    }

    public void updateOre() {
        Db<GuildPlayer> db = Db.use(GuildPlayer.class);
        db.update().set(GuildPlayer::getOre, 0).set(GuildPlayer::getTotalOre, 0);
        db.where().isNull(GuildPlayer::getOre);
        db.execution().update();
    }

    public List<GuildPlayer> findByPlayerUuidListAndGuildInfo(List<UUID> playerUuids) {
        List<GuildPlayer> guildPlayers = this.findByPlayerUuidList(playerUuids);
        this.refreshPlayerGuildInfos(guildPlayers);
        return guildPlayers;
    }

    public void editPlayerGuildName(Integer guildId, String guildName) {
        Db<GuildPlayer> db = Db.use(GuildPlayer.class);
        db.update().set(GuildPlayer::getGuildInfoName, guildName);
        db.where().eq(GuildPlayer::getGuildInfoId, guildId);
        db.execution().update();
    }

    public List<GuildPlayer> findRoleByGuildInfoIds(List<Integer> guildInfoIds) {
        Db<GuildPlayer> db = Db.use(GuildPlayer.class);
        db.where().in(GuildPlayer::getGuildInfoId, guildInfoIds).eq(GuildPlayer::getRoleId, GuildRoleEnum.ONE.getRoleId());
        return db.execution().list();
    }

    public List<GuildPlayer> findByPlayerUuidList(List<UUID> playerUuids) {
        Db<GuildPlayer> db = Db.use(GuildPlayer.class);
        db.where().in(GuildPlayer::getPlayerUuid, playerUuids);
        return db.execution().list();
    }

    public void addMoneyByIds(@NotNull List<Integer> ids, int money) {
        if (money == 0) {
            return;
        }
        Db<GuildPlayer> db = Db.use(GuildPlayer.class);
        db.update().add(GuildPlayer::getMoney, GuildPlayer::getMoney, money).add(GuildPlayer::getWeekMoney, GuildPlayer::getWeekMoney, money).add(GuildPlayer::getTotalMoney, GuildPlayer::getTotalMoney, money);
        db.where().in(GuildPlayer::getId, ids);
        db.execution().update();
    }

    public static GuildPlayerService getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public Optional<GuildPlayer> findByPlayerUuid(UUID playerUuid) {
        Db<GuildPlayer> db = Db.use(GuildPlayer.class);
        db.where().eq(GuildPlayer::getPlayerUuid, playerUuid);
        return db.execution().selectOne();
    }

    private void applyGuildInfoOnUuidResult(Optional playerOptional, GuildInfo guildInfo) {
        this.copyGuildInfoToPlayer((GuildPlayer)playerOptional.get(), guildInfo);
    }

    public List<GuildPlayer> findByGuildInfoIds(List<Integer> guildInfoIds) {
        Db<GuildPlayer> db = Db.use(GuildPlayer.class);
        db.where().in(GuildPlayer::getGuildInfoId, guildInfoIds);
        return db.execution().list();
    }

    private void copyGuildInfoToPlayer(GuildPlayer guildPlayerData, GuildInfo guildInfoData) {
        if (guildPlayerData == null || guildInfoData == null) {
            return;
        }
        GuildPlayer guildPlayer = guildPlayerData;
        GuildInfo guildInfo = guildInfoData;
        GuildPlayer guildPlayer2 = guildPlayerData;
        GuildInfo guildInfo2 = guildInfoData;
        GuildPlayer guildPlayer3 = guildPlayerData;
        GuildInfo guildInfo3 = guildInfoData;
        guildPlayerData.setLevel(guildInfoData.getLevel());
        guildPlayerData.setSeasonRank(guildInfo3.getSeasonRank());
        guildPlayer3.setLogoName(guildInfo3.getLogoName());
        guildPlayer3.setMemberCount(guildInfoData.getMemberCount());
        guildPlayerData.setMemberMaxCount(guildInfo2.getMemberMaxCount());
        guildPlayer2.setGuildInfoMoney(guildInfo2.getMoney());
        guildPlayer2.setProsperityDegree(guildInfoData.getProsperityDegree());
        guildPlayer.setMonthProsperityDegree(guildInfo.getMonthProsperityDegree());
        guildPlayer.setGuildInfoOre(guildInfo.getOre());
    }

    public void addMoneyById(Integer id, int money) {
        Db<GuildPlayer> db = Db.use(GuildPlayer.class);
        db.update().add(GuildPlayer::getMoney, GuildPlayer::getMoney, money).add(GuildPlayer::getWeekMoney, GuildPlayer::getWeekMoney, money).add(GuildPlayer::getTotalMoney, GuildPlayer::getTotalMoney, money);
        db.execution().updateById(id);
    }

    private GuildPlayerService() {
    }

    public void batchAddKillAndDie(@NotNull Map<String, List<UUID>> killDieMap) {
        if (MapUtil.isEmpty(killDieMap)) {
            return;
        }
        Iterator<Map.Entry<String, List<UUID>>> killDieEntryIterator = killDieMap.entrySet().iterator();
        while (killDieEntryIterator.hasNext()) {
            Map.Entry<String, List<UUID>> entry = killDieEntryIterator.next();
            List<UUID> playerUuids = entry.getValue();
            String[] keyParts = ((String)entry.getKey()).split(",");
            int killCount = Integer.parseInt(keyParts[0]);
            int dieCount = Integer.parseInt(keyParts[1]);
            if (killCount == 0 && dieCount == 0) {
                continue;
            }
            Db<GuildPlayer> db = Db.use(GuildPlayer.class);
            db.update().add(GuildPlayer::getKill, GuildPlayer::getKill, killCount).add(GuildPlayer::getDie, GuildPlayer::getDie, dieCount);
            db.where().in(GuildPlayer::getPlayerUuid, playerUuids);
            db.execution().update();
        }
    }

    public void updateLastQuitTimeByPlayerUuid(UUID playerUuid, Date quitTime) {
        Db<GuildPlayer> db = Db.use(GuildPlayer.class);
        db.update().set(GuildPlayer::getLastQuitTime, quitTime);
        db.where().eq(GuildPlayer::getPlayerUuid, playerUuid);
        db.execution().update();
    }

    public Page<GuildPlayer> page(Integer guildId, Integer pageNum, Integer pageSize) {
        Db<GuildPlayer> db = Db.use(GuildPlayer.class);
        db.where().eq(GuildPlayer::getGuildInfoId, guildId).limit(pageNum, pageSize).orderByDesc(GuildPlayer::getTotalMoney);
        return db.execution().page();
    }

    public boolean addMoneyByPlayerUuid(UUID playerUuid, int money) {
        Db<GuildPlayer> db = Db.use(GuildPlayer.class);
        db.update().add(GuildPlayer::getMoney, GuildPlayer::getMoney, money).add(GuildPlayer::getWeekMoney, GuildPlayer::getWeekMoney, money).add(GuildPlayer::getTotalMoney, GuildPlayer::getTotalMoney, money);
        db.where().eq(GuildPlayer::getPlayerUuid, playerUuid);
        if (db.execution().update() > 0) {
            return true;
        }
        return false;
    }

    public void updateRoleIdById(Integer id, int roleId) {
        Db<GuildPlayer> db = Db.use(GuildPlayer.class);
        db.update().set(GuildPlayer::getRoleId, roleId);
        db.execution().updateById(id);
    }

    public boolean subtractMoneyByPlayerUuid(UUID playerUuid, int money) {
        Db<GuildPlayer> db = Db.use(GuildPlayer.class);
        db.update().subtract(GuildPlayer::getMoney, GuildPlayer::getMoney, money);
        db.where().eq(GuildPlayer::getPlayerUuid, playerUuid);
        if (db.execution().update() > 0) {
            return true;
        }
        return false;
    }

    public void addKillAndDie(UUID playerUuid, int killCount, int dieCount) {
        Db<GuildPlayer> db = Db.use(GuildPlayer.class);
        db.update().add(GuildPlayer::getKill, GuildPlayer::getKill, killCount).add(GuildPlayer::getDie, GuildPlayer::getDie, dieCount);
        db.where().eq(GuildPlayer::getPlayerUuid, playerUuid);
        db.execution().update();
    }

    public Integer findCountByGuildId(Integer guildId) {
        Db<GuildPlayer> db = Db.use(GuildPlayer.class);
        db.where().eq(GuildPlayer::getGuildInfoId, guildId);
        return db.execution().count();
    }

    public boolean subtractMoneyByPlayerName(String playerName, int money) {
        Db<GuildPlayer> db = Db.use(GuildPlayer.class);
        db.update().subtract(GuildPlayer::getMoney, GuildPlayer::getMoney, money);
        db.where().eq(GuildPlayer::getPlayerName, playerName);
        if (db.execution().update() > 0) {
            return true;
        }
        return false;
    }

    public void addMoneyByPlayerUuids(@NotNull List<UUID> playerUuids, int money) {
        if (money == 0) {
            return;
        }
        Db<GuildPlayer> db = Db.use(GuildPlayer.class);
        db.update().add(GuildPlayer::getMoney, GuildPlayer::getMoney, money).add(GuildPlayer::getWeekMoney, GuildPlayer::getWeekMoney, money).add(GuildPlayer::getTotalMoney, GuildPlayer::getTotalMoney, money);
        db.where().in(GuildPlayer::getPlayerUuid, playerUuids);
        db.execution().update();
    }

    public void delByGuildId(Integer guildId) {
        Db<GuildPlayer> db = Db.use(GuildPlayer.class);
        db.where().eq(GuildPlayer::getGuildInfoId, guildId);
        db.execution().delete();
    }

    private void refreshPlayerGuildInfos(List<GuildPlayer> guildPlayers) {
        if (CollUtil.isEmpty(guildPlayers)) {
            return;
        }
        List<Integer> guildInfoIds = guildPlayers.stream().map(GuildPlayer::getGuildInfoId).distinct().collect(Collectors.toList());
        Map<Integer, GuildInfo> guildInfoMap = GuildInfoService.getInstance().findByIds(guildInfoIds).stream().collect(Collectors.toMap(GuildInfo::getId, guildInfo -> guildInfo));
        Iterator<GuildPlayer> guildPlayerIterator = guildPlayers.iterator();
        while (guildPlayerIterator.hasNext()) {
            GuildPlayer guildPlayer = guildPlayerIterator.next();
            this.copyGuildInfoToPlayer(guildPlayer, guildInfoMap.get(guildPlayer.getGuildInfoId()));
        }
    }

    public boolean setMoneyByPlayerUuid(UUID playerUuid, int money) {
        Db<GuildPlayer> db = Db.use(GuildPlayer.class);
        db.update().set(GuildPlayer::getMoney, money);
        db.where().eq(GuildPlayer::getPlayerUuid, playerUuid);
        if (db.execution().update() > 0) {
            return true;
        }
        return false;
    }

    public Optional<GuildPlayer> findOneTotalMoneyByGuildId(Integer guildId) {
        Db<GuildPlayer> db = Db.use(GuildPlayer.class);
        db.where().eq(GuildPlayer::getGuildInfoId, guildId).orderByDesc(GuildPlayer::getTotalMoney).limit(1, 1);
        return db.execution().selectOne();
    }

    public boolean setMoneyByPlayerName(String playerName, int money) {
        Db<GuildPlayer> db = Db.use(GuildPlayer.class);
        db.update().set(GuildPlayer::getMoney, money);
        db.where().eq(GuildPlayer::getPlayerName, playerName);
        if (db.execution().update() > 0) {
            return true;
        }
        return false;
    }

    public Optional<GuildPlayer> findByPlayerName(String playerName) {
        Db<GuildPlayer> db = Db.use(GuildPlayer.class);
        db.where().eq(GuildPlayer::getPlayerName, playerName);
        return db.execution().selectOne();
    }

    public Optional<GuildPlayer> findOneRoleByGuildId(Integer guildId) {
        Db<GuildPlayer> db = Db.use(GuildPlayer.class);
        db.where().eq(GuildPlayer::getGuildInfoId, guildId).eq(GuildPlayer::getRoleId, GuildRoleEnum.ONE.getRoleId());
        return db.execution().selectOne();
    }

    public List<GuildPlayer> findByIdAndRoleIds(Integer guildId, List<Integer> roleIds) {
        Db<GuildPlayer> db = Db.use(GuildPlayer.class);
        db.where().eq(GuildPlayer::getGuildInfoId, guildId).in(GuildPlayer::getRoleId, roleIds);
        return db.execution().list();
    }

    public List<GuildPlayer> findByGuildName(String guildName) {
        Db<GuildPlayer> db = Db.use(GuildPlayer.class);
        db.where().eq(GuildPlayer::getGuildInfoName, guildName);
        return db.execution().list();
    }

    public void addOnlyMoneyByPlayerUuid(UUID playerUuid, int money) {
        Db<GuildPlayer> db = Db.use(GuildPlayer.class);
        db.update().add(GuildPlayer::getMoney, GuildPlayer::getMoney, money);
        db.where().eq(GuildPlayer::getPlayerUuid, playerUuid);
        db.execution().update();
    }

    public List<GuildPlayer> findByGuildIdAndGuildInfo(Integer guildId) {
        Db<GuildPlayer> db = Db.use(GuildPlayer.class);
        db.where().eq(GuildPlayer::getGuildInfoId, guildId);
        List<GuildPlayer> guildPlayers = db.execution().list();
        this.refreshPlayerGuildInfos(guildPlayers);
        return guildPlayers;
    }

    public List<GuildPlayer> findByGuildId(Integer guildId) {
        Db<GuildPlayer> db = Db.use(GuildPlayer.class);
        db.where().eq(GuildPlayer::getGuildInfoId, guildId);
        return db.execution().list();
    }

    public void subtractMoneyById(Integer id, int money) {
        Db<GuildPlayer> db = Db.use(GuildPlayer.class);
        db.update().subtract(GuildPlayer::getMoney, GuildPlayer::getMoney, money);
        db.execution().updateById(id);
    }

    public Optional<GuildPlayer> findByPlayerNameAndGuildInfo(String playerName) {
        Optional<GuildPlayer> optional = this.findByPlayerName(playerName);
        if (!optional.isPresent()) {
            return optional;
        }
        GuildInfoService.getInstance().findById(optional.get().getGuildInfoId()).ifPresent(guildInfo -> this.applyGuildInfoOnNameResult(optional, guildInfo));
        return optional;
    }

    private static class SingletonHolder {
        private static final GuildPlayerService INSTANCE = new GuildPlayerService();
    }
}
