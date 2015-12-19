package com.github.captain_miao.android.ble;

import android.bluetooth.BluetoothDevice;

import com.github.captain_miao.android.ble.constant.BleScanState;

/**
 * @author YanLu
 * @since 15/9/14
 */
public interface SimpleScanCallback {

    /**
     * Callback reporting an LE device found during a device scan initiated
     * by the BluetoothAdapter#startLeScan function.
     *
     * @param device Identifies the remote device
     * @param rssi The RSSI value for the remote device as reported by the
     *             Bluetooth hardware. 0 if no RSSI value is available.
     * @param scanRecord The content of the advertisement record offered by
     *                   the remote device.
     */
    void onBleScan(BluetoothDevice device, int rssi, byte[] scanRecord);

    /**
     * Callback when scan could not be started.
     *
     * @param scanState Error code (one of SCAN_FAILED_*) for scan failure.
     */
    void onBleScanFailed(BleScanState scanState);

}
