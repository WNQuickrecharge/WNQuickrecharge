package com.lzy.imagepicker.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 作者：凌章 on 16/10/9 11:32
 * 邮箱：lilingzhang@longshine.com
 */
public class GFImageView extends ImageView {
    private GFImageView.OnImageViewListener mOnImageViewListener;

    public GFImageView(Context context) {
        super(context);
    }

    public GFImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GFImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setOnImageViewListener(GFImageView.OnImageViewListener listener) {
        this.mOnImageViewListener = listener;
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.mOnImageViewListener != null) {
            this.mOnImageViewListener.onDetach();
        }

    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.mOnImageViewListener != null) {
            this.mOnImageViewListener.onAttach();
        }

    }

    protected boolean verifyDrawable(Drawable dr) {
        return this.mOnImageViewListener != null && this.mOnImageViewListener.verifyDrawable(dr) ? true : super.verifyDrawable(dr);
    }

    public void onStartTemporaryDetach() {
        super.onStartTemporaryDetach();
        if (this.mOnImageViewListener != null) {
            this.mOnImageViewListener.onDetach();
        }

    }

    public void onFinishTemporaryDetach() {
        super.onFinishTemporaryDetach();
        if (this.mOnImageViewListener != null) {
            this.mOnImageViewListener.onAttach();
        }

    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.mOnImageViewListener != null) {
            this.mOnImageViewListener.onDraw(canvas);
        }

    }

    public interface OnImageViewListener {
        void onDetach();

        void onAttach();

        boolean verifyDrawable(Drawable var1);

        void onDraw(Canvas var1);
    }
}
