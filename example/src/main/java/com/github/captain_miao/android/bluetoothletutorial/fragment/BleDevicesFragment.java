package com.github.captain_miao.android.bluetoothletutorial.fragment;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.captain_miao.grantap.ListenerPermission;
import com.example.captain_miao.grantap.listeners.PermissionListener;
import com.github.captain_miao.android.ble.BleScanner;
import com.github.captain_miao.android.ble.SimpleScanCallback;
import com.github.captain_miao.android.ble.constant.BleScanState;
import com.github.captain_miao.android.ble.utils.BleLog;
import com.github.captain_miao.android.ble.utils.HexUtil;
import com.github.captain_miao.android.bluetoothletutorial.R;
import com.github.captain_miao.android.bluetoothletutorial.adapter.BleDeviceAdapter;
import com.github.captain_miao.android.bluetoothletutorial.constant.AppConstants;
import com.github.captain_miao.android.bluetoothletutorial.model.BleDevice;
import com.github.captain_miao.android.bluetoothletutorial.model.FavouriteInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * @author Yan Lu
 * @since 2015-07-23
 */

public class BleDevicesFragment extends BaseFragment implements SimpleScanCallback, PermissionListener {
    private static final String TAG = BleDevicesFragment.class.getSimpleName();

    private static final int REQUEST_CODE_OPEN_BLE = 1;
    private static final int REQUEST_CODE_OPEN_GPS = 2;

    private String mTitle;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private List<BleDevice> mDataList = new ArrayList<>();
    private BleDeviceAdapter mAdapter;
    private BleScanner mBleScanner;
    private MenuItem mScanAction;

    public BleDevicesFragment() {
    }

    public static BleDevicesFragment newInstance(String title) {
        BleDevicesFragment f = new BleDevicesFragment();

        Bundle args = new Bundle();

        args.putString(AppConstants.KEY_TITLE, title);
        f.setArguments(args);

        return (f);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            mTitle = getArguments().getString(AppConstants.KEY_TITLE);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frg_swipe_refresh_list, null);
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.activity_main_swipe_refresh_layout);
        mRecyclerView =  (RecyclerView) rootView.findViewById(R.id.recycler_view);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //设置加载圈圈的颜色
        mSwipeRefreshLayout.setColorSchemeResources(R.color.line_color_run_speed_13);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mBleScanner != null) {
                    mBleScanner.stopBleScan();
                } else {
                    mBleScanner = new BleScanner(getContext(), BleDevicesFragment.this);
                }
                bleDeviceHashMap.clear();
                mAdapter.clear();
                mAdapter.notifyDataSetChanged();
                checkPermissionAndStartScan();
                //
                mSwipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 1000);//1秒
            }
        });

        //mDataList.add(mTitle);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new BleDeviceAdapter(getActivity(), mRecyclerView, mDataList);
        mAdapter.setHasMoreData(false);
        mAdapter.setHasFooter(false);
        mRecyclerView.setAdapter(mAdapter);
        mBleScanner = new BleScanner(getContext(), BleDevicesFragment.this);
        //checkPermissionAndStartScan();
    }


    @Override
    public void onResume() {
        super.onResume();
        //if(mBleScanner != null && mBleScanner.isScanning()){
            // TODO: 16/4/20
            //checkPermissionAndStartScan();
        //}
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mBleScanner != null){
            mBleScanner.stopBleScan();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.ble_scan_action, menu);
        mScanAction = menu.findItem(R.id.ble_action_start);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ble_action_start:
                if (mBleScanner.isScanning()) {
                    item.setTitle(R.string.app_ble_scan_start);
                    mBleScanner.stopBleScan();
                } else {
                    item.setTitle(R.string.app_ble_scan_stop);
                    bleDeviceHashMap.clear();
                    mAdapter.clear();
                    mAdapter.notifyDataSetChanged();
                    checkPermissionAndStartScan();
                }
                break;
            }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBleScan(final BluetoothDevice device, final int rssi, final byte[] scanRecord) {
        if(isVisible()) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    BleLog.i(TAG, device.toString() + " rssi: " + rssi);
                    String address = device.getAddress();
                    if (bleDeviceHashMap.get(address) == null) {
                        bleDeviceHashMap.put(address, device);
                        FavouriteInfo favourite = FavouriteInfo.getFavourite(address);
                        BleDevice bleDevice = new BleDevice(address, device.getAddress(),
                                rssi, HexUtil.encodeHexStr(scanRecord), favourite.isFavourite);
                        bleDevice.aliasName = (TextUtils.isEmpty(favourite.name) ? "" : (favourite.name));

                        mAdapter.append(bleDevice);
                        try {
                            mAdapter.notifyItemInserted(mAdapter.getItemCount() - 1);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        //更新rssi
                        List<BleDevice> mList = mAdapter.getList();
                        int i = 0;
                        for (BleDevice bleDevice : mList) {

                            if (bleDevice.address.equalsIgnoreCase(device.getAddress())) {
                                //控制刷新
                                long now = System.currentTimeMillis();
                                if (rssi != bleDevice.rssi && (now - bleDevice.updateTime > 1000)) {
                                    bleDevice.updateTime = now;
                                    bleDevice.rssi = rssi;
                                    bleDevice.broadcast = HexUtil.encodeHexStr(scanRecord);
                                    try {
                                        mAdapter.notifyItemChanged(i);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    //mAdapter.notifyDataSetChanged();
                                }
                                break;
                            }
                            i++;
                        }
                    }
                }
            });
        }
    }

    @Override
    public void onBleScanFailed(BleScanState scanState) {
        Toast.makeText(getContext(), scanState.getMessage(), Toast.LENGTH_LONG).show();
    }


    private HashMap<String, BluetoothDevice> bleDeviceHashMap = new HashMap<>();




    private void checkPermissions(){
        ListenerPermission.from(getActivity())
        .setPermissionListener(this)
        .setPermissions(permissions)
        .setRationaleMsg(R.string.label_request_permission_content)
        .setRationaleConfirmText(R.string.dialog_ok)
        .setDeniedMsg(R.string.label_permission_denial_content)
        .setDeniedCloseButtonText(R.string.label_ok)
        .setGotoSettingButton(false)
        .check();
    }


    String[] permissions = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION};
    public void checkPermissionAndStartScan() {
        checkPermissions();
    }


    @Override
   	public void onActivityResult(int requestCode, int resultCode, Intent data) {
   		if (requestCode == REQUEST_CODE_OPEN_BLE && resultCode == Activity.RESULT_OK) {
   		    // open gps
            if (isGpsOPen(getContext())) {
                permissionGranted();
            } else {
                // 开启GPS
                displayPromptForEnablingGPS();
            }

        } else if (requestCode == REQUEST_CODE_OPEN_GPS && resultCode == Activity.RESULT_OK) {
            // 开启GPS
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (bluetoothAdapter != null) {
                permissionGranted();
            }
        } else {
   			super.onActivityResult(requestCode, resultCode, data);
   		}
   	}


    public void displayPromptForEnablingGPS() {
        final String action = Settings.ACTION_LOCATION_SOURCE_SETTINGS;


        new MaterialDialog.Builder(getActivity())
                .title(R.string.label_request_open_gps_title)
                .content(R.string.label_request_open_gps_content)
                .positiveText(R.string.label_ok)
                .onPositive(new MaterialDialog.SingleButtonCallback() {

                    @Override
                    public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                        startActivityForResult(new Intent(action), REQUEST_CODE_OPEN_GPS);
                    }
                })
                .negativeText(R.string.label_cancel)
                .cancelable(true)
                .negativeText(R.string.label_cancel)
                .show();
    }


    /**
     * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
     *
     * @param context
     * @return true 表示开启
     */
    public static final boolean isGpsOPen(final Context context) {
        LocationManager locationManager
                = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps || network) {
            return true;
        }

        return false;
    }

    @Override
    public void permissionGranted() {
        //请求打开蓝牙
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter != null && !BluetoothAdapter.getDefaultAdapter().isEnabled()) {
            Intent mIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(mIntent, 1);
        } else if (bluetoothAdapter != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (isGpsOPen(getContext())) {
                    // permissions is already available
                    mBleScanner.startBleScan();
                } else {
                    displayPromptForEnablingGPS();
                }
            } else {
                // permissions is already available
                mBleScanner.startBleScan();
            }
        }
    }

    @Override
    public void permissionDenied() {
        if(mScanAction != null) {
            mScanAction.setTitle(R.string.app_ble_scan_start);
        }
        if (mBleScanner != null) {
            mBleScanner.stopBleScan();
        }
        bleDeviceHashMap.clear();
        mAdapter.clear();
        mAdapter.notifyDataSetChanged();
    }
}
