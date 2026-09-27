package cn.handyplus.guild.util;

import cn.handyplus.guild.constants.GuildConstants;
import cn.handyplus.guild.enter.GuildInfo;
import cn.handyplus.guild.enter.GuildPlayer;
import cn.handyplus.guild.lib.BaseConstants;
import cn.handyplus.guild.lib.BaseUtil;
import cn.handyplus.guild.lib.CollUtil;
import cn.handyplus.guild.service.GuildInfoService;
import cn.handyplus.guild.service.GuildPlayerService;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;

public class GuildCacheUtil {
    @NotNull
    public static GuildPlayer getGuildPlayer(@NotNull UUID playerUuid) {
        GuildPlayer guildPlayer = GuildConstants.PLAYER_GUILD_MAP.get(playerUuid);
        if (guildPlayer == null) {
            GuildPlayer guildPlayer2 = GuildConstants.PLAYER_GUILD_OFFLINE_CACHE_MAP.get(playerUuid);
            if (guildPlayer2 != null) {
                guildPlayer = guildPlayer2;
                return guildPlayer;
            }
            guildPlayer = GuildPlayerService.getInstance().findByPlayerUuidAndGuildInfo(playerUuid).orElse(new GuildPlayer());
            GuildCacheUtil.setCache(playerUuid, guildPlayer);
        }
        return guildPlayer;
    }

    public static void refreshByPlayerName(String playerName) {
        Optional<GuildPlayer> optional = GuildPlayerService.getInstance().findByPlayerNameAndGuildInfo(playerName);
        if (!optional.isPresent()) {
            return;
        }
        GuildPlayer guildPlayer = optional.get();
        GuildCacheUtil.setCache(guildPlayer.getPlayerUuid(), guildPlayer);
    }

    public static void setCache(UUID playerUuid, GuildPlayer guildPlayer) {
        GuildCacheUtil.remove(playerUuid);
        if (guildPlayer == null) {
            return;
        }
        if (BaseUtil.getOnlinePlayer(playerUuid).isPresent()) {
            if (guildPlayer.getId() != null) {
                GuildConstants.PLAYER_GUILD_MAP.put(playerUuid, guildPlayer);
                return;
            }
        } else if (BaseConstants.CONFIG.getBoolean("isOfflinePapiCache")) {
            GuildConstants.PLAYER_GUILD_OFFLINE_CACHE_MAP.put(playerUuid, guildPlayer);
        }
    }

    public static void refresh(Integer guildId) {
        Iterator<GuildPlayer> guildPlayerIterator;
        Iterator<GuildPlayer> guildPlayerIterator2 = guildPlayerIterator = GuildPlayerService.getInstance().findByGuildIdAndGuildInfo(guildId).iterator();
        while (guildPlayerIterator2.hasNext()) {
            GuildPlayer guildPlayer = guildPlayerIterator.next();
            guildPlayerIterator2 = guildPlayerIterator;
            GuildCacheUtil.setCache(guildPlayer.getPlayerUuid(), guildPlayer);
        }
    }

    public static void refresh(UUID playerUuid) {
        if (playerUuid == null) {
            return;
        }
        GuildCacheUtil.refresh(Collections.singletonList(playerUuid));
    }

    public static void refresh(String guildName) {
        Optional<GuildInfo> optional = GuildInfoService.getInstance().findByGuildName(guildName);
        if (!optional.isPresent()) {
            return;
        }
        GuildCacheUtil.refresh(optional.get().getId());
    }

    public static void initGuildWorldCache() {
        GuildConstants.GUILD_WORLD_MAP.clear();
        GuildConstants.GUILD_WORLD_MAP.putAll(GuildInfoService.getInstance().findAllWorldName());
    }

    public static void refresh(List<UUID> playerUuids) {
        Iterator<UUID> playerUuidIterator;
        if (CollUtil.isEmpty(playerUuids)) {
            return;
        }
        Map<UUID, GuildPlayer> playerMap = GuildPlayerService.getInstance().findByPlayerUuidListAndGuildInfo(playerUuids).stream().collect(Collectors.toMap(GuildPlayer::getPlayerUuid, uuidKey -> uuidKey));
        Iterator<UUID> playerUuidIterator2 = playerUuidIterator = playerUuids.iterator();
        while (playerUuidIterator2.hasNext()) {
            UUID playerUuid;
            UUID playerUuid2 = playerUuid = playerUuidIterator.next();
            GuildCacheUtil.setCache(playerUuid2, playerMap.get(playerUuid2));
            playerUuidIterator2 = playerUuidIterator;
        }
    }

    public static void remove(UUID playerUuid) {
        GuildConstants.PLAYER_GUILD_MAP.remove(playerUuid);
        GuildConstants.PLAYER_GUILD_OFFLINE_CACHE_MAP.remove(playerUuid);
    }
}
