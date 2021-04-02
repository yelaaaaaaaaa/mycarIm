package com.yryc.imlib.info.io.messageTypeDetailInfo;

import com.yryc.imlib.info.io.Size;

/**
 *type 类型 image
 *
 * content 图片 url
 *
 * size 图片大小 长宽
 */
public class ImageMessageDataInfo extends MessageBodyBaseDataInfo {
    private Size size;

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }
}
