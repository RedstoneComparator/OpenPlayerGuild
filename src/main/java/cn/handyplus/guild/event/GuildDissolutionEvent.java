package cn.handyplus.guild.event;

import cn.handyplus.guild.lib.BaseUtil;
import lombok.Generated;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class GuildDissolutionEvent
extends Event {
    private final static HandlerList HANDLERS = new HandlerList();
    private final Player player;
    private final Integer guildId;
    private final String guildName;

    @NotNull
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public GuildDissolutionEvent(Player player, Integer guildId, String guildName) {
        this.player = player;
        this.guildId = guildId;
        this.guildName = guildName;
    }

    public String getStripColorGuildName() {
        return BaseUtil.stripColor(this.guildName);
    }

    @Generated
    public Player getPlayer() {
        return this.player;
    }

    @Generated
    public Integer getGuildId() {
        return this.guildId;
    }

    @Generated
    public String getGuildName() {
        return this.guildName;
    }
}
