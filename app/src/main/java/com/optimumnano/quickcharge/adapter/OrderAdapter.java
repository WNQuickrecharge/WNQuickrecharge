package com.optimumnano.quickcharge.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.optimumnano.quickcharge.Constants;
import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.activity.order.RechargeControlActivity;
import com.optimumnano.quickcharge.bean.OrderBean;
import com.optimumnano.quickcharge.dialog.PayDialog;
import com.optimumnano.quickcharge.http.BaseResult;
import com.optimumnano.quickcharge.http.HttpCallback;
import com.optimumnano.quickcharge.http.HttpTask;
import com.optimumnano.quickcharge.http.TaskDispatcher;
import com.optimumnano.quickcharge.http.TaskIdGenFactory;
import com.optimumnano.quickcharge.listener.MyDeleteOrderClicListener;
import com.optimumnano.quickcharge.listener.MyOnitemClickListener;
import com.optimumnano.quickcharge.request.DeleteOrderRequest;
import com.optimumnano.quickcharge.response.DeleteOrderResult;
import com.optimumnano.quickcharge.utils.SPConstant;
import com.optimumnano.quickcharge.utils.SharedPreferencesUtil;
import com.optimumnano.quickcharge.utils.ToastUtil;
import com.optimumnano.quickcharge.utils.Tool;

import org.xutils.common.util.LogUtil;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by ds on 2017/3/29.
 * 订单列表适配器
 */
public class OrderAdapter extends BaseQuickAdapter<OrderBean, BaseViewHolder> implements PayDialog.PayCallback {
    private PayDialog payDialog;

    public OrderAdapter(int layoutResId, List<OrderBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderBean item) {

    }

    private Activity activity;

    public void setContext(Activity activity) {
        this.activity = activity;
        payDialog = new PayDialog(activity);
        payDialog.setPayCallback(this);
    }

    /**
     * 删除订单接口
     */
    private MyDeleteOrderClicListener deleteOrderClicListener;

    public void setDeleteOrderClicListener(MyDeleteOrderClicListener deleteOrderClicListener) {
        this.deleteOrderClicListener = deleteOrderClicListener;
    }
    /**
     * item点击事件
     */
    private MyOnitemClickListener onitemClickListener;

    public void setOnitemClickListener(MyOnitemClickListener onitemClickListener) {
        this.onitemClickListener = onitemClickListener;
    }

    @Override
    protected void convert(BaseViewHolder helper, final OrderBean item, final int position) {
        TextView tvStatus = helper.getView(R.id.order_status);
        ImageView imageView = helper.getView(R.id.order_iv);
        switch (item.charge_from) {// 1固定桩 2 移动补电车
            case 1://
                imageView.setImageResource(R.mipmap.chongdianzhuang);
                break;
            case 2:
                imageView.setImageResource(R.mipmap.budianche);
                break;

            default:

                break;
        }
        switch (item.order_status) {
            case 1:
                helper.setText(R.id.order_status, "已取消");
                helper.setVisible(R.id.order_tvPay, true);
                helper.setText(R.id.order_tvPay, "删除订单");
                helper.setVisible(R.id.order_tvComment, false);
                break;
            case 2:
                helper.setText(R.id.order_status, "待支付");
                helper.setVisible(R.id.order_tvPay, true);
                helper.setVisible(R.id.order_tvComment, false);
                helper.setText(R.id.order_tvPay, "支付订单");
                break;
            case 3:
                helper.setText(R.id.order_status, "待充电");
                helper.setVisible(R.id.order_tvPay, true);
                helper.setVisible(R.id.order_tvComment, false);
                helper.setText(R.id.order_tvPay, "去充电");
                break;
            case 4:
                helper.setText(R.id.order_status, "充电中");
                helper.setVisible(R.id.order_tvPay, true);
                helper.setVisible(R.id.order_tvComment, false);
                helper.setText(R.id.order_tvPay, "查看状态");
                break;
            case 5:
                helper.setText(R.id.order_status, "已完成");
                helper.setVisible(R.id.order_tvPay, true);
                helper.setVisible(R.id.order_tvComment, false);
                helper.setText(R.id.order_tvPay, "评价订单");
                break;
            case 6:
                helper.setText(R.id.order_status, "已完成");
                helper.setVisible(R.id.order_tvPay, true);
                helper.setText(R.id.order_tvPay, "删除订单");
                helper.setVisible(R.id.order_tvComment, false);//现在不显示评价字眼，还没实现评价功能

                break;
        }
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        helper.setText(R.id.order_tvDate, item.start_time);
        helper.setText(R.id.order_tvNo, "订单编号 " + item.order_no);
        helper.setText(R.id.order_tvAddress, item.station_addr);
        helper.setText(R.id.order_tvMoney, "￥" + decimalFormat.format(item.charge_cash));//由之前的预付款改为充电金额
        helper.setOnClickListener(R.id.order_tvPay, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (item.order_status) {
                    case 1:
                        deleteOrderClicListener.onDeleteItemClick(view, item.order_no);
                        break;
                    case 2:
                        int paway = SharedPreferencesUtil.getValue(SPConstant.SP_USERINFO, SPConstant.KEY_USERINFO_DEFPAYWAY, PayDialog.pay_yue);
                        payDialog.setPayway(paway);
                        payDialog.setMoney(item.frozen_cash, item.order_no);
                        if (PayDialog.pay_yue == paway) {
                            payDialog.setStatus(PayDialog.EDTPWD);
                        } else {
                            payDialog.setStatus(PayDialog.PAYBT);
                        }
                        payDialog.show();
                        LogUtil.d("待支付");
                        break;
                    case 3:
                        skipAct(item.order_no, Constants.STARTCHARGE);//2
                        LogUtil.d("去充电");
                        break;
                    case 4:
                        skipAct(item.order_no, Constants.GETCHARGEPROGRESS);//1
                        LogUtil.d("充电中");
                        break;
                    case 5:
                        LogUtil.d("已完成");
                        deleteOrderClicListener.onDeleteItemClick(view, item.order_no);
                        break;
                    case 6:
                        LogUtil.d("已完成");
                        deleteOrderClicListener.onDeleteItemClick(view, item.order_no);
                        break;
                }
            }
        });

        helper.setOnClickListener(R.id.order_llMain, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onitemClickListener.onItemClick(v, position);
            }
        });
    }

    @Override
    public void paySuccess(String order_no) {
        payDialog.close();
        skipAct(order_no, Constants.STARTCHARGE);
    }

    private void skipAct(String order_no, int status) {
        Intent intent = new Intent(activity, RechargeControlActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("order_no", order_no);
        bundle.putInt("order_status", status);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }

    @Override
    public void payFail(String msg) {

    }
}
