package cn.handyplus.guild.event;

import cn.handyplus.guild.constants.GuildActivityType;
import lombok.Generated;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class GuildPvpFinishEvent
extends Event {
    private final static HandlerList HANDLERS = new HandlerList();
    private final GuildActivityType activityType;
    private final Integer winGuildId;
    private final Integer failGuildId;
    private final String winGuildName;
    private final String failGuildName;

    public GuildPvpFinishEvent(@NotNull GuildActivityType activityType, @NotNull Integer winGuildId, @NotNull Integer failGuildId, @NotNull String winGuildName, @NotNull String failGuildName) {
        this.activityType = activityType;
        this.winGuildId = winGuildId;
        this.failGuildId = failGuildId;
        this.winGuildName = winGuildName;
        this.failGuildName = failGuildName;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @NotNull
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static void callEvent(@NotNull GuildActivityType activityType, @NotNull Integer winGuildId, @NotNull Integer failGuildId, @NotNull String winGuildName, @NotNull String failGuildName) {
        Bukkit.getServer().getPluginManager().callEvent((Event)new GuildPvpFinishEvent(activityType, winGuildId, failGuildId, winGuildName, failGuildName));
    }

    @Generated
    public GuildActivityType getActivityType() {
        return this.activityType;
    }

    @Generated
    public Integer getWinGuildId() {
        return this.winGuildId;
    }

    @Generated
    public Integer getFailGuildId() {
        return this.failGuildId;
    }

    @Generated
    public String getWinGuildName() {
        return this.winGuildName;
    }

    @Generated
    public String getFailGuildName() {
        return this.failGuildName;
    }
}
