package com.dahuo.learn.lbe.blelibrary;

/**
 * @author YanLu
 * @since 15/9/14
 */
public abstract class BaseBleScanner {
    protected boolean isScanning;

    public abstract void onStartBleScan();

    public abstract void onStopBleScan();
}
