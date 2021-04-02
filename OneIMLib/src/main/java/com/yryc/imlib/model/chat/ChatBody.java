package com.yryc.imlib.model.chat;

import com.yryc.imlib.info.GeneralMessageDataInfo;
import com.yryc.imlib.info.GroupMessageDataInfo;
import com.yryc.imlib.info.ReceiptTypeEnum;

/**
 * @author : Mai_Xiao_Peng
 * @email : Mai_Xiao_Peng@163.com
 * @time : 2018/12/4 15:13
 * @describe : 消息体
 */


public class ChatBody {

    protected String messageId;         //消息id
    protected Long sentTime;           //发送时间
    protected String whoSend;                //谁发的OWNER_100
    protected String whoReceive;                //发给谁OWNER_100
    //from
    protected String avatarUrl;        //对方的头像
    protected String fromName;                  //对方名称
    protected String fromUid;               //OWNER_100
    //body
    protected String type;              //消息类型
    protected String json;      //消息体的json

    protected boolean haveRead;     //已读

    private GeneralMessageDataInfo generalMessageDataInfo;

    private GroupMessageDataInfo groupMessageDataInfo;

    public GeneralMessageDataInfo getGeneralMessageDataInfo() {
        return generalMessageDataInfo;
    }

    public void setGeneralMessageDataInfo(GeneralMessageDataInfo generalMessageDataInfo) {
        this.generalMessageDataInfo = generalMessageDataInfo;
    }

    public GroupMessageDataInfo getGroupMessageDataInfo() {
        return groupMessageDataInfo;
    }

    public void setGroupMessageDataInfo(GroupMessageDataInfo groupMessageDataInfo) {
        this.groupMessageDataInfo = groupMessageDataInfo;
    }

    public boolean isHaveRead() {
        return haveRead;
    }

    public void setHaveRead(boolean haveRead) {
        this.haveRead = haveRead;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public Long getSentTime() {
        return sentTime;
    }

    public void setSentTime(Long sentTime) {
        this.sentTime = sentTime;
    }

    public String getWhoSend() {
        return whoSend;
    }

    public void setWhoSend(String whoSend) {
        this.whoSend = whoSend;
    }

    public String getWhoReceive() {
        return whoReceive;
    }

    public void setWhoReceive(String whoReceive) {
        this.whoReceive = whoReceive;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getFromUid() {
        return fromUid;
    }

    public void setFromUid(String fromUid) {
        this.fromUid = fromUid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
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

        public <T extends ChatBody> T build(T t) {
            if (t instanceof ChatBody) {
                ChatBody chatBody = (ChatBody) t;
                if (isGroup) {
                    chatBody.messageId = groupMessageDataInfo.getMessageId();
                    chatBody.sentTime = groupMessageDataInfo.getSentTime();
                    chatBody.avatarUrl = groupMessageDataInfo.getAvatarUrl();
                    chatBody.fromName = groupMessageDataInfo.getFromName();
                    chatBody.fromUid = groupMessageDataInfo.getFromUserId();
                    chatBody.type = groupMessageDataInfo.getBodyType();
                    chatBody.json = groupMessageDataInfo.getJsonStringBody();
                    chatBody.groupMessageDataInfo = groupMessageDataInfo;
                    chatBody.setHaveRead(ReceiptTypeEnum.READ.getType().equals(groupMessageDataInfo.getReceiptType()));
                } else {
                    chatBody.messageId = generalMessageDataInfo.getMessageId();
                    chatBody.sentTime = generalMessageDataInfo.getSentTime();
                    chatBody.whoReceive = generalMessageDataInfo.getWhoReceive();
                    chatBody.whoSend = generalMessageDataInfo.getWhoSend();
                    chatBody.avatarUrl = generalMessageDataInfo.getAvatarUrl();
                    chatBody.fromName = generalMessageDataInfo.getFromName();
                    chatBody.fromUid = generalMessageDataInfo.getFromUid();
                    chatBody.type = generalMessageDataInfo.getBodyType();
                    chatBody.json = generalMessageDataInfo.getJsonStringBody();
                    chatBody.generalMessageDataInfo = generalMessageDataInfo;
                    chatBody.setHaveRead(ReceiptTypeEnum.READ.getType().equals(generalMessageDataInfo.getReceiptType()));
                }
                return (T) chatBody;
            } else {
                return t;
            }
        }
    }

}
