package cn.handyplus.guild.param;

import java.util.Arrays;
import lombok.Generated;
import org.bukkit.inventory.ItemStack;

public class PlayerItem {
    private ItemStack[] storageContents;
    private ItemStack[] armorContents;
    private ItemStack[] extraContents;
    private ItemStack itemOnCursor;

    @Generated
    public PlayerItem() {
    }

    @Generated
    public ItemStack[] getStorageContents() {
        return this.storageContents;
    }

    @Generated
    public ItemStack[] getArmorContents() {
        return this.armorContents;
    }

    @Generated
    public ItemStack[] getExtraContents() {
        return this.extraContents;
    }

    @Generated
    public ItemStack getItemOnCursor() {
        return this.itemOnCursor;
    }

    @Generated
    public void setStorageContents(ItemStack[] storageContents) {
        this.storageContents = storageContents;
    }

    @Generated
    public void setArmorContents(ItemStack[] armorContents) {
        this.armorContents = armorContents;
    }

    @Generated
    public void setExtraContents(ItemStack[] extraContents) {
        this.extraContents = extraContents;
    }

    @Generated
    public void setItemOnCursor(ItemStack itemOnCursor) {
        this.itemOnCursor = itemOnCursor;
    }

    @Generated
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof PlayerItem)) {
            return false;
        }
        PlayerItem otherItem = (PlayerItem)other;
        if (!otherItem.canEqual(this)) {
            return false;
        }
        if (!Arrays.deepEquals(this.getStorageContents(), otherItem.getStorageContents())) {
            return false;
        }
        if (!Arrays.deepEquals(this.getArmorContents(), otherItem.getArmorContents())) {
            return false;
        }
        if (!Arrays.deepEquals(this.getExtraContents(), otherItem.getExtraContents())) {
            return false;
        }
        ItemStack thisItemOnCursor = this.getItemOnCursor();
        ItemStack otherItemOnCursor = otherItem.getItemOnCursor();
        return !(thisItemOnCursor == null ? otherItemOnCursor != null : !thisItemOnCursor.equals(otherItemOnCursor));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof PlayerItem;
    }

    @Generated
    public int hashCode() {
        int prime = 59;
        int result = 1;
        result = result * 59 + Arrays.deepHashCode(this.getStorageContents());
        result = result * 59 + Arrays.deepHashCode(this.getArmorContents());
        result = result * 59 + Arrays.deepHashCode(this.getExtraContents());
        ItemStack itemOnCursor = this.getItemOnCursor();
        result = result * 59 + (itemOnCursor == null ? 43 : itemOnCursor.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "PlayerItem(storageContents=" + Arrays.deepToString(this.getStorageContents()) + ", armorContents=" + Arrays.deepToString(this.getArmorContents()) + ", extraContents=" + Arrays.deepToString(this.getExtraContents()) + ", itemOnCursor=" + this.getItemOnCursor() + ")";
    }
}
