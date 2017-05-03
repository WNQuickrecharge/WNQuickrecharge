package com.optimumnano.quickcharge.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.activity.StationActivity;
import com.optimumnano.quickcharge.bean.StationBean;
import com.optimumnano.quickcharge.dialog.MyDialog;
import com.optimumnano.quickcharge.manager.EventManager;
import com.optimumnano.quickcharge.net.ManagerCallback;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by mfwn on 2017/4/8.
 */

public class CollectionStationAdapter extends BaseQuickAdapter<StationBean,BaseViewHolder> {
    private Context context;
    private OnItemLongClickListener listener;


    public CollectionStationAdapter(Context context,int layoutResId, List<StationBean> data,OnItemLongClickListener listener) {
        super(layoutResId, data);
        this.context=context;
        this.listener=listener;
    }

    @Override
    protected void convert(BaseViewHolder helper, final StationBean item) {
        TextView electricPrice=helper.getView(R.id.item_collect_electric_price);
        TextView servicePrice=helper.getView(R.id.item_collect_station_service_price);
        TextView freeGuns=helper.getView(R.id.item_collect_free_guns);
        TextView totalGuns=helper.getView(R.id.item_collect_total_guns);
        TextView stationName=helper.getView(R.id.collect_station_name);
        TextView stationAddress=helper.getView(R.id.item_collect_station_address);
        LinearLayout linearLayout=helper.getView(R.id.item_collect_GPS);
        TextView distance=helper.getView(R.id.collect_station_distance);
        LinearLayout root=helper.getView(R.id.ll_root);
//        electricPrice.setText(item.electricPrice);
//        servicePrice.setText(item.servicePrice);
        freeGuns.setText(item.getFreePiles()+"");
        totalGuns.setText(item.getTotalPiles()+"");
        stationName.setText(item.getStationName());
        stationAddress.setText(item.getAddress());
        distance.setText(item.getDistance()+"");
        String price=(item.getMin_price()==item.getMax_price())?item.getMin_price()+"":(item.getMin_price() + "~" + item.getMax_price());
        electricPrice.setText(price);
        String service = (item.getMin_service()==item.getMax_service())?item.getMin_service()+"":item.getMin_service() + "~" + item.getMax_service();
        servicePrice.setText(service);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng latLng=new LatLng(Double.parseDouble(item.getLat()),Double.parseDouble(item.getLng()));
                EventBus.getDefault().post(new EventManager.startGPS(latLng));

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
        root.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                listener.onLongClick(item);
                return true;
            }
        });

    }
    public interface OnItemLongClickListener{
        void onLongClick(StationBean item);
    }
}
