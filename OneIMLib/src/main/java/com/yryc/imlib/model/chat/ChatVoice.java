package com.yryc.imlib.model.chat;

import com.google.gson.Gson;
import com.yryc.imlib.info.GeneralMessageDataInfo;
import com.yryc.imlib.info.GroupMessageDataInfo;

/**
 * @author : Mai_Xiao_Peng
 * @email : Mai_Xiao_Peng@163.com
 * @time : 2018/12/4 10:50
 * @describe :语音
 */

public class ChatVoice extends ChatBody {

    private Body body;

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public static class Body {

        /**
         * type : audio
         * content : content
         * length : 10
         */

        private String type;
        private String content;
        private float length;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public float getLength() {
            return length;
        }

        public void setLength(float length) {
            this.length = length;
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

        public ChatVoice build() {
            ChatVoice chatVoice;
            if (isGroup) {
                chatVoice = new ChatBody.Builder(groupMessageDataInfo).build(new ChatVoice());
            } else {
                chatVoice = new ChatBody.Builder(generalMessageDataInfo).build(new ChatVoice());
            }
            chatVoice.body = new Gson().fromJson(chatVoice.json, Body.class);
            return chatVoice;
        }
    }

}
