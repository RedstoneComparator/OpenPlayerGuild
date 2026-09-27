package cn.handyplus.guild.constants;

import lombok.Generated;

public enum GuiTypeEnum {
    GUILD_ACTIVITY("guild_activity"),
    GUILD_APPLY("guild_apply"),
    GUILD_BANK("guild_bank"),
    GUILD_CREATE("guild_create"),
    GUILD_WORLD_CREATE("guild_world_create"),
    GUILD_EQUIPMENT("guild_equipment"),
    GUILD_INVITATION("guild_invitation"),
    GUILD_LOGO_OPEN("guild_logo_open"),
    GUILD_LOGO_SHOP("guild_logo_shop"),
    GUILD_MANAGE_MEMBER_CREATE("guild_manage_member_create"),
    GUILD_MEMBER("guild_member"),
    GUILD_OPEN("guild_open"),
    GUILD_SACRED_STONE("guild_sacred_stone"),
    GUILD_SETTING("guild_setting"),
    GUILD_SHOP("guild_shop"),
    GUILD_SIGN_IN("guild_sign_in"),
    GUILD_TASK("guild_task"),
    GUILD_TITLE("guild_title"),
    GUILD_VIEW("guild_view"),
    GUILD_NOTICE("guild_notice"),
    VIEW_LOCATION("view_location"),
    VIEW_LOGO_SHOP("view_logo_shop"),
    VIEW_SHOP("view_shop"),
    VIEW_SPAWN_LOCATION("view_spawn_location"),
    VIEW_NOTICE("view_notice"),
    CONFIRM("confirm");

    private final String typeName;

    @Generated
    GuiTypeEnum(String type) {
        this.typeName = type;
    }

    @Generated
    public String getType() {
        return this.typeName;
    }
}
