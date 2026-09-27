package cn.handyplus.guild.param;

import java.util.UUID;
import lombok.Generated;

public class GuildPvpPlayerParam {
    private Integer meGuildId;
    private Integer rivalGuildId;
    private String playerName;
    private UUID playerUuid;
    private Integer kill;
    private Boolean isDeath;

    @Generated
    public GuildPvpPlayerParam() {
    }

    @Generated
    public Integer getMeGuildId() {
        return this.meGuildId;
    }

    @Generated
    public Integer getRivalGuildId() {
        return this.rivalGuildId;
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
    public Integer getKill() {
        return this.kill;
    }

    @Generated
    public Boolean getIsDeath() {
        return this.isDeath;
    }

    @Generated
    public void setMeGuildId(Integer meGuildId) {
        this.meGuildId = meGuildId;
    }

    @Generated
    public void setRivalGuildId(Integer rivalGuildId) {
        this.rivalGuildId = rivalGuildId;
    }

    @Generated
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    @Generated
    public void setPlayerUuid(UUID playerUuid) {
        this.playerUuid = playerUuid;
    }

    @Generated
    public void setKill(Integer kill) {
        this.kill = kill;
    }

    @Generated
    public void setIsDeath(Boolean isDeath) {
        this.isDeath = isDeath;
    }

    @Generated
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof GuildPvpPlayerParam)) {
            return false;
        }
        GuildPvpPlayerParam otherParam = (GuildPvpPlayerParam)other;
        if (!otherParam.canEqual(this)) {
            return false;
        }
        Integer thisMeGuildId = this.getMeGuildId();
        Integer otherMeGuildId = otherParam.getMeGuildId();
        if (thisMeGuildId == null ? otherMeGuildId != null : !((Object)thisMeGuildId).equals(otherMeGuildId)) {
            return false;
        }
        Integer thisRivalGuildId = this.getRivalGuildId();
        Integer otherRivalGuildId = otherParam.getRivalGuildId();
        if (thisRivalGuildId == null ? otherRivalGuildId != null : !((Object)thisRivalGuildId).equals(otherRivalGuildId)) {
            return false;
        }
        Integer thisKill = this.getKill();
        Integer otherKill = otherParam.getKill();
        if (thisKill == null ? otherKill != null : !((Object)thisKill).equals(otherKill)) {
            return false;
        }
        Boolean thisIsDeath = this.getIsDeath();
        Boolean otherIsDeath = otherParam.getIsDeath();
        if (thisIsDeath == null ? otherIsDeath != null : !((Object)thisIsDeath).equals(otherIsDeath)) {
            return false;
        }
        String thisPlayerName = this.getPlayerName();
        String otherPlayerName = otherParam.getPlayerName();
        if (thisPlayerName == null ? otherPlayerName != null : !thisPlayerName.equals(otherPlayerName)) {
            return false;
        }
        UUID thisPlayerUuid = this.getPlayerUuid();
        UUID otherPlayerUuid = otherParam.getPlayerUuid();
        return !(thisPlayerUuid == null ? otherPlayerUuid != null : !((Object)thisPlayerUuid).equals(otherPlayerUuid));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof GuildPvpPlayerParam;
    }

    @Generated
    public int hashCode() {
        int prime = 59;
        int result = 1;
        Integer meGuildId = this.getMeGuildId();
        result = result * 59 + (meGuildId == null ? 43 : ((Object)meGuildId).hashCode());
        Integer rivalGuildId = this.getRivalGuildId();
        result = result * 59 + (rivalGuildId == null ? 43 : ((Object)rivalGuildId).hashCode());
        Integer kill = this.getKill();
        result = result * 59 + (kill == null ? 43 : ((Object)kill).hashCode());
        Boolean isDeath = this.getIsDeath();
        result = result * 59 + (isDeath == null ? 43 : ((Object)isDeath).hashCode());
        String playerName = this.getPlayerName();
        result = result * 59 + (playerName == null ? 43 : playerName.hashCode());
        UUID playerUuid = this.getPlayerUuid();
        result = result * 59 + (playerUuid == null ? 43 : ((Object)playerUuid).hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "GuildPvpPlayerParam(meGuildId=" + this.getMeGuildId() + ", rivalGuildId=" + this.getRivalGuildId() + ", playerName=" + this.getPlayerName() + ", playerUuid=" + this.getPlayerUuid() + ", kill=" + this.getKill() + ", isDeath=" + this.getIsDeath() + ")";
    }
}
