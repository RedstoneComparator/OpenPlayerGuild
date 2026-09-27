package cn.handyplus.guild.event;

import cn.handyplus.guild.lib.HandySchedulerUtil;
import lombok.Generated;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class GuildCreateEvent
extends Event {
    private final static HandlerList HANDLERS = new HandlerList();
    private final Player player;
    private final String guildName;

    public GuildCreateEvent(Player player, String guildName) {
        this.player = player;
        this.guildName = guildName;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @NotNull
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static void callEvent(@NotNull Player player, @NotNull String guildName) {
        HandySchedulerUtil.runTask(() -> Bukkit.getServer().getPluginManager().callEvent((Event)new GuildCreateEvent(player, guildName)));
    }

    @Generated
    public Player getPlayer() {
        return this.player;
    }

    @Generated
    public String getGuildName() {
        return this.guildName;
    }
}
