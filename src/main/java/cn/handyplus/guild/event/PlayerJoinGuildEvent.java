package cn.handyplus.guild.event;

import cn.handyplus.guild.lib.HandySchedulerUtil;
import java.util.UUID;
import lombok.Generated;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayerJoinGuildEvent
extends Event {
    private final static HandlerList HANDLERS = new HandlerList();
    private final String playerName;
    private final UUID playerUuid;
    private final Integer guildInfoId;

    @NotNull
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public PlayerJoinGuildEvent(String playerName, UUID playerUuid, Integer guildInfoId) {
        this.playerName = playerName;
        this.playerUuid = playerUuid;
        this.guildInfoId = guildInfoId;
    }

    public static void callEvent(String playerName, UUID playerUuid, Integer guildInfoId) {
        HandySchedulerUtil.runTask(() -> Bukkit.getServer().getPluginManager().callEvent((Event)new PlayerJoinGuildEvent(playerName, playerUuid, guildInfoId)));
    }

    @Generated
    public String getPlayerName() {
        return this.playerName;
    }

    @Generated
    public UUID getPlayerUuid() {
        return this.playerUuid;
    }

    @Generated
    public Integer getGuildInfoId() {
        return this.guildInfoId;
    }
}
