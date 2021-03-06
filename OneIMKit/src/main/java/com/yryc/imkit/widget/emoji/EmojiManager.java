package com.yryc.imkit.widget.emoji;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;

import com.yryc.imkit.R;

import java.util.ArrayList;
import java.util.List;

public class EmojiManager {

    private static Context gContext;
    private static ArrayList<Integer> emojiCodeList = new ArrayList<>();//编码
    private static ArrayList<Integer> emojiResourceList = new ArrayList<>();//资源文件

    public static void init(Context context) {
        gContext = context.getApplicationContext();
        Resources resources = gContext.getResources();
        int[] codes = resources.getIntArray(R.array.emoji_code_list);
        TypedArray array = resources.obtainTypedArray(R.array.emoji_res_list);
        if (codes.length != array.length()) {//数据长度必须一致
            array.recycle();
            throw new IndexOutOfBoundsException("Code and resource are not match in Emoji xml.");
        }
        for (int i = 0; i < codes.length; i++) {
            emojiCodeList.add(codes[i]);
            emojiResourceList.add(array.getResourceId(i, -1));
        }
        array.recycle();
    }

//    public static int dip2px(float dpValue) {
//        float scale = gContext.getResources().getDisplayMetrics().density;
//        return (int) (dpValue * scale + 0.5f);
//    }

    public static int getSize() {//得到总数据
        return emojiCodeList.size();
    }

    public static int getCode(int position) {//得到编码
        return emojiCodeList.get(position);
    }

    public static List<Integer> getResourceList(int start, int count) {//得到每页的数据
        return new ArrayList<>(emojiResourceList.subList(start, count));
    }

    private static int getResourceByCode(int code) throws Resources.NotFoundException {
        for (int i = 0; i < emojiCodeList.size(); i++) {
            if (emojiCodeList.get(i) == code) {
                return emojiResourceList.get(i);
            }
        }
        throw new Resources.NotFoundException("Unsupported emoji code <" + code + ">, which is not in Emoji list.");
    }

    public static CharSequence parse(String text, float textSize) {
        if (text == null) {
            return "";
        }
        final char[] chars = text.toCharArray();
        final SpannableStringBuilder ssb = new SpannableStringBuilder(text);
        int codePoint;
        boolean isSurrogatePair;
        for (int i = 0; i < chars.length; i++) {
            if (Character.isHighSurrogate(chars[i])) {
                continue;
            } else if (Character.isLowSurrogate(chars[i])) {
                if (i > 0 && Character.isSurrogatePair(chars[i - 1], chars[i])) {
                    codePoint = Character.toCodePoint(chars[i - 1], chars[i]);
                    isSurrogatePair = true;
                } else {
                    continue;
                }
            } else {
                codePoint = (int) chars[i];
                isSurrogatePair = false;
            }
            if (emojiCodeList.contains(codePoint)) {
                Bitmap bitmap = BitmapFactory.decodeResource(gContext.getResources(), getResourceByCode(codePoint));
                BitmapDrawable bmpDrawable = new BitmapDrawable(gContext.getResources(), bitmap);
                bmpDrawable.setBounds(0, 0, (int) textSize, (int) textSize);
                CenterImageSpan imageSpan = new CenterImageSpan(bmpDrawable);
                ssb.setSpan(imageSpan, isSurrogatePair ? i - 1 : i, i + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return ssb;
    }

    private static class CenterImageSpan extends ImageSpan {

        public CenterImageSpan(Drawable draw) {
            super(draw);
        }

        @Override
        public void draw(Canvas canvas, CharSequence text, int start, int end, float x,
                         int top, int y, int bottom, Paint paint) {
            Drawable b = getDrawable();
            Paint.FontMetricsInt fm = paint.getFontMetricsInt();
            int transY = ((bottom - top) - getDrawable().getBounds().bottom) / 2 + top;
            canvas.save();
            canvas.translate(x, transY);
            b.draw(canvas);
            canvas.restore();
        }
    }
}
