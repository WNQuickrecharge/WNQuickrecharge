package com.optimumnano.quickcharge.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.jaychang.st.SimpleText;
import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.activity.StationActivity;
import com.optimumnano.quickcharge.bean.Point;
import com.optimumnano.quickcharge.event.OnNaviEvent;
import com.optimumnano.quickcharge.manager.EventManager;
import com.optimumnano.quickcharge.utils.SPConstant;
import com.optimumnano.quickcharge.utils.SharedPreferencesUtil;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 秋平 on 2017/4/8 0008.
 */

public class DistDetailAcapter extends RecyclerView.Adapter<DistDetailAcapter.ViewHolder> {


    private final List<Point> mValues;
    private final OnListClickListener mListener;

    public DistDetailAcapter(List<Point> mValues, OnListClickListener mListener) {
        this.mValues = mValues;
        this.mListener = mListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_dist_point, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        String lat = SharedPreferencesUtil.getValue(SPConstant.SP_USERINFO, SPConstant.KEY_USERINFO_LAT, "");
        String lon = SharedPreferencesUtil.getValue(SPConstant.SP_USERINFO, SPConstant.KEY_USERINFO_LON, "");

        holder.mItem = mValues.get(position);
        holder.tvAddress.setText(holder.mItem.getStationName());
        double getLat = Double.parseDouble(holder.mItem.getLat());
        double getLon = Double.parseDouble(holder.mItem.getLng());
        double distance = DistanceUtil.getDistance(new LatLng(Double.parseDouble(lat), Double.parseDouble(lon)), new LatLng(getLat, getLon));
        holder.tvDistance.setText(DoubleDP(distance, "#.00"));
        holder.tvDetailAddress.setText(holder.mItem.getAddress());
        double min_price = holder.mItem.getMin_price();
        double max_price = holder.mItem.getMax_price();
        double min_service = holder.mItem.getMin_service();
        double max_service = holder.mItem.getMax_service();

        holder.tvPricePer.setText(min_price+"~"+max_price);
        holder.tvServicePrice.setText(min_service+"~"+max_service);
        String ss="空闲"+holder.mItem.getFreePiles()+"/共"+holder.mItem.getTotalPiles()+"个";
        SimpleText simpleText = SimpleText.create(holder.mView.getContext(), ss)
                .first(holder.mItem.getFreePiles()+"").textColor(R.color.main_color);

        simpleText.linkify(holder.tvNum);
        holder.tvNum.setText(simpleText);
        holder.tvNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnNaviEvent event=new OnNaviEvent();
                event.end=holder.mItem;
                EventBus.getDefault().post(event);
            }
        });
        holder.tvFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new EventManager.openStationDetail(holder.mItem.getId()));
            }
        });
        holder.linearLayout_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new EventManager.onStartStationDetailActivity(holder.mItem));
            }
        });

    }


    /**
     * 解决Double数据显示问题
     *
     * @param number
     * @author Administrator
     */
    public static String DoubleDP(double number, String fm) {
        String per;
        if (number > 1000) {
            per = "km";
            number = number / 1000;
        } else {
            per = "m";
        }

        StringBuffer buffer = new StringBuffer();
        DecimalFormat df = new DecimalFormat(fm);
        if (number < 1.0 && number != 0) {
            buffer.append("0");
        }
        buffer.append(df.format(number));
        buffer.append(per);
        return buffer.toString();

    }


    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        @Bind(R.id.tv_address)
        TextView tvAddress;
        @Bind(R.id.tv_distance)
        TextView tvDistance;
        @Bind(R.id.tv_detail_address)
        TextView tvDetailAddress;
        @Bind(R.id.tv_price_per)
        TextView tvPricePer;
        @Bind(R.id.tv_service_price1)
        TextView tvServicePrice;
        @Bind(R.id.tv_num)
        TextView tvNum;
        @Bind(R.id.tv_nav)
        TextView tvNav;
        @Bind(R.id.tv_fav)
        TextView tvFav;
        @Bind(R.id.ll_list_item_root)
        LinearLayout linearLayout_root;
        public Point mItem;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            mView = view;
        }
    }
}
