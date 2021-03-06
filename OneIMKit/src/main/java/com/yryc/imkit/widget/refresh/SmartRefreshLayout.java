package com.yryc.imkit.widget.refresh;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.AbsListView;
import android.widget.Scroller;
import android.widget.TextView;

import com.yryc.imkit.R;
import com.yryc.imkit.widget.refresh.api.DefaultRefreshFooterCreater;
import com.yryc.imkit.widget.refresh.api.DefaultRefreshFooterCreator;
import com.yryc.imkit.widget.refresh.api.DefaultRefreshHeaderCreater;
import com.yryc.imkit.widget.refresh.api.DefaultRefreshHeaderCreator;
import com.yryc.imkit.widget.refresh.api.OnLoadmoreListener;
import com.yryc.imkit.widget.refresh.api.OnRefreshLoadmoreListener;
import com.yryc.imkit.widget.refresh.api.RefreshContent;
import com.yryc.imkit.widget.refresh.api.RefreshFooter;
import com.yryc.imkit.widget.refresh.api.RefreshHeader;
import com.yryc.imkit.widget.refresh.api.RefreshInternal;
import com.yryc.imkit.widget.refresh.api.RefreshKernel;
import com.yryc.imkit.widget.refresh.api.RefreshLayout;
import com.yryc.imkit.widget.refresh.api.ScrollBoundaryDecider;
import com.yryc.imkit.widget.refresh.constant.DimensionStatus;
import com.yryc.imkit.widget.refresh.constant.RefreshState;
import com.yryc.imkit.widget.refresh.constant.SpinnerStyle;
import com.yryc.imkit.widget.refresh.footer.BallPulseFooter;
import com.yryc.imkit.widget.refresh.header.BezierRadarHeader;
import com.yryc.imkit.widget.refresh.impl.RefreshContentWrapper;
import com.yryc.imkit.widget.refresh.impl.RefreshFooterWrapper;
import com.yryc.imkit.widget.refresh.impl.RefreshHeaderWrapper;
import com.yryc.imkit.widget.refresh.listener.OnLoadMoreListener;
import com.yryc.imkit.widget.refresh.listener.OnMultiPurposeListener;
import com.yryc.imkit.widget.refresh.listener.OnRefreshListener;
import com.yryc.imkit.widget.refresh.listener.OnRefreshLoadMoreListener;
import com.yryc.imkit.widget.refresh.util.DelayedRunnable;
import com.yryc.imkit.widget.refresh.util.DensityUtil;
import com.yryc.imkit.widget.refresh.util.ViscousFluidInterpolator;

import java.util.ArrayList;
import java.util.List;

import static android.view.MotionEvent.obtain;
import static android.view.View.MeasureSpec.AT_MOST;
import static android.view.View.MeasureSpec.EXACTLY;
import static android.view.View.MeasureSpec.getSize;
import static android.view.View.MeasureSpec.makeMeasureSpec;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static com.yryc.imkit.widget.refresh.constant.RefreshState.LoadFinish;
import static com.yryc.imkit.widget.refresh.constant.RefreshState.LoadReleased;
import static com.yryc.imkit.widget.refresh.constant.RefreshState.Loading;
import static com.yryc.imkit.widget.refresh.constant.RefreshState.None;
import static com.yryc.imkit.widget.refresh.constant.RefreshState.PullDownCanceled;
import static com.yryc.imkit.widget.refresh.constant.RefreshState.PullDownToRefresh;
import static com.yryc.imkit.widget.refresh.constant.RefreshState.PullUpCanceled;
import static com.yryc.imkit.widget.refresh.constant.RefreshState.PullUpToLoad;
import static com.yryc.imkit.widget.refresh.constant.RefreshState.RefreshFinish;
import static com.yryc.imkit.widget.refresh.constant.RefreshState.RefreshReleased;
import static com.yryc.imkit.widget.refresh.constant.RefreshState.Refreshing;
import static com.yryc.imkit.widget.refresh.constant.RefreshState.ReleaseToLoad;
import static com.yryc.imkit.widget.refresh.constant.RefreshState.ReleaseToRefresh;
import static com.yryc.imkit.widget.refresh.constant.RefreshState.ReleaseToTwoLevel;
import static com.yryc.imkit.widget.refresh.constant.RefreshState.TwoLevel;
import static com.yryc.imkit.widget.refresh.constant.RefreshState.TwoLevelFinish;
import static com.yryc.imkit.widget.refresh.constant.RefreshState.TwoLevelReleased;
import static java.lang.System.currentTimeMillis;

/**
 * ??????????????????
 * Intelligent RefreshLayout
 * Created by SCWANG on 2017/5/26.
 */
@SuppressLint("RestrictedApi")
@SuppressWarnings({"unused", "WeakerAccess"})
public class SmartRefreshLayout extends ViewGroup implements RefreshLayout, NestedScrollingParent, NestedScrollingChild {

    //<editor-fold desc="???????????? property and variable">

    //<editor-fold desc="????????????">
    protected int mTouchSlop;
    protected int mSpinner;//????????? Spinner
    protected int mLastSpinner;//???????????????Spinner
    protected int mTouchSpinner;//??????????????????Spinner
    protected int mFloorDuration = 250;//??????????????????
    protected int mReboundDuration = 250;//??????????????????
    protected int mScreenHeightPixels;//????????????
    protected float mTouchX;
    protected float mTouchY;
    protected float mLastTouchX;//????????????Header?????????????????????
    protected float mLastTouchY;//????????????????????????
    protected float mDragRate = .5f;
    protected char mDragDirection = 'n';//??????????????? none-n horizontal-h vertical-v
    protected boolean mIsBeingDragged;//??????????????????
    protected boolean mSuperDispatchTouchEvent;//??????????????????????????????
    protected int mFixedHeaderViewId;//????????????????????????Id
    protected int mFixedFooterViewId;//????????????????????????Id

    protected int mMinimumVelocity;
    protected int mMaximumVelocity;
    protected Scroller mScroller;
    protected VelocityTracker mVelocityTracker;
    protected Interpolator mReboundInterpolator;

    //</editor-fold>

    //<editor-fold desc="????????????">
    protected int[] mPrimaryColors;
    protected boolean mEnableRefresh = true;
    protected boolean mEnableLoadMore = false;
    protected boolean mEnableClipHeaderWhenFixedBehind = true;//??? Header FixedBehind ???????????????????????? Header
    protected boolean mEnableClipFooterWhenFixedBehind = true;//??? Footer FixedBehind ???????????????????????? Footer
    protected boolean mEnableHeaderTranslationContent = true;//????????????????????????????????????
    protected boolean mEnableFooterTranslationContent = true;//????????????????????????????????????
    protected boolean mEnableFooterFollowWhenLoadFinished = false;//?????????????????????????????????Footer???????????? 1.0.4-6
    protected boolean mEnablePreviewInEditMode = true;//??????????????????????????????????????????
    protected boolean mEnableOverScrollBounce = true;//????????????????????????
    protected boolean mEnableOverScrollDrag = true;//?????????????????????????????????????????????1.0.4-6
    protected boolean mEnableAutoLoadMore = true;//???????????????????????????????????????????????????
    protected boolean mEnablePureScrollMode = false;//???????????????????????????
    protected boolean mEnableScrollContentWhenLoaded = true;//????????????????????????????????????????????????????????????
    protected boolean mEnableScrollContentWhenRefreshed = true;//??????????????????????????????????????????????????????
    protected boolean mEnableLoadMoreWhenContentNotFull = true;//???????????????????????????????????????????????????????????????
    protected boolean mDisableContentWhenRefresh = false;//???????????????????????????????????????????????????
    protected boolean mDisableContentWhenLoading = false;//???????????????????????????????????????????????????
    protected boolean mFooterNoMoreData = false;//???????????????????????????????????????????????????????????????????????????

    protected boolean mManualLoadMore = false;//?????????????????????LoadMore?????????????????????
    protected boolean mManualNestedScrolling = false;//????????????????????? NestedScrolling?????????????????????
    protected boolean mManualHeaderTranslationContent = false;//?????????????????????????????????????????????
    //</editor-fold>

    //<editor-fold desc="????????????">
    protected OnRefreshListener mRefreshListener;
    protected OnLoadMoreListener mLoadMoreListener;
    protected OnMultiPurposeListener mOnMultiPurposeListener;
    protected ScrollBoundaryDecider mScrollBoundaryDecider;
    //</editor-fold>

    //<editor-fold desc="????????????">
    protected int[] mParentOffsetInWindow = new int[2];
    protected int mTotalUnconsumed;
    protected boolean mNestedScrollInProgress;
    protected NestedScrollingChildHelper mNestedScrollingChildHelper;
    protected NestedScrollingParentHelper mNestedScrollingParentHelper;
    //</editor-fold>

    //<editor-fold desc="????????????">
    /**
     * ????????????
     */
    protected int mHeaderHeight;
    protected DimensionStatus mHeaderHeightStatus = DimensionStatus.DefaultUnNotify;
    /**
     * ????????????
     */
    protected int mFooterHeight;
    protected DimensionStatus mFooterHeightStatus = DimensionStatus.DefaultUnNotify;

    protected int mHeaderInsetStart;    // Header ??????????????????
    protected int mFooterInsetStart;    // Footer ??????????????????
    protected int mHeaderExtendHeight;  //????????????
    protected int mFooterExtendHeight;  //????????????

    protected float mHeaderMaxDragRate = 2.5f;  //??????????????????(????????????/Header??????)
    protected float mFooterMaxDragRate = 2.5f;  //??????????????????(????????????/Footer??????)
    protected float mHeaderTriggerRate = 1.0f;  //?????????????????? ??? HeaderHeight ?????????
    protected float mFooterTriggerRate = 1.0f;  //?????????????????? ??? FooterHeight ?????????

    protected RefreshHeader mRefreshHeader;     //??????????????????
    protected RefreshFooter mRefreshFooter;     //??????????????????
    protected RefreshContent mRefreshContent;   //??????????????????
    //</editor-fold>

    protected Paint mPaint;
    protected Handler mHandler;
    protected RefreshKernel mKernel;
    protected List<DelayedRunnable> mDelayedRunnables;

    protected RefreshState mState = None;          //?????????
    protected RefreshState mViceState = None;      //???????????????????????????????????????????????????

    protected boolean mVerticalPermit = false;                  //??????????????????????????????????????????????????????

    protected long mLastLoadingTime = 0;
    protected long mLastRefreshingTime = 0;

    protected int mHeaderBackgroundColor = 0;                   //???Header??????????????????
    protected int mFooterBackgroundColor = 0;

    protected boolean mHeaderNeedTouchEventWhenRefreshing;      //?????????Header??????????????????
    protected boolean mFooterNeedTouchEventWhenLoading;

    protected boolean mFooterLocked = false;//Footer ??????loading ????????????????????? ????????????????????????

    protected static boolean sManualFooterCreator = false;
    protected static DefaultRefreshFooterCreator sFooterCreator = new DefaultRefreshFooterCreator() {
        @NonNull
        @Override
        public RefreshFooter createRefreshFooter(@NonNull Context context, @NonNull RefreshLayout layout) {
            return new BallPulseFooter(context);
        }
    };
    protected static DefaultRefreshHeaderCreator sHeaderCreator = new DefaultRefreshHeaderCreator() {
        @NonNull
        @Override
        public RefreshHeader createRefreshHeader(@NonNull Context context, @NonNull RefreshLayout layout) {
            return new BezierRadarHeader(context);
        }
    };

    //</editor-fold>

    //<editor-fold desc="???????????? construction methods">
    public SmartRefreshLayout(Context context) {
        super(context);
        this.initView(context, null);
    }

    public SmartRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initView(context, attrs);
    }

    public SmartRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.initView(context, attrs);
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    public SmartRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        setClipToPadding(false);

        DensityUtil density = new DensityUtil();
        ViewConfiguration configuration = ViewConfiguration.get(context);

        mScroller = new Scroller(context);
        mKernel = new RefreshKernelImpl();
        mVelocityTracker = VelocityTracker.obtain();
        mScreenHeightPixels = context.getResources().getDisplayMetrics().heightPixels;
        mReboundInterpolator = new ViscousFluidInterpolator();
        mTouchSlop = configuration.getScaledTouchSlop();
        mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
        mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();

        mNestedScrollingParentHelper = new NestedScrollingParentHelper(this);
        mNestedScrollingChildHelper = new NestedScrollingChildHelper(this);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SmartRefreshLayout);

        ViewCompat.setNestedScrollingEnabled(this, ta.getBoolean(R.styleable.SmartRefreshLayout_srlEnableNestedScrolling, false));
        mDragRate = ta.getFloat(R.styleable.SmartRefreshLayout_srlDragRate, mDragRate);
        mHeaderMaxDragRate = ta.getFloat(R.styleable.SmartRefreshLayout_srlHeaderMaxDragRate, mHeaderMaxDragRate);
        mFooterMaxDragRate = ta.getFloat(R.styleable.SmartRefreshLayout_srlFooterMaxDragRate, mFooterMaxDragRate);
        mHeaderTriggerRate = ta.getFloat(R.styleable.SmartRefreshLayout_srlHeaderTriggerRate, mHeaderTriggerRate);
        mFooterTriggerRate = ta.getFloat(R.styleable.SmartRefreshLayout_srlFooterTriggerRate, mFooterTriggerRate);
        mEnableRefresh = ta.getBoolean(R.styleable.SmartRefreshLayout_srlEnableRefresh, mEnableRefresh);
        mReboundDuration = ta.getInt(R.styleable.SmartRefreshLayout_srlReboundDuration, mReboundDuration);
        mEnableLoadMore = ta.getBoolean(R.styleable.SmartRefreshLayout_srlEnableLoadMore, mEnableLoadMore);
        mHeaderHeight = ta.getDimensionPixelOffset(R.styleable.SmartRefreshLayout_srlHeaderHeight, density.dip2px(100));
        mFooterHeight = ta.getDimensionPixelOffset(R.styleable.SmartRefreshLayout_srlFooterHeight, density.dip2px(60));
        mHeaderInsetStart = ta.getDimensionPixelOffset(R.styleable.SmartRefreshLayout_srlHeaderInsetStart, 0);
        mFooterInsetStart = ta.getDimensionPixelOffset(R.styleable.SmartRefreshLayout_srlFooterInsetStart, 0);
        mDisableContentWhenRefresh = ta.getBoolean(R.styleable.SmartRefreshLayout_srlDisableContentWhenRefresh, mDisableContentWhenRefresh);
        mDisableContentWhenLoading = ta.getBoolean(R.styleable.SmartRefreshLayout_srlDisableContentWhenLoading, mDisableContentWhenLoading);
        mEnableHeaderTranslationContent = ta.getBoolean(R.styleable.SmartRefreshLayout_srlEnableHeaderTranslationContent, mEnableHeaderTranslationContent);
        mEnableFooterTranslationContent = ta.getBoolean(R.styleable.SmartRefreshLayout_srlEnableFooterTranslationContent, mEnableFooterTranslationContent);
        mEnablePreviewInEditMode = ta.getBoolean(R.styleable.SmartRefreshLayout_srlEnablePreviewInEditMode, mEnablePreviewInEditMode);
        mEnableAutoLoadMore = ta.getBoolean(R.styleable.SmartRefreshLayout_srlEnableAutoLoadMore, mEnableAutoLoadMore);
        mEnableOverScrollBounce = ta.getBoolean(R.styleable.SmartRefreshLayout_srlEnableOverScrollBounce, mEnableOverScrollBounce);
        mEnablePureScrollMode = ta.getBoolean(R.styleable.SmartRefreshLayout_srlEnablePureScrollMode, mEnablePureScrollMode);
        mEnableScrollContentWhenLoaded = ta.getBoolean(R.styleable.SmartRefreshLayout_srlEnableScrollContentWhenLoaded, mEnableScrollContentWhenLoaded);
        mEnableScrollContentWhenRefreshed = ta.getBoolean(R.styleable.SmartRefreshLayout_srlEnableScrollContentWhenRefreshed, mEnableScrollContentWhenRefreshed);
        mEnableLoadMoreWhenContentNotFull = ta.getBoolean(R.styleable.SmartRefreshLayout_srlEnableLoadMoreWhenContentNotFull, mEnableLoadMoreWhenContentNotFull);
        mEnableFooterFollowWhenLoadFinished = ta.getBoolean(R.styleable.SmartRefreshLayout_srlEnableFooterFollowWhenLoadFinished, mEnableFooterFollowWhenLoadFinished);
        mEnableClipHeaderWhenFixedBehind = ta.getBoolean(R.styleable.SmartRefreshLayout_srlEnableClipHeaderWhenFixedBehind, mEnableClipHeaderWhenFixedBehind);
        mEnableClipFooterWhenFixedBehind = ta.getBoolean(R.styleable.SmartRefreshLayout_srlEnableClipFooterWhenFixedBehind, mEnableClipFooterWhenFixedBehind);
        mEnableOverScrollDrag = ta.getBoolean(R.styleable.SmartRefreshLayout_srlEnableOverScrollDrag, mEnableOverScrollDrag);
        mFixedHeaderViewId = ta.getResourceId(R.styleable.SmartRefreshLayout_srlFixedHeaderViewId, View.NO_ID);
        mFixedFooterViewId = ta.getResourceId(R.styleable.SmartRefreshLayout_srlFixedFooterViewId, View.NO_ID);

        mManualLoadMore = ta.hasValue(R.styleable.SmartRefreshLayout_srlEnableLoadMore);
        mManualNestedScrolling = ta.hasValue(R.styleable.SmartRefreshLayout_srlEnableNestedScrolling);
        mManualHeaderTranslationContent = ta.hasValue(R.styleable.SmartRefreshLayout_srlEnableHeaderTranslationContent);
        mHeaderHeightStatus = ta.hasValue(R.styleable.SmartRefreshLayout_srlHeaderHeight) ? DimensionStatus.XmlLayoutUnNotify : mHeaderHeightStatus;
        mFooterHeightStatus = ta.hasValue(R.styleable.SmartRefreshLayout_srlFooterHeight) ? DimensionStatus.XmlLayoutUnNotify : mFooterHeightStatus;

        mHeaderExtendHeight = (int) Math.max((mHeaderHeight * (mHeaderMaxDragRate - 1)), 0);
        mFooterExtendHeight = (int) Math.max((mFooterHeight * (mFooterMaxDragRate - 1)), 0);

        int accentColor = ta.getColor(R.styleable.SmartRefreshLayout_srlAccentColor, 0);
        int primaryColor = ta.getColor(R.styleable.SmartRefreshLayout_srlPrimaryColor, 0);
        if (primaryColor != 0) {
            if (accentColor != 0) {
                mPrimaryColors = new int[]{primaryColor, accentColor};
            } else {
                mPrimaryColors = new int[]{primaryColor};
            }
        } else if (accentColor != 0) {
            mPrimaryColors = new int[]{0, accentColor};
        }

        ta.recycle();

    }
    //</editor-fold>

    //<editor-fold desc="???????????? life cycle">

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        final int count = getChildCount();
        if (count > 3) {
            throw new RuntimeException("???????????????3??????View???Most only support three sub view");
        }

        int indexContent = -1;
        int[] indexArray = {1, 0, 2};

        for (int index : indexArray) {
            if (index < count) {
                View view = getChildAt(index);
                if (!(view instanceof RefreshInternal)) {
                    indexContent = index;
                }
                if (RefreshContentWrapper.isScrollableView(view)) {
                    indexContent = index;
                    break;
                }
            }
        }

        int indexHeader = -1;
        int indexFooter = -1;
        if (indexContent >= 0) {
            mRefreshContent = new RefreshContentWrapper(getChildAt(indexContent));
            if (indexContent == 1) {
                indexHeader = 0;
                if (count == 3) {
                    indexFooter = 2;
                }
            } else if (count == 2) {
                indexFooter = 1;
            }
        }

        for (int i = 0; i < count; i++) {
            View view = getChildAt(i);
            if (i == indexHeader || (i != indexFooter && indexHeader == -1 && mRefreshHeader == null && view instanceof RefreshHeader)) {
                mRefreshHeader = (view instanceof RefreshHeader) ? (RefreshHeader) view : new RefreshHeaderWrapper(view);
            } else if (i == indexFooter || (indexFooter == -1 && view instanceof RefreshFooter)) {
                mEnableLoadMore = mEnableLoadMore || !mManualLoadMore;
                mRefreshFooter = (view instanceof RefreshFooter) ? (RefreshFooter) view : new RefreshFooterWrapper(view);
            }
        }


//        //?????????????????????View??????
//        boolean[] unCertainArray = new boolean[count];
//        //???????????????????????? ???View
//        for (int i = 0; i < count; i++) {
//            View view = getChildAt(i);
//            if (view instanceof RefreshHeader && mRefreshHeader == null) {
//                mRefreshHeader = ((RefreshHeader) view);
//            } else if (view instanceof RefreshFooter && mRefreshFooter == null) {
//                mEnableLoadMore = mEnableLoadMore || !mManualLoadMore;
//                mRefreshFooter = ((RefreshFooter) view);
//            } else if (mRefreshContent == null && RefreshContentWrapper.isScrollableView(view)) {
//                mRefreshContent = new RefreshContentWrapper(view);
//            } else {
//                unCertainArray[i] = true;//???????????????
//            }
//        }
//        //????????? ????????????unCertainArray?????????View ????????????????????????
//        for (int i = 0; i < count; i++) {
//            if (unCertainArray[i]) {
//                View view = getChildAt(i);
//                if (count == 1 && mRefreshContent == null) {
//                    mRefreshContent = new RefreshContentWrapper(view);
//                } else if (i == 0 && mRefreshHeader == null) {
//                    mRefreshHeader = new RefreshHeaderWrapper(view);
//                } else if (count == 2 && mRefreshContent == null) {
//                    mRefreshContent = new RefreshContentWrapper(view);
//                } else if (i == 2 && mRefreshFooter == null) {
//                    mEnableLoadMore = mEnableLoadMore || !mManualLoadMore;
//                    mRefreshFooter = new RefreshFooterWrapper(view);
//                } else if (mRefreshContent == null) {
//                    mRefreshContent = new RefreshContentWrapper(view);
//                } else if (i == 1 && count == 2 && mRefreshFooter == null) {
//                    mEnableLoadMore = mEnableLoadMore || !mManualLoadMore;
//                    mRefreshFooter = new RefreshFooterWrapper(view);
//                } else if (mRefreshHeader == null) {
//                    mRefreshHeader = new RefreshHeaderWrapper(view);
//                }
//            }
//        }

        if (isInEditMode()) {
            if (mPrimaryColors != null) {
                if (mRefreshHeader != null) {
                    mRefreshHeader.setPrimaryColors(mPrimaryColors);
                }
                if (mRefreshFooter != null) {
                    mRefreshFooter.setPrimaryColors(mPrimaryColors);
                }
            }

            //????????????
            if (mRefreshContent != null) {
                bringChildToFront(mRefreshContent.getView());
            }
            if (mRefreshHeader != null && mRefreshHeader.getSpinnerStyle() != SpinnerStyle.FixedBehind) {
                bringChildToFront(mRefreshHeader.getView());
            }
            if (mRefreshFooter != null && mRefreshFooter.getSpinnerStyle() != SpinnerStyle.FixedBehind) {
                bringChildToFront(mRefreshFooter.getView());
            }
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (isInEditMode()) return;

        if (mHandler == null) {
            mHandler = new Handler();
        }

        if (mDelayedRunnables != null) {
            for (DelayedRunnable runnable : mDelayedRunnables) {
                mHandler.postDelayed(runnable, runnable.delayMillis);
            }
            mDelayedRunnables.clear();
            mDelayedRunnables = null;
        }

        if (mRefreshHeader == null) {
            mRefreshHeader = sHeaderCreator.createRefreshHeader(getContext(), this);
            if (!(mRefreshHeader.getView().getLayoutParams() instanceof MarginLayoutParams)) {
                if (mRefreshHeader.getSpinnerStyle() == SpinnerStyle.Scale) {
                    addView(mRefreshHeader.getView(), MATCH_PARENT, MATCH_PARENT);
                } else {
                    addView(mRefreshHeader.getView(), MATCH_PARENT, WRAP_CONTENT);
                }
            }
        }
        if (mRefreshFooter == null) {
            mRefreshFooter = sFooterCreator.createRefreshFooter(getContext(), this);
            mEnableLoadMore = mEnableLoadMore || (!mManualLoadMore && sManualFooterCreator);
            if (!(mRefreshFooter.getView().getLayoutParams() instanceof MarginLayoutParams)) {
                if (mRefreshFooter.getSpinnerStyle() == SpinnerStyle.Scale) {
                    addView(mRefreshFooter.getView(), MATCH_PARENT, MATCH_PARENT);
                } else {
                    addView(mRefreshFooter.getView(), MATCH_PARENT, WRAP_CONTENT);
                }
            }
        }

        for (int i = 0, len = getChildCount(); mRefreshContent == null && i < len; i++) {
            View view = getChildAt(i);
            if ((mRefreshHeader == null || view != mRefreshHeader.getView()) &&
                    (mRefreshFooter == null || view != mRefreshFooter.getView())) {
                mRefreshContent = new RefreshContentWrapper(view);
            }
        }

        if (mRefreshContent == null) {
            int padding = DensityUtil.dp2px(20);
            TextView errorView = new TextView(getContext());
            errorView.setTextColor(0xffff6600);
            errorView.setGravity(Gravity.CENTER);
            errorView.setTextSize(20);
            errorView.setPadding(padding, padding, padding, padding);
            errorView.setText("");
            addView(errorView, MATCH_PARENT, MATCH_PARENT);
            mRefreshContent = new RefreshContentWrapper(errorView);
        }

        View fixedHeaderView = mFixedHeaderViewId > 0 ? findViewById(mFixedHeaderViewId) : null;
        View fixedFooterView = mFixedFooterViewId > 0 ? findViewById(mFixedFooterViewId) : null;

        mRefreshContent.setScrollBoundaryDecider(mScrollBoundaryDecider);
        mRefreshContent.setEnableLoadMoreWhenContentNotFull(mEnableLoadMoreWhenContentNotFull);
        mRefreshContent.setUpComponent(mKernel, fixedHeaderView, fixedFooterView);

        if (mSpinner != 0) {
            notifyStateChanged(None);
            mRefreshContent.moveSpinner(mSpinner = 0);
        }

        //????????????
        bringChildToFront(mRefreshContent.getView());
        if (mRefreshHeader.getSpinnerStyle() != SpinnerStyle.FixedBehind) {
            bringChildToFront(mRefreshHeader.getView());
        }
        if (mRefreshFooter.getSpinnerStyle() != SpinnerStyle.FixedBehind) {
            bringChildToFront(mRefreshFooter.getView());
        }

        if (mRefreshListener == null) {
            mRefreshListener = new OnRefreshListener() {
                @Override
                public void onRefresh(RefreshLayout refreshLayout) {
                    refreshLayout.finishRefresh(3000);
                }
            };
        }
        if (mLoadMoreListener == null) {
            mLoadMoreListener = new OnLoadMoreListener() {
                @Override
                public void onLoadMore(RefreshLayout refreshLayout) {
                    refreshLayout.finishLoadMore(2000);
                }
            };
        }
        if (mPrimaryColors != null) {
            mRefreshHeader.setPrimaryColors(mPrimaryColors);
            mRefreshFooter.setPrimaryColors(mPrimaryColors);
        }
        if (!mManualNestedScrolling && !isNestedScrollingEnabled()) {
            for (ViewParent parent = getParent(); parent != null; parent = parent.getParent()) {
                if (parent instanceof NestedScrollingParent) {
                    setNestedScrollingEnabled(true);
                    mManualNestedScrolling = false;
                    break;
                }
            }
        }
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        int minimumHeight = 0;
        final boolean isInEditMode = isInEditMode() && mEnablePreviewInEditMode;

        for (int i = 0, len = getChildCount(); i < len; i++) {
            View child = getChildAt(i);

            if (mRefreshHeader != null && mRefreshHeader.getView() == child) {
                final View headerView = mRefreshHeader.getView();
                final LayoutParams lp = (LayoutParams) headerView.getLayoutParams();
                final int widthSpec = getChildMeasureSpec(widthMeasureSpec, lp.leftMargin + lp.rightMargin, lp.width);
                int heightSpec = heightMeasureSpec;

                if (mHeaderHeightStatus.gteReplaceWith(DimensionStatus.XmlLayoutUnNotify)) {
                    heightSpec = makeMeasureSpec(Math.max(mHeaderHeight - lp.bottomMargin - lp.topMargin, 0), EXACTLY);
                    headerView.measure(widthSpec, heightSpec);
                } else if (mRefreshHeader.getSpinnerStyle() == SpinnerStyle.MatchLayout) {
                    int headerHeight = 0;
                    if (!mHeaderHeightStatus.notified) {
                        measureChild(headerView, widthSpec, makeMeasureSpec(Math.max(getSize(heightSpec) - lp.bottomMargin - lp.topMargin, 0), AT_MOST));
                        headerHeight = headerView.getMeasuredHeight();
                    }
                    headerView.measure(widthSpec, makeMeasureSpec(Math.max(getSize(heightSpec) - lp.bottomMargin - lp.topMargin, 0), EXACTLY));
                    if (headerHeight > 0 && headerHeight != headerView.getMeasuredHeight()) {
                        mHeaderHeight = headerHeight + lp.bottomMargin + lp.topMargin;
                    }
                } else if (lp.height > 0) {
                    if (mHeaderHeightStatus.canReplaceWith(DimensionStatus.XmlExactUnNotify)) {
                        mHeaderHeight = lp.height + lp.bottomMargin + lp.topMargin;
                        mHeaderHeightStatus = DimensionStatus.XmlExactUnNotify;
                    }
                    heightSpec = makeMeasureSpec(lp.height, EXACTLY);
                    headerView.measure(widthSpec, heightSpec);
                } else if (lp.height == WRAP_CONTENT) {
                    heightSpec = makeMeasureSpec(Math.max(getSize(heightMeasureSpec) - lp.bottomMargin - lp.topMargin, 0), AT_MOST);
                    headerView.measure(widthSpec, heightSpec);
                    int measuredHeight = headerView.getMeasuredHeight();
                    if (measuredHeight > 0 && mHeaderHeightStatus.canReplaceWith(DimensionStatus.XmlWrapUnNotify)) {
                        mHeaderHeightStatus = DimensionStatus.XmlWrapUnNotify;
                        mHeaderHeight = headerView.getMeasuredHeight() + lp.bottomMargin + lp.topMargin;
                    } else if (measuredHeight <= 0) {
                        heightSpec = makeMeasureSpec(Math.max(mHeaderHeight - lp.bottomMargin - lp.topMargin, 0), EXACTLY);
                        headerView.measure(widthSpec, heightSpec);
                    }
                } else if (lp.height == MATCH_PARENT) {
                    heightSpec = makeMeasureSpec(Math.max(mHeaderHeight - lp.bottomMargin - lp.topMargin, 0), EXACTLY);
                    headerView.measure(widthSpec, heightSpec);
                } else {
                    headerView.measure(widthSpec, heightSpec);
                }
                if (mRefreshHeader.getSpinnerStyle() == SpinnerStyle.Scale && !isInEditMode) {
                    final int height = Math.max(0, isEnableRefresh() ? mSpinner : 0);
                    heightSpec = makeMeasureSpec(Math.max(height - lp.bottomMargin - lp.topMargin, 0), EXACTLY);
                    headerView.measure(widthSpec, heightSpec);
                }

                if (!mHeaderHeightStatus.notified) {
                    mHeaderHeightStatus = mHeaderHeightStatus.notified();
                    mHeaderExtendHeight = (int) Math.max((mHeaderHeight * (mHeaderMaxDragRate - 1)), 0);
                    mRefreshHeader.onInitialized(mKernel, mHeaderHeight, mHeaderExtendHeight);
                }

                if (isInEditMode && isEnableRefresh()) {
                    minimumHeight += headerView.getMeasuredHeight();
                }
            }

            if (mRefreshFooter != null && mRefreshFooter.getView() == child) {
                final View footerView = mRefreshFooter.getView();
                final LayoutParams lp = (LayoutParams) footerView.getLayoutParams();
                final int widthSpec = getChildMeasureSpec(widthMeasureSpec, lp.leftMargin + lp.rightMargin, lp.width);
                int heightSpec = heightMeasureSpec;
                if (mFooterHeightStatus.gteReplaceWith(DimensionStatus.XmlLayoutUnNotify)) {
                    heightSpec = makeMeasureSpec(Math.max(mFooterHeight - lp.topMargin - lp.bottomMargin, 0), EXACTLY);
                    footerView.measure(widthSpec, heightSpec);
                } else if (mRefreshFooter.getSpinnerStyle() == SpinnerStyle.MatchLayout) {
                    int footerHeight = 0;
                    if (!mFooterHeightStatus.notified) {
                        measureChild(footerView, widthSpec, makeMeasureSpec(getSize(heightSpec) - lp.topMargin - lp.bottomMargin, AT_MOST));
                        footerHeight = footerView.getMeasuredHeight();
                    }
                    footerView.measure(widthSpec, makeMeasureSpec(getSize(heightSpec) - lp.topMargin - lp.bottomMargin, EXACTLY));
                    if (footerHeight > 0 && footerHeight != footerView.getMeasuredHeight()) {
                        mHeaderHeight = footerHeight + lp.topMargin + lp.bottomMargin;
                    }
                } else if (lp.height > 0) {
                    if (mFooterHeightStatus.canReplaceWith(DimensionStatus.XmlExactUnNotify)) {
                        mFooterHeight = lp.height + lp.topMargin + lp.bottomMargin;
                        mFooterHeightStatus = DimensionStatus.XmlExactUnNotify;
                    }
                    heightSpec = makeMeasureSpec(lp.height, EXACTLY);
                    footerView.measure(widthSpec, heightSpec);
                } else if (lp.height == WRAP_CONTENT) {
                    heightSpec = makeMeasureSpec(Math.max(getSize(heightMeasureSpec) - lp.topMargin - lp.bottomMargin, 0), AT_MOST);
                    footerView.measure(widthSpec, heightSpec);
                    int measuredHeight = footerView.getMeasuredHeight();
                    if (measuredHeight > 0 && mFooterHeightStatus.canReplaceWith(DimensionStatus.XmlWrapUnNotify)) {
                        mFooterHeightStatus = DimensionStatus.XmlWrapUnNotify;
                        mFooterHeight = footerView.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
                    } else if (measuredHeight <= 0) {
                        heightSpec = makeMeasureSpec(Math.max(mFooterHeight - lp.topMargin - lp.bottomMargin, 0), EXACTLY);
                        footerView.measure(widthSpec, heightSpec);
                    }
                } else if (lp.height == MATCH_PARENT) {
                    heightSpec = makeMeasureSpec(Math.max(mFooterHeight - lp.topMargin - lp.bottomMargin, 0), EXACTLY);
                    footerView.measure(widthSpec, heightSpec);
                } else {
                    footerView.measure(widthSpec, heightSpec);
                }

                if (mRefreshFooter.getSpinnerStyle() == SpinnerStyle.Scale && !isInEditMode) {
                    final int height = Math.max(0, mEnableLoadMore ? -mSpinner : 0);
                    heightSpec = makeMeasureSpec(Math.max(height - lp.topMargin - lp.bottomMargin, 0), EXACTLY);
                    footerView.measure(widthSpec, heightSpec);
                }

                if (!mFooterHeightStatus.notified) {
                    mFooterHeightStatus = mFooterHeightStatus.notified();
                    mFooterExtendHeight = (int) Math.max((mFooterHeight * (mFooterMaxDragRate - 1)), 0);
                    mRefreshFooter.onInitialized(mKernel, mFooterHeight, mFooterExtendHeight);
                }

                if (isInEditMode && isEnableLoadMore()) {
                    minimumHeight += footerView.getMeasuredHeight();
                }
            }

            if (mRefreshContent != null && mRefreshContent.getView() == child) {
                final LayoutParams lp = (LayoutParams) mRefreshContent.getLayoutParams();
                final int widthSpec = getChildMeasureSpec(widthMeasureSpec,
                        getPaddingLeft() + getPaddingRight() +
                                lp.leftMargin + lp.rightMargin, lp.width);
                final int heightSpec = getChildMeasureSpec(heightMeasureSpec,
                        getPaddingTop() + getPaddingBottom() +
                                lp.topMargin + lp.bottomMargin +
                                ((isInEditMode && isEnableRefresh() && mRefreshHeader != null && (mEnableHeaderTranslationContent || mRefreshHeader.getSpinnerStyle() == SpinnerStyle.FixedBehind)) ? mHeaderHeight : 0) +
                                ((isInEditMode && isEnableLoadMore() && mRefreshFooter != null && (mEnableFooterTranslationContent || mRefreshFooter.getSpinnerStyle() == SpinnerStyle.FixedBehind)) ? mFooterHeight : 0), lp.height);
                mRefreshContent.measure(widthSpec, heightSpec);
                mRefreshContent.onInitialHeaderAndFooter(mHeaderHeight, mFooterHeight);
                minimumHeight += mRefreshContent.getMeasuredHeight();
            }
        }

        setMeasuredDimension(resolveSize(getSuggestedMinimumWidth(), widthMeasureSpec), resolveSize(minimumHeight, heightMeasureSpec));

        mLastTouchX = getMeasuredWidth() / 2;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int paddingLeft = getPaddingLeft();
        final int paddingTop = getPaddingTop();
        final int paddingBottom = getPaddingBottom();


        for (int i = 0, len = getChildCount(); i < len; i++) {
            View child = getChildAt(i);

            if (mRefreshContent != null && mRefreshContent.getView() == child) {
                boolean isPreviewMode = isInEditMode() && mEnablePreviewInEditMode && isEnableRefresh() && mRefreshHeader != null;
                final LayoutParams lp = (LayoutParams) mRefreshContent.getLayoutParams();
                int left = paddingLeft + lp.leftMargin;
                int top = paddingTop + lp.topMargin;
                int right = left + mRefreshContent.getMeasuredWidth();
                int bottom = top + mRefreshContent.getMeasuredHeight();
                if (isPreviewMode && (mEnableHeaderTranslationContent || mRefreshHeader.getSpinnerStyle() == SpinnerStyle.FixedBehind)) {
                    top = top + mHeaderHeight;
                    bottom = bottom + mHeaderHeight;
                }

                mRefreshContent.layout(left, top, right, bottom);
            }
            if (mRefreshHeader != null && mRefreshHeader.getView() == child) {
                boolean isPreviewMode = isInEditMode() && mEnablePreviewInEditMode && isEnableRefresh();
                final View headerView = mRefreshHeader.getView();
                final LayoutParams lp = (LayoutParams) headerView.getLayoutParams();
                int left = lp.leftMargin;
                int top = lp.topMargin + mHeaderInsetStart;
                int right = left + headerView.getMeasuredWidth();
                int bottom = top + headerView.getMeasuredHeight();
                if (!isPreviewMode) {
                    if (mRefreshHeader.getSpinnerStyle() == SpinnerStyle.Translate) {
                        top = top - mHeaderHeight;
                        bottom = bottom - mHeaderHeight;
                        /*
                         * SpinnerStyle.Scale  headerView.getMeasuredHeight() ??????????????????
                         **/
//                    } else if (mRefreshHeader.getSpinnerStyle() == SpinnerStyle.Scale && mSpinner > 0) {
//                        bottom = top + Math.max(Math.max(0, isEnableRefresh() ? mSpinner : 0) - lp.bottomMargin - lp.topMargin, 0);
                    }
                }
                headerView.layout(left, top, right, bottom);
            }
            if (mRefreshFooter != null && mRefreshFooter.getView() == child) {
                final boolean isPreviewMode = isInEditMode() && mEnablePreviewInEditMode && isEnableLoadMore();
                final View footerView = mRefreshFooter.getView();
                final LayoutParams lp = (LayoutParams) footerView.getLayoutParams();
                final SpinnerStyle style = mRefreshFooter.getSpinnerStyle();
                int left = lp.leftMargin;
                int top = lp.topMargin + getMeasuredHeight() - mFooterInsetStart;

                if (isPreviewMode
                        || style == SpinnerStyle.FixedFront
                        || style == SpinnerStyle.FixedBehind) {
                    top = top - mFooterHeight;
                } else if (style == SpinnerStyle.Scale && mSpinner < 0) {
                    top = top - Math.max(isEnableLoadMore() ? -mSpinner : 0, 0);
                }

                int right = left + footerView.getMeasuredWidth();
                int bottom = top + footerView.getMeasuredHeight();
                footerView.layout(left, top, right, bottom);
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        moveSpinner(0, false);
        notifyStateChanged(None);
        mHandler.removeCallbacksAndMessages(null);
        mHandler = null;
        mManualLoadMore = true;
        mManualNestedScrolling = true;
        animationRunnable = null;
        if (reboundAnimator != null) {
            reboundAnimator.removeAllListeners();
            reboundAnimator.removeAllUpdateListeners();
            reboundAnimator.cancel();
            reboundAnimator = null;
        }
    }

    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        View contentView = mRefreshContent != null ? mRefreshContent.getView() : null;
        if (mRefreshHeader != null && mRefreshHeader.getView() == child) {
            if (!isEnableRefresh() || (!mEnablePreviewInEditMode && isInEditMode())) {
                return true;
            }
            if (contentView != null) {
                int bottom = Math.max(contentView.getTop() + contentView.getPaddingTop() + mSpinner, child.getTop());
                if (mHeaderBackgroundColor != 0 && mPaint != null) {
                    mPaint.setColor(mHeaderBackgroundColor);
                    if (mRefreshHeader.getSpinnerStyle() == SpinnerStyle.Scale) {
                        bottom = child.getBottom();
                    } else if (mRefreshHeader.getSpinnerStyle() == SpinnerStyle.Translate) {
                        bottom = child.getBottom() + mSpinner;
                    }
                    canvas.drawRect(child.getLeft(), child.getTop(), child.getRight(), bottom, mPaint);
                }
                if (mEnableClipHeaderWhenFixedBehind && mRefreshHeader.getSpinnerStyle() == SpinnerStyle.FixedBehind) {
                    canvas.save();
                    canvas.clipRect(child.getLeft(), child.getTop(), child.getRight(), bottom);
                    boolean ret = super.drawChild(canvas, child, drawingTime);
                    canvas.restore();
                    return ret;
                }
            }
        }
        if (mRefreshFooter != null && mRefreshFooter.getView() == child) {
            if (!isEnableLoadMore() || (!mEnablePreviewInEditMode && isInEditMode())) {
                return true;
            }
            if (contentView != null) {
                int top = Math.min(contentView.getBottom() - contentView.getPaddingBottom() + mSpinner, child.getBottom());
                if (mFooterBackgroundColor != 0 && mPaint != null) {
                    mPaint.setColor(mFooterBackgroundColor);
                    if (mRefreshFooter.getSpinnerStyle() == SpinnerStyle.Scale) {
                        top = child.getTop();
                    } else if (mRefreshFooter.getSpinnerStyle() == SpinnerStyle.Translate) {
                        top = child.getTop() + mSpinner;
                    }
                    canvas.drawRect(child.getLeft(), top, child.getRight(), child.getBottom(), mPaint);
                }
                if (mEnableClipFooterWhenFixedBehind && mRefreshFooter.getSpinnerStyle() == SpinnerStyle.FixedBehind) {
                    canvas.save();
                    canvas.clipRect(child.getLeft(), top, child.getRight(), child.getBottom());
                    boolean ret = super.drawChild(canvas, child, drawingTime);
                    canvas.restore();
                    return ret;
                }
            }

        }
        return super.drawChild(canvas, child, drawingTime);
    }

    @Override
    public void computeScroll() {
        int lastCurY = mScroller.getCurrY();
        if (mScroller.computeScrollOffset()) {
            int finalY = mScroller.getFinalY();
            if ((finalY < 0 && (mEnableOverScrollDrag || isEnableRefresh()) && mRefreshContent.canRefresh())
                    || (finalY > 0 && (mEnableOverScrollDrag || isEnableLoadMore()) && mRefreshContent.canLoadMore())) {
                if (mVerticalPermit) {
                    float velocity;
                    if (Build.VERSION.SDK_INT >= 14) {
                        velocity = finalY > 0 ? -mScroller.getCurrVelocity() : mScroller.getCurrVelocity();
                    } else {
                        velocity = 1f * (mScroller.getCurrY() - finalY) / Math.max((mScroller.getDuration() - mScroller.timePassed()), 1);
                    }
                    animSpinnerBounce(velocity);
                }
                mScroller.forceFinished(true);
            } else {
                mVerticalPermit = true;//?????????????????????
                invalidate();
            }
        }
    }

    //</editor-fold>

    //<editor-fold desc="???????????? judgement of slide">
    MotionEvent mFalsifyEvent = null;

    @Override
    public boolean dispatchTouchEvent(MotionEvent e) {

        //<editor-fold desc="????????????????????????">
        //---------------------------------------------------------------------------
        //????????????????????????
        //---------------------------------------------------------------------------
        final int action = e.getActionMasked();
        final boolean pointerUp = action == MotionEvent.ACTION_POINTER_UP;
        final int skipIndex = pointerUp ? e.getActionIndex() : -1;

        // Determine focal point
        float sumX = 0, sumY = 0;
        final int count = e.getPointerCount();
        for (int i = 0; i < count; i++) {
            if (skipIndex == i) continue;
            sumX += e.getX(i);
            sumY += e.getY(i);
        }
        final int div = pointerUp ? count - 1 : count;
        final float touchX = sumX / div;
        final float touchY = sumY / div;
        if ((action == MotionEvent.ACTION_POINTER_UP || action == MotionEvent.ACTION_POINTER_DOWN)
                && mIsBeingDragged) {
            mTouchY += touchY - mLastTouchY;
        }
        mLastTouchX = touchX;
        mLastTouchY = touchY;
        //---------------------------------------------------------------------------
        //</editor-fold>

        if (interceptByAnimator(action) || mState.finishing
                || (mState == Loading && mDisableContentWhenLoading)
                || (mState == Refreshing && mDisableContentWhenRefresh)) {
            return false;
        }

        if (mRefreshContent != null) {
            //??? RefreshContent ????????????????????????????????????????????????????????????????????????View??????????????????????????????
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    mVelocityTracker.clear();
                    mVelocityTracker.addMovement(e);
                    mRefreshContent.onActionDown(e);
                    mScroller.forceFinished(true);
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (!mNestedScrollInProgress) {
                        mVelocityTracker.addMovement(e);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if (!mNestedScrollInProgress) {
                        mVelocityTracker.addMovement(e);
                        mVelocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                    }
                case MotionEvent.ACTION_CANCEL:
                    mRefreshContent.onActionUpOrCancel();
            }
        }
        if (mNestedScrollInProgress) {//??????????????????????????????????????????????????????????????????????????????????????? onHorizontalDrag
            int totalUnconsumed = this.mTotalUnconsumed;
            boolean ret = super.dispatchTouchEvent(e);
            //noinspection ConstantConditions
            if (action == MotionEvent.ACTION_MOVE) {
                if (totalUnconsumed == mTotalUnconsumed) {
                    final int offsetX = (int) mLastTouchX;
                    final int offsetMax = getWidth();
                    final float percentX = mLastTouchX / (offsetMax == 0 ? 1 : offsetMax);
                    if (isEnableRefresh() && mSpinner > 0 && mRefreshHeader != null && mRefreshHeader.isSupportHorizontalDrag()) {
                        mRefreshHeader.onHorizontalDrag(percentX, offsetX, offsetMax);
                    } else if (isEnableLoadMore() && mSpinner < 0 && mRefreshFooter != null && mRefreshFooter.isSupportHorizontalDrag()) {
                        mRefreshFooter.onHorizontalDrag(percentX, offsetX, offsetMax);
                    }
                }
//            } else if (action == MotionEvent.ACTION_UP) {
//                startFlingIfNeed(null);
            }

            return ret;
        } else if (!isEnabled()
                || (!isEnableRefresh() && !isEnableLoadMore() && !mEnableOverScrollDrag)
                || (mHeaderNeedTouchEventWhenRefreshing && (mState == Refreshing || mState == TwoLevel))
                || (mFooterNeedTouchEventWhenLoading && (mState == Loading || mState == LoadFinish))) {
            return super.dispatchTouchEvent(e);
        }

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mTouchX = touchX;
                mTouchY = touchY;
                mLastSpinner = 0;
                mTouchSpinner = mSpinner;
                mIsBeingDragged = false;
                mSuperDispatchTouchEvent = super.dispatchTouchEvent(e);
                if (mState == TwoLevel && mTouchY < 5 * getMeasuredHeight() / 6) {
                    mDragDirection = 'h';//?????????????????????????????????????????????
                    return mSuperDispatchTouchEvent;
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                float dx = touchX - mTouchX;
                float dy = touchY - mTouchY;
                if (!mIsBeingDragged && mDragDirection != 'h') {//???????????????????????????  canRefresh canLoadMore ???????????????
                    if (mDragDirection == 'v' || (Math.abs(dy) >= mTouchSlop && Math.abs(dx) < Math.abs(dy))) {//???????????????????????????45???
                        mDragDirection = 'v';
                        if (dy > 0 && (mSpinner < 0 || ((mEnableOverScrollDrag || isEnableRefresh()) && mRefreshContent.canRefresh()))) {
                            mIsBeingDragged = true;
                            mTouchY = touchY - mTouchSlop;//?????? mTouchSlop ??????
                        } else if (dy < 0 && (mSpinner > 0 || ((mEnableOverScrollDrag || isEnableLoadMore()) && ((mState == Loading && mFooterLocked) || mRefreshContent.canLoadMore())))) {
                            mIsBeingDragged = true;
                            mTouchY = touchY + mTouchSlop;//?????? mTouchSlop ??????
                        }
                        if (mIsBeingDragged) {
                            dy = touchY - mTouchY;//?????? mTouchSlop ?????? ???????????? dy
                            if (mSuperDispatchTouchEvent) {//????????????????????????????????????????????????????????????
                                e.setAction(MotionEvent.ACTION_CANCEL);
                                super.dispatchTouchEvent(e);
                            }
                            if (mSpinner > 0 || (mSpinner == 0 && dy > 0)) {
                                mKernel.setState(PullDownToRefresh);
                            } else {
                                mKernel.setState(PullUpToLoad);
                            }
                            getParent().requestDisallowInterceptTouchEvent(true);//?????????????????????????????????
                        }
                    } else if (Math.abs(dx) >= mTouchSlop && Math.abs(dx) > Math.abs(dy) && mDragDirection != 'v') {
                        mDragDirection = 'h';//????????????????????????????????????????????? ???????????? ????????????
                    }
                }
                if (mIsBeingDragged) {
                    int spinner = (int) dy + mTouchSpinner;
                    if ((mViceState.isHeader() && (spinner < 0 || mLastSpinner < 0)) || (mViceState.isFooter() && (spinner > 0 || mLastSpinner > 0))) {
                        mLastSpinner = spinner;
                        long time = e.getEventTime();
                        if (mFalsifyEvent == null) {
                            mFalsifyEvent = obtain(time, time, MotionEvent.ACTION_DOWN, mTouchX + dx, mTouchY, 0);
                            super.dispatchTouchEvent(mFalsifyEvent);
                        }
                        MotionEvent em = obtain(time, time, MotionEvent.ACTION_MOVE, mTouchX + dx, mTouchY + spinner, 0);
                        super.dispatchTouchEvent(em);
                        if (mFooterLocked && dy > mTouchSlop && mSpinner < 0) {
                            mFooterLocked = false;//????????????????????? ??????Footer ?????????
                        }
                        if (spinner > 0 && ((mEnableOverScrollDrag || isEnableRefresh()) && mRefreshContent.canRefresh())) {
                            mTouchY = mLastTouchY = touchY;
                            mTouchSpinner = spinner = 0;
                            mKernel.setState(PullDownToRefresh);
                        } else if (spinner < 0 && ((mEnableOverScrollDrag || isEnableLoadMore()) && mRefreshContent.canLoadMore())) {
                            mTouchY = mLastTouchY = touchY;
                            mTouchSpinner = spinner = 0;
                            mKernel.setState(PullUpToLoad);
                        }
                        if ((mViceState.isHeader() && spinner < 0) || (mViceState.isFooter() && spinner > 0)) {
                            if (mSpinner != 0) {
                                moveSpinnerInfinitely(0);
                            }
                            return true;
                        } else if (mFalsifyEvent != null) {
                            mFalsifyEvent = null;
                            em.setAction(MotionEvent.ACTION_CANCEL);
                            super.dispatchTouchEvent(em);
                        }
                        em.recycle();
                    }
                    moveSpinnerInfinitely(spinner);
                    return true;
                } else if (mFooterLocked && dy > mTouchSlop && mSpinner < 0) {
                    mFooterLocked = false;//????????????????????? ??????Footer ?????????
                }
                break;
            case MotionEvent.ACTION_UP:
                startFlingIfNeed(null);
            case MotionEvent.ACTION_CANCEL:
                mDragDirection = 'n';//??????????????????
                if (mFalsifyEvent != null) {
                    mFalsifyEvent.recycle();
                    mFalsifyEvent = null;
                    long time = e.getEventTime();
                    MotionEvent ec = obtain(time, time, action, mTouchX, touchY, 0);
                    super.dispatchTouchEvent(ec);
                    ec.recycle();
                }
                overSpinner();
                if (mIsBeingDragged) {
                    mIsBeingDragged = false;//??????????????????
                    return true;
                }
                break;
        }
        return super.dispatchTouchEvent(e);
    }

    protected boolean startFlingIfNeed(Float flingVelocity) {
        final float velocity = flingVelocity == null ? mVelocityTracker.getYVelocity() : flingVelocity;
        if (Math.abs(velocity) > mMinimumVelocity) {
            if ((velocity < 0 && ((mEnableOverScrollBounce && (mEnableOverScrollDrag || isEnableLoadMore())) || (mState == Loading && mSpinner >= 0) || (mEnableAutoLoadMore && isEnableLoadMore())))
                    || (velocity > 0 && ((mEnableOverScrollBounce && (mEnableOverScrollDrag || isEnableRefresh())) || (mState == Refreshing && mSpinner <= 0)))) {
                mVerticalPermit = false;//?????????????????????
                mScroller.fling(0, 0, 0, (int) -velocity, 0, 0, -Integer.MAX_VALUE, Integer.MAX_VALUE);
                mScroller.computeScrollOffset();
                invalidate();
            }
            if (velocity * mSpinner < 0 && mState != TwoLevel && mState != mViceState) {
                /*
                 * ????????????????????????????????????
                 * ???????????????????????????????????????????????????
                 * ???????????????:loading refreshing noMoreData
                 */
                animationRunnable = new FlingRunnable(velocity).start();
                return true;
            }
        }
        return false;
    }

    /*
     * ?????????????????????????????????????????????????????????????????????
     */
    protected boolean interceptByAnimator(int action) {
        if (action == MotionEvent.ACTION_DOWN) {
            animationRunnable = null;
            if (reboundAnimator != null) {
                if (mState.finishing) {
                    return true;
                }
                if (mState == PullDownCanceled) {
                    mKernel.setState(PullDownToRefresh);
                } else if (mState == PullUpCanceled) {
                    mKernel.setState(PullUpToLoad);
                }
                reboundAnimator.cancel();
                reboundAnimator = null;
            }
        }
        return reboundAnimator != null;
    }

    /*
     * ????????????????????????????????? SwipeRefreshLayout
     * ????????????????????????????????????????????????
     * ??????????????????????????????????????????????????????????????????????????????
     */
    @Override
    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        // if this is a List < L or another view that doesn't support nested
        // scrolling, ignore this request so that the vertical scroll event
        // isn't stolen
        View target = mRefreshContent.getScrollableView();
        if ((android.os.Build.VERSION.SDK_INT >= 21 || !(target instanceof AbsListView))
                && (target == null || ViewCompat.isNestedScrollingEnabled(target))) {
            super.requestDisallowInterceptTouchEvent(disallowIntercept);
            //} else {
            // Nope.
        }
    }

    //</editor-fold>

    //<editor-fold desc="???????????? state changes">

    protected void notifyStateChanged(RefreshState state) {
        final RefreshState oldState = mState;
        if (oldState != state) {
            mState = state;
            mViceState = state;
            if (mRefreshFooter != null) {
                mRefreshFooter.onStateChanged(this, oldState, state);
            }
            if (mRefreshHeader != null) {
                mRefreshHeader.onStateChanged(this, oldState, state);
            }
            if (mOnMultiPurposeListener != null) {
                mOnMultiPurposeListener.onStateChanged(this, oldState, state);
            }
        }
    }

    protected void setStateDirectLoading() {
        if (mState != Loading) {
            mLastLoadingTime = currentTimeMillis();
//            if (mState != RefreshState.LoadReleased) {
//                if (mState != RefreshState.ReleaseToLoad) {
//                    if (mState != RefreshState.PullUpToLoad) {
//                        mKernel.setState(RefreshState.PullUpToLoad);
//                    }
//                    mKernel.setState(RefreshState.ReleaseToLoad);
//                }
//                notifyStateChanged(RefreshState.LoadReleased);
//                if (mRefreshFooter != null) {
//                    mRefreshFooter.onReleased(this, mFooterHeight, mFooterExtendHeight);
//                }
//            }
            notifyStateChanged(Loading);
            mFooterLocked = true;
            if (mRefreshFooter != null) {
                mRefreshFooter.onStartAnimator(this, mFooterHeight, mFooterExtendHeight);
            }
            if (mLoadMoreListener != null) {
                mLoadMoreListener.onLoadMore(this);
            }
            if (mOnMultiPurposeListener != null) {
                mOnMultiPurposeListener.onLoadMore(this);
                mOnMultiPurposeListener.onFooterStartAnimator(mRefreshFooter, mFooterHeight, mFooterExtendHeight);
            }
        }
    }

    protected void setStateLoading() {
        AnimatorListenerAdapter listener = new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                setStateDirectLoading();
            }
        };
        notifyStateChanged(LoadReleased);
        ValueAnimator animator = animSpinner(-mFooterHeight);
        if (animator != null) {
            animator.addListener(listener);
        }
        if (mRefreshFooter != null) {
            //onReleased ????????????????????? animSpinner ?????? onAnimationEnd ??????
            // ?????? onReleased ?????? ???????????? ??? ?????? animSpinner ????????? ??????
            mRefreshFooter.onReleased(this, mFooterHeight, mFooterExtendHeight);
        }
        if (mOnMultiPurposeListener != null) {
            //??? mRefreshFooter.onReleased ??????
            mOnMultiPurposeListener.onFooterReleased(mRefreshFooter, mFooterHeight, mFooterExtendHeight);
        }
        if (animator == null) {
            //onAnimationEnd ?????????????????? loading ????????? onReleased ????????????
            listener.onAnimationEnd(null);
        }
    }

    protected void setStateRefreshing() {
        AnimatorListenerAdapter listener = new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLastRefreshingTime = currentTimeMillis();
                notifyStateChanged(Refreshing);
                if (mRefreshListener != null) {
                    mRefreshListener.onRefresh(SmartRefreshLayout.this);
                }
                if (mRefreshHeader != null) {
                    mRefreshHeader.onStartAnimator(SmartRefreshLayout.this, mHeaderHeight, mHeaderExtendHeight);
                }
                if (mOnMultiPurposeListener != null) {
                    mOnMultiPurposeListener.onRefresh(SmartRefreshLayout.this);
                    mOnMultiPurposeListener.onHeaderStartAnimator(mRefreshHeader, mHeaderHeight, mHeaderExtendHeight);
                }
            }
        };
        notifyStateChanged(RefreshReleased);
        ValueAnimator animator = animSpinner(mHeaderHeight);
        if (animator != null) {
            animator.addListener(listener);
        }
        if (mRefreshHeader != null) {
            //onReleased ????????????????????? animSpinner ?????? onAnimationEnd ??????
            // ?????? onRefreshReleased?????? ???????????? ??? ?????? animSpinner ????????? ??????
            mRefreshHeader.onReleased(this, mHeaderHeight, mHeaderExtendHeight);
        }
        if (mOnMultiPurposeListener != null) {
            //??? mRefreshHeader.onReleased ??????
            mOnMultiPurposeListener.onHeaderReleased(mRefreshHeader, mHeaderHeight, mHeaderExtendHeight);
        }
        if (animator == null) {
            //onAnimationEnd ?????????????????? Refreshing ????????? onReleased ????????????
            listener.onAnimationEnd(null);
        }
    }

    /**
     * ????????????
     */
    protected void resetStatus() {
        if (mState != None) {
            if (mSpinner == 0) {
                notifyStateChanged(None);
            }
        }
        if (mSpinner != 0) {
            animSpinner(0);
        }
    }

    protected void setViceState(RefreshState state) {
        if (mState.dragging && mState.isHeader() != state.isHeader()) {
            notifyStateChanged(None);
        }
        if (mViceState != state) {
            mViceState = state;
        }
    }

    //</editor-fold>

    //<editor-fold desc="???????????? displacement">

    //<editor-fold desc="???????????? Animator Listener">
    protected Runnable animationRunnable;
    protected ValueAnimator reboundAnimator;

    protected class FlingRunnable implements Runnable {
        int mOffset;
        int mFrame = 0;
        int mFrameDelay = 10;
        float mVelocity;
        float mDamping = 0.95f;
        long mLastTime = AnimationUtils.currentAnimationTimeMillis();

        FlingRunnable(float velocity) {
            mVelocity = velocity;
            mOffset = mSpinner;
        }

        public Runnable start() {
            if (mState.finishing) {
                return null;
            }
            if (mSpinner != 0 && (!(mState.opening || (mFooterNoMoreData && mEnableFooterFollowWhenLoadFinished && isEnableLoadMore()))
                    || ((mState == Loading || (mFooterNoMoreData && mEnableFooterFollowWhenLoadFinished && isEnableLoadMore())) && mSpinner < -mFooterHeight)
                    || (mState == Refreshing && mSpinner > mHeaderHeight))) {
                int frame = 0;
                int offset = mSpinner;
                int spinner = mSpinner;
                float velocity = mVelocity;
                while (spinner * offset > 0) {
                    velocity *= Math.pow(mDamping, ++frame);
                    float velocityFrame = (velocity * (1f * mFrameDelay / 1000));
                    if (Math.abs(velocityFrame) < 1) {
                        if (!mState.opening
                                || (mState == Refreshing && offset > mHeaderHeight)
                                || (mState != Refreshing && offset < -mFooterHeight)) {
                            return null;
                        }
                        break;
                    }
                    offset += velocityFrame;
                }
            }
            postDelayed(this, mFrameDelay);
            return this;
        }

        @Override
        public void run() {
            if (animationRunnable == this && !mState.finishing) {
                mVelocity *= Math.pow(mDamping, ++mFrame);
                long now = AnimationUtils.currentAnimationTimeMillis();
                long span = now - mLastTime;
                float velocity = (mVelocity * (1f * span / 1000));
                if (Math.abs(velocity) > 1) {
                    mLastTime = now;
                    mOffset += velocity;
                    if (mSpinner * mOffset > 0) {
                        moveSpinner(mOffset, false);
                        postDelayed(this, mFrameDelay);
                    } else {
                        animationRunnable = null;
                        moveSpinner(0, false);
                        mRefreshContent.fling((int) -mVelocity);
                        if (mFooterLocked && velocity > 0) {
                            mFooterLocked = false;
                        }
                    }
                } else {
                    animationRunnable = null;
                }
            }
        }
    }

    protected class BounceRunnable implements Runnable {
        int mFrame = 0;
        int mFrameDelay = 10;
        int mSmoothDistance;
        long mLastTime;
        float mOffset = 0;
        float mVelocity;

        BounceRunnable(float velocity, int smoothDistance) {
            mVelocity = velocity;
            mSmoothDistance = smoothDistance;
            mLastTime = AnimationUtils.currentAnimationTimeMillis();
            postDelayed(this, mFrameDelay);
        }

        @Override
        public void run() {
            if (animationRunnable == this && !mState.finishing) {
                if (Math.abs(mSpinner) >= Math.abs(mSmoothDistance)) {
                    if (mSmoothDistance != 0) {
                        mVelocity *= Math.pow(0.45f, ++mFrame);//??????????????????????????????????????????
                    } else {
                        mVelocity *= Math.pow(0.85f, ++mFrame);//????????????????????????
                    }
                } else {
                    mVelocity *= Math.pow(0.95f, ++mFrame);//????????????????????????
                }
                long now = AnimationUtils.currentAnimationTimeMillis();
                float t = 1f * (now - mLastTime) / 1000;
                float velocity = mVelocity * t;
                if (Math.abs(velocity) >= 1) {
                    mLastTime = now;
                    mOffset += velocity;
                    moveSpinnerInfinitely(mOffset);
                    postDelayed(this, mFrameDelay);
                } else {
                    animationRunnable = null;
                    if (Math.abs(mSpinner) >= Math.abs(mSmoothDistance)) {
                        int duration = 10 * Math.min(Math.max((int) DensityUtil.px2dp(Math.abs(mSpinner - mSmoothDistance)), 30), 100);
                        animSpinner(mSmoothDistance, 0, mReboundInterpolator, duration);
                    }
                }
            }
        }
    }
    //</editor-fold>

    protected ValueAnimator animSpinner(int endSpinner) {
        return animSpinner(endSpinner, 0, mReboundInterpolator, mReboundDuration);
    }

    /*
     * ??????????????????
     */
    protected ValueAnimator animSpinner(int endSpinner, int startDelay, Interpolator interpolator, int duration) {
        if (mSpinner != endSpinner) {
            if (reboundAnimator != null) {
                reboundAnimator.cancel();
            }
            animationRunnable = null;
            reboundAnimator = ValueAnimator.ofInt(mSpinner, endSpinner);
            reboundAnimator.setDuration(duration);
            reboundAnimator.setInterpolator(interpolator);
            reboundAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationCancel(Animator animation) {
                    super.onAnimationEnd(animation);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    reboundAnimator = null;
                    if (mSpinner == 0) {
                        if (mState != None && !mState.opening) {
                            notifyStateChanged(None);
                        }
                    } else if (mState != mViceState) {
                        setViceState(mState);
                    }
                }
            });
            reboundAnimator.addUpdateListener(new AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    moveSpinner((int) animation.getAnimatedValue(), true);
                }
            });
            reboundAnimator.setStartDelay(startDelay);
            reboundAnimator.start();
            return reboundAnimator;
        }
        return null;
    }

    /*
     * ??????????????????
     */
    protected void animSpinnerBounce(final float velocity) {
        if (reboundAnimator == null) {
            if (velocity > 0 && (mState == Refreshing || mState == TwoLevel)) {
                animationRunnable = new BounceRunnable(velocity, mHeaderHeight);
            } else if (velocity < 0 && (mState == Loading
                    || (mEnableFooterFollowWhenLoadFinished && mFooterNoMoreData && isEnableLoadMore())
                    || (mEnableAutoLoadMore && !mFooterNoMoreData && isEnableLoadMore() && mState != Refreshing))) {
                animationRunnable = new BounceRunnable(velocity, -mFooterHeight);
            } else if (mSpinner == 0 && mEnableOverScrollBounce) {
                animationRunnable = new BounceRunnable(velocity, 0);
            }
        }
    }

    /*
     * ??????????????????
     * ????????????????????????
     */
    protected void overSpinner() {
        if (mState == TwoLevel) {
            if (mVelocityTracker.getYVelocity() > -1000 && mSpinner > getMeasuredHeight() / 2) {
                ValueAnimator animator = animSpinner(getMeasuredHeight());
                if (animator != null) {
                    animator.setDuration(mFloorDuration);
                }
            } else if (mIsBeingDragged) {
                mKernel.finishTwoLevel();
            }
        } else if (mState == Loading
//                || (mEnableAutoLoadMore && !mFooterNoMoreData && mSpinner < 0 && isEnableLoadMore() && mState != RefreshState.Refreshing)
                || (mEnableFooterFollowWhenLoadFinished && mFooterNoMoreData && mSpinner < 0 && isEnableLoadMore())) {
            if (mSpinner < -mFooterHeight) {
//                mTotalUnconsumed = -mFooterHeight;
                animSpinner(-mFooterHeight);
            } else if (mSpinner > 0) {
//                mTotalUnconsumed = 0;
                animSpinner(0);
            }
        } else if (mState == Refreshing) {
            if (mSpinner > mHeaderHeight) {
//                mTotalUnconsumed = mHeaderHeight;
                animSpinner(mHeaderHeight);
            } else if (mSpinner < 0) {
//                mTotalUnconsumed = 0;
                animSpinner(0);
            }
        } else if (mState == PullDownToRefresh) {
            mKernel.setState(PullDownCanceled);
        } else if (mState == PullUpToLoad) {
            mKernel.setState(PullDownCanceled);
        } else if (mState == ReleaseToRefresh) {
            setStateRefreshing();
        } else if (mState == ReleaseToLoad) {
            setStateLoading();
        } else if (mState == ReleaseToTwoLevel) {
            mKernel.setState(TwoLevelReleased);
        } else if (mSpinner != 0) {
            animSpinner(0);
        }
    }

    protected void moveSpinnerInfinitely(float spinner) {
        if (mState == TwoLevel && spinner > 0) {
            moveSpinner(Math.min((int) spinner, getMeasuredHeight()), false);
        } else if (mState == Refreshing && spinner >= 0) {
            if (spinner < mHeaderHeight) {
                moveSpinner((int) spinner, false);
            } else {
                final double M = mHeaderExtendHeight;
                final double H = Math.max(mScreenHeightPixels * 4 / 3, getHeight()) - mHeaderHeight;
                final double x = Math.max(0, (spinner - mHeaderHeight) * mDragRate);
                final double y = Math.min(M * (1 - Math.pow(100, -x / (H == 0 ? 1 : H))), x);// ?????? y = M(1-100^(-x/H))
                moveSpinner((int) y + mHeaderHeight, false);
            }
        } else if (spinner < 0 && (mState == Loading
                || (mEnableFooterFollowWhenLoadFinished && mFooterNoMoreData && isEnableLoadMore())
                || (mEnableAutoLoadMore && !mFooterNoMoreData && isEnableLoadMore()))) {
            if (spinner > -mFooterHeight) {
                moveSpinner((int) spinner, false);
            } else {
                final double M = mFooterExtendHeight;
                final double H = Math.max(mScreenHeightPixels * 4 / 3, getHeight()) - mFooterHeight;
                final double x = -Math.min(0, (spinner + mFooterHeight) * mDragRate);
                final double y = -Math.min(M * (1 - Math.pow(100, -x / (H == 0 ? 1 : H))), x);// ?????? y = M(1-100^(-x/H))
                moveSpinner((int) y - mFooterHeight, false);
            }
        } else if (spinner >= 0) {
            final double M = mHeaderExtendHeight + mHeaderHeight;
            final double H = Math.max(mScreenHeightPixels / 2, getHeight());
            final double x = Math.max(0, spinner * mDragRate);
            final double y = Math.min(M * (1 - Math.pow(100, -x / (H == 0 ? 1 : H))), x);// ?????? y = M(1-100^(-x/H))
            moveSpinner((int) y, false);
        } else {
            final double M = mFooterExtendHeight + mFooterHeight;
            final double H = Math.max(mScreenHeightPixels / 2, getHeight());
            final double x = -Math.min(0, spinner * mDragRate);
            final double y = -Math.min(M * (1 - Math.pow(100, -x / (H == 0 ? 1 : H))), x);// ?????? y = M(1-100^(-x/H))
            moveSpinner((int) y, false);
        }
        if (mEnableAutoLoadMore && !mFooterNoMoreData && isEnableLoadMore() && spinner < 0
                && mState != Refreshing
                && mState != Loading
                && mState != LoadFinish) {
            setStateDirectLoading();
            if (mDisableContentWhenLoading) {
                animationRunnable = null;
                animSpinner(-mFooterHeight);
            }
        }
    }

    /*
     * ???????????? Scroll
     * moveSpinner ??????????????? ??????????????? {@link android.support.v4.widget.SwipeRefreshLayout#moveSpinner(float)}
     */
    protected void moveSpinner(final int spinner, boolean isAnimator) {
        if (mSpinner == spinner
                && (mRefreshHeader == null || !mRefreshHeader.isSupportHorizontalDrag())
                && (mRefreshFooter == null || !mRefreshFooter.isSupportHorizontalDrag())) {
            return;
        }
        final int oldSpinner = mSpinner;
        this.mSpinner = spinner;
        if (!isAnimator && mViceState.dragging) {
            if (mSpinner > mHeaderHeight * mHeaderTriggerRate) {
                if (mState != ReleaseToTwoLevel) {
                    mKernel.setState(ReleaseToRefresh);
                }
            } else if (-mSpinner > mFooterHeight * mFooterTriggerRate && !mFooterNoMoreData) {
                mKernel.setState(ReleaseToLoad);
            } else if (mSpinner < 0 && !mFooterNoMoreData) {
                mKernel.setState(PullUpToLoad);
            } else if (mSpinner > 0) {
                mKernel.setState(PullDownToRefresh);
            }
        }
        if (mRefreshContent != null) {
            Integer tSpinner = null;
            if (spinner >= 0) {
                if (mEnableHeaderTranslationContent || mRefreshHeader == null || mRefreshHeader.getSpinnerStyle() == SpinnerStyle.FixedBehind) {
                    tSpinner = spinner;
                } else if (oldSpinner < 0) {
                    tSpinner = 0;
                }
            }
            if (spinner <= 0) {
                if (mEnableFooterTranslationContent || mRefreshFooter == null || mRefreshFooter.getSpinnerStyle() == SpinnerStyle.FixedBehind) {
                    tSpinner = spinner;
                } else if (oldSpinner > 0) {
                    tSpinner = 0;
                }
            }
            if (tSpinner != null) {
                mRefreshContent.moveSpinner(tSpinner);
                if ((mHeaderBackgroundColor != 0 && (tSpinner >= 0 || oldSpinner > 0)) ||
                        (mFooterBackgroundColor != 0 && (tSpinner <= 0 || oldSpinner < 0))) {
                    invalidate();
                }
            }
        }
        if ((spinner >= 0 || oldSpinner > 0) && mRefreshHeader != null) {

            final int offset = Math.max(spinner, 0);
            final int headerHeight = mHeaderHeight;
            final int extendHeight = mHeaderExtendHeight;
            final float percent = 1f * offset / (mHeaderHeight == 0 ? 1 : mHeaderHeight);

            if (isEnableRefresh() || (mState == RefreshFinish && isAnimator)) {
                if (oldSpinner != mSpinner) {
                    if (mRefreshHeader.getSpinnerStyle() == SpinnerStyle.Translate) {
                        mRefreshHeader.getView().setTranslationY(mSpinner);
                    } else if (mRefreshHeader.getSpinnerStyle() == SpinnerStyle.Scale) {
                        mRefreshHeader.getView().requestLayout();
                    }
                    if (isAnimator) {
                        mRefreshHeader.onReleasing(percent, offset, headerHeight, extendHeight);
                    }
                }
                if (!isAnimator) {
                    if (mRefreshHeader.isSupportHorizontalDrag()) {
                        final int offsetX = (int) mLastTouchX;
                        final int offsetMax = getWidth();
                        final float percentX = mLastTouchX / (offsetMax == 0 ? 1 : offsetMax);
                        mRefreshHeader.onHorizontalDrag(percentX, offsetX, offsetMax);
                        mRefreshHeader.onPulling(percent, offset, headerHeight, extendHeight);
                    } else if (oldSpinner != mSpinner) {
                        mRefreshHeader.onPulling(percent, offset, headerHeight, extendHeight);
                    }
                }
            }

            if (oldSpinner != mSpinner && mOnMultiPurposeListener != null) {
                if (isAnimator) {
                    mOnMultiPurposeListener.onHeaderReleasing(mRefreshHeader, percent, offset, headerHeight, extendHeight);
                } else {
                    mOnMultiPurposeListener.onHeaderPulling(mRefreshHeader, percent, offset, headerHeight, extendHeight);
                }
            }

        }
        if ((spinner <= 0 || oldSpinner < 0) && mRefreshFooter != null) {

            final int offset = -Math.min(spinner, 0);
            final int footerHeight = mFooterHeight;
            final int extendHeight = mFooterExtendHeight;
            final float percent = offset * 1f / (mFooterHeight == 0 ? 1 : mFooterHeight);

            if (isEnableLoadMore() || (mState == LoadFinish && isAnimator)) {
                if (oldSpinner != mSpinner) {
                    if (mRefreshFooter.getSpinnerStyle() == SpinnerStyle.Translate) {
                        mRefreshFooter.getView().setTranslationY(mSpinner);
                    } else if (mRefreshFooter.getSpinnerStyle() == SpinnerStyle.Scale) {
                        mRefreshFooter.getView().requestLayout();
                    }
                    if (isAnimator) {
                        mRefreshFooter.onReleasing(percent, offset, footerHeight, extendHeight);
                    }
                }

                if (!isAnimator) {
                    if (mRefreshFooter.isSupportHorizontalDrag()) {
                        final int offsetX = (int) mLastTouchX;
                        final int offsetMax = getWidth();
                        final float percentX = mLastTouchX / (offsetMax == 0 ? 1 : offsetMax);
                        mRefreshFooter.onHorizontalDrag(percentX, offsetX, offsetMax);
                        mRefreshFooter.onPulling(percent, offset, footerHeight, extendHeight);
                    } else if (oldSpinner != mSpinner) {
                        mRefreshFooter.onPulling(percent, offset, footerHeight, extendHeight);
                    }
                }
            }

            if (oldSpinner != mSpinner && mOnMultiPurposeListener != null) {
                if (isAnimator) {
                    mOnMultiPurposeListener.onFooterReleasing(mRefreshFooter, percent, offset, footerHeight, extendHeight);
                } else {
                    mOnMultiPurposeListener.onFooterPulling(mRefreshFooter, percent, offset, footerHeight, extendHeight);
                }
            }
        }
    }

    //</editor-fold>

    //<editor-fold desc="???????????? LayoutParams">
    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(MATCH_PARENT, MATCH_PARENT);
    }

    @Override
    protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    public static class LayoutParams extends MarginLayoutParams {

        public LayoutParams(Context context, AttributeSet attrs) {
            super(context, attrs);
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SmartRefreshLayout_Layout);
            backgroundColor = ta.getColor(R.styleable.SmartRefreshLayout_Layout_layout_srlBackgroundColor, backgroundColor);
            if (ta.hasValue(R.styleable.SmartRefreshLayout_Layout_layout_srlSpinnerStyle)) {
                spinnerStyle = SpinnerStyle.values()[ta.getInt(R.styleable.SmartRefreshLayout_Layout_layout_srlSpinnerStyle, SpinnerStyle.Translate.ordinal())];
            }
            ta.recycle();
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        public int backgroundColor = 0;
        public SpinnerStyle spinnerStyle = null;
    }
    //</editor-fold>

    //<editor-fold desc="???????????? NestedScrolling">

    //<editor-fold desc="NestedScrollingParent">

    @Override
    public int getNestedScrollAxes() {
        return mNestedScrollingParentHelper.getNestedScrollAxes();
    }

    @Override
    public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, int nestedScrollAxes) {
        boolean accepted = isEnabled() && isNestedScrollingEnabled() && (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
        accepted = accepted && (mEnableOverScrollDrag || isEnableRefresh() || isEnableLoadMore());
        return accepted;
    }

    @Override
    public void onNestedScrollAccepted(@NonNull View child, @NonNull View target, int axes) {
        // Reset the counter of how much leftover scroll needs to be consumed.
        mNestedScrollingParentHelper.onNestedScrollAccepted(child, target, axes);
        // Dispatch up to the nested parent
        startNestedScroll(axes & ViewCompat.SCROLL_AXIS_VERTICAL);
        mTotalUnconsumed = mSpinner;//0;
//        mTouchSpinner = mSpinner;
        mNestedScrollInProgress = true;
    }

    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed) {
        // If we are in the middle of consuming, a scroll, then we want to move the spinner back up
        // before allowing the list to scroll
        int consumedY = 0;

        if (dy * mTotalUnconsumed > 0) {
            if (Math.abs(dy) > Math.abs(mTotalUnconsumed)) {
                consumedY = mTotalUnconsumed;
                mTotalUnconsumed = 0;
            } else {
                consumedY = dy;
                mTotalUnconsumed -= dy;
            }
            moveSpinnerInfinitely(mTotalUnconsumed);
            if (mViceState.opening || mViceState == None) {
                if (mSpinner > 0) {
                    mKernel.setState(PullDownToRefresh);
                } else {
                    mKernel.setState(PullUpToLoad);
                }
            }
        } else if (dy > 0 && mFooterLocked) {
            consumedY = dy;
            mTotalUnconsumed -= dy;
            moveSpinnerInfinitely(mTotalUnconsumed);
        }

        // Now let our nested parent consume the leftovers
        dispatchNestedPreScroll(dx, dy - consumedY, consumed, null);
        consumed[1] += consumedY;

//        if (mState.opening || (mEnableFooterFollowWhenLoadFinished && mFooterNoMoreData && isEnableLoadMore())) {
//            consumed[1] = 0;
//            if (dispatchNestedPreScroll(dx, dy, consumed, null)) {
//                dy -= consumed[1];
//            }
//            //?????? mTotalUnconsumed???dy ????????????????????????
//            if ((mState == RefreshState.Refreshing || mState == RefreshState.TwoLevel) && (dy * mTotalUnconsumed > 0 /*|| mTouchSpinner > 0*/)) {
//                if (mViceState == RefreshState.None || mViceState == RefreshState.Refreshing) {
//                    mKernel.setState(RefreshState.PullDownToRefresh);
//                }
//                if (Math.abs(dy) > Math.abs(mTotalUnconsumed)) {
//                    consumed[1] += mTotalUnconsumed;
//                    mTotalUnconsumed = 0;
//                    dy -= mTotalUnconsumed;
////                    if (mTouchSpinner <= 0) {
////                        moveSpinnerInfinitely(0);
////                    }
//                } else {
//                    mTotalUnconsumed -= dy;
//                    consumed[1] += dy;
//                    dy = 0;
//                    moveSpinnerInfinitely(mTotalUnconsumed/* + mTouchSpinner*/);
//                }
////
////                if (dy > 0 && mTouchSpinner > 0) {
////                    if (dy > mTouchSpinner) {
////                        consumed[1] += mTouchSpinner;
////                        mTouchSpinner = 0;
////                    } else {
////                        mTouchSpinner -= dy;
////                        consumed[1] += dy;
////                    }
////                    moveSpinnerInfinitely(mTouchSpinner);
////                }
//            } else if ((mState == RefreshState.Loading || (mEnableFooterFollowWhenLoadFinished && mFooterNoMoreData && isEnableLoadMore()))
//                    && (dy * mTotalUnconsumed > 0 /*|| mTouchSpinner < 0*/)) {
//                if (mViceState == RefreshState.None || mViceState == RefreshState.Loading) {
//                    mKernel.setState(RefreshState.PullUpToLoad);
//                }
//                if (Math.abs(dy) > Math.abs(mTotalUnconsumed)) {
//                    consumed[1] += mTotalUnconsumed;
//                    mTotalUnconsumed = 0;
//                    dy -= mTotalUnconsumed;
////                    if (mTouchSpinner >= 0) {
////                        moveSpinnerInfinitely(0);
////                    }
//                } else {
//                    mTotalUnconsumed -= dy;
//                    consumed[1] += dy;
//                    dy = 0;
//                    moveSpinnerInfinitely(mTotalUnconsumed /*+ mTouchSpinner*/);
//                }
//
////                if (dy < 0 && mTouchSpinner < 0) {
////                    if (dy < mTouchSpinner) {
////                        consumed[1] += mTouchSpinner;
////                        mTouchSpinner = 0;
////                    } else {
////                        mTouchSpinner -= dy;
////                        consumed[1] += dy;
////                    }
////                    moveSpinnerInfinitely(mTouchSpinner);
////                }
//
//                if (mFooterLocked && dy > 0) {
//                    mTotalUnconsumed -= dy;
//                    consumed[1] += dy;
//                    moveSpinnerInfinitely(mTotalUnconsumed/* + mTouchSpinner*/);
//                }
//            }
//        } else {
//            int consumedY = 0;
//            if (isEnableRefresh() && dy > 0 && mTotalUnconsumed > 0) {
//                if (dy > mTotalUnconsumed) {
//                    consumedY = dy - mTotalUnconsumed;
//                    mTotalUnconsumed = 0;
//                } else {
//                    mTotalUnconsumed -= dy;
//                    consumedY = dy;
//                }
//                moveSpinnerInfinitely(mTotalUnconsumed);
//            } else if (isEnableLoadMore() && dy < 0 && mTotalUnconsumed < 0) {
//                if (dy < mTotalUnconsumed) {
//                    consumedY = dy - mTotalUnconsumed;
//                    mTotalUnconsumed = 0;
//                } else {
//                    mTotalUnconsumed -= dy;
//                    consumedY = dy;
//                }
//                moveSpinnerInfinitely(mTotalUnconsumed);
//            }
//
//            // Now let our nested parent consume the leftovers
//            dispatchNestedPreScroll(dx, dy - consumedY, consumed, null);
//            consumed[1] += consumedY;
//        }

    }

    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        // Dispatch up to the nested parent first
        dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, mParentOffsetInWindow);

        // This is a bit of a hack. Nested scrolling works from the bottom up, and as we are
        // sometimes between two nested scrolling views, we need a way to be able to know when any
        // nested scrolling parent has stopped handling events. We do that by using the
        // 'offset in window 'functionality to see if we have been moved from the event.
        // This is a decent indication of whether we should take over the event stream or not.
        final int dy = dyUnconsumed + mParentOffsetInWindow[1];
        if (dy != 0 && (mEnableOverScrollDrag || (dy < 0 && isEnableRefresh()) || (dy > 0 && isEnableLoadMore()))) {
            if (mViceState == None) {
                mKernel.setState(dy > 0 ? PullUpToLoad : PullDownToRefresh);
            }
            moveSpinnerInfinitely(mTotalUnconsumed -= dy);
        }
//        if (dy < 0 && isEnableRefresh() /* && (mRefreshContent == null || mRefreshContent.canRefresh())*/) {
//            if (mViceState == RefreshState.None) {
//                mKernel.setState(RefreshState.PullDownToRefresh);
//            }
//            mTotalUnconsumed += Math.abs(dy);
//            moveSpinnerInfinitely(mTotalUnconsumed/* + mTouchSpinner*/);
//        } else if (dy > 0 && isEnableLoadMore() /* && (mRefreshContent == null || mRefreshContent.canLoadMore())*/) {
//            if (mViceState == RefreshState.None) {
//                mKernel.setState(RefreshState.PullUpToLoad);
//            }
//            mTotalUnconsumed -= Math.abs(dy);
//            moveSpinnerInfinitely(mTotalUnconsumed/* + mTouchSpinner*/);
//        }
    }

    @Override
    public boolean onNestedPreFling(@NonNull View target, float velocityX, float velocityY) {
        return mFooterLocked && velocityY > 0 || startFlingIfNeed(-velocityY) || dispatchNestedPreFling(velocityX, velocityY);
    }

    @Override
    public boolean onNestedFling(@NonNull View target, float velocityX, float velocityY, boolean consumed) {
        return dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override
    public void onStopNestedScroll(@NonNull View target) {
        mNestedScrollingParentHelper.onStopNestedScroll(target);
        mNestedScrollInProgress = false;
        // Finish the spinner for nested scrolling if we ever consumed any
        // unconsumed nested scroll
        mTotalUnconsumed = 0;
        overSpinner();
        // Dispatch up our nested parent
        stopNestedScroll();
    }
    //</editor-fold>

    //<editor-fold desc="NestedScrollingChild">
    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        mManualNestedScrolling = true;
        mNestedScrollingChildHelper.setNestedScrollingEnabled(enabled);
    }

    @Override
    public boolean isNestedScrollingEnabled() {
        return mNestedScrollingChildHelper.isNestedScrollingEnabled();
    }

    @Override
    public boolean startNestedScroll(int axes) {
        return mNestedScrollingChildHelper.startNestedScroll(axes);
    }

    @Override
    public void stopNestedScroll() {
        mNestedScrollingChildHelper.stopNestedScroll();
    }

    @Override
    public boolean hasNestedScrollingParent() {
        return mNestedScrollingChildHelper.hasNestedScrollingParent();
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed,
                                        int dyUnconsumed, int[] offsetInWindow) {
        return mNestedScrollingChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed,
                dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        return mNestedScrollingChildHelper.dispatchNestedPreScroll(
                dx, dy, consumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return mNestedScrollingChildHelper.dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return mNestedScrollingChildHelper.dispatchNestedPreFling(velocityX, velocityY);
    }
    //</editor-fold>

    //</editor-fold>

    //<editor-fold desc="???????????? open interface">
    @Override
    public SmartRefreshLayout setFooterHeight(float heightDp) {
        return setFooterHeightPx(DensityUtil.dp2px(heightDp));
    }

    @Override
    public SmartRefreshLayout setFooterHeightPx(int heightPx) {
        if (mFooterHeightStatus.canReplaceWith(DimensionStatus.CodeExact)) {
            mFooterHeight = heightPx;
            mFooterExtendHeight = (int) Math.max((heightPx * (mFooterMaxDragRate - 1)), 0);
            mFooterHeightStatus = DimensionStatus.CodeExactUnNotify;
            if (mRefreshFooter != null) {
                mRefreshFooter.getView().requestLayout();
            }
        }
        return this;
    }

    @Override
    public SmartRefreshLayout setHeaderHeight(float heightDp) {
        return setHeaderHeightPx(DensityUtil.dp2px(heightDp));
    }

    @Override
    public SmartRefreshLayout setHeaderHeightPx(int heightPx) {
        if (mHeaderHeightStatus.canReplaceWith(DimensionStatus.CodeExact)) {
            mHeaderHeight = heightPx;
            mHeaderExtendHeight = (int) Math.max((heightPx * (mHeaderMaxDragRate - 1)), 0);
            mHeaderHeightStatus = DimensionStatus.CodeExactUnNotify;
            if (mRefreshHeader != null) {
                mRefreshHeader.getView().requestLayout();
            }
        }
        return this;
    }

    @Override
    public SmartRefreshLayout setHeaderInsetStart(float insetDp) {
        return setHeaderInsetStartPx(DensityUtil.dp2px(insetDp));
    }

    @Override
    public SmartRefreshLayout setHeaderInsetStartPx(int insetPx) {
        mHeaderInsetStart = insetPx;
        return this;
    }

    @Override
    public SmartRefreshLayout setFooterInsetStart(float insetDp) {
        return setFooterInsetStartPx(DensityUtil.dp2px(insetDp));
    }

    @Override
    public SmartRefreshLayout setFooterInsetStartPx(int insetPx) {
        mFooterInsetStart = insetPx;
        return this;
    }

    /**
     * @param rate ??????????????????/?????????????????? ??????
     * @return RefreshLayout
     */
    @Override
    public SmartRefreshLayout setDragRate(float rate) {
        this.mDragRate = rate;
        return this;
    }

    /**
     * ???????????????????????????Header????????????????????????????????????????????????????????????
     *
     * @param rate ?????????????????????Header???????????????
     */
    @Override
    public SmartRefreshLayout setHeaderMaxDragRate(float rate) {
        this.mHeaderMaxDragRate = rate;
        this.mHeaderExtendHeight = (int) Math.max((mHeaderHeight * (mHeaderMaxDragRate - 1)), 0);
        if (mRefreshHeader != null && mHandler != null) {
            mRefreshHeader.onInitialized(mKernel, mHeaderHeight, mHeaderExtendHeight);
        } else {
            mHeaderHeightStatus = mHeaderHeightStatus.unNotify();
        }
        return this;
    }

    /**
     * ???????????????????????????Footer????????????????????????????????????????????????????????????
     *
     * @param rate ?????????????????????Footer???????????????
     * @return SmartRefreshLayout
     */
    @Override
    public SmartRefreshLayout setFooterMaxDragRate(float rate) {
        this.mFooterMaxDragRate = rate;
        this.mFooterExtendHeight = (int) Math.max((mFooterHeight * (mFooterMaxDragRate - 1)), 0);
        if (mRefreshFooter != null && mHandler != null) {
            mRefreshFooter.onInitialized(mKernel, mFooterHeight, mFooterExtendHeight);
        } else {
            mFooterHeightStatus = mFooterHeightStatus.unNotify();
        }
        return this;
    }

    /**
     * ?????? ?????????????????? ??? HeaderHeight ?????????
     *
     * @param rate ?????????????????? ??? HeaderHeight ?????????
     * @return SmartRefreshLayout
     */
    @Override
    public SmartRefreshLayout setHeaderTriggerRate(float rate) {
        this.mHeaderTriggerRate = rate;
        return this;
    }

    /**
     * ?????? ?????????????????? ??? FooterHeight ?????????
     *
     * @param rate ?????????????????? ??? FooterHeight ?????????
     * @return SmartRefreshLayout
     */
    @Override
    public SmartRefreshLayout setFooterTriggerRate(float rate) {
        this.mFooterTriggerRate = rate;
        return this;
    }

    /**
     * ???????????????????????????
     *
     * @param interpolator ???????????????
     * @return SmartRefreshLayout
     */
    @Override
    public SmartRefreshLayout setReboundInterpolator(@NonNull Interpolator interpolator) {
        this.mReboundInterpolator = interpolator;
        return this;
    }

    /**
     * ????????????????????????
     *
     * @param duration ??????
     * @return SmartRefreshLayout
     */
    @Override
    public SmartRefreshLayout setReboundDuration(int duration) {
        this.mReboundDuration = duration;
        return this;
    }

    /**
     * ??????????????????????????????????????????????????????
     *
     * @param enabled ????????????
     * @return SmartRefreshLayout
     */
    @Override
    public SmartRefreshLayout setEnableLoadMore(boolean enabled) {
        this.mManualLoadMore = true;
        this.mEnableLoadMore = enabled;
        return this;
    }

    /**
     * ??????????????????????????????????????????
     *
     * @param enabled ????????????
     * @return SmartRefreshLayout
     */
    @Override
    public SmartRefreshLayout setEnableRefresh(boolean enabled) {
        this.mEnableRefresh = enabled;
        return this;
    }

    /**
     * ??????????????????????????????????????????
     *
     * @param enabled ????????????
     * @return SmartRefreshLayout
     */
    @Override
    public SmartRefreshLayout setEnableHeaderTranslationContent(boolean enabled) {
        this.mEnableHeaderTranslationContent = enabled;
        this.mManualHeaderTranslationContent = true;
        return this;
    }

    /**
     * ??????????????????????????????????????????
     *
     * @param enabled ????????????
     * @return SmartRefreshLayout
     */
    @Override
    public SmartRefreshLayout setEnableFooterTranslationContent(boolean enabled) {
        this.mEnableFooterTranslationContent = enabled;
        return this;
    }

    /**
     * ???????????????????????????????????????????????????????????????
     *
     * @param enabled ????????????
     * @return SmartRefreshLayout
     */
    @Override
    public SmartRefreshLayout setEnableAutoLoadMore(boolean enabled) {
        this.mEnableAutoLoadMore = enabled;
        return this;
    }

    /**
     * ??????????????????????????????
     *
     * @param enabled ????????????
     * @return SmartRefreshLayout
     */
    @Override
    public SmartRefreshLayout setEnableOverScrollBounce(boolean enabled) {
        this.mEnableOverScrollBounce = enabled;
        return this;
    }

    /**
     * ?????????????????????????????????
     *
     * @param enabled ????????????
     * @return SmartRefreshLayout
     */
    @Override
    public SmartRefreshLayout setEnablePureScrollMode(boolean enabled) {
        this.mEnablePureScrollMode = enabled;
        return this;
    }

    /**
     * ??????????????????????????????????????????????????????????????????
     *
     * @param enabled ????????????
     * @return SmartRefreshLayout
     */
    @Override
    public SmartRefreshLayout setEnableScrollContentWhenLoaded(boolean enabled) {
        this.mEnableScrollContentWhenLoaded = enabled;
        return this;
    }

    /**
     * ??????????????????????????????????????????????????????
     *
     * @param enabled ????????????
     * @return SmartRefreshLayout
     */
    @Override
    public SmartRefreshLayout setEnableScrollContentWhenRefreshed(boolean enabled) {
        this.mEnableScrollContentWhenRefreshed = enabled;
        return this;
    }

    /**
     * ?????????????????????????????????????????????????????????????????????
     *
     * @param enabled ????????????
     * @return SmartRefreshLayout
     */
    @Override
    public SmartRefreshLayout setEnableLoadMoreWhenContentNotFull(boolean enabled) {
        this.mEnableLoadMoreWhenContentNotFull = enabled;
        if (mRefreshContent != null) {
            mRefreshContent.setEnableLoadMoreWhenContentNotFull(enabled);
        }
        return this;
    }

    /**
     * ???????????????????????????????????????????????????
     *
     * @param enabled ????????????
     * @return SmartRefreshLayout
     */
    @Override
    public SmartRefreshLayout setEnableOverScrollDrag(boolean enabled) {
        this.mEnableOverScrollDrag = enabled;
        return this;
    }

    /**
     * ???????????????????????????????????????Footer????????????
     *
     * @param enabled ????????????
     * @return SmartRefreshLayout
     */
    @Override
    public SmartRefreshLayout setEnableFooterFollowWhenLoadFinished(boolean enabled) {
        this.mEnableFooterFollowWhenLoadFinished = enabled;
        return this;
    }

    /**
     * ???????????? ??? Header FixedBehind ???????????????????????? Header
     *
     * @param enabled ????????????
     * @return SmartRefreshLayout
     */
    @Override
    public SmartRefreshLayout setEnableClipHeaderWhenFixedBehind(boolean enabled) {
        this.mEnableClipHeaderWhenFixedBehind = enabled;
        return this;
    }

    /**
     * ???????????? ??? Footer FixedBehind ???????????????????????? Footer
     *
     * @param enabled ????????????
     * @return SmartRefreshLayout
     */
    @Override
    public SmartRefreshLayout setEnableClipFooterWhenFixedBehind(boolean enabled) {
        this.mEnableClipFooterWhenFixedBehind = enabled;
        return this;
    }

    /**
     * ??????????????????????????????????????????????????????+???????????????
     *
     * @param enabled ????????????
     * @return SmartRefreshLayout
     */
    @Override
    public RefreshLayout setEnableNestedScroll(boolean enabled) {
        setNestedScrollingEnabled(enabled);
        return this;
    }

    /**
     * ?????????????????????????????????????????????????????????
     *
     * @param disable ????????????
     * @return SmartRefreshLayout
     */
    @Override
    public SmartRefreshLayout setDisableContentWhenRefresh(boolean disable) {
        this.mDisableContentWhenRefresh = disable;
        return this;
    }

    /**
     * ?????????????????????????????????????????????????????????
     *
     * @param disable ????????????
     * @return SmartRefreshLayout
     */
    @Override
    public SmartRefreshLayout setDisableContentWhenLoading(boolean disable) {
        this.mDisableContentWhenLoading = disable;
        return this;
    }

    /**
     * ??????????????? Header
     *
     * @param header ?????????
     * @return SmartRefreshLayout
     */
    @Override
    public SmartRefreshLayout setRefreshHeader(@NonNull RefreshHeader header) {
        return setRefreshHeader(header, MATCH_PARENT, WRAP_CONTENT);
    }

    /**
     * ??????????????? Header
     *
     * @param header ?????????
     * @param width  ?????? ???????????? MATCH_PARENT, WRAP_CONTENT
     * @param height ?????? ???????????? MATCH_PARENT, WRAP_CONTENT
     * @return SmartRefreshLayout
     */
    @Override
    public SmartRefreshLayout setRefreshHeader(@NonNull RefreshHeader header, int width, int height) {
        if (mRefreshHeader != null) {
            removeView(mRefreshHeader.getView());
        }
        this.mRefreshHeader = header;
        this.mHeaderBackgroundColor = 0;
        this.mHeaderNeedTouchEventWhenRefreshing = false;
        this.mHeaderHeightStatus = mHeaderHeightStatus.unNotify();
        if (header.getSpinnerStyle() == SpinnerStyle.FixedBehind) {
            this.addView(mRefreshHeader.getView(), 0, new LayoutParams(width, height));
        } else {
            this.addView(mRefreshHeader.getView(), width, height);
        }
        return this;
    }

    /**
     * ??????????????? Footer
     *
     * @param footer ????????????
     * @return SmartRefreshLayout
     */
    @Override
    public SmartRefreshLayout setRefreshFooter(@NonNull RefreshFooter footer) {
        return setRefreshFooter(footer, MATCH_PARENT, WRAP_CONTENT);
    }

    /**
     * ??????????????? Footer
     *
     * @param footer ????????????
     * @param width  ?????? ???????????? MATCH_PARENT, WRAP_CONTENT
     * @param height ?????? ???????????? MATCH_PARENT, WRAP_CONTENT
     * @return SmartRefreshLayout
     */
    @Override
    public SmartRefreshLayout setRefreshFooter(@NonNull RefreshFooter footer, int width, int height) {
        if (mRefreshFooter != null) {
            removeView(mRefreshFooter.getView());
        }
        this.mRefreshFooter = footer;
        this.mFooterBackgroundColor = 0;
        this.mFooterNeedTouchEventWhenLoading = false;
        this.mFooterHeightStatus = mFooterHeightStatus.unNotify();
        this.mEnableLoadMore = !mManualLoadMore || mEnableLoadMore;
        if (mRefreshFooter.getSpinnerStyle() == SpinnerStyle.FixedBehind) {
            this.addView(mRefreshFooter.getView(), 0, new LayoutParams(width, height));
        } else {
            this.addView(mRefreshFooter.getView(), width, height);
        }
        return this;
    }

    /**
     * ???????????????Content
     *
     * @param content ????????????
     * @return SmartRefreshLayout
     */
    @Override
    public RefreshLayout setRefreshContent(@NonNull View content) {
        return setRefreshContent(content, MATCH_PARENT, MATCH_PARENT);
    }

    /**
     * ??????????????? Content
     *
     * @param content ????????????
     * @param width   ?????? ???????????? MATCH_PARENT, WRAP_CONTENT
     * @param height  ?????? ???????????? MATCH_PARENT, WRAP_CONTENT
     * @return SmartRefreshLayout
     */
    @Override
    public RefreshLayout setRefreshContent(@NonNull View content, int width, int height) {
        if (mRefreshContent != null) {
            removeView(mRefreshContent.getView());
        }
        addView(content, 0, new LayoutParams(width, height));
        if (mRefreshHeader != null && mRefreshHeader.getSpinnerStyle() == SpinnerStyle.FixedBehind) {
            bringChildToFront(content);
            if (mRefreshFooter != null && mRefreshFooter.getSpinnerStyle() != SpinnerStyle.FixedBehind) {
                bringChildToFront(mRefreshFooter.getView());
            }
        } else if (mRefreshFooter != null && mRefreshFooter.getSpinnerStyle() == SpinnerStyle.FixedBehind) {
            bringChildToFront(content);
            if (mRefreshHeader != null && mRefreshHeader.getSpinnerStyle() == SpinnerStyle.FixedBehind) {
                bringChildToFront(mRefreshHeader.getView());
            }
        }
        mRefreshContent = new RefreshContentWrapper(content);
        if (mHandler != null) {
            View fixedHeaderView = mFixedHeaderViewId > 0 ? findViewById(mFixedHeaderViewId) : null;
            View fixedFooterView = mFixedFooterViewId > 0 ? findViewById(mFixedFooterViewId) : null;

            mRefreshContent.setScrollBoundaryDecider(mScrollBoundaryDecider);
            mRefreshContent.setEnableLoadMoreWhenContentNotFull(mEnableLoadMoreWhenContentNotFull);
            mRefreshContent.setUpComponent(mKernel, fixedHeaderView, fixedFooterView);
        }
        return this;
    }

    /**
     * ?????????????????????????????????
     *
     * @return RefreshFooter
     */
    @Nullable
    @Override
    public RefreshFooter getRefreshFooter() {
        return mRefreshFooter;
    }

    /**
     * ?????????????????????????????????
     *
     * @return RefreshHeader
     */
    @Nullable
    @Override
    public RefreshHeader getRefreshHeader() {
        return mRefreshHeader;
    }

    /**
     * ????????????
     *
     * @return RefreshState
     */
    @Override
    public RefreshState getState() {
        return mState;
    }

    /**
     * ????????????????????????
     *
     * @return SmartRefreshLayout
     */
    @Override
    public SmartRefreshLayout getLayout() {
        return this;
    }

    /**
     * ???????????????????????????
     *
     * @param listener ???????????????
     * @return SmartRefreshLayout
     */
    @Override
    public SmartRefreshLayout setOnRefreshListener(OnRefreshListener listener) {
        this.mRefreshListener = listener;
        return this;
    }

    /**
     * ????????????????????????????????????
     *
     * @param listener ???????????????
     * @return SmartRefreshLayout
     */
    @Override
    public SmartRefreshLayout setOnLoadMoreListener(OnLoadMoreListener listener) {
        this.mLoadMoreListener = listener;
        this.mEnableLoadMore = mEnableLoadMore || (!mManualLoadMore && listener != null);
        return this;
    }

    /**
     * ???????????????????????????
     *
     * @param listener ?????????????????????
     * @return SmartRefreshLayout
     */
    @Override
    public SmartRefreshLayout setOnRefreshLoadMoreListener(OnRefreshLoadMoreListener listener) {
        this.mRefreshListener = listener;
        this.mLoadMoreListener = listener;
        this.mEnableLoadMore = mEnableLoadMore || (!mManualLoadMore && listener != null);
        return this;
    }

    /**
     * ????????????????????????
     *
     * @param listener ???????????? {@link com.yryc.imkit.widget.refresh.listener.SimpleMultiPurposeListener}
     * @return SmartRefreshLayout
     */
    @Override
    public SmartRefreshLayout setOnMultiPurposeListener(OnMultiPurposeListener listener) {
        this.mOnMultiPurposeListener = listener;
        return this;
    }

    /**
     * ??????????????????
     *
     * @param primaryColors ????????????
     * @return SmartRefreshLayout
     */
    @Override
    public SmartRefreshLayout setPrimaryColors(@ColorInt int... primaryColors) {
        if (mRefreshHeader != null) {
            mRefreshHeader.setPrimaryColors(primaryColors);
        }
        if (mRefreshFooter != null) {
            mRefreshFooter.setPrimaryColors(primaryColors);
        }
        mPrimaryColors = primaryColors;
        return this;
    }

    /**
     * ??????????????????
     *
     * @param primaryColorId ????????????ID
     * @return SmartRefreshLayout
     */
    @Override
    public SmartRefreshLayout setPrimaryColorsId(@ColorRes int... primaryColorId) {
        int[] colors = new int[primaryColorId.length];
        for (int i = 0; i < primaryColorId.length; i++) {
            colors[i] = ContextCompat.getColor(getContext(), primaryColorId[i]);
        }
        setPrimaryColors(colors);
        return this;
    }

    /**
     * ??????????????????
     *
     * @param boundary ???????????? {@link com.yryc.imkit.widget.refresh.impl.ScrollBoundaryDeciderAdapter}
     * @return SmartRefreshLayout
     */
    @Override
    public RefreshLayout setScrollBoundaryDecider(ScrollBoundaryDecider boundary) {
        mScrollBoundaryDecider = boundary;
        if (mRefreshContent != null) {
            mRefreshContent.setScrollBoundaryDecider(boundary);
        }
        return this;
    }

    /**
     * ???????????????????????????????????????
     *
     * @param noMoreData ?????????????????????
     * @return SmartRefreshLayout
     */
    @Override
    public SmartRefreshLayout setNoMoreData(boolean noMoreData) {
        mFooterNoMoreData = noMoreData;
        if (mRefreshFooter != null && !mRefreshFooter.setNoMoreData(noMoreData)) {
            System.out.println("Footer:" + mRefreshFooter + "?????????????????????");
        }
        return this;
    }

    /**
     * ????????????
     *
     * @return SmartRefreshLayout
     */
    @Override
    public SmartRefreshLayout finishRefresh() {
        long passTime = System.currentTimeMillis() - mLastRefreshingTime;
        return finishRefresh(Math.max(0, 1000 - (int) passTime));//?????????????????????1000???????????????
    }

    /**
     * ????????????
     *
     * @return SmartRefreshLayout
     */
    @Override
    public SmartRefreshLayout finishLoadMore() {
        long passTime = System.currentTimeMillis() - mLastLoadingTime;
        return finishLoadMore(Math.max(0, 1000 - (int) passTime));//?????????????????????1000???????????????
    }

    /**
     * ????????????
     *
     * @param delayed ????????????
     * @return SmartRefreshLayout
     */
    @Override
    public SmartRefreshLayout finishRefresh(int delayed) {
        return finishRefresh(delayed, true);
    }

    /**
     * ????????????
     *
     * @param success ???????????????????????? ?????????????????????????????????????????????
     * @return SmartRefreshLayout
     */
    @Override
    public SmartRefreshLayout finishRefresh(boolean success) {
        long passTime = System.currentTimeMillis() - mLastRefreshingTime;
        return finishRefresh(success ? Math.max(0, 1000 - (int) passTime) : 0, success);//?????????????????????1000???????????????
    }

    /**
     * ????????????
     *
     * @param delayed ????????????
     * @param success ???????????????????????? ?????????????????????????????????????????????
     * @return SmartRefreshLayout
     */
    @Override
    public SmartRefreshLayout finishRefresh(int delayed, final boolean success) {
        postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mState == Refreshing && mRefreshHeader != null && mRefreshContent != null) {
                    notifyStateChanged(RefreshFinish);
                    int startDelay = mRefreshHeader.onFinish(SmartRefreshLayout.this, success);
                    if (mOnMultiPurposeListener != null) {
                        mOnMultiPurposeListener.onHeaderFinish(mRefreshHeader, success);
                    }
                    if (startDelay < Integer.MAX_VALUE) {
                        if (mIsBeingDragged) {
                            mTouchSpinner = 0;
                            mTouchY = mLastTouchY;
                            mIsBeingDragged = false;
                            long time = System.currentTimeMillis();
                            SmartRefreshLayout.super.dispatchTouchEvent(obtain(time, time, MotionEvent.ACTION_DOWN, mLastTouchX, mTouchY + mSpinner - mTouchSlop * 2, 0));
                            SmartRefreshLayout.super.dispatchTouchEvent(obtain(time, time, MotionEvent.ACTION_MOVE, mLastTouchX, mTouchY + mSpinner, 0));
                        }
                        if (mSpinner > 0) {
                            AnimatorUpdateListener updateListener = null;
                            ValueAnimator valueAnimator = animSpinner(0, startDelay, mReboundInterpolator, mReboundDuration);
                            if (mEnableScrollContentWhenRefreshed) {
                                updateListener = mRefreshContent.scrollContentWhenFinished(mSpinner);
                            }
                            if (valueAnimator != null && updateListener != null) {
                                valueAnimator.addUpdateListener(updateListener);
                            }
                        } else if (mSpinner < 0) {
                            animSpinner(0, startDelay, mReboundInterpolator, mReboundDuration);
                        } else {
                            moveSpinner(0, true);
                            resetStatus();
                        }
                    }
                }
            }
        }, delayed <= 0 ? 1 : delayed);
        return this;
    }

    /**
     * ????????????
     *
     * @param delayed ????????????
     * @return SmartRefreshLayout
     */
    @Override
    public SmartRefreshLayout finishLoadMore(int delayed) {
        return finishLoadMore(delayed, true, false);
    }

    /**
     * ????????????
     *
     * @param success ??????????????????
     * @return SmartRefreshLayout
     */
    @Override
    public SmartRefreshLayout finishLoadMore(boolean success) {
        long passTime = System.currentTimeMillis() - mLastLoadingTime;
        return finishLoadMore(success ? Math.max(0, 1000 - (int) passTime) : 0, success, false);
    }

    /**
     * ????????????
     *
     * @param delayed    ????????????
     * @param success    ??????????????????
     * @param noMoreData ?????????????????????
     * @return SmartRefreshLayout
     */
    @Override
    public SmartRefreshLayout finishLoadMore(int delayed, final boolean success, final boolean noMoreData) {
        postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mState == Loading && mRefreshFooter != null && mRefreshContent != null) {
                    notifyStateChanged(LoadFinish);
                    final int startDelay = mRefreshFooter.onFinish(SmartRefreshLayout.this, success);
                    if (mOnMultiPurposeListener != null) {
                        mOnMultiPurposeListener.onFooterFinish(mRefreshFooter, success);
                    }
                    if (startDelay < Integer.MAX_VALUE) {
                        //????????????????????????????????????
                        final boolean needHoldFooter = noMoreData && mEnableFooterFollowWhenLoadFinished && mSpinner < 0 && mRefreshContent.canLoadMore();
                        final int offset = mSpinner - (needHoldFooter ? Math.max(mSpinner, -mFooterHeight) : 0);
                        //???????????????????????????????????????????????????
                        if (mIsBeingDragged) {
                            mTouchSpinner = mSpinner - offset;
                            mTouchY = mLastTouchY;
                            mIsBeingDragged = false;
                            final long time = System.currentTimeMillis();
                            SmartRefreshLayout.super.dispatchTouchEvent(obtain(time, time, MotionEvent.ACTION_DOWN, mLastTouchX, mTouchY + offset + mTouchSlop * 2, 0));
                            SmartRefreshLayout.super.dispatchTouchEvent(obtain(time, time, MotionEvent.ACTION_MOVE, mLastTouchX, mTouchY + offset, 0));
                        }
                        //??????????????????????????????
                        postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                AnimatorUpdateListener updateListener = null;
                                if (mEnableScrollContentWhenLoaded && offset < 0) {
                                    updateListener = mRefreshContent.scrollContentWhenFinished(mSpinner);
                                }
                                if (updateListener != null) {
                                    updateListener.onAnimationUpdate(ValueAnimator.ofInt(0, 0));
                                }
                                ValueAnimator animator = null;
                                AnimatorListenerAdapter listenerAdapter = new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationCancel(Animator animation) {
                                        super.onAnimationEnd(animation);
                                    }

                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        mFooterLocked = false;
                                        if (noMoreData) {
                                            setNoMoreData(true);
                                        }
                                        if (mState == LoadFinish) {
                                            notifyStateChanged(None);
                                        }
                                    }
                                };
                                if (mSpinner > 0) {
                                    animator = animSpinner(0);
                                } else if (updateListener != null || mSpinner == 0) {
                                    if (reboundAnimator != null) {
                                        reboundAnimator.cancel();
                                        reboundAnimator = null;
                                    }
                                    moveSpinner(0, true);
                                    resetStatus();
                                } else {
                                    if (noMoreData && mEnableFooterFollowWhenLoadFinished) {
                                        if (mSpinner >= -mFooterHeight) {
                                            notifyStateChanged(None);
                                        } else {
                                            animator = animSpinner(-mFooterHeight);
                                        }
                                    } else {
                                        animator = animSpinner(0);
                                    }
                                }
                                if (animator != null) {
                                    animator.addListener(listenerAdapter);
                                } else {
                                    listenerAdapter.onAnimationEnd(null);
                                }
                            }
                        }, mSpinner < 0 ? startDelay : 0);
                    }
                } else {
                    if (noMoreData) {
                        setNoMoreData(true);
                    }
                }
            }
        }, delayed <= 0 ? 1 : delayed);
        return this;
    }

    /**
     * ???????????????????????????????????????
     *
     * @return SmartRefreshLayout
     */
    @Override
    public SmartRefreshLayout finishLoadMoreWithNoMoreData() {
        long passTime = System.currentTimeMillis() - mLastLoadingTime;
        return finishLoadMore(Math.max(0, 1000 - (int) passTime), true, true);
    }

    /**
     * ??????????????????
     *
     * @return ??????????????????
     */
    @Override
    public boolean isRefreshing() {
        return mState == Refreshing;
    }

    /**
     * ??????????????????
     *
     * @return ??????????????????
     */
    @Override
    public boolean isLoading() {
        return mState == Loading;
    }

    /**
     * ????????????
     *
     * @return ????????????
     */
    @Override
    public boolean autoRefresh() {
        return autoRefresh(mHandler == null ? 400 : 0);
    }

    /**
     * ????????????
     *
     * @param delayed ????????????
     * @return ????????????
     */
    @Override
    public boolean autoRefresh(int delayed) {
        return autoRefresh(delayed, mReboundDuration, 1f * (mHeaderHeight + mHeaderExtendHeight / 2) / (mHeaderHeight == 0 ? 1 : mHeaderHeight));
    }

    /**
     * ????????????
     *
     * @param delayed  ????????????
     * @param duration ????????????????????????
     * @param dragRate ?????????????????????????????? ??? 1 ???
     * @return ????????????
     */
    @Override
    public boolean autoRefresh(int delayed, final int duration, final float dragRate) {
        if (mState == None && isEnableRefresh()) {
            if (reboundAnimator != null) {
                reboundAnimator.cancel();
            }
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    reboundAnimator = ValueAnimator.ofInt(mSpinner, (int) (mHeaderHeight * dragRate));
                    reboundAnimator.setDuration(duration);
                    reboundAnimator.setInterpolator(new DecelerateInterpolator());
                    reboundAnimator.addUpdateListener(new AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            moveSpinner((int) animation.getAnimatedValue(), false);
                        }
                    });
                    reboundAnimator.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            mLastTouchX = getMeasuredWidth() / 2;
                            mKernel.setState(PullDownToRefresh);
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            reboundAnimator = null;
                            if (mState != ReleaseToRefresh) {
                                mKernel.setState(ReleaseToRefresh);
                            }
                            overSpinner();
                        }
                    });
                    reboundAnimator.start();
                }
            };
            if (delayed > 0) {
                reboundAnimator = new ValueAnimator();
                postDelayed(runnable, delayed);
            } else {
                runnable.run();
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * ????????????
     *
     * @return ????????????
     */
    @Override
    public boolean autoLoadMore() {
        return autoLoadMore(0);
    }

    /**
     * ????????????
     *
     * @param delayed ????????????
     * @return ????????????
     */
    @Override
    public boolean autoLoadMore(int delayed) {
        return autoLoadMore(delayed, mReboundDuration, 1f * (mFooterHeight + mFooterExtendHeight / 2) / (mFooterHeight == 0 ? 1 : mFooterHeight));
    }

    /**
     * ????????????
     *
     * @param delayed  ????????????
     * @param duration ????????????????????????
     * @param dragRate ?????????????????????????????? ??? 1 ???
     * @return ????????????
     */
    @Override
    public boolean autoLoadMore(int delayed, final int duration, final float dragRate) {
        if (mState == None && (isEnableLoadMore() && !mFooterNoMoreData)) {
            if (reboundAnimator != null) {
                reboundAnimator.cancel();
            }
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    reboundAnimator = ValueAnimator.ofInt(mSpinner, -(int) (mFooterHeight * dragRate));
                    reboundAnimator.setDuration(duration);
                    reboundAnimator.setInterpolator(new DecelerateInterpolator());
                    reboundAnimator.addUpdateListener(new AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            moveSpinner((int) animation.getAnimatedValue(), false);
                        }
                    });
                    reboundAnimator.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            mLastTouchX = getMeasuredWidth() / 2;
                            mKernel.setState(PullUpToLoad);
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            reboundAnimator = null;
                            if (mState != ReleaseToLoad) {
                                mKernel.setState(ReleaseToLoad);
                            }
                            if (mEnableAutoLoadMore) {
                                mEnableAutoLoadMore = false;
                                overSpinner();
                                mEnableAutoLoadMore = true;
                            } else {
                                overSpinner();
                            }
                        }
                    });
                    reboundAnimator.start();
                }
            };
            if (delayed > 0) {
                reboundAnimator = new ValueAnimator();
                postDelayed(runnable, delayed);
            } else {
                runnable.run();
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isEnableRefresh() {
        return mEnableRefresh && !mEnablePureScrollMode;
    }

    @Override
    public boolean isEnableLoadMore() {
        return mEnableLoadMore && !mEnablePureScrollMode;
    }

    /**
     * ???????????? Header ?????????
     *
     * @param creator Header?????????
     */
    public static void setDefaultRefreshHeaderCreator(@NonNull DefaultRefreshHeaderCreator creator) {
        sHeaderCreator = creator;
    }

    /**
     * ???????????? Footer ?????????
     *
     * @param creator Footer?????????
     */
    public static void setDefaultRefreshFooterCreator(@NonNull DefaultRefreshFooterCreator creator) {
        sFooterCreator = creator;
        sManualFooterCreator = true;
    }

    //<editor-fold desc="?????????API">

    /**
     * ???????????????????????????????????????
     *
     * @return SmartRefreshLayout
     * @deprecated ?????? {@link RefreshLayout#setNoMoreData(boolean)} ??????
     */
    @Override
    @Deprecated
    public SmartRefreshLayout resetNoMoreData() {
        return setNoMoreData(false);
    }

    /**
     * ???????????????????????????????????????
     *
     * @param finished ?????????????????????
     * @return SmartRefreshLayout
     * @deprecated ???????????????????????? ?????? {@link RefreshLayout#setNoMoreData(boolean)} ??????
     */
    @Override
    @Deprecated
    public SmartRefreshLayout setLoadmoreFinished(boolean finished) {
        return setNoMoreData(finished);
    }

    /**
     * ????????????
     *
     * @return SmartRefreshLayout
     * @deprecated ?????? {@link #finishLoadMore()} ??????
     */
    @Override
    @Deprecated
    public SmartRefreshLayout finishLoadmore() {
        return finishLoadMore();
    }

    /**
     * ????????????
     *
     * @return SmartRefreshLayout
     * @deprecated ?????? {@link #finishLoadMore(int)} ??????
     */
    @Override
    @Deprecated
    public SmartRefreshLayout finishLoadmore(int delayed) {
        return finishLoadMore(delayed);
    }

    /**
     * ????????????
     *
     * @return SmartRefreshLayout
     * @deprecated ?????? {@link #finishLoadMore(boolean)} ??????
     */
    @Override
    @Deprecated
    public SmartRefreshLayout finishLoadmore(boolean success) {
        return finishLoadMore(success);
    }

    /**
     * ???????????????????????????????????????
     *
     * @return SmartRefreshLayout
     * @deprecated ?????? {@link #finishLoadMoreWithNoMoreData()} ??????
     */
    @Override
    @Deprecated
    public SmartRefreshLayout finishLoadmoreWithNoMoreData() {
        return finishLoadMoreWithNoMoreData();
    }

    /**
     * @return boolean
     * @deprecated ????????????????????????
     */
    @Override
    @Deprecated
    public boolean isLoadmoreFinished() {
        return mFooterNoMoreData;
    }

    /**
     * @return boolean
     * @deprecated ????????????????????????
     */
    @Override
    @Deprecated
    public boolean isEnableAutoLoadMore() {
        return mEnableAutoLoadMore;
    }

    /**
     * @return boolean
     * @deprecated ????????????????????????
     */
    @Override
    @Deprecated
    public boolean isEnableOverScrollBounce() {
        return mEnableOverScrollBounce;
    }

    /**
     * @return boolean
     * @deprecated ????????????????????????
     */
    @Override
    @Deprecated
    public boolean isEnablePureScrollMode() {
        return mEnablePureScrollMode;
    }

    /**
     * @return boolean
     * @deprecated ????????????????????????
     */
    @Override
    @Deprecated
    public boolean isEnableScrollContentWhenLoaded() {
        return mEnableScrollContentWhenLoaded;
    }


    /**
     * ???????????????????????????
     *
     * @return SmartRefreshLayout
     * @deprecated ?????? {@link #setOnLoadMoreListener(OnLoadMoreListener)} ??????
     */
    @Override
    @Deprecated
    public SmartRefreshLayout setOnLoadmoreListener(final OnLoadmoreListener listener) {
        return setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                listener.onLoadmore(refreshLayout);
            }
        });
    }

    /**
     * ????????????????????????????????????
     *
     * @return SmartRefreshLayout
     * @deprecated ?????? {@link #setOnRefreshLoadMoreListener(OnRefreshLoadMoreListener)} ??????
     */
    @Override
    @Deprecated
    public SmartRefreshLayout setOnRefreshLoadmoreListener(final OnRefreshLoadmoreListener listener) {
        return setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                listener.onLoadmore(refreshLayout);
            }

            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                listener.onRefresh(refreshLayout);
            }
        });
    }

    /**
     * ????????????Header?????????
     *
     * @param creator ??????????????????
     * @deprecated ?????? {@link #setDefaultRefreshHeaderCreator(DefaultRefreshHeaderCreator)} ??????
     */
    @Deprecated
    public static void setDefaultRefreshHeaderCreater(@NonNull DefaultRefreshHeaderCreater creator) {
        sHeaderCreator = creator;
    }

    /**
     * ????????????Footer?????????
     *
     * @param creator ??????????????????
     * @deprecated ?????? {@link #setDefaultRefreshFooterCreator(DefaultRefreshFooterCreator)} ??????
     */
    @Deprecated
    public static void setDefaultRefreshFooterCreater(@NonNull DefaultRefreshFooterCreater creator) {
        sFooterCreator = creator;
        sManualFooterCreator = true;
    }

    //</editor-fold>

    //</editor-fold>

    //<editor-fold desc="???????????? RefreshKernel">
    public class RefreshKernelImpl implements RefreshKernel {

        @NonNull
        @Override
        public RefreshLayout getRefreshLayout() {
            return SmartRefreshLayout.this;
        }

        @NonNull
        @Override
        public RefreshContent getRefreshContent() {
            return mRefreshContent;
        }

        @Override
        public RefreshKernel setState(@NonNull RefreshState state) {
            switch (state) {
                case None:
                    resetStatus();
                    break;
                case PullDownToRefresh:
                    if (!mState.opening && isEnableRefresh()) {
                        notifyStateChanged(PullDownToRefresh);
                    } else {
                        setViceState(PullDownToRefresh);
                    }
                    break;
                case PullUpToLoad:
                    if (isEnableLoadMore() && !mState.opening && !mState.finishing && !(mFooterNoMoreData && mEnableFooterFollowWhenLoadFinished)) {
                        notifyStateChanged(PullUpToLoad);
                    } else {
                        setViceState(PullUpToLoad);
                    }
                    break;
                case PullDownCanceled:
                    if (!mState.opening && isEnableRefresh()) {
                        notifyStateChanged(PullDownCanceled);
                        resetStatus();
                    } else {
                        setViceState(PullDownCanceled);
                    }
                    break;
                case PullUpCanceled:
                    if (isEnableLoadMore() && !mState.opening && !(mFooterNoMoreData && mEnableFooterFollowWhenLoadFinished)) {
                        notifyStateChanged(PullUpCanceled);
                        resetStatus();
                    } else {
                        setViceState(PullUpCanceled);
                    }
                    break;
                case ReleaseToRefresh:
                    if (!mState.opening && isEnableRefresh()) {
                        notifyStateChanged(ReleaseToRefresh);
                    } else {
                        setViceState(ReleaseToRefresh);
                    }
                    break;
                case ReleaseToLoad:
                    if (isEnableLoadMore() && !mState.opening && !mState.finishing && !(mFooterNoMoreData && mEnableFooterFollowWhenLoadFinished)) {
                        notifyStateChanged(ReleaseToLoad);
                    } else {
                        setViceState(ReleaseToLoad);
                    }
                    break;
                case ReleaseToTwoLevel: {
                    if (!mState.opening && isEnableRefresh()) {
                        notifyStateChanged(ReleaseToTwoLevel);
                    } else {
                        setViceState(ReleaseToTwoLevel);
                    }
                    break;
                }
                case RefreshReleased: {
                    if (!mState.opening && isEnableRefresh()) {
                        notifyStateChanged(RefreshReleased);
                    } else {
                        setViceState(RefreshReleased);
                    }
                    break;
                }
                case LoadReleased: {
                    if (!mState.opening && isEnableLoadMore()) {
                        notifyStateChanged(LoadReleased);
                    } else {
                        setViceState(LoadReleased);
                    }
                    break;
                }
                case Refreshing:
                    setStateRefreshing();
                    break;
                case Loading:
                    setStateLoading();
                    break;
                case RefreshFinish: {
                    if (mState == Refreshing) {
                        notifyStateChanged(RefreshFinish);
                    }
                    break;
                }
                case LoadFinish: {
                    if (mState == Loading) {
                        notifyStateChanged(LoadFinish);
                    }
                    break;
                }
                case TwoLevelReleased:
                    notifyStateChanged(TwoLevelReleased);
                    break;
                case TwoLevelFinish:
                    notifyStateChanged(TwoLevelFinish);
                    break;
                case TwoLevel:
                    notifyStateChanged(TwoLevel);
                    break;
            }
            return null;
        }

        @Override
        public RefreshKernel startTwoLevel(boolean open) {
            if (open) {
                AnimatorListenerAdapter listener = new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mKernel.setState(TwoLevel);
                    }
                };
                ValueAnimator animator = SmartRefreshLayout.this.animSpinner(getMeasuredHeight());
                if (animator != null && animator == reboundAnimator) {
                    animator.setDuration(mFloorDuration);
                    animator.addListener(listener);
                } else {
                    listener.onAnimationEnd(null);
                }
            } else {
                if (animSpinner(0) == null) {
                    notifyStateChanged(None);
                }
            }
            return this;
        }

        @Override
        public RefreshKernel finishTwoLevel() {
            if (mState == TwoLevel) {
                mKernel.setState(TwoLevelFinish);
                if (mSpinner == 0) {
                    moveSpinner(0, true);
                    notifyStateChanged(None);
                } else {
                    SmartRefreshLayout.this.animSpinner(0).setDuration(mFloorDuration);
                }
            }
            return this;
        }
        //<editor-fold desc="???????????? state changes">

        //</editor-fold>

        //<editor-fold desc="???????????? Spinner">

        public RefreshKernel moveSpinner(int spinner, boolean isAnimator) {
            SmartRefreshLayout.this.moveSpinner(spinner, isAnimator);
            return this;
        }

        public RefreshKernel animSpinner(int endSpinner) {
            SmartRefreshLayout.this.animSpinner(endSpinner);
            return this;
        }

        //</editor-fold>

        //<editor-fold desc="????????????">
        @Override
        public RefreshKernel requestDrawBackgroundForHeader(int backgroundColor) {
            if (mPaint == null && backgroundColor != 0) {
                mPaint = new Paint();
            }
            mHeaderBackgroundColor = backgroundColor;
            return this;
        }

        @Override
        public RefreshKernel requestDrawBackgroundForFooter(int backgroundColor) {
            if (mPaint == null && backgroundColor != 0) {
                mPaint = new Paint();
            }
            mFooterBackgroundColor = backgroundColor;
            return this;
        }

        @Override
        public RefreshKernel requestNeedTouchEventWhenRefreshing(boolean request) {
            mHeaderNeedTouchEventWhenRefreshing = request;
            return this;
        }

        @Override
        public RefreshKernel requestNeedTouchEventWhenLoading(boolean request) {
            mFooterNeedTouchEventWhenLoading = request;
            return this;
        }

        @Override
        public RefreshKernel requestDefaultHeaderTranslationContent(boolean translation) {
            if (!mManualHeaderTranslationContent) {
                mManualHeaderTranslationContent = true;
                mEnableHeaderTranslationContent = translation;
            }
            return this;
        }

        @Override
        public RefreshKernel requestRemeasureHeightForHeader() {
            if (mHeaderHeightStatus.notified) {
                mHeaderHeightStatus = mHeaderHeightStatus.unNotify();
            }
            return this;
        }

        @Override
        public RefreshKernel requestRemeasureHeightForFooter() {
            if (mFooterHeightStatus.notified) {
                mFooterHeightStatus = mFooterHeightStatus.unNotify();
            }
            return this;
        }

        @Override
        public RefreshKernel requestFloorDuration(int duration) {
            mFloorDuration = duration;
            return this;
        }
        //</editor-fold>
    }
    //</editor-fold>

    //<editor-fold desc="???????????? postDelayed??????">

    @Override
    public boolean post(@NonNull Runnable action) {
        if (mHandler == null) {
            mDelayedRunnables = mDelayedRunnables == null ? new ArrayList<DelayedRunnable>() : mDelayedRunnables;
            mDelayedRunnables.add(new DelayedRunnable(action));
            return false;
        }
        return mHandler.post(new DelayedRunnable(action));
    }

    @Override
    public boolean postDelayed(@NonNull Runnable action, long delayMillis) {
        if (delayMillis == 0) {
            new DelayedRunnable(action).run();
            return true;
        }
        if (mHandler == null) {
            mDelayedRunnables = mDelayedRunnables == null ? new ArrayList<DelayedRunnable>() : mDelayedRunnables;
            mDelayedRunnables.add(new DelayedRunnable(action, delayMillis));
            return false;
        }
        return mHandler.postDelayed(new DelayedRunnable(action), delayMillis);
    }

    //</editor-fold>
}
