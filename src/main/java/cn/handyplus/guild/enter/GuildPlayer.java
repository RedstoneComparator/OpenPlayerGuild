package cn.handyplus.guild.enter;

import cn.handyplus.guild.lib.IndexEnum;
import cn.handyplus.guild.lib.TableField;
import cn.handyplus.guild.lib.TableName;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;
import lombok.Generated;

@TableName(value="guild_player", comment="\u516c\u4f1a\u73a9\u5bb6")
public class GuildPlayer
implements Serializable {
    @TableField(value="id", comment="ID")
    private Integer id;
    @TableField(value="guild_info_id", comment="\u516c\u4f1aid", notNull=true, indexEnum=IndexEnum.INDEX)
    private Integer guildInfoId;
    @TableField(value="guild_info_name", comment="\u516c\u4f1a\u540d\u79f0", notNull=true, length=190, indexEnum=IndexEnum.INDEX)
    private String guildInfoName;
    @TableField(value="player_name", comment="\u73a9\u5bb6\u540d\u79f0", notNull=true, indexEnum=IndexEnum.INDEX)
    private String playerName;
    @TableField(value="player_uuid", comment="\u73a9\u5bb6uuid", notNull=true, indexEnum=IndexEnum.UNIQUE)
    private UUID playerUuid;
    @TableField(value="money", comment="\u5f53\u524d\u8d21\u732e\u5ea6", notNull=true, fieldDefault="0")
    private Integer money;
    @TableField(value="week_money", comment="\u5468\u8d21\u732e\u5ea6", notNull=true, fieldDefault="0")
    private Integer weekMoney;
    @TableField(value="total_money", comment="\u603b\u8d21\u732e\u5ea6", notNull=true, fieldDefault="0")
    private Integer totalMoney;
    @TableField(value="guild_money", comment="\u8d21\u732e\u8d44\u91d1", notNull=true, fieldDefault="0")
    private Integer guildMoney;
    @TableField(value="role_id", comment="\u89d2\u8272", notNull=true)
    private Integer roleId;
    @TableField(value="join_time", comment="\u52a0\u5165\u65f6\u95f4")
    private Date joinTime;
    @TableField(value="last_join_time", comment="\u4e0a\u6b21\u767b\u5f55\u65f6\u95f4")
    private Date lastJoinTime;
    @TableField(value="last_quit_time", comment="\u6700\u540e\u79bb\u7ebf\u65f6\u95f4")
    private Date lastQuitTime;
    @TableField(value="pvp_status", comment="pvp\u72b6\u6001", notNull=true, fieldDefault="1")
    private Boolean pvpStatus;
    @TableField(value="pickaxe_time", comment="\u795e\u77f3\u6316\u6398\u65f6\u95f4")
    private Date pickaxeTime;
    @TableField(value="ore", comment="\u77ff\u77f3", notNull=true, fieldDefault="0")
    private Integer ore;
    @TableField(value="total_ore", comment="\u603b\u77ff\u77f3", notNull=true, fieldDefault="0")
    private Integer totalOre;
    @TableField(value="kill", comment="\u516c\u4f1a\u6218\u51fb\u6740\u6b21\u6570", notNull=true, fieldDefault="0")
    private Integer kill;
    @TableField(value="die", comment="\u516c\u4f1a\u6218\u6b7b\u4ea1\u6b21\u6570", notNull=true, fieldDefault="0")
    private Integer die;
    private Integer level;
    private Integer seasonRank;
    private String logoName;
    private Integer memberCount;
    private Integer memberMaxCount;
    private Integer guildInfoMoney;
    private Integer prosperityDegree;
    private Integer monthProsperityDegree;
    private Integer guildInfoOre;

    @Generated
    public GuildPlayer() {
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
    public Integer getMoney() {
        return this.money;
    }

    @Generated
    public Integer getWeekMoney() {
        return this.weekMoney;
    }

    @Generated
    public Integer getTotalMoney() {
        return this.totalMoney;
    }

    @Generated
    public Integer getGuildMoney() {
        return this.guildMoney;
    }

    @Generated
    public Integer getRoleId() {
        return this.roleId;
    }

    @Generated
    public Date getJoinTime() {
        return this.joinTime;
    }

    @Generated
    public Date getLastJoinTime() {
        return this.lastJoinTime;
    }

    @Generated
    public Date getLastQuitTime() {
        return this.lastQuitTime;
    }

    @Generated
    public Boolean getPvpStatus() {
        return this.pvpStatus;
    }

    @Generated
    public Date getPickaxeTime() {
        return this.pickaxeTime;
    }

    @Generated
    public Integer getOre() {
        return this.ore;
    }

    @Generated
    public Integer getTotalOre() {
        return this.totalOre;
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
    public Integer getLevel() {
        return this.level;
    }

    @Generated
    public Integer getSeasonRank() {
        return this.seasonRank;
    }

    @Generated
    public String getLogoName() {
        return this.logoName;
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
    public Integer getGuildInfoMoney() {
        return this.guildInfoMoney;
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
    public Integer getGuildInfoOre() {
        return this.guildInfoOre;
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
    public void setMoney(Integer money) {
        this.money = money;
    }

    @Generated
    public void setWeekMoney(Integer weekMoney) {
        this.weekMoney = weekMoney;
    }

    @Generated
    public void setTotalMoney(Integer totalMoney) {
        this.totalMoney = totalMoney;
    }

    @Generated
    public void setGuildMoney(Integer guildMoney) {
        this.guildMoney = guildMoney;
    }

    @Generated
    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    @Generated
    public void setJoinTime(Date joinTime) {
        this.joinTime = joinTime;
    }

    @Generated
    public void setLastJoinTime(Date lastJoinTime) {
        this.lastJoinTime = lastJoinTime;
    }

    @Generated
    public void setLastQuitTime(Date lastQuitTime) {
        this.lastQuitTime = lastQuitTime;
    }

    @Generated
    public void setPvpStatus(Boolean pvpStatus) {
        this.pvpStatus = pvpStatus;
    }

    @Generated
    public void setPickaxeTime(Date pickaxeTime) {
        this.pickaxeTime = pickaxeTime;
    }

    @Generated
    public void setOre(Integer ore) {
        this.ore = ore;
    }

    @Generated
    public void setTotalOre(Integer totalOre) {
        this.totalOre = totalOre;
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
    public void setLevel(Integer level) {
        this.level = level;
    }

    @Generated
    public void setSeasonRank(Integer seasonRank) {
        this.seasonRank = seasonRank;
    }

    @Generated
    public void setLogoName(String logoName) {
        this.logoName = logoName;
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
    public void setGuildInfoMoney(Integer guildInfoMoney) {
        this.guildInfoMoney = guildInfoMoney;
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
    public void setGuildInfoOre(Integer guildInfoOre) {
        this.guildInfoOre = guildInfoOre;
    }

    @Generated
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof GuildPlayer)) {
            return false;
        }
        GuildPlayer that = (GuildPlayer)other;
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
        Integer money = this.getMoney();
        Integer otherMoney = that.getMoney();
        if (money == null ? otherMoney != null : !((Object)money).equals(otherMoney)) {
            return false;
        }
        Integer weekMoney = this.getWeekMoney();
        Integer otherWeekMoney = that.getWeekMoney();
        if (weekMoney == null ? otherWeekMoney != null : !((Object)weekMoney).equals(otherWeekMoney)) {
            return false;
        }
        Integer totalMoney = this.getTotalMoney();
        Integer otherTotalMoney = that.getTotalMoney();
        if (totalMoney == null ? otherTotalMoney != null : !((Object)totalMoney).equals(otherTotalMoney)) {
            return false;
        }
        Integer guildMoney = this.getGuildMoney();
        Integer otherGuildMoney = that.getGuildMoney();
        if (guildMoney == null ? otherGuildMoney != null : !((Object)guildMoney).equals(otherGuildMoney)) {
            return false;
        }
        Integer roleId = this.getRoleId();
        Integer otherRoleId = that.getRoleId();
        if (roleId == null ? otherRoleId != null : !((Object)roleId).equals(otherRoleId)) {
            return false;
        }
        Boolean pvpStatus = this.getPvpStatus();
        Boolean otherPvpStatus = that.getPvpStatus();
        if (pvpStatus == null ? otherPvpStatus != null : !((Object)pvpStatus).equals(otherPvpStatus)) {
            return false;
        }
        Integer ore = this.getOre();
        Integer otherOre = that.getOre();
        if (ore == null ? otherOre != null : !((Object)ore).equals(otherOre)) {
            return false;
        }
        Integer totalOre = this.getTotalOre();
        Integer otherTotalOre = that.getTotalOre();
        if (totalOre == null ? otherTotalOre != null : !((Object)totalOre).equals(otherTotalOre)) {
            return false;
        }
        Integer kill = this.getKill();
        Integer otherKill = that.getKill();
        if (kill == null ? otherKill != null : !((Object)kill).equals(otherKill)) {
            return false;
        }
        Integer die = this.getDie();
        Integer otherDie = that.getDie();
        if (die == null ? otherDie != null : !((Object)die).equals(otherDie)) {
            return false;
        }
        Integer level = this.getLevel();
        Integer otherLevel = that.getLevel();
        if (level == null ? otherLevel != null : !((Object)level).equals(otherLevel)) {
            return false;
        }
        Integer seasonRank = this.getSeasonRank();
        Integer otherSeasonRank = that.getSeasonRank();
        if (seasonRank == null ? otherSeasonRank != null : !((Object)seasonRank).equals(otherSeasonRank)) {
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
        Integer guildInfoMoney = this.getGuildInfoMoney();
        Integer otherGuildInfoMoney = that.getGuildInfoMoney();
        if (guildInfoMoney == null ? otherGuildInfoMoney != null : !((Object)guildInfoMoney).equals(otherGuildInfoMoney)) {
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
        Integer guildInfoOre = this.getGuildInfoOre();
        Integer otherGuildInfoOre = that.getGuildInfoOre();
        if (guildInfoOre == null ? otherGuildInfoOre != null : !((Object)guildInfoOre).equals(otherGuildInfoOre)) {
            return false;
        }
        String guildInfoName = this.getGuildInfoName();
        String otherGuildInfoName = that.getGuildInfoName();
        if (guildInfoName == null ? otherGuildInfoName != null : !guildInfoName.equals(otherGuildInfoName)) {
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
        Date joinTime = this.getJoinTime();
        Date otherJoinTime = that.getJoinTime();
        if (joinTime == null ? otherJoinTime != null : !((Object)joinTime).equals(otherJoinTime)) {
            return false;
        }
        Date lastJoinTime = this.getLastJoinTime();
        Date otherLastJoinTime = that.getLastJoinTime();
        if (lastJoinTime == null ? otherLastJoinTime != null : !((Object)lastJoinTime).equals(otherLastJoinTime)) {
            return false;
        }
        Date lastQuitTime = this.getLastQuitTime();
        Date otherLastQuitTime = that.getLastQuitTime();
        if (lastQuitTime == null ? otherLastQuitTime != null : !((Object)lastQuitTime).equals(otherLastQuitTime)) {
            return false;
        }
        Date pickaxeTime = this.getPickaxeTime();
        Date otherPickaxeTime = that.getPickaxeTime();
        if (pickaxeTime == null ? otherPickaxeTime != null : !((Object)pickaxeTime).equals(otherPickaxeTime)) {
            return false;
        }
        String logoName = this.getLogoName();
        String otherLogoName = that.getLogoName();
        return !(logoName == null ? otherLogoName != null : !logoName.equals(otherLogoName));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof GuildPlayer;
    }

    @Generated
    public int hashCode() {
        int hashSeed = 59;
        int result = 1;
        Integer id = this.getId();
        result = result * 59 + (id == null ? 43 : ((Object)id).hashCode());
        Integer guildInfoId = this.getGuildInfoId();
        result = result * 59 + (guildInfoId == null ? 43 : ((Object)guildInfoId).hashCode());
        Integer money = this.getMoney();
        result = result * 59 + (money == null ? 43 : ((Object)money).hashCode());
        Integer weekMoney = this.getWeekMoney();
        result = result * 59 + (weekMoney == null ? 43 : ((Object)weekMoney).hashCode());
        Integer totalMoney = this.getTotalMoney();
        result = result * 59 + (totalMoney == null ? 43 : ((Object)totalMoney).hashCode());
        Integer guildMoney = this.getGuildMoney();
        result = result * 59 + (guildMoney == null ? 43 : ((Object)guildMoney).hashCode());
        Integer roleId = this.getRoleId();
        result = result * 59 + (roleId == null ? 43 : ((Object)roleId).hashCode());
        Boolean pvpStatus = this.getPvpStatus();
        result = result * 59 + (pvpStatus == null ? 43 : ((Object)pvpStatus).hashCode());
        Integer ore = this.getOre();
        result = result * 59 + (ore == null ? 43 : ((Object)ore).hashCode());
        Integer totalOre = this.getTotalOre();
        result = result * 59 + (totalOre == null ? 43 : ((Object)totalOre).hashCode());
        Integer kill = this.getKill();
        result = result * 59 + (kill == null ? 43 : ((Object)kill).hashCode());
        Integer die = this.getDie();
        result = result * 59 + (die == null ? 43 : ((Object)die).hashCode());
        Integer level = this.getLevel();
        result = result * 59 + (level == null ? 43 : ((Object)level).hashCode());
        Integer seasonRank = this.getSeasonRank();
        result = result * 59 + (seasonRank == null ? 43 : ((Object)seasonRank).hashCode());
        Integer memberCount = this.getMemberCount();
        result = result * 59 + (memberCount == null ? 43 : ((Object)memberCount).hashCode());
        Integer memberMaxCount = this.getMemberMaxCount();
        result = result * 59 + (memberMaxCount == null ? 43 : ((Object)memberMaxCount).hashCode());
        Integer guildInfoMoney = this.getGuildInfoMoney();
        result = result * 59 + (guildInfoMoney == null ? 43 : ((Object)guildInfoMoney).hashCode());
        Integer prosperityDegree = this.getProsperityDegree();
        result = result * 59 + (prosperityDegree == null ? 43 : ((Object)prosperityDegree).hashCode());
        Integer monthProsperityDegree = this.getMonthProsperityDegree();
        result = result * 59 + (monthProsperityDegree == null ? 43 : ((Object)monthProsperityDegree).hashCode());
        Integer guildInfoOre = this.getGuildInfoOre();
        result = result * 59 + (guildInfoOre == null ? 43 : ((Object)guildInfoOre).hashCode());
        String guildInfoName = this.getGuildInfoName();
        result = result * 59 + (guildInfoName == null ? 43 : guildInfoName.hashCode());
        String playerName = this.getPlayerName();
        result = result * 59 + (playerName == null ? 43 : playerName.hashCode());
        UUID playerUuid = this.getPlayerUuid();
        result = result * 59 + (playerUuid == null ? 43 : ((Object)playerUuid).hashCode());
        Date joinTime = this.getJoinTime();
        result = result * 59 + (joinTime == null ? 43 : ((Object)joinTime).hashCode());
        Date lastJoinTime = this.getLastJoinTime();
        result = result * 59 + (lastJoinTime == null ? 43 : ((Object)lastJoinTime).hashCode());
        Date lastQuitTime = this.getLastQuitTime();
        result = result * 59 + (lastQuitTime == null ? 43 : ((Object)lastQuitTime).hashCode());
        Date pickaxeTime = this.getPickaxeTime();
        result = result * 59 + (pickaxeTime == null ? 43 : ((Object)pickaxeTime).hashCode());
        String logoName = this.getLogoName();
        result = result * 59 + (logoName == null ? 43 : logoName.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "GuildPlayer(id=" + this.getId() + ", guildInfoId=" + this.getGuildInfoId() + ", guildInfoName=" + this.getGuildInfoName() + ", playerName=" + this.getPlayerName() + ", playerUuid=" + this.getPlayerUuid() + ", money=" + this.getMoney() + ", weekMoney=" + this.getWeekMoney() + ", totalMoney=" + this.getTotalMoney() + ", guildMoney=" + this.getGuildMoney() + ", roleId=" + this.getRoleId() + ", joinTime=" + this.getJoinTime() + ", lastJoinTime=" + this.getLastJoinTime() + ", lastQuitTime=" + this.getLastQuitTime() + ", pvpStatus=" + this.getPvpStatus() + ", pickaxeTime=" + this.getPickaxeTime() + ", ore=" + this.getOre() + ", totalOre=" + this.getTotalOre() + ", kill=" + this.getKill() + ", die=" + this.getDie() + ", level=" + this.getLevel() + ", seasonRank=" + this.getSeasonRank() + ", logoName=" + this.getLogoName() + ", memberCount=" + this.getMemberCount() + ", memberMaxCount=" + this.getMemberMaxCount() + ", guildInfoMoney=" + this.getGuildInfoMoney() + ", prosperityDegree=" + this.getProsperityDegree() + ", monthProsperityDegree=" + this.getMonthProsperityDegree() + ", guildInfoOre=" + this.getGuildInfoOre() + ")";
    }
}
