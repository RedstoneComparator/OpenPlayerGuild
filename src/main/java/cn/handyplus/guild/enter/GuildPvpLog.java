package cn.handyplus.guild.enter;

import cn.handyplus.guild.lib.IndexEnum;
import cn.handyplus.guild.lib.TableField;
import cn.handyplus.guild.lib.TableName;
import java.util.Date;
import lombok.Generated;

@TableName(value="guild_pvp_log", comment="\u516c\u4f1a\u6218pvp\u8bb0\u5f55")
public class GuildPvpLog {
    @TableField(value="id", comment="ID")
    private Integer id;
    @TableField(value="type", comment="\u7c7b\u578b", notNull=true)
    private String type;
    @TableField(value="guild_info_id", comment="\u516c\u4f1aid", notNull=true, indexEnum=IndexEnum.INDEX)
    private Integer guildInfoId;
    @TableField(value="guild_info_name", comment="\u516c\u4f1a\u540d\u79f0", notNull=true, length=190)
    private String guildInfoName;
    @TableField(value="result", comment="\u7ed3\u679c")
    private Boolean result;
    @TableField(value="season", comment="\u8d5b\u5b63")
    private Integer season;
    @TableField(value="rank", comment="\u6392\u540d")
    private Integer rank;
    @TableField(value="start_time", comment="\u5f00\u59cb\u65f6\u95f4")
    private Date startTime;
    @TableField(value="end_time", comment="\u7ed3\u675f\u65f6\u95f4")
    private Date endTime;

    @Generated
    public GuildPvpLog() {
    }

    @Generated
    public Integer getId() {
        return this.id;
    }

    @Generated
    public String getType() {
        return this.type;
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
    public Boolean getResult() {
        return this.result;
    }

    @Generated
    public Integer getSeason() {
        return this.season;
    }

    @Generated
    public Integer getRank() {
        return this.rank;
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
    public void setType(String type) {
        this.type = type;
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
    public void setResult(Boolean result) {
        this.result = result;
    }

    @Generated
    public void setSeason(Integer season) {
        this.season = season;
    }

    @Generated
    public void setRank(Integer rank) {
        this.rank = rank;
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
        if (!(other instanceof GuildPvpLog)) {
            return false;
        }
        GuildPvpLog otherGuild = (GuildPvpLog)other;
        if (!otherGuild.canEqual(this)) {
            return false;
        }
        Integer thisId = this.getId();
        Integer otherId = otherGuild.getId();
        if (thisId == null ? otherId != null : !((Object)thisId).equals(otherId)) {
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
        Integer thisSeason = this.getSeason();
        Integer otherSeason = otherGuild.getSeason();
        if (thisSeason == null ? otherSeason != null : !((Object)thisSeason).equals(otherSeason)) {
            return false;
        }
        Integer thisRank = this.getRank();
        Integer otherRank = otherGuild.getRank();
        if (thisRank == null ? otherRank != null : !((Object)thisRank).equals(otherRank)) {
            return false;
        }
        String thisType = this.getType();
        String otherType = otherGuild.getType();
        if (thisType == null ? otherType != null : !thisType.equals(otherType)) {
            return false;
        }
        String thisGuildInfoName = this.getGuildInfoName();
        String otherGuildInfoName = otherGuild.getGuildInfoName();
        if (thisGuildInfoName == null ? otherGuildInfoName != null : !thisGuildInfoName.equals(otherGuildInfoName)) {
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
        return other instanceof GuildPvpLog;
    }

    @Generated
    public int hashCode() {
        int prime = 59;
        int hash = 1;
        Integer idValue = this.getId();
        hash = hash * 59 + (idValue == null ? 43 : ((Object)idValue).hashCode());
        Integer guildInfoIdValue = this.getGuildInfoId();
        hash = hash * 59 + (guildInfoIdValue == null ? 43 : ((Object)guildInfoIdValue).hashCode());
        Boolean resultValue = this.getResult();
        hash = hash * 59 + (resultValue == null ? 43 : ((Object)resultValue).hashCode());
        Integer seasonValue = this.getSeason();
        hash = hash * 59 + (seasonValue == null ? 43 : ((Object)seasonValue).hashCode());
        Integer rankValue = this.getRank();
        hash = hash * 59 + (rankValue == null ? 43 : ((Object)rankValue).hashCode());
        String typeValue = this.getType();
        hash = hash * 59 + (typeValue == null ? 43 : typeValue.hashCode());
        String guildInfoNameValue = this.getGuildInfoName();
        hash = hash * 59 + (guildInfoNameValue == null ? 43 : guildInfoNameValue.hashCode());
        Date startTimeValue = this.getStartTime();
        hash = hash * 59 + (startTimeValue == null ? 43 : ((Object)startTimeValue).hashCode());
        Date endTimeValue = this.getEndTime();
        hash = hash * 59 + (endTimeValue == null ? 43 : ((Object)endTimeValue).hashCode());
        return hash;
    }

    @Generated
    public String toString() {
        return "GuildPvpLog(id=" + this.getId() + ", type=" + this.getType() + ", guildInfoId=" + this.getGuildInfoId() + ", guildInfoName=" + this.getGuildInfoName() + ", result=" + this.getResult() + ", season=" + this.getSeason() + ", rank=" + this.getRank() + ", startTime=" + this.getStartTime() + ", endTime=" + this.getEndTime() + ")";
    }
}
