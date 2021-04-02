package com.yryc.imkit.ui.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.yryc.imkit.R;
import com.yryc.imkit.utils.KeyboardUtils;


/**
 * @author : Mai_Xiao_Peng
 * @email : Mai_Xiao_Peng@163.com
 * @time : 2018/12/22 9:33
 * @describe :添加常用语
 */


public class LanguageDialog extends Dialog implements View.OnClickListener {

    private View view;
    private EditText etLanguageAdd;
    private TextView tvLanguageAddTip;
    private TextView tvCancel;
    private TextView tvConfirm;
    private onDialogListener onDialogListener;
    private Context context;

    public LanguageDialog(@NonNull Context context) {
        super(context);
        this.context = context;
        initView();
    }

    private void initView() {
        view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_language, null);
        setContentView(view);
        etLanguageAdd = findViewById(R.id.et_language_add);
        etLanguageAdd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    etLanguageAdd.setCursorVisible(true);
                } else {
                    etLanguageAdd.setCursorVisible(false);
                }
            }
        });
        etLanguageAdd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                tvLanguageAddTip.setText(String.format(getContext().getResources().getString(R.string.im_add_language_length_tip), s.length()));
            }
        });
        tvLanguageAddTip = findViewById(R.id.tv_language_add_tip);
        tvLanguageAddTip.setText(String.format(getContext().getResources().getString(R.string.im_add_language_length_tip), 0));
        tvCancel = findViewById(R.id.tv_cancel);
        tvConfirm = findViewById(R.id.tv_confirm);
        tvCancel.setOnClickListener(this);
        tvConfirm.setOnClickListener(this);
        getWindow().setGravity(Gravity.CENTER);
        setCanceledOnTouchOutside(false);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_cancel) {
            dismiss();
        } else if (v.getId() == R.id.tv_confirm) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Activity activity = (Activity) context;
                    KeyboardUtils.hideSoftInput(activity);
                }
            }, 100);
//            if (!TextUtils.isEmpty(etLanguageAdd.getText().toString())) {
//                OIMClient.getInstance().addOneWordDataInfo(etLanguageAdd.getText().toString());
//            }
        }
    }

    public interface onDialogListener {
        void onConfirmClick(View v, String s);
    }

    public void setOnDialogListener(LanguageDialog.onDialogListener onDialogListener) {
        this.onDialogListener = onDialogListener;
    }

    public void setEtLanguageAddText(String text) {
        etLanguageAdd.setText(text);
    }

    public void dismissDialog() {
        etLanguageAdd.setText("");
        dismiss();
    }
}
