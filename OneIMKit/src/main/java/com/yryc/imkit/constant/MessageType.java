package com.yryc.imkit.constant;

/**
 * @author : Mai_Xiao_Peng
 * @email : Mai_Xiao_Peng@163.com
 * @time : 2018/11/30 16:25
 * @describe :消息类型
 */


public enum MessageType {

    TEXT(1, "text", ""),
    IMAGE(2, "image", ""),
    VOICE(3, "audio", ""),
    VIDEO(4, "video", ""),
    SYSTEM(5, "system", ""),
    ORDER(6, "order", ""),
    CAR(7, "car", ""),
    RICH(8, "rich", ""),
    LOCATION(9,"location","");

    private Integer code;
    private String type;
    private String remark;

    MessageType(Integer code, String type, String remark) {
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
