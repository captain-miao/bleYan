package com.github.captain_miao.android.bluetoothletutorial;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.github.captain_miao.android.bluetoothletutorial.constant.AppConstants;
import com.github.captain_miao.android.bluetoothletutorial.fragment.BleLogFragment;
import com.github.captain_miao.android.bluetoothletutorial.model.BleDevice;
import com.github.captain_miao.android.supportsdk.BaseActivity;

import java.util.ArrayList;

/**
 * @author YanLu
 * @since 16/8/22
 */
public class DeviceCommunicateActivity extends BaseActivity implements OnTabSelectListener {
    private static final String TAG = "DeviceCommunicate";

    private BleDevice mDevice;
    private String mDeviceName;
    private String mDeviceAddress;


    private Context mContext = this;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private final String[] mTitles = {
            "Device", "Data", "Log"
    };
    private DevicePagerAdapter mAdapter;
    @Override
    public void init(Bundle savedInstanceState) {
        setContentView(R.layout.act_device_commnunicate);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        mDevice = (BleDevice) getIntent().getSerializableExtra(AppConstants.KEY_BLE_DEVICE);
        //mDeviceName = TextUtils.isEmpty(mDevice.name) ? mDevice.address : mDevice.name;
        //mDeviceAddress = mDevice.address;

        //setTitle(mDeviceName);


        for (String title : mTitles) {
            mFragments.add(BleLogFragment.getInstance(title));
        }


        ViewPager vp = (ViewPager) findViewById(R.id.vp);
        mAdapter = new DevicePagerAdapter(getSupportFragmentManager());
        vp.setAdapter(mAdapter);

        SlidingTabLayout tabLayout_1 = (SlidingTabLayout) findViewById(R.id.tl_1);


        tabLayout_1.setViewPager(vp, mTitles, this, mFragments);

        tabLayout_1.setOnTabSelectListener(this);

        vp.setCurrentItem(0);
    }

    @Override
    public void onTabSelect(int position) {

    }

    @Override
    public void onTabReselect(int position) {

    }


    private class DevicePagerAdapter extends FragmentPagerAdapter {
        public DevicePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
    }
}
