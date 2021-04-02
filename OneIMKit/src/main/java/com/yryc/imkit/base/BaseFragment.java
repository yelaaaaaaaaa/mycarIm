package com.yryc.imkit.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yryc.imkit.ui.dialog.LoadingProgressDialog;

import org.greenrobot.eventbus.EventBus;

/**
 * author: Mai_Xiao_Peng
 * email  : Mai_Xiao_Peng@163.com
 * time  : 2017/4/20
 * desc : not mvp
 */

public abstract class BaseFragment extends Fragment {

    protected final String TAG = getClass().getSimpleName().toLowerCase();
    protected View mView;
    protected LoadingProgressDialog progressDialog;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        EventBus.getDefault().register(this);
        mView = View.inflate(getContext(), getLayoutId(), null);
        initView();
        progressDialog = new LoadingProgressDialog(getActivity());
        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    protected abstract int getLayoutId();

    protected abstract void initView();

    protected abstract void initData();

    protected void setSupportFragment(int contentId, Fragment fragment) {
        FragmentTransaction beginTransaction = getChildFragmentManager().beginTransaction();
        beginTransaction.replace(contentId, fragment);
        beginTransaction.commitAllowingStateLoss();
    }

    /**
     * 显示加载进度dialog
     */
    public void showProgressDialog(String text) {
        if (progressDialog.isShowing()) return;
        progressDialog.show(text);
    }

    /**
     * 隐藏加载进度dialog
     */
    public void hideProgressDialog() {
        if (!progressDialog.isShowing()) return;
        progressDialog.dismiss();
    }
}
