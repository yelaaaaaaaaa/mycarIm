package com.yryc.imkit.ui.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.yryc.imkit.R;
import com.yryc.imkit.base.BaseFragment;
import com.yryc.imkit.ui.fragment.FunctionalAreaFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : Mai_Xiao_Peng
 * @email : Mai_Xiao_Peng@163.com
 * @time : 2018/12/5 12:02
 * @describe :
 */


public class ImMoreView extends LinearLayout {

    private View view;
    private ViewPager vpMore;
    private ImLanguageView imLanguage;
    private FrameLayout flAreaContainer;
    private FunctionalAreaFragment functionalAreaFragment;

    public ImMoreView(Context context) {
        this(context, null);
    }

    public ImMoreView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImMoreView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr);
    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr) {
        view = View.inflate(context, R.layout.view_im_more, this);
        vpMore = view.findViewById(R.id.vp_more);
        List<BaseFragment> fragmentList = new ArrayList<>();
        functionalAreaFragment = new FunctionalAreaFragment();
        functionalAreaFragment.setOnLanguageItemClickListener(new FunctionalAreaFragment.OnLanguageItemClickListener() {
            @Override
            public void onClick() {
                resetShowState(false);
            }
        });
        fragmentList.add(functionalAreaFragment);
        vpMore.setAdapter(new FunctionAreaAdapter(((AppCompatActivity) getContext()).getSupportFragmentManager(), fragmentList));
        flAreaContainer = view.findViewById(R.id.fl_area_container);
        imLanguage = new ImLanguageView(getContext());
        flAreaContainer.addView(imLanguage);
        setVisibility(View.GONE);
    }

    public void resetShowState(boolean isShowMore) {
        if (isShowMore) {
            vpMore.setVisibility(View.VISIBLE);
            flAreaContainer.setVisibility(View.GONE);
        } else {
            vpMore.setVisibility(View.GONE);
            flAreaContainer.setVisibility(View.VISIBLE);
        }
    }

    static class FunctionAreaAdapter extends FragmentStatePagerAdapter {

        private List<BaseFragment> list;

        public FunctionAreaAdapter(FragmentManager fm, List<BaseFragment> list) {
            super(fm);
            this.list = list;
        }

        @Override
        public Fragment getItem(int i) {
            return list.get(i);
        }

        @Override
        public int getCount() {
            return list.size();
        }
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == View.GONE) {
            resetShowState(true);
        }
    }
}
