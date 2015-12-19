package com.github.captain_miao.android.ble.constant;

/**
 * @author YanLu
 * @since  2015-09-14
 */

public enum BleConnectState {

    INITIALED(0, "BLE initialed"),
    SCANNING(1, "Scanning"),
    CONNECTING(2, "Connecting"),
    CONNECTED(3, "Connect"),
    SERVICE_IS_DISCOVERING(4, "Services discovering"),
    SERVICE_IS_DISCOVERED(5, "Services discovered"),
    DISCONNECTING(6, "Disconnecting"),
    DISCONNECTED(7, "Disconnected"),
    BLUETOOTH_OFF(8, "Bluetooth_off"),
    SERVICE_IS_NOT_DISCOVERED(9, "Services discover failed");

    public boolean isBluetoothOff() {
        return this == BLUETOOTH_OFF;
    }
    public boolean isServiceDiscovered() {
        return this == SERVICE_IS_DISCOVERED;
    }

    public boolean isConnecting() {
        return this.code > INITIALED.code && this.code < SERVICE_IS_DISCOVERED.code;
    }

    public boolean needConnect() {
        return this.code == INITIALED.code || this.code == DISCONNECTING.code
                || this.code == DISCONNECTED.code;
    }

    public boolean isConnected() {
        return this.code == CONNECTED.code
                || this.code == SERVICE_IS_DISCOVERING.code
                || this.code == SERVICE_IS_DISCOVERED.code;
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
                return BleConnectState.SERVICE_IS_DISCOVERING;
            case 5:
                return BleConnectState.SERVICE_IS_DISCOVERED;
            case 6:
                return BleConnectState.DISCONNECTING;
            case 7:
                return BleConnectState.DISCONNECTED;
            case 8:
                return BleConnectState.BLUETOOTH_OFF;
            case 9:
                return BleConnectState.SERVICE_IS_NOT_DISCOVERED;
            default:
                return BleConnectState.INITIALED;
        }
    }
}
