package com.test.ad.demo.bean;

import com.smartdigimkt.sdk.api.format.SDMNativeAd;


public class RecycleViewDataBean {
    public final static int NORMAL_DATA_TYPE = 1;
    public final static int AD_DATA_TYPE = 2;

    public int dataType;
    public String content;
    public SDMNativeAd nativeAd;
}
