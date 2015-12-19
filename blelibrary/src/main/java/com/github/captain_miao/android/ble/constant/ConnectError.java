package com.github.captain_miao.android.ble.constant;

/**
 * @author YanLu
 * @since  2015-09-14
 */

public enum ConnectError {

    InvalidMac(-1, "非法的mac地址"),
    ScanTimeout(-2, "扫描超时:未发现设备"),
    ConnectTimeout(-3, "蓝牙连接超时"),
    ServiceDiscoverTimeout(-4, "蓝牙服务发现超时"),
    CharNotFound(-7, "蓝牙连接失败"),
    InvalidStatus(-6, "蓝牙状态不对，尝试重启吧"),
    WriteTimeout(-7, "蓝牙超时"),
    ReadTimeout(-8, "蓝牙超时");


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


    public boolean hasConnected() {
        return this.getCode() > CharNotFound.getCode();
    }
}