package com.test.ad.demo.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.smartdigimkt.sdk.api.SDMAdConst;
import com.test.ad.demo.NativeAdActivity;
import com.test.ad.demo.TitleBar;
import com.test.ad.demo.bean.AnnotationAdType;
import com.test.ad.demo.bean.CommonViewBean;
import com.test.ad.demo.util.PlacementIdUtil;
import com.test.ad.demo.util.ViewUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public abstract class BaseActivity extends Activity {
    protected String TAG = getClass().getSimpleName();
    protected String mCurrentPlacementName;
    protected String mCurrentPlacementId;
    protected TextView mTVShowLog;
    private Map<String, String> mPlacementIdMap;
    private CommonViewBean mCommonViewBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(getContentViewId());

        initView();
        initListener();
        initData();
    }

    protected abstract int getContentViewId();

    protected abstract @AnnotationAdType
    int getAdType();

    protected abstract void onSelectPlacementId(String placementId);

    protected void initView() {
        initViewWithCommonView(getCommonViewBean());
    }

    protected void initListener() {
    }

    @CallSuper
    protected void initData() {
        initPlacementIdMap(getAdType());

        if (mCommonViewBean != null) {
            initPlacementListAdapter(mCommonViewBean.getSpinnerSelectPlacement());
        }
    }

    protected CommonViewBean getCommonViewBean() {
        return null;
    }

    private void initViewWithCommonView(CommonViewBean commonViewBean) {
        mCommonViewBean = commonViewBean;
        if (commonViewBean != null) {
            TitleBar titleBar = commonViewBean.getTitleBar();
            if (titleBar != null) {
                setTitleBar(titleBar, commonViewBean.getTitleResId());
            }
            mTVShowLog = commonViewBean.getTvLogView();
            if (mTVShowLog != null) {
                mTVShowLog.setMovementMethod(ScrollingMovementMethod.getInstance());
            }
        }
    }

    private void setTitleBar(TitleBar titleBar, int titleResId) {
        if (titleBar != null && titleResId != 0) {
            titleBar.setTitle(titleResId);
            titleBar.setListener(v -> {
                finish();
            });
        }
    }

    protected void printLogOnUI(String msg) {
        if (mTVShowLog == null || TextUtils.isEmpty(msg)) return;

        ViewUtil.printLog(mTVShowLog, msg);
    }

    protected String getNativeAdType() {
        return NativeAdActivity.NATIVE_SELF_RENDER_TYPE;
    }

    private void initPlacementIdMap(@AnnotationAdType int adType) {
        switch (adType) {
            case SDMAdConst.ATMixedFormatAdType.SPLASH:
                mPlacementIdMap = PlacementIdUtil.getSplashPlacements(this);
                break;
            case SDMAdConst.ATMixedFormatAdType.NATIVE:
                mPlacementIdMap = getNativeAdType().equals(NativeAdActivity.NATIVE_SELF_RENDER_TYPE) ? PlacementIdUtil.getNativeSelfrenderPlacements(this) : PlacementIdUtil.getNativeExpressPlacements(this);
                break;
            case SDMAdConst.ATMixedFormatAdType.BANNER:
                mPlacementIdMap = PlacementIdUtil.getBannerPlacements(this);
                break;
            case SDMAdConst.ATMixedFormatAdType.INTERSTITIAL:
                mPlacementIdMap = PlacementIdUtil.getInterstitialPlacements(this);
                break;
            case SDMAdConst.ATMixedFormatAdType.REWARDED_VIDEO:
                mPlacementIdMap = PlacementIdUtil.getRewardedVideoPlacements(this);
                break;
        }
    }

    protected void initPlacementListAdapter(Spinner spinner) {
        if (spinner == null || mPlacementIdMap == null || mPlacementIdMap.size() == 0) return;

        List<String> placementNameList = new ArrayList<>(mPlacementIdMap.keySet());

        sortPlacementList(placementNameList);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_dropdown_item, placementNameList);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new PlacementSelectListenerImpl());
    }

    private void sortPlacementList(List<String> placementNameList) {
        if (placementNameList != null && !placementNameList.isEmpty()) {
            Collections.sort(placementNameList);

            String excludeName = "All";
            if (placementNameList.contains(excludeName)) {
                placementNameList.remove(excludeName);
                placementNameList.add(0, excludeName);
            }
        }
    }


    private class PlacementSelectListenerImpl implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {
            mCurrentPlacementName = parent.getSelectedItem().toString();
            if (mPlacementIdMap != null && mPlacementIdMap.size() > 0) {
                mCurrentPlacementId = mPlacementIdMap.get(mCurrentPlacementName);
            }

            if (!TextUtils.isEmpty(mCurrentPlacementId)) {
                onSelectPlacementId(mCurrentPlacementId);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    }

}
