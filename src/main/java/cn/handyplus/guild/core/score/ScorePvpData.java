package cn.handyplus.guild.core.score;

import lombok.Generated;

public class ScorePvpData {
    private Integer failCount;
    private Boolean joined;
    private Integer e;
    private Integer matchCount;
    private Integer guildId;
    private String guildName;
    private Integer winCount;

    @Generated
    public void setFailCount(Integer failCount) {
        this.failCount = failCount;
    }

    @Generated
    public String getGuildName() {
        return this.guildName;
    }

    @Generated
    public Integer getFailCount() {
        return this.failCount;
    }

    @Generated
    public Integer getWinCount() {
        return this.winCount;
    }

    @Generated
    public void setScore(Integer score) {
        this.e = score;
    }

    @Generated
    public void setWinCount(Integer winCount) {
        this.winCount = winCount;
    }

    @Generated
    public Integer getMatchCount() {
        return this.matchCount;
    }

    @Generated
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof ScorePvpData)) {
            return false;
        }
        if (!((ScorePvpData)(other = (ScorePvpData)other)).XxXXxx(this)) {
            return false;
        }
        Object thisValue = this.getGuildId();
        Object otherValue = ((ScorePvpData)other).getGuildId();
        if (thisValue == null ? otherValue != null : !thisValue.equals(otherValue)) {
            return false;
        }
        thisValue = this.getScore();
        otherValue = ((ScorePvpData)other).getScore();
        if (thisValue == null ? otherValue != null : !thisValue.equals(otherValue)) {
            return false;
        }
        thisValue = this.getJoined();
        otherValue = ((ScorePvpData)other).getJoined();
        if (thisValue == null ? otherValue != null : !thisValue.equals(otherValue)) {
            return false;
        }
        thisValue = this.getMatchCount();
        otherValue = ((ScorePvpData)other).getMatchCount();
        if (thisValue == null ? otherValue != null : !thisValue.equals(otherValue)) {
            return false;
        }
        thisValue = this.getWinCount();
        otherValue = ((ScorePvpData)other).getWinCount();
        if (thisValue == null ? otherValue != null : !thisValue.equals(otherValue)) {
            return false;
        }
        thisValue = this.getFailCount();
        otherValue = ((ScorePvpData)other).getFailCount();
        if (thisValue == null ? otherValue != null : !thisValue.equals(otherValue)) {
            return false;
        }
        thisValue = this.getGuildName();
        other = ((ScorePvpData)other).getGuildName();
        if (thisValue == null ? other != null : !thisValue.equals(other)) {
            return false;
        }
        return true;
    }

    @Generated
    public int hashCode() {
        int result = 59;
        result = 1;
        Object value = this.getGuildId();
        result = result * 59 + (value == null ? 43 : value.hashCode());
        value = this.getScore();
        result = result * 59 + (value == null ? 43 : value.hashCode());
        value = this.getJoined();
        result = result * 59 + (value == null ? 43 : value.hashCode());
        value = this.getMatchCount();
        result = result * 59 + (value == null ? 43 : value.hashCode());
        value = this.getWinCount();
        result = result * 59 + (value == null ? 43 : value.hashCode());
        value = this.getFailCount();
        result = result * 59 + (value == null ? 43 : value.hashCode());
        value = this.getGuildName();
        result = result * 59 + (value == null ? 43 : value.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return new StringBuilder().insert(0, "ScorePvpData(guildId=").append(this.getGuildId()).append(", guildName=").append(this.getGuildName()).append(", score=").append(this.getScore()).append(", joined=").append(this.getJoined()).append(", matchCount=").append(this.getMatchCount()).append(", winCount=").append(this.getWinCount()).append(", failCount=").append(this.getFailCount()).append(")").toString();
    }

    @Generated
    public Boolean getJoined() {
        return this.joined;
    }

    @Generated
    public void setMatchCount(Integer matchCount) {
        this.matchCount = matchCount;
    }

    @Generated
    public void setGuildId(Integer guildId) {
        this.guildId = guildId;
    }

    @Generated
    public Integer getGuildId() {
        return this.guildId;
    }

    public static String pPPppP(String text) {
        int length = text.length();
        int index = length - 1;
        char[] chars = new char[length];
        int keySecond = 102;
        int cursor = index;
        int keyFirst = 37;
        while (cursor >= 0) {
            int charIndex = index--;
            chars[charIndex] = (char)(text.charAt(charIndex) ^ keyFirst);
            if (index < 0) break;
            int nextCharIndex = index--;
            chars[nextCharIndex] = (char)(text.charAt(nextCharIndex) ^ keySecond);
            cursor = index;
        }
        return new String(chars);
    }

    @Generated
    public boolean XxXXxx(Object other) {
        return other instanceof ScorePvpData;
    }

    @Generated
    public ScorePvpData() {
    }

    @Generated
    public void setJoined(Boolean joined) {
        this.joined = joined;
    }

    @Generated
    public void setGuildName(String guildName) {
        this.guildName = guildName;
    }

    @Generated
    public Integer getScore() {
        return this.e;
    }
}
