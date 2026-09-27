package cn.handyplus.guild.event;

import java.util.List;
import lombok.Generated;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class GuildBuffEvent
extends Event {
    private final static HandlerList HANDLERS = new HandlerList();
    private final Player player;
    private final List<String> buffList;

    @NotNull
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public GuildBuffEvent(Player player, List<String> buffList) {
        super(!Bukkit.isPrimaryThread());
        this.player = player;
        this.buffList = buffList;
    }

    @Generated
    public Player getPlayer() {
        return this.player;
    }

    @Generated
    public List<String> getBuffList() {
        return this.buffList;
    }
}
