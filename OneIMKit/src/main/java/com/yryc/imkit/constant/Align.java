package com.yryc.imkit.constant;

/**
 * @author : Mai_Xiao_Peng
 * @email : Mai_Xiao_Peng@163.com
 * @time : 2018/11/30 16:25
 * @describe :图片对齐方式
 */

public enum Align {

    LEFT(2, "left", ""),
    RIGHT(4, "right", ""),
    TOP(1, "top", ""),
    BOTTOM(5, "bottom", ""),
    CENTER(3, "center", "");

    private int code;
    private String type;
    private String remark;

    Align(int code, String type, String remark) {
        this.code = code;
        this.type = type;
        this.remark = remark;
    }

    public int getCode() {
        return code;
    }

    public String getType() {
        return type;
    }

    public String getRemark() {
        return remark;
    }
}
