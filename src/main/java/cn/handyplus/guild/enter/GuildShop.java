package cn.handyplus.guild.enter;

import cn.handyplus.guild.lib.IndexEnum;
import cn.handyplus.guild.lib.TableField;
import cn.handyplus.guild.lib.TableName;
import java.util.UUID;
import lombok.Generated;

@TableName(value="guild_shop", comment="\u516c\u4f1a\u5546\u5e97")
public class GuildShop {
    @TableField(value="id", comment="ID")
    private Integer id;
    @TableField(value="price", comment="\u4ef7\u683c", notNull=true)
    private Integer price;
    @TableField(value="item_stack", comment="\u81ea\u5b9a\u4e49\u7269\u54c1", length=20000, notNull=true)
    private String itemStack;
    @TableField(value="level", comment="\u9700\u6c42\u516c\u4f1a\u7b49\u7ea7", notNull=true, fieldDefault="1")
    private Integer level;
    @TableField(value="number", comment="\u9650\u8d2d\u6b21\u6570", notNull=true, fieldDefault="0")
    private Integer number;
    @TableField(value="type", comment="\u9650\u8d2d\u7c7b\u578b", notNull=true, fieldDefault="only")
    private String type;
    @TableField(value="player_name", comment="\u4e0a\u67b6\u73a9\u5bb6\u540d\u79f0")
    private String playerName;
    @TableField(value="player_uuid", comment="\u4e0a\u67b6\u73a9\u5bb6uuid")
    private UUID playerUuid;
    @TableField(value="guild_info_id", comment="\u516c\u4f1aid", notNull=true, fieldDefault="0", indexEnum=IndexEnum.INDEX)
    private Integer guildInfoId;

    @Generated
    public GuildShop() {
    }

    @Generated
    public Integer getId() {
        return this.id;
    }

    @Generated
    public Integer getPrice() {
        return this.price;
    }

    @Generated
    public String getItemStack() {
        return this.itemStack;
    }

    @Generated
    public Integer getLevel() {
        return this.level;
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
    public String getPlayerName() {
        return this.playerName;
    }

    @Generated
    public UUID getPlayerUuid() {
        return this.playerUuid;
    }

    @Generated
    public Integer getGuildInfoId() {
        return this.guildInfoId;
    }

    @Generated
    public void setId(Integer id) {
        this.id = id;
    }

    @Generated
    public void setPrice(Integer price) {
        this.price = price;
    }

    @Generated
    public void setItemStack(String itemStack) {
        this.itemStack = itemStack;
    }

    @Generated
    public void setLevel(Integer level) {
        this.level = level;
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
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    @Generated
    public void setPlayerUuid(UUID playerUuid) {
        this.playerUuid = playerUuid;
    }

    @Generated
    public void setGuildInfoId(Integer guildInfoId) {
        this.guildInfoId = guildInfoId;
    }

    @Generated
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof GuildShop)) {
            return false;
        }
        GuildShop otherGuild = (GuildShop)other;
        if (!otherGuild.canEqual(this)) {
            return false;
        }
        Integer thisId = this.getId();
        Integer otherId = otherGuild.getId();
        if (thisId == null ? otherId != null : !((Object)thisId).equals(otherId)) {
            return false;
        }
        Integer thisPrice = this.getPrice();
        Integer otherPrice = otherGuild.getPrice();
        if (thisPrice == null ? otherPrice != null : !((Object)thisPrice).equals(otherPrice)) {
            return false;
        }
        Integer thisLevel = this.getLevel();
        Integer otherLevel = otherGuild.getLevel();
        if (thisLevel == null ? otherLevel != null : !((Object)thisLevel).equals(otherLevel)) {
            return false;
        }
        Integer thisNumber = this.getNumber();
        Integer otherNumber = otherGuild.getNumber();
        if (thisNumber == null ? otherNumber != null : !((Object)thisNumber).equals(otherNumber)) {
            return false;
        }
        Integer thisGuildInfoId = this.getGuildInfoId();
        Integer otherGuildInfoId = otherGuild.getGuildInfoId();
        if (thisGuildInfoId == null ? otherGuildInfoId != null : !((Object)thisGuildInfoId).equals(otherGuildInfoId)) {
            return false;
        }
        String thisItemStack = this.getItemStack();
        String otherItemStack = otherGuild.getItemStack();
        if (thisItemStack == null ? otherItemStack != null : !thisItemStack.equals(otherItemStack)) {
            return false;
        }
        String thisType = this.getType();
        String otherType = otherGuild.getType();
        if (thisType == null ? otherType != null : !thisType.equals(otherType)) {
            return false;
        }
        String thisPlayerName = this.getPlayerName();
        String otherPlayerName = otherGuild.getPlayerName();
        if (thisPlayerName == null ? otherPlayerName != null : !thisPlayerName.equals(otherPlayerName)) {
            return false;
        }
        UUID thisPlayerUuid = this.getPlayerUuid();
        UUID otherPlayerUuid = otherGuild.getPlayerUuid();
        return !(thisPlayerUuid == null ? otherPlayerUuid != null : !((Object)thisPlayerUuid).equals(otherPlayerUuid));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof GuildShop;
    }

    @Generated
    public int hashCode() {
        int prime = 59;
        int hash = 1;
        Integer idValue = this.getId();
        hash = hash * 59 + (idValue == null ? 43 : ((Object)idValue).hashCode());
        Integer priceValue = this.getPrice();
        hash = hash * 59 + (priceValue == null ? 43 : ((Object)priceValue).hashCode());
        Integer levelValue = this.getLevel();
        hash = hash * 59 + (levelValue == null ? 43 : ((Object)levelValue).hashCode());
        Integer numberValue = this.getNumber();
        hash = hash * 59 + (numberValue == null ? 43 : ((Object)numberValue).hashCode());
        Integer guildInfoIdValue = this.getGuildInfoId();
        hash = hash * 59 + (guildInfoIdValue == null ? 43 : ((Object)guildInfoIdValue).hashCode());
        String itemStackValue = this.getItemStack();
        hash = hash * 59 + (itemStackValue == null ? 43 : itemStackValue.hashCode());
        String typeValue = this.getType();
        hash = hash * 59 + (typeValue == null ? 43 : typeValue.hashCode());
        String playerNameValue = this.getPlayerName();
        hash = hash * 59 + (playerNameValue == null ? 43 : playerNameValue.hashCode());
        UUID playerUuidValue = this.getPlayerUuid();
        hash = hash * 59 + (playerUuidValue == null ? 43 : ((Object)playerUuidValue).hashCode());
        return hash;
    }

    @Generated
    public String toString() {
        return "GuildShop(id=" + this.getId() + ", price=" + this.getPrice() + ", itemStack=" + this.getItemStack() + ", level=" + this.getLevel() + ", number=" + this.getNumber() + ", type=" + this.getType() + ", playerName=" + this.getPlayerName() + ", playerUuid=" + this.getPlayerUuid() + ", guildInfoId=" + this.getGuildInfoId() + ")";
    }
}
