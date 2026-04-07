package com.test.ad.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;

public class NativeMainActivity extends Activity implements View.OnClickListener {

    private RelativeLayout nativeBtn;
    private RelativeLayout nativeExpressBtn;
//    private RelativeLayout nativeListBtn;
//    private RelativeLayout nativeDrawBtn;
//    private RelativeLayout nativePatchBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_native_main);

        initView();
    }

    private void initView() {
        TitleBar titleBar = findViewById(R.id.title_bar);
        titleBar.setTitle(R.string.sdm_title_native);
        titleBar.setListener(new TitleBarClickListener() {
            @Override
            public void onBackClick(View v) {
                finish();
            }
        });

        nativeBtn = findViewById(R.id.nativeBtn);
        nativeExpressBtn = findViewById(R.id.nativeExpressBtn);

        nativeBtn.setOnClickListener(this);
        nativeExpressBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.nativeBtn:
                Intent intent1 = new Intent(NativeMainActivity.this, NativeAdActivity.class);
                intent1.putExtra("native_type", NativeAdActivity.NATIVE_SELF_RENDER_TYPE);
                startActivity(intent1);
                break;
            case R.id.nativeExpressBtn:
                Intent intent2 = new Intent(NativeMainActivity.this, NativeAdActivity.class);
                intent2.putExtra("native_type", NativeAdActivity.NATIVE_EXPRESS_TYPE);
                startActivity(intent2);
                break;
        }
    }

}
