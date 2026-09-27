package cn.handyplus.guild.core.season;

import cn.handyplus.guild.constants.GuildPvpRoomStatusEnum;
import cn.handyplus.guild.core.season.SeasonPvpPlayer;
import cn.handyplus.guild.enter.GuildPvpLocation;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.Generated;
import org.bukkit.Location;

public class SeasonPvpRoom {
    private List<Integer> guildIdList;
    private Date startTime;
    private Integer g;
    private Location lookLocation;
    private Map<UUID, Location> originLocationMap;
    private Map<UUID, Integer> startGuildPlayerMap;
    private Map<Integer, List<UUID>> preparationPlayerMap;
    private GuildPvpRoomStatusEnum status;
    private Map<Integer, List<SeasonPvpPlayer>> startPlayerMap;
    private GuildPvpLocation guildPvpLocation;

    @Generated
    public Map<UUID, Location> getOriginLocationMap() {
        return this.originLocationMap;
    }

    @Generated
    public Map<Integer, List<SeasonPvpPlayer>> getStartPlayerMap() {
        return this.startPlayerMap;
    }

    @Generated
    public void setStartGuildPlayerMap(Map<UUID, Integer> startGuildPlayerMap) {
        this.startGuildPlayerMap = startGuildPlayerMap;
    }

    @Generated
    public void setSeason(Integer season) {
        this.g = season;
    }

    @Generated
    public GuildPvpLocation getGuildPvpLocation() {
        return this.guildPvpLocation;
    }

    @Generated
    public Integer getSeason() {
        return this.g;
    }

    @Generated
    public GuildPvpRoomStatusEnum getStatus() {
        return this.status;
    }

    @Generated
    public int hashCode() {
        int result = 59;
        result = 1;
        Object value = this.getSeason();
        result = result * 59 + (value == null ? 43 : value.hashCode());
        value = this.getGuildIdList();
        result = result * 59 + (value == null ? 43 : value.hashCode());
        value = this.getStatus();
        result = result * 59 + (value == null ? 43 : value.hashCode());
        value = this.getGuildPvpLocation();
        result = result * 59 + (value == null ? 43 : value.hashCode());
        value = this.getPreparationPlayerMap();
        result = result * 59 + (value == null ? 43 : value.hashCode());
        value = this.getStartPlayerMap();
        result = result * 59 + (value == null ? 43 : value.hashCode());
        value = this.getStartGuildPlayerMap();
        result = result * 59 + (value == null ? 43 : value.hashCode());
        value = this.getOriginLocationMap();
        result = result * 59 + (value == null ? 43 : value.hashCode());
        value = this.getLookLocation();
        result = result * 59 + (value == null ? 43 : value.hashCode());
        value = this.getStartTime();
        result = result * 59 + (value == null ? 43 : value.hashCode());
        return result;
    }

    @Generated
    public void setOriginLocationMap(Map<UUID, Location> originLocationMap) {
        this.originLocationMap = originLocationMap;
    }

    @Generated
    public void setPreparationPlayerMap(Map<Integer, List<UUID>> preparationPlayerMap) {
        this.preparationPlayerMap = preparationPlayerMap;
    }

    @Generated
    public String toString() {
        return new StringBuilder().insert(0, "SeasonPvpRoom(season=").append(this.getSeason()).append(", guildIdList=").append(this.getGuildIdList()).append(", status=").append((Object)this.getStatus()).append(", guildPvpLocation=").append(this.getGuildPvpLocation()).append(", preparationPlayerMap=").append(this.getPreparationPlayerMap()).append(", startPlayerMap=").append(this.getStartPlayerMap()).append(", startGuildPlayerMap=").append(this.getStartGuildPlayerMap()).append(", originLocationMap=").append(this.getOriginLocationMap()).append(", lookLocation=").append(this.getLookLocation()).append(", startTime=").append(this.getStartTime()).append(")").toString();
    }

    @Generated
    public Date getStartTime() {
        return this.startTime;
    }

    @Generated
    public List<Integer> getGuildIdList() {
        return this.guildIdList;
    }

    @Generated
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    @Generated
    public Location getLookLocation() {
        return this.lookLocation;
    }

    @Generated
    public void setLookLocation(Location lookLocation) {
        this.lookLocation = lookLocation;
    }

    @Generated
    public boolean XxXXxx(Object other) {
        return other instanceof SeasonPvpRoom;
    }

    @Generated
    public void setStatus(GuildPvpRoomStatusEnum status) {
        this.status = status;
    }

    @Generated
    public void setStartPlayerMap(Map<Integer, List<SeasonPvpPlayer>> startPlayerMap) {
        this.startPlayerMap = startPlayerMap;
    }

    @Generated
    public void setGuildPvpLocation(GuildPvpLocation guildPvpLocation) {
        this.guildPvpLocation = guildPvpLocation;
    }

    @Generated
    public Map<UUID, Integer> getStartGuildPlayerMap() {
        return this.startGuildPlayerMap;
    }

    @Generated
    public Map<Integer, List<UUID>> getPreparationPlayerMap() {
        return this.preparationPlayerMap;
    }

    @Generated
    public SeasonPvpRoom() {
    }

    @Generated
    public void setGuildIdList(List<Integer> guildIdList) {
        this.guildIdList = guildIdList;
    }

    @Generated
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof SeasonPvpRoom)) {
            return false;
        }
        if (!((SeasonPvpRoom)(other = (SeasonPvpRoom)other)).XxXXxx(this)) {
            return false;
        }
        Integer season = this.getSeason();
        Integer season2 = ((SeasonPvpRoom)other).getSeason();
        if (season == null ? season2 != null : !season.equals(season2)) {
            return false;
        }
        List<Integer> guildIdList = this.getGuildIdList();
        List<Integer> guildIdList2 = ((SeasonPvpRoom)other).getGuildIdList();
        if (guildIdList == null ? guildIdList2 != null : !guildIdList.equals(guildIdList2)) {
            return false;
        }
        GuildPvpRoomStatusEnum status = this.getStatus();
        GuildPvpRoomStatusEnum status2 = ((SeasonPvpRoom)other).getStatus();
        if (status == null ? status2 != null : !status.equals(status2)) {
            return false;
        }
        GuildPvpLocation guildPvpLocation = this.getGuildPvpLocation();
        GuildPvpLocation guildPvpLocation2 = ((SeasonPvpRoom)other).getGuildPvpLocation();
        if (guildPvpLocation == null ? guildPvpLocation2 != null : !guildPvpLocation.equals(guildPvpLocation2)) {
            return false;
        }
        Map<Integer, List<UUID>> preparationPlayerMap = this.getPreparationPlayerMap();
        Map<Integer, List<UUID>> preparationPlayerMap2 = ((SeasonPvpRoom)other).getPreparationPlayerMap();
        if (preparationPlayerMap == null ? preparationPlayerMap2 != null : !preparationPlayerMap.equals(preparationPlayerMap2)) {
            return false;
        }
        Map<Integer, List<SeasonPvpPlayer>> startPlayerMap = this.getStartPlayerMap();
        Map<Integer, List<SeasonPvpPlayer>> startPlayerMap2 = ((SeasonPvpRoom)other).getStartPlayerMap();
        if (startPlayerMap == null ? startPlayerMap2 != null : !startPlayerMap.equals(startPlayerMap2)) {
            return false;
        }
        Map<UUID, Integer> startGuildPlayerMap = this.getStartGuildPlayerMap();
        Map<UUID, Integer> startGuildPlayerMap2 = ((SeasonPvpRoom)other).getStartGuildPlayerMap();
        if (startGuildPlayerMap == null ? startGuildPlayerMap2 != null : !startGuildPlayerMap.equals(startGuildPlayerMap2)) {
            return false;
        }
        Map<UUID, Location> originLocationMap = this.getOriginLocationMap();
        Map<UUID, Location> originLocationMap2 = ((SeasonPvpRoom)other).getOriginLocationMap();
        if (originLocationMap == null ? originLocationMap2 != null : !originLocationMap.equals(originLocationMap2)) {
            return false;
        }
        Location lookLocation = this.getLookLocation();
        Location lookLocation2 = ((SeasonPvpRoom)other).getLookLocation();
        if (lookLocation == null ? lookLocation2 != null : !lookLocation.equals(lookLocation2)) {
            return false;
        }
        Date startTime = this.getStartTime();
        Date startTime2 = ((SeasonPvpRoom)other).getStartTime();
        if (startTime == null ? startTime2 != null : !startTime.equals(startTime2)) {
            return false;
        }
        return true;
    }
}
