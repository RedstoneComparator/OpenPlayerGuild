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
import java.util.concurrent.CompletableFuture;
import net.kyori.adventure.key.Key;
import net.thenextlvl.worlds.Dimension;
import net.thenextlvl.worlds.Level;
import net.thenextlvl.worlds.WorldRegistry;
import net.thenextlvl.worlds.WorldsAccess;
import net.thenextlvl.worlds.generator.Generator;
import net.thenextlvl.worlds.generator.GeneratorType;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class WorldsUtil {
    private WorldsUtil() {
    }

    protected static void createWorld(Player player, Integer guildId, String worldName, String alias) {
        if (StrUtil.isEmpty(worldName)) {
            throw new RuntimeException("\u6ca1\u6709\u4e16\u754c\u540d\u5b57");
        }
        Optional<GuildInfo> guildInfoOptional = GuildInfoService.getInstance().findById(guildId);
        if (!guildInfoOptional.isPresent()) {
            return;
        }
        GuildInfo guildInfo = guildInfoOptional.get();
        String difficulty = ConfigUtil.WORLD_CONFIG.getString("difficulty");
        boolean pvpMode = ConfigUtil.WORLD_CONFIG.getBoolean("PVPMode");
        boolean keepSpawnInMemory = ConfigUtil.WORLD_CONFIG.getBoolean("keepSpawnInMemory");
        try {
            Level level = WorldsUtil.buildLevel(worldName);
            CompletableFuture<World> worldFuture = WorldsAccess.access().create(level);
            worldFuture.whenComplete((world, error) -> {
                if (error != null || world == null) {
                    HandySchedulerUtil.runTask(() -> MessageUtil.sendMessage(player, BaseUtil.getLangMsg("worldsFailureMsg")));
                    return;
                }
                WorldsUtil.registerLevel(level);
                HandySchedulerUtil.runTask(() -> WorldsUtil.initGuildWorld(player, guildInfo, world, difficulty, pvpMode, keepSpawnInMemory, alias));
            });
        }
        catch (Exception exception) {
            MessageUtil.sendConsoleMessage("Worlds\u521b\u5efa\u516c\u4f1a\u4e16\u754c\u5931\u8d25: " + exception.getMessage());
            MessageUtil.sendMessage(player, BaseUtil.getLangMsg("worldsFailureMsg"));
        }
    }

    protected static Location getSpawnLocation(String worldName) {
        World world = Bukkit.getWorld((String)worldName);
        return world == null ? null : world.getSpawnLocation();
    }

    protected static void removeWorld(String worldName, List<UUID> playerUuids) {
        if (StrUtil.isEmpty(worldName)) {
            return;
        }
        HandySchedulerUtil.runTask(() -> {
            for (UUID playerUuid : playerUuids) {
                WorldsUtil.removePlayerFromWorld(playerUuid, worldName);
            }
            World world = Bukkit.getWorld((String)worldName);
            if (world == null) {
                return;
            }
            CompletableFuture<Boolean> deleteFuture = WorldsAccess.access().delete(world);
            deleteFuture.whenComplete((deleted, error) -> HandySchedulerUtil.runTask(() -> {
                if (error != null) {
                    MessageUtil.sendConsoleMessage("\u516c\u4f1a\u89e3\u6563,\u5220\u9664\u516c\u4f1a\u4e16\u754c" + worldName + "\u5931\u8d25");
                    return;
                }
                MessageUtil.sendConsoleMessage("\u516c\u4f1a\u89e3\u6563,\u5220\u9664\u516c\u4f1a\u4e16\u754c" + worldName + (Boolean.TRUE.equals(deleted) ? "\u6210\u529f" : "\u5931\u8d25"));
            }));
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

    private static Level buildLevel(@NotNull String worldName) {
        String customGenerator;
        Level.Builder levelBuilder = Level.builder((Key)Key.key((String)worldName)).dimension(WorldsUtil.getDimension(ConfigUtil.WORLD_CONFIG.getString("environment"))).generatorType(WorldsUtil.getGeneratorType(ConfigUtil.WORLD_CONFIG.getString("worldType"))).structures(Boolean.valueOf(ConfigUtil.WORLD_CONFIG.getBoolean("generateStructures")));
        Long seed = WorldsUtil.getSeed(ConfigUtil.WORLD_CONFIG.getString("seedString"));
        if (seed != null) {
            levelBuilder.seed(seed);
        }
        if (StrUtil.isNotEmpty(customGenerator = ConfigUtil.WORLD_CONFIG.getString("customGenerator"))) {
            levelBuilder.generator(Generator.fromString((String)customGenerator));
        }
        return levelBuilder.build();
    }

    private static void registerLevel(Level level) {
        WorldRegistry worldRegistry = WorldsAccess.access().getWorldRegistry();
        if (worldRegistry.isRegistered(level.key())) {
            worldRegistry.setEnabled(level.key(), true);
            return;
        }
        worldRegistry.register(level, true);
    }

    private static void initGuildWorld(Player player, GuildInfo guildInfo, World world, String difficulty, boolean pvpMode, boolean keepSpawnInMemory, String alias) {
        String worldName = world.getName();
        world.setDifficulty(Difficulty.valueOf((String)difficulty));
        world.setPVP(pvpMode);
        world.setKeepSpawnInMemory(keepSpawnInMemory);
        Location spawnLocation = world.getSpawnLocation();
        int borderX = ConfigUtil.WORLD_CONFIG.getInt("guildSetting." + guildInfo.getLevel() + ".x");
        int borderZ = ConfigUtil.WORLD_CONFIG.getInt("guildSetting." + guildInfo.getLevel() + ".z");
        WorldBorderUtil.setBorder(spawnLocation, borderX, borderZ);
        WorldRuleUtil.setGameRule(worldName);
        GuildInfoService.getInstance().updateWorldName(guildInfo.getId(), worldName);
        GuildCacheUtil.initGuildWorldCache();
        EntitySchedulerUtil.teleport((Entity)player, spawnLocation);
        MessageUtil.sendTitle(player, guildInfo.getGuildName(), BaseUtil.getLangMsg("welcomeTitle", MapUtil.of("${player}", player.getName())));
        if (PlayerGuild.USE_WORLD_EDIT && ConfigUtil.WORLD_EDIT_CONFIG.getBoolean("enable", true)) {
            WorldEditUtil.create(spawnLocation);
        }
        GuildSetSpawnEvent.callEvent(spawnLocation, guildInfo.getId(), player);
    }

    private static Dimension getDimension(String environment) {
        if ("NETHER".equalsIgnoreCase(environment)) {
            return Dimension.THE_NETHER;
        }
        if ("THE_END".equalsIgnoreCase(environment)) {
            return Dimension.THE_END;
        }
        return Dimension.OVERWORLD;
    }

    private static GeneratorType getGeneratorType(String worldType) {
        if ("FLAT".equalsIgnoreCase(worldType)) {
            return GeneratorType.FLAT;
        }
        if ("LARGE_BIOMES".equalsIgnoreCase(worldType)) {
            return GeneratorType.LARGE_BIOMES;
        }
        if ("AMPLIFIED".equalsIgnoreCase(worldType)) {
            return GeneratorType.AMPLIFIED;
        }
        return GeneratorType.NORMAL;
    }

    private static Long getSeed(String seedString) {
        if (StrUtil.isEmpty(seedString)) {
            return null;
        }
        try {
            return Long.valueOf(seedString);
        }
        catch (NumberFormatException numberFormatException) {
            return (long)seedString.hashCode();
        }
    }
}
