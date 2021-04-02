package com.yryc.imlib.xmpp;


import com.yryc.imlib.info.io.MessageDataInfoBean;

/**
 * @author : sklyand
 * @email :
 * @time : 2020/4/22 15:50
 * @describe ：
 */
public interface ChatListener {


    void onSendMessage(MessageDataInfoBean messageDataInfoBean);


}
