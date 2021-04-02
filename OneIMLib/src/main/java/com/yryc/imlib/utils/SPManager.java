package com.yryc.imlib.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;

/**
 * SP相关操作
 */
public class SPManager {

    //存放的配置信息文件名称
    private final static String OIM_BASE_CONFIG = "oim_base_config";
    // 相关的sp key
    public final static String SP_KEY_DEVICE_ID = "deviceId";
    public final static String SP_KEY_ANDROID_ID = "android_id";
    public final static String SP_KEY_APP_KEY = "appKey";
    public final static String SP_KEY_TOKEN = "token";
    public final static String SP_KEY_USER_ID = "userId";

    public final static String SP_IS_DEBUG = "is_debug";
    /**
     * 最近一次聊天记录信息
     */
    public final static String SP_LAST_CHAT_INFO = "chat_info";
    public final static String SP_KEYBOARD_HEIGHT = "keyboard_height";
    /**
     * 语音播放模式
     */
    public final static String SP_VOICE_MODE = "voice_mode";

    private Context mContext;
    private SharedPreferences mSettings;
    private SharedPreferences.Editor mEditor;

    private static SPManager spManager;

    public synchronized static SPManager getInstance(Context context) {
        if (spManager == null) {
            spManager = new SPManager(context);
        }
        return spManager;
    }


    private SPManager(Context context) {
        mContext = context;
        mSettings = mContext.getSharedPreferences(OIM_BASE_CONFIG, Context.MODE_PRIVATE);
        mEditor = mSettings.edit();
    }

    /**
     * 获取配置信息内容
     *
     * @param key          名称参数
     * @param defaultValue 默认值
     * @return
     */
    public Object get(String key, Object defaultValue) {

        if (defaultValue instanceof String) {
            return (mSettings != null) ? mSettings.getString(key, String.valueOf(defaultValue)) : String.valueOf(defaultValue);
        } else if (defaultValue instanceof Integer) {
            return (mSettings != null) ? mSettings.getInt(key, (Integer) defaultValue) : defaultValue;
        } else if (defaultValue instanceof Long) {
            return (mSettings != null) ? mSettings.getLong(key, (Long) defaultValue) : defaultValue;
        } else if (defaultValue instanceof Boolean) {
            return (mSettings != null) ? mSettings.getBoolean(key, (Boolean) defaultValue) : defaultValue;
        } else if (defaultValue instanceof Float) {
            return (mSettings != null) ? mSettings.getFloat(key, (Float) defaultValue) : defaultValue;
        }

        return defaultValue;
    }

    /**
     * 获取配置信息内容
     *
     * @param key   名称参数
     * @param clazz 默认值
     * @return
     */
    public <T> T get(String key, Class<T> clazz) {
        if (mSettings == null) {
            return null;
        }

        String objStr = mSettings.getString(key, "");
        if (TextUtils.isEmpty(objStr)) {
            return null;
        }

        Gson gson = new Gson();
        return gson.fromJson(objStr, clazz);

    }

    /**
     * 存放配置信息内容
     *
     * @param key   名称参数
     * @param value 值信息(基本数据类型)
     * @return
     */
    public boolean set(String key, Object value) {
        return putObject(key, value);
    }

    /**
     * 清除对应的缓存
     *
     * @param key
     * @return
     */
    public boolean remove(String key) {
        mEditor.remove(key);
        return mEditor.commit();
    }

    private boolean putObject(String key, Object value) {
        if (mEditor == null)
            return false;
        if (value instanceof String) {
            mEditor.putString(key, String.valueOf(value));
            return mEditor.commit();
        } else if (value instanceof Integer) {
            mEditor.putInt(key, Integer.valueOf(String.valueOf(value)));
            return mEditor.commit();
        } else if (value instanceof Long) {
            mEditor.putLong(key, Long.valueOf(String.valueOf(value)));
            return mEditor.commit();
        } else if (value instanceof Boolean) {
            mEditor.putBoolean(key, Boolean.valueOf(String.valueOf(value)));
            return mEditor.commit();
        } else if (value instanceof Float) {
            mEditor.putFloat(key, Float.valueOf(String.valueOf(value)));
            return mEditor.commit();

        } else {
            Gson gson = new Gson();
            String jsonStr = gson.toJson(value);
            mEditor.putString(key, jsonStr);
            return mEditor.commit();
        }

    }

    public boolean contain(String key) {
        return mSettings.contains(key);
    }
}
