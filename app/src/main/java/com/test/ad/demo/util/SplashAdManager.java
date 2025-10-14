package com.test.ad.demo.util;

import android.content.Context;
import android.view.ViewGroup;

import com.smartdigimkt.sdk.api.AdError;
import com.smartdigimkt.sdk.api.SDMAdRequest;
import com.smartdigimkt.sdk.api.format.SDMSplashAd;
import com.smartdigimkt.sdk.api.format.SDMSplashAdExtraInfo;
import com.smartdigimkt.sdk.api.format.SDMSplashListener;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Description:开屏广告的管理类
 *
 */
public class SplashAdManager {
    private static final String TAG = SplashAdManager.class.getSimpleName();
    private static final Object mListenerLockObj = new Object();

    private SDMSplashAd mSdmSplashAd;
    private final Set<SDMSplashListener> mSplashListenerSet;

    private SplashAdManager() {
        // 保持私有构造方法不变
        mSplashListenerSet = new CopyOnWriteArraySet<SDMSplashListener>();
    }

    // 静态内部类持有单例实例
    private static class Holder {
        private static final SplashAdManager INSTANCE = new SplashAdManager();
    }

    // 简化获取实例方法
    public static SplashAdManager getInstance() {
        return Holder.INSTANCE;
    }

    public void addListener(SDMSplashListener listener) {
        mSplashListenerSet.add(listener);
    }

    public void loadAd(Context context, String placementId, SDMAdRequest adRequest) {
        if(mSdmSplashAd == null) {
            mSdmSplashAd = new SDMSplashAd(context, placementId);
            mSdmSplashAd.setListener(new SDMSplashListener() {
                @Override
                public void onAdLoaded(boolean isTimeout) {
                    if (mSplashListenerSet != null) {
                        synchronized (mListenerLockObj) {
                            for (SDMSplashListener listener : mSplashListenerSet) {
                                listener.onAdLoaded(isTimeout);
                            }
                        }
                    }
                }

                @Override
                public void onAdLoadTimeout() {
                    if (mSplashListenerSet != null) {
                        synchronized (mListenerLockObj) {
                            for (SDMSplashListener listener : mSplashListenerSet) {
                                listener.onAdLoadTimeout();
                            }
                        }
                    }
                }

                @Override
                public void onAdLoadFail(AdError adError) {
                    if (mSplashListenerSet != null) {
                        synchronized (mListenerLockObj) {
                            for (SDMSplashListener listener : mSplashListenerSet) {
                                listener.onAdLoadFail(adError);
                            }
                        }
                    }
                }

                @Override
                public void onAdShow() {
                    if (mSplashListenerSet != null) {
                        synchronized (mListenerLockObj) {
                            for (SDMSplashListener listener : mSplashListenerSet) {
                                listener.onAdShow();
                            }
                        }
                    }
                }

                @Override
                public void onAdClick() {
                    if (mSplashListenerSet != null) {
                        synchronized (mListenerLockObj) {
                            for (SDMSplashListener listener : mSplashListenerSet) {
                                listener.onAdClick();
                            }
                        }
                    }
                }

                @Override
                public void onAdClose(SDMSplashAdExtraInfo sdmSplashAdExtraInfo) {
                    if (mSplashListenerSet != null) {
                        synchronized (mListenerLockObj) {
                            for (SDMSplashListener listener : mSplashListenerSet) {
                                listener.onAdClose(sdmSplashAdExtraInfo);
                            }
                        }
                    }
                }

                @Override
                public void onAdShowFail(AdError adError) {
                    if (mSplashListenerSet != null) {
                        synchronized (mListenerLockObj) {
                            for (SDMSplashListener listener : mSplashListenerSet) {
                                listener.onAdShowFail(adError);
                            }
                        }
                    }
                }

                @Override
                public void onDeeplinkCallback(boolean isSuccess) {
                    if (mSplashListenerSet != null) {
                        synchronized (mListenerLockObj) {
                            for (SDMSplashListener listener : mSplashListenerSet) {
                                listener.onDeeplinkCallback(isSuccess);
                            }
                        }
                    }
                }
            });
        }
        mSdmSplashAd.load(adRequest);
    }

    public void showAd(ViewGroup container) {
        if (mSdmSplashAd != null) {
            mSdmSplashAd.show(container);
            mSdmSplashAd = null;
        } else {
            if (mSplashListenerSet != null) {
                synchronized (mListenerLockObj) {
                    for (SDMSplashListener listener : mSplashListenerSet) {
                        listener.onAdShowFail(new AdError("-1", "splash ad is null"));
                    }
                }
            }
        }
    }

    public boolean isAdReady() {
        if (mSdmSplashAd != null) {
            return mSdmSplashAd.isAdReady();
        }
        return false;
    }

    public SDMSplashAd getSdmSplashAd(){
        return mSdmSplashAd;
    }

    public void destroy() {
        if (mSdmSplashAd != null) {
            mSdmSplashAd.destroy();
            mSdmSplashAd = null;
        }
        if (mSplashListenerSet != null) {
            synchronized (mListenerLockObj) {
                mSplashListenerSet.clear();
            }
        }
    }
}
