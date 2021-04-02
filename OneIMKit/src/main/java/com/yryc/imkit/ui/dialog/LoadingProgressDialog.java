package com.yryc.imkit.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.yryc.imkit.R;


/**
 * Description:
 * <p>
 * Author: czk
 * Date：2020/5/16 11:28
 */
public class LoadingProgressDialog extends Dialog {

    TextView tvLoadDesc;

    public LoadingProgressDialog(@NonNull Context context) {
        this(context, R.style.BaseDialog);
    }

    public LoadingProgressDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        initDialog();
    }

    private void initDialog() {
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_loading, null);
        setContentView(contentView);
        getWindow().setGravity(Gravity.CENTER);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        tvLoadDesc = findViewById(R.id.tv_load_desc);
    }

    public void show(String text) {
        tvLoadDesc.setText(text);
        show();
    }

}
