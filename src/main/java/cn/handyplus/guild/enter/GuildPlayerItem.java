package cn.handyplus.guild.enter;

import cn.handyplus.guild.lib.IndexEnum;
import cn.handyplus.guild.lib.TableField;
import cn.handyplus.guild.lib.TableName;
import java.util.Date;
import java.util.UUID;
import lombok.Generated;

@TableName(value="guild_player_item", comment="\u516c\u4f1a\u6218\u73a9\u5bb6\u7269\u54c1\u8bb0\u5f55")
public class GuildPlayerItem {
    @TableField(value="id", comment="ID")
    private Integer id;
    @TableField(value="player_name", comment="\u73a9\u5bb6\u540d\u79f0", notNull=true)
    private String playerName;
    @TableField(value="player_uuid", comment="\u73a9\u5bb6uuid", notNull=true, indexEnum=IndexEnum.INDEX)
    private UUID playerUuid;
    @TableField(value="item_stack", comment="\u81ea\u5b9a\u4e49\u7269\u54c1", length=20000, notNull=true)
    private String itemStack;
    @TableField(value="create_time", comment="\u521b\u5efa\u65f6\u95f4")
    private Date createTime;

    @Generated
    public GuildPlayerItem() {
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
    public String getItemStack() {
        return this.itemStack;
    }

    @Generated
    public Date getCreateTime() {
        return this.createTime;
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
    public void setItemStack(String itemStack) {
        this.itemStack = itemStack;
    }

    @Generated
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Generated
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof GuildPlayerItem)) {
            return false;
        }
        GuildPlayerItem otherGuild = (GuildPlayerItem)other;
        if (!otherGuild.canEqual(this)) {
            return false;
        }
        Integer thisId = this.getId();
        Integer otherId = otherGuild.getId();
        if (thisId == null ? otherId != null : !((Object)thisId).equals(otherId)) {
            return false;
        }
        String thisPlayerName = this.getPlayerName();
        String otherPlayerName = otherGuild.getPlayerName();
        if (thisPlayerName == null ? otherPlayerName != null : !thisPlayerName.equals(otherPlayerName)) {
            return false;
        }
        UUID thisPlayerUuid = this.getPlayerUuid();
        UUID otherPlayerUuid = otherGuild.getPlayerUuid();
        if (thisPlayerUuid == null ? otherPlayerUuid != null : !((Object)thisPlayerUuid).equals(otherPlayerUuid)) {
            return false;
        }
        String thisItemStack = this.getItemStack();
        String otherItemStack = otherGuild.getItemStack();
        if (thisItemStack == null ? otherItemStack != null : !thisItemStack.equals(otherItemStack)) {
            return false;
        }
        Date thisCreateTime = this.getCreateTime();
        Date otherCreateTime = otherGuild.getCreateTime();
        return !(thisCreateTime == null ? otherCreateTime != null : !((Object)thisCreateTime).equals(otherCreateTime));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof GuildPlayerItem;
    }

    @Generated
    public int hashCode() {
        int prime = 59;
        int hash = 1;
        Integer idValue = this.getId();
        hash = hash * 59 + (idValue == null ? 43 : ((Object)idValue).hashCode());
        String playerNameValue = this.getPlayerName();
        hash = hash * 59 + (playerNameValue == null ? 43 : playerNameValue.hashCode());
        UUID playerUuidValue = this.getPlayerUuid();
        hash = hash * 59 + (playerUuidValue == null ? 43 : ((Object)playerUuidValue).hashCode());
        String itemStackValue = this.getItemStack();
        hash = hash * 59 + (itemStackValue == null ? 43 : itemStackValue.hashCode());
        Date createTimeValue = this.getCreateTime();
        hash = hash * 59 + (createTimeValue == null ? 43 : ((Object)createTimeValue).hashCode());
        return hash;
    }

    @Generated
    public String toString() {
        return "GuildPlayerItem(id=" + this.getId() + ", playerName=" + this.getPlayerName() + ", playerUuid=" + this.getPlayerUuid() + ", itemStack=" + this.getItemStack() + ", createTime=" + this.getCreateTime() + ")";
    }
}
