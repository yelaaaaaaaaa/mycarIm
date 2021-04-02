package com.yryc.imkit.ui.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.google.gson.Gson;
import com.yryc.imkit.R;
import com.yryc.imkit.base.BaseFragment;
import com.yryc.imkit.core.slim.SlimAdapter;
import com.yryc.imkit.core.slim.SlimInjector;
import com.yryc.imkit.core.slim.viewinjector.IViewInjector;
import com.yryc.imkit.impl.IFChatListAction;
import com.yryc.imkit.widget.swipemenu.SwipeMenuRecyclerView;
import com.yryc.imkit.widget.xview.XLoadView;
import com.yryc.imlib.info.ConversationListDataInfo;
import com.yryc.imlib.info.event.GroupChatMessageEvent;
import com.yryc.imlib.model.chat.ChatList;
import com.yryc.imlib.model.net.ChatListBean;
import com.yryc.imlib.retrofit.ApiService;
import com.yryc.imlib.retrofit.RetrofitServiceCreator;
import com.yryc.imlib.rx.RxThrowableConsumer;
import com.yryc.imlib.rx.RxUtils;
import com.yryc.imlib.utils.SPManager;
import com.yryc.imlib.xmpp.ImUtil;
import com.yryc.imlib.xmpp.LoginUser;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * Description: 群聊列表
 * <p>
 * Author: czk
 * Date：2020/5/28 10:57
 */

public abstract class GroupChatListFragment extends BaseFragment implements IFChatListAction {

    /**
     * 消息列表
     */
    protected List<Object> mData = new ArrayList<>();

    private SwipeMenuRecyclerView mRvChatList;
    private XLoadView xLoadView;
    protected SlimAdapter mAdapter;
    private boolean isFirst = true;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_group_chat_list;
    }

    @Override
    public void initView() {
        mRvChatList = mView.findViewById(R.id.rv_chat_list);
        xLoadView = mView.findViewById(R.id.xlv_content);
        mRvChatList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public void initData() {
        //加载更多
        mAdapter = SlimAdapter.create().attachTo(mRvChatList).updateData(mData);
        registerChatLayoutAndData(mAdapter);
        refreshChatList(true);
        isFirst = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isFirst) {
            refreshChatList(false);
        }
    }

    /**
     * 加载聊天列表内容
     */
    public void refreshChatList(boolean isFirst) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("imUserId", SPManager.getInstance(getContext()).get(SPManager.SP_KEY_USER_ID, ""));
        if (isFirst) {
            showProgressDialog(getResources().getString(R.string.loaded_wait_moment));
        }
        ApiService apiService = RetrofitServiceCreator.createService(getContext(), ApiService.class);
        apiService.getGroupChatList(data).compose(RxUtils.rxSchedulerHelper())
                .compose(RxUtils.handleResult()).subscribe(new Consumer<ChatListBean>() {
            @Override
            public void accept(ChatListBean chatListBean) throws Exception {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideProgressDialog();
                        Log.i(TAG, String.format("refreshChatList:{%s}", new Gson().toJson(chatListBean)));
                        showChatList(chatListBean.getListDataInfo((String) SPManager.getInstance(getContext()).get(SPManager.SP_KEY_USER_ID, ""), true));
                    }
                });
            }
        }, new RxThrowableConsumer() {
            @Override
            public void handleThrowable(Throwable throwable) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideProgressDialog();
//                        showChatList(ImUtil.getInstance().getChatList());
                    }
                });
            }

            @Override
            public void handleConnectException() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideProgressDialog();
//                        showChatList(ImUtil.getInstance().getChatList());
                    }
                });
            }
        });

    }

    /**
     * 展示列表
     *
     * @return
     */
    public void showChatList(List<ConversationListDataInfo> chatList) {
        List<Object> chatListData = new ArrayList<>();
        for (ConversationListDataInfo daoChatListInfo : chatList) {
            chatListData.add(new ChatList.Builder(daoChatListInfo).build());
            //每个群都得加监听
            ImUtil.getInstance().joinMultiUserChat(LoginUser.getInstance().getUserName(), daoChatListInfo.getTopicId());
        }
        if (chatListData.size() > 0) {
            mData.clear();
            mData.addAll(chatListData);
            mAdapter.notifyDataSetChanged();
            xLoadView.visibleSuccessView();
        } else {
            mData.clear();
            mAdapter.notifyDataSetChanged();
            xLoadView.visibleEmptyView();
        }
    }

    /**
     * 聊天自定义布局 重写此方法
     */
    @Override
    public void registerChatLayoutAndData(SlimAdapter slimAdapter) {
        slimAdapter.register(getChatListItemLayoutId(), new SlimInjector<ChatList>() {
            @Override
            public void onInject(final ChatList chatList, IViewInjector iViewInjector) {
                injectChatListItemDataSource(chatList, iViewInjector);
            }
        });
    }

    /**
     * 进入聊天内容页面
     * 保存数据
     */
    public void enterChatContentActivity(ChatList chatList) {
//        SPManager.getInstance(getContext()).set(SPManager.SP_LAST_CHAT_INFO, chatList);
//        IM.sImUtils.openSingleChatActivity(getActivity(), chatList.getUserId(), chatList.getUserName());
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void newMessageEvent(MessageEvent messageEvent) {
//        if (messageEvent.eventType == MessageEvent.ME_UPDATE_CONVERSATION_LIST) {
//            refreshChatList(false);
//        } else if (messageEvent.eventType == MessageEvent.ME_NOTIFICATION_STATUS_UPDATE) {
//            String network = (String) messageEvent.values[0];
//            Log.e("network", network);
//        }
//    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleDefaultEvent(GroupChatMessageEvent chatMessageEvent) {
        refreshChatList(false);
    }
}
