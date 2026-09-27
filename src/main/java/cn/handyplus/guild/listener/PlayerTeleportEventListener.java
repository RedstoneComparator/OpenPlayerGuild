package cn.handyplus.guild.listener;

import cn.handyplus.guild.constants.GuildConstants;
import cn.handyplus.guild.core.match.MatchPvpUtil;
import cn.handyplus.guild.core.season.SeasonPvpUtil;
import cn.handyplus.guild.core.stronghold.StrongholdPvpManager;
import cn.handyplus.guild.core.stronghold.StrongholdPvpUtil;
import cn.handyplus.guild.enter.GuildPlayer;
import cn.handyplus.guild.lib.BaseUtil;
import cn.handyplus.guild.lib.HandyListener;
import cn.handyplus.guild.lib.MessageUtil;
import cn.handyplus.guild.util.ConfigUtil;
import cn.handyplus.guild.util.GuildCacheUtil;
import java.util.Optional;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@HandyListener
public class PlayerTeleportEventListener
implements Listener {
    private void cancelSeasonPvpTeleport(@NotNull PlayerTeleportEvent event) {
        if (SeasonPvpUtil.isRunning(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
        }
    }

    private void cancelStrongholdTeleport(@NotNull PlayerTeleportEvent event) {
        if (!StrongholdPvpUtil.isRunning(event.getPlayer().getUniqueId())) {
            return;
        }
        if (StrongholdPvpManager.isRespawnTeleport(event.getPlayer().getUniqueId(), event.getTo())) {
            return;
        }
        event.setCancelled(true);
    }

    private void cancelMatchTeleport(@NotNull PlayerTeleportEvent event) {
        if (!MatchPvpUtil.isRunning(event.getPlayer().getUniqueId())) {
            return;
        }
        event.setCancelled(true);
    }

    private static boolean isBlockedGuildWorld(@NotNull Player player, @Nullable World world) {
        if (player.isOp()) {
            return false;
        }
        return PlayerTeleportEventListener.getGuildIdByWorld(world).filter(guildId -> {
            if (!PlayerTeleportEventListener.isSameGuild(player, guildId)) {
                return true;
            }
            return false;
        }).isPresent();
    }

    @EventHandler(priority=EventPriority.HIGH)
    public void onGuildWorldTeleport(PlayerTeleportEvent event) {
        if (event.isCancelled()) {
            return;
        }
        PlayerTeleportEvent teleportEvent = event;
        World world = teleportEvent.getTo().getWorld();
        if (teleportEvent.getFrom().getWorld().equals((Object)world)) {
            return;
        }
        if (!ConfigUtil.WORLD_CONFIG.getBoolean("notGuildPlayerTp", true)) {
            return;
        }
        if (PlayerTeleportEventListener.isBlockedGuildWorld(event.getPlayer(), world)) {
            PlayerTeleportEvent blockedEvent = event;
            blockedEvent.setCancelled(true);
            MessageUtil.sendMessage(blockedEvent.getPlayer(), BaseUtil.getLangMsg("noJoinGuildWorld"));
        }
    }

    @EventHandler(priority=EventPriority.MONITOR)
    public void onPvpEvent(PlayerTeleportEvent event) {
        if (!ConfigUtil.KIT_PVP_CONFIG.getBoolean("disabledTeleport", true)) {
            return;
        }
        PlayerTeleportEventListener listener = this;
        PlayerTeleportEvent teleportEvent = event;
        this.cancelSeasonPvpTeleport(event);
        listener.cancelMatchTeleport(teleportEvent);
        listener.cancelStrongholdTeleport(teleportEvent);
    }

    private static boolean isSameGuild(@NotNull Player player, @NotNull Integer guildId) {
        GuildPlayer guildPlayer = GuildCacheUtil.getGuildPlayer(player.getUniqueId());
        return guildId.equals(guildPlayer.getGuildInfoId());
    }

    @NotNull
    private static Optional<Integer> getGuildIdByWorld(@Nullable World world) {
        if (world == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(GuildConstants.GUILD_WORLD_MAP.get(world.getName()));
    }
}
