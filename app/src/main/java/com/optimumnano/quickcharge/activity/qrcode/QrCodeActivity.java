package com.optimumnano.quickcharge.activity.qrcode;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.optimumnano.quickcharge.Constants;
import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.activity.order.OrderActivity;
import com.optimumnano.quickcharge.base.BaseActivity;
import com.uuzuche.lib_zxing.activity.CaptureFragment;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

// TODO: 2017/4/6 0006 扫码二维码的值
// TODO: 2017/4/6 0006 与页面关联
public class QrCodeActivity extends BaseActivity {

    CaptureFragment fragment = new CaptureFragment();
    public static final int REQUEST_CODE = 113;
    @Bind(R.id.et_record_number)
    EditText etRecordNumber;
    @Bind(R.id.tv_submit)
    TextView tvSubmit;
    @Bind(R.id.rb_qr)
    RadioButton rbQr;
    @Bind(R.id.rb_write)
    RadioButton rbWrite;
    @Bind(R.id.ll_button)
    LinearLayout llButton;
    @Bind(R.id.iv_deng)
    ImageView ivDeng;
    @Bind(R.id.ll_sence_two)
    LinearLayout llSenceTwo;
    @Bind(R.id.rg_buttom)
    RadioGroup rgButtom;
    @Bind(R.id.ll_sence_two_Top)
    LinearLayout llSenceTwoTop;

    private boolean isLightOpen = false;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);
        ButterKnife.bind(this);

        initViews();
        // 为二维码扫描界面设置定制化界面
        CodeUtils.setFragmentArgs(fragment, R.layout.qr_code_layout);
        fragment.setAnalyzeCallback(analyzeCallback);
        getSupportFragmentManager().beginTransaction().replace(R.id.qr_code, fragment).commit();

        rgButtom.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rb_qr:
                        llSenceTwo.setBackgroundColor(Color.parseColor("#00000000"));
                        llSenceTwoTop.setVisibility(View.INVISIBLE);
                        ivDeng.setVisibility(View.VISIBLE);
                        setTitle(getString(R.string.qr_title));
                        break;
                    case R.id.rb_write:
                        llSenceTwoTop.setVisibility(View.VISIBLE);
                        tvTitle.setText(R.string.client_number);
                        llSenceTwo.setBackgroundColor(Color.parseColor("#999999"));
                        ivDeng.setVisibility(View.GONE);
                        closeLight();
                        break;
                }
            }
        });

    }

    public void openLight() {
        ivDeng.setImageResource(R.drawable.ic_qr_deng_on);
        isLightOpen = true;
        /**
         * 打开闪光灯
         */
        CodeUtils.isLightEnable(true);
    }

    private void closeLight() {
        ivDeng.setImageResource(R.drawable.ic_qr_deng);
        isLightOpen = false;
        /**
         * 关闭闪光灯
         */
        CodeUtils.isLightEnable(false);
    }



    @Override
    public void initViews() {
        super.initViews();
        setTitle(getString(R.string.qr_title));
    }

    @Override
    protected void onDestroy() {
        if (isLightOpen){
            closeLight();
        }
        super.onDestroy();
    }

    /**
     * 二维码解析回调函数
     */
    CodeUtils.AnalyzeCallback analyzeCallback = new CodeUtils.AnalyzeCallback() {
        @Override
        public void onAnalyzeSuccess(Bitmap mBitmap, final String result) {
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_SUCCESS);
            bundle.putString(CodeUtils.RESULT_STRING, result);
            resultIntent.putExtras(bundle);
            QrCodeActivity.this.setResult(RESULT_OK, resultIntent);
            QrCodeActivity.this.finish();
        }

        @Override
        public void onAnalyzeFailed() {
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_FAILED);
            bundle.putString(CodeUtils.RESULT_STRING, "");
            resultIntent.putExtras(bundle);
            QrCodeActivity.this.setResult(RESULT_OK, resultIntent);
            QrCodeActivity.this.finish();
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (REQUEST_CODE == requestCode
                && Activity.RESULT_OK == resultCode) {
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(Constants.RESULT_TYPE) == Constants.RESULT_SUCCESS) {
                    this.setResult(RESULT_OK, data);
                    this.finish();
                }
            }
        }
    }

    public static void start(Context mContext) {
        Intent intent = new Intent(mContext, QrCodeActivity.class);
        mContext.startActivity(intent);
    }

    @OnClick({R.id.tv_submit, R.id.iv_deng})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_submit:
                //跳转支付
                Bundle bundle = new Bundle();
                bundle.putString("gun_no","67867678901234517");
                skipActivity(OrderActivity.class,bundle);
                break;
            case R.id.iv_deng:
                if (!isLightOpen)
                openLight();
                else
                    closeLight();
                break;
        }
    }




}
