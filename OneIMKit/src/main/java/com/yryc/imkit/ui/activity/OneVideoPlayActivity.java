package com.yryc.imkit.ui.activity;

import android.media.MediaPlayer;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yryc.imkit.R;
import com.yryc.imkit.base.BaseActivity;
import com.yryc.imkit.constant.IntentKey;
import com.yryc.imkit.ui.view.ImVideoView;

/**
 * @author : Mai_Xiao_Peng
 * @email : Mai_Xiao_Peng@163.com
 * @time : 2018/12/13 17:49
 * @describe :
 */


public class OneVideoPlayActivity extends BaseActivity {

    private ImVideoView vvVideo;
    private ImageView ivPlay;
    private String mVideoPath;
    private ImageView ivBackground;

    @Override
    protected void onResume() {
        vvVideo.resume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        vvVideo.pause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        vvVideo.stopPlayback();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void beforeSetContentView() {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_video_play;
    }

    @Override
    protected void initView() {
        mVideoPath = getIntent().getStringExtra(IntentKey.VIDEO_PLAY_ACTIVITY_KEY_VIDEO_PATH);
        vvVideo = findViewById(R.id.vv_video);
        ivPlay = findViewById(R.id.iv_play);
        ivBackground = findViewById(R.id.iv_background);
        vvVideo.setVideoPath(mVideoPath);
        ivBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivPlay.setVisibility(View.GONE);
                ivBackground.setVisibility(View.GONE);
                vvVideo.start();
                vvVideo.setVisibility(View.VISIBLE);
            }
        });
        vvVideo.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                ivPlay.setVisibility(View.VISIBLE);
                ivBackground.setVisibility(View.VISIBLE);
                vvVideo.setVisibility(View.GONE);
            }
        });
        Glide.with(this).load(mVideoPath).into(ivBackground);
    }

    @Override
    protected void initData() {

    }

}
