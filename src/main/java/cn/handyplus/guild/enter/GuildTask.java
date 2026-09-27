package cn.handyplus.guild.enter;

import cn.handyplus.guild.enter.GuildTaskDemand;
import cn.handyplus.guild.lib.IndexEnum;
import cn.handyplus.guild.lib.TableField;
import cn.handyplus.guild.lib.TableName;
import java.util.Date;
import java.util.List;
import lombok.Generated;

@TableName(value="guild_task", comment="\u516c\u4f1a\u4efb\u52a1")
public class GuildTask {
    @TableField(value="id", comment="ID")
    private Integer id;
    @TableField(value="guild_info_id", comment="\u516c\u4f1aid", notNull=true, indexEnum=IndexEnum.INDEX)
    private Integer guildInfoId;
    @TableField(value="task_id", comment="\u4efb\u52a1id", notNull=true)
    private Integer taskId;
    @TableField(value="task_name", comment="\u4efb\u52a1\u540d\u79f0", notNull=true)
    private String taskName;
    @TableField(value="status", comment="\u4efb\u52a1\u662f\u5426\u5b8c\u6210 false \u672a\u5b8c\u6210\uff0ctrue \u5df2\u5b8c\u6210", notNull=true)
    private Boolean status;
    @TableField(value="demand_success", comment="\u76ee\u6807\u662f\u5426\u5b8c\u6210 false \u672a\u5b8c\u6210\uff0ctrue \u5df2\u5b8c\u6210", notNull=true)
    private Boolean demandSuccess;
    @TableField(value="start_date", comment="\u5f00\u59cb\u65f6\u95f4", notNull=true)
    private Date startDate;
    @TableField(value="end_date", comment="\u7ed3\u675f\u65f6\u95f4", notNull=true)
    private Date endDate;
    private List<GuildTaskDemand> guildTaskDemandList;

    @Generated
    public GuildTask() {
    }

    @Generated
    public Integer getId() {
        return this.id;
    }

    @Generated
    public Integer getGuildInfoId() {
        return this.guildInfoId;
    }

    @Generated
    public Integer getTaskId() {
        return this.taskId;
    }

    @Generated
    public String getTaskName() {
        return this.taskName;
    }

    @Generated
    public Boolean getStatus() {
        return this.status;
    }

    @Generated
    public Boolean getDemandSuccess() {
        return this.demandSuccess;
    }

    @Generated
    public Date getStartDate() {
        return this.startDate;
    }

    @Generated
    public Date getEndDate() {
        return this.endDate;
    }

    @Generated
    public List<GuildTaskDemand> getGuildTaskDemandList() {
        return this.guildTaskDemandList;
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
    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    @Generated
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    @Generated
    public void setStatus(Boolean status) {
        this.status = status;
    }

    @Generated
    public void setDemandSuccess(Boolean demandSuccess) {
        this.demandSuccess = demandSuccess;
    }

    @Generated
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @Generated
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Generated
    public void setGuildTaskDemandList(List<GuildTaskDemand> guildTaskDemandList) {
        this.guildTaskDemandList = guildTaskDemandList;
    }

    @Generated
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof GuildTask)) {
            return false;
        }
        GuildTask otherGuild = (GuildTask)other;
        if (!otherGuild.canEqual(this)) {
            return false;
        }
        Integer thisId = this.getId();
        Integer otherId = otherGuild.getId();
        if (thisId == null ? otherId != null : !((Object)thisId).equals(otherId)) {
            return false;
        }
        Integer thisGuildInfoId = this.getGuildInfoId();
        Integer otherGuildInfoId = otherGuild.getGuildInfoId();
        if (thisGuildInfoId == null ? otherGuildInfoId != null : !((Object)thisGuildInfoId).equals(otherGuildInfoId)) {
            return false;
        }
        Integer thisTaskId = this.getTaskId();
        Integer otherTaskId = otherGuild.getTaskId();
        if (thisTaskId == null ? otherTaskId != null : !((Object)thisTaskId).equals(otherTaskId)) {
            return false;
        }
        Boolean thisStatus = this.getStatus();
        Boolean otherStatus = otherGuild.getStatus();
        if (thisStatus == null ? otherStatus != null : !((Object)thisStatus).equals(otherStatus)) {
            return false;
        }
        Boolean thisDemandSuccess = this.getDemandSuccess();
        Boolean otherDemandSuccess = otherGuild.getDemandSuccess();
        if (thisDemandSuccess == null ? otherDemandSuccess != null : !((Object)thisDemandSuccess).equals(otherDemandSuccess)) {
            return false;
        }
        String thisTaskName = this.getTaskName();
        String otherTaskName = otherGuild.getTaskName();
        if (thisTaskName == null ? otherTaskName != null : !thisTaskName.equals(otherTaskName)) {
            return false;
        }
        Date thisStartDate = this.getStartDate();
        Date otherStartDate = otherGuild.getStartDate();
        if (thisStartDate == null ? otherStartDate != null : !((Object)thisStartDate).equals(otherStartDate)) {
            return false;
        }
        Date thisEndDate = this.getEndDate();
        Date otherEndDate = otherGuild.getEndDate();
        if (thisEndDate == null ? otherEndDate != null : !((Object)thisEndDate).equals(otherEndDate)) {
            return false;
        }
        List<GuildTaskDemand> thisGuildTaskDemandList = this.getGuildTaskDemandList();
        List<GuildTaskDemand> otherGuildTaskDemandList = otherGuild.getGuildTaskDemandList();
        return !(thisGuildTaskDemandList == null ? otherGuildTaskDemandList != null : !((Object)thisGuildTaskDemandList).equals(otherGuildTaskDemandList));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof GuildTask;
    }

    @Generated
    public int hashCode() {
        int prime = 59;
        int hash = 1;
        Integer idValue = this.getId();
        hash = hash * 59 + (idValue == null ? 43 : ((Object)idValue).hashCode());
        Integer guildInfoIdValue = this.getGuildInfoId();
        hash = hash * 59 + (guildInfoIdValue == null ? 43 : ((Object)guildInfoIdValue).hashCode());
        Integer taskIdValue = this.getTaskId();
        hash = hash * 59 + (taskIdValue == null ? 43 : ((Object)taskIdValue).hashCode());
        Boolean statusValue = this.getStatus();
        hash = hash * 59 + (statusValue == null ? 43 : ((Object)statusValue).hashCode());
        Boolean demandSuccessValue = this.getDemandSuccess();
        hash = hash * 59 + (demandSuccessValue == null ? 43 : ((Object)demandSuccessValue).hashCode());
        String taskNameValue = this.getTaskName();
        hash = hash * 59 + (taskNameValue == null ? 43 : taskNameValue.hashCode());
        Date startDateValue = this.getStartDate();
        hash = hash * 59 + (startDateValue == null ? 43 : ((Object)startDateValue).hashCode());
        Date endDateValue = this.getEndDate();
        hash = hash * 59 + (endDateValue == null ? 43 : ((Object)endDateValue).hashCode());
        List<GuildTaskDemand> guildTaskDemandListValue = this.getGuildTaskDemandList();
        hash = hash * 59 + (guildTaskDemandListValue == null ? 43 : ((Object)guildTaskDemandListValue).hashCode());
        return hash;
    }

    @Generated
    public String toString() {
        return "GuildTask(id=" + this.getId() + ", guildInfoId=" + this.getGuildInfoId() + ", taskId=" + this.getTaskId() + ", taskName=" + this.getTaskName() + ", status=" + this.getStatus() + ", demandSuccess=" + this.getDemandSuccess() + ", startDate=" + this.getStartDate() + ", endDate=" + this.getEndDate() + ", guildTaskDemandList=" + this.getGuildTaskDemandList() + ")";
    }
}
