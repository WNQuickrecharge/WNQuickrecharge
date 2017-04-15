package com.optimumnano.quickcharge.popupWindow;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.adapter.DistDetailAcapter;
import com.optimumnano.quickcharge.adapter.OnListClickListener;
import com.optimumnano.quickcharge.adapter.RegionAdatper;
import com.optimumnano.quickcharge.bean.Point;
import com.optimumnano.quickcharge.utils.DividerItemDecoration;

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
 * Created by 秋平 on 2017/4/8 0008.
 */

public class DistShowPopupWindow extends BasePopupWindowForListView {

    @Bind(R.id.rv_city_dist)
    RecyclerView rvCityDist;
    @Bind(R.id.tv_dc_in_list)
    RecyclerView rvDcInList;


    HashMap<TypeSelect, List<Point>> mHashMap = new HashMap<>();
    private List<TypeSelect> mLeft;
    private Set<TypeSelect> mSet = new HashSet<>();

    private DistDetailAcapter mAdapterDist;
    private RegionAdatper mAdapterRegion;

    public DistShowPopupWindow(View contentView, int width, int height, List mDatas) {
        super(contentView, width, height, true, mDatas);
    }

    public void setData(List<Point> mDatas) {

//        mDatas.addAll(mDatas);
        for (int i = 0; i < mDatas.size(); i++) {
            TypeSelect select = new TypeSelect(mDatas.get(i).yydistrict);
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

        this.mDatas.clear();
        this.mDatas.addAll(get());

        mAdapterDist.notifyDataSetChanged();
        mAdapterRegion.notifyDataSetChanged();
    }

    public List<Point> get() {
        List<Point> mPoint = null;
        Iterator iter = mHashMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            TypeSelect select = (TypeSelect) entry.getKey();
            if (select.isSelect) {
                mPoint = (List<Point>) entry.getValue();
                return mPoint;
            }
        }
        return mPoint;
    }


    @Override
    protected void initWidget() {
        ButterKnife.bind(this, mContentView);
        if (mLeft == null) {
            mLeft = new ArrayList<>();
        }
        mAdapterDist = new DistDetailAcapter(mDatas, new OnListClickListener() {
            @Override
            public void onShowMessage(Object item) {


            }
        });
        mAdapterRegion = new RegionAdatper(mLeft, new OnListClickListener() {
            @Override
            public void onShowMessage(Object item) {
                TypeSelect ty = (TypeSelect) item;
                mDatas.clear();
                mDatas.addAll(get());
                mAdapterDist.notifyDataSetChanged();

            }
        });
        rvCityDist.addItemDecoration(new DividerItemDecoration(mContext,
                DividerItemDecoration.VERTICAL_LIST));
        rvCityDist.setLayoutManager(new LinearLayoutManager(mContext));
        rvDcInList.addItemDecoration(new DividerItemDecoration(mContext,
                DividerItemDecoration.VERTICAL_LIST));
        rvDcInList.setLayoutManager(new LinearLayoutManager(mContext));
        rvCityDist.setAdapter(mAdapterRegion);
        rvDcInList.setAdapter(mAdapterDist);
    }

    @Override
    protected void initEvents() {

    }

    @Override
    protected void init() {

    }

    @Override
    protected void initParams(Object... params) {

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
            TypeSelect other = (TypeSelect) obj;
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

}
