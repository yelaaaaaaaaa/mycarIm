package com.yryc.imlib.info.io.messageTypeDetailInfo;

/**
 * 基本数据类型
 */

public class MessageBodyBaseDataInfo {
    private String type;        //text | image | voice |order商家报价消息|car 新车二手车 车辆信息 |system
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }
}
