package com.yryc.imkit.widget.emoji.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.style.DynamicDrawableSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.yryc.imkit.R;
import com.yryc.imkit.widget.emoji.EmojiManager;
import com.yryc.imkit.widget.emoji.EmojiconHandler;


public class EmojiEdittext extends EditText {
    private Context context;
    private View view = null;
    private int mEmojiconSize;
    private int mEmojiconAlignment;
    private int mEmojiconTextSize;
    private boolean mUseSystemDefault = false;

    public EmojiEdittext(Context context) {
        super(context);
        this.context = context;
        initView(context, null);
    }

    public EmojiEdittext(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView(context, attrs);
    }

    public EmojiEdittext(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView(context, attrs);
    }

    public EmojiEdittext(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        mEmojiconSize = (int) getTextSize();
        mEmojiconTextSize = (int) getTextSize();
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.Emojicon);
        mEmojiconSize = (int) a.getDimension(R.styleable.Emojicon_emojiconSize, getTextSize());
        mEmojiconAlignment = a.getInt(R.styleable.Emojicon_emojiconAlignment, DynamicDrawableSpan.ALIGN_BASELINE);
        mUseSystemDefault = a.getBoolean(R.styleable.Emojicon_emojiconUseSystemDefault, false);
        a.recycle();
        mEmojiconTextSize = (int) getTextSize();
        setText(getText());
        addTextChangedListener(textWatcher);
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (view != null)
                view.setEnabled(!editable.toString().isEmpty());//???????????????????????????????????????
            int start = getSelectionStart();
            int end = getSelectionEnd();
            removeTextChangedListener(this);//????????????
            updateText();
            CharSequence cs = EmojiManager.parse(editable.toString(), getTextSize());
            setText(cs, BufferType.SPANNABLE);
            setSelection(start, end);
            addTextChangedListener(this);
        }
    };

    public void setEmojiconSize(int pixels) {
        mEmojiconSize = pixels;
        updateText();
    }

    private void updateText() {
        EmojiconHandler.addEmojis(getContext(), getText(), mEmojiconSize, mEmojiconAlignment, mEmojiconTextSize, mUseSystemDefault);
    }

    public void setUseSystemDefault(boolean useSystemDefault) {
        mUseSystemDefault = useSystemDefault;
    }

    /**
     * ?????????????????????????????????????????????
     */
    public void isEnable(View view) {
        this.view = view;
    }
}
