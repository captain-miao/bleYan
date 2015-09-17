package com.dahuo.learn.lbe.bluetoothletutorial.ble;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;

import com.dahuo.learn.lbe.blelibrary.BaseBleService;
import com.dahuo.learn.lbe.blelibrary.constant.BleConnectState;
import com.dahuo.learn.lbe.blelibrary.constant.BleConstants;
import com.dahuo.learn.lbe.blelibrary.utils.BleLog;
import com.dahuo.learn.lbe.blelibrary.utils.BleUtils;
import com.dahuo.learn.lbe.blelibrary.utils.HexUtil;


/**
 * @author YanLu
 * @since 15/9/15
 */
public class AppBleService extends BaseBleService {
    private final static String TAG = AppBleService.class.getName();

    //发现服务之后，写入保持长连接 for We Coach
    @Override
    public void onDiscoverServices(BluetoothGatt gatt) {
        final BluetoothGattService weCoachService = gatt
				.getService(BleConstants.UUID_WECOACH_SERVICE);
        if (weCoachService != null) {
            final BluetoothGattCharacteristic connectConf = weCoachService.getCharacteristic(BleConstants.UUID_CONPRO_CONF2);
            if(connectConf != null) {
                BleLog.i(TAG, "onDiscoverServices: connectConf()");
                connectConf.setValue(BleConstants.CONPROMODEL);
                write(connectConf);
            }
            else {
                //没有服务 异常了
                BleUtils.refreshDeviceCache(gatt);
            }
        } else {
            //没有服务 异常了
            BleUtils.refreshDeviceCache(gatt);
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
            if (device != null
                    && device.getName() != null
                    && device.getName().equals(BleConstants.DEVICE_NAME)) {

                if (rssi < -90) {
                    return;
                }

                int len = scanRecord.length;
                if (len <= (10 + 12)) {
                    return;
                }
                // 获取广播中的mac地址
                String scanHex = HexUtil.encodeHexStr(scanRecord);
                String mac = scanHex.substring(10, 22);
                // 如果mac地址和保存的地址不一致，跳过
                //if (!targetDeviceMac.equalsIgnoreCase(mac)) {
                //    return;
                //}
                updateState(BleConnectState.CONNECTING);
                stopScan();

                BleLog.i(TAG, "connect " + device.getAddress());
                connectDevice(device);
            }
        }
    }

    /**
     *
     * @param errorCode Error code (one of SCAN_FAILED_*) for scan failure.
     */
    @Override
    public void onBleScanFailed(int errorCode) {

    }
}
