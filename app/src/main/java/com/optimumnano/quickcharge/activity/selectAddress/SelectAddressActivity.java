package com.optimumnano.quickcharge.activity.selectAddress;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;

import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SelectAddressActivity extends BaseActivity {

    @Bind(R.id.et_record_number)
    EditText etRecordNumber;
    @Bind(R.id.rv_sug)
    RecyclerView rvSug;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_address);
        ButterKnife.bind(this);
    }
}
