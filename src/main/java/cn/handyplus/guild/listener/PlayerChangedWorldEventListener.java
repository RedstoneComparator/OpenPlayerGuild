package cn.handyplus.guild.listener;

import cn.handyplus.guild.constants.GuildConstants;
import cn.handyplus.guild.constants.GuildRoleEnum;
import cn.handyplus.guild.enter.GuildInfo;
import cn.handyplus.guild.enter.GuildPlayer;
import cn.handyplus.guild.hook.ResidenceUtil;
import cn.handyplus.guild.lib.HandyListener;
import cn.handyplus.guild.lib.HandySchedulerUtil;
import cn.handyplus.guild.service.GuildInfoService;
import cn.handyplus.guild.util.ConfigUtil;
import cn.handyplus.guild.util.GuildCacheUtil;
import java.util.Optional;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@HandyListener
public class PlayerChangedWorldEventListener
implements Listener {
    private static void teleportToResidenceSpawn(Player player, GuildInfo guildInfo) {
        Player targetPlayer = player;
        ResidenceUtil.init(targetPlayer, guildInfo, targetPlayer.getWorld().getSpawnLocation());
    }

    @EventHandler
    public void onFlyEvent(PlayerChangedWorldEvent event) {
        if (!ConfigUtil.WORLD_CONFIG.getBoolean("fly", false)) {
            return;
        }
        Player player = event.getPlayer();
        if (!PlayerChangedWorldEventListener.isGuildWorld(player, player.getWorld())) {
            return;
        }
        HandySchedulerUtil.runTaskLater(() -> PlayerChangedWorldEventListener.enableFlight(player), 20L);
    }

    private static boolean isGuildWorld(@NotNull Player player, @Nullable World world) {
        return PlayerChangedWorldEventListener.getGuildIdByWorld(world).filter(guildId -> PlayerChangedWorldEventListener.J(player, guildId)).isPresent();
    }

    private static boolean J(@NotNull Player player, @NotNull Integer guildId) {
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

    @EventHandler
    public void onResidenceEvent(PlayerChangedWorldEvent event) {
        if (!ConfigUtil.WORLD_CONFIG.getBoolean("residence")) {
            return;
        }
        Player player = event.getPlayer();
        Optional<Integer> optional = PlayerChangedWorldEventListener.getGuildIdByWorld(player.getWorld());
        if (!optional.isPresent()) {
            return;
        }
        GuildPlayer guildPlayer = GuildCacheUtil.getGuildPlayer(player.getUniqueId());
        Integer guildInfoId = optional.get();
        if (!guildInfoId.equals(guildPlayer.getGuildInfoId()) || !GuildRoleEnum.ONE.getRoleId().equals(guildPlayer.getRoleId())) {
            return;
        }
        GuildInfoService.getInstance().findById(guildInfoId).ifPresent(guildInfo -> PlayerChangedWorldEventListener.teleportToResidenceSpawn(player, guildInfo));
    }

    private static void enableFlight(Player player) {
        Player targetPlayer = player;
        targetPlayer.setAllowFlight(true);
        targetPlayer.setFlying(true);
    }
}
