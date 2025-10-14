package com.test.ad.demo.util;

import android.content.Context;
import android.util.Log;

import com.smartdigimkt.china.api.ChinaDeviceDataInfo;
import com.smartdigimkt.sdk.api.DeviceDataInfo;
import com.smartdigimkt.sdk.api.SDMInitConfig;
import com.smartdigimkt.sdk.api.SDMSDK;
import com.smartdigimkt.sdk.api.SDMUserDeviceInfo;
import com.test.ad.demo.BuildConfig;

public class SDKUtil {
    public static boolean isInitialized = false;
    public static boolean isStarted = false;

    public static void initSDK(Context context) {
        if (isInitialized) {
            return;
        }
        isInitialized = true;

        SDMSDK.getInstance().getSetting().deniedUploadDeviceInfo(
                DeviceDataInfo.DEVICE_MINOR_DATA,
                DeviceDataInfo.DEVICE_SCREEN_SIZE
        );
        SDMUserDeviceInfo sdmUserDeviceInfo = new SDMUserDeviceInfo();
        sdmUserDeviceInfo.setDevGaid("gaid");
        sdmUserDeviceInfo.setDevImei("ime");
        sdmUserDeviceInfo.setDevOaid("oaid");
        sdmUserDeviceInfo.setDevUID2Token("uid2token");
        SDMSDK.getInstance().getSetting().setUserDeviceInfo(sdmUserDeviceInfo);
        SDMSDK.getInstance().getSetting().setDebug(BuildConfig.DEBUG);
        SDMSDK.getInstance().init(context, new SDMInitConfig.Builder(PlacementIdUtil.getAppId(context), PlacementIdUtil.getAppKey(context)).build());
    }

    public static void start() {
        if (isStarted) {
            return;
        }
        isStarted = true;

        SDMSDK.getInstance().start(new SDMSDK.InitCallBack() {
            @Override
            public void success() {
                Log.e("SDKUtil", "SDMSDK init success");
            }

            @Override
            public void fail(String s) {
                Log.e("SDKUtil", "SDMSDK init fail" + s);
            }
        });
    }
}
