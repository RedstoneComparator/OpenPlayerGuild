package cn.handyplus.guild.util;

import cn.handyplus.guild.PlayerGuild;
import cn.handyplus.guild.hook.MyPetV3Util;
import cn.handyplus.guild.hook.MyPetV4Util;
import cn.handyplus.guild.lib.CollUtil;
import cn.handyplus.guild.lib.EntitySchedulerUtil;
import cn.handyplus.guild.lib.LegacyUtil;
import cn.handyplus.guild.lib.MessageUtil;
import cn.handyplus.guild.lib.PlayerSchedulerUtil;
import cn.handyplus.guild.util.BuffManageUtil;
import cn.handyplus.guild.util.ConfigUtil;
import cn.handyplus.guild.util.LeatherUtil;
import cn.handyplus.race.api.PlayerRaceApi;
import cn.handyplus.title.api.PlayerTitleApi;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.Nullable;

public class KitPvpUtil {
    public final static Map<UUID, Double> GUILD_PVP_START_PLAYER_HEALTH_MAP = new ConcurrentHashMap<UUID, Double>();

    private static void disableFlight(Player player) {
        if (!ConfigUtil.KIT_PVP_CONFIG.getBoolean("banFly", false)) {
            return;
        }
        player.setAllowFlight(false);
    }

    private static void removePotionEffects(Player player) {
        if (!ConfigUtil.KIT_PVP_CONFIG.getBoolean("potionEffectBuff", true)) {
            return;
        }
        Collection<PotionEffect> activeEffects = player.getActivePotionEffects();
        if (CollUtil.isNotEmpty(activeEffects)) {
            for (PotionEffect potionEffect : activeEffects) {
                player.removePotionEffect(potionEffect.getType());
            }
            MessageUtil.sendMessage(player, ConfigUtil.KIT_PVP_CONFIG.getString("potionEffectBuffOnMsg"));
        }
    }

    private static void restoreMaxHealth(Player targetPlayer) {
        Player player = targetPlayer;
        player.setHealth(LegacyUtil.getMaxHealth((LivingEntity)player));
    }

    public static void kitPvp(Player targetPlayer) {
        Player player = targetPlayer;
        KitPvpUtil.performCommands(player);
        KitPvpUtil.disableFlight(player);
        KitPvpUtil.removeTitleBuff(player);
        KitPvpUtil.removeRaceBuff(player);
        KitPvpUtil.i(player);
        KitPvpUtil.disableAdapt(player);
        KitPvpUtil.removePet(player);
        KitPvpUtil.removePotionEffects(player);
        KitPvpUtil.applyMaxHealth(player);
        KitPvpUtil.restoreMaxHealth(player);
    }

    private static void i(Player player) {
        BuffManageUtil.removeBuff(player);
    }

    private static void performCommands(Player player) {
        List<String> commands = ConfigUtil.KIT_PVP_CONFIG.getStringList("performCommand.command");
        if (CollUtil.isEmpty(commands)) {
            return;
        }
        if (!ConfigUtil.KIT_PVP_CONFIG.getBoolean("performCommand.enable", false)) {
            return;
        }
        for (String command : commands) {
            if ((command = command.replace("${player}", player.getName())).contains("[Console]")) {
                PlayerSchedulerUtil.dispatchCommand(command.replace("[Console]", ""));
                continue;
            }
            PlayerSchedulerUtil.playerPerformCommand(player, command);
        }
    }

    public static void startPvp(Player targetPlayer, Location location, Color color, @Nullable GameMode gameMode) {
        Player player = targetPlayer;
        EntitySchedulerUtil.runSafeOnEntityScheduler((LivingEntity)player, () -> {
            KitPvpUtil.kitPvp(player);
            if (gameMode != null) {
                player.setGameMode(gameMode);
            }
            EntitySchedulerUtil.teleport((Entity)player, location);
            LeatherUtil.equipment(player, color);
        });
    }

    private static void removePet(Player player) {
        if (!ConfigUtil.KIT_PVP_CONFIG.getBoolean("banMyPet", true)) {
            return;
        }
        if (PlayerGuild.USE_MY_PET_4) {
            MyPetV4Util.removeMyPet(player);
            return;
        }
        if (PlayerGuild.USE_MY_PET_3) {
            MyPetV3Util.removeMyPet(player);
        }
    }

    private static void applyMaxHealth(Player player) {
        if (!ConfigUtil.KIT_PVP_CONFIG.getBoolean("maxHealth.enable", false)) {
            return;
        }
        GUILD_PVP_START_PLAYER_HEALTH_MAP.put(player.getUniqueId(), LegacyUtil.getMaxHealth((LivingEntity)player));
        double maxHealth = ConfigUtil.KIT_PVP_CONFIG.getDouble("maxHealth.health", 20.0);
        LegacyUtil.setMaxHealth((LivingEntity)player, maxHealth);
    }

    private KitPvpUtil() {
    }

    private static void disableAdapt(Player player) {
        if (PlayerGuild.USE_ADAPT) {
            if (ConfigUtil.KIT_PVP_CONFIG.getBoolean("adapt", false)) {
                MessageUtil.sendMessage(player, ConfigUtil.KIT_PVP_CONFIG.getString("adaptOffMsg"));
            }
        }
    }

    private static void removeTitleBuff(Player targetPlayer) {
        if (PlayerGuild.USE_TITLE) {
            if (ConfigUtil.KIT_PVP_CONFIG.getBoolean("playerTitleBuff", true)) {
                Player player = targetPlayer;
                PlayerTitleApi.removePlayerBuff((Player)player);
                MessageUtil.sendMessage(player, ConfigUtil.KIT_PVP_CONFIG.getString("playerTitleBuffOffMsg"));
            }
        }
    }

    private static void removeRaceBuff(Player player) {
        if (PlayerGuild.USE_RACE) {
            if (ConfigUtil.KIT_PVP_CONFIG.getBoolean("playerRaceBuff", true)) {
                PlayerRaceApi.getInstance().temporaryCancel(player);
                MessageUtil.sendMessage(player, ConfigUtil.KIT_PVP_CONFIG.getString("playerRaceBuffOffMsg"));
            }
        }
    }
}
