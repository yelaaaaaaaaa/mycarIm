package com.yryc.imkit.ui.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.yryc.imkit.R;
import com.yryc.imkit.widget.emoji.widget.EmojiBoard;

import org.jivesoftware.smack.util.StringUtils;

/**
 * @author : Mai_Xiao_Peng
 * @email : Mai_Xiao_Peng@163.com
 * @time : 2018/12/5 12:02
 * @describe :
 */


public class ImEmojiView extends LinearLayout {

    private View view;
    private EmojiBoard ebEmojiBoard;
    private ImExtension mImExtension;

    public ImEmojiView(Context context) {
        this(context, null);
    }

    public ImEmojiView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImEmojiView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr);
    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr) {
        view = View.inflate(context, R.layout.view_im_emoji, this);
        ebEmojiBoard = view.findViewById(R.id.eb_emoji_board);
        ebEmojiBoard.setItemClickListener(new EmojiBoard.OnEmojiItemClickListener() {//表情框点击事件
            @Override
            public void onClick(String code) {
                EditText etMsg = mImExtension.getEtMessage();
                if (etMsg == null || StringUtils.isNullOrEmpty(code)) return;
                if (code.equals("/DEL")) {//删除图标
                    etMsg.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
                } else {//插入表情
                    etMsg.getText().insert(etMsg.getSelectionStart(), code);
                }
            }
        });
        setVisibility(View.GONE);
    }

    public void setImExtension(ImExtension mImExtension) {
        this.mImExtension = mImExtension;
    }
}
