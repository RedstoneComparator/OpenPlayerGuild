package cn.handyplus.guild.constants;

import lombok.Generated;

public enum GuildPvpBattleTypeEnum {
    MATCH("match"),
    SCORE_MATCH("score_match");

    private final String typeName;

    @Generated
    GuildPvpBattleTypeEnum(String type) {
        this.typeName = type;
    }

    @Generated
    public String getType() {
        return this.typeName;
    }
}
