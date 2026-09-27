package cn.handyplus.guild.constants;

import lombok.Generated;

public enum ShopSearchEnum {
    ALL("all", "全部"),
    SYSTEM("system", "系统"),
    PLAYER("player", "玩家");

    private final String description;
    private final String typeName;

    @Generated
    ShopSearchEnum(String type, String desc) {
        this.typeName = type;
        this.description = desc;
    }

    @Generated
    public String getType() {
        return this.typeName;
    }

    @Generated
    public String getDesc() {
        return this.description;
    }
}
