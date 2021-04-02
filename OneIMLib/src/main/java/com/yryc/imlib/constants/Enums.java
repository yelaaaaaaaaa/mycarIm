package com.yryc.imlib.constants;

public class Enums {

    /**
     * 连接状态
     */
    public enum ConnectionStatus {
        NETWORK_UNAVAILABLE(-1, "Network is unavailable."),
        CONNECTED(0, "Connect Success."),
        CONNECTING(1, "Connecting"),
        DISCONNECTED(2, "Disconnected"),
        KICKED_OFFLINE_BY_OTHER_CLIENT(3, "Login on the other device, and be kicked offline."),
        TOKEN_INCORRECT(4, "Token incorrect."),
        SERVER_INVALID(5, "Server invalid."),
        CONN_USER_BLOCKED(6, "User blocked by admin");

        private int code;
        private String msg;

        private ConnectionStatus(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public int getValue() {
            return this.code;
        }

        public String getMessage() {
            return this.msg;
        }
    }

    /**
     * 错误信息
     */
    public enum ErrorCode {
        UNKNOWN(-1, "unknown"),
        CONNECTED(0, "connected"),
        ERR_CONNECT_FAIL(1, "connect fail"),
        ERR_SEND_FAIL(2, "send fail"),
        ERR_ADD_WORD_FAIL(3, "add word fail"),
        ERR_ADD_WORD_SUCCESS(4, "add word fail"),
        ERR_DEL_WORD_FAIL(5, "delete word fail"),
        ERR_DEL_WORD_SUCCESS(6, "add word fail");

        private int code;
        private String msg;

        private ErrorCode(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public int getValue() {
            return this.code;
        }

        public String getMessage() {
            return this.msg;
        }

        public static ErrorCode valueOf(int code) {
            ErrorCode[] var1 = values();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                ErrorCode c = var1[var3];
                if (code == c.getValue()) {
                    return c;
                }
            }
            ErrorCode c = UNKNOWN;
            c.code = code;
            c.msg = code + "";
            return c;
        }
    }

    /**
     * 多媒体状态
     */
    public enum MediaStatus {
        UNPLAY(0, "unplay"),
        PLAY(1, "play");

        private int status;
        private String msg;

        private MediaStatus(int status, String msg) {
            this.status = status;
            this.msg = msg;
        }

        public int getValue() {
            return this.status;
        }

        public String getMessage() {
            return this.msg;
        }

        public static MediaStatus valueOf(int status) {
            MediaStatus[] var1 = values();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                MediaStatus c = var1[var3];
                if (status == c.getValue()) {
                    return c;
                }
            }
            MediaStatus c = UNPLAY;
            c.status = status;
            c.msg = status + "";
            return c;
        }
    }

    /**
     * 消息状态
     */
    public enum MessageStatus {
        SENDING(0, "sending"),
        SEND_SUCCESS(1, "send_success"),
        SEND_FAIL(2, "send_fail"),
        UNREAD(3, "unread"),
        READ(4, "read");

        private int status;
        private String msg;

        private MessageStatus(int status, String msg) {
            this.status = status;
            this.msg = msg;
        }

        public int getValue() {
            return this.status;
        }

        public String getMessage() {
            return this.msg;
        }

        public static MessageStatus valueOf(int status) {
            MessageStatus[] var1 = values();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                MessageStatus c = var1[var3];
                if (status == c.getValue()) {
                    return c;
                }
            }
            MessageStatus c = SENDING;
            c.status = status;
            c.msg = status + "";
            return c;
        }
    }
}
