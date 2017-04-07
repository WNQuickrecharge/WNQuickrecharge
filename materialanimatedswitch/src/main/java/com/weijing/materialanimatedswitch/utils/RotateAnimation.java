package com.weijing.materialanimatedswitch.utils;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.LinearInterpolator;

public class RotateAnimation {

    public RotateAnimation with(@NonNull View view) {
        this.view = view;
        return this;
    }

    private long currentPlayTime = 0;

    public static RotateAnimation create() {
        return new RotateAnimation();
    }

    ObjectAnimator imageViewObjectAnimator;

    public void start() {
        if (view == null) throw new NullPointerException("View cant be null!");
        if (direction == CLOCKWISE) {
            imageViewObjectAnimator = ObjectAnimator.ofFloat(view, "rotation", 0f, 360f);
        } else {
            imageViewObjectAnimator = ObjectAnimator.ofFloat(view, "rotation", 360f, 0);
        }
//        final ObjectAnimator imageViewObjectAnimator = ObjectAnimator.ofFloat(view, "rotation", 0f, 360f);
        imageViewObjectAnimator.setDuration(duration); // miliseconds
        imageViewObjectAnimator.setRepeatMode(repeatMode);
        imageViewObjectAnimator.setRepeatCount(repeatCount);
        imageViewObjectAnimator.setInterpolator(new LinearInterpolator());
        imageViewObjectAnimator.start();
//        imageViewObjectAnimator.setCurrentPlayTime(currentPlayTime);
    }

    public void stop() {
//        currentPlayTime = imageViewObjectAnimator.getCurrentPlayTime();
        imageViewObjectAnimator.cancel();
    }

    private int duration = 2000;
    private int direction = CLOCKWISE;
    private int repeatMode = ValueAnimator.RESTART;
    private int repeatCount = INFINITE;
    private View view;

    public static final int CLOCKWISE = 4;
    public static final int ANTICLOCKWISE = 5;

    public static final int RESTART = 1;
    public static final int REVERSE = 2;
    public static final int INFINITE = -1;

    public RotateAnimation setDuration(int duration) {
        this.duration = duration;
        return this;
    }

    public RotateAnimation setDirection(int direction) {
        this.direction = direction;
        return this;
    }

    public RotateAnimation setRepeatMode(int repeatMode) {
        this.repeatMode = repeatMode;
        return this;
    }

    public RotateAnimation setRepeatCount(int repeatCount) {
        this.repeatCount = repeatCount;
        return this;
    }
}
