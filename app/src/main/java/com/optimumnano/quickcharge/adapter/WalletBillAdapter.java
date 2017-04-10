package com.optimumnano.quickcharge.adapter;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.optimumnano.quickcharge.R;

import java.util.List;


/**
 * 作者：邓传亮 on 2017/4/7 12:43
 * <p>
 * 邮箱：dengchuanliang@optimumchina.com
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
