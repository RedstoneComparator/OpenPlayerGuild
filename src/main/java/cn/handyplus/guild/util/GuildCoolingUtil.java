package cn.handyplus.guild.util;

import cn.handyplus.guild.enter.GuildPlayerCooling;
import cn.handyplus.guild.lib.BaseConstants;
import cn.handyplus.guild.lib.BaseUtil;
import cn.handyplus.guild.lib.DateUtil;
import cn.handyplus.guild.lib.MessageUtil;
import cn.handyplus.guild.lib.StrUtil;
import cn.handyplus.guild.service.GuildPlayerCoolingService;
import cn.handyplus.guild.util.ConfigUtil;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import org.bukkit.entity.Player;

public class GuildCoolingUtil {
    private GuildCoolingUtil() {
    }

    public static String pPPppP(String value) {
        int length = value.length();
        int index = length - 1;
        char[] chars = new char[length];
        int xorKeyOne = 92;
        int remaining = index;
        int xorKeyTwo = 49;
        while (remaining >= 0) {
            int posOne = index--;
            chars[posOne] = (char)(value.charAt(posOne) ^ xorKeyTwo);
            if (index < 0) break;
            int posTwo = index--;
            chars[posTwo] = (char)(value.charAt(posTwo) ^ xorKeyOne);
            remaining = index;
        }
        return new String(chars);
    }

    public static String getInvitationCoolingButton(UUID playerUuid) {
        long remainingHours = GuildCoolingUtil.getJoinCoolingRemainHours(playerUuid);
        return StrUtil.replace(ConfigUtil.INVITATION_CONFIG.getString("member.coolingButton", ""), "${time}", String.valueOf(remainingHours));
    }

    public static boolean checkJoinCooling(Player player) {
        long remainingHours = GuildCoolingUtil.getJoinCoolingRemainHours(player.getUniqueId());
        if (remainingHours > 0L) {
            String message = BaseUtil.getLangMsg("joinCoolingMsg").replace("${time}", String.valueOf(remainingHours));
            MessageUtil.sendMessage(player, message);
            return true;
        }
        return false;
    }

    public static long getJoinCoolingRemainHours(UUID playerUuid) {
        Optional<GuildPlayerCooling> optional = GuildPlayerCoolingService.getInstance().findByPlayerUuid(playerUuid);
        if (!optional.isPresent()) {
            return 0L;
        }
        GuildPlayerCooling guildPlayerCooling = optional.get();
        int coolingHours = BaseConstants.CONFIG.getInt("joinCooling", 0);
        if (coolingHours <= 0) {
            GuildPlayerCoolingService.getInstance().delByPlayerUuid(playerUuid);
            return 0L;
        }
        long remainingHours = DateUtil.between(guildPlayerCooling.getLeaveTime(), new Date());
        if ((remainingHours = (long)coolingHours - remainingHours) <= 0L) {
            GuildPlayerCoolingService.getInstance().delByPlayerUuid(playerUuid);
            return 0L;
        }
        return remainingHours;
    }
}
