package cn.handyplus.guild.core.season;

import cn.handyplus.guild.constants.GuildActivityType;
import cn.handyplus.guild.constants.GuildPvpRoomStatusEnum;
import cn.handyplus.guild.core.common.GuildPvpBuffUtil;
import cn.handyplus.guild.core.common.GuildPvpLogUtil;
import cn.handyplus.guild.core.common.GuildPvpMsgUtil;
import cn.handyplus.guild.core.common.GuildPvpTpUtil;
import cn.handyplus.guild.core.season.SeasonPvpConstants;
import cn.handyplus.guild.core.season.SeasonPvpPlayer;
import cn.handyplus.guild.core.season.SeasonPvpRoom;
import cn.handyplus.guild.core.season.SeasonPvpUtil;
import cn.handyplus.guild.enter.GuildInfo;
import cn.handyplus.guild.enter.GuildPlayer;
import cn.handyplus.guild.enter.GuildPvp;
import cn.handyplus.guild.enter.GuildPvpLocation;
import cn.handyplus.guild.enter.GuildSpawnPvpLocation;
import cn.handyplus.guild.lib.BaseUtil;
import cn.handyplus.guild.lib.CollUtil;
import cn.handyplus.guild.lib.HandyRunnable;
import cn.handyplus.guild.lib.HandySchedulerUtil;
import cn.handyplus.guild.lib.MapUtil;
import cn.handyplus.guild.lib.MessageUtil;
import cn.handyplus.guild.lib.PlayerSchedulerUtil;
import cn.handyplus.guild.lib.RgbTextUtil;
import cn.handyplus.guild.lib.StrUtil;
import cn.handyplus.guild.service.GuildInfoService;
import cn.handyplus.guild.service.GuildPlayerService;
import cn.handyplus.guild.service.GuildPvpLocationService;
import cn.handyplus.guild.service.GuildPvpService;
import cn.handyplus.guild.service.GuildSpawnPvpLocationService;
import cn.handyplus.guild.util.ConfigUtil;
import cn.handyplus.guild.util.GuildCacheUtil;
import cn.handyplus.guild.util.GuildUtil;
import cn.handyplus.guild.util.KitPvpUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SeasonPvpManager {
    private volatile static SeasonPvpRoom currentRoom;

    private static void finishRankedGuild(SeasonPvpRoom room, Integer guildId, List players, Integer season) {
        if (currentRoom != room) {
            return;
        }
        SeasonPvpManager.recordAndReward(guildId, 1, players, season);
    }

    private static void rewardGuild(@NotNull Integer guildId, @NotNull Integer rank, @NotNull List<SeasonPvpPlayer> players) {
        Optional<GuildInfo> optional = GuildInfoService.getInstance().findById(guildId);
        if (!optional.isPresent()) {
            return;
        }
        GuildInfo guildInfo = optional.get();
        String rankKey = rank <= 3 ? rank.toString() : "all";
        int guildMoney = ConfigUtil.ACTIVITY_CONFIG.getInt(new StringBuilder().insert(0, "season.reward.").append(rankKey).append(".guildMoney").toString());
        int guildActive = ConfigUtil.ACTIVITY_CONFIG.getInt(new StringBuilder().insert(0, "season.reward.").append(rankKey).append(".guildActive").toString());
        int playerMoney = ConfigUtil.ACTIVITY_CONFIG.getInt(new StringBuilder().insert(0, "season.reward.").append(rankKey).append(".playerMoney").toString());
        List<String> commands = ConfigUtil.ACTIVITY_CONFIG.getStringList(new StringBuilder().insert(0, "season.reward.").append(rankKey).append(".command").toString());
        MessageUtil.sendAllMessage(ConfigUtil.ACTIVITY_CONFIG.getString("season.endMsg", "${win}").replace("${win}", guildInfo.getGuildName()).replace("${rank}", rank.toString()).replace("${guildMoney}", String.valueOf(guildMoney)).replace("${guildActive}", String.valueOf(guildActive)).replace("${playerMoney}", String.valueOf(playerMoney)));
        if (playerMoney != 0 && CollUtil.isNotEmpty(players)) {
            List<UUID> playerUuids = players.stream().map(SeasonPvpPlayer::getPlayerUuid).collect(Collectors.toList());
            GuildPlayerService.getInstance().addMoneyByPlayerUuids(playerUuids, playerMoney);
        }
        for (SeasonPvpPlayer seasonPvpPlayer : players) {
            if (CollUtil.isEmpty(commands)) {
                continue;
            }
            for (String command : commands) {
                if (StrUtil.isEmpty(command)) {
                    continue;
                }
                command = StrUtil.replace(command, "${player}", seasonPvpPlayer.getPlayerName());
                PlayerSchedulerUtil.dispatchCommand(command);
            }
        }
        GuildInfoService.getInstance().addMoneyById(guildId, guildMoney);
        GuildInfoService.getInstance().addProsperityDegreeById(guildId, guildActive);
        GuildCacheUtil.refresh(guildId);
    }

    @Nullable
    public static synchronized SeasonPvpRoom OOooOo() {
        return currentRoom;
    }

    private static void initStartPlayers() {
        for (Integer guildId : currentRoom.getGuildIdList()) {
            List<UUID> preparationPlayers = currentRoom.getPreparationPlayerMap().getOrDefault(guildId, new ArrayList());
            ArrayList<SeasonPvpPlayer> startPlayers = new ArrayList<SeasonPvpPlayer>();
            for (UUID playerUuid : preparationPlayers) {
                Optional<Player> optional = BaseUtil.getOnlinePlayer(playerUuid);
                if (!optional.isPresent()) {
                    continue;
                }
                Player player = optional.get();
                if (player.isDead()) {
                    continue;
                }
                SeasonPvpPlayer seasonPvpPlayer = new SeasonPvpPlayer();
                seasonPvpPlayer.setMeGuildId(guildId);
                seasonPvpPlayer.setPlayerName(player.getName());
                seasonPvpPlayer.setPlayerUuid(playerUuid);
                seasonPvpPlayer.setKill(1);
                seasonPvpPlayer.setSeason(currentRoom.getSeason());
                seasonPvpPlayer.setIsDeath(true);
                startPlayers.add(seasonPvpPlayer);
                currentRoom.getOriginLocationMap().put(playerUuid, player.getLocation());
            }
            if (!CollUtil.isNotEmpty(startPlayers)) continue;
            currentRoom.getStartPlayerMap().put(guildId, startPlayers);
        }
        currentRoom.getPreparationPlayerMap().clear();
    }

    public static synchronized void handleDeath(@NotNull Player player) {
        if (currentRoom == null || !GuildPvpRoomStatusEnum.RUNNING.equals((Object)currentRoom.getStatus())) {
            return;
        }
        Integer guildId = currentRoom.getStartGuildPlayerMap().get(player.getUniqueId());
        if (guildId == null) {
            return;
        }
        List<SeasonPvpPlayer> startPlayers = currentRoom.getStartPlayerMap().get(guildId);
        if (CollUtil.isEmpty(startPlayers)) {
            return;
        }
        int deathCount = 0;
        for (SeasonPvpPlayer seasonPvpPlayer : startPlayers) {
            if (seasonPvpPlayer.getPlayerUuid().equals(player.getUniqueId())) {
                seasonPvpPlayer.setIsDeath(true);
            }
            if (!Boolean.TRUE.equals(seasonPvpPlayer.getIsDeath())) continue;
            ++deathCount;
        }
        if (startPlayers.size() == deathCount) {
            int remainingGuildCount = currentRoom.getStartPlayerMap().size();
            currentRoom.getStartPlayerMap().remove(guildId);
            SeasonPvpManager.recordAndReward(guildId, remainingGuildCount, startPlayers, currentRoom.getSeason());
        }
    }

    private static synchronized void F(@NotNull List<GuildPlayer> players, int remainSecond) {
        if (currentRoom == null || !GuildPvpRoomStatusEnum.PREPARING.equals((Object)currentRoom.getStatus())) {
            return;
        }
        for (GuildPlayer guildPlayer : players) {
            Optional<Player> optional = BaseUtil.getOnlinePlayer(guildPlayer.getPlayerUuid());
            if (!optional.isPresent()) {
                continue;
            }
            Player player = optional.get();
            if (currentRoom.getPreparationPlayerMap().getOrDefault(guildPlayer.getGuildInfoId(), new ArrayList()).contains(player.getUniqueId())) {
                continue;
            }
            String prepareMessage = ConfigUtil.ACTIVITY_CONFIG.getString("season.seasonPvpPrepareMsg", "${time}").replace("${time}", String.valueOf(remainSecond));
            MessageUtil.sendMessage(player, prepareMessage);
            String acceptText = ConfigUtil.ACTIVITY_CONFIG.getString("matePvpPrepareAccept");
            String refuseText = ConfigUtil.ACTIVITY_CONFIG.getString("matePvpPrepareRefuse");
            RgbTextUtil.init(acceptText).addClickCommand("/plg acceptPvp").addExtra(RgbTextUtil.init(refuseText).addClickCommand("/plg denyPvp")).send((CommandSender)player);
        }
    }

    private static void assignSpawnLocations(@NotNull Map<Integer, GuildSpawnPvpLocation> spawnLocations) {
        int spawnIndex = 0;
        int colorIndex = 0;
        Map<Integer, Color> colorMap = SeasonPvpUtil.getColorMap();
        for (Integer guildId : currentRoom.getStartPlayerMap().keySet()) {
            GuildSpawnPvpLocation spawnLocation = spawnLocations.get(spawnIndex);
            ++spawnIndex;
            if (spawnLocation == null) {
                spawnIndex = 0;
                spawnLocation = spawnLocations.get(++spawnIndex);
            }
            Location location = GuildUtil.getLocation(spawnLocation);
            List<SeasonPvpPlayer> startPlayers = currentRoom.getStartPlayerMap().get(guildId);
            Color color = colorMap.get(colorIndex);
            ++colorIndex;
            Color fallbackColor = color == null ? Color.RED : color;
            for (SeasonPvpPlayer seasonPvpPlayer : startPlayers) {
                Optional<Player> optional = BaseUtil.getOnlinePlayer(seasonPvpPlayer.getPlayerUuid());
                if (!optional.isPresent()) {
                    continue;
                }
                KitPvpUtil.startPvp(optional.get(), location, fallbackColor, null);
                currentRoom.getStartGuildPlayerMap().put(seasonPvpPlayer.getPlayerUuid(), seasonPvpPlayer.getMeGuildId());
            }
        }
    }

    private static void finishSeason() {
        SeasonPvpRoom seasonPvpRoom = currentRoom;
        if (seasonPvpRoom == null) {
            return;
        }
        seasonPvpRoom.setStatus(GuildPvpRoomStatusEnum.FINISHED);
        HandySchedulerUtil.runTaskAsynchronously(() -> SeasonPvpManager.restoreBuffs(new HashMap<UUID, Integer>(seasonPvpRoom.getStartGuildPlayerMap())));
        for (UUID playerUuid : seasonPvpRoom.getOriginLocationMap().keySet()) {
            Optional<Player> optional = BaseUtil.getOnlinePlayer(playerUuid);
            if (!optional.isPresent()) {
                continue;
            }
            Player player = optional.get();
            Location location = seasonPvpRoom.getOriginLocationMap().get(playerUuid);
            GuildPvpTpUtil.tp(seasonPvpRoom.getLookLocation(), location, player);
        }
        SeasonPvpManager.resetSeasonData();
        SeasonPvpManager.currentRoom = null;
    }

    private static void createRoom(@NotNull List<Integer> guildIds, @NotNull Integer season, @NotNull GuildPvpLocation location) {
        SeasonPvpRoom seasonPvpRoom = new SeasonPvpRoom();
        seasonPvpRoom.setSeason(season);
        seasonPvpRoom.setGuildIdList(new ArrayList<Integer>(guildIds));
        seasonPvpRoom.setStatus(GuildPvpRoomStatusEnum.PREPARING);
        seasonPvpRoom.setGuildPvpLocation(location);
        seasonPvpRoom.setPreparationPlayerMap(MapUtil.of());
        seasonPvpRoom.setStartPlayerMap(MapUtil.of());
        seasonPvpRoom.setStartGuildPlayerMap(MapUtil.of());
        seasonPvpRoom.setOriginLocationMap(MapUtil.of());
        for (Integer guildId : guildIds) {
            seasonPvpRoom.getPreparationPlayerMap().put(guildId, new ArrayList());
        }
        currentRoom = seasonPvpRoom;
        List<GuildPlayer> onlinePlayers = GuildPlayerService.getInstance().findByGuildInfoIds(guildIds);
        HandyRunnable task = new HandyRunnable(){
            private int elapsedSecond;

            @Override
            public void run() {
                if (currentRoom != seasonPvpRoom) {
                    this.cancel();
                    return;
                }
                ++this.elapsedSecond;
                SeasonPvpManager.tickPreparation(onlinePlayers, this.elapsedSecond);
                if (this.elapsedSecond >= 60) {
                    HandySchedulerUtil.runTask(() -> SeasonPvpManager.startRoom());
                    this.cancel();
                }
            }
        };
        HandySchedulerUtil.runTaskTimerAsynchronously(task, 1L, 20L);
    }

    private static void tickPreparation(@NotNull List<GuildPlayer> players, int elapsedSecond) {
        SeasonPvpRoom seasonPvpRoom = currentRoom;
        if (seasonPvpRoom == null || !GuildPvpRoomStatusEnum.PREPARING.equals((Object)seasonPvpRoom.getStatus())) {
            return;
        }
        int remainSecond = 60 - elapsedSecond;
        if (SeasonPvpConstants.zGTXUt.contains(elapsedSecond)) {
            SeasonPvpManager.F(players, remainSecond);
        }
        if (SeasonPvpConstants.F.contains(elapsedSecond)) {
            GuildPvpMsgUtil.sendPrepareTitle(seasonPvpRoom.getPreparationPlayerMap(), remainSecond);
        }
    }

    public static synchronized boolean acceptPreparation(@NotNull Integer guildId, @NotNull UUID playerUuid) {
        if (currentRoom == null || !GuildPvpRoomStatusEnum.PREPARING.equals((Object)currentRoom.getStatus())) {
            return false;
        }
        List<UUID> preparationPlayers = currentRoom.getPreparationPlayerMap().get(guildId);
        if (preparationPlayers == null || preparationPlayers.contains(playerUuid)) {
            return false;
        }
        preparationPlayers.add(playerUuid);
        return true;
    }

    public static synchronized void startSeason() {
        if (currentRoom != null && !GuildPvpRoomStatusEnum.FINISHED.equals((Object)currentRoom.getStatus())) {
            return;
        }
        List<GuildPvp> guildPvps = GuildPvpService.getInstance().findBySeason();
        if (CollUtil.isEmpty(guildPvps)) {
            MessageUtil.sendAllMessage(ConfigUtil.ACTIVITY_CONFIG.getString("seasonNoMsg"));
            return;
        }
        Date startDate = new Date();
        ArrayList<Integer> guildIds = new ArrayList<Integer>(guildPvps.size());
        for (GuildPvp guildPvp : guildPvps) {
            guildPvp.setIsStart(true);
            guildPvp.setStartTime(startDate);
            guildIds.add(guildPvp.getId());
        }
        GuildPvpService.getInstance().batchUpdateStart(guildIds, startDate);
        if (guildPvps.size() < 2) {
            MessageUtil.sendAllMessage(ConfigUtil.ACTIVITY_CONFIG.getString("seasonNoMsg"));
            return;
        }
        List<Integer> guildInfoIds = guildPvps.stream().map(GuildPvp::getGuildInfoId).collect(Collectors.toList());
        List<GuildInfo> guildInfos = GuildInfoService.getInstance().findByIds(guildInfoIds);
        List<String> guildNames = guildInfos.stream().map(GuildInfo::getGuildName).collect(Collectors.toList());
        Integer season = guildPvps.get(0).getSeason();
        MessageUtil.sendConsoleMessage(new StringBuilder().insert(0, "第").append(season).append("赛季公会战开始, 参赛公会:").append(CollUtil.listToStr(guildNames)).toString());
        List<GuildPvpLocation> locations = GuildPvpLocationService.getInstance().findByType(GuildActivityType.SEASON.getType());
        if (CollUtil.isEmpty(locations)) {
            MessageUtil.sendAllMessage(ConfigUtil.PVP_LOCATION_CONFIG.getString("noSeasonLocation"));
            return;
        }
        GuildPvpLocation location = locations.get(ThreadLocalRandom.current().nextInt(locations.size()));
        SeasonPvpManager.createRoom(guildInfoIds, season, location);
    }

    public static synchronized void denyPreparation(@NotNull Integer guildId, @NotNull UUID playerUuid) {
        if (currentRoom == null || !GuildPvpRoomStatusEnum.PREPARING.equals((Object)currentRoom.getStatus())) {
            return;
        }
        List<UUID> preparationPlayers = currentRoom.getPreparationPlayerMap().get(guildId);
        if (CollUtil.isEmpty(preparationPlayers)) {
            return;
        }
        preparationPlayers.remove(playerUuid);
    }

    @NotNull
    private static Map<Integer, GuildSpawnPvpLocation> loadSpawnLocations(@NotNull GuildPvpLocation location) {
        List<GuildSpawnPvpLocation> spawns = GuildSpawnPvpLocationService.getInstance().list(location.getId());
        String lookSpawnName = ConfigUtil.ACTIVITY_CONFIG.getString("season.lookLocation");
        for (GuildSpawnPvpLocation spawn : spawns) {
            if (!spawn.getSpawnName().equalsIgnoreCase(lookSpawnName)) continue;
            currentRoom.setLookLocation(GuildUtil.getLocation(spawn));
            break;
        }
        List<GuildSpawnPvpLocation> otherSpawns = spawns.stream().filter(item -> !item.getSpawnName().equalsIgnoreCase(lookSpawnName)).collect(Collectors.toList());
        HashMap<Integer, GuildSpawnPvpLocation> spawnByIndex = MapUtil.newHashMapWithExpectedSize(otherSpawns.size());
        for (int index = 0; index < otherSpawns.size(); ++index) {
            spawnByIndex.put(index, otherSpawns.get(index));
        }
        return spawnByIndex;
    }

    private static synchronized void recordAndReward(@NotNull Integer guildId, @NotNull Integer rank, @NotNull List<SeasonPvpPlayer> players, @NotNull Integer season) {
        if (currentRoom == null) {
            return;
        }
        Integer rankValue = rank;
        GuildPvpLogUtil.asyncRecordPvpLog(guildId, rankValue, season, currentRoom.getStartTime(), players);
        SeasonPvpManager.rewardGuild(guildId, rankValue, players);
        if (rank == 1) {
            SeasonPvpManager.finishSeason();
            return;
        }
        if (rank == 2) {
            SeasonPvpRoom seasonPvpRoom = currentRoom;
            for (Integer otherGuildId : new ArrayList<Integer>(seasonPvpRoom.getStartPlayerMap().keySet())) {
                List<SeasonPvpPlayer> rankedPlayers = seasonPvpRoom.getStartPlayerMap().get(otherGuildId);
                HandySchedulerUtil.runTaskLater(() -> SeasonPvpManager.finishRankedGuild(seasonPvpRoom, otherGuildId, rankedPlayers, season), 20L);
            }
        }
    }

    private static synchronized void startRoom() {
        if (currentRoom == null || !GuildPvpRoomStatusEnum.PREPARING.equals((Object)currentRoom.getStatus())) {
            return;
        }
        currentRoom.setStatus(GuildPvpRoomStatusEnum.STARTING);
        GuildPvpLocation location = currentRoom.getGuildPvpLocation();
        Map<String, String> placeholders = MapUtil.of("${season}", String.valueOf(currentRoom.getSeason()), "${location}", location.getName());
        MessageUtil.sendConsoleMessage(BaseUtil.getLangMsg("seasonPvpLocationMsg", placeholders));
        Map<Integer, GuildSpawnPvpLocation> spawnLocations = SeasonPvpManager.loadSpawnLocations(location);
        if (MapUtil.isEmpty(spawnLocations)) {
            SeasonPvpManager.currentRoom = null;
            return;
        }
        SeasonPvpManager.initStartPlayers();
        if (currentRoom.getStartPlayerMap().size() < 2) {
            MessageUtil.sendAllMessage(ConfigUtil.ACTIVITY_CONFIG.getString("season.noPlayer"));
            SeasonPvpManager.currentRoom = null;
            return;
        }
        currentRoom.setStartTime(new Date());
        currentRoom.setStatus(GuildPvpRoomStatusEnum.RUNNING);
        SeasonPvpManager.assignSpawnLocations(spawnLocations);
    }

    private static void resetSeasonData() {
        HandySchedulerUtil.runTaskAsynchronously(() -> {
            List<GuildInfo> guildInfos = GuildInfoService.getInstance().findAll();
            if (CollUtil.isEmpty(guildInfos)) {
                return;
            }
            Integer season = GuildPvpService.getInstance().findSeason();
            ArrayList<GuildPvp> guildPvps = new ArrayList<GuildPvp>(guildInfos.size());
            for (GuildInfo guildInfo : guildInfos) {
                GuildPvp guildPvp = new GuildPvp();
                guildPvp.setSeason(season);
                guildPvp.setGuildInfoId(guildInfo.getId());
                guildPvp.setIsJoin(true);
                guildPvp.setIsStart(true);
                guildPvps.add(guildPvp);
            }
            GuildPvpService.getInstance().batchAdd(guildPvps);
        });
    }

    private SeasonPvpManager() {
    }

    public static synchronized boolean forceStop() {
        if (currentRoom == null || GuildPvpRoomStatusEnum.FINISHED.equals((Object)currentRoom.getStatus())) {
            return false;
        }
        SeasonPvpManager.finishSeason();
        return true;
    }

    private static void restoreBuffs(Map<UUID, Integer> playerGuilds) {
        playerGuilds.keySet().forEach(GuildPvpBuffUtil::restorePlayerBuff);
    }
}
