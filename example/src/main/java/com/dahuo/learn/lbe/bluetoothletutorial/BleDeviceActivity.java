package com.dahuo.learn.lbe.bluetoothletutorial;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

import com.dahuo.learn.lbe.blelibrary.BleCallback;
import com.dahuo.learn.lbe.blelibrary.BluetoothHelper;
import com.dahuo.learn.lbe.blelibrary.ConnectCallback;
import com.dahuo.learn.lbe.blelibrary.constant.BleConnectState;
import com.dahuo.learn.lbe.blelibrary.constant.ConnectError;
import com.dahuo.learn.lbe.blelibrary.utils.HexUtil;
import com.dahuo.learn.lbe.bluetoothletutorial.ble.AppBluetoothHelper;
import com.dahuo.learn.lbe.bluetoothletutorial.model.BleDevice;
import com.dahuo.learn.lbe.supportsdk.BaseActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Created by on 15/8/3.
 */
public class BleDeviceActivity extends BaseActivity implements View.OnClickListener, BluetoothHelper.OnBindListener {
    private static final String TAG = BleDeviceActivity.class.getSimpleName();


    private final String LIST_NAME = "NAME";
    private final String LIST_UUID = "UUID";
    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";

    private TextView mTVConnectionState;
    private TextView mDataField;
    private String mDeviceName;
    private String mDeviceAddress;
    private ExpandableListView mGattServicesList;
    private ArrayList<BluetoothGattService> mGattServices =
            new ArrayList<BluetoothGattService>();
    private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics =
            new ArrayList<ArrayList<BluetoothGattCharacteristic>>();
    private boolean mConnected = false;
    private BluetoothGattCharacteristic mNotifyCharacteristic;


    private BleDevice mDevice;
    private AppBluetoothHelper mBleHelper;
    @Override
    public void init(Bundle savedInstanceState) {
        setContentView(R.layout.act_device);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mDevice = (BleDevice) getIntent().getSerializableExtra("bleDevice");
        mDeviceName = mDevice.name;
        mDeviceAddress = mDevice.address;
        if (TextUtils.isEmpty(mDeviceAddress)) {
            finish();
            return;
        }

        setTitle(mDeviceName);
        initView();
        showProgressDialog();
        mBleHelper = new AppBluetoothHelper(this);
        mBleHelper.setBleCallback(mBleCallback);
        mBleHelper.bindService(this);
    }




    private void initView() {
        //findViewById(R.id.btn_check_device).setOnClickListener(this);
        // Sets up UI references.
        ((TextView) findViewById(R.id.device_address)).setText(mDeviceAddress);
        mGattServicesList = (ExpandableListView) findViewById(R.id.gatt_services_list);
        mGattServicesList.setOnChildClickListener(servicesListClickListner);
        mTVConnectionState = (TextView) findViewById(R.id.connection_state);
        mDataField = (TextView) findViewById(R.id.data_value);
   	}


    @Override
    protected void onPause() {
        super.onPause();
        mBleHelper.release();
    }

    @Override
   	public void onClick(View v) {

   		switch (v.getId()) {

        }
   	}

    private BleCallback mBleCallback = new BleCallback() {
        @Override
        public void onFailed(String msg) {

        }

        @Override
        public void onConnectionStateChange(int status, int newStatus) {
            BleConnectState connectState = BleConnectState.getBleConnectState(newStatus);
            mTVConnectionState.setText(connectState.getMessage());
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            //服务发现成功
            if (gatt != null && status == BluetoothGatt.GATT_SUCCESS) {
                displayGattServices(gatt.getServices());
            }
        }
    };


    // Demonstrates how to iterate through the supported GATT Services/Characteristics.
    // In this sample, we populate the data structure that is bound to the ExpandableListView
    // on the UI.
    private void displayGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null) return;
        String uuid = null;
        String unknownServiceString = getResources().getString(R.string.unknown_service);
        String unknownCharaString = getResources().getString(R.string.unknown_characteristic);
        ArrayList<HashMap<String, String>> gattServiceData = new ArrayList<HashMap<String, String>>();
        ArrayList<ArrayList<HashMap<String, String>>> gattCharacteristicData
                = new ArrayList<ArrayList<HashMap<String, String>>>();
        mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();

        // Loops through available GATT Services.
        for (BluetoothGattService gattService : gattServices) {
            HashMap<String, String> currentServiceData = new HashMap<String, String>();
            uuid = gattService.getUuid().toString();
            currentServiceData.put(
                    LIST_NAME, SampleGattAttributes.lookup(uuid, unknownServiceString));
            currentServiceData.put(LIST_UUID, uuid);
            gattServiceData.add(currentServiceData);

            ArrayList<HashMap<String, String>> gattCharacteristicGroupData =
                    new ArrayList<HashMap<String, String>>();
            List<BluetoothGattCharacteristic> gattCharacteristics =
                    gattService.getCharacteristics();
            ArrayList<BluetoothGattCharacteristic> charas =
                    new ArrayList<BluetoothGattCharacteristic>();

            // Loops through available Characteristics.
            for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                charas.add(gattCharacteristic);
                HashMap<String, String> currentCharaData = new HashMap<String, String>();
                uuid = gattCharacteristic.getUuid().toString();
                currentCharaData.put(
                        LIST_NAME, SampleGattAttributes.lookup(uuid, unknownCharaString));
                currentCharaData.put(LIST_UUID, uuid);
                gattCharacteristicGroupData.add(currentCharaData);
            }
            mGattCharacteristics.add(charas);
            gattCharacteristicData.add(gattCharacteristicGroupData);
            mGattServices.add(gattService);
        }

        SimpleExpandableListAdapter gattServiceAdapter = new SimpleExpandableListAdapter(
                this,
                gattServiceData,
                R.layout.ble_expandable_list_item,
                new String[]{LIST_NAME, LIST_UUID},
                new int[]{android.R.id.text1, android.R.id.text2},
                gattCharacteristicData,
                R.layout.ble_expandable_list_item,
                new String[]{LIST_NAME, LIST_UUID},
                new int[]{android.R.id.text1, android.R.id.text2}
        );
        mGattServicesList.setAdapter(gattServiceAdapter);
    }

        // If a given GATT characteristic is selected, check for supported features.  This sample
    // demonstrates 'Read' and 'Notify' features.  See
    // http://d.android.com/reference/android/bluetooth/BluetoothGatt.html for the complete
    // list of supported characteristic features.
    private final ExpandableListView.OnChildClickListener servicesListClickListner =
            new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView parent, View v, int groupPosition,
                                            int childPosition, long id) {
                    if (mGattCharacteristics != null) {
                        final BluetoothGattService service = mGattServices.get(groupPosition);
                        final BluetoothGattCharacteristic characteristic =
                                mGattCharacteristics.get(groupPosition).get(childPosition);
                        UUID serviceUUID = service.getUuid();
                        UUID characteristicUUID = characteristic.getUuid();
                        if(characteristicUUID.equals(UUID.fromString(SampleGattAttributes.BONG_I_II_UUID_CHAR_SEND))) {
                            //发送 震动 和 亮灯 命令
                            mBleHelper.writeCharacteristic(serviceUUID, characteristicUUID, HexUtil.hexStringToByteArray("26FFFFFF20"));
                            mBleHelper.writeCharacteristic(serviceUUID, characteristicUUID, HexUtil.hexStringToByteArray("26FFFFFF30"));

                        }
//                        final int charaProp = characteristic.getProperties();
//                        if ((charaProp | BluetoothGattCharacteristic.PROPERTY_READ) > 0) {
//                            // If there is an active notification on a characteristic, clear
//                            // it first so it doesn't update the data field on the user interface.
//                            //if (mNotifyCharacteristic != null) {
//                                //setCharacteristicNotification(mNotifyCharacteristic, false);
//                                //mNotifyCharacteristic = null;
//                            //}
//                            //readCharacteristic(characteristic);
//                        }
//                        if ((charaProp | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
//                            mNotifyCharacteristic = characteristic;
//                            //setCharacteristicNotification(characteristic, true);
//                        }
                        return true;
                    }
                    return false;
                }
            };

    @Override
    public void onServiceConnected() {
        connectDevice(mDeviceAddress);
    }


    public void connectDevice(String deviceMac){
        mBleHelper.connectDevice(deviceMac,new ConnectCallback() {
            @Override
            public void onConnectSuccess() {
                //mTVConnectionState.setText(R.string.connected);
                mBleHelper.mConnCallback = null;
                dismissProgressDialog();
            }

            @Override
            public void onConnectFailed(ConnectError error) {
                dismissProgressDialog();
            }
        });
    }
}
