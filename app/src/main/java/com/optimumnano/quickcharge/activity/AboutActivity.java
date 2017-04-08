package com.optimumnano.quickcharge.activity;

import android.os.Bundle;
import android.view.View;

import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.base.BaseActivity;

/**
 * Created by mfwn on 2017/4/8.
 */

public class AboutActivity extends BaseActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initViews();
    }

    @Override
    public void initViews() {
        super.initViews();
        setTitle("关于");
        tvLeft.setVisibility(View.VISIBLE);
    }
}
