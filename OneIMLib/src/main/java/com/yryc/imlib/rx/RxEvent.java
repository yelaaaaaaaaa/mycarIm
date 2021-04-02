package com.yryc.imlib.rx;



public class RxEvent {

    private int eventType;
    private Object data;

    public RxEvent(int eventType, Object data) {
        this.eventType = eventType;
        this.data = data;
    }

    /**
     * 事件类型
     */
    public static class EventType {

        /**
         * 文章列表页刷新
         */
        public static final int DISCOVERY_ARTICLE_LIST_REFRESH = 3000;

        /**
         * 退出登录
         */
        public static final int SYSTEM_LOGIN_OUT = 10001;

        /**
         * 弹出取消关注的dialog
         */
        public static final int OPEN_NATIVE_DIALOG = 10002;

        /**
         * 用户信息更新
         */
        public static final int USERINFO_UPDATE = 10003;
        
        /**
         * 发送消息
         */
        public final static int EVENT_SEND_MESSAGE = 0x1001;

        /**
         * 点击常用语
         */
        public final static int EVENT_SEND_LANGUAGE = 0x2001;

        /**
         * 网络结果响应
         */
        public final static int EVENT_NET_RESPONSE = 0x3001;

    }

    public int getEventType() {
        return eventType;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
