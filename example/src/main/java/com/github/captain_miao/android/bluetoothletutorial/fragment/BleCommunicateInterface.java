package com.github.captain_miao.android.bluetoothletutorial.fragment;

/**
 * @author YanLu
 * @since 16/8/26
 */
public interface BleCommunicateInterface {
    void onAppendLog(String log);
    void onDataReceiver(byte[] data);
}
