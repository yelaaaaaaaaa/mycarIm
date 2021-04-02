package com.yryc.imkit.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

/**
 * author: Mai_Xiao_Peng
 * email  : Mai_Xiao_Peng@163.com
 * time  : 2017/4/20
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        beforeSetContentView();
        setContentView(getLayoutId());
        mContext = this;
        initView();
        initData();
        initOther(savedInstanceState);
    }

    protected void beforeSetContentView() {

    }

    protected abstract int getLayoutId();

    protected void initOther(Bundle savedInstanceState) {

    }

    protected abstract void initView();

    protected abstract void initData();

    protected void setSupportFragment(int contentId, Fragment fragment) {
        FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
        beginTransaction.replace(contentId, fragment);
        beginTransaction.commitAllowingStateLoss();
    }

    /**
     * 打开界面
     */
    public void startActivity(Class<?> dest) {
        Intent intent = new Intent(mContext, dest);
        mContext.startActivity(intent);
    }

    /**
     * 打开界面
     */
    public void startActivity(Class<?> dest, Bundle bundle) {
        Intent intent = new Intent(mContext, dest);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }

}
