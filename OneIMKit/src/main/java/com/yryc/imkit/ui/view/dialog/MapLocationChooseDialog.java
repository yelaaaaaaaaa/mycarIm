package com.yryc.imkit.ui.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.yryc.imkit.R;
import com.yryc.imkit.utils.NavigationUtil;

/**
 * @author : Mai_Xiao_Peng
 * @email : Mai_Xiao_Peng@163.com
 * @time : 2018/7/31 10:08
 * @describe :
 */


public class MapLocationChooseDialog extends Dialog implements View.OnClickListener {

    private View contentView;
    private Button btnChooseGaode;
    private View btnChooseBaidu;
    private View btnCancel;
    private View lineGaode;
    private double[] location;
    private String poiName;

    public MapLocationChooseDialog(@NonNull Context context) {
        this(context, R.style.BaseDialog);
    }

    public MapLocationChooseDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        initDialog();
        initView();
    }

    private void initDialog() {
        contentView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_map_location_choose, null);
        setContentView(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        layoutParams.width = getContext().getResources().getDisplayMetrics().widthPixels;
        contentView.setLayoutParams(layoutParams);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
    }

    private void initView() {
        btnChooseGaode = contentView.findViewById(R.id.bt_choose_gaode);
        btnChooseBaidu = contentView.findViewById(R.id.bt_choose_baidu);
        btnChooseGaode.setOnClickListener(this);
        btnChooseBaidu.setOnClickListener(this);
        btnCancel = contentView.findViewById(R.id.bt_cancel);
        btnCancel.setOnClickListener(this);
        boolean isGaodePackageInstalled = NavigationUtil.isPackageInstalled(getContext(), NavigationUtil.GAODE_PACKAGENAME);
        boolean isBaiduPackageInstalled = NavigationUtil.isPackageInstalled(getContext(), NavigationUtil.BAIDU_PACKAGENAME);
        if (isGaodePackageInstalled) {
            btnChooseGaode.setVisibility(View.VISIBLE);
        } else {
            btnChooseGaode.setVisibility(View.GONE);
        }
        if (isBaiduPackageInstalled) {
            btnChooseBaidu.setVisibility(View.VISIBLE);
        } else {
            btnChooseBaidu.setVisibility(View.GONE);
        }
    }

    public void show(double[] location, String poiName) {
        this.location = location;
        this.poiName = poiName;
        show();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bt_choose_gaode) {
            NavigationUtil.gaodeLocation(getContext(), location, poiName);
        } else if (v.getId() == R.id.bt_choose_baidu) {
            NavigationUtil.baiduLocation(getContext(), location, poiName);
        } else if (v.getId() == R.id.bt_cancel) {
            dismiss();
        }
    }
}
