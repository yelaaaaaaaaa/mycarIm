package com.yryc.imkit.ui.activity;

import android.view.View;
import android.widget.FrameLayout;

import com.yryc.imkit.R;
import com.yryc.imkit.base.BaseActivity;
import com.yryc.imkit.impl.IAChatListAction;
import com.yryc.imkit.ui.fragment.ChatListFragment;

/**
 * @author : Mai_Xiao_Peng
 * @email : Mai_Xiao_Peng@163.com
 * @time : 2018/12/3 16:25
 * @describe :
 */

public abstract class ChatListActivity extends BaseActivity implements IAChatListAction<ChatListFragment> {

    protected ChatListFragment chatContentFragment;
    protected FrameLayout flTopContainer;
    protected FrameLayout flBodyContainer;
    protected FrameLayout flBottomContainer;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_onechat_list;
    }

    @Override
    protected void initView() {
        flTopContainer = findViewById(R.id.fl_top_container);
        flBodyContainer = findViewById(R.id.fl_body_container);
        flBottomContainer = findViewById(R.id.fl_bottom_container);
        View addView2TopContainer = addView2TopContainer();
        if (addView2TopContainer != null) {
            flTopContainer.addView(addView2TopContainer);
        }
        View addView2BottomContainer = addView2BottomContainer();
        if (addView2BottomContainer != null) {
            flBottomContainer.addView(addView2BottomContainer);
        }
        chatContentFragment = addChatContent2BodyContainer();
        if (chatContentFragment != null) {
            setSupportFragment(R.id.fl_body_container, chatContentFragment);
        }
    }

    @Override
    protected void initData() {

    }

}
