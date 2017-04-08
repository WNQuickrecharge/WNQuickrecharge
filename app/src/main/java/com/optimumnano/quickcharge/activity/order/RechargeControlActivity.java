package com.optimumnano.quickcharge.activity.order;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.base.BaseActivity;
import com.optimumnano.quickcharge.dialog.WaitRechargeDialog;
import com.optimumnano.quickcharge.views.WaveLoadingView;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RechargeControlActivity extends BaseActivity implements View.OnClickListener {

    private WaveLoadingView waveLoadingView;
    private TextView tvPersent,tvStart,tvStop,tvDescone,tvDescTwo,tvTime;
    int persent = 0;
    static ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
    private WaitRechargeDialog dialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge_control);
        initViews();
        initListener();
    }

    private void initListener() {
        tvStart.setOnClickListener(this);
        tvStop.setOnClickListener(this);
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

                break;
            case R.id.rechargecon_tvStart:
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
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
