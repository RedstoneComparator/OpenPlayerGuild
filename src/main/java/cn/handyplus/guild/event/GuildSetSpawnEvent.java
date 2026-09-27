package cn.handyplus.guild.event;

import lombok.Generated;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class GuildSetSpawnEvent
extends Event
implements Cancellable {
    private final static HandlerList HANDLERS = new HandlerList();
    private final Location location;
    private final Integer guildInfoId;
    private final Player player;
    private boolean cancelled;

    public GuildSetSpawnEvent(Location location, Integer guildInfoId, Player player) {
        super(!Bukkit.isPrimaryThread());
        this.location = location;
        this.guildInfoId = guildInfoId;
        this.player = player;
    }

    @NotNull
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public static void callEvent(@NotNull Location location, @NotNull Integer guildInfoId, @NotNull Player player) {
        GuildSetSpawnEvent event = new GuildSetSpawnEvent(location, guildInfoId, player);
        Bukkit.getServer().getPluginManager().callEvent((Event)event);
    }

    @Generated
    public Location getLocation() {
        return this.location;
    }

    @Generated
    public Integer getGuildInfoId() {
        return this.guildInfoId;
    }

    @Generated
    public Player getPlayer() {
        return this.player;
    }
}
