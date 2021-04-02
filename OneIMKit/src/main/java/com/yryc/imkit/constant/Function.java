package com.yryc.imkit.constant;


import android.os.Parcel;
import android.os.Parcelable;

import com.yryc.imkit.R;


/**
 * @author : Mai_Xiao_Peng
 * @email : Mai_Xiao_Peng@163.com
 * @time : 2018/12/5 14:31
 * @describe :
 */


public enum Function implements Parcelable {

    ALBUM(1, "相册", R.drawable.ic_im_album, ""),
    SHOOTING(2, "拍摄", R.drawable.ic_im_shooting, ""),
    LANGUAGE(3, "常用语", R.drawable.ic_im_language, ""),
    LOCATION(4, "我的位置", R.drawable.ic_im_location, "");

    private Integer code;
    private String type;
    private Integer resId;
    private String remark;

    Function(Integer code, String type, Integer resId, String remark) {
        this.code = code;
        this.type = type;
        this.resId = resId;
        this.remark = remark;
    }

    Function(Parcel in) {
        if (in.readByte() == 0) {
            code = null;
        } else {
            code = in.readInt();
        }
        type = in.readString();
        if (in.readByte() == 0) {
            resId = null;
        } else {
            resId = in.readInt();
        }
        remark = in.readString();
    }

    public static final Creator<Function> CREATOR = new Creator<Function>() {
        @Override
        public Function createFromParcel(Parcel in) {
            Function value = Function.values()[in.readInt()];
            return value;
        }

        @Override
        public Function[] newArray(int size) {
            return new Function[size];
        }
    };

    public Integer getResId() {
        return resId;
    }

    public Integer getCode() {
        return code;
    }

    public String getType() {
        return type;
    }

    public String getRemark() {
        return remark;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (code == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(code);
        }
        dest.writeString(type);
        if (resId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(resId);
        }
        dest.writeString(remark);
    }
}
