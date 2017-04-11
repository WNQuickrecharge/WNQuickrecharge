package com.optimumnano.quickcharge.adapter;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.bean.GunBean;

import java.util.List;

/**
 * Created by mfwn on 2017/4/11.
 */

public class StationGunsAdapter extends BaseQuickAdapter<GunBean,BaseViewHolder> {
    public StationGunsAdapter(int layoutResId, List<GunBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GunBean item) {
        TextView gunOperation=helper.getView(R.id.gun_status_operation);
        String gunStatus = item.gunStatus;

    }
}
