package com.optimumnano.quickcharge.activity.filter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.SeekBar;
import android.widget.TextView;

import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.base.BaseActivity;
import com.weijing.materialanimatedswitch.MaterialAnimatedSwitch;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FilterActivity extends BaseActivity {

    @Bind(R.id.tv_location)
    TextView tvLocation;
    @Bind(R.id.switch_msg)
    MaterialAnimatedSwitch switchMsg;
    @Bind(R.id.sk_km)
    SeekBar mKm;
    @Bind(R.id.sb_kv)
    SeekBar mKv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        ButterKnife.bind(this);
        initViews();
        setTitle(getString(R.string.select_title));

        if (!TextUtils.isEmpty(mHelper.getCity())){
            tvLocation.setText(mHelper.getCity());
        }else {
            tvLocation.setText(R.string.unkown_dis);
        }

        switchMsg.check(mHelper.isShowOnlyFree());
        mKm.setProgress(mHelper.showDistance()-10);
        mKv.setProgress(mHelper.showKV()-60);

        switchMsg.setOnCheckedChangeListener(new MaterialAnimatedSwitch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(int id, boolean isChecked) {
                mHelper.setIsShowOnlyFree(isChecked);
            }
        });
        mKm.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress%10>0){
                    seekBar.setProgress(progress/10*10+10);
                }else {
                    mHelper.setShowDistance(progress+10);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        mKv.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress%10>0){
                    seekBar.setProgress(progress/10*10+10);
                }else {
                    mHelper.setShowDistance(progress*6+60);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

    }

    public static void start(Context mContext) {
        Intent intent = new Intent(mContext, FilterActivity.class);
        mContext.startActivity(intent);
    }
}
