package com.optimumnano.quickcharge.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.adapter.CollectionStationAdapter;
import com.optimumnano.quickcharge.adapter.StationGunsAdapter;
import com.optimumnano.quickcharge.base.BaseActivity;
import com.optimumnano.quickcharge.bean.GunBean;
import com.optimumnano.quickcharge.bean.StationBean;
import com.optimumnano.quickcharge.views.MyDivier;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mfwn on 2017/4/10.
 */

public class StationActivity extends BaseActivity {

    private StationBean station;
    private RecyclerView recyclerView;
    private TextView stationName,stationAddress,serviceName,onServiceTime,stationDistance,stationFreeGuns,stationTotalGuns;
    private TextView stationGunOperation;
    private StationGunsAdapter adapter;
    private List<GunBean>  gunBeanList=new ArrayList<>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_detail);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        station = (StationBean) extras.getSerializable("Station");
        initViews();
        initData();
        dataChanged();
    }

    private void dataChanged() {
        if (adapter == null) {
            adapter = new StationGunsAdapter(R.layout.adapter_station_gun, gunBeanList);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }


    @Override
    public void initViews() {
        super.initViews();
        setTitle(station.getStationName());
        tvLeft.setVisibility(View.VISIBLE);
        stationName= (TextView) findViewById(R.id.station_detail_name);
        stationAddress= (TextView) findViewById(R.id.station_detail_address);
        serviceName= (TextView) findViewById(R.id.station_detail_serviceName);
        onServiceTime= (TextView) findViewById(R.id.station_detail_onServiceTime);
        recyclerView= (RecyclerView) findViewById(R.id.station_guns_recycleView);
        stationDistance= (TextView) findViewById(R.id.station_detail_distance);
        stationTotalGuns= (TextView) findViewById(R.id.station_detail_total_guns);
        stationFreeGuns= (TextView) findViewById(R.id.station_detail_free_guns);
        stationGunOperation= (TextView) findViewById(R.id.gun_status_operation);
    }
    private void initData() {
        stationName.setText(station.getStationName());
        stationAddress.setText(station.getAddress());
        serviceName.setText(station.getServiceName());
        onServiceTime.setText(station.getOnServiceTime());
        stationFreeGuns.setText(station.getFreeGuns());
        stationTotalGuns.setText(station.getTotalGuns());
        gunBeanList.add(new GunBean("0001","正在充电"));
        gunBeanList.add(new GunBean("0002","正在充电"));
        gunBeanList.add(new GunBean("0003","正在充电"));
        gunBeanList.add(new GunBean("0004","立即充电"));
        gunBeanList.add(new GunBean("0005","立即充电"));
        gunBeanList.add(new GunBean("0006","立即充电"));
        gunBeanList.add(new GunBean("0007","立即充电"));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        MyDivier de = new MyDivier(this,MyDivier.VERTICAL_LIST);
        recyclerView.addItemDecoration(de);
        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {

            }
        });
    }
}
