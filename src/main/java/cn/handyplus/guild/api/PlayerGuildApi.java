package cn.handyplus.guild.api;

import cn.handyplus.guild.command.admin.RewardCommand;
import cn.handyplus.guild.command.function.SignInCommand;
import cn.handyplus.guild.constants.ApplyTypeEnum;
import cn.handyplus.guild.constants.GuildConstants;
import cn.handyplus.guild.constants.GuildRoleEnum;
import cn.handyplus.guild.core.match.MatchPvpUtil;
import cn.handyplus.guild.core.season.SeasonPvpPlayer;
import cn.handyplus.guild.core.season.SeasonPvpUtil;
import cn.handyplus.guild.core.stronghold.StrongholdPvpUtil;
import cn.handyplus.guild.enter.GuildApply;
import cn.handyplus.guild.enter.GuildInfo;
import cn.handyplus.guild.enter.GuildNotice;
import cn.handyplus.guild.enter.GuildNoticeRead;
import cn.handyplus.guild.enter.GuildPlayer;
import cn.handyplus.guild.enter.GuildShop;
import cn.handyplus.guild.enter.GuildTask;
import cn.handyplus.guild.enter.GuildTaskDemand;
import cn.handyplus.guild.lib.CollUtil;
import cn.handyplus.guild.lib.MessageUtil;
import cn.handyplus.guild.param.GuildPvpPlayerParam;
import cn.handyplus.guild.service.GuildApplyService;
import cn.handyplus.guild.service.GuildInfoService;
import cn.handyplus.guild.service.GuildNoticeReadService;
import cn.handyplus.guild.service.GuildNoticeService;
import cn.handyplus.guild.service.GuildPlayerService;
import cn.handyplus.guild.service.GuildShopService;
import cn.handyplus.guild.service.GuildTaskDemandService;
import cn.handyplus.guild.service.GuildTaskService;
import cn.handyplus.guild.util.ConfigUtil;
import cn.handyplus.guild.util.NoticeUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlayerGuildApi {
    private PlayerGuildApi() {
    }

    public static boolean giveGuildMoneyByName(@NotNull String guildName, int amount) {
        return GuildInfoService.getInstance().addMoneyByName(guildName, amount);
    }

    public static boolean takeGuildMoneyByName(@NotNull String guildName, int amount) {
        return GuildInfoService.getInstance().subtractMoneyByName(guildName, amount);
    }

    public static boolean setGuildMoneyByName(@NotNull String guildName, int amount) {
        return GuildInfoService.getInstance().setMoneyByName(guildName, amount);
    }

    public static boolean giveGuildProsperityDegreeByName(@NotNull String guildName, int amount) {
        return GuildInfoService.getInstance().addProsperityDegreeByName(guildName, amount);
    }

    public static boolean takeGuildProsperityDegreeByName(@NotNull String guildName, int amount) {
        return GuildInfoService.getInstance().subtractProsperityDegreeByName(guildName, amount);
    }

    public static boolean setGuildProsperityDegreeByName(@NotNull String guildName, int amount) {
        return GuildInfoService.getInstance().setProsperityDegreeByName(guildName, amount);
    }

    public static boolean giveGuildOreByName(@NotNull String guildName, int amount) {
        return GuildInfoService.getInstance().addOreByName(guildName, amount);
    }

    public static boolean takeGuildOreByName(@NotNull String guildName, int amount) {
        return GuildInfoService.getInstance().subtractOreByName(guildName, amount);
    }

    public static boolean setGuildOreByName(@NotNull String guildName, int amount) {
        return GuildInfoService.getInstance().setOreByName(guildName, amount);
    }

    public static boolean givePlayerMoney(@NotNull UUID playerUuid, int amount) {
        return GuildPlayerService.getInstance().addMoneyByPlayerUuid(playerUuid, amount);
    }

    public static boolean takePlayerMoney(@NotNull UUID playerUuid, int amount) {
        return GuildPlayerService.getInstance().subtractMoneyByPlayerUuid(playerUuid, amount);
    }

    public static boolean setPlayerMoney(@NotNull UUID playerUuid, int amount) {
        return GuildPlayerService.getInstance().setMoneyByPlayerUuid(playerUuid, amount);
    }

    public static String getPlayerGuildName(@NotNull UUID playerUuid) {
        Optional<GuildPlayer> guildPlayerOptional = GuildPlayerService.getInstance().findByPlayerUuid(playerUuid);
        return guildPlayerOptional.map(GuildPlayer::getGuildInfoName).orElse(null);
    }

    public static Integer getPlayerMoney(@NotNull UUID playerUuid) {
        Optional<GuildPlayer> guildPlayerOptional = GuildPlayerService.getInstance().findByPlayerUuid(playerUuid);
        return guildPlayerOptional.isPresent() ? guildPlayerOptional.get().getMoney() : 0;
    }

    public static Integer getPlayerGuildMoney(@NotNull UUID playerUuid) {
        Optional<GuildPlayer> guildPlayerOptional = GuildPlayerService.getInstance().findByPlayerUuid(playerUuid);
        return guildPlayerOptional.isPresent() ? guildPlayerOptional.get().getGuildMoney() : 0;
    }

    public static List<String> getPlayerGuildMember(@NotNull UUID playerUuid) {
        Optional<GuildPlayer> guildPlayerOptional = GuildPlayerService.getInstance().findByPlayerUuid(playerUuid);
        if (!guildPlayerOptional.isPresent()) {
            return new ArrayList<String>();
        }
        GuildPlayer guildPlayer = guildPlayerOptional.get();
        List<GuildPlayer> guildPlayers = GuildPlayerService.getInstance().findByGuildId(guildPlayer.getGuildInfoId());
        return guildPlayers.stream().map(GuildPlayer::getPlayerName).collect(Collectors.toList());
    }

    public static String getPlayerGuildRole(@NotNull UUID playerUuid) {
        Optional<GuildPlayer> guildPlayerOptional = GuildPlayerService.getInstance().findByPlayerUuid(playerUuid);
        return guildPlayerOptional.map(guildPlayer -> GuildRoleEnum.getRoleName(guildPlayer.getRoleId())).orElse(null);
    }

    public static GuildRoleEnum getPlayerGuildRoleEnum(@NotNull UUID playerUuid) {
        Optional<GuildPlayer> guildPlayerOptional = GuildPlayerService.getInstance().findByPlayerUuid(playerUuid);
        return guildPlayerOptional.map(guildPlayer -> GuildRoleEnum.getEnumThrow(guildPlayer.getRoleId())).orElse(null);
    }

    public static Integer getPlayerKill(@NotNull UUID playerUuid) {
        Optional<GuildPlayer> guildPlayerOptional = GuildPlayerService.getInstance().findByPlayerUuid(playerUuid);
        return guildPlayerOptional.map(GuildPlayer::getKill).orElse(0);
    }

    public static Integer getPlayerDie(@NotNull UUID playerUuid) {
        Optional<GuildPlayer> guildPlayerOptional = GuildPlayerService.getInstance().findByPlayerUuid(playerUuid);
        return guildPlayerOptional.map(GuildPlayer::getDie).orElse(0);
    }

    public static boolean isPvp(@NotNull Player player) {
        if (PlayerGuildApi.matchStart(player.getUniqueId())) {
            return true;
        }
        if (PlayerGuildApi.matchPreparation(player.getUniqueId())) {
            return true;
        }
        if (PlayerGuildApi.seasonPreparation(player.getUniqueId())) {
            return true;
        }
        if (StrongholdPvpUtil.isPlaying(player.getUniqueId())) {
            return true;
        }
        return PlayerGuildApi.seasonStart(player.getUniqueId());
    }

    public static void closePvp(@NotNull UUID playerUuid) {
        GuildConstants.PLAYER_API_PVP_MAP.put(playerUuid, true);
    }

    public static void openPvp(@NotNull UUID playerUuid) {
        GuildConstants.PLAYER_API_PVP_MAP.remove(playerUuid);
    }

    public static List<UUID> getMatchStartPlayer(@NotNull Integer guildId) {
        List<GuildPvpPlayerParam> players = MatchPvpUtil.getStartPlayers(guildId);
        if (CollUtil.isEmpty(players)) {
            return new ArrayList<UUID>();
        }
        return players.stream().map(GuildPvpPlayerParam::getPlayerUuid).collect(Collectors.toList());
    }

    public static List<UUID> getSeasonStartPlayer(@NotNull Integer guildId) {
        List<SeasonPvpPlayer> players = SeasonPvpUtil.getStartPlayers(guildId);
        if (CollUtil.isEmpty(players)) {
            return new ArrayList<UUID>();
        }
        return players.stream().map(SeasonPvpPlayer::getPlayerUuid).filter(SeasonPvpUtil::isRunning).collect(Collectors.toList());
    }

    public static List<String> getAllGuildName() {
        return PlayerGuildApi.getAllGuild().stream().map(GuildInfo::getGuildName).collect(Collectors.toList());
    }

    public static List<GuildInfo> getAllGuild() {
        return GuildInfoService.getInstance().findAll();
    }

    public static List<String> getGuildPlayer(@NotNull String guildName) {
        List<GuildPlayer> guildPlayers = GuildPlayerService.getInstance().findByGuildName(guildName);
        return guildPlayers.stream().map(GuildPlayer::getPlayerName).collect(Collectors.toList());
    }

    public static boolean existGuild(@NotNull UUID playerUuid) {
        return GuildPlayerService.getInstance().findByPlayerUuid(playerUuid).isPresent();
    }

    public static List<GuildPlayer> findPlayerByGuildId(@NotNull Integer guildId) {
        return GuildPlayerService.getInstance().findByGuildId(guildId);
    }

    public static List<GuildNotice> findNoticeByGuildId(@NotNull Integer guildId) {
        return GuildNoticeService.getInstance().list(guildId, null);
    }

    public static List<GuildShop> findShopByGuildId(@NotNull Integer guildId) {
        return GuildShopService.getInstance().list(guildId);
    }

    public static List<GuildTask> findTaskByGuildId(@NotNull Integer guildId) {
        return GuildTaskService.getInstance().list(guildId);
    }

    public static boolean changeGuildJoinMode(@NotNull UUID playerUuid, boolean joinMode) {
        Optional<GuildInfo> guildInfoOptional = PlayerGuildApi.findManageableGuildInfo(playerUuid);
        if (!guildInfoOptional.isPresent()) {
            return false;
        }
        GuildInfo guildInfo = guildInfoOptional.get();
        if (Boolean.valueOf(joinMode).equals(guildInfo.getJoinMode())) {
            return true;
        }
        int price = ConfigUtil.SETTING_CONFIG.getInt("joinMode.price");
        if (guildInfo.getMoney() < price) {
            return false;
        }
        GuildInfoService.getInstance().editJoinMode(guildInfo.getId(), joinMode, price);
        return true;
    }

    public static boolean changeGuildPvpStatus(@NotNull UUID playerUuid, boolean pvpStatus) {
        Optional<GuildInfo> guildInfoOptional = PlayerGuildApi.findManageableGuildInfo(playerUuid);
        if (!guildInfoOptional.isPresent()) {
            return false;
        }
        GuildInfo guildInfo = guildInfoOptional.get();
        if (Boolean.valueOf(pvpStatus).equals(guildInfo.getPvpStatus())) {
            return true;
        }
        int price = ConfigUtil.SETTING_CONFIG.getInt("pvp.price");
        if (guildInfo.getMoney() < price) {
            return false;
        }
        GuildInfoService.getInstance().editPlayerPvpStatus(guildInfo.getId(), pvpStatus, price);
        return true;
    }

    public static void publishGuildNotice(@NotNull Player player, @NotNull String noticeContent) {
        NoticeUtil.sendNotice(player, noticeContent);
    }

    public static boolean deleteGuildNotice(@NotNull UUID playerUuid, @NotNull Integer noticeId) {
        Optional<GuildPlayer> guildPlayerOptional = GuildPlayerService.getInstance().findByPlayerUuid(playerUuid);
        Optional<GuildNotice> guildNoticeOptional = GuildNoticeService.getInstance().findById(noticeId);
        if (!guildPlayerOptional.isPresent() || !guildNoticeOptional.isPresent()) {
            return false;
        }
        GuildPlayer guildPlayer = guildPlayerOptional.get();
        GuildNotice guildNotice = guildNoticeOptional.get();
        if (!GuildRoleEnum.ONE.getRoleId().equals(guildPlayer.getRoleId()) || !guildPlayer.getGuildInfoId().equals(guildNotice.getGuildInfoId())) {
            return false;
        }
        GuildNoticeService.getInstance().delById(noticeId);
        GuildNoticeReadService.getInstance().delByGuildNoticeId(noticeId);
        return true;
    }

    public static boolean markGuildNoticeRead(@NotNull UUID playerUuid, @NotNull Integer noticeId) {
        Optional<GuildPlayer> guildPlayerOptional = GuildPlayerService.getInstance().findByPlayerUuid(playerUuid);
        Optional<GuildNotice> guildNoticeOptional = GuildNoticeService.getInstance().findById(noticeId);
        if (!guildPlayerOptional.isPresent() || !guildNoticeOptional.isPresent()) {
            return false;
        }
        GuildPlayer guildPlayer = guildPlayerOptional.get();
        GuildNotice guildNotice = guildNoticeOptional.get();
        if (!guildPlayer.getGuildInfoId().equals(guildNotice.getGuildInfoId())) {
            return false;
        }
        List<Integer> readNoticeIds = GuildNoticeReadService.getInstance().findGuildNoticeId(guildPlayer.getGuildInfoId(), playerUuid);
        if (readNoticeIds.contains(noticeId)) {
            return true;
        }
        GuildNoticeRead guildNoticeRead = new GuildNoticeRead();
        guildNoticeRead.setGuildInfoId(guildPlayer.getGuildInfoId());
        guildNoticeRead.setGuildNoticeId(noticeId);
        guildNoticeRead.setReadPlayerName(guildPlayer.getPlayerName());
        guildNoticeRead.setReadPlayerUuid(playerUuid);
        guildNoticeRead.setReadTime(new Date());
        GuildNoticeReadService.getInstance().add(guildNoticeRead);
        return true;
    }

    public static List<GuildTask> findTaskProgressByGuildId(@NotNull Integer guildId) {
        List<GuildTask> tasks = GuildTaskService.getInstance().list(guildId);
        if (CollUtil.isEmpty(tasks)) {
            return tasks;
        }
        List<Integer> taskIds = tasks.stream().map(GuildTask::getId).collect(Collectors.toList());
        Map<Integer, List<GuildTaskDemand>> demandsByTaskId = GuildTaskDemandService.getInstance().findByGuildTaskIds(taskIds).stream().collect(Collectors.groupingBy(GuildTaskDemand::getGuildTaskId));
        for (GuildTask task : tasks) {
            task.setGuildTaskDemandList(demandsByTaskId.getOrDefault(task.getId(), Collections.emptyList()));
        }
        return tasks;
    }

    public static boolean claimGuildTaskReward(@NotNull UUID playerUuid, @NotNull Integer taskId) {
        Optional<GuildPlayer> guildPlayerOptional = GuildPlayerService.getInstance().findByPlayerUuid(playerUuid);
        Optional<GuildTask> guildTaskOptional = GuildTaskService.getInstance().findRewardTaskById(taskId);
        if (!guildPlayerOptional.isPresent() || !guildTaskOptional.isPresent()) {
            return false;
        }
        GuildPlayer guildPlayer = guildPlayerOptional.get();
        GuildTask guildTask = guildTaskOptional.get();
        if (!guildPlayer.getGuildInfoId().equals(guildTask.getGuildInfoId())) {
            return false;
        }
        return RewardCommand.rewardTask(Collections.singletonList(guildTask)) > 0;
    }

    public static List<GuildApply> findPendingApplyByGuildId(@NotNull Integer guildId) {
        return GuildApplyService.getInstance().findByGuildId(guildId, ApplyTypeEnum.PENDING.getId());
    }

    public static boolean signInPlayer(@NotNull Player player, @NotNull String signInType) {
        return SignInCommand.signIn((CommandSender)player, (OfflinePlayer)player, signInType);
    }

    public static boolean isPlayerSignedInToday(@NotNull UUID playerUuid) {
        return SignInCommand.isSignedIn(playerUuid);
    }

    public static void startGuildShopSell(@NotNull Player player) {
        GuildConstants.CREATE_GUILD_INFO_MAP.put(player.getUniqueId(), "sell");
        MessageUtil.sendMessage(player, ConfigUtil.SHOP_CONFIG.getString("sellMsg"));
    }

    private static Optional<GuildInfo> findManageableGuildInfo(@NotNull UUID playerUuid) {
        Optional<GuildPlayer> guildPlayerOptional = GuildPlayerService.getInstance().findByPlayerUuid(playerUuid);
        if (!guildPlayerOptional.isPresent()) {
            return Optional.empty();
        }
        GuildPlayer guildPlayer = guildPlayerOptional.get();
        if (guildPlayer.getRoleId() > GuildRoleEnum.TWO.getRoleId()) {
            return Optional.empty();
        }
        return GuildInfoService.getInstance().findById(guildPlayer.getGuildInfoId());
    }

    private static boolean matchStart(UUID playerUuid) {
        return MatchPvpUtil.isRunning(playerUuid);
    }

    private static boolean matchPreparation(UUID playerUuid) {
        return MatchPvpUtil.isPreparing(playerUuid);
    }

    private static boolean seasonStart(UUID playerUuid) {
        return SeasonPvpUtil.isRunning(playerUuid);
    }

    private static boolean seasonPreparation(UUID playerUuid) {
        return SeasonPvpUtil.isPreparing(playerUuid);
    }
}
