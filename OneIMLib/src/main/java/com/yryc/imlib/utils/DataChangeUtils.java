package com.yryc.imlib.utils;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.yryc.imlib.constants.Enums;
import com.yryc.imlib.info.ConversationListDataInfo;
import com.yryc.imlib.info.GeneralMessageDataInfo;
import com.yryc.imlib.info.RichData;
import com.yryc.imlib.info.io.ReceiveMessageDataInfo;
import com.yryc.imlib.info.io.SendMessageDataInfo;
import com.yryc.imlib.info.io.messageTypeDetailInfo.MessageBodyBaseDataInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class DataChangeUtils {

    public static HashMap<String, String> listMessageAbbreviation;          //放聊天列表 最后一条消息显示对应String  防自定义数据类型扩张

    //接收的消息转成平层储存
    public static GeneralMessageDataInfo receiveToUniversalDataInfo(ReceiveMessageDataInfo receiveMessage) {

        if (receiveMessage == null) {
            return null;
        }

        GeneralMessageDataInfo universalData = new GeneralMessageDataInfo();

        universalData.setMessageId(receiveMessage.getId());
        universalData.setSentTime(receiveMessage.getSent_time());
        universalData.setWhoReceive(receiveMessage.getTo());
//        universalData.setGroupType(receiveMessage.getT());
//        universalData.setHaveReadUnread(false);
//
//        universalData.setType(receiveMessage.getType());
//        if (receiveMessage.getFrom() != null) {
//            universalData.setWhoSend(receiveMessage.getFrom().getUid());
//            universalData.setAvatarUrl(receiveMessage.getFrom().getAvatar_url());
//            universalData.setFromUserId(receiveMessage.getFrom().getName());
//            universalData.setFromUserId(receiveMessage.getFrom().getUid());
//        }
//        universalData.setMessageStatus(Enums.MessageStatus.UNREAD.getValue());
        universalData.setMediaStatus(Enums.MediaStatus.UNPLAY.getValue());

        universalData.setJsonStringBody(receiveMessage.getJsonStringBody());

        return universalData;
    }

    //发送消息转成io结构
    public static SendMessageDataInfo universalToSendDataInfo(GeneralMessageDataInfo universalData) {

        if (universalData == null) {
            return null;
        }

//        Gson gson = new Gson();
//        MessageBodyBaseDataInfo messageBodyDataInfo = gson.fromJson(universalData.getJsonStringBody(), MessageBodyBaseDataInfo.class);

        SendMessageDataInfo sendMessageDataInfo = new SendMessageDataInfo(universalData.getJsonStringBody(), universalData.getWhoReceive(), universalData.getMessageId());
        return sendMessageDataInfo;
    }

    // 更新回话列表数据
    public static ConversationListDataInfo updateSingleConversation(List<ConversationListDataInfo> conversationListDataList,
                                                                    GeneralMessageDataInfo messageData,
                                                                    boolean isReceiver) {

        String conversationId = isReceiver ? messageData.getWhoSend() : messageData.getWhoReceive();

        ConversationListDataInfo userConversation = null;
        for (ConversationListDataInfo dataInfo : conversationListDataList) {
            if (dataInfo.getUserId().equals(conversationId)) {
                userConversation = dataInfo;
                break;
            }
        }

        if (userConversation == null) {
            userConversation = new ConversationListDataInfo();
            if (isReceiver) {
                //userConversation.setUserId(messageData.getFromUserId());
//                if (!TextUtils.isEmpty(messageData.getFromUserId())) {
//                    userConversation.setUserName(messageData.getFromUserId());
//                }
//                if (!TextUtils.isEmpty(messageData.getAvatarUrl())) {
//                    userConversation.setHaedImageUrl(messageData.getAvatarUrl());
//                }

            } else {
                userConversation.setUserId(messageData.getWhoReceive());
            }
        }

        userConversation.setLastWords(typeToListShowString(messageData.getType(), messageData));
        userConversation.setLastMessageId(messageData.getMessageId());
        userConversation.setFromWho(isReceiver ? 2 : 1);
        userConversation.setLastWordsTime(messageData.getSentTime());
        userConversation.setNumberOfUnreadMessages(isReceiver ? (userConversation.getNumberOfUnreadMessages() + 1) : 0);

        return userConversation;
    }

    // 更新回话列表数据
    public static List<ConversationListDataInfo> updateConversationListForReceiver(List<ConversationListDataInfo> conversationListDataList,
                                                                                   List<GeneralMessageDataInfo> messageDataList, String myUserId) {

        if (messageDataList == null || messageDataList.size() == 0) {
            return null;
        }

        List<ConversationListDataInfo> tempList = new ArrayList<>();
        tempList.addAll(conversationListDataList);

        List<ConversationListDataInfo> updateDataList = new ArrayList<>();
        for (GeneralMessageDataInfo messageDataInfo : messageDataList) {
            ConversationListDataInfo listDataInfo;
            if (myUserId != null && myUserId.equals(messageDataInfo.getWhoSend())) {
                listDataInfo = DataChangeUtils.updateSingleConversation(tempList, messageDataInfo, false);
            } else {
                listDataInfo = DataChangeUtils.updateSingleConversation(tempList, messageDataInfo, true);
            }

            if (!tempList.contains(listDataInfo)) {
                tempList.add(listDataInfo);
            }

            if (!updateDataList.contains(listDataInfo)) {
                updateDataList.add(listDataInfo);
            }
        }

        return updateDataList;
    }

    //发送非文字信息 在聊天列表的显示
    private static String typeToListShowString(String type, GeneralMessageDataInfo messageData) {
        Gson sendbody = new Gson();
        MessageBodyBaseDataInfo messageBodyDataInfo = sendbody.fromJson(messageData.getJsonStringBody(), MessageBodyBaseDataInfo.class);
        String body = messageBodyDataInfo.getContent();
        if (!type.equals("text")) {
            Iterator map1it = listMessageAbbreviation.entrySet().iterator();
            if (type.equals("rich")) {
                Gson gson = new Gson();
                RichData richData = gson.fromJson(messageData.getJsonStringBody(), RichData.class);
                if (richData != null && richData.getTip() != null) {
                    body = "[" + richData.getTip() + "]";
                }
            }else{
                while (map1it.hasNext()) {
                    Map.Entry<String, String> entry = (Map.Entry<String, String>) map1it.next();
                    System.out.println("Key: " + entry.getKey() + " Value: " + entry.getValue());
                    if (type.equals(entry.getKey())) {
                        body = entry.getValue();
                        break;
                    }
                }
            }
        }

        return body;
    }

    public static void setListMessageAbbreviation(HashMap<String, String> hashMap) {
        if (listMessageAbbreviation == null) {
            listMessageAbbreviation = new HashMap<>();
            listMessageAbbreviation.put("image", "[图片]");
            listMessageAbbreviation.put("audio", "[语音]");
            listMessageAbbreviation.put("video", "[视频]");
            listMessageAbbreviation.put("rich", "[图文]");
            listMessageAbbreviation.put("system", "[系统通知]");
            listMessageAbbreviation.put("order", "[报价信息]");
            listMessageAbbreviation.put("car", "[车辆信息]");
        }
        if (hashMap != null) {
            listMessageAbbreviation.putAll(hashMap);
        }


    }
}
