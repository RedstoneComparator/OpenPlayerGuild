package cn.handyplus.guild.constants;

import cn.handyplus.guild.constants.GuildRoleEnum;
import cn.handyplus.guild.enter.GuildPlayer;
import cn.handyplus.guild.hook.PlaceholderApiUtil;
import cn.handyplus.guild.lib.BaseConstants;
import cn.handyplus.guild.lib.BaseUtil;
import cn.handyplus.guild.lib.ItemStackUtil;
import cn.handyplus.guild.lib.LegacyUtil;
import cn.handyplus.guild.lib.SignService;
import cn.handyplus.guild.lib.StrUtil;
import cn.handyplus.guild.service.GuildInfoService;
import cn.handyplus.guild.util.GuildCacheUtil;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import lombok.Generated;
import org.bukkit.OfflinePlayer;

public enum PapiHandleEnum {
    NAME(LegacyUtil.J("\r)\u000e-"), "%playerGuild_name%"),
    STRIP_COLOR_NAME("strip_color_name",
            LegacyUtil.J("F8\u000f)\u001a-\u0011\u000f\u0016!\u000f,<;\u0017:\n8<+\f$\f:<&\u0002%\u0006m")),
    SHOW_NAME(LegacyUtil.J(";\u000b'\u0014\u0017\r)\u000e-"),
            "%playerGuild_show_name%"),
    MONEY("money", "%playerGuild_money%"),
    WEEK_MONEY("week_money",
            LegacyUtil.J("F8\u000f)\u001a-\u0011\u000f\u0016!\u000f,<?\u0006-\b\u0017\u000e'\r-\u001am")),
    TOTAL_MONEY(LegacyUtil.J("<\f<\u0002$<%\f&\u00061"),
            "%playerGuild_total_money%"),
    ROLE("role", LegacyUtil.J("F8\u000f)\u001a-\u0011\u000f\u0016!\u000f,<:\f$\u0006m")),
    STRIP_COLOR_ROLE(LegacyUtil.J("\u0010<\u0011!\u0013\u0017\u0000'\u000f'\u0011\u0017\u0011'\u000f-"),
            "%playerGuild_strip_color_role%"),
    GUILD_COUNT("guild_count",
            LegacyUtil.J("m\u0013$\u00021\u0006:$=\n$\u0007\u0017\u0004=\n$\u0007\u0017\u0000'\u0016&\u0017m")),
    KILL(LegacyUtil.J("\b!\u000f$"), "%playerGuild_kill%"),
    DIE("die", LegacyUtil.J("m\u0013$\u00021\u0006:$=\n$\u0007\u0017\u0007!\u0006m")),
    GUILD_SEASON_RANK(LegacyUtil.J("/\u0016!\u000f,<;\u0006)\u0010'\r\u0017\u0011)\r#"),
            "%playerGuild_guild_season_rank%"),
    GUILD_LOGO("guild_logo", LegacyUtil.J("F/\u0016!\u000f,<$\f/\fm")),
    GUILD_LEVEL(LegacyUtil.J("/\u0016!\u000f,<$\u0006>\u0006$"),
            "%guild_level%"),
    GUILD_MEMBER_COUNT("guild_member_count",
            LegacyUtil.J("F/\u0016!\u000f,<%\u0006%\u0001-\u0011\u0017\u0000'\u0016&\u0017m")),
    GUILD_MEMBER_MAX_COUNT(LegacyUtil.J("\u0004=\n$\u0007\u0017\u000e-\u000e*\u0006:<%\u00020<+\f=\r<"),
            "%playerGuild_guild_member_max_count%"),
    GUILD_MONEY("guild_money", LegacyUtil.J("m\u0004=\n$\u0007\u0017\u000e'\r-\u001am")),
    GUILD_PROSPERITY_DEGREE(LegacyUtil.J("/\u0016!\u000f,<8\u0011'\u00108\u0006:\n<\u001a\u0017\u0007-\u0004:\u0006-"),
            "%playerGuild_guild_prosperity_degree%"),
    GUILD_MONTH_PROSPERITY_DEGREE(
            "guild_month_prosperity_degree",
            LegacyUtil.J("m\u0013$\u00021\u0006:$=\n$\u0007\u0017\u0004=\n$\u0007\u0017\u000e'\r<\u000b\u0017\u0013:\f;\u0013-\u0011!\u00171<,\u0006/\u0011-\u0006m")),
    GUILD_ORE(LegacyUtil.J("/\u0016!\u000f,<'\u0011-"),
            "%playerGuild_guild_ore%");

    private final String placeholderName;
    private final String M;
    private final static Map<String, PapiHandleEnum> F = new HashMap<String, PapiHandleEnum>();

    static {
        for (PapiHandleEnum papiHandleEnum : values()) {
            F.put(papiHandleEnum.placeholderName.toLowerCase(Locale.ROOT), papiHandleEnum);
        }
    }

    @Generated
    PapiHandleEnum(String placeholder, String desc) {
        this.placeholderName = placeholder;
        this.M = desc;
    }

    public static String execution(OfflinePlayer offlinePlayer, String placeholder) {
        if (!SignService.getInstance().isSuccess()) {
            return null;
        }
        String emptyResult = "";
        PapiHandleEnum papiHandleEnum = PapiHandleEnum.findByPlaceholder(placeholder);
        if (papiHandleEnum == null) {
            return emptyResult;
        }
        GuildPlayer guildPlayer = GuildCacheUtil.getGuildPlayer(offlinePlayer.getUniqueId());
        boolean hasGuild = guildPlayer.getId() != null;
        switch (papiHandleEnum) {
            case NAME: {
                if (!hasGuild) {
                    return LegacyUtil.parseColor(BaseConstants.CONFIG.getString("default"));
                }
                String nameColor = BaseConstants.CONFIG.getString(
                        LegacyUtil.J("/\u0016!\u000f,0-\u0017<\n&\u0004f") + guildPlayer.getLevel()
                                + ".nameColor", "");
                return LegacyUtil.parseColor(nameColor + guildPlayer.getGuildInfoName());
            }
            case STRIP_COLOR_NAME: {
                String rawName = hasGuild ? guildPlayer.getGuildInfoName()
                        : BaseConstants.CONFIG.getString("default");
                return BaseUtil.stripColor(rawName);
            }
            case SHOW_NAME: {
                String prefix = BaseConstants.CONFIG.getString(
                        LegacyUtil.J("\u0013:\u0006.\n0!:\u0002+\b-\u0017;"), "");
                String suffix = BaseConstants.CONFIG.getString(
                        "suffixBrackets", "");
                prefix = PlaceholderApiUtil.set(offlinePlayer, prefix);
                suffix = PlaceholderApiUtil.set(offlinePlayer, suffix);
                if (!hasGuild) {
                    return LegacyUtil.parseColor(BaseConstants.CONFIG.getString("default"));
                }
                String nameColor = BaseConstants.CONFIG.getString(
                        LegacyUtil.J("/\u0016!\u000f,0-\u0017<\n&\u0004f") + guildPlayer.getLevel()
                                + ".nameColor", "");
                return LegacyUtil.parseColor(prefix + nameColor + guildPlayer.getGuildInfoName() + suffix);
            }
            case MONEY: {
                return LegacyUtil.parseColor(hasGuild ? guildPlayer.getMoney().toString() : LegacyUtil.J("x"));
            }
            case WEEK_MONEY: {
                return LegacyUtil.parseColor(hasGuild ? guildPlayer.getWeekMoney().toString() : "0");
            }
            case TOTAL_MONEY: {
                return LegacyUtil.parseColor(hasGuild ? guildPlayer.getTotalMoney().toString() : LegacyUtil.J("x"));
            }
            case ROLE: {
                return LegacyUtil.parseColor(hasGuild ? GuildRoleEnum.getRoleName(guildPlayer.getRoleId())
                        : BaseUtil.getLangMsg("role.not", ""));
            }
            case STRIP_COLOR_ROLE: {
                String rawRole = hasGuild ? GuildRoleEnum.getRoleName(guildPlayer.getRoleId())
                        : BaseUtil.getLangMsg(LegacyUtil.J("\u0011'\u000f-M&\f<"), "");
                return LegacyUtil.parseColor(BaseUtil.stripColor(rawRole));
            }
            case GUILD_COUNT: {
                return LegacyUtil.parseColor(GuildInfoService.getInstance().findCount().toString());
            }
            case KILL: {
                return LegacyUtil.parseColor(hasGuild ? guildPlayer.getKill().toString() : "0");
            }
            case DIE: {
                return LegacyUtil.parseColor(hasGuild ? guildPlayer.getDie().toString() : LegacyUtil.J("x"));
            }
            case GUILD_SEASON_RANK: {
                return LegacyUtil.parseColor(hasGuild ? guildPlayer.getSeasonRank().toString() : "0");
            }
            case GUILD_LOGO: {
                return LegacyUtil.parseColor(hasGuild && StrUtil.isNotEmpty(guildPlayer.getLogoName())
                        ? ItemStackUtil.itemStackDeserialize(guildPlayer.getLogoName()).getType().name() : "");
            }
            case GUILD_LEVEL: {
                return LegacyUtil.parseColor(hasGuild ? guildPlayer.getLevel().toString() : LegacyUtil.J("x"));
            }
            case GUILD_MEMBER_COUNT: {
                return LegacyUtil.parseColor(hasGuild ? guildPlayer.getMemberCount().toString() : "0");
            }
            case GUILD_MEMBER_MAX_COUNT: {
                return LegacyUtil.parseColor(hasGuild ? guildPlayer.getMemberMaxCount().toString() : LegacyUtil.J("x"));
            }
            case GUILD_MONEY: {
                return LegacyUtil.parseColor(hasGuild ? guildPlayer.getGuildInfoMoney().toString() : "0");
            }
            case GUILD_PROSPERITY_DEGREE: {
                return LegacyUtil.parseColor(hasGuild ? guildPlayer.getProsperityDegree().toString() : LegacyUtil.J("x"));
            }
            case GUILD_MONTH_PROSPERITY_DEGREE: {
                return LegacyUtil.parseColor(hasGuild ? guildPlayer.getMonthProsperityDegree().toString() : "0");
            }
            case GUILD_ORE: {
                return LegacyUtil.parseColor(hasGuild ? guildPlayer.getGuildInfoOre().toString() : LegacyUtil.J("x"));
            }
        }
        return LegacyUtil.parseColor(emptyResult);
    }

    private static PapiHandleEnum findByPlaceholder(String placeholder) {
        if (placeholder == null) {
            return null;
        }
        return F.get(placeholder.toLowerCase(Locale.ROOT));
    }

    @Generated
    public String getPlaceholder() {
        return this.placeholderName;
    }

    @Generated
    public String getDesc() {
        return this.M;
    }
}
