package com.github.captain_miao.android.bluetoothletutorial.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.github.captain_miao.android.bluetoothletutorial.R;
import com.github.captain_miao.android.bluetoothletutorial.model.FavouriteInfo;
import com.github.captain_miao.android.supportsdk.refresh.BaseLoadMoreRecyclerAdapter;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * @author YanLu
 * @since 15/9/15
 */
public class DeviceConfigAdapter extends BaseLoadMoreRecyclerAdapter<FavouriteInfo, DeviceConfigAdapter.ItemViewHolder> implements View.OnClickListener {

    private Context mContext;
    private RecyclerView mRecyclerView;
    public DeviceConfigAdapter(Context context, RecyclerView recyclerView, List<FavouriteInfo> items) {
        mContext = context;
        mRecyclerView = recyclerView;
        appendToList(items);
    }


    @Override
    public ItemViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.device_config_item_view, parent, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int position = mRecyclerView.getChildAdapterPosition(v);
                final FavouriteInfo bleDevice = getItem(position);
                new MaterialDialog.Builder(mContext)
                        .title("Setting name")
                        .input("input name",
                                TextUtils.isEmpty(bleDevice.name) ? "" : bleDevice.name,
                                new MaterialDialog.InputCallback() {
                                    @Override
                                    public void onInput(MaterialDialog materialDialog, CharSequence charSequence) {
                                        bleDevice.name = charSequence.toString();
                                        bleDevice.save();
                                        notifyItemChanged(position);
                                    }
                                })
                        .positiveText(R.string.label_ok)
                        .cancelable(true)
                        .negativeText(R.string.label_cancel)
                        .show();
            }
        });

        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final int position = mRecyclerView.getChildAdapterPosition(v);
                final FavouriteInfo bleDevice = getItem(position);

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
        return new DeviceConfigAdapter.ItemViewHolder(view);
    }


    @Override
    public void onBindItemViewHolder(DeviceConfigAdapter.ItemViewHolder vh, int position) {
        FavouriteInfo bleDevice = getItem(position);

        vh.mTvName.setText(bleDevice.name);
        vh.mIvFavourite.setImageResource(
                bleDevice.isFavourite ? R.drawable.icon_favourite_on : R.drawable.icon_favourite_off);
        vh.mTvMac.setText(mContext.getString(R.string.ble_mac_label, bleDevice.address));
        vh.mIvFavourite.setTag(position);
        vh.mIvFavourite.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        final int position = (int) v.getTag();
        final FavouriteInfo bleDevice = getItem(position);
        bleDevice.isFavourite = !bleDevice.isFavourite;

        bleDevice.save();
        notifyItemChanged(position);
    }


    public static class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mTvName;
        public ImageView mIvFavourite;
        public TextView mTvMac;

        public ItemViewHolder(View view) {
            super(view);
            mTvName = (TextView) view.findViewById(R.id.tv_name);
            mIvFavourite = (ImageView) view.findViewById(R.id.iv_favourite);
            mTvMac = (TextView) view.findViewById(R.id.tv_mac);
            //view.setOnClickListener(this);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTvMac.getText();
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();

        }
    }
}
