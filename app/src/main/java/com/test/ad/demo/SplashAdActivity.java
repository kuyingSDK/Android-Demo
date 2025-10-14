package com.test.ad.demo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import com.smartdigimkt.sdk.api.AdError;
import com.smartdigimkt.sdk.api.SDMAdConst;
import com.smartdigimkt.sdk.api.SDMAdRequest;
import com.smartdigimkt.sdk.api.format.SDMLossInfo;
import com.smartdigimkt.sdk.api.format.SDMSplashAdExtraInfo;
import com.smartdigimkt.sdk.api.format.SDMSplashListener;
import com.test.ad.demo.base.BaseActivity;
import com.test.ad.demo.bean.CommonViewBean;
import com.test.ad.demo.util.SDKUtil;
import com.test.ad.demo.util.SplashAdManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SplashAdActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "SplashAdActivity";

//    private Spinner mSpinnerPlacementId;

    SDMSplashListener listener = new SDMSplashListener() {
        @Override
        public void onAdLoaded(boolean isTimeout) {
            Log.i(TAG, "onAdLoaded---------isTimeout:" + isTimeout);
            printLogOnUI("onAdLoaded---------isTimeout:" + isTimeout);
        }

        @Override
        public void onAdLoadTimeout() {
            Log.i(TAG, "onAdLoadTimeout---------");
            printLogOnUI("onAdLoadTimeout---------");
        }

        @Override
        public void onAdLoadFail(AdError adError) {
            Log.i(TAG, "onNoAdError---------:" + adError.getErrorInfo());
            printLogOnUI("onNoAdError---------:" + adError.getErrorInfo());
        }

        @Override
        public void onAdShow() {
            Log.i(TAG, "onAdShow---------:");
            printLogOnUI("onAdShow---------");
        }

        @Override
        public void onAdClick() {
            Log.i(TAG, "onAdClick---------:");
            printLogOnUI("onAdClick---------");
        }

        @Override
        public void onAdClose(SDMSplashAdExtraInfo sdmSplashAdExtraInfo) {
            int dismissType = sdmSplashAdExtraInfo.getDismissType();
            String dismissTypeStr = "";
            switch (dismissType) {
                case SDMSplashAdExtraInfo.SPLASH_DISMISS_TYPE.NORMAL:
                    dismissTypeStr = "Normal";
                    break;
                case SDMSplashAdExtraInfo.SPLASH_DISMISS_TYPE.SKIP:
                    dismissTypeStr = "Skip";
                    break;
                case SDMSplashAdExtraInfo.SPLASH_DISMISS_TYPE.TIMEOVER:
                    dismissTypeStr = "TimeOver";
                    break;
                case SDMSplashAdExtraInfo.SPLASH_DISMISS_TYPE.CLICKAD:
                    dismissTypeStr = "ClickAd";
                    break;
                case SDMSplashAdExtraInfo.SPLASH_DISMISS_TYPE.SHOWFAILED:
                    dismissTypeStr = "ShowFailed";
                    break;
                default:
                    dismissTypeStr = "unknown";
                    break;
            }
            Log.i(TAG, "onAdDismiss---------:" + dismissTypeStr);
            printLogOnUI("onAdDismiss---------" + dismissTypeStr);
        }

        @Override
        public void onAdShowFail(AdError adError) {
            Log.i(TAG, "onAdShowFail---------:" + adError.getErrorInfo());
            printLogOnUI("onAdShowFail---------:" + adError.getErrorInfo());
        }

        @Override
        public void onDeeplinkCallback(boolean isSuccess) {
        }
    };

    @Override
    public int getContentViewId() {
        return R.layout.activity_splash;
    }

    @Override
    protected int getAdType() {
        return SDMAdConst.ATMixedFormatAdType.SPLASH;
    }

    @Override
    public void initView() {
        super.initView();
        findViewById(R.id.rl_type).setSelected(true);
    }

    @Override
    protected CommonViewBean getCommonViewBean() {
        CommonViewBean commonViewBean = new CommonViewBean();
        commonViewBean.setTitleBar(findViewById(R.id.title_bar));
        commonViewBean.setTvLogView(findViewById(R.id.tv_show_log));
        commonViewBean.setSpinnerSelectPlacement(findViewById(R.id.spinner_1));
        commonViewBean.setTitleResId(R.string.sdm_title_splash);
        return commonViewBean;
    }

    @Override
    protected void initListener() {
        findViewById(R.id.is_ad_ready_btn).setOnClickListener(this);
        findViewById(R.id.load_ad_btn).setOnClickListener(this);
        findViewById(R.id.show_ad_btn).setOnClickListener(this);
    }

    @Override
    protected void onSelectPlacementId(String placementId) {
    }

    private void loadAd() {
        SDKUtil.start();
        Log.i(TAG, "loadAd---------:");
        printLogOnUI("loadAd---------:");
        SDMAdRequest adRequest = new SDMAdRequest.Builder().build();

        SplashAdManager splashAdManager = SplashAdManager.getInstance();
        splashAdManager.addListener(listener);
        splashAdManager.loadAd(this, mCurrentPlacementId, adRequest);
    }

    private void showAd() {
        Log.i(TAG, "showAd");
        printLogOnUI("showAd");
        if (SplashAdManager.getInstance().isAdReady()) {
            Intent intent = new Intent(this, SplashAdShowActivity.class);
            startActivity(intent);
        } else {
            printLogOnUI("showAd without ad ready");
        }
    }

    private void isAdReady() {
        boolean isReady = SplashAdManager.getInstance().isAdReady();
        if (isReady) {
            Log.i(TAG, "SplashAd is ready to show.");
            printLogOnUI("SplashAd is ready to show.");
        } else {
            Log.i(TAG, "SplashAd isn't ready to show.");
            printLogOnUI("SplashAd isn't ready to show.");
        }
    }

    @Override
    protected void onDestroy() {
        SplashAdManager.getInstance().destroy();
        super.onDestroy();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        if (v == null) return;
        switch (v.getId()) {
            case R.id.is_ad_ready_btn:
                isAdReady();
                break;
            case R.id.load_ad_btn:
                loadAd();
                break;
            case R.id.show_ad_btn:
                showAd();
                break;
        }
    }
}
