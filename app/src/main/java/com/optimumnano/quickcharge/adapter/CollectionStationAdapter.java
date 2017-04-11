package com.optimumnano.quickcharge.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.activity.StationActivity;
import com.optimumnano.quickcharge.bean.StationBean;

import org.xutils.common.util.LogUtil;

import java.util.List;

/**
 * Created by mfwn on 2017/4/8.
 */

public class CollectionStationAdapter extends BaseQuickAdapter<StationBean,BaseViewHolder> {
    private Context context;


    public CollectionStationAdapter(Context context,int layoutResId, List<StationBean> data) {
        super(layoutResId, data);
        this.context=context;
    }

    @Override
    protected void convert(BaseViewHolder helper, final StationBean item) {
        TextView electricPrice=helper.getView(R.id.item_collect_electric_price);
        TextView servicePrice=helper.getView(R.id.item_collect_station_service_price);
        TextView freeGuns=helper.getView(R.id.item_collect_free_guns);
        TextView totalGuns=helper.getView(R.id.item_collect_total_guns);
        TextView stationName=helper.getView(R.id.collect_station_name);
        LinearLayout linearLayout=helper.getView(R.id.item_collect_GPS);
        LinearLayout root=helper.getView(R.id.ll_root);
        electricPrice.setText(item.electricPrice);
        servicePrice.setText(item.servicePrice);
        freeGuns.setText(item.freeGuns);
        totalGuns.setText(item.totalGuns);
        stationName.setText(item.stationName);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, StationActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("Station",item);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

    }
}
