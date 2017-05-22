package com.optimumnano.quickcharge.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.optimumnano.quickcharge.R;

/**
 * Created by chenwenguang on 2017/5/21.
 */

public class SimpleDialog extends Dialog {
    public SimpleDialog(@NonNull Context context) {
        super(context);
    }

    private TextView rightButton;//确定按钮
    private TextView leftButtuon;//取消按钮
//    private TextView titleTv;//消息标题文本
    private TextView messageTv;//消息提示文本
    private int messageTvColor;
    private String titleStr;//从外界设置的title文本
    private String messageStr;//从外界设置的消息文本
    //确定文本和取消文本的显示内容
    private String yesStr, noStr;

    private onNoOnclickListener noOnclickListener;//取消按钮被点击了的监听器
    private onYesOnclickListener yesOnclickListener;//确定按钮被点击了的监听器

    private boolean created = false;

    /**
     * 设置取消按钮的显示内容和监听
     *
     * @param str
     * @param onNoOnclickListener
     */
    public void setNoOnclickListener(String str, onNoOnclickListener onNoOnclickListener) {
        noStr = str;
        if (created) {
            leftButtuon.setText(str);
        }
        this.noOnclickListener = onNoOnclickListener;
    }

    /**
     * 设置确定按钮的显示内容和监听
     *
     * @param str
     * @param onYesOnclickListener
     */
    public void setYesOnclickListener(String str, onYesOnclickListener onYesOnclickListener) {
        yesStr = str;
        if (created) {
            rightButton.setText(str);
        }
        this.yesOnclickListener = onYesOnclickListener;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_dialog_simple_style);
        //按空白处不能取消动画
        //setCanceledOnTouchOutside(false);

        //初始化界面控件
        initView();
        //初始化界面数据
        initData();
        //初始化界面控件的事件
        initEvent();
        created = true;
    }

    /**
     * 初始化界面的确定和取消监听器
     */
    private void initEvent() {
        //设置确定按钮被点击后，向外界提供监听
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (yesOnclickListener != null) {
                    yesOnclickListener.onYesClick();
                }
            }
        });
        //设置取消按钮被点击后，向外界提供监听
        leftButtuon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (noOnclickListener != null) {
                    noOnclickListener.onNoClick();
                }
            }
        });
    }

    /**
     * 初始化界面控件的显示数据
     */
    private void initData() {
        //如果用户自定了title和message
//        if (titleStr != null) {
//            titleTv.setText(titleStr);
//        }
        if (messageStr != null) {
            messageTv.setText(messageStr);
        }
        //如果设置按钮的文字
        if (yesStr != null) {
            rightButton.setText(yesStr);
        }
        if (noStr != null) {
            leftButtuon.setText(noStr);
        }
    }

    /**
     * 初始化界面控件
     */
    private void initView() {
        rightButton = (TextView) findViewById(R.id.tv_confirm_cancel_ask);
        leftButtuon = (TextView) findViewById(R.id.right_delete);
//        titleTv = (TextView) findViewById(R.id.title);
        messageTv = (TextView) findViewById(R.id.message_content);
    }

    public void setTitle(String title) {
        titleStr = title;
//        if (created) {
//            titleTv.setText(title);
//        }
    }

    public void setMessage(String message) {
        messageStr = message;
        if (created) {
            messageTv.setText(messageStr);
        }
    }

    /**
     * 从外界Activity为Dialog设置dialog的message
     *
     * @param message
     */
    public void setMessage(String message, @ColorInt int color) {
        messageStr = message;
        if (created) {
            messageTv.setText(messageStr);
            messageTv.setTextColor(color);
        }
    }

    /**
     * 设置确定按钮和取消被点击的接口
     */
    public interface onYesOnclickListener {
        void onYesClick();
    }

    public interface onNoOnclickListener {
        void onNoClick();
    }
}
