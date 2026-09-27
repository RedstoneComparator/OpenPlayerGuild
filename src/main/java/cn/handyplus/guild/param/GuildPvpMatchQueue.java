package cn.handyplus.guild.param;

import java.util.UUID;
import lombok.Generated;

public class GuildPvpMatchQueue {
    private Integer guildId;
    private UUID playerUuid;
    private Integer waitSecond;

    @Generated
    public static GuildPvpMatchQueueBuilder builder() {
        return new GuildPvpMatchQueueBuilder();
    }

    @Generated
    public Integer getGuildId() {
        return this.guildId;
    }

    @Generated
    public UUID getPlayerUuid() {
        return this.playerUuid;
    }

    @Generated
    public Integer getWaitSecond() {
        return this.waitSecond;
    }

    @Generated
    public void setGuildId(Integer guildId) {
        this.guildId = guildId;
    }

    @Generated
    public void setPlayerUuid(UUID playerUuid) {
        this.playerUuid = playerUuid;
    }

    @Generated
    public void setWaitSecond(Integer waitSecond) {
        this.waitSecond = waitSecond;
    }

    @Generated
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof GuildPvpMatchQueue)) {
            return false;
        }
        GuildPvpMatchQueue otherQueue = (GuildPvpMatchQueue)other;
        if (!otherQueue.canEqual(this)) {
            return false;
        }
        Integer thisGuildId = this.getGuildId();
        Integer otherGuildId = otherQueue.getGuildId();
        if (thisGuildId == null ? otherGuildId != null : !((Object)thisGuildId).equals(otherGuildId)) {
            return false;
        }
        Integer thisWaitSecond = this.getWaitSecond();
        Integer otherWaitSecond = otherQueue.getWaitSecond();
        if (thisWaitSecond == null ? otherWaitSecond != null : !((Object)thisWaitSecond).equals(otherWaitSecond)) {
            return false;
        }
        UUID thisPlayerUuid = this.getPlayerUuid();
        UUID otherPlayerUuid = otherQueue.getPlayerUuid();
        return !(thisPlayerUuid == null ? otherPlayerUuid != null : !((Object)thisPlayerUuid).equals(otherPlayerUuid));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof GuildPvpMatchQueue;
    }

    @Generated
    public int hashCode() {
        int prime = 59;
        int result = 1;
        Integer guildId = this.getGuildId();
        result = result * 59 + (guildId == null ? 43 : ((Object)guildId).hashCode());
        Integer waitSecond = this.getWaitSecond();
        result = result * 59 + (waitSecond == null ? 43 : ((Object)waitSecond).hashCode());
        UUID playerUuid = this.getPlayerUuid();
        result = result * 59 + (playerUuid == null ? 43 : ((Object)playerUuid).hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "GuildPvpMatchQueue(guildId=" + this.getGuildId() + ", playerUuid=" + this.getPlayerUuid() + ", waitSecond=" + this.getWaitSecond() + ")";
    }

    @Generated
    public GuildPvpMatchQueue(Integer guildId, UUID playerUuid, Integer waitSecond) {
        this.guildId = guildId;
        this.playerUuid = playerUuid;
        this.waitSecond = waitSecond;
    }

    @Generated
    public GuildPvpMatchQueue() {
    }

    @Generated
    public static class GuildPvpMatchQueueBuilder {
        @Generated
        private Integer guildId;
        @Generated
        private UUID playerUuid;
        @Generated
        private Integer waitSecond;

        @Generated
        GuildPvpMatchQueueBuilder() {
        }

        @Generated
        public GuildPvpMatchQueueBuilder guildId(Integer guildId) {
            this.guildId = guildId;
            return this;
        }

        @Generated
        public GuildPvpMatchQueueBuilder playerUuid(UUID playerUuid) {
            this.playerUuid = playerUuid;
            return this;
        }

        @Generated
        public GuildPvpMatchQueueBuilder waitSecond(Integer waitSecond) {
            this.waitSecond = waitSecond;
            return this;
        }

        @Generated
        public GuildPvpMatchQueue build() {
            return new GuildPvpMatchQueue(this.guildId, this.playerUuid, this.waitSecond);
        }

        @Generated
        public String toString() {
            return "GuildPvpMatchQueue.GuildPvpMatchQueueBuilder(guildId=" + this.guildId + ", playerUuid=" + this.playerUuid + ", waitSecond=" + this.waitSecond + ")";
        }
    }
}
