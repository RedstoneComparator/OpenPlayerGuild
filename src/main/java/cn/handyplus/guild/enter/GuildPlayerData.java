package cn.handyplus.guild.enter;

import cn.handyplus.guild.lib.IndexEnum;
import cn.handyplus.guild.lib.TableField;
import cn.handyplus.guild.lib.TableName;
import java.io.Serializable;
import java.util.UUID;
import lombok.Generated;

@TableName(value="guild_player_data", comment="\u516c\u4f1a\u73a9\u5bb6\u6570\u636e")
public class GuildPlayerData
implements Serializable {
    @TableField(value="id", comment="ID")
    private Integer id;
    @TableField(value="player_name", comment="\u73a9\u5bb6\u540d\u79f0", notNull=true)
    private String playerName;
    @TableField(value="player_uuid", comment="\u73a9\u5bb6uuid", notNull=true, indexEnum=IndexEnum.UNIQUE)
    private UUID playerUuid;
    @TableField(value="money", comment="\u5f53\u524d\u8d21\u732e\u5ea6", notNull=true, fieldDefault="0")
    private Integer money;
    @TableField(value="week_money", comment="\u5468\u8d21\u732e\u5ea6", notNull=true, fieldDefault="0")
    private Integer weekMoney;
    @TableField(value="total_money", comment="\u603b\u8d21\u732e\u5ea6", notNull=true, fieldDefault="0")
    private Integer totalMoney;
    @TableField(value="guild_money", comment="\u8d21\u732e\u8d44\u91d1", notNull=true, fieldDefault="0")
    private Integer guildMoney;
    @TableField(value="ore", comment="\u77ff\u77f3", notNull=true, fieldDefault="0")
    private Integer ore;
    @TableField(value="total_ore", comment="\u603b\u77ff\u77f3", notNull=true, fieldDefault="0")
    private Integer totalOre;
    @TableField(value="kill", comment="\u516c\u4f1a\u6218\u51fb\u6740\u6b21\u6570", notNull=true, fieldDefault="0")
    private Integer kill;
    @TableField(value="die", comment="\u516c\u4f1a\u6218\u6b7b\u4ea1\u6b21\u6570", notNull=true, fieldDefault="0")
    private Integer die;

    @Generated
    public GuildPlayerData() {
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
    public Integer getMoney() {
        return this.money;
    }

    @Generated
    public Integer getWeekMoney() {
        return this.weekMoney;
    }

    @Generated
    public Integer getTotalMoney() {
        return this.totalMoney;
    }

    @Generated
    public Integer getGuildMoney() {
        return this.guildMoney;
    }

    @Generated
    public Integer getOre() {
        return this.ore;
    }

    @Generated
    public Integer getTotalOre() {
        return this.totalOre;
    }

    @Generated
    public Integer getKill() {
        return this.kill;
    }

    @Generated
    public Integer getDie() {
        return this.die;
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
    public void setMoney(Integer money) {
        this.money = money;
    }

    @Generated
    public void setWeekMoney(Integer weekMoney) {
        this.weekMoney = weekMoney;
    }

    @Generated
    public void setTotalMoney(Integer totalMoney) {
        this.totalMoney = totalMoney;
    }

    @Generated
    public void setGuildMoney(Integer guildMoney) {
        this.guildMoney = guildMoney;
    }

    @Generated
    public void setOre(Integer ore) {
        this.ore = ore;
    }

    @Generated
    public void setTotalOre(Integer totalOre) {
        this.totalOre = totalOre;
    }

    @Generated
    public void setKill(Integer kill) {
        this.kill = kill;
    }

    @Generated
    public void setDie(Integer die) {
        this.die = die;
    }

    @Generated
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof GuildPlayerData)) {
            return false;
        }
        GuildPlayerData otherGuild = (GuildPlayerData)other;
        if (!otherGuild.canEqual(this)) {
            return false;
        }
        Integer thisId = this.getId();
        Integer otherId = otherGuild.getId();
        if (thisId == null ? otherId != null : !((Object)thisId).equals(otherId)) {
            return false;
        }
        Integer thisMoney = this.getMoney();
        Integer otherMoney = otherGuild.getMoney();
        if (thisMoney == null ? otherMoney != null : !((Object)thisMoney).equals(otherMoney)) {
            return false;
        }
        Integer thisWeekMoney = this.getWeekMoney();
        Integer otherWeekMoney = otherGuild.getWeekMoney();
        if (thisWeekMoney == null ? otherWeekMoney != null : !((Object)thisWeekMoney).equals(otherWeekMoney)) {
            return false;
        }
        Integer thisTotalMoney = this.getTotalMoney();
        Integer otherTotalMoney = otherGuild.getTotalMoney();
        if (thisTotalMoney == null ? otherTotalMoney != null : !((Object)thisTotalMoney).equals(otherTotalMoney)) {
            return false;
        }
        Integer thisGuildMoney = this.getGuildMoney();
        Integer otherGuildMoney = otherGuild.getGuildMoney();
        if (thisGuildMoney == null ? otherGuildMoney != null : !((Object)thisGuildMoney).equals(otherGuildMoney)) {
            return false;
        }
        Integer thisOre = this.getOre();
        Integer otherOre = otherGuild.getOre();
        if (thisOre == null ? otherOre != null : !((Object)thisOre).equals(otherOre)) {
            return false;
        }
        Integer thisTotalOre = this.getTotalOre();
        Integer otherTotalOre = otherGuild.getTotalOre();
        if (thisTotalOre == null ? otherTotalOre != null : !((Object)thisTotalOre).equals(otherTotalOre)) {
            return false;
        }
        Integer thisKill = this.getKill();
        Integer otherKill = otherGuild.getKill();
        if (thisKill == null ? otherKill != null : !((Object)thisKill).equals(otherKill)) {
            return false;
        }
        Integer thisDie = this.getDie();
        Integer otherDie = otherGuild.getDie();
        if (thisDie == null ? otherDie != null : !((Object)thisDie).equals(otherDie)) {
            return false;
        }
        String thisPlayerName = this.getPlayerName();
        String otherPlayerName = otherGuild.getPlayerName();
        if (thisPlayerName == null ? otherPlayerName != null : !thisPlayerName.equals(otherPlayerName)) {
            return false;
        }
        UUID thisPlayerUuid = this.getPlayerUuid();
        UUID otherPlayerUuid = otherGuild.getPlayerUuid();
        return !(thisPlayerUuid == null ? otherPlayerUuid != null : !((Object)thisPlayerUuid).equals(otherPlayerUuid));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof GuildPlayerData;
    }

    @Generated
    public int hashCode() {
        int prime = 59;
        int hash = 1;
        Integer idValue = this.getId();
        hash = hash * 59 + (idValue == null ? 43 : ((Object)idValue).hashCode());
        Integer moneyValue = this.getMoney();
        hash = hash * 59 + (moneyValue == null ? 43 : ((Object)moneyValue).hashCode());
        Integer weekMoneyValue = this.getWeekMoney();
        hash = hash * 59 + (weekMoneyValue == null ? 43 : ((Object)weekMoneyValue).hashCode());
        Integer totalMoneyValue = this.getTotalMoney();
        hash = hash * 59 + (totalMoneyValue == null ? 43 : ((Object)totalMoneyValue).hashCode());
        Integer guildMoneyValue = this.getGuildMoney();
        hash = hash * 59 + (guildMoneyValue == null ? 43 : ((Object)guildMoneyValue).hashCode());
        Integer oreValue = this.getOre();
        hash = hash * 59 + (oreValue == null ? 43 : ((Object)oreValue).hashCode());
        Integer totalOreValue = this.getTotalOre();
        hash = hash * 59 + (totalOreValue == null ? 43 : ((Object)totalOreValue).hashCode());
        Integer killValue = this.getKill();
        hash = hash * 59 + (killValue == null ? 43 : ((Object)killValue).hashCode());
        Integer dieValue = this.getDie();
        hash = hash * 59 + (dieValue == null ? 43 : ((Object)dieValue).hashCode());
        String playerNameValue = this.getPlayerName();
        hash = hash * 59 + (playerNameValue == null ? 43 : playerNameValue.hashCode());
        UUID playerUuidValue = this.getPlayerUuid();
        hash = hash * 59 + (playerUuidValue == null ? 43 : ((Object)playerUuidValue).hashCode());
        return hash;
    }

    @Generated
    public String toString() {
        return "GuildPlayerData(id=" + this.getId() + ", playerName=" + this.getPlayerName() + ", playerUuid=" + this.getPlayerUuid() + ", money=" + this.getMoney() + ", weekMoney=" + this.getWeekMoney() + ", totalMoney=" + this.getTotalMoney() + ", guildMoney=" + this.getGuildMoney() + ", ore=" + this.getOre() + ", totalOre=" + this.getTotalOre() + ", kill=" + this.getKill() + ", die=" + this.getDie() + ")";
    }
}
