package com.optimumnano.quickcharge.activity.qrcode;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.base.BaseActivity;
import com.optimumnano.quickcharge.utils.PublicUtils;
import com.uuzuche.lib_zxing.activity.CaptureFragment;
import com.uuzuche.lib_zxing.activity.CodeUtils;

/**
 * 扫描获取车牌
 * Created by zhangjiancheng on 2017/6/8.
 */

public class QrCodeForCarActivity extends BaseActivity {

    CaptureFragment fragment;

    private FrameLayout qrFrameLayout;
    private ImageView light;
    private boolean isLightOpen = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code_for_car);
        initViews();

        qrFrameLayout = (FrameLayout) findViewById(R.id.qr_code);
        light = (ImageView) findViewById(R.id.iv_deng);

        fragment = new CaptureFragment();
        // 为二维码扫描界面设置定制化界面
        CodeUtils.setFragmentArgs(fragment, R.layout.qr_code_layout);
        fragment.setAnalyzeCallback(analyzeCallback);
        getSupportFragmentManager().beginTransaction().replace(R.id.qr_code, fragment).commit();
        initListener();
    }

    private void initListener() {
        light.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isLightOpen) {
                    openLight();
                } else {
                    closeLight();
                }
            }
        });
    }

    @Override
    public void initViews() {
        super.initViews();
        setTitle("扫码获取车牌");
    }

    /**
     * {"plate":"粤F12345","vin":"LGAX4C448F8012597","terminalnum":"1507010273"}
     * 二维码解析回调函数
     */
    CodeUtils.AnalyzeCallback analyzeCallback = new CodeUtils.AnalyzeCallback() {
        @Override
        public void onAnalyzeSuccess(Bitmap mBitmap, final String result) {
            String plate = PublicUtils.getPlateValue(result,"plate");
            String vin = PublicUtils.getPlateValue(result,"vin");
            String terminalnum = PublicUtils.getPlateValue(result,"terminalnum");
            if (null == plate) {
                showToast("请扫正确的车牌号二维码");
                return;
            } else {
                Intent intent = new Intent();
                intent.putExtra("qr_result", plate);
                intent.putExtra("vin", vin);
                intent.putExtra("terminalnum", terminalnum);
                setResult(523, intent);
                finish();
            }
        }

        @Override
        public void onAnalyzeFailed() {
            showToast("扫码失败");
        }
    };

    public void openLight() {
        light.setImageResource(R.drawable.ic_qr_deng_on);
        isLightOpen = true;
        /**
         * 打开闪光灯
         */
        CodeUtils.isLightEnable(true);
    }

    private void closeLight() {
        light.setImageResource(R.drawable.ic_qr_deng);
        isLightOpen = false;
        /**
         * 关闭闪光灯
         */
        CodeUtils.isLightEnable(false);
    }
}
