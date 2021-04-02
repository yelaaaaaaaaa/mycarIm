package com.yryc.imlib.utils;

import com.yryc.imlib.constants.Enums;

/**
 * 一些公共方法
 */
public class OIMUtils {

    public static Enums.ConnectionStatus errorCodeToStatus(int errorCode) {
        Enums.ConnectionStatus status;
        switch(Enums.ErrorCode.valueOf(errorCode)) {
            case CONNECTED:
                status = Enums.ConnectionStatus.CONNECTED;
                break;
            case ERR_CONNECT_FAIL:
                status = Enums.ConnectionStatus.DISCONNECTED;
                break;

            default:
                status = Enums.ConnectionStatus.NETWORK_UNAVAILABLE;
        }

        return status;
    }
}
