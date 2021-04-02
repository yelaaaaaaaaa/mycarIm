package com.yryc.imkit.widget.refresh.api;

import com.yryc.imkit.widget.refresh.listener.OnRefreshListener;

/**
 * 刷新加载组合监听器
 * @deprecated 使用 {@link com.yryc.imkit.widget.refresh.listener.OnRefreshLoadMoreListener} 代替
 * Created by SCWANG on 2017/5/26.
 */
@Deprecated
public interface OnRefreshLoadmoreListener extends OnRefreshListener, OnLoadmoreListener {
}
