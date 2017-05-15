package com.optimumnano.quickcharge.activity.filter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.SeekBar;
import android.widget.TextView;

import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.activity.MainActivity;
import com.optimumnano.quickcharge.base.BaseActivity;
import com.optimumnano.quickcharge.manager.EventManager;
import com.weijing.materialanimatedswitch.MaterialAnimatedSwitch;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FilterActivity extends BaseActivity {

    @Bind(R.id.tv_location)
    TextView tvLocation;
    @Bind(R.id.switch_msg)
    MaterialAnimatedSwitch switchMsg;
    @Bind(R.id.sk_km)
    SeekBar mKm;
    @Bind(R.id.sb_kv)
    SeekBar mKv;
    @Bind(R.id.tv_submit)
    TextView tvSubmit;
    private String mCurCity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        ButterKnife.bind(this);
        initViews();
        setTitle(getString(R.string.select_title));
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);

        if (!TextUtils.isEmpty(mHelper.getCity())) {
            tvLocation.setText(mHelper.getCity());
        } else {
            tvLocation.setText(R.string.unkown_dis);
        }

        switchMsg.check(mHelper.isShowOnlyFree());
        mKm.setProgress((mHelper.showDistance() - 2) * 5);
        if (mHelper.showKV()<240) {
            mKv.setProgress((mHelper.showKV() / 60 - 1) * 10);
        }else {
            mKv.setProgress((mHelper.showKV() / 60 - 2) * 10);
        }

/*        switchMsg.setOnCheckedChangeListener(new MaterialAnimatedSwitch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(int id, boolean isChecked) {
                mHelper.setIsShowOnlyFree(isChecked);
            }
        });*/
        mKm.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
               /* if (progress % 10 > 0) {
                    if (progress % 10 >= 5) {
                        seekBar.setProgress(progress / 10 * 10 + 10);
                    } else {
                        seekBar.setProgress(progress / 10 * 10);
                    }
                } else {
//                    mHelper.setShowDistance(progress + 10);
                }*/
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                if (progress % 10 > 0) {
                    if (progress % 10 >= 5) {
                        seekBar.setProgress(progress / 10 * 10 + 10);
                    } else {
                        seekBar.setProgress(progress / 10 * 10);
                    }
                }
            }
        });

        mKv.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                /* if (progress % 10 > 0) {
                    if (progress % 10 >= 5) {
                        seekBar.setProgress(progress / 10 * 10 + 10);
                    } else {
                        seekBar.setProgress(progress / 10 * 10);
                    }
                } else {
//                    mHelper.setKV(progress * 6 + 60);
                }*/
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                if (progress % 10 > 0) {
                    if (progress % 10 >= 5) {
                        seekBar.setProgress(progress / 10 * 10 + 10);
                    } else {
                        seekBar.setProgress(progress / 10 * 10);
                    }
                }
            }
        });

    }

    public static void start(Context mContext) {
        Intent intent = new Intent(mContext, FilterActivity.class);
        mContext.startActivity(intent);
    }

    @OnClick(R.id.tv_submit)
    public void onViewClicked() {
        mHelper.setIsShowOnlyFree(switchMsg.isChecked());
        if (mKv.getProgress()/10<2){
            mHelper.setKV((mKv.getProgress()/10 +1) * 60);
        }else {
            mHelper.setKV((mKv.getProgress()/10 +2) * 60);
        }
        mHelper.setShowDistance(mKm.getProgress()/10*2 +2);
        showToast(getString(R.string.edit_sai_xuan_success));

        if (mCurCity!=null){
            mHelper.updateCity(mCurCity);
            //EventBus.getDefault().post(new EventManager.getCurrentCity(mCurCity));
        }
        skipActivity(MainActivity.class,null);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                EventBus.getDefault().post(new EventManager.onFilterParamsChange());
                finish();
            }
        },500);
    }

    @OnClick(R.id.ll_location)
    public void onViewLocation() {
//        showToast(getString(R.string.now_no_support));
        skipActivity(ChoseCityActivity.class,null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void changeCity(EventManager.changeCity event) {
        mCurCity = event.cityname;
        tvLocation.setText(mCurCity);
        logtesti("changeCity "+event.cityname);
    }
}
