package com.yryc.imlib.info.io.messageTypeDetailInfo;

import com.yryc.imlib.info.io.Size;

/**
 * type 类型 video
 *
 * content 视频播放地址 url
 *
 * size 视频的宽高
 *
 * cover 封面截图的 url,可以使用 oss 自动截取的
 */
public class VideoMessageDataInfo extends MessageBodyBaseDataInfo {
    private Size size;
    private String cover;

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }
}
