package com.github.captain_miao.android.ble;

import android.content.Context;
import android.os.Build;

/**
 * @author YanLu
 * @since 15/9/14
 */

public class BleScanner {
    private final static String TAG = BleScanner.class.getName();
    public BaseBleScanner bleScanner;

    public BleScanner(Context context, final SimpleScanCallback callback) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            bleScanner = new LollipopBleScanner(callback);
        } else {
            bleScanner = new JellyBeanBleScanner(context, callback);
        }
    }


    public boolean isScanning(){
        return bleScanner.isScanning;
    }

    public void startBleScan(){
        bleScanner.onStartBleScan();
    }

    public void startBleScan(long timeoutMillis){
        bleScanner.onStartBleScan(timeoutMillis);
    }
    public void stopBleScan(){
        bleScanner.onStopBleScan();
    }

}
