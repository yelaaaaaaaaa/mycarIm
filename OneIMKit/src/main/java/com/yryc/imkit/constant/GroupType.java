package com.yryc.imkit.constant;

/**
 * @author : Mai_Xiao_Peng
 * @email : Mai_Xiao_Peng@163.com
 * @time : 2018/11/30 16:25
 * @describe :消息类型
 */


public enum GroupType {
    PERSONAL(1, "p", "个人信息"),
    MEMBER(2, "m", "群信息");

    private Integer code;
    private String type;
    private String remark;

    GroupType(Integer code, String type, String remark) {
        this.code = code;
        this.type = type;
        this.remark = remark;
    }

    public Integer getCode() {
        return code;
    }

    public String getType() {
        return type;
    }

    public String getRemark() {
        return remark;
    }
}
