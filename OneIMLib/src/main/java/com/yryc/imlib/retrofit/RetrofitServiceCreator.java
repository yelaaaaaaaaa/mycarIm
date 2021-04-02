package com.yryc.imlib.retrofit;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.ConnectionPool;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author : sklyand
 * @email : zhengdengyao@51yryc.com
 * @time : 2019/7/22 09:13
 * @describe ：
 */
public class RetrofitServiceCreator {
    public static final String API_BASE_URL = "http://gateway.devproxy.51yryc.com";

    public static <S> S createService(final Context context, Class<S> serviceClass) {
        OkHttpClient client = provideOkHttpClient();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//RxJava 适配器
                .client(client)
                .build();

        return retrofit.create(serviceClass);
    }
    public static <S> S createService( Class<S> serviceClass) {
        OkHttpClient client = provideOkHttpClient();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//RxJava 适配器
                .client(client)
                .build();

        return retrofit.create(serviceClass);
    }
    public static OkHttpClient provideOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        builder.addInterceptor(chain -> {

            boolean isEncrypt = false;
            //非加密处理
            Request request = chain.request();
            Request.Builder builderNew = request.newBuilder();
            RequestBody requestBody = request.body();
            builderNew.addHeader("yc-is-debug", "1");
            //返回数据解密
            Response originalResponse = chain.proceed(builderNew.build());
            return originalResponse;
        });
        //设置超时
        builder.connectTimeout(10, TimeUnit.SECONDS);
        builder.readTimeout(10, TimeUnit.SECONDS);
        builder.writeTimeout(10, TimeUnit.SECONDS);
        builder.connectionPool(new ConnectionPool(8, 15, TimeUnit.SECONDS));
        //错误重连
        builder.retryOnConnectionFailure(true);
        return builder.build();
    }
    /**
     * http 日志拦截器，打印日志
     */
    public static class PrintLogInterceptor implements Interceptor {



        @Override
        public Response intercept(Chain chain) throws IOException {
            String token = chain.request().header("token");
            String urid = chain.request().header("urid");
            String body = bodyToString(chain.request());
            String url = chain.request().url().toString();
            if (isJson(body)){
                printJson("ClientRequestBody",body,"request ,"+url);
            }
//            Log.d("ClientRequestBody", String.format(Locale.ENGLISH,
//                    " body: %s",
//                    body));
            Response resp = chain.proceed(chain.request());
//            LogUtils.d("ClientRequest", String.format(Locale.ENGLISH,
//                    "token: %s, device-id: %s, url: %s, uuid: %s, ",
//                    token, AppLogic.getInstance().getDeviceId(), url,
//                    urid ));
            Log.d("ClientRequest", String.format(Locale.ENGLISH,
                    "token: %s, device-id: %s, url: %s, uuid: %s, ",
                    "", "", url,
                    ""));
            if (resp.isSuccessful() ){
                if (isJson(bodyToString(resp))){
                    printJson("ClientResponseBody",bodyToString(resp),"resp ,"+url);
                }
            }else {
                Log.d("ClientRequestError", String.format(Locale.ENGLISH,
                        " httpError code %d",
                        resp.code()));
            }
            return resp;
        }
    }

    private static JSONObject getJson(String content){
        try {
            return new JSONObject(content);
        } catch (Exception e) {
            return null;
        }
    }

    private static boolean isJson(String content){
        try {
            JsonParser jsonParser = new JsonParser();
            JsonElement jsonElement = jsonParser.parse(content);
            return  jsonElement.isJsonObject();
        } catch (Exception e) {
            return false;
        }
    }
    private static String bodyToString(final Request request) {
        if (null != request.body()) {
            Buffer buffer = new Buffer();
            try {
                request.body().writeTo(buffer);
                //buffer.readString(Charset.defaultCharset());
                return buffer.readString(Charset.defaultCharset());
            } catch (Exception ex) {
                return "not work";
            }
        } else {
            return "";
        }
    }

    private static String bodyToString(final Response resp) {
        if (null != resp.body()) {
            try {
                Buffer buffer = resp.body().source().buffer().clone();
                return buffer.readString(Charset.defaultCharset());
            } catch (Exception ex) {
                return "not work";
            }
        } else {
            return "";
        }
    }

    private static void printLine(String tag, boolean isTop) {
        if (isTop) {
            Log.d(tag, "╔═══════════════════════════════════════════════════════════════════════════════════════");
        } else {
            Log.d(tag, "╚═══════════════════════════════════════════════════════════════════════════════════════");
        }
    }
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    public static void printJson(String tag, String msg, String headString) {

        String message;

        try {
            if (msg.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(msg);
                message = jsonObject.toString(4);//最重要的方法，就一行，返回格式化的json字符串，其中的数字4是缩进字符数
            } else if (msg.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(msg);
                message = jsonArray.toString(4);
            } else {
                message = msg;
            }
        } catch (JSONException e) {
            message = msg;
        }

        printLine(tag, true);
        message = headString + LINE_SEPARATOR + message;
        String[] lines = message.split(LINE_SEPARATOR);
        for (String line : lines) {
            Log.d(tag, "║ " + line);
        }
        printLine(tag, false);

    }
}
