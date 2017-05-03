package com.optimumnano.quickcharge.adapter.city;


import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.adapter.OnListItemClickListener;

import java.util.List;

/**
 * 作者：邓传亮 on 2017/4/26 20:41
 * <p>
 * 邮箱：dengchuanliang@optimumchina.com
 */
public class hHotCityAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    private final OnListItemClickListener mListener;

    public hHotCityAdapter(int layoutResId, List data, OnListItemClickListener listener) {
        super(layoutResId, data);
        mListener = listener;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final String item) {
        helper.setText(R.id.tv_hotcity_name,item);
        helper.getView(R.id.tv_hotcity_name).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClickListener(item,helper.getAdapterPosition());
            }
        });
    }


}
