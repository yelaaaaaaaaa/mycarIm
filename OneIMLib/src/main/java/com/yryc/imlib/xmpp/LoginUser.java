package com.yryc.imlib.xmpp;

/**
 * @author xpx
 * @time 2020/4/27 17:54
 * @describe:
 */
public class LoginUser {
    private String userName;
    private String pwd;
    private String toUser;
    private String topicId;
    private boolean isGroup;
    private String groupName;
    private String groupTopicId;

    private static LoginUser mInstance;

    private LoginUser() {

    }

    public static LoginUser getInstance() {
        if (null == mInstance) {
            synchronized (LoginUser.class) {
                mInstance = new LoginUser();
            }
        }
        return mInstance;
    }

    public void setLoginInfo(String userName, String pwd) {
        this.userName = userName;
        this.pwd = pwd;
    }

    public void setToUserInfo(String toUser, String topicId) {
        this.toUser = toUser;
        this.topicId = topicId;
    }

    public void setToUserInfo(String toUser) {
        this.toUser = toUser;
    }

    public void setLoginInfo(String toUser, String topicId, String groupName, String groupTopicId, boolean isGroup) {
        this.toUser = toUser;
        this.topicId = topicId;
        this.groupName = groupName;
        this.groupTopicId = groupTopicId;
        this.isGroup = isGroup;
    }

    public String getGroupTopicId() {
        return groupTopicId;
    }

    public void setGroupTopicId(String groupTopicId) {
        this.groupTopicId = groupTopicId;
    }

    public boolean isGroup() {
        return isGroup;
    }

    public void setGroup(boolean group) {
        isGroup = group;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }
}
