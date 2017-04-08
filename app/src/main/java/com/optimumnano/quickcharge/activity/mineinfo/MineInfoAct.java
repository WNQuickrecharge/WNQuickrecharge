package com.optimumnano.quickcharge.activity.mineinfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.base.BaseActivity;
import com.optimumnano.quickcharge.manager.ModifyUserInformationManager;
import com.optimumnano.quickcharge.net.ManagerCallback;
import com.optimumnano.quickcharge.utils.Base64Image;
import com.optimumnano.quickcharge.utils.SharedPreferencesUtil;
import com.optimumnano.quickcharge.views.BottomSheetDialog;
import com.optimumnano.quickcharge.views.CircleImageView;
import com.optimumnano.quickcharge.views.GlideImageLoader;
import com.optimumnano.quickcharge.views.MenuItem1;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.util.LogUtil;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.optimumnano.quickcharge.utils.SPConstant.KEY_USERINFO_HEADIMG_URL;
import static com.optimumnano.quickcharge.utils.SPConstant.KEY_USERINFO_MOBILE;
import static com.optimumnano.quickcharge.utils.SPConstant.KEY_USERINFO_NICKNAME;
import static com.optimumnano.quickcharge.utils.SPConstant.KEY_USERINFO_SEX;
import static com.optimumnano.quickcharge.utils.SPConstant.SP_USERINFO;

/**
 * 作者：邓传亮 on 2017/4/5 15:47
 * <p>
 * 邮箱：dengchuanliang@optimumchina.com
 */
public class MineInfoAct extends BaseActivity {

    private static final int REQUEST_CODE_OPEN_ALBUM = 1001;
    private static final int REQUEST_CODE_OPEN_CAMERA = 1002;
    @Bind(R.id.act_mineinfo_iv_headimg)
    CircleImageView mHeadview;
    public ImagePicker imagePicker;
    @Bind(R.id.act_mineinfo_tv_nickname)
    MenuItem1 mTvNickname;
    @Bind(R.id.act_mineinfo_tv_sex)
    MenuItem1 mTvSex;
    @Bind(R.id.act_mineinfo_tv_phone)
    MenuItem1 mTvPhone;
    private BottomSheetDialog mBsdialog;
    private EditText mInputInfoEt;
    private AlertDialog mSetSexDialog;
    private AlertDialog mInPutInfoDialog;
    private String mNickname;
    private int mUerSex =1;//默认为男
    private RadioGroup mSexRb;
    private ModifyUserInformationManager mManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_info);
        ButterKnife.bind(this);
        initViews();
        initData();
        initListener();
    }

    private void initListener() {

    }

    private void initData() {
        mManager = new ModifyUserInformationManager();
    }

    @Override
    public void initViews() {
        super.initViews();
        initImagePick();
        setRightTitle("");
        showBack();
        setTitle("个人资料");
        initUserInfo();
    }

    private void initUserInfo() {
        //初始化头像
        String headimgurl = SharedPreferencesUtil.getValue(SP_USERINFO, KEY_USERINFO_HEADIMG_URL, "");
        Glide.with(MineInfoAct.this)
                .load(headimgurl).diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.head).placeholder(R.drawable.head).into(mHeadview);
        //初始化昵称
        mNickname = SharedPreferencesUtil.getValue(SP_USERINFO, KEY_USERINFO_NICKNAME, "");
        mTvNickname.setRightText(mNickname);
        //初始化性别
        mUerSex = SharedPreferencesUtil.getValue(SP_USERINFO, KEY_USERINFO_SEX, 1);
        mTvSex.setRightText(mUerSex ==1?"男":"女");
        //初始化电话号码
        String phone = SharedPreferencesUtil.getValue(SP_USERINFO, KEY_USERINFO_MOBILE, "");
        mTvPhone.setRightText(phone);

        View popView = LayoutInflater.from(this).inflate(R.layout.layout_change_headview, null);
        mBsdialog = new BottomSheetDialog(this);
        mBsdialog.setContentView(popView);
        mBsdialog.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent);
        popView.findViewById(R.id.layout_changehead_tv_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MineInfoAct.this, ImageGridActivity.class);
                intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, true); // 是否是直接打开相机
                startActivityForResult(intent, REQUEST_CODE_OPEN_CAMERA);
                hideBottomDialog();
            }
        });
        popView.findViewById(R.id.layout_changehead_tv_album).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MineInfoAct.this, ImageGridActivity.class), REQUEST_CODE_OPEN_ALBUM);
                hideBottomDialog();
            }
        });
        popView.findViewById(R.id.layout_changehead_tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideBottomDialog();
            }
        });
    }


    private void initImagePick() {
        imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());
        imagePicker.setShowCamera(true);  //显示拍照按钮
        imagePicker.setCrop(true);        //允许裁剪（单选才有效）
        imagePicker.setMultiMode(false);//单选
        imagePicker.setStyle(CropImageView.Style.CIRCLE);  //裁剪框的形状
//        imagePicker.setFocusWidth(400);   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
//        imagePicker.setFocusHeight(400);  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(200);//保存文件的宽度。单位像素
        imagePicker.setOutPutY(200);//保存文件的高度。单位像素
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if ( mInPutInfoDialog != null){
            mInPutInfoDialog.dismiss();
            mInPutInfoDialog =null;
        }
        if ( mSetSexDialog != null){
            mSetSexDialog.dismiss();
            mSetSexDialog =null;
        }

    }


    @OnClick({R.id.act_mineinfo_tv_changehead,R.id.act_mineinfo_tv_nickname,R.id.act_mineinfo_tv_sex,R.id.act_mineinfo_tv_phone,})
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.act_mineinfo_tv_changehead:
                showBottomDialog();
                break;
            case R.id.act_mineinfo_tv_nickname:
                alertModifyInfoDialog();
                break;
            case R.id.act_mineinfo_tv_sex:
                alertModifySexDialog();
                break;
            case R.id.act_mineinfo_tv_phone:
                //alertModifyInfoDialog(2);
                break;
            default:
                break;
        }
    }

    public void showBottomDialog() {
        if (!mBsdialog.isShowing())
            mBsdialog.show();
        mBsdialog.setCanceledOnTouchOutside(false);

    }

    public void hideBottomDialog() {
        if (mBsdialog.isShowing())
            mBsdialog.dismiss();
    }

    private void alertModifyInfoDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // 初始化视图
        View view = View.inflate(this, R.layout.dialog_input_info_et, null);
        mInputInfoEt = (EditText) view.findViewById(R.id.dialog_input_info_et);
        TextView btn_ok = (TextView) view.findViewById(R.id.dialog_input_info_tv_ok);
        TextView btn_cancel = (TextView) view.findViewById(R.id.dialog_input_info_tv_cancel);
        // 设置监听
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputvalue = mInputInfoEt.getText().toString().trim();
                if (TextUtils.isEmpty(inputvalue)){
                    showToast("请输入昵称");
                    return;
                }
                mInPutInfoDialog.dismiss();
                modifyUserInfo(inputvalue, mUerSex,1);
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInPutInfoDialog.dismiss();
            }
        });
        builder.setView(view);
        // 显示对话框
        mInPutInfoDialog = builder.show();

    }

    private void modifyUserInfo(final String nickname, final int uerSex, final int modifyType) {
        mManager.modifyNickNameAndSex(nickname, uerSex, new ManagerCallback() {
            @Override
            public void onSuccess(Object returnContent) {
                showToast("修改成功");

                if (1 == modifyType){//改昵称
                    mNickname = nickname;
                    mTvNickname.setRightText(mNickname);
                    SharedPreferencesUtil.putValue(SP_USERINFO, KEY_USERINFO_NICKNAME, mNickname);
                } else if (2 == modifyType){//改性别
                    mUerSex = uerSex;
                    mTvSex.setRightText(mUerSex ==1?"男":"女");
                    SharedPreferencesUtil.putValue(SP_USERINFO, KEY_USERINFO_SEX, mUerSex);
                }

                LogUtil.i("test==modifyUserInfo onSuccess ");
            }

            @Override
            public void onFailure(String msg) {
                showToast("修改失败"+msg);
                LogUtil.i("test==modifyUserInfo onFailure "+msg);
            }
        });
    }

    private void modifyHeadView(final String url, final String imagebase64) {
        mManager.modifyHeadView(imagebase64, new ManagerCallback() {
            @Override
            public void onSuccess(Object returnContent) {
                showToast("修改成功");

                JSONObject dataJson = null;
                try {
                    dataJson = new JSONObject(returnContent.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String netHeadUrl = dataJson.optString("url");
                Glide.with(MineInfoAct.this)
                        .load(url).diskCacheStrategy(DiskCacheStrategy.ALL).into(mHeadview);
                SharedPreferencesUtil.putValue(SP_USERINFO, KEY_USERINFO_HEADIMG_URL, netHeadUrl);
                LogUtil.i("test==modifyHeadView onSuccess "+netHeadUrl);
            }

            @Override
            public void onFailure(String msg) {
                showToast(msg);
                LogUtil.i("test==modifyHeadView onFailure "+msg);
            }
        });
    }

    private void alertModifySexDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // 初始化视图
        View view = View.inflate(this, R.layout.dialog_input_info_sex_rb, null);
        mSexRb = (RadioGroup) view.findViewById(R.id.dialog_input_info_rg);
        TextView btn_ok = (TextView) view.findViewById(R.id.dialog_input_info_tv_ok);
        TextView btn_cancel = (TextView) view.findViewById(R.id.dialog_input_info_tv_cancel);
        // 设置监听
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int buttonId = mSexRb.getCheckedRadioButtonId();
                int sex=1;
                if (R.id.dialog_input_info_rg_male==buttonId){
                    sex =1;
                }else if (R.id.dialog_input_info_rg_female==buttonId){
                    sex =2;
                }
                mSetSexDialog.dismiss();
                modifyUserInfo(mNickname, sex ,2);
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSetSexDialog.dismiss();
            }
        });
        builder.setView(view);
        // 显示对话框
        mSetSexDialog = builder.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            String url = "";
            ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
            if (images != null && images.size() != 0 && !images.get(0).path.equals("")) {
                url = images.get(0).path;
                String headViewBase64 = Base64Image.image2Base64Str(url);
                modifyHeadView(url,headViewBase64);

            }
        }
    }

}
