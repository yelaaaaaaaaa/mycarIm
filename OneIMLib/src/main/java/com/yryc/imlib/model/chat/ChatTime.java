package com.yryc.imlib.model.chat;

import com.yryc.imlib.info.GeneralMessageDataInfo;
import com.yryc.imlib.info.GroupMessageDataInfo;

/**
 * @author : Mai_Xiao_Peng
 * @email : Mai_Xiao_Peng@163.com
 * @time : 2018/12/11 19:28
 * @describe : 时间
 */


public class ChatTime extends ChatBody {


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

        public ChatTime build() {
            ChatTime chatTime;
            if (isGroup) {
                chatTime = new ChatBody.Builder(groupMessageDataInfo).build(new ChatTime());
            } else {
                chatTime = new ChatBody.Builder(generalMessageDataInfo).build(new ChatTime());
            }
            return chatTime;
        }
    }

}
