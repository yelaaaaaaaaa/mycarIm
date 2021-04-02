package com.yryc.imkit.im;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.yryc.imkit.R;
import com.yryc.imkit.ui.view.ImExtension;
import com.yryc.imkit.utils.KeyboardUtils;
import com.yryc.imkit.utils.ScreenUtils;
import com.yryc.imlib.utils.SPManager;


/**
 * @author : Mai_Xiao_Peng
 * @email : Mai_Xiao_Peng@163.com
 * @time : 2019/1/4 14:40
 * @describe :
 */


public class ImKeyBoard {

    private final Activity activity;
    private final View contentView;
    private ImExtension imExtension;
    private boolean isShowKeyboard = false;

    private Handler handler = new Handler();

    public ImKeyBoard(Activity activity, View contentView, ImExtension imExtension) {
        this.activity = activity;
        this.contentView = contentView;
        this.imExtension = imExtension;
        init(activity, contentView, imExtension);
    }

    private void init(Activity activity, View contentView, ImExtension imExtension) {
        this.imExtension.getEtMessage().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (imExtension.isShown()) {
                    updateSoftInputMethod(activity, WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
                    KeyboardUtils.showSoftInput(imExtension.getEtMessage());
                    isShowKeyboard = true;
                } else {
                    updateSoftInputMethod(activity, WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                    KeyboardUtils.showSoftInput(imExtension.getEtMessage());
                    isShowKeyboard = true;
                }
                updateEmojiImg();
                return false;
            }
        });
        this.contentView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    updateSoftInputMethod(activity, WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
                    hidePanelKeyBoard();
                    KeyboardUtils.hideSoftInput(activity);
                    isShowKeyboard = false;
                    updateEmojiImg();
                }
                return false;
            }
        });
        this.imExtension.getIvMore().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imExtension.isShown()) {
                    if (!imExtension.isMoreShown()) {
                        showMorePanelAll();
                    } else if (isShowKeyboard) {
                        KeyboardUtils.hideSoftInput(activity);
                        isShowKeyboard = false;
                    } else {
                        KeyboardUtils.showSoftInput(imExtension.getEtMessage());
                        isShowKeyboard = true;
                    }
                } else {
                    showMorePanelAll();
                }
                updateEmojiImg();
            }
        });
        this.imExtension.getIvEmoji().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imExtension.isShown()) {
                    if (!imExtension.isEmojiShown()) {
                        showEmojiPanelAll();
                    } else if (isShowKeyboard) {
                        KeyboardUtils.hideSoftInput(activity);
                        isShowKeyboard = false;
                    } else {
                        KeyboardUtils.showSoftInput(imExtension.getEtMessage());
                        isShowKeyboard = true;
                    }
                } else {
                    showEmojiPanelAll();
                }
                updateEmojiImg();
            }
        });
        this.activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        if (!SPManager.getInstance(activity).contain(SPManager.SP_KEYBOARD_HEIGHT)) {
            imExtension.postDelayed(new Runnable() {
                @Override
                public void run() {
                    KeyboardUtils.showSoftInput(imExtension.getEtMessage());
                    isShowKeyboard = true;
                }
            }, 100);
        }
    }

    //    更新表情按钮图标
    private void updateEmojiImg() {
        if (!isShowKeyboard && imExtension.isShown() && imExtension.isEmojiShown()) {
            imExtension.getIvEmoji().setImageResource(R.drawable.ic_im_keyboard);
        } else {
            imExtension.getIvEmoji().setImageResource(R.drawable.ic_im_emoji);
        }
    }

    /**
     * 隐藏面板键盘
     */
    public void hidePanelAndKeyBoard(View view) {
        updateSoftInputMethod(activity, WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        hidePanelKeyBoard();
        KeyboardUtils.hideSoftInput(activity, view);
        isShowKeyboard = false;
    }


    /**
     * 隐藏面板键盘
     */
    public void hidePanelKeyBoard() {
        if (imExtension.isShown()) {
            hidePanel();
            updateEmojiImg();
        }
    }

    /**
     * 显示更多面板隐藏键盘
     */
    private void showMorePanelAll() {
        updateSoftInputMethod(activity, WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        showMorePanel();
        KeyboardUtils.hideSoftInput(activity);
        isShowKeyboard = false;
    }

    /**
     * 显示表情面板隐藏键盘
     */
    private void showEmojiPanelAll() {
        updateSoftInputMethod(activity, WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        showEmojiPanel();
        KeyboardUtils.hideSoftInput(activity);
        isShowKeyboard = false;
    }

    /**
     * 显示更多面板
     */
    private void showMorePanel() {
        imExtension.showMessageEditText();
        int softKeyboardHeight = (int) SPManager.getInstance(activity).get(SPManager.SP_KEYBOARD_HEIGHT, 0);
        imExtension.getImEmojiView().setVisibility(View.GONE);
        imExtension.getImMoreView().getLayoutParams().height = softKeyboardHeight;
        imExtension.getImMoreView().setVisibility(View.VISIBLE);
    }

    /**
     * 显示表情面板
     */
    private void showEmojiPanel() {
        imExtension.showMessageEditText();
        int softKeyboardHeight = (int) SPManager.getInstance(activity).get(SPManager.SP_KEYBOARD_HEIGHT, 0);
        imExtension.getImMoreView().setVisibility(View.GONE);
        imExtension.getImEmojiView().getLayoutParams().height = softKeyboardHeight;
        imExtension.getImEmojiView().setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏面板
     */
    private void hidePanel() {
        if (imExtension.isShown()) {
            imExtension.hidePanel();
        }
    }

    /**
     * 获取键盘的高度
     */
    public int getSoftKeyboardHeight() {
        Rect rect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        //屏幕当前可见高度，不包括状态栏
        int displayHeight = rect.bottom - rect.top;
        //屏幕可用高度
        int availableHeight = ScreenUtils.getAvailableScreenHeight(activity);
        //计算键盘高度
        int softInputHeight = availableHeight - displayHeight - ScreenUtils.getStatusBarHeight(activity);
        return softInputHeight;
    }

    /**
     * 更改输入法软键盘弹出方式
     *
     * @param activity
     * @param softInputMode
     */
    public void updateSoftInputMethod(Activity activity, int softInputMode) {
        if (!activity.isFinishing()) {
            WindowManager.LayoutParams params = activity.getWindow().getAttributes();
            if (params.softInputMode != softInputMode) {
                params.softInputMode = softInputMode;
                activity.getWindow().setAttributes(params);
            }
        }
    }

}
