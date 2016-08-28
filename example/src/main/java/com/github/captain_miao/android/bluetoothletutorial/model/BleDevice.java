package com.github.captain_miao.android.bluetoothletutorial.model;


import com.github.captain_miao.android.bluetoothletutorial.common.BaseModel;

/**
 * @author Yan Lu
 * @since 2015-07-23
 */
public class BleDevice extends BaseModel {
    public String name;
    public String aliasName;
    public String address;
    public String broadcast;
    public long   updateTime;// receive broadcast message time
    public int rssi;
    public boolean isConnected;
    public boolean isFavourite;

    public BleDevice() {
        this(null);
    }

    public BleDevice(String name, String addr) {
        this.name = name;
        this.address = addr;
        this.aliasName = "";
    }

    public BleDevice(String name, String address, int rssi, String broadcast) {
        this.name = name;
        this.address = address;
        this.rssi = rssi;
        this.broadcast = broadcast;
        this.updateTime = System.currentTimeMillis();
        this.aliasName = "";
    }
    public BleDevice(String name, String address, int rssi, String broadcast, boolean isFavourite) {
        this.name = name;
        this.address = address;
        this.rssi = rssi;
        this.broadcast = broadcast;
        this.updateTime = System.currentTimeMillis();
        this.isFavourite = isFavourite;
        this.aliasName = "";
    }

    public BleDevice(String addr) {
        this(null, addr);
    }
}
