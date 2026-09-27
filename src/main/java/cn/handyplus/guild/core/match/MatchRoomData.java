package cn.handyplus.guild.core.match;

import cn.handyplus.guild.enter.GuildPvpLocation;
import lombok.Generated;
import org.bukkit.Location;

public class MatchRoomData {
    private Integer locationId;
    private Location lookLocation;
    private Location firstLocation;
    private Location secondLocation;
    private GuildPvpLocation guildPvpLocation;

    @Generated
    public Location getLookLocation() {
        return this.lookLocation;
    }

    @Generated
    public String toString() {
        return new StringBuilder().insert(0, "MatchRoomData(locationId=").append(this.getLocationId()).append(", guildPvpLocation=").append(this.getGuildPvpLocation()).append(", firstLocation=").append(this.getFirstLocation()).append(", secondLocation=").append(this.getSecondLocation()).append(", lookLocation=").append(this.getLookLocation()).append(")").toString();
    }

    @Generated
    public MatchRoomData() {
    }

    @Generated
    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    @Generated
    public Location getFirstLocation() {
        return this.firstLocation;
    }

    @Generated
    public void setLookLocation(Location lookLocation) {
        this.lookLocation = lookLocation;
    }

    @Generated
    public void setGuildPvpLocation(GuildPvpLocation guildPvpLocation) {
        this.guildPvpLocation = guildPvpLocation;
    }

    @Generated
    public void setSecondLocation(Location secondLocation) {
        this.secondLocation = secondLocation;
    }

    @Generated
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof MatchRoomData)) {
            return false;
        }
        if (!((MatchRoomData)(other = (MatchRoomData)other)).XxXXxx(this)) {
            return false;
        }
        Object thisValue = this.getLocationId();
        Object otherValue = ((MatchRoomData)other).getLocationId();
        if (thisValue == null ? otherValue != null : !thisValue.equals(otherValue)) {
            return false;
        }
        thisValue = this.getGuildPvpLocation();
        otherValue = ((MatchRoomData)other).getGuildPvpLocation();
        if (thisValue == null ? otherValue != null : !thisValue.equals(otherValue)) {
            return false;
        }
        thisValue = this.getFirstLocation();
        otherValue = ((MatchRoomData)other).getFirstLocation();
        if (thisValue == null ? otherValue != null : !thisValue.equals(otherValue)) {
            return false;
        }
        thisValue = this.getSecondLocation();
        otherValue = ((MatchRoomData)other).getSecondLocation();
        if (thisValue == null ? otherValue != null : !thisValue.equals(otherValue)) {
            return false;
        }
        thisValue = this.getLookLocation();
        other = ((MatchRoomData)other).getLookLocation();
        if (thisValue == null ? other != null : !thisValue.equals(other)) {
            return false;
        }
        return true;
    }

    @Generated
    public GuildPvpLocation getGuildPvpLocation() {
        return this.guildPvpLocation;
    }

    @Generated
    public int hashCode() {
        int result = 59;
        result = 1;
        Object value = this.getLocationId();
        result = result * 59 + (value == null ? 43 : value.hashCode());
        value = this.getGuildPvpLocation();
        result = result * 59 + (value == null ? 43 : value.hashCode());
        value = this.getFirstLocation();
        result = result * 59 + (value == null ? 43 : value.hashCode());
        value = this.getSecondLocation();
        result = result * 59 + (value == null ? 43 : value.hashCode());
        value = this.getLookLocation();
        result = result * 59 + (value == null ? 43 : value.hashCode());
        return result;
    }

    @Generated
    public void setFirstLocation(Location firstLocation) {
        this.firstLocation = firstLocation;
    }

    @Generated
    public Location getSecondLocation() {
        return this.secondLocation;
    }

    @Generated
    public boolean XxXXxx(Object other) {
        return other instanceof MatchRoomData;
    }

    @Generated
    public Integer getLocationId() {
        return this.locationId;
    }
}
