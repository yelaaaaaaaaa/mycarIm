package com.yryc.imlib.info.io.messageTypeDetailInfo;

/**
 * "title": "订单报价信息",
 *     "content": "服务地址：福建省福州市晋安区东二环泰禾 广场广场广场广场广场\n服务时间：2018-01-01 12:25\n服务项目：\n普通洗车     X1     ￥30.00\n美孚金装1号机油保养(多余机油升数及机油 及机油及机油     X1     ￥30.00",
 *     "footer": "合计：￥428.00，点击查看 ",
 *     "open_url": "yryc://order/noginx/order_no",
 *     "params": {
 *       "orderNo": "订单号",
 *       "version": "报价版本",
 *       "merchantId": 11
 *     },
 *     "type": "order"
 *
 * 订单报价信息
 *
 */
public class MerchantQuoteMessageDataInfo extends MessageBodyBaseDataInfo {

    private String title;
    private String footer;
    private String open_url;
    private ParamsDataInfo params;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFooter() {
        return footer;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }

    public String getOpen_url() {
        return open_url;
    }

    public void setOpen_url(String open_url) {
        this.open_url = open_url;
    }

    public ParamsDataInfo getParams() {
        return params;
    }

    public void setParams(ParamsDataInfo params) {
        this.params = params;
    }
}
