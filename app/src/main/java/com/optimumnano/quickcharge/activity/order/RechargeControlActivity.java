package com.optimumnano.quickcharge.activity.order;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.optimumnano.quickcharge.Constants;
import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.base.BaseActivity;
import com.optimumnano.quickcharge.bean.LongConnectMessageBean;
import com.optimumnano.quickcharge.dialog.WaitRechargeDialog;
import com.optimumnano.quickcharge.manager.OrderManager;
import com.optimumnano.quickcharge.net.HttpApi;
import com.optimumnano.quickcharge.net.ManagerCallback;
import com.optimumnano.quickcharge.utils.SharedPreferencesUtil;
import com.optimumnano.quickcharge.views.WaveLoadingView;
import com.zsoft.signala.Connection;
import com.zsoft.signala.ConnectionState;
import com.zsoft.signala.SendCallback;
import com.zsoft.signala.transport.StateBase;
import com.zsoft.signala.transport.longpolling.LongPollingTransport;

import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static com.optimumnano.quickcharge.utils.SPConstant.KEY_USERINFO_USER_ID;
import static com.optimumnano.quickcharge.utils.SPConstant.SP_USERINFO;

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
    private static final String TAG="RechargeControlActivity";
    private final static String HUB_URL = HttpApi.long_connet_url;
    private int progress = 1;

    private int orderStatus;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge_control);
        startConnetService();
        getExtras();
        initViews();
        initListener();
        initData();

    }

    private void startConnetService() {
        new Thread(){
            @Override
            public void run() {
                super.run();
                beginConnect();
            }
        }.start();
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
//                tvTime.setText("充电预计时间"+ha.get("time_remain")+"分钟");
//                tvPersent.setText(persent+"%");
//                waveLoadingView.setWaveHeight(persent);
//                tvDescone.setText("正在充电中");
//                tvDescTwo.setText("请您稍作休息");
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


    /**
     * 开启推送服务 panderman 2013-10-25
     */
    private void beginConnect() {
        try {
            // conn.setConnectionId(UUID.randomUUID().toString()+"|$9");
            // hub = conn.CreateHubProxy("ChatHub");

            conn.Start();

        } catch (Exception e) {
            e.printStackTrace();
        }
//        hub.On("addNewMessageToPage", new HubOnDataCallback() {
//            @Override
//            public void OnReceived(JSONArray args) {
//                EditText chatText = (EditText) findViewById(R.id.chat_text);
//                //chatText.setText(args.toString());
//                for (int i = 0; i < args.length(); i++) {
//                    chatText.append(args.opt(i).toString());
//                }
//
//
//
//
//                if (chatText.getText().toString()!=null){
//                    hub.Invoke("result", args, new HubInvokeCallback() {
//                        @Override
//                        public void OnResult(boolean succeeded, String response) {
//                            Log.d("result","success");
//                        }
//
//                        @Override
//                        public void OnError(Exception ex) {
//
//                        }
//                    });
//                }
//            }
//
//        });



    }

    private Connection conn = new Connection(HUB_URL, this, new LongPollingTransport(),"UUid=15678979657876") {
        @Override
        public void OnError(Exception exception) {
            Log.d(TAG, "OnError=" + exception.getMessage());
        }

        @Override
        public void OnMessage(String message) {
            Log.d(TAG, "message=" + message);
            LongConnectMessageBean longConnectMessageBean = JSON.parseObject(message, LongConnectMessageBean.class);
            int status = longConnectMessageBean.getStatus();
            /** 订单状态
             * 免单=0,
             * 已取消=1,
             * 待支付=2,
             * 待充电=3,
             * 充电中=4,
             * 待评价=5,
             * 已完成=6
             **/

            switch (status) {
                case 0://停止充电的情况


                    break;
                case 4://充电中
                    int charge_soc = longConnectMessageBean.getCharge_soc();
                    int time_remain = longConnectMessageBean.getTime_remain();
                    tvTime.setText("充电预计时间"+time_remain+"分钟");
                    tvPersent.setText(persent+"%");
                    waveLoadingView.setWaveHeight(persent);
                    tvDescone.setText("正在充电中");
                    tvDescTwo.setText("请您稍作休息");


                    break;

                case 5:

                    break;
                case 6://充电完成,跳转界面获取生成的订单信息.
                    String order_no = longConnectMessageBean.getOrder_no();
                    int power_time = longConnectMessageBean.getPower_time();
                    Double consume_money = longConnectMessageBean.getConsume_money();
                    Double forzen_cash = longConnectMessageBean.getForzen_cash();
                    Double back_cash = longConnectMessageBean.getBack_cash();

                    break;

                default:
                    break;
            }

        }

        @Override
        public void OnStateChanged(StateBase oldState, StateBase newState) {
            Log.d(TAG, "OnStateChanged=" + oldState.getState() + " -> " + newState.getState());
            if (newState.getState()== ConnectionState.Connected){
                String userID = SharedPreferencesUtil.getValue(SP_USERINFO, KEY_USERINFO_USER_ID, "");
                conn.Send("{'data_type':1,'user_id':+"+ userID +"}", new SendCallback() {
                    @Override
                    public void OnSent(CharSequence messageSent) {
                        Log.d("onSent","正在传送");
                    }

                    @Override
                    public void OnError(Exception ex) {
                        ex.printStackTrace();
                    }
                });
            }
        }
    };
}
