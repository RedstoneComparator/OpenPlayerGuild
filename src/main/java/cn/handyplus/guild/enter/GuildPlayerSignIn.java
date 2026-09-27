package cn.handyplus.guild.enter;

import cn.handyplus.guild.lib.IndexEnum;
import cn.handyplus.guild.lib.TableField;
import cn.handyplus.guild.lib.TableName;
import java.util.Date;
import java.util.UUID;
import lombok.Generated;

@TableName(value="guild_player_sign_in", comment="\u516c\u4f1a\u73a9\u5bb6\u7b7e\u5230\u8bb0\u5f55")
public class GuildPlayerSignIn {
    @TableField(value="id", comment="ID")
    private Integer id;
    @TableField(value="player_name", comment="\u73a9\u5bb6\u540d\u79f0", notNull=true)
    private String playerName;
    @TableField(value="player_uuid", comment="\u73a9\u5bb6uuid", notNull=true, indexEnum=IndexEnum.INDEX)
    private UUID playerUuid;
    @TableField(value="sign_in_time", comment="\u7b7e\u5230\u65f6\u95f4", notNull=true)
    private Date signInTime;

    @Generated
    public GuildPlayerSignIn() {
    }

    @Generated
    public Integer getId() {
        return this.id;
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
    public Date getSignInTime() {
        return this.signInTime;
    }

    @Generated
    public void setId(Integer id) {
        this.id = id;
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
    public void setSignInTime(Date signInTime) {
        this.signInTime = signInTime;
    }

    @Generated
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof GuildPlayerSignIn)) {
            return false;
        }
        GuildPlayerSignIn otherGuild = (GuildPlayerSignIn)other;
        if (!otherGuild.canEqual(this)) {
            return false;
        }
        Integer thisId = this.getId();
        Integer otherId = otherGuild.getId();
        if (thisId == null ? otherId != null : !((Object)thisId).equals(otherId)) {
            return false;
        }
        String thisPlayerName = this.getPlayerName();
        String otherPlayerName = otherGuild.getPlayerName();
        if (thisPlayerName == null ? otherPlayerName != null : !thisPlayerName.equals(otherPlayerName)) {
            return false;
        }
        UUID thisPlayerUuid = this.getPlayerUuid();
        UUID otherPlayerUuid = otherGuild.getPlayerUuid();
        if (thisPlayerUuid == null ? otherPlayerUuid != null : !((Object)thisPlayerUuid).equals(otherPlayerUuid)) {
            return false;
        }
        Date thisSignInTime = this.getSignInTime();
        Date otherSignInTime = otherGuild.getSignInTime();
        return !(thisSignInTime == null ? otherSignInTime != null : !((Object)thisSignInTime).equals(otherSignInTime));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof GuildPlayerSignIn;
    }

    @Generated
    public int hashCode() {
        int prime = 59;
        int hash = 1;
        Integer idValue = this.getId();
        hash = hash * 59 + (idValue == null ? 43 : ((Object)idValue).hashCode());
        String playerNameValue = this.getPlayerName();
        hash = hash * 59 + (playerNameValue == null ? 43 : playerNameValue.hashCode());
        UUID playerUuidValue = this.getPlayerUuid();
        hash = hash * 59 + (playerUuidValue == null ? 43 : ((Object)playerUuidValue).hashCode());
        Date signInTimeValue = this.getSignInTime();
        hash = hash * 59 + (signInTimeValue == null ? 43 : ((Object)signInTimeValue).hashCode());
        return hash;
    }

    @Generated
    public String toString() {
        return "GuildPlayerSignIn(id=" + this.getId() + ", playerName=" + this.getPlayerName() + ", playerUuid=" + this.getPlayerUuid() + ", signInTime=" + this.getSignInTime() + ")";
    }
}
