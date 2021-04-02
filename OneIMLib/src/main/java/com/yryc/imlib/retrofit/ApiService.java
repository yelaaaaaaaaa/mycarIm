package com.yryc.imlib.retrofit;


import com.yryc.imlib.model.net.ChatListBean;
import com.yryc.imlib.model.net.GroupTopicBean;
import com.yryc.imlib.model.net.MessageHistoryBean;
import com.yryc.imlib.model.net.OssInfo;
import com.yryc.imlib.model.net.TopicBean;
import com.yryc.imlib.rx.BaseResponse;

import java.util.Map;

import io.reactivex.Flowable;
import retrofit2.http.Body;
import retrofit2.http.POST;


/**
 * @author : sklyand
 * @email : zhengdengyao@51yryc.com
 * @time : 2019/7/22 09:23
 * @describe ：
 */
public interface ApiService {

    /**
     * OSStoken获取
     * <p>
     * businessType 业务类型(user-head:用户头像; user-order:订单拍照; merchant:商户信息; user-driving-license:用户车主驾照图片;user-vehicle-license:用户车主行驶证图片;deal-car:买卖车图片; designated-license:代驾平台图片;driver-license:司机图片)
     * handleType   操作类型(upload:上传; down:下载
     *
     * @return
     */
    @POST("/carowner/v1-0/oss/getToken")
    Flowable<BaseResponse<OssInfo>> getOssPhoneToken(@Body Map<String, String> body);

    /**
     * @param body
     * @return 获取单聊历史记录
     */
    @POST("/carowner/im/v1-0/chat/getChatLogPage")
    Flowable<BaseResponse<MessageHistoryBean>> getChatLogPage(@Body Map<String, Object> body);

    /**
     * @param body
     * @return 获取topic id
     */
    @POST("/carowner/im/v1-0/chat/getTopic")
    Flowable<BaseResponse<TopicBean>> getTopic(@Body Map<String, String> body);

    /**
     * @param body
     * @return 获取聊天列表
     */
    @POST("/carowner/im/v1-0/chat/getTopicListByUser")
    Flowable<BaseResponse<ChatListBean>> getChatList(@Body Map<String, Object> body);

    /**
     * @param body
     * @return 创建群聊
     */
    @POST("/carowner/im/v1-0/groupchat/createGroupChatTopic")
    Flowable<BaseResponse<GroupTopicBean>> createGroupChatTopic(@Body Map<String, Object> body);

    /**
     * @param body
     * @return 加入群聊
     */
    @POST("/carowner/im/v1-0/groupchat/joinGroupChatTopic")
    Flowable<BaseResponse> joinGroupChatTopic(@Body Map<String, Object> body);

    /**
     * @param body
     * @return 获取群聊历史记录
     */
    @POST("/carowner/im/v1-0/groupchat/getGroupChatLogPage")
    Flowable<BaseResponse<MessageHistoryBean>> getGroupChatLogPage(@Body Map<String, Object> body);

    /**
     * @param body
     * @return 获取聊天列表
     */
    @POST("/carowner/im/v1-0/groupchat/getGroupTopicListByUser")
    Flowable<BaseResponse<ChatListBean>> getGroupChatList(@Body Map<String, Object> body);

    /**
     * @param body
     * @return 群聊(已收到)回执
     */
    @POST("/carowner/im/v1-0/groupchat/receiveGroupChat")
    Flowable<BaseResponse> receiveGroupChat(@Body Map<String, Object> body);
}
