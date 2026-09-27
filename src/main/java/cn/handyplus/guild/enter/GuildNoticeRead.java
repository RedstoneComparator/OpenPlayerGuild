package cn.handyplus.guild.enter;

import cn.handyplus.guild.lib.IndexEnum;
import cn.handyplus.guild.lib.TableField;
import cn.handyplus.guild.lib.TableName;
import java.util.Date;
import java.util.UUID;
import lombok.Generated;

@TableName(value="guild_notice_read", comment="\u516c\u4f1a\u5df2\u8bfb\u516c\u544a")
public class GuildNoticeRead {
    @TableField(value="id", comment="ID")
    private Integer id;
    @TableField(value="guild_info_id", comment="\u516c\u4f1aid", notNull=true, indexEnum=IndexEnum.INDEX)
    private Integer guildInfoId;
    @TableField(value="guild_notice_id", comment="\u516c\u544aId", notNull=true)
    private Integer guildNoticeId;
    @TableField(value="read_player_name", comment="\u5df2\u8bfb\u73a9\u5bb6\u540d\u79f0", notNull=true)
    private String readPlayerName;
    @TableField(value="read_player_uuid", comment="\u5df2\u8bfb\u73a9\u5bb6uuid", notNull=true)
    private UUID readPlayerUuid;
    @TableField(value="read_time", comment="\u5df2\u8bfb\u65f6\u95f4")
    private Date readTime;

    @Generated
    public Integer getId() {
        return this.id;
    }

    @Generated
    public Integer getGuildInfoId() {
        return this.guildInfoId;
    }

    @Generated
    public Integer getGuildNoticeId() {
        return this.guildNoticeId;
    }

    @Generated
    public String getReadPlayerName() {
        return this.readPlayerName;
    }

    @Generated
    public UUID getReadPlayerUuid() {
        return this.readPlayerUuid;
    }

    @Generated
    public Date getReadTime() {
        return this.readTime;
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
    public void setGuildNoticeId(Integer guildNoticeId) {
        this.guildNoticeId = guildNoticeId;
    }

    @Generated
    public void setReadPlayerName(String readPlayerName) {
        this.readPlayerName = readPlayerName;
    }

    @Generated
    public void setReadPlayerUuid(UUID readPlayerUuid) {
        this.readPlayerUuid = readPlayerUuid;
    }

    @Generated
    public void setReadTime(Date readTime) {
        this.readTime = readTime;
    }
}
