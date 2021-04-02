package com.yryc.imkit.ui.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yryc.imkit.R;
import com.yryc.imkit.core.slim.SlimAdapter;
import com.yryc.imkit.core.slim.SlimInjector;
import com.yryc.imkit.core.slim.viewinjector.IViewInjector;
import com.yryc.imkit.ui.view.dialog.LanguageDialog;
import com.yryc.imlib.client.MessageEvent;

import com.yryc.imlib.info.WordDataInfo;
import com.yryc.imlib.rx.RxEvent;
import com.yryc.imlib.rx.RxEvent.EventType;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : Mai_Xiao_Peng
 * @email : Mai_Xiao_Peng@163.com
 * @time : 2018/12/20 14:21
 * @describe :
 */


public class ImLanguageView extends LinearLayout {

    private View view;
    private List<WordDataInfo> mData = new ArrayList<>();
    private LanguageDialog languageDialog;
    private TextView tvLanguageAdd;
    private RecyclerView rvLanguage;
    private SlimAdapter mSlimAdapter;

    public ImLanguageView(Context context) {
        this(context, null);
    }

    public ImLanguageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImLanguageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr);
        EventBus.getDefault().register(this);
    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr) {
        view = View.inflate(context, R.layout.view_im_language, this);
        tvLanguageAdd = view.findViewById(R.id.tv_language_add);
        tvLanguageAdd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                languageDialog.show();
            }
        });
        rvLanguage = view.findViewById(R.id.rv_language);
        rvLanguage.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mSlimAdapter = SlimAdapter.create().register(R.layout.item_language, new SlimInjector<WordDataInfo>() {
            @Override
            public void onInject(final WordDataInfo wordDataInfo, IViewInjector iViewInjector) {
                iViewInjector.text(R.id.tv_language, wordDataInfo.getContent())
                        .clicked(R.id.ll_root, new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                EventBus.getDefault().post(new MessageEvent(EventType.EVENT_SEND_LANGUAGE, wordDataInfo.getContent()));
                            }
                        });
            }
        }).attachTo(rvLanguage).updateData(mData);
        languageDialog = new LanguageDialog(getContext());
        getLanguageList();
    }

    private void getLanguageList() {
        mData.clear();
        //List<WordDataInfo> wordDataInfoList = OIMClient.getInstance().getWordDataInfoList();
        //mData.addAll(wordDataInfoList);
        mSlimAdapter.notifyDataSetChanged();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void newMessageEvent(MessageEvent messageEvent) {
        if (messageEvent.eventType == MessageEvent.ME_ADD_WORD_SUCCESS) {
            WordDataInfo wordDataInfo = (WordDataInfo) messageEvent.values[0];
            mData.add(wordDataInfo);
            languageDialog.dismissDialog();
        }
    }

}
