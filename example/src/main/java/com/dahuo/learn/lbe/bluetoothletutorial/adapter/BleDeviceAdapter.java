package com.dahuo.learn.lbe.bluetoothletutorial.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.dahuo.learn.lbe.bluetoothletutorial.BleDeviceActivity;
import com.dahuo.learn.lbe.bluetoothletutorial.R;
import com.dahuo.learn.lbe.bluetoothletutorial.constant.AppConstants;
import com.dahuo.learn.lbe.bluetoothletutorial.model.BleDevice;
import com.dahuo.learn.lbe.bluetoothletutorial.model.FavouriteInfo;
import com.dahuo.learn.lbe.supportsdk.refresh.BaseLoadMoreRecyclerAdapter;

import java.util.List;

/**
 * @author YanLu
 * @since 15/9/15
 */
public class BleDeviceAdapter extends BaseLoadMoreRecyclerAdapter<BleDevice, BleDeviceAdapter.ItemViewHolder> implements View.OnClickListener {

    private Context mContext;
    private RecyclerView mRecyclerView;
    public BleDeviceAdapter(Context context, RecyclerView recyclerView, List<BleDevice> items) {
        mContext = context;
        mRecyclerView = recyclerView;
        appendToList(items);
    }


    @Override
    public ItemViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ble_item_view, parent, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = mRecyclerView.getChildAdapterPosition(v);
                mContext.startActivity(new Intent(mContext, BleDeviceActivity.class)
                .putExtra(AppConstants.KEY_BLE_DEVICE, getItem(position)));
            }
        });
        return new BleDeviceAdapter.ItemViewHolder(view);
    }


    @Override
    public void onBindItemViewHolder(BleDeviceAdapter.ItemViewHolder vh, int position) {
        BleDevice bleDevice = getItem(position);
        boolean isEnable = (System.currentTimeMillis() - bleDevice.updateTime) < 60 * 1000;
        vh.mTvName.setText(
                TextUtils.isEmpty(bleDevice.name) ?
                        mContext.getString(R.string.ble_unknown)
                        : bleDevice.name + "(" + bleDevice.aliasName  + ")");
        vh.mIvFavourite.setImageResource(
                bleDevice.isFavourite ? R.drawable.icon_favourite_on : R.drawable.icon_favourite_off);
        vh.mTvMac.setText(mContext.getString(R.string.ble_mac_label, bleDevice.address));
        setBleValue(vh.mTvdBm, bleDevice.rssi, isEnable);
        vh.mTvBroadcast.setText(mContext.getString(R.string.ble_broadcast_label, bleDevice.broadcast));
        vh.mTvName.setEnabled(isEnable);
        vh.mTvMac.setEnabled(isEnable);
        vh.mTvdBm.setEnabled(isEnable);
        vh.mTvBroadcast.setEnabled(isEnable);
        vh.mIvFavourite.setTag(position);
        vh.mIvFavourite.setOnClickListener(this);
    }


    @SuppressWarnings(value={"deprecation"})
    private void setBleValue(TextView tv, int value, boolean enable) {
        if (!enable) {
            tv.setTextColor(mContext.getResources().getColor(R.color.light_grey));
        } else if (value > -70) {
            tv.setTextColor(mContext.getResources().getColor(R.color.ble_dbm_green));
        } else if (value > -90) {
            tv.setTextColor(mContext.getResources().getColor(R.color.ble_dbm_yellow));
        } else {
            tv.setTextColor(mContext.getResources().getColor(R.color.ble_dbm_red));
        }
        tv.setText(mContext.getString(R.string.ble_dbm_label, value));
    }

    @Override
    public void onClick(View v) {
        final int position = (int) v.getTag();
        final BleDevice bleDevice = getItem(position);
        bleDevice.isFavourite = !bleDevice.isFavourite;
        if(bleDevice.isFavourite){
            new MaterialDialog.Builder(mContext)
                    .title("Setting name")
                    .input("input name",
                            TextUtils.isEmpty(bleDevice.aliasName) ? "" : bleDevice.aliasName,
                            new MaterialDialog.InputCallback() {
                                @Override
                                public void onInput(MaterialDialog materialDialog, CharSequence charSequence) {
                                    new FavouriteInfo(bleDevice.address, charSequence.toString(), bleDevice.isFavourite).save();
                                    bleDevice.aliasName = charSequence.toString();
                                    notifyItemChanged(position);
                                }
                            })
                    .positiveText(R.string.label_ok)
                    .cancelable(true)
                    .negativeText(R.string.label_cancel)
                    .show();
        } else {
            FavouriteInfo favourite = FavouriteInfo.getFavourite(bleDevice.address);
            new FavouriteInfo(bleDevice.address, favourite.name, false).save();
            notifyItemChanged(position);
        }
    }


    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        public TextView mTvName;
        public ImageView mIvFavourite;
        public TextView mTvMac;
        public TextView mTvdBm;
        public TextView mTvBroadcast;

        public ItemViewHolder(View view) {
            super(view);
            mTvName = (TextView) view.findViewById(R.id.tv_name);
            mIvFavourite = (ImageView) view.findViewById(R.id.iv_favourite);
            mTvMac = (TextView) view.findViewById(R.id.tv_mac);
            mTvdBm = (TextView) view.findViewById(R.id.tv_dbm);
            mTvBroadcast = (TextView) view.findViewById(R.id.tv_broadcast);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTvMac.getText();
        }
    }
}
