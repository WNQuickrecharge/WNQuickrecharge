package com.optimumnano.quickcharge.activity.invoice;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.netease.hearttouch.htrefreshrecyclerview.HTRefreshRecyclerView;
import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.adapter.InvoiceRecordAdapter;
import com.optimumnano.quickcharge.base.BaseActivity;
import com.optimumnano.quickcharge.bean.InvoiceRecordBean;
import com.optimumnano.quickcharge.http.BaseResult;
import com.optimumnano.quickcharge.http.HttpCallback;
import com.optimumnano.quickcharge.http.HttpTask;
import com.optimumnano.quickcharge.http.TaskIdGenFactory;
import com.optimumnano.quickcharge.request.GetInvoiceRecordRequest;
import com.optimumnano.quickcharge.response.GetInvoiceRecordResult;
import com.optimumnano.quickcharge.utils.ToastUtil;
import com.optimumnano.quickcharge.utils.Tool;
import com.optimumnano.quickcharge.views.MyDivier;

import java.util.ArrayList;
import java.util.List;

/**
 * 开票记录页
 */
public class InvoiceRecordActivity extends BaseActivity implements HttpCallback {
    HTRefreshRecyclerView recyclerView;

    private List<InvoiceRecordBean> list = new ArrayList<>();
    private InvoiceRecordAdapter adapter;

    private int mGetInvoiceOrderListTaskId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_record);
        initViews();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTaskDispatcher.cancel(mGetInvoiceOrderListTaskId);
    }

    @Override
    protected void onResume() {
        super.onResume();
        recyclerView.startAutoRefresh();
    }

    @Override
    public void initViews() {
        super.initViews();
        setTitle("开票记录");
        recyclerView = (HTRefreshRecyclerView) findViewById(R.id.invoice_record_recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        MyDivier divier = new MyDivier(this, MyDivier.VERTICAL_LIST);
        recyclerView.addItemDecoration(divier);

    }

    private void initData() {
        if (!Tool.isConnectingToInternet()) {
            showToast("无网络");
            return;
        }

        mGetInvoiceOrderListTaskId = TaskIdGenFactory.gen();
        mTaskDispatcher.dispatch(new HttpTask(
                mGetInvoiceOrderListTaskId,new GetInvoiceRecordRequest(
                                                new GetInvoiceRecordResult(mContext)), this));
    }

    private void dataChanged() {
        if (adapter == null) {
            adapter = new InvoiceRecordAdapter(R.layout.adapter_invoicer_record, list);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onRequestSuccess(int id, BaseResult result) {
        if (isFinishing()) {
            return;
        }
        if (mGetInvoiceOrderListTaskId == id) {
            list.clear();
            list.addAll(((GetInvoiceRecordResult) result).getResp().getResult());
            dataChanged();
        }
    }

    @Override
    public void onRequestFail(int id, BaseResult result) {
        if (isFinishing()) {
            return;
        }
        if (mGetInvoiceOrderListTaskId == id) {
            showToast(ToastUtil.formatToastText(mContext, ((GetInvoiceRecordResult) result).getResp()));
        }
    }

    @Override
    public void onRequestCancel(int id) {

    }
}
