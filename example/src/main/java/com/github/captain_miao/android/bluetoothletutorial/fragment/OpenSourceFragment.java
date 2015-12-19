package com.github.captain_miao.android.bluetoothletutorial.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.captain_miao.android.bluetoothletutorial.R;
import com.github.captain_miao.android.bluetoothletutorial.constant.AppConstants;
import com.github.captain_miao.android.supportsdk.utils.DeviceUtilsLite;


/**
 * @author Yan Lu
 * @since 2015-07-23
 */

public class OpenSourceFragment extends BaseFragment {
    private static final String TAG = OpenSourceFragment.class.getSimpleName();
    private String mTitle;
    private TextView mTvTitle;

    public OpenSourceFragment() {
    }


    public static OpenSourceFragment newInstance(String title) {
        OpenSourceFragment f = new OpenSourceFragment();

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
        View rootView = inflater.inflate(R.layout.frg_about, null);
        mTvTitle = (TextView) rootView.findViewById(R.id.tvVersion);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mTvTitle.setText(getString(R.string.app_version_about,
      				DeviceUtilsLite.getAppVersionName(getActivity())));
    }



}
