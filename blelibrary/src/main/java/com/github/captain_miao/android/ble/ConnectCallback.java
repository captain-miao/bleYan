package com.github.captain_miao.android.ble;


import com.github.captain_miao.android.ble.constant.ConnectError;

public abstract class ConnectCallback {
    public abstract void onConnectSuccess();

    public abstract void onConnectFailed(ConnectError error);
}