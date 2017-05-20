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
import com.optimumnano.quickcharge.bean.GunBean;
import com.optimumnano.quickcharge.bean.PileBean;
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
    private boolean isChargeing = true;
    private boolean isMaintain = true;
    private boolean isNetWorkOutTime = true;

    private Context context;

    private int mGetGunInfoTaskId;
    private TaskDispatcher mTaskDispatcher;
    private boolean mActive;

    private String finalFristFreeGunNo;
    private GunBean finalFirstFreeGun;

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
        TextView pileStatus = helper.getView(R.id.pile_status_operation);
        TextView pileNo = helper.getView(R.id.tv_station_item_pileNo);
        LinearLayout linearLayout = helper.getView(R.id.ll_gunList);
        ImageView imageView = helper.getView(R.id.gun_status_img);
        String fristFreeGunNo = "";
        String firstFreePileNo = "";
        GunBean firstFreeGun = null;
        for (int i = 0; i < item.getGunList().size(); i++) {
            View inflate = LayoutInflater.from(context).inflate(R.layout.itemview_station_pile_gun, null);
            TextView gunNumber = (TextView) inflate.findViewById(R.id.tv_station_gun_number);
            gunNumber.setText(item.getGunList().get(i).getGun_code());
            linearLayout.addView(inflate);

        }
        if (item.getGunList().size() > 0) {
            eletricPrice.setText(Html.fromHtml(context.getString(R.string.station_electric_price, "¥ " + item.getGunList().get(0).max_price + "/kw.h")));
            servicePrice.setText(Html.fromHtml(context.getString(R.string.station_service_price, "¥ " + item.getGunList().get(0).max_service + "/kw.h")));
            pileNo.setText(item.getGunList().get(0).getPileNo());
            for (int i = 0; i < item.getGunList().size(); i++) {
                int gunStatus = item.getGunList().get(i).getGunStatus();
                if (gunStatus != CHARGEING) {//所有的枪处于正在充电,桩才是正在充电状态
                    isChargeing = false;
                }
                if (gunStatus != MAINTAIN) {
                    isMaintain = false;
                }
                if (gunStatus != NETWORK_OUTTIME) {
                    isNetWorkOutTime = false;
                }
            }
            if (isChargeing) {
                item.setStatus(CHARGEING);
            }
            if (isMaintain || isNetWorkOutTime) {
                item.setStatus(MAINTAIN);
            }

            for (int i = 0; i < item.getGunList().size(); i++) {
                GunBean gunBean = item.getGunList().get(i);
                if (gunBean.getGunStatus() == PROMPTLY_CHARGE) {
                    fristFreeGunNo = gunBean.getGun_code();
                    firstFreeGun = gunBean;
                    firstFreePileNo = gunBean.getPileNo();

                    item.setStatus(PROMPTLY_CHARGE);
                    break;
                }
            }
        }
        int status = item.getStatus();
        switch (status) {
            case PROMPTLY_CHARGE:
                pileStatus.setText(R.string.charge_soon);
                pileStatus.setTextColor(context.getResources().getColor(R.color.white));
                imageView.setImageResource(R.mipmap.cdzhuang01);
                pileStatus.setClickable(true);
                finalFristFreeGunNo = fristFreeGunNo;
                finalFirstFreeGun = firstFreeGun;

                final String finalFirstFreePileNo = firstFreePileNo;
                final String finalFristFreeGunNo1 = fristFreeGunNo;
                pileStatus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        OrderManager.getGunInfo(item.getPileNo(), new ManagerCallback() {
//                            @Override
//                            public void onSuccess(Object returnContent) {
//                                super.onSuccess(returnContent);
//                                Intent intent = new Intent(context, OrderActivity.class);
//                                Bundle bundle = new Bundle();
//                                bundle.putString("gun_no", finalFristFreeGunNo);
//                                bundle.putSerializable("gunBean", finalFirstFreeGun);
//                                intent.putExtras(bundle);
//                                context.startActivity(intent);
//                            }
//
//                            @Override
//                            public void onFailure(String msg) {
//                                super.onFailure(msg);
//                                ToastUtil.showToast(context, context.getString(R.string.get_gun_info_fail));
//                            }
//                        });

                        if (!Tool.isConnectingToInternet()) {
                            ToastUtil.showToast(context, "无网络");
                            return;
                        }
                        mGetGunInfoTaskId = TaskIdGenFactory.gen();
                        mTaskDispatcher.dispatch(new HttpTask(mGetGunInfoTaskId,
                                new GetGunInfoRequest(new GetGunInfoResult(context),
                                        finalFirstFreePileNo + finalFristFreeGunNo1), (HttpCallback) StationPilesAdapter.this));
                    }
                });

                break;

            case CHARGEING:
                pileStatus.setText(R.string.charging);
                pileStatus.setBackgroundColor(context.getResources().getColor(R.color.white));
                pileStatus.setTextColor(context.getResources().getColor(R.color.color99));
                imageView.setImageResource(R.mipmap.cdzhuang02);
                pileStatus.setClickable(false);
                break;
            case MAINTAIN:
                pileStatus.setText(R.string.maintaining);
                pileStatus.setBackgroundColor(context.getResources().getColor(R.color.white));
                pileStatus.setTextColor(context.getResources().getColor(R.color.colorCC));
                imageView.setImageResource(R.mipmap.cdzhuang03);
                pileStatus.setClickable(false);
                break;
            default:
                break;
        }

    }

    @Override
    public void onRequestCancel(int id) {

    }

    @Override
    public void onRequestFail(int id, BaseResult result) {
        if (!mActive) {
            return;
        }
        ToastUtil.showToast(context, context.getString(R.string.get_gun_info_fail));
    }

    @Override
    public void onRequestSuccess(int id, BaseResult result) {
        if (!mActive) {
            return;
        }
        Intent intent = new Intent(context, OrderActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("gun_no", finalFristFreeGunNo);
        bundle.putSerializable("gunBean", finalFirstFreeGun);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
}
