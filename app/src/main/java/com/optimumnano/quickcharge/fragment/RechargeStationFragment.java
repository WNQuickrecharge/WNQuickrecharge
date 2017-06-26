package com.optimumnano.quickcharge.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
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
import com.baidu.mapapi.utils.DistanceUtil;
import com.jaychang.st.SimpleText;
import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.activity.MainActivity;
import com.optimumnano.quickcharge.activity.StationActivity;
import com.optimumnano.quickcharge.activity.qrcode.QrCodeActivity;
import com.optimumnano.quickcharge.activity.qrcode.QrCodeForCarActivity;
import com.optimumnano.quickcharge.activity.selectAddress.SelectAddressNewAct;
import com.optimumnano.quickcharge.adapter.OnListItemClickListener;
import com.optimumnano.quickcharge.adapter.SearchStationAdapter;
import com.optimumnano.quickcharge.base.BaseActivity;
import com.optimumnano.quickcharge.base.BaseFragment;
import com.optimumnano.quickcharge.bean.Point;
import com.optimumnano.quickcharge.bean.StationBean;
import com.optimumnano.quickcharge.data.PreferencesHelper;
import com.optimumnano.quickcharge.event.OnNaviEvent;
import com.optimumnano.quickcharge.http.BaseResult;
import com.optimumnano.quickcharge.http.HttpCallback;
import com.optimumnano.quickcharge.http.HttpTask;
import com.optimumnano.quickcharge.http.TaskIdGenFactory;
import com.optimumnano.quickcharge.manager.EventManager;
import com.optimumnano.quickcharge.request.CancelAskOrderRequest;
import com.optimumnano.quickcharge.request.GetCityStationRequest;
import com.optimumnano.quickcharge.request.GetMapRegionInfoRequest;
import com.optimumnano.quickcharge.response.CancelAskOrderResult;
import com.optimumnano.quickcharge.response.GetCityStationResult;
import com.optimumnano.quickcharge.response.GetMapRegionInfoResult;
import com.optimumnano.quickcharge.utils.DividerItemDecoration;
import com.optimumnano.quickcharge.utils.SPConstant;
import com.optimumnano.quickcharge.utils.SharedPreferencesUtil;
import com.optimumnano.quickcharge.utils.StringUtils;
import com.optimumnano.quickcharge.utils.ToastUtil;
import com.optimumnano.quickcharge.utils.Tool;
import com.optimumnano.quickcharge.views.BottomSheetDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.common.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by mfwn on 2017/6/26.
 */

public class RechargeStationFragment extends BaseFragment implements HttpCallback, OnListItemClickListener {
    @Bind(R.id.mapView)
    TextureMapView mapView;
    @Bind(R.id.iv_location)
    ImageView ivLocation;
    @Bind(R.id.ll_search_recharge_station_frame)
    LinearLayout searchRechargeStaionFrame;
    @Bind(R.id.iv_icon_search)
    ImageView iconSearch;
    @Bind(R.id.et_ask_car_input)
    EditText askCarInput;
    @Bind(R.id.search_station_rv)
    RecyclerView searchRv;


    private BaiduMap mBaiduMap;
    private List<Point> mStationList;
    private List<Point> mSearchResult = new ArrayList<>();
    private SearchStationAdapter mStationAdapter;

    private int mGetCityStationTaskId;
    BitmapDescriptor bitmap = null;
    private View mPopView;
    private BottomSheetDialog mBsdialog;

    private PreferencesHelper mHelper;
    public LocationClient locationClient;
    boolean isFirstLoc = true; // 是否首次定位
    private int mGetMapRegionInfoTaskId;

    private List<Point> mPiont;
    public BDLocationListener myListener = new RechargeStationFragment.MyLocationListener();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.fragment_recharge_station, container, false);
        ButterKnife.bind(this, mainView);
        mHelper = new PreferencesHelper(getActivity());
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
        return mainView;
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mTaskDispatcher.cancel(mGetMapRegionInfoTaskId);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mBaiduMap = mapView.getMap();
        //不显示"baidu" logo
        mapView.removeViewAt(1);
        // 不显示地图上比例尺
        mapView.showScaleControl(false);
        // 不显示地图缩放控件（按钮控制栏）
        mapView.showZoomControls(false);
        // 不显示指南针
        mBaiduMap.getUiSettings().setCompassEnabled(false);

        locationClient = new LocationClient(getActivity().getApplicationContext());
        locationClient.registerLocationListener(myListener);
        initLocation();
        startLocation();
        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });

        askCarInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int length = s.toString().length();
                if (length > 0) {
                    iconSearch.setVisibility(View.GONE);
                    if (mStationList != null) {
                        mSearchResult.clear();
                        for (Point point : mStationList) {
                            if (point.StationName.contains(s.toString())) {
                                mSearchResult.add(point);
                            }
                        }
                        if (mSearchResult.isEmpty()) {
                            ToastUtil.showToast(getActivity(), "未搜索到结果");
//                            Tool.hiddenSoftKeyboard(getActivity(),askCarInput);
                        }

                        mStationAdapter.setNewData(mSearchResult);
                    } else {
                        getStations();
                    }

                } else {
                    mSearchResult.clear();
                    mStationAdapter.setNewData(mSearchResult);
                    iconSearch.setVisibility(View.VISIBLE);
                }
            }
        });


        getStations();
        mStationAdapter = new SearchStationAdapter(mSearchResult, this);
        searchRv.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        searchRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        searchRv.setAdapter(mStationAdapter);

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFilterParamsChange(EventManager.onFilterParamsChange event) {
        mBaiduMap.clear();
        startLocation();
        getStations();
    }


    private void getStations() {
        if (!Tool.isConnectingToInternet()) {
            return;
        }
        mGetCityStationTaskId = TaskIdGenFactory.gen();
        String city = ((BaseActivity) getActivity()).mHelper.getCity();
        mTaskDispatcher.dispatch(new HttpTask(mGetCityStationTaskId,
                new GetCityStationRequest(new GetCityStationResult(mContext), city), this));
    }

    @Override
    public void onRequestSuccess(int id, BaseResult result) {
        if (deAlive()) {
            return;
        }
        if (mGetMapRegionInfoTaskId == id) {
            closeLoading();
            List<Point> points = ((GetMapRegionInfoResult) result).getMapRegionInfoHttpResp().getResult();
            if (mPiont != null && mPiont.equals(points)) {
                return;
            }
            mPiont = points;
            getMainActivityRadioGroupChoose();
        }  else if (mGetCityStationTaskId == id) {
            mStationList = ((GetCityStationResult) result).getResp().getResult();
        }

    }

    @Override
    public void onRequestFail(int id, BaseResult result) {

    }

    @Override
    public void onRequestCancel(int id) {

    }

    @Override
    public void onItemClickListener(Object item, int position) {
        Tool.hiddenSoftKeyboard(getActivity(), askCarInput);
        Point point = (Point) item;
        showBottomDialog(point, true);
        markStation(point);
        LatLng ll = new LatLng(point.Lat, point.Lng);
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(ll).zoom(15.0f);
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
    }

    private void showBottomDialog(Object obj, boolean needCalcDistance) {
        if (obj instanceof Point) {
            mPopView = LayoutInflater.from(getActivity()).inflate(R.layout.adapter_dist_point, null);
            //bottomDialogRoot = (LinearLayout) mPopView.findViewById(R.id.ll_bottom_dialog_root);
            mBsdialog = new BottomSheetDialog(getActivity());
            mBsdialog.setContentView(mPopView);
            mBsdialog.getWindow().findViewById(R.id.design_bottom_sheet).
                    setBackgroundResource(android.R.color.transparent);
            final RechargeStationFragment.ViewHolder holder = new RechargeStationFragment.ViewHolder(mPopView);

            final Point infoUtil = (Point) obj;
            holder.mItem = infoUtil;
            holder.tvAddress.setText(holder.mItem.StationName);
            holder.tvDetailAddress.setText(holder.mItem.Address);
            if (needCalcDistance) {
                String lat = SharedPreferencesUtil.getValue(SPConstant.SP_CITY, SPConstant.KEY_USERINFO_CURRENT_LAT, "");
                String lon = SharedPreferencesUtil.getValue(SPConstant.SP_CITY, SPConstant.KEY_USERINFO_CURRENT_LON, "");
                double distance = DistanceUtil.getDistance(new LatLng(Double.parseDouble(lat), Double.parseDouble(lon)), new LatLng(holder.mItem.Lat, holder.mItem.Lng));
                holder.mItem.distance = distance / 1000;
            }
            holder.tvDistance.setText(StringUtils.formatDouble(holder.mItem.distance) + "km");

            holder.tvPhonenum.setText(holder.mItem.Phone);
            String e = holder.mItem.min_price == holder.mItem.max_price ? holder.mItem.max_price + "" : holder.mItem.min_price + "~" + holder.mItem.max_price;
            String s = holder.mItem.min_service == holder.mItem.max_service ? holder.mItem.max_service + "" : holder.mItem.min_service + "~" + holder.mItem.max_service;
            String sb = "电费:" + e + "元/度,服务费:" + s + "元/度";
            SimpleText st = SimpleText.create(holder.mView.getContext(), sb)
                    .first(e).textColor(R.color.red).first(s).textColor(R.color.red);
            st.linkify(holder.tvPricePer);
            holder.tvPricePer.setText(st);
            String ss = "空闲" + holder.mItem.FreePiles + "/共" + holder.mItem.TotalPiles + "个";
            SimpleText simpleText = SimpleText.create(holder.mView.getContext(), ss)
                    .first(holder.mItem.FreePiles).textColor(R.color.main_color);
            simpleText.linkify(holder.tvNum);
            holder.tvNum.setText(simpleText);

            mPopView.setBackgroundResource(R.drawable.sp_map_infowindow);
            holder.gpsRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OnNaviEvent event = new OnNaviEvent();
                    event.end = infoUtil;
                    EventBus.getDefault().post(event);
                    mBsdialog.dismiss();
                }
            });
            holder.tvCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mBsdialog.dismiss();
                }
            });

            holder.collectRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(new EventManager.addCollectStation(holder.mItem.Id));
                }
            });
            holder.bottomDialogRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), StationActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Station", transPointToStationBean(infoUtil));
                    intent.putExtras(bundle);
                    startActivity(intent);
                    mBsdialog.dismiss();
                }
            });
            holder.tvPhonenum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    requestPermission(holder.mItem.Phone);
                }
            });
            mBsdialog.show();
        }
    }

    private void markStation(Point info) {
        LatLng latLng;
        OverlayOptions options;
        Marker marker;
        if (bitmap == null)
            bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.chongdianzhuang0001);
        //获取经纬度
        //latLng = gpsToBd09ll(new LatLng(info.Lat, info.Lng));//将后台的wgs84坐标转为bd09坐标
        latLng = new LatLng(info.Lat, info.Lng);//原始数据就是bd09坐标,不用转
        //设置marker
        options = new MarkerOptions()
                .position(latLng)//设置位置
                .icon(bitmap)//设置图标样式
                .zIndex(9); // 设置marker所在层级
//                        .draggable(true); // 设置手势拖拽;
        //添加marker
        marker = (Marker) mBaiduMap.addOverlay(options);
        //使用marker携带info信息，当点击事件的时候可以通过marker获得info信息
        Bundle bundle = new Bundle();
        //info必须实现序列化接口
        bundle.putSerializable("info", info);
        marker.setExtraInfo(bundle);
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
        @Bind(R.id.tv_phonenum)
        TextView tvPhonenum;
        @Bind(R.id.tv_cancel)
        TextView tvCancel;
        @Bind(R.id.ll_bottom_dialog_root)
        LinearLayout bottomDialogRoot;
        @Bind(R.id.ll_collect_root)
        LinearLayout collectRoot;
        @Bind(R.id.ll_gps_root)
        LinearLayout gpsRoot;
        public Point mItem;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
            mView = view;
        }
    }

    private StationBean transPointToStationBean(Point mItem) {
        StationBean bean = new StationBean();
        bean.setCity(mItem.City);
        bean.setDistance(StringUtils.formatDouble(mItem.distance) + "km");
        bean.setId(mItem.Id);
        bean.setAddress(mItem.Address);
        bean.setDel(mItem.IsDel);
        bean.setUpdateTime(mItem.UpdateTime);
        bean.setLat(mItem.Lat + "");
        bean.setLng(mItem.Lng + "");
        bean.setFreePiles(Integer.parseInt(mItem.FreePiles));
        bean.setTotalPiles(Integer.parseInt(mItem.TotalPiles));
        bean.setStationName(mItem.StationName);
        bean.setState(mItem.State);
        bean.setUpdateTime(mItem.UpdateTime);
        bean.setMax_price(mItem.max_price);
        bean.setMin_price(mItem.min_price);
        bean.setMax_service(mItem.max_service);
        bean.setMin_service(mItem.min_service);
        bean.setManagementCompany(mItem.ManagementCompany);
        bean.setRunTimeSpan(mItem.RunTimeSpan);
        return bean;
    }

    private void requestPermission(String servicePhone) {
        //判断Android版本是否大于23
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE);

            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE},
                        RechargeFragment.RequestPermissionType.REQUEST_CODE_ASK_CALL_PHONE);
                return;
            } else {
                callPhone(servicePhone);
            }
        } else {
            callPhone(servicePhone);
        }
    }

    private void callPhone(String servicePhone) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + servicePhone + ""));
        startActivity(intent);
    }

    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mapView == null) {
                closeLoading();
                return;
            }
            locationClient.stop();
            String city = location.getCity();
            SharedPreferencesUtil.putValue(SPConstant.SP_CITY, SPConstant.KEY_USERINFO_CURRENT_LAT, location.getLatitude() + "");
            SharedPreferencesUtil.putValue(SPConstant.SP_CITY, SPConstant.KEY_USERINFO_CURRENT_LON, location.getLongitude() + "");

            if (TextUtils.isEmpty(mHelper.getCity()))
                mHelper.updateCity(city);//只保存一次,防止修改城市时,被当前定位城市覆盖

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
//                mHelper.updateCity(location.getCity());//只保存一次,防止修改城市时,被当前定位城市覆盖
                mHelper.setLocation(location.getLongitude(), location.getLatitude());
                initPoint();
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                // 网络定位结果
                sb.append(location.getAddrStr());    //获取地址信息
//                mHelper.updateCity(location.getCity());//只保存一次,防止修改城市时,被当前定位城市覆盖
                mHelper.setLocation(location.getLongitude(), location.getLatitude());
                initPoint();
            }
            //定位失败
            else {
            }
            sb.append(location.getLocationDescribe());    //位置语义化信息
            LogUtil.d(sb.toString());

            if (location.getLocType() != BDLocation.TypeNetWorkLocation)//非网络定位结果
                closeLoading();
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }


    }

    private void closeLoading() {
        ((MainActivity) getActivity()).closeLoading();
    }

    private void initPoint() {
        mGetMapRegionInfoTaskId = TaskIdGenFactory.gen();
        mTaskDispatcher.dispatch(new HttpTask(mGetMapRegionInfoTaskId,
                new GetMapRegionInfoRequest(new GetMapRegionInfoResult(mContext), mHelper), this));


    }

    private void getMainActivityRadioGroupChoose() {
        int checkedRadioButtonId = MainActivity.getRg().getCheckedRadioButtonId();
        switch (checkedRadioButtonId) {
            case R.id.main_rbRecharge:
                markerNearStaion();
//                //searchRechargeStaionFrame.setVisibility(View.VISIBLE);
//                askCarInputFrame.setVisibility(View.GONE);
//                carComingSoon.setVisibility(View.GONE);
//                waitCar.setVisibility(View.GONE);
//                handler.removeMessages(1002);
                break;



            default:
                break;
        }
    }

    private void markerNearStaion() {
        //清空地图
        mBaiduMap.clear();
        if (mPiont != null && mPiont.size() != 0)
            loadNearRechargeStationOnToMap();

    }

    private void loadNearRechargeStationOnToMap() {
        mBaiduMap.clear();
        LatLng latLng;
        OverlayOptions options;
        Marker marker;
        for (Point info : mPiont) {
            if (bitmap == null)
                bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.chongdianzhuang0001);
            //获取经纬度
            //latLng = gpsToBd09ll(new LatLng(info.Lat, info.Lng));//将后台的wgs84坐标转为bd09坐标
            latLng = new LatLng(info.Lat, info.Lng);//原始数据就是bd09坐标,不用转
            //设置marker
            options = new MarkerOptions()
                    .position(latLng)//设置位置
                    .icon(bitmap)//设置图标样式
                    .zIndex(9); // 设置marker所在层级
//                        .draggable(true); // 设置手势拖拽;
            //添加marker
            marker = (Marker) mBaiduMap.addOverlay(options);
            //使用marker携带info信息，当点击事件的时候可以通过marker获得info信息
            Bundle bundle = new Bundle();
            //info必须实现序列化接口
            bundle.putSerializable("info", info);
            marker.setExtraInfo(bundle);
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

    public void startLocation() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            if (Tool.isConnectingToInternet()) {
                ((MainActivity) getActivity()).showLoading("加载中...");
            } else {
                ((MainActivity) getActivity()).showToast("网络连接异常");
            }
            if (locationClient != null)
                locationClient.start();
            else {
                locationClient = new LocationClient(getActivity().getApplicationContext());
                locationClient.registerLocationListener(myListener);
                locationClient.start();
            }
        }

    }

    @OnClick({R.id.iv_location})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_location:
                isFirstLoc = true;
                if (locationClient != null)
                    startLocation();
                break;



            default:
                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mapView != null)
            mapView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mapView != null)
            mapView.onResume();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mapView != null)
            mapView.onDestroy();
    }
}
