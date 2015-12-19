package com.github.captain_miao.android.bluetoothletutorial.expandablerecyclerview;

import java.io.Serializable;

/**
 *
 * @author YanLu
 * @version 1.0
 * @since 19/9/2015
 */
public class VerticalChildObject implements Serializable{

    public String mNameText;
    public String mUUIDText;
    public String mPermissionText;
    public String mPropertyText;
    public String mWriteTypeText;

    @Override
    public String toString() {
        return "VerticalChildObject{" +
                "mNameText='" + mNameText + '\'' +
                ", mUUIDText='" + mUUIDText + '\'' +
                ", mPermissionText='" + mPermissionText + '\'' +
                ", mPropertyText='" + mPropertyText + '\'' +
                ", mWriteTypeText='" + mWriteTypeText + '\'' +
                '}';
    }
}
