package com.optimumnano.quickcharge.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.bean.SuggestionInfo;
import com.optimumnano.quickcharge.utils.ToastUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 秋平 on 2017/4/6 0006.
 */

public class SugAddressAdapter extends RecyclerView.Adapter<SugAddressAdapter.ViewHolder> {


    private final List<PoiInfo> mValues;
    private final OnListClickListener mListener;
    private Context context;


    public SugAddressAdapter(List<PoiInfo> mValues, OnListClickListener mListener, Context context) {
        this.mValues = mValues;
        this.mListener = mListener;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adpater_address, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.tvPoiName.setText(holder.mItem.name);
        holder.tvPoiDetail.setText(holder.mItem.address);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.mItem.location == null) {
                    ToastUtil.showToast(context,"当前地址没有经纬度信息");
                    return;
                }
                if (mListener!=null)
                    mListener.onShowMessage(holder.mItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        @Bind(R.id.tv_poi_name)
        TextView tvPoiName;
        @Bind(R.id.tv_poi_detail)
        TextView tvPoiDetail;
        public PoiInfo mItem;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            mView = view;
        }
    }
}
