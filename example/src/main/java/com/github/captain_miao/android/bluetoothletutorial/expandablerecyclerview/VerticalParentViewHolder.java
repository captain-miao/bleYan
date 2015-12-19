package com.github.captain_miao.android.bluetoothletutorial.expandablerecyclerview;

import android.annotation.SuppressLint;
import android.os.Build;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;
import com.github.captain_miao.android.bluetoothletutorial.R;


/**
 *
 * @author YanLu
 * @version 1.0
 * @since 19/9/2015
 */
public class VerticalParentViewHolder extends ParentViewHolder {
    private static final float INITIAL_POSITION = 0.0f;
    private static final float ROTATED_POSITION = 180f;
    private static final float PIVOT_VALUE = 0.5f;
    private static final long DEFAULT_ROTATE_DURATION_MS = 200;
    private static final boolean HONEYCOMB_AND_ABOVE = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;


    public TextView mTvName;
    public TextView mTvUUID;
    public ImageView mArrowExpandImageView;

    /**
     * Public constructor for the CustomViewHolder.
     *
     * @param itemView the view of the parent item. Find/modify views using this.
     */
    public VerticalParentViewHolder(View itemView) {
        super(itemView);

        mTvName = (TextView) itemView.findViewById(R.id.tv_parent_name);
        mTvUUID = (TextView) itemView.findViewById(R.id.tv_parent_uuid);
        mArrowExpandImageView = (ImageView) itemView.findViewById(R.id.list_item_parent_horizontal_arrow_imageView);
    }

    public void bind(VerticalParentObject parentObject) {
        mTvName.setText(parentObject.mNameText);
        mTvUUID.setText(parentObject.mUUIDText);
    }


    @SuppressLint("NewApi")
    @Override
    public void setExpanded(boolean isExpanded) {
        super.setExpanded(isExpanded);
        if (!HONEYCOMB_AND_ABOVE) {
            return;
        }

        if (isExpanded) {
            mArrowExpandImageView.setRotation(ROTATED_POSITION);
        } else {
            mArrowExpandImageView.setRotation(INITIAL_POSITION);
        }
    }

    @Override
    public void onExpansionToggled(boolean isExpanded) {
        super.onExpansionToggled(isExpanded);
        if (!HONEYCOMB_AND_ABOVE) {
            return;
        }

        RotateAnimation rotateAnimation = new RotateAnimation(ROTATED_POSITION,
                INITIAL_POSITION,
                RotateAnimation.RELATIVE_TO_SELF, PIVOT_VALUE,
                RotateAnimation.RELATIVE_TO_SELF, PIVOT_VALUE);
        rotateAnimation.setDuration(DEFAULT_ROTATE_DURATION_MS);
        rotateAnimation.setFillAfter(true);
        mArrowExpandImageView.startAnimation(rotateAnimation);
    }
}
