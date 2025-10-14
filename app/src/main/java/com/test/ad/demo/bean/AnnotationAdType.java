package com.test.ad.demo.bean;

import android.support.annotation.IntDef;


import com.smartdigimkt.sdk.api.SDMAdConst.ATMixedFormatAdType;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef(value = {ATMixedFormatAdType.NATIVE, ATMixedFormatAdType.INTERSTITIAL, ATMixedFormatAdType.REWARDED_VIDEO,
        ATMixedFormatAdType.BANNER, ATMixedFormatAdType.SPLASH})
@Retention(RetentionPolicy.SOURCE)
public @interface AnnotationAdType {

}
