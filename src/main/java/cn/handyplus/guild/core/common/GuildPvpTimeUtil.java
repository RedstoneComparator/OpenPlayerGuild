package cn.handyplus.guild.core.common;

import cn.handyplus.guild.lib.CollUtil;
import cn.handyplus.guild.lib.DateUtil;
import cn.handyplus.guild.lib.StrUtil;
import cn.handyplus.guild.util.ConfigUtil;
import java.util.Date;
import java.util.List;

public class GuildPvpTimeUtil {
    public static boolean isTimeOpen(String timeConfigKey, String defaultTimeRange, boolean includeEnd) {
        int currentMinute = GuildPvpTimeUtil.getMinute(DateUtil.format(new Date(), "HH:mm"));
        String timeRange = timeConfigKey;
        int startMinute = GuildPvpTimeUtil.getStartMinute(timeRange, defaultTimeRange);
        int endMinute = GuildPvpTimeUtil.getEndMinute(timeRange, defaultTimeRange);
        if (includeEnd) {
            if (currentMinute >= startMinute && currentMinute <= endMinute) {
                return true;
            }
            return false;
        }
        if (currentMinute >= startMinute && currentMinute < endMinute) {
            return true;
        }
        return false;
    }

    public static boolean isDateOpen(String dateConfigKey) {
        String weekDaysText = ConfigUtil.ACTIVITY_CONFIG.getString(dateConfigKey, "");
        if (StrUtil.isEmpty(weekDaysText)) {
            return true;
        }
        List<Integer> dayOfWeekList = StrUtil.strToIntList(weekDaysText);
        if (CollUtil.isEmpty(dayOfWeekList)) {
            return false;
        }
        return dayOfWeekList.contains(DateUtil.dayOfWeekEnum(new Date()));
    }

    private GuildPvpTimeUtil() {
    }

    public static String[] getTimeArr(String timeConfigKey, String defaultTimeRange) {
        String timeRange = ConfigUtil.ACTIVITY_CONFIG.getString(timeConfigKey, defaultTimeRange);
        if (StrUtil.isEmpty(timeRange) || !timeRange.contains("~")) {
            return defaultTimeRange.split("~");
        }
        String[] parts = timeRange.split("~");
        if (parts.length < 2) {
            return defaultTimeRange.split("~");
        }
        String[] timeArr = new String[2];
        timeArr[0] = parts[0].trim();
        timeArr[1] = parts[1].trim();
        return timeArr;
    }

    public static boolean isTimeOpen(String timeConfigKey, boolean includeEnd) {
        String timeRange = ConfigUtil.ACTIVITY_CONFIG.getString(timeConfigKey);
        if (StrUtil.isEmpty(timeRange)) {
            return true;
        }
        if (!timeRange.contains("~")) {
            return false;
        }
        String[] parts = timeRange.split("~");
        if (parts.length < 2) {
            return false;
        }
        int currentMinute = GuildPvpTimeUtil.getMinute(DateUtil.format(new Date(), "HH:mm"));
        int startMinute = GuildPvpTimeUtil.getMinute(parts[0].trim());
        int endMinute = GuildPvpTimeUtil.getMinute(parts[1].trim());
        if (includeEnd) {
            if (currentMinute >= startMinute && currentMinute <= endMinute) {
                return true;
            }
            return false;
        }
        if (currentMinute >= startMinute && currentMinute < endMinute) {
            return true;
        }
        return false;
    }

    public static boolean isAfterEndTime(String timeConfigKey, String defaultTimeRange) {
        if (GuildPvpTimeUtil.getMinute(DateUtil.format(new Date(), "HH:mm")) >= GuildPvpTimeUtil.getEndMinute(timeConfigKey, defaultTimeRange)) {
            return true;
        }
        return false;
    }

    public static int getMinute(String timeText) {
        if (StrUtil.isEmpty(timeText) || !timeText.contains(":")) {
            return 0;
        }
        String[] parts = timeText.split(":");
        return Integer.parseInt(parts[0]) * 60 + Integer.parseInt(parts[1]);
    }

    public static int getEndMinute(String timeConfigKey, String defaultTimeRange) {
        return GuildPvpTimeUtil.getMinute(GuildPvpTimeUtil.getTimeArr(timeConfigKey, defaultTimeRange)[1]);
    }

    public static int getStartMinute(String timeConfigKey, String defaultTimeRange) {
        return GuildPvpTimeUtil.getMinute(GuildPvpTimeUtil.getTimeArr(timeConfigKey, defaultTimeRange)[0]);
    }
}
