package com.yryc.imlib.info.io.messageTypeDetailInfo;

/**
 * type 类型 audio
 * <p>
 * content 录音地址 url
 * <p>
 * length 语音的长度
 */
public class VoiceMessageDataInfo extends MessageBodyBaseDataInfo {
    long length;

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }
}
