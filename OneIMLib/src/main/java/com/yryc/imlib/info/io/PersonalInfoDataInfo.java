package com.yryc.imlib.info.io;

/**
 * 聊天对象的个人信息
 */
public class PersonalInfoDataInfo {
    private String type;

    private String avatar_url;

    private String name;

    private String uid;

    public PersonalInfoDataInfo(String uid, String name, String avatar_url,String type) {
        this.avatar_url = avatar_url;
        this.name = name;
        this.uid = uid;
        this.type = type;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
