package com.optimumnano.quickcharge.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.jaychang.st.SimpleText;
import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.activity.qrcode.QrCodeActivity;
import com.optimumnano.quickcharge.activity.selectAddress.SelectAddressActivity;
import com.optimumnano.quickcharge.base.BaseFragment;
import com.optimumnano.quickcharge.bean.Point;
import com.optimumnano.quickcharge.bean.StationBean;
import com.optimumnano.quickcharge.bean.SuggestionInfo;
import com.optimumnano.quickcharge.data.PreferencesHelper;
import com.optimumnano.quickcharge.event.OnNaviEvent;
import com.optimumnano.quickcharge.event.OnPushDataEvent;
import com.optimumnano.quickcharge.manager.CityStationListManager;
import com.optimumnano.quickcharge.manager.EventManager;
import com.optimumnano.quickcharge.manager.MapManager;
import com.optimumnano.quickcharge.net.ManagerCallback;
import com.optimumnano.quickcharge.utils.SPConstant;
import com.optimumnano.quickcharge.utils.SharedPreferencesUtil;
import com.optimumnano.quickcharge.utils.Tool;
import org.greenrobot.eventbus.EventBus;
import org.xutils.common.util.LogUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.optimumnano.quickcharge.adapter.DistDetailAcapter.DoubleDP;

/**
 * 充电
 */
public class RechargeFragment extends BaseFragment {
    @Bind(R.id.mapView)
    TextureMapView mapView;
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
    private InfoWindow mInfoWindow;
    private MapManager mManager = new MapManager();

    public LocationClient locationClient;
    public BDLocationListener myListener = new MyLocationListener();
    private BaiduMap mBaiduMap;
    private PreferencesHelper mHelper;
    private List<Point> mPiont;
    boolean isFirstLoc = true; // 是否首次定位

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {


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
        mBaiduMap = mapView.getMap();
        locationClient = new LocationClient(getActivity().getApplicationContext());
        locationClient.registerLocationListener(myListener);
        initLocation();
//        startLocation();
        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mBaiduMap.hideInfoWindow();
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });

        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                //从marker中获取info信息
                Bundle bundle = marker.getExtraInfo();
                final Point infoUtil = (Point) bundle.getSerializable("info");
                View view = View.inflate(getContext(), R.layout.adapter_dist_point, null);
                ViewHolder holder = new ViewHolder(view);
                holder.mItem = infoUtil;
                holder.tvAddress.setText(holder.mItem.StationName);
                holder.tvDistance.setText(DoubleDP(holder.mItem.distance, "#.00"));
                holder.tvDetailAddress.setText(holder.mItem.Address);
                String sb = "电费:1.5元/度,服务费:0.5元/度";
                SimpleText st = SimpleText.create(holder.mView.getContext(), sb)
                        .first("1.5").first("0.5").textColor(R.color.red);
                st.linkify(holder.tvPricePer);
                holder.tvPricePer.setText(st);
                String ss = "空闲" + holder.mItem.FreePiles + "/共" + holder.mItem.TotalPiles + "个";
                SimpleText simpleText = SimpleText.create(holder.mView.getContext(), ss)
                        .first(holder.mItem.FreePiles+"").textColor(R.color.main_color);
                simpleText.linkify(holder.tvNum);
                holder.tvNum.setText(simpleText);
                view.setBackgroundResource(R.drawable.sp_map_infowindow);
                holder.tvNav.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OnNaviEvent event = new OnNaviEvent();
                        event.end = infoUtil;
                        EventBus.getDefault().post(event);
                    }
                });
                //将信息显示在界面上
                LatLng ll = marker.getPosition();
                mInfoWindow = new InfoWindow(view, ll, -47);
                mBaiduMap.showInfoWindow(mInfoWindow);
                return true;
            }
        });


    }


    public class ViewHolder {

        public final View mView;
        @Bind(R.id.tv_address)
        TextView tvAddress;
        @Bind(R.id.tv_distance)
        TextView tvDistance;
        @Bind(R.id.tv_detail_address)
        TextView tvDetailAddress;
        @Bind(R.id.tv_price_per)
        TextView tvPricePer;
        @Bind(R.id.tv_num)
        TextView tvNum;
        @Bind(R.id.tv_nav)
        TextView tvNav;
        @Bind(R.id.tv_fav)
        TextView tvFav;
        public Point mItem;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
            mView = view;
        }
    }

    public void startLocation() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            if (locationClient != null)
                locationClient.start();
            else {
                locationClient = new LocationClient(getActivity().getApplicationContext());
                locationClient.registerLocationListener(myListener);
                locationClient.start();
            }
        }

    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();

        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
                MyLocationConfiguration.LocationMode.NORMAL, true, null));

        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("WGS84");
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
                isFirstLoc = true;
                if (locationClient != null)
                    locationClient.start();
                break;
            case R.id.et_address:
                SelectAddressActivity.start(getActivity());
                break;
            case R.id.tv_charge_now:
                ask();
                break;
            case R.id.tv_charge_late:
                break;
            case R.id.tv_scan_charge:
                QrCodeActivity.start(getActivity());
                break;
        }
    }

    private void ask() {
        String phoneNumber = null;
        if (!Tool.isMobileNO(etPhone.getText().toString().trim())) {

            Toast.makeText(getActivity(), "请输入正确的电话号码", Toast.LENGTH_LONG).show();
            return;
        }
        phoneNumber = etPhone.getText().toString().trim();
        String carNumber = null;
        if (!Tool.isCarnumberNO(etPlate.getText().toString().trim())) {
            Toast.makeText(getActivity(), "请输入车牌号", Toast.LENGTH_SHORT).show();
            return;
        }
        String address = null;
        if (TextUtils.isEmpty(etAddress.getText().toString().trim())) {
            Toast.makeText(getActivity(), "地址不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        address = etAddress.getText().toString().trim();
        carNumber = etPlate.getText().toString().trim();
        mManager.getAskCharge(mHelper, phoneNumber, "Hl", address, carNumber, new ManagerCallback() {
            @Override
            public void onSuccess(Object returnContent) {
                super.onSuccess(returnContent);
            }

            @Override
            public void onFailure(String msg) {
                super.onFailure(msg);
            }
        });
    }

    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mapView == null) {
                return;
            }
            final double longitude = location.getLongitude();
            final double latitude = location.getLatitude();
            SharedPreferencesUtil.putValue(SPConstant.SP_USERINFO,SPConstant.KEY_USERINFO_LAT,latitude+"");
            SharedPreferencesUtil.putValue(SPConstant.SP_USERINFO,SPConstant.KEY_USERINFO_LON,longitude+"");
            //获取定位结果
            StringBuffer sb = new StringBuffer(256);

            sb.append(location.getLatitude() + "\n");    //获取纬度信息
            sb.append(location.getLongitude());    //获取纬度信息

            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(15.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }

            if (location.getLocType() == BDLocation.TypeGpsLocation) {
                sb.append(location.getAddrStr());    //获取地址信息
                mHelper.updateCity(location.getCity());
                mHelper.setLocation(location.getLongitude(), location.getLatitude());
                initPoint();

                new CityStationListManager().getCityStations(location.getCity(), new ManagerCallback() {
                    @Override
                    public void onSuccess(Object returnContent) {
                        super.onSuccess(returnContent);
                        String list = (String) returnContent;
                        List<Point> stationBeanList = JSON.parseArray(list, Point.class);
                        EventBus.getDefault().post(new EventManager.getCityStationList(stationBeanList));
                        LogUtil.i(returnContent.toString());
                    }

                    @Override
                    public void onFailure(String msg) {
                        super.onFailure(msg);
                    }
                });
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                // 网络定位结果
                sb.append(location.getAddrStr());    //获取地址信息
                mHelper.updateCity(location.getCity());
                mHelper.setLocation(location.getLongitude(), location.getLatitude());
                initPoint();
                new CityStationListManager().getCityStations(location.getCity(), new ManagerCallback() {
                    @Override
                    public void onSuccess(Object returnContent) {
                        super.onSuccess(returnContent);
                        String list = (String) returnContent;
                        List<Point> stationBeanList = JSON.parseArray(list, Point.class);
                        EventBus.getDefault().post(new EventManager.getCityStationList(stationBeanList));
                        LogUtil.i(returnContent.toString());
                    }

                    @Override
                    public void onFailure(String msg) {
                        super.onFailure(msg);
                    }
                });
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
                if (mPiont != null && mPiont.equals(returnContent))
                    return;
                Log.d("TAG", "sss");
                mPiont = (List<Point>) returnContent;
//                for (int i = 0; i < mPiont.size(); i++) {
//                    marker(new LatLng(mPiont.get(i).Lat, mPiont.get(i).Lng), R.drawable.che);
//                }
                marker(mPiont, R.drawable.che);
                EventBus.getDefault().post(new OnPushDataEvent(mPiont));
            }

            @Override
            public void onFailure(String msg) {
                super.onFailure(msg);
            }
        });
    }

    private void marker(List<Point> mPiont, int pic) {
        //清空地图
        mBaiduMap.clear();
        //创建marker的显示图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(pic);
        LatLng latLng = null;
        Marker marker;
        OverlayOptions options;
        for (Point info : mPiont) {
            //获取经纬度
            latLng = new LatLng(Double.parseDouble(info.Lat), Double.parseDouble(info.Lng));
            //设置marker
            options = new MarkerOptions()
                    .position(latLng)//设置位置
                    .icon(bitmap)//设置图标样式
                    .zIndex(9) // 设置marker所在层级
                    .draggable(true); // 设置手势拖拽;
            //添加marker
            marker = (Marker) mBaiduMap.addOverlay(options);
            //使用marker携带info信息，当点击事件的时候可以通过marker获得info信息
            Bundle bundle = new Bundle();
            //info必须实现序列化接口
            bundle.putSerializable("info", info);
            marker.setExtraInfo(bundle);
        }
        //将地图显示在最后一个marker的位置
//
//        BitmapDescriptor bitmap = BitmapDescriptorFactory
//                .fromResource(pic);
//        //构建MarkerOption，用于在地图上添加Marker
//        OverlayOptions option = new MarkerOptions()
//                .position(point)
//                .icon(bitmap);
//        //在地图上添加Marker，并显示
//        mapView.getMap().addOverlay(option);
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
                SuggestionInfo info = (SuggestionInfo) bundle.getSerializable(SelectAddressActivity.KEY_FOR_RESULT);
                etAddress.setText(info.key);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mapView != null)
            mapView.onResume();
        startLocation();
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
