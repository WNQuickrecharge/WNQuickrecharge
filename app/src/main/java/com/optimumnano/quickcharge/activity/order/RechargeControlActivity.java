package com.optimumnano.quickcharge.activity.order;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.optimumnano.quickcharge.Constants;
import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.base.BaseActivity;
import com.optimumnano.quickcharge.dialog.WaitRechargeDialog;
import com.optimumnano.quickcharge.manager.OrderManager;
import com.optimumnano.quickcharge.net.ManagerCallback;
import com.optimumnano.quickcharge.views.WaveLoadingView;

import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 充电控制
 */
public class RechargeControlActivity extends BaseActivity implements View.OnClickListener {

    private WaveLoadingView waveLoadingView;
    private TextView tvPersent,tvStart,tvStop,tvDescone,tvDescTwo,tvTime;
    static ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
    private WaitRechargeDialog dialog;

    private int persent = 0;
    private OrderManager orderManager = new OrderManager();
//    private boolean canStartcharege = false;//是否可以点击开始充电

    private String orderNo;
    private ShortMessageCountDownTimer smcCountDownTimer;
    private RequestCallback callback = new RequestCallback();

    public static final int GETCONNECT = 0;
    public static final int GETCHARGEPROGRESS = 1;
    public static final int STARTCHARGE = 2;
    public static final int STOPCHARGE = 3;
    private int progress = 1;

    private int orderStatus;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge_control);
        getExtras();
        initViews();
        initListener();
        initData();
    }
    private void getExtras(){
        orderNo = getIntent().getExtras().getString("order_no");
//        gunNo = getIntent().getExtras().getString("gun_no");
        orderStatus = getIntent().getExtras().getInt("order_status");
    }
    private void initListener() {
        tvStart.setOnClickListener(this);
        tvStop.setOnClickListener(this);
    }
    private void initData(){
//        startCountTime(1000*1000,2000);
    }

    @Override
    public void initViews() {
        super.initViews();
        setTitle("充电控制");
        setRightTitle("使用帮助");
        waveLoadingView = (WaveLoadingView) findViewById(R.id.waveLoadingView);
        tvPersent = (TextView) findViewById(R.id.rechargecon_tvPersent);
        tvDescone = (TextView) findViewById(R.id.rechargecon_tvDescone);
        tvDescTwo = (TextView) findViewById(R.id.rechargecon_tvDescTwo);
        tvStart = (TextView) findViewById(R.id.rechargecon_tvStart);
        tvStop = (TextView) findViewById(R.id.rechargecon_tvStop);
        tvTime = (TextView) findViewById(R.id.rechargecon_tvTime);

        if (orderStatus == Constants.GETCHARGEPROGRESS){
            startCountTime(1000*1000,10*1000);
        }

        dialog = new WaitRechargeDialog(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rechargecon_tvStop:
                orderManager.stopCharge(orderNo,callback,STOPCHARGE);
                break;
            case R.id.rechargecon_tvStart:
                startRecharge();
                break;
            default:
                break;
        }
    }

    private void startRecharge() {
//        if (!canStartcharege){
//            showToast("等待连接中。。。");
//            return;
//        }
        dialog.show();
        orderManager.startCharge(orderNo,callback,STARTCHARGE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void startCountTime(long allTime, long time) {
        stopCountTime();
        smcCountDownTimer = new ShortMessageCountDownTimer(allTime, time);
        smcCountDownTimer.start();
    }
    private void stopCountTime() {
        if (smcCountDownTimer != null) {
            smcCountDownTimer.cancel();
        }
    }
    class ShortMessageCountDownTimer extends CountDownTimer {
        public ShortMessageCountDownTimer(long millisInFuture,long countDownInterval) {
            super(millisInFuture, countDownInterval);
//            if (!canStartcharege){
//                orderManager.getGunConnect(gunNo,callback,GETCONNECT);
//            }
//            else{
                orderManager.getChargeProgress(orderNo,progress++,callback,GETCHARGEPROGRESS);
//            }

        }
        @Override
        public void onTick(long arg0) {
//            if (!canStartcharege){
//                orderManager.getGunConnect(gunNo,callback,GETCHARGEPROGRESS);
//            }
//            else{
                orderManager.getChargeProgress(orderNo,progress++,callback,GETCHARGEPROGRESS);
//            }
        }
        @Override
        public void onFinish() {}
    }
    class RequestCallback extends ManagerCallback<String>{
        @Override
        public void onSuccess(String returnContent, int requestCode) {
            super.onSuccess(returnContent, requestCode);
            //握手连接
//            if (requestCode == GETCONNECT){
////                canStartcharege = true;
//                stopCountTime();
//            }
//            //开始充电
//            else
            if (requestCode == STARTCHARGE){
                startCountTime(1000*1000,10*1000);
                dialog.cancelDialog();
                tvStart.setVisibility(View.GONE);
                tvStop.setVisibility(View.VISIBLE);
            }
            //结束充电
            else if (requestCode == STOPCHARGE){
                skipActivity(OrderDetlActivity.class,null);
                finish();
            }
            //充电进度查询
            else {
//                {"time_remain":60,"status":"充电中","progress":40}
                HashMap<String,Object> ha = new Gson().fromJson(returnContent,new TypeToken<HashMap<String,Object>>(){}.getType());
                persent = (int) Double.parseDouble(ha.get("progress").toString());
                tvTime.setVisibility(View.VISIBLE);
                tvTime.setText("充电预计时间"+ha.get("time_remain")+"分钟");
                tvPersent.setText(persent+"%");
                waveLoadingView.setWaveHeight(persent);
                tvDescone.setText("正在充电中");
                tvDescTwo.setText("请您稍作休息");
            }
        }
        @Override
        public void onFailure(String msg, int requestCode) {
            super.onFailure(msg, requestCode);
            if (requestCode == STARTCHARGE){
                dialog.cancelDialog();
                showToast(msg);
            }
            else {
                stopCountTime();
            }
        }
    }
}
