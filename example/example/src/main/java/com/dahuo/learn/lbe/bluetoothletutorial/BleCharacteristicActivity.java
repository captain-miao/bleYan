package com.dahuo.learn.lbe.bluetoothletutorial;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.dahuo.learn.lbe.bluetoothletutorial.ble.AppBluetoothHelper;
import com.dahuo.learn.lbe.bluetoothletutorial.constant.AppConstants;
import com.dahuo.learn.lbe.supportsdk.BaseActivity;

import java.util.UUID;

/**
 * Created by on 15/8/3.
 */
public class BleCharacteristicActivity extends BaseActivity implements View.OnClickListener{
    private static final String TAG = BleCharacteristicActivity.class.getSimpleName();

    private TextView mTVConnectionState;
    private EditText mEvSendData;
    private TextView mDataField;


    private AppBluetoothHelper mBleHelper;
    private UUID serviceUUID;
    private UUID characteristicUUID;
    @Override
    public void init(Bundle savedInstanceState) {
        setContentView(R.layout.act_characteristic);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        serviceUUID = UUID.fromString(getIntent().getStringExtra(AppConstants.KEY_BLE_SERVICE));
        characteristicUUID = UUID.fromString(getIntent().getStringExtra(AppConstants.KEY_BLE_CHARACTERISTIC));
        if (serviceUUID == null || characteristicUUID == null) {
            finish();
            return;
        }

        setTitle(characteristicUUID.toString());
        initView();
        showProgressDialog();
    }




    private void initView() {
        //findViewById(R.id.btn_check_device).setOnClickListener(this);
        // Sets up UI references.
        mTVConnectionState = (TextView) findViewById(R.id.connection_state);
        mEvSendData = (EditText) findViewById(R.id.write_data_value);
        mDataField = (TextView) findViewById(R.id.data_value);
   	}


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
   	public void onClick(View v) {

   		switch (v.getId()) {

        }
   	}

}
