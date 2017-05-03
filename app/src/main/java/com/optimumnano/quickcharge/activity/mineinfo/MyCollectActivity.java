package com.optimumnano.quickcharge.activity.mineinfo;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.netease.hearttouch.htrefreshrecyclerview.HTRefreshListener;
import com.netease.hearttouch.htrefreshrecyclerview.HTRefreshRecyclerView;
import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.adapter.CollectionStationAdapter;
import com.optimumnano.quickcharge.baiduUtil.BaiduNavigation;
import com.optimumnano.quickcharge.baiduUtil.WTMBaiduLocation;
import com.optimumnano.quickcharge.base.BaseActivity;
import com.optimumnano.quickcharge.bean.StationBean;
import com.optimumnano.quickcharge.dialog.MyDialog;
import com.optimumnano.quickcharge.manager.CollectManager;
import com.optimumnano.quickcharge.manager.EventManager;
import com.optimumnano.quickcharge.net.ManagerCallback;
import com.optimumnano.quickcharge.views.MyDivier;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.common.util.LogUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mfwn on 2017/4/8.
 */

public class MyCollectActivity extends BaseActivity implements HTRefreshListener {
    private HTRefreshRecyclerView recyclerView;
    private CollectionStationAdapter adapter;
    private List<StationBean> stationBeanList = new ArrayList<>();
    private CollectManager manager=new CollectManager();
    private MyDialog myDialog;
    private BaiduNavigation navigation;
    private WTMBaiduLocation location;
    private LatLng myPoint;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_collect);
        navigation=new BaiduNavigation(this);
        initViews();
        initData();
        EventBus.getDefault().register(this);
        //dataChanged();
    }

    private void initData() {//114.3717,22.704188
        manager.getCollect(new ManagerCallback() {
            @Override
            public void onSuccess(Object returnContent) {
                super.onSuccess(returnContent);
                LogUtil.i("ret"+returnContent);
                List<StationBean> list = JSON.parseArray(returnContent.toString(), StationBean.class);
                LocationClient client=new LocationClient(MyCollectActivity.this);
                for (StationBean bean: list) {
                    double distance = DistanceUtil.getDistance(myPoint, new LatLng(Double.parseDouble(bean.getLat()), Double.parseDouble(bean.getLng())));
                    distance/=1000;
                    DecimalFormat decimalFormat=new DecimalFormat("0.00");
                    String format = decimalFormat.format(distance);
                    bean.setDistance(format+" km");
                }
                stationBeanList.clear();
                stationBeanList.addAll(list);
                recyclerView.setRefreshCompleted(true);
                dataChanged();
                closeLoading();
            }

            @Override
            public void onFailure(String msg) {
                super.onFailure(msg);
                recyclerView.setRefreshCompleted(true);
                closeLoading();
                showToast(msg);
            }
        });


    }

    @Override
    public void initViews() {
        super.initViews();
        setTitle("收藏");
        location=new WTMBaiduLocation(this);
        location.start();
        location.setLocationListner(new WTMBaiduLocation.OnLocationReceivedListner() {
            @Override
            public void onLocationReceived(BDLocation bdLocation) {
                myPoint=new LatLng(bdLocation.getLatitude(),bdLocation.getLongitude());
                location.stopLocation();
            }
        });
        tvLeft.setVisibility(View.VISIBLE);
        myDialog=new MyDialog(this,R.style.MyDialog);
        myDialog.setCancelable(true);
        recyclerView = (HTRefreshRecyclerView) findViewById(R.id.collects_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter=new CollectionStationAdapter(this, R.layout.adapter_collect_station, stationBeanList, new CollectionStationAdapter.OnItemLongClickListener() {
            @Override
            public void onLongClick(final StationBean item) {
                myDialog.setTitle("删除收藏");
                myDialog.setMessage("您要删除该收藏站点吗?");
                myDialog.setNoOnclickListener("取消", new MyDialog.onNoOnclickListener() {
                    @Override
                    public void onNoClick() {
                        myDialog.dismiss();
                    }
                });
                myDialog.setYesOnclickListener("确定", new MyDialog.onYesOnclickListener() {
                    @Override
                    public void onYesClick() {
                        manager.deleteCollectStation(item.getId(), new ManagerCallback() {
                            @Override
                            public void onSuccess(Object returnContent) {
                                super.onSuccess(returnContent);
                                showToast("删除成功!");
                                stationBeanList.remove(item);
                                dataChanged();
                                myDialog.dismiss();
                            }

                            @Override
                            public void onFailure(String msg) {
                                super.onFailure(msg);
                                showToast(msg);
                            }
                        });
                    }
                });
                myDialog.show();
            }
        });
        recyclerView.setAdapter(adapter);
        MyDivier de = new MyDivier(this, MyDivier.VERTICAL_LIST);
        recyclerView.addItemDecoration(de);
        recyclerView.setOnRefreshListener(this);//实现OnRefreshListener接口
        recyclerView.setLoadMoreViewShow(false);
        recyclerView.setEnableScrollOnRefresh(true);


    }

    private void dataChanged() {
//        if (adapter == null) {
//            adapter = new CollectionStationAdapter(this, R.layout.adapter_collect_station, stationBeanList);
//            recyclerView.setAdapter(adapter);
//        } else {
            adapter.notifyDataSetChanged();
//        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void startGPS(EventManager.startGPS event) {
        LatLng latLng = event.latLng;
        showLoading();
        navigation.start(myPoint,latLng);
        navigation.setOnRoutePlanDoneListener(new BaiduNavigation.OnRoutePlanDoneListener() {
            @Override
            public void onRoutePlanDone() {
                closeLoading();
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        location.stopLocation();
    }

    @Override
    public void onRefresh() {
        initData();
    }
}
