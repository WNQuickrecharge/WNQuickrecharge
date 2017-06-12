package com.optimumnano.quickcharge.activity.selectAddress;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.LogoPosition;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.base.BaseActivity;
import com.optimumnano.quickcharge.bean.MapPosition;
import com.optimumnano.quickcharge.utils.ListUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 补电车地址选择页面
 * Created by zjc on 2017/6/5.
 */

public class SelectAddressNewAct extends BaseActivity implements BaiduMap.OnMapLoadedCallback,
        OnGetPoiSearchResultListener, OnGetSuggestionResultListener, BaiduMap.OnMapStatusChangeListener {
    private MapView mapView;
    private ImageView imageView;
    private EditText et_deliver_order_search;
    private ListView mSearchListView;
    private ListView mListView;
    private LinearLayout popview_ll;

    private BaiduMap mBaiduMap;
    private MyLocationListener myListener = new MyLocationListener();
    private LocationClient mLocClient;
    private MapPosition mPositionModel;
    private SearchAddressAdapter mAdapter;
    private PoiSearch mPoiSearch;
    private SuggestionSearch mSuggestionSearch = null;
    private GeoCoder mGeoSearch;
    private List<PoiInfo> mAddresssource;
    private List<PoiInfo> mSearchAddresssource = new ArrayList<>();
    private AddressAdapter adpter;
    private boolean isEtSearchEmpty = false;//用来解决poi搜索耗时，当搜索框为空后还有搜索结果页面出现的情况
    private InputMethodManager mInputMethodManager;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPoiSearch.destroy();
        mapView.onDestroy();
        mBaiduMap.setMyLocationEnabled(false);
        if (mPositionModel == null) {
            showToast("获取地址失败,请重试");
            return;
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_address_new);
        initViews();
        initMap();
        setListener();
    }

    private void setListener() {
        /**
         *点击选择地址
         */
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PoiInfo poiInfo = mAddresssource.get(position);
                Intent intent = new Intent();
                intent.putExtra("poiInfo", poiInfo);
                setResult(522, intent);
                finish();
            }
        });
        /**
         * 地址搜索监听
         */
        et_deliver_order_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                mSearchAddresssource.clear();
                if (!TextUtils.isEmpty(editable)) {
                    isEtSearchEmpty = false;
                    mSearchListView.setVisibility(View.VISIBLE);
                    mPoiSearch.searchInCity((new PoiCitySearchOption())
                            .city(mHelper.getCity())
                            .keyword(editable.toString()).pageNum(0));
                    popview_ll.setVisibility(View.GONE);
                } else {
                    isEtSearchEmpty = true;
                    mSearchListView.setVisibility(View.GONE);
                    popview_ll.setVisibility(View.VISIBLE);
                }
            }
        });
        /**
         * 搜索地址item点击事件
         */
        mSearchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PoiInfo poiInfo = mSearchAddresssource.get(position);
                setMark(poiInfo);
            }
        });

    }

    private void setMark(PoiInfo poiInfo) {
        mPositionModel = new MapPosition();
        mPositionModel.setLat(poiInfo.location.latitude);
        mPositionModel.setLon(poiInfo.location.longitude);
        mPositionModel.setAddress(poiInfo.address);
        mPositionModel.setName(poiInfo.name);
        et_deliver_order_search.setText(poiInfo.name);
        mSearchListView.setVisibility(View.GONE);
        if (popview_ll.getVisibility() != View.VISIBLE) {
            popview_ll.setVisibility(View.VISIBLE);
        } else {
            popview_ll.setVisibility(View.GONE);
        }
        hideInput(this);
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(poiInfo.location).zoom(18.0f);
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        initLocationService();
    }

    private void initLocationService() {
        if (myListener == null)
            myListener = new MyLocationListener();
        mLocClient.registerLocationListener(myListener);
        mLocClient.start();
    }

    @Override
    public void initViews() {
        super.initViews();
        setTitle("充电地址");
        mapView = (MapView) findViewById(R.id.bmapView);
        imageView = (ImageView) findViewById(R.id.centerLocationIv);
        et_deliver_order_search = (EditText) findViewById(R.id.et_deliver_order_search);
        mSearchListView = (ListView) findViewById(R.id.rv_search_recyclerView);
        mListView = (ListView) findViewById(R.id.popview_listview);
        popview_ll = (LinearLayout) findViewById(R.id.popview_ll);

        mInputMethodManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    protected void hideInput(Activity activity) {
        if (activity.getCurrentFocus() != null && activity.getCurrentFocus().getWindowToken() != null) {
            mInputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    private void initMap() {
        //地理编码
        mGeoSearch = GeoCoder.newInstance();
        // 初始化搜索模块，注册搜索事件监听
        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(this);
        // 初始化建议搜索模块，注册建议搜索事件监听
        mSuggestionSearch = SuggestionSearch.newInstance();
        mSuggestionSearch.setOnGetSuggestionResultListener(this);
        mapView.showZoomControls(false);
        mapView.setLogoPosition(LogoPosition.logoPostionRightBottom);
        mBaiduMap = mapView.getMap();
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mBaiduMap.setOnMapLoadedCallback(this);
        //监听地图变化
        mBaiduMap.setOnMapStatusChangeListener(this);
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mLocClient = new LocationClient(mContext);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start();
    }

    @Override
    public void onMapLoaded() {

    }

    @Override
    public void onMapStatusChangeStart(MapStatus mapStatus) {

    }

    @Override
    public void onMapStatusChange(MapStatus mapStatus) {

    }

    @Override
    public void onMapStatusChangeFinish(MapStatus mapStatus) {
        LatLng target = mapStatus.target;
        getLocationAddress(target);
    }

    @Override
    public void onGetPoiResult(PoiResult poiResult) {
        if (poiResult == null || poiResult.getAllPoi() == null) {
            return;
        }

        mSearchAddresssource = poiResult.getAllPoi();
        if (null == mAdapter) {
            mAdapter = new SearchAddressAdapter();
            mSearchListView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

    }

    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

    }

    @Override
    public void onGetSuggestionResult(SuggestionResult suggestionResult) {

    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mapView == null) {
                return;
            }

            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            LatLng ll = new LatLng(location.getLatitude(),
                    location.getLongitude());
            getLocationAddress(ll);
            MapStatus.Builder builder = new MapStatus.Builder();
            builder.target(ll).zoom(18.0f);
            mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            mLocClient.stop();
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }

    }

    private void getLocationAddress(LatLng latLng) {
        mGeoSearch.reverseGeoCode(new ReverseGeoCodeOption().location(latLng));
        mGeoSearch.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
            @Override
            public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
//                mGeoSearch.destroy();
            }

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    return;
                }
//                mEtLocation.setText(result.getAddress());
                if (!ListUtils.isEmpty(result.getPoiList())) {
                    mPositionModel = new MapPosition();
                    mPositionModel.setLat(result.getPoiList().get(0).location.latitude);
                    mPositionModel.setLon(result.getPoiList().get(0).location.longitude);
                    mPositionModel.setAddress(result.getPoiList().get(0).address);
                    mPositionModel.setName(result.getPoiList().get(0).name);
//                    mPointNameTv.setText(result.getPoiList().get(0).name);
                    mAddresssource = result.getPoiList();
                    if (mAddresssource != null && mAddresssource.size() > 0) {
                        if (adpter == null) {
                            adpter = new AddressAdapter();
                            mListView.setVisibility(View.VISIBLE);
                            mListView.setAdapter(adpter);
                        } else {
                            adpter.notifyDataSetChanged();
                            mListView.setSelection(0);
                        }
                    }
                } else {
                    mAddresssource.clear();
                    adpter.notifyDataSetChanged();
                    mPositionModel = null;//防止当无法获取当前位置信息返回mPositionModel对象是之前的最后一次值
                    showToast("无法获取当前位置信息");
                }
//                mGeoSearch.destroy();

            }
        });
    }

    /**
     * 下面的自动定位适配器
     */
    private class AddressAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mAddresssource.size() == 0 ? 0 : mAddresssource.size();
        }

        @Override
        public Object getItem(int position) {
            return mAddresssource.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder vh;
            if (convertView == null) {
                vh = new ViewHolder();
                convertView = convertView.inflate(mContext, R.layout.itemview_dialog_pick_address, null);
                vh.addressName = (TextView) convertView.findViewById(R.id.pick_address_name);
                vh.addressDetail = (TextView) convertView.findViewById(R.id.pick_address_address_detail);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }
            PoiInfo poiInfo = mAddresssource.get(position);
            vh.addressName.setText(poiInfo.address);
            vh.addressDetail.setText(poiInfo.name);
            return convertView;
        }
    }

    /**
     * 搜索框listview适配器
     */
    private class SearchAddressAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mSearchAddresssource.size() == 0 ? 0 : mSearchAddresssource.size();
        }

        @Override
        public Object getItem(int position) {
            return mSearchAddresssource.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder vh;
            if (convertView == null) {
                vh = new ViewHolder();
                convertView = convertView.inflate(mContext, R.layout.itemview_map_search, null);
                vh.addressName = (TextView) convertView.findViewById(R.id.tv_name);
                vh.addressDetail = (TextView) convertView.findViewById(R.id.tv_address);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }
            PoiInfo poiInfo = mSearchAddresssource.get(position);
            vh.addressName.setText(poiInfo.address);
            vh.addressDetail.setText(poiInfo.name);
            return convertView;
        }
    }

    static class ViewHolder {
        TextView addressName;
        TextView addressDetail;
    }
}
