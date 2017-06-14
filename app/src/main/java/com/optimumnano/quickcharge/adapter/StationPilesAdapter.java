package com.optimumnano.quickcharge.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.activity.order.OrderActivity;
import com.optimumnano.quickcharge.base.BaseActivity;
import com.optimumnano.quickcharge.bean.GunBean;
import com.optimumnano.quickcharge.bean.PileBean;
import com.optimumnano.quickcharge.bean.RechargeGunBean;
import com.optimumnano.quickcharge.http.BaseResult;
import com.optimumnano.quickcharge.http.HttpCallback;
import com.optimumnano.quickcharge.http.HttpTask;
import com.optimumnano.quickcharge.http.TaskDispatcher;
import com.optimumnano.quickcharge.http.TaskIdGenFactory;
import com.optimumnano.quickcharge.request.GetGunInfoRequest;
import com.optimumnano.quickcharge.response.GetGunInfoResult;
import com.optimumnano.quickcharge.utils.ToastUtil;
import com.optimumnano.quickcharge.utils.Tool;

import java.util.List;

/**
 * Created by mfwn on 2017/4/11.
 */

public class StationPilesAdapter extends BaseQuickAdapter<PileBean, BaseViewHolder> implements HttpCallback {
    public static final int PROMPTLY_CHARGE = 1;//立即充电
    public static final int CHARGEING = 2;       //充电中
    public static final int MAINTAIN = 3;         //维护
    public static final int APPOINTMENT = 4;      //预约
    public static final int NETWORK_OUTTIME = 5;         //网络断开
    public static final int GUN_STATUS_NULL = 0; //服务器返回的状态为null

    private Context context;
    private int mGetGunInfoTaskId;
    private TaskDispatcher mTaskDispatcher;
    private boolean mActive;
    private GunBean gunBean;
    private List<GunBean> gunList;

    public StationPilesAdapter(int layoutResId, List<PileBean> data, Context context) {
        super(layoutResId, data);
        this.context = context;
        mTaskDispatcher = TaskDispatcher.getInstance(context);
        mActive = true;
    }


    @Override
    public void onViewDetachedFromWindow(BaseViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        mActive = false;
        mTaskDispatcher.cancel(mGetGunInfoTaskId);
    }

    @Override
    protected void convert(BaseViewHolder helper, final PileBean item) {
        TextView eletricPrice = helper.getView(R.id.tv_electric_price);
        TextView servicePrice = helper.getView(R.id.tv_service_price);
        TextView pileNo = helper.getView(R.id.tv_station_item_pileNo);
        LinearLayout linearLayout = helper.getView(R.id.ll_gunList);
        gunList = item.getGunList();
        for (int i = 0; i < item.getGunList().size(); i++) {
            View inflate = LayoutInflater.from(context).inflate(R.layout.itemview_station_pile_gun, null);
            TextView gunNumber = (TextView) inflate.findViewById(R.id.tv_station_gun_number);
            ImageView ivStatus = (ImageView) inflate.findViewById(R.id.iv_station_gun_status);
            TextView tvOperation = (TextView) inflate.findViewById(R.id.tv_gun_operation);
            gunBean = gunList.get(i);
            showGunOperation(ivStatus, tvOperation,i);
            gunNumber.setText(item.getGunList().get(i).getGun_code());
            linearLayout.addView(inflate);

        }
        if (item.getGunList().size() > 0){
            eletricPrice.setText(Html.fromHtml(context.getString(R.string.station_electric_price, "¥ " + item.getGunList().get(0).max_price + "/kw.h")));
            servicePrice.setText(Html.fromHtml(context.getString(R.string.station_service_price, "¥ " + item.getGunList().get(0).max_service + "/kw.h")));
            pileNo.setText(item.getGunList().get(0).getPileNo());
        }

    }

    private void showGunOperation(ImageView ivStatus, TextView tvOperation, final int position) {
        int gunStatus = gunBean.getGunStatus();
        if (gunStatus == PROMPTLY_CHARGE){
            ivStatus.setImageResource(R.mipmap.icon_zhuang_1);
            tvOperation.setBackgroundResource(R.drawable.bg_gun_operation_1);
            tvOperation.setText("立即充电");
            tvOperation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!Tool.isConnectingToInternet()) {
                        ToastUtil.showToast(context, "无网络");
                        return;
                    }
                    ((BaseActivity) mContext).showLoading();
                    mGetGunInfoTaskId = TaskIdGenFactory.gen();
                    mTaskDispatcher.dispatch(new HttpTask(mGetGunInfoTaskId,
                            new GetGunInfoRequest(new GetGunInfoResult(context),
                                    gunBean.getPileNo() + gunList.get(position).getGun_code()), (HttpCallback) StationPilesAdapter.this));
                }
            });
        }else if (gunStatus == CHARGEING){
            ivStatus.setImageResource(R.mipmap.icon_zhuang_2);
            tvOperation.setBackgroundResource(R.drawable.bg_gun_operation_2);
            tvOperation.setTextColor(context.getResources().getColor(R.color.main_color));
            tvOperation.setText("充电中");
        }else if (gunStatus == MAINTAIN || gunStatus ==NETWORK_OUTTIME || gunStatus == GUN_STATUS_NULL){
            ivStatus.setImageResource(R.mipmap.icon_zhuang_3);
            tvOperation.setBackgroundResource(R.drawable.bg_gun_operation_3);
            tvOperation.setTextColor(context.getResources().getColor(R.color.colorCC));
            tvOperation.setText("正在维护");
        }
    }


    @Override
    public void onRequestCancel(int id) {

    }

    @Override
    public void onRequestFail(int id, BaseResult result) {
        ((BaseActivity) mContext).closeLoading();
        ToastUtil.showToast(context, context.getString(R.string.get_gun_info_fail));
        if (!mActive) {

            return;
        }
    }

    @Override
    public void onRequestSuccess(int id, BaseResult result) {
        ((BaseActivity) mContext).closeLoading();
        if (!mActive) {
            return;
        }
        RechargeGunBean resultGunBean = ((GetGunInfoResult) result).getResp().getResult();
        Intent intent = new Intent(context, OrderActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("gunNo", gunBean.getPileNo()+gunBean.getGun_code());
        bundle.putSerializable("gunBean", resultGunBean);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
}
