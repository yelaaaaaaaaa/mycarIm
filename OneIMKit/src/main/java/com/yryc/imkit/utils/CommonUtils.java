package com.yryc.imkit.utils;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.yryc.imkit.R;
import com.yryc.imkit.constant.Config;
import com.yryc.imkit.constant.MessageType;
import com.yryc.imlib.model.chat.ChatBody;
import com.yryc.imlib.model.chat.ChatCar;
import com.yryc.imlib.model.chat.ChatImage;
import com.yryc.imlib.model.chat.ChatLocation;
import com.yryc.imlib.model.chat.ChatOrder;
import com.yryc.imlib.model.chat.ChatText;
import com.yryc.imlib.model.chat.ChatVideo;
import com.yryc.imlib.model.chat.ChatVoice;
import com.yryc.imlib.client.MessageEvent;
import com.yryc.imlib.info.GeneralMessageDataInfo;
import com.yryc.imlib.rx.RxEvent.EventType;
import com.yryc.imlib.utils.SPManager;
import com.yryc.imlib.xmpp.ImUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * @author : Mai_Xiao_Peng
 * @email : Mai_Xiao_Peng@163.com
 * @time : 2018/12/14 11:30
 * @describe :
 */


public class CommonUtils {


    public static ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

    /**
     * 发送消息
     *
     * @param context
     * @param messageType
     * @param messageBody
     * @param filePath
     */
    public static void sendMessage(Context context, MessageType messageType, Object messageBody, String filePath) {
        if (messageType.getType().equals(MessageType.VOICE.getType())) {
            ChatVoice.Body body = (ChatVoice.Body) messageBody;
            ImUtil.getInstance().sendAudioMessage(body.getContent(), body.getLength());
        } else if (messageType.getType().equals(MessageType.IMAGE.getType())) {
            ChatImage.Body body = (ChatImage.Body) messageBody;
            ImUtil.getInstance().sendImageMessage(body.getContent());
        } else if (messageType.getType().equals(MessageType.VIDEO.getType())) {
            ChatVideo.Body body = (ChatVideo.Body) messageBody;
            ImUtil.getInstance().sendVideoMessage(body.getContent(),body.getCover(),body.getLength());
        }
    }

    /**
     * 发送消息
     *
     * @param context
     * @param messageType
     * @param messageBody
     */
    public static void sendMessage(Context context, MessageType messageType, Object messageBody) {
        if (messageType.getType().equals(MessageType.TEXT.getType())) {
            ChatText.Body body = (ChatText.Body) messageBody;
            ImUtil.getInstance().sendTextMessage(body.getContent());
        } else if (messageType.getType().equals(MessageType.VOICE.getType())) {
            ChatVoice.Body body = (ChatVoice.Body) messageBody;
            ImUtil.getInstance().sendAudioMessage(body.getContent(), body.getLength());
        } else if (messageType.getType().equals(MessageType.IMAGE.getType())) {
            ChatImage.Body body = (ChatImage.Body) messageBody;
            ImUtil.getInstance().sendImageMessage(body.getContent());
        } else if (messageType.getType().equals(MessageType.VIDEO.getType())) {
            ChatVideo.Body body = (ChatVideo.Body) messageBody;
            ImUtil.getInstance().sendVideoMessage(body.getContent(),body.getCover(),body.getLength());
        } else if (messageType.getType().equals(MessageType.LOCATION.getType())) {
            ChatLocation.Body body = (ChatLocation.Body) messageBody;
            ImUtil.getInstance().sendLocationMessage(body.getContent(), body.getAddress(), body.getLocation());
        }
    }

    /**
     * 发送订单消息
     *
     * @param context
     * @param targetUserId
     * @param body
     */
    public static void sendOrderMessage(Context context, String targetUserId, ChatOrder.Body body) {
        GeneralMessageDataInfo generalMessageDataInfo = new GeneralMessageDataInfo();
        generalMessageDataInfo.setJsonStringBody(new Gson().toJson(body));
        generalMessageDataInfo.setSentTime(System.currentTimeMillis());
        generalMessageDataInfo.setType(MessageType.ORDER.getType());
        //generalMessageDataInfo.setGroupType(GroupType.PERSONAL.getType());
        generalMessageDataInfo.setWhoReceive(targetUserId);
        generalMessageDataInfo.setWhoSend((String) SPManager.getInstance(context).get(SPManager.SP_KEY_USER_ID, ""));
        generalMessageDataInfo.setMessageId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
        // OIMClient.getInstance().sendMessage(generalMessageDataInfo);
        EventBus.getDefault().post(new MessageEvent(EventType.EVENT_SEND_MESSAGE, generalMessageDataInfo));
    }

    /**
     * 发送新车二手车消息
     *
     * @param context
     * @param targetUserId
     * @param body
     */
    public static void sendCarMessage(Context context, String targetUserId, ChatCar.Body body) {
        GeneralMessageDataInfo generalMessageDataInfo = new GeneralMessageDataInfo();
        generalMessageDataInfo.setJsonStringBody(new Gson().toJson(body));
        generalMessageDataInfo.setSentTime(System.currentTimeMillis());
        generalMessageDataInfo.setType(MessageType.CAR.getType());
        //generalMessageDataInfo.setGroupType(GroupType.PERSONAL.getType());
        generalMessageDataInfo.setWhoReceive(targetUserId);
        generalMessageDataInfo.setWhoSend((String) SPManager.getInstance(context).get(SPManager.SP_KEY_USER_ID, ""));
        generalMessageDataInfo.setMessageId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
        //OIMClient.getInstance().sendMessage(generalMessageDataInfo);
        EventBus.getDefault().post(new MessageEvent(EventType.EVENT_SEND_MESSAGE, generalMessageDataInfo));
    }

    /**
     * 发送离线消息
     *
     * @param chatBody
     */
    public static void sendOfflineMessage(ChatBody chatBody) {
        GeneralMessageDataInfo generalMessageDataInfo = new GeneralMessageDataInfo();
        generalMessageDataInfo.setAvatarUrl(chatBody.getAvatarUrl());
        generalMessageDataInfo.setFromName(chatBody.getFromName());
        generalMessageDataInfo.setFromUid(chatBody.getFromUid());
        generalMessageDataInfo.setJsonStringBody(chatBody.getJson());
        generalMessageDataInfo.setType(chatBody.getType());
        //generalMessageDataInfo.setGroupType(chatBody.getType());
        generalMessageDataInfo.setWhoReceive(chatBody.getWhoReceive());
        generalMessageDataInfo.setWhoSend(chatBody.getWhoSend());

        //generalMessageDataInfo.setMessageStatus(Enums.MessageStatus.READ.getValue());
        generalMessageDataInfo.setMessageId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
        generalMessageDataInfo.setSentTime(System.currentTimeMillis());
        //OIMClient.getInstance().addOfflineMessage(generalMessageDataInfo);
        EventBus.getDefault().post(new MessageEvent(EventType.EVENT_SEND_MESSAGE, generalMessageDataInfo));
    }

    /**
     * 根据时间长短计算语音条宽度:220dp
     *
     * @param context
     * @param seconds
     * @return
     */
    public synchronized static int getVoiceLineWight(Context context, int seconds) {
        //1-2s是最短的。2-10s每秒增加一个单位。10-60s每10s增加一个单位。
        if (seconds <= 2) {
            return PixelUtils.dip2px(context, 70);
        } else if (seconds <= 10) {
            //90~170
            return PixelUtils.dip2px(context, 70 + 5 * seconds);
        } else {
            //170~220
            return PixelUtils.dip2px(context, 110 + 5 * (seconds / 10) - 5);
        }
    }

    /**
     * 复制文本
     *
     * @param context
     * @param text
     */
    public static void copyText2Clipboard(Context context, String text) {
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("text", text);
        clipboardManager.setPrimaryClip(clipData);
        Toast.makeText(context, "已复制", Toast.LENGTH_SHORT).show();
    }

    /**
     * 下载图片
     *
     * @param context
     * @param url
     */
    public static void downloadImage(Activity context, String url) {
        cachedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                File file = null;
                try {
                    file = Glide.with(context).asFile()
                            .load(url)
                            .submit()
                            .get();
                    File newFile = new File(Config.Dir.IMG + UUID.randomUUID() + ".jpg");
                    File dir = new File(newFile.getParent());
                    if (!dir.exists()) {
                        dir.mkdir();
                    }
                    CommonUtils.copy(file, newFile);
                    try {
                        //文件插入到系统图库
                        MediaStore.Images.Media.insertImage(context.getContentResolver(),
                                newFile.getAbsolutePath(), newFile.getName(), null);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    // 最后通知图库更新
                    context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + newFile.getAbsolutePath())));
                    context.runOnUiThread(() -> Toast.makeText(context, String.format(context.getString(R.string.image_download_tip), "/yryc/imkit/image"), Toast.LENGTH_SHORT).show());
                } catch (Exception e) {

                }
            }
        });
    }


    /**
     * 复制文件
     *
     * @param source 输入文件
     * @param target 输出文件
     */
    public static void copy(File source, File target) {
        FileInputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            fileInputStream = new FileInputStream(source);
            fileOutputStream = new FileOutputStream(target);
            byte[] buffer = new byte[1024];
            while (fileInputStream.read(buffer) > 0) {
                fileOutputStream.write(buffer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 获取视频时长、长、高
     *
     * @param filePath
     * @return
     */
    public static List<String> getVideoMetaData(String filePath) {
        List<String> meta = new ArrayList<>();
        android.media.MediaMetadataRetriever mmr = new android.media.MediaMetadataRetriever();
        try {
            mmr.setDataSource(filePath);
            meta.add(mmr.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_DURATION));
            meta.add(mmr.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
            meta.add(mmr.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
        } catch (Exception ex) {
        } finally {
            mmr.release();
        }
        return meta;
    }

    /**
     * 获取图片宽高
     *
     * @return
     */
    public static List<Integer> getImageMetaData(String filePath) {
        List<Integer> meta = new ArrayList<>();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        int width = options.outWidth;
        int height = options.outHeight;
        meta.add(width);
        meta.add(height);
        return meta;
    }

    /**
     * 生成临时用户UUID
     *
     * @return
     */
    public static String getTempUserID() {
        return "temp_" + UUID.randomUUID();
    }
}
