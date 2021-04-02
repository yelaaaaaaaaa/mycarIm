package com.yryc.imlib.model.chat;

import android.os.Parcel;
import android.os.Parcelable;

import com.yryc.imlib.info.ConversationListDataInfo;

/**
 * @author : Mai_Xiao_Peng
 * @email : Mai_Xiao_Peng@163.com
 * @time : 2018/11/30 16:22
 * @describe : 聊天列表
 */

public class ChatList implements Parcelable {

    private String userId;//聊天对象userid

    private String userName;    //聊天对象名称

    private String lastWords;//最后一句话

    private int fromWho;//谁说的       1 我说的 2对方说的

    private Long lastWordsTime;   //最后一句的时间

    private String haedImageUrl;  //头像url

    private int numberOfUnreadMessages; //未读消息数

    private String topicId;

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

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public ChatList() {
    }


    public static class Builder {
        private ConversationListDataInfo conversationListDataInfo;

        public Builder(ConversationListDataInfo conversationListDataInfo) {
            this.conversationListDataInfo = conversationListDataInfo;
        }

        public ChatList build() {
            ChatList chatList = new ChatList();
            chatList.fromWho = conversationListDataInfo.getFromWho();
            chatList.haedImageUrl = conversationListDataInfo.getHaedImageUrl();
            chatList.lastWords = conversationListDataInfo.getLastWords();
            chatList.lastWordsTime = conversationListDataInfo.getLastWordsTime();
            chatList.numberOfUnreadMessages = conversationListDataInfo.getNumberOfUnreadMessages();
            chatList.userId = conversationListDataInfo.getUserId();
            chatList.userName = conversationListDataInfo.getUserName();
            chatList.topicId = conversationListDataInfo.getTopicId();
            return chatList;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userId);
        dest.writeString(this.userName);
        dest.writeString(this.lastWords);
        dest.writeInt(this.fromWho);
        dest.writeValue(this.lastWordsTime);
        dest.writeString(this.haedImageUrl);
        dest.writeInt(this.numberOfUnreadMessages);
        dest.writeString(this.topicId);
    }

    protected ChatList(Parcel in) {
        this.userId = in.readString();
        this.userName = in.readString();
        this.lastWords = in.readString();
        this.fromWho = in.readInt();
        this.lastWordsTime = (Long) in.readValue(Long.class.getClassLoader());
        this.haedImageUrl = in.readString();
        this.numberOfUnreadMessages = in.readInt();
        this.topicId = in.readString();
    }

    public static final Creator<ChatList> CREATOR = new Creator<ChatList>() {
        @Override
        public ChatList createFromParcel(Parcel source) {
            return new ChatList(source);
        }

        @Override
        public ChatList[] newArray(int size) {
            return new ChatList[size];
        }
    };
}
