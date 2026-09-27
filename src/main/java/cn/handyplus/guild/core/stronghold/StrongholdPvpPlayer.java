package cn.handyplus.guild.core.stronghold;

import java.util.UUID;
import lombok.Generated;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public class StrongholdPvpPlayer {
    @NotNull
    private String playerName;
    private Location originLocation;
    private int die;
    private int kill;
    @NotNull
    private UUID playerUuid;
    private GameMode originGameMode;
    @NotNull
    private Integer guildId;
    private boolean waitingRespawn;
    private int deathCount;

    @Generated
    public void setPlayerUuid(@NotNull UUID playerUuid) {
        if (playerUuid == null) {
            throw new NullPointerException("playerUuid is marked non-null but is null");
        }
        this.playerUuid = playerUuid;
    }

    @Generated
    public void setOriginGameMode(GameMode originGameMode) {
        this.originGameMode = originGameMode;
    }

    @Generated
    public void setOriginLocation(Location originLocation) {
        this.originLocation = originLocation;
    }

    @Generated
    public StrongholdPvpPlayer() {
    }

    @Generated
    public boolean isWaitingRespawn() {
        return this.waitingRespawn;
    }

    @Generated
    public int getDie() {
        return this.die;
    }

    @Generated
    public void setDie(int die) {
        this.die = die;
    }

    @Generated
    public Location getOriginLocation() {
        return this.originLocation;
    }

    @Generated
    public String toString() {
        return new StringBuilder().insert(0, "StrongholdPvpPlayer(guildId=").append(this.getGuildId()).append(", playerName=").append(this.getPlayerName()).append(", playerUuid=").append(this.getPlayerUuid()).append(", kill=").append(this.getKill()).append(", die=").append(this.getDie()).append(", deathCount=").append(this.getDeathCount()).append(", waitingRespawn=").append(this.isWaitingRespawn()).append(", originLocation=").append(this.getOriginLocation()).append(", originGameMode=").append(this.getOriginGameMode()).append(")").toString();
    }

    @Generated
    public void setWaitingRespawn(boolean waitingRespawn) {
        this.waitingRespawn = waitingRespawn;
    }

    @NotNull
    @Generated
    public UUID getPlayerUuid() {
        return this.playerUuid;
    }

    @Generated
    public void setDeathCount(int deathCount) {
        this.deathCount = deathCount;
    }

    @Generated
    public int getKill() {
        return this.kill;
    }

    @Generated
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof StrongholdPvpPlayer)) {
            return false;
        }
        if (!((StrongholdPvpPlayer)(other = (StrongholdPvpPlayer)other)).XxXXxx(this)) {
            return false;
        }
        if (this.getKill() != ((StrongholdPvpPlayer)other).getKill()) {
            return false;
        }
        if (this.getDie() != ((StrongholdPvpPlayer)other).getDie()) {
            return false;
        }
        if (this.getDeathCount() != ((StrongholdPvpPlayer)other).getDeathCount()) {
            return false;
        }
        if (this.isWaitingRespawn() != ((StrongholdPvpPlayer)other).isWaitingRespawn()) {
            return false;
        }
        Object thisValue = this.getGuildId();
        Object otherValue = ((StrongholdPvpPlayer)other).getGuildId();
        if (thisValue == null ? otherValue != null : !thisValue.equals(otherValue)) {
            return false;
        }
        thisValue = this.getPlayerName();
        otherValue = ((StrongholdPvpPlayer)other).getPlayerName();
        if (thisValue == null ? otherValue != null : !thisValue.equals(otherValue)) {
            return false;
        }
        thisValue = this.getPlayerUuid();
        otherValue = ((StrongholdPvpPlayer)other).getPlayerUuid();
        if (thisValue == null ? otherValue != null : !thisValue.equals(otherValue)) {
            return false;
        }
        thisValue = this.getOriginLocation();
        otherValue = ((StrongholdPvpPlayer)other).getOriginLocation();
        if (thisValue == null ? otherValue != null : !thisValue.equals(otherValue)) {
            return false;
        }
        thisValue = this.getOriginGameMode();
        other = ((StrongholdPvpPlayer)other).getOriginGameMode();
        if (thisValue == null ? other != null : !thisValue.equals(other)) {
            return false;
        }
        return true;
    }

    @Generated
    public void setGuildId(@NotNull Integer guildId) {
        if (guildId == null) {
            throw new NullPointerException("guildId is marked non-null but is null");
        }
        this.guildId = guildId;
    }

    @NotNull
    @Generated
    public Integer getGuildId() {
        return this.guildId;
    }

    @Generated
    public void setPlayerName(@NotNull String playerName) {
        if (playerName == null) {
            throw new NullPointerException("playerName is marked non-null but is null");
        }
        this.playerName = playerName;
    }

    @Generated
    public GameMode getOriginGameMode() {
        return this.originGameMode;
    }

    @Generated
    public int getDeathCount() {
        return this.deathCount;
    }

    @Generated
    public int hashCode() {
        int result = 59;
        result = 1;
        result = 59 + this.getKill();
        result = result * 59 + this.getDie();
        result = result * 59 + this.getDeathCount();
        result = result * 59 + (this.isWaitingRespawn() ? 79 : 97);
        Object value = this.getGuildId();
        result = result * 59 + (value == null ? 43 : value.hashCode());
        value = this.getPlayerName();
        result = result * 59 + (value == null ? 43 : value.hashCode());
        value = this.getPlayerUuid();
        result = result * 59 + (value == null ? 43 : value.hashCode());
        value = this.getOriginLocation();
        result = result * 59 + (value == null ? 43 : value.hashCode());
        value = this.getOriginGameMode();
        result = result * 59 + (value == null ? 43 : value.hashCode());
        return result;
    }

    @NotNull
    @Generated
    public String getPlayerName() {
        return this.playerName;
    }

    @Generated
    public void setKill(int kill) {
        this.kill = kill;
    }

    @Generated
    public boolean XxXXxx(Object object) {
        return object instanceof StrongholdPvpPlayer;
    }
}
