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
import com.optimumnano.quickcharge.manager.OrderManager;
import com.optimumnano.quickcharge.net.ManagerCallback;
import com.optimumnano.quickcharge.utils.ToastUtil;

import java.util.List;

/**
 * Created by mfwn on 2017/4/11.
 */

public class StationPilesAdapter extends BaseQuickAdapter<PileBean, BaseViewHolder> {
    public static final int PROMPTLY_CHARGE = 1;//立即充电
    public static final int CHARGEING = 2;       //充电中
    public static final int MAINTAIN = 3;         //维护
    public static final int APPOINTMENT = 4;      //预约
    private boolean isChargeing = true;
    private boolean isMaintain = true;

    private Context context;

    public StationPilesAdapter(int layoutResId, List<PileBean> data, Context context) {
        super(layoutResId, data);
        this.context = context;
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
        GunBean firstFreeGun=null;
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
                if (!(item.getGunList().get(i).getGunStatus() == CHARGEING)) {//所有的枪处于正在充电,桩才是正在充电状态
                    isChargeing = false;
                } else if (!(item.getGunList().get(i).getGunStatus() == MAINTAIN)) {
                    isMaintain = false;
                }
            }
            if (isChargeing) {
                item.setStatus(CHARGEING);
            }
            if (isMaintain) {
                item.setStatus(MAINTAIN);
            }

            for (int i = 0; i < item.getGunList().size(); i++) {
                if (item.getGunList().get(i).getGunStatus() == PROMPTLY_CHARGE) {
                    fristFreeGunNo = item.getGunList().get(i).getGun_code();
                    firstFreeGun=item.getGunList().get(i);
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
                final String finalFristFreeGunNo = fristFreeGunNo;
                final GunBean finalFirstFreeGun = firstFreeGun;
                pileStatus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OrderManager.getGunInfo(item.getPileNo(), new ManagerCallback() {
                            @Override
                            public void onSuccess(Object returnContent) {
                                super.onSuccess(returnContent);
                                Intent intent = new Intent(context, OrderActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("gun_no", finalFristFreeGunNo);
                                bundle.putSerializable("gunBean", finalFirstFreeGun);
                                intent.putExtras(bundle);
                                context.startActivity(intent);
                            }

                            @Override
                            public void onFailure(String msg) {
                                super.onFailure(msg);
                                ToastUtil.showToast(context, context.getString(R.string.get_gun_info_fail));
                            }
                        });

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
}
