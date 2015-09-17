package com.dahuo.learn.lbe.blelibrary.utils;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothProfile;

import java.lang.reflect.Method;

/**
 * @author YanLu
 * @since 15/9/14
 */
public class BleUtils {
    private final static String TAG = BleUtils.class.getName();


    /**
   	 * Clears the device cache.
   	 */
   	public static boolean refreshDeviceCache(BluetoothGatt gatt) {
   	 /*
      * There is a refresh() method in BluetoothGatt class but for now it's hidden. We will call it using reflections.
   	  */
   		try {
   			final Method refresh = BluetoothGatt.class.getMethod("refresh");
   			if (refresh != null) {
   				final boolean success = (Boolean) refresh.invoke(gatt);
   				BleLog.i(TAG, "Refreshing result: " + success);
   				return success;
   			}
   		} catch (Exception e) {
   			BleLog.e(TAG, "An exception occured while refreshing device");
   		}
   		return false;
   	}



    //蓝牙连接状态
    public static String getBleConnectStatus(int status) {
        switch (status) {
            case BluetoothProfile.STATE_DISCONNECTED:
                return "STATE_DISCONNECTED";

            case BluetoothProfile.STATE_CONNECTING:
                return "STATE_CONNECTING";

            case BluetoothProfile.STATE_CONNECTED:
                return "STATE_CONNECTED";

            case BluetoothProfile.STATE_DISCONNECTING:
                return "STATE_DISCONNECTING";

            default:
                return "STATE_UNKNOWN: " + status;
        }
    }

    //GATT 状态
    public static String getGattStatus(int status) {
        switch (status) {
            case BluetoothGatt.GATT_SUCCESS:
                return "GATT_SUCCESS";

            case BluetoothGatt.GATT_READ_NOT_PERMITTED:
                return "GATT_READ_NOT_PERMITTED";

            case BluetoothGatt.GATT_WRITE_NOT_PERMITTED:
                return "GATT_WRITE_NOT_PERMITTED";

            case BluetoothGatt.GATT_INSUFFICIENT_AUTHENTICATION:
                return "GATT_INSUFFICIENT_AUTHENTICATION";

            case BluetoothGatt.GATT_REQUEST_NOT_SUPPORTED:
                return "GATT_REQUEST_NOT_SUPPORTED";

            case BluetoothGatt.GATT_INSUFFICIENT_ENCRYPTION:
                return "GATT_INSUFFICIENT_ENCRYPTION";

            case BluetoothGatt.GATT_INVALID_OFFSET:
                return "GATT_INVALID_OFFSET";

            case BluetoothGatt.GATT_INVALID_ATTRIBUTE_LENGTH:
                return "GATT_INVALID_ATTRIBUTE_LENGTH";

            case BluetoothGatt.GATT_FAILURE:
                return "GATT_FAILURE";

            default:
                return "STATE_UNKNOWN: " + status;
        }
    }
}
