package com.yryc.onecarim;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yryc.imkit.constant.Function;
import com.yryc.imkit.im.IM;
import com.yryc.imkit.widget.emoji.EmojiManager;
import com.yryc.imlib.model.net.GroupTopicBean;
import com.yryc.imlib.retrofit.ApiService;
import com.yryc.imlib.retrofit.RetrofitServiceCreator;
import com.yryc.imkit.ui.activity.OneChatListActivity;
import com.yryc.imkit.ui.activity.OneGroupChatListActivity;
import com.yryc.imlib.rx.RxThrowableConsumer;
import com.yryc.imlib.rx.RxUtils;
import com.yryc.imlib.xmpp.ImUtil;
import com.yryc.imlib.xmpp.LoginUser;

import java.util.HashMap;

import io.reactivex.functions.Consumer;


public class MainActivity extends AppCompatActivity {

    String uid_one = "USER_1140";
    String token_one = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhcHBpZCI6ImJBNWw1TEJmZlB3YyIsImF1ZCI6InljIiwiZXhwIjoxODc2MjA5MzA5LCJpYXQiOjE1NjUxNjkzMDksImlzcyI6InljIiwianRpIjoiMm1zOWljZWdpMmNzcWNub28wMDA5ZmQxIiwibmJmIjoxNTY1MTY5MzA5LCJ1aWQiOiJVU0VSXzExNDAifQ.gjXQPtinewU9Jm2FDhdB4SFBBkbDB_ZZhu3vWQxNvBE";
    String uid_two = "USER_1143";
    String token_two = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhcHBpZCI6ImJBNWw1TEJmZlB3YyIsImF1ZCI6InljIiwiZXhwIjoxODc2MjA5MzMwLCJpYXQiOjE1NjUxNjkzMzAsImlzcyI6InljIiwianRpIjoiMm1zOWlkazg3YWtlajdjOG9jMDAwZGkzIiwibmJmIjoxNTY1MTY5MzMwLCJ1aWQiOiJVU0VSXzExNDMifQ.KkHhJ80pZTMW7q5a88_MOEjtdLM2Pjrgk3LU8BB-5IY";


    //测试环境
    //    String uid_two = "USER_491";
//    String token_two = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhcHBpZCI6ImxFdFRFY0J2RXJjdCIsImF1ZCI6InljIiwiZXhwIjoxODczODU0MzkzLCJpYXQiOjE1NjI4MTQzOTMsImlzcyI6InljIiwianRpIjoiMm1vM21xOWxmNTJlczA4NDRjMDAxbjUxIiwibmJmIjoxNTYyODE0MzkzLCJ1aWQiOiJVU0VSXzQ5MSJ9.3B6LzfzlNwyXMSV0enFsuOkSR8NON024crv2ZJvGs9E";

    TextView tv_aaaa;
    LinearLayout ll_aaaa;
    boolean trueOrFalse = true;
    String fromName = "test13";
    String toName = "test11";
    String toNameUid = "test11";
    String toName2 = "test2";
    String toNameUid2 = "test2";
    String roomName = "测试群聊6";
    String groupTopicId = "5ed7096c9fc66d0001cffb97";
//    String roomName = "测试群聊8";
//    String groupTopicId = "5ed1f9089fc66d0001cff3c3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_aaaa = findViewById(R.id.tv_aaaa);
        ll_aaaa = findViewById(R.id.ll_aaaa);
        Button btAaa = findViewById(R.id.bt_aaa);
        Button bt_aab = findViewById(R.id.bt_aab);
        Button btCcc = findViewById(R.id.bt_ccc);
        Button btDdd = findViewById(R.id.bt_ddd);
        Button btEee = findViewById(R.id.bt_eee);
        Button btfff = findViewById(R.id.bt_fff);
        Button btggg = findViewById(R.id.bt_ggg);
        Button bt_hhh = findViewById(R.id.bt_hhh);
        Button bt_iii = findViewById(R.id.bt_iii);

        btAaa.setOnClickListener(onClickListener);
        bt_aab.setOnClickListener(onClickListener);
        btCcc.setOnClickListener(onClickListener);
        btDdd.setOnClickListener(onClickListener);
        btEee.setOnClickListener(onClickListener);
        btEee.setOnClickListener(onClickListener);
        btfff.setOnClickListener(onClickListener);
        btggg.setOnClickListener(onClickListener);
        bt_hhh.setOnClickListener(onClickListener);
        bt_iii.setOnClickListener(onClickListener);
        Button bt_bbbbb = findViewById(R.id.bt_bbbbb);
        bt_bbbbb.setOnClickListener(onClickListener);

        btAaa.setText("连接" + toName);
        btEee.setText("连接" + toName2);
        btDdd.setText("登录" + fromName);
        bt_aab.setText("聊天列表");

        EmojiManager.init(this);//初始化
        ImUtil.getInstance().setContext(this);
        ImUtil.getInstance().loginXmpp(fromName, "123456");
        new IM.ImBuilder(getApplicationContext())
                .setEnableFunctions(Function.ALBUM, Function.SHOOTING, Function.LOCATION)
                .build();
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.bt_aaa:
//                    final ImUtil imutila = new IM.ImBuilder(MainActivity.this)
//                            .setAppKey("bA5l5LBffPwc")      //开发
////                            .setAppKey("lEtTEcBvErct")      //测试
//                            .setDebug(true)
//                            .setEnableFunctions(Function.ALBUM, Function.SHOOTING, Function.LANGUAGE)
//                            .setToken(token_one)
//                            .setDeviceId("")
//                            .setChannelName(uid_one).build();
//                    imutila.connect(new ConnectCallback() {
////                        @Override
////                        public void onTokenIncorrect() {
////                            Log.e("ssh", "        ============");
////                        }
////
////                        @Override
////                        public void onSuccess(String var) {
////                            Log.e("ssh", "        ============");
////                            imutila.openChatList(MainActivity.this);
////                            tv_aaaa.setText("连接林耀鹏商家");
////                            trueOrFalse = false;
////                        }
////
////                        @Override
////                        public void onError(Enums.ErrorCode error) {
////                            Log.e("ssh", error.getMessage() + "        ============");
////                        }
////                    });
//                    IM.sImUtils.openSingleChatActivity(MainActivity.this,"USER_1143","林");

                    IM.sImUtils.openSingleChatActivity(MainActivity.this, toNameUid);
                    break;
                case R.id.bt_aab:
//                    final ImUtil imutil = new IM.ImBuilder(MainActivity.this)
//                            .setAppKey("bA5l5LBffPwc")      //开发
////                            .setAppKey("lEtTEcBvErct")      //测试
//                            .setDebug(true)
//                            .setEnableFunctions(Function.ALBUM, Function.SHOOTING, Function.LANGUAGE)
//                            .setToken(token_two)
//                            .setDeviceId("")
//                            .setChannelName(uid_two).build();
//                    imutil.connect(new ConnectCallback() {
//                        @Override
//                        public void onTokenIncorrect() {
//                            Log.e("ssh", "        ============");
//                        }
//
//                        @Override
//                        public void onSuccess(String var) {
//                            Log.e("ssh", "        ============");
//                            imutil.openChatList(MainActivity.this);
//                            tv_aaaa.setText("连接林耀鹏商家");
//                            trueOrFalse = false;
//                        }
//
//                        @Override
//                        public void onError(Enums.ErrorCode error) {
//                            Log.e("ssh", error.getMessage() + "        ============");
//                        }
//                    });
//                    IM.sImUtils.openSingleChatActivity(MainActivity.this, "test14@yc-dev.com", "test14");
                    Intent intent = new Intent(MainActivity.this, OneChatListActivity.class);
                    startActivity(intent);
                    break;
                case R.id.bt_bbbbb:
                    break;
                case R.id.bt_ccc:
                    ImUtil.getInstance().logout();
                    break;
                case R.id.bt_ddd:
                    ImUtil.getInstance().loginXmpp(fromName, "123456");
                    break;
                case R.id.bt_eee:
                    IM.sImUtils.openSingleChatActivity(MainActivity.this, toNameUid2);
                    break;
                case R.id.bt_fff:
                    createGroup();
                    break;
                case R.id.bt_ggg:
                    joinGroup();
                    break;
                case R.id.bt_hhh:
                    startGroup();
                    break;
                case R.id.bt_iii:
                    Intent intent2 = new Intent(MainActivity.this, OneGroupChatListActivity.class);
                    startActivity(intent2);
                    break;
            }
        }
    };

    /**
     * 创建群聊
     */
    private void createGroup() {
        HashMap<String, Object> data = new HashMap<>();
        data.put("topicName", roomName);
        data.put("topicType", "1");
        ApiService apiService = RetrofitServiceCreator.createService(MainActivity.this, ApiService.class);
        apiService.createGroupChatTopic(data).compose(RxUtils.rxSchedulerHelper())
                .compose(RxUtils.handleResult()).subscribe(new Consumer<GroupTopicBean>() {
            @Override
            public void accept(GroupTopicBean groupTopicBean) throws Exception {
                Toast.makeText(MainActivity.this, "创建成功", Toast.LENGTH_SHORT).show();
                LoginUser.getInstance().setGroupName(roomName);
                LoginUser.getInstance().setGroupTopicId(groupTopicBean.getTopicId());
            }
        }, new RxThrowableConsumer());
    }

    /**
     * 加入群聊
     */
    private void joinGroup() {
        HashMap<String, Object> data = new HashMap<>();
        data.put("imUserId", fromName);
        data.put("topicId", LoginUser.getInstance().getGroupTopicId());
        ApiService apiService = RetrofitServiceCreator.createService(MainActivity.this, ApiService.class);
        apiService.joinGroupChatTopic(data).compose(RxUtils.rxSchedulerHelper())
                .compose(RxUtils.handleResultCode()).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Toast.makeText(MainActivity.this, "加入成功", Toast.LENGTH_SHORT).show();
                ImUtil.getInstance().joinMultiUserChat(fromName, LoginUser.getInstance().getGroupTopicId());
            }
        }, new RxThrowableConsumer());
    }

    /**
     * 开始群聊
     */
    private void startGroup() {
//        IM.sImUtils.openGroupChatActivity(MainActivity.this, roomName, StringUtils.isNullOrEmpty(LoginUser.getInstance().getGroupTopicId()) ? groupTopicId : LoginUser.getInstance().getGroupTopicId());
        IM.sImUtils.openGroupChatActivity(MainActivity.this, roomName, groupTopicId);
    }

    @Override
    protected void onDestroy() {
        ImUtil.getInstance().logout();
        super.onDestroy();
    }
}
