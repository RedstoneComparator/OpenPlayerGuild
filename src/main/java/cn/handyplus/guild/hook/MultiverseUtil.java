package cn.handyplus.guild.hook;

import cn.handyplus.guild.PlayerGuild;
import cn.handyplus.guild.enter.GuildInfo;
import cn.handyplus.guild.event.GuildSetSpawnEvent;
import cn.handyplus.guild.hook.WorldBorderUtil;
import cn.handyplus.guild.hook.WorldEditUtil;
import cn.handyplus.guild.hook.WorldRuleUtil;
import cn.handyplus.guild.lib.BaseUtil;
import cn.handyplus.guild.lib.EntitySchedulerUtil;
import cn.handyplus.guild.lib.HandySchedulerUtil;
import cn.handyplus.guild.lib.MapUtil;
import cn.handyplus.guild.lib.MessageUtil;
import cn.handyplus.guild.lib.PlayerSchedulerUtil;
import cn.handyplus.guild.lib.StrUtil;
import cn.handyplus.guild.service.GuildInfoService;
import cn.handyplus.guild.util.ConfigUtil;
import cn.handyplus.guild.util.GuildCacheUtil;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.mvplugins.multiverse.core.MultiverseCoreApi;
import org.mvplugins.multiverse.core.utils.result.Attempt;
import org.mvplugins.multiverse.core.world.LoadedMultiverseWorld;
import org.mvplugins.multiverse.core.world.MultiverseWorld;
import org.mvplugins.multiverse.core.world.WorldManager;
import org.mvplugins.multiverse.core.world.options.CreateWorldOptions;
import org.mvplugins.multiverse.core.world.options.DeleteWorldOptions;
import org.mvplugins.multiverse.external.vavr.control.Option;

public class MultiverseUtil {
    private MultiverseUtil() {
    }

    protected static void createWorld(Player player, Integer guildId, String worldName, String alias) {
        CreateWorldOptions createWorldOptions;
        if (StrUtil.isEmpty(worldName)) {
            throw new RuntimeException("\u6ca1\u6709\u4e16\u754c\u540d\u5b57");
        }
        Optional<GuildInfo> guildInfoOptional = GuildInfoService.getInstance().findById(guildId);
        if (!guildInfoOptional.isPresent()) {
            return;
        }
        GuildInfo guildInfo = guildInfoOptional.get();
        String environment = ConfigUtil.WORLD_CONFIG.getString("environment");
        String worldType = ConfigUtil.WORLD_CONFIG.getString("worldType");
        String seedString = ConfigUtil.WORLD_CONFIG.getString("seedString");
        boolean generateStructures = ConfigUtil.WORLD_CONFIG.getBoolean("generateStructures");
        String difficulty = ConfigUtil.WORLD_CONFIG.getString("difficulty");
        boolean pvpMode = ConfigUtil.WORLD_CONFIG.getBoolean("PVPMode");
        boolean keepSpawnInMemory = ConfigUtil.WORLD_CONFIG.getBoolean("keepSpawnInMemory");
        String customGenerator = ConfigUtil.WORLD_CONFIG.getString("customGenerator");
        WorldManager worldManager = MultiverseCoreApi.get().getWorldManager();
        Attempt attempt = worldManager.createWorld(createWorldOptions = CreateWorldOptions.worldName((String)worldName).environment(World.Environment.valueOf((String)environment)).seed(seedString).worldType(WorldType.valueOf((String)worldType)).generateStructures(generateStructures).generatorSettings(customGenerator));
        if (attempt.isFailure()) {
            MessageUtil.sendMessage(player, BaseUtil.getLangMsg("multiverseCoreFailureMsg"));
            return;
        }
        LoadedMultiverseWorld world = (LoadedMultiverseWorld)attempt.get();
        world.setAlias(alias);
        world.setDifficulty(Difficulty.valueOf((String)difficulty));
        world.setPvp(pvpMode);
        world.setKeepSpawnInMemory(keepSpawnInMemory);
        Location spawnLocation = world.getSpawnLocation();
        int borderX = ConfigUtil.WORLD_CONFIG.getInt("guildSetting." + guildInfo.getLevel() + ".x");
        int borderZ = ConfigUtil.WORLD_CONFIG.getInt("guildSetting." + guildInfo.getLevel() + ".z");
        WorldBorderUtil.setBorder(spawnLocation, borderX, borderZ);
        WorldRuleUtil.setGameRule(worldName);
        GuildInfoService.getInstance().updateWorldName(guildId, worldName);
        GuildCacheUtil.initGuildWorldCache();
        EntitySchedulerUtil.teleport((Entity)player, spawnLocation);
        MessageUtil.sendTitle(player, guildInfo.getGuildName(), BaseUtil.getLangMsg("welcomeTitle", MapUtil.of("${player}", player.getName())));
        if (PlayerGuild.USE_WORLD_EDIT && ConfigUtil.WORLD_EDIT_CONFIG.getBoolean("enable", true)) {
            WorldEditUtil.create(spawnLocation);
        }
        GuildSetSpawnEvent.callEvent(spawnLocation, guildId, player);
    }

    protected static Location getSpawnLocation(String worldName) {
        WorldManager worldManager = MultiverseCoreApi.get().getWorldManager();
        Option worldOption = worldManager.getWorld(worldName);
        return ((MultiverseWorld)worldOption.get()).getSpawnLocation();
    }

    protected static void removeWorld(String worldName, List<UUID> playerUuids) {
        if (StrUtil.isEmpty(worldName)) {
            return;
        }
        HandySchedulerUtil.runTask(() -> {
            for (UUID playerUuid : playerUuids) {
                MultiverseUtil.removePlayerFromWorld(playerUuid, worldName);
            }
            Option worldOption = MultiverseCoreApi.get().getWorldManager().getWorld(worldName);
            MultiverseCoreApi.get().getWorldManager().deleteWorld(DeleteWorldOptions.world((MultiverseWorld)((MultiverseWorld)worldOption.get())));
        });
    }

    protected static void removePlayerFromWorld(UUID playerUuid, String worldName) {
        if (StrUtil.isEmpty(worldName)) {
            return;
        }
        Optional<Player> onlinePlayer = BaseUtil.getOnlinePlayer(playerUuid);
        if (onlinePlayer.isPresent() && onlinePlayer.get().getWorld().getName().equals(worldName)) {
            PlayerSchedulerUtil.performCommand(onlinePlayer.get(), "spawn");
        }
    }

    protected static void setWorldAlias(String worldName, String guildName) {
        if (StrUtil.isEmpty(worldName)) {
            return;
        }
        World bukkitWorld = Bukkit.getWorld((String)worldName);
        if (bukkitWorld == null) {
            return;
        }
        WorldManager worldManager = MultiverseCoreApi.get().getWorldManager();
        MultiverseWorld world = (MultiverseWorld)worldManager.getWorld(worldName).get();
        String alias = ConfigUtil.WORLD_CONFIG.getString("aliasName", "${guildName}").replace("${guildName}", guildName);
        world.setAlias(alias);
    }

    protected static String getWorldAlias(String worldName) {
        if (StrUtil.isEmpty(worldName)) {
            return worldName;
        }
        World bukkitWorld = Bukkit.getWorld((String)worldName);
        if (bukkitWorld == null) {
            return worldName;
        }
        WorldManager worldManager = MultiverseCoreApi.get().getWorldManager();
        Option worldOption = worldManager.getWorld(worldName);
        if (worldOption.isEmpty()) {
            return worldName;
        }
        MultiverseWorld world = (MultiverseWorld)worldOption.get();
        return StrUtil.isEmpty(world.getAlias()) ? worldName : world.getAlias();
    }
}
