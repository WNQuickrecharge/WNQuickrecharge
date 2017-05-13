package com.optimumnano.quickcharge.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.netease.hearttouch.htrefreshrecyclerview.HTLoadMoreListener;
import com.netease.hearttouch.htrefreshrecyclerview.HTRefreshListener;
import com.netease.hearttouch.htrefreshrecyclerview.HTRefreshRecyclerView;
import com.netease.hearttouch.htrefreshrecyclerview.base.HTBaseViewHolder;
import com.netease.hearttouch.htrefreshrecyclerview.viewimpl.HTDefaultVerticalRefreshViewHolder;
import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.activity.order.OrderlistDetailActivity;
import com.optimumnano.quickcharge.activity.order.OrderlistDetailtwoActivity;
import com.optimumnano.quickcharge.adapter.OrderAdapter;
import com.optimumnano.quickcharge.base.BaseFragment;
import com.optimumnano.quickcharge.bean.OrderBean;
import com.optimumnano.quickcharge.listener.MyOnitemClickListener;
import com.optimumnano.quickcharge.manager.OrderManager;
import com.optimumnano.quickcharge.net.ManagerCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单
 */
public class OrderFragment extends BaseFragment implements View.OnClickListener, HTLoadMoreListener, HTRefreshListener {
    private View mainView;
    HTRefreshRecyclerView recyclerView;
    private Context ctx;

    private OrderAdapter adapter;
    private List<OrderBean> orderList = new ArrayList<>();
    private OrderManager orderManager = new OrderManager();

    private int pageSize = 1;//当前页
    private int pageCount=10;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_order, container, false);
        ctx = getActivity();
        return mainView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
//        dataChanged();
    }

    private void initViews() {
        recyclerView = (HTRefreshRecyclerView) getActivity().findViewById(R.id.order_recyclerView);

        adapter = new OrderAdapter(R.layout.adapter_order,orderList);
        HTBaseViewHolder viewHolder = new HTDefaultVerticalRefreshViewHolder(getActivity());
        viewHolder.setRefreshViewBackgroundResId(R.color.foreground_material_dark);
        recyclerView.setRefreshViewHolder(viewHolder);//不设置样式,则使用默认箭头样式
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));//设置列表布局方式
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);//设置数据源
        recyclerView.setOnLoadMoreListener(this);//实现OnLoadMoreListener接口
        recyclerView.setOnRefreshListener(this);//实现OnRefreshListener接口
        recyclerView.setLoadMoreViewShow(false);
        recyclerView.setEnableScrollOnRefresh(true);

        adapter.setContext(getActivity());
        adapter.setOnitemClickListener(new MyOnitemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                OrderBean orderBean = orderList.get(position);
                Intent intent;
                if (orderBean.order_status==2 || orderBean.order_status==4 ||
                        orderBean.order_status == 1 || orderBean.order_status == 3){
                    intent = new Intent(getActivity(), OrderlistDetailActivity.class);
                }
                else {
                    intent = new Intent(getActivity(), OrderlistDetailtwoActivity.class);
                }

                Bundle bundle = new Bundle();
                bundle.putSerializable("orderbean",orderList.get(position));
                intent.putExtras(bundle);
                getActivity().startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        //initData();
        recyclerView.startAutoRefresh();
    }

    private void initData(){
        orderManager.getAllOrderlist(pageSize, pageCount, new ManagerCallback<List<OrderBean>>() {
            @Override
            public void onSuccess(List<OrderBean> returnContent) {
                super.onSuccess(returnContent);

                if (pageSize == 1){
                    pageSize = 1;
                    orderList.clear();
                }
                if (returnContent.size() < pageCount){
                    recyclerView.setRefreshCompleted(false);
                }else {
                    recyclerView.setRefreshCompleted(true);
                }
                orderList.addAll(returnContent);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(String msg) {
                super.onFailure(msg);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            //订单支付
            case R.id.order_tvPay:

                break;
        }
    }

    //下拉刷新
    @Override
    public void onRefresh() {
        pageSize = 1;

        initData();
    }
    //上拉加载
    @Override
    public void onLoadMore() {
        pageSize ++;
        initData();
    }

    @Override
    protected void lazyLoad() {
        initData();
    }
}
