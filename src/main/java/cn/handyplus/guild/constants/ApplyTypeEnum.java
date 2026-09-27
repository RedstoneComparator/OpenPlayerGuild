package cn.handyplus.guild.constants;

import lombok.Generated;

public enum ApplyTypeEnum {
    PENDING(Integer.valueOf(1), "待审批"),
    AGREE(Integer.valueOf(2), "同意"),
    REFUSE(Integer.valueOf(3), "拒绝"),
    CANCEL(Integer.valueOf(4), "取消");

    private final String applyName;
    private final Integer applyId;

    @Generated
    ApplyTypeEnum(Integer id, String label) {
        this.applyId = id;
        this.applyName = label;
    }

    @Generated
    public Integer getId() {
        return this.applyId;
    }

    @Generated
    public String getName() {
        return this.applyName;
    }
}
