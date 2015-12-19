package com.github.captain_miao.android.ble;

public abstract class TimeoutCallback implements Runnable {
    public abstract void onTimeout();

    @Override
    public void run() {
        onTimeout();
    }
}