package cn.handyplus.guild.constants;

import cn.handyplus.guild.lib.BaseUtil;
import cn.handyplus.guild.lib.LegacyUtil;
import java.util.ArrayList;
import java.util.List;
import lombok.Generated;

public enum GuildSortEnum {
    PROSPERITY_DEGREE(Integer.valueOf(1), LegacyUtil.J("8\u0011'\u00108\u0006:\n<\u001a\u0017\u0007-\u0004:\u0006-"),
            "guildSort.prosperityDegree"),
    LEVEL(Integer.valueOf(2), "level",
            LegacyUtil.J("/\u0016!\u000f,0'\u0011<M$\u0006>\u0006$")),
    MONEY(Integer.valueOf(3), "money", LegacyUtil.J("/\u0016!\u000f,0'\u0011<M%\f&\u00061")),
    CREATE_TIME(Integer.valueOf(4), LegacyUtil.J("+\u0011-\u0002<\u0006\u0017\u0017!\u000e-"),
            "guildSort.createTime"),
    MEMBER_COUNT(Integer.valueOf(5), "member_count",
            LegacyUtil.J("/\u0016!\u000f,0'\u0011<M%\u0006%\u0001-\u0011\u000b\f=\r<")),
    MONTH_PROSPERITY_DEGREE(Integer.valueOf(6), LegacyUtil.J("%\f&\u0017 <8\u0011'\u00108\u0006:\n<\u001a\u0017\u0007-\u0004:\u0006-"),
            "guildSort.monthProsperityDegree");

    private final String descriptionKey;
    private final String M;
    private final Integer sortId;

    @Generated
    GuildSortEnum(Integer id, String sortName, String langKey) {
        this.sortId = id;
        this.M = sortName;
        this.descriptionKey = langKey;
    }

    public static String getTypeName(GuildSortEnum sort) {
        return BaseUtil.getLangMsg(sort.descriptionKey);
    }

    @Generated
    public Integer getId() {
        return this.sortId;
    }

    public static GuildSortEnum getNextEnum(String sortName) {
        GuildSortEnum guildSortEnum = GuildSortEnum.getEnum(sortName);
        if (guildSortEnum == null) {
            return PROSPERITY_DEGREE;
        }
        int nextId = guildSortEnum.getId() + 1;
        if (nextId > 6) {
            nextId = 1;
        }
        for (GuildSortEnum candidate : values()) {
            if (candidate.getId() == nextId) {
                return candidate;
            }
        }
        return PROSPERITY_DEGREE;
    }

    @Generated
    public String getName() {
        return this.M;
    }

    public static GuildSortEnum getEnum(String sortName) {
        for (GuildSortEnum guildSortEnum : values()) {
            if (guildSortEnum.getName().equalsIgnoreCase(sortName)) {
                return guildSortEnum;
            }
        }
        return PROSPERITY_DEGREE;
    }

    public static List<String> getSortList() {
        ArrayList<String> sortNames = new ArrayList<String>();
        for (GuildSortEnum guildSortEnum : values()) {
            sortNames.add(guildSortEnum.getName());
        }
        return sortNames;
    }
}
