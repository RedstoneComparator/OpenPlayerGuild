package cn.handyplus.guild.core.season;

import java.util.UUID;
import lombok.Generated;

public class SeasonPvpPlayer {
    private String playerName;
    private Integer meGuildId;
    private Integer kill;
    private Boolean isDeath;
    private Integer F;
    private UUID playerUuid;

    @Generated
    public UUID getPlayerUuid() {
        return this.playerUuid;
    }

    @Generated
    public String getPlayerName() {
        return this.playerName;
    }

    @Generated
    public Boolean getIsDeath() {
        return this.isDeath;
    }

    @Generated
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof SeasonPvpPlayer)) {
            return false;
        }
        if (!((SeasonPvpPlayer)(other = (SeasonPvpPlayer)other)).XxXXxx(this)) {
            return false;
        }
        Object thisValue = this.getMeGuildId();
        Object otherValue = ((SeasonPvpPlayer)other).getMeGuildId();
        if (thisValue == null ? otherValue != null : !thisValue.equals(otherValue)) {
            return false;
        }
        thisValue = this.getKill();
        otherValue = ((SeasonPvpPlayer)other).getKill();
        if (thisValue == null ? otherValue != null : !thisValue.equals(otherValue)) {
            return false;
        }
        thisValue = this.getIsDeath();
        otherValue = ((SeasonPvpPlayer)other).getIsDeath();
        if (thisValue == null ? otherValue != null : !thisValue.equals(otherValue)) {
            return false;
        }
        thisValue = this.getSeason();
        otherValue = ((SeasonPvpPlayer)other).getSeason();
        if (thisValue == null ? otherValue != null : !thisValue.equals(otherValue)) {
            return false;
        }
        thisValue = this.getPlayerName();
        otherValue = ((SeasonPvpPlayer)other).getPlayerName();
        if (thisValue == null ? otherValue != null : !thisValue.equals(otherValue)) {
            return false;
        }
        thisValue = this.getPlayerUuid();
        other = ((SeasonPvpPlayer)other).getPlayerUuid();
        if (thisValue == null ? other != null : !thisValue.equals(other)) {
            return false;
        }
        return true;
    }

    @Generated
    public int hashCode() {
        int result = 59;
        result = 1;
        Object value = this.getMeGuildId();
        result = result * 59 + (value == null ? 43 : value.hashCode());
        value = this.getKill();
        result = result * 59 + (value == null ? 43 : value.hashCode());
        value = this.getIsDeath();
        result = result * 59 + (value == null ? 43 : value.hashCode());
        value = this.getSeason();
        result = result * 59 + (value == null ? 43 : value.hashCode());
        value = this.getPlayerName();
        result = result * 59 + (value == null ? 43 : value.hashCode());
        value = this.getPlayerUuid();
        result = result * 59 + (value == null ? 43 : value.hashCode());
        return result;
    }

    @Generated
    public void setMeGuildId(Integer meGuildId) {
        this.meGuildId = meGuildId;
    }

    @Generated
    public Integer getMeGuildId() {
        return this.meGuildId;
    }

    @Generated
    public Integer getSeason() {
        return this.F;
    }

    @Generated
    public void setSeason(Integer season) {
        this.F = season;
    }

    public static String pPPppP(String text) {
        int length = text.length();
        int index = length - 1;
        char[] chars = new char[length];
        int keySecond = 69;
        int cursor = index;
        int keyFirst = 54;
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
    public SeasonPvpPlayer() {
    }

    @Generated
    public String toString() {
        return new StringBuilder().insert(0, "SeasonPvpPlayer(meGuildId=").append(this.getMeGuildId()).append(", playerName=").append(this.getPlayerName()).append(", playerUuid=").append(this.getPlayerUuid()).append(", kill=").append(this.getKill()).append(", isDeath=").append(this.getIsDeath()).append(", season=").append(this.getSeason()).append(")").toString();
    }

    @Generated
    public void setKill(Integer kill) {
        this.kill = kill;
    }

    @Generated
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    @Generated
    public boolean XxXXxx(Object other) {
        return other instanceof SeasonPvpPlayer;
    }

    @Generated
    public void setIsDeath(Boolean isDeath) {
        this.isDeath = isDeath;
    }

    @Generated
    public Integer getKill() {
        return this.kill;
    }

    @Generated
    public void setPlayerUuid(UUID playerUuid) {
        this.playerUuid = playerUuid;
    }
}
