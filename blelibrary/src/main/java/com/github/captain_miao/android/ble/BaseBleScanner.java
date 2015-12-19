package com.github.captain_miao.android.ble;

import android.os.Handler;

import com.github.captain_miao.android.ble.constant.BleScanState;

/**
 * @author YanLu
 * @since 15/9/14
 */
public abstract class BaseBleScanner {
    public final static long defaultTimeout = 10 *1000;
    protected boolean isScanning;

    public abstract void onStartBleScan();
    public abstract void onStartBleScan(long timeoutMillis);

    public abstract void onStopBleScan();

    public abstract void onBleScanFailed(BleScanState scanState);

    protected Handler timeoutHandler  = new Handler();
    protected Runnable timeoutRunnable = new Runnable() {
        @Override
        public void run() {
            onStopBleScan();
            onBleScanFailed(BleScanState.SCAN_TIMEOUT);
        }
    };
}
