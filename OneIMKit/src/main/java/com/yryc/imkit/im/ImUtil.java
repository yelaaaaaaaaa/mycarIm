package com.yryc.imkit.im;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.yryc.imlib.model.chat.ChatBody;
import com.yryc.imlib.model.chat.ChatCar;
import com.yryc.imlib.model.chat.ChatOrder;
import com.yryc.imkit.ui.activity.OneChatContentActivity;
import com.yryc.imkit.ui.activity.OneGroupChatContentActivity;
import com.yryc.imkit.utils.CommonUtils;
import com.yryc.imlib.xmpp.LoginUser;

import org.jivesoftware.smack.util.StringUtils;

import java.util.HashMap;

public class ImUtil {

    public ImOptions imOptions;

    public ImUtil(ImOptions imOptions) {
        this.imOptions = imOptions;
    }

    public ImOptions getImOptions() {
        return imOptions;
    }

    /**
     * 进入单聊页面
     *
     * @param context
     * @param toUser
     * @return
     */
    public ImUtil openSingleChatActivity(Context context, String toUser) {
        if (StringUtils.isNullOrEmpty(toUser)) {
            Toast.makeText(context, "聊天对象为空", Toast.LENGTH_SHORT).show();
            return this;
        }
        syncUpdateUserInfo(context, toUser, "", "", "", false);
        Intent intent = new Intent(context, OneChatContentActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        return this;
    }


    /**
     * 进入群聊页面
     *
     * @param context
     * @param groupName
     * @param groupTopicId
     * @return
     */
    public ImUtil openGroupChatActivity(Context context, String groupName, String groupTopicId) {
        if (StringUtils.isNullOrEmpty(groupName) || StringUtils.isNullOrEmpty(groupTopicId)) {
            Toast.makeText(context, "聊天群为空", Toast.LENGTH_SHORT).show();
            return this;
        }
        com.yryc.imlib.xmpp.ImUtil.getInstance().joinMultiUserChat(LoginUser.getInstance().getUserName(), groupTopicId);
        syncUpdateUserInfo(context, "", "", groupName, groupTopicId, true);
        Intent intent = new Intent(context, OneGroupChatContentActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        return this;
    }


    /**
     * 保存单聊对象用户信息
     *
     * @param context
     * @param toUser       单聊对象ID
     * @param topicId      单聊topicId
     * @param groupName    群聊对象ID
     * @param groupTopicId 群聊topicId
     * @param isGroup      是否群聊
     * @return
     */
    public ImUtil syncUpdateUserInfo(Context context, String toUser, String topicId, String groupName, String groupTopicId, boolean isGroup) {
        LoginUser.getInstance().setLoginInfo(toUser, topicId, groupName, groupTopicId, isGroup);
        return this;
    }

    /**
     * 获取未读消息总数
     *
     * @return
     */
    public int getTotalNumberOfUnreadMessages() {
//        return OIMClient.getInstance().getTotalNumberOfUnreadMessages();
        return 0;
    }

    /**
     * 发送订单消息
     *
     * @param context
     * @param targetUserId
     * @param body
     */
    public void sendOrderMessage(Context context, String targetUserId, ChatOrder.Body body) {
        CommonUtils.sendOrderMessage(context, targetUserId, body);
    }

    /**
     * 发送新车二手车消息
     *
     * @param context
     * @param targetUserId
     * @param body
     */
    public void sendCarMessage(Context context, String targetUserId, ChatCar.Body body) {
        CommonUtils.sendCarMessage(context, targetUserId, body);
    }


    /**
     * 发送离线消息
     *
     * @param chatBody
     */
    public void sendOfflineMessage(ChatBody chatBody) {
        CommonUtils.sendOfflineMessage(chatBody);
    }

//    /**
//     * 获取聊天列表数据
//     *
//     * @return
//     */
//    public List<ConversationListDataInfo> getConversationListDataList() {
//        return OIMClient.getInstance().getConversationListDataList();
//    }
//
//    //未读消息数回调
//    public ImUtil setNumberOfMessagesListenter(IUnReadMessageListener listenter) {
//        OIMClient.getInstance().addunReadMessageListener(listenter);
//        return this;
//    }
//
//    //未读消息数回调
//    public ImUtil removeLastUnReadMessageListener() {
//        OIMClient.getInstance().removeLastUnReadMessageListener();
//        return this;
//    }

    //自定义消息是最后一条时在聊天列表显示的
    public ImUtil setListMessageAbbreviation(HashMap<String, String> hashMap) {
        // OIMClient.getInstance().addListMessageAbbreviation(hashMap);
        return this;
    }

    public void disconnect() {
        //OIMClient.getInstance().disconnect();
    }

}
