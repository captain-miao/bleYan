package com.dahuo.learn.lbe.bluetoothletutorial.ble;

import android.content.Context;
import android.content.Intent;

import com.dahuo.learn.lbe.blelibrary.BleCallback;
import com.dahuo.learn.lbe.blelibrary.BluetoothHelper;

/**
 * @author YanLu
 * @since 15/9/15
 * <p/>
 * 目标：
 * 1. 蓝牙的各种状态 可以回调
 * 2. 加入超时机制
 * 3. 支持 写、notify、读属性值
 */
public class AppBluetoothHelper extends BluetoothHelper {
    private final static String TAG = AppBluetoothHelper.class.getName();


    public AppBluetoothHelper(Context context) {
        super(context);
    }

    @Override
    public boolean bindService(OnBindListener bindListener) {
        this.mBindListener = bindListener;
        return context.getApplicationContext().bindService(
                new Intent(context, AppBleService.class), this, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void unbindService() {
        context.getApplicationContext().unbindService(this);
    }

}
