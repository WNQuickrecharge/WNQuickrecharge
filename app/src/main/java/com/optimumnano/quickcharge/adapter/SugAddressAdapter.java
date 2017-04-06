package com.optimumnano.quickcharge.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.optimumnano.quickcharge.bean.SuggestionInfo;

import butterknife.ButterKnife;

/**
 * Created by 秋平 on 2017/4/6 0006.
 */

public class SugAddressAdapter extends  RecyclerView.Adapter<SugAddressAdapter.ViewHolder>{


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
    public final View mView;

    public SuggestionInfo mItem;

    public ViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
        mView = view;
    }
}
}
