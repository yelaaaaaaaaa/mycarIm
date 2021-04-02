package com.yryc.imkit.ui.activity;

import android.view.View;

import com.yryc.imkit.ui.fragment.OneGroupChatContentFragment;
import com.yryc.imkit.ui.view.TitleBar;
import com.yryc.imlib.xmpp.LoginUser;

/**
 * Description: 群聊页面
 * <p>
 * Author: czk
 * Date：2020/5/27 14:55
 */


public class OneGroupChatContentActivity extends GroupChatContentActivity {

    @Override
    protected void initView() {
        super.initView();
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    public View addView2TopContainer() {
        TitleBar titleBar = new TitleBar(this);
        titleBar.setTitle(TitleBar.TITLE_TYPE_LEFT, LoginUser.getInstance().getGroupName());
//        titleBar.setRightText("重新确认订单价格");
        titleBar.setOnTitleBarEventListener(new TitleBar.OnTitleBarEventListener() {
            @Override
            public void onRightTextClick() {

            }

            @Override
            public void onTitleTextClick() {
            }
        });
        return titleBar;
    }

    @Override
    public OneGroupChatContentFragment addChatContent2BodyContainer() {
        OneGroupChatContentFragment oneChatContentFragment = new OneGroupChatContentFragment();
        return oneChatContentFragment;
    }

    @Override
    public View addView2BottomContainer() {
        return null;
    }

}
