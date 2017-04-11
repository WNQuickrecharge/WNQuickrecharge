package com.optimumnano.quickcharge.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jaychang.st.SimpleText;
import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.bean.Point;
import com.optimumnano.quickcharge.event.OnNaviEvent;

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
        holder.mItem = mValues.get(position);
        holder.tvAddress.setText(holder.mItem.StationName);
        holder.tvDistance.setText(DoubleDP(holder.mItem.distance, "#.00"));
        holder.tvDetailAddress.setText(holder.mItem.Address);
        holder.tvPricePer.setText("电费");
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
        @Bind(R.id.tv_num)
        TextView tvNum;
        @Bind(R.id.tv_nav)
        TextView tvNav;
        @Bind(R.id.tv_fav)
        TextView tvFav;
        public Point mItem;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            mView = view;
        }
    }
}
