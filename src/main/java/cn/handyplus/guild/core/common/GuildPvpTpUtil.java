package cn.handyplus.guild.core.common;

import cn.handyplus.guild.lib.BaseConstants;
import cn.handyplus.guild.lib.BaseUtil;
import cn.handyplus.guild.lib.EntitySchedulerUtil;
import cn.handyplus.guild.lib.MessageUtil;
import cn.handyplus.guild.lib.PlayerSchedulerUtil;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GuildPvpTpUtil {
    private final static String MODEL_SPAWN = "spawn";
    private final static String MODEL_LOOK_LOCATION = "lookLocation";
    private final static String MODEL_DEFAULT = "default";

    public static void tp(@Nullable Location lookLocation, Location spawnLocation, @NotNull Player targetPlayer) {
        if (targetPlayer.isDead()) {
            return;
        }
        String endModel = BaseConstants.CONFIG.getString("pvpEndModel", MODEL_DEFAULT);
        if (MODEL_DEFAULT.equalsIgnoreCase(endModel)) {
            if (!EntitySchedulerUtil.teleport((Entity)targetPlayer, spawnLocation)) {
                Player player = targetPlayer;
                MessageUtil.sendMessage(player, BaseUtil.getLangMsg("tpFailureMsg"));
                PlayerSchedulerUtil.performCommand(player, MODEL_SPAWN);
            }
            return;
        }
        if (MODEL_LOOK_LOCATION.equalsIgnoreCase(endModel)) {
            EntitySchedulerUtil.teleport((Entity)targetPlayer, lookLocation != null ? lookLocation : spawnLocation);
            return;
        }
        if (MODEL_SPAWN.equalsIgnoreCase(endModel)) {
            PlayerSchedulerUtil.performCommand(targetPlayer, MODEL_SPAWN);
        }
    }

    public static String pPPppP(String text) {
        int length = text.length();
        int index = length - 1;
        char[] chars = new char[length];
        int keySecond = 75;
        int cursor = index;
        int keyFirst = 30;
        while (cursor >= 0) {
            int charIndex = index--;
            chars[charIndex] = (char)(text.charAt(charIndex) ^ keyFirst);
            if (index < 0) break;
            int nextCharIndex = index--;
            chars[nextCharIndex] = (char)(text.charAt(nextCharIndex) ^ keySecond);
            cursor = index;
        }
        return new String(chars);
    }

    private GuildPvpTpUtil() {
    }
}
