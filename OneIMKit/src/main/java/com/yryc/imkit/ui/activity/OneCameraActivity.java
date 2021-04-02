package com.yryc.imkit.ui.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.cjt2325.cameralibrary.JCameraView;
import com.cjt2325.cameralibrary.listener.ClickListener;
import com.cjt2325.cameralibrary.listener.ErrorListener;
import com.cjt2325.cameralibrary.listener.JCameraListener;
import com.cjt2325.cameralibrary.util.FileUtil;
import com.cjt2325.cameralibrary.util.LogUtil;
import com.yryc.imkit.R;
import com.yryc.imkit.constant.Config;
import com.yryc.imkit.constant.IntentKey;

import java.lang.ref.WeakReference;

public class OneCameraActivity extends AppCompatActivity {

    private JCameraView jCameraView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_camera_video);
        jCameraView = findViewById(R.id.camera_view);
        //设置视频保存路径
        jCameraView.setSaveVideoPath(Config.Dir.VIDEO);
        jCameraView.setFeatures(JCameraView.BUTTON_STATE_BOTH);
        jCameraView.setTip(getResources().getString(R.string.video_long_click));
        jCameraView.setMediaQuality(JCameraView.MEDIA_QUALITY_MIDDLE);
        jCameraView.setErrorLisenter(new JCamara(this));
        //JCameraView监听
        jCameraView.setJCameraLisenter(new JCameraListener() {
            @Override
            public void captureSuccess(Bitmap bitmap) {
                //获取图片bitmap
                String path = FileUtil.saveBitmap(Config.Dir.IMG, bitmap);
                Intent intent = new Intent();
                intent.putExtra(IntentKey.CAMARE_ACTIVITY_KEY_PHOTO_PATH, path);
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void recordSuccess(String url, Bitmap firstFrame) {
                //获取视频路径
                Intent intent = new Intent();
                intent.putExtra(IntentKey.CAMARE_ACTIVITY_KEY_VIDEO_URL, url);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        jCameraView.setLeftClickListener(new ClickListener() {
            @Override
            public void onClick() {
                OneCameraActivity.this.finish();
            }
        });
        jCameraView.setRightClickListener(new ClickListener() {
            @Override
            public void onClick() {
                Toast.makeText(OneCameraActivity.this, "Right", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        //全屏显示
        if (Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        } else {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(option);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        jCameraView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        jCameraView.onPause();
    }

    public static class JCamara implements ErrorListener {

        private WeakReference<OneCameraActivity> activityWeakReference;

        public JCamara(OneCameraActivity oneCameraActivity) {
            activityWeakReference = new WeakReference<>(oneCameraActivity);
        }

        @Override
        public void onError() {
            if (activityWeakReference == null || activityWeakReference.get() == null) return;
            //错误监听
            LogUtil.e("JCameraView", "camera error");
            Intent intent = new Intent();
            activityWeakReference.get().setResult(103, intent);
            activityWeakReference.get().finish();
        }

        @Override
        public void AudioPermissionError() {
            //Toast.makeText(OneCameraActivity.this, "", Toast.LENGTH_SHORT).show();
        }
    }

}
