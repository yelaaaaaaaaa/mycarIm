package com.yryc.imkit.im;

import android.content.Context;

import com.yryc.imkit.constant.Function;
import com.yryc.imkit.listener.OnImChatListEventListener;
import com.yryc.imkit.listener.OnImChatRichItemClickListener;
import com.yryc.imlib.client.MessageEvent;
import com.yryc.imlib.rx.RxEvent.EventType;

import org.greenrobot.eventbus.EventBus;

import java.util.Arrays;

/**
 * @author : Mai_Xiao_Peng
 * @email : Mai_Xiao_Peng@163.com
 * @time : 2019/1/2 16:12
 * @describe :
 */


public class IM {

    public static ImUtil sImUtils;

    public static class ImBuilder {

        private ImOptions imOptions;

        private Context context;

        public ImBuilder(Context context) {
            this.context = context;
            initOptions();
        }

        /**
         * 初始化配置
         */
        private void initOptions() {
            imOptions = new ImOptions();
            imOptions.debug = false;
        }

        /**
         * 设置appKey
         */
        public ImBuilder setAppKey(String appKey) {
            imOptions.appKey = appKey;
            return this;
        }

        /**
         * debug
         */
        public ImBuilder setDebug(boolean isDebug) {
            imOptions.debug = isDebug;
            return this;
        }

        /**
         * token
         *
         * @param token
         * @return
         */
        public ImBuilder setToken(String token) {
            imOptions.token = token;
            return this;
        }

        /**
         * deviceId
         *
         * @param deviceId
         * @return
         */
        public ImBuilder setDeviceId(String deviceId) {
            imOptions.deviceId = deviceId;
            return this;
        }

        /**
         * channelName
         *
         * @param channelName
         * @return
         */
        public ImBuilder setChannelName(String channelName) {
            imOptions.channelName = channelName;
            return this;
        }

        /**
         * 启用功能
         *
         * @param functions
         * @return
         */
        public ImBuilder setEnableFunctions(Function... functions) {
            imOptions.functions.addAll(Arrays.asList(functions));
            return this;
        }

        /**
         * 设置聊天列表点击监听
         *
         * @param onImChatListEventListener
         * @return
         */
        public ImBuilder setOnImChatListEventListener(OnImChatListEventListener onImChatListEventListener) {
            imOptions.onImChatListEventListener = onImChatListEventListener;
            return this;
        }

        /**
         * 设置聊天列表点击监听
         *
         * @param onImChatRichItemClickListener
         * @return
         */
        public ImBuilder setOnImChatRichItemClickListener(OnImChatRichItemClickListener onImChatRichItemClickListener) {
            imOptions.onImChatRichItemClickListener = onImChatRichItemClickListener;
            return this;
        }

        public ImUtil build() {
            sImUtils = new ImUtil(imOptions);
            imOptions.onImChatRichItemNetResponseListener = (view) ->
                    EventBus.getDefault().post(new MessageEvent(EventType.EVENT_NET_RESPONSE, view));
            //OIMClient.init(context, imOptions.appKey, imOptions.debug);
            return sImUtils;
        }

    }
}
