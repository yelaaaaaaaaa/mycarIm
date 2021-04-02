package com.yryc.imlib.info.io.messageTypeDetailInfo;

public class ParamsDataInfo {
    private String orderNo;     //订单号

    private String version;     //报价版本

    private int merchantId;     //

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderNo() {
        return this.orderNo;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getVersion() {
        return this.version;
    }

    public void setMerchantId(int merchantId) {
        this.merchantId = merchantId;
    }

    public int getMerchantId() {
        return this.merchantId;
    }
}
