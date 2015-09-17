package com.dahuo.learn.lbe.blelibrary;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.os.Build;

import com.dahuo.learn.lbe.blelibrary.utils.BleLog;

import java.util.List;

/**
 * @author YanLu
 * @since 15/9/14
 *
 * Android5.0以后 的蓝牙扫描
 */

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class LollipopBleScanner extends BaseBleScanner {
    private final static String TAG = LollipopBleScanner.class.getName();


    private BluetoothLeScanner mBluetoothScanner = null;
    private SimpleScanCallback mScanCallback = null;

    public LollipopBleScanner(SimpleScanCallback callback) {
        mScanCallback = callback;
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter != null) {
            mBluetoothScanner = bluetoothAdapter.getBluetoothLeScanner();
        }
    }


    @SuppressWarnings(value = {"deprecation"})
    @Override
    public void onStartBleScan() {
        if (mBluetoothScanner != null) {
            mBluetoothScanner.startScan(scanCallback);
            isScanning = true;
        } else {
            mScanCallback.onBleScanFailed(-1);//蓝牙 未开启
        }
        BleLog.i(TAG, "mBluetoothScanner.startScan()");
    }

    @SuppressWarnings(value = {"deprecation"})
    @Override
    public void onStopBleScan() {
        isScanning = false;
        if (mBluetoothScanner != null) {
            mBluetoothScanner.stopScan(scanCallback);
        }
    }


    private ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            BleLog.i(TAG, "onScanResult: " + callbackType + " ScanResult:" + result);
            if (result.getScanRecord() != null) {
                mScanCallback.onBleScan(result.getDevice(), result.getRssi(), result.getScanRecord().getBytes());
            }
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);
            BleLog.i(TAG, "onBatchScanResults()");
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
            BleLog.i(TAG, "onScanFailed: " + errorCode);
            mScanCallback.onBleScanFailed(errorCode);
            //onStopBleScan();
        }
    };
}
