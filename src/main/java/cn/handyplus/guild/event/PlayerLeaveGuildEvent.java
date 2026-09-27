package cn.handyplus.guild.event;

import cn.handyplus.guild.lib.HandySchedulerUtil;
import lombok.Generated;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayerLeaveGuildEvent
extends Event {
    private final static HandlerList HANDLERS = new HandlerList();
    private final Player player;
    private final OfflinePlayer offlinePlayer;
    private final Integer guildPlayerId;
    private final String leaveType;

    @NotNull
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public PlayerLeaveGuildEvent(Player player, Integer guildPlayerId) {
        this.player = player;
        this.guildPlayerId = guildPlayerId;
        this.leaveType = "activeLeave";
        this.offlinePlayer = null;
    }

    public PlayerLeaveGuildEvent(OfflinePlayer offlinePlayer, Integer guildPlayerId) {
        this.player = null;
        this.guildPlayerId = guildPlayerId;
        this.leaveType = "kickedLeave";
        this.offlinePlayer = offlinePlayer;
    }

    public static void callEvent(@NotNull Player player, @NotNull Integer guildPlayerId) {
        HandySchedulerUtil.runTask(() -> Bukkit.getServer().getPluginManager().callEvent((Event)new PlayerLeaveGuildEvent(player, guildPlayerId)));
    }

    public static void callEvent(@NotNull OfflinePlayer offlinePlayer, @NotNull Integer guildPlayerId) {
        HandySchedulerUtil.runTask(() -> Bukkit.getServer().getPluginManager().callEvent((Event)new PlayerLeaveGuildEvent(offlinePlayer, guildPlayerId)));
    }

    @Generated
    public Player getPlayer() {
        return this.player;
    }

    @Generated
    public OfflinePlayer getOfflinePlayer() {
        return this.offlinePlayer;
    }

    @Generated
    public Integer getGuildPlayerId() {
        return this.guildPlayerId;
    }

    @Generated
    public String getLeaveType() {
        return this.leaveType;
    }
}
