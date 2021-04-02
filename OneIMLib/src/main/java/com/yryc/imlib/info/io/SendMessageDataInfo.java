package com.yryc.imlib.info.io;

import com.yryc.imlib.info.io.messageTypeDetailInfo.MessageBodyBaseDataInfo;

/**
 * 发送信息
 */
public class SendMessageDataInfo {

//    private MessageBodyBaseDataInfo body;
    private String jsonStringBody;
    private String id;
    private String to;

    public SendMessageDataInfo(String jsonStringBody, String to, String id) {
//        this.body = body;
        this.jsonStringBody = jsonStringBody;
        this.to = to;
        this.id = id;
    }

//    public MessageBodyBaseDataInfo getBody() {
//        return body;
//    }
//
//    public void setBody(MessageBodyBaseDataInfo body) {
//        this.body = body;
//    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getJsonStringBody() {
        return jsonStringBody;
    }

    public void setJsonStringBody(String jsonStringBody) {
        this.jsonStringBody = jsonStringBody;
    }
}
