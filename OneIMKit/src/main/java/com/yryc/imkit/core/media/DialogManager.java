package com.yryc.imkit.core.media;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yryc.imkit.R;


public class DialogManager {

    private Dialog mDialog;

    private RelativeLayout rlVoice;
    private TextView tvVoice;
    private RelativeLayout rlVoiceCancel;
    private TextView tvVoiceCancel;
    private Context mContext;

    public DialogManager(Context context) {
        mContext = context;
    }

    public void showRecordingDialog() {
        mDialog = new Dialog(mContext, R.style.Theme_audioDialog);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.dialog_manager, null);
        mDialog.setContentView(view);
        mDialog.setCanceledOnTouchOutside(false);
        rlVoice = view.findViewById(R.id.rl_voice);
        tvVoice = view.findViewById(R.id.tv_voice);
        rlVoiceCancel = view.findViewById(R.id.rl_voice_cancel);
        tvVoiceCancel = view.findViewById(R.id.tv_voice_cancel);
        mDialog.show();

    }

    /**
     * 设置正在录音时的dialog界面
     */
    public void recording() {
        if (mDialog != null && mDialog.isShowing()) {
            rlVoice.setVisibility(View.VISIBLE);
            tvVoice.setVisibility(View.VISIBLE);
            rlVoiceCancel.setVisibility(View.GONE);
            tvVoiceCancel.setVisibility(View.GONE);
            rlVoice.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.yuyin_voice_1));
            tvVoice.setText(R.string.up_for_cancel);
        }
    }

    /**
     * 取消界面
     */
    public void wantToCancel() {
        if (mDialog != null && mDialog.isShowing()) {
            rlVoice.setVisibility(View.GONE);
            tvVoice.setVisibility(View.GONE);
            rlVoiceCancel.setVisibility(View.VISIBLE);
            tvVoiceCancel.setVisibility(View.VISIBLE);
            rlVoiceCancel.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.yuyin_cancel));
            tvVoiceCancel.setText(R.string.want_to_cancel);
        }

    }

    // 时间过短
    public void tooShort() {
        if (mDialog != null && mDialog.isShowing()) {
            rlVoiceCancel.setVisibility(View.VISIBLE);
            tvVoiceCancel.setVisibility(View.VISIBLE);
            rlVoice.setVisibility(View.GONE);
            tvVoice.setVisibility(View.GONE);
            rlVoiceCancel.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.yuyin_gantanhao));
            tvVoiceCancel.setText(R.string.time_too_short);
        }
    }

    // 隐藏dialog
    public void dimissDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
            mDialog = null;
        }
    }

    public void updateVoiceLevel(int level) {
        if (level > 0 && level < 6) {

        } else {
            level = 5;
        }
        if (mDialog != null && mDialog.isShowing()) {
            int resId = mContext.getResources().getIdentifier("yuyin_voice_" + level,
                    "drawable", mContext.getPackageName());
            rlVoice.setBackgroundResource(resId);
        }
    }

    public TextView getTipsTxt() {
        return tvVoice;
    }

}
