package cn.handyplus.guild.core.season;

import cn.handyplus.guild.constants.GuildPvpRoomStatusEnum;
import cn.handyplus.guild.core.season.SeasonPvpManager;
import cn.handyplus.guild.core.season.SeasonPvpPlayer;
import cn.handyplus.guild.core.season.SeasonPvpRoom;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Color;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SeasonPvpUtil {
    public static boolean isRunning(@NotNull Integer guildId) {
        SeasonPvpRoom seasonPvpRoom = SeasonPvpManager.OOooOo();
        if (seasonPvpRoom != null && GuildPvpRoomStatusEnum.RUNNING.equals((Object)seasonPvpRoom.getStatus()) && seasonPvpRoom.getStartPlayerMap() != null && seasonPvpRoom.getStartPlayerMap().containsKey(guildId)) {
            return true;
        }
        return false;
    }

    private SeasonPvpUtil() {
    }

    public static boolean isRunning() {
        SeasonPvpRoom seasonPvpRoom = SeasonPvpManager.OOooOo();
        if (seasonPvpRoom != null && GuildPvpRoomStatusEnum.RUNNING.equals((Object)seasonPvpRoom.getStatus()) && seasonPvpRoom.getStartPlayerMap() != null && !seasonPvpRoom.getStartPlayerMap().isEmpty()) {
            return true;
        }
        return false;
    }

    public static boolean isActive(@NotNull Integer guildId) {
        SeasonPvpRoom seasonPvpRoom = SeasonPvpManager.OOooOo();
        if (SeasonPvpUtil.isPreparing(guildId) || SeasonPvpUtil.isRunning(guildId) || seasonPvpRoom != null && GuildPvpRoomStatusEnum.STARTING.equals((Object)seasonPvpRoom.getStatus()) && seasonPvpRoom.getGuildIdList() != null && seasonPvpRoom.getGuildIdList().contains(guildId)) {
            return true;
        }
        return false;
    }

    public static boolean isPreparing(@NotNull UUID playerUuid) {
        SeasonPvpRoom seasonPvpRoom = SeasonPvpManager.OOooOo();
        if (seasonPvpRoom == null || !GuildPvpRoomStatusEnum.PREPARING.equals((Object)seasonPvpRoom.getStatus()) || seasonPvpRoom.getPreparationPlayerMap() == null) {
            return false;
        }
        Iterator<List<UUID>> preparationIterator = seasonPvpRoom.getPreparationPlayerMap().values().iterator();
        while (preparationIterator.hasNext()) {
            if (!preparationIterator.next().contains(playerUuid)) continue;
            return true;
        }
        return false;
    }

    @Nullable
    public static Integer getGuildIdByPlayer(@NotNull UUID playerUuid) {
        SeasonPvpRoom seasonPvpRoom = SeasonPvpManager.OOooOo();
        if (seasonPvpRoom == null || seasonPvpRoom.getStartGuildPlayerMap() == null) {
            return null;
        }
        return seasonPvpRoom.getStartGuildPlayerMap().get(playerUuid);
    }

    public static boolean isPreparing(@NotNull Integer guildId) {
        SeasonPvpRoom seasonPvpRoom = SeasonPvpManager.OOooOo();
        if (seasonPvpRoom != null && GuildPvpRoomStatusEnum.PREPARING.equals((Object)seasonPvpRoom.getStatus()) && seasonPvpRoom.getPreparationPlayerMap() != null && seasonPvpRoom.getPreparationPlayerMap().containsKey(guildId)) {
            return true;
        }
        return false;
    }

    @Nullable
    public static Location getLookLocation() {
        SeasonPvpRoom seasonPvpRoom = SeasonPvpManager.OOooOo();
        if (seasonPvpRoom == null) {
            return null;
        }
        return seasonPvpRoom.getLookLocation();
    }

    @NotNull
    public static List<UUID> getPreparationPlayers(@NotNull Integer guildId) {
        SeasonPvpRoom seasonPvpRoom = SeasonPvpManager.OOooOo();
        if (seasonPvpRoom == null || seasonPvpRoom.getPreparationPlayerMap() == null) {
            return new ArrayList<UUID>();
        }
        return new ArrayList<UUID>(seasonPvpRoom.getPreparationPlayerMap().getOrDefault(guildId, new ArrayList()));
    }

    public static List<SeasonPvpPlayer> getStartPlayers(@NotNull Integer guildId) {
        SeasonPvpRoom seasonPvpRoom = SeasonPvpManager.OOooOo();
        if (seasonPvpRoom == null || seasonPvpRoom.getStartPlayerMap() == null) {
            return new ArrayList<SeasonPvpPlayer>();
        }
        return new ArrayList<SeasonPvpPlayer>(seasonPvpRoom.getStartPlayerMap().getOrDefault(guildId, new ArrayList()));
    }

    public static boolean isRunning(@NotNull UUID playerUuid) {
        SeasonPvpRoom seasonPvpRoom = SeasonPvpManager.OOooOo();
        if (seasonPvpRoom != null && GuildPvpRoomStatusEnum.RUNNING.equals((Object)seasonPvpRoom.getStatus()) && seasonPvpRoom.getStartGuildPlayerMap() != null && seasonPvpRoom.getStartGuildPlayerMap().containsKey(playerUuid)) {
            return true;
        }
        return false;
    }

    public static Map<Integer, Color> getColorMap() {
        HashMap<Integer, Color> colorMap = Maps.newHashMapWithExpectedSize(16);
        colorMap.put(0, Color.RED);
        colorMap.put(1, Color.BLUE);
        colorMap.put(2, Color.GRAY);
        colorMap.put(3, Color.BLACK);
        colorMap.put(4, Color.WHITE);
        colorMap.put(5, Color.MAROON);
        colorMap.put(6, Color.YELLOW);
        colorMap.put(7, Color.OLIVE);
        colorMap.put(8, Color.LIME);
        colorMap.put(9, Color.GREEN);
        colorMap.put(10, Color.AQUA);
        colorMap.put(11, Color.TEAL);
        colorMap.put(12, Color.SILVER);
        colorMap.put(13, Color.NAVY);
        colorMap.put(14, Color.FUCHSIA);
        colorMap.put(15, Color.PURPLE);
        colorMap.put(16, Color.ORANGE);
        return colorMap;
    }
}
