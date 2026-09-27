package cn.handyplus.guild.enter;

import cn.handyplus.guild.lib.IndexEnum;
import cn.handyplus.guild.lib.TableField;
import cn.handyplus.guild.lib.TableName;
import java.io.Serializable;
import lombok.Generated;

@TableName(value="guild_logo", comment="\u516c\u4f1a\u62e5\u6709logo")
public class GuildLogo
implements Serializable {
    @TableField(value="id", comment="ID")
    private Integer id;
    @TableField(value="guild_info_id", comment="\u516c\u4f1aid", notNull=true, indexEnum=IndexEnum.INDEX)
    private Integer guildInfoId;
    @TableField(value="logo_id", comment="logoId")
    private Integer logoId;
    @TableField(value="logo_name", comment="logo", length=10000, notNull=true)
    private String logoName;
    @TableField(value="use_flag", comment="\u662f\u5426\u4f7f\u7528")
    private Boolean useFlag;

    @Generated
    public GuildLogo() {
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
    public Integer getLogoId() {
        return this.logoId;
    }

    @Generated
    public String getLogoName() {
        return this.logoName;
    }

    @Generated
    public Boolean getUseFlag() {
        return this.useFlag;
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
    public void setLogoId(Integer logoId) {
        this.logoId = logoId;
    }

    @Generated
    public void setLogoName(String logoName) {
        this.logoName = logoName;
    }

    @Generated
    public void setUseFlag(Boolean useFlag) {
        this.useFlag = useFlag;
    }

    @Generated
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof GuildLogo)) {
            return false;
        }
        GuildLogo that = (GuildLogo)other;
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
        Integer logoId = this.getLogoId();
        Integer otherLogoId = that.getLogoId();
        if (logoId == null ? otherLogoId != null : !((Object)logoId).equals(otherLogoId)) {
            return false;
        }
        Boolean useFlag = this.getUseFlag();
        Boolean otherUseFlag = that.getUseFlag();
        if (useFlag == null ? otherUseFlag != null : !((Object)useFlag).equals(otherUseFlag)) {
            return false;
        }
        String logoName = this.getLogoName();
        String otherLogoName = that.getLogoName();
        return !(logoName == null ? otherLogoName != null : !logoName.equals(otherLogoName));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof GuildLogo;
    }

    @Generated
    public int hashCode() {
        int hashSeed = 59;
        int result = 1;
        Integer id = this.getId();
        result = result * 59 + (id == null ? 43 : ((Object)id).hashCode());
        Integer guildInfoId = this.getGuildInfoId();
        result = result * 59 + (guildInfoId == null ? 43 : ((Object)guildInfoId).hashCode());
        Integer logoId = this.getLogoId();
        result = result * 59 + (logoId == null ? 43 : ((Object)logoId).hashCode());
        Boolean useFlag = this.getUseFlag();
        result = result * 59 + (useFlag == null ? 43 : ((Object)useFlag).hashCode());
        String logoName = this.getLogoName();
        result = result * 59 + (logoName == null ? 43 : logoName.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "GuildLogo(id=" + this.getId() + ", guildInfoId=" + this.getGuildInfoId() + ", logoId=" + this.getLogoId() + ", logoName=" + this.getLogoName() + ", useFlag=" + this.getUseFlag() + ")";
    }
}
