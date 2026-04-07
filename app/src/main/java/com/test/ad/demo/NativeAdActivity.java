package com.test.ad.demo;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.smartdigimkt.sdk.api.AdError;
import com.smartdigimkt.sdk.api.SDMAdConst;
import com.smartdigimkt.sdk.api.SDMAdRequest;
import com.smartdigimkt.sdk.api.format.SDMNative;
import com.smartdigimkt.sdk.api.format.SDMNativeAd;
import com.smartdigimkt.sdk.api.format.SDMNativeAdListener;
import com.smartdigimkt.sdk.api.format.SDMNativeAdMediaViewListener;
import com.smartdigimkt.sdk.api.format.SDMNativeLoadListener;
import com.test.ad.demo.base.BaseActivity;
import com.test.ad.demo.bean.CommonViewBean;
import com.test.ad.demo.util.SDKUtil;
import com.test.ad.demo.util.SelfRenderViewUtil;

import java.util.ArrayList;
import java.util.List;

public class NativeAdActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = NativeAdActivity.class.getSimpleName();
    public static final String NATIVE_SELF_RENDER_TYPE = "1";
    public static final String NATIVE_EXPRESS_TYPE = "2";

    private final List<String> mData = new ArrayList<>();

    private FrameLayout mNativeAdView;
    private SDMNative mSDMNative;
    private SDMNativeAd mNativeAd;
    private View mSelfRenderView;
    private TextView mTVLoadAdBtn;
    private TextView mTVIsAdReadyBtn;
    private TextView mTVShowAdBtn;
    private View mPanel;

    /** Last size passed to load(); used to give template branch a non-zero height when WRAP_CONTENT measures 0. */
    private int mLastRequestedAdWidth;
    private int mLastRequestedAdHeight;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_native;
    }

    @Override
    protected int getAdType() {
        return SDMAdConst.ATMixedFormatAdType.NATIVE;
    }

    @Override
    protected void onSelectPlacementId(String placementId) {
        initATNativeAd(placementId);
    }

    @Override
    protected CommonViewBean getCommonViewBean() {
        final CommonViewBean commonViewBean = new CommonViewBean();
        commonViewBean.setTitleBar(findViewById(R.id.title_bar));
        commonViewBean.setTvLogView(findViewById(R.id.tv_show_log));
        commonViewBean.setSpinnerSelectPlacement(findViewById(R.id.spinner_1));

        String nativeType = getNativeAdTypeFromIntent();
        if (NATIVE_SELF_RENDER_TYPE.equals(nativeType)) {
            commonViewBean.setTitleResId(R.string.sdm_native_self);
        } else {
            commonViewBean.setTitleResId(R.string.sdm_native_express);
        }
        return commonViewBean;
    }

    @Override
    protected String getNativeAdType() {
        return getNativeAdTypeFromIntent();
    }

    private String getNativeAdTypeFromIntent() {
        String type = getIntent().getStringExtra("native_type");
        return TextUtils.isEmpty(type) ? NATIVE_SELF_RENDER_TYPE : type;
    }

    @Override
    protected void initView() {
        super.initView();
        mTVLoadAdBtn = findViewById(R.id.load_ad_btn);
        mTVIsAdReadyBtn = findViewById(R.id.is_ad_ready_btn);
        mTVShowAdBtn = findViewById(R.id.show_ad_btn);
        mNativeAdView = findViewById(R.id.native_ad_view);
        initPanel();
    }

    @Override
    protected void initListener() {
        super.initListener();
        mTVLoadAdBtn.setOnClickListener(this);
        mTVIsAdReadyBtn.setOnClickListener(this);
        mTVShowAdBtn.setOnClickListener(this);
    }

    private void initPanel() {
        mPanel = findViewById(R.id.rl_panel);
        mSelfRenderView = findViewById(R.id.native_selfrender_view);
        if (NATIVE_EXPRESS_TYPE.equals(getNativeAdTypeFromIntent())) {
            mSelfRenderView.setVisibility(View.GONE);
        }
        View closeView = mSelfRenderView.findViewById(R.id.native_ad_close);
        if (closeView != null) {
            closeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i(TAG, "native ad onAdCloseButtonClick");
                    printLogOnUI("native ad onAdCloseButtonClick");

                    exitNativePanel();
                }
            });
        }
    }

    private void initATNativeAd(String placementId) {
        mSDMNative = new SDMNative(NativeAdActivity.this, placementId);
        mSDMNative.setListener(new SDMNativeLoadListener() {
            @Override
            public void onAdLoaded(SDMNativeAd sdmNativeAd) {
                Log.i(TAG, "onNativeAdLoaded");
                printLogOnUI("load success...");
                if (mNativeAd != null) {
                    mNativeAd.destroy();
                }
                mNativeAd = sdmNativeAd;
            }

            @Override
            public void onAdLoadFail(AdError adError) {
                Log.i(TAG, "onNativeAdLoadFail, " + adError.getErrorInfo());
                printLogOnUI("load fail...：" + adError.getErrorInfo());
            }
        });
    }

    private static SDMNativeAdMediaViewListener nativeMediaListener() {
        return new SDMNativeAdMediaViewListener() {
            @Override
            public void onVideoAdStartPlay(long l) {
            }

            @Override
            public void onVideoAdComplete() {
            }

            @Override
            public void onVideoError(String s, String s1) {
            }

            @Override
            public void onProgressUpdate(long l, long l1) {
            }


            public void onClickCloseView() {
            }
        };
    }

    private void loadAd(int adViewWidth, int adViewHeight) {
        mLastRequestedAdWidth = adViewWidth;
        mLastRequestedAdHeight = adViewHeight;
        SDKUtil.start();
        printLogOnUI(getString(R.string.sdm_ad_status_loading));
        SDMAdRequest sdmAdRequest = new SDMAdRequest.Builder()
                .setAdWidth(adViewWidth)
                .setAdHeight(adViewHeight)
                .build();
        mSDMNative.load(sdmAdRequest);
    }

    private int computeNativeSlotWidthPx() {
        int w = mNativeAdView != null && mNativeAdView.getWidth() > 0
                ? mNativeAdView.getWidth()
                : getResources().getDisplayMetrics().widthPixels;
        if (mLastRequestedAdWidth > 0) {
            w = mLastRequestedAdWidth;
        }
        return w;
    }

    private int computeNativeSlotHeightPx() {
        if (mLastRequestedAdHeight > 0) {
            return mLastRequestedAdHeight;
        }
        if (mNativeAd != null) {
            int ih = mNativeAd.getAdImageHeight();
            int iw = mNativeAd.getAdImageWidth();
            if (ih > 0 && iw > 0) {
                int w = computeNativeSlotWidthPx();
                return Math.max(1, ih * w / iw);
            }
        }
        int w = computeNativeSlotWidthPx();
        return w * 3 / 4;
    }

    private boolean isAdReady() {
        boolean isReady = (mNativeAd != null && mNativeAd.isAdReady());
        Log.i(TAG, "isAdReady: " + isReady);
        printLogOnUI("isAdReady：" + isReady);

        return isReady;
    }

    private void showAd() {
        if (mNativeAd != null) {

            mNativeAd.setListener(new SDMNativeAdListener() {
                @Override
                public void onAdShow() {
                    Log.i(TAG, "native ad onAdImpressed:\n");
                    printLogOnUI("onAdImpressed");
                }

                @Override
                public void onAdClosed() {
                    Log.i(TAG, "native ad onAdCloseButtonClick");
                    printLogOnUI("native ad onAdCloseButtonClick");

                    exitNativePanel();
                }

                @Override
                public void onAdClick() {
                    Log.i(TAG, "native ad onAdClicked:\n");
                    printLogOnUI("onAdClicked");
                }

                @Override
                public void onDeeplinkCallback(boolean b) {

                }

                @Override
                public void onShowFailed(AdError adError) {
                    Log.w(TAG, "onShowFailed: " + (adError != null ? adError.getErrorInfo() : "null"));
                    printLogOnUI("onShowFailed: " + (adError != null ? adError.getErrorInfo() : ""));
                }
            });

            mNativeAdView.removeAllViews();

            List<View> clickViews = null;
            final boolean[] templateShown = {false};
            try {
                if (mNativeAd.isTemplateAd()) {
                    View mediaView = mNativeAd.getMediaView(this, nativeMediaListener());
                    if (mediaView != null) {

                        if (mediaView.getParent() instanceof ViewGroup) {
                            ((ViewGroup) mediaView.getParent()).removeView(mediaView);
                        }
                        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                                FrameLayout.LayoutParams.MATCH_PARENT,
                                FrameLayout.LayoutParams.WRAP_CONTENT);
                        lp.gravity = Gravity.CENTER;
                        mNativeAdView.addView(mediaView, lp);

                        mNativeAd.onResume();
                        mNativeAd.registerAdView(mNativeAdView, null, null, null, null);
                        Log.i(TAG, "template/express: attached getMediaView(), childCount=" + mNativeAdView.getChildCount());
                    } else {

                        printLogOnUI("template/express: getMediaView() null");
                        Log.w(TAG, "getCustomAdContainer() and getMediaView() both null");

                    }
                } else {
                    ViewGroup nativeAdContainer = mNativeAd.getCustomAdContainer();
                    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                            FrameLayout.LayoutParams.MATCH_PARENT,
                            FrameLayout.LayoutParams.WRAP_CONTENT);
                    mNativeAdView.addView(nativeAdContainer, params);
                    if (mSelfRenderView.getParent() != null) {
                        ((ViewGroup) mSelfRenderView.getParent()).removeView(mSelfRenderView);
                    }
                    nativeAdContainer.addView(mSelfRenderView);
                    clickViews = SelfRenderViewUtil.bindSelfRenderView(this, mNativeAd, mSelfRenderView);
                }
            } catch (Exception e) {
                e.printStackTrace();
                printLogOnUI("showAd error: " + e.getMessage());
            }
            mNativeAdView.setVisibility(View.VISIBLE);
            mPanel.setVisibility(View.VISIBLE);
            if (templateShown[0]) {
                final ViewGroup templateRoot = (ViewGroup) mNativeAdView.getChildAt(0);
                mNativeAdView.post(() -> {
                    if (mNativeAd == null || templateRoot == null || templateRoot.getParent() != mNativeAdView) {
                        return;
                    }
                    int h = computeNativeSlotHeightPx();
                    FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                            FrameLayout.LayoutParams.MATCH_PARENT, h);
                    templateRoot.setLayoutParams(lp);
                    mNativeAd.registerAdView(templateRoot, null, null, lp, null);
                    mNativeAdView.requestLayout();
                });
            } else {
                mNativeAd.registerAdView(mNativeAdView, clickViews, null, null, null);
            }
        } else {
            printLogOnUI("this placement no cache!");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyAd();
        if (mSDMNative != null) {
            mSDMNative.setListener(null);
            mSDMNative.destroy();
        }
    }

    private void destroyAd() {
        if (mNativeAd != null) {
            mNativeAd.destroy();
        }
    }

    @Override
    protected void onPause() {
        if (mNativeAd != null) {
            mNativeAd.onPause();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (mNativeAd != null) {
            mNativeAd.onResume();
        }
        super.onResume();
    }

    private void exitNativePanel() {
        mData.clear();
        destroyAd();
        mPanel.setVisibility(View.GONE);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mPanel.getVisibility() == View.VISIBLE) {
            exitNativePanel();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        if (v == null) return;

        switch (v.getId()) {
            case R.id.load_ad_btn:
                final int adViewWidth = mNativeAdView.getWidth() != 0
                        ? mNativeAdView.getWidth()
                        : getResources().getDisplayMetrics().widthPixels;
                final int adViewHeight = adViewWidth * 3 / 4;
                loadAd(adViewWidth, adViewHeight);
                break;
            case R.id.is_ad_ready_btn:
                isAdReady();
                break;
            case R.id.show_ad_btn:
                if (isAdReady()) {
                    showAd();
                }
                break;
        }
    }
}
