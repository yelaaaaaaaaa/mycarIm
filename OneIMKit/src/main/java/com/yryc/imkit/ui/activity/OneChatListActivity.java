package com.yryc.imkit.ui.activity;

import android.view.View;

import com.yryc.imkit.im.IM;
import com.yryc.imkit.listener.OnImChatItemClickListener;
import com.yryc.imkit.listener.OnImChatItemScrollListener;
import com.yryc.imkit.listener.OnImChatRichItemClickListener;
import com.yryc.imlib.model.chat.ChatCar;
import com.yryc.imlib.model.chat.ChatOrder;
import com.yryc.imlib.model.chat.ChatRich;
import com.yryc.imkit.ui.fragment.ChatListFragment;
import com.yryc.imkit.ui.fragment.OneChatListFragment;
import com.yryc.imkit.ui.view.FButtonView;
import com.yryc.imkit.ui.view.TitleBar;
import com.yryc.imkit.utils.CommonUtils;

/**
 * @author : Mai_Xiao_Peng
 * @email : Mai_Xiao_Peng@163.com
 * @time : 2018/12/11 11:34
 * @describe :
 */

public class OneChatListActivity extends ChatListActivity {

    @Override
    protected void initView() {
        super.initView();
    }

    @Override
    protected void initData() {
        super.initData();
        IM.sImUtils.imOptions.onImChatItemClickListener = new OnImChatItemClickListener() {
            @Override
            public void onChatOrderClick(ChatOrder chatOrder) {

            }

            @Override
            public void onChatCarClick(ChatCar chatCar) {

            }

            @Override
            public void onChatRichBodyClick(ChatRich.Body body) {

            }
        };
        IM.sImUtils.imOptions.onImChatItemScrollListener = new OnImChatItemScrollListener() {
            @Override
            public void onChatOrderVisibilityState(ChatOrder chatOrder) {

            }
        };
        IM.sImUtils.imOptions.onImChatRichItemClickListener = new OnImChatRichItemClickListener() {
            @Override
            public void onRichItemClick(FButtonView fButtonView, ChatRich.Body.Button button) {
                IM.sImUtils.imOptions.onImChatRichItemNetResponseListener.onImChatRichItemNetResponse(fButtonView);
            }
        };
    }

    @Override
    public View addView2TopContainer() {
        TitleBar titleBar = new TitleBar(this);
        titleBar.setTitle("消息");
        titleBar.setOnTitleBarEventListener(new TitleBar.OnTitleBarEventListener() {
            @Override
            public void onRightTextClick() {

            }

            @Override
            public void onTitleTextClick() {
                String tempUserID = CommonUtils.getTempUserID();
                IM.sImUtils.openSingleChatActivity(OneChatListActivity.this, tempUserID);
            }
        });
        return titleBar;
    }

    @Override
    public ChatListFragment addChatContent2BodyContainer() {
        return new OneChatListFragment();
    }

    @Override
    public View addView2BottomContainer() {
        return null;
    }

}
