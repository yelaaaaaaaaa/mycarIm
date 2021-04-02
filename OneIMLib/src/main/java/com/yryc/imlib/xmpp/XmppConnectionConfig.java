package com.yryc.imlib.xmpp;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.yryc.imlib.retrofit.ApiService;
import com.yryc.imlib.retrofit.RetrofitServiceCreator;
import com.yryc.imlib.client.MessageEvent;
import com.yryc.imlib.db.ConversationListDao;
import com.yryc.imlib.db.DataBase;
import com.yryc.imlib.db.GeneralMessageDataInfoDao;
import com.yryc.imlib.db.GroupMessageDataInfoDao;
import com.yryc.imlib.info.ConversationListDataInfo;
import com.yryc.imlib.info.GeneralMessageDataInfo;
import com.yryc.imlib.info.ReceiptTypeEnum;
import com.yryc.imlib.info.event.ChatMessageEvent;
import com.yryc.imlib.info.event.GroupChatMessageEvent;
import com.yryc.imlib.info.io.MessageDataInfoBean;
import com.yryc.imlib.rx.RxEvent;
import com.yryc.imlib.rx.RxThrowableConsumer;
import com.yryc.imlib.rx.RxUtils;

import org.greenrobot.eventbus.EventBus;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.muc.MucEnterConfiguration;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChatManager;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.EntityFullJid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.jid.parts.Resourcepart;
import org.jxmpp.stringprep.XmppStringprepException;

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Locale;

import io.reactivex.functions.Consumer;


/**
 * @author xiejinbo
 * @date 2019/9/19 0019 14:30
 */
public class XmppConnectionConfig extends XMPPTCPConnection {
    private static XmppConnectionConfig connection = null;
    private static int SERVER_PORT = 5222;
    private static String SERVER_HOST = "47.111.97.41";
    private static String SERVER_NAME = "yc-dev.com";
    private static ConnectionListener connectionListener;
    private static final String TAG = "XmppConnectionConfig";
    private ChatManager chatManager;
    private ChatListener chatListener;
    private static Gson gson;
    public static GeneralMessageDataInfoDao messageDataInfoDao;
    public static ConversationListDao messageListDao;
    public static StanzaListener lastStanzaListener;
    public static GroupMessageDataInfoDao groupMessageDataInfoDao;

    private XmppConnectionConfig(XMPPTCPConnectionConfiguration config) {
        super(config);
    }

    /**
     * 单例模式
     *
     * @return
     */
    public synchronized static XmppConnectionConfig getInstance(Context context) {

        return getConnection(context);
    }

    /**
     * 创建连接
     *
     * @return
     */
    public static XmppConnectionConfig getConnection(Context context) {
        if (connection == null) {
            // 开线程打开连接，避免在主线程里面执行HTTP请求
            // Caused by: android.os.NetworkOnMainThreadException
            /*new Thread(new Runnable() {
                @Override
                public void run() {*/
            openConnection(context);
               /* }
            }).start();
*/
        }
        return connection;
    }

    /**
     * 判断是否已连接
     *
     * @return
     */
    public boolean checkConnection() {
        return null != connection && connection.isConnected();
    }

    public static String getJidNameByName(String name) {
        return String.format(Locale.ENGLISH, "%s@%s", name, SERVER_NAME);
    }

    public static EntityBareJid getBareJidByName(String name) {
        try {
            return JidCreate.entityBareFrom(getJidNameByName(name));
        } catch (XmppStringprepException e) {
            e.printStackTrace();
        }
        return null;
    }

    public EntityFullJid getFullJidByName(String name) {
        try {
            return JidCreate.entityFullFrom(name);
        } catch (XmppStringprepException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 打开连接
     *
     * @return
     */
    public static boolean openConnection(Context context) {
        try {
            if (null == connection || !connection.isAuthenticated()) {
                //SmackConfiguration.DEBUG = true;
                messageDataInfoDao = DataBase.getInstance(context).getDb().getMessageDataInfoDao();
                messageListDao = DataBase.getInstance(context).getDb().getMessageListDao();
                groupMessageDataInfoDao = DataBase.getInstance(context).getDb().getGroupMessageDataInfoDao();
                XMPPTCPConnectionConfiguration.Builder config = XMPPTCPConnectionConfiguration.builder();
                //设置openfire主机IP
                config.setHostAddress(InetAddress.getByName(SERVER_HOST));
                //config.setHost(SERVER_HOST);
                //设置登录openfire的用户名和密码
                //config.setUsernameAndPassword("test1","123456");
                //设置openfire服务器名称
                config.setXmppDomain(SERVER_NAME);
                //设置端口号：默认5222
                config.setPort(SERVER_PORT);
                //设置客服端类型
                config.setResource("Android");
                //禁用SSL连接
                config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);

                //设置离线状态
                config.setSendPresence(true);
                //设置开启压缩，可以节省流量
                config.setCompressionEnabled(true);

                //SmackConfiguration.DEBUG=true;


                //需要经过同意才可以添加好友
                //Roster.setDefaultSubscriptionMode(Roster.SubscriptionMode.manual);

                // 将相应机制隐掉
                //SASLAuthentication.blacklistSASLMechanism("SCRAM-SHA-1");
                //SASLAuthentication.blacklistSASLMechanism("DIGEST-MD5");

                connection = new XmppConnectionConfig(config.build());
                //connection.connect();// 连接到服务器
//                Roster.getInstanceFor(connection).setSubscriptionMode(Roster.SubscriptionMode.manual);
//                ReconnectionManager reconnectionManager = ReconnectionManager.getInstanceFor(connection);
//                // 重联间隔5秒
//                reconnectionManager.setFixedDelay(5);
//                reconnectionManager.enableAutomaticReconnection();//开启重联机制

                // 维持ping
//                PingManager.setDefaultPingInterval(30);
//                PingManager pingManager = PingManager.getInstanceFor(connection);
//                pingManager.registerPingFailedListener(new PingFailedListener() {
//                    @Override
//                    public void pingFailed() {
//                        Log.e("xjbo", "pingFailed");
//                    }
//                });

                // 添加连接监听

                connectionListener = new ConnectionListener() {
                    @Override
                    public void connected(XMPPConnection connection) {
                        Log.e(TAG, "已经连接");
                        Log.e(TAG, "当前线路：" + connection.getHost());
                    }

                    @Override
                    public void authenticated(XMPPConnection connection, boolean resumed) {
                        Log.e(TAG, "已经登录");
                    }

                    @Override
                    public void connectionClosed() {
                        Log.e(TAG, "连接关闭");
                    }

                    @Override
                    public void connectionClosedOnError(Exception e) {
                        Log.e(TAG, "Exception: " + e.toString());
                        Log.e(TAG, "连接错误");
                    }
                };
                connection.addConnectionListener(connectionListener);
                if (gson == null) {
                    gson = new Gson();
                }
                lastStanzaListener = new StanzaListener() {
                    @Override
                    public void processStanza(Stanza packet) throws InterruptedException {
                        Message message = (Message) packet;
                        if (message.getBody() != null) {
                            handleMessageInfo(message);
                        }
                    }
                };
                connection.addAsyncStanzaListener(lastStanzaListener, MessageTypeFilter.NORMAL_OR_CHAT);
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 关闭连接
     *
     * @return
     */
    public void closeConnection() {
        if (connection != null) {
            connection.removeConnectionListener(connectionListener);
            if (connection.isConnected()) {
                connection.disconnect();
            }
            connection = null;
        }
        Log.d("closeConnection", "关闭连接");
    }

    /**
     * 判断是否已经通过身份验证，是否已经完成登录
     *
     * @return
     */
    public boolean checkAuthenticated() {
        return null != connection && connection.isAuthenticated();
    }

    /**
     * xmpp登录
     *
     * @param userName 用户名
     * @param password 密码
     * @return
     */
    public void loginXmpp(Context context, String userName, String password) {
        try {
            if (getConnection(context) == null) {

            } else if (isConnected()) {
                getConnection(context).login(userName, password);
            } else {
                connectOpenfire(context);
                getConnection(context).login(userName, password);
            }

            // 更改在线状态
            //setPresence(5);

        } catch (IOException | InterruptedException | SmackException | XMPPException e) {
            e.printStackTrace();
        }
    }


    /**
     * 更改用户状态
     */
    public void setPresence(int code) {
        if (connection == null) {
            return;
        }
        Presence presence;
        try {
            switch (code) {
                case 0:
                    presence = new Presence(Presence.Type.available);
                    connection.sendStanza(presence);
                    Log.v("state", "设置在线");
                    break;
                case 1:
                    presence = new Presence(Presence.Type.available);
                    presence.setMode(Presence.Mode.chat);
                    connection.sendStanza(presence);
                    Log.v("state", "设置Q我吧");
                    break;
                case 2:
                    presence = new Presence(Presence.Type.available);
                    presence.setMode(Presence.Mode.dnd);
                    connection.sendStanza(presence);
                    Log.v("state", "设置忙碌");
                    break;
                case 3:
                    presence = new Presence(Presence.Type.available);
                    presence.setMode(Presence.Mode.away);
                    connection.sendStanza(presence);
                    Log.v("state", "设置离开");
                    break;
                case 4:
//                    Roster roster = con.getRoster();
//                    Collection<RosterEntry> entries = roster.getEntries();
//                    for (RosterEntry entry : entries) {
//                        presence = new Presence(Presence.Type.unavailable);
//                        presence.setPacketID(Packet.ID_NOT_AVAILABLE);
//                        presence.setFrom(con.getUser());
//                        presence.setTo(entry.getUser());
//                        con.sendPacket(presence);
//                        Log.v("state", presence.toXML());
//                    }
//                    // 向同一用户的其他客户端发送隐身状态
//                    presence = new Presence(Presence.Type.unavailable);
//                    presence.setPacketID(Packet.ID_NOT_AVAILABLE);
//                    presence.setFrom(con.getUser());
//                    presence.setTo(StringUtils.parseBareAddress(con.getUser()));
//                    con.sendStanza(presence);
//                    Log.v("state", "设置隐身");
//                    break;
                case 5:
                    presence = new Presence(Presence.Type.unavailable);
                    connection.sendStanza(presence);
                    Log.v("state", "设置离线");
                    break;
                default:
                    break;
            }
        } catch (SmackException.NotConnectedException | InterruptedException e) {
            e.printStackTrace();
        }
    }


    /**
     * 创建聊天窗口
     *
     * @return Chat
     */
    public Chat getFriendChat(String user) {
        if (connection == null) {
            return null;
        }
        if (gson == null) {
            gson = new Gson();
        }
        chatManager = ChatManager.getInstanceFor(connection);
        return chatManager.chatWith(getBareJidByName(user));
    }

    /**
     * 去除最后一个单聊接收监听（防止出现单聊页面有多个接收监听，导致重复接收的问题）
     */
    public void removeLastSingleChatStanzaListener() {
        if (connection == null && lastStanzaListener == null) return;
        connection.removeAsyncStanzaListener(lastStanzaListener);
    }

    public ChatListener getChatListener() {
        return chatListener;
    }

    public void setChatListener(ChatListener chatListener) {
        this.chatListener = chatListener;
    }

    /**
     * 处理收到的消息
     */
    private static void handleMessageInfo(Message message) {
        Log.d("handleMessageInfo", message.getBody());
        MessageDataInfoBean messageDataInfoBean = gson.fromJson(message.getBody(), MessageDataInfoBean.class);
        if (messageDataInfoBean == null) {
            return;
        }

        String type = messageDataInfoBean.getType();
        if (type == null) {
            return;
        }
        //群聊
        if (message.getType().toString().equals("groupchat") && "chat".equals(type)&&messageDataInfoBean.getBody()!=null) {
//            if (message.getFrom() != null) {
//                messageDataInfoBean.setFromUserId(message.getFrom().getLocalpartOrNull().toString());
//            }
//            if (message.getTo() != null) {
//                messageDataInfoBean.setToUserId(message.getTo().getLocalpartOrNull().toString());
//            }
            messageDataInfoBean.setGroup(true);
            receiveGroupChat(messageDataInfoBean);
            groupMessageDataInfoDao.replaceWith(messageDataInfoBean.changeToGroupDaoMessageDataInfo());
            upDataChatListData(message, messageDataInfoBean, false);
            EventBus.getDefault().post(new GroupChatMessageEvent(messageDataInfoBean));
        } else {
            //单聊
            messageDataInfoBean.setGroup(false);
            switch (type) {
                case "receipt"://回执消息
                    MessageDataInfoBean messageDataInfoBeanUpdate = null;
                    GeneralMessageDataInfo daoMessageDataInfo = messageDataInfoDao.getMessageById(messageDataInfoBean.getId());
                    if (daoMessageDataInfo != null) {
                        daoMessageDataInfo.setSortNum(messageDataInfoBean.getSortNum());
                        daoMessageDataInfo.setReceiptType(messageDataInfoBean.getReceiptType());
                        messageDataInfoDao.update(daoMessageDataInfo);
                        messageDataInfoBeanUpdate = daoMessageDataInfo.changeToMessageDataInfoBean();
                    }
                    if (messageDataInfoBeanUpdate != null) {
                        EventBus.getDefault().post(new ChatMessageEvent(false, messageDataInfoBeanUpdate));
                    }
                    break;
                case "chat":
//                    if (message.getFrom() != null) {
//                        messageDataInfoBean.setFromUserId(message.getFrom().getLocalpartOrNull().toString());
//                    }
//                    if (message.getTo() != null) {
//                        messageDataInfoBean.setToUserId(message.getTo().getLocalpartOrNull().toString());
//                    }
                    messageDataInfoBean.setReceiptType(ReceiptTypeEnum.RECEIVED.getType());
                    messageDataInfoDao.replaceWith(messageDataInfoBean.changeToDaoMessageDataInfo());
                    upDataChatListData(message, messageDataInfoBean, false);
                    sendReceipt(ReceiptTypeEnum.RECEIVED.getType(), messageDataInfoBean);
                    EventBus.getDefault().post(new ChatMessageEvent(true, messageDataInfoBean));
//                if (chatListener!=null){
//                    chatListener.onReceiverMessage(messageDataInfoBean);
//                }
                    break;
            }
        }

    }

    /**
     * @param message
     * @param messageDataInfoBean 更新列表数据
     */
    private static void upDataChatListData(Message message, MessageDataInfoBean messageDataInfoBean, boolean isSelf) {
        String urserId = null;
        if (isSelf) {
            urserId = message.getTo().getLocalpartOrNull().toString();
        } else {
            urserId = message.getFrom().getLocalpartOrNull().toString();
        }
        ConversationListDataInfo daoChatListInfo = messageListDao.selectByUserid(urserId);
        if (daoChatListInfo == null) {
            daoChatListInfo = new ConversationListDataInfo();
            daoChatListInfo.setUserId(urserId);
            daoChatListInfo.setLastWords(messageDataInfoBean.getBody().getContent());
            daoChatListInfo.setType(messageDataInfoBean.getBody().getType());
            daoChatListInfo.setLastWordsTime(messageDataInfoBean.getCreateTime());
            daoChatListInfo.setUserName(urserId);
            daoChatListInfo.setTopicId(messageDataInfoBean.getTopicId());
            if (messageDataInfoBean.isGroup()){
                daoChatListInfo.setIsGroup(1);
            }else {
                daoChatListInfo.setIsGroup(0);
            }
            messageListDao.replaceWith(daoChatListInfo);
        } else {
            daoChatListInfo.setUserId(urserId);
            daoChatListInfo.setLastWords(messageDataInfoBean.getBody().getContent());
            daoChatListInfo.setType(messageDataInfoBean.getBody().getType());
            daoChatListInfo.setLastWordsTime(messageDataInfoBean.getCreateTime());
            daoChatListInfo.setUserName(urserId);
            daoChatListInfo.setTopicId(messageDataInfoBean.getTopicId());
            if (messageDataInfoBean.isGroup()){
                daoChatListInfo.setIsGroup(1);
            }else {
                daoChatListInfo.setIsGroup(0);
            }
            messageListDao.replaceWith(daoChatListInfo);
        }
        EventBus.getDefault().post(daoChatListInfo);
    }

    /**
     * @return 获取到ChatManager
     */
    public ChatManager getChatManager() {
        return chatManager;
    }

    /**
     * 发送单人聊天消息
     */
    public void sendSingleMessage(Chat chat, MessageDataInfoBean messageDataInfoBean) {
        try {
            String body = gson.toJson(messageDataInfoBean);
            Message message = new Message();
            message.setBody(body);
            message.setType(Message.Type.chat);
            if (chatListener != null && !messageDataInfoBean.getType().equals("receipt")) {
                chatListener.onSendMessage(messageDataInfoBean);
            }
            Log.d("handleMessageInfo", message.getBody());
            chat.send(message);
            EventBus.getDefault().post(new MessageEvent(RxEvent.EventType.EVENT_SEND_MESSAGE, messageDataInfoBean));
            upDataChatListData(message, messageDataInfoBean, true);
            messageDataInfoDao.replaceWith(messageDataInfoBean.changeToDaoMessageDataInfo());
        } catch (SmackException.NotConnectedException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送群组聊天消息
     *
     * @param muc muc
     */
    public void sendGroupMessage(MultiUserChat muc, MessageDataInfoBean messageDataInfoBean) {
        try {
            if (muc == null) {
                return;
            }
            String body = gson.toJson(messageDataInfoBean);
            Message message = new Message();
            message.setBody(body);
            message.setType(Message.Type.groupchat);
            Log.d("sendGroupMessage", message.getBody());
            muc.sendMessage(message);
            upDataChatListData(message, messageDataInfoBean, true);
            messageDataInfoDao.replaceWith(messageDataInfoBean.changeToDaoMessageDataInfo());
        } catch (SmackException.NotConnectedException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param receiptType
     * @param messageDataInfoBean 发送回执消息
     */
    public static void sendReceipt(String receiptType, MessageDataInfoBean messageDataInfoBean) {
        if (connection == null) {
            return;
        }
        Message stanza = new Message();
        stanza.setType(Message.Type.chat);
        //发送回执
        MessageDataInfoBean messageDataInfoBeanReceipt = new MessageDataInfoBean();
        messageDataInfoBeanReceipt.setId(messageDataInfoBean.getId());
        messageDataInfoBeanReceipt.setReceiptType(receiptType);
        messageDataInfoBeanReceipt.setTopicId(messageDataInfoBean.getTopicId());
        messageDataInfoBeanReceipt.setType("receipt");
        messageDataInfoBeanReceipt.setCreateTime(System.currentTimeMillis());
        if (gson == null) {
            gson = new Gson();
        }
        stanza.setBody(gson.toJson(messageDataInfoBeanReceipt));
        Log.d("handleMessageInfo", stanza.getBody());
        stanza.setTo(getBareJidByName(messageDataInfoBean.getFromUserId()));
        stanza.setFrom(getBareJidByName(messageDataInfoBean.getToUserId()));
        try {
            connection.sendStanza(stanza);
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param receiptType
     * @param messageDataInfoBean 发送回执消息
     */
    public  void sendGroupReceipt(MultiUserChat muc,String receiptType, MessageDataInfoBean messageDataInfoBean) {
        if (connection == null) {
            return;
        }
        //发送回执
        MessageDataInfoBean messageDataInfoBeanReceipt = new MessageDataInfoBean();
        messageDataInfoBeanReceipt.setId(messageDataInfoBean.getId());
        messageDataInfoBeanReceipt.setReceiptType(receiptType);
        messageDataInfoBeanReceipt.setTopicId(messageDataInfoBean.getTopicId());
        messageDataInfoBeanReceipt.setType("receipt");
        messageDataInfoBeanReceipt.setCreateTime(System.currentTimeMillis());
        try {
            if (muc == null) {
                return;
            }
            String body = gson.toJson(messageDataInfoBeanReceipt);
            Message message = new Message();
            message.setBody(body);
            message.setType(Message.Type.groupchat);
            Log.d("sendGroupMessage", message.getBody());
            muc.sendMessage(message);
        } catch (SmackException.NotConnectedException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    /**
     * 加入会议室
     *
     * @param user        昵称
     * @param roomTopicId 会议室名
     */
    public MultiUserChat joinMultiUserChat(Context context, String user, String roomTopicId) {
        if (getConnection(context) == null) {
            return null;
        }
        if (!isConnected()){
            return null;
        }
        try {
            //获取群管理对象
            MultiUserChatManager multiUserChatManager = MultiUserChatManager.getInstanceFor(connection);
            //通过群管理对象获取该群房间对象
            MultiUserChat multiUserChat = multiUserChatManager.getMultiUserChat(JidCreate.entityBareFrom(roomTopicId + "@conference." + getConnection(context).getXMPPServiceDomain()));

            MucEnterConfiguration.Builder builder = multiUserChat.getEnterConfigurationBuilder(Resourcepart.from(user));
            //只获取最后0条历史记录
            //builder.requestMaxCharsHistory(0);
            MucEnterConfiguration mucEnterConfiguration = builder.build();
            //加入群
            multiUserChat.join(mucEnterConfiguration);
            multiUserChat.addMessageListener(new MessageListener() {
                @Override
                public void processMessage(Message message) {
                    handleMessageInfo(message);
                }
            });
            Log.i("MultiUserChat", "会议室【" + roomTopicId + "】加入成功........");
            return multiUserChat;
        } catch (XMPPException | XmppStringprepException | InterruptedException | SmackException e) {
            e.printStackTrace();
            Log.i("MultiUserChat", "会议室【" + roomTopicId + "】加入失败........");
            return null;
        }
    }

    /**
     * 判断用户是否在线
     *
     * @param jid
     * @return 0代表不在线，1代表在线
     */
    public int IsUserOnLine(String jid) {
        Roster roster = Roster.getInstanceFor(connection);
        try {
            Presence presence = roster.getPresence(JidCreate.bareFrom(jid));
            if (presence.isAvailable()) {
                return 1;
            } else {
                return 0;
            }
        } catch (XmppStringprepException e) {
            e.printStackTrace();
            return 0;
        }
    }


    public void logOut() {
        //这里需要先将登陆状态改变为“离线”，再断开连接，不然在后台还是上线的状态
        try {
            setPresence(5);
            if (connection.isConnected()) {
                connection.disconnect();
            }
            connection.removeConnectionListener(connectionListener);
            connection = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 连接openfire
     */
    public void connectOpenfire(Context context) {
        if (connection == null) {
            connection = getInstance(context);
        }
        try {
            connection.connect();
        } catch (SmackException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XMPPException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 群聊(已收到)回执
     *
     * @return
     */
    public static void receiveGroupChat(MessageDataInfoBean messageDataInfoBean) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("createTime", System.currentTimeMillis());
        data.put("imUserId", LoginUser.getInstance().getUserName());
        data.put("messageId", messageDataInfoBean.getId());
        data.put("topicId", LoginUser.getInstance().getGroupTopicId());
        ApiService apiService = RetrofitServiceCreator.createService(ApiService.class);
        apiService.receiveGroupChat(data).compose(RxUtils.rxSchedulerHelper())
                .compose(RxUtils.handleResultCode()).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
            }
        }, new RxThrowableConsumer());
    }
}
