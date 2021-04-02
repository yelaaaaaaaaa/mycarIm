package com.yryc.imlib.info;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * 聊天列表单条数据
 */
@Entity(tableName = "yryc_im_message_list_data")
public class ConversationListDataInfo {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "conversation_list_user_id")
    private String userId;//聊天对象userid
    @ColumnInfo(name = "conversation_list_topicid")
    private String topicId;
    @ColumnInfo(name = "conversation_list_user_name")
    private String userName;    //聊天对象名称
    @ColumnInfo(name = "conversation_list_last_words")
    private String lastWords;//最后一句话
    @ColumnInfo(name = "conversation_list_last_messageid")
    private String lastMessageId;//最后一句消息id
    @ColumnInfo(name = "conversation_list_from_who")
    private int fromWho;//谁说的       1 我说的 2对方说的
    @ColumnInfo(name = "conversation_list_last_words_time")
    private Long lastWordsTime;   //最后一句的时间
    @ColumnInfo(name = "conversation_list_last_word_type")
    private String type = "text";
    @ColumnInfo(name = "conversation_list_head_url")
    private String haedImageUrl;  //头像url
    @ColumnInfo(name = "conversation_list_unread_messages")
    private int numberOfUnreadMessages; //未读消息数
    @ColumnInfo(name = "conversation_list_is_group")
    private int isGroup; //0是单聊 1是群聊

    @Ignore
    public ConversationListDataInfo() {
    }

    public ConversationListDataInfo(String userId, String userName, String lastWords, int fromWho, Long lastWordsTime, String haedImageUrl, int numberOfUnreadMessages) {
        this.userId = userId;
        this.userName = userName;
        this.lastWords = lastWords;
        this.fromWho = fromWho;
        this.lastWordsTime = lastWordsTime;
        this.haedImageUrl = haedImageUrl;
        this.numberOfUnreadMessages = numberOfUnreadMessages;
    }

    public int getIsGroup() {
        return isGroup;
    }

    public void setIsGroup(int isGroup) {
        this.isGroup = isGroup;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLastWords() {
        return lastWords;
    }

    public void setLastWords(String lastWords) {
        this.lastWords = lastWords;
    }

    public int getFromWho() {
        return fromWho;
    }

    public void setFromWho(int fromWho) {
        this.fromWho = fromWho;
    }

    public Long getLastWordsTime() {
        return lastWordsTime;
    }

    public void setLastWordsTime(Long lastWordsTime) {
        this.lastWordsTime = lastWordsTime;
    }

    public String getHaedImageUrl() {
        return haedImageUrl;
    }

    public void setHaedImageUrl(String haedImageUrl) {
        this.haedImageUrl = haedImageUrl;
    }

    public int getNumberOfUnreadMessages() {
        return numberOfUnreadMessages;
    }

    public void setNumberOfUnreadMessages(int numberOfUnreadMessages) {
        this.numberOfUnreadMessages = numberOfUnreadMessages;
    }

    public String getLastMessageId() {
        return lastMessageId;
    }

    public void setLastMessageId(String lastMessageId) {
        this.lastMessageId = lastMessageId;
    }
}
