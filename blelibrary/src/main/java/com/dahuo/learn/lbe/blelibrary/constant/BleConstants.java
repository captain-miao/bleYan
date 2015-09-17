package com.dahuo.learn.lbe.blelibrary.constant;

import java.util.UUID;

/**
 * @author YanLu
 * @since  2015-09-14
 */

public class BleConstants {

//	public static final String DEVICE_NAME = "RunnerT";
    public static final String DEVICE_NAME = "WeCoach-Pro";

	//用来区分不同的uuid 消息
    public static final int BLE_UUID_MSG_ID = 9000;

    //蓝牙读写 消息id
    public static final int MSG_BLE_ID_CHARACTERISTIC_WRITE 		= 20000;
    public static final int MSG_BLE_ID_DESCRIPTOR_WRITE 			= 20001;
    public static final int MSG_BLE_ID_CHARACTERISTIC_NOTIFICATION 	= 20002;
    public static final int MSG_BLE_ID_CHARACTERISTIC_READ 			= 20003;
    public static final int MSG_BLE_ID_DESCRIPTOR_READ	 			= 20004;
    public static final int MSG_BLE_ID_RELIABLE_WRITE_COMPLETED		= 20005;
    public static final int MSG_BLE_ID_READ_REMOTE_RSSI				= 20006;
    public static final int MSG_BLE_ID_MTU_CHANGED					= 20007;
    public static final int MSG_BLE_ID_SERVICES_DISCOVERED			= 20008;


    //控制消息
    public static final int MSG_CONTROL_ID_REGISTER                 = 30000;
   	public static final int MSG_CONTROL_ID_UNREGISTER               = 30001;
	public static final int MSG_CONTROL_ID_CONNECT_DEVICE			= 30002;
	public static final int MSG_CONTROL_ID_CONNECT_MAC  			= 30003;
	public static final int MSG_CONTROL_ID_WRITE_CHARACTERISTIC		= 30004;
	public static final int MSG_CONTROL_ID_DESCRIPTOR_NOTIFICATION  = 30005;
	public static final int MSG_CONTROL_ID_READ_CHARACTERISTIC      = 30006;
   	public static final int MSG_START_SCAN = 3;
   	public static final int MSG_STATE_CHANGED = 4;
   	public static final int MSG_DEVICE_FOUND = 5;
   	public static final int MSG_DEVICE_CONNECT = 6;
   	public static final int MSG_DEVICE_DISCONNECT = 7;
   	public static final int MSG_DEVICE_DATA = 8;
   	public static final int MSG_STOP_SCAN = 9;
   	public static final int MSG_SERVICE_IS_COVERING = 16;//add
   	public static final int MSG_SERVICE_IS_COVERED = 17;//add
   	public static final int MSG_TREADMILL_RELEASE = 18;//add

	//指令消息
	public static final int MSG_TREADMILL_VELOCITY_COMMAND = 300;
	public static final int MSG_TREADMILL_LUBRICATE_COMMAND = 301;

    public static final int MSG_READ_SWIM_STATUS_COMMAND = 400;
    public static final int MSG_WRITE_SWIM_STATUS_COMMAND = 401;
    public static final int MSG_WRITE_SWIM_FRAGMENT_COMMAND = 402;
    public static final int MSG_SUBSCRIBE_SWIM_DATA_COMMAND = 403;
    public static final int MSG_UNSUBSCRIBE_SWIM_DATA_COMMAND = 404;

    public static final int MSG_BLE_WRITE_DATA_COMMAND = 450;
    public static final int MSG_BLE_UPDATE_NOTIFICATION_COMMAND = 451;
    public static final int MSG_BLE_READ_CHARACTERISTIC_COMMAND = 452;
   	// wecoach
   	public static final int MSG_TREADMILL_CONNECTING = 10;
   	public static final int MSG_TREADMILL_DISCONNECTING = 11;
   	public static final int MSG_TREADMILL_CONNECT = 13;
   	public static final int MSG_TREADMILL_DISCONNECT = 14;
   	public static final int MSG_TREADMILL_TIME_OUT = 15;

   	public static final int MSG_TREADMILL_START = 21;
   	public static final int MSG_TREADMILL_STOP = 22;
   	public static final int MSG_VELOCITY_ADD1 = 23;
   	public static final int MSG_VELOCITY_ADD2 = 24;
   	public static final int MSG_VELOCITY_ADD3 = 25;
   	public static final int MSG_LUBRICATE_START = 26;
   	public static final int MSG_LUBRICATE_STOP = 27;

    // service
    public static final UUID UUID_WECOACH_SERVICE = UUID.fromString("E54EAA50-371B-476C-99A3-74d267e3edbe");
    public static final UUID UUID_SWIM_SERVICE = UUID.fromString("E54EAB50-371B-476C-99A3-74D267E3EDBE");

	//接受传感器 数据(没用指令)
	public static final int UUID_READ_DATA_MSG_ID = BLE_UUID_MSG_ID + 1;
	public static final UUID UUID_CCC = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
    //BE:ED:E3:67:D2:74:A3:99:6C:47:1B:37:51:AB:4E:E5
    //public static final UUID UUID_READ_DATA = UUID.fromString("BEEDE367-D274-A399-6C47-1B3751AB4EE5");
    public static final UUID UUID_READ_DATA = UUID.fromString("e54eab51-371b-476c-99a3-74d267e3edbe");

	//长连接保护(内部使用，跟用户基本无关)
	public static final int UUID_CONPRO_CONF_MSG_ID = UUID_READ_DATA_MSG_ID + 1;
    public static final UUID UUID_CONPRO_CONF = UUID.fromString("E54EAB55-371B-476C-99A3-74D267E3EDBE");
    public static final UUID UUID_CONPRO_CONF2 = UUID.fromString("E54EAA55-371B-476C-99A3-74D267E3EDBE");
    public static final byte[] CONPROMODEL = { (byte) 0x88 };


	//启动、加档、减档、停止
	public static final int UUID_WORK_MODEL_MSG_ID = UUID_CONPRO_CONF_MSG_ID + 1;
    public static final UUID UUID_WORKMODEL_CONF = UUID.fromString("E54EAA52-371B-476C-99A3-74D267E3EDAE");
    public static final byte[] WORKMODEL_START = { (byte) 0x15, (byte) 0x00, (byte) 0x00 };
    public static final byte[] WORKMODEL_START_LOW_SPEED = { (byte) 0x15, (byte) 0x10, (byte) 0x00 };
    public static final byte[] WORKMODEL_STOP = { (byte) 0xa1, (byte) 0x00, (byte) 0x00 };
    public static final byte[] WORKMODEL_ADD1 = { (byte) 0x15, (byte) 0x18, (byte) 0x00 };
    public static final byte[] WORKMODEL_ADD2 = { (byte) 0x15, (byte) 0x22, (byte) 0x00 };
    public static final byte[] WORKMODEL_ADD3 = { (byte) 0x15, (byte) 0x60, (byte) 0x00 };

	//设备加油
	public static final int UUID_LUBRICATE_CONF_MSG_ID = UUID_WORK_MODEL_MSG_ID + 1;
    public static final UUID UUID_LUBRICATE_CONF = UUID.fromString("E54EAA53-371B-476C-99A3-74D267E3EDAE");
    public static final byte[] LUBRICATEMODEL_START = { (byte) 0x88 };
    public static final byte[] LUBRICATEMODEL_STOP = { (byte) 0x00 };

	//游泳状态
	public static final int UUID_SWIM_STATUS_MSG_ID = UUID_LUBRICATE_CONF_MSG_ID + 1;
    // swim状态: 1字节
	public static final UUID UUID_READ_SWIM_STATUS = UUID.fromString("e54eab57-371b-476c-99a3-74d267e3edbe");

    // 数据读取 需要读出的块号: 2字节
    public static final int UUID_READ_SWIM_FRAGMENT_MSG_ID = UUID_SWIM_STATUS_MSG_ID + 1;
	public static final UUID UUID_READ_SWIM_FRAGMENT = UUID.fromString("e54eab52-371b-476c-99a3-74d267e3edbe");

    //设备时间
	public static final int UUID_WECOACH_DEVICE_TIME_MSG_ID = UUID_READ_SWIM_FRAGMENT_MSG_ID + 1;
    // 设备时间: 4字节
	public static final UUID UUID_WECOACH_DEVICE_TIME = UUID.fromString("e54eab54-371b-476c-99a3-74d267e3edbe");




	//用来区分不同类型的消息
    public static final int BLE_MSG_ID_CONNECTION_STATE_CHANGED = 10000;//蓝牙连接状态变化
    public static final int BLE_MSG_ID_CHARACTERISTIC_CHANGED   = 10001;//蓝牙数据读写


    public static final String BLE_MSG_SERVICE_UUID_KEY   		 = "service_uuid";
    public static final String BLE_MSG_CHARACTERISTIC_UUID_KEY   = "characteristic_uuid";
    public static final String BLE_MSG_DESCRIPTOR_UUID_KEY   	 = "descriptor_uuid";
    public static final String BLE_MSG_VALUE_KEY   	 			 = "ble_value";
    public static final String BLE_MSG_ENABLE_KEY   	 		 = "ble_enable";
    public static final String BLE_MSG_BLE_DEVICE_KEY			 = "ble_device";
    public static final String BLE_MSG_BLE_GATT_KEY			     = "ble_gatt";
}
