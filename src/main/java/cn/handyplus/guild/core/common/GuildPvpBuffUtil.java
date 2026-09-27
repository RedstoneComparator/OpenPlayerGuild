package cn.handyplus.guild.core.common;

import cn.handyplus.guild.PlayerGuild;
import cn.handyplus.guild.core.match.MatchPvpUtil;
import cn.handyplus.guild.lib.BaseUtil;
import cn.handyplus.guild.lib.HandySchedulerUtil;
import cn.handyplus.guild.lib.LegacyUtil;
import cn.handyplus.guild.lib.MessageUtil;
import cn.handyplus.guild.param.GuildPvpPlayerParam;
import cn.handyplus.guild.util.BuffManageUtil;
import cn.handyplus.guild.util.ConfigUtil;
import cn.handyplus.guild.util.KitPvpUtil;
import cn.handyplus.guild.util.LeatherUtil;
import cn.handyplus.race.api.PlayerRaceApi;
import cn.handyplus.title.api.PlayerTitleApi;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GuildPvpBuffUtil {
    public static void restorePlayerBuff(@NotNull UUID playerUuid) {
        Optional<Player> onlinePlayerOptional = BaseUtil.getOnlinePlayer(playerUuid);
        if (!onlinePlayerOptional.isPresent()) {
            return;
        }
        Player player = onlinePlayerOptional.get();
        if (ConfigUtil.KIT_PVP_CONFIG.getBoolean("maxHealth.enable", false)) {
            Double startHealth = KitPvpUtil.GUILD_PVP_START_PLAYER_HEALTH_MAP.get(playerUuid);
            LegacyUtil.setMaxHealth((LivingEntity)player, startHealth);
            KitPvpUtil.GUILD_PVP_START_PLAYER_HEALTH_MAP.remove(playerUuid);
        }
        boolean enabled = ConfigUtil.KIT_PVP_CONFIG.getBoolean("playerTitleBuff", true);
        if (PlayerGuild.USE_TITLE && enabled) {
            Player titlePlayer = player;
            PlayerTitleApi.setPlayerBuff((Player)titlePlayer);
            MessageUtil.sendMessage(titlePlayer, ConfigUtil.KIT_PVP_CONFIG.getString("playerTitleBuffOnMsg"));
        }
        enabled = ConfigUtil.KIT_PVP_CONFIG.getBoolean("playerRaceBuff", true);
        if (PlayerGuild.USE_RACE && enabled) {
            PlayerRaceApi.getInstance().resetPlayerRace(player);
            MessageUtil.sendMessage(player, ConfigUtil.KIT_PVP_CONFIG.getString("playerRaceBuffOnMsg"));
        }
        if (ConfigUtil.KIT_PVP_CONFIG.getBoolean("equipment", false)) {
            LeatherUtil.setPlayerItemStack(player);
        }
        enabled = ConfigUtil.KIT_PVP_CONFIG.getBoolean("adapt", false);
        if (PlayerGuild.USE_ADAPT && enabled) {
            MessageUtil.sendMessage(player, ConfigUtil.KIT_PVP_CONFIG.getString("adaptOnMsg"));
        }
        BuffManageUtil.callBuffEvent(player);
    }

    private GuildPvpBuffUtil() {
    }

    public static void restoreBuff(@NotNull Integer firstGuildId, @NotNull Integer secondGuildId) {
        ArrayList<GuildPvpPlayerParam> playerParams;
        ArrayList<GuildPvpPlayerParam> playerParamsCopy = playerParams = new ArrayList<GuildPvpPlayerParam>();
        playerParams.addAll(MatchPvpUtil.getStartPlayers(secondGuildId));
        playerParamsCopy.addAll(MatchPvpUtil.getStartPlayers(firstGuildId));
        HandySchedulerUtil.runTaskAsynchronously(() -> playerParamsCopy.forEach(guildPvpPlayerParam -> GuildPvpBuffUtil.restorePlayerBuff(guildPvpPlayerParam.getPlayerUuid())));
    }
}
