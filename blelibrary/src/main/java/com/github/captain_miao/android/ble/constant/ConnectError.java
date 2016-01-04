package com.github.captain_miao.android.ble.constant;

/**
 * @author YanLu
 * @since  2015-09-14
 */

public enum ConnectError {

    ConnectTimeout(-3, "CONNECT_TIME_OUT"),
    InvalidStatus(-6, "BLUETOOTH_INVALID_STATUS");


    ConnectError(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}