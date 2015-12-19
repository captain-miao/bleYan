package com.github.captain_miao.android.bluetoothletutorial.expandablerecyclerview;


import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;

import java.util.List;

/**
 * @author YanLu
 * @version 1.0
 * @since 19/9/2015
 */
public class VerticalParentObject implements ParentListItem {

    // A List<Object> or subclass of List must be added for the object to work correctly
    private List<Object> mChildObjectList;
    public String mNameText;
    public String mUUIDText;
    private int mParentNumber;
    private boolean mInitiallyExpanded;



    public int getParentNumber() {
        return mParentNumber;
    }

    public void setParentNumber(int parentNumber) {
        mParentNumber = parentNumber;
    }

    /**
     * Getter method for the list of children associated with this parent object
     *
     * @return list of all children associated with this specific parent object
     */
    @Override
    public List<Object> getChildItemList() {
        return mChildObjectList;
    }

    /**
     * Setter method for the list of children associated with this parent object
     *
     * @param childObjectList the list of all children associated with this parent object
     */
    public void setChildObjectList(List<Object> childObjectList) {
        mChildObjectList = childObjectList;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return mInitiallyExpanded;
    }

    public void setInitiallyExpanded(boolean initiallyExpanded) {
        mInitiallyExpanded = initiallyExpanded;
    }
}
