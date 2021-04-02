package com.yryc.imkit.widget.refresh.impl;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.yryc.imkit.widget.refresh.SmartRefreshLayout;
import com.yryc.imkit.widget.refresh.api.RefreshHeader;
import com.yryc.imkit.widget.refresh.api.RefreshInternal;
import com.yryc.imkit.widget.refresh.api.RefreshKernel;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 刷新头部包装
 * Created by SCWANG on 2017/5/26.
 */
public class RefreshHeaderWrapper extends RefreshInternalWrapper implements RefreshHeader, InvocationHandler {

    private RefreshKernel mRefreshKernel;
    private Method mRequestDrawBackgroundForFooterMethod;
    private Method mRequestRemeasureHeightForFooterMethod;
    private Method mRequestNeedTouchEventWhenLoadingMethod;

    public RefreshHeaderWrapper(View wrapper) {
        super(wrapper);
    }

    @Override
    public void onInitialized(@NonNull RefreshKernel kernel, int height, int extendHeight) {
        if (mWrapperView instanceof RefreshInternal) {
            RefreshKernel proxy = (RefreshKernel) Proxy.newProxyInstance(RefreshKernel.class.getClassLoader(), new Class[]{RefreshKernel.class}, this);
            proxy.requestDrawBackgroundForFooter(0);
            proxy.requestRemeasureHeightForFooter();
            proxy.requestNeedTouchEventWhenLoading(false);
            mRefreshKernel = kernel;
            ((RefreshInternal) mWrapperView).onInitialized(proxy, height, extendHeight);
        } else {
            ViewGroup.LayoutParams params = mWrapperView.getLayoutParams();
            if (params instanceof SmartRefreshLayout.LayoutParams) {
                kernel.requestDrawBackgroundForHeader(((SmartRefreshLayout.LayoutParams) params).backgroundColor);
            }
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object returnValue = null;
        if (mRefreshKernel != null) {
            if (method.equals(mRequestDrawBackgroundForFooterMethod)) {
                mRefreshKernel.requestDrawBackgroundForHeader((int) args[0]);
            } else if (method.equals(mRequestRemeasureHeightForFooterMethod)) {
                mRefreshKernel.requestRemeasureHeightForHeader();
            } else if (method.equals(mRequestNeedTouchEventWhenLoadingMethod)) {
                mRefreshKernel.requestNeedTouchEventWhenRefreshing((boolean) args[0]);
            } else {
                returnValue = method.invoke(mRefreshKernel, args);
            }
        }
        if (method.getReturnType().equals(RefreshKernel.class)) {
            if (mRefreshKernel == null && RefreshKernel.class.equals(method.getDeclaringClass())) {
                if (mRequestDrawBackgroundForFooterMethod == null) {
                    mRequestDrawBackgroundForFooterMethod = method;
                } else if (mRequestRemeasureHeightForFooterMethod == null) {
                    mRequestRemeasureHeightForFooterMethod = method;
                } else if (mRequestNeedTouchEventWhenLoadingMethod == null) {
                    mRequestNeedTouchEventWhenLoadingMethod = method;
                }
            }
            return proxy;
        }
        return returnValue;
    }
}
