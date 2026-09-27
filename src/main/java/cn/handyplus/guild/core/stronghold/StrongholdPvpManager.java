package cn.handyplus.guild.core.stronghold;

import cn.handyplus.guild.constants.GuildActivityType;
import cn.handyplus.guild.constants.GuildPvpRoomStatusEnum;
import cn.handyplus.guild.core.common.GuildPvpBuffUtil;
import cn.handyplus.guild.core.common.GuildPvpLogUtil;
import cn.handyplus.guild.core.common.GuildPvpMsgUtil;
import cn.handyplus.guild.core.common.GuildPvpUtil;
import cn.handyplus.guild.core.season.SeasonPvpUtil;
import cn.handyplus.guild.core.stronghold.StrongholdPvpConstants;
import cn.handyplus.guild.core.stronghold.StrongholdPvpPlayer;
import cn.handyplus.guild.core.stronghold.StrongholdPvpPoint;
import cn.handyplus.guild.core.stronghold.StrongholdPvpRoom;
import cn.handyplus.guild.core.stronghold.StrongholdPvpUtil;
import cn.handyplus.guild.core.stronghold.StrongholdPvpView;
import cn.handyplus.guild.enter.GuildInfo;
import cn.handyplus.guild.enter.GuildPlayer;
import cn.handyplus.guild.enter.GuildPvpLocation;
import cn.handyplus.guild.enter.GuildSpawnPvpLocation;
import cn.handyplus.guild.lib.BaseUtil;
import cn.handyplus.guild.lib.CollUtil;
import cn.handyplus.guild.lib.EntitySchedulerUtil;
import cn.handyplus.guild.lib.HandyRunnable;
import cn.handyplus.guild.lib.HandySchedulerUtil;
import cn.handyplus.guild.lib.LegacyUtil;
import cn.handyplus.guild.lib.MessageUtil;
import cn.handyplus.guild.lib.PlayerSchedulerUtil;
import cn.handyplus.guild.lib.StrUtil;
import cn.handyplus.guild.param.GuildPvpMatchQueue;
import cn.handyplus.guild.service.GuildInfoService;
import cn.handyplus.guild.service.GuildPlayerService;
import cn.handyplus.guild.service.GuildPvpLocationService;
import cn.handyplus.guild.service.GuildSpawnPvpLocationService;
import cn.handyplus.guild.util.ConfigUtil;
import cn.handyplus.guild.util.GuildUtil;
import cn.handyplus.guild.util.KitPvpUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StrongholdPvpManager {
    public static synchronized boolean handleDeath(@NotNull Player player) {
        StrongholdPvpPlayer killedPlayer = StrongholdPvpManager.getPlayer(player.getUniqueId());
        if (killedPlayer == null) {
            return false;
        }
        if (killedPlayer.isWaitingRespawn()) {
            return true;
        }
        StrongholdPvpPlayer victim = killedPlayer;
        victim.setWaitingRespawn(true);
        victim.setDie(victim.getDie() + 1);
        victim.setDeathCount(victim.getDeathCount() + 1);
        Player killer = player.getKiller();
        if (killer != null) {
            StrongholdPvpPlayer killerPlayer = StrongholdPvpManager.getPlayer(killer.getUniqueId());
            if (killerPlayer != null && !killerPlayer.getGuildId().equals(killedPlayer.getGuildId())) {
                killerPlayer.setKill(killerPlayer.getKill() + 1);
            }
        }
        StrongholdPvpManager.respawnPlayer(player);
        return true;
    }

    private static void pPPpPP(@NotNull StrongholdPvpRoom room, @NotNull Integer guildId, @NotNull Location spawnLocation, @NotNull Color color, @NotNull Map<UUID, StrongholdPvpPlayer> playerMap) {
        List preparationPlayers = room.getPreparationPlayerMap().getOrDefault(guildId, new ArrayList());
        for (GuildPlayer guildPlayer : room.getGuildPlayerMap().getOrDefault(guildId, new ArrayList<GuildPlayer>())) {
            if (!preparationPlayers.contains(guildPlayer.getPlayerUuid())) {
                continue;
            }
            Optional<Player> optional = BaseUtil.getOnlinePlayer(guildPlayer.getPlayerUuid());
            if (!optional.isPresent()) {
                continue;
            }
            Player player = optional.get();
            if (player.isDead()) {
                continue;
            }
            StrongholdPvpPlayer roomPlayer = new StrongholdPvpPlayer();
            roomPlayer.setGuildId(guildId);
            roomPlayer.setPlayerName(player.getName());
            roomPlayer.setPlayerUuid(player.getUniqueId());
            roomPlayer.setOriginLocation(player.getLocation());
            roomPlayer.setOriginGameMode(player.getGameMode());
            playerMap.put(player.getUniqueId(), roomPlayer);
            KitPvpUtil.startPvp(player, spawnLocation, color, GameMode.SURVIVAL);
        }
    }

    private static int countNearbyPlayers(@NotNull StrongholdPvpRoom room, @NotNull StrongholdPvpPoint point, @NotNull Integer guildId) {
        int radius = ConfigUtil.ACTIVITY_CONFIG.getInt("strongholdPvp.radius");
        radius = Math.max(1, radius);
        int playerCount = 0;
        for (StrongholdPvpPlayer roomPlayer : room.getPlayerMap().values()) {
            if (!guildId.equals(roomPlayer.getGuildId())) continue;
            if (roomPlayer.isWaitingRespawn()) {
                continue;
            }
            Optional<Player> optional = BaseUtil.getOnlinePlayer(roomPlayer.getPlayerUuid());
            if (!optional.isPresent()) {
                continue;
            }
            Player player = optional.get();
            if (player.isDead()) {
                continue;
            }
            Location location = player.getLocation();
            if (location.getWorld() == null || point.getLocation().getWorld() == null) continue;
            if (!location.getWorld().getName().equals(point.getLocation().getWorld().getName())) {
                continue;
            }
            if (location.distance(point.getLocation()) > (double)radius) continue;
            ++playerCount;
        }
        return playerCount;
    }

    @NotNull
    private static String J(@NotNull StrongholdPvpRoom room) {
        Iterator<Integer> guildIdIterator;
        ArrayList<String> guildNames = new ArrayList<String>(room.getGuildIdList().size());
        Iterator<Integer> loopIterator = guildIdIterator = room.getGuildIdList().iterator();
        while (loopIterator.hasNext()) {
            Integer guildId = guildIdIterator.next();
            loopIterator = guildIdIterator;
            guildNames.add(StrongholdPvpManager.resolveGuildName(room, guildId));
        }
        return String.join((CharSequence)" &fVS &7", guildNames);
    }

    private static void F() {
        Iterator<StrongholdPvpRoom> roomIterator;
        int configuredSeconds = ConfigUtil.ACTIVITY_CONFIG.getInt("strongholdPvp.time");
        int maxSeconds = Math.max(1, configuredSeconds);
        Iterator<StrongholdPvpRoom> loopIterator = roomIterator = new ArrayList<StrongholdPvpRoom>(StrongholdPvpConstants.ROOM_MAP.values()).iterator();
        while (loopIterator.hasNext()) {
            StrongholdPvpRoom room = roomIterator.next();
            if (!GuildPvpRoomStatusEnum.RUNNING.equals((Object)room.getStatus())) {
                loopIterator = roomIterator;
                continue;
            }
            StrongholdPvpRoom runningRoom = room;
            int startSecond = runningRoom.getStartSecond() + 1;
            runningRoom.setStartSecond(startSecond);
            HandySchedulerUtil.runTask(() -> StrongholdPvpManager.tickRoom(runningRoom, startSecond, maxSeconds));
            loopIterator = roomIterator;
        }
    }

    public static boolean isRespawnTeleport(@NotNull UUID playerUuid, @Nullable Location targetLocation) {
        StrongholdPvpPlayer roomPlayer = StrongholdPvpManager.getPlayer(playerUuid);
        if (roomPlayer == null || !roomPlayer.isWaitingRespawn() || targetLocation == null) {
            return false;
        }
        Location spawnLocation = StrongholdPvpManager.getSpawnLocation(roomPlayer.getGuildId());
        if (spawnLocation == null || spawnLocation.getWorld() == null || targetLocation.getWorld() == null) {
            return false;
        }
        if (spawnLocation.getWorld().getName().equals(targetLocation.getWorld().getName()) && spawnLocation.distanceSquared(targetLocation) < 0.01) {
            return true;
        }
        return false;
    }

    private static void M(@NotNull StrongholdPvpRoom room) {
        Iterator<StrongholdPvpPoint> pointIterator;
        int pointScore = ConfigUtil.ACTIVITY_CONFIG.getInt("strongholdPvp.pointScore");
        pointScore = Math.max(1, pointScore);
        Iterator<StrongholdPvpPoint> loopIterator = pointIterator = room.getPointList().iterator();
        while (loopIterator.hasNext()) {
            StrongholdPvpPoint point = pointIterator.next();
            Integer ownerGuildId = StrongholdPvpManager.IiIiIi(room, point);
            if (ownerGuildId == null) {
                loopIterator = pointIterator;
                continue;
            }
            Integer scoredGuildId = ownerGuildId;
            room.getScoreMap().put(scoredGuildId, room.getScoreMap().getOrDefault(scoredGuildId, 0) + pointScore);
            point.setOwnerGuildId(ownerGuildId);
            Integer occupiedGuildId = ownerGuildId;
            point.getOccupySecondMap().put(occupiedGuildId, point.getOccupySecondMap().getOrDefault(occupiedGuildId, 0) + 1);
            Integer pointScoredGuildId = ownerGuildId;
            point.getScoreMap().put(pointScoredGuildId, point.getScoreMap().getOrDefault(pointScoredGuildId, 0) + pointScore);
            loopIterator = pointIterator;
        }
        if (room.getStartSecond() % 60 == 0) {
            StrongholdPvpManager.sendScoreMessage(room);
        }
    }

    @NotNull
    private static List<StrongholdPvpPlayer> getGuildPlayers(@NotNull Map<UUID, StrongholdPvpPlayer> players, @NotNull Integer guildId) {
        return players.values().stream().filter(roomPlayer -> guildId.equals(roomPlayer.getGuildId())).collect(Collectors.toList());
    }

    private static void J(@NotNull StrongholdPvpRoom room, int remainSecond) {
        String message = ConfigUtil.ACTIVITY_CONFIG.getString("strongholdPvp.prepareMsg", "");
        message = message.replace("${time}", String.valueOf(remainSecond));
        GuildPvpMsgUtil.sendPrepareMessage(room.getGuildPlayerMap(), room.getPreparationPlayerMap(), message);
    }

    private static void restoreGameMode(@NotNull Player player, @NotNull StrongholdPvpPlayer roomPlayer) {
        GameMode gameMode = roomPlayer.getOriginGameMode() == null ? GameMode.SURVIVAL : roomPlayer.getOriginGameMode();
        player.setGameMode(gameMode);
    }

    private static int getMatchGuildSize() {
        return Math.max(2, ConfigUtil.ACTIVITY_CONFIG.getInt("strongholdPvp.matchGuildSize", 2));
    }

    @NotNull
    private static List<GuildPvpMatchQueue> pPPpPP(@NotNull List<GuildPvpMatchQueue> queues, int size) {
        ArrayList<GuildPvpMatchQueue> selected;
        ArrayList<GuildPvpMatchQueue> shuffledQueues = new ArrayList<GuildPvpMatchQueue>(queues);
        Collections.shuffle(shuffledQueues);
        ArrayList<GuildPvpMatchQueue> result = new ArrayList<GuildPvpMatchQueue>(size);
        Iterator queueIterator = shuffledQueues.iterator();
        do {
            GuildPvpMatchQueue queue;
            Iterator loopIterator = queueIterator;
            while (true) {
                if (!loopIterator.hasNext()) {
                    return result;
                }
                queue = (GuildPvpMatchQueue)queueIterator.next();
                if ((queue = StrongholdPvpConstants.QUEUE_MAP.remove(queue.getGuildId())) != null) break;
                loopIterator = queueIterator;
            }
            selected = result;
            selected.add(queue);
        } while (selected.size() != size);
        return result;
    }

    private static void giveReward(@NotNull StrongholdPvpRoom room, @NotNull Integer guildId, int playerMoney, int guildMoney, @NotNull String message, @NotNull List<String> commands) {
        if (guildMoney > 0) {
            GuildInfoService.getInstance().addMoneyById(guildId, guildMoney);
        }
        String guildName = StrongholdPvpManager.resolveGuildName(room, guildId);
        List<StrongholdPvpPlayer> roomPlayers = StrongholdPvpManager.getGuildPlayers(room.getPlayerMap(), guildId);
        if (playerMoney > 0 && CollUtil.isNotEmpty(roomPlayers)) {
            List<UUID> playerUuids = roomPlayers.stream().map(StrongholdPvpPlayer::getPlayerUuid).collect(Collectors.toList());
            GuildPlayerService.getInstance().addMoneyByPlayerUuids(playerUuids, playerMoney);
        }
        for (StrongholdPvpPlayer roomPlayer : roomPlayers) {
            if (StrUtil.isNotEmpty(message)) {
                MessageUtil.sendMessage(roomPlayer.getPlayerUuid(), message.replace("${money}", String.valueOf(playerMoney)).replace("${guildMoney}", String.valueOf(guildMoney)));
            }
            for (String command : commands) {
                if (StrUtil.isEmpty(command)) {
                    continue;
                }
                PlayerSchedulerUtil.dispatchCommand(command.replace("${player}", roomPlayer.getPlayerName()).replace("${guildName}", guildName));
            }
        }
    }

    @Nullable
    public static synchronized StrongholdPvpRoom getRoomByGuildId(@NotNull Integer guildId) {
        Integer roomLocationId = StrongholdPvpConstants.GUILD_ROOM_MAP.get(guildId);
        if (roomLocationId == null) {
            return null;
        }
        return StrongholdPvpConstants.ROOM_MAP.get(roomLocationId);
    }

    @Nullable
    private static Integer IiIiIi(@NotNull StrongholdPvpRoom room, @NotNull StrongholdPvpPoint point) {
        Integer ownerGuildId = null;
        int maxCount = 0;
        boolean tied = false;
        Iterator<Integer> guildIdIterator = room.getGuildIdList().iterator();
        block0: while (true) {
            Iterator<Integer> loopIterator = guildIdIterator;
            while (loopIterator.hasNext()) {
                Integer guildId = guildIdIterator.next();
                int nearbyCount = StrongholdPvpManager.countNearbyPlayers(room, point, guildId);
                if (nearbyCount <= 0) continue block0;
                if (nearbyCount < maxCount) {
                    loopIterator = guildIdIterator;
                    continue;
                }
                if (nearbyCount == maxCount) {
                    tied = true;
                    loopIterator = guildIdIterator;
                    continue;
                }
                ownerGuildId = guildId;
                maxCount = nearbyCount;
                tied = false;
                loopIterator = guildIdIterator;
            }
            break;
        }
        if (tied) {
            return null;
        }
        return ownerGuildId;
    }

    public static synchronized boolean acceptPreparation(@NotNull Integer guildId, @NotNull UUID playerUuid) {
        StrongholdPvpRoom room = StrongholdPvpManager.getRoomByGuildId(guildId);
        if (room == null || !GuildPvpRoomStatusEnum.PREPARING.equals((Object)room.getStatus())) {
            return false;
        }
        List<UUID> preparationPlayers = room.getPreparationPlayerMap().get(guildId);
        if (preparationPlayers == null || preparationPlayers.contains(playerUuid)) {
            return false;
        }
        preparationPlayers.add(playerUuid);
        return true;
    }

    private static void J() {
        for (GuildPvpMatchQueue queue : new ArrayList<GuildPvpMatchQueue>(StrongholdPvpConstants.QUEUE_MAP.values())) {
            int waitSecond = queue.getWaitSecond() + 1;
            queue.setWaitSecond(waitSecond);
            if (waitSecond <= 300) {
                continue;
            }
            StrongholdPvpConstants.QUEUE_MAP.remove(queue.getGuildId());
            String message = ConfigUtil.ACTIVITY_CONFIG.getString("strongholdPvp.noMatchMsg");
            MessageUtil.sendMessage(queue.getPlayerUuid(), message);
        }
        while (true) {
            if (StrongholdPvpConstants.QUEUE_MAP.size() < 2) break;
            int matchGuildSize = StrongholdPvpManager.getMatchGuildSize();
            if (StrongholdPvpConstants.QUEUE_MAP.size() < matchGuildSize) {
                return;
            }
            AvailableRoom availableRoom = StrongholdPvpManager.findAvailableRoom();
            if (availableRoom == null) {
                return;
            }
            List<GuildPvpMatchQueue> selectedQueues = StrongholdPvpManager.pPPpPP(new ArrayList<GuildPvpMatchQueue>(StrongholdPvpConstants.QUEUE_MAP.values()), matchGuildSize);
            if (selectedQueues.size() < matchGuildSize) {
                for (GuildPvpMatchQueue restoredQueue : selectedQueues) {
                    StrongholdPvpConstants.QUEUE_MAP.put(restoredQueue.getGuildId(), restoredQueue);
                }
                return;
            }
            StrongholdPvpManager.createRoom(availableRoom, selectedQueues);
        }
    }

    private static void createRoom(@NotNull AvailableRoom roomData, @NotNull List<GuildPvpMatchQueue> queues) {
        List<Integer> guildIds = queues.stream().map(GuildPvpMatchQueue::getGuildId).collect(Collectors.toList());
        List<GuildPlayer> guildPlayers = GuildPlayerService.getInstance().findByGuildInfoIds(guildIds);
        StrongholdPvpRoom room = new StrongholdPvpRoom();
        AvailableRoom roomDataRef = roomData;
        room.setLocationId(roomData.locationId);
        room.setGuildPvpLocation(roomDataRef.pvpLocation);
        room.setGuildLocationMap(StrongholdPvpManager.IiIiIi(guildIds, roomDataRef.spawnLocations));
        room.setPointList(roomData.points);
        room.setGuildIdList(guildIds);
        room.setGuildNameMap(StrongholdPvpManager.loadGuildNames(guildIds));
        room.setStatus(GuildPvpRoomStatusEnum.PREPARING);
        room.setPrepareSecond(1);
        room.setStartSecond(1);
        HashMap<Integer, Integer> scoreMap = new HashMap<Integer, Integer>(guildIds.size());
        for (Integer guildId : guildIds) {
            scoreMap.put(guildId, 0);
        }
        room.setScoreMap(scoreMap);
        room.setGuildPlayerMap(guildPlayers.stream().collect(Collectors.groupingBy(GuildPlayer::getGuildInfoId)));
        room.setPreparationPlayerMap(GuildPvpUtil.initPreparationPlayerMap(guildIds));
        room.setPlayerMap(new HashMap<UUID, StrongholdPvpPlayer>());
        StrongholdPvpConstants.ROOM_MAP.put(roomData.locationId, room);
        for (Integer guildId : guildIds) {
            StrongholdPvpConstants.GUILD_ROOM_MAP.put(guildId, roomData.locationId);
            StrongholdPvpConstants.QUEUE_MAP.remove(guildId);
        }
        StrongholdPvpView.upsertHologram(room);
        StrongholdPvpManager.sendMatchSuccessMessage(room);
    }

    private static void J(@NotNull StrongholdPvpRoom room, @NotNull Integer guildId) {
        int participationContribution = ConfigUtil.ACTIVITY_CONFIG.getInt("strongholdPvp.participationContribution");
        int participationGuildMoney = ConfigUtil.ACTIVITY_CONFIG.getInt("strongholdPvp.participationGuildMoney");
        String message = ConfigUtil.ACTIVITY_CONFIG.getString("strongholdPvp.participationMsg", "");
        List commands = ConfigUtil.ACTIVITY_CONFIG.getStringList("strongholdPvp.participationCommand");
        StrongholdPvpManager.giveReward(room, guildId, 0, participationGuildMoney, message, commands);
    }

    private static void removeRoom(@NotNull StrongholdPvpRoom room) {
        HandySchedulerUtil.runTask(() -> StrongholdPvpView.deleteHologram(room));
        for (Integer guildId : room.getGuildIdList()) {
            StrongholdPvpConstants.GUILD_ROOM_MAP.remove(guildId);
        }
        for (UUID playerUuid : new HashSet<UUID>(room.getPlayerMap().keySet())) {
            StrongholdPvpConstants.PLAYER_ROOM_MAP.remove(playerUuid);
        }
        StrongholdPvpConstants.ROOM_MAP.remove(room.getLocationId());
    }

    public static synchronized void denyPreparation(@NotNull Integer guildId, @NotNull UUID playerUuid) {
        StrongholdPvpRoom room = StrongholdPvpManager.getRoomByGuildId(guildId);
        if (room == null || !GuildPvpRoomStatusEnum.PREPARING.equals((Object)room.getStatus())) {
            return;
        }
        List<UUID> preparationPlayers = room.getPreparationPlayerMap().get(guildId);
        if (CollUtil.isEmpty(preparationPlayers)) {
            return;
        }
        preparationPlayers.remove(playerUuid);
    }

    @NotNull
    private static String buildScoreText(@NotNull StrongholdPvpRoom room) {
        Iterator<Integer> guildIdIterator;
        ArrayList<String> parts = new ArrayList<String>(room.getGuildIdList().size());
        Iterator<Integer> loopIterator = guildIdIterator = room.getGuildIdList().iterator();
        while (loopIterator.hasNext()) {
            Integer guildId = guildIdIterator.next();
            loopIterator = guildIdIterator;
            parts.add(StrongholdPvpManager.resolveGuildName(room, guildId) + ": " + room.getScoreMap().getOrDefault(guildId, 0));
        }
        return String.join((CharSequence)" &8| &7", parts);
    }

    public static synchronized int forceStop() {
        Iterator roomIterator;
        ArrayList<StrongholdPvpRoom> rooms = new ArrayList<StrongholdPvpRoom>(StrongholdPvpConstants.ROOM_MAP.values());
        Iterator loopIterator = roomIterator = rooms.iterator();
        while (loopIterator.hasNext()) {
            StrongholdPvpRoom room = (StrongholdPvpRoom)roomIterator.next();
            loopIterator = roomIterator;
            StrongholdPvpRoom stoppedRoom = room;
            stoppedRoom.setStatus(GuildPvpRoomStatusEnum.FINISHED);
            StrongholdPvpManager.restorePlayers(stoppedRoom);
            StrongholdPvpManager.removeRoom(stoppedRoom);
        }
        return rooms.size();
    }

    private static void sendScoreMessage(@NotNull StrongholdPvpRoom room) {
        Iterator<StrongholdPvpPlayer> playerIterator;
        String message = ConfigUtil.ACTIVITY_CONFIG.getString("strongholdPvp.scoreMsg", "");
        message = message.replace("${scores}", StrongholdPvpManager.buildScoreText(room));
        Iterator<StrongholdPvpPlayer> loopIterator = playerIterator = room.getPlayerMap().values().iterator();
        while (loopIterator.hasNext()) {
            MessageUtil.sendMessage(playerIterator.next().getPlayerUuid(), message);
            loopIterator = playerIterator;
        }
    }

    public static synchronized void toggleQueue(@NotNull Player player, @NotNull Integer guildId) {
        if (StrongholdPvpConstants.QUEUE_MAP.remove(guildId) != null) {
            return;
        }
        if (StrongholdPvpConstants.GUILD_ROOM_MAP.containsKey(guildId)) {
            return;
        }
        if (!StrongholdPvpUtil.isOpen()) {
            MessageUtil.sendMessage(player, ConfigUtil.ACTIVITY_CONFIG.getString("strongholdPvp.stopMsg"));
            return;
        }
        if (GuildPvpUtil.isClickCooling(player, ConfigUtil.ACTIVITY_CONFIG.getInt("strongholdPvp.clickWait"))) {
            return;
        }
        if (GuildPvpUtil.isPvpLocationMissing(player, GuildActivityType.STRONGHOLD)) {
            return;
        }
        GuildPvpMatchQueue queue = GuildPvpMatchQueue.builder().guildId(guildId).playerUuid(player.getUniqueId()).waitSecond(0).build();
        StrongholdPvpConstants.QUEUE_MAP.put(guildId, queue);
        HandySchedulerUtil.runTaskAsynchronously(() -> GuildPvpMsgUtil.sendMatchNotice(player, guildId, GuildActivityType.STRONGHOLD));
    }

    public static void startRespawnCountdown(@NotNull UUID playerUuid) {
        UUID targetUuid = playerUuid;
        StrongholdPvpPlayer roomPlayer = StrongholdPvpManager.getPlayer(targetUuid);
        Optional<Player> optional = BaseUtil.getOnlinePlayer(targetUuid);
        if (roomPlayer == null || !roomPlayer.isWaitingRespawn() || !optional.isPresent()) {
            return;
        }
        optional.get().setGameMode(GameMode.SPECTATOR);
        int baseSecond = ConfigUtil.ACTIVITY_CONFIG.getInt("strongholdPvp.respawnBaseSecond");
        baseSecond = Math.max(1, baseSecond);
        int maxSecond = ConfigUtil.ACTIVITY_CONFIG.getInt("strongholdPvp.respawnMaxSecond");
        maxSecond = Math.max(baseSecond, maxSecond);
        int respawnSecond = StrongholdPvpManager.getRespawnDelay(baseSecond, maxSecond, roomPlayer.getDeathCount());
        HandySchedulerUtil.runTaskTimer(new HandyRunnable(){
            private int remaining = respawnSecond;

            @Override
            public void run() {
                StrongholdPvpPlayer pvpPlayer = StrongholdPvpManager.getPlayer(playerUuid);
                Optional<Player> playerOptional = BaseUtil.getOnlinePlayer(playerUuid);
                if (pvpPlayer == null || !pvpPlayer.isWaitingRespawn() || !playerOptional.isPresent()) {
                    this.cancel();
                    return;
                }
                Player player = playerOptional.get();
                if (this.remaining > 0) {
                    String title = ConfigUtil.ACTIVITY_CONFIG.getString("strongholdPvp.respawnTitle", "");
                    String subTitle = ConfigUtil.ACTIVITY_CONFIG.getString("strongholdPvp.respawnSubTitle", "");
                    subTitle = subTitle.replace("${time}", String.valueOf(this.remaining));
                    MessageUtil.sendTitle(player, title, subTitle);
                    --this.remaining;
                    return;
                }
                Location spawnLocation = StrongholdPvpManager.getSpawnLocation(pvpPlayer.getGuildId());
                player.setGameMode(GameMode.SURVIVAL);
                if (spawnLocation != null) {
                    EntitySchedulerUtil.teleport(player, spawnLocation);
                }
                HandySchedulerUtil.runTaskLater(() -> {
                    pvpPlayer.setWaitingRespawn(false);
                    player.setHealth(LegacyUtil.getMaxHealth(player));
                }, 1L);
                this.cancel();
            }
        }, 1L, 20L);
    }

    private static void finishRoomWithWinner(@NotNull StrongholdPvpRoom room, @Nullable Integer winnerGuildId) {
        if (winnerGuildId == null) {
            for (Integer guildId : room.getGuildIdList()) {
                StrongholdPvpManager.J(room, guildId);
            }
            String drawMessage = ConfigUtil.ACTIVITY_CONFIG.getString("strongholdPvp.drawMsg", "");
            drawMessage = drawMessage.replace("${scores}", StrongholdPvpManager.buildScoreText(room));
            MessageUtil.sendAllMessage(drawMessage);
            return;
        }
        int winContribution = ConfigUtil.ACTIVITY_CONFIG.getInt("strongholdPvp.winContribution");
        int winGuildMoney = ConfigUtil.ACTIVITY_CONFIG.getInt("strongholdPvp.winGuildMoney");
        String winMessage = ConfigUtil.ACTIVITY_CONFIG.getString("strongholdPvp.winMsg", "");
        List<String> commands = ConfigUtil.ACTIVITY_CONFIG.getStringList("strongholdPvp.winCommand");
        StrongholdPvpManager.giveReward(room, winnerGuildId, winContribution, winGuildMoney, winMessage, commands);
        for (Integer guildId : room.getGuildIdList()) {
            if (winnerGuildId.equals(guildId)) continue;
            StrongholdPvpManager.J(room, guildId);
        }
        String finishMessage = ConfigUtil.ACTIVITY_CONFIG.getString("strongholdPvp.finishMsg", "");
        finishMessage = finishMessage.replace("${win}", StrongholdPvpManager.resolveGuildName(room, winnerGuildId)).replace("${scores}", StrongholdPvpManager.buildScoreText(room));
        MessageUtil.sendAllMessage(finishMessage);
    }

    public static synchronized void init() {
        StrongholdPvpManager.J();
        StrongholdPvpManager.tickPrepareRooms();
        StrongholdPvpManager.F();
    }

    @Nullable
    private static AvailableRoom findAvailableRoom() {
        ArrayList<Integer> usedLocationIds = new ArrayList<Integer>(StrongholdPvpConstants.ROOM_MAP.keySet());
        List<GuildPvpLocation> locations = GuildPvpLocationService.getInstance().listByNotIds(GuildActivityType.STRONGHOLD.getType(), usedLocationIds);
        if (CollUtil.isEmpty(locations)) {
            return null;
        }
        List<GuildPvpLocation> shuffledLocations = locations;
        Collections.shuffle(shuffledLocations);
        Map<Integer, List<GuildSpawnPvpLocation>> spawnLocationsByLocationId = GuildSpawnPvpLocationService.getInstance().listByLocationIds(shuffledLocations.stream().map(GuildPvpLocation::getId).collect(Collectors.toList()));
        int matchGuildSize = StrongholdPvpManager.getMatchGuildSize();
        for (GuildPvpLocation location : shuffledLocations) {
            List<GuildSpawnPvpLocation> spawnLocations = spawnLocationsByLocationId.get(location.getId());
            if (CollUtil.isEmpty(spawnLocations)) continue;
            if (spawnLocations.size() <= matchGuildSize) continue;
            AvailableRoom roomData = new AvailableRoom();
            roomData.locationId = location.getId();
            roomData.pvpLocation = location;
            roomData.spawnLocations = new ArrayList<Location>();
            for (int spawnIndex = 0; spawnIndex < matchGuildSize; ++spawnIndex) {
                roomData.spawnLocations.add(GuildUtil.getLocation(spawnLocations.get(spawnIndex)));
            }
            roomData.points = new ArrayList<StrongholdPvpPoint>();
            for (int pointIndex = matchGuildSize; pointIndex < spawnLocations.size(); ++pointIndex) {
                GuildSpawnPvpLocation spawn = spawnLocations.get(pointIndex);
                StrongholdPvpPoint point = new StrongholdPvpPoint();
                point.setName(spawn.getSpawnName());
                point.setLocation(GuildUtil.getLocation(spawn));
                point.setHologramLocation(StrongholdPvpView.getHologramLocation(point.getLocation()));
                roomData.points.add(point);
            }
            return roomData;
        }
        return null;
    }

    private static void finishRoom(@NotNull StrongholdPvpRoom room) {
        if (!GuildPvpRoomStatusEnum.RUNNING.equals((Object)room.getStatus())) {
            return;
        }
        StrongholdPvpRoom finishedRoom = room;
        finishedRoom.setStatus(GuildPvpRoomStatusEnum.FINISHED);
        Integer winnerGuildId = StrongholdPvpManager.getWinnerGuildId(finishedRoom);
        StrongholdPvpManager.finishRoomWithWinner(finishedRoom, winnerGuildId);
        GuildPvpLogUtil.asyncRecordPvpLog(finishedRoom, winnerGuildId);
        StrongholdPvpManager.restorePlayers(finishedRoom);
        StrongholdPvpManager.removeRoom(finishedRoom);
    }

    @Nullable
    public static synchronized StrongholdPvpPlayer getPlayer(@NotNull UUID playerUuid) {
        StrongholdPvpRoom room;
        Integer roomLocationId = StrongholdPvpConstants.PLAYER_ROOM_MAP.get(playerUuid);
        StrongholdPvpRoom resolvedRoom = room = roomLocationId == null ? null : StrongholdPvpConstants.ROOM_MAP.get(roomLocationId);
        if (room == null) {
            return null;
        }
        return room.getPlayerMap().get(playerUuid);
    }

    @Nullable
    public static Location getSpawnLocation(@NotNull Integer guildId) {
        StrongholdPvpRoom room = StrongholdPvpManager.getRoomByGuildId(guildId);
        if (room == null || CollUtil.isEmpty(room.getGuildIdList())) {
            return null;
        }
        return room.getGuildLocationMap().get(guildId);
    }

    private static void tickRoom(@NotNull StrongholdPvpRoom room, int elapsedSecond, int maxSeconds) {
        if (!GuildPvpRoomStatusEnum.RUNNING.equals((Object)room.getStatus())) {
            return;
        }
        StrongholdPvpManager.M(room);
        if (StrongholdPvpView.shouldRender(elapsedSecond)) {
            StrongholdPvpView.render(room);
        }
        StrongholdPvpView.upsertHologram(room);
        if (elapsedSecond >= maxSeconds) {
            StrongholdPvpManager.finishRoom(room);
        }
    }

    @NotNull
    private static Map<Integer, String> loadGuildNames(@NotNull List<Integer> guildIds) {
        Iterator<GuildInfo> guildInfoIterator;
        HashMap<Integer, String> guildNames = new HashMap<Integer, String>(guildIds.size());
        Iterator<GuildInfo> loopIterator = guildInfoIterator = GuildInfoService.getInstance().findByIds(guildIds).iterator();
        while (loopIterator.hasNext()) {
            GuildInfo guildInfo = guildInfoIterator.next();
            loopIterator = guildInfoIterator;
            guildNames.put(guildInfo.getId(), guildInfo.getGuildName());
        }
        return guildNames;
    }

    public static synchronized void handleQuit(@NotNull Player player) {
        Integer roomLocationId = StrongholdPvpConstants.PLAYER_ROOM_MAP.get(player.getUniqueId());
        StrongholdPvpRoom room = roomLocationId == null ? null : StrongholdPvpConstants.ROOM_MAP.get(roomLocationId);
        StrongholdPvpPlayer roomPlayer = room == null ? null : room.getPlayerMap().get(player.getUniqueId());
        if (roomPlayer == null) {
            return;
        }
        StrongholdPvpManager.restoreGameMode(player, roomPlayer);
        StrongholdPvpConstants.PLAYER_ROOM_MAP.remove(player.getUniqueId());
        room.getPlayerMap().remove(player.getUniqueId());
    }

    @NotNull
    private static Map<Integer, Location> IiIiIi(@NotNull List<Integer> guildIds, @NotNull List<Location> locations) {
        HashMap<Integer, Location> spawnLocationMap = new HashMap<Integer, Location>(guildIds.size());
        if (locations.size() < guildIds.size()) {
            MessageUtil.sendConsoleMessage(new StringBuilder().insert(0, "[PlayerGuild] 据点战场地出生点数量不足，公会数 ").append(guildIds.size()).append("，出生点 ").append(locations.size()).append("，将跳过缺失的出生点，请检查场地配置。").toString());
        }
        int count = Math.min(guildIds.size(), locations.size());
        for (int index = 0; index < count; ++index) {
            spawnLocationMap.put(guildIds.get(index), locations.get(index));
        }
        return spawnLocationMap;
    }

    private static void startRoom(@NotNull StrongholdPvpRoom room) {
        if (!GuildPvpRoomStatusEnum.PREPARING.equals((Object)room.getStatus())) {
            return;
        }
        room.setStartTime(new Date());
        HashMap<UUID, StrongholdPvpPlayer> playerMap = new HashMap<UUID, StrongholdPvpPlayer>();
        Map<Integer, Color> colorMap = SeasonPvpUtil.getColorMap();
        for (int index = 0; index < room.getGuildIdList().size(); ++index) {
            Integer guildId = room.getGuildIdList().get(index);
            Color color = colorMap.get(index);
            if (color == null) {
                color = Color.RED;
            }
            StrongholdPvpManager.pPPpPP(room, guildId, room.getGuildLocationMap().get(guildId), color, playerMap);
        }
        room.setPlayerMap(playerMap);
        for (UUID playerUuid : playerMap.keySet()) {
            StrongholdPvpConstants.PLAYER_ROOM_MAP.put(playerUuid, room.getLocationId());
        }
        room.setStatus(GuildPvpRoomStatusEnum.RUNNING);
        room.setStartSecond(1);
        String startMessage = ConfigUtil.ACTIVITY_CONFIG.getString("strongholdPvp.startAllMsg", "");
        startMessage = startMessage.replace("${guilds}", StrongholdPvpManager.J(room));
        MessageUtil.sendAllMessage(startMessage);
    }

    private static int getRespawnDelay(int baseSecond, int maxSecond, int deathCount) {
        int delay = baseSecond;
        for (int step = 1; step < deathCount; ++step) {
            if (delay >= maxSecond / 2) {
                return maxSecond;
            }
            delay *= 2;
        }
        return Math.min(delay, maxSecond);
    }

    private StrongholdPvpManager() {
    }

    private static void tickPrepare(@NotNull StrongholdPvpRoom room, int prepareSecond) {
        if (!GuildPvpRoomStatusEnum.PREPARING.equals((Object)room.getStatus())) {
            return;
        }
        if (StrongholdPvpConstants.PREPARE_NOTICE_SECONDS.contains(prepareSecond)) {
            StrongholdPvpManager.J(room, 31 - prepareSecond);
        }
        if (StrongholdPvpConstants.COUNT_DOWN_SECONDS.contains(prepareSecond)) {
            GuildPvpMsgUtil.sendPrepareTitle(room.getPreparationPlayerMap(), 31 - prepareSecond);
        }
        if (prepareSecond > 31) {
            StrongholdPvpManager.startRoom(room);
        }
    }

    private static void F(@NotNull StrongholdPvpRoom room) {
        String message = ConfigUtil.ACTIVITY_CONFIG.getString("strongholdPvp.noPlayer");
        for (List<GuildPlayer> guildPlayers : room.getGuildPlayerMap().values()) {
            for (GuildPlayer guildPlayer : guildPlayers) {
                MessageUtil.sendMessage(guildPlayer.getPlayerUuid(), message);
            }
        }
        StrongholdPvpManager.removeRoom(room);
    }

    private static void respawnPlayer(@NotNull Player player) {
        HandySchedulerUtil.runTaskLater(() -> {
            if (player.isOnline() && player.isDead()) {
                player.spigot().respawn();
            }
        }, 1L);
    }

    @NotNull
    private static String resolveGuildName(@NotNull StrongholdPvpRoom room, @NotNull Integer guildId) {
        Integer targetGuildId = guildId;
        return room.getGuildNameMap().getOrDefault(targetGuildId, String.valueOf(targetGuildId));
    }

    private static void tickPrepareRooms() {
        Iterator<StrongholdPvpRoom> roomIterator;
        Iterator<StrongholdPvpRoom> loopIterator = roomIterator = new ArrayList<StrongholdPvpRoom>(StrongholdPvpConstants.ROOM_MAP.values()).iterator();
        while (loopIterator.hasNext()) {
            StrongholdPvpRoom room = roomIterator.next();
            if (!GuildPvpRoomStatusEnum.PREPARING.equals((Object)room.getStatus())) {
                loopIterator = roomIterator;
                continue;
            }
            StrongholdPvpRoom preparingRoom = room;
            int prepareSecond = preparingRoom.getPrepareSecond() + 1;
            preparingRoom.setPrepareSecond(prepareSecond);
            HandySchedulerUtil.runTask(() -> StrongholdPvpManager.tickPrepare(preparingRoom, prepareSecond));
            loopIterator = roomIterator;
        }
    }

    @NotNull
    public static synchronized List<UUID> getPreparationPlayers(@NotNull Integer guildId) {
        StrongholdPvpRoom room = StrongholdPvpManager.getRoomByGuildId(guildId);
        if (room == null) {
            return new ArrayList<UUID>();
        }
        return new ArrayList<UUID>(room.getPreparationPlayerMap().getOrDefault(guildId, new ArrayList()));
    }

    private static void sendMatchSuccessMessage(@NotNull StrongholdPvpRoom room) {
        String message = ConfigUtil.ACTIVITY_CONFIG.getString("strongholdPvp.matchSuccessMsg");
        for (List<GuildPlayer> guildPlayers : room.getGuildPlayerMap().values()) {
            for (GuildPlayer guildPlayer : guildPlayers) {
                MessageUtil.sendMessage(guildPlayer.getPlayerUuid(), message);
            }
        }
    }

    @Nullable
    private static Integer getWinnerGuildId(@NotNull StrongholdPvpRoom room) {
        Iterator<Integer> guildIdIterator;
        Integer winnerGuildId = null;
        int maxScore = Integer.MIN_VALUE;
        boolean tied = false;
        Iterator<Integer> loopIterator = guildIdIterator = room.getGuildIdList().iterator();
        while (loopIterator.hasNext()) {
            Integer guildId = guildIdIterator.next();
            int score = room.getScoreMap().getOrDefault(guildId, 0);
            if (score < 4) {
                loopIterator = guildIdIterator;
                continue;
            }
            if (score == 4) {
                tied = true;
                loopIterator = guildIdIterator;
                continue;
            }
            winnerGuildId = guildId;
            maxScore = score;
            tied = false;
            loopIterator = guildIdIterator;
        }
        if (tied) {
            return null;
        }
        return winnerGuildId;
    }

    private static void restorePlayers(@NotNull StrongholdPvpRoom room) {
        for (StrongholdPvpPlayer roomPlayer : room.getPlayerMap().values()) {
            Optional<Player> optional = BaseUtil.getOnlinePlayer(roomPlayer.getPlayerUuid());
            if (!optional.isPresent()) {
                continue;
            }
            Player player = optional.get();
            StrongholdPvpManager.restoreGameMode(player, roomPlayer);
            GuildPvpBuffUtil.restorePlayerBuff(roomPlayer.getPlayerUuid());
            if (player.isDead()) continue;
            EntitySchedulerUtil.teleport((Entity)player, roomPlayer.getOriginLocation());
        }
    }

    @Nullable
    public static synchronized GuildPvpMatchQueue pPpppP(@NotNull Integer guildId) {
        return StrongholdPvpConstants.QUEUE_MAP.get(guildId);
    }

    private static class AvailableRoom {
        Integer locationId;
        GuildPvpLocation pvpLocation;
        List<Location> spawnLocations;
        List<StrongholdPvpPoint> points;
    }
}
