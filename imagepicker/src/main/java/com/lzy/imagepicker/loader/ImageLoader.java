package com.lzy.imagepicker.loader;

import android.app.Activity;

import com.lzy.imagepicker.view.GFImageView;

import java.io.Serializable;

public interface ImageLoader extends Serializable {

    void displayImage(Activity activity, String path, GFImageView imageView, int width, int height);
    void clearMemoryCache();
}
