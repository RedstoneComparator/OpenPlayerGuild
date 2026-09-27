package cn.handyplus.guild.core.common;

import java.util.List;
import java.util.UUID;
import lombok.Generated;

public class GuildPvpFinishResult {
    private final List<UUID> uuidList;
    private final String winnerName;
    private final String failedGuildName;

    @Generated
    public GuildPvpFinishResult(String winName, String failName, List<UUID> playerUuids) {
        this.winnerName = winName;
        this.failedGuildName = failName;
        this.uuidList = playerUuids;
    }

    @Generated
    public String getWinGuildName() {
        return this.winnerName;
    }

    @Generated
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof GuildPvpFinishResult)) {
            return false;
        }
        if (!((GuildPvpFinishResult)((Object)(other = (GuildPvpFinishResult)((Object)other)))).XxXXxx(this)) {
            return false;
        }
        Object thisValue = this.getWinGuildName();
        String otherValue = ((GuildPvpFinishResult)((Object)other)).getWinGuildName();
        if (thisValue == null ? otherValue != null : !thisValue.equals(otherValue)) {
            return false;
        }
        thisValue = this.getFailGuildName();
        otherValue = ((GuildPvpFinishResult)((Object)other)).getFailGuildName();
        if (thisValue == null ? otherValue != null : !thisValue.equals(otherValue)) {
            return false;
        }
        thisValue = this.getPlayerUuidList();
        other = ((GuildPvpFinishResult)((Object)other)).getPlayerUuidList();
        if (thisValue == null ? other != null : !thisValue.equals(other)) {
            return false;
        }
        return true;
    }

    @Generated
    public List<UUID> getPlayerUuidList() {
        return this.uuidList;
    }

    @Generated
    public String getFailGuildName() {
        return this.failedGuildName;
    }

    public static String pPPppP(String value) {
        int n2 = value.length();
        int n3 = n2 - 1;
        char[] cArray = new char[n2];
        int n4 = 31;
        int n5 = n3;
        int n6 = 82;
        while (n5 >= 0) {
            int n7 = n3--;
            cArray[n7] = (char)(value.charAt(n7) ^ n6);
            if (n3 < 0) break;
            int n8 = n3--;
            cArray[n8] = (char)(value.charAt(n8) ^ n4);
            n5 = n3;
        }
        return new String(cArray);
    }

    @Generated
    public String toString() {
        return new StringBuilder().insert(0, "GuildPvpFinishResult(winGuildName=").append(this.getWinGuildName()).append(", failGuildName=").append(this.getFailGuildName()).append(", playerUuidList=").append(this.getPlayerUuidList()).append(")").toString();
    }

    @Generated
    public int hashCode() {
        int hash = 59;
        hash = 1;
        Object value = this.getWinGuildName();
        hash = hash * 59 + (value == null ? 43 : value.hashCode());
        value = this.getFailGuildName();
        hash = hash * 59 + (value == null ? 43 : value.hashCode());
        value = this.getPlayerUuidList();
        hash = hash * 59 + (value == null ? 43 : value.hashCode());
        return hash;
    }

    @Generated
    public boolean XxXXxx(Object other) {
        return other instanceof GuildPvpFinishResult;
    }
}
