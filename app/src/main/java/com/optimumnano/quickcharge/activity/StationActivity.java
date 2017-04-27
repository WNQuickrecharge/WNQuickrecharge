package com.optimumnano.quickcharge.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.model.LatLng;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.adapter.StationPilesAdapter;
import com.optimumnano.quickcharge.baiduUtil.BaiduNavigation;
import com.optimumnano.quickcharge.baiduUtil.WTMBaiduLocation;
import com.optimumnano.quickcharge.base.BaseActivity;
import com.optimumnano.quickcharge.bean.GunBean;
import com.optimumnano.quickcharge.bean.PileBean;
import com.optimumnano.quickcharge.bean.StationBean;
import com.optimumnano.quickcharge.manager.StationManager;
import com.optimumnano.quickcharge.net.ManagerCallback;
import com.optimumnano.quickcharge.views.MyDivier;

import org.xutils.common.util.LogUtil;

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
    private StationPilesAdapter adapter;
    private List<PileBean>  pileBeanList=new ArrayList<>();
    private LinearLayout stationGPS;
    private WTMBaiduLocation location;
    private LatLng myPoint;
    private BaiduNavigation navigation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_detail);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        station = (StationBean) extras.getSerializable("Station");
        location=new WTMBaiduLocation(this);
        initViews();
        initData();
        dataChanged();
    }

    private void dataChanged() {
        if (adapter == null) {
            adapter = new StationPilesAdapter(R.layout.adapter_station_gun, pileBeanList,this);
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
        location.setLocationListner(new WTMBaiduLocation.OnLocationReceivedListner() {
            @Override
            public void onLocationReceived(BDLocation bdLocation) {
                myPoint=new LatLng(bdLocation.getLatitude(),bdLocation.getLongitude());
                location.stopLocation();
            }
        });
        stationGPS = (LinearLayout) findViewById(R.id.station_detail_GPS);
        stationGPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoading();
                navigation.start(myPoint,new LatLng(Double.parseDouble(station.getLat()),Double.parseDouble(station.getLng())));
                navigation.setOnRoutePlanDoneListener(new BaiduNavigation.OnRoutePlanDoneListener() {
                    @Override
                    public void onRoutePlanDone() {
                        closeLoading();
                    }
                });
            }
        });
    }
    private void initData() {
        navigation=new BaiduNavigation(this);
        stationName.setText(station.getStationName());
        stationAddress.setText(station.getAddress());
        serviceName.setText(station.getManagementCompany());
        onServiceTime.setText(station.getRunTimeSpan());
        stationFreeGuns.setText(station.getFreePiles()+"");
        stationTotalGuns.setText(station.getTotalPiles()+"");
        stationDistance.setText(station.getDistance());
        new StationManager().getGunsDetail(station.getId(), new ManagerCallback() {
            @Override
            public void onSuccess(Object returnContent) {
                super.onSuccess(returnContent);
                String result = (String) returnContent;
                List<GunBean> gunBeenList = JSON.parseArray(result, GunBean.class);
                List<PileBean> list = transGunBeanListToPileBeanList(gunBeenList);
                pileBeanList.addAll(list);
                dataChanged();
            }

            @Override
            public void onFailure(String msg) {
                super.onFailure(msg);
                showToast(msg);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        MyDivier de = new MyDivier(this,MyDivier.VERTICAL_LIST);
        recyclerView.addItemDecoration(de);
        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {

            }
        });
    }

    private List<PileBean> transGunBeanListToPileBeanList(List<GunBean> gunBeenList) {
        List<String> pileNoList=new ArrayList<>();
        for (int i=0;i<gunBeenList.size();i++) {
            if (!pileNoList.contains(gunBeenList.get(i).getPileNo())) {
                pileNoList.add(gunBeenList.get(i).getPileNo());
            }
        }
        List<PileBean> pileList=new ArrayList<>(pileNoList.size());
        for (int i = 0; i <pileNoList.size() ; ++i) {
            PileBean pileBean=new PileBean();
            List<GunBean> gunList = new ArrayList<>();
            for (int j = 0; j < gunBeenList.size(); ++j) {
                if (pileNoList.get(i).equals(gunBeenList.get(j).getPileNo())) {
                    gunList.add(gunBeenList.get(j));
                    pileBean.setGunList(gunList);
                }
            }
            pileList.add(pileBean);
        }
        LogUtil.i("pileList=="+pileList);
        return pileList;

    }

    @Override
    protected void onResume() {
        super.onResume();
        location.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        location.stopLocation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        location.onLocationDestroy();
    }


}
