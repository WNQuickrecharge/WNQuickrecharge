package com.optimumnano.quickcharge.base;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.optimumnano.quickcharge.BuildConfig;
import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.data.PreferencesHelper;
import com.optimumnano.quickcharge.utils.AppManager;
import com.optimumnano.quickcharge.utils.ToastUtil;

import org.xutils.common.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ds on 2017/1/13.
 */
public class BaseActivity extends AppCompatActivity {
    private int REQUEST_CODE_PERMISSION = 0x00099;
    public TextView tvTitle,tvRight,tvLeft;
    public ImageView ivRight;
    public Toolbar toolbar;

    protected PreferencesHelper mHelper;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getAppManager().addActivity(this);
        mHelper=new PreferencesHelper(this);
    }
    public void initViews(){
        tvTitle = (TextView) findViewById(R.id.title_tvTitle);
        ivRight = (ImageView) findViewById(R.id.title_ivRight);
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        tvRight = (TextView) findViewById(R.id.title_tvRight);
        tvLeft = (TextView) findViewById(R.id.title_tvLeft);
        toolbar.setTitle("");
        toolbar.setTitleTextColor(getResources().getColor(R.color.main_color));
        toolbar.setTitleTextAppearance(this,R.style.Toolbar_TitleText);
        toolbar.setNavigationIcon(R.drawable.backtwo);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    public void setTitle(String title){
        tvTitle.setText(title);
    }
    public void setLeftTitle(String title){
//        toolbar.setTitle(title);
        tvLeft.setText(title);
        tvLeft.setVisibility(View.VISIBLE);
        tvLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLeftDoSomething();
            }
        });
    }

     protected void onLeftDoSomething() {

    }
    protected void onRightDoSomething() {

    }

    public void setRightTitle(String rightTitle){
        tvRight.setText(rightTitle);
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRightDoSomething();
            }
        });
    }
    public void hideBack(){
        toolbar.setNavigationIcon(null);
    }
    public void showBack(){
        toolbar.setNavigationIcon(R.drawable.backtwo);
    }

    public void skipActivity(Class toActivity, Bundle bundle) {
        Intent intent = new Intent(this, toActivity);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    public void showToast(String msg) {
        ToastUtil.showToast(this.getApplicationContext(), msg);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().removeActivity(this);
    }







    /**
     * 请求权限
     *
     * @param permissions 请求的权限
     * @param requestCode 请求权限的请求码
     */
    public void requestPermission(String[] permissions, int requestCode) {
        this.REQUEST_CODE_PERMISSION = requestCode;
        if (checkPermissions(permissions)) {
            permissionSuccess(REQUEST_CODE_PERMISSION);
        } else {
            List<String> needPermissions = getDeniedPermissions(permissions);
            ActivityCompat.requestPermissions(this, needPermissions.toArray(new String[needPermissions.size()]), REQUEST_CODE_PERMISSION);
        }
    }

    /**
     * 检测所有的权限是否都已授权
     *
     * @param permissions
     * @return
     */
    public boolean checkPermissions(String[] permissions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) !=
                    PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取权限集中需要申请权限的列表
     *
     * @param permissions
     * @return
     */
    private List<String> getDeniedPermissions(String[] permissions) {
        List<String> needRequestPermissionList = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) !=
                    PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                needRequestPermissionList.add(permission);
            }
        }
        return needRequestPermissionList;
    }


    /**
     * 系统请求权限回调
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (verifyPermissions(grantResults)) {
                permissionSuccess(REQUEST_CODE_PERMISSION);
            } else {
                permissionFail(REQUEST_CODE_PERMISSION);
//                showTipsDialog();
            }
        }
    }

    /**
     * 确认所有的权限是否都已授权
     *
     * @param grantResults
     * @return
     */
    private boolean verifyPermissions(int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 显示提示对话框
     */
    private void showTipsDialog() {
        new AlertDialog.Builder(this)
                .setTitle("提示信息")
                .setMessage("当前应用缺少必要权限，该功能暂时无法使用。如若需要，请单击【确定】按钮前往设置中心进行权限授权。")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startAppSettings();
                    }
                }).show();
    }

    /**
     * 启动当前应用设置页面
     */
    private void startAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }

    /**
     * 获取权限成功
     *
     * @param requestCode
     */
    public void permissionSuccess(int requestCode) {
        LogUtil.d("sssssssssss获取权限成功=" + requestCode);
    }

    /**
     * 权限获取失败
     * @param requestCode
     */
    public void permissionFail(int requestCode) {
//        Log.d(TAG, "获取权限失败=" + requestCode);
    }

    public void logi(String msg){
        if (BuildConfig.DEBUG)
            LogUtil.i("test== "+msg);
    }
}
