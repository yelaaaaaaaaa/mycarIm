package com.yryc.imkit.constant;

/**
 * @author : Mai_Xiao_Peng
 * @email : Mai_Xiao_Peng@163.com
 * @time : 2018/11/30 16:25
 * @describe :图片对齐方式
 */

public enum Orientation {

    HORIZONTAL(0, "horizontal", ""),
    VERTICAL(1, "vertical", "");

    private int code;
    private String type;
    private String remark;

    Orientation(int code, String type, String remark) {
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
