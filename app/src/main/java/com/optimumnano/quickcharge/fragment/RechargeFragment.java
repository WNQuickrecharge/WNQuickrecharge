package com.optimumnano.quickcharge.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.activity.qrcode.QrCodeActivity;
import com.optimumnano.quickcharge.activity.selectAddress.SelectAddressActivity;
import com.optimumnano.quickcharge.base.BaseFragment;
import com.optimumnano.quickcharge.bean.Point;
import com.optimumnano.quickcharge.bean.SuggestionInfo;
import com.optimumnano.quickcharge.data.PreferencesHelper;
import com.optimumnano.quickcharge.event.OnPushDataEvent;
import com.optimumnano.quickcharge.manager.MapManager;
import com.optimumnano.quickcharge.net.ManagerCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.xutils.common.util.LogUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 充电
 */
public class RechargeFragment extends BaseFragment {
    @Bind(R.id.mapView)
    MapView mapView;
    @Bind(R.id.iv_location)
    ImageView ivLocation;
    @Bind(R.id.et_address)
    EditText etAddress;
    @Bind(R.id.et_phone)
    EditText etPhone;
    @Bind(R.id.et_plate)
    EditText etPlate;
    @Bind(R.id.tv_tip)
    TextView tvTip;
    @Bind(R.id.tv_charge_now)
    TextView tvChargeNow;
    @Bind(R.id.tv_charge_late)
    TextView tvChargeLate;
    @Bind(R.id.tv_scan_charge)
    TextView tvScanCharge;

    private MapManager mManager=new MapManager();

    public LocationClient locationClient;
    public BDLocationListener myListener = new MyLocationListener();
    private BaiduMap mBaidumap;

    private PreferencesHelper mHelper;
    private List<Point> mPiont;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        SDKInitializer.initialize(getActivity().getApplicationContext());

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.fragment_recharge, container, false);
        ButterKnife.bind(this, mainView);
        mHelper = new PreferencesHelper(getActivity());
        return mainView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        locationClient = new LocationClient(getActivity().getApplicationContext());
        locationClient.registerLocationListener(myListener);
        initLocation();
        startLocation();
        mBaidumap = mapView.getMap();

    }

    public void startLocation() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            locationClient.start();
        }

    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");
        //可选，默认gcj02，设置返回的定位结果坐标系
        int span = 30000;
        option.setScanSpan(span);
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);
        //可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);
        //可选，默认false,设置是否使用gps
        option.setLocationNotify(true);
        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);
        //可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);
        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        locationClient.setLocOption(option);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.iv_location, R.id.et_address, R.id.tv_charge_now, R.id.tv_charge_late, R.id.tv_scan_charge})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_location:
                break;
            case R.id.et_address:
                SelectAddressActivity.start(getActivity());
                break;
            case R.id.tv_charge_now:
                break;
            case R.id.tv_charge_late:
                break;
            case R.id.tv_scan_charge:
                QrCodeActivity.start(getActivity());
                break;
        }
    }

    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //获取定位结果
            StringBuffer sb = new StringBuffer(256);

            sb.append(location.getLatitude() + "\n");    //获取纬度信息
            sb.append(location.getLongitude());    //获取纬度信息
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            //定义Maker坐标点
            LatLng point = new LatLng(latitude, longitude);

            MapStatus mMapStatus = new MapStatus.Builder()
                    .target(point)
                    .zoom(13)
                    .build();
            //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
            MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
            //改变地图状态
            mBaidumap.setMapStatus(mMapStatusUpdate);

//            //构建Marker图标
//            marker(point, R.drawable.icon_openmap_mark);
//            marker(new LatLng(22.5616, 113.951462), R.drawable.icon_openmap_focuse_mark);
//            marker(new LatLng(22.5139, 113.952736), R.drawable.icon_openmap_focuse_mark);
//            marker(new LatLng(22.5271, 113.955156), R.drawable.icon_openmap_focuse_mark);
//            marker(new LatLng(22.5853, 113.955866), R.drawable.icon_openmap_focuse_mark);

            if (location.getLocType() == BDLocation.TypeGpsLocation) {
                sb.append(location.getAddrStr());    //获取地址信息
                mHelper.updateCity(location.getCity());
                mHelper.setLocation(location.getLatitude(), location.getLatitude());
                initPoint();
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                // 网络定位结果
                sb.append(location.getAddrStr());    //获取地址信息
                mHelper.updateCity(location.getCity());
                mHelper.setLocation(location.getLatitude(), location.getLatitude());
                initPoint();
            }
            //定位失败
            else {
            }
            sb.append(location.getLocationDescribe());    //位置语义化信息
            LogUtil.d(sb.toString());
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {
        }
    }

    private void initPoint() {
        mManager.getReigonInfo(mHelper, new ManagerCallback() {
            @Override
            public void onSuccess(Object returnContent) {
                super.onSuccess(returnContent);
                mPiont= (List<Point>) returnContent;
                EventBus.getDefault().post(new OnPushDataEvent(mPiont));
            }
            @Override
            public void onFailure(String msg) {
                super.onFailure(msg);
            }
        });
    }

    private void marker(LatLng point, int pic) {
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(pic);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap);
        //在地图上添加Marker，并显示
        mapView.getMap().addOverlay(option);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (SelectAddressActivity.REQUEST_CODE == requestCode
                && Activity.RESULT_OK == resultCode) {
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                SuggestionInfo info= (SuggestionInfo) bundle.getSerializable(SelectAddressActivity.KEY_FOR_RESULT);
                etAddress.setText(info.key);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mapView != null)
            mapView.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mapView != null)
            mapView.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mapView != null)
            mapView.onDestroy();
    }


}
