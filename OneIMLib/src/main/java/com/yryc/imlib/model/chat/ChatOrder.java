package com.yryc.imlib.model.chat;

import com.google.gson.Gson;
import com.yryc.imlib.info.GeneralMessageDataInfo;
import com.yryc.imlib.info.GroupMessageDataInfo;

/**
 * @author : Mai_Xiao_Peng
 * @email : Mai_Xiao_Peng@163.com
 * @time : 2018/12/11 19:28
 * @describe : 订单
 */


public class ChatOrder extends ChatBody {

    private Body body;

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public static class Body {

        /**
         * title : 订单报价信息
         * content : 服务地址：福建省福州市晋安区东二环泰禾 广场广场广场广场广场
         * 服务时间：2018-01-01 12:25
         * 服务项目：
         * 普通洗车     X1     ￥30.00
         * 美孚金装1号机油保养(多余机油升数及机油 及机油及机油     X1     ￥30.00
         * footer : 合计：￥428.00，点击查看
         * open_url : yryc://order/noginx/order_no
         * params : {"orderNo":"订单号","version":"报价版本","merchantId":11}
         * type : order
         */

        private String title;
        private String content;
        private String footer;
        private String open_url;
        private ParamsBean params;
        private String type;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
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

        public ParamsBean getParams() {
            return params;
        }

        public void setParams(ParamsBean params) {
            this.params = params;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public static class ParamsBean {
            /**
             * orderNo : 订单号
             * version : 报价版本
             * merchantId : 11
             */

            private String orderNo;
            private String version;
            private long merchantId;
            private long orderCategory;

            public long getOrderCategory() {
                return orderCategory;
            }

            public void setOrderCategory(long orderCategory) {
                this.orderCategory = orderCategory;
            }

            public String getOrderNo() {
                return orderNo;
            }

            public void setOrderNo(String orderNo) {
                this.orderNo = orderNo;
            }

            public String getVersion() {
                return version;
            }

            public void setVersion(String version) {
                this.version = version;
            }

            public long getMerchantId() {
                return merchantId;
            }

            public void setMerchantId(long merchantId) {
                this.merchantId = merchantId;
            }
        }
    }

    public static class Builder {
        private GeneralMessageDataInfo generalMessageDataInfo;
        private GroupMessageDataInfo groupMessageDataInfo;
        private boolean isGroup;

        public Builder(GeneralMessageDataInfo generalMessageDataInfo) {
            this.generalMessageDataInfo = generalMessageDataInfo;
            isGroup = false;
        }

        public Builder(GroupMessageDataInfo groupMessageDataInfo) {
            this.groupMessageDataInfo = groupMessageDataInfo;
            isGroup = true;
        }

        public ChatOrder build() {
            ChatOrder chatOrder;
            if (isGroup) {
                chatOrder = new ChatBody.Builder(groupMessageDataInfo).build(new ChatOrder());
            } else {
                chatOrder = new ChatBody.Builder(generalMessageDataInfo).build(new ChatOrder());
            }
            chatOrder.body = new Gson().fromJson(chatOrder.json, Body.class);
            return chatOrder;
        }
    }

}
