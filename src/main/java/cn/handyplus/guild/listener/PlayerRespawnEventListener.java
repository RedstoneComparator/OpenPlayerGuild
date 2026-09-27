package cn.handyplus.guild.listener;

import cn.handyplus.guild.api.PlayerGuildApi;
import cn.handyplus.guild.core.match.MatchPvpUtil;
import cn.handyplus.guild.core.season.SeasonPvpUtil;
import cn.handyplus.guild.core.stronghold.StrongholdPvpManager;
import cn.handyplus.guild.core.stronghold.StrongholdPvpPlayer;
import cn.handyplus.guild.lib.EntitySchedulerUtil;
import cn.handyplus.guild.lib.HandyListener;
import cn.handyplus.guild.lib.HandyRunnable;
import cn.handyplus.guild.lib.HandySchedulerUtil;
import cn.handyplus.guild.lib.MessageUtil;
import cn.handyplus.guild.util.BuffManageUtil;
import cn.handyplus.guild.util.ConfigUtil;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.jetbrains.annotations.NotNull;

@HandyListener
public class PlayerRespawnEventListener
implements Listener {
    private final static Map<UUID, HandyRunnable> RESPAWN_TASKS = new ConcurrentHashMap<UUID, HandyRunnable>();

    @EventHandler(priority=EventPriority.MONITOR)
    public void onStronghold(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        StrongholdPvpPlayer strongholdPvpPlayer = StrongholdPvpManager.getPlayer(player.getUniqueId());
        if (strongholdPvpPlayer == null || !strongholdPvpPlayer.isWaitingRespawn()) {
            return;
        }
        Location location = StrongholdPvpManager.getSpawnLocation(strongholdPvpPlayer.getGuildId());
        if (location != null) {
            event.setRespawnLocation(location);
        }
        HandySchedulerUtil.runTaskLater(() -> StrongholdPvpManager.startRespawnCountdown(player.getUniqueId()), 1L);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (!HandySchedulerUtil.isFolia()) {
            return;
        }
        UUID uuid = event.getEntity().getUniqueId();
        PlayerRespawnEventListener.cancelRespawnTask(uuid);
        HandyRunnable respawnRunnable = new HandyRunnable(){

            @Override
            public void run() {
                Player player = Bukkit.getPlayer(uuid);
                if (player == null) {
                    PlayerRespawnEventListener.cancelRespawnTask(uuid);
                    return;
                }
                EntitySchedulerUtil.runSafeOnEntityScheduler(player, () -> {
                    if (!player.isOnline()) {
                        PlayerRespawnEventListener.cancelRespawnTask(uuid);
                        return;
                    }
                    if (player.isDead()) {
                        return;
                    }
                    if (!PlayerGuildApi.isPvp(player)) {
                        BuffManageUtil.callBuffEvent(player);
                    }
                    PlayerRespawnEventListener.cancelRespawnTask(uuid);
                });
            }
        };
        RESPAWN_TASKS.put(uuid, respawnRunnable);
        HandySchedulerUtil.runTaskTimer(respawnRunnable, 20L, 20L);
    }

    private static void restoreBuff(Player player) {
        BuffManageUtil.callBuffEvent(player);
    }

    @EventHandler(priority=EventPriority.MONITOR)
    public void onSeason(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        if (SeasonPvpUtil.getGuildIdByPlayer(player.getUniqueId()) == null) {
            return;
        }
        Location location = SeasonPvpUtil.getLookLocation();
        if (location == null) {
            return;
        }
        event.setRespawnLocation(location);
        MessageUtil.sendMessage(player, ConfigUtil.PVP_LOCATION_CONFIG.getString("lookLocation"));
    }

    private static void cancelRespawnTask(@NotNull UUID playerId) {
        HandyRunnable handyRunnable = RESPAWN_TASKS.remove(playerId);
        if (handyRunnable != null) {
            handyRunnable.cancel();
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        if (PlayerGuildApi.isPvp(player)) {
            return;
        }
        HandySchedulerUtil.runTaskLater(() -> PlayerRespawnEventListener.restoreBuff(player), 1L);
    }

    @EventHandler(priority=EventPriority.MONITOR)
    public void onMatch(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        Integer guildId = MatchPvpUtil.getGuildIdByPlayer(player.getUniqueId());
        if (guildId == null) {
            return;
        }
        Location location = MatchPvpUtil.getLookLocation(guildId);
        if (location == null) {
            return;
        }
        event.setRespawnLocation(location);
        MessageUtil.sendMessage(player, ConfigUtil.PVP_LOCATION_CONFIG.getString("lookLocation"));
    }
}
