package cn.handyplus.guild.event;

import cn.handyplus.guild.enter.GuildInfo;
import lombok.Generated;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class GuildUpEvent
extends Event
implements Cancellable {
    private final static HandlerList HANDLERS = new HandlerList();
    private boolean cancelled;
    private final GuildInfo guildInfo;
    private final Integer newLevel;
    private final CommandSender commandSender;

    public GuildUpEvent(@NotNull GuildInfo guildInfo, @NotNull Integer newLevel, @NotNull CommandSender commandSender) {
        this.guildInfo = guildInfo;
        this.newLevel = newLevel;
        this.commandSender = commandSender;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @NotNull
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Generated
    public GuildInfo getGuildInfo() {
        return this.guildInfo;
    }

    @Generated
    public Integer getNewLevel() {
        return this.newLevel;
    }

    @Generated
    public CommandSender getCommandSender() {
        return this.commandSender;
    }
}
