package com.optimumnano.quickcharge.activity.mineinfo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.loader.FrescoImageLoader;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.base.BaseActivity;
import com.optimumnano.quickcharge.utils.SharedPreferencesUtil;
import com.optimumnano.quickcharge.views.CircleImageView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.baidu.mapapi.BMapManager.getContext;
import static com.optimumnano.quickcharge.utils.SPConstant.KEY_UERINFO_HEADIMG;
import static com.optimumnano.quickcharge.utils.SPConstant.SP_USERINFO;

/**
 * 作者：邓传亮 on 2017/4/5 15:47
 * <p>
 * 邮箱：dengchuanliang@optimumchina.com
 */
public class MineInfoAct extends BaseActivity {

    private static final int PICK_HEAD_IMAGE = 1001;
    @Bind(R.id.mine_ivHead)
    CircleImageView ivHead;
    public ImagePicker imagePicker;

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

    }

    @Override
    public void initViews() {
        super.initViews();
        initImagePick();
        initHeadView();
        setRightTitle("");
        showBack();
        setTitle("个人资料");
    }

    private void initHeadView() {
        //TODO 先获取网络数据
        String url = SharedPreferencesUtil.getValue(SP_USERINFO,KEY_UERINFO_HEADIMG, "");
        if (!TextUtils.isEmpty(url)){
            Bitmap bmp = null;
            try {
                bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.fromFile(new File(url)));
                ivHead.setImageBitmap(bmp);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void initImagePick() {
        imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new FrescoImageLoader(this));
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
    }


    @OnClick({R.id.mine_ivHead})
    public void onClick(View view){
        Intent intent=null;
        switch (view.getId()) {
            case R.id.mine_ivHead:
                intent=new Intent(getContext(), ImageGridActivity.class);
                startActivityForResult(intent,PICK_HEAD_IMAGE);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_HEAD_IMAGE && resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            String url="";
            ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
            if (images != null && images.size() != 0 && !images.get(0).path.equals("")){
                url=images.get(0).path;
                Bitmap bmp = null;
                try {
                    bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.fromFile(new File(url)));
                    ivHead.setImageBitmap(bmp);
                    SharedPreferencesUtil. putValue(SP_USERINFO,KEY_UERINFO_HEADIMG,url);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
