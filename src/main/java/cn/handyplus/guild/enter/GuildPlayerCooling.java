package cn.handyplus.guild.enter;

import cn.handyplus.guild.lib.IndexEnum;
import cn.handyplus.guild.lib.TableField;
import cn.handyplus.guild.lib.TableName;
import java.util.Date;
import java.util.UUID;
import lombok.Generated;

@TableName(value="guild_player_cooling", comment="\u73a9\u5bb6\u51b7\u5374")
public class GuildPlayerCooling {
    @TableField(value="id", comment="ID")
    private Integer id;
    @TableField(value="player_name", comment="\u73a9\u5bb6\u540d\u79f0", notNull=true)
    private String playerName;
    @TableField(value="player_uuid", comment="\u73a9\u5bb6uuid", notNull=true, indexEnum=IndexEnum.UNIQUE)
    private UUID playerUuid;
    @TableField(value="leave_time", comment="\u79bb\u5f00\u65f6\u95f4", notNull=true)
    private Date leaveTime;

    @Generated
    public GuildPlayerCooling() {
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
    public Date getLeaveTime() {
        return this.leaveTime;
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
    public void setLeaveTime(Date leaveTime) {
        this.leaveTime = leaveTime;
    }

    @Generated
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof GuildPlayerCooling)) {
            return false;
        }
        GuildPlayerCooling that = (GuildPlayerCooling)other;
        if (!that.canEqual(this)) {
            return false;
        }
        Integer id = this.getId();
        Integer otherId = that.getId();
        if (id == null ? otherId != null : !((Object)id).equals(otherId)) {
            return false;
        }
        String playerName = this.getPlayerName();
        String otherPlayerName = that.getPlayerName();
        if (playerName == null ? otherPlayerName != null : !playerName.equals(otherPlayerName)) {
            return false;
        }
        UUID playerUuid = this.getPlayerUuid();
        UUID otherPlayerUuid = that.getPlayerUuid();
        if (playerUuid == null ? otherPlayerUuid != null : !((Object)playerUuid).equals(otherPlayerUuid)) {
            return false;
        }
        Date leaveTime = this.getLeaveTime();
        Date otherLeaveTime = that.getLeaveTime();
        return !(leaveTime == null ? otherLeaveTime != null : !((Object)leaveTime).equals(otherLeaveTime));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof GuildPlayerCooling;
    }

    @Generated
    public int hashCode() {
        int hashSeed = 59;
        int result = 1;
        Integer id = this.getId();
        result = result * 59 + (id == null ? 43 : ((Object)id).hashCode());
        String playerName = this.getPlayerName();
        result = result * 59 + (playerName == null ? 43 : playerName.hashCode());
        UUID playerUuid = this.getPlayerUuid();
        result = result * 59 + (playerUuid == null ? 43 : ((Object)playerUuid).hashCode());
        Date leaveTime = this.getLeaveTime();
        result = result * 59 + (leaveTime == null ? 43 : ((Object)leaveTime).hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "GuildPlayerCooling(id=" + this.getId() + ", playerName=" + this.getPlayerName() + ", playerUuid=" + this.getPlayerUuid() + ", leaveTime=" + this.getLeaveTime() + ")";
    }
}
