package com.yryc.imlib.model.net;

import com.yryc.imlib.info.ConversationListDataInfo;
import com.yryc.imlib.info.io.MessageDataInfoBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : sklyand
 * @email :
 * @time : 2020/5/22 11:07
 * @describe ：
 */
public class ChatListBean {

    private List<TopicListBean> topicList;

    public List<TopicListBean> getTopicList() {
        return topicList;
    }

    public void setTopicList(List<TopicListBean> topicList) {
        this.topicList = topicList;
    }

    public static class TopicListBean {
        /**
         * fromUserId :
         * lastMessage : {"body":{"address":"","content":"","cover":"","length":"","location":{"latitude":"","longitude":""},"size":{"height":"","width":""},"type":""},"createTime":0,"fromUserId":"","id":"","receiptType":"","sortNum":0,"toUserId":"","topicId":"","type":""}
         * toUserId :
         * topicId :
         * topicType : 0
         */
        //单聊  发起用户id
        private String fromUserId;
        //单聊  接收用户id
        private String toUserId;

        //群聊  发起用户id
        private String adminUserId;
        //群聊  群名称
        private String topicName;

        //        主题id
        private String topicId;
        //        单聊主题类型(1=车主to商家，2=车主to车主)
//        群聊主题类型(1=车友群))
        private int topicType;
        private int unreadCount; //未读消息数
        private MessageDataInfoBean lastMessage;

        public MessageDataInfoBean getLastMessage() {
            return lastMessage;
        }

        public void setLastMessage(MessageDataInfoBean lastMessage) {
            this.lastMessage = lastMessage;
        }

        public int getUnreadCount() {
            return unreadCount;
        }

        public void setUnreadCount(int unreadCount) {
            this.unreadCount = unreadCount;
        }

        public String getFromUserId() {
            return fromUserId;
        }

        public void setFromUserId(String fromUserId) {
            this.fromUserId = fromUserId;
        }

        public String getToUserId() {
            return toUserId;
        }

        public void setToUserId(String toUserId) {
            this.toUserId = toUserId;
        }

        public String getTopicId() {
            return topicId;
        }

        public void setTopicId(String topicId) {
            this.topicId = topicId;
        }

        public int getTopicType() {
            return topicType;
        }

        public void setTopicType(int topicType) {
            this.topicType = topicType;
        }

        public String getAdminUserId() {
            return adminUserId;
        }

        public void setAdminUserId(String adminUserId) {
            this.adminUserId = adminUserId;
        }

        public String getTopicName() {
            return topicName;
        }

        public void setTopicName(String topicName) {
            this.topicName = topicName;
        }
    }

    public List<ConversationListDataInfo> getListDataInfo(String selfId, boolean isGroup) {
        List<ConversationListDataInfo> dataInfoList = new ArrayList<>();
        if (topicList == null || topicList.size() == 0) {
            return dataInfoList;
        }
        for (TopicListBean topicListBean : topicList) {
            MessageDataInfoBean messageDataInfoBean = topicListBean.getLastMessage();
            ConversationListDataInfo conversationListDataInfo = new ConversationListDataInfo();
            conversationListDataInfo.setIsGroup(isGroup ? 1 : 0);
            if (isGroup) {
                conversationListDataInfo.setUserName(topicListBean.getTopicName());

            } else {
                if (selfId.equals(messageDataInfoBean.getFromUserId())) {
                    conversationListDataInfo.setFromWho(1);
                    conversationListDataInfo.setUserId(messageDataInfoBean.getToUserId());
                    conversationListDataInfo.setUserName(messageDataInfoBean.getToUserId());
                } else {
                    conversationListDataInfo.setFromWho(2);
                    conversationListDataInfo.setUserId(messageDataInfoBean.getFromUserId());
                    conversationListDataInfo.setUserName(messageDataInfoBean.getFromUserId());
                }
            }
            conversationListDataInfo.setTopicId(messageDataInfoBean.getTopicId());
            conversationListDataInfo.setLastMessageId(messageDataInfoBean.getId());
            conversationListDataInfo.setType(messageDataInfoBean.getBody().getType());
            conversationListDataInfo.setLastWords(messageDataInfoBean.getBody().getContent());
            conversationListDataInfo.setLastWordsTime(messageDataInfoBean.getCreateTime());
            conversationListDataInfo.setNumberOfUnreadMessages(topicListBean.getUnreadCount());
            dataInfoList.add(conversationListDataInfo);
        }
        return dataInfoList;
    }
}
