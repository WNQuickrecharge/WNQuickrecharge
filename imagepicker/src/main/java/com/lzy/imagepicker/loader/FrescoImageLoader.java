package com.lzy.imagepicker.loader;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.AbstractDraweeController;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.view.DraweeHolder;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.lzy.imagepicker.R;
import com.lzy.imagepicker.view.GFImageView;

/**
 * 作者：凌章 on 16/10/9 11:29
 * 邮箱：lilingzhang@longshine.com
 */

public class FrescoImageLoader implements ImageLoader {
    private Context context;

    public FrescoImageLoader(Context context) {
        this(context, Bitmap.Config.RGB_565);
    }

    public FrescoImageLoader(Context context, Bitmap.Config config) {
        this.context = context;
        ImagePipelineConfig imagePipelineConfig = ImagePipelineConfig.newBuilder(context).setBitmapsConfig(config).build();
        Fresco.initialize(context, imagePipelineConfig);
    }

    @Override
    public void displayImage(Activity activity, String path, final GFImageView imageView, int width, int height) {
        Resources resources = this.context.getResources();
        final Drawable defaultDrawable = resources.getDrawable(R.mipmap.default_image);
        GenericDraweeHierarchy hierarchy = (new GenericDraweeHierarchyBuilder(resources)).setFadeDuration(300).setPlaceholderImage(defaultDrawable).setFailureImage(defaultDrawable).setProgressBarImage(new ProgressBarDrawable()).build();
        final DraweeHolder draweeHolder = DraweeHolder.create(hierarchy, this.context);
        imageView.setOnImageViewListener(new GFImageView.OnImageViewListener() {
            public void onDetach() {
                draweeHolder.onDetach();
            }

            public void onAttach() {
                draweeHolder.onAttach();
            }

            public boolean verifyDrawable(Drawable dr) {
                return dr == draweeHolder.getHierarchy().getTopLevelDrawable();
            }

            public void onDraw(Canvas canvas) {
                Drawable drawable = draweeHolder.getHierarchy().getTopLevelDrawable();
                if (drawable == null) {
                    imageView.setImageDrawable(defaultDrawable);
                } else {
                    imageView.setImageDrawable(drawable);
                }

            }
        });
        Uri uri = (new Uri.Builder()).scheme("file").path(path).build();
        ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithSource(uri).setResizeOptions(new ResizeOptions(width, height)).build();
        AbstractDraweeController controller = Fresco.newDraweeControllerBuilder().setOldController(draweeHolder.getController()).setImageRequest(imageRequest).build();
        draweeHolder.setController(controller);
    }


    @Override
    public void clearMemoryCache() {

    }
}
