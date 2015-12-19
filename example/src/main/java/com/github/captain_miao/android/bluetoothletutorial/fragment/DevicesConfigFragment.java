package com.github.captain_miao.android.bluetoothletutorial.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.activeandroid.query.Select;
import com.github.captain_miao.android.bluetoothletutorial.R;
import com.github.captain_miao.android.bluetoothletutorial.adapter.DeviceConfigAdapter;
import com.github.captain_miao.android.bluetoothletutorial.constant.AppConstants;
import com.github.captain_miao.android.bluetoothletutorial.model.FavouriteInfo;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Yan Lu
 * @since 2015-10-10
 */

public class DevicesConfigFragment extends BaseFragment {
    private static final String TAG = DevicesConfigFragment.class.getSimpleName();
    private String mTitle;

    private RecyclerView mRecyclerView;
    private List<FavouriteInfo> mDataList = new ArrayList<>();
    private DeviceConfigAdapter mAdapter;
    public DevicesConfigFragment() {
    }


    public static DevicesConfigFragment newInstance(String title) {
        DevicesConfigFragment f = new DevicesConfigFragment();

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frg_device_config, null);
        mRecyclerView =  (RecyclerView) rootView.findViewById(R.id.recycler_view);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        List<FavouriteInfo> infoList = new Select()
                .from(FavouriteInfo.class)
                .orderBy("Id desc")
                .execute();
        if(infoList != null) {
            mDataList.addAll(infoList);
        }
        mAdapter = new DeviceConfigAdapter(getActivity(), mRecyclerView, mDataList);
        mAdapter.setHasMoreData(false);
        mAdapter.setHasFooter(false);
        mRecyclerView.setAdapter(mAdapter);
    }
}
