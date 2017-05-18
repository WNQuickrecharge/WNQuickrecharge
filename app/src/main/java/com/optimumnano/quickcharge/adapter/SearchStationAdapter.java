package com.optimumnano.quickcharge.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.bean.Point;

import java.util.List;

/**
 * 作者：邓传亮 on 2017/5/17 19:43
 * <p>
 * 邮箱：dengchuanliang@optimumchina.com
 */
public class SearchStationAdapter extends BaseQuickAdapter<Point, BaseViewHolder> {
    private OnListItemClickListener mListener;
    public SearchStationAdapter(List<Point> data, OnListItemClickListener listener) {
        super(R.layout.adpater_address, data);
        mListener = listener;
    }

    @Override
    public void convert(final BaseViewHolder holder, final Point item) {
        holder.getView(R.id.ll_address_root).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClickListener(item,holder.getAdapterPosition());
            }
        });
        holder.setText(R.id.tv_poi_name,item.StationName );
        holder.setText(R.id.tv_poi_detail,item.Address);
    }


}
