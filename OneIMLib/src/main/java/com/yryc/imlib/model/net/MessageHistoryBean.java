package com.yryc.imlib.model.net;

import com.yryc.imlib.info.io.MessageDataInfoBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : sklyand
 * @email :
 * @time : 2020/5/22 10:34
 * @describe ：
 */
public class MessageHistoryBean {

    /**
     * list : [{"address":"","content":"","cover":"","createTime":0,"fromUserId":"","length":"","location":{"latitude":"","longitude":""},"messageId":"","readTime":0,"receivedTime":0,"size":{"height":"","width":""},"sortNum":0,"status":"","toUserId":"","topicId":"","type":""}]
     * sortNum : 0
     * topicId :
     */

    private String topicId;
    private List<MessageDataInfoBean> list = new ArrayList<>();

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public List<MessageDataInfoBean> getList() {
        return list;
    }

    public void setList(List<MessageDataInfoBean> list) {
        this.list = list;
    }

}
