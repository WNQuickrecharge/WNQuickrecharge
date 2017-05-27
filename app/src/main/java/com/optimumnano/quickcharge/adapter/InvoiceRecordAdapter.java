package com.optimumnano.quickcharge.adapter;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.activity.invoice.PayCenterActivity;
import com.optimumnano.quickcharge.bean.InvoiceRecordBean;
import com.optimumnano.quickcharge.utils.StringUtils;
import com.optimumnano.quickcharge.views.MenuItem1;

import java.util.List;

/**
 * Created by PC on 2017/5/7.
 */

public class InvoiceRecordAdapter extends BaseQuickAdapter<InvoiceRecordBean, BaseViewHolder> {

    public InvoiceRecordAdapter(int layoutResId, List<InvoiceRecordBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final InvoiceRecordBean item) {
        /**
         * 整个item点击
         */
        LinearLayout all_item = helper.getView(R.id.all_item);
        /**
         *发票类型
         */
        MenuItem1 miType = helper.getView(R.id.adapter_invoice_record_miInvoiceType);
        /**
         *开票金额
         */
        MenuItem1 miInvoiceMoney = helper.getView(R.id.adapter_invoice_record_miInvoiceMoney);
        /**
         *运费
         */
        MenuItem1 miPostcash = helper.getView(R.id.adapter_invoice_record_miPostcash);
        /**
         * 开票时间
         */
        helper.setText(R.id.adapter_invoice_record_miInvoiceTime, "开票时间:" + item.CreateTime);
        /**
         *订单编号
         */
        helper.setText(R.id.adapter_invoice_record_tvOrderno, item.InvoiceOrderNo);
        /**
         *状态
         * 待开具发票/待处理----->表示已经支付成功
         */

        if ("待开具发票".equals(item.Status)) {
            helper.setText(R.id.adapter_invoice_record_tvStatus, item.Status);

        } else {
            helper.setText(R.id.adapter_invoice_record_tvStatus, item.Status);
            helper.setOnClickListener(R.id.all_item, new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent();
                    intent.setClass(mContext, PayCenterActivity.class);
                    intent.putExtra("allmoney", item.InvoiceAmount);
                    intent.putExtra("order_no", item.InvoiceOrderNo );
                    intent.putExtra("money", item.Postage);
                    mContext.startActivity(intent);
                }
            });
        }


        miType.setRightText(item.Type);
        miInvoiceMoney.setRightText("￥" + item.InvoiceAmount + "");
        miPostcash.setRightText("￥" + item.Postage + "");
        if (StringUtils.isEmpty(item.OperateTime)) {
            helper.setVisible(R.id.adapter_invoice_record_rlOperate, false);
        } else {
            helper.setVisible(R.id.adapter_invoice_record_rlOperate, true);
            /**
             * 快递类型
             */
            helper.setText(R.id.adapter_invoice_record_tvSendType, item.LogisticsName);
            /**
             * 配送时间
             */
            helper.setText(R.id.adapter_invoice_record_tvSendTime, "配送时间:" + item.OperateTime);
        }
    }
}
