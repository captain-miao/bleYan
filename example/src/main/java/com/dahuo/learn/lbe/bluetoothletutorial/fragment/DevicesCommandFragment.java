package com.dahuo.learn.lbe.bluetoothletutorial.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.activeandroid.query.Select;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.dahuo.learn.lbe.bluetoothletutorial.R;
import com.dahuo.learn.lbe.bluetoothletutorial.adapter.DeviceCommandAdapter;
import com.dahuo.learn.lbe.bluetoothletutorial.constant.AppConstants;
import com.dahuo.learn.lbe.bluetoothletutorial.model.BleCommandInfo;
import com.dahuo.learn.lbe.supportsdk.app.AppToast;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Yan Lu
 * @since 2015-10-10
 */

public class DevicesCommandFragment extends BaseFragment {
    private static final String TAG = DevicesCommandFragment.class.getSimpleName();
    private String mTitle;

    private RecyclerView mRecyclerView;
    private List<BleCommandInfo> mDataList = new ArrayList<>();
    private DeviceCommandAdapter mAdapter;
    public DevicesCommandFragment() {
    }


    public static DevicesCommandFragment newInstance(String title) {
        DevicesCommandFragment f = new DevicesCommandFragment();

        Bundle args = new Bundle();

        args.putString(AppConstants.KEY_TITLE, title);
        f.setArguments(args);

        return (f);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if(getArguments() != null) {
            mTitle = getArguments().getString(AppConstants.KEY_TITLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frg_command_config, null);
        mRecyclerView =  (RecyclerView) rootView.findViewById(R.id.recycler_view);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        List<BleCommandInfo> infoList = new Select()
                .from(BleCommandInfo.class)
                .orderBy("Id desc")
                .execute();
        if(infoList != null) {
            mDataList.addAll(infoList);
        }
        mAdapter = new DeviceCommandAdapter(getActivity(), mRecyclerView, mDataList);
        mAdapter.setHasMoreData(false);
        mAdapter.setHasFooter(false);
        mRecyclerView.setAdapter(mAdapter);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.ble_command_action, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ble_command_action_add:
                final MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                        .title("Add Command")
                        .customView(R.layout.dialog_customview_command, true)
                        .positiveText(R.string.label_ok)
                        .cancelable(true)
                        .negativeText(R.string.label_cancel)
                        .show();
                View dialogView = dialog.getCustomView();
                if (dialogView != null) {
                    final EditText nameEdit = (EditText) dialogView.findViewById(R.id.et_command_name);
                    final EditText hexEdit = (EditText) dialogView.findViewById(R.id.et_command_data);

                    View positive = dialog.getActionButton(DialogAction.POSITIVE);
                    positive.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String name = nameEdit.getText().toString();
                            String hex = hexEdit.getText().toString();
                            if (!TextUtils.isEmpty(hex)) {
                                if (!TextUtils.isEmpty(name)) {
                                    BleCommandInfo cmd = new BleCommandInfo(name, hex);
                                    cmd.save();
                                    mAdapter.append(cmd);
                                    mAdapter.notifyItemInserted(mAdapter.getItemCount() - 1);
                                    dialog.dismiss();
                                } else {
                                    AppToast.showCenter(getActivity(), "command is empty");
                                }
                            } else {
                                AppToast.showCenter(getActivity(), "name is empty");
                            }
                        }
                    });
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
