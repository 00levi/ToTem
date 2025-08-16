package com.axiel7.tioanime.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

public class DeviceUtils {
    public static boolean isTV(Context context) {
        PackageManager pm = context.getPackageManager();
        return pm.hasSystemFeature(PackageManager.FEATURE_LEANBACK) ||
                pm.hasSystemFeature(PackageManager.FEATURE_TELEVISION) ||
                (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2 &&
                        pm.hasSystemFeature("android.software.leanback_only"));
    }
}
