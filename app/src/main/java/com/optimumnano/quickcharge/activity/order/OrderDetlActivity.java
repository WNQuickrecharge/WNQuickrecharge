package com.optimumnano.quickcharge.activity.order;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.base.BaseActivity;
import com.optimumnano.quickcharge.dialog.CommentDialog;

public class OrderDetlActivity extends BaseActivity implements View.OnClickListener {
    private TextView tvConfirm ,tvComment;

    private CommentDialog dialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detl);
        initViews();
    }

    @Override
    public void initViews() {
        super.initViews();
        setTitle("订单详情");

        tvComment = (TextView) findViewById(R.id.orderdetl_tvComment);
        tvConfirm = (TextView) findViewById(R.id.orderdetl_tvConfirm);

        dialog = new CommentDialog(this);
        tvConfirm.setOnClickListener(this);
        tvComment.setOnClickListener(this);
        dialog.setConfirmListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.orderdetl_tvComment:
                dialog.show();
                break;
            case R.id.orderdetl_tvConfirm:

                break;
            case  R.id.dialog_comment_tvComment:
                showToast(dialog.getComment()+dialog.getRatingCorn());
                dialog.close();
                break;
            default:
                break;
        }
    }
}
