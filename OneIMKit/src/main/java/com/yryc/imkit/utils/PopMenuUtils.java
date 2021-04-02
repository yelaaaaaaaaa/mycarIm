package com.yryc.imkit.utils;

import android.content.Context;
import android.os.Vibrator;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.yryc.imkit.constant.ExpandType;
import com.yryc.imlib.utils.SPManager;


import java.util.ArrayList;
import java.util.List;

/**
 * @author : Mai_Xiao_Peng
 * @email : Mai_Xiao_Peng@163.com
 * @time : 2019/7/19 13:53
 * @describe :
 */


public class PopMenuUtils {

    public static int TEXT = 0x01;
    public static int IMAGE = 0x02;
    public static int VOICE = 0x03;

    /**
     * 获取popupMenu
     *
     * @param menuType
     * @param context
     * @param anchor
     * @param onMenuItemClickListener
     * @return
     */
    public static PopupMenu showPopMenu(int menuType, Context context, View anchor, PopMenuUtils.OnMenuItemClickListenerImpl onMenuItemClickListener) {
        Vibrator vibrator = (Vibrator) context.getSystemService(context.VIBRATOR_SERVICE);
        vibrator.vibrate(100);
        PopupMenu menu = new PopupMenu(context, anchor);
        List<ExpandType> popMenuExpandList = getPopMenuExpandList(context, menuType);
        for (ExpandType expandType : popMenuExpandList) {
            menu.getMenu().add(0, expandType.getCode(), 0, expandType.getType());
        }
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == ExpandType.COPY.getCode()) {
                    if (onMenuItemClickListener != null) {
                        onMenuItemClickListener.onCopyClick(item);
                    }
                } else if (item.getItemId() == ExpandType.DOWNLOAD_IMAGE.getCode()) {
                    if (onMenuItemClickListener != null) {
                        onMenuItemClickListener.onDownloadClick(item);
                    }
                } else if (item.getItemId() == ExpandType.VOICE_MODE_RECEIVER.getCode()
                        || item.getItemId() == ExpandType.VOICE_MODE_SPEAKER.getCode()) {
                    if (onMenuItemClickListener != null) {
                        onMenuItemClickListener.onToggleVoiceMode(context, item);
                    }
                }
                return false;
            }
        });
        menu.show();
        return menu;
    }

    private static List<ExpandType> getPopMenuExpandList(Context context, int menuType) {
        List<ExpandType> list = new ArrayList<>();
        if (menuType == TEXT) {
            list.add(ExpandType.COPY);
            return list;
        } else if (menuType == IMAGE) {
            list.add(ExpandType.DOWNLOAD_IMAGE);
        } else if (menuType == VOICE) {
            int mode = (int) SPManager.getInstance(context).get(SPManager.SP_VOICE_MODE, ExpandType.VOICE_MODE_RECEIVER.getCode());
            if (mode == ExpandType.VOICE_MODE_RECEIVER.getCode()) {
                list.add(ExpandType.VOICE_MODE_SPEAKER);
            } else {
                list.add(ExpandType.VOICE_MODE_RECEIVER);
            }
        }
        return list;
    }

    public static class OnMenuItemClickListenerImpl implements OnMenuItemClickListener {

        public OnMenuItemClickListenerImpl() {
        }

        @Override
        public void onCopyClick(MenuItem menuItem) {

        }

        @Override
        public void onDownloadClick(MenuItem menuItem) {

        }

        @Override
        public void onToggleVoiceMode(Context context, MenuItem menuItem) {
            if (menuItem.getItemId() == ExpandType.VOICE_MODE_RECEIVER.getCode()) {
                SPManager.getInstance(context).set(SPManager.SP_VOICE_MODE, ExpandType.VOICE_MODE_RECEIVER.getCode());
                Toast.makeText(context, "已经切换成听筒播放模式", Toast.LENGTH_SHORT).show();
            } else if (menuItem.getItemId() == ExpandType.VOICE_MODE_SPEAKER.getCode()) {
                SPManager.getInstance(context).set(SPManager.SP_VOICE_MODE, ExpandType.VOICE_MODE_SPEAKER.getCode());
                Toast.makeText(context, "已经切换成扬声器播放模式", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private interface OnMenuItemClickListener {
        void onCopyClick(MenuItem menuItem);

        void onDownloadClick(MenuItem menuItem);

        void onToggleVoiceMode(Context context, MenuItem menuItem);
    }

}
