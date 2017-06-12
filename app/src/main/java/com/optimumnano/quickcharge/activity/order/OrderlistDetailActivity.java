package com.optimumnano.quickcharge.activity.order;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.optimumnano.quickcharge.Constants;
import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.base.BaseActivity;
import com.optimumnano.quickcharge.bean.OrderBean;
import com.optimumnano.quickcharge.dialog.MyDialog;
import com.optimumnano.quickcharge.dialog.PayDialog;
import com.optimumnano.quickcharge.http.BaseResult;
import com.optimumnano.quickcharge.http.HttpCallback;
import com.optimumnano.quickcharge.http.HttpTask;
import com.optimumnano.quickcharge.http.TaskIdGenFactory;
import com.optimumnano.quickcharge.manager.EventManager;
import com.optimumnano.quickcharge.request.CancelOrderRequest;
import com.optimumnano.quickcharge.request.DeleteOrderRequest;
import com.optimumnano.quickcharge.response.CancelOrderResult;
import com.optimumnano.quickcharge.response.DeleteOrderResult;
import com.optimumnano.quickcharge.utils.SPConstant;
import com.optimumnano.quickcharge.utils.SharedPreferencesUtil;
import com.optimumnano.quickcharge.utils.ToastUtil;
import com.optimumnano.quickcharge.utils.Tool;
import com.optimumnano.quickcharge.views.MenuItem1;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;

import static com.optimumnano.quickcharge.R.id.iv_order_icon;

/**
 * 订单详情页面，包含下面四种情况
 * 已取消=1,
 * 待支付=2,
 * 待充电=3,
 * 充电中=4,
 */
public class OrderlistDetailActivity extends BaseActivity implements View.OnClickListener, PayDialog.PayCallback, HttpCallback {
    private TextView tvPay, tvCancel, tvWatchStatus;
    private TextView tvStatus, tvOrdernum, tvCompany, tvAddress, tvDate;
    private OrderBean orderBean;
    private MenuItem1 miGunNum, miPileType, miElec, miPower, miForzenCatsh;

    private MyDialog myDialog;
    private PayDialog payDialog;
    private ImageView orderIcon;

    private int mCancelOrderTaskId;
    private int mDeleteOrderTaskId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderlist_detail);
        getExtras();
        initViews();
        initListener();
        initData();
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

        myDialog = new MyDialog(this, R.style.MyDialog);
        myDialog.setCancelable(false);
    }

    private void initListener() {
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
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
            tvPay.setText("删除订单");
            tvPay.setTextColor(getResources().getColor(R.color.red));
            tvPay.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.shape_delete_order));
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
        tvCompany.setText(orderBean.station_name);
        tvAddress.setText(orderBean.station_addr);
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
                    deleteOrderClick();
                } else {
                    int paway = SharedPreferencesUtil.getValue(SPConstant.SP_USERINFO, SPConstant.KEY_USERINFO_DEFPAYWAY, PayDialog.pay_yue);
                    payDialog.setPayway(paway);
                    payDialog.setMoney(orderBean.frozen_cash, orderBean.order_no);
                    if (PayDialog.pay_yue == paway) {
                        payDialog.setStatus(PayDialog.EDTPWD);
                    } else {
                        payDialog.setStatus(PayDialog.PAYBT);
                    }
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

    private void deleteOrderClick() {
        myDialog.setTitle("确认是否删除");
        myDialog.setMessage("订单一旦删除将不可恢复，\n请确认是否删除。");
        myDialog.setYesOnclickListener("确认", new MyDialog.onYesOnclickListener() {
            @Override
            public void onYesClick() {
                deleteOrder(orderBean.order_no);
                myDialog.dismiss();
            }
        });
        myDialog.setNoOnclickListener("取消", new MyDialog.onNoOnclickListener() {
            @Override
            public void onNoClick() {
                myDialog.dismiss();
            }
        });

        myDialog.show();
    }

    /**
     * 删除订单
     *
     * @param str
     */
    private void deleteOrder(String str) {
        if (!Tool.isConnectingToInternet()) {
            ToastUtil.showToast(mContext, "无网络");
            return;
        }
        mDeleteOrderTaskId = TaskIdGenFactory.gen();
        mTaskDispatcher.dispatch(new HttpTask(mDeleteOrderTaskId,
                new DeleteOrderRequest(new DeleteOrderResult(mContext), str), this));
    }

    private void cancelOrder() {
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

    @Override
    public void onRequestSuccess(int id, BaseResult result) {
        if (isFinishing()) {
            return;
        }
        if (mCancelOrderTaskId == id) {
            showToast("取消成功");
            finish();
        }
        if (mDeleteOrderTaskId == id) {
            ToastUtil.showToast(mContext, "删除成功");
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTaskDispatcher.cancel(mDeleteOrderTaskId);
        mTaskDispatcher.cancel(mCancelOrderTaskId);
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void weiXinPayCallback(EventManager.WeiXinPayCallback event) {
        int code = event.code;
        if (0 == code) {
            finish();
        } else {
            //微信支付失败
        }
        logtesti("orderdetail weixinpay callback " + event.code);
    }
}
