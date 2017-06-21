package com.optimumnano.quickcharge.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.optimumnano.quickcharge.bean.StationBean;
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
    private Context context;

    public DistDetailAcapter(List<Point> mValues, OnListClickListener mListener,Context context) {
        this.mValues = mValues;
        this.mListener = mListener;
        this.context=context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_dist_point_1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.tvAddress.setText(holder.mItem.StationName);
        String lat = SharedPreferencesUtil.getValue(SPConstant.SP_CITY, SPConstant.KEY_USERINFO_CURRENT_LAT, "");
        String lon = SharedPreferencesUtil.getValue(SPConstant.SP_CITY, SPConstant.KEY_USERINFO_CURRENT_LON, "");
        double distance = DistanceUtil.getDistance(new LatLng(Double.parseDouble(lat), Double.parseDouble(lon)), new LatLng(holder.mItem.Lat, holder.mItem.Lng));
        holder.tvDistance.setText(DoubleDP(distance, "#.00"));
        distance/=1000;
        DecimalFormat decimalFormat=new DecimalFormat("0.00");
        String format = decimalFormat.format(distance);
        holder.mItem.distance=Double.parseDouble(format);
        holder.tvDetailAddress.setText(holder.mItem.Address);
//        String sb="电费:1.5元/度,服务费:0.5元/度";
//        SimpleText st = SimpleText.create(holder.mView.getContext(), sb)
//                .first("1.5").first("0.5").textColor(R.color.red);
//        st.linkify(holder.tvPricePer);
        if (holder.mItem.max_price==holder.mItem.min_price) {
            holder.waveLine1.setVisibility(View.GONE);
            holder.tvElectricPricMax.setVisibility(View.GONE);
            holder.tvElectricPricMin.setText(holder.mItem.min_price+"");
        }else {
            holder.waveLine1.setVisibility(View.VISIBLE);
            holder.tvElectricPricMax.setVisibility(View.VISIBLE);
            holder.tvElectricPricMin.setText(holder.mItem.min_price+"");
            holder.tvElectricPricMax.setText(holder.mItem.max_price+"");
        }
        if (holder.mItem.max_service==holder.mItem.min_service) {
            holder.waveLine2.setVisibility(View.GONE);
            holder.tvServicePricMax.setVisibility(View.GONE);
            holder.tvServicePricMin.setText(holder.mItem.min_service+"");
        }else {
            holder.waveLine2.setVisibility(View.VISIBLE);
            holder.tvServicePricMax.setVisibility(View.VISIBLE);
            holder.tvServicePricMin.setText(holder.mItem.min_service+"");
            holder.tvServicePricMax.setText(holder.mItem.max_service+"");
        }
        String ss="空闲"+holder.mItem.FreePiles+"/共"+holder.mItem.TotalPiles+"个";
        SimpleText simpleText = SimpleText.create(holder.mView.getContext(), ss)
                .first(holder.mItem.FreePiles).textColor(R.color.main_color);
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
                EventBus.getDefault().post(new EventManager.addCollectStation(holder.mItem.Id));
            }
        });
        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StationBean bean = transPointToStationBean(holder.mItem);
                Intent intent=new Intent(context, StationActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("Station",bean);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

    }

    private StationBean transPointToStationBean(Point mItem) {
        StationBean bean=new StationBean();
        bean.setCity(mItem.City);
        bean.setDistance(mItem.distance+"km");
        bean.setId(mItem.Id);
        bean.setAddress(mItem.Address);
        bean.setDel(mItem.IsDel);
        bean.setUpdateTime(mItem.UpdateTime);
        bean.setLat(mItem.Lat+"");
        bean.setLng(mItem.Lng+"");
        bean.setFreePiles(Integer.parseInt(mItem.FreePiles));
        bean.setTotalPiles(Integer.parseInt(mItem.TotalPiles));
        bean.setStationName(mItem.StationName);
        bean.setState(mItem.State);
        bean.setUpdateTime(mItem.UpdateTime);
        bean.setMax_price(mItem.max_price);
        bean.setMin_price(mItem.min_price);
        bean.setMax_service(mItem.max_service);
        bean.setMin_service(mItem.min_service);
        bean.setManagementCompany(mItem.ManagementCompany);
        bean.setRunTimeSpan(mItem.RunTimeSpan);
        return bean;
    }


    /**
     * 解决Double数据显示问题
     *
     * @param number
     * @author Administrator
     */
    public static String DoubleDP(double number, String fm) {
        String per;
//        if (number > 1000) {
            per = "km";
            number = number / 1000;
//        } else {
//            per = "m";
//        }

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

        @Bind(R.id.tv_num)
        TextView tvNum;
        @Bind(R.id.tv_nav)
        TextView tvNav;
        @Bind(R.id.tv_fav)
        TextView tvFav;
        @Bind(R.id.ll_root)
        LinearLayout root;
        @Bind(R.id.tv_electric_price_min)
        TextView tvElectricPricMin;
        @Bind(R.id.tv_electric_price_max)
        TextView tvElectricPricMax;
        @Bind(R.id.tv_service_price_min)
        TextView tvServicePricMin;
        @Bind(R.id.tv_service_price_max)
        TextView tvServicePricMax;
        @Bind(R.id.tv_wave_line_1)
        TextView waveLine1;
        @Bind(R.id.tv_wave_line_2)
        TextView waveLine2;

        public Point mItem;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            mView = view;
        }
    }
}
