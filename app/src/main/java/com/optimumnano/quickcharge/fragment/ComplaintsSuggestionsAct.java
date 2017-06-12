package com.optimumnano.quickcharge.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.base.BaseActivity;

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
                String str = phone_number.getText().toString();
                //用intent启动拨打电话
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + str));
                startActivity(intent);
            }
        });
    }
}
