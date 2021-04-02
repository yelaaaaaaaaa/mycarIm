package com.yryc.imlib.info.io;

/**
 * 接收的单条消息
 */
public class ReceiveMessageDataInfo {

    private String type;
    private String jsonStringBody;

    private PersonalInfoDataInfo from;
    private String id;
    private Long sent_time;
    private String to;
    private String t;       //t 消息类型 p 表示个人信息 m 表示群信息

    public ReceiveMessageDataInfo(String t, PersonalInfoDataInfo from, String id, Long sent_time, String to) {
        this.from = from;
        this.id = id;
        this.sent_time = sent_time;
        this.to = to;
        this.t = t;
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

    public PersonalInfoDataInfo getFrom() {
        return from;
    }

    public void setFrom(PersonalInfoDataInfo from) {
        this.from = from;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getSent_time() {
        return sent_time;
    }

    public void setSent_time(Long sent_time) {
        this.sent_time = sent_time;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getT() {
        return t;
    }

    public void setT(String t) {
        this.t = t;
    }
}
