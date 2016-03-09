package com.github.captain_miao.android.bluetoothletutorial;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.github.captain_miao.android.bluetoothletutorial.fragment.AboutFragment;
import com.github.captain_miao.android.bluetoothletutorial.fragment.BleDevicesFragment;
import com.github.captain_miao.android.bluetoothletutorial.fragment.ConfigFragment;
import com.github.captain_miao.android.supportsdk.app.AppUpgrade;
import com.mikepenz.aboutlibraries.LibsBuilder;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.umeng.analytics.MobclickAgent;

import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;
import it.neokree.materialnavigationdrawer.elements.MaterialAccount;
import it.neokree.materialnavigationdrawer.elements.MaterialSection;
import it.neokree.materialnavigationdrawer.elements.listeners.MaterialAccountListener;

public class HomeActivity extends MaterialNavigationDrawer implements MaterialAccountListener {
    public static final String TAG = "HomeActivity";
    MaterialAccount account;

    @Override
    public void init(Bundle bundle) {
        //KitKat translucent modes
        setTranslucentStatus(this, true);


        //账号处理
        account = new MaterialAccount(this.getResources(),
                getString(R.string.about_me_name), getString(R.string.about_me_email),
                R.drawable.profile, R.drawable.header);
        this.addAccount(account);

        // set listener
        this.setAccountListener(this);

        MaterialSection homeSection =
                newSection(getString(R.string.app_home),
                new IconicsDrawable(this)
                    .icon(FontAwesome.Icon.faw_home)
                    .color(Color.WHITE)
                    .sizeDp(24),
                BleDevicesFragment.newInstance(getString(R.string.app_home)));

        MaterialSection configSection =
                newSection(getString(R.string.app_user_config),
                new IconicsDrawable(this)
                    .icon(FontAwesome.Icon.faw_gg_circle)
                    .color(Color.WHITE)
                    .sizeDp(24),
                ConfigFragment.newInstance(getString(R.string.app_user_config)));

        MaterialSection openSourceSection =
                newSection(getString(R.string.app_open_source),
                new IconicsDrawable(this)
                    .icon(FontAwesome.Icon.faw_github)
                    .color(Color.WHITE)
                    .sizeDp(24),
                new LibsBuilder().fragment());
        MaterialSection aboutSection =
                newSection(getString(R.string.app_about),
                new IconicsDrawable(this)
                    .icon(GoogleMaterial.Icon.gmd_email)
                    .color(Color.WHITE)
                    .sizeDp(24),
                AboutFragment.newInstance(getString(R.string.app_about)));


        addSection(homeSection);
        addSection(configSection);
        addSection(openSourceSection);
        addSection(aboutSection);

        disableLearningPattern();
        // add pattern
        this.setBackPattern(MaterialNavigationDrawer.BACKPATTERN_BACK_TO_FIRST);
        //allowArrowAnimation();
        enableToolbarElevation();

        AppUpgrade.update(this);//版本更新
    }


    @TargetApi(19)
    public void setTranslucentStatus(Activity activity, boolean on) {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            Window win = activity.getWindow();
            WindowManager.LayoutParams winParams = win.getAttributes();
            final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            if (on) {
                winParams.flags |= bits;
            } else {
                winParams.flags &= ~bits;
            }
            win.setAttributes(winParams);

            SystemBarTintManager tintManager = new SystemBarTintManager(activity);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setNavigationBarTintEnabled(false);
            tintManager.setStatusBarTintColor(activity.getResources().getColor(R.color.colorPrimary));
            //tintManager.setNavigationBarTintColor(activity.getResources().getColor(R.color.colorPrimary));
            //			tintManager.setStatusBarTintResource(R.color.colorPrimary);
        }
    }

    @Override
    public void onAccountOpening(MaterialAccount materialAccount) {

    }

    @Override
    public void onChangeAccount(MaterialAccount materialAccount) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
