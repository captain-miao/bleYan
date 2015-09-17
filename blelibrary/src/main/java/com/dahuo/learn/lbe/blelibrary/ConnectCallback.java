package com.dahuo.learn.lbe.blelibrary;


import com.dahuo.learn.lbe.blelibrary.constant.ConnectError;

public abstract class ConnectCallback {
    public abstract void onConnectSuccess();

    public abstract void onConnectFailed(ConnectError error);
}