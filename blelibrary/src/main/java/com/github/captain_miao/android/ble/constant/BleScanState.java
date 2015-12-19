package com.github.captain_miao.android.ble.constant;

/**
 * @author YanLu
 * @since  2015-09-14
 */

public enum BleScanState {

    SCAN_TIMEOUT(-2, "SCAN_SUCCESS_TIME_OUT"),
    BLUETOOTH_OFF(-1, "BLUETOOTH_OFF"),
    SCAN_SUCCESS(0, "SCAN_SUCCESS"),
    SCAN_FAILED_ALREADY_STARTED(1, "SCAN_FAILED_ALREADY_STARTED"),
    SCAN_FAILED_APPLICATION_REGISTRATION_FAILED(2, "SCAN_FAILED_APPLICATION_REGISTRATION_FAILED"),
    SCAN_FAILED_INTERNAL_ERROR(3, "SCAN_FAILED_INTERNAL_ERROR"),
    SCAN_FAILED_FEATURE_UNSUPPORTED(4, "SCAN_FAILED_FEATURE_UNSUPPORTED"),
    SCAN_FAILED_OUT_OF_HARDWARE_RESOURCES(5, "SCAN_FAILED_OUT_OF_HARDWARE_RESOURCES");


    BleScanState(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private int code;
    private String message;

    public int getCode() {
        return code;
    }


    public String getMessage() {
        return message;
    }

    public static BleScanState newInstance(int code) {
        switch (code) {
            case -2:
                return BleScanState.SCAN_TIMEOUT;
            case -1:
                return BleScanState.BLUETOOTH_OFF;

            case 1:
                return BleScanState.SCAN_FAILED_ALREADY_STARTED;
            case 2:
                return BleScanState.SCAN_FAILED_APPLICATION_REGISTRATION_FAILED;
            case 3:
                return BleScanState.SCAN_FAILED_INTERNAL_ERROR;
            case 4:
                return BleScanState.SCAN_FAILED_FEATURE_UNSUPPORTED;
            case 5:
                return BleScanState.SCAN_FAILED_OUT_OF_HARDWARE_RESOURCES;
            default:
                return BleScanState.SCAN_SUCCESS;
        }
    }
}
