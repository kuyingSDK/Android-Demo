package com.test.ad.demo;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.smartdigimkt.sdk.api.AdError;
import com.smartdigimkt.sdk.api.SDMAdConst;
import com.smartdigimkt.sdk.api.format.SDMBannerListener;
import com.smartdigimkt.sdk.api.format.SDMBannerView;
import com.test.ad.demo.base.BaseActivity;
import com.test.ad.demo.bean.CommonViewBean;
import com.test.ad.demo.util.SDKUtil;

import java.util.HashMap;
import java.util.Map;

public class BannerAdActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = BannerAdActivity.class.getSimpleName();

    private SDMBannerView mBannerView;
    private TextView tvLoadAdBtn;
    private ScrollView scrollView;
    private FrameLayout mBannerViewContainer;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_banner;
    }

    @Override
    protected int getAdType() {
        return SDMAdConst.ATMixedFormatAdType.BANNER;
    }

    @Override
    protected void onSelectPlacementId(String placementId) {
        mBannerView.setPlacementId(placementId);
    }

    @Override
    protected void initView() {
        super.initView();
        findViewById(R.id.rl_type).setSelected(true);
        tvLoadAdBtn = findViewById(R.id.banner_load_ad_btn);
        mBannerViewContainer = findViewById(R.id.adview_container);

        //Loading and displaying ads should keep the container and BannerView visible all the time
        mBannerViewContainer.setVisibility(View.VISIBLE);

        scrollView = findViewById(R.id.scroll_view);

        initBannerView();
//        addBannerViewToContainer();
    }

    @Override
    protected CommonViewBean getCommonViewBean() {
        CommonViewBean commonViewBean = new CommonViewBean();
        commonViewBean.setTitleBar(findViewById(R.id.title_bar));
        commonViewBean.setTvLogView(findViewById(R.id.tv_show_log));
        commonViewBean.setSpinnerSelectPlacement(findViewById(R.id.spinner_1));
        commonViewBean.setTitleResId(R.string.sdm_title_banner);
        return commonViewBean;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initListener() {
        super.initListener();
        if (mTVShowLog != null) {
            mTVShowLog.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN || motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                        view.getParent().requestDisallowInterceptTouchEvent(true);
                    }
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        view.getParent().requestDisallowInterceptTouchEvent(false);
                    }
                    return false;
                }
            });
        }
        tvLoadAdBtn.setOnClickListener(this);
    }

    private void initBannerView() {
        mBannerView = new SDMBannerView(this);
        //Loading and displaying ads should keep the container and BannerView visible all the time
        mBannerView.setVisibility(View.VISIBLE);
        mBannerView.setListener(new SDMBannerListener() {
            @Override
            public void onAdLoaded() {
                Log.i(TAG, "onBannerLoaded");
                printLogOnUI("onBannerLoaded");
                addBannerViewToContainer();
                if (scrollView != null) {
                    scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                }
            }

            @Override
            public void onAdLoadFail(AdError adError) {
                Log.i(TAG, "onBannerFailed: " + adError.getErrorInfo());
                printLogOnUI("onBannerFailed" + adError.getErrorInfo());
            }

            @Override
            public void onAdClick() {
                Log.i(TAG, "onBannerClicked:");
                printLogOnUI("onBannerClicked");
            }

            @Override
            public void onAdShow() {
                Log.i(TAG, "onBannerShow:");
                printLogOnUI("onBannerShow");
            }

            @Override
            public void onAdClose() {
                removeBannerViewToContainer();
                Log.i(TAG, "onBannerClose:");
                printLogOnUI("onBannerClose");
            }

            @Override
            public void onDeeplinkCallback(boolean b) {
                Log.i(TAG, "onDeeplinkCallback:" + "--status:" + b);
            }
        });
    }

    private void loadAd() {
        SDKUtil.start();
        printLogOnUI(getString(R.string.sdm_ad_status_loading));
        //Loading and displaying ads should keep the container and BannerView visible all the time
        mBannerView.setVisibility(View.VISIBLE);
        mBannerViewContainer.setVisibility(View.VISIBLE);

        mBannerView.load();
    }

    private void addBannerViewToContainer() {
        if (mBannerViewContainer != null && mBannerView != null) {
            mBannerViewContainer.removeAllViews();
            mBannerViewContainer.addView(mBannerView,
                    new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, mBannerViewContainer.getLayoutParams().height));
        }
    }

    private void removeBannerViewToContainer() {
        if (mBannerViewContainer != null && mBannerView != null) {
            mBannerViewContainer.removeAllViews();
        }
    }

    @Override
    protected void onDestroy() {
        if (mBannerViewContainer != null) {
            mBannerViewContainer.removeAllViews();
        }
        if (mBannerView != null) {
            mBannerView.destroy();
        }
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        if (v == null) return;

        if (v.getId() == R.id.banner_load_ad_btn) {
            loadAd();
        }
    }
}
