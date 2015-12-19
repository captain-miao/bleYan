package com.github.captain_miao.android.bluetoothletutorial.expandablerecyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.github.captain_miao.android.bluetoothletutorial.R;
import com.github.captain_miao.android.bluetoothletutorial.app.AppLog;

import java.util.List;

/**
 * An example custom implementation of the ExpandableRecyclerAdapter.
 *
 * @author YanLu
 * @version 1.0
 * @since 19/9/2015
 */
public abstract class VerticalExpandableAdapter extends ExpandableRecyclerAdapter2<VerticalParentViewHolder, VerticalChildViewHolder> {
    private static final String TAG = VerticalExpandableAdapter.class.getSimpleName();
    private LayoutInflater mInflater;

    protected abstract void onClickCharacteristic(String serviceUUID, String characteristicUUID);
    /**
     * Public primary constructor.
     *
     * @param context for inflating views
     * @param parentItemList the list of parent items to be displayed in the RecyclerView
     */
    public VerticalExpandableAdapter(Context context, List<ParentListItem> parentItemList) {
        super(context, parentItemList);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public void onListItemExpanded(int position) {

    }

    @Override
    public void onListItemCollapsed(int position) {

    }

    /**
     * OnCreateViewHolder implementation for parent items. The desired ParentViewHolder should
     * be inflated here
     *
     * @param parent for inflating the View
     * @return the user's custom parent ViewHolder that must extend ParentViewHolder
     */
    @Override
    public VerticalParentViewHolder onCreateParentViewHolder(ViewGroup parent) {
        View view = mInflater.inflate(R.layout.list_item_parent_vertical, parent, false);
        return new VerticalParentViewHolder(view);
    }

    /**
     * OnCreateViewHolder implementation for child items. The desired ChildViewHolder should
     * be inflated here
     *
     * @param parent for inflating the View
     * @return the user's custom parent ViewHolder that must extend ParentViewHolder
     */
    @Override
    public VerticalChildViewHolder onCreateChildViewHolder(ViewGroup parent) {
        View view = mInflater.inflate(R.layout.list_item_child_vertical, parent, false);
        return new VerticalChildViewHolder(view) {
            @Override
            void onClickChildView(int position) {
                int parentPositon = position - getExpandedItemCount2(position);
                //int childPositon = getExpandedItemCount(parentPositon);

                Object parentItem = getParentItem(parentPositon);
                Object helperItem = getHelperItem(position);
                if(helperItem instanceof VerticalChildObject && parentItem instanceof VerticalParentObject){
                    AppLog.i(TAG, helperItem.toString());
                    onClickCharacteristic(((VerticalParentObject) parentItem).mUUIDText, ((VerticalChildObject) helperItem).mUUIDText);
                    //mContext.startActivity(new Intent(mContext, BleCharacteristicActivity.class)
                    //        .putExtra(AppConstants.KEY_BLE_SERVICE, ((VerticalParentObject) parentItem).mUUIDText)
                    //        .putExtra(AppConstants.KEY_BLE_CHARACTERISTIC, ((VerticalChildObject) helperItem).mUUIDText));
                }
            }
        };
    }

    /**
     * OnBindViewHolder implementation for parent items. Any data or view modifications of the
     * parent view should be performed here.
     *
     * @param parentViewHolder the ViewHolder of the parent item created in OnCreateParentViewHolder
     * @param position the position in the RecyclerView of the item
     */
    @Override
    public void onBindParentViewHolder(VerticalParentViewHolder parentViewHolder, int position, Object parentObject) {
        VerticalParentObject verticalParentObject = (VerticalParentObject) parentObject;
        parentViewHolder.bind(verticalParentObject);
    }

    /**
     * OnBindViewHolder implementation for child items. Any data or view modifications of the
     * child view should be performed here.
     *
     * @param childViewHolder the ViewHolder of the child item created in OnCreateChildViewHolder
     * @param position the position in the RecyclerView of the item
     */
    @Override
    public void onBindChildViewHolder(VerticalChildViewHolder childViewHolder, int position, Object childObject) {
        VerticalChildObject verticalChildObject = (VerticalChildObject) childObject;
        childViewHolder.bind(verticalChildObject);
    }
}