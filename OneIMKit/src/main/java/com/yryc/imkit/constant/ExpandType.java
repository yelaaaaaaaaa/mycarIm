package com.yryc.imkit.constant;

/**
 * @author : Mai_Xiao_Peng
 * @email : Mai_Xiao_Peng@163.com
 * @time : 2018/11/30 16:25
 * @describe :图片对齐方式
 */

public enum ExpandType {

    COPY(0, "复制", ""),
    DOWNLOAD_IMAGE(1, "下载图片", ""),
    VOICE_MODE_RECEIVER(2, "使用听筒模式", ""),
    VOICE_MODE_SPEAKER(3, "使用扬声器模式", "");


    private Integer code;
    private String type;
    private String remark;

    ExpandType(Integer code, String type, String remark) {
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
