package cn.handyplus.guild.enter;

import cn.handyplus.guild.lib.IndexEnum;
import cn.handyplus.guild.lib.TableField;
import cn.handyplus.guild.lib.TableName;
import java.io.Serializable;
import lombok.Generated;

@TableName(value="guild_buff", comment="\u516c\u4f1a\u5c5e\u6027")
public class GuildBuff
implements Serializable {
    @TableField(value="id", comment="ID")
    private Integer id;
    @TableField(value="guild_info_id", comment="\u516c\u4f1aid", notNull=true, indexEnum=IndexEnum.UNIQUE, uniqueGroup="guild_buff_type")
    private Integer guildInfoId;
    @TableField(value="buff_type", comment="buff\u7c7b\u578b", notNull=true, indexEnum=IndexEnum.UNIQUE, uniqueGroup="guild_buff_type")
    private String buffType;
    @TableField(value="level", comment="\u5c5e\u6027\u7b49\u7ea7")
    private Integer level;

    @Generated
    public GuildBuff() {
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
    public String getBuffType() {
        return this.buffType;
    }

    @Generated
    public Integer getLevel() {
        return this.level;
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
    public void setBuffType(String buffType) {
        this.buffType = buffType;
    }

    @Generated
    public void setLevel(Integer level) {
        this.level = level;
    }

    @Generated
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof GuildBuff)) {
            return false;
        }
        GuildBuff that = (GuildBuff)other;
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
        Integer level = this.getLevel();
        Integer otherLevel = that.getLevel();
        if (level == null ? otherLevel != null : !((Object)level).equals(otherLevel)) {
            return false;
        }
        String buffType = this.getBuffType();
        String otherBuffType = that.getBuffType();
        return !(buffType == null ? otherBuffType != null : !buffType.equals(otherBuffType));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof GuildBuff;
    }

    @Generated
    public int hashCode() {
        int hashSeed = 59;
        int result = 1;
        Integer id = this.getId();
        result = result * 59 + (id == null ? 43 : ((Object)id).hashCode());
        Integer guildInfoId = this.getGuildInfoId();
        result = result * 59 + (guildInfoId == null ? 43 : ((Object)guildInfoId).hashCode());
        Integer level = this.getLevel();
        result = result * 59 + (level == null ? 43 : ((Object)level).hashCode());
        String buffType = this.getBuffType();
        result = result * 59 + (buffType == null ? 43 : buffType.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "GuildBuff(id=" + this.getId() + ", guildInfoId=" + this.getGuildInfoId() + ", buffType=" + this.getBuffType() + ", level=" + this.getLevel() + ")";
    }
}
