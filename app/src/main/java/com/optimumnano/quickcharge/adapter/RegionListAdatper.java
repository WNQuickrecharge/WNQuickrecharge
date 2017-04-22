package com.optimumnano.quickcharge.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.fragment.RechargerListFrag;
import com.optimumnano.quickcharge.popupWindow.DistShowPopupWindow;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 秋平 on 2017/4/8 0008.
 */

public class RegionListAdatper extends RecyclerView.Adapter<RegionListAdatper.ViewHolder> {


    private final List<RechargerListFrag.TypeSelect> mValues;
    private final OnListClickListener mListener;


    public RegionListAdatper(List<RechargerListFrag.TypeSelect> mValues, OnListClickListener mListener) {
        this.mValues = mValues;
        this.mListener = mListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_region, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        holder.tvRegion.setText(holder.mItem.dist);

        if (holder.mItem.isSelect) {
            holder.tvRegion.setTextColor(Color.parseColor("#36d863"));
            holder.mView.setBackgroundColor(Color.parseColor("#eeeeee"));
        } else {
            holder.tvRegion.setTextColor(Color.parseColor("#333333"));
            holder.mView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                holder.mItem.isSelect=true;
                for (int i = 0; i < mValues.size(); i++) {
                    if (i != position) {
                        mValues.get(i).isSelect = false;
                    } else {
                        mValues.get(i).isSelect = true;
                    }
                }
                if (mListener != null) {
                    mListener.onShowMessage(holder.mItem);
                }
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        @Bind(R.id.tv_region)
        TextView tvRegion;
        public RechargerListFrag.TypeSelect mItem;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            mView = view;
        }
    }
}
