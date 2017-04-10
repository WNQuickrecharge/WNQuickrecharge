package com.optimumnano.quickcharge.activity.mineinfo;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;

import com.netease.hearttouch.htrefreshrecyclerview.HTLoadMoreListener;
import com.netease.hearttouch.htrefreshrecyclerview.HTRefreshListener;
import com.netease.hearttouch.htrefreshrecyclerview.HTRefreshRecyclerView;
import com.netease.hearttouch.htrefreshrecyclerview.base.HTBaseViewHolder;
import com.netease.hearttouch.htrefreshrecyclerview.viewimpl.HTDefaultVerticalRefreshViewHolder;
import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.adapter.MyMessageAdapter;
import com.optimumnano.quickcharge.base.BaseActivity;
import com.optimumnano.quickcharge.bean.MessageBean;
import com.optimumnano.quickcharge.manager.GetMineInfoManager;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者：邓传亮 on 2017/4/8 16:47
 * <p>
 * 邮箱：dengchuanliang@optimumchina.com
 */
public class MyMessageAct extends BaseActivity implements HTRefreshListener, HTLoadMoreListener {
    @Bind(R.id.act_mine_message_rv)
    HTRefreshRecyclerView mRefreshLayout;
    private GetMineInfoManager mManager;
    private ArrayList<MessageBean> mData;
    private MyMessageAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_message);
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
        for (int i = 0; i < 30; i++) {
            MessageBean messageBean = new MessageBean();
            messageBean.content=i+"content";
            messageBean.time=i+"time";
            messageBean.type=i+"type";
            mData.add(messageBean);
        }
    }

    @Override
    public void initViews() {
        super.initViews();
        setRightTitle("");
        setTitle("消息");
        initRefreshView();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void initRefreshView() {

        mAdapter = new MyMessageAdapter(R.layout.item_message_list,mData);
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
}
