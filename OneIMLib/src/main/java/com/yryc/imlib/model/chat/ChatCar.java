package com.yryc.imlib.model.chat;

import com.google.gson.Gson;
import com.yryc.imlib.info.GeneralMessageDataInfo;
import com.yryc.imlib.info.GroupMessageDataInfo;

/**
 * @author : Mai_Xiao_Peng
 * @email : Mai_Xiao_Peng@163.com
 * @time : 2018/12/11 19:28
 * @describe : 新车二手车
 */


public class ChatCar extends ChatBody {

    private ChatCar.Body body;

    public ChatCar.Body getBody() {
        return body;
    }

    public void setBody(ChatCar.Body body) {
        this.body = body;
    }

    public static class Body {


        /**
         * title : 凯美瑞
         * content : 17万 - 25万
         * open_url : http://baidu.com
         * img_url : 车的图片
         * type : car
         */

        private String title;
        private String content;
        private String open_url;
        private String img_url;
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

        public String getOpen_url() {
            return open_url;
        }

        public void setOpen_url(String open_url) {
            this.open_url = open_url;
        }

        public String getImg_url() {
            return img_url;
        }

        public void setImg_url(String img_url) {
            this.img_url = img_url;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
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

        public ChatCar build() {
            ChatCar chatCar;
            if (isGroup) {
                chatCar = new ChatBody.Builder(groupMessageDataInfo).build(new ChatCar());
            } else {
                chatCar = new ChatBody.Builder(generalMessageDataInfo).build(new ChatCar());
            }
            chatCar.body = new Gson().fromJson(chatCar.json, ChatCar.Body.class);
            return chatCar;
        }
    }
}
