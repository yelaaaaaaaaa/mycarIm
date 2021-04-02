package com.yryc.imkit.ui.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.yryc.imkit.R;
import com.yryc.imkit.base.BaseFragment;
import com.yryc.imkit.constant.Config;
import com.yryc.imkit.constant.Function;
import com.yryc.imkit.constant.IntentKey;
import com.yryc.imkit.constant.MessageType;
import com.yryc.imkit.constant.OperateCode;
import com.yryc.imkit.core.helper.PermissionHelper;
import com.yryc.imkit.core.slim.SlimAdapter;
import com.yryc.imkit.core.slim.SlimInjector;
import com.yryc.imkit.core.slim.viewinjector.IViewInjector;
import com.yryc.imkit.im.IM;
import com.yryc.imlib.model.chat.ChatImage;
import com.yryc.imlib.model.chat.ChatVideo;
import com.yryc.imkit.ui.activity.OneCameraActivity;
import com.yryc.imkit.ui.activity.OneMapLocationActivity;
import com.yryc.imkit.utils.CommonUtils;
import com.yryc.imkit.utils.OssUtils;
import com.yryc.imlib.client.MessageEvent;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : Mai_Xiao_Peng
 * @email : Mai_Xiao_Peng@163.com
 * @time : 2018/12/5 14:36
 * @describe :
 */


public class FunctionalAreaFragment extends BaseFragment {

    private RecyclerView rvFunction;
    private OnLanguageItemClickListener onLanguageItemClickListener;
    private List<Object> mData = new ArrayList<>();


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_functional_area;
    }

    @Override
    protected void initView() {
        rvFunction = mView.findViewById(R.id.rl_function);
    }

    @Override
    protected void initData() {
        if (IM.sImUtils.imOptions.functions.size() > 0) {
            mData.addAll(IM.sImUtils.imOptions.functions);
        }
        rvFunction.setLayoutManager(new GridLayoutManager(getContext(), 4, GridLayoutManager.VERTICAL, false));
        SlimAdapter.create().register(R.layout.item_function_area, new SlimInjector<Function>() {
            @Override
            public void onInject(final Function function, IViewInjector iViewInjector) {
                iViewInjector.text(R.id.tv_function_desc, function.getType())
                        .image(R.id.iv_function, function.getResId())
                        .clicked(R.id.rl_function_root, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (function.getType().equals(Function.ALBUM.getType())) {
                                    handleAlbumAndShootingClick(view, Function.ALBUM);
                                } else if (function.getType().equals(Function.SHOOTING.getType())) {
                                    handleAlbumAndShootingClick(view, Function.SHOOTING);
                                } else if (function.getType().equals(Function.LANGUAGE.getType())) {
                                    if (onLanguageItemClickListener != null) {
                                        onLanguageItemClickListener.onClick();
                                    }
                                } else if (function.getType().equals(Function.LOCATION.getType())) {
                                    handleLocationClick(view, Function.LOCATION);
                                }
                            }
                        });
            }
        }).attachTo(rvFunction).updateData(mData);
    }

    private void handleLocationClick(View view, Function location) {
        PermissionHelper permissionHelper = new PermissionHelper(getActivity());
        permissionHelper.requestPermissions(getResources().getString(R.string.tip_permission), new PermissionHelper.PermissionListener() {
            @Override
            public void doAfterGrand(String... permission) {
                Intent intent = new Intent(FunctionalAreaFragment.this.getActivity(), OneMapLocationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }

            @Override
            public void doAfterDenied(String... permission) {
                Toast.makeText(getContext(), "请授权,否则无法发送位置", Toast.LENGTH_SHORT).show();
            }
        }, Manifest.permission.ACCESS_COARSE_LOCATION);
    }

    /**
     * 处理相册照片
     *
     * @param view
     * @param function
     */
    private void handleAlbumAndShootingClick(View view, Function function) {
        PermissionHelper permissionHelper = new PermissionHelper(getActivity());
        permissionHelper.requestPermissions(getResources().getString(R.string.tip_permission), new PermissionHelper.PermissionListener() {
            @Override
            public void doAfterGrand(String... permission) {
                if (function.getCode() == Function.SHOOTING.getCode()) {
                    startActivityForResult(new Intent(getContext(), OneCameraActivity.class), OperateCode.RequestCode.SYSTEM_SHOOTING_ACTIVITY);
                } else if (function.getCode() == Function.ALBUM.getCode()) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, OperateCode.RequestCode.SYSTEM_ALBUM_ACTIVITY);
                }
            }

            @Override
            public void doAfterDenied(String... permission) {
                Toast.makeText(getContext(), "请授权,否则无法拍照", Toast.LENGTH_SHORT).show();
            }
        }, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void newMessageEvent(MessageEvent messageEvent) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == OperateCode.RequestCode.SYSTEM_SHOOTING_ACTIVITY && resultCode == Activity.RESULT_OK && data != null) {
            if (!TextUtils.isEmpty(data.getStringExtra(IntentKey.CAMARE_ACTIVITY_KEY_PHOTO_PATH))) {
                uploadFile(MessageType.IMAGE, data.getStringExtra(IntentKey.CAMARE_ACTIVITY_KEY_PHOTO_PATH));
            }
            if (!TextUtils.isEmpty(data.getStringExtra(IntentKey.CAMARE_ACTIVITY_KEY_VIDEO_URL))) {
                uploadFile(MessageType.VIDEO, data.getStringExtra(IntentKey.CAMARE_ACTIVITY_KEY_VIDEO_URL));
            }
        } else if (requestCode == OperateCode.RequestCode.SYSTEM_ALBUM_ACTIVITY && resultCode == Activity.RESULT_OK && data != null) {
            handleAlbumPicUpload(requestCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 相册选择图片上传
     *
     * @param requestCode
     * @param data
     */
    private void handleAlbumPicUpload(int requestCode, Intent data) {
        try {
            Uri selectedImage = data.getData(); //获取系统返回的照片的Uri
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContext().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);//从系统表中查询指定Uri对应的照片
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String path = cursor.getString(columnIndex);  //获取照片路径
            cursor.close();
            uploadFile(MessageType.IMAGE, path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 上传文件获取osstoken
     *
     * @param messageType
     * @param path
     */
    private void uploadFile(MessageType messageType, String path) {
        showProgressDialog(getResources().getString(R.string.loaded_wait_send));
        OssUtils.uploadOssServer(getContext(), "user-head", "upload", path, new OssUtils.OnOssUploadStateListener() {
            @Override
            public void onOssUploadSuccess(String ossPath) {
                sendMessage(messageType, path, ossPath);
                hideProgressDialog();
            }

            @Override
            public void onOssUploadError(Exception e) {
                Log.e(TAG, e.getMessage());
                hideProgressDialog();
            }
        });
    }

    /**
     * 发送消息
     *
     * @param messageType
     * @param filePath
     * @param ossPath
     */
    private void sendMessage(MessageType messageType, String filePath, String ossPath) {
        if (messageType.getType().equals(MessageType.IMAGE.getType())) {
            ChatImage.Body body = new ChatImage.Body();
            body.setType(MessageType.IMAGE.getType());
            body.setContent(ossPath);
            List<Integer> imageMetaData = CommonUtils.getImageMetaData(filePath);
            body.setSize(new ChatImage.Body.SizeBean(imageMetaData.get(0), imageMetaData.get(1)));
            CommonUtils.sendMessage(getContext(), messageType, body, filePath);
        } else if (messageType.getType().equals(MessageType.VIDEO.getType())) {
            ChatVideo.Body body = new ChatVideo.Body();
            body.setType(MessageType.VIDEO.getType());
            body.setContent(ossPath);
            body.setCover(String.format(Config.OSS_VIDEO, ossPath, 100));
            List<String> videoMetaData = CommonUtils.getVideoMetaData(filePath);
            body.setLength((int) Math.ceil(Integer.parseInt(videoMetaData.get(0)) / 1000.0f));
            body.setSize(new ChatVideo.Body.SizeBean(Integer.parseInt(videoMetaData.get(1)), Integer.parseInt(videoMetaData.get(2))));
            CommonUtils.sendMessage(getContext(), messageType, body, filePath);
        }
    }

    public interface OnLanguageItemClickListener {
        void onClick();
    }

    public void setOnLanguageItemClickListener(OnLanguageItemClickListener onLanguageItemClickListener) {
        this.onLanguageItemClickListener = onLanguageItemClickListener;
    }
}
