package com.test.ad.demo;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.smartdigimkt.sdk.api.AdError;
import com.smartdigimkt.sdk.api.SDMAdConst;
import com.smartdigimkt.sdk.api.format.SDMInterstitialAd;
import com.smartdigimkt.sdk.api.format.SDMInterstitialListener;
import com.test.ad.demo.base.BaseActivity;
import com.test.ad.demo.bean.CommonViewBean;
import com.test.ad.demo.util.SDKUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InterstitialAdActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = InterstitialAdActivity.class.getSimpleName();

    private final Map<String, Boolean> mAutoLoadPlacementIdMap = new HashMap<>();

    private SDMInterstitialAd mInterstitialAd;
    private boolean mIsAutoLoad;
    private TextView mTVLoadAdBtn;
    private TextView mTVIsAdReadyBtn;
    private TextView mTVShowAdBtn;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_interstitial;
    }

    @Override
    protected int getAdType() {
        return SDMAdConst.ATMixedFormatAdType.INTERSTITIAL;
    }

    @Override
    protected void onSelectPlacementId(String placementId) {
        initInterstitialAd(placementId);
    }

    @Override
    protected CommonViewBean getCommonViewBean() {
        final CommonViewBean commonViewBean = new CommonViewBean();
        commonViewBean.setTitleBar(findViewById(R.id.title_bar));
        commonViewBean.setSpinnerSelectPlacement(findViewById(R.id.spinner_1));
        commonViewBean.setTvLogView(findViewById(R.id.tv_show_log));
        commonViewBean.setTitleResId(R.string.sdm_title_interstitial);

        return commonViewBean;
    }

    @Override
    protected void initView() {
        super.initView();
        mTVLoadAdBtn = findViewById(R.id.load_ad_btn);
        mTVIsAdReadyBtn = findViewById(R.id.is_ad_ready_btn);
        mTVShowAdBtn = findViewById(R.id.show_ad_btn);
    }

    @Override
    protected void initListener() {
        super.initListener();
        mTVLoadAdBtn.setOnClickListener(this);
        mTVIsAdReadyBtn.setOnClickListener(this);
        mTVShowAdBtn.setOnClickListener(this);
    }

    private void initInterstitialAd(String placementId) {
        mInterstitialAd = new SDMInterstitialAd(this, placementId);
        mInterstitialAd.setListener(new SDMInterstitialListener() {
            @Override
            public void onAdLoaded() {
                Log.i(TAG, "onInterstitialAdLoaded");
                printLogOnUI("onInterstitialAdLoaded");
            }

            @Override
            public void onAdLoadFail(AdError adError) {
                Log.i(TAG, "onInterstitialAdLoadFail:\n" + adError.getErrorInfo());
                printLogOnUI("onInterstitialAdLoadFail:" + adError.getErrorInfo());
            }

            @Override
            public void onAdClick() {
                Log.i(TAG, "onInterstitialAdClicked:\n");
                printLogOnUI("onInterstitialAdClicked");
            }

            @Override
            public void onAdShow() {
                Log.i(TAG, "onInterstitialAdShow:\n");
                printLogOnUI("onInterstitialAdShow");
            }

            @Override
            public void onAdClose() {
                Log.i(TAG, "onInterstitialAdClose:\n");
                printLogOnUI("onInterstitialAdClose");
            }

            @Override
            public void onAdVideoStart() {
                Log.i(TAG, "onInterstitialAdVideoStart:\n");
                printLogOnUI("onInterstitialAdVideoStart");
            }

            @Override
            public void onAdVideoEnd() {
                Log.i(TAG, "onInterstitialAdVideoEnd:\n");
                printLogOnUI("onInterstitialAdVideoEnd");
            }

            @Override
            public void onAdShowFail(AdError adError) {
                Log.i(TAG, "onInterstitialAdVideoError:\n");
                printLogOnUI("onInterstitialAdVideoError");
            }

            @Override
            public void onDeeplinkCallback(boolean b) {
                Log.i(TAG, "onDeeplinkCallback:--status:" + b);
                printLogOnUI("onDeeplinkCallback");
            }
        });

    }

    private void loadAd() {
        SDKUtil.start();
        if (mInterstitialAd == null) {
            printLogOnUI("SDMInterstitialAd is not init.");
            return;
        }
        printLogOnUI(getString(R.string.sdm_ad_status_loading));
        mInterstitialAd.load();
    }

    private void isAdReady() {
        printLogOnUI("interstitial ad ready status:" + mInterstitialAd.isAdReady());
    }

    private void showAd() {
        mInterstitialAd.show(InterstitialAdActivity.this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mInterstitialAd != null) {
            mInterstitialAd.destroy();
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        if (v == null) return;
        switch (v.getId()) {
            case R.id.load_ad_btn:
                loadAd();
                break;
            case R.id.is_ad_ready_btn:
                isAdReady();
                break;
            case R.id.show_ad_btn:
                if (mInterstitialAd.isAdReady()) {
                    showAd();
                }
                break;
        }
    }
}

