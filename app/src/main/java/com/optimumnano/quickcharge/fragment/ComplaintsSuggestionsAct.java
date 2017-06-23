package com.optimumnano.quickcharge.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.base.BaseActivity;
import com.optimumnano.quickcharge.utils.ToastUtil;

/**
 * 投诉与建议页面
 * Created by zhangjiancheng on 2017/6/5.
 */

public class ComplaintsSuggestionsAct extends BaseActivity {

    private TextView phone_number;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaints_suggestions);
        initViews();
    }

    @Override
    public void initViews() {
        super.initViews();
        setTitle("投诉与建议");
        phone_number = (TextView) findViewById(R.id.phone_number);
        phone_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String str = phone_number.getText().toString();
//                //用intent启动拨打电话
//                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + str));
//                startActivity(intent);
                showOrderEvaluateDailog();

            }

            private void showOrderEvaluateDailog() {
                AlertDialog.Builder builder = new AlertDialog.Builder(ComplaintsSuggestionsAct.this);
                final AlertDialog dialog = builder.create();

                View view = View.inflate(ComplaintsSuggestionsAct.this, R.layout.dialog_order_evaluation, null);
                // dialog.setView(view);// 将自定义的布局文件设置给dialog
                dialog.setView(view, 20, 20, 20, 20);// 设置边距为0,保证在2.x的版本上运行没问题

                final RatingBar oneBar = (RatingBar) view.findViewById(R.id.one);
                final RatingBar twoBar = (RatingBar) view.findViewById(R.id.two);
                RatingBar threeBar = (RatingBar) view.findViewById(R.id.three);
                RatingBar fourBar = (RatingBar) view.findViewById(R.id.four);
                TextView canncel = (TextView) view.findViewById(R.id.evaluate_canncel);
                EditText input = (EditText) view.findViewById(R.id.editText);
                TextView submit = (TextView) view.findViewById(R.id.submit_evaluate);

                oneBar.setIsIndicator(false);
                twoBar.setIsIndicator(false);
                threeBar.setIsIndicator(false);
                fourBar.setIsIndicator(false);

                canncel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        float one = oneBar.getRating();
                        float two = twoBar.getRating();
                        showToast("提交评价" + one + ":" + two);

                        dialog.dismiss();
                    }
                });
                oneBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                        showToast("正在改变...");
                    }
                });
                dialog.show();
            }
        });
    }
}
