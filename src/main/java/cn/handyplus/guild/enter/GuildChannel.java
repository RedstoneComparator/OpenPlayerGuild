package cn.handyplus.guild.enter;

import cn.handyplus.guild.lib.IndexEnum;
import cn.handyplus.guild.lib.TableField;
import cn.handyplus.guild.lib.TableName;
import java.util.Date;
import java.util.UUID;
import lombok.Generated;

@TableName(value="guild_channel", comment="\u73a9\u5bb6\u6e20\u9053")
public class GuildChannel {
    @TableField(value="id", comment="ID")
    private Integer id;
    @TableField(value="player_name", comment="\u73a9\u5bb6\u540d\u79f0", notNull=true)
    private String playerName;
    @TableField(value="player_uuid", comment="\u73a9\u5bb6uuid", notNull=true, indexEnum=IndexEnum.INDEX)
    private UUID playerUuid;
    @TableField(value="guild_name", comment="\u516c\u4f1a\u540d\u79f0", length=190)
    private String guildName;
    @TableField(value="tp_time", comment="\u4f20\u9001\u65f6\u95f4")
    private Date tpTime;
    @TableField(value="server_name", comment="\u670d\u52a1\u5668\u540d\u79f0", notNull=true)
    private String serverName;
    @TableField(value="spawn_location", comment="\u516c\u4f1a\u4e3b\u57ce\u5750\u6807", length=2000)
    private String spawnLocation;

    @Generated
    public GuildChannel() {
    }

    @Generated
    public Integer getId() {
        return this.id;
    }

    @Generated
    public String getPlayerName() {
        return this.playerName;
    }

    @Generated
    public UUID getPlayerUuid() {
        return this.playerUuid;
    }

    @Generated
    public String getGuildName() {
        return this.guildName;
    }

    @Generated
    public Date getTpTime() {
        return this.tpTime;
    }

    @Generated
    public String getServerName() {
        return this.serverName;
    }

    @Generated
    public String getSpawnLocation() {
        return this.spawnLocation;
    }

    @Generated
    public void setId(Integer id) {
        this.id = id;
    }

    @Generated
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    @Generated
    public void setPlayerUuid(UUID playerUuid) {
        this.playerUuid = playerUuid;
    }

    @Generated
    public void setGuildName(String guildName) {
        this.guildName = guildName;
    }

    @Generated
    public void setTpTime(Date tpTime) {
        this.tpTime = tpTime;
    }

    @Generated
    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    @Generated
    public void setSpawnLocation(String spawnLocation) {
        this.spawnLocation = spawnLocation;
    }

    @Generated
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof GuildChannel)) {
            return false;
        }
        GuildChannel that = (GuildChannel)other;
        if (!that.canEqual(this)) {
            return false;
        }
        Integer id = this.getId();
        Integer otherId = that.getId();
        if (id == null ? otherId != null : !((Object)id).equals(otherId)) {
            return false;
        }
        String playerName = this.getPlayerName();
        String otherPlayerName = that.getPlayerName();
        if (playerName == null ? otherPlayerName != null : !playerName.equals(otherPlayerName)) {
            return false;
        }
        UUID playerUuid = this.getPlayerUuid();
        UUID otherPlayerUuid = that.getPlayerUuid();
        if (playerUuid == null ? otherPlayerUuid != null : !((Object)playerUuid).equals(otherPlayerUuid)) {
            return false;
        }
        String guildName = this.getGuildName();
        String otherGuildName = that.getGuildName();
        if (guildName == null ? otherGuildName != null : !guildName.equals(otherGuildName)) {
            return false;
        }
        Date tpTime = this.getTpTime();
        Date otherTpTime = that.getTpTime();
        if (tpTime == null ? otherTpTime != null : !((Object)tpTime).equals(otherTpTime)) {
            return false;
        }
        String serverName = this.getServerName();
        String otherServerName = that.getServerName();
        if (serverName == null ? otherServerName != null : !serverName.equals(otherServerName)) {
            return false;
        }
        String spawnLocation = this.getSpawnLocation();
        String otherSpawnLocation = that.getSpawnLocation();
        return !(spawnLocation == null ? otherSpawnLocation != null : !spawnLocation.equals(otherSpawnLocation));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof GuildChannel;
    }

    @Generated
    public int hashCode() {
        int hashSeed = 59;
        int result = 1;
        Integer id = this.getId();
        result = result * 59 + (id == null ? 43 : ((Object)id).hashCode());
        String playerName = this.getPlayerName();
        result = result * 59 + (playerName == null ? 43 : playerName.hashCode());
        UUID playerUuid = this.getPlayerUuid();
        result = result * 59 + (playerUuid == null ? 43 : ((Object)playerUuid).hashCode());
        String guildName = this.getGuildName();
        result = result * 59 + (guildName == null ? 43 : guildName.hashCode());
        Date tpTime = this.getTpTime();
        result = result * 59 + (tpTime == null ? 43 : ((Object)tpTime).hashCode());
        String serverName = this.getServerName();
        result = result * 59 + (serverName == null ? 43 : serverName.hashCode());
        String spawnLocation = this.getSpawnLocation();
        result = result * 59 + (spawnLocation == null ? 43 : spawnLocation.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "GuildChannel(id=" + this.getId() + ", playerName=" + this.getPlayerName() + ", playerUuid=" + this.getPlayerUuid() + ", guildName=" + this.getGuildName() + ", tpTime=" + this.getTpTime() + ", serverName=" + this.getServerName() + ", spawnLocation=" + this.getSpawnLocation() + ")";
    }
}
