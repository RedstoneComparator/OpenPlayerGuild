package cn.handyplus.guild.enter;

import cn.handyplus.guild.lib.IndexEnum;
import cn.handyplus.guild.lib.TableField;
import cn.handyplus.guild.lib.TableName;
import java.util.Date;
import java.util.UUID;
import lombok.Generated;

@TableName(value="guild_player_pickaxe", comment="\u516c\u4f1a\u73a9\u5bb6\u7a3f\u5b50\u8bb0\u5f55")
public class GuildPlayerPickaxe {
    @TableField(value="id", comment="ID")
    private Integer id;
    @TableField(value="guild_info_id", comment="\u516c\u4f1aid", notNull=true, indexEnum=IndexEnum.INDEX)
    private Integer guildInfoId;
    @TableField(value="player_name", comment="\u73a9\u5bb6\u540d\u79f0", notNull=true)
    private String playerName;
    @TableField(value="player_uuid", comment="\u73a9\u5bb6uuid", notNull=true)
    private UUID playerUuid;
    @TableField(value="pickaxe_time", comment="\u795e\u77f3\u6316\u6398\u65f6\u95f4", notNull=true)
    private Date pickaxeTime;
    @TableField(value="type", comment="\u7a3f\u5b50\u7c7b\u578b", notNull=true)
    private String type;
    @TableField(value="rewards", comment="\u672c\u6b21\u6316\u6398\u5956\u52b1", notNull=true)
    private Integer rewards;

    @Generated
    public GuildPlayerPickaxe() {
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
    public Date getPickaxeTime() {
        return this.pickaxeTime;
    }

    @Generated
    public String getType() {
        return this.type;
    }

    @Generated
    public Integer getRewards() {
        return this.rewards;
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
    public void setPickaxeTime(Date pickaxeTime) {
        this.pickaxeTime = pickaxeTime;
    }

    @Generated
    public void setType(String type) {
        this.type = type;
    }

    @Generated
    public void setRewards(Integer rewards) {
        this.rewards = rewards;
    }

    @Generated
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof GuildPlayerPickaxe)) {
            return false;
        }
        GuildPlayerPickaxe otherGuild = (GuildPlayerPickaxe)other;
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
        Integer thisRewards = this.getRewards();
        Integer otherRewards = otherGuild.getRewards();
        if (thisRewards == null ? otherRewards != null : !((Object)thisRewards).equals(otherRewards)) {
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
        Date thisPickaxeTime = this.getPickaxeTime();
        Date otherPickaxeTime = otherGuild.getPickaxeTime();
        if (thisPickaxeTime == null ? otherPickaxeTime != null : !((Object)thisPickaxeTime).equals(otherPickaxeTime)) {
            return false;
        }
        String thisType = this.getType();
        String otherType = otherGuild.getType();
        return !(thisType == null ? otherType != null : !thisType.equals(otherType));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof GuildPlayerPickaxe;
    }

    @Generated
    public int hashCode() {
        int prime = 59;
        int hash = 1;
        Integer idValue = this.getId();
        hash = hash * 59 + (idValue == null ? 43 : ((Object)idValue).hashCode());
        Integer guildInfoIdValue = this.getGuildInfoId();
        hash = hash * 59 + (guildInfoIdValue == null ? 43 : ((Object)guildInfoIdValue).hashCode());
        Integer rewardsValue = this.getRewards();
        hash = hash * 59 + (rewardsValue == null ? 43 : ((Object)rewardsValue).hashCode());
        String playerNameValue = this.getPlayerName();
        hash = hash * 59 + (playerNameValue == null ? 43 : playerNameValue.hashCode());
        UUID playerUuidValue = this.getPlayerUuid();
        hash = hash * 59 + (playerUuidValue == null ? 43 : ((Object)playerUuidValue).hashCode());
        Date pickaxeTimeValue = this.getPickaxeTime();
        hash = hash * 59 + (pickaxeTimeValue == null ? 43 : ((Object)pickaxeTimeValue).hashCode());
        String typeValue = this.getType();
        hash = hash * 59 + (typeValue == null ? 43 : typeValue.hashCode());
        return hash;
    }

    @Generated
    public String toString() {
        return "GuildPlayerPickaxe(id=" + this.getId() + ", guildInfoId=" + this.getGuildInfoId() + ", playerName=" + this.getPlayerName() + ", playerUuid=" + this.getPlayerUuid() + ", pickaxeTime=" + this.getPickaxeTime() + ", type=" + this.getType() + ", rewards=" + this.getRewards() + ")";
    }
}
