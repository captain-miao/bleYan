package com.github.captain_miao.android.ble;

import android.bluetooth.BluetoothGatt;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import com.github.captain_miao.android.ble.constant.BleConnectState;
import com.github.captain_miao.android.ble.constant.BleConstants;
import com.github.captain_miao.android.ble.constant.ConnectError;
import com.github.captain_miao.android.ble.utils.BleLog;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author YanLu
 * @since  2015-09-14
 *
 * target：
 * 1. support ble connect status
 * 2. support message timeout
 * 3. support write、notify、read
 *
 */
public abstract class BluetoothHelper implements ServiceConnection, AppHandler.HandleMessageListener<BluetoothHelper> {
    private final static String TAG = BluetoothHelper.class.getName();
    private long connectTimeout = 15000;
    protected Context context;
    protected BleCallback mBleCallback;
    private static AppHandler<BluetoothHelper> appHandler;
    public final Map<UUID, BleCallback> mCallbacks = new HashMap<>();

	private Messenger mReceiveMessenger;//from BleService receive message
	private Messenger mSendMessage = null;//send message to BleService
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

    public boolean startScanDevice() {
   		return sendMsgWithoutSubscribe(BleConstants.MSG_CONTROL_ID_START_SCAN);
   	}
    public boolean stopScanDevice() {
   		return sendMsgWithoutSubscribe(BleConstants.MSG_CONTROL_ID_STOP_SCAN);
   	}

    public boolean disconnectDevice() {
   		return sendMsgWithoutSubscribe(BleConstants.MSG_CONTROL_ID_UNREGISTER);
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

    public boolean updateCharacteristicNotification(UUID serviceUUID, UUID characteristicUUID, UUID descriptorUUID, boolean enable) {

        Message msg = Message.obtain(null, BleConstants.MSG_CONTROL_ID_DESCRIPTOR_NOTIFICATION);
        if (msg != null && mSendMessage != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(BleConstants.BLE_MSG_SERVICE_UUID_KEY, serviceUUID);
            bundle.putSerializable(BleConstants.BLE_MSG_CHARACTERISTIC_UUID_KEY, characteristicUUID);
            bundle.putSerializable(BleConstants.BLE_MSG_DESCRIPTOR_UUID_KEY, descriptorUUID);
            bundle.putBoolean(BleConstants.BLE_MSG_ENABLE_KEY, enable);
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

    public boolean readFromCharacteristic(UUID serviceUUID, UUID CharacteristicUUID) {

        Message msg = Message.obtain(null, BleConstants.MSG_CONTROL_ID_READ_CHARACTERISTIC);
        if (msg != null && mSendMessage != null) {
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
                    if (mState == BleConnectState.SERVICE_IS_DISCOVERED) {
                        appHandler.removeCallbacks(mConnectTimeout);
                        mConnCallback.onConnectSuccess();
                    } else if (mState == BleConnectState.SERVICE_IS_NOT_DISCOVERED) {
                        appHandler.removeCallbacks(mConnectTimeout);
                        mConnCallback.onConnectFailed(ConnectError.InvalidStatus);
                    } else if (mState == BleConnectState.DISCONNECTED) {
                        appHandler.removeCallbacks(mConnectTimeout);
                        mConnCallback.onConnectFailed(ConnectError.ConnectTimeout);
                        for (BleCallback callback : mCallbacks.values()) {
                            if (callback != null) {
                                callback.onFailed("ble disconnected...");
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
                }
                break;
            }
            case BleConstants.MSG_BLE_ID_DESCRIPTOR_WRITE: {
                //onDescriptorWrite
                BleLog.i(TAG, "MSG_BLE_ID_CHARACTERISTIC_WRITE");
                if (data != null && mBleCallback != null) {
                    UUID uuid = (UUID) data.getSerializable(BleConstants.BLE_MSG_CHARACTERISTIC_UUID_KEY);
                    mBleCallback.onDescriptorWrite(uuid, msg.arg1);
                }
                break;
            }
            case BleConstants.MSG_BLE_ID_CHARACTERISTIC_NOTIFICATION: {
                //onCharacteristicChanged
                if (data != null && mBleCallback != null) {
                    UUID uuid = (UUID) data.getSerializable(BleConstants.BLE_MSG_CHARACTERISTIC_UUID_KEY);
                    mBleCallback.onCharacteristicNotification(uuid, (byte[]) msg.obj);
                }
                break;
            }
            case BleConstants.MSG_BLE_ID_CHARACTERISTIC_READ: {
                //onCharacteristicRead
                if (data != null && mBleCallback != null) {
                    UUID uuid = (UUID) data.getSerializable(BleConstants.BLE_MSG_CHARACTERISTIC_UUID_KEY);
                    mBleCallback.onCharacteristicRead(uuid, (byte[]) msg.obj);
                }
                break;
            }
            case BleConstants.MSG_BLE_ID_DESCRIPTOR_READ: {
                //onDescriptorRead
                if (data != null && mBleCallback != null) {
                    UUID uuid = (UUID) data.getSerializable(BleConstants.BLE_MSG_CHARACTERISTIC_UUID_KEY);
                    mBleCallback.onDescriptorRead(uuid, (byte[]) msg.obj);
                }
                break;
            }
            case BleConstants.MSG_BLE_ID_RELIABLE_WRITE_COMPLETED: {
                //onReliableWriteCompleted
                if(mBleCallback != null){
                    mBleCallback.onReliableWriteCompleted(msg.arg1);
                }
                break;
            }
            case BleConstants.MSG_BLE_ID_READ_REMOTE_RSSI: {
                //onReadRemoteRssi
                if(mBleCallback != null){
                    mBleCallback.onReadRemoteRssi(msg.arg2, msg.arg1);
                }
                break;
            }
            case BleConstants.MSG_BLE_ID_MTU_CHANGED: {
                //onMtuChanged
                if(mBleCallback != null){
                    mBleCallback.onMtuChanged(msg.arg2, msg.arg1);
                }
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
