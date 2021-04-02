package com.yryc.imlib.rx;


import android.net.ParseException;
import android.util.MalformedJsonException;

import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.functions.Consumer;
import retrofit2.HttpException;


public class RxThrowableConsumer implements Consumer<Throwable> {


    public RxThrowableConsumer() {
    }


    @Override
    public void accept(Throwable throwable) throws Exception {
        throwable.printStackTrace();
        if (throwable instanceof SocketTimeoutException || throwable instanceof SocketException) {

            handleConnectException();
        } else if (throwable instanceof ConnectException || throwable instanceof UnknownHostException || throwable instanceof HttpException) {

            handleConnectException();
        } else if (throwable instanceof JsonParseException
                || throwable instanceof JSONException
                || throwable instanceof ParseException || throwable instanceof MalformedJsonException) {  //解析数据错误

            handleConnectException();
        } else {
            handleThrowable(throwable);
        }
    }

    public void handleThrowable(Throwable throwable) {
        //ToastUtils.showShortToastSafe(throwable.getMessage());
    }

    public void handleConnectException() {
    }

}
