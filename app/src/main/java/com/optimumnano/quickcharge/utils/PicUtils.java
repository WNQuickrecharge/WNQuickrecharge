package com.optimumnano.quickcharge.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;

/**
 * Created by 秋平 on 2017/3/28 0028.
 */

public class PicUtils {

    /**
     * 添加边框
     *
     * @param bm  原图片
     * @param res 边框资源
     * @return
     */
    public static Bitmap addBigFrame(Context mContext, Bitmap bm, int res) {
        Bitmap bitmap = decodeBitmap(mContext, res);
        Drawable[] array = new Drawable[2];
        array[0] = new BitmapDrawable(bm);
        Bitmap b = resizeBitmap(bitmap, bm.getWidth(), bm.getHeight());
        array[1] = new BitmapDrawable(b);
        LayerDrawable layer = new LayerDrawable(array);
        return drawableToBitmap(layer);
    }

    /**
     * 将Drawable转换成Bitmap
     *
     * @return
     */
    private static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        // canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }


    /**
     * 图片缩放
     *
     * @param bm
     * @param w  缩小或放大成的宽
     * @param h  缩小或放大成的高
     * @return
     */
    public static Bitmap resizeBitmap(Bitmap bm, int w, int h) {
        Bitmap BitmapOrg = bm;

        int width = BitmapOrg.getWidth();
        int height = BitmapOrg.getHeight();

        float scaleWidth = width/((float) w)  ;
        float scaleHeight = height/((float) h)  ;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(BitmapOrg, 0, 0, w, h, matrix, true);
    }


    /**
     * 将R.drawable.*转换成Bitmap
     *
     * @param res
     * @return
     */
    private static Bitmap decodeBitmap(Context mContext, int res) {
        return BitmapFactory.decodeResource(mContext.getResources(), res);
    }
}
