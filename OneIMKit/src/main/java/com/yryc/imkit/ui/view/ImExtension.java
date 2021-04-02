package com.yryc.imkit.ui.view;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yryc.imkit.R;
import com.yryc.imkit.constant.MessageType;
import com.yryc.imkit.core.media.AudioRecordButton;
import com.yryc.imkit.im.ImKeyBoard;
import com.yryc.imkit.widget.emoji.widget.EmojiEdittext;
import com.yryc.imlib.model.chat.ChatText;
import com.yryc.imlib.model.chat.ChatVoice;
import com.yryc.imkit.utils.CommonUtils;
import com.yryc.imkit.utils.OssUtils;

/**
 * @author : Mai_Xiao_Peng
 * @email : Mai_Xiao_Peng@163.com
 * @time : 2018/12/5 11:39
 * @describe :
 */


public class ImExtension extends LinearLayout implements View.OnClickListener {

    private final String TAG = ImExtension.class.getSimpleName().toLowerCase();

    private View view;
    private FrameLayout flContainer;
    private EditText etMessage;
    private TextView tvSend;
    private ImageView ivMore;
    private ImEmojiView imEmojiView;
    private ImMoreView imMoreView;
    private ImageView ivEmoji;
    private ImageView ivVoice;
    private AudioRecordButton arbRecord;
    private Gson gson = new Gson();
    private Activity activity;
    private ImKeyBoard imKeyBoard;

    public ImExtension(Context context) {
        this(context, null);
    }

    public ImExtension(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImExtension(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        activity = (Activity) context;
        initView(context, attrs, defStyleAttr);
    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr) {
        view = View.inflate(context, R.layout.view_im_extension, this);
        flContainer = view.findViewById(R.id.fl_container);
        arbRecord = view.findViewById(R.id.arb_record);
        arbRecord.setTag(false);
        arbRecord.setAudioFinishRecorderListener(new AudioRecordButton.AudioFinishRecorderListener() {
            @Override
            public void onFinished(float seconds, String filePath) {
                Log.i(TAG, String.format("%s:%s", "setAudioFinishRecorderListener->onFinished", String.valueOf(seconds) + " " + filePath));
                if (mListener != null) {
                    mListener.onUploadStart();
                }
                OssUtils.uploadOssServer(getContext(), "user-head", "upload", filePath, new OssUtils.OnOssUploadStateListener() {
                    @Override
                    public void onOssUploadSuccess(String ossPath) {
                        ChatVoice.Body body = new ChatVoice.Body();
                        body.setLength(seconds);
                        body.setContent(ossPath);
                        body.setType(MessageType.VOICE.getType());
                        CommonUtils.sendMessage(getContext(), MessageType.VOICE, body);
                        if (mListener != null) {
                            mListener.onUploadSuccess();
                        }
                    }

                    @Override
                    public void onOssUploadError(Exception e) {
                        Log.e(TAG, e.getMessage());
                        if (mListener != null) {
                            mListener.onUploadError();
                        }
                    }
                });
            }
        });
        ivEmoji = view.findViewById(R.id.iv_emoji);
        ivEmoji.setOnClickListener(this);
        ivVoice = view.findViewById(R.id.iv_voice);
        ivVoice.setImageResource(R.drawable.ic_im_voice);
        ivVoice.setOnClickListener(this);
        etMessage = view.findViewById(R.id.et_message);
        tvSend = view.findViewById(R.id.tv_send);
        tvSend.setOnClickListener(this);
        ivMore = view.findViewById(R.id.iv_more);
        ivMore.setOnClickListener(this);
        etMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0) {
                    tvSend.setVisibility(View.VISIBLE);
                    ivMore.setVisibility(View.INVISIBLE);
                } else {
                    tvSend.setVisibility(View.INVISIBLE);
                    ivMore.setVisibility(View.VISIBLE);
                }
            }
        });
        imEmojiView = new ImEmojiView(getContext());
        imEmojiView.setImExtension(this);
        imMoreView = new ImMoreView(getContext());
        flContainer.addView(imEmojiView);
        flContainer.addView(imMoreView);
    }

    public void setImKeyBoard(ImKeyBoard mImKeyBoard) {
        imKeyBoard = mImKeyBoard;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.iv_voice) {
            boolean tag = (boolean) arbRecord.getTag();
            if (tag == false) {
                ivVoice.setImageResource(R.drawable.ic_im_keyboard);
                arbRecord.setTag(true);
                etMessage.setVisibility(View.INVISIBLE);
                arbRecord.setVisibility(View.VISIBLE);
                if (imKeyBoard != null) {
                    imKeyBoard.hidePanelAndKeyBoard(etMessage);
                }
            } else {
                ivVoice.setImageResource(R.drawable.ic_im_voice);
                arbRecord.setTag(false);
                etMessage.setVisibility(View.VISIBLE);
                arbRecord.setVisibility(View.INVISIBLE);
            }
        } else if (view.getId() == R.id.tv_send) {
            ChatText.Body body = new ChatText.Body();
            body.setType(MessageType.TEXT.getType());
            body.setContent(etMessage.getText().toString().trim());
            CommonUtils.sendMessage(getContext(), MessageType.TEXT, body);
            etMessage.setText("");
        }
    }

    public ImMoreView getImMoreView() {
        return imMoreView;
    }

    public ImEmojiView getImEmojiView() {
        return imEmojiView;
    }

    public EditText getEtMessage() {
        return etMessage;
    }

    public FrameLayout getFlContainer() {
        return flContainer;
    }

    public ImageView getIvMore() {
        return ivMore;
    }

    public ImageView getIvEmoji() {
        return ivEmoji;
    }

    //    显示文字输入框，隐藏语音框
    public void showMessageEditText() {
        boolean tag = (boolean) arbRecord.getTag();
        if (tag) {
            ivVoice.setImageResource(R.drawable.ic_im_voice);
            arbRecord.setTag(false);
            etMessage.setVisibility(View.VISIBLE);
            arbRecord.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 功能区是否展示
     *
     * @return
     */
    public boolean isShown() {
        boolean isShown = false;
        for (int i = 0; i < flContainer.getChildCount(); i++) {
            if (flContainer.getChildAt(i).isShown()) {
                isShown = true;
            }
        }
        return isShown;
    }

    /**
     * 更多功能区是否展示
     *
     * @return
     */
    public boolean isMoreShown() {
        return flContainer.getChildAt(1) != null && flContainer.getChildAt(1).isShown();
    }

    /**
     * 表情区是否展示
     *
     * @return
     */
    public boolean isEmojiShown() {
        return flContainer.getChildAt(0) != null && flContainer.getChildAt(0).isShown();
    }

    /**
     * 隐藏功能区
     */
    public void hidePanel() {
        for (int i = 0; i < flContainer.getChildCount(); i++) {
            if (flContainer.getChildAt(i).isShown()) {
                flContainer.getChildAt(i).setVisibility(View.GONE);
            }
        }
    }

    public void setChatMessage(String chatMessage) {
        etMessage.setText(chatMessage);
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == View.GONE) {
            imMoreView.resetShowState(true);
        }
    }

    public interface AudioUploadListener {
        void onUploadStart();

        void onUploadSuccess();

        void onUploadError();
    }

    private AudioUploadListener mListener;

    public void setAudioUploadListener(AudioUploadListener listener) {
        mListener = listener;
    }
}
