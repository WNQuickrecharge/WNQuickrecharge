package com.optimumnano.quickcharge.adapter;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.bean.OrderBean;

import java.util.List;

/**
 * Created by ds on 2017/3/29.
 */
public class OrderAdapter extends BaseQuickAdapter<OrderBean,BaseViewHolder> {
    public OrderAdapter(int layoutResId, List<OrderBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderBean item) {
        TextView tvStatus = helper.getView(R.id.order_status);
        switch (item.status){
            case 0:
                helper.setText(R.id.order_status,"待支付");
                helper.setVisible(R.id.order_tvPay,true);
                helper.setVisible(R.id.order_tvComment,false);
                helper.setText(R.id.order_tvPay,"支付订单");
                break;
            case 1:
                helper.setText(R.id.order_status,"充电中");
                helper.setVisible(R.id.order_tvPay,true);
                helper.setVisible(R.id.order_tvComment,false);
                helper.setText(R.id.order_tvPay,"查看状态");
                break;
            case 2:
                helper.setText(R.id.order_status,"已完成");
                helper.setVisible(R.id.order_tvPay,true);
                helper.setVisible(R.id.order_tvComment,false);
                helper.setText(R.id.order_tvPay,"评价订单");
                break;
            case 3:
                helper.setText(R.id.order_status,"已完成");
                helper.setVisible(R.id.order_tvPay,false);
                helper.setVisible(R.id.order_tvComment,true);

                break;
        }
    }
}
