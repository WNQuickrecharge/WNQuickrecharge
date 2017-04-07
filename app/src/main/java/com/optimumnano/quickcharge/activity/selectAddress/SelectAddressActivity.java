package com.optimumnano.quickcharge.activity.selectAddress;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.adapter.OnListClickListener;
import com.optimumnano.quickcharge.adapter.SugAddressAdapter;
import com.optimumnano.quickcharge.base.BaseActivity;
import com.optimumnano.quickcharge.bean.SuggestionInfo;
import com.optimumnano.quickcharge.utils.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SelectAddressActivity extends BaseActivity implements  OnGetSuggestionResultListener, OnListClickListener {

    @Bind(R.id.et_record_number)
    EditText etRecordNumber;
    @Bind(R.id.rv_sug)
    RecyclerView rvSug;


    private PoiSearch mPoiSearch = null;
    private SuggestionSearch mSuggestionSearch = null;
    private List<SuggestionInfo> suggest=new ArrayList<>();

    SugAddressAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_address);
        ButterKnife.bind(this);
        initViews();
        setTitle(getString(R.string.chong_address));
        mPoiSearch = PoiSearch.newInstance();
        mSuggestionSearch = SuggestionSearch.newInstance();
        mSuggestionSearch.setOnGetSuggestionResultListener(this);
        mAdapter=new SugAddressAdapter(suggest,this);
        rvSug.setAdapter(mAdapter);
        rvSug.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST));
        rvSug.setLayoutManager(new LinearLayoutManager(this));
        etRecordNumber.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {

            }

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2,
                                      int arg3) {
                if (cs.length() <= 0) {
                    return;
                }
                String city = mHelper.getCity();
                /**
                 * 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新
                 */
                mSuggestionSearch
                        .requestSuggestion((new SuggestionSearchOption())
                                .keyword(cs.toString()).city(city));
            }
        });
    }


    @Override
    public void onGetSuggestionResult(SuggestionResult res) {
        if (res == null || res.getAllSuggestions() == null) {
            return;
        }
        suggest.clear();
        for (SuggestionResult.SuggestionInfo info : res.getAllSuggestions()) {
            if (info.key != null&&!TextUtils.isEmpty(info.city)) {
                SuggestionInfo info1=new SuggestionInfo();
                info1.key=info.key;
                info1.city=info.city;
                info1.district=info.district;
                info1.lat=info.pt.latitude;
                info1.lng=info.pt.longitude;
                suggest.add(info1);
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        mPoiSearch.destroy();
        mSuggestionSearch.destroy();
        super.onDestroy();
    }

    @Override
    public void onShowMessage(Object item) {
        Intent resultIntent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_FOR_RESULT, (SuggestionInfo)item);
        resultIntent.putExtras(bundle);
        this.setResult(RESULT_OK, resultIntent);
        this.finish();
    }
    public static String KEY_FOR_RESULT="data";
    public static int REQUEST_CODE=1001;
    public static void start(Context mContext){
        Intent intent=new Intent(mContext,SelectAddressActivity.class);
        ((BaseActivity)mContext).startActivityForResult(intent,REQUEST_CODE);
    }
}
