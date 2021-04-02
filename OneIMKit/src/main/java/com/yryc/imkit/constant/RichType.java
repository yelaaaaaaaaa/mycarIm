package com.yryc.imkit.constant;

/**
 * @author : Mai_Xiao_Peng
 * @email : Mai_Xiao_Peng@163.com
 * @time : 2018/11/30 16:25
 * @describe :图片对齐方式
 */

public enum RichType {
    NONE(0, "none", ""),
    SCHEME(3, "scheme", ""),
    API(2, "api", ""),
    H5(1, "h5", "");

    private Integer code;
    private String type;
    private String remark;

    RichType(Integer code, String type, String remark) {
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
