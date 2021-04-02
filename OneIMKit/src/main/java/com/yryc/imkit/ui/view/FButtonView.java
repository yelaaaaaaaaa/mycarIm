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
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.yryc.imkit.constant.Align;
import com.yryc.imkit.constant.RichType;
import com.yryc.imlib.model.chat.ChatRich;


import java.util.List;

/**
 * @author : Mai_Xiao_Peng+
 * @email : Mai_Xiao_Peng@163.com
 * @time : 2019/3/28 10:36
 * @describe :
 */


public class FButtonView extends AppCompatTextView {

    private Context context;
    private ChatRich chatRich;
    private ChatRich.Body.Button chatRichBodyButton;
    private String messageId;

    public FButtonView(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public void init(ChatRich chatRich, ChatRich.Body.Button chatRichBodyButton) {
        this.chatRich = chatRich;
        this.chatRichBodyButton = chatRichBodyButton;
        this.messageId = chatRich.getMessageId();
    }

    private void initView() {
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
    }

    /**
     * 显示按钮UI
     */
    public void showButtonUI() {
        if (chatRichBodyButton == null) return;
        modifyButtonStyle(chatRichBodyButton.getAlign(), chatRichBodyButton.getStyle(), chatRichBodyButton.getContent());
    }

    /**
     * 显示按钮结果UI
     */
    public void showButtonResultUI() {
        if (chatRichBodyButton.getActionResult() != null) {
            modifyButtonStyle(chatRichBodyButton.getActionResult().getAlign(), chatRichBodyButton.getActionResult().getStyle(), chatRichBodyButton.getActionResult().getContent());
        }
    }

    /**
     * 改变UI
     */
    public void changeButtonClickUI() {
        if (chatRichBodyButton.getActionType() == RichType.API.getCode() || chatRichBodyButton.getActionType() == RichType.SCHEME.getCode()) {
            LinearLayout parent = (LinearLayout) getParent();
            parent.removeAllViews();
            parent.addView(FButtonView.this);
            chatRich.getBody().setResult(chatRichBodyButton);
            String json = new Gson().toJson(chatRich.getBody());
            //OIMClient.getInstance().updateJsonStringBody(messageId, json);
            chatRich.setJson(json);
            chatRich.getGeneralMessageDataInfo().setJsonStringBody(json);
            showButtonResultUI();
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

    /**
     * 修改按钮样式
     *
     * @param align
     * @param styleList
     * @param content
     */
    private void modifyButtonStyle(int align, List<ChatRich.Body.Style> styleList, String content) {
        setGravity(returnTextGravity(align));
        SpannableString spannableString = new SpannableString(content);
        if (styleList != null && styleList.size() > 0) {
            for (ChatRich.Body.Style style : styleList) {
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
        }
        setText(spannableString);
    }
}
