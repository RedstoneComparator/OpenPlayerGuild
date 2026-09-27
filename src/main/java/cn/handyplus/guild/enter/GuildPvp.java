package cn.handyplus.guild.enter;

import cn.handyplus.guild.lib.IndexEnum;
import cn.handyplus.guild.lib.TableField;
import cn.handyplus.guild.lib.TableName;
import java.util.Date;
import lombok.Generated;

@TableName(value="guild_pvp", comment="\u516c\u4f1a\u8d5b\u5b63\u516c\u4f1a\u6218")
public class GuildPvp {
    @TableField(value="id", comment="ID")
    private Integer id;
    @TableField(value="season", comment="\u8d5b\u5b63", notNull=true, indexEnum=IndexEnum.UNIQUE, uniqueGroup="season_guild")
    private Integer season;
    @TableField(value="guild_info_id", comment="\u516c\u4f1aid", notNull=true, indexEnum=IndexEnum.UNIQUE, uniqueGroup="season_guild")
    private Integer guildInfoId;
    @TableField(value="is_join", comment="\u662f\u5426\u53c2\u52a0", notNull=true)
    private Boolean isJoin;
    @TableField(value="is_start", comment="\u662f\u5426\u5f00\u59cb", fieldDefault="0", notNull=true)
    private Boolean isStart;
    @TableField(value="start_time", comment="\u5f00\u59cb\u65f6\u95f4")
    private Date startTime;

    @Generated
    public GuildPvp() {
    }

    @Generated
    public Integer getId() {
        return this.id;
    }

    @Generated
    public Integer getSeason() {
        return this.season;
    }

    @Generated
    public Integer getGuildInfoId() {
        return this.guildInfoId;
    }

    @Generated
    public Boolean getIsJoin() {
        return this.isJoin;
    }

    @Generated
    public Boolean getIsStart() {
        return this.isStart;
    }

    @Generated
    public Date getStartTime() {
        return this.startTime;
    }

    @Generated
    public void setId(Integer id) {
        this.id = id;
    }

    @Generated
    public void setSeason(Integer season) {
        this.season = season;
    }

    @Generated
    public void setGuildInfoId(Integer guildInfoId) {
        this.guildInfoId = guildInfoId;
    }

    @Generated
    public void setIsJoin(Boolean isJoin) {
        this.isJoin = isJoin;
    }

    @Generated
    public void setIsStart(Boolean isStart) {
        this.isStart = isStart;
    }

    @Generated
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    @Generated
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof GuildPvp)) {
            return false;
        }
        GuildPvp otherGuild = (GuildPvp)other;
        if (!otherGuild.canEqual(this)) {
            return false;
        }
        Integer thisId = this.getId();
        Integer otherId = otherGuild.getId();
        if (thisId == null ? otherId != null : !((Object)thisId).equals(otherId)) {
            return false;
        }
        Integer thisSeason = this.getSeason();
        Integer otherSeason = otherGuild.getSeason();
        if (thisSeason == null ? otherSeason != null : !((Object)thisSeason).equals(otherSeason)) {
            return false;
        }
        Integer thisGuildInfoId = this.getGuildInfoId();
        Integer otherGuildInfoId = otherGuild.getGuildInfoId();
        if (thisGuildInfoId == null ? otherGuildInfoId != null : !((Object)thisGuildInfoId).equals(otherGuildInfoId)) {
            return false;
        }
        Boolean thisIsJoin = this.getIsJoin();
        Boolean otherIsJoin = otherGuild.getIsJoin();
        if (thisIsJoin == null ? otherIsJoin != null : !((Object)thisIsJoin).equals(otherIsJoin)) {
            return false;
        }
        Boolean thisIsStart = this.getIsStart();
        Boolean otherIsStart = otherGuild.getIsStart();
        if (thisIsStart == null ? otherIsStart != null : !((Object)thisIsStart).equals(otherIsStart)) {
            return false;
        }
        Date thisStartTime = this.getStartTime();
        Date otherStartTime = otherGuild.getStartTime();
        return !(thisStartTime == null ? otherStartTime != null : !((Object)thisStartTime).equals(otherStartTime));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof GuildPvp;
    }

    @Generated
    public int hashCode() {
        int prime = 59;
        int hash = 1;
        Integer idValue = this.getId();
        hash = hash * 59 + (idValue == null ? 43 : ((Object)idValue).hashCode());
        Integer seasonValue = this.getSeason();
        hash = hash * 59 + (seasonValue == null ? 43 : ((Object)seasonValue).hashCode());
        Integer guildInfoIdValue = this.getGuildInfoId();
        hash = hash * 59 + (guildInfoIdValue == null ? 43 : ((Object)guildInfoIdValue).hashCode());
        Boolean isJoinValue = this.getIsJoin();
        hash = hash * 59 + (isJoinValue == null ? 43 : ((Object)isJoinValue).hashCode());
        Boolean isStartValue = this.getIsStart();
        hash = hash * 59 + (isStartValue == null ? 43 : ((Object)isStartValue).hashCode());
        Date startTimeValue = this.getStartTime();
        hash = hash * 59 + (startTimeValue == null ? 43 : ((Object)startTimeValue).hashCode());
        return hash;
    }

    @Generated
    public String toString() {
        return "GuildPvp(id=" + this.getId() + ", season=" + this.getSeason() + ", guildInfoId=" + this.getGuildInfoId() + ", isJoin=" + this.getIsJoin() + ", isStart=" + this.getIsStart() + ", startTime=" + this.getStartTime() + ")";
    }
}
