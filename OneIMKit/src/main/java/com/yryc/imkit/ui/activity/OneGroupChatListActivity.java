package com.yryc.imkit.ui.activity;

import android.view.View;

import com.yryc.imkit.im.IM;
import com.yryc.imkit.listener.OnImChatItemClickListener;
import com.yryc.imkit.listener.OnImChatItemScrollListener;
import com.yryc.imkit.listener.OnImChatRichItemClickListener;
import com.yryc.imlib.model.chat.ChatCar;
import com.yryc.imlib.model.chat.ChatOrder;
import com.yryc.imlib.model.chat.ChatRich;
import com.yryc.imkit.ui.fragment.GroupChatListFragment;
import com.yryc.imkit.ui.fragment.OneGroupChatListFragment;
import com.yryc.imkit.ui.view.FButtonView;
import com.yryc.imkit.ui.view.TitleBar;
import com.yryc.imkit.utils.CommonUtils;

/**
 * Description: 群聊列表
 * <p>
 * Author: czk
 * Date：2020/5/28 10:57
 */

public class OneGroupChatListActivity extends GroupChatListActivity {

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
        titleBar.setTitle("群聊消息");
        titleBar.setOnTitleBarEventListener(new TitleBar.OnTitleBarEventListener() {
            @Override
            public void onRightTextClick() {

            }

            @Override
            public void onTitleTextClick() {
                String tempUserID = CommonUtils.getTempUserID();
                IM.sImUtils.openSingleChatActivity(OneGroupChatListActivity.this, tempUserID);
            }
        });
        return titleBar;
    }

    @Override
    public GroupChatListFragment addChatContent2BodyContainer() {
        return new OneGroupChatListFragment();
    }

    @Override
    public View addView2BottomContainer() {
        return null;
    }

}
