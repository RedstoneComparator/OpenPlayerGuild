package cn.handyplus.guild.enter;

import cn.handyplus.guild.lib.IndexEnum;
import cn.handyplus.guild.lib.TableField;
import cn.handyplus.guild.lib.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Generated;

@TableName(value="guild_info", comment="\u516c\u4f1a\u4fe1\u606f")
public class GuildInfo
implements Serializable {
    @TableField(value="id", comment="ID")
    private Integer id;
    @TableField(value="guild_name", comment="\u516c\u4f1a\u540d\u79f0", notNull=true, indexEnum=IndexEnum.UNIQUE, length=190)
    private String guildName;
    @TableField(value="logo_id", comment="logoId")
    private Integer logoId;
    @TableField(value="logo_name", comment="logo", length=10000, notNull=true)
    private String logoName;
    @TableField(value="description", comment="\u516c\u4f1a\u4ecb\u7ecd", length=1000)
    private String description;
    @TableField(value="level", comment="\u516c\u4f1a\u7b49\u7ea7")
    private Integer level;
    @TableField(value="money", comment="\u516c\u4f1a\u8d44\u91d1", fieldDefault="0")
    private Integer money;
    @TableField(value="prosperity_degree", comment="\u516c\u4f1a\u6d3b\u8dc3", fieldDefault="0")
    private Integer prosperityDegree;
    @TableField(value="month_prosperity_degree", comment="\u6708\u5ea6\u516c\u4f1a\u6d3b\u8dc3", fieldDefault="0")
    private Integer monthProsperityDegree;
    @TableField(value="sacred_stone_level", comment="\u516c\u4f1a\u795e\u77f3\u7b49\u7ea7", notNull=true, fieldDefault="1")
    private Integer sacredStoneLevel;
    @TableField(value="title_id", comment="\u79f0\u53f7id")
    private Integer titleId;
    @TableField(value="world_name", comment="\u516c\u4f1a\u57fa\u5730", indexEnum=IndexEnum.UNIQUE)
    private String worldName;
    @TableField(value="world_location", comment="\u516c\u4f1a\u5750\u6807", length=2000)
    private String spawnLocation;
    @TableField(value="member_count", comment="\u516c\u4f1a\u5f53\u524d\u4eba\u6570")
    private Integer memberCount;
    @TableField(value="member_max_count", comment="\u516c\u4f1a\u603b\u4eba\u6570")
    private Integer memberMaxCount;
    @TableField(value="creator", comment="\u521b\u5efa\u4eba")
    private String creator;
    @TableField(value="create_time", comment="\u521b\u5efa\u65f6\u95f4")
    private Date createTime;
    @TableField(value="pvp_status", comment="pvp\u72b6\u6001", notNull=true, fieldDefault="1")
    private Boolean pvpStatus;
    @TableField(value="join_mode", comment="\u52a0\u5165\u6a21\u5f0f true \u624b\u52a8 false \u81ea\u52a8", notNull=true, fieldDefault="1")
    private Boolean joinMode;
    @TableField(value="ore", comment="\u77ff\u77f3", notNull=true, fieldDefault="0")
    private Integer ore;
    @TableField(value="season_rank", comment="\u8d5b\u5b63\u6392\u884c", notNull=true, fieldDefault="0")
    private Integer seasonRank;
    @TableField(value="luck_perms_group", comment="\u6743\u9650\u7ec4")
    private String luckPermsGroup;

    @Generated
    public GuildInfo() {
    }

    @Generated
    public Integer getId() {
        return this.id;
    }

    @Generated
    public String getGuildName() {
        return this.guildName;
    }

    @Generated
    public Integer getLogoId() {
        return this.logoId;
    }

    @Generated
    public String getLogoName() {
        return this.logoName;
    }

    @Generated
    public String getDescription() {
        return this.description;
    }

    @Generated
    public Integer getLevel() {
        return this.level;
    }

    @Generated
    public Integer getMoney() {
        return this.money;
    }

    @Generated
    public Integer getProsperityDegree() {
        return this.prosperityDegree;
    }

    @Generated
    public Integer getMonthProsperityDegree() {
        return this.monthProsperityDegree;
    }

    @Generated
    public Integer getSacredStoneLevel() {
        return this.sacredStoneLevel;
    }

    @Generated
    public Integer getTitleId() {
        return this.titleId;
    }

    @Generated
    public String getWorldName() {
        return this.worldName;
    }

    @Generated
    public String getSpawnLocation() {
        return this.spawnLocation;
    }

    @Generated
    public Integer getMemberCount() {
        return this.memberCount;
    }

    @Generated
    public Integer getMemberMaxCount() {
        return this.memberMaxCount;
    }

    @Generated
    public String getCreator() {
        return this.creator;
    }

    @Generated
    public Date getCreateTime() {
        return this.createTime;
    }

    @Generated
    public Boolean getPvpStatus() {
        return this.pvpStatus;
    }

    @Generated
    public Boolean getJoinMode() {
        return this.joinMode;
    }

    @Generated
    public Integer getOre() {
        return this.ore;
    }

    @Generated
    public Integer getSeasonRank() {
        return this.seasonRank;
    }

    @Generated
    public String getLuckPermsGroup() {
        return this.luckPermsGroup;
    }

    @Generated
    public void setId(Integer id) {
        this.id = id;
    }

    @Generated
    public void setGuildName(String guildName) {
        this.guildName = guildName;
    }

    @Generated
    public void setLogoId(Integer logoId) {
        this.logoId = logoId;
    }

    @Generated
    public void setLogoName(String logoName) {
        this.logoName = logoName;
    }

    @Generated
    public void setDescription(String description) {
        this.description = description;
    }

    @Generated
    public void setLevel(Integer level) {
        this.level = level;
    }

    @Generated
    public void setMoney(Integer money) {
        this.money = money;
    }

    @Generated
    public void setProsperityDegree(Integer prosperityDegree) {
        this.prosperityDegree = prosperityDegree;
    }

    @Generated
    public void setMonthProsperityDegree(Integer monthProsperityDegree) {
        this.monthProsperityDegree = monthProsperityDegree;
    }

    @Generated
    public void setSacredStoneLevel(Integer sacredStoneLevel) {
        this.sacredStoneLevel = sacredStoneLevel;
    }

    @Generated
    public void setTitleId(Integer titleId) {
        this.titleId = titleId;
    }

    @Generated
    public void setWorldName(String worldName) {
        this.worldName = worldName;
    }

    @Generated
    public void setSpawnLocation(String spawnLocation) {
        this.spawnLocation = spawnLocation;
    }

    @Generated
    public void setMemberCount(Integer memberCount) {
        this.memberCount = memberCount;
    }

    @Generated
    public void setMemberMaxCount(Integer memberMaxCount) {
        this.memberMaxCount = memberMaxCount;
    }

    @Generated
    public void setCreator(String creator) {
        this.creator = creator;
    }

    @Generated
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Generated
    public void setPvpStatus(Boolean pvpStatus) {
        this.pvpStatus = pvpStatus;
    }

    @Generated
    public void setJoinMode(Boolean joinMode) {
        this.joinMode = joinMode;
    }

    @Generated
    public void setOre(Integer ore) {
        this.ore = ore;
    }

    @Generated
    public void setSeasonRank(Integer seasonRank) {
        this.seasonRank = seasonRank;
    }

    @Generated
    public void setLuckPermsGroup(String luckPermsGroup) {
        this.luckPermsGroup = luckPermsGroup;
    }

    @Generated
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof GuildInfo)) {
            return false;
        }
        GuildInfo that = (GuildInfo)other;
        if (!that.canEqual(this)) {
            return false;
        }
        Integer id = this.getId();
        Integer otherId = that.getId();
        if (id == null ? otherId != null : !((Object)id).equals(otherId)) {
            return false;
        }
        Integer logoId = this.getLogoId();
        Integer otherLogoId = that.getLogoId();
        if (logoId == null ? otherLogoId != null : !((Object)logoId).equals(otherLogoId)) {
            return false;
        }
        Integer level = this.getLevel();
        Integer otherLevel = that.getLevel();
        if (level == null ? otherLevel != null : !((Object)level).equals(otherLevel)) {
            return false;
        }
        Integer money = this.getMoney();
        Integer otherMoney = that.getMoney();
        if (money == null ? otherMoney != null : !((Object)money).equals(otherMoney)) {
            return false;
        }
        Integer prosperityDegree = this.getProsperityDegree();
        Integer otherProsperityDegree = that.getProsperityDegree();
        if (prosperityDegree == null ? otherProsperityDegree != null : !((Object)prosperityDegree).equals(otherProsperityDegree)) {
            return false;
        }
        Integer monthProsperityDegree = this.getMonthProsperityDegree();
        Integer otherMonthProsperityDegree = that.getMonthProsperityDegree();
        if (monthProsperityDegree == null ? otherMonthProsperityDegree != null : !((Object)monthProsperityDegree).equals(otherMonthProsperityDegree)) {
            return false;
        }
        Integer sacredStoneLevel = this.getSacredStoneLevel();
        Integer otherSacredStoneLevel = that.getSacredStoneLevel();
        if (sacredStoneLevel == null ? otherSacredStoneLevel != null : !((Object)sacredStoneLevel).equals(otherSacredStoneLevel)) {
            return false;
        }
        Integer titleId = this.getTitleId();
        Integer otherTitleId = that.getTitleId();
        if (titleId == null ? otherTitleId != null : !((Object)titleId).equals(otherTitleId)) {
            return false;
        }
        Integer memberCount = this.getMemberCount();
        Integer otherMemberCount = that.getMemberCount();
        if (memberCount == null ? otherMemberCount != null : !((Object)memberCount).equals(otherMemberCount)) {
            return false;
        }
        Integer memberMaxCount = this.getMemberMaxCount();
        Integer otherMemberMaxCount = that.getMemberMaxCount();
        if (memberMaxCount == null ? otherMemberMaxCount != null : !((Object)memberMaxCount).equals(otherMemberMaxCount)) {
            return false;
        }
        Boolean pvpStatus = this.getPvpStatus();
        Boolean otherPvpStatus = that.getPvpStatus();
        if (pvpStatus == null ? otherPvpStatus != null : !((Object)pvpStatus).equals(otherPvpStatus)) {
            return false;
        }
        Boolean joinMode = this.getJoinMode();
        Boolean otherJoinMode = that.getJoinMode();
        if (joinMode == null ? otherJoinMode != null : !((Object)joinMode).equals(otherJoinMode)) {
            return false;
        }
        Integer ore = this.getOre();
        Integer otherOre = that.getOre();
        if (ore == null ? otherOre != null : !((Object)ore).equals(otherOre)) {
            return false;
        }
        Integer seasonRank = this.getSeasonRank();
        Integer otherSeasonRank = that.getSeasonRank();
        if (seasonRank == null ? otherSeasonRank != null : !((Object)seasonRank).equals(otherSeasonRank)) {
            return false;
        }
        String guildName = this.getGuildName();
        String otherGuildName = that.getGuildName();
        if (guildName == null ? otherGuildName != null : !guildName.equals(otherGuildName)) {
            return false;
        }
        String logoName = this.getLogoName();
        String otherLogoName = that.getLogoName();
        if (logoName == null ? otherLogoName != null : !logoName.equals(otherLogoName)) {
            return false;
        }
        String description = this.getDescription();
        String otherDescription = that.getDescription();
        if (description == null ? otherDescription != null : !description.equals(otherDescription)) {
            return false;
        }
        String worldName = this.getWorldName();
        String otherWorldName = that.getWorldName();
        if (worldName == null ? otherWorldName != null : !worldName.equals(otherWorldName)) {
            return false;
        }
        String spawnLocation = this.getSpawnLocation();
        String otherSpawnLocation = that.getSpawnLocation();
        if (spawnLocation == null ? otherSpawnLocation != null : !spawnLocation.equals(otherSpawnLocation)) {
            return false;
        }
        String creator = this.getCreator();
        String otherCreator = that.getCreator();
        if (creator == null ? otherCreator != null : !creator.equals(otherCreator)) {
            return false;
        }
        Date createTime = this.getCreateTime();
        Date otherCreateTime = that.getCreateTime();
        if (createTime == null ? otherCreateTime != null : !((Object)createTime).equals(otherCreateTime)) {
            return false;
        }
        String luckPermsGroup = this.getLuckPermsGroup();
        String otherLuckPermsGroup = that.getLuckPermsGroup();
        return !(luckPermsGroup == null ? otherLuckPermsGroup != null : !luckPermsGroup.equals(otherLuckPermsGroup));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof GuildInfo;
    }

    @Generated
    public int hashCode() {
        int hashSeed = 59;
        int result = 1;
        Integer id = this.getId();
        result = result * 59 + (id == null ? 43 : ((Object)id).hashCode());
        Integer logoId = this.getLogoId();
        result = result * 59 + (logoId == null ? 43 : ((Object)logoId).hashCode());
        Integer level = this.getLevel();
        result = result * 59 + (level == null ? 43 : ((Object)level).hashCode());
        Integer money = this.getMoney();
        result = result * 59 + (money == null ? 43 : ((Object)money).hashCode());
        Integer prosperityDegree = this.getProsperityDegree();
        result = result * 59 + (prosperityDegree == null ? 43 : ((Object)prosperityDegree).hashCode());
        Integer monthProsperityDegree = this.getMonthProsperityDegree();
        result = result * 59 + (monthProsperityDegree == null ? 43 : ((Object)monthProsperityDegree).hashCode());
        Integer sacredStoneLevel = this.getSacredStoneLevel();
        result = result * 59 + (sacredStoneLevel == null ? 43 : ((Object)sacredStoneLevel).hashCode());
        Integer titleId = this.getTitleId();
        result = result * 59 + (titleId == null ? 43 : ((Object)titleId).hashCode());
        Integer memberCount = this.getMemberCount();
        result = result * 59 + (memberCount == null ? 43 : ((Object)memberCount).hashCode());
        Integer memberMaxCount = this.getMemberMaxCount();
        result = result * 59 + (memberMaxCount == null ? 43 : ((Object)memberMaxCount).hashCode());
        Boolean pvpStatus = this.getPvpStatus();
        result = result * 59 + (pvpStatus == null ? 43 : ((Object)pvpStatus).hashCode());
        Boolean joinMode = this.getJoinMode();
        result = result * 59 + (joinMode == null ? 43 : ((Object)joinMode).hashCode());
        Integer ore = this.getOre();
        result = result * 59 + (ore == null ? 43 : ((Object)ore).hashCode());
        Integer seasonRank = this.getSeasonRank();
        result = result * 59 + (seasonRank == null ? 43 : ((Object)seasonRank).hashCode());
        String guildName = this.getGuildName();
        result = result * 59 + (guildName == null ? 43 : guildName.hashCode());
        String logoName = this.getLogoName();
        result = result * 59 + (logoName == null ? 43 : logoName.hashCode());
        String description = this.getDescription();
        result = result * 59 + (description == null ? 43 : description.hashCode());
        String worldName = this.getWorldName();
        result = result * 59 + (worldName == null ? 43 : worldName.hashCode());
        String spawnLocation = this.getSpawnLocation();
        result = result * 59 + (spawnLocation == null ? 43 : spawnLocation.hashCode());
        String creator = this.getCreator();
        result = result * 59 + (creator == null ? 43 : creator.hashCode());
        Date createTime = this.getCreateTime();
        result = result * 59 + (createTime == null ? 43 : ((Object)createTime).hashCode());
        String luckPermsGroup = this.getLuckPermsGroup();
        result = result * 59 + (luckPermsGroup == null ? 43 : luckPermsGroup.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "GuildInfo(id=" + this.getId() + ", guildName=" + this.getGuildName() + ", logoId=" + this.getLogoId() + ", logoName=" + this.getLogoName() + ", description=" + this.getDescription() + ", level=" + this.getLevel() + ", money=" + this.getMoney() + ", prosperityDegree=" + this.getProsperityDegree() + ", monthProsperityDegree=" + this.getMonthProsperityDegree() + ", sacredStoneLevel=" + this.getSacredStoneLevel() + ", titleId=" + this.getTitleId() + ", worldName=" + this.getWorldName() + ", spawnLocation=" + this.getSpawnLocation() + ", memberCount=" + this.getMemberCount() + ", memberMaxCount=" + this.getMemberMaxCount() + ", creator=" + this.getCreator() + ", createTime=" + this.getCreateTime() + ", pvpStatus=" + this.getPvpStatus() + ", joinMode=" + this.getJoinMode() + ", ore=" + this.getOre() + ", seasonRank=" + this.getSeasonRank() + ", luckPermsGroup=" + this.getLuckPermsGroup() + ")";
    }
}
