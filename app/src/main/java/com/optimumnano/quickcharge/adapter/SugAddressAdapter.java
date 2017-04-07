package com.optimumnano.quickcharge.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.bean.SuggestionInfo;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 秋平 on 2017/4/6 0006.
 */

public class SugAddressAdapter extends RecyclerView.Adapter<SugAddressAdapter.ViewHolder> {


    private final List<SuggestionInfo> mValues;
    private final OnListClickListener mListener;


    public SugAddressAdapter(List<SuggestionInfo> mValues, OnListClickListener mListener) {
        this.mValues = mValues;
        this.mListener = mListener;
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
        holder.tvPoiName.setText(holder.mItem.key);
        holder.tvPoiDetail.setText(holder.mItem.city + " - " + holder.mItem.district);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        public SuggestionInfo mItem;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            mView = view;
        }
    }
}
