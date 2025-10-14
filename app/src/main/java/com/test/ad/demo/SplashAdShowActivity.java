package com.test.ad.demo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.smartdigimkt.sdk.api.AdError;
import com.smartdigimkt.sdk.api.format.SDMSplashAdExtraInfo;
import com.smartdigimkt.sdk.api.format.SDMSplashListener;
import com.test.ad.demo.base.BaseActivity;
import com.test.ad.demo.util.SDKUtil;
import com.test.ad.demo.util.SplashAdManager;
import com.test.ad.demo.zoomout.SplashZoomOutManager;

public class SplashAdShowActivity extends Activity {

    private static final String TAG = SplashAdShowActivity.class.getSimpleName();
    SDMSplashListener listener = new SDMSplashListener() {
        @Override
        public void onAdLoaded(boolean isTimeout) {

        }

        @Override
        public void onAdLoadTimeout() {

        }

        @Override
        public void onAdLoadFail(AdError adError) {

        }

        @Override
        public void onAdShow() {

        }

        @Override
        public void onAdClick() {

        }

        @Override
        public void onAdClose(SDMSplashAdExtraInfo sdmSplashAdExtraInfo) {
            finish();
        }

        @Override
        public void onAdShowFail(AdError adError) {
            Toast.makeText(SplashAdShowActivity.this, adError.getErrorInfo(), Toast.LENGTH_SHORT).show();
            finish();
        }

        @Override
        public void onDeeplinkCallback(boolean isSuccess) {

        }
    };
    FrameLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_ad_show);
        container = findViewById(R.id.splash_ad_container);

        showAd();
    }

    private void showAd() {
        SplashAdManager.getInstance().addListener(listener);
        SplashAdManager.getInstance().showAd(container);
    }

    @Override
    protected void onDestroy() {
        SplashAdManager.getInstance().destroy();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
