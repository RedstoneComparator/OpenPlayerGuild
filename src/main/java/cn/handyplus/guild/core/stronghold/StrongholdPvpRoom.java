package cn.handyplus.guild.core.stronghold;

import cn.handyplus.guild.constants.GuildPvpRoomStatusEnum;
import cn.handyplus.guild.core.stronghold.StrongholdPvpPlayer;
import cn.handyplus.guild.core.stronghold.StrongholdPvpPoint;
import cn.handyplus.guild.enter.GuildPlayer;
import cn.handyplus.guild.enter.GuildPvpLocation;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.Generated;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StrongholdPvpRoom {
    @NotNull
    private GuildPvpLocation I;
    @NotNull
    private Map<Integer, List<UUID>> preparationPlayerMap;
    @NotNull
    private GuildPvpRoomStatusEnum status;
    @NotNull
    private Integer prepareSecond;
    @NotNull
    private Integer startSecond;
    @NotNull
    private Map<Integer, List<GuildPlayer>> guildPlayerMap;
    @NotNull
    private Map<UUID, StrongholdPvpPlayer> playerMap;
    @NotNull
    private Map<Integer, Location> guildLocationMap;
    @NotNull
    private List<Integer> guildIdList;
    @NotNull
    private List<StrongholdPvpPoint> pointList;
    @NotNull
    private Map<Integer, Integer> scoreMap;
    @NotNull
    private Map<Integer, String> guildNameMap;
    @NotNull
    private Integer locationId;
    @Nullable
    private Date startTime;

    @Generated
    public void setGuildNameMap(@NotNull Map<Integer, String> guildNameMap) {
        if (guildNameMap == null) {
            throw new NullPointerException("guildNameMap is marked non-null but is null");
        }
        this.guildNameMap = guildNameMap;
    }

    @Generated
    public String toString() {
        return new StringBuilder().insert(0, "StrongholdPvpRoom(locationId=").append(this.getLocationId()).append(", guildPvpLocation=").append(this.getGuildPvpLocation()).append(", guildLocationMap=").append(this.getGuildLocationMap()).append(", pointList=").append(this.getPointList()).append(", guildIdList=").append(this.getGuildIdList()).append(", guildNameMap=").append(this.getGuildNameMap()).append(", status=").append((Object)this.getStatus()).append(", prepareSecond=").append(this.getPrepareSecond()).append(", startSecond=").append(this.getStartSecond()).append(", scoreMap=").append(this.getScoreMap()).append(", guildPlayerMap=").append(this.getGuildPlayerMap()).append(", preparationPlayerMap=").append(this.getPreparationPlayerMap()).append(", playerMap=").append(this.getPlayerMap()).append(", startTime=").append(this.getStartTime()).append(")").toString();
    }

    @NotNull
    @Generated
    public List<Integer> getGuildIdList() {
        return this.guildIdList;
    }

    @NotNull
    @Generated
    public List<StrongholdPvpPoint> getPointList() {
        return this.pointList;
    }

    @NotNull
    @Generated
    public Map<Integer, List<UUID>> getPreparationPlayerMap() {
        return this.preparationPlayerMap;
    }

    @Generated
    public void setStatus(@NotNull GuildPvpRoomStatusEnum status) {
        if (status == null) {
            throw new NullPointerException("status is marked non-null but is null");
        }
        this.status = status;
    }

    @NotNull
    @Generated
    public Map<Integer, String> getGuildNameMap() {
        return this.guildNameMap;
    }

    @Generated
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof StrongholdPvpRoom)) {
            return false;
        }
        if (!((StrongholdPvpRoom)(other = (StrongholdPvpRoom)other)).XxXXxx(this)) {
            return false;
        }
        Object thisValue = this.getLocationId();
        Object otherValue = ((StrongholdPvpRoom)other).getLocationId();
        if (thisValue == null ? otherValue != null : !thisValue.equals(otherValue)) {
            return false;
        }
        thisValue = this.getPrepareSecond();
        otherValue = ((StrongholdPvpRoom)other).getPrepareSecond();
        if (thisValue == null ? otherValue != null : !thisValue.equals(otherValue)) {
            return false;
        }
        thisValue = this.getStartSecond();
        otherValue = ((StrongholdPvpRoom)other).getStartSecond();
        if (thisValue == null ? otherValue != null : !thisValue.equals(otherValue)) {
            return false;
        }
        thisValue = this.getGuildPvpLocation();
        otherValue = ((StrongholdPvpRoom)other).getGuildPvpLocation();
        if (thisValue == null ? otherValue != null : !thisValue.equals(otherValue)) {
            return false;
        }
        thisValue = this.getGuildLocationMap();
        otherValue = ((StrongholdPvpRoom)other).getGuildLocationMap();
        if (thisValue == null ? otherValue != null : !thisValue.equals(otherValue)) {
            return false;
        }
        thisValue = this.getPointList();
        otherValue = ((StrongholdPvpRoom)other).getPointList();
        if (thisValue == null ? otherValue != null : !thisValue.equals(otherValue)) {
            return false;
        }
        thisValue = this.getGuildIdList();
        otherValue = ((StrongholdPvpRoom)other).getGuildIdList();
        if (thisValue == null ? otherValue != null : !thisValue.equals(otherValue)) {
            return false;
        }
        thisValue = this.getGuildNameMap();
        otherValue = ((StrongholdPvpRoom)other).getGuildNameMap();
        if (thisValue == null ? otherValue != null : !thisValue.equals(otherValue)) {
            return false;
        }
        thisValue = this.getStatus();
        otherValue = ((StrongholdPvpRoom)other).getStatus();
        if (thisValue == null ? otherValue != null : !thisValue.equals(otherValue)) {
            return false;
        }
        thisValue = this.getScoreMap();
        otherValue = ((StrongholdPvpRoom)other).getScoreMap();
        if (thisValue == null ? otherValue != null : !thisValue.equals(otherValue)) {
            return false;
        }
        thisValue = this.getGuildPlayerMap();
        otherValue = ((StrongholdPvpRoom)other).getGuildPlayerMap();
        if (thisValue == null ? otherValue != null : !thisValue.equals(otherValue)) {
            return false;
        }
        thisValue = this.getPreparationPlayerMap();
        otherValue = ((StrongholdPvpRoom)other).getPreparationPlayerMap();
        if (thisValue == null ? otherValue != null : !thisValue.equals(otherValue)) {
            return false;
        }
        thisValue = this.getPlayerMap();
        otherValue = ((StrongholdPvpRoom)other).getPlayerMap();
        if (thisValue == null ? otherValue != null : !thisValue.equals(otherValue)) {
            return false;
        }
        thisValue = this.getStartTime();
        other = ((StrongholdPvpRoom)other).getStartTime();
        if (thisValue == null ? other != null : !thisValue.equals(other)) {
            return false;
        }
        return true;
    }

    @Generated
    public void setLocationId(@NotNull Integer locationId) {
        if (locationId == null) {
            throw new NullPointerException("locationId is marked non-null but is null");
        }
        this.locationId = locationId;
    }

    @Generated
    public void setGuildPvpLocation(@NotNull GuildPvpLocation guildPvpLocation) {
        if (guildPvpLocation == null) {
            throw new NullPointerException("guildPvpLocation is marked non-null but is null");
        }
        this.I = guildPvpLocation;
    }

    @Generated
    public void setPlayerMap(@NotNull Map<UUID, StrongholdPvpPlayer> playerMap) {
        if (playerMap == null) {
            throw new NullPointerException("playerMap is marked non-null but is null");
        }
        this.playerMap = playerMap;
    }

    @Generated
    public void setScoreMap(@NotNull Map<Integer, Integer> scoreMap) {
        if (scoreMap == null) {
            throw new NullPointerException("scoreMap is marked non-null but is null");
        }
        this.scoreMap = scoreMap;
    }

    @Generated
    public boolean XxXXxx(Object object) {
        return object instanceof StrongholdPvpRoom;
    }

    @Generated
    public void setPointList(@NotNull List<StrongholdPvpPoint> pointList) {
        if (pointList == null) {
            throw new NullPointerException("pointList is marked non-null but is null");
        }
        this.pointList = pointList;
    }

    @Generated
    public void setPrepareSecond(@NotNull Integer prepareSecond) {
        if (prepareSecond == null) {
            throw new NullPointerException("prepareSecond is marked non-null but is null");
        }
        this.prepareSecond = prepareSecond;
    }

    @NotNull
    @Generated
    public GuildPvpRoomStatusEnum getStatus() {
        return this.status;
    }

    @Generated
    public void setPreparationPlayerMap(@NotNull Map<Integer, List<UUID>> preparationPlayerMap) {
        if (preparationPlayerMap == null) {
            throw new NullPointerException("preparationPlayerMap is marked non-null but is null");
        }
        this.preparationPlayerMap = preparationPlayerMap;
    }

    @NotNull
    @Generated
    public Map<Integer, List<GuildPlayer>> getGuildPlayerMap() {
        return this.guildPlayerMap;
    }

    @NotNull
    @Generated
    public Map<Integer, Location> getGuildLocationMap() {
        return this.guildLocationMap;
    }

    @NotNull
    @Generated
    public GuildPvpLocation getGuildPvpLocation() {
        return this.I;
    }

    @Generated
    public void setGuildPlayerMap(@NotNull Map<Integer, List<GuildPlayer>> guildPlayerMap) {
        if (guildPlayerMap == null) {
            throw new NullPointerException("guildPlayerMap is marked non-null but is null");
        }
        this.guildPlayerMap = guildPlayerMap;
    }

    @Nullable
    @Generated
    public Date getStartTime() {
        return this.startTime;
    }

    @NotNull
    @Generated
    public Map<Integer, Integer> getScoreMap() {
        return this.scoreMap;
    }

    @NotNull
    @Generated
    public Integer getLocationId() {
        return this.locationId;
    }

    @Generated
    public StrongholdPvpRoom() {
    }

    @NotNull
    @Generated
    public Map<UUID, StrongholdPvpPlayer> getPlayerMap() {
        return this.playerMap;
    }

    @NotNull
    @Generated
    public Integer getStartSecond() {
        return this.startSecond;
    }

    @Generated
    public void setStartSecond(@NotNull Integer startSecond) {
        if (startSecond == null) {
            throw new NullPointerException("startSecond is marked non-null but is null");
        }
        this.startSecond = startSecond;
    }

    @Generated
    public int hashCode() {
        int result = 59;
        result = 1;
        Object value = this.getLocationId();
        result = result * 59 + (value == null ? 43 : value.hashCode());
        value = this.getPrepareSecond();
        result = result * 59 + (value == null ? 43 : value.hashCode());
        value = this.getStartSecond();
        result = result * 59 + (value == null ? 43 : value.hashCode());
        value = this.getGuildPvpLocation();
        result = result * 59 + (value == null ? 43 : value.hashCode());
        value = this.getGuildLocationMap();
        result = result * 59 + (value == null ? 43 : value.hashCode());
        value = this.getPointList();
        result = result * 59 + (value == null ? 43 : value.hashCode());
        value = this.getGuildIdList();
        result = result * 59 + (value == null ? 43 : value.hashCode());
        value = this.getGuildNameMap();
        result = result * 59 + (value == null ? 43 : value.hashCode());
        value = this.getStatus();
        result = result * 59 + (value == null ? 43 : value.hashCode());
        value = this.getScoreMap();
        result = result * 59 + (value == null ? 43 : value.hashCode());
        value = this.getGuildPlayerMap();
        result = result * 59 + (value == null ? 43 : value.hashCode());
        value = this.getPreparationPlayerMap();
        result = result * 59 + (value == null ? 43 : value.hashCode());
        value = this.getPlayerMap();
        result = result * 59 + (value == null ? 43 : value.hashCode());
        value = this.getStartTime();
        result = result * 59 + (value == null ? 43 : value.hashCode());
        return result;
    }

    @Generated
    public void setGuildLocationMap(@NotNull Map<Integer, Location> guildLocationMap) {
        if (guildLocationMap == null) {
            throw new NullPointerException("guildLocationMap is marked non-null but is null");
        }
        this.guildLocationMap = guildLocationMap;
    }

    @Generated
    public void setGuildIdList(@NotNull List<Integer> guildIdList) {
        if (guildIdList == null) {
            throw new NullPointerException("guildIdList is marked non-null but is null");
        }
        this.guildIdList = guildIdList;
    }

    @Generated
    public void setStartTime(@Nullable Date startTime) {
        this.startTime = startTime;
    }

    @NotNull
    @Generated
    public Integer getPrepareSecond() {
        return this.prepareSecond;
    }
}
