package com.yryc.imkit.listener;

import com.yryc.imlib.model.chat.ChatRich;
import com.yryc.imkit.ui.view.FButtonView;

/**
 * @author : Mai_Xiao_Peng
 * @email : Mai_Xiao_Peng@163.com
 * @time : 2019/4/9 10:53
 * @describe :
 */

public interface OnImChatRichItemClickListener {

    void onRichItemClick(FButtonView fButtonView, ChatRich.Body.Button button);

}
