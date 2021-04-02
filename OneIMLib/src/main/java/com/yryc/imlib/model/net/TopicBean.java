package com.yryc.imlib.model.net;

/**
 * @author : sklyand
 * @email :
 * @time : 2020/5/22 10:56
 * @describe ：
 */
public class TopicBean {

    /**
     * fromUserId :
     * toUserId :
     * topicId :
     */

    private String fromUserId;
    private String toUserId;
    private String topicId;

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
}
