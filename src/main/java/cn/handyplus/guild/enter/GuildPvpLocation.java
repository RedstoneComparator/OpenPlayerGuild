package cn.handyplus.guild.enter;

import cn.handyplus.guild.lib.IndexEnum;
import cn.handyplus.guild.lib.TableField;
import cn.handyplus.guild.lib.TableName;
import lombok.Generated;

@TableName(value="guild_pvp_location", comment="\u516c\u4f1a\u6218\u4f4d\u7f6e")
public class GuildPvpLocation {
    @TableField(value="id", comment="ID")
    private Integer id;
    @TableField(value="name", comment="\u540d\u79f0", notNull=true, indexEnum=IndexEnum.UNIQUE, uniqueGroup="name_type")
    private String name;
    @TableField(value="type", comment="\u7c7b\u578b", notNull=true, fieldDefault="mate", indexEnum=IndexEnum.UNIQUE, uniqueGroup="name_type")
    private String type;

    @Generated
    public GuildPvpLocation() {
    }

    @Generated
    public Integer getId() {
        return this.id;
    }

    @Generated
    public String getName() {
        return this.name;
    }

    @Generated
    public String getType() {
        return this.type;
    }

    @Generated
    public void setId(Integer id) {
        this.id = id;
    }

    @Generated
    public void setName(String name) {
        this.name = name;
    }

    @Generated
    public void setType(String type) {
        this.type = type;
    }

    @Generated
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof GuildPvpLocation)) {
            return false;
        }
        GuildPvpLocation otherGuild = (GuildPvpLocation)other;
        if (!otherGuild.canEqual(this)) {
            return false;
        }
        Integer thisId = this.getId();
        Integer otherId = otherGuild.getId();
        if (thisId == null ? otherId != null : !((Object)thisId).equals(otherId)) {
            return false;
        }
        String thisName = this.getName();
        String otherName = otherGuild.getName();
        if (thisName == null ? otherName != null : !thisName.equals(otherName)) {
            return false;
        }
        String thisType = this.getType();
        String otherType = otherGuild.getType();
        return !(thisType == null ? otherType != null : !thisType.equals(otherType));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof GuildPvpLocation;
    }

    @Generated
    public int hashCode() {
        int prime = 59;
        int hash = 1;
        Integer idValue = this.getId();
        hash = hash * 59 + (idValue == null ? 43 : ((Object)idValue).hashCode());
        String nameValue = this.getName();
        hash = hash * 59 + (nameValue == null ? 43 : nameValue.hashCode());
        String typeValue = this.getType();
        hash = hash * 59 + (typeValue == null ? 43 : typeValue.hashCode());
        return hash;
    }

    @Generated
    public String toString() {
        return "GuildPvpLocation(id=" + this.getId() + ", name=" + this.getName() + ", type=" + this.getType() + ")";
    }
}
