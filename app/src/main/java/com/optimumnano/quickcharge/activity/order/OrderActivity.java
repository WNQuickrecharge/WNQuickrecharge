package com.optimumnano.quickcharge.activity.order;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.optimumnano.quickcharge.Constants;
import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.base.BaseActivity;
import com.optimumnano.quickcharge.bean.RechargeGunBean;
import com.optimumnano.quickcharge.dialog.PayDialog;
import com.optimumnano.quickcharge.dialog.PayWayDialog;
import com.optimumnano.quickcharge.manager.OrderManager;
import com.optimumnano.quickcharge.net.ManagerCallback;
import com.optimumnano.quickcharge.views.MenuItem1;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 下单界面
 */
public class OrderActivity extends BaseActivity implements View.OnClickListener, PayDialog.PayCallback {

    @Bind(R.id.order_tvConfirm)
    TextView tvConfirm;
    @Bind(R.id.order_payway)
    MenuItem1 miPayway;
    @Bind(R.id.order_miRechargeNum)
    MenuItem1 miRechargenum;
    @Bind(R.id.order_miType)
    MenuItem1 miType;
    @Bind(R.id.order_miElectric)
    MenuItem1 miElectric;
    @Bind(R.id.order_miPower)
    MenuItem1 miPower;
    @Bind(R.id.order_miSimPrice)
    MenuItem1 miSimprice;
    @Bind(R.id.order_edtMoney)
    EditText edtMoney;
    @Bind(R.id.order_tvAllkwh)
    TextView tvAllkwh;

    private PayDialog payDialog;
    private PayWayDialog payWayDialog;
    private OrderManager orderManager = new OrderManager();
    private RechargeGunBean gunBean;
    private String orderNo = "";//订单号
    private String gunNo = "67867678901234517";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        ButterKnife.bind(this);
        initViews();
        initData();
        initDialog();
    }

    @Override
    public void initViews() {
        super.initViews();
        setTitle("充电订单");

        tvConfirm.setOnClickListener(this);
        miPayway.setOnClickListener(this);
    }
    private void initData(){
        orderManager.getGunInfo(gunNo, new ManagerCallback<RechargeGunBean>() {
            @Override
            public void onSuccess(RechargeGunBean returnContent) {
                super.onSuccess(returnContent);
                gunBean = returnContent;
                loadData();
            }
            @Override
            public void onFailure(String msg) {
                super.onFailure(msg);
            }
        });
    }
    private void loadData(){
        miRechargenum.setRightText("67867678901234517");
        miType.setRightText(gunBean.pile_type);
        miElectric.setRightText(gunBean.elec_current+"A");
        miPower.setRightText(gunBean.power+"kwh");
        miSimprice.setRightText(gunBean.price+"元/kwh");
    }
    private void initDialog(){
        payDialog = new PayDialog(this);
        payDialog.setPayCallback(this);
        payWayDialog = new PayWayDialog(this);
        payDialog.setPaywayListener(this);
        payWayDialog.setViewClickListener(new PayWayDialog.PayWayDialogClick() {
            @Override
            public void onMenuClick(int payway) {
                switch (payway){
                    //微信
                    case PayDialog.pay_wx:
                        miPayway.setIvLeftDrawable(R.drawable.wx);
                        miPayway.setTvLeftText("微信");
                        break;
                    //支付寶
                    case PayDialog.pay_zfb:
                        miPayway.setIvLeftDrawable(R.drawable.zfb);
                        miPayway.setTvLeftText("支付宝");
                        break;
                    //余額
                    case PayDialog.pay_yue:
                        miPayway.setIvLeftDrawable(R.drawable.yue);
                        miPayway.setTvLeftText("余额");
                        break;
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.order_tvConfirm:
                addOrder();
                break;
            case R.id.order_payway:
                payWayDialog.show();
                break;

        }
    }
    //下单
    private void addOrder(){
        orderManager.addOrder("67867678901234517", edtMoney.getText().toString(), new ManagerCallback<String>() {
            @Override
            public void onSuccess(String returnContent) {
                super.onSuccess(returnContent);
                Gson gson = new Gson();
                HashMap<String,Object> ha = gson.fromJson(returnContent,new TypeToken<HashMap<String,Object>>(){}.getType());
                orderNo = ha.get("order_no").toString();
                payDialog.setMoney(Double.parseDouble(edtMoney.getText().toString()),orderNo);
                payDialog.setStatus(0);
                payDialog.show();
            }

            @Override
            public void onFailure(String msg) {
                super.onFailure(msg);
                showToast(msg+"");
            }
        });
    }

    @Override
    public void paySuccess(String orderNo) {
        Bundle bundle = new Bundle();
        bundle.putString("order_no",orderNo);
        bundle.putString("gun_no",gunNo);
        bundle.putInt("order_status", Constants.STARTCHARGE);
        skipActivity(RechargeControlActivity.class,bundle);
    }

    @Override
    public void payFail() {

    }
}
