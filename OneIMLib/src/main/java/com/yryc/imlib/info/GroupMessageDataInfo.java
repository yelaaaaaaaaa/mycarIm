package com.yryc.imlib.info;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.yryc.imlib.info.io.MessageDataInfoBean;


/**
 * 储存的平层消息结构
 */
@Entity(tableName = "yryc_im_group_message_data")
public class GroupMessageDataInfo {
    @NonNull
    @PrimaryKey()
    @ColumnInfo(name = "conversation_message_id")
    private String messageId;         //消息id
    @ColumnInfo(name = "message_topic_id")
    private String topicId;         //会话id
    @ColumnInfo(name = "conversation_sent_time")
    private Long sentTime;           //发送时间
    //from
    @ColumnInfo(name = "conversation_from_avatar_url")
    private String avatarUrl;        //对方的头像
    @ColumnInfo(name = "conversation_from_name")
    private String fromName;                  //对方名称
    @ColumnInfo(name = "conversation_from_uid")
    private String fromUserId;               //OWNER_100
    @ColumnInfo(name = "conversation_to_uid")
    private String toUserId;

    @ColumnInfo(name = "conversation_file_path")
    private String filePath;   //储存路径

    @ColumnInfo(name = "conversation_media_status")
    private int mediaStatus;  // 消息多媒体状态 0为未播放过，1为播放过

    @ColumnInfo(name = "conversation_json_string_body")
    private String jsonStringBody;

    //消息体的body
    @ColumnInfo(name = "conversation_body_type")
    private String bodyType = "text";              //消息类型（）

    @ColumnInfo(name = "conversation_type")
    private String type = "chat";              //消息类型(聊天类型)
//    @ColumnInfo(name = "conversation_group_type")
//    private String groupType; //分组消息类型 p 表示个人信息 m 表示群信息

    @ColumnInfo(name = "receiptType")
    private String receiptType; //回执类型（已发送 | 已收到 | 已读）

    @ColumnInfo(name = "conversation_messages_sortNum")
    private Long sortNum; //排序num

    public GroupMessageDataInfo() {
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public Long getSortNum() {
        return sortNum;
    }

    public void setSortNum(Long sortNum) {
        this.sortNum = sortNum;
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

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }


    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getReceiptType() {
        return receiptType;
    }

    public void setReceiptType(String receiptType) {
        this.receiptType = receiptType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String getJsonStringBody() {
        return jsonStringBody;
    }

    public void setJsonStringBody(String jsonStringBody) {
        this.jsonStringBody = jsonStringBody;
    }


    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
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



    public int getMediaStatus() {
        return mediaStatus;
    }

    public void setMediaStatus(int mediaStatus) {
        this.mediaStatus = mediaStatus;
    }

    public String getBodyType() {
        return bodyType;
    }

    public void setBodyType(String bodyType) {
        this.bodyType = bodyType;
    }

    public MessageDataInfoBean changeToMessageDataInfoBean(){
        MessageDataInfoBean messageDataInfoBean = new MessageDataInfoBean();
        messageDataInfoBean.setCreateTime(sentTime);
        messageDataInfoBean.setFromUserId(fromUserId);
        messageDataInfoBean.setToUserId(toUserId);
        messageDataInfoBean.setId(messageId);
        messageDataInfoBean.setReceiptType(receiptType);
        messageDataInfoBean.setTopicId(topicId);
        messageDataInfoBean.setType(type);
        messageDataInfoBean.setSortNum(sortNum);
        if (jsonStringBody!=null){
            MessageDataInfoBean.BodyBean bodyBean = new Gson().fromJson(jsonStringBody, MessageDataInfoBean.BodyBean.class);
            messageDataInfoBean.setBody(bodyBean);
        }
        return messageDataInfoBean;
    }
}
