package com.yryc.imlib.info;

/**
 * @author : sklyand
 * @email :
 * @time : 2020/4/22 17:36
 * @describe ：
 */
public enum  ReceiptTypeEnum  {
    SEND("send"),SENDING("sending"),SENDFAIL("sendFail"),RECEIVED("received"),READ("read");
    private  String type;

     ReceiptTypeEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
