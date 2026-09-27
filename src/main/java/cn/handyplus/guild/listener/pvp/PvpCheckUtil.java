package cn.handyplus.guild.listener.pvp;

import cn.handyplus.guild.constants.GuildConstants;
import cn.handyplus.guild.core.match.MatchPvpUtil;
import cn.handyplus.guild.core.score.ScorePvpUtil;
import cn.handyplus.guild.core.season.SeasonPvpUtil;
import cn.handyplus.guild.core.stronghold.StrongholdPvpUtil;
import cn.handyplus.guild.enter.GuildPlayer;
import cn.handyplus.guild.lib.CollUtil;
import cn.handyplus.guild.util.ConfigUtil;
import java.util.List;
import org.bukkit.entity.Player;

public final class PvpCheckUtil {
    public static boolean check(Player attacker, Player defender) {
        if (!ConfigUtil.SETTING_CONFIG.getBoolean("pvp.enable", false)) {
            return false;
        }
        List noWorldList = ConfigUtil.SETTING_CONFIG.getStringList("pvp.noWorld");
        if (CollUtil.isNotEmpty(noWorldList) && noWorldList.contains(attacker.getWorld().getName())) {
            return false;
        }
        if (GuildConstants.PLAYER_API_PVP_MAP.get(attacker.getUniqueId()) != null || GuildConstants.PLAYER_API_PVP_MAP.get(defender.getUniqueId()) != null) {
            return false;
        }
        if (PvpCheckUtil.J(attacker, defender)) {
            return true;
        }
        if (PvpCheckUtil.isScoreRunning(attacker, defender)) {
            return true;
        }
        if (PvpCheckUtil.isSeasonRunning(attacker, defender)) {
            return true;
        }
        if (PvpCheckUtil.isStrongholdRunning(attacker, defender)) {
            return true;
        }
        return PvpCheckUtil.isSameGuildPvp(attacker, defender);
    }

    private static boolean isSeasonRunning(Player attacker, Player defender) {
        if (SeasonPvpUtil.isRunning(defender.getUniqueId()) && !SeasonPvpUtil.isRunning(attacker.getUniqueId())) {
            return true;
        }
        return false;
    }

    private PvpCheckUtil() {
    }

    private static boolean isSameGuildPvp(Player attacker, Player defender) {
        GuildPlayer attackerGuild = GuildConstants.PLAYER_GUILD_MAP.get(attacker.getUniqueId());
        GuildPlayer defenderGuild = GuildConstants.PLAYER_GUILD_MAP.get(defender.getUniqueId());
        if (attackerGuild != null && defenderGuild != null && attackerGuild.getGuildInfoId().equals(defenderGuild.getGuildInfoId()) && attackerGuild.getPvpStatus().booleanValue() && defenderGuild.getPvpStatus().booleanValue()) {
            return true;
        }
        return false;
    }

    private static boolean isStrongholdRunning(Player attacker, Player defender) {
        if (StrongholdPvpUtil.isRunning(defender.getUniqueId()) && !StrongholdPvpUtil.isRunning(attacker.getUniqueId())) {
            return true;
        }
        return false;
    }

    private static boolean J(Player attacker, Player defender) {
        if (MatchPvpUtil.isMatchRunning(defender.getUniqueId()) && !MatchPvpUtil.isMatchRunning(attacker.getUniqueId())) {
            return true;
        }
        return false;
    }

    private static boolean isScoreRunning(Player attacker, Player defender) {
        if (ScorePvpUtil.isRunning(defender.getUniqueId()) && !ScorePvpUtil.isRunning(attacker.getUniqueId())) {
            return true;
        }
        return false;
    }
}
