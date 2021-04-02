package com.yryc.imlib.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.yryc.imlib.constants.Constants;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.SecureRandom;

public class DeviceUtils {

    public static String getDeviceId(Context context, String appKey) {
        SPManager sp = SPManager.getInstance(context);
        String deviceId = (String) sp.get(SPManager.SP_KEY_DEVICE_ID, "");
        if (TextUtils.isEmpty(deviceId)) {
            String[] params = new String[]{getDeviceId(context), appKey, context.getPackageName()};
            deviceId = ShortMD5(params);

            sp.set(SPManager.SP_KEY_DEVICE_ID, deviceId);
        }

        return deviceId;
    }

    public static String getDeviceId(Context context) {
        SPManager sp = SPManager.getInstance(context);
        String deviceId = (String) sp.get(SPManager.SP_KEY_ANDROID_ID, "");
        if (TextUtils.isEmpty(deviceId)) {
            deviceId = Settings.Secure.getString(context.getApplicationContext().getContentResolver(), "android_id");
            if (TextUtils.isEmpty(deviceId)) {
                SecureRandom random = new SecureRandom();
                deviceId = (new BigInteger(64, random)).toString(16);
            }

            sp.set(SPManager.SP_KEY_ANDROID_ID, deviceId);
        }

        return deviceId;
    }

    public static String getDeviceIMEI(Context context) {
        return getDeviceId(context);
    }

    public static String ShortMD5(String... args) {
        try {
            StringBuilder builder = new StringBuilder();
            String[] var2 = args;
            int var3 = args.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                String arg = var2[var4];
                builder.append(arg);
            }

            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(builder.toString().getBytes());
            byte[] mds = mdInst.digest();
            mds = Base64.encode(mds, 0);
            String result = new String(mds);
            result = result.replace("=", "").replace("+", "-").replace("/", "_").replace("\n", "");
            return result;
        } catch (Exception var6) {
            return "";
        }
    }

    public static String getPhoneInformation(Context context) {
        String MCCMNC = "";

        try {
            TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
            MCCMNC = telephonyManager.getNetworkOperator();
        } catch (SecurityException var8) {
            Log.e("DeviceUtils", "SecurityException!!!");
        }

        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        String network = "";
        NetworkInfo networkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
        if (networkInfo != null) {
            network = networkInfo.getTypeName();
        }

        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (manufacturer == null) {
            manufacturer = "";
        }

        if (model == null) {
            model = "";
        }

        String devInfo = manufacturer + "|";
        devInfo = devInfo + model;
        devInfo = devInfo + "|";
        devInfo = devInfo + String.valueOf(Build.VERSION.SDK_INT);
        devInfo = devInfo + "|";
        devInfo = devInfo + network;
        devInfo = devInfo + "|";
        devInfo = devInfo + MCCMNC;
        devInfo = devInfo + "|";
        devInfo = devInfo + context.getPackageName();
        devInfo = devInfo.replace("-", "_");

        return devInfo;
    }

    public static String getNetworkType(Context context) {
        String strNetworkType = "";
        NetworkInfo networkInfo = ((ConnectivityManager)context.getSystemService("connectivity")).getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            if (networkInfo.getType() == 1) {
                strNetworkType = "WIFI";
            } else if (networkInfo.getType() == 0) {
                String _strSubTypeName = networkInfo.getSubtypeName();
                int networkType = networkInfo.getSubtype();
                switch(networkType) {
                    case Constants.NETWORK_TYPE_GPRS:
                    case Constants.NETWORK_TYPE_EDGE:
                    case Constants.NETWORK_TYPE_CDMA:
                    case Constants.NETWORK_TYPE_1xRTT:
                    case Constants.NETWORK_TYPE_IDEN:
                        strNetworkType = "2G";
                        break;
                    case Constants.NETWORK_TYPE_UMTS:
                    case Constants.NETWORK_TYPE_EVDO_0:
                    case Constants.NETWORK_TYPE_EVDO_A:
                    case Constants.NETWORK_TYPE_HSDPA:
                    case Constants.NETWORK_TYPE_HSUPA:
                    case Constants.NETWORK_TYPE_HSPA:
                    case Constants.NETWORK_TYPE_EVDO_B:
                    case Constants.NETWORK_TYPE_EHRPD:
                    case Constants.NETWORK_TYPE_HSPAP:
                        strNetworkType = "3G";
                        break;
                    case Constants.NETWORK_TYPE_LTE:
                        strNetworkType = "4G";
                        break;
                    default:
                        if (!_strSubTypeName.equalsIgnoreCase("TD-SCDMA") && !_strSubTypeName.equalsIgnoreCase("WCDMA") && !_strSubTypeName.equalsIgnoreCase("CDMA2000")) {
                            strNetworkType = _strSubTypeName;
                        } else {
                            strNetworkType = "3G";
                        }
                }
            }
        }

        return strNetworkType;
    }
}
