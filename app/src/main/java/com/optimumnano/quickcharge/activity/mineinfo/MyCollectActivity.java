package com.optimumnano.quickcharge.activity.mineinfo;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.base.BaseActivity;

/**
 * Created by mfwn on 2017/4/8.
 */

public class MyCollectActivity extends BaseActivity {
    private RecyclerView recyclerView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_collect);
        initViews();
    }

    @Override
    public void initViews() {
        super.initViews();
        setTitle("收藏");
        tvLeft.setVisibility(View.VISIBLE);
        recyclerView= (RecyclerView) findViewById(R.id.collects_recyclerView);
    }
}
