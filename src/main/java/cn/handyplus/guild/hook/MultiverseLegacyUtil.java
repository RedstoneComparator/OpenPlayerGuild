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
import cn.handyplus.guild.lib.HookPluginEnum;
import cn.handyplus.guild.lib.HookPluginUtil;
import cn.handyplus.guild.lib.MapUtil;
import cn.handyplus.guild.lib.MessageUtil;
import cn.handyplus.guild.lib.PlayerSchedulerUtil;
import cn.handyplus.guild.lib.StrUtil;
import cn.handyplus.guild.service.GuildInfoService;
import cn.handyplus.guild.util.ConfigUtil;
import cn.handyplus.guild.util.GuildCacheUtil;
import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.MVWorldManager;
import com.onarandombox.MultiverseCore.api.MultiverseWorld;
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

public class MultiverseLegacyUtil {
    private static MultiverseCore MULTIVERSE_CORE;

    private MultiverseLegacyUtil() {
    }

    public static void init() {
        HookPluginUtil.hookToPlugin(HookPluginEnum.MULTIVERSE_CORE, 4).ifPresent(hookedPlugin -> {
            MULTIVERSE_CORE = (MultiverseCore)hookedPlugin;
        });
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
        String environment = ConfigUtil.WORLD_CONFIG.getString("environment");
        String worldType = ConfigUtil.WORLD_CONFIG.getString("worldType");
        String seedString = ConfigUtil.WORLD_CONFIG.getString("seedString");
        boolean generateStructures = ConfigUtil.WORLD_CONFIG.getBoolean("generateStructures");
        String difficulty = ConfigUtil.WORLD_CONFIG.getString("difficulty");
        boolean pvpMode = ConfigUtil.WORLD_CONFIG.getBoolean("PVPMode");
        boolean keepSpawnInMemory = ConfigUtil.WORLD_CONFIG.getBoolean("keepSpawnInMemory");
        String customGenerator = ConfigUtil.WORLD_CONFIG.getString("customGenerator");
        MVWorldManager worldManager = MULTIVERSE_CORE.getMVWorldManager();
        boolean created = worldManager.addWorld(worldName, World.Environment.valueOf((String)environment), seedString, WorldType.valueOf((String)worldType), Boolean.valueOf(generateStructures), customGenerator);
        if (!created) {
            MessageUtil.sendMessage(player, BaseUtil.getLangMsg("multiverseCoreFailureMsg"));
            return;
        }
        MultiverseWorld world = worldManager.getMVWorld(worldName);
        world.setAlias(alias);
        world.setDifficulty(Difficulty.valueOf((String)difficulty));
        world.setPVPMode(pvpMode);
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
        MVWorldManager worldManager = MULTIVERSE_CORE.getMVWorldManager();
        MultiverseWorld world = worldManager.getMVWorld(worldName);
        return world.getSpawnLocation();
    }

    protected static void removeWorld(String worldName, List<UUID> playerUuids) {
        if (StrUtil.isEmpty(worldName)) {
            return;
        }
        HandySchedulerUtil.runTask(() -> {
            for (UUID playerUuid : playerUuids) {
                MultiverseLegacyUtil.removePlayerFromWorld(playerUuid, worldName);
            }
            boolean deleted = MULTIVERSE_CORE.getMVWorldManager().deleteWorld(worldName);
            MessageUtil.sendConsoleMessage("\u516c\u4f1a\u89e3\u6563,\u5220\u9664\u516c\u4f1a\u4e16\u754c" + worldName + (deleted ? "\u6210\u529f" : "\u5931\u8d25"));
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
        MVWorldManager worldManager = MULTIVERSE_CORE.getMVWorldManager();
        MultiverseWorld world = worldManager.getMVWorld(worldName);
        if (world == null) {
            return;
        }
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
        MVWorldManager worldManager = MULTIVERSE_CORE.getMVWorldManager();
        MultiverseWorld world = worldManager.getMVWorld(worldName);
        if (world == null) {
            return worldName;
        }
        return StrUtil.isEmpty(world.getAlias()) ? worldName : world.getAlias();
    }
}
