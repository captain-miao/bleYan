package com.dahuo.learn.lbe.blelibrary.constant;

/**
 * @author YanLu
 * @since  2015-09-14
 */

public enum BleConnectState {

    INITIALED(0, "蓝牙未连接"),
    SCANNING(1, "扫描中"),
    CONNECTING(2, "设备连接中"),
    CONNECTED(3, "设备已连接"),
    SERVICE_IS_COVERING(4, "服务发现中"),
    SERVICE_IS_COVERED(5, "已发现服务"),
    DISCONNECTING(6, "连接已断开"),
    DISCONNECTED(7, "连接已断开"),
    BLUETOOTH_OFF(8, "蓝牙关闭"),
    SERVICE_IS_NOT_COVERED(9, "服务发现失败");

    public boolean isBluetoothOff() {
        return this == BLUETOOTH_OFF;
    }
    public boolean isServiceDiscovered() {
        return this == SERVICE_IS_COVERED;
    }

    public boolean isConnecting() {
        return this.code > INITIALED.code && this.code < SERVICE_IS_COVERED.code;
    }

    public boolean needConnect() {
        return this.code == INITIALED.code || this.code == DISCONNECTING.code
                || this.code == DISCONNECTED.code;
    }

    public boolean isConnected() {
        return this.code == CONNECTED.code
                || this.code == SERVICE_IS_COVERING.code
                || this.code == SERVICE_IS_COVERED.code;
    }

    BleConnectState(int code, String message) {
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

    public static BleConnectState getBleConnectState(int code){
        switch (code){
            case 0:
                return BleConnectState.INITIALED;
            case 1:
                return BleConnectState.SCANNING;
            case 2:
                return BleConnectState.CONNECTED;
            case 3:
                return BleConnectState.CONNECTING;
            case 4:
                return BleConnectState.SERVICE_IS_COVERING;
            case 5:
                return BleConnectState.SERVICE_IS_COVERED;
            case 6:
                return BleConnectState.DISCONNECTING;
            case 7:
                return BleConnectState.DISCONNECTED;
            case 8:
                return BleConnectState.BLUETOOTH_OFF;
            case 9:
                return BleConnectState.SERVICE_IS_NOT_COVERED;
            default:
                return BleConnectState.INITIALED;
        }
    }
}
