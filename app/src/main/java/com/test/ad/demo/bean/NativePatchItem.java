package com.test.ad.demo.bean;


import com.smartdigimkt.sdk.api.format.SDMNativeAd;

public class NativePatchItem {

    public int type = 0;

    public SDMNativeAd nativeAd;
    public String videoUrl;


    public NativePatchItem(int type, SDMNativeAd nativeAd, String videoUrl) {
        this.type = type;
        this.nativeAd = nativeAd;
        this.videoUrl = videoUrl;
    }
}
