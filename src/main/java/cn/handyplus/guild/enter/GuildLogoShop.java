package cn.handyplus.guild.enter;

import cn.handyplus.guild.lib.TableField;
import cn.handyplus.guild.lib.TableName;
import java.io.Serializable;
import lombok.Generated;

@TableName(value="guild_logo_shop", comment="\u516c\u4f1alogo\u5546\u57ce")
public class GuildLogoShop
implements Serializable {
    @TableField(value="id", comment="ID")
    private Integer id;
    @TableField(value="price", comment="\u4ef7\u683c", notNull=true)
    private Integer price;
    @TableField(value="item_stack", comment="\u81ea\u5b9a\u4e49\u7269\u54c1", length=20000, notNull=true)
    private String itemStack;
    @TableField(value="level", comment="\u9700\u6c42\u516c\u4f1a\u7b49\u7ea7", notNull=true, fieldDefault="1")
    private Integer level;
    @TableField(value="default_logo_flag", comment="\u662f\u5426\u9ed8\u8ba4logo", notNull=true, fieldDefault="0")
    private Boolean defaultLogoFlag;

    @Generated
    public GuildLogoShop() {
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
    public Boolean getDefaultLogoFlag() {
        return this.defaultLogoFlag;
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
    public void setDefaultLogoFlag(Boolean defaultLogoFlag) {
        this.defaultLogoFlag = defaultLogoFlag;
    }

    @Generated
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof GuildLogoShop)) {
            return false;
        }
        GuildLogoShop that = (GuildLogoShop)other;
        if (!that.canEqual(this)) {
            return false;
        }
        Integer id = this.getId();
        Integer otherId = that.getId();
        if (id == null ? otherId != null : !((Object)id).equals(otherId)) {
            return false;
        }
        Integer price = this.getPrice();
        Integer otherPrice = that.getPrice();
        if (price == null ? otherPrice != null : !((Object)price).equals(otherPrice)) {
            return false;
        }
        Integer level = this.getLevel();
        Integer otherLevel = that.getLevel();
        if (level == null ? otherLevel != null : !((Object)level).equals(otherLevel)) {
            return false;
        }
        Boolean defaultLogoFlag = this.getDefaultLogoFlag();
        Boolean otherDefaultLogoFlag = that.getDefaultLogoFlag();
        if (defaultLogoFlag == null ? otherDefaultLogoFlag != null : !((Object)defaultLogoFlag).equals(otherDefaultLogoFlag)) {
            return false;
        }
        String itemStack = this.getItemStack();
        String otherItemStack = that.getItemStack();
        return !(itemStack == null ? otherItemStack != null : !itemStack.equals(otherItemStack));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof GuildLogoShop;
    }

    @Generated
    public int hashCode() {
        int hashSeed = 59;
        int result = 1;
        Integer id = this.getId();
        result = result * 59 + (id == null ? 43 : ((Object)id).hashCode());
        Integer price = this.getPrice();
        result = result * 59 + (price == null ? 43 : ((Object)price).hashCode());
        Integer level = this.getLevel();
        result = result * 59 + (level == null ? 43 : ((Object)level).hashCode());
        Boolean defaultLogoFlag = this.getDefaultLogoFlag();
        result = result * 59 + (defaultLogoFlag == null ? 43 : ((Object)defaultLogoFlag).hashCode());
        String itemStack = this.getItemStack();
        result = result * 59 + (itemStack == null ? 43 : itemStack.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "GuildLogoShop(id=" + this.getId() + ", price=" + this.getPrice() + ", itemStack=" + this.getItemStack() + ", level=" + this.getLevel() + ", defaultLogoFlag=" + this.getDefaultLogoFlag() + ")";
    }
}
