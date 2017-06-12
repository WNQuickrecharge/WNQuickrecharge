package com.optimumnano.quickcharge.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.optimumnano.quickcharge.dialog.MyDialog;
import com.optimumnano.quickcharge.http.BaseResult;
import com.optimumnano.quickcharge.http.HttpCallback;
import com.optimumnano.quickcharge.http.HttpTask;
import com.optimumnano.quickcharge.http.TaskIdGenFactory;
import com.optimumnano.quickcharge.listener.MyDeleteOrderClicListener;
import com.optimumnano.quickcharge.listener.MyOnitemClickListener;
import com.optimumnano.quickcharge.request.DeleteOrderRequest;
import com.optimumnano.quickcharge.request.GetOrderListRequest;
import com.optimumnano.quickcharge.response.DeleteOrderResult;
import com.optimumnano.quickcharge.response.GetOrderListResult;
import com.optimumnano.quickcharge.utils.ToastUtil;
import com.optimumnano.quickcharge.utils.Tool;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单
 */
public class OrderFragment extends BaseFragment implements View.OnClickListener, HTLoadMoreListener, HTRefreshListener, HttpCallback {
    private View mainView;
    HTRefreshRecyclerView recyclerView;
    private Context ctx;

    private MyDialog myDialog;
    private OrderAdapter adapter;
    private List<OrderBean> orderList = new ArrayList<>();

    private int pageSize = 1;//当前页
    private int pageCount = 10;

    private int mGetOrderListTaskId;
    private int mDeleteOrderTaskId;

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

    @Override
    public void onDetach() {
        super.onDetach();
        mTaskDispatcher.cancel(mGetOrderListTaskId);
        mTaskDispatcher.cancel(mDeleteOrderTaskId);
    }

    private void initViews() {
        recyclerView = (HTRefreshRecyclerView) getActivity().findViewById(R.id.order_recyclerView);

        adapter = new OrderAdapter(R.layout.adapter_order, orderList);
        HTBaseViewHolder viewHolder = new HTDefaultVerticalRefreshViewHolder(getActivity());
        viewHolder.setRefreshViewBackgroundResId(R.color.foreground_material_dark);
        recyclerView.setRefreshViewHolder(viewHolder);//不设置样式,则使用默认箭头样式
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));//设置列表布局方式
        //recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);//设置数据源
        recyclerView.setOnLoadMoreListener(this);//实现OnLoadMoreListener接口
        recyclerView.setOnRefreshListener(this);//实现OnRefreshListener接口
        recyclerView.setLoadMoreViewShow(false);
        recyclerView.setEnableScrollOnRefresh(true);

        myDialog = new MyDialog(ctx, R.style.MyDialog);
        myDialog.setCancelable(false);

        adapter.setContext(getActivity());
        /**
         * item点击事件，分类型跳转---->5目前等同于6状态
         * 已取消=1,
         * 待支付=2,
         * 待充电=3,
         * 充电中=4,
         * 待评价=5,
         * 已完成=6
         */
        adapter.setOnitemClickListener(new MyOnitemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                OrderBean orderBean = orderList.get(position);
                Intent intent;
                if (orderBean.order_status == 2 || orderBean.order_status == 4 ||
                        orderBean.order_status == 1 || orderBean.order_status == 3) {
                    intent = new Intent(getActivity(), OrderlistDetailActivity.class);
                } else {
                    intent = new Intent(getActivity(), OrderlistDetailtwoActivity.class);
                }

                Bundle bundle = new Bundle();
                bundle.putSerializable("orderbean", orderList.get(position));
                intent.putExtras(bundle);
                getActivity().startActivity(intent);
            }
        });
        /**
         * 删除订单
         */
        adapter.setDeleteOrderClicListener(new MyDeleteOrderClicListener() {
            @Override
            public void onDeleteItemClick(View view, final String str) {
                /**
                 * 删除订单弹出框
                 */
                myDialog.setTitle("确认是否删除");
                myDialog.setMessage("订单一旦删除将不可恢复，\n请确认是否删除。");
                myDialog.setYesOnclickListener("确认", new MyDialog.onYesOnclickListener() {
                    @Override
                    public void onYesClick() {
                        deleteOrder(str);
                        myDialog.dismiss();
                    }
                });
                myDialog.setNoOnclickListener("取消", new MyDialog.onNoOnclickListener() {
                    @Override
                    public void onNoClick() {
                        myDialog.dismiss();
                    }
                });

                myDialog.show();
            }
        });
    }

    /**
     * 删除订单
     *
     * @param str
     */
    private void deleteOrder(String str) {
        if (!Tool.isConnectingToInternet()) {
            ToastUtil.showToast(mContext, "无网络");
            return;
        }
        mDeleteOrderTaskId = TaskIdGenFactory.gen();
        mTaskDispatcher.dispatch(new HttpTask(mDeleteOrderTaskId,
                new DeleteOrderRequest(new DeleteOrderResult(mContext), str), this));
    }

    @Override
    public void onResume() {
        super.onResume();
        recyclerView.startAutoRefresh();
    }

    private void initData() {
//        orderManager.getAllOrderlist(pageSize, pageCount, new ManagerCallback<List<OrderBean>>() {
//            @Override
//            public void onSuccess(List<OrderBean> returnContent) {
//                super.onSuccess(returnContent);
//
//                if (pageSize == 1) {
//                    pageSize = 1;
//                    orderList.clear();
//                }
//                if (returnContent.size() < pageCount) {
//                    recyclerView.setRefreshCompleted(false);
//                } else {
//                    recyclerView.setRefreshCompleted(true);
//                }
//                orderList.addAll(returnContent);
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onFailure(String msg) {
//                super.onFailure(msg);
//            }
//        });
        if (!Tool.isConnectingToInternet()) {
            ToastUtil.showToast(getActivity(), "无网络!");
        }
        mGetOrderListTaskId = TaskIdGenFactory.gen();
        mTaskDispatcher.dispatch(new HttpTask(mGetOrderListTaskId,
                new GetOrderListRequest(new GetOrderListResult(mContext), pageCount, pageSize), this));

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
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
        pageSize++;
        initData();
    }

    @Override
    protected void lazyLoad() {
        initData();
    }


    @Override
    public void onRequestSuccess(int id, BaseResult result) {
        if (deAlive()) {
            return;
        }
        if (id == mGetOrderListTaskId) {
            if (pageSize == 1) {
                pageSize = 1;
                orderList.clear();
            }
            List<OrderBean> dataList = ((GetOrderListResult) result).getOrderListHttpResp().getResult();
            if (dataList.size() < pageCount) {
                recyclerView.setRefreshCompleted(false);
            } else {
                recyclerView.setRefreshCompleted(true);
            }
            orderList.addAll(dataList);
            adapter.notifyDataSetChanged();
        }
        if (mDeleteOrderTaskId == id) {
            ToastUtil.showToast(mContext, "删除成功");
            recyclerView.startAutoRefresh();
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onRequestFail(int id, BaseResult result) {
        if (deAlive()) {
            return;
        }
        ToastUtil.showToast(mContext, ToastUtil.formatToastText(mContext,
                ((GetOrderListResult) result).getOrderListHttpResp()));
        recyclerView.setRefreshCompleted(true);
    }

    @Override
    public void onRequestCancel(int id) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
