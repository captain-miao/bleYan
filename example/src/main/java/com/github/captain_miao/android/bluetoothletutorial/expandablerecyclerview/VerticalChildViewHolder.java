package com.github.captain_miao.android.bluetoothletutorial.expandablerecyclerview;

import android.view.View;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.github.captain_miao.android.bluetoothletutorial.R;


/**
 * Custom child ViewHolder. Any views should be found and set to public variables here to be
 * referenced in your custom ExpandableAdapter later.
 * <p>
 * Must extend ChildViewHolder.
 *
 * @author YanLu
 * @version 1.0
 * @since 19/9/2015
 */
public abstract class VerticalChildViewHolder extends ChildViewHolder {

    public TextView mTvName;
    public TextView mTvUUID;
    public TextView mTvPermission;
    public TextView mTvProperty;
    public TextView mTvWriteType;

    abstract void onClickChildView(int position);

    /**
     * Public constructor for the custom child ViewHolder
     *
     * @param itemView the child ViewHolder's view
     */
    public VerticalChildViewHolder(View itemView) {
        super(itemView);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //去 读写界面
                onClickChildView(getAdapterPosition());
            }
        });

        mTvName = (TextView) itemView.findViewById(R.id.tv_child_name);
        mTvUUID = (TextView) itemView.findViewById(R.id.tv_child_uuid);
        mTvPermission = (TextView) itemView.findViewById(R.id.tv_child_permission);
        mTvProperty = (TextView) itemView.findViewById(R.id.tv_child_property);
        mTvWriteType = (TextView) itemView.findViewById(R.id.tv_child_write_type);
    }

    public void bind(VerticalChildObject childObject) {
        mTvName.setText(childObject.mNameText);
        mTvUUID.setText(childObject.mUUIDText);
        mTvPermission.setText(childObject.mPermissionText);
        mTvProperty.setText(childObject.mPropertyText);
        mTvWriteType.setText(childObject.mWriteTypeText);
    }

}