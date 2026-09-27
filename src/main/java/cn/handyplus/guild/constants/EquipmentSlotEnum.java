package cn.handyplus.guild.constants;

import java.util.ArrayList;
import java.util.List;
import lombok.Generated;

public enum EquipmentSlotEnum {
    HAND("HAND"),
    OFF_HAND("OFF_HAND"),
    FEET("FEET"),
    LEGS("LEGS"),
    CHEST("CHEST");

    private final String slotType;

    @Generated
    EquipmentSlotEnum(String type) {
        this.slotType = type;
    }

    @Generated
    public String getType() {
        return this.slotType;
    }

    public static EquipmentSlotEnum getType(String typeName) {
        for (EquipmentSlotEnum equipmentSlotEnum : values()) {
            if (equipmentSlotEnum.getType().equalsIgnoreCase(typeName)) {
                return equipmentSlotEnum;
            }
        }
        return null;
    }

    public static List<String> getEquipmentSlotList() {
        ArrayList<String> typeList = new ArrayList<String>();
        for (EquipmentSlotEnum equipmentSlotEnum : values()) {
            typeList.add(equipmentSlotEnum.getType());
        }
        return typeList;
    }
}
