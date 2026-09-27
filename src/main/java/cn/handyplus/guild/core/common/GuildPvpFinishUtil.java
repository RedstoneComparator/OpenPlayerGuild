package cn.handyplus.guild.core.common;

import cn.handyplus.guild.core.common.GuildPvpFinishResult;
import cn.handyplus.guild.core.common.GuildPvpMsgUtil;
import cn.handyplus.guild.core.common.GuildPvpTpUtil;
import cn.handyplus.guild.core.match.MatchPvpUtil;
import cn.handyplus.guild.enter.GuildInfo;
import cn.handyplus.guild.lib.BaseUtil;
import cn.handyplus.guild.lib.CollUtil;
import cn.handyplus.guild.param.GuildPvpPlayerParam;
import cn.handyplus.guild.service.GuildInfoService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GuildPvpFinishUtil {
    public static GuildPvpFinishResult finishPlayers(@NotNull Integer loserGuildId, @NotNull Integer winnerGuildId) {
        Integer[] guildIds = new Integer[2];
        guildIds[0] = loserGuildId;
        guildIds[1] = winnerGuildId;
        Map<Integer, GuildInfo> guildInfoMap = GuildInfoService.getInstance().findMapByIds(CollUtil.of(guildIds));
        GuildInfo loserGuildInfo = guildInfoMap.get(loserGuildId);
        GuildInfo winnerGuildInfo = guildInfoMap.get(winnerGuildId);
        String loserGuildName = loserGuildInfo == null ? "" : loserGuildInfo.getGuildName();
        String winnerGuildName = winnerGuildInfo == null ? "" : winnerGuildInfo.getGuildName();
        List<GuildPvpPlayerParam> pvpPlayers = new ArrayList<GuildPvpPlayerParam>();
        pvpPlayers.addAll(MatchPvpUtil.getStartPlayers(winnerGuildId));
        pvpPlayers.addAll(MatchPvpUtil.getStartPlayers(loserGuildId));
        ArrayList<UUID> playerUuids = new ArrayList<UUID>();
        for (GuildPvpPlayerParam guildPvpPlayerParam : pvpPlayers) {
            playerUuids.add(guildPvpPlayerParam.getPlayerUuid());
            Optional<Player> onlinePlayerOptional = BaseUtil.getOnlinePlayer(guildPvpPlayerParam.getPlayerUuid());
            if (!onlinePlayerOptional.isPresent()) {
                continue;
            }
            Player player = onlinePlayerOptional.get();
            GuildPvpMsgUtil.sendFinishTitle(player, winnerGuildName, loserGuildName);
            GuildPvpTpUtil.tp(MatchPvpUtil.getLookLocation(winnerGuildId), MatchPvpUtil.getOriginLocation(guildPvpPlayerParam.getPlayerUuid()), player);
        }
        return new GuildPvpFinishResult(winnerGuildName, loserGuildName, playerUuids);
    }

    private GuildPvpFinishUtil() {
    }
}
