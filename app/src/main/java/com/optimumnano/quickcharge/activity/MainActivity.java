package com.optimumnano.quickcharge.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.activity.filter.FilterActivity;
import com.optimumnano.quickcharge.activity.mineinfo.MyMessageAct;
import com.optimumnano.quickcharge.base.BaseActivity;
import com.optimumnano.quickcharge.bean.Point;
import com.optimumnano.quickcharge.event.OnPushDataEvent;
import com.optimumnano.quickcharge.fragment.MineFragment;
import com.optimumnano.quickcharge.fragment.OrderFragment;
import com.optimumnano.quickcharge.fragment.RechargeFragment;
import com.optimumnano.quickcharge.popupWindow.showHelper.BaseShowHelper;
import com.optimumnano.quickcharge.popupWindow.showHelper.DistShowHepler;
import com.optimumnano.quickcharge.utils.KeyboardWatcher;
import com.optimumnano.quickcharge.views.MyViewPager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {
    private MyViewPager viewPager;
    private RadioGroup rg;
    private RadioButton rbRecharge;

    private List<Fragment> listFrg = new ArrayList<>();
    private RechargeFragment rechargeFragment;
    private OrderFragment orderFragment;
    private MineFragment mineFragment;

    KeyboardWatcher keyboardWatcher;

    private DistShowHepler mShowHelper;

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
        mShowHelper=new DistShowHepler(this);
        requestPermission(permissions,0);
        initViews();
        initData();
        initListener();
        keyboardWatcher=new KeyboardWatcher(this);
        keyboardWatcher.setListener(new KeyboardWatcher.OnKeyboardToggleListener() {
            @Override
            public void onKeyboardShown(int keyboardSize) {

            }

            @Override
            public void onKeyboardClosed() {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void initViews() {
        super.initViews();
        setTitle(getString(R.string.recharge));
        setLeftTitle("筛选");
        setRightTitle("列表");
        hideBack();

        viewPager = (MyViewPager) findViewById(R.id.main_viewPager);
        rg = (RadioGroup) findViewById(R.id.main_rg);
        rbRecharge = (RadioButton) findViewById(R.id.main_rbRecharge);
        rbRecharge.setChecked(true);

    }

    @Override
    protected void onLeftDoSomething() {
        FilterActivity.start(this);
    }

    @Override
    protected void onRightDoSomething() {
        super.onRightDoSomething();
        mShowHelper.setData(mData);
        mShowHelper.show(BaseShowHelper.SHOW_TYPE_VIEW,toolbar);
    }

    private void initListener() {
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.main_rbRecharge:
                        setTitle(getString(R.string.recharge));
                        setLeftTitle("筛选");
                        setRightTitle("列表");
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.main_rbOrder:
                        setTitle(getString(R.string.order));
                        setLeftTitle("");

                        setRightTitle("");
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.main_rbMine:
                        setTitle(getString(R.string.mine));
                        setLeftTitle("");
                        setRightTitle("消息");
                        viewPager.setCurrentItem(2);

                        tvRight.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(MainActivity.this, MyMessageAct.class));
                            }
                        });
                        break;
                    default:
                        break;
                }
            }
        });
    }
    public void initData(){
        rechargeFragment = new RechargeFragment();
        orderFragment = new OrderFragment();
        mineFragment = new MineFragment();
        listFrg.add(rechargeFragment);
        listFrg.add(orderFragment);
        listFrg.add(mineFragment);
        viewPager.setAdapter(fpa);
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        rechargeFragment.onActivityResult(requestCode,resultCode,data);
    }
    List<Point> mData;
    @Subscribe
    public void onPushData(OnPushDataEvent event) {
        mData= (List<Point>) event.getObj();
    }

}
