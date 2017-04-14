package com.optimumnano.quickcharge.activity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.base.BaseActivity;
import com.optimumnano.quickcharge.net.HttpApi;

/**
 * Created by mfwn on 2017/4/8.
 */

public class AboutActivity extends BaseActivity {
    private WebView mWebView;
    private ProgressDialog progressDialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        progressDialog=new ProgressDialog(this);
        initViews();
    }

    @Override
    public void initViews() {
        super.initViews();
        setTitle("关于");
        tvLeft.setVisibility(View.VISIBLE);
        mWebView= (WebView) findViewById(R.id.about_webView);
        WebSettings settings = mWebView.getSettings();
        // 设置字符集编码
        settings.setDefaultTextEncodingName("UTF-8");
        // 开启JavaScript支持
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);//隐藏缩放控件
        settings.setBlockNetworkImage(true);// 首先阻塞图片，让图片不显示
        settings.setBlockNetworkImage(false);//  页面加载好以后，在放开图片：
        settings.setDomStorageEnabled(true);
        //下面两句设置要一起才能设置起作用.
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        mWebView.loadUrl(HttpApi.about_url);
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                showLoading();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                hideLoading();
            }
        });
    }

    public void showLoading() {
        if (progressDialog != null && !progressDialog.isShowing()) {
            progressDialog.show();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        progressDialog.dismiss();
        mWebView.destroy();
    }

    public void hideLoading() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
