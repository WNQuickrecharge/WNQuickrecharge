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
import com.optimumnano.quickcharge.manager.InvoiceManager;
import com.optimumnano.quickcharge.response.GetInvoiceOrderListResult;
import com.optimumnano.quickcharge.utils.ToastUtil;
import com.optimumnano.quickcharge.utils.Tool;
import com.optimumnano.quickcharge.views.MyDivier;

import java.util.ArrayList;
import java.util.List;

public class InvoiceRecordActivity extends BaseActivity implements HttpCallback {
    HTRefreshRecyclerView recyclerView;

    private List<InvoiceRecordBean> list = new ArrayList<>();
    private InvoiceRecordAdapter adapter;

    private InvoiceManager manager = new InvoiceManager();

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
    public void initViews() {
        super.initViews();
        setTitle("开票记录");
        recyclerView = (HTRefreshRecyclerView) findViewById(R.id.invoice_record_recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        MyDivier divier = new MyDivier(this, MyDivier.VERTICAL_LIST);
        recyclerView.addItemDecoration(divier);

    }

    private void initData() {
//        manager.getOrderlist(new ManagerCallback<List<InvoiceRecordBean>>() {
//            @Override
//            public void onSuccess(List<InvoiceRecordBean> returnContent) {
//                super.onSuccess(returnContent);
//                list.addAll(returnContent);
//                dataChanged();
//            }
//
//            @Override
//            public void onFailure(String msg) {
//                super.onFailure(msg);
//                showToast(msg);
//            }
//        });


        if (!Tool.isConnectingToInternet()) {
            showToast("无网络");
            return;
        }


    }

    private void dataChanged() {
        if (adapter == null) {
            adapter = new InvoiceRecordAdapter(R.layout.adapter_invoicer_record, list);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    //http

    @Override
    public void onRequestSuccess(int id, BaseResult result) {
        if (isFinishing()) {
            return;
        }
        if (mGetInvoiceOrderListTaskId == id) {
            list.addAll(((GetInvoiceOrderListResult) result).getResp().getResult());
            dataChanged();
        }
    }

    @Override
    public void onRequestFail(int id, BaseResult result) {
        if (isFinishing()) {
            return;
        }
        if (mGetInvoiceOrderListTaskId == id) {
            showToast(ToastUtil.formatToastText(mContext, ((GetInvoiceOrderListResult) result).getResp()));
        }
    }

    @Override
    public void onRequestCancel(int id) {

    }
}
