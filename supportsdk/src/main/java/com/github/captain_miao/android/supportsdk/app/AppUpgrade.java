package com.github.captain_miao.android.supportsdk.app;

import android.content.Context;

import com.umeng.update.UmengUpdateAgent;

/**
 * @author YanLu
 * @since 15/9/17
 */
public class AppUpgrade {

    public static void update(Context context) {
        UmengUpdateAgent.setUpdateOnlyWifi(true);
        UmengUpdateAgent.update(context);
    }
}
