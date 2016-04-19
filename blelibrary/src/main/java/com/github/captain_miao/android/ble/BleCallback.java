package com.github.captain_miao.android.ble;

import android.bluetooth.BluetoothGatt;

import java.util.UUID;

/**
 * @author YanLu
 * @since 2015-09-14
 */
public abstract class BleCallback {
    public UUID uuid;

    protected BleCallback(UUID uuid) {
        this.uuid = uuid;
    }
    protected BleCallback() {
    }


    public void onFailed(UUID uuid, String msg){

    }
    public void onFailed(String msg){

    }


    //Callback triggered as a result of a remote characteristic notification.
    //BluetoothGattCallback#onCharacteristicChanged --> onCharacteristicNotification
    public void onCharacteristicNotification(UUID uuid, byte[] data) {

    }

    //Callback reporting the result of a characteristic read operation.
    //BluetoothGattCallback#onCharacteristicChanged
    public void onCharacteristicRead(UUID uuid, byte[] data) {

    }

    //Callback indicating the result of a characteristic write operation.
    //BluetoothGattCallback#onCharacteristicWrite
    public void onCharacteristicWrite(UUID uuid, int status) {

    }

    //Callback indicating when GATT client has connected/disconnected to/from a remote GATT server.
    //BluetoothGattCallback#onConnectionStateChange
    public void onConnectionStateChange(int status, int newStatus) {

    }

    //Callback reporting the result of a descriptor read operation.
    //BluetoothGattCallback#onDescriptorRead
    public void onDescriptorRead(UUID uuid, byte[] data) {

    }

    //Callback indicating the result of a descriptor write operation.
    //BluetoothGattCallback#onDescriptorWrite
    public void onDescriptorWrite(UUID uuid, int status) {

    }

    //Callback indicating the MTU for a given device connection has changed.
    //BluetoothGattCallback#onConnectionStateChange
    public void onMtuChanged(int mtu, int status) {

    }

    //Callback reporting the RSSI for a remote device connection.
    //BluetoothGattCallback#onReadRemoteRssi
    public void onReadRemoteRssi(int rssi, int status) {

    }

    //Callback invoked when a reliable write transaction has been completed.
    //BluetoothGattCallback#onReliableWriteCompleted
    public void onReliableWriteCompleted(int status) {

    }

    //Callback invoked when the list of remote services,
    // characteristics and descriptors for the remote device have been updated,
    // ie new services have been discovered.
    //BluetoothGattCallback#onServicesDiscovered
    public void onServicesDiscovered(BluetoothGatt gatt, int status) {

    }
}