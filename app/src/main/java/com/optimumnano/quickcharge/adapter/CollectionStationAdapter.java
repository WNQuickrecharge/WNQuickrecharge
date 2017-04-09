package com.optimumnano.quickcharge.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.optimumnano.quickcharge.bean.StationBean;

import java.util.List;

/**
 * Created by mfwn on 2017/4/8.
 */

public class CollectionStationAdapter extends BaseQuickAdapter<StationBean,BaseViewHolder> {


    public CollectionStationAdapter(int layoutResId, List<StationBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, StationBean item) {

    }
}
