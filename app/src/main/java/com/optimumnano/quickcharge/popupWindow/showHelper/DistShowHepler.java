package com.optimumnano.quickcharge.popupWindow.showHelper;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.bean.Point;
import com.optimumnano.quickcharge.popupWindow.DistShowPopupWindow;

import java.util.List;

/**
 * Created by 秋平 on 2017/4/8 0008.
 */

public class DistShowHepler extends BaseShowHelper {

    DistShowPopupWindow mPopupWindow;


    public DistShowHepler(Activity context) {
        super(context,true);
    }

    public void setData(List<Point> mDatas) {
        mPopupWindow.setData(mDatas);
    }


    @Override
    protected PopupWindow onCreatePopupWindow() {
        View contentView = LayoutInflater.from(context).inflate(R.layout.popup_window_for_city, null);
        mPopupWindow = new DistShowPopupWindow(contentView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, null);
        return mPopupWindow;
    }

    public DistShowPopupWindow getmPopupWindow() {
        return mPopupWindow;
    }
}
