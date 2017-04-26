package com.optimumnano.quickcharge.activity.filter;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.adapter.city.CityResultAdapter;
import com.optimumnano.quickcharge.adapter.city.CityShowAdapter;
import com.optimumnano.quickcharge.adapter.city.CurrentCityState;
import com.optimumnano.quickcharge.adapter.city.OnCityClickListener;
import com.optimumnano.quickcharge.adapter.city.OnLocateClickListener;
import com.optimumnano.quickcharge.base.BaseActivity;
import com.optimumnano.quickcharge.bean.CityModel;
import com.optimumnano.quickcharge.manager.EventManager;
import com.optimumnano.quickcharge.utils.AppManager;
import com.optimumnano.quickcharge.utils.PinyinUtils;
import com.optimumnano.quickcharge.utils.SPConstant;
import com.optimumnano.quickcharge.utils.SharedPreferencesUtil;
import com.optimumnano.quickcharge.utils.Tool;
import com.optimumnano.quickcharge.views.SideLetterBar;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 作者：邓传亮 on 2017/4/26 10:33
 * <p>
 * 邮箱：dengchuanliang@optimumchina.com
 */

public class ChoseCityActivity extends BaseActivity {

    @Bind(R.id.et_search)
    EditText mEtSearch;
    @Bind(R.id.iv_search_clear)
    ImageView mIvSearchClear;
    @Bind(R.id.listview_all_city)
    RecyclerView mListviewAllCity;
    @Bind(R.id.tv_letter_overlay)
    TextView mTvLetterOverlay;
    @Bind(R.id.listview_search_result)
    RecyclerView mListviewSearchResult;
    @Bind(R.id.empty_view)
    LinearLayout mEmptyView;
    @Bind(R.id.side_letter_bar)
    SideLetterBar mSideLetterBar;
    private String[] citys;

    private CityShowAdapter mCityAdapter;//城市列表adapter
    private CityResultAdapter mResultAdapter;//搜索结果列表
    private List<CityModel> cityModels;//接口返回所有城市的结果集
    private List<CityModel> resultModels;//搜索结果集

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chose_city);
        ButterKnife.bind(this);
        initViews();
    }

    @Override
    public void initViews() {
        super.initViews();
        setTitle("选择城市");
        setRightTitle("");
        initialize();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }


    private void initialize() {
        citys=getResources().getStringArray(R.array.citys);
        mIvSearchClear.setVisibility(View.GONE);
        resultModels = new ArrayList<>();
        mListviewAllCity.setLayoutManager(new LinearLayoutManager(ChoseCityActivity.this));
        mListviewSearchResult.setLayoutManager(new LinearLayoutManager(ChoseCityActivity.this));
        mResultAdapter = new CityResultAdapter(ChoseCityActivity.this, resultModels);
        mListviewSearchResult.setAdapter(mResultAdapter);
        initSearch();//搜索功能
        ArrayList cityList = new ArrayList<>();
        for (int i = 0; i < citys.length; i++) {
            CityModel city=new CityModel(citys[i], PinyinUtils.getPinYin(citys[i]));
            cityList.add(city);
        }
        initRecycleCityList(cityList);

    }

    public void initSearch() {
        mResultAdapter.setOnCityClickListener(new OnCityClickListener() {
            @Override
            public void onCityClick(String cityname) {
                EventBus.getDefault().post(new EventManager.changeCity(cityname));
                SharedPreferencesUtil.putValue(SPConstant.SP_CITY,SPConstant.KEY_USERINFO_CURRENT_CITY,cityname);
                AppManager.getAppManager().finishActivity();
            }
        });
        mEtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                resultModels.clear();
                String keyword = s.toString();
                if (TextUtils.isEmpty(keyword)) {
                    mIvSearchClear.setVisibility(View.GONE);
                    mEmptyView.setVisibility(View.GONE);
                    mListviewSearchResult.setVisibility(View.GONE);
                    mListviewAllCity.setVisibility(View.VISIBLE);
                } else {
                    mIvSearchClear.setVisibility(View.VISIBLE);
                    mListviewSearchResult.setVisibility(View.VISIBLE);
                    mListviewAllCity.setVisibility(View.GONE);
                    searchCity(keyword);
                    if (resultModels == null || resultModels.size() == 0) {
                        mEmptyView.setVisibility(View.VISIBLE);
                    } else {
                        mEmptyView.setVisibility(View.GONE);
                        mResultAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    void searchCity(String s) {
        for (CityModel cityModel : cityModels) {
            if (cityModel.cityName.contains(s) || cityModel.pinyin.contains(s)) {
                resultModels.add(cityModel);
            }
        }
    }


    /**
     * 初始化接口返回的城市列表
     */
    public void initRecycleCityList(List<CityModel> cityModels) {
        this.cityModels = cityModels;
        String[] stringArray = getResources().getStringArray(R.array.hotcity);

        ArrayList hotcity = new ArrayList<>();
        for (int i = 0; i < stringArray.length; i++) {
            hotcity.add(stringArray[i]);
        }
        mCityAdapter = new CityShowAdapter(ChoseCityActivity.this, cityModels,hotcity);

        mListviewAllCity.setAdapter(mCityAdapter);
        getLocation();
        mCityAdapter.setOnLocateClickListener(new OnLocateClickListener() {
            @Override
            public void onLocateClick() {
                getLocation();
            }
        });
        mCityAdapter.setOnCityClickListener(new OnCityClickListener() {
            @Override
            public void onCityClick(String cityname) {
                if (cityname == null) {
                    showToast("选择城市出错了");
                } else {
                    SharedPreferencesUtil.putValue(SPConstant.SP_CITY,SPConstant.KEY_USERINFO_CURRENT_CITY,cityname);
                    EventBus.getDefault().post(new EventManager.changeCity(cityname));
                    AppManager.getAppManager().finishActivity();
                }
            }
        });

        mSideLetterBar.setOverlay(mTvLetterOverlay);
        mSideLetterBar.setOnLetterChangedListener(new SideLetterBar.OnLetterChangedListener() {
            @Override
            public void onLetterChanged(String letter) {
                int position = mCityAdapter.getLetterPosition(letter);
                if (position == -2) {//点到定位时滑动到顶部
                    LinearLayoutManager llm = (LinearLayoutManager) mListviewAllCity.getLayoutManager();
                    llm.scrollToPositionWithOffset(0, 0);
                    llm.setStackFromEnd(true);
                    return;
                }

                if (position != -1) {
                    LinearLayoutManager llm = (LinearLayoutManager) mListviewAllCity.getLayoutManager();
                    llm.scrollToPositionWithOffset(position+1, 0);//将指定的position滑动到距离上面第0个的位置，也就是顶部。
                    llm.setStackFromEnd(true);
                }
                //position = -1 时是点到mSideLetterBar的i,u之类的不存在的拼音
            }
        });
    }

    //删除搜索功能
    @OnClick(R.id.iv_search_clear)
    public void onClick() {
        mEtSearch.setText("");
        mIvSearchClear.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.GONE);
        mListviewSearchResult.setVisibility(View.GONE);
        mListviewAllCity.setVisibility(View.VISIBLE);
    }

    public void setLocationSuccess(String city) {
        if (city != null) {
            mCityAdapter.updateLocateState(CurrentCityState.SUCCESS, city);
        }
    }

    //定位
    public void getLocation() {
        String lat = SharedPreferencesUtil.getValue(SPConstant.SP_CITY,SPConstant.KEY_USERINFO_CURRENT_LAT,"");
        String lon = SharedPreferencesUtil.getValue(SPConstant.SP_CITY,SPConstant.KEY_USERINFO_CURRENT_LON,"");

        if (!lat.equals("") && !lon.equals("")) {
            LatLng latLng = new LatLng(Double.parseDouble(lat), Double.parseDouble(lon));
            getLocationAddress(latLng);
        } else {
            showToast("获取当前位置失败");
        }
    }

    private void getLocationAddress(LatLng latLng) {
        if (!Tool.isConnectingToInternet()){
            showToast("网络连接异常");
            mCityAdapter.updateLocateState(CurrentCityState.FAILED,"");
            return;
        }

        final GeoCoder mSearch = GeoCoder.newInstance();
        mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(latLng));
        mSearch.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
            @Override
            public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
                mSearch.destroy();
            }

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    showToast("获取当前位置失败");
                    return;
                }
//                int i = result.getAddress().indexOf("省");
//                int j = result.getAddress().indexOf("市");
//                String str = result.getAddress().substring(i + 1, j + 1);
                String city = result.getAddressDetail().city;
                mCityAdapter.updateLocateState(CurrentCityState.SUCCESS, city);
                mSearch.destroy();
            }
        });
    }

}
