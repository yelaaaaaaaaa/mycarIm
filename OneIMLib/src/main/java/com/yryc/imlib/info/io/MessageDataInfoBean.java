package com.yryc.imlib.info.io;

import com.google.gson.Gson;
import com.yryc.imlib.info.GeneralMessageDataInfo;
import com.yryc.imlib.info.GroupMessageDataInfo;


/**
 * 接收的单条消息
 */
public class MessageDataInfoBean {


    /**
     * type : chat
     * id : msg id
     * topicId : topic id
     * createTime : 1587362757342
     * user : {}
     * body : {"content":"content","type":"text | image | voice"}
     */

    private String type;
    private String id;
    private String topicId;
    private long createTime;
    private String cover;
    private String receiptType;
    private BodyBean body;
    private String fromUserId;
    private String toUserId;
    private long sortNum;
    private boolean isGroup;

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public boolean isGroup() {
        return isGroup;
    }

    public void setGroup(boolean group) {
        isGroup = group;
    }

    public long getSortNum() {
        return sortNum;
    }

    public void setSortNum(long sortNum) {
        this.sortNum = sortNum;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }


    public BodyBean getBody() {
        return body;
    }

    public void setBody(BodyBean body) {
        this.body = body;
    }

    public static class BodyBean {

        /**
         * content : http://
         * cover : xxxx
         * length : 10
         * size : {"height":120,"width":100}
         * type : video
         */

        private String content;
        private String cover;
        private int length;
        private Size size;
        private String type;
        private Location location;
        private String address;

        public Size getSize() {
            return size;
        }

        public void setSize(Size size) {
            this.size = size;
        }

        public Location getLocation() {
            return location;
        }

        public void setLocation(Location location) {
            this.location = location;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public int getLength() {
            return length;
        }

        public void setLength(int length) {
            this.length = length;
        }


        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    public GeneralMessageDataInfo changeToDaoMessageDataInfo(){
        GeneralMessageDataInfo daoMessageDataInfo = new GeneralMessageDataInfo();
        daoMessageDataInfo.setReceiptType(receiptType);
        if (body!=null){
            Gson gson = new Gson();
            daoMessageDataInfo.setJsonStringBody(gson.toJson(body));
        }
        daoMessageDataInfo.setWhoSend(fromUserId);
        daoMessageDataInfo.setWhoReceive(toUserId);
        daoMessageDataInfo.setType(type);
        daoMessageDataInfo.setBodyType(body.getType());
        daoMessageDataInfo.setMessageId(id);
        daoMessageDataInfo.setTopicId(topicId);
        daoMessageDataInfo.setSentTime(createTime);
        daoMessageDataInfo.setFromUid(fromUserId);
        daoMessageDataInfo.setFromName(fromUserId);
        daoMessageDataInfo.setSortNum(sortNum);
        return daoMessageDataInfo;
    }
    public GroupMessageDataInfo changeToGroupDaoMessageDataInfo(){
        GroupMessageDataInfo daoMessageDataInfo = new GroupMessageDataInfo();
        daoMessageDataInfo.setReceiptType(receiptType);
        if (body!=null){
            Gson gson = new Gson();
            daoMessageDataInfo.setJsonStringBody(gson.toJson(body));
        }
        daoMessageDataInfo.setType(type);
        daoMessageDataInfo.setBodyType(body.getType());
        daoMessageDataInfo.setMessageId(id);
        daoMessageDataInfo.setTopicId(topicId);
        daoMessageDataInfo.setSentTime(createTime);
        daoMessageDataInfo.setToUserId(toUserId);
        daoMessageDataInfo.setFromUserId(fromUserId);
        daoMessageDataInfo.setFromName(fromUserId);
        daoMessageDataInfo.setSortNum(sortNum);
        return daoMessageDataInfo;
    }
}
