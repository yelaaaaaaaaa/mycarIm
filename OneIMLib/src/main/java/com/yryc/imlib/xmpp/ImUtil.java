package com.yryc.imlib.xmpp;

import android.content.Context;

import com.yryc.imlib.info.ConversationListDataInfo;
import com.yryc.imlib.info.GeneralMessageDataInfo;
import com.yryc.imlib.info.GroupMessageDataInfo;
import com.yryc.imlib.info.ReceiptTypeEnum;
import com.yryc.imlib.info.io.Location;
import com.yryc.imlib.info.io.MessageDataInfoBean;
import com.yryc.imlib.info.io.Size;
import com.yryc.imlib.utils.SPManager;

import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smackx.muc.MultiUserChat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * @author : sklyand
 * @email :
 * @time : 2020/4/23 09:30
 * @describe ：
 */
public class ImUtil {
    private Context context;
    private Chat chat;
    private static ImUtil mInstance;
    private HashMap<String, MultiUserChat> groupMap;

    private ImUtil() {
    }

    public static ImUtil getInstance() {
        if (mInstance == null) {
            synchronized (ImUtil.class) {
                mInstance = new ImUtil();
            }
        }
        return mInstance;
    }

    public void setContext(Context context) {
        this.context = context;
    }


    /**
     * @param userName
     * @param password 登录xmpp
     */

    public void loginXmpp(String userName, String password) {
        if (null == context) return;
        if (groupMap==null){
            groupMap = new HashMap<>();
        }
        XmppConnectionConfig.getInstance(context).loginXmpp(context, userName, password);
        LoginUser.getInstance().setLoginInfo(userName, password);
        SPManager.getInstance(context).set(SPManager.SP_KEY_USER_ID, userName);
    }

    public void logout() {
        if (null == context) return;
        XmppConnectionConfig.getInstance(context).logOut();
        this.context = null;
        this.chat = null;
    }

    /**
     * 判断是否已连接
     *
     * @return
     */
    public boolean checkConnection() {
        if (null == context) return false;
        return null != XmppConnectionConfig.getInstance(context) && XmppConnectionConfig.getInstance(context).isConnected();
    }

    /**
     * 判断是否已经通过身份验证，是否已经完成登录
     *
     * @return
     */
    public boolean checkAuthenticated() {
        if (null == context) return false;
        return null != XmppConnectionConfig.getInstance(context) && XmppConnectionConfig.getInstance(context).isAuthenticated();
    }

    /**
     * @param user         用户名
     * @param chatListener 开启聊天
     */
    public void starChat(String user, ChatListener chatListener) {
        if (null == context) return;
        XmppConnectionConfig xmppConnectionConfig = XmppConnectionConfig.getInstance(context);
        xmppConnectionConfig.setChatListener(chatListener);
        chat = XmppConnectionConfig.getInstance(context).getFriendChat(user);
    }

    public void joinMultiUserChat(String user,String roomTopicId){
        if (null == context||groupMap.get(roomTopicId)!=null) return;
        XmppConnectionConfig xmppConnectionConfig = XmppConnectionConfig.getInstance(context);
        MultiUserChat multiUserChat = xmppConnectionConfig.joinMultiUserChat(context,user,roomTopicId);
        groupMap.put(roomTopicId,multiUserChat);
    }
    /**
     * @param messageDataInfoBean 发送单聊消息
     */
    public void sendMessage(MessageDataInfoBean messageDataInfoBean) {
        if (null == context) return;
        LoginUser loginUser =  LoginUser.getInstance();
        if (loginUser.isGroup()){
            if (groupMap==null){
                return;
            }
            messageDataInfoBean.setTopicId(loginUser.getGroupTopicId());
            MultiUserChat muc = groupMap.get(loginUser.getGroupTopicId());
            XmppConnectionConfig.getInstance(context).sendGroupMessage(muc, messageDataInfoBean);
        }else {
            XmppConnectionConfig.getInstance(context).sendSingleMessage(chat, messageDataInfoBean);
        }

    }

    public void sendReceipt(String receiptType, MessageDataInfoBean messageDataInfoBean) {
        if (null == context) return;
        XmppConnectionConfig.getInstance(context).sendReceipt(receiptType, messageDataInfoBean);
    }

    public void sendGroupReceipt(String receiptType, MessageDataInfoBean messageDataInfoBean) {
        if (null == context) return;
        MultiUserChat muc = groupMap.get(messageDataInfoBean.getTopicId());
        if (muc!=null){
            XmppConnectionConfig.getInstance(context).sendGroupReceipt(muc,receiptType, messageDataInfoBean);
        }

    }

    /**
     * 发送文字消息
     */
    public MessageDataInfoBean sendTextMessage(String content) {
        LoginUser loginUser = LoginUser.getInstance();
        MessageDataInfoBean messageDataInfoBean = new MessageDataInfoBean();
        messageDataInfoBean.setType("chat");
        messageDataInfoBean.setCreateTime(System.currentTimeMillis());
        messageDataInfoBean.setTopicId(loginUser.getTopicId());
        messageDataInfoBean.setToUserId(loginUser.getToUser());
        messageDataInfoBean.setFromUserId(loginUser.getUserName());
        messageDataInfoBean.setId(UUID.randomUUID().toString());
        messageDataInfoBean.setReceiptType(ReceiptTypeEnum.SENDING.getType());
        MessageDataInfoBean.BodyBean bodyBean = new MessageDataInfoBean.BodyBean();
        bodyBean.setType("text");
        bodyBean.setContent(content);
        messageDataInfoBean.setBody(bodyBean);
        sendMessage(messageDataInfoBean);
        return messageDataInfoBean;
    }

    /**
     * 发送图片消息
     */
    public MessageDataInfoBean sendImageMessage(String content) {
        LoginUser loginUser = LoginUser.getInstance();
        MessageDataInfoBean messageDataInfoBean = new MessageDataInfoBean();
        messageDataInfoBean.setType("chat");
        messageDataInfoBean.setTopicId(loginUser.getTopicId());
        messageDataInfoBean.setToUserId(loginUser.getToUser());
        messageDataInfoBean.setFromUserId(loginUser.getUserName());
        messageDataInfoBean.setId(UUID.randomUUID().toString());
        messageDataInfoBean.setReceiptType(ReceiptTypeEnum.SENDING.getType());
        MessageDataInfoBean.BodyBean bodyBean = new MessageDataInfoBean.BodyBean();
        bodyBean.setType("image");
        bodyBean.setContent(content);
        messageDataInfoBean.setBody(bodyBean);
        messageDataInfoBean.setCreateTime(System.currentTimeMillis());
        sendMessage(messageDataInfoBean);
        return messageDataInfoBean;
    }

    /**
     * 发送视频消息
     */
    public MessageDataInfoBean sendVideoMessage(String content, String cover, int length) {
        LoginUser loginUser = LoginUser.getInstance();
        MessageDataInfoBean messageDataInfoBean = new MessageDataInfoBean();
        messageDataInfoBean.setType("chat");
        messageDataInfoBean.setTopicId(loginUser.getTopicId());
        messageDataInfoBean.setToUserId(loginUser.getToUser());
        messageDataInfoBean.setFromUserId(loginUser.getUserName());
        messageDataInfoBean.setId(UUID.randomUUID().toString());
        messageDataInfoBean.setReceiptType(ReceiptTypeEnum.SENDING.getType());
        MessageDataInfoBean.BodyBean bodyBean = new MessageDataInfoBean.BodyBean();
        bodyBean.setType("video");
        bodyBean.setContent(content);
        bodyBean.setCover(cover);
        bodyBean.setLength(length);
        bodyBean.setSize(new Size(120, 100));
        messageDataInfoBean.setBody(bodyBean);
        messageDataInfoBean.setCreateTime(System.currentTimeMillis());
        sendMessage(messageDataInfoBean);
        return messageDataInfoBean;
    }

    /**
     * 发送语音消息
     */
    public MessageDataInfoBean sendAudioMessage(String content, float aodioLength) {
        LoginUser loginUser = LoginUser.getInstance();
        MessageDataInfoBean messageDataInfoBean = new MessageDataInfoBean();
        messageDataInfoBean.setType("chat");
        messageDataInfoBean.setTopicId(loginUser.getTopicId());
        messageDataInfoBean.setToUserId(loginUser.getToUser());
        messageDataInfoBean.setFromUserId(loginUser.getUserName());
        messageDataInfoBean.setId(UUID.randomUUID().toString());
        messageDataInfoBean.setReceiptType(ReceiptTypeEnum.SENDING.getType());
        MessageDataInfoBean.BodyBean bodyBean = new MessageDataInfoBean.BodyBean();
        bodyBean.setType("audio");
        bodyBean.setContent(content);
        bodyBean.setLength((int) aodioLength);
        messageDataInfoBean.setBody(bodyBean);
        messageDataInfoBean.setCreateTime(System.currentTimeMillis());
        sendMessage(messageDataInfoBean);
        return messageDataInfoBean;
    }

    /**
     * 发送位置消息
     */
    public MessageDataInfoBean sendLocationMessage(String locationName, String address, Location location) {
        LoginUser loginUser = LoginUser.getInstance();
        MessageDataInfoBean messageDataInfoBean = new MessageDataInfoBean();
        messageDataInfoBean.setType("chat");
        messageDataInfoBean.setTopicId(loginUser.getTopicId());
        messageDataInfoBean.setToUserId(loginUser.getToUser());
        messageDataInfoBean.setFromUserId(loginUser.getUserName());
        messageDataInfoBean.setId(UUID.randomUUID().toString());
        messageDataInfoBean.setReceiptType(ReceiptTypeEnum.SENDING.getType());
        MessageDataInfoBean.BodyBean bodyBean = new MessageDataInfoBean.BodyBean();
        bodyBean.setType("location");
        bodyBean.setContent(locationName);
        bodyBean.setAddress(address);
        bodyBean.setLocation(location);
        messageDataInfoBean.setBody(bodyBean);
        messageDataInfoBean.setCreateTime(System.currentTimeMillis());
        sendMessage(messageDataInfoBean);
        return messageDataInfoBean;
    }

    /**
     * @param uid
     * @param sortNum
     * @return 查询和某人所有的聊天
     */
    public List<MessageDataInfoBean> getChatMessageList(String uid, long sortNum,int size) {
        List<GeneralMessageDataInfo> daoMessageDataInfos = XmppConnectionConfig.getInstance(context).messageDataInfoDao.selectAll(uid, LoginUser.getInstance().getUserName(), sortNum, size);
        List<MessageDataInfoBean> messageDataInfoBeans = new ArrayList<>();
        for (GeneralMessageDataInfo daoMessageDataInfo : daoMessageDataInfos) {
            messageDataInfoBeans.add(daoMessageDataInfo.changeToMessageDataInfoBean());
        }
        Collections.reverse(messageDataInfoBeans);
        return messageDataInfoBeans;
    }
    /**
     * @param sortNum
     * @return 查询群聊所有的聊天
     */
    public List<MessageDataInfoBean> getGroupChatMessageList(String TopicId, long sortNum,int size) {
        List<GroupMessageDataInfo> daoMessageDataInfos = XmppConnectionConfig.getInstance(context).groupMessageDataInfoDao.selectMessageByTopicId(TopicId, sortNum,size);
        List<MessageDataInfoBean> messageDataInfoBeans = new ArrayList<>();
        for (GroupMessageDataInfo daoMessageDataInfo : daoMessageDataInfos) {
            messageDataInfoBeans.add(daoMessageDataInfo.changeToMessageDataInfoBean());
        }
        Collections.reverse(messageDataInfoBeans);
        return messageDataInfoBeans;
    }
    /**
     * @param sortNum
     * @return 查询和某人所有的聊天
     */
    public List<MessageDataInfoBean> getChatMessageByTopicId(String topicId, long sortNum,int size) {
        List<GeneralMessageDataInfo> daoMessageDataInfos = XmppConnectionConfig.getInstance(context).messageDataInfoDao.selectMessageByTopicId(topicId  , sortNum,size);
        List<MessageDataInfoBean> messageDataInfoBeans = new ArrayList<>();
        for (GeneralMessageDataInfo daoMessageDataInfo : daoMessageDataInfos) {
            messageDataInfoBeans.add(daoMessageDataInfo.changeToMessageDataInfoBean());
        }
        Collections.reverse(messageDataInfoBeans);
        return messageDataInfoBeans;
    }
    /**
     * @return 查询群聊所有的聊天
     */
    public void deleteGroupChatMessageList(String topicId) {
        XmppConnectionConfig.getInstance(context).groupMessageDataInfoDao.deleteByUid(topicId);
    }
    /**
     * @param messageid
     *
     * @return 根据id查询某条消息记录
     */
    public MessageDataInfoBean getMessageById(String messageid) {
        GeneralMessageDataInfo daoMessageDataInfo = XmppConnectionConfig.getInstance(context).messageDataInfoDao.getMessageById(messageid);
        if (daoMessageDataInfo != null) {
            return daoMessageDataInfo.changeToMessageDataInfoBean();
        } else {
            return null;
        }

    }

    /**
     * @param messageDataInfoBean 更新某条消息记录
     */
    public void updateMessage(MessageDataInfoBean messageDataInfoBean) {
        if (messageDataInfoBean != null) {
            XmppConnectionConfig.getInstance(context).messageDataInfoDao.update(messageDataInfoBean.changeToDaoMessageDataInfo());
        }
    }
    /**
     * @param messageDataInfoBean 更新某条群聊消息记录
     */
    public void updateGroupMessage(MessageDataInfoBean messageDataInfoBean) {
        if (messageDataInfoBean != null) {
            XmppConnectionConfig.getInstance(context).groupMessageDataInfoDao.update(messageDataInfoBean.changeToGroupDaoMessageDataInfo());
        }
    }

    /**
     * @param data 更新多条条消息记录
     */
    public void updateAllMessage(List<GeneralMessageDataInfo> data) {
        if (data != null && data.size() > 0) {
            XmppConnectionConfig.getInstance(context).messageDataInfoDao.update(data);
        }
    }
    /**
     * @param data 更新多条群聊消息记录
     */
    public void updateGroupAllMessage(List<GroupMessageDataInfo> data) {
        if (data != null && data.size() > 0) {
            XmppConnectionConfig.getInstance(context).groupMessageDataInfoDao.update(data);
        }
    }
    /**
     * @param data 更新多条群聊消息记录
     */
    public void replaceGroupAllMessage(List<GroupMessageDataInfo> data) {
        if (data != null && data.size() > 0) {
            XmppConnectionConfig.getInstance(context).groupMessageDataInfoDao.replaceAllWith(data);
        }
    }
    /**
     * 删除和某人的聊天记录
     */
    public void deleteChatMessageByUid(String uid, String myUid) {
        XmppConnectionConfig.getInstance(context).messageDataInfoDao.deleteByUid(uid, myUid);
    }

    /**
     * 添加一条记录
     */
    public void addMessage(MessageDataInfoBean messageDataInfoBean) {
        if (messageDataInfoBean == null) {
            return;
        }
        XmppConnectionConfig.getInstance(context).messageDataInfoDao.replaceWith(messageDataInfoBean.changeToDaoMessageDataInfo());
    }

    /**
     * 替换或添加一些聊天数据
     */
    public void replaceWithMessage(List<GeneralMessageDataInfo> data) {
        XmppConnectionConfig.getInstance(context).messageDataInfoDao.replaceAllWith(data);
    }

    /**
     * 查询聊天列表所有数据
     */
    public List<ConversationListDataInfo> getChatList() {
        return XmppConnectionConfig.getInstance(context).messageListDao.selectAll();
    }

    /**
     * 查询聊天列表所有数据
     */
    public ConversationListDataInfo getChatListInfoByUserid(String uid) {
        return XmppConnectionConfig.getInstance(context).messageListDao.selectByUserid(uid);
    }

    /**
     * 替换或添加一条聊天列表数据
     */
    public void replaceWithChatListInfo(ConversationListDataInfo daoChatListInfo) {
        XmppConnectionConfig.getInstance(context).messageListDao.replaceWith(daoChatListInfo);
    }

    /**
     * 替换或添加一些聊天列表数据
     */
    public void replaceWithChatListInfo(List<ConversationListDataInfo> daoChatListInfo) {
        XmppConnectionConfig.getInstance(context).messageListDao.replaceAllWith(daoChatListInfo);
    }

    /**
     * 删除一条聊天列表数据
     */
    public void deleteChatListInfo(ConversationListDataInfo daoChatListInfo) {
        XmppConnectionConfig.getInstance(context).messageListDao.delete(daoChatListInfo);
    }

    /**
     * @param user 获取jid名字
     */
    public String getJidName(String user) {
        return XmppConnectionConfig.getJidNameByName(user);
    }
}
