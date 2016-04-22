package com.github.captain_miao.android.bluetoothletutorial;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.internal.MDTintHelper;
import com.afollestad.materialdialogs.util.DialogUtils;
import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.github.captain_miao.android.ble.BleCallback;
import com.github.captain_miao.android.ble.BluetoothHelper;
import com.github.captain_miao.android.ble.ConnectCallback;
import com.github.captain_miao.android.ble.constant.BleConnectState;
import com.github.captain_miao.android.ble.constant.ConnectError;
import com.github.captain_miao.android.ble.utils.BleUtils;
import com.github.captain_miao.android.ble.utils.HexUtil;
import com.github.captain_miao.android.bluetoothletutorial.app.AppLog;
import com.github.captain_miao.android.bluetoothletutorial.ble.AppBluetoothHelper;
import com.github.captain_miao.android.bluetoothletutorial.constant.AppConstants;
import com.github.captain_miao.android.bluetoothletutorial.expandablerecyclerview.VerticalChildObject;
import com.github.captain_miao.android.bluetoothletutorial.expandablerecyclerview.VerticalExpandableAdapter;
import com.github.captain_miao.android.bluetoothletutorial.expandablerecyclerview.VerticalParentObject;
import com.github.captain_miao.android.bluetoothletutorial.fragment.BottomSheetLogView;
import com.github.captain_miao.android.bluetoothletutorial.model.BleCommandInfo;
import com.github.captain_miao.android.bluetoothletutorial.model.BleDevice;
import com.github.captain_miao.android.supportsdk.BaseActivity;
import com.github.captain_miao.android.supportsdk.app.AppToast;
import com.github.captain_miao.android.supportsdk.utils.DateUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;

/**
 * Created by on 15/8/3.
 */
public class BleDeviceActivity extends BaseActivity implements View.OnClickListener,
                                            BluetoothHelper.OnBindListener, ExpandableRecyclerAdapter.ExpandCollapseListener {
    private static final String TAG = BleDeviceActivity.class.getSimpleName();

    private TextView mTVConnectionState;
    private TextView mDataField;
    private String mDeviceName;
    private String mDeviceAddress;
    private VerticalExpandableAdapter mExpandableAdapter;
    private RecyclerView mRecyclerView;
    private Map<String,BluetoothGattCharacteristic> mCharacteristicsMap = new HashMap<>();


    private BleDevice mDevice;
    private AppBluetoothHelper mBleHelper;
    private MaterialDialog dialog;
    private TextView mDataCharacteristic;
    @Override
    public void init(Bundle savedInstanceState) {
        setContentView(R.layout.act_device);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mDevice = (BleDevice) getIntent().getSerializableExtra(AppConstants.KEY_BLE_DEVICE);
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
        mRecyclerView = (RecyclerView) findViewById(R.id.linear_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mTVConnectionState = (TextView) findViewById(R.id.connection_state);
        mDataField = (TextView) findViewById(R.id.data_value);
   	}


    @Override
    protected void onPause() {
        super.onPause();
        //mBleHelper.release();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBleHelper.release();
    }

    @Override
   	public void onClick(View v) {

   		switch (v.getId()) {

        }
   	}

    //显示 10行数据
    Queue<String> mDataQueue = new LinkedList<String>();

    private BleCallback mBleCallback = new BleCallback() {
        @Override
        public void onFailed(String msg) {
            appendLog("onFailed " + msg);
        }

        @Override
        public void onDescriptorWrite(UUID uuid, int status) {
            AppLog.i(TAG, "onDescriptorWrite: " + BleUtils.getGattStatus(status));
            appendLog("onDescWrite: " + BleUtils.getGattStatus(status));
        }

        @Override
        public void onCharacteristicRead(UUID uuid, byte[] data) {
            String values = HexUtil.encodeHexStr(data);

            AppLog.i(TAG, "onCharacteristicRead: " + values);
            appendLog("onCharRead: " + values);
            if (dialog != null && dialog.isShowing()) {
                mDataCharacteristic.setText(values);
            }
        }

        @Override
        public void onCharacteristicNotification(UUID uuid, byte[] data) {
            String values = HexUtil.encodeHexStr(data);
            AppLog.i(TAG, "onCharacteristicNotification: " + values);
            appendLog("onChaNotify: " + values);
            mDataQueue.add(values);
            int size = mDataQueue.size();
            while (size > 10){
                mDataQueue.poll();
                size--;
            }

            mDataField.setText("");
            for(String v : mDataQueue){
                mDataField.append(v);
                mDataField.append("\n");
            }

            if(sb != null && sb.length() < 100 * 1024) {
                sb.append(values).append("\n");
            } else {
                mDataField.append(getString(R.string.app_tips_notify_data_too_long));
            }
        }

        @Override
        public void onCharacteristicWrite(UUID uuid, int status) {
            AppLog.i(TAG, "onCharacteristicWrite: " + BleUtils.getGattStatus(status));
            appendLog("onCharWrite: " + BleUtils.getGattStatus(status));
        }

        @Override
        public void onConnectionStateChange(int status, int newStatus) {
            BleConnectState connectState = BleConnectState.getBleConnectState(newStatus);
            mTVConnectionState.setText(AppBluetoothHelper.getConnectStateForShow(BleDeviceActivity.this, connectState.getCode()));
            appendLog("stateChange: " + AppBluetoothHelper.getConnectStateForShow(BleDeviceActivity.this, connectState.getCode()));
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            //服务发现成功
            if (gatt != null && status == BluetoothGatt.GATT_SUCCESS) {
                displayGattServices(gatt.getServices());
                appendLog("Discovered success");
            } else {
                AppToast.show(BleDeviceActivity.this, R.string.app_tips_discover_services_fail);
                appendLog("Discovered fail");
            }
        }
    };


    // Demonstrates how to iterate through the supported GATT Services/Characteristics.
    // In this sample, we populate the data structure that is bound to the ExpandableListView
    // on the UI.
    private void displayGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null) return;

        mExpandableAdapter = new VerticalExpandableAdapter(this, getAdapterData(gattServices)) {

            @Override
            protected void onClickCharacteristic(final String serviceUUID, final String characteristicUUID) {
                dialog = new MaterialDialog.Builder(BleDeviceActivity.this)
                        .title(R.string.app_tips_write_read_data)
                        .customView(R.layout.dialog_customview, true)
                        .positiveText(R.string.label_ok)
                        .cancelable(true)
                        .negativeText(R.string.label_cancel)
                        .show();
                View dialogView = dialog.getCustomView();
                if(dialogView != null) {
                    final BluetoothGattCharacteristic characteristic = mCharacteristicsMap.get(characteristicUUID);
                    final int charaProp = characteristic.getProperties();
                    //发送数据
                    mDataCharacteristic = (TextView) dialogView.findViewById(R.id.tv_read_characteristic_data);
                    final EditText hexEdit = (EditText) dialogView.findViewById(R.id.write_data_value);
                    if (hexEdit != null) {
                        hexEdit.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                final int length = s.toString().length();
                                invalidateInputMinMaxIndicator(hexEdit, length);
                            }

                            @Override
                            public void afterTextChanged(Editable s) {

                            }
                        });
                        View positive = dialog.getActionButton(DialogAction.POSITIVE);
                        positive.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String hex = hexEdit.getText().toString();
                                if (!TextUtils.isEmpty(hex)) {
                                    if (hex.length() > 20) {
                                        AppToast.showCenter(BleDeviceActivity.this, R.string.app_tips_bluetooth_data_max_len);
                                    } else if (hex.length() % 2 == 0) {
                                        mBleHelper.writeCharacteristic(UUID.fromString(serviceUUID),
                                                UUID.fromString(characteristicUUID),
                                                HexUtil.hexStringToByteArray(hex));
                                        dialog.dismiss();
                                    } else {
                                        AppToast.showCenter(BleDeviceActivity.this, R.string.app_tips_data_must_be_even);
                                    }
                                } else {
                                    AppToast.showCenter(BleDeviceActivity.this, R.string.app_tips_command_empty);
                                }
                            }
                        });

                        final Button loadCommand = (Button) dialogView.findViewById(R.id.btn_load_command);
                        loadCommand.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final BleCommandInfo[] commandInfos = BleCommandInfo.queryAllCommands();
                                new MaterialDialog.Builder(mContext)
                                        .title(R.string.label_command)
                                        .items(commandInfos)
                                        .itemsCallback(new MaterialDialog.ListCallback() {
                                            @Override
                                            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                                if (!TextUtils.isEmpty(text)) {
                                                    if (text.length() >= 5) {
                                                        BleCommandInfo command = commandInfos[which];
                                                        hexEdit.setText(command.command);
                                                        hexEdit.setSelection(command.command.length());
                                                    }
                                                }
                                            }
                                        })
                                        .show();
                            }
                        });
                        if((charaProp & BluetoothGattCharacteristic.PROPERTY_WRITE)
                                ==  BluetoothGattCharacteristic.PROPERTY_WRITE) {
                            dialog.findViewById(R.id.write_container_view).setVisibility(View.VISIBLE);
                        } else {
                            dialog.findViewById(R.id.write_container_view).setVisibility(View.GONE);
                        }
                    }


                    //读属性值
                    final Button readButton = (Button) dialogView.findViewById(R.id.btn_read_characteristic_data);
                    if (readButton != null) {
                        readButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mBleHelper.readFromCharacteristic(UUID.fromString(serviceUUID), UUID.fromString(characteristicUUID));
                                //dialog.dismiss();
                            }
                        });
                        if((charaProp & BluetoothGattCharacteristic.PROPERTY_READ)
                                == BluetoothGattCharacteristic.PROPERTY_READ ) {
                            dialog.findViewById(R.id.read_container_view).setVisibility(View.VISIBLE);
                        } else {
                            dialog.findViewById(R.id.read_container_view).setVisibility(View.GONE);
                        }
                    }
                    //开启notify
                    final CheckBox notifyCheckBox = (CheckBox) dialogView.findViewById(R.id.cb_notify);


                    List<BluetoothGattDescriptor> descriptors = characteristic.getDescriptors();
                    if (descriptors != null && descriptors.size() > 0) {
                        for (BluetoothGattDescriptor descriptor : descriptors) {
                            if(descriptor.getValue() == BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                                    || descriptor.getValue() == BluetoothGattDescriptor.ENABLE_INDICATION_VALUE ){
                                notifyCheckBox.setChecked(true);
                                notifyCheckBox.setText("Enable");
                            } else {
                                notifyCheckBox.setChecked(false);
                                notifyCheckBox.setText("Disable");
                            }
                        }
                    }
                    if (notifyCheckBox != null) {
                        notifyCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                List<BluetoothGattDescriptor> descriptors = characteristic.getDescriptors();
                                if (descriptors != null && descriptors.size() > 0) {
                                    for (BluetoothGattDescriptor descriptor : descriptors) {
                                        final int properties = characteristic.getProperties();
                                        if ((properties | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
                                            mBleHelper.updateCharacteristicNotification(UUID.fromString(serviceUUID),
                                                    UUID.fromString(characteristicUUID),
                                                    descriptor.getUuid(),
                                                    isChecked);
                                        }
                                    }
                                    if (isChecked) {
                                        sb = new StringBuffer();
                                    }
                                }
                                dialog.dismiss();
                            }

                        });
                        if((charaProp & BluetoothGattCharacteristic.PROPERTY_NOTIFY)
                                == BluetoothGattCharacteristic.PROPERTY_NOTIFY) {
                            dialog.findViewById(R.id.notify_container_view).setVisibility(View.VISIBLE);
                        } else {
                            dialog.findViewById(R.id.notify_container_view).setVisibility(View.GONE);
                        }
                    }


                }
                //是否支持读、写、notify
            }
        };

        // Attach this activity to the Adapter as the ExpandCollapseListener
        mExpandableAdapter.addExpandCollapseListener(this);
        mRecyclerView.setAdapter(mExpandableAdapter);

        //mGattServicesList.setAdapter(gattServiceAdapter);
    }


    @Override
    public void onServiceConnected() {
        connectDevice(mDeviceAddress);
    }


    public void connectDevice(String deviceMac){
        mBleHelper.connectDevice(deviceMac, new ConnectCallback() {
            @Override
            public void onConnectSuccess() {
                mBleHelper.mConnCallback = null;
                dismissProgressDialog();
            }

            @Override
            public void onConnectFailed(ConnectError error) {
                dismissProgressDialog();
                AppToast.show(BleDeviceActivity.this, R.string.app_tips_connect_fail);
            }
        });
    }

    /**
     * Save the instance state of the adapter to keep expanded/collapsed states when rotating or
     * pausing the activity.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mExpandableAdapter.onSaveInstanceState(outState);
    }

    /**
     * Load the expanded/collapsed states of the adapter back into the view when done rotating or
     * resuming the activity.
     */
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mExpandableAdapter.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onListItemExpanded(int position) {

    }

    @Override
    public void onListItemCollapsed(int position) {

    }

    private ArrayList<ParentListItem> getAdapterData(List<BluetoothGattService> gattServices) {
        String unknownServiceString = getResources().getString(R.string.unknown_service);
        String unknownCharaString = getResources().getString(R.string.unknown_characteristic);
        ArrayList<ParentListItem> parentObjectList = new ArrayList<>();
        for (BluetoothGattService gattService : gattServices) {
            String serviceUUID = gattService.getUuid().toString();
            ArrayList<Object> childObjectList = new ArrayList<>();

            List<BluetoothGattCharacteristic> gattCharacteristics = gattService.getCharacteristics();

            for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                VerticalChildObject verticalChildObject = new VerticalChildObject();
                String uuid = gattCharacteristic.getUuid().toString();
                mCharacteristicsMap.put(uuid, gattCharacteristic);
                verticalChildObject.mUUIDText = uuid;
                verticalChildObject.mNameText = SampleGattAttributes.lookup(uuid, unknownCharaString);
                verticalChildObject.mPermissionText = getString(R.string.label_lbe_permissions,
                        BleUtils.getPermission(gattCharacteristic.getPermissions()));
                verticalChildObject.mPropertyText = getString(R.string.label_lbe_properties,
                        BleUtils.getProperties(gattCharacteristic.getProperties()));
                verticalChildObject.mWriteTypeText = getString(R.string.label_lbe_write_type,
                        BleUtils.getWriteType(gattCharacteristic.getWriteType()));
                childObjectList.add(verticalChildObject);
            }


            VerticalParentObject verticalParentObject = new VerticalParentObject();
            verticalParentObject.mNameText = SampleGattAttributes.lookup(serviceUUID, unknownServiceString);
            verticalParentObject.mUUIDText = serviceUUID;
            verticalParentObject.setChildObjectList(childObjectList);
            parentObjectList.add(verticalParentObject);
        }
        return parentObjectList;
    }

    private StringBuffer sb;
    public void onShare(View view) {
        if (sb != null && sb.length() > 0) {

            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, sb.toString());
            sendIntent.setType("text/plain");
            startActivity(sendIntent);

        } else {
            AppToast.show(this, "Ble data is empty");
        }
    }

    public void onClear(View view) {
        if(sb != null) {
            sb = new StringBuffer();
        }
        logInfoList.clear();
        initLogHeight();
    }

    //5条空数据 为了可以显示BottomSheetDialog
    private void initLogHeight(){
        logInfoList.add("");
        logInfoList.add("");
        logInfoList.add("");
        logInfoList.add("");
        logInfoList.add("");
    }

    protected void invalidateInputMinMaxIndicator(EditText input, int currentLength) {
        if (input != null) {
            int materialBlue = ContextCompat.getColor(this, com.afollestad.materialdialogs.R.color.md_material_blue_600);
            int widgetColor = DialogUtils.resolveColor(this, com.afollestad.materialdialogs.R.attr.colorAccent, materialBlue);

            if (Build.VERSION.SDK_INT >= 21) {
                widgetColor = DialogUtils.resolveColor(this, android.R.attr.colorAccent, widgetColor);
            }
            final boolean isDisabled = currentLength > 20;
            final int colorText = isDisabled ? ContextCompat.getColor(this, R.color.red)
                    : -1;
            final int colorWidget = isDisabled ? ContextCompat.getColor(this, R.color.red)
                    : widgetColor;
            input.setTextColor(colorText);
            MDTintHelper.setTint(input, colorWidget);
        }
    }

    List<String> logInfoList = new ArrayList<String>();
    BottomSheetLogView logView = null;
    //蓝牙日志
    public void onShowLogView(View view){
        if(logInfoList.size() < 5) {
            initLogHeight();
        }

        logView = BottomSheetLogView.show(this, logInfoList);
        if(!logView.isShowing()) {
            logView.show();
        }
    }

    private void appendLog(String log) {
        logInfoList.add(DateUtils.formatDateDefault(System.currentTimeMillis()) + log);
        if (logView != null) {
            logView.appendLog(DateUtils.formatDateDefault(System.currentTimeMillis()) + log);
        }
    }
}
