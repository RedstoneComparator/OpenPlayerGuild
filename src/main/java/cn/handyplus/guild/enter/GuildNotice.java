package cn.handyplus.guild.enter;

import cn.handyplus.guild.lib.IndexEnum;
import cn.handyplus.guild.lib.TableField;
import cn.handyplus.guild.lib.TableName;
import java.util.Date;
import java.util.UUID;
import lombok.Generated;

@TableName(value="guild_notice", comment="\u516c\u4f1a\u516c\u544a")
public class GuildNotice {
    @TableField(value="id", comment="ID")
    private Integer id;
    @TableField(value="guild_info_id", comment="\u516c\u4f1aid", notNull=true, indexEnum=IndexEnum.INDEX)
    private Integer guildInfoId;
    @TableField(value="guild_info_name", comment="\u516c\u4f1a\u540d\u79f0")
    private String guildInfoName;
    @TableField(value="title", comment="\u516c\u544a\u6d88\u606f\u6807\u9898", notNull=true)
    private String title;
    @TableField(value="content", comment="\u516c\u544a\u6d88\u606f\u5185\u5bb9", length=256, notNull=true)
    private String content;
    @TableField(value="send_player_name", comment="\u53d1\u9001\u73a9\u5bb6\u540d\u79f0", notNull=true)
    private String sendPlayerName;
    @TableField(value="send_player_uuid", comment="\u53d1\u9001\u73a9\u5bb6uuid", notNull=true)
    private UUID sendPlayerUuid;
    @TableField(value="send_player_role_id", comment="\u53d1\u9001\u73a9\u5bb6\u89d2\u8272", notNull=true, fieldDefault="1")
    private Integer sendPlayerRoleId;
    @TableField(value="create_time", comment="\u521b\u5efa\u65f6\u95f4")
    private Date createTime;

    @Generated
    public Integer getId() {
        return this.id;
    }

    @Generated
    public Integer getGuildInfoId() {
        return this.guildInfoId;
    }

    @Generated
    public String getGuildInfoName() {
        return this.guildInfoName;
    }

    @Generated
    public String getTitle() {
        return this.title;
    }

    @Generated
    public String getContent() {
        return this.content;
    }

    @Generated
    public String getSendPlayerName() {
        return this.sendPlayerName;
    }

    @Generated
    public UUID getSendPlayerUuid() {
        return this.sendPlayerUuid;
    }

    @Generated
    public Integer getSendPlayerRoleId() {
        return this.sendPlayerRoleId;
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
    public void setGuildInfoId(Integer guildInfoId) {
        this.guildInfoId = guildInfoId;
    }

    @Generated
    public void setGuildInfoName(String guildInfoName) {
        this.guildInfoName = guildInfoName;
    }

    @Generated
    public void setTitle(String title) {
        this.title = title;
    }

    @Generated
    public void setContent(String content) {
        this.content = content;
    }

    @Generated
    public void setSendPlayerName(String sendPlayerName) {
        this.sendPlayerName = sendPlayerName;
    }

    @Generated
    public void setSendPlayerUuid(UUID sendPlayerUuid) {
        this.sendPlayerUuid = sendPlayerUuid;
    }

    @Generated
    public void setSendPlayerRoleId(Integer sendPlayerRoleId) {
        this.sendPlayerRoleId = sendPlayerRoleId;
    }

    @Generated
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
