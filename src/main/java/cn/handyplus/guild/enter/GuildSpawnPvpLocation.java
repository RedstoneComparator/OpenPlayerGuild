package cn.handyplus.guild.enter;

import cn.handyplus.guild.lib.IndexEnum;
import cn.handyplus.guild.lib.TableField;
import cn.handyplus.guild.lib.TableName;
import lombok.Generated;

@TableName(value="guild_spawn_pvp_location", comment="\u516c\u4f1apvp\u51fa\u751f\u4f4d\u7f6e")
public class GuildSpawnPvpLocation {
    @TableField(value="id", comment="ID")
    private Integer id;
    @TableField(value="guild_pvp_location", comment="\u516c\u4f1apvp\u4f4d\u7f6eId", notNull=true, indexEnum=IndexEnum.INDEX)
    private Integer guildPvpLocationId;
    @TableField(value="spawn_name", comment="\u51fa\u751f\u70b9\u540d\u79f0", notNull=true)
    private String spawnName;
    @TableField(value="x", comment="x")
    private double x;
    @TableField(value="y", comment="y")
    private double y;
    @TableField(value="z", comment="z")
    private double z;
    @TableField(value="yaw", comment="yaw")
    private double yaw;
    @TableField(value="pitch", comment="pitch")
    private double pitch;
    @TableField(value="world", comment="\u4e16\u754c\u540d")
    private String world;

    @Generated
    public GuildSpawnPvpLocation() {
    }

    @Generated
    public Integer getId() {
        return this.id;
    }

    @Generated
    public Integer getGuildPvpLocationId() {
        return this.guildPvpLocationId;
    }

    @Generated
    public String getSpawnName() {
        return this.spawnName;
    }

    @Generated
    public double getX() {
        return this.x;
    }

    @Generated
    public double getY() {
        return this.y;
    }

    @Generated
    public double getZ() {
        return this.z;
    }

    @Generated
    public double getYaw() {
        return this.yaw;
    }

    @Generated
    public double getPitch() {
        return this.pitch;
    }

    @Generated
    public String getWorld() {
        return this.world;
    }

    @Generated
    public void setId(Integer id) {
        this.id = id;
    }

    @Generated
    public void setGuildPvpLocationId(Integer guildPvpLocationId) {
        this.guildPvpLocationId = guildPvpLocationId;
    }

    @Generated
    public void setSpawnName(String spawnName) {
        this.spawnName = spawnName;
    }

    @Generated
    public void setX(double x) {
        this.x = x;
    }

    @Generated
    public void setY(double y) {
        this.y = y;
    }

    @Generated
    public void setZ(double z) {
        this.z = z;
    }

    @Generated
    public void setYaw(double yaw) {
        this.yaw = yaw;
    }

    @Generated
    public void setPitch(double pitch) {
        this.pitch = pitch;
    }

    @Generated
    public void setWorld(String world) {
        this.world = world;
    }

    @Generated
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof GuildSpawnPvpLocation)) {
            return false;
        }
        GuildSpawnPvpLocation otherGuild = (GuildSpawnPvpLocation)other;
        if (!otherGuild.canEqual(this)) {
            return false;
        }
        if (Double.compare(this.getX(), otherGuild.getX()) != 0) {
            return false;
        }
        if (Double.compare(this.getY(), otherGuild.getY()) != 0) {
            return false;
        }
        if (Double.compare(this.getZ(), otherGuild.getZ()) != 0) {
            return false;
        }
        if (Double.compare(this.getYaw(), otherGuild.getYaw()) != 0) {
            return false;
        }
        if (Double.compare(this.getPitch(), otherGuild.getPitch()) != 0) {
            return false;
        }
        Integer thisId = this.getId();
        Integer otherId = otherGuild.getId();
        if (thisId == null ? otherId != null : !((Object)thisId).equals(otherId)) {
            return false;
        }
        Integer thisGuildPvpLocationId = this.getGuildPvpLocationId();
        Integer otherGuildPvpLocationId = otherGuild.getGuildPvpLocationId();
        if (thisGuildPvpLocationId == null ? otherGuildPvpLocationId != null : !((Object)thisGuildPvpLocationId).equals(otherGuildPvpLocationId)) {
            return false;
        }
        String thisSpawnName = this.getSpawnName();
        String otherSpawnName = otherGuild.getSpawnName();
        if (thisSpawnName == null ? otherSpawnName != null : !thisSpawnName.equals(otherSpawnName)) {
            return false;
        }
        String thisWorld = this.getWorld();
        String otherWorld = otherGuild.getWorld();
        return !(thisWorld == null ? otherWorld != null : !thisWorld.equals(otherWorld));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof GuildSpawnPvpLocation;
    }

    @Generated
    public int hashCode() {
        int prime = 59;
        int hash = 1;
        long xBits = Double.doubleToLongBits(this.getX());
        hash = hash * 59 + (int)(xBits >>> 32 ^ xBits);
        long yBits = Double.doubleToLongBits(this.getY());
        hash = hash * 59 + (int)(yBits >>> 32 ^ yBits);
        long zBits = Double.doubleToLongBits(this.getZ());
        hash = hash * 59 + (int)(zBits >>> 32 ^ zBits);
        long yawBits = Double.doubleToLongBits(this.getYaw());
        hash = hash * 59 + (int)(yawBits >>> 32 ^ yawBits);
        long pitchBits = Double.doubleToLongBits(this.getPitch());
        hash = hash * 59 + (int)(pitchBits >>> 32 ^ pitchBits);
        Integer idValue = this.getId();
        hash = hash * 59 + (idValue == null ? 43 : ((Object)idValue).hashCode());
        Integer guildPvpLocationIdValue = this.getGuildPvpLocationId();
        hash = hash * 59 + (guildPvpLocationIdValue == null ? 43 : ((Object)guildPvpLocationIdValue).hashCode());
        String spawnNameValue = this.getSpawnName();
        hash = hash * 59 + (spawnNameValue == null ? 43 : spawnNameValue.hashCode());
        String worldValue = this.getWorld();
        hash = hash * 59 + (worldValue == null ? 43 : worldValue.hashCode());
        return hash;
    }

    @Generated
    public String toString() {
        return "GuildSpawnPvpLocation(id=" + this.getId() + ", guildPvpLocationId=" + this.getGuildPvpLocationId() + ", spawnName=" + this.getSpawnName() + ", x=" + this.getX() + ", y=" + this.getY() + ", z=" + this.getZ() + ", yaw=" + this.getYaw() + ", pitch=" + this.getPitch() + ", world=" + this.getWorld() + ")";
    }
}
