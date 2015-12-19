package com.github.captain_miao.android.bluetoothletutorial.ble;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;

import com.github.captain_miao.android.ble.BaseBleService;
import com.github.captain_miao.android.ble.constant.BleConnectState;
import com.github.captain_miao.android.ble.constant.BleScanState;
import com.github.captain_miao.android.ble.utils.BleLog;
import com.github.captain_miao.android.bluetoothletutorial.app.AppLog;

import java.util.UUID;


/**
 * @author YanLu
 * @since 15/9/15
 */
public class AppBleService extends BaseBleService {
    private final static String TAG = AppBleService.class.getName();

    //发现服务之后，可以做一些初始化
    @Override
    public void onDiscoverServices(BluetoothGatt gatt) {
        //for test
        final BluetoothGattService gattService = gatt
				.getService(UUID.fromString("E54EAA50-371B-476C-99A3-74d267e3edbe"));
        if (gattService != null) {
            final BluetoothGattCharacteristic connectConf =
                    gattService.getCharacteristic(UUID.fromString("E54EAA55-371B-476C-99A3-74D267E3EDBE"));
            if(connectConf != null) {
                AppLog.i(TAG, "onDiscoverServices: connectConf()");
                connectConf.setValue(new byte[]{ (byte) 0x88 });
                write(connectConf);
            }
        }
    }

    /**
     * 如果通过BleService扫描设备,回调方法
     *
     * @param device     Identifies the remote device
     * @param rssi       The RSSI value for the remote device as reported by the
     *                   Bluetooth hardware. 0 if no RSSI value is available.
     * @param scanRecord The content of the advertisement record offered by
     */
    @Override
    public void onBleScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
        synchronized (this) {
            //状态已更新，停止扫描...
            if (mState != BleConnectState.SCANNING) {
                stopScan();
                return;
            }
            BleLog.i(TAG, "onScan " + device + " " + rssi);
            if (device != null && device.getName() != null) {

                if (rssi < -90) {//弱信号
                    return;
                }

                //是否 需要去连接~
                updateState(BleConnectState.CONNECTING);
                stopScan();

                BleLog.i(TAG, "connect " + device.getAddress());
                connectDevice(device);
            }
        }
    }

    /**
     *
     * @param scanState Error code (one of SCAN_FAILED_*) for scan failure.
     */
    @Override
    public void onBleScanFailed(BleScanState scanState) {

    }
}
