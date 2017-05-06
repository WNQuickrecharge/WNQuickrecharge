package com.optimumnano.quickcharge.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.baidu.navisdk.adapter.BNCommonSettingParam;
import com.baidu.navisdk.adapter.BNOuterLogUtil;
import com.baidu.navisdk.adapter.BNOuterTTSPlayerCallback;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BNaviSettingManager;
import com.baidu.navisdk.adapter.BaiduNaviManager;
import com.igexin.sdk.PushManager;
import com.optimumnano.quickcharge.Constants;
import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.activity.filter.FilterActivity;
import com.optimumnano.quickcharge.activity.invoice.InvoiceActivity;
import com.optimumnano.quickcharge.activity.login.LoginActivity;
import com.optimumnano.quickcharge.activity.test.BNDemoGuideActivity;
import com.optimumnano.quickcharge.alipay.PayResult;
import com.optimumnano.quickcharge.base.BaseActivity;
import com.optimumnano.quickcharge.bean.Point;
import com.optimumnano.quickcharge.event.OnNaviEvent;
import com.optimumnano.quickcharge.event.OnPushDataEvent;
import com.optimumnano.quickcharge.fragment.MineFragment;
import com.optimumnano.quickcharge.fragment.OrderFragment;
import com.optimumnano.quickcharge.fragment.RechargerViewPagerFrag;
import com.optimumnano.quickcharge.manager.CollectManager;
import com.optimumnano.quickcharge.manager.EventManager;
import com.optimumnano.quickcharge.net.ManagerCallback;
import com.optimumnano.quickcharge.service.MyIntentService;
import com.optimumnano.quickcharge.utils.AppManager;
import com.optimumnano.quickcharge.utils.KeyboardWatcher;
import com.optimumnano.quickcharge.utils.SPConstant;
import com.optimumnano.quickcharge.utils.SharedPreferencesUtil;
import com.optimumnano.quickcharge.views.MyViewPager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends BaseActivity {

    private String mSDCardPath = null;

    private boolean hasInitSuccess = false;
    private boolean hasRequestComAuth = false;

    String authinfo = null;


    private MyViewPager viewPager;
    private RadioGroup rg;
    private RadioButton rbRecharge;

    private List<Fragment> listFrg = new ArrayList<>();
    //private RechargeFragment rechargeFragment;
    private OrderFragment orderFragment;
    private MineFragment mineFragment;
    private RechargerViewPagerFrag rechargerViewPagerFrag;

    KeyboardWatcher keyboardWatcher;

    //private DistShowHepler mShowHelper;
    private boolean doubleBackToExitPressedOnce;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //权限申请
        String[] permissions = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.CAMERA
        };
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
//        mShowHelper = new DistShowHepler(this);
//        mShowHelper.getmPopupWindow().setOnDismissListener(new PopupWindow.OnDismissListener() {
//            @Override
//            public void onDismiss() {
//                isShow=false;
//                //setLeftTitle("筛选");
//            }
//        });
        requestPermission(permissions, 0);
        initViews();
        initData();
        initListener();
        keyboardWatcher = new KeyboardWatcher(this);
        keyboardWatcher.setListener(new KeyboardWatcher.OnKeyboardToggleListener() {
            @Override
            public void onKeyboardShown(int keyboardSize) {

            }

            @Override
            public void onKeyboardClosed() {

            }
        });

        BNOuterLogUtil.setLogSwitcher(true);

        initListener();
        if (initDirs()) {
            initNavi();
        }


        PushManager.getInstance().registerPushIntentService(this.getApplicationContext(), MyIntentService.class);
    }

    private boolean initDirs() {
        mSDCardPath = getSdcardDir();
        if (mSDCardPath == null) {
            return false;
        }
        File f = new File(mSDCardPath, Constants.APP_FOLDER_NAME);
        if (!f.exists()) {
            try {
                f.mkdir();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    private String getSdcardDir() {
        if (Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().toString();
        }
        return null;
    }

    private void initNavi() {
        BNOuterTTSPlayerCallback ttsCallback = null;
        BaiduNaviManager.getInstance().init(this, mSDCardPath, Constants.APP_FOLDER_NAME, new BaiduNaviManager.NaviInitListener() {
            @Override
            public void onAuthResult(int status, String msg) {
                if (0 == status) {
                    authinfo = "key校验成功!";

                } else {
                    authinfo = "key校验失败, " + msg;
                }
                Log.d("TAG", authinfo);
            }

            public void initSuccess() {
                Log.d("TAG", "百度导航引擎初始化成功");
                hasInitSuccess = true;
                initSetting();
            }

            public void initStart() {
                Log.d("TAG", "百度导航引擎初始化开始");
            }

            public void initFailed() {
                Log.d("TAG", "百度导航引擎初始化失败");
            }

        }, null, ttsHandler, ttsPlayStateListener);

    }

    /**
     * 内部TTS播报状态回调接口
     */
    private BaiduNaviManager.TTSPlayStateListener ttsPlayStateListener = new BaiduNaviManager.TTSPlayStateListener() {
        @Override
        public void playEnd() {
            // showToastMsg("TTSPlayStateListener : TTS play end");
        }

        @Override
        public void playStart() {
            // showToastMsg("TTSPlayStateListener : TTS play start");
        }
    };

    /**
     * 内部TTS播报状态回传handler
     */
    private Handler ttsHandler = new Handler() {
        public void handleMessage(Message msg) {
            int type = msg.what;
            switch (type) {
                case BaiduNaviManager.TTSPlayMsgType.PLAY_START_MSG: {
                    // showToastMsg("Handler : TTS play start");
                    break;
                }
                case BaiduNaviManager.TTSPlayMsgType.PLAY_END_MSG: {
                    // showToastMsg("Handler : TTS play end");
                    break;
                }
                default:
                    break;
            }
        }
    };

    private void initSetting() {
        // BNaviSettingManager.setDayNightMode(BNaviSettingManager.DayNightMode.DAY_NIGHT_MODE_DAY);
        BNaviSettingManager
                .setShowTotalRoadConditionBar(BNaviSettingManager.PreViewRoadCondition.ROAD_CONDITION_BAR_SHOW_ON);
        BNaviSettingManager.setVoiceMode(BNaviSettingManager.VoiceMode.Veteran);
        // BNaviSettingManager.setPowerSaveMode(BNaviSettingManager.PowerSaveMode.DISABLE_MODE);
        BNaviSettingManager.setRealRoadCondition(BNaviSettingManager.RealRoadCondition.NAVI_ITS_ON);
        Bundle bundle = new Bundle();
        // 必须设置APPID，否则会静音
        bundle.putString(BNCommonSettingParam.TTS_APP_ID, "9459984");
        BNaviSettingManager.setNaviSdkParam(bundle);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!EventBus.getDefault().isRegistered(this))
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //EventBus.getDefault().unregister(this);
    }

    @Override
    public void initViews() {
        super.initViews();
        setTitle(getString(R.string.recharge));
        setLeftTitle("定位");
        setRightTitle("列表");
        hideBack();

        viewPager = (MyViewPager) findViewById(R.id.main_viewPager);
        rg = (RadioGroup) findViewById(R.id.main_rg);
        rbRecharge = (RadioButton) findViewById(R.id.main_rbRecharge);
        rbRecharge.setChecked(true);

    }

    @Override
    protected void onLeftDoSomething() {
//        if (isShow==true){
//            mShowHelper.getmPopupWindow().dismiss();
//        }else {
//            FilterActivity.start(this);
//        }
        FilterActivity.start(this);
    }

    //boolean isShow = false;

    @Override
    protected void onRightDoSomething() {
        super.onRightDoSomething();
//        mShowHelper.setData(mData);
//        mShowHelper.show(BaseShowHelper.SHOW_TYPE_VIEW, toolbar);
//        isShow = mShowHelper.getmPopupWindow().isShowing();
//        if (isShow){
//            setLeftTitle("定位");
//        }else{
//           // setLeftTitle("筛选");
//        }
        switch (viewPager.getCurrentItem()){
            case 0:
                String s = tvRight.getText().toString();
                if ("列表".equals(s)){
                    tvRight.setText("地图");
                    rechargerViewPagerFrag.getViewPager().setCurrentItem(1);
                }else if ("地图".equals(s)){
                    tvRight.setText("列表");
                    rechargerViewPagerFrag.getViewPager().setCurrentItem(0);
                }
                break;
            case 1:
                skipActivity(InvoiceActivity.class,null);
                break;
            case 2:

                break;
        }

    }

    private void initListener() {
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.main_rbRecharge:
                        setTitle(getString(R.string.recharge));
                        setLeftTitle("定位");
                        setRightTitle("列表");
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.main_rbOrder:
                        setTitle(getString(R.string.order));
                        setLeftTitle("");
                        setRightTitle("");
                        //setRightTitle("开发票");
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.main_rbMine:
                        setTitle(getString(R.string.mine));
                        setLeftTitle("");
                        setRightTitle("");//第一版不做消息
                        viewPager.setCurrentItem(2);

                        /*tvRight.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(MainActivity.this, MyMessageAct.class));
                            }
                        });*/
                        break;
                    default:
                        break;
                }
            }
        });
        tvRight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                switch (s.toString()) {
                    case "列表":
                        if (rechargerViewPagerFrag.getViewPager()!=null) {
                            rechargerViewPagerFrag.getViewPager().setCurrentItem(0);
                        }
                        break;

                    case "地图":
                        if (rechargerViewPagerFrag.getViewPager()!=null) {
                            rechargerViewPagerFrag.getViewPager().setCurrentItem(1);
                        }
                        break;

                    default:
                        break;
                }
            }
        });
    }

    public void initData() {
        rechargerViewPagerFrag=new RechargerViewPagerFrag();
        //rechargeFragment = new RechargeFragment();
        orderFragment = new OrderFragment();
        mineFragment = new MineFragment();
        listFrg.add(rechargerViewPagerFrag);
        listFrg.add(orderFragment);
        listFrg.add(mineFragment);
        viewPager.setAdapter(fpa);
        viewPager.setOffscreenPageLimit(3);
    }

    FragmentPagerAdapter fpa = new FragmentPagerAdapter(getSupportFragmentManager()) {
        @Override
        public Fragment getItem(int position) {
            return listFrg.get(position);
        }

        @Override
        public int getCount() {
            return listFrg.size();
        }
    };

    @Override
    public void permissionSuccess(int requestCode) {
        super.permissionSuccess(requestCode);
    }

    @Override
    public void permissionFail(int requestCode) {
        super.permissionFail(requestCode);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        keyboardWatcher.destroy();
        EventBus.getDefault().unregister(this);
        PushManager.getInstance().stopService(this.getApplicationContext());//停止SDK服务
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        rechargerViewPagerFrag.onActivityResult(requestCode, resultCode, data);
    }

    List<Point> mData;

    @Subscribe
    public void onPushData(OnPushDataEvent event) {
        mData = (List<Point>) event.getObj();
    }

    @Subscribe
    public void onNavi(OnNaviEvent event) {
        if (event == null)
            return;

        navi(event.end);
    }

    private void navi(Point mPoint) {
        if (BaiduNaviManager.isNaviInited()) {
            //routeplanToNavi(BNRoutePlanNode.CoordinateType.WGS84, mPoint);
            routeplanToNavi(BNRoutePlanNode.CoordinateType.BD09LL,BNRoutePlanNode.CoordinateType.BD09LL, mPoint);
        }
    }

    private BNRoutePlanNode.CoordinateType mCoordinateType = null;

    private void routeplanToNavi(BNRoutePlanNode.CoordinateType coType, Point mPoint) {
        mCoordinateType = coType;
        if (!hasInitSuccess) {
            Log.d("TAG", "还未初始化!");
        }
        BNRoutePlanNode sNode = null;
        BNRoutePlanNode eNode = null;
        sNode = new BNRoutePlanNode(mHelper.getLocation().lng, mHelper.getLocation().lat, "我的位置", null, coType);
        eNode = new BNRoutePlanNode(mPoint.Lng, mPoint.Lat, mPoint.StationName, null, coType);
        if (sNode != null && eNode != null) {
            List<BNRoutePlanNode> list = new ArrayList<BNRoutePlanNode>();
            list.add(sNode);
            list.add(eNode);
            BaiduNaviManager.getInstance().launchNavigator(this, list, 1, true, new DemoRoutePlanListener(sNode));
        }
    }
    private void routeplanToNavi(BNRoutePlanNode.CoordinateType startPonitCoType,BNRoutePlanNode.CoordinateType endPointCoType ,Point mPoint) {
        //mCoordinateType = coType;
        if (!hasInitSuccess) {
            Log.d("TAG", "还未初始化!");
        }
        BNRoutePlanNode sNode = null;
        BNRoutePlanNode eNode = null;
        sNode = new BNRoutePlanNode(mHelper.getLocation().lng, mHelper.getLocation().lat, "我的位置", null, startPonitCoType);
        eNode = new BNRoutePlanNode(mPoint.Lng, mPoint.Lat, mPoint.StationName, null, endPointCoType);
        if (sNode != null && eNode != null) {
            List<BNRoutePlanNode> list = new ArrayList<BNRoutePlanNode>();
            list.add(sNode);
            list.add(eNode);
            BaiduNaviManager.getInstance().launchNavigator(this, list, 1, true, new DemoRoutePlanListener(sNode));
        }
    }

    public static final String ROUTE_PLAN_NODE = "routePlanNode";

    public class DemoRoutePlanListener implements BaiduNaviManager.RoutePlanListener {

        private BNRoutePlanNode mBNRoutePlanNode = null;

        public DemoRoutePlanListener(BNRoutePlanNode node) {
            mBNRoutePlanNode = node;
        }

        @Override
        public void onJumpToNavigator() {
//            /*
//             * 设置途径点以及resetEndNode会回调该接口
//             */
//            for (Activity ac : activityList) {
//                if (ac.getClass().getName().endsWith("BNDemoGuideActivity")) {
//                    return;
//                }
//            }
            Intent intent = new Intent(MainActivity.this, BNDemoGuideActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(ROUTE_PLAN_NODE, (BNRoutePlanNode) mBNRoutePlanNode);
            intent.putExtras(bundle);
            startActivity(intent);

        }

        @Override
        public void onRoutePlanFailed() {
            Log.d("TAG", "算路失败");
        }
    }


    public void pay(String order) {

        final String orderInfo = order;   // 订单信息
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(MainActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }


    private static final int SDK_PAY_FLAG = 1;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Toast.makeText(MainActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(MainActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void openStationDetail(EventManager.openStationDetail event) {
        int id = event.id;
        new CollectManager().addCollectStation(id, new ManagerCallback() {
            @Override
            public void onSuccess(Object returnContent) {
                super.onSuccess(returnContent);
                showToast("收藏成功!");
            }

            @Override
            public void onFailure(String msg) {
                super.onFailure(msg);
                showToast(msg);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            AppManager.getAppManager().finishAllActivity();
            return;
        }
        doubleBackToExitPressedOnce = true;
        showToast(getString(R.string.exit_hint));
        new Thread(){
            @Override
            public void run() {
                super.run();
                SystemClock.sleep(2000);
                doubleBackToExitPressedOnce=false;
            }
        }.start();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void cookieTimeOut(EventManager.cookieTimeOut event) {
        AppManager.getAppManager().finishAllActivity();
        SharedPreferencesUtil.getEditor(SPConstant.SP_COOKIE).clear().commit();
        startActivity(new Intent(this, LoginActivity.class));
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void mainActivitySelectOrderTag(EventManager.mainActivitySelectOrderTag event) {
       viewPager.setCurrentItem(1);
        rg.check(R.id.main_rbOrder);
    }
}
