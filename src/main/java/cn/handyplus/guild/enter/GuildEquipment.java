package cn.handyplus.guild.enter;

import cn.handyplus.guild.lib.TableField;
import cn.handyplus.guild.lib.TableName;
import lombok.Generated;

@TableName(value="guild_equipment", comment="\u516c\u4f1a\u81ea\u5b9a\u4e49\u88c5\u5907")
public class GuildEquipment {
    @TableField(value="id", comment="ID")
    private Integer id;
    @TableField(value="item_stack", comment="\u81ea\u5b9a\u4e49\u7269\u54c1", length=20000, notNull=true)
    private String itemStack;
    @TableField(value="slot", comment="\u7269\u54c1\u4f4d\u7f6e")
    private String slot;

    @Generated
    public GuildEquipment() {
    }

    @Generated
    public Integer getId() {
        return this.id;
    }

    @Generated
    public String getItemStack() {
        return this.itemStack;
    }

    @Generated
    public String getSlot() {
        return this.slot;
    }

    @Generated
    public void setId(Integer id) {
        this.id = id;
    }

    @Generated
    public void setItemStack(String itemStack) {
        this.itemStack = itemStack;
    }

    @Generated
    public void setSlot(String slot) {
        this.slot = slot;
    }

    @Generated
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof GuildEquipment)) {
            return false;
        }
        GuildEquipment that = (GuildEquipment)other;
        if (!that.canEqual(this)) {
            return false;
        }
        Integer id = this.getId();
        Integer otherId = that.getId();
        if (id == null ? otherId != null : !((Object)id).equals(otherId)) {
            return false;
        }
        String itemStack = this.getItemStack();
        String otherItemStack = that.getItemStack();
        if (itemStack == null ? otherItemStack != null : !itemStack.equals(otherItemStack)) {
            return false;
        }
        String slot = this.getSlot();
        String otherSlot = that.getSlot();
        return !(slot == null ? otherSlot != null : !slot.equals(otherSlot));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof GuildEquipment;
    }

    @Generated
    public int hashCode() {
        int hashSeed = 59;
        int result = 1;
        Integer id = this.getId();
        result = result * 59 + (id == null ? 43 : ((Object)id).hashCode());
        String itemStack = this.getItemStack();
        result = result * 59 + (itemStack == null ? 43 : itemStack.hashCode());
        String slot = this.getSlot();
        result = result * 59 + (slot == null ? 43 : slot.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "GuildEquipment(id=" + this.getId() + ", itemStack=" + this.getItemStack() + ", slot=" + this.getSlot() + ")";
    }
}
