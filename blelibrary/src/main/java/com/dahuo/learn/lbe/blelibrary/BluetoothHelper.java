package com.dahuo.learn.lbe.blelibrary;

import android.bluetooth.BluetoothGatt;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import com.dahuo.learn.lbe.blelibrary.constant.BleConnectState;
import com.dahuo.learn.lbe.blelibrary.constant.BleConstants;
import com.dahuo.learn.lbe.blelibrary.constant.ConnectError;
import com.dahuo.learn.lbe.blelibrary.utils.BleLog;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author YanLu
 * @since  2015-09-14
 *
 * 目标：
 * 1. 蓝牙的各种状态 可以回调
 * 2. 加入超时机制
 * 3. 支持 写、notify、读属性值
 *
 */
public abstract class BluetoothHelper implements ServiceConnection, AppHandler.HandleMessageListener<BluetoothHelper> {
    private final static String TAG = BluetoothHelper.class.getName();
    private long connectTimeout = 15000;
    protected Context context;
    protected BleCallback mBleCallback;
    private static AppHandler<BluetoothHelper> appHandler;
    public final Map<UUID, BleCallback> mCallbacks = new HashMap<>();
	//蓝牙 处理，封装成 callback 方式
	private Messenger mReceiveMessenger;//从BleService接消息
	private Messenger mSendMessage = null;//发送消息给BleService
	public BleConnectState mState = BleConnectState.INITIALED;

    public ConnectCallback mConnCallback;

   	public TimeoutCallback  mConnectTimeout;
   	protected OnBindListener   mBindListener;
   	protected BluetoothGatt    mGatt;

    public BluetoothHelper(Context context) {
        this.context = context;
        appHandler = new AppHandler<>(this, this);
        mReceiveMessenger = new Messenger(appHandler);
    }
    public BluetoothHelper(Context context, BleCallback bleCallback) {
        this(context);
        this.mBleCallback = bleCallback;
    }

    public void setBleCallback(BleCallback bleCallback) {
        this.mBleCallback = bleCallback;
    }

    public abstract boolean bindService(OnBindListener bindListener);
    public abstract void unbindService();


    //连接设备
    public boolean connectDevice(String mac, final ConnectCallback connectCallback) {
        if(mState.isServiceDiscovered()){
            connectCallback.onConnectSuccess();
            return true;
        } else {
            Message msg = Message.obtain(null, BleConstants.MSG_CONTROL_ID_CONNECT_MAC);
            msg.obj = mac;
            this.mConnCallback = connectCallback;
            if (mSendMessage != null) {
                try {
                    appHandler.postDelayed(mConnectTimeout, connectTimeout);
                    mSendMessage.send(msg);
                    return true;
                } catch (RemoteException e) {
                    BleLog.w(TAG, "Lost connection to service" + e.toString());
                }
            }
            return false;
        }
    }

    public boolean stopScanDevice() {
   		return sendMsgWithoutSubscribe(BleConstants.MSG_STOP_SCAN);
   	}

    public boolean disconnectDevice() {
   		return sendMsgWithoutSubscribe(BleConstants.MSG_DEVICE_DISCONNECT);
   	}




    public boolean writeCharacteristic(UUID serviceUUID, UUID CharacteristicUUID, byte[] values) {

        Message msg = Message.obtain(null, BleConstants.MSG_CONTROL_ID_WRITE_CHARACTERISTIC);
        if (msg != null && mSendMessage != null) {
            msg.obj = values;
            Bundle bundle = new Bundle();
            bundle.putSerializable(BleConstants.BLE_MSG_SERVICE_UUID_KEY, serviceUUID);
            bundle.putSerializable(BleConstants.BLE_MSG_CHARACTERISTIC_UUID_KEY, CharacteristicUUID);
            msg.setData(bundle);
            try {
                //msg.replyTo = mReceiveMessenger;
                mSendMessage.send(msg);
                return true;
            } catch (RemoteException e) {
                BleLog.w(TAG, "Lost connection to service" + e.toString());
            }
        }
        return false;
    }



    public boolean sendWriteMsg(Message msg, BleCallback callback) {
        if (msg != null && mSendMessage != null) {
            try {
                mSendMessage.send(msg);
                if(callback != null) {
                    mCallbacks.put(callback.uuid, callback);
                }
                return true;
            } catch (RemoteException e) {
                BleLog.w(TAG, "Lost connection to service" + e.toString());
            }
        }
        return false;
    }
    public boolean sendReceiveNotifyData(Message msg, BleCallback callback) {
        if (msg != null && mSendMessage != null) {
            try {
                msg.replyTo = mReceiveMessenger;
                mSendMessage.send(msg);
                if(callback != null) {
                    mCallbacks.put(callback.uuid, callback);
                }
                return true;
            } catch (RemoteException e) {
                BleLog.w(TAG, "Lost connection to service" + e.toString());
            }
        }
        return false;
    }
    public boolean sendWriteMsg(Message msg) {

       // Message msg = Message.obtain(null, msgId);
        if (msg != null && mSendMessage != null) {
            try {
                //msg.replyTo = mReceiveMessenger;
                mSendMessage.send(msg);
                return true;
            } catch (RemoteException e) {
                BleLog.w(TAG, "Lost connection to service" + e.toString());
            }
        }
        return false;
    }

    public boolean sendMsgAndSubscribe(int msgId) {

        Message msg = Message.obtain(null, msgId);
        if (msg != null && mSendMessage != null) {
            try {
                msg.replyTo = mReceiveMessenger;
                mSendMessage.send(msg);
                return true;
            } catch (RemoteException e) {
                BleLog.w(TAG, "Lost connection to service" + e.toString());
            }
        }
        return false;
    }

    public boolean sendMsgWithoutSubscribe(int msgId) {

        Message msg = Message.obtain(null, msgId);
        if (msg != null && mSendMessage != null) {
            try {
                mSendMessage.send(msg);
                return true;
            } catch (RemoteException e) {
                BleLog.w(TAG, "Lost connection to service" + e.toString());
            }
        }
        return false;
    }


    @Override
    public void onHandleMessage(BluetoothHelper reference, Message msg) {
        Bundle data = msg.getData();
        switch (msg.what) {
            case BleConstants.BLE_MSG_ID_CONNECTION_STATE_CHANGED: {
                BleConnectState newStatus = (BleConnectState) msg.obj;
                if (mBleCallback != null) {
                    mBleCallback.onConnectionStateChange(mState.getCode(), newStatus.getCode());
                }
                mState = newStatus;
                if (mConnCallback != null) {
                    if (mState == BleConnectState.SERVICE_IS_COVERED) {
                        appHandler.removeCallbacks(mConnectTimeout);
                        mConnCallback.onConnectSuccess();
                    } else if (mState == BleConnectState.SERVICE_IS_NOT_COVERED) {
                        appHandler.removeCallbacks(mConnectTimeout);
                        mConnCallback.onConnectFailed(ConnectError.InvalidStatus);
                    } else if (mState == BleConnectState.DISCONNECTED) {
                        appHandler.removeCallbacks(mConnectTimeout);
                        mConnCallback.onConnectFailed(ConnectError.ConnectTimeout);
                        for (BleCallback callback : mCallbacks.values()) {
                            if (callback != null) {
                                callback.onFailed("蓝牙断开连接...");
                            }
                        }
                    }
                }
                break;
            }
            case BleConstants.MSG_BLE_ID_CHARACTERISTIC_WRITE: {
                //onCharacteristicWrite
                BleLog.i(TAG, "MSG_BLE_ID_CHARACTERISTIC_WRITE");
                if (data != null && mBleCallback != null) {
                    UUID uuid = (UUID) data.getSerializable(BleConstants.BLE_MSG_CHARACTERISTIC_UUID_KEY);
                    mBleCallback.onCharacteristicWrite(uuid, msg.arg1);
                    //BleCallback callback = mCallbacks.get(characteristicUUID);
                    //if (callback != null && callback.uuid.equals(characteristicUUID)) {
                    //    callback.onCharacteristicWrite(msg.arg1);
                    //}
                }
                break;
            }
            case BleConstants.MSG_BLE_ID_DESCRIPTOR_WRITE: {
                //onDescriptorWrite
                BleLog.i(TAG, "MSG_BLE_ID_CHARACTERISTIC_WRITE");
                if (data != null && mBleCallback != null) {
                    UUID uuid = (UUID) data.getSerializable(BleConstants.BLE_MSG_CHARACTERISTIC_UUID_KEY);
                    mBleCallback.onDescriptorWrite(uuid, msg.arg1);
                    //BleCallback callback = mCallbacks.get(characteristicUUID);
                    //if (callback != null && callback.uuid.equals(characteristicUUID)) {
                    //    callback.onDescriptorWrite(msg.arg1);
                    //}
                }
                break;
            }
            case BleConstants.MSG_BLE_ID_CHARACTERISTIC_NOTIFICATION: {
                //onCharacteristicChanged
                if (data != null && mBleCallback != null) {
                    UUID uuid = (UUID) data.getSerializable(BleConstants.BLE_MSG_CHARACTERISTIC_UUID_KEY);
                    mBleCallback.onCharacteristicNotification(uuid, (byte[]) msg.obj);
                    //BleCallback callback = mCallbacks.get(characteristicUUID);
                    //if (callback != null && callback.uuid.equals(characteristicUUID)) {
                    //    callback.onCharacteristicNotification(((byte[]) msg.obj));
                    //}
                }
                break;
            }
            case BleConstants.MSG_BLE_ID_CHARACTERISTIC_READ: {
                //onCharacteristicRead
                if (data != null && mBleCallback != null) {
                    UUID uuid = (UUID) data.getSerializable(BleConstants.BLE_MSG_CHARACTERISTIC_UUID_KEY);
                    mBleCallback.onCharacteristicRead(uuid, (byte[]) msg.obj);
                    //BleCallback callback = mCallbacks.get(characteristicUUID);
                    //if (callback != null && callback.uuid.equals(characteristicUUID)) {
                    //    callback.onCharacteristicRead(((byte[]) msg.obj));
                    //}
                }
                break;
            }
            case BleConstants.MSG_BLE_ID_DESCRIPTOR_READ: {
                //onDescriptorRead
                if (data != null && mBleCallback != null) {
                    UUID uuid = (UUID) data.getSerializable(BleConstants.BLE_MSG_CHARACTERISTIC_UUID_KEY);
                    mBleCallback.onDescriptorRead(uuid, (byte[]) msg.obj);
                    //BleCallback callback = mCallbacks.get(characteristicUUID);
                    //if (callback != null && callback.uuid.equals(characteristicUUID)) {
                    //    callback.onDescriptorRead(((byte[]) msg.obj));
                    //}
                }
                break;
            }
            case BleConstants.MSG_BLE_ID_RELIABLE_WRITE_COMPLETED: {
                //onReliableWriteCompleted
                if(mBleCallback != null){
                    mBleCallback.onReliableWriteCompleted(msg.arg1);
                }
//                for (BleCallback callback : mCallbacks.values()) {
//                    if(callback != null) {
//                        callback.onReliableWriteCompleted(msg.arg1);
//                    }
//                }
                break;
            }
            case BleConstants.MSG_BLE_ID_READ_REMOTE_RSSI: {
                //onReadRemoteRssi
                if(mBleCallback != null){
                    mBleCallback.onReadRemoteRssi(msg.arg2, msg.arg1);
                }
//                for (BleCallback callback : mCallbacks.values()) {
//                    if(callback != null) {
//                        callback.onReadRemoteRssi(msg.arg2, msg.arg1);
//                    }
//                }
                break;
            }
            case BleConstants.MSG_BLE_ID_MTU_CHANGED: {
                //onMtuChanged
                if(mBleCallback != null){
                    mBleCallback.onMtuChanged(msg.arg2, msg.arg1);
                }
//                for (BleCallback callback : mCallbacks.values()) {
//                    if(callback != null) {
//                        callback.onMtuChanged(msg.arg2, msg.arg1);
//                    }
//                }
                break;
            }
            case BleConstants.MSG_BLE_ID_SERVICES_DISCOVERED: {
                //onServicesDiscovered
                mGatt = (BluetoothGatt) msg.obj;
                if (mBleCallback != null) {
                    mBleCallback.onServicesDiscovered(mGatt, msg.arg1);
                }
                break;
            }
        }
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        BleLog.i(TAG, "mConnection onServiceConnected");
        mSendMessage = new Messenger(service);
        sendMsgAndSubscribe(BleConstants.MSG_CONTROL_ID_REGISTER);


        mConnectTimeout = new TimeoutCallback() {
   			@Override
   			public void onTimeout() {
                if(mConnCallback != null) {
                    mConnCallback.onConnectFailed(ConnectError.ConnectTimeout);
                }
   				stopScanDevice();
   			}
   		};


        if(mBindListener != null){
            mBindListener.onServiceConnected();
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        BleLog.i(TAG, "mConnection onServiceDisconnected");
        mSendMessage = null;
    }


    public boolean isBinded(){
        return mSendMessage != null;
    }


    public void release(){
        sendMsgAndSubscribe(BleConstants.MSG_CONTROL_ID_UNREGISTER);
        if(isBinded()) {
            try {
                unbindService();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
   	}

    public interface OnBindListener {

        void onServiceConnected();
    }
}
