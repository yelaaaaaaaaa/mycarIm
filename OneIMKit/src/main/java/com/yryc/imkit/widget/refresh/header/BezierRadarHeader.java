package com.yryc.imkit.widget.refresh.header;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

import com.yryc.imkit.R;
import com.yryc.imkit.widget.refresh.api.RefreshHeader;
import com.yryc.imkit.widget.refresh.api.RefreshKernel;
import com.yryc.imkit.widget.refresh.api.RefreshLayout;
import com.yryc.imkit.widget.refresh.constant.RefreshState;
import com.yryc.imkit.widget.refresh.constant.SpinnerStyle;
import com.yryc.imkit.widget.refresh.header.bezierradar.RippleView;
import com.yryc.imkit.widget.refresh.header.bezierradar.RoundDotView;
import com.yryc.imkit.widget.refresh.header.bezierradar.RoundProgressView;
import com.yryc.imkit.widget.refresh.header.bezierradar.WaveView;
import com.yryc.imkit.widget.refresh.util.DensityUtil;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/**
 * 贝塞尔曲线类雷达风格刷新组件
 * Created by lcodecore on 2016/10/2.
 */

@SuppressWarnings({"UnusedReturnValue", "unused"})
public class BezierRadarHeader extends FrameLayout implements RefreshHeader {

    private WaveView mWaveView;
    private RippleView mRippleView;
    private RoundDotView mDotView;
    private RoundProgressView mProgressView;
    private boolean mEnableHorizontalDrag = false;
    private boolean mIsRunning;
    private Integer mAccentColor;
    private Integer mPrimaryColor;

    //<editor-fold desc="FrameLayout">
    public BezierRadarHeader(Context context) {
        this(context,null);
    }

    public BezierRadarHeader(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public BezierRadarHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr);
    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr) {
        setMinimumHeight(DensityUtil.dp2px(100));

        mWaveView = new WaveView(getContext());
        mRippleView = new RippleView(getContext());
        mDotView = new RoundDotView(getContext());
        mProgressView = new RoundProgressView(getContext());
        if (isInEditMode()) {
            this.addView(mWaveView, MATCH_PARENT, MATCH_PARENT);
            this.addView(mProgressView, MATCH_PARENT, MATCH_PARENT);
            mWaveView.setHeadHeight(1000);
        } else {
            this.addView(mWaveView, MATCH_PARENT, MATCH_PARENT);
            this.addView(mDotView, MATCH_PARENT, MATCH_PARENT);
            this.addView(mProgressView, MATCH_PARENT, MATCH_PARENT);
            this.addView(mRippleView, MATCH_PARENT, MATCH_PARENT);
            mProgressView.setScaleX(0);
            mProgressView.setScaleY(0);
        }


        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BezierRadarHeader);

        mEnableHorizontalDrag = ta.getBoolean(R.styleable.BezierRadarHeader_srlEnableHorizontalDrag, mEnableHorizontalDrag);
        if (ta.hasValue(R.styleable.BezierRadarHeader_srlPrimaryColor)) {
            setPrimaryColor(ta.getColor(R.styleable.BezierRadarHeader_srlPrimaryColor, 0));
        }
        if (ta.hasValue(R.styleable.BezierRadarHeader_srlAccentColor)) {
            setAccentColor(ta.getColor(R.styleable.BezierRadarHeader_srlAccentColor, 0));
        }

        ta.recycle();
    }

    //</editor-fold>

    //<editor-fold desc="API">
    public BezierRadarHeader setPrimaryColor(@ColorInt int color) {
        mPrimaryColor = color;
        mWaveView.setWaveColor(color);
        mProgressView.setBackColor(color);
        return this;
    }

    public BezierRadarHeader setAccentColor(@ColorInt int color) {
        mAccentColor = color;
        mDotView.setDotColor(color);
        mRippleView.setFrontColor(color);
        mProgressView.setFrontColor(color);
        return this;
    }

    public BezierRadarHeader setPrimaryColorId(@ColorRes int colorId) {
        setPrimaryColor(ContextCompat.getColor(getContext(), colorId));
        return this;
    }

    public BezierRadarHeader setAccentColorId(@ColorRes int colorId) {
        setAccentColor(ContextCompat.getColor(getContext(), colorId));
        return this;
    }

    public BezierRadarHeader setEnableHorizontalDrag(boolean enable) {
        this.mEnableHorizontalDrag = enable;
        if (!enable) {
            mWaveView.setWaveOffsetX(-1);
        }
        return this;
    }

    //</editor-fold>

    //<editor-fold desc="RefreshHeader">
    @Override
    @Deprecated
    public void setPrimaryColors(@ColorInt int ... colors) {
        if (colors.length > 0 && mPrimaryColor == null) {
            setPrimaryColor(colors[0]);
            mPrimaryColor = null;
        }
        if (colors.length > 1 && mAccentColor == null) {
            setAccentColor(colors[1]);
            mAccentColor = null;
        }
    }

    @NonNull
    public View getView() {
        return this;
    }

    @NonNull
    @Override
    public SpinnerStyle getSpinnerStyle() {
        return SpinnerStyle.Scale;
    }

    @Override
    public void onInitialized(@NonNull RefreshKernel kernel, int height, int extendHeight) {
    }

    @Override
    public boolean isSupportHorizontalDrag() {
        return mEnableHorizontalDrag;
    }

    @Override
    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {
        mWaveView.setWaveOffsetX(offsetX);
        mWaveView.invalidate();
    }

    @Override
    public void onPulling(float percent, int offset, int height, int extendHeight) {
        mWaveView.setHeadHeight(Math.min(height, offset));
        mWaveView.setWaveHeight((int)(1.9f* Math.max(0, offset - height)));
        mDotView.setFraction(percent);
        if (mIsRunning) {
            mWaveView.invalidate();
        }
    }

    @Override
    public void onReleasing(float percent, int offset, int height, int extendHeight) {
        onPulling(percent, offset, height, extendHeight);
    }

    @Override
    public void onReleased(final RefreshLayout layout, int height, int extendHeight) {
        mIsRunning = true;
        mWaveView.setHeadHeight(height);
        ValueAnimator animator = ValueAnimator.ofInt(
                mWaveView.getWaveHeight(), 0,
                -(int)(mWaveView.getWaveHeight()*0.8),0,
                -(int)(mWaveView.getWaveHeight()*0.4f),0);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mWaveView.setWaveHeight((int) animation.getAnimatedValue()/2);
                mWaveView.invalidate();
            }
        });
        animator.setInterpolator(new DecelerateInterpolator());
        animator.setDuration(800);
        animator.start();
        /*处理圈圈进度条**/
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(1, 0);
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mDotView.setVisibility(INVISIBLE);
                mProgressView.animate().scaleX((float) 1.0);
                mProgressView.animate().scaleY((float) 1.0);
                layout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mProgressView.startAnim();
                    }
                }, 200);
            }
        });

        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.setDuration(300);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mDotView.setAlpha((Float) animation.getAnimatedValue());
            }
        });
        valueAnimator.start();
    }

    @Override
    public void onStartAnimator(@NonNull RefreshLayout layout, int height, int extendHeight) {

    }

    @Override
    public int onFinish(@NonNull RefreshLayout layout, boolean success) {
        mProgressView.stopAnim();
        mProgressView.animate().scaleX(0f);
        mProgressView.animate().scaleY(0f);
        mRippleView.setVisibility(VISIBLE);
        mRippleView.startReveal();
        return 400;
    }

    @Override
    public void onStateChanged(RefreshLayout refreshLayout, RefreshState oldState, RefreshState newState) {
        switch (newState) {
            case None:
                mRippleView.setVisibility(GONE);
                mDotView.setAlpha(1);
                mDotView.setVisibility(VISIBLE);
                break;
            case PullDownToRefresh:
                mProgressView.setScaleX(0);
                mProgressView.setScaleY(0);
                break;
            case PullUpToLoad:
                break;
            case Refreshing:
                break;
            case Loading:
                break;
        }
    }
    //</editor-fold>
}
