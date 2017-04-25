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
import java.util.List;

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
    private ArrayList<BillBean> mData=new ArrayList<>();
    private WalletBillAdapter mAdapter;
    private int pageIndex=1;
    private int pageSize=10;
    private boolean frist=true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_bill);
        ButterKnife.bind(this);
        initViews();
        showLoading();
        initData();
        initListener();
    }

    private void initListener() {

    }

    private void initData() {

        GetMineInfoManager.getTransactionBill(pageIndex, pageSize, new ManagerCallback<List<BillBean>>() {// index 从1开始
            @Override
            public void onSuccess(List<BillBean> result) {
                LogUtil.i("test==getTransactionBill onSuccess "+result);

                if (1==pageIndex){
                    mData.clear();
                }
                if (result != null && result.size() < pageSize)
                    mRefreshLayout.setRefreshCompleted(false);
                else
                    mRefreshLayout.setRefreshCompleted(true);
                mData.addAll(result);
                mAdapter.notifyDataSetChanged();
                if (frist){
                    frist=false;
                    closeLoading();
                }
            }

            @Override
            public void onFailure(String msg) {
                showToast(msg);
                mRefreshLayout.setRefreshCompleted(true);
                if (frist){
                    frist=false;
                    closeLoading();
                }
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

        mAdapter = new WalletBillAdapter(R.layout.item_bill_list,mData,WalletBillAct.this);
        HTBaseViewHolder viewHolder = new HTDefaultVerticalRefreshViewHolder(this);
        viewHolder.setRefreshViewBackgroundResId(R.color.foreground_material_dark);
        mRefreshLayout.setRefreshViewHolder(viewHolder);//不设置样式,则使用默认箭头样式
        mRefreshLayout.setLayoutManager(new LinearLayoutManager(this));//设置列表布局方式
        mRefreshLayout.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        mRefreshLayout.setAdapter(mAdapter);//设置数据源
        mRefreshLayout.setOnLoadMoreListener(this);//实现OnLoadMoreListener接口
        mRefreshLayout.setOnRefreshListener(this);//实现OnRefreshListener接口
        mRefreshLayout.setLoadMoreViewShow(false);
        mRefreshLayout.setEnableScrollOnRefresh(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onRefresh() {
        pageIndex=1;
        initData();
    }

    @Override
    public void onLoadMore() {
        pageIndex++;
        initData();
    }

    @Override
    public void onItemClickListener(Object item,int position) {
        Intent intent = new Intent(WalletBillAct.this,WalletBillDetailAct.class);
        Bundle bound=new Bundle();
        bound.putSerializable("BillBean",(BillBean)item);
        intent.putExtras(bound);
        startActivity(intent);

    }
}
