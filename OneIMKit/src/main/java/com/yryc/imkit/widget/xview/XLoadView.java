package com.yryc.imkit.widget.xview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.yryc.imkit.R;

/**
 * @author : Mai_Xiao_Peng
 * @email : Mai_Xiao_Peng@163.com
 * @time : 2019/1/3 15:25
 * @describe :
 */


public class XLoadView extends FrameLayout {

    private View loadingView;
    private View successView;
    private View errorView;
    private View emptyView;
    private XLoadViewCreator xLoadViewCreator;
    private int emptyLayoutId;
    private int successLayoutId;
    private int errorLayoutId;
    private int loadingLayoutId;

    public XLoadView(@NonNull Context context) {
        this(context, null);
    }

    public XLoadView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        xLoadViewCreator = new XLoadViewCreator(context);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.XLoadView);
        emptyLayoutId = typedArray.getResourceId(R.styleable.XLoadView_xlv_empty, 0);
        successLayoutId = typedArray.getResourceId(R.styleable.XLoadView_xlv_success, 0);
        errorLayoutId = typedArray.getResourceId(R.styleable.XLoadView_xlv_error, 0);
        loadingLayoutId = typedArray.getResourceId(R.styleable.XLoadView_xlv_loading, 0);
        typedArray.recycle();
        if (loadingLayoutId != 0) {
            setLoadingView(xLoadViewCreator.createLoadingView(loadingLayoutId));
        }
        if (emptyLayoutId != 0) {
            setEmptyView(xLoadViewCreator.createEmptyView(emptyLayoutId));
        }
        if (errorLayoutId != 0) {
            setErrorView(xLoadViewCreator.createErrorView(errorLayoutId));
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        setSuccessView();
    }

    public void setSuccessView() {
        if (this.successView != null) {
            removeView(this.successView);
        }
        if (getChildCount() > 0) {
            successView = getChildAt(getChildCount() - 1);
        } else {
            if (successLayoutId != 0) {
                successView = xLoadViewCreator.createSuccessView(successLayoutId);
                addView(successView);
            }
        }
    }

    public void setErrorView(View errorView) {
        if (this.errorView != null) {
            removeView(this.errorView);
        }
        this.errorView = errorView;
        addView(errorView);
    }

    public void setLoadingView(View loadingView) {
        if (this.loadingView != null) {
            removeView(this.loadingView);
        }
        this.loadingView = loadingView;
        addView(loadingView);
    }

    public void setEmptyView(View emptyView) {
        if (this.emptyView != null) {
            removeView(this.emptyView);
        }
        this.emptyView = emptyView;
        addView(emptyView);
    }

    public void visibleLoadingView() {
        post(new Runnable() {
            @Override
            public void run() {
                if (loadingView != null) {
                    loadingView.setVisibility(VISIBLE);
                }
                if (successView != null) {
                    successView.setVisibility(INVISIBLE);
                }
                if (errorView != null) {
                    errorView.setVisibility(GONE);
                }
                if (emptyView != null) {
                    emptyView.setVisibility(GONE);
                }
            }
        });
    }

    public void visibleSuccessView() {
        post(new Runnable() {
            @Override
            public void run() {
                if (loadingView != null) {
                    loadingView.setVisibility(GONE);
                }
                if (successView != null) {
                    successView.setVisibility(VISIBLE);
                }
                if (errorView != null) {
                    errorView.setVisibility(GONE);
                }
                if (emptyView != null) {
                    emptyView.setVisibility(GONE);
                }
            }
        });
    }

    public void visibleErrorView() {
        post(new Runnable() {
            @Override
            public void run() {
                if (loadingView != null) {
                    loadingView.setVisibility(GONE);
                }
                if (successView != null) {
                    successView.setVisibility(INVISIBLE);
                }
                if (errorView != null) {
                    errorView.setVisibility(VISIBLE);
                }
                if (emptyView != null) {
                    emptyView.setVisibility(GONE);
                }
            }
        });
    }

    public void visibleEmptyView() {
        post(new Runnable() {
            @Override
            public void run() {
                if (loadingView != null) {
                    loadingView.setVisibility(GONE);
                }
                if (successView != null) {
                    successView.setVisibility(INVISIBLE);
                }
                if (errorView != null) {
                    errorView.setVisibility(GONE);
                }
                if (emptyView != null) {
                    emptyView.setVisibility(VISIBLE);
                }
            }
        });
    }

}
