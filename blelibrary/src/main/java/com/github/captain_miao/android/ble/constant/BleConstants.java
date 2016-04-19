package com.github.captain_miao.android.ble.constant;

/**
 * @author YanLu
 * @since  2015-09-14
 */

public class BleConstants {

    //ble response message id
    public static final int MSG_BLE_ID_CHARACTERISTIC_WRITE 		= 20000;
    public static final int MSG_BLE_ID_DESCRIPTOR_WRITE 			= 20001;
    public static final int MSG_BLE_ID_CHARACTERISTIC_NOTIFICATION 	= 20002;
    public static final int MSG_BLE_ID_CHARACTERISTIC_READ 			= 20003;
    public static final int MSG_BLE_ID_DESCRIPTOR_READ	 			= 20004;
    public static final int MSG_BLE_ID_RELIABLE_WRITE_COMPLETED		= 20005;
    public static final int MSG_BLE_ID_READ_REMOTE_RSSI				= 20006;
    public static final int MSG_BLE_ID_MTU_CHANGED					= 20007;
    public static final int MSG_BLE_ID_SERVICES_DISCOVERED			= 20008;


    //ble control message id
    public static final int MSG_CONTROL_ID_REGISTER                 = 30000;
   	public static final int MSG_CONTROL_ID_UNREGISTER               = 30001;
	public static final int MSG_CONTROL_ID_CONNECT_DEVICE			= 30002;
	public static final int MSG_CONTROL_ID_CONNECT_MAC  			= 30003;
   	public static final int MSG_CONTROL_ID_START_SCAN               = 30004;
   	public static final int MSG_CONTROL_ID_STOP_SCAN                = 30005;


    //ble read message id
	public static final int MSG_CONTROL_ID_WRITE_CHARACTERISTIC		= 40000;
	public static final int MSG_CONTROL_ID_DESCRIPTOR_NOTIFICATION  = 40001;
	public static final int MSG_CONTROL_ID_READ_CHARACTERISTIC      = 40002;






	//用来区分不同类型的消息
    public static final int BLE_MSG_ID_CONNECTION_STATE_CHANGED = 10000;//蓝牙连接状态变化


    public static final String BLE_MSG_SERVICE_UUID_KEY   		 = "service_uuid";
    public static final String BLE_MSG_CHARACTERISTIC_UUID_KEY   = "characteristic_uuid";
    public static final String BLE_MSG_DESCRIPTOR_UUID_KEY   	 = "descriptor_uuid";
    public static final String BLE_MSG_VALUE_KEY   	 			 = "ble_value";
    public static final String BLE_MSG_ENABLE_KEY   	 		 = "ble_enable";
    public static final String BLE_MSG_BLE_DEVICE_KEY			 = "ble_device";
    public static final String BLE_MSG_BLE_GATT_KEY			     = "ble_gatt";
}
