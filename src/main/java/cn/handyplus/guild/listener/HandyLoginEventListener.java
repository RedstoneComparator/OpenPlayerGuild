package cn.handyplus.guild.listener;

import cn.handyplus.guild.command.player.JoinCommand;
import cn.handyplus.guild.constants.ApplyTypeEnum;
import cn.handyplus.guild.constants.GuildRoleEnum;
import cn.handyplus.guild.core.common.GuildPvpTimeUtil;
import cn.handyplus.guild.core.match.MatchPvpUtil;
import cn.handyplus.guild.core.score.ScorePvpUtil;
import cn.handyplus.guild.core.season.SeasonPvpUtil;
import cn.handyplus.guild.core.stronghold.StrongholdPvpManager;
import cn.handyplus.guild.core.stronghold.StrongholdPvpPlayer;
import cn.handyplus.guild.enter.GuildApply;
import cn.handyplus.guild.enter.GuildChannel;
import cn.handyplus.guild.enter.GuildNotice;
import cn.handyplus.guild.enter.GuildPlayer;
import cn.handyplus.guild.hook.PlayerChatUtil;
import cn.handyplus.guild.hook.PlayerScoreboardUtil;
import cn.handyplus.guild.lib.BaseConstants;
import cn.handyplus.guild.lib.BaseUtil;
import cn.handyplus.guild.lib.CollUtil;
import cn.handyplus.guild.lib.DateUtil;
import cn.handyplus.guild.lib.EntitySchedulerUtil;
import cn.handyplus.guild.lib.HandyListener;
import cn.handyplus.guild.lib.HandyLoginEvent;
import cn.handyplus.guild.lib.HandySchedulerUtil;
import cn.handyplus.guild.lib.JsonUtil;
import cn.handyplus.guild.lib.RgbTextUtil;
import cn.handyplus.guild.lib.SignService;
import cn.handyplus.guild.lib.StrUtil;
import cn.handyplus.guild.param.GuildSpawn;
import cn.handyplus.guild.service.GuildApplyService;
import cn.handyplus.guild.service.GuildChannelService;
import cn.handyplus.guild.service.GuildNoticeReadService;
import cn.handyplus.guild.service.GuildNoticeService;
import cn.handyplus.guild.service.GuildPlayerService;
import cn.handyplus.guild.service.GuildPlayerSignInService;
import cn.handyplus.guild.service.GuildPvpService;
import cn.handyplus.guild.util.BuffManageUtil;
import cn.handyplus.guild.util.ConfigUtil;
import cn.handyplus.guild.util.GuildCacheUtil;
import cn.handyplus.guild.util.GuildUtil;
import cn.handyplus.guild.util.LeatherUtil;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

@HandyListener
public class HandyLoginEventListener
implements Listener {
    private static void teleportToChannelSpawn(HandyLoginEvent event, GuildChannel guildChannel) {
        GuildUtil.teleport(event.getPlayer(), JsonUtil.toBean(guildChannel.getSpawnLocation(), GuildSpawn.class), guildChannel.getGuildName());
    }

    @EventHandler(priority=EventPriority.HIGHEST)
    public void onNotice(@NotNull HandyLoginEvent event) {

    }

    private boolean L(@NotNull Player player) {
        Integer guildId = ScorePvpUtil.getGuildIdByPlayer(player.getUniqueId());
        if (guildId == null) {
            return false;
        }
        Location location = ScorePvpUtil.getLookLocation(guildId);
        if (location == null) {
            Player target = player;
            EntitySchedulerUtil.teleport((Entity)target, target.getWorld().getSpawnLocation());
        } else {
            EntitySchedulerUtil.teleport((Entity)player, location);
        }
        return true;
    }

    private boolean F(@NotNull Player player) {
        Integer guildId = MatchPvpUtil.getMatchGuildIdByPlayer(player.getUniqueId());
        if (guildId == null) {
            return false;
        }
        Location location = MatchPvpUtil.getLookLocation(guildId);
        if (location == null) {
            Player target = player;
            EntitySchedulerUtil.teleport((Entity)target, target.getWorld().getSpawnLocation());
        } else {
            EntitySchedulerUtil.teleport((Entity)player, location);
        }
        return true;
    }

    private boolean J(@NotNull Player player) {
        if (SeasonPvpUtil.getGuildIdByPlayer(player.getUniqueId()) == null) {
            return false;
        }
        Location location = SeasonPvpUtil.getLookLocation();
        if (location == null) {
            Player target = player;
            EntitySchedulerUtil.teleport((Entity)target, target.getWorld().getSpawnLocation());
        } else {
            EntitySchedulerUtil.teleport((Entity)player, location);
        }
        return true;
    }

    private void F(@NotNull Player player, @NotNull GuildPlayer guildPlayer) {
        List<Integer> readIds = GuildNoticeReadService.getInstance().findGuildNoticeId(guildPlayer.getGuildInfoId(), player.getUniqueId());
        List<GuildNotice> notices = GuildNoticeService.getInstance().list(guildPlayer.getGuildInfoId(), readIds);
        if (CollUtil.isNotEmpty(notices)) {
            String unreadMsg = ConfigUtil.NOTICE_CONFIG.getString("unreadMsg", "").replace("${size}", String.valueOf(notices.size()));
            String lookNotice = ConfigUtil.NOTICE_CONFIG.getString("lookNotice");
            RgbTextUtil.init(unreadMsg).addExtra(RgbTextUtil.init(lookNotice).addClickCommand("/plg me notice")).send((CommandSender)player);
        }
    }

    @EventHandler
    public void onPvp(HandyLoginEvent event) {
        if (!SignService.getInstance().isSuccess()) {
            return;
        }
        Player player = event.getPlayer();
        EntitySchedulerUtil.runSafeOnEntityScheduler((LivingEntity)player, () -> this.applyLoginEquipmentDelay(player));
    }

    private void handleLogin(Player player) {
        Optional<GuildPlayer> optional = GuildPlayerService.getInstance().findByPlayerUuidAndGuildInfo(player.getUniqueId());
        if (!optional.isPresent()) {
            boolean noGuildRemind = BaseConstants.CONFIG.getBoolean("noGuildRemind", true);
            if (noGuildRemind) {
                RgbTextUtil.init(BaseUtil.getLangMsg("noGuildRemind")).addClickCommand("/plg open").send((CommandSender)player);
            }
            return;
        }
        GuildPlayer guildPlayer = optional.get();
        Player self = player;
        Player cachedPlayer = player;
        GuildPlayer cachedGuildPlayer = guildPlayer;
        GuildCacheUtil.setCache(cachedPlayer.getUniqueId(), cachedGuildPlayer);
        GuildUtil.cacheGuildTask(cachedGuildPlayer.getGuildInfoId());
        GuildPlayerService.getInstance().updateLastJoinTimeByPlayer(player);
        HandyLoginEventListener listener = this;
        Player loginPlayer = player;
        this.F(player, guildPlayer);
        this.sendSignInTip(loginPlayer);
        listener.J(loginPlayer, guildPlayer);
        listener.sendSeasonPvpTip(cachedPlayer, guildPlayer);
        BuffManageUtil.callBuffEvent(self);
        PlayerChatUtil.regPlayerChannel(self, guildPlayer.getGuildInfoId());
        PlayerScoreboardUtil.addLine(Collections.singletonList(self), guildPlayer.getGuildInfoId());
    }

    private void J(@NotNull Player player, @NotNull GuildPlayer guildPlayer) {
        if (((GuildPlayer)guildPlayer).getRoleId() > GuildRoleEnum.TWO.getRoleId()) {
            return;
        }
        List<GuildApply> applies = GuildApplyService.getInstance().findByGuildId(guildPlayer.getGuildInfoId(), ApplyTypeEnum.PENDING.getId());
        for (GuildApply guildApply : applies) {
            JoinCommand.sendApplyMessage(guildApply.getPlayerName(), guildApply.getId(), player);
        }
    }

    @EventHandler(priority=EventPriority.HIGH)
    public void onTp(@NotNull HandyLoginEvent event) {
        if (!SignService.getInstance().isSuccess()) {
            return;
        }
        HandySchedulerUtil.runTaskLaterAsynchronously(() -> {
            String serverName = BaseConstants.CONFIG.getString("serverName");
            Optional<GuildChannel> optional = GuildChannelService.getInstance().findByPlayerUuidAndServerName(event.getPlayer().getUniqueId(), serverName);
            if (!optional.isPresent()) {
                return;
            }
            GuildChannel guildChannel = optional.get();
            GuildChannelService.getInstance().delById(guildChannel.getId());
            HandySchedulerUtil.runTask(() -> HandyLoginEventListener.teleportToChannelSpawn(event, guildChannel));
        }, 20L);
    }

    private void applyLoginEquipmentDelay(Player player) {
        if (this.F(player) || this.L(player) || this.J(player) || this.teleportToStrongholdSpawn(player)) {
            return;
        }
        int delaySeconds = ConfigUtil.KIT_PVP_CONFIG.getInt("loginEquipmentDelay", 1);
        HandySchedulerUtil.runTaskLater(() -> LeatherUtil.setPlayerItemStack(player), 20L * (long)delaySeconds);
    }

    private void sendSeasonPvpTip(@NotNull Player player, @NotNull GuildPlayer guildPlayer) {
        if (!ConfigUtil.ACTIVITY_CONFIG.getBoolean("seasonPvp.enable")) {
            return;
        }
        if (guildPlayer.getRoleId() > GuildRoleEnum.TWO.getRoleId()) {
            return;
        }
        if (GuildPvpService.getInstance().findByGuildInfoId(guildPlayer.getGuildInfoId()).isPresent()) {
            return;
        }
        int startDate = ConfigUtil.ACTIVITY_CONFIG.getInt("seasonPvp.startDate");
        Integer dayOfWeek = DateUtil.dayOfWeekEnum(new Date());
        int currentMinute = GuildPvpTimeUtil.getMinute(DateUtil.format(new Date(), "HH:mm"));
        int startMinute = GuildPvpTimeUtil.getMinute(ConfigUtil.ACTIVITY_CONFIG.getString("seasonPvp.startTime", ""));
        if (dayOfWeek != startDate || currentMinute < startMinute) {
            RgbTextUtil.init(BaseUtil.getLangMsg("noGuildSeason")).addClickCommand("/plg me activity ").send((CommandSender)player);
        }
    }

    private void sendSignInTip(@NotNull Player player) {
        if (!ConfigUtil.VIEW_CONFIG.getBoolean("signin.enable")) {
            return;
        }
        if (GuildPlayerSignInService.getInstance().findByPlayerUuid(player.getUniqueId()).isPresent()) {
            return;
        }
        String tip = BaseUtil.getLangMsg("signInTip");
        if (StrUtil.isEmpty(tip)) {
            return;
        }
        RgbTextUtil.init(tip).addClickCommand("/plg me signIn").send((CommandSender)player);
    }

    @EventHandler
    public void onPlayer(HandyLoginEvent event) {
        if (!SignService.getInstance().isSuccess()) {
            return;
        }
        Player player = event.getPlayer();
        HandySchedulerUtil.runTaskAsynchronously(() -> this.handleLogin(player));
    }

    private boolean teleportToStrongholdSpawn(@NotNull Player player) {
        StrongholdPvpPlayer strongholdPvpPlayer = StrongholdPvpManager.getPlayer(player.getUniqueId());
        if (strongholdPvpPlayer == null) {
            return false;
        }
        Location location = StrongholdPvpManager.getSpawnLocation(strongholdPvpPlayer.getGuildId());
        if (location == null) {
            Player target = player;
            EntitySchedulerUtil.teleport((Entity)target, target.getWorld().getSpawnLocation());
        } else {
            EntitySchedulerUtil.teleport((Entity)player, location);
        }
        return true;
    }
}
