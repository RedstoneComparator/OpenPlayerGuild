package cn.handyplus.guild.enter;

import cn.handyplus.guild.lib.IndexEnum;
import cn.handyplus.guild.lib.TableField;
import cn.handyplus.guild.lib.TableName;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;
import lombok.Generated;

@TableName(value="guild_apply", comment="\u516c\u4f1a\u7533\u8bf7\u8bb0\u5f55")
public class GuildApply
implements Serializable {
    @TableField(value="id", comment="ID")
    private Integer id;
    @TableField(value="guild_info_id", comment="\u516c\u4f1aid", notNull=true, indexEnum=IndexEnum.INDEX)
    private Integer guildInfoId;
    @TableField(value="player_name", comment="\u73a9\u5bb6\u540d\u79f0", notNull=true)
    private String playerName;
    @TableField(value="player_uuid", comment="\u73a9\u5bb6uuid", notNull=true)
    private UUID playerUuid;
    @TableField(value="apply_time", comment="\u7533\u8bf7\u65f6\u95f4", notNull=true)
    private Date applyTime;
    @TableField(value="apply_result", comment="\u5ba1\u6279\u7ed3\u679c", notNull=true)
    private Integer applyResult;
    @TableField(value="apply_player_name", comment="\u5ba1\u6279\u73a9\u5bb6\u540d\u79f0")
    private String applyPlayerName;
    @TableField(value="apply_player_uuid", comment="\u5ba1\u6279\u73a9\u5bb6uuid")
    private UUID applyPlayerUuid;

    @Generated
    public GuildApply() {
    }

    @Generated
    public Integer getId() {
        return this.id;
    }

    @Generated
    public Integer getGuildInfoId() {
        return this.guildInfoId;
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
    public Date getApplyTime() {
        return this.applyTime;
    }

    @Generated
    public Integer getApplyResult() {
        return this.applyResult;
    }

    @Generated
    public String getApplyPlayerName() {
        return this.applyPlayerName;
    }

    @Generated
    public UUID getApplyPlayerUuid() {
        return this.applyPlayerUuid;
    }

    @Generated
    public void setId(Integer id) {
        this.id = id;
    }

    @Generated
    public void setGuildInfoId(Integer guildInfoId) {
        this.guildInfoId = guildInfoId;
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
    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }

    @Generated
    public void setApplyResult(Integer applyResult) {
        this.applyResult = applyResult;
    }

    @Generated
    public void setApplyPlayerName(String applyPlayerName) {
        this.applyPlayerName = applyPlayerName;
    }

    @Generated
    public void setApplyPlayerUuid(UUID applyPlayerUuid) {
        this.applyPlayerUuid = applyPlayerUuid;
    }

    @Generated
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof GuildApply)) {
            return false;
        }
        GuildApply that = (GuildApply)other;
        if (!that.canEqual(this)) {
            return false;
        }
        Integer id = this.getId();
        Integer otherId = that.getId();
        if (id == null ? otherId != null : !((Object)id).equals(otherId)) {
            return false;
        }
        Integer guildInfoId = this.getGuildInfoId();
        Integer otherGuildInfoId = that.getGuildInfoId();
        if (guildInfoId == null ? otherGuildInfoId != null : !((Object)guildInfoId).equals(otherGuildInfoId)) {
            return false;
        }
        Integer applyResult = this.getApplyResult();
        Integer otherApplyResult = that.getApplyResult();
        if (applyResult == null ? otherApplyResult != null : !((Object)applyResult).equals(otherApplyResult)) {
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
        Date applyTime = this.getApplyTime();
        Date otherApplyTime = that.getApplyTime();
        if (applyTime == null ? otherApplyTime != null : !((Object)applyTime).equals(otherApplyTime)) {
            return false;
        }
        String applyPlayerName = this.getApplyPlayerName();
        String otherApplyPlayerName = that.getApplyPlayerName();
        if (applyPlayerName == null ? otherApplyPlayerName != null : !applyPlayerName.equals(otherApplyPlayerName)) {
            return false;
        }
        UUID applyPlayerUuid = this.getApplyPlayerUuid();
        UUID otherApplyPlayerUuid = that.getApplyPlayerUuid();
        return !(applyPlayerUuid == null ? otherApplyPlayerUuid != null : !((Object)applyPlayerUuid).equals(otherApplyPlayerUuid));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof GuildApply;
    }

    @Generated
    public int hashCode() {
        int hashSeed = 59;
        int result = 1;
        Integer id = this.getId();
        result = result * 59 + (id == null ? 43 : ((Object)id).hashCode());
        Integer guildInfoId = this.getGuildInfoId();
        result = result * 59 + (guildInfoId == null ? 43 : ((Object)guildInfoId).hashCode());
        Integer applyResult = this.getApplyResult();
        result = result * 59 + (applyResult == null ? 43 : ((Object)applyResult).hashCode());
        String playerName = this.getPlayerName();
        result = result * 59 + (playerName == null ? 43 : playerName.hashCode());
        UUID playerUuid = this.getPlayerUuid();
        result = result * 59 + (playerUuid == null ? 43 : ((Object)playerUuid).hashCode());
        Date applyTime = this.getApplyTime();
        result = result * 59 + (applyTime == null ? 43 : ((Object)applyTime).hashCode());
        String applyPlayerName = this.getApplyPlayerName();
        result = result * 59 + (applyPlayerName == null ? 43 : applyPlayerName.hashCode());
        UUID applyPlayerUuid = this.getApplyPlayerUuid();
        result = result * 59 + (applyPlayerUuid == null ? 43 : ((Object)applyPlayerUuid).hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "GuildApply(id=" + this.getId() + ", guildInfoId=" + this.getGuildInfoId() + ", playerName=" + this.getPlayerName() + ", playerUuid=" + this.getPlayerUuid() + ", applyTime=" + this.getApplyTime() + ", applyResult=" + this.getApplyResult() + ", applyPlayerName=" + this.getApplyPlayerName() + ", applyPlayerUuid=" + this.getApplyPlayerUuid() + ")";
    }
}
