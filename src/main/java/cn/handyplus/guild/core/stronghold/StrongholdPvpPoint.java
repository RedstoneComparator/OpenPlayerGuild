package cn.handyplus.guild.core.stronghold;

import java.util.HashMap;
import java.util.Map;
import lombok.Generated;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StrongholdPvpPoint {
    @NotNull
    private Location hologramLocation;
    @NotNull
    private Map<Integer, Integer> scoreMap;
    @Nullable
    private Integer ownerGuildId;
    @NotNull
    private Location location;
    @NotNull
    private Map<Integer, Integer> occupySecondMap;
    @NotNull
    private String name;

    @Generated
    public String toString() {
        return new StringBuilder().insert(0, "StrongholdPvpPoint(name=").append(this.getName()).append(", location=").append(this.getLocation()).append(", hologramLocation=").append(this.getHologramLocation()).append(", ownerGuildId=").append(this.getOwnerGuildId()).append(", occupySecondMap=").append(this.getOccupySecondMap()).append(", scoreMap=").append(this.getScoreMap()).append(")").toString();
    }

    @Generated
    public void setOwnerGuildId(@Nullable Integer ownerGuildId) {
        this.ownerGuildId = ownerGuildId;
    }

    @NotNull
    @Generated
    public String getName() {
        return this.name;
    }

    @NotNull
    @Generated
    public Map<Integer, Integer> getScoreMap() {
        return this.scoreMap;
    }

    @Generated
    public void setHologramLocation(@NotNull Location hologramLocation) {
        if (hologramLocation == null) {
            throw new NullPointerException("hologramLocation is marked non-null but is null");
        }
        this.hologramLocation = hologramLocation;
    }

    @Generated
    public StrongholdPvpPoint() {
        StrongholdPvpPoint instance = this;
        this.occupySecondMap = new HashMap<Integer, Integer>();
        StrongholdPvpPoint instance2 = this;
        instance.scoreMap = new HashMap<Integer, Integer>();
    }

    @NotNull
    @Generated
    public Location getLocation() {
        return this.location;
    }

    @NotNull
    @Generated
    public Location getHologramLocation() {
        return this.hologramLocation;
    }

    @Generated
    public int hashCode() {
        int result = 59;
        result = 1;
        Object value = this.getOwnerGuildId();
        result = result * 59 + (value == null ? 43 : value.hashCode());
        value = this.getName();
        result = result * 59 + (value == null ? 43 : ((Object)value).hashCode());
        value = this.getLocation();
        result = result * 59 + (value == null ? 43 : ((Object)value).hashCode());
        value = this.getHologramLocation();
        result = result * 59 + (value == null ? 43 : ((Object)value).hashCode());
        value = this.getOccupySecondMap();
        result = result * 59 + (value == null ? 43 : ((Object)value).hashCode());
        value = this.getScoreMap();
        result = result * 59 + (value == null ? 43 : ((Object)value).hashCode());
        return result;
    }

    @NotNull
    @Generated
    public Map<Integer, Integer> getOccupySecondMap() {
        return this.occupySecondMap;
    }

    @Generated
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof StrongholdPvpPoint)) {
            return false;
        }
        if (!((StrongholdPvpPoint)((Object)(other = (StrongholdPvpPoint)((Object)other)))).XxXXxx(this)) {
            return false;
        }
        Object thisValue = this.getOwnerGuildId();
        Object otherValue = ((StrongholdPvpPoint)((Object)other)).getOwnerGuildId();
        if (thisValue == null ? otherValue != null : !((Object)thisValue).equals(otherValue)) {
            return false;
        }
        thisValue = this.getName();
        otherValue = ((StrongholdPvpPoint)((Object)other)).getName();
        if (thisValue == null ? otherValue != null : !((Object)thisValue).equals(otherValue)) {
            return false;
        }
        thisValue = this.getLocation();
        otherValue = ((StrongholdPvpPoint)((Object)other)).getLocation();
        if (thisValue == null ? otherValue != null : !((Object)thisValue).equals(otherValue)) {
            return false;
        }
        thisValue = this.getHologramLocation();
        otherValue = ((StrongholdPvpPoint)((Object)other)).getHologramLocation();
        if (thisValue == null ? otherValue != null : !((Object)thisValue).equals(otherValue)) {
            return false;
        }
        thisValue = this.getOccupySecondMap();
        otherValue = ((StrongholdPvpPoint)((Object)other)).getOccupySecondMap();
        if (thisValue == null ? otherValue != null : !((Object)thisValue).equals(otherValue)) {
            return false;
        }
        thisValue = this.getScoreMap();
        other = ((StrongholdPvpPoint)((Object)other)).getScoreMap();
        if (thisValue == null ? other != null : !((Object)thisValue).equals(other)) {
            return false;
        }
        return true;
    }

    @Generated
    public void setOccupySecondMap(@NotNull Map<Integer, Integer> occupySecondMap) {
        if (occupySecondMap == null) {
            throw new NullPointerException("occupySecondMap is marked non-null but is null");
        }
        this.occupySecondMap = occupySecondMap;
    }

    @Generated
    public void setName(@NotNull String name) {
        if (name == null) {
            throw new NullPointerException("name is marked non-null but is null");
        }
        this.name = name;
    }

    @Generated
    public void setLocation(@NotNull Location location) {
        if (location == null) {
            throw new NullPointerException("location is marked non-null but is null");
        }
        this.location = location;
    }

    @Generated
    public void setScoreMap(@NotNull Map<Integer, Integer> scoreMap) {
        if (scoreMap == null) {
            throw new NullPointerException("scoreMap is marked non-null but is null");
        }
        this.scoreMap = scoreMap;
    }

    @Nullable
    @Generated
    public Integer getOwnerGuildId() {
        return this.ownerGuildId;
    }

    @Generated
    public boolean XxXXxx(Object object) {
        return object instanceof StrongholdPvpPoint;
    }
}
