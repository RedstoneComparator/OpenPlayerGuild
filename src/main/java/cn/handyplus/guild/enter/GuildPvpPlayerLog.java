package cn.handyplus.guild.enter;

import cn.handyplus.guild.lib.IndexEnum;
import cn.handyplus.guild.lib.TableField;
import cn.handyplus.guild.lib.TableName;
import java.util.Date;
import java.util.UUID;
import lombok.Generated;

@TableName(value="guild_pvp_player_log", comment="\u516c\u4f1a\u6218pvp\u73a9\u5bb6\u8bb0\u5f55")
public class GuildPvpPlayerLog {
    @TableField(value="id", comment="ID")
    private Integer id;
    @TableField(value="guild_pvp_log_id", comment="\u516c\u4f1a\u6218pvp\u8bb0\u5f55ID", notNull=true, indexEnum=IndexEnum.INDEX)
    private Integer guildPvpLogId;
    @TableField(value="guild_info_id", comment="\u516c\u4f1aid", notNull=true, indexEnum=IndexEnum.INDEX)
    private Integer guildInfoId;
    @TableField(value="guild_info_name", comment="\u516c\u4f1a\u540d\u79f0", notNull=true, length=190)
    private String guildInfoName;
    @TableField(value="player_name", comment="\u73a9\u5bb6\u540d\u79f0", notNull=true)
    private String playerName;
    @TableField(value="player_uuid", comment="\u73a9\u5bb6uuid", notNull=true, indexEnum=IndexEnum.INDEX)
    private UUID playerUuid;
    @TableField(value="result", comment="\u7ed3\u679c")
    private Boolean result;
    @TableField(value="kill", comment="\u51fb\u6740", notNull=true, fieldDefault="0")
    private Integer kill;
    @TableField(value="die", comment="\u6b7b\u4ea1", notNull=true, fieldDefault="0")
    private Integer die;
    @TableField(value="start_time", comment="\u5f00\u59cb\u65f6\u95f4")
    private Date startTime;
    @TableField(value="end_time", comment="\u7ed3\u675f\u65f6\u95f4")
    private Date endTime;

    @Generated
    public GuildPvpPlayerLog() {
    }

    @Generated
    public Integer getId() {
        return this.id;
    }

    @Generated
    public Integer getGuildPvpLogId() {
        return this.guildPvpLogId;
    }

    @Generated
    public Integer getGuildInfoId() {
        return this.guildInfoId;
    }

    @Generated
    public String getGuildInfoName() {
        return this.guildInfoName;
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
    public Boolean getResult() {
        return this.result;
    }

    @Generated
    public Integer getKill() {
        return this.kill;
    }

    @Generated
    public Integer getDie() {
        return this.die;
    }

    @Generated
    public Date getStartTime() {
        return this.startTime;
    }

    @Generated
    public Date getEndTime() {
        return this.endTime;
    }

    @Generated
    public void setId(Integer id) {
        this.id = id;
    }

    @Generated
    public void setGuildPvpLogId(Integer guildPvpLogId) {
        this.guildPvpLogId = guildPvpLogId;
    }

    @Generated
    public void setGuildInfoId(Integer guildInfoId) {
        this.guildInfoId = guildInfoId;
    }

    @Generated
    public void setGuildInfoName(String guildInfoName) {
        this.guildInfoName = guildInfoName;
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
    public void setResult(Boolean result) {
        this.result = result;
    }

    @Generated
    public void setKill(Integer kill) {
        this.kill = kill;
    }

    @Generated
    public void setDie(Integer die) {
        this.die = die;
    }

    @Generated
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    @Generated
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @Generated
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof GuildPvpPlayerLog)) {
            return false;
        }
        GuildPvpPlayerLog otherGuild = (GuildPvpPlayerLog)other;
        if (!otherGuild.canEqual(this)) {
            return false;
        }
        Integer thisId = this.getId();
        Integer otherId = otherGuild.getId();
        if (thisId == null ? otherId != null : !((Object)thisId).equals(otherId)) {
            return false;
        }
        Integer thisGuildPvpLogId = this.getGuildPvpLogId();
        Integer otherGuildPvpLogId = otherGuild.getGuildPvpLogId();
        if (thisGuildPvpLogId == null ? otherGuildPvpLogId != null : !((Object)thisGuildPvpLogId).equals(otherGuildPvpLogId)) {
            return false;
        }
        Integer thisGuildInfoId = this.getGuildInfoId();
        Integer otherGuildInfoId = otherGuild.getGuildInfoId();
        if (thisGuildInfoId == null ? otherGuildInfoId != null : !((Object)thisGuildInfoId).equals(otherGuildInfoId)) {
            return false;
        }
        Boolean thisResult = this.getResult();
        Boolean otherResult = otherGuild.getResult();
        if (thisResult == null ? otherResult != null : !((Object)thisResult).equals(otherResult)) {
            return false;
        }
        Integer thisKill = this.getKill();
        Integer otherKill = otherGuild.getKill();
        if (thisKill == null ? otherKill != null : !((Object)thisKill).equals(otherKill)) {
            return false;
        }
        Integer thisDie = this.getDie();
        Integer otherDie = otherGuild.getDie();
        if (thisDie == null ? otherDie != null : !((Object)thisDie).equals(otherDie)) {
            return false;
        }
        String thisGuildInfoName = this.getGuildInfoName();
        String otherGuildInfoName = otherGuild.getGuildInfoName();
        if (thisGuildInfoName == null ? otherGuildInfoName != null : !thisGuildInfoName.equals(otherGuildInfoName)) {
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
        Date thisStartTime = this.getStartTime();
        Date otherStartTime = otherGuild.getStartTime();
        if (thisStartTime == null ? otherStartTime != null : !((Object)thisStartTime).equals(otherStartTime)) {
            return false;
        }
        Date thisEndTime = this.getEndTime();
        Date otherEndTime = otherGuild.getEndTime();
        return !(thisEndTime == null ? otherEndTime != null : !((Object)thisEndTime).equals(otherEndTime));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof GuildPvpPlayerLog;
    }

    @Generated
    public int hashCode() {
        int prime = 59;
        int hash = 1;
        Integer idValue = this.getId();
        hash = hash * 59 + (idValue == null ? 43 : ((Object)idValue).hashCode());
        Integer guildPvpLogIdValue = this.getGuildPvpLogId();
        hash = hash * 59 + (guildPvpLogIdValue == null ? 43 : ((Object)guildPvpLogIdValue).hashCode());
        Integer guildInfoIdValue = this.getGuildInfoId();
        hash = hash * 59 + (guildInfoIdValue == null ? 43 : ((Object)guildInfoIdValue).hashCode());
        Boolean resultValue = this.getResult();
        hash = hash * 59 + (resultValue == null ? 43 : ((Object)resultValue).hashCode());
        Integer killValue = this.getKill();
        hash = hash * 59 + (killValue == null ? 43 : ((Object)killValue).hashCode());
        Integer dieValue = this.getDie();
        hash = hash * 59 + (dieValue == null ? 43 : ((Object)dieValue).hashCode());
        String guildInfoNameValue = this.getGuildInfoName();
        hash = hash * 59 + (guildInfoNameValue == null ? 43 : guildInfoNameValue.hashCode());
        String playerNameValue = this.getPlayerName();
        hash = hash * 59 + (playerNameValue == null ? 43 : playerNameValue.hashCode());
        UUID playerUuidValue = this.getPlayerUuid();
        hash = hash * 59 + (playerUuidValue == null ? 43 : ((Object)playerUuidValue).hashCode());
        Date startTimeValue = this.getStartTime();
        hash = hash * 59 + (startTimeValue == null ? 43 : ((Object)startTimeValue).hashCode());
        Date endTimeValue = this.getEndTime();
        hash = hash * 59 + (endTimeValue == null ? 43 : ((Object)endTimeValue).hashCode());
        return hash;
    }

    @Generated
    public String toString() {
        return "GuildPvpPlayerLog(id=" + this.getId() + ", guildPvpLogId=" + this.getGuildPvpLogId() + ", guildInfoId=" + this.getGuildInfoId() + ", guildInfoName=" + this.getGuildInfoName() + ", playerName=" + this.getPlayerName() + ", playerUuid=" + this.getPlayerUuid() + ", result=" + this.getResult() + ", kill=" + this.getKill() + ", die=" + this.getDie() + ", startTime=" + this.getStartTime() + ", endTime=" + this.getEndTime() + ")";
    }
}
