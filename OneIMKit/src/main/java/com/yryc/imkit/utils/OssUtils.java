package com.yryc.imkit.utils;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider;
import com.alibaba.sdk.android.oss.common.utils.OSSUtils;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.OSSRequest;
import com.alibaba.sdk.android.oss.model.OSSResult;
import com.alibaba.sdk.android.oss.model.ObjectMetadata;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.google.gson.Gson;
import com.yryc.imkit.constant.MessageType;
import com.yryc.imlib.model.net.OssInfo;
import com.yryc.imlib.retrofit.ApiService;
import com.yryc.imlib.retrofit.RetrofitServiceCreator;
import com.yryc.imlib.rx.RxThrowableConsumer;
import com.yryc.imlib.rx.RxUtils;

import java.io.File;
import java.util.HashMap;

import io.reactivex.functions.Consumer;

/**
 * @author : Mai_Xiao_Peng
 * @email : Mai_Xiao_Peng@163.com
 * @time : 2018/8/3 16:23
 * @describe :
 */


public class OssUtils {

    private static final String TAG = OSSUtils.class.getSimpleName();

    /**
     * oss 异步上传对象
     *
     * @param ossInfo
     * @param uri
     * @param ossCompletedCallback
     * @return
     */
    public static OSSAsyncTask<PutObjectResult> asyncUploadObject(Context context, OssInfo ossInfo, Uri uri, OSSCompletedCallback ossCompletedCallback) {
        // ACCESS_ID,ACCESS_KEY是在阿里云申请的
        OSSCredentialProvider credentialProvider = new OSSStsTokenCredentialProvider(ossInfo.getAccessKeyId(),
                ossInfo.getAccessKeySecret(), ossInfo.getSecurityToken());
        //该配置类如果不设置，会有默认配置，具体可看该类
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        OSS oss = new OSSClient(context, ossInfo.getEndPoint(), credentialProvider, conf);
        //创建上传Object的Metadata
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setCacheControl("no-cache");
        objectMetadata.setHeader("Pragma", "no-cache");
        objectMetadata.setContentType("video/mpeg4");
        File uploadFile = new File(uri.getPath());
        if (!uploadFile.exists()) return null;
        objectMetadata.setContentDisposition("inline;filename=" + uploadFile.getName());
        // 构造上传请求
        PutObjectRequest request = new PutObjectRequest(ossInfo.getBucket(),
                ossInfo.getFolder() + File.separator + uploadFile.getName(), uploadFile.getAbsolutePath(), objectMetadata);
        try {
            return oss.asyncPutObject(request, ossCompletedCallback);
        } catch (Exception e) {
            Log.e("OssUtils", "oss 云存储对象上传图片异常");
        } finally {

        }
        return null;
    }
    /**
     * oss 异步上传对象
     *
     * @param ossInfo
     * @param uri
     * @param ossCompletedCallback
     * @return
     */
    public static OSSAsyncTask<PutObjectResult> asyncUploadObject(Context context, OssInfo ossInfo, Uri uri, MessageType messageType, OSSCompletedCallback ossCompletedCallback) {
        // ACCESS_ID,ACCESS_KEY是在阿里云申请的
        OSSCredentialProvider credentialProvider = new OSSStsTokenCredentialProvider(ossInfo.getAccessKeyId(),
                ossInfo.getAccessKeySecret(), ossInfo.getSecurityToken());
        //该配置类如果不设置，会有默认配置，具体可看该类
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        OSS oss = new OSSClient(context, ossInfo.getEndPoint(), credentialProvider, conf);
        //创建上传Object的Metadata
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setCacheControl("no-cache");
        objectMetadata.setHeader("Pragma", "no-cache");
        if (messageType.getType().equals(MessageType.IMAGE.getType())) {
            objectMetadata.setContentType("image/jpeg");
        } else if (messageType.getType().equals(MessageType.VIDEO.getType())) {
            objectMetadata.setContentType("video/mpeg4");
        }else if (messageType.getType().equals(MessageType.VOICE.getType())) {
            objectMetadata.setContentType("audio/amr");
        }

        File uploadFile = new File(uri.getPath());
        if (!uploadFile.exists()) return null;
        objectMetadata.setContentDisposition("inline;filename=" + uploadFile.getName());
        // 构造上传请求
        PutObjectRequest request = new PutObjectRequest(ossInfo.getBucket(),
                ossInfo.getFolder() + File.separator + uploadFile.getName(), uploadFile.getAbsolutePath(), objectMetadata);
        try {
            return oss.asyncPutObject(request, ossCompletedCallback);
        } catch (Exception e) {
            Log.e("OssUtils", "oss 云存储对象上传图片异常");
        } finally {

        }
        return null;
    }
    /**
     * 上传Oss服务器
     *
     * @param context
     * @param ossBusinessType
     * @param ossHandleType
     * @param path
     */
    public static void uploadOssServer(Context context, String ossBusinessType, String ossHandleType, String path, MessageType messageType, OnOssUploadStateListener onOssUploadStateListener) {
        Uri uri = Uri.fromFile(new File(path));
        HashMap<String, String> body = new HashMap<>();
        body.put("businessType", ossBusinessType);
        body.put("handleType", ossHandleType);
        RetrofitServiceCreator.createService(context, ApiService.class).getOssPhoneToken(body)
                .compose(RxUtils.rxSchedulerHelper())
                .compose(RxUtils.handleResult()).subscribe(new Consumer<OssInfo>() {
            @Override
            public void accept(OssInfo ossInfo) throws Exception {
                try {
                    OssUtils.asyncUploadObject(context, ossInfo, uri, messageType,new OSSCompletedCallback() {
                        @Override
                        public void onSuccess(OSSRequest ossRequest, OSSResult ossResult) {
                            Log.e(TAG, String.format("%s:%s", "uploadOssServer-onSuccess", new Gson().toJson(ossRequest)));
                            String ossPath = ossInfo.getHost() + File.separator + ossInfo.getFolder() + File.separator + new File(uri.getPath()).getName();
                            onOssUploadStateListener.onOssUploadSuccess(ossPath);
                        }

                        @Override
                        public void onFailure(OSSRequest ossRequest, ClientException e, ServiceException e1) {
                            Log.e(TAG, String.format("%s:%s", "uploadOssServer-onFailure", e.getMessage()));
                            onOssUploadStateListener.onOssUploadError(e1);
                        }
                    });
                } catch (Exception exception) {
                    Log.e(TAG, String.format("%s:%s", "uploadFile", exception.getMessage()));
                    onOssUploadStateListener.onOssUploadError(exception);
                }
            }
        },new RxThrowableConsumer());
    }

    /**
     * 上传Oss服务器
     *
     * @param context
     * @param ossBusinessType
     * @param ossHandleType
     * @param path
     */
    public static void uploadOssServer(Context context, String ossBusinessType, String ossHandleType, String path, OnOssUploadStateListener onOssUploadStateListener) {
        Uri uri = Uri.fromFile(new File(path));
        HashMap<String, String> body = new HashMap<>();
        body.put("businessType", ossBusinessType);
        body.put("handleType", ossHandleType);
        RetrofitServiceCreator.createService(context, ApiService.class).getOssPhoneToken(body)
                .compose(RxUtils.rxSchedulerHelper())
                .compose(RxUtils.handleResult()).subscribe(new Consumer<OssInfo>() {
            @Override
            public void accept(OssInfo ossInfo) throws Exception {
                try {
                    OssUtils.asyncUploadObject(context, ossInfo, uri, new OSSCompletedCallback() {
                        @Override
                        public void onSuccess(OSSRequest ossRequest, OSSResult ossResult) {
                            Log.e(TAG, String.format("%s:%s", "uploadOssServer-onSuccess", new Gson().toJson(ossRequest)));
                            String ossPath = ossInfo.getHost() + File.separator + ossInfo.getFolder() + File.separator + new File(uri.getPath()).getName();
                            onOssUploadStateListener.onOssUploadSuccess(ossPath);
                        }

                        @Override
                        public void onFailure(OSSRequest ossRequest, ClientException e, ServiceException e1) {
                            Log.e(TAG, String.format("%s:%s", "uploadOssServer-onFailure", e.getMessage()));
                            onOssUploadStateListener.onOssUploadError(e1);
                        }
                    });
                } catch (Exception exception) {
                    Log.e(TAG, String.format("%s:%s", "uploadFile", exception.getMessage()));
                    onOssUploadStateListener.onOssUploadError(exception);
                }
            }
        },new RxThrowableConsumer(){
            @Override
            public void handleThrowable(Throwable throwable) {
                super.handleThrowable(throwable);
                onOssUploadStateListener.onOssUploadError(null);
            }

            @Override
            public void handleConnectException() {
                super.handleConnectException();
                onOssUploadStateListener.onOssUploadError(null);
            }
        });
//        OkHttpUtils.post().url(Url.BASE + Url.PHONE_TOKEN)
//                .addHeader(Config.Headers.YC_PLAT_ID, "4")
//                .addHeader("yc-is-debug","1")
//                .addParams("businessType", ossBusinessType)
//                .addParams("handleType", ossHandleType)
//                .build().execute(new StringCallback() {
//            @Override
//            public void onError(Call call, Exception e, int i) {
//                Log.e(TAG, String.format("%s:%s", "uploadFile", e.getMessage()));
//                onOssUploadStateListener.onOssUploadError(e);
//            }
//
//            @Override
//            public void onResponse(String s, int i) {
//                try {
//                    BaseResponse<OssInfo> ossInfoBaseResponse = new Gson().fromJson(s, new TypeToken<BaseResponse<OssInfo>>() {
//                    }.getType());
//                    OssUtils.asyncUploadObject(context, ossInfoBaseResponse.getData(), uri, new OSSCompletedCallback() {
//                        @Override
//                        public void onSuccess(OSSRequest ossRequest, OSSResult ossResult) {
//                            Log.e(TAG, String.format("%s:%s", "uploadOssServer-onSuccess", new Gson().toJson(ossRequest)));
//                            String ossPath = ossInfoBaseResponse.getData().getHost() + File.separator + ossInfoBaseResponse.getData().getFolder() + File.separator + new File(uri.getPath()).getName();
//                            onOssUploadStateListener.onOssUploadSuccess(ossPath);
//                        }
//
//                        @Override
//                        public void onFailure(OSSRequest ossRequest, ClientException e, ServiceException e1) {
//                            Log.e(TAG, String.format("%s:%s", "uploadOssServer-onFailure", e.getMessage()));
//                            onOssUploadStateListener.onOssUploadError(e1);
//                        }
//                    });
//                } catch (Exception exception) {
//                    Log.e(TAG, String.format("%s:%s", "uploadFile", exception.getMessage()));
//                    onOssUploadStateListener.onOssUploadError(exception);
//                }
//
//            }
//        });
    }

    public interface OnOssUploadStateListener {
        void onOssUploadSuccess(String ossPath);

        void onOssUploadError(Exception e);
    }
}
