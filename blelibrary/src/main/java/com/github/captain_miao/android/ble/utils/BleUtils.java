package com.github.captain_miao.android.ble.utils;

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

    //BluetoothGattCharacteristic Permission
    public static String getPermission(int permission) {
        StringBuilder permissionStr = new StringBuilder();
        String binaryString = Integer.toBinaryString(permission);
        binaryString = String.format("%16s", binaryString).replace(' ', '0');
        int len = binaryString.length();
        //PERMISSION_READ (0x00000001)
        if(binaryString.charAt(len - 1) == '1'){
            permissionStr.append("READ,");
        }
        //PERMISSION_READ_ENCRYPTED (0x00000002)
        if(binaryString.charAt(len - 2) == '1'){
            permissionStr.append("READ_ENCRYPTED,");
        }
        //PERMISSION_READ_ENCRYPTED_MITM (0x00000004)
        if(binaryString.charAt(len - 3) == '1'){
            permissionStr.append("READ_ENCRYPTED_MITM,");
        }


        //PERMISSION_WRITE (0x00000010)
        if(binaryString.charAt(len - 5) == '1'){
            permissionStr.append("WRITE,");
        }
        //PERMISSION_WRITE_ENCRYPTED (0x00000020)
        if(binaryString.charAt(len - 6) == '1'){
            permissionStr.append("WRITE_ENCRYPTED,");
        }
        //PERMISSION_WRITE_ENCRYPTED_MITM (0x00000040)
        if(binaryString.charAt(len - 7) == '1'){
            permissionStr.append("WRITE_ENCRYPTED_MITM,");
        }


        //PERMISSION_WRITE_SIGNED (0x00000080)
        if(binaryString.charAt(len - 8) == '1'){
            permissionStr.append("WRITE_ENCRYPTED_MITM,");
        }
        //PERMISSION_WRITE_SIGNED_MITM (0x00000100)
        if(binaryString.charAt(len - 9) == '1'){
            permissionStr.append("WRITE_SIGNED_MITM,");
        }

        String perStr = permissionStr.toString();
        if(perStr.endsWith(",")) {
            perStr = perStr.substring(0, perStr.length() - 1);
        }

        return perStr;
    }
    //BluetoothGattCharacteristic Properties
    public static String getProperties(int properties) {
        StringBuilder permissionStr = new StringBuilder();
        String binaryString = Integer.toBinaryString(properties);
        binaryString = String.format("%16s", binaryString).replace(' ', '0');
        int len = binaryString.length();
        //PROPERTY_BROADCAST (0x00000001)
        if(binaryString.charAt(len - 1) == '1'){
            permissionStr.append("BROADCAST,");
        }
        //PROPERTY_READ (0x00000002)
        if(binaryString.charAt(len - 2) == '1'){
            permissionStr.append("READ,");
        }
        //PROPERTY_WRITE_NO_RESPONSE (0x00000004)
        if(binaryString.charAt(len - 3) == '1'){
            permissionStr.append("WRITE_NO_RESPONSE,");
        }
        //PROPERTY_WRITE (0x00000008)
        if(binaryString.charAt(len - 4) == '1'){
            permissionStr.append("READ_ENCRYPTED_MITM,");
        }


        //PROPERTY_NOTIFY (0x00000010)
        if(binaryString.charAt(len - 5) == '1'){
            permissionStr.append("NOTIFY,");
        }
        //PROPERTY_INDICATE (0x00000020)
        if(binaryString.charAt(len - 6) == '1'){
            permissionStr.append("INDICATE,");
        }
        //PROPERTY_SIGNED_WRITE (0x00000040)
        if(binaryString.charAt(len - 7) == '1'){
            permissionStr.append("SIGNED_WRITE,");
        }

        //PROPERTY_EXTENDED_PROPS (0x00000080)
        if(binaryString.charAt(len - 8) == '1'){
            permissionStr.append("EXTENDED_PROPS,");
        }

        String perStr = permissionStr.toString();
        if(perStr.endsWith(",")) {
            perStr = perStr.substring(0, perStr.length() - 1);
        }

        return perStr;
    }
    //BluetoothGattCharacteristic WriteType
    public static String getWriteType(int writeType) {
        StringBuilder permissionStr = new StringBuilder();
        String binaryString = Integer.toBinaryString(writeType);
        binaryString = String.format("%16s", binaryString).replace(' ', '0');
        int len = binaryString.length();
        //WRITE_TYPE_NO_RESPONSE (0x00000001)
        if(binaryString.charAt(len - 1) == '1'){
            permissionStr.append("NO_RESPONSE,");
        }
        //WRITE_TYPE_DEFAULT (0x00000002)
        if(binaryString.charAt(len - 2) == '1'){
            permissionStr.append("WRITE,");
        }
        //WRITE_TYPE_SIGNED (0x00000004)
        if(binaryString.charAt(len - 3) == '1'){
            permissionStr.append("SIGNED,");
        }


        String perStr = permissionStr.toString();
        if(perStr.endsWith(",")) {
            perStr = perStr.substring(0, perStr.length() - 1);
        }

        return perStr;
    }
}
