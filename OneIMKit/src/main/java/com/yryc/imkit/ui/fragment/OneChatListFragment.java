package com.yryc.imkit.ui.fragment;

import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.yryc.imkit.R;
import com.yryc.imkit.core.slim.viewinjector.IViewInjector;
import com.yryc.imkit.im.IM;
import com.yryc.imlib.model.chat.ChatList;
import com.yryc.imkit.utils.DatabindingViewUtil;
import com.yryc.imkit.widget.swipemenu.SwipeHorizontalMenuLayout;


/**
 * @author : Mai_Xiao_Peng
 * @email : Mai_Xiao_Peng@163.com
 * @time : 2018/12/11 11:38
 * @describe :
 */


public class OneChatListFragment extends ChatListFragment {

    /**
     * 获取布局item
     *
     * @return
     */
    @Override
    public int getChatListItemLayoutId() {
        return R.layout.item_chat_list;
    }

    /**
     * 注入数据
     *
     * @param chatList
     * @param injector
     */
    @Override
    public void injectChatListItemDataSource(final ChatList chatList, IViewInjector injector) {
        injector.text(R.id.tv_message_title, chatList.getUserName())
                .text(R.id.tv_message_desc, chatList.getLastWords())
                .text(R.id.tv_message_time, DatabindingViewUtil.LongTimeToDetailedTime(chatList.getLastWordsTime()))
                .clicked(R.id.ll_chat_list, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (IM.sImUtils.imOptions.onImChatListEventListener != null) {
                            IM.sImUtils.syncUpdateUserInfo(getContext(), chatList.getUserId(), "", "", "", false);
                            IM.sImUtils.imOptions.onImChatListEventListener.onChatListItemClick(chatList.getUserId(), chatList.getUserName());
                        } else {
                            IM.sImUtils.openSingleChatActivity(getContext(), chatList.getUserId());
                        }
                    }
                })
                .clicked(R.id.tv_delete, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //OIMClient.getInstance().deleteConversationListByUserid(chatList.getUserId());
                        SwipeHorizontalMenuLayout swipeHorizontalMenuLayout = (SwipeHorizontalMenuLayout) injector.findViewById(R.id.swipe_menu);
                        swipeHorizontalMenuLayout.smoothCloseMenu();
                    }
                });
        if (chatList.getNumberOfUnreadMessages() == 0) {
            injector.visibility(R.id.tv_message_number, View.GONE);
        } else {
            injector.visibility(R.id.tv_message_number, View.VISIBLE)
                    .text(R.id.tv_message_number, DatabindingViewUtil.NumberOfUnreadMessages(chatList.getNumberOfUnreadMessages()));
        }
        Glide.with(getContext()).load(chatList.getHaedImageUrl())
                .apply(new RequestOptions().placeholder(R.drawable.ic_im_avatar_default).error(R.drawable.ic_im_avatar_default))
                .into((ImageView) injector.findViewById(R.id.iv_chat_list_avatar));
    }

}
