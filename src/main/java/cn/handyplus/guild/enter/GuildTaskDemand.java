package cn.handyplus.guild.enter;

import cn.handyplus.guild.lib.IndexEnum;
import cn.handyplus.guild.lib.TableField;
import cn.handyplus.guild.lib.TableName;
import java.util.Date;
import lombok.Generated;

@TableName(value="guild_task_demand", comment="\u516c\u4f1a\u4efb\u52a1\u76ee\u6807\u8fdb\u5ea6")
public class GuildTaskDemand {
    @TableField(value="id", comment="ID")
    private Integer id;
    @TableField(value="guild_info_id", comment="\u516c\u4f1aid", notNull=true, indexEnum=IndexEnum.INDEX)
    private Integer guildInfoId;
    @TableField(value="task_id", comment="\u4efb\u52a1id", notNull=true)
    private Integer taskId;
    @TableField(value="guild_task_id", comment="\u516c\u4f1a\u4efb\u52a1id", notNull=true, indexEnum=IndexEnum.INDEX)
    private Integer guildTaskId;
    @TableField(value="type", comment="\u7c7b\u578b", length=32, notNull=true)
    private String type;
    @TableField(value="completion_amount", comment="\u5b8c\u6210\u8fc7\u7684\u6570\u91cf", notNull=true)
    private Integer completionAmount;
    @TableField(value="amount", comment="\u6570\u91cf", notNull=true)
    private Integer amount;
    @TableField(value="item_stack", comment="\u81ea\u5b9a\u4e49\u7269\u54c1", length=20000, notNull=true)
    private String itemStack;
    @TableField(value="start_date", comment="\u5f00\u59cb\u65f6\u95f4", notNull=true)
    private Date startDate;
    @TableField(value="end_date", comment="\u7ed3\u675f\u65f6\u95f4", notNull=true)
    private Date endDate;

    @Generated
    public GuildTaskDemand() {
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
    public Integer getGuildTaskId() {
        return this.guildTaskId;
    }

    @Generated
    public String getType() {
        return this.type;
    }

    @Generated
    public Integer getCompletionAmount() {
        return this.completionAmount;
    }

    @Generated
    public Integer getAmount() {
        return this.amount;
    }

    @Generated
    public String getItemStack() {
        return this.itemStack;
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
    public void setGuildTaskId(Integer guildTaskId) {
        this.guildTaskId = guildTaskId;
    }

    @Generated
    public void setType(String type) {
        this.type = type;
    }

    @Generated
    public void setCompletionAmount(Integer completionAmount) {
        this.completionAmount = completionAmount;
    }

    @Generated
    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    @Generated
    public void setItemStack(String itemStack) {
        this.itemStack = itemStack;
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
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof GuildTaskDemand)) {
            return false;
        }
        GuildTaskDemand otherGuild = (GuildTaskDemand)other;
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
        Integer thisGuildTaskId = this.getGuildTaskId();
        Integer otherGuildTaskId = otherGuild.getGuildTaskId();
        if (thisGuildTaskId == null ? otherGuildTaskId != null : !((Object)thisGuildTaskId).equals(otherGuildTaskId)) {
            return false;
        }
        Integer thisCompletionAmount = this.getCompletionAmount();
        Integer otherCompletionAmount = otherGuild.getCompletionAmount();
        if (thisCompletionAmount == null ? otherCompletionAmount != null : !((Object)thisCompletionAmount).equals(otherCompletionAmount)) {
            return false;
        }
        Integer thisAmount = this.getAmount();
        Integer otherAmount = otherGuild.getAmount();
        if (thisAmount == null ? otherAmount != null : !((Object)thisAmount).equals(otherAmount)) {
            return false;
        }
        String thisType = this.getType();
        String otherType = otherGuild.getType();
        if (thisType == null ? otherType != null : !thisType.equals(otherType)) {
            return false;
        }
        String thisItemStack = this.getItemStack();
        String otherItemStack = otherGuild.getItemStack();
        if (thisItemStack == null ? otherItemStack != null : !thisItemStack.equals(otherItemStack)) {
            return false;
        }
        Date thisStartDate = this.getStartDate();
        Date otherStartDate = otherGuild.getStartDate();
        if (thisStartDate == null ? otherStartDate != null : !((Object)thisStartDate).equals(otherStartDate)) {
            return false;
        }
        Date thisEndDate = this.getEndDate();
        Date otherEndDate = otherGuild.getEndDate();
        return !(thisEndDate == null ? otherEndDate != null : !((Object)thisEndDate).equals(otherEndDate));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof GuildTaskDemand;
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
        Integer guildTaskIdValue = this.getGuildTaskId();
        hash = hash * 59 + (guildTaskIdValue == null ? 43 : ((Object)guildTaskIdValue).hashCode());
        Integer completionAmountValue = this.getCompletionAmount();
        hash = hash * 59 + (completionAmountValue == null ? 43 : ((Object)completionAmountValue).hashCode());
        Integer amountValue = this.getAmount();
        hash = hash * 59 + (amountValue == null ? 43 : ((Object)amountValue).hashCode());
        String typeValue = this.getType();
        hash = hash * 59 + (typeValue == null ? 43 : typeValue.hashCode());
        String itemStackValue = this.getItemStack();
        hash = hash * 59 + (itemStackValue == null ? 43 : itemStackValue.hashCode());
        Date startDateValue = this.getStartDate();
        hash = hash * 59 + (startDateValue == null ? 43 : ((Object)startDateValue).hashCode());
        Date endDateValue = this.getEndDate();
        hash = hash * 59 + (endDateValue == null ? 43 : ((Object)endDateValue).hashCode());
        return hash;
    }

    @Generated
    public String toString() {
        return "GuildTaskDemand(id=" + this.getId() + ", guildInfoId=" + this.getGuildInfoId() + ", taskId=" + this.getTaskId() + ", guildTaskId=" + this.getGuildTaskId() + ", type=" + this.getType() + ", completionAmount=" + this.getCompletionAmount() + ", amount=" + this.getAmount() + ", itemStack=" + this.getItemStack() + ", startDate=" + this.getStartDate() + ", endDate=" + this.getEndDate() + ")";
    }
}
