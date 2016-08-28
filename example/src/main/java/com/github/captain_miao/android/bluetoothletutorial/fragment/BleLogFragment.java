package com.github.captain_miao.android.bluetoothletutorial.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.captain_miao.android.bluetoothletutorial.R;
import com.github.captain_miao.recyclerviewutils.WrapperRecyclerView;

import java.util.ArrayList;
import java.util.List;


@SuppressLint("ValidFragment")
public class BleLogFragment extends BleFragment {
    private String mTitle;
    private WrapperRecyclerView mRefreshRecyclerView;

    private List<String> mDataList = new ArrayList<>();

    @Override
    public void onAppendLog(String log) {
        mDataList.add(log);
    }

    public static BleLogFragment getInstance(String title) {
        BleLogFragment frg = new BleLogFragment();
        frg.mTitle = title;
        return frg;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frg_ble_log, container, false);
        mRefreshRecyclerView = (WrapperRecyclerView) v.findViewById(R.id.refresh_recycler_view);

        return v;
    }
}