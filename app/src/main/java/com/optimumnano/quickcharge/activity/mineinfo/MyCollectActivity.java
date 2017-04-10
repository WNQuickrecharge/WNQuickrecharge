package com.optimumnano.quickcharge.activity.mineinfo;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.adapter.CollectionStationAdapter;
import com.optimumnano.quickcharge.base.BaseActivity;
import com.optimumnano.quickcharge.bean.StationBean;
import com.optimumnano.quickcharge.views.MyDivier;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mfwn on 2017/4/8.
 */

public class MyCollectActivity extends BaseActivity {
    private RecyclerView recyclerView;
    private CollectionStationAdapter adapter;
    private List<StationBean> stationBeanList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_collect);
        initViews();
        initData();
        dataChanged();
    }

    private void initData() {//114.3717,22.704188
        stationBeanList.add(new StationBean(114.3717, 22.704188, "天俊工业园", "沃特玛", "8:00-18:30", "10", "20", "1.5", "0.2", "燕子岭充电站"));
        stationBeanList.add(new StationBean(114.3717, 22.704188, "天俊工业园", "民富", "8:00-18:30", "10", "20", "1.5", "0.2", "燕子岭充电站"));
        stationBeanList.add(new StationBean(114.3717, 22.704188, "天俊工业园", "沃特玛", "8:00-18:30", "10", "20", "1.5", "0.2", "燕子岭充电站"));
    }

    @Override
    public void initViews() {
        super.initViews();
        setTitle("收藏");
        tvLeft.setVisibility(View.VISIBLE);
        recyclerView = (RecyclerView) findViewById(R.id.collects_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        MyDivier de = new MyDivier(this, MyDivier.VERTICAL_LIST);
        recyclerView.addItemDecoration(de);
        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {

            }
        });
    }

    private void dataChanged() {
        if (adapter == null) {
            adapter = new CollectionStationAdapter(this, R.layout.adapter_collect_station, stationBeanList);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }
}
