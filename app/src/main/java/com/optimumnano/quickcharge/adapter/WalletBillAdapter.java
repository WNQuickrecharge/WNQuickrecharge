package com.optimumnano.quickcharge.adapter;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.optimumnano.quickcharge.R;

import java.util.List;

/**
 * Created by ds on 2017/3/29.
 */
public class WalletBillAdapter extends BaseQuickAdapter<String,BaseViewHolder> {
    public WalletBillAdapter(int layoutResId, List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        TextView tvStatus = helper.getView(R.id.item_bill_list_tv);
        tvStatus.setText(item);
    }
}
