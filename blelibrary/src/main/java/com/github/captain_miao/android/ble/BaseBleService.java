package com.github.captain_miao.android.ble;


import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import com.github.captain_miao.android.ble.constant.BleConnectState;
import com.github.captain_miao.android.ble.constant.BleConstants;
import com.github.captain_miao.android.ble.utils.BleLog;
import com.github.captain_miao.android.ble.utils.BleUtils;
import com.github.captain_miao.android.ble.utils.HexUtil;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class BaseBleService extends Service implements SimpleScanCallback{
	private final static String TAG = BaseBleService.class.getName();

	protected final BleServiceHandle mHandler;
	private final Messenger mMessenger;

	private BluetoothGatt mGatt = null;
	public BleConnectState mState = BleConnectState.INITIALED;
	private BleScanner mBleScanner;

	//消息队列
	private final List<Messenger> mClients = new LinkedList<>();
	protected static final Queue<Object> sWriteQueue = new ConcurrentLinkedQueue<>();
	private static boolean sIsWriting = false;

	//发现服务之后，可以做一些初始化操作
	public abstract void onDiscoverServices(final BluetoothGatt gatt);


	private BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
		@Override
		public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
			BleLog.i(TAG, "onConnectionStateChange: State = " + BleUtils.getBleConnectStatus(status)
					+ " newState = " + BleUtils.getBleConnectStatus(newState));

			if (newState == BluetoothProfile.STATE_CONNECTED) {
				updateState(BleConnectState.CONNECTED);
				//开始发现服务
				BleLog.i(TAG, "gatt.discoverServices()");
				gatt.discoverServices();
			} else if (newState == BluetoothProfile.STATE_CONNECTING) {
				updateState(BleConnectState.CONNECTING);
			} else if (newState == BluetoothProfile.STATE_DISCONNECTING) {
				updateState(BleConnectState.DISCONNECTING);
			} else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
				//断开了，需要做什么处理？
				sIsWriting = false;
				sWriteQueue.clear();
				updateState(BleConnectState.DISCONNECTED);
            }
		}

		@Override
		public void onServicesDiscovered(BluetoothGatt gatt, int status) {

			if (status == BluetoothGatt.GATT_SUCCESS) {
				onDiscoverServices(gatt);
				//需要返回 gatt
				updateState(BleConnectState.SERVICE_IS_DISCOVERED);
			} else {
				BleUtils.refreshDeviceCache(mGatt);
				//失败 需要做何处理 129
				if(mState != BleConnectState.SERVICE_IS_NOT_DISCOVERED) {
                    updateState(mState);
                }
			}

			//MSG_BLE_ID_SERVICES_DISCOVERED
			Message msg = Message.obtain();
			msg.what = BleConstants.MSG_BLE_ID_SERVICES_DISCOVERED;
			msg.arg1 = status;
       		msg.obj = gatt;
			notifyAllBleClients(msg);
			BleLog.i(TAG, "onServicesDiscovered: " + BleUtils.getGattStatus(status));
		}

		@Override
		public void onCharacteristicWrite(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic, int status) {
			BleLog.i(TAG, "onCharacteristicWrite: " + BleUtils.getGattStatus(status));
			UUID uuid = characteristic.getUuid();
			sendBleMessage(BleConstants.MSG_BLE_ID_CHARACTERISTIC_WRITE, status, uuid);
			onNextWrite();
		}

		@Override
		public void onDescriptorWrite(BluetoothGatt gatt,
				BluetoothGattDescriptor descriptor, int status) {
			BleLog.i(TAG, "onDescriptorWrite: " + BleUtils.getGattStatus(status));
			UUID uuid = descriptor.getUuid();

			sendBleMessage(BleConstants.MSG_BLE_ID_DESCRIPTOR_WRITE, status, uuid);
			onNextWrite();
		}

		@Override
		public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
			final byte[] data = characteristic.getValue();
			BleLog.i(TAG, "onCharacteristicChanged: " + HexUtil.encodeHexStr(data));
			UUID uuid = characteristic.getUuid();

			sendBleMessage(BleConstants.MSG_BLE_ID_CHARACTERISTIC_NOTIFICATION, BluetoothGatt.GATT_SUCCESS, data, uuid);
			onNextWrite();
		}

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
			final byte[] data = characteristic.getValue();
			BleLog.i(TAG, "onCharacteristicRead: " + HexUtil.encodeHexStr(data));
			UUID uuid = characteristic.getUuid();

			sendBleMessage(BleConstants.MSG_BLE_ID_CHARACTERISTIC_READ, status, data, uuid);
			onNextWrite();
        }

		@Override
		public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
			final byte[] data = descriptor.getValue();
			BleLog.i(TAG, "onCharacteristicRead: " + HexUtil.encodeHexStr(data));
			UUID uuid = descriptor.getUuid();

			sendBleMessage(BleConstants.MSG_BLE_ID_DESCRIPTOR_READ, status, data, uuid);
			onNextWrite();
		}

		@Override
		public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
			BleLog.i(TAG, "onReliableWriteCompleted: " + BleUtils.getGattStatus(status));

			Message msg = Message.obtain();
			msg.what = BleConstants.MSG_BLE_ID_RELIABLE_WRITE_COMPLETED;
			msg.arg1 = status;
			notifyAllBleClients(msg);
			onNextWrite();
		}

		@Override
		public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
			BleLog.i(TAG, "onReadRemoteRssi: " + rssi + " status:" + BleUtils.getGattStatus(status));

			Message msg = Message.obtain();
			msg.what = BleConstants.MSG_BLE_ID_READ_REMOTE_RSSI;
			msg.arg1 = status;
			msg.arg2 = rssi;
			notifyAllBleClients(msg);
			onNextWrite();
		}

		@Override
		public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
			BleLog.i(TAG, "onMtuChanged: " + BleUtils.getGattStatus(status));

			Message msg = Message.obtain();
			msg.what = BleConstants.MSG_BLE_ID_MTU_CHANGED;
			msg.arg1 = status;
			msg.arg2 = mtu;
			notifyAllBleClients(msg);
			onNextWrite();
		}
	};


	public BaseBleService() {
		mHandler = new BleServiceHandle(this);
		mMessenger = new Messenger(mHandler);
	}

	@Override
	public IBinder onBind(Intent intent) {
		BleLog.i(TAG, "onBind");
		return mMessenger.getBinder();
	}

	synchronized public void updateState(BleConnectState newState) {
		if (mState != newState) {
			mState = newState;
			Message msg = Message.obtain(null, BleConstants.BLE_MSG_ID_CONNECTION_STATE_CHANGED);
			if (msg != null) {
				msg.obj = mState;
				notifyAllBleClients(msg);
			}
		}
	}

	/**
	 * 开启蓝牙扫描
	 */
	public void startScan(){
		if(mBleScanner == null) {
			mBleScanner = new BleScanner(this, this);
		}

		mBleScanner.startBleScan();
	}
	/**
	 * 停止蓝牙扫描
	 */
	public void stopScan(){
		if(mBleScanner != null) {
			mBleScanner.stopBleScan();
			mBleScanner = null;
		}

	}


	/**
	 * (直接连接 需要设备可连接范围中)
	 */
	public boolean directlyConnectDevice(String deviceMac) {
		return directlyConnectDevice(deviceMac, false);
	}
	public boolean directlyConnectDevice(String deviceMac, boolean autoConnect) {
		final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
		BluetoothAdapter mBluetoothAdapter = bluetoothManager.getAdapter();
		BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(deviceMac);
		return connectDevice(device, autoConnect);
	}

	public boolean connectDevice(final BluetoothDevice device, boolean autoConnect) {
		mGatt = device.connectGatt(this, autoConnect, mGattCallback);

		if(mGatt != null){
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mGatt.connect();
                }
            });
		} else {
			BleLog.e(TAG, "serviceConnect mGatt==null");
		}
		return true;
	}

	public boolean connectDevice(final BluetoothDevice device) {
		return connectDevice(device, false);
	}

	private synchronized void  sendBleMessage(int msgId, int status, byte[] values,  UUID uuid){
		Message msg = Message.obtain();
		msg.what = msgId;
		msg.arg1 = status;
		msg.obj = values;
		Bundle data = new Bundle();
		data.putSerializable(BleConstants.BLE_MSG_CHARACTERISTIC_UUID_KEY, uuid);
		msg.setData(data);
		notifyAllBleClients(msg);
	}
	private synchronized void  sendBleMessage(int msgId, int status,  UUID uuid){
		Message msg = Message.obtain();
		msg.what = msgId;
		msg.arg1 = status;
		Bundle data = new Bundle();
		data.putSerializable(BleConstants.BLE_MSG_CHARACTERISTIC_UUID_KEY, uuid);
		msg.setData(data);
		notifyAllBleClients(msg);
	}

	//notify 订阅者
	public void notifyAllBleClients(Message msg) {
		for (int i = mClients.size() - 1; i >= 0; i--) {
			Messenger messenger = mClients.get(i);
			if (!sendMessage(messenger, msg)) {
				mClients.remove(messenger);
			}
		}
	}
	
	private boolean sendMessage(Messenger messenger, Message msg) {
		boolean success = true;
		try {
			messenger.send(msg);
		} catch (RemoteException e) {
			BleLog.w(TAG, "Lost connection to client" + e);
			success = false;
		}
		return success;
	}

	protected synchronized boolean writeToCharacteristic(UUID serviceUUID, UUID characteristicUUID, byte[] values) {
		BluetoothGattService gattService = mGatt == null ? null : mGatt.getService(serviceUUID);
		if(gattService != null) {
			BluetoothGattCharacteristic gattCharacteristic = gattService.getCharacteristic(characteristicUUID);
			if(gattCharacteristic != null) {
				gattCharacteristic.setValue(values);
				write(gattCharacteristic);
				return true;
			}
		}
		return false;
	}



	/**
	 * enable notify or disable notify
	 */
	public void updateCharacteristicNotification(UUID serviceUUID, UUID CharacteristicUUID, UUID descriptorUUID, boolean enable) {
		//接收消息
		final BluetoothGattService service = mGatt == null ? null : mGatt.getService(serviceUUID);
		if (service != null) {
			final BluetoothGattCharacteristic readData = service.getCharacteristic(CharacteristicUUID);
			mGatt.setCharacteristicNotification(readData, enable);
			final BluetoothGattDescriptor config = readData.getDescriptor(descriptorUUID);
			if(config != null) {
				config.setValue(enable ? BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
						: BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
				write(config);
			}
		}
	}


	//读取 特征值
	public boolean readFromCharacteristic(UUID serviceUUID, UUID CharacteristicUUID){
		BluetoothGattService gattService = mGatt.getService(serviceUUID);
		if(gattService != null) {
			BluetoothGattCharacteristic gattCharacteristic = gattService.getCharacteristic(CharacteristicUUID);
			if(gattCharacteristic != null) {

				mGatt.setCharacteristicNotification(gattCharacteristic, true);
    			mGatt.readCharacteristic(gattCharacteristic);
				return true;
			}
		}

		return false;
	}


	protected synchronized void write(Object o) {
		if (sWriteQueue.isEmpty() && !sIsWriting) {
			doWrite(o);
		} else {
			sWriteQueue.add(o);
		}
	}

	private synchronized void onNextWrite() {
		sIsWriting = false;
		nextWrite();
	}

	private synchronized void nextWrite() {
		//empty enable write
		if(sIsWriting) {
			sIsWriting = !sWriteQueue.isEmpty();
		}
		if (!sWriteQueue.isEmpty() && !sIsWriting) {
			doWrite(sWriteQueue.poll());
		}
	}

	private synchronized void doWrite(Object o) {
		if (o instanceof BluetoothGattCharacteristic) {
			sIsWriting = mGatt.writeCharacteristic((BluetoothGattCharacteristic) o);
		} else if (o instanceof BluetoothGattDescriptor) {
			sIsWriting = mGatt.writeDescriptor((BluetoothGattDescriptor) o);
		} else {
			nextWrite();
		}
	}

    public void addClient(Messenger messenger) {
        mClients.add(messenger);
    }
    public void removeClient(Messenger messenger) {
        mClients.remove(messenger);
        //没有了连接...释放蓝牙？
        if(mClients.size() == 0){
            release();
        }
    }

    public BluetoothGatt getGatt() {
		return mGatt;
	}

	//对于 读写数据，需要使用uuid来区分
	private synchronized Bundle obtainData(UUID uuid) {
		Bundle data = new Bundle();
		data.putSerializable(BleConstants.BLE_MSG_CHARACTERISTIC_UUID_KEY, uuid);
		return data;
	}

	//释放蓝牙
	public void release() {
		BleLog.i(TAG, "release()");
		sIsWriting = false;
		sWriteQueue.clear();
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mGatt.disconnect();
            }
        });
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    if (mGatt != null) {
                        mGatt.close();
                        mGatt = null;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, 500);
    }

	@Override
	public void onDestroy() {
		super.onDestroy();
		release();
		BleLog.i(TAG, "onDestroy()");
	}
}
