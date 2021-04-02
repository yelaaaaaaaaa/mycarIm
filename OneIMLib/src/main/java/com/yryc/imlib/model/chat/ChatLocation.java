package com.yryc.imlib.model.chat;

import com.google.gson.Gson;
import com.yryc.imlib.info.GeneralMessageDataInfo;
import com.yryc.imlib.info.GroupMessageDataInfo;
import com.yryc.imlib.info.io.Location;

/**
 * @author : Mai_Xiao_Peng
 * @email : Mai_Xiao_Peng@163.com
 * @time : 2018/12/4 10:50
 * @describe :地图
 */

public class ChatLocation extends ChatBody {

    private Body body;

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public static class Body {

        private String type;
        private String content;
        private String address;
        private Location location;

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

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public Location getLocation() {
            return location;
        }

        public void setLocation(Location location) {
            this.location = location;
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

        public ChatLocation build() {
            ChatLocation chatLocation;
            if (isGroup) {
                chatLocation = new ChatBody.Builder(groupMessageDataInfo).build(new ChatLocation());
            } else {
                chatLocation = new ChatBody.Builder(generalMessageDataInfo).build(new ChatLocation());
            }
            chatLocation.body = new Gson().fromJson(chatLocation.json, Body.class);
            return chatLocation;
        }
    }

}
