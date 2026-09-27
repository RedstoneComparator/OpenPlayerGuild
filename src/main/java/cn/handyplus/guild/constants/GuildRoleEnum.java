package cn.handyplus.guild.constants;

import cn.handyplus.guild.lib.BaseUtil;
import java.util.ArrayList;
import java.util.List;
import lombok.Generated;

public enum GuildRoleEnum {
    ONE(Integer.valueOf(1), "role.one"),
    TWO(Integer.valueOf(2), "role.two"),
    THREE(Integer.valueOf(3), "role.three"),
    FOUR(Integer.valueOf(4), "role.four");

    private final String nameKey;
    private final Integer roleNumber;

    @Generated
    GuildRoleEnum(Integer roleId, String langKey) {
        this.roleNumber = roleId;
        this.nameKey = langKey;
    }

    public static List<String> getEnum() {
        ArrayList<String> roleIds = new ArrayList<String>();
        for (GuildRoleEnum guildRoleEnum : values()) {
            roleIds.add(guildRoleEnum.getRoleId().toString());
        }
        return roleIds;
    }

    public static String getRoleName(GuildRoleEnum role) {
        if (role != null) {
            return BaseUtil.getLangMsg(role.nameKey);
        }
        return null;
    }

    public static GuildRoleEnum getEnumThrow(Integer roleId) {
        if (roleId < 1) {
            roleId = 1;
        }
        if (roleId > 4) {
            roleId = 4;
        }
        for (GuildRoleEnum guildRoleEnum : values()) {
            if (guildRoleEnum.roleNumber.equals(roleId)) {
                return guildRoleEnum;
            }
        }
        throw new RuntimeException("错误的公会角色!!");
    }

    public static String getRoleName(Integer roleId) {
        return GuildRoleEnum.getRoleName(GuildRoleEnum.getEnumThrow(roleId));
    }

    @Generated
    public Integer getRoleId() {
        return this.roleNumber;
    }
}
