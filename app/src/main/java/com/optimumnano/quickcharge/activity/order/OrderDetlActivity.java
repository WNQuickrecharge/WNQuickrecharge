package com.optimumnano.quickcharge.activity.order;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.activity.MainActivity;
import com.optimumnano.quickcharge.base.BaseActivity;
import com.optimumnano.quickcharge.bean.OrderBean;
import com.optimumnano.quickcharge.dialog.CommentDialog;
import com.optimumnano.quickcharge.manager.EventManager;
import com.optimumnano.quickcharge.manager.OrderManager;
import com.optimumnano.quickcharge.net.ManagerCallback;
import com.optimumnano.quickcharge.views.MenuItem1;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;

import butterknife.Bind;
import butterknife.ButterKnife;

public class OrderDetlActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.orderdetl_tvConfirm)
    TextView tvConfirm ;
    @Bind(R.id.orderdetl_tvComment)
    TextView tvComment;
    @Bind(R.id.orderdetl_tvOrderNum)
    TextView tvOrderNum;
    @Bind(R.id.orderdetl_miUseTime)
    MenuItem1 miUseTime;
    @Bind(R.id.orderdetl_miAllelec)
    MenuItem1 miAllelec;
    @Bind(R.id.orderdetl_miUseMoney)
    MenuItem1 miUseMoney;
    @Bind(R.id.orderdetl_tvServiceCash)
    TextView tvServiceMoney;
    @Bind(R.id.orderdetl_miAllMoney)
    MenuItem1 miAllMoney;
    @Bind(R.id.orderdetl_miYfMoney)
    MenuItem1 miYFMoney;
    @Bind(R.id.orderdetl_miBackMoney)
    MenuItem1 miBackMoney;

    private OrderBean orderBean;

    private CommentDialog dialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detl);
        ButterKnife.bind(this);
        initViews();
        String order_no = getIntent().getExtras().getString("order_no");
        getOrderInfo(order_no);

    }

    private void getOrderInfo(String order_no) {
        OrderManager.getOrderByOrderNo(order_no, new ManagerCallback() {
            @Override
            public void onSuccess(Object returnContent) {
                super.onSuccess(returnContent);
                String s = returnContent.toString();
                orderBean = JSON.parseObject(s, OrderBean.class);
                initData();
            }

            @Override
            public void onFailure(String msg) {
                super.onFailure(msg);
                showToast(msg);
                finish();
            }
        });
    }

    @Override
    public void initViews() {
        super.initViews();
        setTitle("充电结算详情");

        dialog = new CommentDialog(this);
        tvConfirm.setOnClickListener(this);
        tvComment.setOnClickListener(this);
        dialog.setConfirmListener(this);
    }

    private void initData(){
        tvOrderNum.setText(orderBean.order_no);
        miUseTime.setRightText(orderBean.power_time+"分钟");
        miAllelec.setRightText(orderBean.charge_vol+"kwh");
        miUseMoney.setRightText("￥"+orderBean.charge_cash);
//        tvServiceMoney.setText();
        miAllMoney.setRightText("￥"+orderBean.charge_cash);//暂时没加服务费
        miYFMoney.setRightText("￥"+orderBean.frozen_cash);
        double backMoney = orderBean.frozen_cash - orderBean.charge_cash;
        DecimalFormat df = new DecimalFormat("0.00");
        String formatMoney = df.format(backMoney);
        miBackMoney.setRightText("￥"+formatMoney);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.orderdetl_tvComment:
                dialog.show();
                break;
            case R.id.orderdetl_tvConfirm:
                Intent intent=new Intent(OrderDetlActivity.this, MainActivity.class);
                OrderDetlActivity.this.startActivity(intent);
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        SystemClock.sleep(100);
                        EventBus.getDefault().post(new EventManager.mainActivitySelectOrderTag());
                    }
                }.start();

                break;
            case  R.id.dialog_comment_tvComment:
                showToast(dialog.getComment()+dialog.getRatingCorn());
                dialog.close();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
