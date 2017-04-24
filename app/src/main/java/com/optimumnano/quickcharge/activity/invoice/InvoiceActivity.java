package com.optimumnano.quickcharge.activity.invoice;

import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.adapter.InvoiceAdapter;
import com.optimumnano.quickcharge.base.BaseActivity;
import com.optimumnano.quickcharge.bean.InvoiceOrder;
import com.optimumnano.quickcharge.bean.InvoiceOrderGroup;
import com.optimumnano.quickcharge.manager.InvoiceManager;
import com.optimumnano.quickcharge.net.ManagerCallback;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 开发票界面
 */
public class InvoiceActivity extends BaseActivity implements View.OnClickListener, InvoiceAdapter.OnCheckListener {
    private ExpandableListView listView;
    private TextView tvNext,tvAllMoney;

    private InvoiceAdapter adapter;
    private List<InvoiceOrder> list;
    private List<String> listStr = new ArrayList<>();

    private List<InvoiceOrderGroup> group = new ArrayList<>();
    private List<List<InvoiceOrder>> child = new ArrayList<>();

    private InvoiceManager manager = new InvoiceManager();
    private int month;
    private double allMoney = 0;//发票金额

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);
        initViews();
        initData();
    }

    @Override
    public void initViews() {
        super.initViews();
        setTitle("开发票");
        setRightTitle("开票记录");
        tvRight.setOnClickListener(this);

        tvNext = (TextView) findViewById(R.id.invoice_tvNext);
        tvNext.setOnClickListener(this);
        tvAllMoney = (TextView) findViewById(R.id.invoice_tvMoney);
        listView = (ExpandableListView) findViewById(R.id.invoice_Listv);
        listView.setGroupIndicator(null);
    }

    private void initData() {
        manager.getInvoiceRecord(new ManagerCallback<List<InvoiceOrder>>() {
            @Override
            public void onSuccess(List<InvoiceOrder> returnContent) {
                super.onSuccess(returnContent);
                list = returnContent;
                dealData();
            }

            @Override
            public void onFailure(String msg) {
                super.onFailure(msg);
            }
        });
    }

    private void dealData() {
        List<InvoiceOrder> list1 = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (i == 0) {
                month = list.get(i).ConsumeMonth;
                listStr.add(month + "月");
                list1.add(list.get(i));
            } else if (i == list.size() - 1) {
                List<InvoiceOrder> list2 = new ArrayList<>();
                list2.addAll(list1);
                child.add(list2);
            } else if (list.get(i).ConsumeMonth == month) {
                list1.add(list.get(i));
            } else {
                List<InvoiceOrder> list2 = new ArrayList<>();
                list2.addAll(list1);
                child.add(list2);
                listStr.add(list.get(i).ConsumeMonth+"月");

                list1.clear();
                list1.add(list.get(i));
                month = list.get(i).ConsumeMonth;
            }
        }
        for (int j=0;j<listStr.size();j++){
            double money = 0;
            InvoiceOrderGroup orderGroup = new InvoiceOrderGroup();
            for (InvoiceOrder order1:child.get(j)){
                money = addMoney(money, order1.ConsumeCash);
            }
            orderGroup.ConsumeMonth = listStr.get(j);
            orderGroup.money = money;
            orderGroup.isChecked = false;
            group.add(orderGroup);
        }
        dataChanged();
    }
    //double相加计算
    private double addMoney(double money, double money1) {
        BigDecimal decimal = new BigDecimal(Double.toString(money));
        BigDecimal b2 = new BigDecimal(Double.toString(money1));
        money = decimal.add(b2).doubleValue();
        return money;
    }
    //double相减计算
    private double subMoney(double money, double money1) {
        BigDecimal decimal = new BigDecimal(Double.toString(money));
        BigDecimal b2 = new BigDecimal(Double.toString(money1));
        money = decimal.subtract(b2).doubleValue();
        return money;
    }

    private void dataChanged() {
        if (adapter == null) {
            adapter = new InvoiceAdapter(this, child, group);
            adapter.setOnChecked(this);
            listView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.invoice_tvNext:
                Bundle bundle = new Bundle();
                bundle.putDouble("money",allMoney);
                skipActivity(InvoiceTypeActivity.class,bundle);
                break;
            case R.id.title_tvRight:

                break;
        }
    }

    @Override
    public void onCheck(int position) {
        if (group.get(position).isChecked){
            group.get(position).isChecked = false;
            for (int i = 0;i<child.get(position).size();i++){
                child.get(position).get(i).isChecked = false;
            }
            allMoney = subMoney(allMoney,group.get(position).money);
        }
        else{
            group.get(position).isChecked = true;
            for (int i = 0;i<child.get(position).size();i++){
                child.get(position).get(i).isChecked = true;
            }
            allMoney = addMoney(allMoney,group.get(position).money);
        }
        dataChanged();
        tvAllMoney.setText("￥"+allMoney);
    }
}

