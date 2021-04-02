package com.yryc.imkit.ui.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.TypedValue;
import android.view.Gravity;

import com.yryc.imkit.constant.Align;
import com.yryc.imlib.model.chat.ChatRich;

/**
 * @author : Mai_Xiao_Peng+
 * @email : Mai_Xiao_Peng@163.com
 * @time : 2019/3/28 10:36
 * @describe :
 */


public class FTextView extends AppCompatTextView {

    private Context context;
    private ChatRich.Body.Content content;

    public FTextView(Context context) {
        super(context);
        this.context = context;
    }

    public void init(ChatRich.Body.Content content) {
        this.content = content;
        initView();
    }

    private void initView() {
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
        setGravity(returnTextGravity(content.getAlign()));
        if (content.getStyle() != null) {
            SpannableString spannableString = new SpannableString(content.getContent());
            for (ChatRich.Body.Style style : content.getStyle()) {
                int end = style.getLoc() + style.getLen() > spannableString.length() ? spannableString.length() : style.getLoc() + style.getLen();
                ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor(style.getColor()));
                spannableString.setSpan(colorSpan, style.getLoc(), end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                RelativeSizeSpan sizeSpan = new RelativeSizeSpan(style.getSize() / 10.0f);
                spannableString.setSpan(sizeSpan, style.getLoc(), end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                if (style.isBold()) {
                    StyleSpan styleBold = new StyleSpan(Typeface.BOLD);
                    spannableString.setSpan(styleBold, style.getLoc(), end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                }
            }
            setText(spannableString);
        }
    }

    /**
     * 获取对齐方式
     *
     * @param align
     * @return
     */
    private int returnTextGravity(int align) {
        if (align == Align.LEFT.getCode()) {
            return Gravity.LEFT;
        } else if (align == Align.RIGHT.getCode()) {
            return Gravity.RIGHT;
        } else if (align == Align.TOP.getCode()) {
            return Gravity.TOP;
        } else if (align == Align.CENTER.getCode()) {
            return Gravity.CENTER;
        } else if (align == Align.BOTTOM.getCode()) {
            return Gravity.BOTTOM;
        }
        return Gravity.LEFT;
    }
}
