package com.optimumnano.quickcharge.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.bean.BillBean;

import java.text.DecimalFormat;
import java.util.List;


/**
 * 作者：邓传亮 on 2017/4/7 12:43
 * <p>
 * 邮箱：dengchuanliang@optimumchina.com
 */
public class WalletBillAdapter extends BaseQuickAdapter<BillBean, BaseViewHolder> {
    private OnListItemClickListener mListener;

    public WalletBillAdapter(int layoutResId, List<BillBean> data, OnListItemClickListener listener) {
        super(layoutResId, data);
        mListener = listener;
    }

    @Override
    protected void convert(final BaseViewHolder holder, final BillBean item) {
        holder.getView(R.id.item_bill_ll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClickListener(item,holder.getAdapterPosition());
            }
        });
        DecimalFormat df = new DecimalFormat("0.00");
        holder.setText(R.id.item_bill_list_tv_busamount,df.format(item.Cash));
        holder.setText(R.id.item_bill_list_tv_busdescribe,item.Title);
        holder.setText(R.id.item_bill_list_tv_buspayway,item.PayType);
        holder.setText(R.id.item_bill_list_tv_bustime,item.CreateTime);
    }
}
