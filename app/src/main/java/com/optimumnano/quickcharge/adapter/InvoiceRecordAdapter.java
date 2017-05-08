package com.optimumnano.quickcharge.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.bean.InvoiceRecordBean;
import com.optimumnano.quickcharge.utils.StringUtils;
import com.optimumnano.quickcharge.views.MenuItem1;

import java.util.List;

/**
 * Created by PC on 2017/5/7.
 */

public class InvoiceRecordAdapter extends BaseQuickAdapter<InvoiceRecordBean,BaseViewHolder> {

    public InvoiceRecordAdapter(int layoutResId, List<InvoiceRecordBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, InvoiceRecordBean item) {
        MenuItem1 miType = helper.getView(R.id.adapter_invoice_record_miInvoiceType);
        MenuItem1 miInvoiceMoney = helper.getView(R.id.adapter_invoice_record_miInvoiceMoney);
        MenuItem1 miPostcash = helper.getView(R.id.adapter_invoice_record_miPostcash);
        helper.setText(R.id.adapter_invoice_record_miInvoiceTime,item.CreateTime);
        helper.setText(R.id.adapter_invoice_record_tvOrderno,item.InvoiceOrderNo);
        helper.setText(R.id.adapter_invoice_record_tvStatus,item.Status);
        miType.setRightText(item.Type);
        miInvoiceMoney.setRightText(item.InvoiceAmount+"");
        miPostcash.setRightText(item.Postage+"");
        if (StringUtils.isEmpty(item.OperateTime)){
            helper.setVisible(R.id.adapter_invoice_record_rlOperate,false);
        }
    }
}
