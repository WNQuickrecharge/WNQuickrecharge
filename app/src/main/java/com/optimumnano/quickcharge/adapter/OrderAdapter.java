package com.optimumnano.quickcharge.adapter;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.bean.OrderBean;

import org.xutils.common.util.LogUtil;

import java.util.List;

/**
 * Created by ds on 2017/3/29.
 * 订单列表适配器
 */
public class OrderAdapter extends BaseQuickAdapter<OrderBean,BaseViewHolder> {
    public OrderAdapter(int layoutResId, List<OrderBean> data) {
        super(layoutResId, data);
    }
    private Activity activity;
    public void setContext(Activity activity){
        this.activity = activity;
    }

    @Override
    protected void convert(BaseViewHolder helper, final OrderBean item) {
        TextView tvStatus = helper.getView(R.id.order_status);
        switch (item.order_status){
            case 1:
                helper.setText(R.id.order_status,"已取消");
                helper.setVisible(R.id.order_tvPay,false);
                helper.setVisible(R.id.order_tvComment,false);
                break;
            case 2:
                helper.setText(R.id.order_status,"待支付");
                helper.setVisible(R.id.order_tvPay,true);
                helper.setVisible(R.id.order_tvComment,false);
                helper.setText(R.id.order_tvPay,"支付订单");
                break;
            case 3:
                helper.setText(R.id.order_status,"待充电");
                helper.setVisible(R.id.order_tvPay,true);
                helper.setVisible(R.id.order_tvComment,false);
                helper.setText(R.id.order_tvPay,"查看状态");
                break;
            case 4:
                helper.setText(R.id.order_status,"充电中");
                helper.setVisible(R.id.order_tvPay,true);
                helper.setVisible(R.id.order_tvComment,false);
                helper.setText(R.id.order_tvPay,"查看状态");
                break;
            case 5:
                helper.setText(R.id.order_status,"已完成");
                helper.setVisible(R.id.order_tvPay,true);
                helper.setVisible(R.id.order_tvComment,false);
                helper.setText(R.id.order_tvPay,"评价订单");
                break;
            case 6:
                helper.setText(R.id.order_status,"已完成");
                helper.setVisible(R.id.order_tvPay,false);
                helper.setVisible(R.id.order_tvComment,true);

                break;
        }
        helper.setText(R.id.order_tvNo,item.order_no);
        helper.setText(R.id.order_tvMoney,"￥"+item.frozen_cash);
        helper.setOnClickListener(R.id.order_tvPay, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (item.order_status){
                    case 2:
//                        helper.setText(R.id.order_status,"待支付");
//                        helper.setText(R.id.order_tvPay,"支付订单");
                        LogUtil.d("待支付");
                        break;
                    case 3:
//                        helper.setText(R.id.order_status,"待充电");
//                        helper.setText(R.id.order_tvPay,"查看状态");
                        LogUtil.d("待充电");
                        break;
                    case 4:
//                        helper.setText(R.id.order_status,"充电中");
//                        helper.setText(R.id.order_tvPay,"查看状态");
                        LogUtil.d("充电中");
                        break;
                    case 5:
//                        helper.setText(R.id.order_status,"已完成");
//                        helper.setText(R.id.order_tvPay,"评价订单");
                        LogUtil.d("已完成");
                        break;
                }
            }
        });
    }
}
