package com.github.captain_miao.android.bluetoothletutorial.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import com.afollestad.materialdialogs.internal.MDTintHelper;
import com.afollestad.materialdialogs.util.DialogUtils;
import com.github.captain_miao.android.bluetoothletutorial.R;
import com.github.captain_miao.android.bluetoothletutorial.adapter.DeviceCommandAdapter;
import com.github.captain_miao.android.bluetoothletutorial.constant.AppConstants;
import com.github.captain_miao.android.bluetoothletutorial.model.BleCommandInfo;
import com.github.captain_miao.android.supportsdk.app.AppToast;

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
                        .title(R.string.label_action_add_command)
                        .customView(R.layout.dialog_customview_command, true)
                        .positiveText(R.string.label_ok)
                        .cancelable(true)
                        .negativeText(R.string.label_cancel)
                        .show();
                View dialogView = dialog.getCustomView();
                if (dialogView != null) {
                    final EditText nameEdit = (EditText) dialogView.findViewById(R.id.et_command_name);
                    final EditText hexEdit = (EditText) dialogView.findViewById(R.id.et_command_data);
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
                            String name = nameEdit.getText().toString();
                            String hex = hexEdit.getText().toString();
                            if (!TextUtils.isEmpty(hex)) {
                                if (hex.length() > 20) {
                                    AppToast.showCenter(getActivity(), R.string.app_tips_bluetooth_data_max_len);
                                } else {
                                    if (!TextUtils.isEmpty(name)) {
                                        BleCommandInfo cmd = new BleCommandInfo(name, hex);
                                        cmd.save();
                                        mAdapter.append(cmd);
                                        mAdapter.notifyItemInserted(mAdapter.getItemCount() - 1);
                                        dialog.dismiss();
                                    } else {
                                        AppToast.showCenter(getActivity(), R.string.app_tips_name_empty);
                                    }
                                }
                            } else {
                                AppToast.showCenter(getActivity(), R.string.app_tips_command_empty);
                            }
                        }
                    });
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    protected void invalidateInputMinMaxIndicator(EditText inputMinMax, int currentLength) {
        if (inputMinMax != null) {
            int materialBlue = ContextCompat.getColor(getActivity(), com.afollestad.materialdialogs.R.color.md_material_blue_600);
            int widgetColor = DialogUtils.resolveColor(getActivity(), com.afollestad.materialdialogs.R.attr.colorAccent, materialBlue);

            if (Build.VERSION.SDK_INT >= 21) {
                widgetColor = DialogUtils.resolveColor(getActivity(), android.R.attr.colorAccent, widgetColor);
            }
            final boolean isDisabled = currentLength > 20;
            final int colorText = isDisabled ? ContextCompat.getColor(getActivity(), R.color.red)
                    : -1;
            final int colorWidget = isDisabled ? ContextCompat.getColor(getActivity(), R.color.red)
                    : widgetColor;
            inputMinMax.setTextColor(colorText);
            MDTintHelper.setTint(inputMinMax, colorWidget);
        }
    }

}
