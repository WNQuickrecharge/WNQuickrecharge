package com.optimumnano.quickcharge.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.activity.order.OrderActivity;
import com.optimumnano.quickcharge.bean.PileBean;

import java.util.List;

/**
 * Created by mfwn on 2017/4/11.
 */

public class StationPilesAdapter extends BaseQuickAdapter<PileBean,BaseViewHolder> {
    public static final int PROMPTLY_CHARGE=1;//立即充电
    public static final int CHARGEING=2;       //充电中
    public static final int MAINTAIN=3;         //维护
    public static final int APPOINTMENT=4;      //预约
    private boolean isChargeing=true;
    private boolean isMaintain=true;

    private Activity activity;
    public StationPilesAdapter(int layoutResId, List<PileBean> data,Activity context) {
        super(layoutResId, data);
        activity=context;
    }

    @Override
    protected void convert(BaseViewHolder helper, final PileBean item) {
        TextView eletricPrice=helper.getView(R.id.tv_electric_price);
        TextView servicePrice=helper.getView(R.id.tv_service_price);
        Button pileStatus=helper.getView(R.id.pile_status_operation);
        TextView pileNo=helper.getView(R.id.tv_station_item_pileNo);
        LinearLayout linearLayout=helper.getView(R.id.ll_gunList);
        ImageView imageView=helper.getView(R.id.gun_status_img);

        for (int i=0;i<item.getGunList().size();i++) {
            View inflate = activity.getLayoutInflater().inflate(R.layout.itemview_station_pile_gun, null);
            TextView gunNumber = (TextView) inflate.findViewById(R.id.tv_station_gun_number);
            gunNumber.setText(item.getGunList().get(i).getGun_code());
            linearLayout.addView(inflate);

        }
        if (item.getGunList().size()>0) {
            eletricPrice.setText(Html.fromHtml(activity.getString(R.string.station_electric_price,"¥ "+item.getGunList().get(0).max_price+"/kw.h")));
            servicePrice.setText(Html.fromHtml(activity.getString(R.string.station_service_price,"¥ "+item.getGunList().get(0).max_service+"/kw.h")));
            pileNo.setText(item.getGunList().get(0).getPileNo());
            for (int i=0;i<item.getGunList().size();i++){
                if (!(item.getGunList().get(i).getGunStatus()==CHARGEING)) {//所有的枪处于正在充电,桩才是正在充电状态
                    isChargeing=false;
                }else if (!(item.getGunList().get(i).getGunStatus()==MAINTAIN)){
                    isMaintain=false;
                }
            }
            if (isChargeing){
                item.setStatus(CHARGEING);
            }
            if (isMaintain){
                item.setStatus(MAINTAIN);
            }

            for (int i=0;i<item.getGunList().size();i++){
                if (item.getGunList().get(i).getGunStatus()==PROMPTLY_CHARGE) {
                    item.setStatus(PROMPTLY_CHARGE);
                    break;
                }
            }
        }
        int status = item.getStatus();
        switch (status) {
            case PROMPTLY_CHARGE:
                pileStatus.setText("立即充电");
                pileStatus.setBackgroundColor(activity.getResources().getColor(R.color.main_color));
                pileStatus.setTextColor(activity.getResources().getColor(R.color.white));
                imageView.setImageResource(R.mipmap.cdzhuang01);
                pileStatus.setClickable(true);
                pileStatus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(activity, OrderActivity.class);
                        Bundle bundle=new Bundle();
//                        bundle.putString("gun_no",item.get);
//                        intent.putExtras(bundle);
//                        activity.startActivity();
                    }
                });
                break;

            case CHARGEING:
                pileStatus.setText("正在充电");
                pileStatus.setBackgroundColor(activity.getResources().getColor(R.color.white));
                pileStatus.setTextColor(activity.getResources().getColor(R.color.color99));
                imageView.setImageResource(R.mipmap.cdzhuang02);
                pileStatus.setClickable(false);
                break;
            case MAINTAIN:
                pileStatus.setText("正在维护");
                pileStatus.setBackgroundColor(activity.getResources().getColor(R.color.white));
                pileStatus.setTextColor(activity.getResources().getColor(R.color.colorCC));
                imageView.setImageResource(R.mipmap.cdzhuang03);
                pileStatus.setClickable(false);
                break;
            default:
                break;
        }



    }
}
