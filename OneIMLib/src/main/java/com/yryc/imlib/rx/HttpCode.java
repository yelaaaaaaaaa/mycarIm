package com.yryc.imlib.rx;

/**
 * @author : sklyand
 * @email : zhengdengyao@51yryc.com
 * @time : 2019/7/18 16:21
 * @describe ：
 */
public enum HttpCode {
    HTTP_OK(0, "请求成功"),
    HTTP_EXPIRED_401(-401, "token过期"),
    HTTP_EXPIRED_402(-402, "token过期"),
    HTTP_USER_ALREADY_REGISTERED(-12201020, "用户己注册"),
    HTTP_NULL(1000, "服务数据返回null"),
    HTTP_OK_NULL(1001, "请求成功无数据返回"),
    HTTP_UNKNOWN(10000, "未知错误");

    private Integer code;

    private String message;

    HttpCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
