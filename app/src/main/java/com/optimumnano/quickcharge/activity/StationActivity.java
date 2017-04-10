package com.optimumnano.quickcharge.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.base.BaseActivity;
import com.optimumnano.quickcharge.bean.StationBean;

import java.io.Serializable;

/**
 * Created by mfwn on 2017/4/10.
 */

public class StationActivity extends BaseActivity {

    private StationBean station;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_detail);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        station = (StationBean) extras.getSerializable("Station");
        initViews();
    }

    @Override
    public void initViews() {
        super.initViews();
        setTitle(station.getStationName());
        tvLeft.setVisibility(View.VISIBLE);
    }
}
