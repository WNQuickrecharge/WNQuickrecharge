package com.optimumnano.quickcharge.activity.mineinfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;

import com.netease.hearttouch.htrefreshrecyclerview.HTLoadMoreListener;
import com.netease.hearttouch.htrefreshrecyclerview.HTRefreshListener;
import com.netease.hearttouch.htrefreshrecyclerview.HTRefreshRecyclerView;
import com.netease.hearttouch.htrefreshrecyclerview.base.HTBaseViewHolder;
import com.netease.hearttouch.htrefreshrecyclerview.viewimpl.HTDefaultVerticalRefreshViewHolder;
import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.adapter.OnListItemClickListener;
import com.optimumnano.quickcharge.adapter.WalletBillAdapter;
import com.optimumnano.quickcharge.base.BaseActivity;
import com.optimumnano.quickcharge.bean.BillBean;
import com.optimumnano.quickcharge.manager.GetMineInfoManager;
import com.optimumnano.quickcharge.net.ManagerCallback;

import org.xutils.common.util.LogUtil;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者：邓传亮 on 2017/4/7 15:47
 * <p>
 * 邮箱：dengchuanliang@optimumchina.com
 */
public class WalletBillAct extends BaseActivity implements HTRefreshListener, HTLoadMoreListener,OnListItemClickListener {
    @Bind(R.id.act_wattet_bill_rv)
    HTRefreshRecyclerView mRefreshLayout;
    private GetMineInfoManager mManager;
    private ArrayList<BillBean> mData;
    private WalletBillAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_bill);
        ButterKnife.bind(this);
        initData();
        initViews();
        initListener();
    }

    private void initListener() {

    }

    private void initData() {
        mManager = new GetMineInfoManager();

        mData = new ArrayList();
        double f=0d;
        for (int i = 0; i < 30; i++) {
            BillBean bean = new BillBean();
            bean.amount=100.00d+i*900+f;
            f=f+0.09;
            mData.add(bean);
        }

        GetMineInfoManager.getTransactionBill(1, 10, new ManagerCallback() {
            @Override
            public void onSuccess(Object returnContent) {
                showToast("获取成功");

                LogUtil.i("test==getTransactionBill onSuccess "+returnContent);
            }

            @Override
            public void onFailure(String msg) {
                showToast("获取失败");
                LogUtil.i("test==getTransactionBill onFailure "+msg);
            }
        });
    }

    @Override
    public void initViews() {
        super.initViews();
        setRightTitle("");
        showBack();
        setTitle("交易明细");
        initRefreshView();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void initRefreshView() {

        mAdapter = new WalletBillAdapter(R.layout.item_bill_list,mData,this);
        HTBaseViewHolder viewHolder = new HTDefaultVerticalRefreshViewHolder(this);
        viewHolder.setRefreshViewBackgroundResId(R.color.foreground_material_dark);
        mRefreshLayout.setRefreshViewHolder(viewHolder);//不设置样式,则使用默认箭头样式
        mRefreshLayout.setLayoutManager(new LinearLayoutManager(this));//设置列表布局方式
        mRefreshLayout.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        mRefreshLayout.setAdapter(mAdapter);//设置数据源
        mRefreshLayout.setOnLoadMoreListener(this);//实现OnLoadMoreListener接口
        mRefreshLayout.setOnRefreshListener(this);//实现OnRefreshListener接口
        mRefreshLayout.setLoadMoreViewShow(true);
        mRefreshLayout.setEnableScrollOnRefresh(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onRefresh() {
        mAdapter.setNewData(mData);
        mRefreshLayout.setRefreshCompleted(false);
    }

    @Override
    public void onLoadMore() {
        mRefreshLayout.setRefreshCompleted(false);
    }

    @Override
    public void onItemClickListener(Object item,int position) {
        showToast(((BillBean)item).amount+"   "+position);
        Intent intent = new Intent(WalletBillAct.this,WalletBillDetailAct.class);
        Bundle bound=new Bundle();
        bound.putSerializable("BillBean",(BillBean)item);
        intent.putExtras(bound);
        startActivity(intent);

    }
}
