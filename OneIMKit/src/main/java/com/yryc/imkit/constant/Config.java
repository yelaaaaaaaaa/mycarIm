package com.yryc.imkit.constant;

import android.os.Environment;

/**
 * @author : Mai_Xiao_Peng
 * @email : Mai_Xiao_Peng@163.com
 * @time : 2018/12/7 10:55
 * @describe :
 */

public class Config {

    public static final String GDMapURL = "https://restapi.amap.com/v3/staticmap?location=%s,%s&zoom=16&size=368*150&markers=-1,https://storage.51yryc.com/app-use/location-1x.png,:%s,%s&key=f6216b482a13ce56e1d8510022e5e8a7";

    public static final String OSS_VIDEO = "%s?x-oss-process=video/snapshot,t_%s,m_fast,ar_auto";

    /**
     * 存储目录配置
     */
    public interface Dir {
        String ROOT = Environment.getExternalStorageDirectory() + "/yryc/imkit/";

        String IMG = ROOT + "image/";

        String VIDEO = ROOT + "video/";

        String VOICE = ROOT + "voice/";

        String FILE = ROOT + "file/";

        String LOG = ROOT + "log/";
    }

    /**
     * 请求头
     */
    public interface Headers {
        //用户登录token
        String AUTHORIZATION = "Authorization";
        //设备Imei
        String DEVICEID = "device-id";
        //平台标识，1为安卓,2为iOS,3为Web,4无验证签名
        String YC_PLAT_ID = "yc-plat-id";
        //手机品牌
        String YC_PHONE_BRAND = "yc-phone-brand";
        //手机型号
        String YC_PHONE_MODEL = "yc-phone-model";
        //android sdk code
        String YC_SYSTEM = "yc-system";
        //时间戳
        String YC_TS = "yc-ts";
        //友盟token
        String YC_DEVICE_ID = "yc-device-id";
        //签名
        String YC_SIGN = "yc-sign";
        //uuid
        String YC_REQUEST_ID = "yc-request-id";
    }

}
