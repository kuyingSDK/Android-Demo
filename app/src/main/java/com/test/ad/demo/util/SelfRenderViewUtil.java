package com.test.ad.demo.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.smartdigimkt.sdk.api.format.SDMNativeAd;
import com.smartdigimkt.sdk.api.format.SDMNativeAdMediaViewListener;
import com.test.ad.demo.R;
import com.test.ad.demo.view.NetworkImageView;

import java.util.ArrayList;
import java.util.List;

public class SelfRenderViewUtil {
    private static final String TAG = SelfRenderViewUtil.class.getSimpleName();

    public static List<View> bindSelfRenderView(Context context, SDMNativeAd adMaterial, View selfRenderView) {
        //log

        int padding = dip2px(context, 5);
        selfRenderView.setPadding(padding, padding, padding, padding);
        TextView titleView = (TextView) selfRenderView.findViewById(R.id.native_ad_title);
        TextView descView = (TextView) selfRenderView.findViewById(R.id.native_ad_desc);
        TextView ctaView = (TextView) selfRenderView.findViewById(R.id.native_ad_install_btn);
        TextView adFromView = (TextView) selfRenderView.findViewById(R.id.native_ad_from);
        FrameLayout iconArea = (FrameLayout) selfRenderView.findViewById(R.id.native_ad_image);
        FrameLayout contentArea = (FrameLayout) selfRenderView.findViewById(R.id.native_ad_content_image_area);
        final NetworkImageView logoView = (NetworkImageView) selfRenderView.findViewById(R.id.native_ad_logo);
        View closeView = selfRenderView.findViewById(R.id.native_ad_close);
        FrameLayout shakeViewContainer = (FrameLayout) selfRenderView.findViewById(R.id.native_ad_shake_view_container);
        FrameLayout slideViewContainer = (FrameLayout) selfRenderView.findViewById(R.id.native_ad_slide_view_container);
        FrameLayout rotateViewContainer = (FrameLayout) selfRenderView.findViewById(R.id.native_ad_rotate_view_container);
        FrameLayout adLogoContainer = selfRenderView.findViewById(R.id.native_ad_logo_container);   //v6.1.52+

        // bind view
        List<View> clickViewList = new ArrayList<>();//click views


        String title = adMaterial.getTitle();
        // title
        if (!TextUtils.isEmpty(title)) {
            titleView.setText(title);
            clickViewList.add(titleView);
            titleView.setVisibility(View.VISIBLE);
        } else {
            titleView.setVisibility(View.GONE);
        }


        String descriptionText = adMaterial.getDescription();
        if (!TextUtils.isEmpty(descriptionText)) {
            // desc
            descView.setText(descriptionText);
            clickViewList.add(descView);
            descView.setVisibility(View.VISIBLE);
        } else {
            descView.setVisibility(View.GONE);
        }

        // icon
        String iconImageUrl = adMaterial.getIcon();
        final NetworkImageView iconView = new NetworkImageView(context);
        if (!TextUtils.isEmpty(iconImageUrl)) {
            iconArea.addView(iconView);
            iconView.setImageUrl(iconImageUrl);
            clickViewList.add(iconView);
            iconArea.setVisibility(View.VISIBLE);
        }

        // cta button
        String callToActionText = adMaterial.getCallToAction();
        if (!TextUtils.isEmpty(callToActionText)) {
            ctaView.setText(callToActionText);
            clickViewList.add(ctaView);
            ctaView.setVisibility(View.VISIBLE);
        } else {
            ctaView.setVisibility(View.GONE);
        }


        // media view
        View mediaView = adMaterial.getMediaView(context, new SDMNativeAdMediaViewListener() {
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

            @Override
            public void onClickCloseView() {

            }
        });
        int mainImageHeight = adMaterial.getAdImageHeight();
        int mainImageWidth = adMaterial.getAdImageWidth();
        FrameLayout.LayoutParams mainImageParam = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT
                , FrameLayout.LayoutParams.WRAP_CONTENT);
        if (mediaView == null) {
            ViewTreeObserver viewTreeObserver = selfRenderView.getViewTreeObserver();
            viewTreeObserver.addOnGlobalLayoutListener(
                    new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            // 移除监听器
                            selfRenderView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                            int realMainImageWidth = selfRenderView.getWidth() - dip2px(context,
                                    10);
                            int realMainHeight = 0;

                            if (mainImageWidth > 0 && mainImageHeight > 0 && mainImageWidth > mainImageHeight) {
                                realMainHeight = realMainImageWidth * mainImageHeight / mainImageWidth;
                                mainImageParam.width = realMainImageWidth;
                                mainImageParam.height = realMainHeight;
                            } else {
                                mainImageParam.width = FrameLayout.LayoutParams.MATCH_PARENT;
                                mainImageParam.height = realMainImageWidth * 600 / 1024;
                            }
                        }
                    });
        } else {
            int realMainImageWidth = context.getResources()
                    .getDisplayMetrics().widthPixels - dip2px(context, 10);
            if (context.getResources().getDisplayMetrics().widthPixels > context.getResources()
                    .getDisplayMetrics().heightPixels) {//Horizontal screen
                realMainImageWidth = context.getResources()
                        .getDisplayMetrics().widthPixels - dip2px(context, 10) - dip2px(context,
                        330) - dip2px(context, 130);
            }
            if (mainImageWidth > 0 && mainImageHeight > 0 && mainImageWidth > mainImageHeight) {
                mainImageParam.width = FrameLayout.LayoutParams.MATCH_PARENT;
                mainImageParam.height = realMainImageWidth * mainImageHeight / mainImageWidth;
            } else {
                mainImageParam.width = FrameLayout.LayoutParams.MATCH_PARENT;
                mainImageParam.height = realMainImageWidth * 600 / 1024;
            }
        }

        contentArea.removeAllViews();
        if (mediaView != null) {
            if (mediaView.getParent() != null) {
                ((ViewGroup) mediaView.getParent()).removeView(mediaView);
            }
            mainImageParam.gravity = Gravity.CENTER;
            mediaView.setLayoutParams(mainImageParam);
            contentArea.addView(mediaView, mainImageParam);
            //clickViewList.add(mediaView);
            contentArea.setVisibility(View.VISIBLE);
        } else {
            contentArea.removeAllViews();
            contentArea.setVisibility(View.GONE);
        }


        //Ad Logo
        String adChoiceIconUrl = adMaterial.getAdChoiceIconUrl();
        if (!TextUtils.isEmpty(adChoiceIconUrl)) {
            logoView.setImageUrl(adChoiceIconUrl);
            logoView.setVisibility(View.VISIBLE);
        }else {
            logoView.setImageBitmap(null);
            logoView.setVisibility(View.GONE);
        }

        //⚠️注意：以下三种组件只渲染其中一种即可，若广告不支持则返回的View为null。
        shakeViewContainer.removeAllViews();
        slideViewContainer.removeAllViews();
        rotateViewContainer.removeAllViews();

        View sixInfoView = selfRenderView.findViewById(R.id.six_info);
        if (!TextUtils.isEmpty(adMaterial.getAppName())) {
            sixInfoView.setVisibility(View.VISIBLE);
            TextView functionTextView = sixInfoView.findViewById(R.id.function_test);
            TextView developerTextView = sixInfoView.findViewById(R.id.developer_test);
            TextView versionTextView = sixInfoView.findViewById(R.id.version_test);
            TextView privacyTextView = sixInfoView.findViewById(R.id.privacy_test);
            TextView permissionTextView = sixInfoView.findViewById(R.id.permission_test);

            developerTextView.setText(
                    TextUtils.isEmpty(adMaterial.getPublisher()) ? "" : adMaterial.getPublisher());
            versionTextView.setText(
                    TextUtils.isEmpty(adMaterial.getAppVersion()) ? "" : adMaterial.getAppVersion());

            if (!TextUtils.isEmpty(adMaterial.getFunctionUrl())) {
                functionTextView.setVisibility(View.VISIBLE);
                setOpenUrlClickListener(functionTextView, adMaterial.getFunctionUrl());
            } else {
                functionTextView.setOnClickListener(null);
                functionTextView.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(adMaterial.getPrivacyUrl())) {
                privacyTextView.setVisibility(View.VISIBLE);
                setOpenUrlClickListener(privacyTextView, adMaterial.getPrivacyUrl());
            } else {
                privacyTextView.setVisibility(View.GONE);
                privacyTextView.setOnClickListener(null);
            }

            if (!TextUtils.isEmpty(adMaterial.getPermissionUrl())) {
                permissionTextView.setVisibility(View.VISIBLE);
                setOpenUrlClickListener(permissionTextView, adMaterial.getPermissionUrl());
            } else {
                permissionTextView.setVisibility(View.GONE);
                permissionTextView.setOnClickListener(null);
            }
        } else {
            sixInfoView.setVisibility(View.GONE);
        }

        return clickViewList;
    }


    private static void setOpenUrlClickListener(View view, final String url) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    Context context = view.getContext();
                    if (context != null) {
                        context.startActivity(intent);
                    }
                } catch (Throwable e2) {
                    e2.printStackTrace();
                }
            }
        });

    }

    public static int dip2px(Context context, float dipValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }


}
