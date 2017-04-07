package com.optimumnano.quickcharge.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.base.BaseActivity;
import com.optimumnano.quickcharge.manager.ModifyUserInformationManager;
import com.optimumnano.quickcharge.net.ManagerCallback;
import com.optimumnano.quickcharge.utils.MD5Utils;
import com.optimumnano.quickcharge.utils.SharedPreferencesUtil;

/**
 * Created by mfwn on 2017/4/6.
 */

public class ModifyPassword extends BaseActivity implements View.OnClickListener{
    private EditText oldPassword,newPassword,confirmPassword;
    private TextView confirmTextView;
    ModifyUserInformationManager manager= new ModifyUserInformationManager();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_password);
        initViews();
    }

    @Override
    public void initViews() {
        super.initViews();
        oldPassword= (EditText) findViewById(R.id.modify_password_old_password);
        newPassword= (EditText) findViewById(R.id.modify_password_new_Pwd);
        confirmPassword= (EditText) findViewById(R.id.modify_password_confirm_Pwd);
        confirmTextView= (TextView) findViewById(R.id.modify_password_confirm_Pwd_textView);
        setTitle("修改密码");
        setRightTitle("");
        confirmTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.modify_password_confirm_Pwd_textView){
            String oldPwd = oldPassword.getText().toString();
            String newPwd = newPassword.getText().toString();

            String confirmPwd = confirmPassword.getText().toString();
            if (!TextUtils.equals(newPwd,confirmPwd)){
                showToast("两次密码不一致,请重新输入");
                return;
            }
            String md5OldPwd = MD5Utils.encodeMD5(oldPwd);
            String md5NewPwd = MD5Utils.encodeMD5(newPwd);

            manager.modifyPassword(SharedPreferencesUtil.getValue("userInform","mobile",""),
                    md5OldPwd,md5NewPwd,new Manager());
        }
    }

    class Manager extends ManagerCallback{
        @Override
        public void onSuccess(Object returnContent) {
            super.onSuccess(returnContent);
            showToast("密码修改成功");
        }

        @Override
        public void onFailure(String msg) {
            super.onFailure(msg);
            showToast(msg);
        }
    }
}
