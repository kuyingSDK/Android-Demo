package com.test.ad.demo;

import static com.test.ad.demo.AdConst.innerGetProcessName;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.smartdigimkt.sdk.api.AdError;
import com.smartdigimkt.sdk.api.SDMAdConst;
import com.smartdigimkt.sdk.api.format.SDMRewardVideoListener;
import com.smartdigimkt.sdk.api.format.SDMRewardedVideoAd;
import com.test.ad.demo.base.BaseActivity;
import com.test.ad.demo.bean.CommonViewBean;
import com.test.ad.demo.util.SDKUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RewardVideoAdActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = RewardVideoAdActivity.class.getSimpleName();


    private final Map<String, Boolean> mAutoLoadPlacementIdMap = new HashMap<>();
    private SDMRewardedVideoAd mRewardVideoAd;
    private boolean mIsAutoLoad;
    private TextView mTvLoadAdBtn;
    private TextView mTvIsAdReadyBtn;
    private TextView mTvShowAdBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("Vicent", getClass().getSimpleName() + " onCreate progressName: " + innerGetProcessName(this));
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_video;
    }

    @Override
    protected int getAdType() {
        return SDMAdConst.ATMixedFormatAdType.REWARDED_VIDEO;
    }

    @Override
    protected void onSelectPlacementId(String placementId) {
        initRewardVideoAd(placementId);
    }

    @Override
    protected CommonViewBean getCommonViewBean() {
        final CommonViewBean commonViewBean = new CommonViewBean();
        commonViewBean.setTitleBar(findViewById(R.id.title_bar));
        commonViewBean.setTvLogView(findViewById(R.id.tv_show_log));
        commonViewBean.setSpinnerSelectPlacement(findViewById(R.id.spinner_1));
        commonViewBean.setTitleResId(R.string.sdm_title_rewarded_video);
        return commonViewBean;
    }

    @Override
    protected void initView() {
        super.initView();
        mTvLoadAdBtn = findViewById(R.id.load_ad_btn);
        mTvIsAdReadyBtn = findViewById(R.id.is_ad_ready_btn);
        mTvShowAdBtn = findViewById(R.id.show_ad_btn);
    }

    @Override
    protected void initListener() {
        super.initListener();
        mTvLoadAdBtn.setOnClickListener(this);
        mTvIsAdReadyBtn.setOnClickListener(this);
        mTvShowAdBtn.setOnClickListener(this);
    }

    private void initRewardVideoAd(String placementId) {
        mRewardVideoAd = new SDMRewardedVideoAd(this, placementId);
        mRewardVideoAd.setListener(new SDMRewardVideoListener() {
            @Override
            public void onAdLoaded() {
                Log.i(TAG, "onRewardedVideoAdLoaded");
                printLogOnUI("onRewardedVideoAdLoaded");
                mRewardVideoAd.getSDMAd().getEcpmInfo().getEcpm(SDMAdConst.CURRENCY.RMB);
                mRewardVideoAd.getSDMAd().getPlaceInfo().getFormat();
            }

            @Override
            public void onAdLoadFail(AdError adError) {
                Log.i(TAG, "onRewardedVideoAdFailed error:" + adError.getErrorInfo());
                printLogOnUI("onRewardedVideoAdFailed:" + adError.getErrorInfo());
            }

            @Override
            public void onAdReward() {
                Log.e(TAG, "onReward:\n");
                printLogOnUI("onReward");
            }

            @Override
            public void onAdPlayStart() {
                Log.i(TAG, "onRewardedVideoAdPlayStart:\n");
                printLogOnUI("onRewardedVideoAdPlayStart");
            }

            @Override
            public void onAdPlayEnd() {
                Log.i(TAG, "onRewardedVideoAdPlayEnd:\n");
                printLogOnUI("onRewardedVideoAdPlayEnd");
            }

            @Override
            public void onAdPlayFailed(AdError adError) {
                Log.i(TAG, "onRewardedVideoAdFailed error:" + adError.getErrorInfo());
                printLogOnUI("onRewardedVideoAdFailed:" + adError.getErrorInfo());
            }

            @Override
            public void onAdClick() {
                Log.i(TAG, "onRewardedVideoAdPlayClicked:\n");
                printLogOnUI("onRewardedVideoAdPlayClicked");
            }

            @Override
            public void onDeeplinkCallback(boolean b) {
                Log.i(TAG, "onDeeplinkCallback:" + b + "\n");
                printLogOnUI("onDeeplinkCallback");
            }

            @Override
            public void onAdClose() {
                Log.i(TAG, "onRewardedVideoAdClosed:\n");
                printLogOnUI("onRewardedVideoAdClosed");
            }
        });

    }


    private void loadAd() {
        SDKUtil.start();
        if (mRewardVideoAd == null) {
            printLogOnUI("ATRewardVideoAd is not init.");
            return;
        }
        printLogOnUI(getString(R.string.sdm_ad_status_loading));
        mRewardVideoAd.load();
    }

    private void isAdReady() {
        if (mRewardVideoAd == null) {
            return;
        }
        boolean isReady = mRewardVideoAd.isAdReady();
        printLogOnUI("video ad ready status:" + isReady);
    }


    private void showAd() {
        mRewardVideoAd.show(RewardVideoAdActivity.this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mRewardVideoAd != null) {
            mRewardVideoAd.destroy();
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.load_ad_btn:
                loadAd();
                break;
            case R.id.is_ad_ready_btn:
                isAdReady();
                break;
            case R.id.show_ad_btn:
                if (mRewardVideoAd != null && mRewardVideoAd.isAdReady()) {
                    showAd();
                } else {
                    printLogOnUI(getString(R.string.sdm_ad_status_not_load));
                }
                break;
        }
    }
}

