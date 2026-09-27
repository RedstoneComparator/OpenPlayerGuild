package cn.handyplus.guild.param;

import lombok.Generated;

public class GuildSpawn {
    private String serverName;
    private Integer id;
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;
    private String world;
    private String permission;

    @Generated
    public GuildSpawn() {
    }

    @Generated
    public String getServerName() {
        return this.serverName;
    }

    @Generated
    public Integer getId() {
        return this.id;
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
    public float getYaw() {
        return this.yaw;
    }

    @Generated
    public float getPitch() {
        return this.pitch;
    }

    @Generated
    public String getWorld() {
        return this.world;
    }

    @Generated
    public String getPermission() {
        return this.permission;
    }

    @Generated
    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    @Generated
    public void setId(Integer id) {
        this.id = id;
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
    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    @Generated
    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    @Generated
    public void setWorld(String world) {
        this.world = world;
    }

    @Generated
    public void setPermission(String permission) {
        this.permission = permission;
    }

    @Generated
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof GuildSpawn)) {
            return false;
        }
        GuildSpawn otherSpawn = (GuildSpawn)other;
        if (!otherSpawn.canEqual(this)) {
            return false;
        }
        if (Double.compare(this.getX(), otherSpawn.getX()) != 0) {
            return false;
        }
        if (Double.compare(this.getY(), otherSpawn.getY()) != 0) {
            return false;
        }
        if (Double.compare(this.getZ(), otherSpawn.getZ()) != 0) {
            return false;
        }
        if (Float.compare(this.getYaw(), otherSpawn.getYaw()) != 0) {
            return false;
        }
        if (Float.compare(this.getPitch(), otherSpawn.getPitch()) != 0) {
            return false;
        }
        Integer thisId = this.getId();
        Integer otherId = otherSpawn.getId();
        if (thisId == null ? otherId != null : !((Object)thisId).equals(otherId)) {
            return false;
        }
        String thisServerName = this.getServerName();
        String otherServerName = otherSpawn.getServerName();
        if (thisServerName == null ? otherServerName != null : !thisServerName.equals(otherServerName)) {
            return false;
        }
        String thisWorld = this.getWorld();
        String otherWorld = otherSpawn.getWorld();
        if (thisWorld == null ? otherWorld != null : !thisWorld.equals(otherWorld)) {
            return false;
        }
        String thisPermission = this.getPermission();
        String otherPermission = otherSpawn.getPermission();
        return !(thisPermission == null ? otherPermission != null : !thisPermission.equals(otherPermission));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof GuildSpawn;
    }

    @Generated
    public int hashCode() {
        int prime = 59;
        int result = 1;
        long xBits = Double.doubleToLongBits(this.getX());
        result = result * 59 + (int)(xBits >>> 32 ^ xBits);
        long yBits = Double.doubleToLongBits(this.getY());
        result = result * 59 + (int)(yBits >>> 32 ^ yBits);
        long zBits = Double.doubleToLongBits(this.getZ());
        result = result * 59 + (int)(zBits >>> 32 ^ zBits);
        result = result * 59 + Float.floatToIntBits(this.getYaw());
        result = result * 59 + Float.floatToIntBits(this.getPitch());
        Integer id = this.getId();
        result = result * 59 + (id == null ? 43 : ((Object)id).hashCode());
        String serverName = this.getServerName();
        result = result * 59 + (serverName == null ? 43 : serverName.hashCode());
        String world = this.getWorld();
        result = result * 59 + (world == null ? 43 : world.hashCode());
        String permission = this.getPermission();
        result = result * 59 + (permission == null ? 43 : permission.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "GuildSpawn(serverName=" + this.getServerName() + ", id=" + this.getId() + ", x=" + this.getX() + ", y=" + this.getY() + ", z=" + this.getZ() + ", yaw=" + this.getYaw() + ", pitch=" + this.getPitch() + ", world=" + this.getWorld() + ", permission=" + this.getPermission() + ")";
    }
}
