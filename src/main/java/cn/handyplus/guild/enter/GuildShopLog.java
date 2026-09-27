package cn.handyplus.guild.enter;

import cn.handyplus.guild.lib.IndexEnum;
import cn.handyplus.guild.lib.TableField;
import cn.handyplus.guild.lib.TableName;
import java.util.Date;
import java.util.UUID;
import lombok.Generated;

@TableName(value="guild_shop_log", comment="\u516c\u4f1a\u5546\u5e97\u73a9\u5bb6\u8d2d\u4e70\u8bb0\u5f55")
public class GuildShopLog {
    @TableField(value="id", comment="ID")
    private Integer id;
    @TableField(value="guild_info_id", comment="\u516c\u4f1aid", notNull=true, indexEnum=IndexEnum.INDEX)
    private Integer guildInfoId;
    @TableField(value="guild_info_name", comment="\u516c\u4f1a\u540d\u79f0", notNull=true, length=190)
    private String guildInfoName;
    @TableField(value="player_name", comment="\u73a9\u5bb6\u540d\u79f0", notNull=true)
    private String playerName;
    @TableField(value="player_uuid", comment="\u73a9\u5bb6uuid", notNull=true)
    private UUID playerUuid;
    @TableField(value="guild_shop_id", comment="\u516c\u4f1a\u5546\u5e97\u7269\u54c1id", notNull=true)
    private Integer guildShopId;
    @TableField(value="number", comment="\u8d2d\u4e70\u6b21\u6570", notNull=true, fieldDefault="0")
    private Integer number;
    @TableField(value="type", comment="\u7c7b\u578b", notNull=true, fieldDefault="only")
    private String type;
    @TableField(value="buy_time", comment="\u8d2d\u4e70\u65f6\u95f4")
    private Date buyTime;

    @Generated
    public GuildShopLog() {
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
    public Integer getGuildShopId() {
        return this.guildShopId;
    }

    @Generated
    public Integer getNumber() {
        return this.number;
    }

    @Generated
    public String getType() {
        return this.type;
    }

    @Generated
    public Date getBuyTime() {
        return this.buyTime;
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
    public void setGuildShopId(Integer guildShopId) {
        this.guildShopId = guildShopId;
    }

    @Generated
    public void setNumber(Integer number) {
        this.number = number;
    }

    @Generated
    public void setType(String type) {
        this.type = type;
    }

    @Generated
    public void setBuyTime(Date buyTime) {
        this.buyTime = buyTime;
    }

    @Generated
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof GuildShopLog)) {
            return false;
        }
        GuildShopLog otherGuild = (GuildShopLog)other;
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
        Integer thisGuildShopId = this.getGuildShopId();
        Integer otherGuildShopId = otherGuild.getGuildShopId();
        if (thisGuildShopId == null ? otherGuildShopId != null : !((Object)thisGuildShopId).equals(otherGuildShopId)) {
            return false;
        }
        Integer thisNumber = this.getNumber();
        Integer otherNumber = otherGuild.getNumber();
        if (thisNumber == null ? otherNumber != null : !((Object)thisNumber).equals(otherNumber)) {
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
        String thisType = this.getType();
        String otherType = otherGuild.getType();
        if (thisType == null ? otherType != null : !thisType.equals(otherType)) {
            return false;
        }
        Date thisBuyTime = this.getBuyTime();
        Date otherBuyTime = otherGuild.getBuyTime();
        return !(thisBuyTime == null ? otherBuyTime != null : !((Object)thisBuyTime).equals(otherBuyTime));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof GuildShopLog;
    }

    @Generated
    public int hashCode() {
        int prime = 59;
        int hash = 1;
        Integer idValue = this.getId();
        hash = hash * 59 + (idValue == null ? 43 : ((Object)idValue).hashCode());
        Integer guildInfoIdValue = this.getGuildInfoId();
        hash = hash * 59 + (guildInfoIdValue == null ? 43 : ((Object)guildInfoIdValue).hashCode());
        Integer guildShopIdValue = this.getGuildShopId();
        hash = hash * 59 + (guildShopIdValue == null ? 43 : ((Object)guildShopIdValue).hashCode());
        Integer numberValue = this.getNumber();
        hash = hash * 59 + (numberValue == null ? 43 : ((Object)numberValue).hashCode());
        String guildInfoNameValue = this.getGuildInfoName();
        hash = hash * 59 + (guildInfoNameValue == null ? 43 : guildInfoNameValue.hashCode());
        String playerNameValue = this.getPlayerName();
        hash = hash * 59 + (playerNameValue == null ? 43 : playerNameValue.hashCode());
        UUID playerUuidValue = this.getPlayerUuid();
        hash = hash * 59 + (playerUuidValue == null ? 43 : ((Object)playerUuidValue).hashCode());
        String typeValue = this.getType();
        hash = hash * 59 + (typeValue == null ? 43 : typeValue.hashCode());
        Date buyTimeValue = this.getBuyTime();
        hash = hash * 59 + (buyTimeValue == null ? 43 : ((Object)buyTimeValue).hashCode());
        return hash;
    }

    @Generated
    public String toString() {
        return "GuildShopLog(id=" + this.getId() + ", guildInfoId=" + this.getGuildInfoId() + ", guildInfoName=" + this.getGuildInfoName() + ", playerName=" + this.getPlayerName() + ", playerUuid=" + this.getPlayerUuid() + ", guildShopId=" + this.getGuildShopId() + ", number=" + this.getNumber() + ", type=" + this.getType() + ", buyTime=" + this.getBuyTime() + ")";
    }
}
