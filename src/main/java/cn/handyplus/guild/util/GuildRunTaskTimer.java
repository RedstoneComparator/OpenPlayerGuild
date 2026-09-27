package cn.handyplus.guild.util;

import cn.handyplus.guild.PlayerGuild;
import cn.handyplus.guild.command.admin.RewardCommand;
import cn.handyplus.guild.constants.GuildConstants;
import cn.handyplus.guild.core.match.MatchPvpManager;
import cn.handyplus.guild.core.score.ScorePvpManager;
import cn.handyplus.guild.core.season.SeasonPvpManager;
import cn.handyplus.guild.core.stronghold.StrongholdPvpManager;
import cn.handyplus.guild.enter.GuildInfo;
import cn.handyplus.guild.enter.GuildPlayer;
import cn.handyplus.guild.enter.GuildSpawnPvpLocation;
import cn.handyplus.guild.enter.GuildTask;
import cn.handyplus.guild.event.GuildClearMonthProsperityDegreeEvent;
import cn.handyplus.guild.hook.PlayerChatUtil;
import cn.handyplus.guild.hook.PlayerTaskUtil;
import cn.handyplus.guild.lib.BaseConstants;
import cn.handyplus.guild.lib.CollUtil;
import cn.handyplus.guild.lib.DateUtil;
import cn.handyplus.guild.lib.HandyRunnable;
import cn.handyplus.guild.lib.HandySchedulerUtil;
import cn.handyplus.guild.lib.LockUtil;
import cn.handyplus.guild.lib.MapUtil;
import cn.handyplus.guild.lib.MessageUtil;
import cn.handyplus.guild.lib.SignService;
import cn.handyplus.guild.lib.StrUtil;
import cn.handyplus.guild.lib.VersionCheckEnum;
import cn.handyplus.guild.service.GuildInfoService;
import cn.handyplus.guild.service.GuildPlayerService;
import cn.handyplus.guild.service.GuildSpawnPvpLocationService;
import cn.handyplus.guild.service.GuildTaskService;
import cn.handyplus.guild.util.ConfigUtil;
import cn.handyplus.guild.util.GuildCacheUtil;
import cn.handyplus.guild.util.GuildUtil;
import cn.handyplus.guild.util.UpUtil;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Semaphore;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GuildRunTaskTimer {
    private final static Semaphore weeklyMoneyLock;
    private final static Semaphore e;
    private final static Semaphore seasonStartLock;
    private final static Semaphore matchInitLock;
    private final static Semaphore F;
    private final static Semaphore taskStartLock;

    private static void cacheSacredStoneProsperity() {
        HandySchedulerUtil.runTaskTimerAsynchronously(() -> {
            List<GuildInfo> guildInfos = GuildInfoService.getInstance().list();
            if (CollUtil.isEmpty(guildInfos)) {
                return;
            }
            List<UUID> onlinePlayerUuids = new ArrayList<UUID>();
            for (Player player : Bukkit.getOnlinePlayers()) {
                onlinePlayerUuids.add(player.getUniqueId());
            }
            Map<Integer, List<GuildPlayer>> guildPlayersByGuildId = MapUtil.of();
            if (CollUtil.isNotEmpty(onlinePlayerUuids)) {
                List<GuildPlayer> onlineGuildPlayers = GuildPlayerService.getInstance().findByPlayerUuidList(onlinePlayerUuids);
                guildPlayersByGuildId = onlineGuildPlayers.stream().collect(Collectors.groupingBy(GuildPlayer::getGuildInfoId));
            }
            Map<Integer, List<Integer>> degreeBatches = new HashMap();
            for (GuildInfo guildInfo : guildInfos) {
                List<GuildPlayer> guildPlayers = guildPlayersByGuildId.get(guildInfo.getId());
                if (CollUtil.isEmpty(guildPlayers)) continue;
                Integer sacredStoneLevel = guildInfo.getSacredStoneLevel();
                int prosperityDegree = (int)(ConfigUtil.SACRED_STONE_CONFIG.getDouble(sacredStoneLevel + ".value", 1.0) * (double)guildPlayers.size());
                if (prosperityDegree <= 0) continue;
                degreeBatches.computeIfAbsent(prosperityDegree, degree -> new ArrayList()).add(guildInfo.getId());
            }
            GuildInfoService.getInstance().batchAddProsperityDegree((Map<Integer, List<Integer>>)degreeBatches);
        }, 1L, ConfigUtil.SACRED_STONE_CONFIG.getLong("time") * 20L);
    }

    private static int nullSafeInt(@Nullable Integer value) {
        if (value == null) {
            return 0;
        }
        return value;
    }

    private static void cleanTaskDemandCache() {
        HandySchedulerUtil.runTaskTimerAsynchronously(() -> {
            Set guildIds = GuildConstants.PLAYER_GUILD_MAP.values().stream().map(GuildPlayer::getGuildInfoId).filter(Objects::nonNull).collect(Collectors.toCollection(HashSet::new));
            GuildConstants.GUILD_TASK_DEMAND_MAP.entrySet().removeIf(demandEntry -> {
                if (!guildIds.contains(demandEntry.getKey())) {
                    return true;
                }
                return false;
            });
        }, 1L, 12000L);
    }

    public static void addPluginChunkTicket() {
        Iterator<GuildSpawnPvpLocation> locationIterator;
        if (BaseConstants.VERSION_ID < VersionCheckEnum.V_1_15.getVersionId()) {
            return;
        }
        if (HandySchedulerUtil.isFolia()) {
            return;
        }
        if (!Bukkit.isPrimaryThread()) {
            HandySchedulerUtil.runTask(GuildRunTaskTimer::addPluginChunkTicket);
            return;
        }
        List<GuildSpawnPvpLocation> guildSpawnPvpLocations = GuildSpawnPvpLocationService.getInstance().findAll();
        if (CollUtil.isEmpty(guildSpawnPvpLocations)) {
            return;
        }
        Iterator<GuildSpawnPvpLocation> locationIterator2 = locationIterator = guildSpawnPvpLocations.iterator();
        while (locationIterator2.hasNext()) {
            GuildSpawnPvpLocation guildSpawnPvpLocation = locationIterator.next();
            if (Bukkit.getWorld((String)guildSpawnPvpLocation.getWorld()) == null) {
                locationIterator2 = locationIterator;
                continue;
            }
            GuildUtil.getLocation(guildSpawnPvpLocation).getChunk().addPluginChunkTicket((Plugin)PlayerGuild.INSTANCE);
            locationIterator2 = locationIterator;
        }
    }

    private static void addGuildToBatch(int totalCost, @NotNull Integer guildId, int money, @NotNull Map<Integer, List<Integer>> affordableMap, @NotNull List<Integer> unaffordableList) {
        if (money >= totalCost) {
            affordableMap.computeIfAbsent(totalCost, costKey -> new ArrayList()).add(guildId);
            return;
        }
        unaffordableList.add(guildId);
    }

    public static void initTask() {
        HandySchedulerUtil.runTaskTimerAsynchronously(new HandyRunnable(){

            @Override
            public void run() {

                if (!LockUtil.tryPass("initTask")) {
                    return;
                }
                try {
                    UpUtil.up();
                    GuildRunTaskTimer.cachePlayerGuild();
                    PlayerChatUtil.regChannel();
                    GuildRunTaskTimer.initPvpManagers();
                    GuildRunTaskTimer.addPluginChunkTicket();
                    GuildCacheUtil.initGuildWorldCache();
                    this.cancel();
                }
                finally {
                    LockUtil.done("initTask");
                }
            }
        }, 60L, 20L);
    }

    public static void cachePlayerGuild() {
        Iterator playerIterator;
        ArrayList<UUID> onlinePlayerUuids = new ArrayList<UUID>();
        Iterator playerIterator2 = playerIterator = Bukkit.getOnlinePlayers().iterator();
        while (playerIterator2.hasNext()) {
            Player player = (Player)playerIterator.next();
            playerIterator2 = playerIterator;
            onlinePlayerUuids.add(player.getUniqueId());
        }
        if (CollUtil.isEmpty(onlinePlayerUuids)) {
            return;
        }
        GuildCacheUtil.refresh(onlinePlayerUuids);
    }

    private static void refreshTasksDaily() {
        if (!F.tryAcquire()) {
            return;
        }
        try {
            if (!ConfigUtil.TASK_CONFIG.getBoolean("refresh")) {
                return;
            }
            if (DateUtil.dayOfWeekEnum(new Date()) != 1) {
                return;
            }
            if (!"01:00".equals(DateUtil.format(new Date(), "HH:mm"))) {
                return;
            }
            List<GuildInfo> guildInfos = GuildInfoService.getInstance().findAll();
            if (CollUtil.isEmpty(guildInfos)) {
                return;
            }
            List<Integer> guildIds = guildInfos.stream().map(GuildInfo::getId).collect(Collectors.toList());
            Map<Integer, List<GuildPlayer>> guildPlayersByGuildInfoId = GuildPlayerService.getInstance().findMapByGuildInfoIds(guildIds);
            String refreshMessage = ConfigUtil.TASK_CONFIG.getString("refreshMsg", "");
            Iterator<GuildInfo> guildInfoIterator = guildInfos.iterator();
            while (guildInfoIterator.hasNext()) {
                GuildInfo guildInfo = guildInfoIterator.next();
                PlayerTaskUtil.createTask(guildInfo.getId());
                Iterator<GuildPlayer> playerIterator = guildPlayersByGuildInfoId.getOrDefault(guildInfo.getId(), new ArrayList()).iterator();
                while (playerIterator.hasNext()) {
                    MessageUtil.sendMessage(playerIterator.next().getPlayerUuid(), refreshMessage);
                }
            }
            MessageUtil.sendConsoleMessage(new StringBuilder().insert(0, "重置任务完成时间:").append(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss")).toString());
            return;
        }
        finally {
            F.release();
        }
    }

    private static void I() {
        if (!taskStartLock.tryAcquire()) {
            return;
        }
        try {
            if (!ConfigUtil.TASK_CONFIG.getBoolean("refresh")) {
                return;
            }
            Integer dayOfWeek = DateUtil.dayOfWeekEnum(new Date());
            if (dayOfWeek.intValue() != ConfigUtil.TASK_CONFIG.getInt("startDate", 7)) {
                return;
            }
            String startTime = ConfigUtil.TASK_CONFIG.getString("startTime", "23:00");
            String currentTime = DateUtil.format(new Date(), "HH:mm");
            if (!startTime.equals(currentTime)) {
                return;
            }
            MessageUtil.sendConsoleMessage(new StringBuilder().insert(0, "公会任务 开始结算时间:星期").append(dayOfWeek).append(" ").append(currentTime).toString());
            List<GuildTask> guildTasks = GuildTaskService.getInstance().findSeasonTask();
            MessageUtil.sendConsoleMessage(new StringBuilder().insert(0, "公会任务 本周完成任务数量").append(guildTasks.size()).toString());
            if (CollUtil.isEmpty(guildTasks)) {
                return;
            }
            RewardCommand.rewardTask(guildTasks);
            MessageUtil.sendConsoleMessage(new StringBuilder().insert(0, "每周任务结算发奖 完成时间:").append(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss")).toString());
            return;
        }
        finally {
            taskStartLock.release();
        }
    }

    private static void startSeasonPvp() {
        if (!seasonStartLock.tryAcquire()) {
            return;
        }
        try {
            if (!ConfigUtil.ACTIVITY_CONFIG.getBoolean("seasonPvp.enable", true)) {
                return;
            }
            int startDay = ConfigUtil.ACTIVITY_CONFIG.getInt("seasonPvp.startDate");
            if (DateUtil.dayOfWeekEnum(new Date()) != startDay) {
                return;
            }
            String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
            String startTime = ConfigUtil.ACTIVITY_CONFIG.getString("seasonPvp.startTime", "");
            List<String> remindTimes = ConfigUtil.ACTIVITY_CONFIG.getStringList("seasonPvp.remindTime");
            if (CollUtil.isNotEmpty(remindTimes) && remindTimes.contains(currentTime)) {
                String remindMessage = ConfigUtil.ACTIVITY_CONFIG.getString("season.seasonPvpRemindMsg");
                if (StrUtil.isNotEmpty(remindMessage)) {
                    remindMessage = remindMessage.replace("${time}", startTime);
                    MessageUtil.sendAllMessage(remindMessage);
                }
            }
            if (!currentTime.equals(startTime)) {
                return;
            }
            SeasonPvpManager.startSeason();
            return;
        }
        finally {
            seasonStartLock.release();
        }
    }

    static {
        matchInitLock = new Semaphore(1);
        seasonStartLock = new Semaphore(1);
        F = new Semaphore(1);
        taskStartLock = new Semaphore(1);
        e = new Semaphore(1);
        weeklyMoneyLock = new Semaphore(1);
    }

    private static int calcMaintenanceCost(@NotNull String settingKey, @NotNull GuildInfo guildInfo) {
        int cost = BaseConstants.CONFIG.getInt(new StringBuilder().insert(0, "guildSetting.").append(guildInfo.getLevel()).append(".").append(settingKey).toString(), 0);
        if (cost > 0) {
            return cost * GuildRunTaskTimer.nullSafeInt(guildInfo.getMemberCount());
        }
        return 0;
    }

    private static void initPvpManagers() {
        HandySchedulerUtil.runTaskTimerAsynchronously(() -> {
            if (!matchInitLock.tryAcquire()) {
                return;
            }
            try {
                MatchPvpManager.init();
                ScorePvpManager.init();
                StrongholdPvpManager.init();
                return;
            }
            finally {
                matchInitLock.release();
            }
        }, 1L, 20L);
        GuildRunTaskTimer.cacheSacredStoneProsperity();
        GuildRunTaskTimer.e();
        GuildRunTaskTimer.cleanTaskDemandCache();
        GuildRunTaskTimer.F();
        HandySchedulerUtil.runTaskTimerAsynchronously(() -> {
            GuildRunTaskTimer.startSeasonPvp();
            GuildRunTaskTimer.refreshTasksDaily();
            GuildRunTaskTimer.I();
            GuildRunTaskTimer.deductMaintenanceDaily();
            GuildRunTaskTimer.J();
        }, 1L, 1200L);
    }

    private static void e() {
        HandySchedulerUtil.runTaskTimerAsynchronously(() -> {
            if (!"01 00".equals(DateUtil.format(new Date(), "dd HH"))) {
                return;
            }
            if (!BaseConstants.CONFIG.getBoolean("monthClear", true)) {
                return;
            }
            List<GuildInfo> guildInfos = GuildInfoService.getInstance().list();
            if (CollUtil.isEmpty(guildInfos)) {
                return;
            }
            GuildInfoService.getInstance().clearMonthProsperityDegree();
            List<Integer> guildIds = guildInfos.stream().map(GuildInfo::getId).collect(Collectors.toList());
            Map<Integer, List<GuildPlayer>> guildPlayersByGuildInfoId = GuildPlayerService.getInstance().findMapByGuildInfoIds(guildIds);
            for (GuildInfo guildInfo : guildInfos) {
                HashMap<Integer, List<OfflinePlayer>> rolePlayers = MapUtil.of();
                Iterator<Map.Entry<Integer, List<GuildPlayer>>> roleEntryIterator = guildPlayersByGuildInfoId.getOrDefault(guildInfo.getId(), new ArrayList<GuildPlayer>()).stream().collect(Collectors.groupingBy(GuildPlayer::getRoleId)).entrySet().iterator();
                while (roleEntryIterator.hasNext()) {
                    Map.Entry<Integer, List<GuildPlayer>> entry = roleEntryIterator.next();
                    ArrayList<OfflinePlayer> roleOfflinePlayers = new ArrayList<OfflinePlayer>();
                    Iterator<GuildPlayer> playerIterator = entry.getValue().iterator();
                    while (playerIterator.hasNext()) {
                        roleOfflinePlayers.add(Bukkit.getOfflinePlayer(playerIterator.next().getPlayerUuid()));
                    }
                    rolePlayers.put(entry.getKey(), roleOfflinePlayers);
                }
                GuildClearMonthProsperityDegreeEvent.callEvent(rolePlayers, guildInfo.getGuildName(), guildInfo.getMonthProsperityDegree());
            }
            MessageUtil.sendConsoleMessage(new StringBuilder().insert(0, "公会月度活跃清理,清理时间:").append(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss")).toString());
        }, 1L, 72000L);
    }

    private static void deductMaintenanceDaily() {
        if (!e.tryAcquire()) {
            return;
        }
        try {
            if (!"00:00".equals(DateUtil.format(new Date(), "HH:mm"))) {
                return;
            }
            List<GuildInfo> guildInfos = GuildInfoService.getInstance().findAll();
            if (CollUtil.isEmpty(guildInfos)) {
                return;
            }
            HashMap<Integer, List<Integer>> moneyCostMap = new HashMap<Integer, List<Integer>>();
            ArrayList<Integer> overMoneyGuildIds = new ArrayList<Integer>();
            HashMap<Integer, List<Integer>> activeCostMap = new HashMap<Integer, List<Integer>>();
            ArrayList<Integer> overActiveGuildIds = new ArrayList<Integer>();
            for (GuildInfo guildInfo : guildInfos) {
                int cost = GuildRunTaskTimer.calcMaintenanceCost("maintenanceMoney", guildInfo);
                if (cost > 0) {
                    GuildRunTaskTimer.addGuildToBatch(cost, guildInfo.getId(), GuildRunTaskTimer.nullSafeInt(guildInfo.getMoney()), moneyCostMap, overMoneyGuildIds);
                }
                if ((cost = GuildRunTaskTimer.calcMaintenanceCost("maintenanceActive", guildInfo)) <= 0) continue;
                GuildRunTaskTimer.addGuildToBatch(cost, guildInfo.getId(), GuildRunTaskTimer.nullSafeInt(guildInfo.getProsperityDegree()), activeCostMap, overActiveGuildIds);
            }
            GuildInfoService.getInstance().batchSubtractMoney(moneyCostMap, overMoneyGuildIds);
            GuildInfoService.getInstance().batchSubtractProsperityDegree(activeCostMap, overActiveGuildIds);
            MessageUtil.sendConsoleMessage(new StringBuilder().insert(0, "公会维护资金扣除完成时间:").append(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss")).toString());
            return;
        }
        finally {
            e.release();
        }
    }

    private GuildRunTaskTimer() {
    }

    private static void F() {
        HandySchedulerUtil.runTaskTimerAsynchronously(GuildConstants.PLAYER_GUILD_OFFLINE_CACHE_MAP::clear, 0L, 36000L);
    }

    private static void J() {
        if (!weeklyMoneyLock.tryAcquire()) {
            return;
        }
        try {
            Date date = new Date();
            if (DateUtil.dayOfWeekEnum(date) != 1) {
                return;
            }
            if (!"00:00".equals(DateUtil.format(date, "HH:mm"))) {
                return;
            }
            int clearedCount = GuildPlayerService.getInstance().clearWeekMoney();
            MessageUtil.sendConsoleMessage(new StringBuilder().insert(0, "清理周贡献成功,清理时间:").append(DateUtil.format(date, "yyyy-MM-dd HH:mm:ss")).append(",本次清理:").append(clearedCount).append("数据").toString());
            return;
        }
        finally {
            weeklyMoneyLock.release();
        }
    }
}
