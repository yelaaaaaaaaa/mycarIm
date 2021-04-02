package com.yryc.imkit.ui.activity;

import android.view.View;

import com.yryc.imkit.ui.fragment.ChatContentFragment;
import com.yryc.imkit.ui.fragment.OneChatContentFragment;
import com.yryc.imkit.ui.view.TitleBar;
import com.yryc.imlib.xmpp.LoginUser;

/**
 * @author : Mai_Xiao_Peng
 * @email : Mai_Xiao_Peng@163.com
 * @time : 2018/12/11 11:49
 * @describe :
 */


public class OneChatContentActivity extends ChatContentActivity {

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
        titleBar.setTitle(TitleBar.TITLE_TYPE_LEFT, LoginUser.getInstance().getToUser());
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
    public ChatContentFragment addChatContent2BodyContainer() {
        OneChatContentFragment oneChatContentFragment = new OneChatContentFragment();
        return oneChatContentFragment;
    }

    @Override
    public View addView2BottomContainer() {
        return null;
    }

}
