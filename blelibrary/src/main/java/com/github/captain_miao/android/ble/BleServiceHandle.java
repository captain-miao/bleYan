package com.github.captain_miao.android.ble;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.os.Message;

import com.github.captain_miao.android.ble.constant.BleConstants;
import com.github.captain_miao.android.ble.utils.BleLog;

import java.util.UUID;


/**
 * @author YanLu
 * @since  2015-09-14
 *
 * handle ble commend
 */
public class BleServiceHandle extends AppHandler<BaseBleService> {
	private final static String TAG = BleServiceHandle.class.getName();

	public BleServiceHandle(BaseBleService reference) {
		super(reference);
		mMessageListener = new HandleMessageListener<BaseBleService>() {
			@Override
			public void onHandleMessage(BaseBleService reference, Message msg) {
				onBleServiceMessage(reference, msg);
			}
		};
	}


	public void onBleServiceMessage(BaseBleService reference, Message msg) {
		if (msg != null) {
			Bundle data = msg.getData();
			switch (msg.what) {
				case BleConstants.MSG_CONTROL_ID_REGISTER:
					BleLog.i(TAG, "BleService Registered");
					if (msg.replyTo != null) {
						reference.addClient(msg.replyTo);
					}

					break;
				case BleConstants.MSG_CONTROL_ID_UNREGISTER:
					BleLog.i(TAG, "client unregistered");
					if (msg.replyTo != null) {
						reference.removeClient(msg.replyTo);
					}
					break;


				case BleConstants.MSG_CONTROL_ID_START_SCAN: {
					BleLog.i(TAG, "start scan...");
					reference.startScan();
					break;
				}
				case BleConstants.MSG_CONTROL_ID_STOP_SCAN: {
					BleLog.i(TAG, "start scan...");
					reference.stopScan();
					break;
				}
				case BleConstants.MSG_CONTROL_ID_CONNECT_DEVICE: {
					BluetoothDevice device = data.getParcelable(BleConstants.BLE_MSG_BLE_DEVICE_KEY);
					reference.connectDevice(device);
					break;
				}
				case BleConstants.MSG_CONTROL_ID_CONNECT_MAC: {
					String mac = (String) msg.obj;
					reference.directlyConnectDevice(mac);
					break;
				}
				//write_characteristic
				case BleConstants.MSG_CONTROL_ID_WRITE_CHARACTERISTIC: {
					if(data != null) {
						UUID serviceUUID = (UUID) data.getSerializable(BleConstants.BLE_MSG_SERVICE_UUID_KEY);
						UUID characteristicUUID = (UUID) data.getSerializable(BleConstants.BLE_MSG_CHARACTERISTIC_UUID_KEY);
						byte[] values = (byte[]) msg.obj;
						reference.writeToCharacteristic(serviceUUID, characteristicUUID, values);
					}
					break;
				}
				//setCharacteristicNotification
				case BleConstants.MSG_CONTROL_ID_DESCRIPTOR_NOTIFICATION: {
					if(data != null) {
						UUID serviceUUID = (UUID) data.getSerializable(BleConstants.BLE_MSG_SERVICE_UUID_KEY);
						UUID characteristicUUID = (UUID) data.getSerializable(BleConstants.BLE_MSG_CHARACTERISTIC_UUID_KEY);
						UUID descriptorUUID = (UUID) data.getSerializable(BleConstants.BLE_MSG_DESCRIPTOR_UUID_KEY);
						boolean enable = data.getBoolean(BleConstants.BLE_MSG_ENABLE_KEY);
						reference.updateCharacteristicNotification(serviceUUID, characteristicUUID,
								descriptorUUID, enable);
					}
					break;
				}
				//readCharacteristic
				case BleConstants.MSG_CONTROL_ID_READ_CHARACTERISTIC: {
					if(data != null) {
						UUID serviceUUID = (UUID) data.getSerializable(BleConstants.BLE_MSG_SERVICE_UUID_KEY);
						UUID characteristicUUID = (UUID) data.getSerializable(BleConstants.BLE_MSG_CHARACTERISTIC_UUID_KEY);
						reference.readFromCharacteristic(serviceUUID, characteristicUUID);
					}
					break;
				}

			}
		} else {
			BleLog.i(TAG, "onBleServiceMessage() msg is null");
		}
	}
}
