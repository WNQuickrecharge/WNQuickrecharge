package com.optimumnano.quickcharge.activity.order;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.optimumnano.quickcharge.Constants;
import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.base.BaseActivity;
import com.optimumnano.quickcharge.bean.OrderBean;
import com.optimumnano.quickcharge.dialog.PayDialog;
import com.optimumnano.quickcharge.http.BaseResult;
import com.optimumnano.quickcharge.http.HttpCallback;
import com.optimumnano.quickcharge.http.HttpTask;
import com.optimumnano.quickcharge.http.TaskIdGenFactory;
import com.optimumnano.quickcharge.manager.OrderManager;
import com.optimumnano.quickcharge.request.CancelOrderRequest;
import com.optimumnano.quickcharge.response.CancelOrderResult;
import com.optimumnano.quickcharge.utils.SPConstant;
import com.optimumnano.quickcharge.utils.SharedPreferencesUtil;
import com.optimumnano.quickcharge.utils.ToastUtil;
import com.optimumnano.quickcharge.utils.Tool;
import com.optimumnano.quickcharge.views.MenuItem1;

import java.text.DecimalFormat;

import static com.optimumnano.quickcharge.R.id.iv_order_icon;

public class OrderlistDetailActivity extends BaseActivity implements View.OnClickListener, PayDialog.PayCallback, HttpCallback {
    private TextView tvPay, tvCancel, tvWatchStatus;
    private TextView tvStatus, tvOrdernum, tvCompany, tvAddress, tvDate;
    private OrderBean orderBean;
    private MenuItem1 miGunNum, miPileType, miElec, miPower, miForzenCatsh;

    private PayDialog payDialog;
    private OrderManager orderManager = new OrderManager();
    private ImageView orderIcon;

    private int mCancelOrderTaskId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderlist_detail);
        getExtras();
        initViews();
        initListener();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTaskDispatcher.cancel(mCancelOrderTaskId);
    }

    private void getExtras() {
        orderBean = (OrderBean) getIntent().getExtras().getSerializable("orderbean");
    }

    @Override
    public void initViews() {
        super.initViews();
        setTitle("订单详情");

        tvPay = (TextView) findViewById(R.id.orderlistDetl_tvPay);
        tvCancel = (TextView) findViewById(R.id.orderdtel_tvCancel);
        tvWatchStatus = (TextView) findViewById(R.id.orderlistDetl_tvWatchStatus);
        tvStatus = (TextView) findViewById(R.id.orderlistDetl_tvStatus);
        miGunNum = (MenuItem1) findViewById(R.id.orderlistDetl_miGunNum);
        miPileType = (MenuItem1) findViewById(R.id.orderlistDetl_miPileType);
        miElec = (MenuItem1) findViewById(R.id.orderlistDetl_miElec);
        miPower = (MenuItem1) findViewById(R.id.orderlistDetl_miPower);
        miForzenCatsh = (MenuItem1) findViewById(R.id.orderlistDetl_miFrozenCash);
        tvOrdernum = (TextView) findViewById(R.id.orderlistDetl_tvOrdernum);
        tvCompany = (TextView) findViewById(R.id.orderlistDetl_tvCompany);
        tvAddress = (TextView) findViewById(R.id.orderlistDetl_tvAddress);
        tvDate = (TextView) findViewById(R.id.orderlistDetl_tvTime);
        orderIcon = (ImageView) findViewById(iv_order_icon);
    }

    private void initListener() {
        tvPay.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
        tvWatchStatus.setOnClickListener(this);
    }

    private void initData() {
        payDialog = new PayDialog(this);
        payDialog.setMoney(orderBean.frozen_cash, orderBean.order_no);
        payDialog.setPayCallback(this);
        if (orderBean.order_status == 1) {
            tvStatus.setText("已取消");
            tvPay.setVisibility(View.VISIBLE);
            tvPay.setText("确定");
            tvCancel.setVisibility(View.GONE);
            tvWatchStatus.setVisibility(View.GONE);
        } else if (orderBean.order_status == 2) {
            tvPay.setText("支付");
            tvStatus.setText("待支付");
            tvPay.setVisibility(View.VISIBLE);
            tvCancel.setVisibility(View.VISIBLE);
            tvWatchStatus.setVisibility(View.GONE);
        } else if (orderBean.order_status == 3) {
            tvStatus.setText("待充电");
            tvPay.setVisibility(View.GONE);
            tvCancel.setVisibility(View.GONE);
            tvWatchStatus.setVisibility(View.VISIBLE);
        } else if (orderBean.order_status == 4) {
            tvStatus.setText("充电中");
            tvPay.setVisibility(View.GONE);
            tvCancel.setVisibility(View.GONE);
            tvWatchStatus.setVisibility(View.VISIBLE);
        }
        tvOrdernum.setText(orderBean.order_no);
        miGunNum.setRightText(orderBean.gun_code);
        miPileType.setRightText(orderBean.pile_type);
        miElec.setRightText(orderBean.elec_current + "A");
        miPower.setRightText(orderBean.power + "kw");
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        String formatFrozenCash = decimalFormat.format(orderBean.frozen_cash);
        miForzenCatsh.setRightText("￥" + formatFrozenCash);
        tvDate.setText(orderBean.start_time);
        switch (orderBean.charge_from) {
            case 1:
                orderIcon.setImageResource(R.mipmap.chongdianzhuang);
                break;
            case 2:
                orderIcon.setImageResource(R.mipmap.budianche);
                break;

            default:

                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.orderlistDetl_tvPay:
                if (orderBean.order_status == 1) {
                    finish();
                } else {
                    payDialog.setPayway(SharedPreferencesUtil.getValue(SPConstant.SP_USERINFO, SPConstant.KEY_USERINFO_DEFPAYWAY, PayDialog.pay_yue));
                    payDialog.show();
                }
                break;
            case R.id.orderdtel_tvCancel:
                cancelOrder();
                break;
            case R.id.orderlistDetl_tvWatchStatus:
                Bundle bundle = new Bundle();
                if (orderBean.order_status == 3) {//待充电
                    bundle.putInt("order_status", Constants.STARTCHARGE);
                }
                if (orderBean.order_status == 4) {//充电中
                    bundle.putInt("order_status", Constants.GETCHARGEPROGRESS);
                }
                bundle.putString("order_no", orderBean.order_no);
                skipActivity(RechargeControlActivity.class, bundle);
                break;
            default:
                break;
        }
    }

    private void cancelOrder() {
//        orderManager.cancelOrder(orderBean.order_no, new ManagerCallback<String>() {
//            @Override
//            public void onSuccess(String returnContent) {
//                super.onSuccess(returnContent);
//                showToast("取消成功");
//                finish();
//            }
//
//            @Override
//            public void onFailure(String msg) {
//                super.onFailure(msg);
//            }
//        });
        if (!Tool.isConnectingToInternet()) {
            showToast("无网络");
            return;
        }
        mCancelOrderTaskId = TaskIdGenFactory.gen();
        mTaskDispatcher.dispatch(new HttpTask(mCancelOrderTaskId,
                new CancelOrderRequest(new CancelOrderResult(mContext), orderBean.order_no), this));
    }


    @Override
    public void paySuccess(String order_no) {
        Bundle bundle = new Bundle();
        bundle.putString("order_no", orderBean.order_no);
        bundle.putInt("order_status", Constants.STARTCHARGE);
        skipActivity(RechargeControlActivity.class, bundle);
        finish();
    }

    @Override
    public void payFail(String msg) {
        showToast(msg);
    }

    //http

    @Override
    public void onRequestSuccess(int id, BaseResult result) {
        if (isFinishing()) {
            return;
        }
        if (mCancelOrderTaskId == id) {
            showToast("取消成功");
            finish();
        }
    }

    @Override
    public void onRequestFail(int id, BaseResult result) {
        if (isFinishing()) {
            return;
        }
        if (mCancelOrderTaskId == id) {
            showToast(ToastUtil.formatToastText(mContext, ((CancelOrderResult) result).getResp()));
        }
    }

    @Override
    public void onRequestCancel(int id) {

    }
}
