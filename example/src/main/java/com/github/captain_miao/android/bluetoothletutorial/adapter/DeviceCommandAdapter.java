package com.github.captain_miao.android.bluetoothletutorial.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.captain_miao.android.bluetoothletutorial.R;
import com.github.captain_miao.android.bluetoothletutorial.model.BleCommandInfo;
import com.github.captain_miao.android.supportsdk.app.AppToast;
import com.github.captain_miao.android.supportsdk.refresh.BaseLoadMoreRecyclerAdapter;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * @author YanLu
 * @since 15/9/15
 */
public class DeviceCommandAdapter extends BaseLoadMoreRecyclerAdapter<BleCommandInfo, DeviceCommandAdapter.ItemViewHolder> {

    private Context mContext;
    private RecyclerView mRecyclerView;
    public DeviceCommandAdapter(Context context, RecyclerView recyclerView, List<BleCommandInfo> items) {
        mContext = context;
        mRecyclerView = recyclerView;
        appendToList(items);
    }


    @Override
    public ItemViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.device_command_item_view, parent, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int position = mRecyclerView.getChildAdapterPosition(v);
                final BleCommandInfo bleDevice = getItem(position);
                final MaterialDialog dialog = new MaterialDialog.Builder(mContext)
                        .title("Edit Command")
                        .customView(R.layout.dialog_customview_command, true)
                        .positiveText(R.string.label_ok)
                        .cancelable(true)
                        .negativeText(R.string.label_cancel)
                        .show();
                View dialogView = dialog.getCustomView();
                if (dialogView != null) {
                    final EditText nameEdit = (EditText) dialogView.findViewById(R.id.et_command_name);
                    final EditText hexEdit = (EditText) dialogView.findViewById(R.id.et_command_data);
                    nameEdit.setText(bleDevice.name);
                    hexEdit.setText(bleDevice.command);
                    View positive = dialog.getActionButton(DialogAction.POSITIVE);
                    positive.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String name = nameEdit.getText().toString();
                            String command = hexEdit.getText().toString();
                            if (!TextUtils.isEmpty(command)) {
                                if (!TextUtils.isEmpty(name)) {
                                    bleDevice.name = name;
                                    bleDevice.command = command;
                                    //BleCommandInfo cmd = new BleCommandInfo(name, command);
                                    bleDevice.save();
                                    notifyItemChanged(position);
                                    dialog.dismiss();
                                } else {
                                    AppToast.showCenter(mContext, "command is empty");
                                }
                            } else {
                                AppToast.showCenter(mContext, "name is empty");
                            }
                        }
                    });
                }
            }
        });

        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final int position = mRecyclerView.getChildAdapterPosition(v);
                final BleCommandInfo bleDevice = getItem(position);

                new SweetAlertDialog(mContext, SweetAlertDialog.NORMAL_TYPE)
                        .setTitleText("delete")
                        .setContentText("delete it?")
                        .setCancelText("CANCEL")
                        .setConfirmText("OK")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                remove(position);
                                notifyItemRemoved(position);
                                bleDevice.delete();
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .show();

                return true;
            }
        });
        return new DeviceCommandAdapter.ItemViewHolder(view);
    }


    @Override
    public void onBindItemViewHolder(DeviceCommandAdapter.ItemViewHolder vh, int position) {
        BleCommandInfo bleDevice = getItem(position);

        vh.mTvName.setText(bleDevice.name);
        vh.mTvCommand.setText(mContext.getString(R.string.ble_command_label, bleDevice.command));
    }




    public static class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mTvName;
        public TextView mTvCommand;

        public ItemViewHolder(View view) {
            super(view);
            mTvName = (TextView) view.findViewById(R.id.tv_name);
            mTvCommand = (TextView) view.findViewById(R.id.tv_command);
            //view.setOnClickListener(this);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTvCommand.getText();
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();

        }
    }
}
