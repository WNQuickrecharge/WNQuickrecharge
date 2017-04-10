package com.optimumnano.quickcharge.popupWindow.showHelper;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.optimumnano.quickcharge.R;


/**
 * Created by 秋平 on 2015/12/2.
 */
public abstract class BaseShowHelper {
    public static final String SHOW_TYPE_CENTER = "show_center";
    public static final String SHOW_TYPE_BOTTOM = "show_bottom";
    public static final String SHOW_TYPE_VIEW = "show_view";

    protected Activity context;

    protected PopupWindow mPopupWindow;

    private boolean isNeedCloseGlim;

    public BaseShowHelper(Activity context) {
        this.context = context;
        initPopupWindow();
    }

    public BaseShowHelper(Activity context, boolean isNeedCloseGlim) {
        this.context = context;
        this.isNeedCloseGlim = isNeedCloseGlim;
        initPopupWindow();
    }

    protected void initPopupWindow() {
        mPopupWindow = onCreatePopupWindow();
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (!isNeedCloseGlim) {
                    WindowManager.LayoutParams lp = context.getWindow().getAttributes();
                    lp.alpha = 1.0f;
                    context.getWindow().setAttributes(lp);
                }
            }
        });
    }

    protected abstract PopupWindow onCreatePopupWindow();

    public void show(String type, View view) {
        if (mPopupWindow.isShowing())
            mPopupWindow.dismiss();
        if (type.equals(SHOW_TYPE_CENTER)) {
            showCenter();
        } else if (type.equals(SHOW_TYPE_VIEW)) {
            showView(view);
        } else if (type.equals(SHOW_TYPE_BOTTOM)) {
            showBottom();
        } else {
            showBottom();
        }
        if (!isNeedCloseGlim) {
            WindowManager.LayoutParams lp = context.getWindow().getAttributes();
            lp.alpha = .5f;
            context.getWindow().setAttributes(lp);
        }
    }

    public void dissmis() {
        mPopupWindow.dismiss();
    }

    protected void showCenter() {
        mPopupWindow.setAnimationStyle(R.style.anim_popup_fadeInOrOut);
        mPopupWindow.showAtLocation(getRootView(context), Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
    }


    protected void showBottom() {
        mPopupWindow.setAnimationStyle(R.style.anim_popup_dir);
        mPopupWindow.showAtLocation(getRootView(context), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    protected void showView(View view) {
        if (view != null) {
            mPopupWindow.setAnimationStyle(R.style.anim_popup_fadeOut_dir_in);
            mPopupWindow.showAsDropDown(view, 0, dpToPx(context, 0));
        } else {
            mPopupWindow.setAnimationStyle(R.style.anim_popup_fadeInOrOut);
            mPopupWindow.showAtLocation(getRootView(context), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        }
    }


    protected static View getRootView(Activity context) {
        return ((ViewGroup) context.findViewById(android.R.id.content)).getChildAt(0);
    }


    protected static int dpToPx(Context context, int dp) {
        return (int) (context.getResources().getDisplayMetrics().density * dp + 0.5f);
    }
}
