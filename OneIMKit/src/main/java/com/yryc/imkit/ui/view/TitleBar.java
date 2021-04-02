package com.yryc.imkit.ui.view;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yryc.imkit.R;
import com.yryc.imkit.utils.PixelUtils;

/**
 * @author : Mai_Xiao_Peng
 * @email : Mai_Xiao_Peng@163.com
 * @time : 2018/12/11 14:28
 * @describe :
 */


public class TitleBar extends LinearLayout implements View.OnClickListener {

    public static final int TITLE_TYPE_CENTER = 1;
    public static final int TITLE_TYPE_LEFT = 2;

    private ImageView ivLeftIcon;
    private TextView tvTitle;
    private TextView tvRightText;
    private OnTitleBarEventListener onTitleBarEventListener;

    public TitleBar(Context context) {
        this(context, null);
    }

    public TitleBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr);
    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr) {
        View view = View.inflate(getContext(), R.layout.include_title_bar_white, this);
        ivLeftIcon = view.findViewById(R.id.iv_toolbar_left_icon);
        ivLeftIcon.setOnClickListener(this);
        tvTitle = view.findViewById(R.id.tv_toolbar_title);
        tvTitle.setOnClickListener(this);
        tvRightText = view.findViewById(R.id.tv_toolbar_right_text);
        tvRightText.setOnClickListener(this);
        setTitle(TITLE_TYPE_CENTER, "标题栏");
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_toolbar_left_icon) {
            Activity activity = (Activity) getContext();
            activity.finish();
        } else if (v.getId() == R.id.tv_toolbar_right_text) {
            if (onTitleBarEventListener != null) {
                onTitleBarEventListener.onRightTextClick();
            }
        } else if (v.getId() == R.id.tv_toolbar_title) {
            if (onTitleBarEventListener != null) {
                onTitleBarEventListener.onTitleTextClick();
            }
        }
    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    public void setTitle(int titleType, String title) {
        RelativeLayout.LayoutParams titleLayoutParams = (RelativeLayout.LayoutParams) tvTitle.getLayoutParams();
        if (titleType == TITLE_TYPE_LEFT) {
            titleLayoutParams.removeRule(RelativeLayout.CENTER_IN_PARENT);
            titleLayoutParams.addRule(RelativeLayout.RIGHT_OF, ivLeftIcon.getId());
            titleLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
            titleLayoutParams.setMargins(PixelUtils.dp2px(getContext(), 10), 0, 0, 0);
        } else if (titleType == TITLE_TYPE_CENTER) {
            titleLayoutParams.removeRule(RelativeLayout.RIGHT_OF);
            titleLayoutParams.removeRule(RelativeLayout.CENTER_VERTICAL);
            titleLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            titleLayoutParams.setMargins(0, 0, 0, 0);
        }
        tvTitle.setLayoutParams(titleLayoutParams);
        setTitle(title);
    }

    public void setRightText(String rightText) {
        tvRightText.setText(rightText);
    }

    public interface OnTitleBarEventListener {
        void onRightTextClick();

        void onTitleTextClick();
    }

    public void setOnTitleBarEventListener(OnTitleBarEventListener onTilleBarEventListener) {
        this.onTitleBarEventListener = onTilleBarEventListener;
    }
}
