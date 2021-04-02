package com.yryc.imlib.model.chat;

import com.google.gson.Gson;
import com.yryc.imlib.info.GeneralMessageDataInfo;
import com.yryc.imlib.info.GroupMessageDataInfo;

/**
 * @author : Mai_Xiao_Peng
 * @email : Mai_Xiao_Peng@163.com
 * @time : 2018/12/4 10:50
 * @describe :视频
 */

public class ChatVideo extends ChatBody {

    private Body body;

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public static class Body {

        /**
         * type : video
         * content : content
         * size : {"width":100,"height":120}
         * cover : xxxx
         */

        private String type;
        private String content;
        private SizeBean size;
        private String cover;
        private int length;

        public int getLength() {
            return length;
        }

        public void setLength(int length) {
            this.length = length;
        }

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

        public SizeBean getSize() {
            return size;
        }

        public void setSize(SizeBean size) {
            this.size = size;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public static class SizeBean {
            /**
             * width : 100
             * height : 120
             */

            private int width;
            private int height;

            public SizeBean(int width, int height) {
                this.width = width;
                this.height = height;
            }

            public int getWidth() {
                return width;
            }

            public void setWidth(int width) {
                this.width = width;
            }

            public int getHeight() {
                return height;
            }

            public void setHeight(int height) {
                this.height = height;
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

        public ChatVideo build() {
            ChatVideo chatVideo;
            if (isGroup) {
                chatVideo = new ChatBody.Builder(groupMessageDataInfo).build(new ChatVideo());
            } else {
                chatVideo = new ChatBody.Builder(generalMessageDataInfo).build(new ChatVideo());
            }
            chatVideo.body = new Gson().fromJson(chatVideo.json, Body.class);
            return chatVideo;
        }
    }

}
