package com.yryc.imlib.info.io;

/**
 * 聊天对象的个人信息
 */
public class UserInfo {
    private String avatar_url;

    private String name;

    private String uid;

    public UserInfo(String uid, String name, String avatar_url) {
        this.avatar_url = avatar_url;
        this.name = name;
        this.uid = uid;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
