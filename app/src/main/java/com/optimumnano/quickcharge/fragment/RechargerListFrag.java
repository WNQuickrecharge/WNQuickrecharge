package com.optimumnano.quickcharge.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.activity.MainActivity;
import com.optimumnano.quickcharge.activity.StationActivity;
import com.optimumnano.quickcharge.adapter.DistDetailAcapter;
import com.optimumnano.quickcharge.adapter.OnListClickListener;
import com.optimumnano.quickcharge.adapter.RegionListAdatper;
import com.optimumnano.quickcharge.base.BaseActivity;
import com.optimumnano.quickcharge.base.BaseFragment;
import com.optimumnano.quickcharge.bean.Point;
import com.optimumnano.quickcharge.data.PreferencesHelper;
import com.optimumnano.quickcharge.manager.EventManager;
import com.optimumnano.quickcharge.manager.StationManager;
import com.optimumnano.quickcharge.net.ManagerCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by mfwn on 2017/4/21.
 */

@SuppressLint("ValidFragment")
public class RechargerListFrag extends BaseFragment{
    @Bind(R.id.rv_city_dist)
    RecyclerView rvCityDist;
    @Bind(R.id.tv_dc_in_list)
    RecyclerView rvDcInList;


    HashMap<RechargerListFrag.TypeSelect, List<Point>> mHashMap = new HashMap<>();
    private List<RechargerListFrag.TypeSelect> mLeft;
    private Set<RechargerListFrag.TypeSelect> mSet = new HashSet<>();
    private List<Point> mDatas;

    private DistDetailAcapter mAdapterDist;
    private RegionListAdatper mAdapterRegion;
    private View inflate;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        inflate = inflater.inflate(R.layout.popup_window_for_city, container, false);
        return inflate;
    }

    @SuppressLint("ValidFragment")
    public RechargerListFrag(List<Point> mDatas) {
        this.mDatas = mDatas;
    }
    public RechargerListFrag(){}
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }



    private void initData() {
        ButterKnife.bind(this,inflate);
        if (mLeft == null) {
            mLeft = new ArrayList<>();
        }
        mAdapterDist=new DistDetailAcapter(mDatas, new OnListClickListener() {
            @Override
            public void onShowMessage(Object item) {

            }
        },getActivity());
        mAdapterRegion=new RegionListAdatper(mLeft, new OnListClickListener() {
            @Override
            public void onShowMessage(Object item) {
                TypeSelect ty = (TypeSelect) item;
                mDatas.clear();
                mDatas.addAll(get());
                mAdapterDist.notifyDataSetChanged();
            }
        });
//        rvCityDist.addItemDecoration(new DividerItemDecoration(getActivity(),
//                DividerItemDecoration.VERTICAL_LIST));
        rvCityDist.setLayoutManager(new LinearLayoutManager(getActivity()));
//        rvDcInList.addItemDecoration(new DividerItemDecoration(getActivity(),
//                DividerItemDecoration.VERTICAL_LIST));
        rvDcInList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvCityDist.setAdapter(mAdapterRegion);
        rvDcInList.setAdapter(mAdapterDist);
    }

    @Override
    protected void lazyLoad() {

        StationManager.getCityStations(((BaseActivity)getActivity()).mHelper.getCity(), new ManagerCallback() {
            @Override
            public void onSuccess(Object returnContent) {
                super.onSuccess(returnContent);
                String result = returnContent.toString();
                List<Point> stationBeanList = JSON.parseArray(result, Point.class);
                setData(stationBeanList);


            }

            @Override
            public void onFailure(String msg) {
                super.onFailure(msg);
            }
        });
    }


    public class TypeSelect {
        public String dist;
        public boolean isSelect = false;

        public TypeSelect(String type) {
            dist = type;
        }

        /**
         * 重写equals 和hashCode  实现set 排序，去重
         *
         * @param obj
         * @return
         */
        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (this == obj) {
                return true;
            }
            RechargerListFrag.TypeSelect other = (RechargerListFrag.TypeSelect) obj;
            if (this.dist.equals(other.dist)) {
                return true;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return dist.hashCode();
        }
    }
    public void setData(List<Point> mDatas) {
        if (mDatas==null || mDatas.size()==0){
            if (null != this.mDatas)
                this.mDatas.clear();
            if (null != mLeft)
                mLeft.clear();
            if (null != mAdapterDist)
                mAdapterDist.notifyDataSetChanged();
            if (null != mAdapterRegion)
                mAdapterRegion.notifyDataSetChanged();
            return;
        }

//        mDatas.addAll(mDatas);
        for (int i = 0; i < mDatas.size(); i++) {
            RechargerListFrag.TypeSelect select = new RechargerListFrag.TypeSelect(mDatas.get(i).yydistrict);
            if (i == 0) {
                select.isSelect = true;
            }
            mSet.add(select);

        }
        if (mLeft != null) {
            mLeft.clear();
            mLeft.addAll(mSet);
        }
        else
            mLeft=new ArrayList<>(mSet);

        for (int i = 0; i < mLeft.size(); i++) {
            List<Point> mPoint = new ArrayList<>();
            for (int j = 0; j < mDatas.size(); j++) {
                if (mLeft.get(i).dist.equals(mDatas.get(j).yydistrict)) {
                    mPoint.add(mDatas.get(j));
                }
            }
            mHashMap.put(mLeft.get(i), mPoint);
        }
        if (this.mDatas!=null) {
            this.mDatas.clear();
            this.mDatas.addAll(get());
        }else {
            this.mDatas=new ArrayList<>();
            this.mDatas.addAll(get());
        }
        initData();

        mAdapterDist.notifyDataSetChanged();
        mAdapterRegion.notifyDataSetChanged();
    }
    public List<Point> get() {
        List<Point> mPoint = null;
        Iterator iter = mHashMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            RechargerListFrag.TypeSelect select = (RechargerListFrag.TypeSelect) entry.getKey();
            if (select.isSelect) {
                mPoint = (List<Point>) entry.getValue();
                return mPoint;
            }
        }
        return mPoint;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void openStationActivity(EventManager.openStationActivity event) {
        Intent intent=new Intent(getActivity(), StationActivity.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable("Station",event.bean);
        intent.putExtras(bundle);
        getActivity().startActivity(intent);
    }


}
