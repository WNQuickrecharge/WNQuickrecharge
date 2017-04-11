package com.optimumnano.quickcharge.activity.order;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;

import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.base.BaseActivity;
import com.optimumnano.quickcharge.dialog.WaitRechargeDialog;
import com.optimumnano.quickcharge.manager.OrderManager;
import com.optimumnano.quickcharge.net.ManagerCallback;
import com.optimumnano.quickcharge.views.WaveLoadingView;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 充电控制
 */
public class RechargeControlActivity extends BaseActivity implements View.OnClickListener {

    private WaveLoadingView waveLoadingView;
    private TextView tvPersent,tvStart,tvStop,tvDescone,tvDescTwo,tvTime;
    int persent = 0;
    static ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
    private WaitRechargeDialog dialog;

    private OrderManager orderManager = new OrderManager();
    private boolean canStartcharege = false;//是否可以点击开始充电

    private String orderNo,gunNo;
    private ShortMessageCountDownTimer smcCountDownTimer;
    private RequestCallback callback = new RequestCallback();

    public static final int GETCONNECT = 0;
    public static final int GETCHARGEPROGRESS = 1;
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
        gunNo = getIntent().getExtras().getString("gun_no");
    }
    private void initListener() {
        tvStart.setOnClickListener(this);
        tvStop.setOnClickListener(this);
    }
    private void initData(){
        startCountTime(1000*1000,2000);
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


        dialog = new WaitRechargeDialog(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rechargecon_tvStop:
//                waveLoadingView
                skipActivity(OrderDetlActivity.class,null);
                break;
            case R.id.rechargecon_tvStart:
                startRecharge();
                break;
            default:
                break;
        }
    }

    private void startRecharge() {
        if (!canStartcharege){
            showToast("等待连接中。。。");
            return;
        }
        dialog.show();
        tvStart.setVisibility(View.GONE);
        tvStop.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10000);
                    dialog.cancelDialog();
                    service.scheduleAtFixedRate(new Runnable() {
                        @Override
                        public void run() {
                            while (persent<100){
                                persent += 1;
                                waveLoadingView.setWaveHeight(persent);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        tvTime.setVisibility(View.VISIBLE);
                                        tvPersent.setText(persent+"%");
                                    }
                                });
                                try {
                                    Thread.sleep(500);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }, 0, 500, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
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
            if (!canStartcharege){
                orderManager.getGunConnect(gunNo,callback,GETCONNECT);
            }

        }
        @Override
        public void onTick(long arg0) {
            if (!canStartcharege){
                orderManager.getGunConnect(gunNo,callback,GETCHARGEPROGRESS);
            }
        }
        @Override
        public void onFinish() {}
    }
    class RequestCallback extends ManagerCallback<String>{
        @Override
        public void onSuccess(String returnContent, int requestCode) {
            super.onSuccess(returnContent, requestCode);
            if (requestCode == GETCONNECT){
                canStartcharege = true;
                stopCountTime();
            }
            else {

            }

        }

        @Override
        public void onFailure(String msg, int requestCode) {
            super.onFailure(msg, requestCode);
            showToast(msg);
        }
    }
}
