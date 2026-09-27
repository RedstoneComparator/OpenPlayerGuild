package cn.handyplus.guild.constants;

import cn.handyplus.guild.lib.BaseUtil;
import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import org.jetbrains.annotations.NotNull;

public enum GuildActivityType {
    SEASON("season",
            "activityType.season"),
    MATCH("mate",
            "activityType.match"),
    SCORE_MATCH("score_match",
            "activityType.score_match"),
    STRONGHOLD("stronghold",
            "activityType.stronghold");

    private final String descriptionKey;
    private final String typeName;

    @Generated
    GuildActivityType(String type, String langKey) {
        this.typeName = type;
        this.descriptionKey = langKey;
    }

    public static String getDesc(GuildActivityType activity) {
        return BaseUtil.getLangMsg(activity.descriptionKey);
    }

    public static GuildActivityType getTypeThrow(@NotNull String type) {
        for (GuildActivityType guildActivityType : values()) {
            if (guildActivityType.typeName.equals(type)) {
                return guildActivityType;
            }
        }
        throw new RuntimeException(BaseUtil.getLangMsg("typeFailureMsg") + type);
    }

    public static List<String> getTypeList() {
        ArrayList<String> typeList = new ArrayList<String>();
        for (GuildActivityType guildActivityType : values()) {
            typeList.add(guildActivityType.typeName);
        }
        return typeList;
    }

    @Generated
    public String getType() {
        return this.typeName;
    }

    public static List<String> gelAllNotScore() {
        ArrayList<String> typeList = new ArrayList<String>();
        for (GuildActivityType guildActivityType : values()) {
            if (!guildActivityType.typeName.equals(GuildActivityType.SCORE_MATCH.typeName)) {
                typeList.add(guildActivityType.typeName);
            }
        }
        return typeList;
    }
}
