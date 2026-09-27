package cn.handyplus.guild.event;

import java.util.List;
import java.util.Map;
import lombok.Generated;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class GuildClearMonthProsperityDegreeEvent
extends Event {
    private final static HandlerList HANDLERS = new HandlerList();
    private final Map<Integer, List<OfflinePlayer>> member;
    private final String guildName;
    private final Integer monthProsperityDegree;

    public GuildClearMonthProsperityDegreeEvent(Map<Integer, List<OfflinePlayer>> member, String guildName, Integer monthProsperityDegree) {
        super(!Bukkit.isPrimaryThread());
        this.member = member;
        this.guildName = guildName;
        this.monthProsperityDegree = monthProsperityDegree;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @NotNull
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static void callEvent(@NotNull Map<Integer, List<OfflinePlayer>> member, @NotNull String guildName, @NotNull Integer monthProsperityDegree) {
        Bukkit.getServer().getPluginManager().callEvent((Event)new GuildClearMonthProsperityDegreeEvent(member, guildName, monthProsperityDegree));
    }

    @Generated
    public Map<Integer, List<OfflinePlayer>> getMember() {
        return this.member;
    }

    @Generated
    public String getGuildName() {
        return this.guildName;
    }

    @Generated
    public Integer getMonthProsperityDegree() {
        return this.monthProsperityDegree;
    }
}
