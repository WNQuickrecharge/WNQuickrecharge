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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        ButterKnife.bind(this);
        initViews();
        setTitle(getString(R.string.select_title));

        if (!TextUtils.isEmpty(mHelper.getCity())) {
            tvLocation.setText(mHelper.getCity());
        } else {
            tvLocation.setText(R.string.unkown_dis);
        }

        switchMsg.check(mHelper.isShowOnlyFree());
        mKm.setProgress(mHelper.showDistance() - 10);
        mKv.setProgress(mHelper.showKV() - 60);

        switchMsg.setOnCheckedChangeListener(new MaterialAnimatedSwitch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(int id, boolean isChecked) {
                mHelper.setIsShowOnlyFree(isChecked);
            }
        });
        mKm.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress % 10 > 0) {
                    if (progress % 10 >= 5) {
                        seekBar.setProgress(progress / 10 * 10 + 10);
                    } else {
                        seekBar.setProgress(progress / 10 * 10);
                    }
                } else {
//                    mHelper.setShowDistance(progress + 10);
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
                if (progress % 10 > 0) {
                    if (progress % 10 >= 5) {
                        seekBar.setProgress(progress / 10 * 10 + 10);
                    } else {
                        seekBar.setProgress(progress / 10 * 10);
                    }
                } else {
//                    mHelper.setKV(progress * 6 + 60);
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

    @OnClick(R.id.tv_submit)
    public void onViewClicked() {
        mHelper.setIsShowOnlyFree(switchMsg.isChecked());
        mHelper.setKV(mKv.getProgress() * 6 + 60);
        mHelper.setShowDistance(mKm.getProgress() * 6 + 60);
        showToast(getString(R.string.edit_sai_xuan_success));
    }

    @OnClick(R.id.ll_location)
    public void onViewLocation() {
        showToast(getString(R.string.now_no_support));
    }
}
