package com.yryc.imkit.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yryc.imkit.R;
import com.yryc.imkit.base.BaseFragment;
import com.yryc.imkit.constant.Align;
import com.yryc.imkit.constant.Config;
import com.yryc.imkit.constant.IntentKey;
import com.yryc.imkit.constant.MessageType;
import com.yryc.imkit.constant.Orientation;
import com.yryc.imkit.core.media.MediaManager;
import com.yryc.imkit.core.slim.SlimAdapter;
import com.yryc.imkit.core.slim.SlimInjector;
import com.yryc.imkit.core.slim.viewinjector.IViewInjector;
import com.yryc.imkit.im.IM;
import com.yryc.imkit.im.ImKeyBoard;
import com.yryc.imkit.impl.IFChatContentAction;
import com.yryc.imkit.widget.emoji.widget.EmojiTextview;
import com.yryc.imlib.model.chat.ChatBody;
import com.yryc.imlib.model.chat.ChatCar;
import com.yryc.imlib.model.chat.ChatImage;
import com.yryc.imlib.model.chat.ChatLocation;
import com.yryc.imlib.model.chat.ChatOrder;
import com.yryc.imlib.model.chat.ChatRich;
import com.yryc.imlib.model.chat.ChatRichLeft;
import com.yryc.imlib.model.chat.ChatRichNone;
import com.yryc.imlib.model.chat.ChatRichRight;
import com.yryc.imlib.model.chat.ChatRichTop;
import com.yryc.imlib.model.chat.ChatSystem;
import com.yryc.imlib.model.chat.ChatText;
import com.yryc.imlib.model.chat.ChatTime;
import com.yryc.imlib.model.chat.ChatVideo;
import com.yryc.imlib.model.chat.ChatVoice;
import com.yryc.imlib.model.net.MessageHistoryBean;
import com.yryc.imlib.model.net.TopicBean;
import com.yryc.imlib.retrofit.ApiService;
import com.yryc.imlib.retrofit.RetrofitServiceCreator;
import com.yryc.imkit.ui.activity.OneVideoPlayActivity;
import com.yryc.imkit.ui.view.FButtonView;
import com.yryc.imkit.ui.view.FLineView;
import com.yryc.imkit.ui.view.FTextView;
import com.yryc.imkit.ui.view.ImExtension;
import com.yryc.imkit.utils.CommonUtils;
import com.yryc.imkit.utils.NavigationUtil;
import com.yryc.imkit.utils.PixelUtils;
import com.yryc.imkit.utils.PopMenuUtils;
import com.yryc.imkit.utils.ScreenUtils;
import com.yryc.imkit.utils.TimeUtils;
import com.yryc.imkit.widget.gallery.model.GalleryPhotoModel;
import com.yryc.imkit.widget.gallery.view.GalleryView;
import com.yryc.imlib.client.MessageEvent;
import com.yryc.imlib.info.GeneralMessageDataInfo;
import com.yryc.imlib.info.ReceiptTypeEnum;
import com.yryc.imlib.info.event.ChatMessageEvent;
import com.yryc.imlib.info.io.HasReadDataInfo;
import com.yryc.imlib.info.io.MessageDataInfoBean;
import com.yryc.imlib.rx.RxEvent.EventType;
import com.yryc.imlib.rx.RxThrowableConsumer;
import com.yryc.imlib.rx.RxUtils;
import com.yryc.imlib.utils.SPManager;
import com.yryc.imlib.xmpp.ChatListener;
import com.yryc.imlib.xmpp.ImUtil;
import com.yryc.imlib.xmpp.LoginUser;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jivesoftware.smack.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import io.reactivex.functions.Consumer;


/**
 * @author : Mai_Xiao_Peng
 * @email : Mai_Xiao_Peng@163.com
 * @time : 2018/11/30 11:23
 * @describe :
 */

public abstract class ChatContentFragment extends BaseFragment implements IFChatContentAction {

    /**
     * 消息列表
     */
    private List<Object> mData = new ArrayList<>();
    private List<AnimationDrawable> mAnimationDrawables = new ArrayList<>();
    private GeneralMessageDataInfo mLastChatMessageData;
    private RecyclerView rvChatList;
    private SlimAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private LoginUser mLoginUser;
    private ImExtension imExtension;
    private ClassicsHeader smartRefreshHeader;
    private SmartRefreshLayout smartRefresh;

    //    辅助参数：是否在下载中，如果是下载中不能进行下一次下载
    private boolean isLoading = false;
    private boolean isHasMore = false;
    private int defaultPageSize = 10;

    private ImUtil imUtil;
    private ImKeyBoard imKeyBoard;

    private int topPostion = -1;
    private int bottomPostion = -1;
    private GalleryView gvGallery;
    private ApiService apiService;

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_chat_content;
    }

    @Override
    public void initView() {
        mLoginUser = LoginUser.getInstance();
        imUtil = ImUtil.getInstance();
        imUtil.setContext(getActivity());
        gvGallery = mView.findViewById(R.id.gv_gallery);
        rvChatList = mView.findViewById(R.id.rv_chat_list);
        imExtension = mView.findViewById(R.id.im_extension);
        smartRefresh = mView.findViewById(R.id.smart_refresh);
        smartRefreshHeader = mView.findViewById(R.id.smart_refresh_header);
        smartRefresh.setEnableLoadmore(false);
        smartRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                loadMore();
            }
        });
        if (enableImChatExtension()) {
            imExtension.setVisibility(View.VISIBLE);
        } else {
            imExtension.setVisibility(View.GONE);
        }
        imExtension.setAudioUploadListener(new ImExtension.AudioUploadListener() {
            @Override
            public void onUploadStart() {
                showProgressDialog(getResources().getString(R.string.loaded_wait_send));
            }

            @Override
            public void onUploadSuccess() {
                hideProgressDialog();
            }

            @Override
            public void onUploadError() {
                hideProgressDialog();
            }
        });

        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvChatList.setLayoutManager(mLayoutManager);
        rvChatList.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
//                if (newState == RecyclerView.SCROLL_STATE_IDLE && mLayoutManager.findFirstCompletelyVisibleItemPosition() == 0) {
//                    loadMore();
//                }
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    searchVisibilityListHaveChatOrder();
                }
            }

        });
        rvChatList.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (bottom < oldBottom) {
//                    SPManager.getInstance(getActivity()).set(SPManager.SP_KEYBOARD_HEIGHT, imKeyBoard.getSoftKeyboardHeight());
                }
            }
        });
        rvChatList.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            Rect rect = new Rect();
            int windowVisibleBottomWithoutKeyboard;

            @Override
            public void onGlobalLayout() {
                rect.setEmpty();
                rvChatList.getWindowVisibleDisplayFrame(rect);
                int detal = windowVisibleBottomWithoutKeyboard - rect.bottom;
                if (detal > ScreenUtils.getAvailableScreenHeight(getActivity()) / 3) {
                    SPManager.getInstance(getActivity()).set(SPManager.SP_KEYBOARD_HEIGHT, detal);
                } else {
                    windowVisibleBottomWithoutKeyboard = rect.bottom;
                }
            }
        });
        //加载更多
        mAdapter = SlimAdapter.create().enableDiff()
                .attachTo(rvChatList).updateData(mData);

        registerChatLayoutAndData(mAdapter);
        imKeyBoard = new ImKeyBoard(getActivity(), rvChatList, imExtension);
        imExtension.setImKeyBoard(imKeyBoard);
    }

    /**
     * 搜索可见列表是否包含订单信息
     */
    private void searchVisibilityListHaveChatOrder() {
        if (IM.sImUtils.imOptions.onImChatItemScrollListener == null) {
            return;
        }
        if (mData.size() <= 0) {
            IM.sImUtils.imOptions.onImChatItemScrollListener.onChatOrderVisibilityState(null);
            return;
        }
        if (bottomPostion == -1) {
            bottomPostion = mLayoutManager.findLastVisibleItemPosition();
        }
        if (topPostion == -1 || topPostion > mLayoutManager.findFirstVisibleItemPosition()) {
            if (topPostion == -1) {
                topPostion = mLayoutManager.findFirstVisibleItemPosition();
                notifyHaveReadChange(topPostion, bottomPostion);
            } else {
                notifyHaveReadChange(mLayoutManager.findFirstVisibleItemPosition(), topPostion);
                topPostion = mLayoutManager.findFirstVisibleItemPosition();
            }
        }
        Log.e("ssh", topPostion + "      " + bottomPostion);

        int firstVisibleItemPosition = mLayoutManager.findFirstVisibleItemPosition();
        int lastVisibleItemPosition = mLayoutManager.findLastVisibleItemPosition();
        ChatOrder chatOrder = null;
        for (int i = lastVisibleItemPosition; i >= firstVisibleItemPosition && i > -1; i--) {
            if (mData.get(i) instanceof ChatOrder) {
                chatOrder = (ChatOrder) mData.get(i);
                break;
            }
        }
        IM.sImUtils.imOptions.onImChatItemScrollListener.onChatOrderVisibilityState(chatOrder);
    }

    @Override
    public void initData() {
        initIM(LoginUser.getInstance().getToUser());
    }


    private void initIM(String toUser) {
        apiService = RetrofitServiceCreator.createService(getContext(), ApiService.class);
        HashMap<String, String> data = new HashMap<>();
        data.put("fromUserId", LoginUser.getInstance().getUserName());
        data.put("toUserId", LoginUser.getInstance().getToUser());
        data.put("topicType", "1");
        showProgressDialog(getResources().getString(R.string.loaded_wait_moment));
        apiService.getTopic(data).compose(RxUtils.rxSchedulerHelper())
                .compose(RxUtils.handleResult()).subscribe(new Consumer<TopicBean>() {
            @Override
            public void accept(TopicBean topicBean) throws Exception {
                mLoginUser.setTopicId(topicBean.getTopicId());
                LoginUser.getInstance().setTopicId(topicBean.getTopicId());
                imUtil.starChat(toUser, new ChatListener() {
                    @Override
                    public void onSendMessage(MessageDataInfoBean messageDataInfoBean) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                        chatAdapter.add(messageDataInfoBean);
//                        scrollToEnd();
                            }
                        });
                    }
                });
                refreshChatList();
            }
        }, new RxThrowableConsumer() {
            @Override
            public void handleThrowable(Throwable throwable) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "开启会话失败", Toast.LENGTH_SHORT).show();
                        hideProgressDialog();
                        getActivity().finish();
                    }
                });
            }

            @Override
            public void handleConnectException() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "开启会话失败", Toast.LENGTH_SHORT).show();
                        hideProgressDialog();
                        getActivity().finish();
                    }
                });
            }
        });

    }

    /**
     * 加载聊天列表内容
     */
    public void refreshChatList() {
        getChatListData(0, false);
    }

    /**
     * 加载更多
     */
    private void loadMore() {
        if (isLoading || mLastChatMessageData == null || mLastChatMessageData.getSortNum() == 0 || !isHasMore) {
            smartRefresh.finishRefresh();
            return;
        }
        getChatListData(mLastChatMessageData.getSortNum(), true);

    }

    /**
     * 获取数据源
     *
     * @return
     */
    public void getChatListData(long sortNum, boolean isShowMore) {
//        OIMClient.getInstance().getChatRecondForTargetId(mLoginUser.getToUser(), 0, new ChatDataCallback() {
//            @Override
//            public void onSuccess(List<GeneralMessageDataInfo> var) {
//                Log.i(TAG, String.format("getChatListData:{%s}", new Gson().toJson(var)));
//        if (var.size() < defaultPageSize) {
//                    isHasMore = false;
//                } else {
//                    isHasMore = true;
//                }
//                if (var.size() > 0) {
//                    mLastChatMessageData = var.get(var.size() - 1);
//                }
//                mData.clear();
//                mData.addAll(adjustData(var));
//                mAdapter.notifyDataSetChanged();
//                rvChatList.scrollToPosition(mAdapter.getItemCount() - 1);
//                rvChatList.postDelayed(() -> searchVisibilityListHaveChatOrder(), 100);
//            }
//
//            @Override
//            public void onError(Enums.ErrorCode error) {
//
//            }
//        });
        isLoading = true;
        HashMap<String, Object> data = new HashMap<>();
        data.put("pageSize", defaultPageSize);
        data.put("sortNum", sortNum);
        data.put("topicId", LoginUser.getInstance().getTopicId());
        apiService.getChatLogPage(data).compose(RxUtils.rxSchedulerHelper())
                .compose(RxUtils.handleResult()).subscribe(new Consumer<MessageHistoryBean>() {
            @Override
            public void accept(MessageHistoryBean messageHistoryBean) throws Exception {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideProgressDialog();
                        smartRefresh.finishRefresh();
                        smartRefreshHeader.setLastUpdateTime(new Date());
                        showHistoryChatList(messageHistoryBean.getList(), isShowMore, false);
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
                        smartRefresh.finishRefresh();
                        showHistoryChatList(null, isShowMore, true);
                    }
                });
            }

            @Override
            public void handleConnectException() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideProgressDialog();
                        smartRefresh.finishRefresh();
                        showHistoryChatList(null, isShowMore, true);
                    }
                });
            }
        });
    }

    //    展示历史消息
    private void showHistoryChatList(List<MessageDataInfoBean> historys, boolean isShowMore, boolean isLoadError) {
        isLoading = false;
        if (isLoadError) {
            historys = imUtil.getChatMessageList(LoginUser.getInstance().getToUser(), isShowMore && mLastChatMessageData != null ? mLastChatMessageData.getSortNum() : 0, defaultPageSize);
        }

        if (historys.size() < defaultPageSize) {
            isHasMore = false;
        } else {
            isHasMore = true;
        }
        if (historys != null && historys.size() > 0) {
            //未读消息全部置为已读
            //List<GeneralMessageDataInfo> upDateList = new ArrayList<>();
            List<GeneralMessageDataInfo> newDateList = new ArrayList<>();
            for (MessageDataInfoBean messageDataInfoBean : historys) {
                if (ReceiptTypeEnum.RECEIVED.getType().equals(messageDataInfoBean.getReceiptType())) {
                    sendReceiptFilter(messageDataInfoBean);
                    // upDateList.add(messageDataInfoBean.changeToDaoMessageDataInfo());
                }
                newDateList.add(messageDataInfoBean.changeToDaoMessageDataInfo());
            }
            //更新数据库数据
            imUtil.replaceWithMessage(newDateList);
            if (newDateList != null && newDateList.size() > 0) {
                mLastChatMessageData = newDateList.get(0);
                List<Object> objects = adjustData(newDateList);

                if (isShowMore) {
                    mData.addAll(0, objects);
                    topPostion = topPostion + objects.size();
                    mAdapter.notifyItemRangeInserted(0, objects.size());
                } else {
                    mData.clear();
                    mData.addAll(objects);
                    scrollToEnd();
                }
            }
        }
    }

    /**
     * 调整数据源
     *
     * @param list
     * @return
     */
    private List<Object> adjustData(List<GeneralMessageDataInfo> list) {
//        Collections.reverse(list);
        List<Object> chatList = new ArrayList<>();
        long interval = 20 * 60 * 1000;
        for (int i = 0; i < list.size(); i++) {
            GeneralMessageDataInfo generalMessageDataInfo = list.get(i);
            if (i == 0) {
                chatList.add(new ChatTime.Builder(generalMessageDataInfo).build());
            } else if (generalMessageDataInfo.getSentTime() - list.get(i - 1).getSentTime() >= interval) {
                chatList.add(new ChatTime.Builder(generalMessageDataInfo).build());
            }
            adjust(chatList, generalMessageDataInfo);
        }
        return chatList;
    }


    /**
     * 调整数据源
     *
     * @param message
     * @return-
     */
    private List<Object> adjustNewData(GeneralMessageDataInfo message) {
        List<Object> chatList = new ArrayList<>();
        long interval = 20 * 60 * 1000;
        if (mData.size() > 0) {
            ChatBody chatBody = null;
            if (mData.get(mData.size() - 1) instanceof ChatRichNone) {
                ChatRichNone chatRichNone = (ChatRichNone) mData.get(mData.size() - 1);
                chatBody = chatRichNone.getChatRich();
            } else if (mData.get(mData.size() - 1) instanceof ChatRichLeft) {
                ChatRichLeft chatRichLeft = (ChatRichLeft) mData.get(mData.size() - 1);
                chatBody = chatRichLeft.getChatRich();
            } else if (mData.get(mData.size() - 1) instanceof ChatRichRight) {
                ChatRichRight chatRichRight = (ChatRichRight) mData.get(mData.size() - 1);
                chatBody = chatRichRight.getChatRich();
            } else if (mData.get(mData.size() - 1) instanceof ChatRichTop) {
                ChatRichTop chatRichTop = (ChatRichTop) mData.get(mData.size() - 1);
                chatBody = chatRichTop.getChatRich();
            } else {
                chatBody = (ChatBody) mData.get(mData.size() - 1);
            }
            if (message.getSentTime() - chatBody.getSentTime() >= interval) {
                chatList.add(new ChatTime.Builder(message).build());
            }
        } else {
            chatList.add(new ChatTime.Builder(message).build());
        }
        adjust(chatList, message);
        return chatList;
    }

    /**
     * 调整数据源
     *
     * @param chatList
     * @param generalMessageDataInfo
     */
    private List<Object> adjust(List<Object> chatList, GeneralMessageDataInfo generalMessageDataInfo) {
        if (MessageType.TEXT.getType().equals(generalMessageDataInfo.getBodyType())) {
            chatList.add(new ChatText.Builder(generalMessageDataInfo).build());
        } else if (MessageType.IMAGE.getType().equals(generalMessageDataInfo.getBodyType())) {
            chatList.add(new ChatImage.Builder(generalMessageDataInfo).build());
        } else if (MessageType.LOCATION.getType().equals(generalMessageDataInfo.getBodyType())) {
            chatList.add(new ChatLocation.Builder(generalMessageDataInfo).build());
        } else if (MessageType.VOICE.getType().equals(generalMessageDataInfo.getBodyType())) {
            chatList.add(new ChatVoice.Builder(generalMessageDataInfo).build());
        } else if (MessageType.VIDEO.getType().equals(generalMessageDataInfo.getBodyType())) {
            chatList.add(new ChatVideo.Builder(generalMessageDataInfo).build());
        } else if (MessageType.SYSTEM.getType().equals(generalMessageDataInfo.getBodyType())) {
            chatList.add(new ChatSystem.Builder(generalMessageDataInfo).build());
        } else if (MessageType.ORDER.getType().equals(generalMessageDataInfo.getBodyType())) {
            chatList.add(new ChatOrder.Builder(generalMessageDataInfo).build());
        } else if (MessageType.CAR.getType().equals(generalMessageDataInfo.getBodyType())) {
            chatList.add(new ChatCar.Builder(generalMessageDataInfo).build());
        } else if (MessageType.RICH.getType().equals(generalMessageDataInfo.getBodyType())) {
            ChatRich chatRich = new ChatRich.Builder(generalMessageDataInfo).build();
            ChatRich.Body.Image image = chatRich.getBody().getImage();
            if (image == null) {
                chatList.add(new ChatRichNone().setChatRich(chatRich));
            } else {
                if (image.getImagePosition() == Align.LEFT.getCode()) {
                    chatList.add(new ChatRichLeft().setChatRich(chatRich));
                } else if (image.getImagePosition() == Align.RIGHT.getCode()) {
                    chatList.add(new ChatRichRight().setChatRich(chatRich));
                } else if (image.getImagePosition() == Align.TOP.getCode()) {
                    chatList.add(new ChatRichTop().setChatRich(chatRich));
                }
            }
        } else {
            addCustomDataSource(chatList, generalMessageDataInfo);
        }
        return chatList;
    }

    /**
     * 聊天自定义布局 重写此方法
     */
    public void registerChatLayoutAndData(SlimAdapter slimAdapter) {
        slimAdapter.register(R.layout.item_chat_content_text, new SlimInjector<ChatText>() {
            @Override
            public void onInject(ChatText chatText, IViewInjector iViewInjector) {
                handleTextDataInject(chatText, iViewInjector);
            }
        }).register(R.layout.item_chat_content_image, new SlimInjector<ChatImage>() {
            @Override
            public void onInject(ChatImage chatImage, IViewInjector iViewInjector) {
                handleImageDataInject(chatImage, iViewInjector);
            }
        }).register(R.layout.item_chat_content_location, new SlimInjector<ChatLocation>() {
            @Override
            public void onInject(ChatLocation chatLocation, IViewInjector iViewInjector) {
                handleChatLocationInject(chatLocation, iViewInjector);
            }
        }).register(R.layout.item_chat_content_voice, new SlimInjector<ChatVoice>() {
            @Override
            public void onInject(ChatVoice chatVoice, IViewInjector iViewInjector) {
                handleVoiceDataInject(chatVoice, iViewInjector);
            }
        }).register(R.layout.item_chat_content_video, new SlimInjector<ChatVideo>() {
            @Override
            public void onInject(ChatVideo chatVideo, IViewInjector iViewInjector) {
                handleVideoDataInject(chatVideo, iViewInjector);
            }
        }).register(R.layout.item_chat_ts, new SlimInjector<ChatTime>() {
            @Override
            public void onInject(ChatTime chatTime, IViewInjector iViewInjector) {
                iViewInjector.text(R.id.tv_desc, TimeUtils.getNewChatTime(chatTime.getSentTime()));
            }
        }).register(R.layout.item_chat_ts, new SlimInjector<ChatSystem>() {
            @Override
            public void onInject(ChatSystem chatSystem, IViewInjector iViewInjector) {
                iViewInjector.text(R.id.tv_desc, chatSystem.getBody().getContent());
            }
        }).register(R.layout.item_chat_content_order, new SlimInjector<ChatOrder>() {
            @Override
            public void onInject(ChatOrder chatOrder, IViewInjector iViewInjector) {
                handleOrderDataInject(chatOrder, iViewInjector);
            }
        }).register(R.layout.item_chat_content_car, new SlimInjector<ChatCar>() {
            @Override
            public void onInject(ChatCar chatCar, IViewInjector iViewInjector) {
                handleCarDataInject(chatCar, iViewInjector);
            }
        }).register(R.layout.item_chat_content_rich, new SlimInjector<ChatRichNone>() {
            @Override
            public void onInject(ChatRichNone chatRichNone, IViewInjector iViewInjector) {
                handleChatRichNoneInject(chatRichNone, iViewInjector);
            }
        }).register(R.layout.item_chat_content_rich, new SlimInjector<ChatRichLeft>() {
            @Override
            public void onInject(ChatRichLeft chatRichLeft, IViewInjector iViewInjector) {
                handleChatRichLeftInject(chatRichLeft, iViewInjector);
            }
        }).register(R.layout.item_chat_content_rich, new SlimInjector<ChatRichRight>() {
            @Override
            public void onInject(ChatRichRight chatRichRight, IViewInjector iViewInjector) {
                handleChatRichRightInject(chatRichRight, iViewInjector);
            }
        }).register(R.layout.item_chat_content_rich, new SlimInjector<ChatRichTop>() {
            @Override
            public void onInject(ChatRichTop chatRichTop, IViewInjector iViewInjector) {
                handleChatRichTopInject(chatRichTop, iViewInjector);
            }
        });
        registerCustomChatLayout(slimAdapter);
    }

    /**
     * 地图数据注入
     *
     * @param chatLocation
     * @param iViewInjector
     */
    private void handleChatLocationInject(ChatLocation chatLocation, IViewInjector iViewInjector) {
        ImageView ivAvatar = (ImageView) iViewInjector.findViewById(R.id.iv_avatar);
        ivAvatar.setOnClickListener(v -> {
            if (IM.sImUtils.imOptions.onImChatAvatarClickListener != null) {
                IM.sImUtils.imOptions.onImChatAvatarClickListener.onImChatAvatarClick(v, chatLocation.getFromUid());
            }
        });
        iViewInjector.text(R.id.tv_poi_name, chatLocation.getBody().getContent())
                .text(R.id.tv_address, chatLocation.getBody().getAddress());
        ImageView ivBody = (ImageView) iViewInjector.findViewById(R.id.iv_image);
        LinearLayout llLocation = (LinearLayout) iViewInjector.findViewById(R.id.ll_location);
        TextView tvHasRead = (TextView) iViewInjector.findViewById(R.id.tv_has_read);
        adjustChatTargetLayout(chatLocation, tvHasRead, ivAvatar, llLocation);
        Glide.with(getContext()).load(chatLocation.getAvatarUrl())
                .apply(new RequestOptions().placeholder(R.drawable.ic_im_avatar_default).error(R.drawable.ic_im_avatar_default)).into(ivAvatar);
        String mapUrl = String.format(Config.GDMapURL,
                String.valueOf(chatLocation.getBody().getLocation().getLongitude()),
                String.valueOf(chatLocation.getBody().getLocation().getLatitude()),
                String.valueOf(chatLocation.getBody().getLocation().getLongitude()),
                String.valueOf(chatLocation.getBody().getLocation().getLatitude()));
        Glide.with(getContext()).load(mapUrl).into(ivBody);
        iViewInjector.clicked(R.id.ll_location, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double[] location = {chatLocation.getBody().getLocation().getLatitude(), chatLocation.getBody().getLocation().getLongitude()};
                NavigationUtil.mapLocation(getActivity(), location, chatLocation.getBody().getContent());
            }
        });
    }

    private void generateButtonRichItem(View view, int containerId, ChatRich body) {
        if (body.getBody().getResult() != null) {
            LinearLayout llButtonRoot = view.findViewById(containerId);
            LinearLayout llButton = llButtonRoot.findViewById(R.id.ll_button);
            llButton.removeAllViews();
            FButtonView fButtonView = new FButtonView(getContext());
            fButtonView.init(body, body.getBody().getResult());
            fButtonView.showButtonResultUI();
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            layoutParams.weight = 1;
            fButtonView.setLayoutParams(layoutParams);
            llButton.addView(fButtonView);
        } else {
            generateButtonRich(view, containerId, body.getBody().getButtons(), body);
        }
    }

    boolean isTheFrontDesk = true;

    @Override
    public void onResume() {
        if (!isTheFrontDesk) {
            int cacheLast = bottomPostion;
            bottomPostion = mData.size() - 1;
            notifyHaveReadChange(cacheLast, bottomPostion);
        }
        isTheFrontDesk = true;
        super.onResume();
    }

    @Override
    public void onPause() {
        isTheFrontDesk = false;
        super.onPause();
    }

    /**
     * 动态生成富文本按钮
     *
     * @param view
     * @param containerId
     * @param buttons
     * @param body
     */
    private void generateButtonRich(View view, int containerId, List<ChatRich.Body.Button> buttons, ChatRich body) {
        LinearLayout llButtonRoot = view.findViewById(containerId);
        LinearLayout llButton = llButtonRoot.findViewById(R.id.ll_button);
        llButton.removeAllViews();
        if (buttons != null && buttons.size() > 0) {
            llButtonRoot.setVisibility(View.VISIBLE);
            //控制父布局高度
            if (body.getBody().getButtonOrientation() == Orientation.HORIZONTAL.getCode()) {
                llButton.setOrientation(LinearLayout.HORIZONTAL);
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) llButton.getLayoutParams();
                layoutParams.height = PixelUtils.dip2px(getContext(), 45);
            } else {
                llButton.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) llButton.getLayoutParams();
                layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            }
            for (int i = 0; i < buttons.size(); i++) {
                ChatRich.Body.Button button = buttons.get(i);
                FButtonView fButtonView = new FButtonView(getContext());
                fButtonView.init(body, button);
                fButtonView.showButtonUI();
                fButtonView.setOnClickListener(v -> {
                    if (IM.sImUtils.imOptions.onImChatRichItemClickListener != null) {
                        IM.sImUtils.imOptions.onImChatRichItemClickListener.onRichItemClick(fButtonView, button);
                    }
                });
                if (body.getBody().getButtonOrientation() == Orientation.HORIZONTAL.getCode()) {
                    //水平布局按钮动态排列方式
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                    layoutParams.weight = 1;
                    fButtonView.setLayoutParams(layoutParams);
                    llButton.addView(fButtonView);
                    if (i != buttons.size() - 1) {
                        FLineView fLineView = new FLineView(getContext());
                        llButton.addView(fLineView);
                        LinearLayout.LayoutParams fLineViewLayoutParams = (LinearLayout.LayoutParams) fLineView.getLayoutParams();
                        fLineViewLayoutParams.width = PixelUtils.dip2px(getContext(), 1);
                        fLineViewLayoutParams.height = PixelUtils.dip2px(getContext(), 35);
                        fLineViewLayoutParams.gravity = Gravity.CENTER_VERTICAL;
                    }
                } else {
                    //垂直布局按钮动态排列方式
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.setMargins(0, PixelUtils.dip2px(getContext(), 6), 0, PixelUtils.dip2px(getContext(), 9));
                    fButtonView.setLayoutParams(layoutParams);
                    fButtonView.setMaxLines(2);
                    fButtonView.setEllipsize(TextUtils.TruncateAt.END);
                    llButton.addView(fButtonView);
                    if (i != buttons.size() - 1) {
                        FLineView fLineView = new FLineView(getContext());
                        llButton.addView(fLineView);
                        LinearLayout.LayoutParams fLineViewLayoutParams = (LinearLayout.LayoutParams) fLineView.getLayoutParams();
                        fLineViewLayoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
                        fLineViewLayoutParams.height = 1;
                    }
                }

            }
        } else {
            llButtonRoot.setVisibility(View.GONE);
        }
    }

    /**
     * 生成富文本item
     *
     * @param view
     * @param containerId
     * @param contentList
     */
    private void generateContentRichItem(View view, int containerId, List<ChatRich.Body.Content> contentList) {
        ViewGroup containerView = view.findViewById(containerId);
        containerView.removeAllViews();
        if (contentList != null && contentList.size() > 0) {
            containerView.setVisibility(View.VISIBLE);
            for (ChatRich.Body.Content content : contentList) {
                containerView.addView(generateRichTextView(content));
            }
        } else {
            containerView.setVisibility(View.GONE);
        }
    }

    /**
     * 生成富文本
     *
     * @param content
     * @return
     */
    private TextView generateRichTextView(ChatRich.Body.Content content) {
        FTextView fTextView = new FTextView(getContext());
        fTextView.init(content);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        fTextView.setLayoutParams(layoutParams);
        return fTextView;
    }

    /**
     * 设置范围间距
     *
     * @param body
     */
    private void resetRichRegionMargin(LinearLayout header, LinearLayout body, LinearLayout footer, LinearLayout button) {
        LinearLayout llButtonList = button.findViewById(R.id.ll_button);
        if (header.getChildCount() > 0) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) body.getLayoutParams();
            layoutParams.setMargins(0, PixelUtils.dip2px(getContext(), 15), 0, 0);
            body.setLayoutParams(layoutParams);
        } else {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) body.getLayoutParams();
            layoutParams.setMargins(0, PixelUtils.dip2px(getContext(), 0), 0, 0);
            body.setLayoutParams(layoutParams);
        }
        if (header.getChildCount() + body.getChildCount() > 0) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) footer.getLayoutParams();
            layoutParams.setMargins(0, PixelUtils.dip2px(getContext(), 15), 0, 0);
            footer.setLayoutParams(layoutParams);
        } else {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) footer.getLayoutParams();
            layoutParams.setMargins(0, PixelUtils.dip2px(getContext(), 0), 0, 0);
            footer.setLayoutParams(layoutParams);
        }
        if (header.getChildCount() + body.getChildCount() + footer.getChildCount() > 0 && llButtonList.getChildCount() > 0) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) button.getLayoutParams();
            layoutParams.setMargins(0, PixelUtils.dip2px(getContext(), 15), 0, 0);
            button.setLayoutParams(layoutParams);
            LinearLayout parent = (LinearLayout) button.getParent();
            parent.setPadding(PixelUtils.dp2px(getContext(), 10), PixelUtils.dp2px(getContext(), 15)
                    , PixelUtils.dp2px(getContext(), 10), PixelUtils.dp2px(getContext(), 0));
        } else {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) button.getLayoutParams();
            layoutParams.setMargins(0, PixelUtils.dip2px(getContext(), 0), 0, 0);
            button.setLayoutParams(layoutParams);
            LinearLayout parent = (LinearLayout) button.getParent();
            parent.setPadding(PixelUtils.dp2px(getContext(), 10), PixelUtils.dp2px(getContext(), 15)
                    , PixelUtils.dp2px(getContext(), 10), PixelUtils.dp2px(getContext(), 15));
        }
    }

    private void handleChatRichNoneInject(ChatRichNone chatRichNone, IViewInjector iViewInjector) {
        ImageView ivAvatar = (ImageView) iViewInjector.findViewById(R.id.iv_avatar);
        ivAvatar.setOnClickListener(v -> {
            if (IM.sImUtils.imOptions.onImChatAvatarClickListener != null) {
                IM.sImUtils.imOptions.onImChatAvatarClickListener.onImChatAvatarClick(v, chatRichNone.getChatRich().getFromUid());
            }
        });
        RelativeLayout rlRich = (RelativeLayout) iViewInjector.findViewById(R.id.rl_rich);
        TextView tv_has_read = (TextView) iViewInjector.findViewById(R.id.tv_has_read);
        adjustChatTargetLayout(chatRichNone.getChatRich(), tv_has_read, ivAvatar, rlRich);
        Glide.with(getContext()).load(chatRichNone.getChatRich().getAvatarUrl())
                .apply(new RequestOptions().placeholder(R.drawable.ic_im_avatar_default).error(R.drawable.ic_im_avatar_default)).into(ivAvatar);
        View richNone = View.inflate(getContext(), R.layout.include_item_chat_content_rich_none, null);
        rlRich.removeAllViews();
        rlRich.addView(richNone);
        ChatRich.Body body = chatRichNone.getChatRich().getBody();
        generateContentRichItem(richNone, R.id.ll_header, body.getHeader());
        generateContentRichItem(richNone, R.id.ll_body, body.getBody());
        generateContentRichItem(richNone, R.id.ll_footer, body.getFooter());
        generateButtonRichItem(richNone, R.id.ll_button_root, chatRichNone.getChatRich());
        resetRichRegionMargin(richNone.findViewById(R.id.ll_header),
                richNone.findViewById(R.id.ll_body),
                richNone.findViewById(R.id.ll_footer),
                richNone.findViewById(R.id.ll_button_root));
        richNone.findViewById(R.id.ll_root).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (IM.sImUtils.imOptions.onImChatItemClickListener != null) {
                    IM.sImUtils.imOptions.onImChatItemClickListener.onChatRichBodyClick(chatRichNone.getChatRich().getBody());
                }
            }
        });
    }


    private void handleChatRichLeftInject(ChatRichLeft chatRichLeft, IViewInjector iViewInjector) {
        ImageView ivAvatar = (ImageView) iViewInjector.findViewById(R.id.iv_avatar);
        ivAvatar.setOnClickListener(v -> {
            if (IM.sImUtils.imOptions.onImChatAvatarClickListener != null) {
                IM.sImUtils.imOptions.onImChatAvatarClickListener.onImChatAvatarClick(v, chatRichLeft.getChatRich().getFromUid());
            }
        });
        RelativeLayout rlRich = (RelativeLayout) iViewInjector.findViewById(R.id.rl_rich);
        TextView tv_has_read = (TextView) iViewInjector.findViewById(R.id.tv_has_read);
        adjustChatTargetLayout(chatRichLeft.getChatRich(), tv_has_read, ivAvatar, rlRich);
        Glide.with(getContext()).load(chatRichLeft.getChatRich().getAvatarUrl())
                .apply(new RequestOptions().placeholder(R.drawable.ic_im_avatar_default).error(R.drawable.ic_im_avatar_default)).into(ivAvatar);
        View richLeft = View.inflate(getContext(), R.layout.include_item_chat_content_rich_left, null);
        ImageView ivCover = richLeft.findViewById(R.id.iv_cover);
        ivCover.getLayoutParams().width = PixelUtils.dip2px(getContext(), Integer.parseInt(chatRichLeft.getChatRich().getBody().getImage().getImageWidth()));
        ivCover.getLayoutParams().height = PixelUtils.dip2px(getContext(), Integer.parseInt(chatRichLeft.getChatRich().getBody().getImage().getImageHeight()));
        Glide.with(getContext())
                .load(chatRichLeft.getChatRich().getBody().getImage().getImageUrl())
                .apply(new RequestOptions()
                        .transforms(new CenterCrop(), new RoundedCorners(10)
                        ).placeholder(R.drawable.ic_im_avatar_default).error(R.drawable.ic_im_avatar_default))
                .into(ivCover);
        rlRich.removeAllViews();
        rlRich.addView(richLeft);
        ChatRich.Body body = chatRichLeft.getChatRich().getBody();
        generateContentRichItem(richLeft, R.id.ll_header, body.getHeader());
        generateContentRichItem(richLeft, R.id.ll_body, body.getBody());
        generateContentRichItem(richLeft, R.id.ll_footer, body.getFooter());
        generateButtonRichItem(richLeft, R.id.ll_button_root, chatRichLeft.getChatRich());
        resetRichRegionMargin((LinearLayout) iViewInjector.findViewById(R.id.ll_header),
                (LinearLayout) iViewInjector.findViewById(R.id.ll_body),
                (LinearLayout) iViewInjector.findViewById(R.id.ll_footer),
                (LinearLayout) iViewInjector.findViewById(R.id.ll_button_root));
        richLeft.findViewById(R.id.ll_root).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (IM.sImUtils.imOptions.onImChatItemClickListener != null) {
                    IM.sImUtils.imOptions.onImChatItemClickListener.onChatRichBodyClick(chatRichLeft.getChatRich().getBody());
                }
            }
        });
    }

    private void handleChatRichRightInject(ChatRichRight chatRichRight, IViewInjector iViewInjector) {
        ImageView ivAvatar = (ImageView) iViewInjector.findViewById(R.id.iv_avatar);
        ivAvatar.setOnClickListener(v -> {
            if (IM.sImUtils.imOptions.onImChatAvatarClickListener != null) {
                IM.sImUtils.imOptions.onImChatAvatarClickListener.onImChatAvatarClick(v, chatRichRight.getChatRich().getFromUid());
            }
        });
        RelativeLayout rlRich = (RelativeLayout) iViewInjector.findViewById(R.id.rl_rich);
        TextView tv_has_read = (TextView) iViewInjector.findViewById(R.id.tv_has_read);
        adjustChatTargetLayout(chatRichRight.getChatRich(), tv_has_read, ivAvatar, rlRich);
        Glide.with(getContext()).load(chatRichRight.getChatRich().getAvatarUrl())
                .apply(new RequestOptions().placeholder(R.drawable.ic_im_avatar_default).error(R.drawable.ic_im_avatar_default)).into(ivAvatar);
        View richRight = View.inflate(getContext(), R.layout.include_item_chat_content_rich_right, null);
        ImageView ivCover = richRight.findViewById(R.id.iv_cover);
        ivCover.getLayoutParams().width = PixelUtils.dip2px(getContext(), Integer.parseInt(chatRichRight.getChatRich().getBody().getImage().getImageWidth()));
        ivCover.getLayoutParams().height = PixelUtils.dip2px(getContext(), Integer.parseInt(chatRichRight.getChatRich().getBody().getImage().getImageHeight()));
        Glide.with(getContext())
                .load(chatRichRight.getChatRich().getBody().getImage().getImageUrl())
                .apply(new RequestOptions()
                        .transforms(new CenterCrop(), new RoundedCorners(10)
                        ).placeholder(R.drawable.ic_im_avatar_default).error(R.drawable.ic_im_avatar_default))
                .into(ivCover);
        rlRich.removeAllViews();
        rlRich.addView(richRight);
        ChatRich.Body body = chatRichRight.getChatRich().getBody();
        generateContentRichItem(richRight, R.id.ll_header, body.getHeader());
        generateContentRichItem(richRight, R.id.ll_body, body.getBody());
        generateContentRichItem(richRight, R.id.ll_footer, body.getFooter());
        generateButtonRichItem(richRight, R.id.ll_button_root, chatRichRight.getChatRich());
        resetRichRegionMargin((LinearLayout) iViewInjector.findViewById(R.id.ll_header),
                (LinearLayout) iViewInjector.findViewById(R.id.ll_body),
                (LinearLayout) iViewInjector.findViewById(R.id.ll_footer),
                (LinearLayout) iViewInjector.findViewById(R.id.ll_button_root));
        richRight.findViewById(R.id.ll_root).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (IM.sImUtils.imOptions.onImChatItemClickListener != null) {
                    IM.sImUtils.imOptions.onImChatItemClickListener.onChatRichBodyClick(chatRichRight.getChatRich().getBody());
                }
            }
        });
    }

    private void handleChatRichTopInject(ChatRichTop chatRichTop, IViewInjector iViewInjector) {
        ImageView ivAvatar = (ImageView) iViewInjector.findViewById(R.id.iv_avatar);
        ivAvatar.setOnClickListener(v -> {
            if (IM.sImUtils.imOptions.onImChatAvatarClickListener != null) {
                IM.sImUtils.imOptions.onImChatAvatarClickListener.onImChatAvatarClick(v, chatRichTop.getChatRich().getFromUid());
            }
        });
        RelativeLayout rlRich = (RelativeLayout) iViewInjector.findViewById(R.id.rl_rich);
        TextView tv_has_read = (TextView) iViewInjector.findViewById(R.id.tv_has_read);
        adjustChatTargetLayout(chatRichTop.getChatRich(), tv_has_read, ivAvatar, rlRich);
        Glide.with(getContext()).load(chatRichTop.getChatRich().getAvatarUrl())
                .apply(new RequestOptions().placeholder(R.drawable.ic_im_avatar_default).error(R.drawable.ic_im_avatar_default)).into(ivAvatar);
        View richTop = View.inflate(getContext(), R.layout.include_item_chat_content_rich_top, null);
        rlRich.removeAllViews();
        rlRich.addView(richTop);
        ChatRich.Body body = chatRichTop.getChatRich().getBody();
        ImageView ivCover = richTop.findViewById(R.id.iv_cover);
        TextView tvTitle = richTop.findViewById(R.id.tv_title);
        Glide.with(getContext())
                .load(chatRichTop.getChatRich().getBody().getImage().getImageUrl())
                .apply(new RequestOptions()
                        .transforms(new CenterCrop(), new RoundedCorners(10)
                        ).placeholder(R.drawable.ic_im_avatar_default).error(R.drawable.ic_im_avatar_default))
                .into(ivCover);
        tvTitle.setText(chatRichTop.getChatRich().getBody().getImage().getImageTitle());
        generateContentRichItem(richTop, R.id.ll_header, body.getHeader());
        generateContentRichItem(richTop, R.id.ll_body, body.getBody());
        generateContentRichItem(richTop, R.id.ll_footer, body.getFooter());
        generateButtonRichItem(richTop, R.id.ll_button_root, chatRichTop.getChatRich());
        richTop.findViewById(R.id.ll_root).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (IM.sImUtils.imOptions.onImChatItemClickListener != null) {
                    IM.sImUtils.imOptions.onImChatItemClickListener.onChatRichBodyClick(chatRichTop.getChatRich().getBody());
                }
            }
        });
    }

    /**
     * 新车二手车车
     *
     * @param chatCar
     * @param iViewInjector
     */
    private void handleCarDataInject(ChatCar chatCar, IViewInjector iViewInjector) {
        ImageView ivAvatar = (ImageView) iViewInjector.findViewById(R.id.iv_avatar);
        ivAvatar.setOnClickListener(v -> {
            if (IM.sImUtils.imOptions.onImChatAvatarClickListener != null) {
                IM.sImUtils.imOptions.onImChatAvatarClickListener.onImChatAvatarClick(v, chatCar.getFromUid());
            }
        });
        RelativeLayout rlCar = (RelativeLayout) iViewInjector.findViewById(R.id.rl_car);
        TextView tv_has_read = (TextView) iViewInjector.findViewById(R.id.tv_has_read);
        adjustChatTargetLayout(chatCar, tv_has_read, ivAvatar, rlCar);
        Glide.with(getContext()).load(chatCar.getAvatarUrl())
                .apply(new RequestOptions().placeholder(R.drawable.ic_im_avatar_default).error(R.drawable.ic_im_avatar_default)).into(ivAvatar);
        Glide.with(getContext()).load(chatCar.getBody().getImg_url())
                .apply(new RequestOptions().placeholder(R.drawable.ic_im_avatar_default).error(R.drawable.ic_im_avatar_default)).into((ImageView) iViewInjector.findViewById(R.id.iv_cover));
        iViewInjector.text(R.id.tv_title, chatCar.getBody().getTitle())
                .text(R.id.tv_content, chatCar.getBody().getContent())
                .clicked(R.id.rl_car, v -> {
                    if (IM.sImUtils.imOptions.onImChatItemClickListener != null) {
                        IM.sImUtils.imOptions.onImChatItemClickListener.onChatCarClick(chatCar);
                    }
                });
    }

    /**
     * 文本数据展示
     *
     * @param chatText
     * @param iViewInjector
     */
    private void handleTextDataInject(ChatText chatText, IViewInjector iViewInjector) {
        ImageView ivAvatar = (ImageView) iViewInjector.findViewById(R.id.iv_avatar);
        ivAvatar.setOnClickListener(v -> {
            if (IM.sImUtils.imOptions.onImChatAvatarClickListener != null) {
                IM.sImUtils.imOptions.onImChatAvatarClickListener.onImChatAvatarClick(v, chatText.getFromUid());
            }
        });
        EmojiTextview tvMessage = (EmojiTextview) iViewInjector.findViewById(R.id.tv_message);
        TextView tvHasRead = (TextView) iViewInjector.findViewById(R.id.tv_has_read);
        adjustChatTargetLayout(chatText, tvHasRead, ivAvatar, tvMessage);
        Glide.with(getContext()).load(chatText.getAvatarUrl())
                .apply(new RequestOptions().placeholder(R.drawable.ic_im_avatar_default).error(R.drawable.ic_im_avatar_default)).into(ivAvatar);
        if (chatText.getFromUid().equals(mLoginUser.getToUser())) {
            tvMessage.setSelected(true);
        } else {
            tvMessage.setSelected(false);
        }
        tvMessage.setText(chatText.getBody().getContent());
        tvMessage.setOnLongClickListener(v -> {
            PopMenuUtils.showPopMenu(PopMenuUtils.TEXT, getActivity(), tvMessage, new PopMenuUtils.OnMenuItemClickListenerImpl() {
                @Override
                public void onCopyClick(MenuItem menuItem) {
                    CommonUtils.copyText2Clipboard(getActivity(), tvMessage.getText().toString());
                }
            });
            return false;
        });
    }

    /**
     * 图片数据展示
     *
     * @param chatImage
     * @param iViewInjector
     */
    private void handleImageDataInject(ChatImage chatImage, IViewInjector iViewInjector) {
        ImageView ivAvatar = (ImageView) iViewInjector.findViewById(R.id.iv_avatar);
        ivAvatar.setOnClickListener(v -> {
            if (IM.sImUtils.imOptions.onImChatAvatarClickListener != null) {
                IM.sImUtils.imOptions.onImChatAvatarClickListener.onImChatAvatarClick(v, chatImage.getFromUid());
            }
        });
        ImageView ivBody = (ImageView) iViewInjector.findViewById(R.id.iv_image);
        TextView tvHasRead = (TextView) iViewInjector.findViewById(R.id.tv_has_read);
        adjustChatTargetLayout(chatImage, tvHasRead, ivAvatar, ivBody);
        Glide.with(getContext()).load(chatImage.getAvatarUrl())
                .apply(new RequestOptions().placeholder(R.drawable.ic_im_avatar_default).error(R.drawable.ic_im_avatar_default)).into(ivAvatar);
        Glide.with(getContext()).load(chatImage.getBody().getContent())
                .apply(new RequestOptions().placeholder(R.drawable.ic_im_avatar_default).error(R.drawable.ic_im_avatar_default))
                .into(ivBody);
        iViewInjector.clicked(R.id.iv_image, v -> {
            List<GalleryPhotoModel> list = new ArrayList<>();
            list.add(new GalleryPhotoModel(chatImage.getBody().getContent()));
            gvGallery.showPhotoGallery(0, list, ivBody);
        }).longClicked(R.id.iv_image, v -> {
            PopMenuUtils.showPopMenu(PopMenuUtils.IMAGE, getActivity(), ivBody, new PopMenuUtils.OnMenuItemClickListenerImpl() {
                @Override
                public void onDownloadClick(MenuItem menuItem) {
                    CommonUtils.downloadImage(getActivity(), chatImage.getBody().getContent());
                }
            });
            return false;
        });
        /**
         * 计算宽高比
         */
        ChatImage.Body.SizeBean size = chatImage.getBody().getSize();
        if (size != null && size.getHeight() > 0 && size.getWidth() > 0) {
            float v = size.getHeight() * 1.0f / size.getWidth() * 1.0f;
            ViewGroup.LayoutParams layoutParams = ivBody.getLayoutParams();
            layoutParams.width = PixelUtils.dip2px(getContext(), 120);
            layoutParams.height = PixelUtils.dip2px(getContext(), 120 * v);
            ivBody.setLayoutParams(layoutParams);
        } else {
            //兼容旧图片没有宽高比
            if (chatImage.getFromUid().equals(mLoginUser.getToUser())) {
                ivBody.setScaleType(ImageView.ScaleType.FIT_START);
            } else {
                ivBody.setScaleType(ImageView.ScaleType.FIT_END);
            }
        }
    }

    /**
     * 视频数据展示
     *
     * @param chatVideo
     * @param iViewInjector
     */
    private void handleVideoDataInject(ChatVideo chatVideo, IViewInjector iViewInjector) {
        ImageView ivAvatar = (ImageView) iViewInjector.findViewById(R.id.iv_avatar);
        ivAvatar.setOnClickListener(v -> {
            if (IM.sImUtils.imOptions.onImChatAvatarClickListener != null) {
                IM.sImUtils.imOptions.onImChatAvatarClickListener.onImChatAvatarClick(v, chatVideo.getFromUid());
            }
        });
        ImageView ivVideo = (ImageView) iViewInjector.findViewById(R.id.iv_video);
        RelativeLayout rlVideo = (RelativeLayout) iViewInjector.findViewById(R.id.rl_video);
        TextView tvHasRead = (TextView) iViewInjector.findViewById(R.id.tv_has_read);
        TextView tvTime = (TextView) iViewInjector.findViewById(R.id.tv_time);
        tvTime.setText(String.format(getString(R.string.format_time), chatVideo.getBody().getLength() / 60, chatVideo.getBody().getLength() % 60));
        adjustChatTargetLayout(chatVideo, tvHasRead, ivAvatar, rlVideo);
        Glide.with(getContext()).load(chatVideo.getAvatarUrl())
                .apply(new RequestOptions().placeholder(R.drawable.ic_im_avatar_default).error(R.drawable.ic_im_avatar_default)).into(ivAvatar);
        Glide.with(getContext()).load(chatVideo.getBody().getCover())
                .apply(new RequestOptions().placeholder(R.drawable.ic_im_avatar_default).error(R.drawable.ic_im_avatar_default)).into(ivVideo);

        rlVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), OneVideoPlayActivity.class);
                intent.putExtra(IntentKey.VIDEO_PLAY_ACTIVITY_KEY_VIDEO_PATH, chatVideo.getBody().getContent());
                startActivity(intent);
            }
        });
        /**
         * 计算宽高比
         */
        ChatVideo.Body.SizeBean size = chatVideo.getBody().getSize();
        if (size != null && size.getHeight() > 0 && size.getWidth() > 0) {
            float v = size.getHeight() * 1.0f / size.getWidth() * 1.0f;
            ViewGroup.LayoutParams layoutParams = rlVideo.getLayoutParams();
            layoutParams.width = PixelUtils.dip2px(getContext(), 120);
            layoutParams.height = PixelUtils.dip2px(getContext(), 120 * v);
            rlVideo.setLayoutParams(layoutParams);
        }
    }

    /**
     * 订单类型数据展示
     *
     * @param chatOrder
     * @param iViewInjector
     */
    private void handleOrderDataInject(ChatOrder chatOrder, IViewInjector iViewInjector) {
        ImageView ivAvatar = (ImageView) iViewInjector.findViewById(R.id.iv_avatar);
        ivAvatar.setOnClickListener(v -> {
            if (IM.sImUtils.imOptions.onImChatAvatarClickListener != null) {
                IM.sImUtils.imOptions.onImChatAvatarClickListener.onImChatAvatarClick(v, chatOrder.getFromUid());
            }
        });
        LinearLayout llOrder = (LinearLayout) iViewInjector.findViewById(R.id.ll_order);
        TextView tvHasRead = (TextView) iViewInjector.findViewById(R.id.tv_has_read);
        adjustChatTargetLayout(chatOrder, tvHasRead, ivAvatar, llOrder);
        Glide.with(getContext()).load(chatOrder.getAvatarUrl())
                .apply(new RequestOptions().placeholder(R.drawable.ic_im_avatar_default).error(R.drawable.ic_im_avatar_default)).into(ivAvatar);
        iViewInjector.text(R.id.tv_order_title, chatOrder.getBody().getTitle())
                .text(R.id.tv_order_body, chatOrder.getBody().getContent())
                .text(R.id.tv_order_footer, chatOrder.getBody().getFooter());
        iViewInjector.clicked(R.id.ll_order, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (IM.sImUtils.imOptions.onImChatItemClickListener != null) {
                    IM.sImUtils.imOptions.onImChatItemClickListener.onChatOrderClick(chatOrder);
                }
            }
        });
    }

    /**
     * 语音布局内容展示
     *
     * @param chatVoice
     * @param iViewInjector
     */
    private void handleVoiceDataInject(ChatVoice chatVoice, IViewInjector iViewInjector) {
        if (chatVoice.getFromUid().equals(mLoginUser.getToUser())) {
            iViewInjector.visibility(R.id.rl_target_voice, View.VISIBLE)
                    .visibility(R.id.rl_mine_voice, View.GONE);
            if (!chatVoice.isHaveRead()) {
                iViewInjector.visibility(R.id.iv_target_red, View.VISIBLE);
            } else {
                iViewInjector.visibility(R.id.iv_target_red, View.GONE);
            }
            RelativeLayout rlVoiceLine = (RelativeLayout) iViewInjector.findViewById(R.id.rl_target_voice_line);
            ImageView ivAvatar = (ImageView) iViewInjector.findViewById(R.id.iv_target_avatar);
            ivAvatar.setOnClickListener(v -> {
                if (IM.sImUtils.imOptions.onImChatAvatarClickListener != null) {
                    IM.sImUtils.imOptions.onImChatAvatarClickListener.onImChatAvatarClick(v, chatVoice.getFromUid());
                }
            });
            ImageView ivTargetSinger = (ImageView) iViewInjector.findViewById(R.id.iv_target_singer);
            TextView tvVoiceTime = (TextView) iViewInjector.findViewById(R.id.tv_target_voice_time);
            TextView tvHasRead = (TextView) iViewInjector.findViewById(R.id.tv_has_read);
            TextView tv_right_click = (TextView) iViewInjector.findViewById(R.id.tv_right_click);
            handleVoice(chatVoice, ivAvatar, ivTargetSinger, rlVoiceLine, tvVoiceTime, tv_right_click);
            settingReadUnreadDisplay(tvHasRead, chatVoice, true);
        } else {
            iViewInjector.visibility(R.id.rl_mine_voice, View.VISIBLE)
                    .visibility(R.id.rl_target_voice, View.GONE)
                    .visibility(R.id.iv_mine_red, View.GONE);
            RelativeLayout rlVoiceLine = (RelativeLayout) iViewInjector.findViewById(R.id.rl_mine_voice_line);
            ImageView ivAvatar = (ImageView) iViewInjector.findViewById(R.id.iv_mine_avatar);
            ivAvatar.setOnClickListener(v -> {
                if (IM.sImUtils.imOptions.onImChatAvatarClickListener != null) {
                    IM.sImUtils.imOptions.onImChatAvatarClickListener.onImChatAvatarClick(v, chatVoice.getFromUid());
                }
            });
            ImageView ivTargetSinger = (ImageView) iViewInjector.findViewById(R.id.iv_mine_singer);
            TextView tvVoiceTime = (TextView) iViewInjector.findViewById(R.id.tv_mine_voice_time);
            TextView tvHasRead = (TextView) iViewInjector.findViewById(R.id.tv_has_read);
            TextView tv_left_click = (TextView) iViewInjector.findViewById(R.id.tv_left_click);
            handleVoice(chatVoice, ivAvatar, ivTargetSinger, rlVoiceLine, tvVoiceTime, tv_left_click);
            settingReadUnreadDisplay(tvHasRead, chatVoice, true);
        }
    }

    /**
     * 语音播放
     *
     * @param chatVoice
     * @param ivAvatar
     * @param ivSinger
     * @param rlVoiceLine
     * @param tvVoiceTime
     */
    private void handleVoice(ChatVoice chatVoice, ImageView ivAvatar, ImageView ivSinger, RelativeLayout rlVoiceLine, TextView tvVoiceTime, TextView tvVoiceClick) {
        Glide.with(getContext()).load(chatVoice.getAvatarUrl())
                .apply(new RequestOptions().placeholder(R.drawable.ic_im_avatar_default).error(R.drawable.ic_im_avatar_default)).into(ivAvatar);
        //设置时间长度
        int voiceLength = (int) chatVoice.getBody().getLength();
        tvVoiceTime.setText(voiceLength <= 0 ? 1 + "''" : voiceLength + "''");
        //更改并显示录音条长度
        RelativeLayout.LayoutParams ps = (RelativeLayout.LayoutParams) tvVoiceClick.getLayoutParams();
        tvVoiceClick.setPadding(0, 0, 0, 0);
        ps.width = CommonUtils.getVoiceLineWight(getContext(), voiceLength);
        tvVoiceClick.setLayoutParams(ps);
        tvVoiceClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GeneralMessageDataInfo generalMessageDataInfo = chatVoice.getGeneralMessageDataInfo();
                if (ReceiptTypeEnum.RECEIVED.getType().equals(generalMessageDataInfo.getReceiptType())) {
                    generalMessageDataInfo.setReceiptType(ReceiptTypeEnum.READ.getType());
                    chatVoice.setHaveRead(true);
                    //更新数据库数据
                    imUtil.updateMessage(generalMessageDataInfo.changeToMessageDataInfoBean());
                    imUtil.sendReceipt(ReceiptTypeEnum.READ.getType(), generalMessageDataInfo.changeToMessageDataInfoBean());
                    mAdapter.notifyDataSetChanged();
                }
                //语音动画播放
                final AnimationDrawable animationDrawable = (AnimationDrawable) ivSinger.getDrawable();
                resetAnim(animationDrawable);//重置动画
                animationDrawable.start();
                //播放前重置
                MediaManager.release();
                //开始实质播放
                MediaManager.playSound(getActivity(), chatVoice.getBody().getContent(), new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        animationDrawable.selectDrawable(0);//显示动画第一帧
                        animationDrawable.stop();
                    }
                });
            }
        });
        tvVoiceClick.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PopMenuUtils.showPopMenu(PopMenuUtils.VOICE, getContext(), tvVoiceClick, new PopMenuUtils.OnMenuItemClickListenerImpl() {
                    @Override
                    public void onToggleVoiceMode(Context context, MenuItem menuItem) {
                        super.onToggleVoiceMode(context, menuItem);
                    }
                });
                return false;
            }
        });
    }

    /**
     * 调整聊天布局 左右
     *
     * @param chatBody
     * @param avatar
     * @param body
     */
    public void adjustChatTargetLayout(ChatBody chatBody, View avatar, View body) {
        RelativeLayout.LayoutParams avatarLayoutParams = (RelativeLayout.LayoutParams) avatar.getLayoutParams();
        RelativeLayout.LayoutParams bodyLayoutParams = (RelativeLayout.LayoutParams) body.getLayoutParams();
        if (chatBody.getFromUid().equals(mLoginUser.getToUser())) {
            avatarLayoutParams.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            bodyLayoutParams.removeRule(RelativeLayout.LEFT_OF);
            avatarLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            bodyLayoutParams.addRule(RelativeLayout.RIGHT_OF, avatar.getId());
            bodyLayoutParams.setMargins(PixelUtils.dp2px(getActivity(), 10.0f), 0, 0, 0);
        } else {
            avatarLayoutParams.removeRule(RelativeLayout.ALIGN_PARENT_LEFT);
            bodyLayoutParams.removeRule(RelativeLayout.RIGHT_OF);
            avatarLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            bodyLayoutParams.addRule(RelativeLayout.LEFT_OF, avatar.getId());
            bodyLayoutParams.setMargins(0, 0, PixelUtils.dp2px(getActivity(), 10.0f), 0);
        }
        avatar.setLayoutParams(avatarLayoutParams);
        body.setLayoutParams(bodyLayoutParams);
    }

    /**
     * 调整聊天布局 左右     有已读未读的
     *
     * @param chatBody
     * @param avatar
     * @param body
     */
    public void adjustChatTargetLayout(ChatBody chatBody, TextView tvHaveRead, View avatar, View body) {
        RelativeLayout.LayoutParams avatarLayoutParams = (RelativeLayout.LayoutParams) avatar.getLayoutParams();
        RelativeLayout.LayoutParams bodyLayoutParams = (RelativeLayout.LayoutParams) body.getLayoutParams();
        if (chatBody.getFromUid().equals(mLoginUser.getToUser())) {
            settingReadUnreadDisplay(tvHaveRead, chatBody, false);
            avatarLayoutParams.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            bodyLayoutParams.removeRule(RelativeLayout.LEFT_OF);
            avatarLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            bodyLayoutParams.addRule(RelativeLayout.RIGHT_OF, avatar.getId());
            bodyLayoutParams.setMargins(PixelUtils.dp2px(getActivity(), 10.0f), 0, 0, 0);
        } else {
            settingReadUnreadDisplay(tvHaveRead, chatBody, true);
            avatarLayoutParams.removeRule(RelativeLayout.ALIGN_PARENT_LEFT);
            bodyLayoutParams.removeRule(RelativeLayout.RIGHT_OF);
            avatarLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            bodyLayoutParams.addRule(RelativeLayout.LEFT_OF, avatar.getId());
            bodyLayoutParams.setMargins(0, 0, PixelUtils.dp2px(getActivity(), 10.0f), 0);
        }
        avatar.setLayoutParams(avatarLayoutParams);
        body.setLayoutParams(bodyLayoutParams);
    }

    public boolean isShownPanel() {
        return imExtension.isShown();
    }

    public void hidePanel() {
        imExtension.hidePanel();
    }

    /**
     * 重置动画
     *
     * @param animationDrawable
     */
    private void resetAnim(AnimationDrawable animationDrawable) {
        if (animationDrawable == null) return;
        if (!mAnimationDrawables.contains(animationDrawable)) {
            mAnimationDrawables.add(animationDrawable);
        }
        for (AnimationDrawable ad : mAnimationDrawables) {
            ad.selectDrawable(0);
            ad.stop();
        }
    }

    /**
     * 设置已读未读显示
     *
     * @param tvHaveRead
     * @param chatBody
     * @param show
     */
    public void settingReadUnreadDisplay(TextView tvHaveRead, ChatBody chatBody, boolean show) {
        if (show) {
            tvHaveRead.setVisibility(View.VISIBLE);
            if (chatBody.isHaveRead()) {
                tvHaveRead.setText("已读");
                tvHaveRead.setTextColor(0xff989898);
            } else {
                tvHaveRead.setText("未读");
                tvHaveRead.setTextColor(0xff3C73F1);
            }
            if (chatBody.getWhoReceive().startsWith("temp_")) {
                tvHaveRead.setVisibility(View.GONE);
            }
        } else {
            tvHaveRead.setVisibility(View.GONE);
        }
    }

    /**
     * 本地通知修改已读的界面
     *
     * @param info
     */
    public void notifyHaveReadChange(HasReadDataInfo info) {
        for (Object o : mData) {
            if (o instanceof ChatBody) {
                ChatBody chatBody = (ChatBody) o;
                for (String s : info.getMessageidList()) {
                    if (chatBody.getMessageId().equals(s)) {
                        chatBody.setHaveRead(true);
                        break;
                    }
                }
            }
            if (o instanceof ChatRichNone) {
                ChatRichNone chatBody = (ChatRichNone) o;
                for (String s : info.getMessageidList()) {
                    if (chatBody.getChatRich().getMessageId().equals(s)) {
                        chatBody.getChatRich().setHaveRead(true);
                        break;
                    }
                }
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 整理目前已读条目 并发送im消息
     *
     * @param begin
     * @param end
     */
    public void notifyHaveReadChange(int begin, int end) {
        ArrayList<String> strings = new ArrayList<>();
        if (begin < 0 || end < 0) return;
        for (int i = begin; i <= end; i++) {
            if (mData.get(i) instanceof ChatBody) {
                ChatBody chatBody = (ChatBody) mData.get(i);
                if (!chatBody.isHaveRead() && chatBody.getWhoSend().equals(mLoginUser.getToUser())) {
                    if (!MessageType.VOICE.getType().equals(chatBody.getType())) {
                        chatBody.setHaveRead(true);
                    }
                    strings.add(chatBody.getMessageId());
                    mAdapter.notifyItemChanged(i);
                }
            }
            if (mData.get(i) instanceof ChatRichNone) {
                ChatRich chatBody = ((ChatRichNone) mData.get(i)).getChatRich();
                if (!chatBody.isHaveRead() && chatBody.getWhoSend().equals(mLoginUser.getToUser())) {
                    chatBody.setHaveRead(true);
                    strings.add(chatBody.getMessageId());
                    mAdapter.notifyItemChanged(i);
                }
            }

        }
        if (strings.size() > 0) {
            String idString = strings.toString().substring(1, strings.toString().length() - 1).replace(" ", "");
            sendHaveRead(idString);
        }
    }

    /**
     * 发送已读im消息
     *
     * @param idlist
     */
    private void sendHaveRead(String idlist) {
        //OIMClient.getInstance().sendHaveReadID(mLoginUser.getToUser(), idlist);
    }

    /**
     * 是否启用IM聊天功能
     *
     * @param isEnable
     */
    public void enableImExtension(boolean isEnable) {
        if (imExtension == null) return;
        imExtension.setVisibility(isEnable ? View.VISIBLE : View.GONE);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void newMessageEvent(MessageEvent messageEvent) {
        if (messageEvent.eventType == EventType.EVENT_SEND_MESSAGE) {
            GeneralMessageDataInfo value = ((MessageDataInfoBean) messageEvent.values[0]).changeToDaoMessageDataInfo();
            mData.addAll(adjustNewData(value));
            bottomPostion = mData.size() - 1;
            Log.e("ssh", "bottomPostion = " + bottomPostion);
            Log.e("ssh", "EVENT_SEND_MESSAGE = " + value);
            mAdapter.notifyDataSetChanged();
            rvChatList.scrollToPosition(mAdapter.getItemCount() - 1);
//        } else if (messageEvent.eventType == MessageEvent.ME_RECEIVER_CHAT_MESSAGE) {
//            List<GeneralMessageDataInfo> list = (List<GeneralMessageDataInfo>) messageEvent.values[0];
//            ArrayList<String> strings = new ArrayList<>();
//            if (list != null) {
//                for (GeneralMessageDataInfo generalMessageDataInfo : list) {
//                    mData.addAll(adjustNewData(generalMessageDataInfo));
//                    strings.add(generalMessageDataInfo.getMessageId());
//                }
//            }
//            if (isTheFrontDesk) {
//                bottomPostion = mData.size() - 1;
//                notifyHaveReadChange(bottomPostion, bottomPostion);
//            }
//            mAdapter.notifyDataSetChanged();
//            rvChatList.scrollToPosition(mAdapter.getItemCount() - 1);
//        } else if (messageEvent.eventType == EventType.EVENT_SEND_LANGUAGE) {
//            String message = (String) messageEvent.values[0];
//            imExtension.setChatMessage(message);
//        } else if (messageEvent.eventType == MessageEvent.ME_NEW_READ_MESSAGE) {
//            HasReadDataInfo info = (HasReadDataInfo) messageEvent.values[0];
//            notifyHaveReadChange(info);
//        } else if (messageEvent.eventType == EventType.EVENT_NET_RESPONSE) {
//            View view = (View) messageEvent.values[0];
//            if (view instanceof FButtonView) {
//                FButtonView fButtonView = (FButtonView) view;
//                fButtonView.changeButtonClickUI();
//            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleDefaultEvent(ChatMessageEvent chatMessageEvent) {
        MessageDataInfoBean messageDataInfoBean = chatMessageEvent.getMessageDataInfoBean();
        if (messageDataInfoBean == null) return;
        if (chatMessageEvent.isChat()) {
            handleReceiverChatMessage(messageDataInfoBean);
        } else {
            handleUpdateChatMessage(messageDataInfoBean);
        }
    }

    //    处理消息接收
    private void handleReceiverChatMessage(MessageDataInfoBean messageDataInfoBean) {
        if (StringUtils.isNullOrEmpty(messageDataInfoBean.getToUserId()) || !messageDataInfoBean.getToUserId().equals(LoginUser.getInstance().getUserName()))
            return;
        //发送已读回执
        sendReceiptFilter(messageDataInfoBean);
        if (messageDataInfoBean != null) {
            mData.addAll(adjustNewData(messageDataInfoBean.changeToDaoMessageDataInfo()));
        }
        if (isTheFrontDesk) {
            bottomPostion = mData.size() - 1;
            notifyHaveReadChange(bottomPostion, bottomPostion);
        }
        mAdapter.notifyDataSetChanged();
        rvChatList.scrollToPosition(mAdapter.getItemCount() - 1);
    }

    //    处理消息状态修改
    private void handleUpdateChatMessage(MessageDataInfoBean messageDataInfoBean) {
        int index = -1;
        for (int i = mData.size() - 1; i >= 0; i--) {
            if (mData.get(i) instanceof ChatBody
                    && messageDataInfoBean.getId().equals(((ChatBody) mData.get(i)).getMessageId())
                    && ReceiptTypeEnum.READ.getType().equals(messageDataInfoBean.getReceiptType())) {
                ((ChatBody) mData.get(i)).setHaveRead(true);
                index = i;
                break;
            }
        }

        final int position = index;
        if (index != -1) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAdapter.notifyItemChanged(position);
                }
            });
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void scrollToEnd() {
        if (isTheFrontDesk) {
            bottomPostion = mData.size() - 1;
            notifyHaveReadChange(bottomPostion, bottomPostion);
        }
        mAdapter.notifyDataSetChanged();
        rvChatList.scrollToPosition(mAdapter.getItemCount() - 1);
    }

    //发送已读回执(除语音回执)
    private void sendReceiptFilter(MessageDataInfoBean messageDataInfoBean) {
        if (messageDataInfoBean != null && !MessageType.VOICE.getType().equals(messageDataInfoBean.getBody().getType())) {
            messageDataInfoBean.setReceiptType(ReceiptTypeEnum.READ.getType());
            imUtil.sendReceipt(ReceiptTypeEnum.READ.getType(), messageDataInfoBean);
        }
    }
}
