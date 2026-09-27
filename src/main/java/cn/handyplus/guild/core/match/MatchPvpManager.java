package cn.handyplus.guild.core.match;

import cn.handyplus.guild.constants.GuildActivityType;
import cn.handyplus.guild.constants.GuildPvpBattleTypeEnum;
import cn.handyplus.guild.constants.GuildPvpRoomStatusEnum;
import cn.handyplus.guild.core.common.GuildPvpBuffUtil;
import cn.handyplus.guild.core.common.GuildPvpLogUtil;
import cn.handyplus.guild.core.common.GuildPvpMsgUtil;
import cn.handyplus.guild.core.common.GuildPvpTpUtil;
import cn.handyplus.guild.core.common.GuildPvpUtil;
import cn.handyplus.guild.core.match.MatchPvpConstants;
import cn.handyplus.guild.core.match.MatchPvpReward;
import cn.handyplus.guild.core.match.MatchPvpUtil;
import cn.handyplus.guild.core.match.MatchRoomData;
import cn.handyplus.guild.core.score.ScorePvpManager;
import cn.handyplus.guild.core.score.ScorePvpReward;
import cn.handyplus.guild.enter.GuildPlayer;
import cn.handyplus.guild.enter.GuildPvpLocation;
import cn.handyplus.guild.enter.GuildSpawnPvpLocation;
import cn.handyplus.guild.lib.BaseUtil;
import cn.handyplus.guild.lib.CollUtil;
import cn.handyplus.guild.lib.DateUtil;
import cn.handyplus.guild.lib.HandySchedulerUtil;
import cn.handyplus.guild.lib.LegacyUtil;
import cn.handyplus.guild.lib.MessageUtil;
import cn.handyplus.guild.lib.RgbTextUtil;
import cn.handyplus.guild.param.GuildPvpMatchQueue;
import cn.handyplus.guild.param.GuildPvpMatchRoom;
import cn.handyplus.guild.param.GuildPvpPlayerParam;
import cn.handyplus.guild.service.GuildPlayerService;
import cn.handyplus.guild.service.GuildPvpLocationService;
import cn.handyplus.guild.service.GuildPvpLogService;
import cn.handyplus.guild.service.GuildSpawnPvpLocationService;
import cn.handyplus.guild.util.ConfigUtil;
import cn.handyplus.guild.util.GuildUtil;
import cn.handyplus.guild.util.KitPvpUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MatchPvpManager {
    public static synchronized GuildPvpMatchQueue pPpppP(Integer guildId) {
        return MatchPvpConstants.e.get(guildId);
    }

    private static List<GuildPvpMatchQueue> selectQueues() {
        List<GuildPvpMatchQueue> waitingQueues = MatchPvpConstants.e.values().stream().filter(queue -> {
            if (queue.getWaitSecond() > 60) {
                return true;
            }
            return false;
        }).collect(Collectors.toList());
        if (waitingQueues.size() >= 2) {
            return GuildPvpUtil.takeTwoQueue(waitingQueues, MatchPvpConstants.e);
        }
        ArrayList<GuildPvpMatchQueue> candidates = new ArrayList<GuildPvpMatchQueue>(MatchPvpConstants.e.values());
        if (waitingQueues.size() == 1) {
            GuildPvpMatchQueue firstQueue = waitingQueues.get(0);
            candidates.removeIf(candidateQueue -> MatchPvpManager.isSameGuild(firstQueue, candidateQueue));
            Collections.shuffle(candidates);
            ArrayList<GuildPvpMatchQueue> pair = new ArrayList<GuildPvpMatchQueue>(2);
            GuildPvpMatchQueue removedFirst = MatchPvpConstants.e.remove(firstQueue.getGuildId());
            if (removedFirst != null) {
                pair.add(removedFirst);
            }
            Iterator<GuildPvpMatchQueue> candidateIterator = candidates.iterator();
            while (candidateIterator.hasNext()) {
                GuildPvpMatchQueue candidate = candidateIterator.next();
                GuildPvpMatchQueue removedCandidate = MatchPvpConstants.e.remove(candidate.getGuildId());
                if (removedCandidate == null) {
                    continue;
                }
                pair.add(removedCandidate);
                return pair;
            }
            return pair;
        }
        return GuildPvpUtil.takeTwoQueue(candidates, MatchPvpConstants.e);
    }

    private static void F() {
        Iterator<GuildPvpMatchRoom> roomIterator;
        Iterator<GuildPvpMatchRoom> loopIterator = roomIterator = new ArrayList<GuildPvpMatchRoom>(MatchPvpConstants.A.values()).iterator();
        while (loopIterator.hasNext()) {
            GuildPvpMatchRoom guildPvpMatchRoom = roomIterator.next();
            if (!GuildPvpRoomStatusEnum.PREPARING.equals((Object)guildPvpMatchRoom.getStatus())) {
                loopIterator = roomIterator;
                continue;
            }
            GuildPvpMatchRoom prepareRoom = guildPvpMatchRoom;
            int prepareSecond = prepareRoom.getPrepareSecond() + 1;
            prepareRoom.setPrepareSecond(prepareSecond);
            int remainSecond = 31 - prepareSecond;
            if (MatchPvpConstants.M.contains(prepareSecond)) {
                MatchPvpManager.sendPrepareCountdown(guildPvpMatchRoom, remainSecond);
            }
            if (MatchPvpConstants.XxXxxX.contains(prepareSecond)) {
                GuildPvpMsgUtil.sendPrepareTitle(guildPvpMatchRoom.getPreparationPlayerMap(), remainSecond);
            }
            if (prepareSecond <= 31) {
                loopIterator = roomIterator;
                continue;
            }
            Iterator<Integer> guildIdIterator = guildPvpMatchRoom.getGuildIdList().iterator();
            while (guildIdIterator.hasNext()) {
                Integer guildId = guildIdIterator.next();
                if (CollUtil.isNotEmpty(guildPvpMatchRoom.getPreparationPlayerMap().get(guildId))) {
                    continue;
                }
                if (GuildPvpBattleTypeEnum.SCORE_MATCH.equals((Object)guildPvpMatchRoom.getBattleType())) {
                    GuildPvpMatchRoom scoreRoom = guildPvpMatchRoom;
                    if (ScorePvpManager.handleNoPlayerForfeit(scoreRoom, MatchPvpManager.buildStartPlayerMap(scoreRoom))) {
                        return;
                    }
                }
                MatchPvpManager.cancelRoom(guildPvpMatchRoom, true);
                return;
            }
            GuildPvpMatchRoom roomToStart = guildPvpMatchRoom;
            roomToStart.setStatus(GuildPvpRoomStatusEnum.STARTING);
            HandySchedulerUtil.runTask(() -> MatchPvpManager.startRoom(roomToStart.getGuildIdList()));
            loopIterator = roomIterator;
        }
    }

    private static GuildPvpLocation copyLocation(GuildPvpLocation source) {
        GuildPvpLocation copiedLocation;
        GuildPvpLocation copy = copiedLocation = new GuildPvpLocation();
        GuildPvpLocation sourceRef = source;
        copiedLocation.setId(sourceRef.getId());
        copy.setName(sourceRef.getName());
        copy.setType(source.getType());
        return copy;
    }

    public static synchronized void denyPreparation(Integer guildId, UUID playerUuid) {
        GuildPvpMatchRoom guildPvpMatchRoom = MatchPvpManager.getRoomByGuildId(guildId);
        if (guildPvpMatchRoom == null || !GuildPvpRoomStatusEnum.PREPARING.equals((Object)guildPvpMatchRoom.getStatus())) {
            return;
        }
        List<UUID> playerUuids = guildPvpMatchRoom.getPreparationPlayerMap().get(guildId);
        if (CollUtil.isEmpty(playerUuids)) {
            return;
        }
        playerUuids.remove(playerUuid);
    }

    public static synchronized int forceStop(@NotNull GuildPvpBattleTypeEnum battleType) {
        ArrayList<GuildPvpMatchRoom> rooms = new ArrayList<GuildPvpMatchRoom>(MatchPvpConstants.A.values());
        int stoppedCount = 0;
        Iterator<GuildPvpMatchRoom> roomIterator = rooms.iterator();
        while (roomIterator.hasNext()) {
            GuildPvpMatchRoom guildPvpMatchRoom = roomIterator.next();
            if (!battleType.equals((Object)guildPvpMatchRoom.getBattleType())) {
                continue;
            }
            guildPvpMatchRoom.setStatus(GuildPvpRoomStatusEnum.FINISHED);
            MatchPvpManager.F(guildPvpMatchRoom);
            if (GuildPvpBattleTypeEnum.SCORE_MATCH.equals((Object)battleType)) {
                ScorePvpManager.releaseRunning(guildPvpMatchRoom.getGuildIdList());
            }
            ++stoppedCount;
            MatchPvpManager.finishRoom(guildPvpMatchRoom.getGuildIdList());
        }
        return stoppedCount;
    }

    public static synchronized GuildPvpMatchRoom getRoomByGuildId(Integer guildId) {
        Integer roomLocationId = MatchPvpConstants.F.get(guildId);
        if (roomLocationId == null) {
            return null;
        }
        return MatchPvpConstants.A.get(roomLocationId);
    }

    public static synchronized void cancelQueue(Integer guildId) {
        MatchPvpConstants.e.remove(guildId);
    }

    public static synchronized List<GuildPvpMatchRoom> listRooms() {
        return new ArrayList<GuildPvpMatchRoom>(MatchPvpConstants.A.values());
    }

    private static void F(@NotNull GuildPvpMatchRoom matchRoom) {
        Iterator<List<GuildPvpPlayerParam>> playerListIterator = matchRoom.getStartPlayerMap().values().iterator();
        while (playerListIterator.hasNext()) {
            Iterator<GuildPvpPlayerParam> playerIterator = playerListIterator.next().iterator();
            while (playerIterator.hasNext()) {
                UUID playerUuid = playerIterator.next().getPlayerUuid();
                Optional<Player> optional = BaseUtil.getOnlinePlayer(playerUuid);
                if (!optional.isPresent()) {
                    continue;
                }
                HandySchedulerUtil.runTaskAsynchronously(() -> GuildPvpBuffUtil.restorePlayerBuff(playerUuid));
                GuildPvpTpUtil.tp(matchRoom.getLookLocation(), matchRoom.getOriginLocationMap().get(playerUuid), optional.get());
            }
        }
    }

    private static Map<Integer, List<GuildPvpPlayerParam>> buildStartPlayerMap(GuildPvpMatchRoom matchRoom) {
        HashMap<Integer, List<GuildPvpPlayerParam>> startPlayerMap = new HashMap<Integer, List<GuildPvpPlayerParam>>(2);
        HashMap<UUID, Location> originLocationMap = new HashMap<UUID, Location>();
        Integer firstGuildId = matchRoom.getGuildIdList().get(0);
        Integer secondGuildId = matchRoom.getGuildIdList().get(1);
        Iterator<Integer> guildIdIterator = matchRoom.getGuildIdList().iterator();
        while (guildIdIterator.hasNext()) {
            Integer guildId = guildIdIterator.next();
            ArrayList<GuildPvpPlayerParam> playerParams = new ArrayList<GuildPvpPlayerParam>();
            Iterator<UUID> uuidIterator = matchRoom.getPreparationPlayerMap().getOrDefault(guildId, new ArrayList<UUID>()).iterator();
            while (uuidIterator.hasNext()) {
                UUID playerUuid = uuidIterator.next();
                Optional<Player> optional = BaseUtil.getOnlinePlayer(playerUuid);
                if (!optional.isPresent()) {
                    continue;
                }
                Player player = optional.get();
                if (player.isDead()) {
                    continue;
                }
                GuildPvpPlayerParam guildPvpPlayerParam = new GuildPvpPlayerParam();
                guildPvpPlayerParam.setMeGuildId(guildId);
                guildPvpPlayerParam.setRivalGuildId(guildId.equals(firstGuildId) ? secondGuildId : firstGuildId);
                guildPvpPlayerParam.setPlayerName(player.getName());
                guildPvpPlayerParam.setPlayerUuid(playerUuid);
                guildPvpPlayerParam.setKill(0);
                guildPvpPlayerParam.setIsDeath(false);
                playerParams.add(guildPvpPlayerParam);
                originLocationMap.put(playerUuid, player.getLocation());
            }
            startPlayerMap.put(guildId, playerParams);
        }
        matchRoom.setOriginLocationMap(originLocationMap);
        matchRoom.setStartPlayerMap(startPlayerMap);
        return startPlayerMap;
    }

    public static synchronized void init() {
        MatchPvpManager.processQueue();
        MatchPvpManager.F();
        MatchPvpManager.J();
    }

    private static void J(GuildPvpMatchRoom matchRoom) {
        Iterator<Integer> guildIdIterator;
        Iterator<Integer> loopIterator = guildIdIterator = matchRoom.getGuildIdList().iterator();
        while (loopIterator.hasNext()) {
            Integer guildId = guildIdIterator.next();
            MatchPvpConstants.F.remove(guildId);
            loopIterator = guildIdIterator;
        }
        MatchPvpConstants.A.remove(matchRoom.getLocationId());
    }

    private static MatchRoomData findAvailableRoom() {
        ArrayList<Integer> usedLocationIds = new ArrayList<Integer>(MatchPvpConstants.A.keySet());
        List<GuildPvpLocation> locations = GuildPvpLocationService.getInstance().listByNotIds(GuildActivityType.MATCH.getType(), usedLocationIds);
        if (CollUtil.isEmpty(locations)) {
            return null;
        }
        Collections.shuffle(locations);
        List<Integer> locationIds = locations.stream().map(GuildPvpLocation::getId).collect(Collectors.toList());
        Map<Integer, List<GuildSpawnPvpLocation>> spawnLocationsByLocationId = GuildSpawnPvpLocationService.getInstance().listByLocationIds(locationIds);
        Iterator<GuildPvpLocation> locationIterator = locations.iterator();
        while (locationIterator.hasNext()) {
            GuildPvpLocation location = locationIterator.next();
            List<GuildSpawnPvpLocation> spawnLocations = spawnLocationsByLocationId.get(location.getId());
            if (CollUtil.isEmpty(spawnLocations)) {
                continue;
            }
            MatchRoomData matchRoomData = new MatchRoomData();
            matchRoomData.setLocationId(location.getId());
            matchRoomData.setGuildPvpLocation(MatchPvpManager.copyLocation(location));
            matchRoomData.setFirstLocation(GuildUtil.getLocation(spawnLocations.get(0)));
            matchRoomData.setSecondLocation(spawnLocations.size() > 1 ? GuildUtil.getLocation(spawnLocations.get(1)) : matchRoomData.getFirstLocation().clone());
            if (spawnLocations.size() >= 3) {
                matchRoomData.setLookLocation(GuildUtil.getLocation(spawnLocations.get(2)));
            }
            return matchRoomData;
        }
        return null;
    }

    public static synchronized boolean acceptPreparation(Integer guildId, UUID playerUuid) {
        GuildPvpMatchRoom guildPvpMatchRoom = MatchPvpManager.getRoomByGuildId(guildId);
        if (guildPvpMatchRoom == null || !GuildPvpRoomStatusEnum.PREPARING.equals((Object)guildPvpMatchRoom.getStatus())) {
            return false;
        }
        List<UUID> playerUuids = guildPvpMatchRoom.getPreparationPlayerMap().get(guildId);
        if (playerUuids == null || playerUuids.contains(playerUuid)) {
            return false;
        }
        playerUuids.add(playerUuid);
        return true;
    }

    private static void J(List<GuildPvpMatchRoom> rooms) {
        String actionBarText = ConfigUtil.ACTIVITY_CONFIG.getString("matePvpTimeMsg", "");
        Iterator<GuildPvpMatchRoom> roomIterator = rooms.iterator();
        while (roomIterator.hasNext()) {
            GuildPvpMatchRoom guildPvpMatchRoom = roomIterator.next();
            Iterator<List<GuildPvpPlayerParam>> playerListIterator = guildPvpMatchRoom.getStartPlayerMap().values().iterator();
            while (playerListIterator.hasNext()) {
                Iterator<GuildPvpPlayerParam> playerIterator = playerListIterator.next().iterator();
                while (playerIterator.hasNext()) {
                    GuildPvpPlayerParam guildPvpPlayerParam = playerIterator.next();
                    if (guildPvpPlayerParam.getIsDeath().booleanValue()) {
                        continue;
                    }
                    Optional<Player> optional = BaseUtil.getOnlinePlayer(guildPvpPlayerParam.getPlayerUuid());
                    if (!optional.isPresent()) {
                        continue;
                    }
                    Player player = optional.get();
                    if (player.getHealth() <= 0.0) {
                        continue;
                    }
                    double damage = LegacyUtil.getMaxHealth((LivingEntity)player) * 0.1;
                    int health = (int)(player.getHealth() - (damage < 2.0 ? 2.0 : damage));
                    if (health <= 0) {
                        player.setHealth(0.0);
                        continue;
                    }
                    player.setHealth((double)health);
                    RgbTextUtil.init(actionBarText).sendActionBar(player);
                }
            }
        }
    }

    private static void J() {
        int maxSeconds = ConfigUtil.ACTIVITY_CONFIG.getInt("matePvpTime", 300);
        if (maxSeconds < 1) {
            return;
        }
        ArrayList<GuildPvpMatchRoom> expiredRooms = new ArrayList<GuildPvpMatchRoom>();
        Iterator<GuildPvpMatchRoom> roomIterator = new ArrayList<GuildPvpMatchRoom>(MatchPvpConstants.A.values()).iterator();
        block0: while (true) {
            Iterator<GuildPvpMatchRoom> loopIterator = roomIterator;
            while (loopIterator.hasNext()) {
                GuildPvpMatchRoom matchRoom = roomIterator.next();
                if (!GuildPvpRoomStatusEnum.RUNNING.equals((Object)matchRoom.getStatus())) {
                    loopIterator = roomIterator;
                    continue;
                }
                GuildPvpMatchRoom runningRoom = matchRoom;
                runningRoom.setStartSecond(runningRoom.getStartSecond() + 1);
                if (runningRoom.getStartSecond() < maxSeconds) continue block0;
                expiredRooms.add(matchRoom);
                continue block0;
            }
            break;
        }
        if (CollUtil.isEmpty(expiredRooms)) {
            return;
        }
        HandySchedulerUtil.runTask(() -> MatchPvpManager.J(expiredRooms));
    }

    public static synchronized void handleDeath(@NotNull Player player) {
        Integer guildId = MatchPvpUtil.getGuildIdByPlayer(player.getUniqueId());
        if (guildId == null) {
            return;
        }
        List<GuildPvpPlayerParam> startPlayers = MatchPvpUtil.getStartPlayers(guildId);
        if (CollUtil.isEmpty(startPlayers)) {
            return;
        }
        int deathCount = 0;
        Integer rivalGuildId = null;
        for (GuildPvpPlayerParam playerParam : startPlayers) {
            if (playerParam.getPlayerUuid().equals(player.getUniqueId())
                    && !playerParam.getIsDeath().booleanValue()) {
                playerParam.setIsDeath(true);
                rivalGuildId = playerParam.getRivalGuildId();
                Player killer = player.getKiller();
                if (killer != null) {
                    for (GuildPvpPlayerParam killerParam : MatchPvpUtil.getStartPlayers(playerParam.getRivalGuildId())) {
                        if (!killerParam.getPlayerUuid().equals(killer.getUniqueId())) {
                            continue;
                        }
                        killerParam.setKill(killerParam.getKill() + 1);
                        break;
                    }
                }
            }
            if (!playerParam.getIsDeath().booleanValue()) {
                continue;
            }
            ++deathCount;
        }
        if (startPlayers.size() == deathCount && rivalGuildId != null) {
            MatchPvpManager.finishBattle(guildId, rivalGuildId);
        }
    }

    private static void J(@NotNull GuildPvpBattleTypeEnum battleType, @NotNull Integer guildId, @NotNull Integer rivalGuildId) {
        if (GuildPvpBattleTypeEnum.SCORE_MATCH.equals((Object)battleType)) {
            ScorePvpReward.issueReward(guildId, rivalGuildId);
            return;
        }
        MatchPvpReward.issueReward(guildId, rivalGuildId);
    }

    private static boolean isSameGuild(GuildPvpMatchQueue sourceQueue, GuildPvpMatchQueue targetQueue) {
        return targetQueue.getGuildId().equals(sourceQueue.getGuildId());
    }

    public static synchronized void cancelNoPlayerRoom(@NotNull GuildPvpMatchRoom matchRoom) {
        MatchPvpManager.cancelRoom(matchRoom, true);
    }

    public static synchronized void finishRoom(List<Integer> guildIds) {
        if (CollUtil.isEmpty(guildIds)) {
            return;
        }
        Integer locationId = null;
        for (Integer guildId : guildIds) {
            guildId = MatchPvpConstants.F.remove(guildId);
            if (locationId != null) continue;
            locationId = guildId;
        }
        if (locationId != null) {
            MatchPvpConstants.A.remove(locationId);
        }
    }

    private static void issueRewardLater(GuildPvpBattleTypeEnum battleType, Integer guildId, Integer rivalGuildId) {
        MatchPvpManager.J(battleType, guildId, rivalGuildId);
    }

    private static void notifyNoPlayerAndFinish(GuildPvpMatchRoom matchRoom, List<Integer> guildIds) {
        Iterator<Integer> guildIdIterator = guildIds.iterator();
        while (guildIdIterator.hasNext()) {
            Integer guildId = guildIdIterator.next();
            Iterator<GuildPvpPlayerParam> playerIterator = matchRoom.getStartPlayerMap().getOrDefault(guildId, new ArrayList<GuildPvpPlayerParam>()).iterator();
            while (playerIterator.hasNext()) {
                GuildPvpPlayerParam guildPvpPlayerParam = playerIterator.next();
                MessageUtil.sendMessage(guildPvpPlayerParam.getPlayerUuid(), ConfigUtil.ACTIVITY_CONFIG.getString("noPlayer"));
            }
        }
        MatchPvpManager.J(matchRoom);
    }

    private static void processQueue() {
        Iterator<GuildPvpMatchQueue> queueIterator = new ArrayList<GuildPvpMatchQueue>(MatchPvpConstants.e.values()).iterator();
        while (queueIterator.hasNext()) {
            GuildPvpMatchQueue queue = queueIterator.next();
            int waitSecond = queue.getWaitSecond() + 1;
            queue.setWaitSecond(waitSecond);
            if (waitSecond <= 300) {
                continue;
            }
            MatchPvpConstants.e.remove(queue.getGuildId());
            MessageUtil.sendMessage(queue.getPlayerUuid(), ConfigUtil.ACTIVITY_CONFIG.getString("mateNoMsg"));
        }
        while (MatchPvpConstants.e.size() >= 2) {
            MatchRoomData matchRoomData = MatchPvpManager.findAvailableRoom();
            if (matchRoomData == null) {
                return;
            }
            List<GuildPvpMatchQueue> selectedQueues = MatchPvpManager.selectQueues();
            if (selectedQueues.size() < 2) {
                Iterator<GuildPvpMatchQueue> restoreIterator = selectedQueues.iterator();
                while (restoreIterator.hasNext()) {
                    GuildPvpMatchQueue restoreQueue = restoreIterator.next();
                    MatchPvpConstants.e.put(restoreQueue.getGuildId(), restoreQueue);
                }
                return;
            }
            MatchPvpManager.createRoom(matchRoomData, selectedQueues, GuildPvpBattleTypeEnum.MATCH);
        }
    }

    private static void createRoom(MatchRoomData roomData, List<GuildPvpMatchQueue> queues, GuildPvpBattleTypeEnum battleType) {
        List<Integer> guildIds = queues.stream().map(GuildPvpMatchQueue::getGuildId).collect(Collectors.toList());
        List<GuildPlayer> guildPlayers = GuildPlayerService.getInstance().findByGuildInfoIds(guildIds);
        GuildPvpMatchRoom guildPvpMatchRoom = new GuildPvpMatchRoom();
        guildPvpMatchRoom.setLocationId(roomData.getLocationId());
        guildPvpMatchRoom.setBattleType(battleType);
        guildPvpMatchRoom.setGuildPvpLocation(roomData.getGuildPvpLocation());
        guildPvpMatchRoom.setFirstLocation(roomData.getFirstLocation());
        guildPvpMatchRoom.setSecondLocation(roomData.getSecondLocation());
        guildPvpMatchRoom.setLookLocation(roomData.getLookLocation());
        guildPvpMatchRoom.setGuildIdList(guildIds);
        guildPvpMatchRoom.setStatus(GuildPvpRoomStatusEnum.PREPARING);
        guildPvpMatchRoom.setPrepareSecond(0);
        guildPvpMatchRoom.setStartSecond(0);
        guildPvpMatchRoom.setGuildPlayerMap(guildPlayers.stream().collect(Collectors.groupingBy(GuildPlayer::getGuildInfoId)));
        guildPvpMatchRoom.setPreparationPlayerMap(GuildPvpUtil.initPreparationPlayerMap(guildIds));
        if (GuildPvpBattleTypeEnum.SCORE_MATCH.equals(battleType)) {
            ScorePvpManager.autoAcceptPreparation(guildPvpMatchRoom);
        }
        guildPvpMatchRoom.setStartPlayerMap(new HashMap<Integer, List<GuildPvpPlayerParam>>(2));
        guildPvpMatchRoom.setOriginLocationMap(new HashMap<UUID, Location>());
        MatchPvpConstants.A.put(roomData.getLocationId(), guildPvpMatchRoom);
        for (Integer guildId : guildIds) {
            MatchPvpConstants.F.put(guildId, roomData.getLocationId());
            MatchPvpConstants.e.remove(guildId);
        }
    }

    private static void cancelRoom(GuildPvpMatchRoom matchRoom, boolean notifyPlayers) {
        if (notifyPlayers) {
            Iterator<Integer> guildIdIterator = matchRoom.getGuildIdList().iterator();
            while (guildIdIterator.hasNext()) {
                Integer guildId = guildIdIterator.next();
                Iterator<UUID> uuidIterator = matchRoom.getPreparationPlayerMap().getOrDefault(guildId, new ArrayList<UUID>()).iterator();
                while (uuidIterator.hasNext()) {
                    MessageUtil.sendMessage(uuidIterator.next(), ConfigUtil.ACTIVITY_CONFIG.getString("noPlayer"));
                }
            }
        }
        MatchPvpManager.J(matchRoom);
    }

    private static void sendPrepareCountdown(GuildPvpMatchRoom matchRoom, int remainSecond) {
        String message = ConfigUtil.ACTIVITY_CONFIG.getString("matePvpPrepareMsg", "${time}").replace("${time}", String.valueOf(remainSecond));
        GuildPvpMsgUtil.sendPrepareMessage(matchRoom.getGuildPlayerMap(), matchRoom.getPreparationPlayerMap(), message);
    }

    private MatchPvpManager() {
    }

    public static synchronized void startRoom(List<Integer> guildIds) {
        GuildPvpMatchRoom guildPvpMatchRoom = MatchPvpManager.getRoomByGuildId(guildIds.get(0));
        if (guildPvpMatchRoom == null) {
            MatchPvpManager.removeGuildRooms(guildIds);
            return;
        }
        if (guildPvpMatchRoom.getGuildPvpLocation() == null || guildPvpMatchRoom.getFirstLocation() == null || guildPvpMatchRoom.getSecondLocation() == null) {
            MatchPvpManager.cancelRoom(guildPvpMatchRoom, false);
            return;
        }
        Date startTime = new Date();
        guildPvpMatchRoom.setStartTime(startTime);
        HashMap<Integer, List<GuildPvpPlayerParam>> startPlayerMap = new HashMap<Integer, List<GuildPvpPlayerParam>>(2);
        HashMap<UUID, Location> originLocationMap = new HashMap<UUID, Location>();
        Integer firstGuildId = guildIds.get(0);
        Integer secondGuildId = guildIds.get(1);
        for (Integer guildId : guildIds) {
            List<UUID> playerUuids = guildPvpMatchRoom.getPreparationPlayerMap().getOrDefault(guildId, new ArrayList<UUID>());
            ArrayList<GuildPvpPlayerParam> playerParams = new ArrayList<GuildPvpPlayerParam>();
            for (UUID playerUuid : playerUuids) {
                Optional<Player> optional = BaseUtil.getOnlinePlayer(playerUuid);
                if (!optional.isPresent()) {
                    continue;
                }
                Player player = optional.get();
                if (player.isDead()) {
                    continue;
                }
                GuildPvpPlayerParam guildPvpPlayerParam = new GuildPvpPlayerParam();
                guildPvpPlayerParam.setMeGuildId(guildId);
                guildPvpPlayerParam.setRivalGuildId(guildId.equals(firstGuildId) ? secondGuildId : firstGuildId);
                guildPvpPlayerParam.setPlayerName(player.getName());
                guildPvpPlayerParam.setPlayerUuid(playerUuid);
                guildPvpPlayerParam.setKill(0);
                guildPvpPlayerParam.setIsDeath(false);
                playerParams.add(guildPvpPlayerParam);
                originLocationMap.put(playerUuid, player.getLocation());
            }
            startPlayerMap.put(guildId, playerParams);
        }
        guildPvpMatchRoom.setOriginLocationMap(originLocationMap);
        guildPvpMatchRoom.setStartPlayerMap(startPlayerMap);
        List<GuildPvpPlayerParam> firstPlayers = startPlayerMap.get(firstGuildId);
        List<GuildPvpPlayerParam> secondPlayers = startPlayerMap.get(secondGuildId);
        if (CollUtil.isEmpty(firstPlayers) || CollUtil.isEmpty(secondPlayers)) {
            if (GuildPvpBattleTypeEnum.SCORE_MATCH.equals((Object)guildPvpMatchRoom.getBattleType()) && ScorePvpManager.handleNoPlayerForfeit(guildPvpMatchRoom, startPlayerMap)) {
                return;
            }
            MatchPvpManager.notifyNoPlayerAndFinish(guildPvpMatchRoom, guildIds);
            return;
        }
        Location firstLocation = guildPvpMatchRoom.getFirstLocation();
        Location secondLocation = guildPvpMatchRoom.getSecondLocation();
        for (GuildPvpPlayerParam firstParam : firstPlayers) {
            Optional<Player> optional = BaseUtil.getOnlinePlayer(firstParam.getPlayerUuid());
            if (!optional.isPresent()) {
                continue;
            }
            Player player = optional.get();
            if (player.isDead()) {
                continue;
            }
            KitPvpUtil.startPvp(player, firstLocation, Color.BLUE, null);
        }
        for (GuildPvpPlayerParam secondParam : secondPlayers) {
            Optional<Player> optional = BaseUtil.getOnlinePlayer(secondParam.getPlayerUuid());
            if (!optional.isPresent()) {
                continue;
            }
            Player player = optional.get();
            if (player.isDead()) {
                continue;
            }
            KitPvpUtil.startPvp(player, secondLocation, Color.RED, null);
        }
        guildPvpMatchRoom.setStatus(GuildPvpRoomStatusEnum.RUNNING);
        guildPvpMatchRoom.setStartSecond(0);
        HandySchedulerUtil.runTaskAsynchronously(() -> GuildPvpMsgUtil.sendStartNotice(guildIds));
    }

    public static synchronized void toggleQueue(@NotNull Player player, @NotNull Integer guildId) {
        if (MatchPvpConstants.e.remove(guildId) != null) {
            return;
        }
        if (MatchPvpConstants.F.containsKey(guildId)) {
            return;
        }
        if (!MatchPvpUtil.isOpen()) {
            MessageUtil.sendMessage(player, ConfigUtil.ACTIVITY_CONFIG.getString("matePvpStopMsg"));
            return;
        }
        if (GuildPvpUtil.isClickCooling(player, ConfigUtil.ACTIVITY_CONFIG.getInt("matePvpClick"))) {
            return;
        }
        if (GuildPvpUtil.isPvpLocationMissing(player, GuildActivityType.MATCH)) {
            return;
        }
        int todayCount = GuildPvpLogService.getInstance().findByGuildInfoIdAndStartTime(guildId, DateUtil.getToday());
        boolean unlimited = ConfigUtil.ACTIVITY_CONFIG.getBoolean("matePvp.unlimited");
        if (todayCount >= ConfigUtil.ACTIVITY_CONFIG.getInt("matePvp.number") && !unlimited) {
            MessageUtil.sendMessage(player, ConfigUtil.ACTIVITY_CONFIG.getString("mateLimitMsg"));
            return;
        }
        GuildPvpMatchQueue guildPvpMatchQueue = GuildPvpMatchQueue.builder().guildId(guildId).playerUuid(player.getUniqueId()).waitSecond(0).build();
        MatchPvpConstants.e.put(guildId, guildPvpMatchQueue);
        HandySchedulerUtil.runTaskAsynchronously(() -> GuildPvpMsgUtil.sendMatchNotice(player, guildId, GuildActivityType.MATCH));
    }

    public static synchronized void finishBattle(@NotNull Integer guildId, @NotNull Integer rivalGuildId) {
        GuildPvpMatchRoom guildPvpMatchRoom = MatchPvpManager.getRoomByGuildId(rivalGuildId);
        if (guildPvpMatchRoom == null) {
            guildPvpMatchRoom = MatchPvpManager.getRoomByGuildId(guildId);
        }
        if (guildPvpMatchRoom == null || GuildPvpRoomStatusEnum.FINISHED.equals((Object)guildPvpMatchRoom.getStatus())) {
            return;
        }
        GuildPvpBattleTypeEnum guildPvpBattleTypeEnum = guildPvpMatchRoom.getBattleType();
        guildPvpMatchRoom.setStatus(GuildPvpRoomStatusEnum.FINISHED);
        GuildPvpLogUtil.asyncRecordPvpLog(guildId, rivalGuildId);
        GuildPvpBuffUtil.restoreBuff(guildId, rivalGuildId);
        HandySchedulerUtil.runTaskLater(() -> MatchPvpManager.issueRewardLater(guildPvpBattleTypeEnum, guildId, rivalGuildId), 20L);
    }

    private static void removeGuildRooms(List<Integer> guildIds) {
        Iterator<Integer> guildIdIterator;
        Iterator<Integer> loopIterator = guildIdIterator = guildIds.iterator();
        while (loopIterator.hasNext()) {
            Integer guildId = guildIdIterator.next();
            MatchPvpConstants.F.remove(guildId);
            loopIterator = guildIdIterator;
        }
    }

    public static synchronized boolean createScoreRoom(List<Integer> guildIds) {
        Iterator<Integer> guildIdIterator;
        block7: {
            block6: {
                if (CollUtil.isEmpty(guildIds)) break block6;
                if (guildIds.size() >= 2) break block7;
            }
            return false;
        }
        for (Integer guildId : guildIds) {
            if (!MatchPvpConstants.e.containsKey(guildId) && !MatchPvpConstants.F.containsKey(guildId)) continue;
            return false;
        }
        MatchRoomData matchRoomData = MatchPvpManager.findAvailableRoom();
        if (matchRoomData == null) {
            return false;
        }
        ArrayList<GuildPvpMatchQueue> queues = new ArrayList<GuildPvpMatchQueue>(guildIds.size());
        Iterator<Integer> loopIterator = guildIdIterator = guildIds.iterator();
        while (loopIterator.hasNext()) {
            Integer queuedGuildId = guildIdIterator.next();
            GuildPvpMatchQueue queue = new GuildPvpMatchQueue();
            loopIterator = guildIdIterator;
            GuildPvpMatchQueue newQueue = queue;
            newQueue.setGuildId(queuedGuildId);
            newQueue.setWaitSecond(0);
            queues.add(newQueue);
        }
        MatchPvpManager.createRoom(matchRoomData, queues, GuildPvpBattleTypeEnum.SCORE_MATCH);
        return true;
    }
}
