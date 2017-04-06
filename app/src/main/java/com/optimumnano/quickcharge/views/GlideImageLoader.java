package com.optimumnano.quickcharge.views;

import android.app.Activity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lzy.imagepicker.loader.ImageLoader;


public class GlideImageLoader implements ImageLoader {
    
        @Override
        public void displayImage(Activity activity, String path, ImageView imageView, int width, int height) {
            Glide.with(activity)
                    .load(path).diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);

        }
    
        @Override
        public void clearMemoryCache() {
            //这里是清除缓存的方法,根据需要自己实现
        }
    }