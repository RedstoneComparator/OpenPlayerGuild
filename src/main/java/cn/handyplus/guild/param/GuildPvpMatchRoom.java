package cn.handyplus.guild.param;

import cn.handyplus.guild.constants.GuildPvpBattleTypeEnum;
import cn.handyplus.guild.constants.GuildPvpRoomStatusEnum;
import cn.handyplus.guild.enter.GuildPlayer;
import cn.handyplus.guild.enter.GuildPvpLocation;
import cn.handyplus.guild.param.GuildPvpPlayerParam;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.Generated;
import org.bukkit.Location;

public class GuildPvpMatchRoom {
    private Integer locationId;
    private GuildPvpBattleTypeEnum battleType;
    private GuildPvpLocation guildPvpLocation;
    private Location firstLocation;
    private Location secondLocation;
    private Location lookLocation;
    private List<Integer> guildIdList;
    private GuildPvpRoomStatusEnum status;
    private Integer prepareSecond;
    private Integer startSecond;
    private Map<Integer, List<GuildPlayer>> guildPlayerMap;
    private Map<Integer, List<UUID>> preparationPlayerMap;
    private Map<Integer, List<GuildPvpPlayerParam>> startPlayerMap;
    private Map<UUID, Location> originLocationMap;
    private Date startTime;

    @Generated
    public GuildPvpMatchRoom() {
    }

    @Generated
    public Integer getLocationId() {
        return this.locationId;
    }

    @Generated
    public GuildPvpBattleTypeEnum getBattleType() {
        return this.battleType;
    }

    @Generated
    public GuildPvpLocation getGuildPvpLocation() {
        return this.guildPvpLocation;
    }

    @Generated
    public Location getFirstLocation() {
        return this.firstLocation;
    }

    @Generated
    public Location getSecondLocation() {
        return this.secondLocation;
    }

    @Generated
    public Location getLookLocation() {
        return this.lookLocation;
    }

    @Generated
    public List<Integer> getGuildIdList() {
        return this.guildIdList;
    }

    @Generated
    public GuildPvpRoomStatusEnum getStatus() {
        return this.status;
    }

    @Generated
    public Integer getPrepareSecond() {
        return this.prepareSecond;
    }

    @Generated
    public Integer getStartSecond() {
        return this.startSecond;
    }

    @Generated
    public Map<Integer, List<GuildPlayer>> getGuildPlayerMap() {
        return this.guildPlayerMap;
    }

    @Generated
    public Map<Integer, List<UUID>> getPreparationPlayerMap() {
        return this.preparationPlayerMap;
    }

    @Generated
    public Map<Integer, List<GuildPvpPlayerParam>> getStartPlayerMap() {
        return this.startPlayerMap;
    }

    @Generated
    public Map<UUID, Location> getOriginLocationMap() {
        return this.originLocationMap;
    }

    @Generated
    public Date getStartTime() {
        return this.startTime;
    }

    @Generated
    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    @Generated
    public void setBattleType(GuildPvpBattleTypeEnum battleType) {
        this.battleType = battleType;
    }

    @Generated
    public void setGuildPvpLocation(GuildPvpLocation guildPvpLocation) {
        this.guildPvpLocation = guildPvpLocation;
    }

    @Generated
    public void setFirstLocation(Location firstLocation) {
        this.firstLocation = firstLocation;
    }

    @Generated
    public void setSecondLocation(Location secondLocation) {
        this.secondLocation = secondLocation;
    }

    @Generated
    public void setLookLocation(Location lookLocation) {
        this.lookLocation = lookLocation;
    }

    @Generated
    public void setGuildIdList(List<Integer> guildIdList) {
        this.guildIdList = guildIdList;
    }

    @Generated
    public void setStatus(GuildPvpRoomStatusEnum status) {
        this.status = status;
    }

    @Generated
    public void setPrepareSecond(Integer prepareSecond) {
        this.prepareSecond = prepareSecond;
    }

    @Generated
    public void setStartSecond(Integer startSecond) {
        this.startSecond = startSecond;
    }

    @Generated
    public void setGuildPlayerMap(Map<Integer, List<GuildPlayer>> guildPlayerMap) {
        this.guildPlayerMap = guildPlayerMap;
    }

    @Generated
    public void setPreparationPlayerMap(Map<Integer, List<UUID>> preparationPlayerMap) {
        this.preparationPlayerMap = preparationPlayerMap;
    }

    @Generated
    public void setStartPlayerMap(Map<Integer, List<GuildPvpPlayerParam>> startPlayerMap) {
        this.startPlayerMap = startPlayerMap;
    }

    @Generated
    public void setOriginLocationMap(Map<UUID, Location> originLocationMap) {
        this.originLocationMap = originLocationMap;
    }

    @Generated
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    @Generated
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof GuildPvpMatchRoom)) {
            return false;
        }
        GuildPvpMatchRoom otherRoom = (GuildPvpMatchRoom)other;
        if (!otherRoom.canEqual(this)) {
            return false;
        }
        Integer thisLocationId = this.getLocationId();
        Integer otherLocationId = otherRoom.getLocationId();
        if (thisLocationId == null ? otherLocationId != null : !((Object)thisLocationId).equals(otherLocationId)) {
            return false;
        }
        Integer thisPrepareSecond = this.getPrepareSecond();
        Integer otherPrepareSecond = otherRoom.getPrepareSecond();
        if (thisPrepareSecond == null ? otherPrepareSecond != null : !((Object)thisPrepareSecond).equals(otherPrepareSecond)) {
            return false;
        }
        Integer thisStartSecond = this.getStartSecond();
        Integer otherStartSecond = otherRoom.getStartSecond();
        if (thisStartSecond == null ? otherStartSecond != null : !((Object)thisStartSecond).equals(otherStartSecond)) {
            return false;
        }
        GuildPvpBattleTypeEnum thisBattleType = this.getBattleType();
        GuildPvpBattleTypeEnum otherBattleType = otherRoom.getBattleType();
        if (thisBattleType == null ? otherBattleType != null : !((Object)((Object)thisBattleType)).equals((Object)otherBattleType)) {
            return false;
        }
        GuildPvpLocation thisGuildPvpLocation = this.getGuildPvpLocation();
        GuildPvpLocation otherGuildPvpLocation = otherRoom.getGuildPvpLocation();
        if (thisGuildPvpLocation == null ? otherGuildPvpLocation != null : !((Object)thisGuildPvpLocation).equals(otherGuildPvpLocation)) {
            return false;
        }
        Location thisFirstLocation = this.getFirstLocation();
        Location otherFirstLocation = otherRoom.getFirstLocation();
        if (thisFirstLocation == null ? otherFirstLocation != null : !thisFirstLocation.equals(otherFirstLocation)) {
            return false;
        }
        Location thisSecondLocation = this.getSecondLocation();
        Location otherSecondLocation = otherRoom.getSecondLocation();
        if (thisSecondLocation == null ? otherSecondLocation != null : !thisSecondLocation.equals(otherSecondLocation)) {
            return false;
        }
        Location thisLookLocation = this.getLookLocation();
        Location otherLookLocation = otherRoom.getLookLocation();
        if (thisLookLocation == null ? otherLookLocation != null : !thisLookLocation.equals(otherLookLocation)) {
            return false;
        }
        List<Integer> thisGuildIdList = this.getGuildIdList();
        List<Integer> otherGuildIdList = otherRoom.getGuildIdList();
        if (thisGuildIdList == null ? otherGuildIdList != null : !((Object)thisGuildIdList).equals(otherGuildIdList)) {
            return false;
        }
        GuildPvpRoomStatusEnum thisStatus = this.getStatus();
        GuildPvpRoomStatusEnum otherStatus = otherRoom.getStatus();
        if (thisStatus == null ? otherStatus != null : !((Object)((Object)thisStatus)).equals((Object)otherStatus)) {
            return false;
        }
        Map<Integer, List<GuildPlayer>> thisGuildPlayerMap = this.getGuildPlayerMap();
        Map<Integer, List<GuildPlayer>> otherGuildPlayerMap = otherRoom.getGuildPlayerMap();
        if (thisGuildPlayerMap == null ? otherGuildPlayerMap != null : !((Object)thisGuildPlayerMap).equals(otherGuildPlayerMap)) {
            return false;
        }
        Map<Integer, List<UUID>> thisPreparationPlayerMap = this.getPreparationPlayerMap();
        Map<Integer, List<UUID>> otherPreparationPlayerMap = otherRoom.getPreparationPlayerMap();
        if (thisPreparationPlayerMap == null ? otherPreparationPlayerMap != null : !((Object)thisPreparationPlayerMap).equals(otherPreparationPlayerMap)) {
            return false;
        }
        Map<Integer, List<GuildPvpPlayerParam>> thisStartPlayerMap = this.getStartPlayerMap();
        Map<Integer, List<GuildPvpPlayerParam>> otherStartPlayerMap = otherRoom.getStartPlayerMap();
        if (thisStartPlayerMap == null ? otherStartPlayerMap != null : !((Object)thisStartPlayerMap).equals(otherStartPlayerMap)) {
            return false;
        }
        Map<UUID, Location> thisOriginLocationMap = this.getOriginLocationMap();
        Map<UUID, Location> otherOriginLocationMap = otherRoom.getOriginLocationMap();
        if (thisOriginLocationMap == null ? otherOriginLocationMap != null : !((Object)thisOriginLocationMap).equals(otherOriginLocationMap)) {
            return false;
        }
        Date thisStartTime = this.getStartTime();
        Date otherStartTime = otherRoom.getStartTime();
        return !(thisStartTime == null ? otherStartTime != null : !((Object)thisStartTime).equals(otherStartTime));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof GuildPvpMatchRoom;
    }

    @Generated
    public int hashCode() {
        int prime = 59;
        int result = 1;
        Integer locationId = this.getLocationId();
        result = result * 59 + (locationId == null ? 43 : ((Object)locationId).hashCode());
        Integer prepareSecond = this.getPrepareSecond();
        result = result * 59 + (prepareSecond == null ? 43 : ((Object)prepareSecond).hashCode());
        Integer startSecond = this.getStartSecond();
        result = result * 59 + (startSecond == null ? 43 : ((Object)startSecond).hashCode());
        GuildPvpBattleTypeEnum battleType = this.getBattleType();
        result = result * 59 + (battleType == null ? 43 : ((Object)((Object)battleType)).hashCode());
        GuildPvpLocation guildPvpLocation = this.getGuildPvpLocation();
        result = result * 59 + (guildPvpLocation == null ? 43 : ((Object)guildPvpLocation).hashCode());
        Location firstLocation = this.getFirstLocation();
        result = result * 59 + (firstLocation == null ? 43 : firstLocation.hashCode());
        Location secondLocation = this.getSecondLocation();
        result = result * 59 + (secondLocation == null ? 43 : secondLocation.hashCode());
        Location lookLocation = this.getLookLocation();
        result = result * 59 + (lookLocation == null ? 43 : lookLocation.hashCode());
        List<Integer> guildIdList = this.getGuildIdList();
        result = result * 59 + (guildIdList == null ? 43 : ((Object)guildIdList).hashCode());
        GuildPvpRoomStatusEnum status = this.getStatus();
        result = result * 59 + (status == null ? 43 : ((Object)((Object)status)).hashCode());
        Map<Integer, List<GuildPlayer>> guildPlayerMap = this.getGuildPlayerMap();
        result = result * 59 + (guildPlayerMap == null ? 43 : ((Object)guildPlayerMap).hashCode());
        Map<Integer, List<UUID>> preparationPlayerMap = this.getPreparationPlayerMap();
        result = result * 59 + (preparationPlayerMap == null ? 43 : ((Object)preparationPlayerMap).hashCode());
        Map<Integer, List<GuildPvpPlayerParam>> startPlayerMap = this.getStartPlayerMap();
        result = result * 59 + (startPlayerMap == null ? 43 : ((Object)startPlayerMap).hashCode());
        Map<UUID, Location> originLocationMap = this.getOriginLocationMap();
        result = result * 59 + (originLocationMap == null ? 43 : ((Object)originLocationMap).hashCode());
        Date startTime = this.getStartTime();
        result = result * 59 + (startTime == null ? 43 : ((Object)startTime).hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "GuildPvpMatchRoom(locationId=" + this.getLocationId() + ", battleType=" + (Object)((Object)this.getBattleType()) + ", guildPvpLocation=" + this.getGuildPvpLocation() + ", firstLocation=" + this.getFirstLocation() + ", secondLocation=" + this.getSecondLocation() + ", lookLocation=" + this.getLookLocation() + ", guildIdList=" + this.getGuildIdList() + ", status=" + (Object)((Object)this.getStatus()) + ", prepareSecond=" + this.getPrepareSecond() + ", startSecond=" + this.getStartSecond() + ", guildPlayerMap=" + this.getGuildPlayerMap() + ", preparationPlayerMap=" + this.getPreparationPlayerMap() + ", startPlayerMap=" + this.getStartPlayerMap() + ", originLocationMap=" + this.getOriginLocationMap() + ", startTime=" + this.getStartTime() + ")";
    }
}
