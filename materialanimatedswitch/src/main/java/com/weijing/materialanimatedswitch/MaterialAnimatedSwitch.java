package com.weijing.materialanimatedswitch;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.View;

import com.weijing.materialanimatedswitch.observer.BallFinishObservable;
import com.weijing.materialanimatedswitch.observer.BallMoveObservable;
import com.weijing.materialanimatedswitch.painter.BallPainter;
import com.weijing.materialanimatedswitch.painter.BallShadowPainter;
import com.weijing.materialanimatedswitch.painter.BasePainter;

import java.util.Observable;
import java.util.Observer;


public class MaterialAnimatedSwitch extends View {

    private int margin;
    private BasePainter basePainter;
    private BallPainter ballPainter;
    private BallShadowPainter ballShadowPainter;
    private MaterialAnimatedSwitchState actualState;
    //  private IconPressPainter iconPressPainter;
//  private IconReleasePainter iconReleasePainter;
    private int baseColorRelease = Color.parseColor("#3061BE");
    private int baseColorPress = Color.parseColor("#D7E7FF");
    private int ballColorRelease = Color.parseColor("#5992FB");
    private int ballColorPress = Color.parseColor("#FFFFFF");
    private int ballShadowColor = Color.parseColor("#99000000");
    //  private Bitmap releaseIcon;
//  private Bitmap pressIcon;
    private BallFinishObservable ballFinishObservable;
    private BallMoveObservable ballMoveObservable;
    private boolean isClickable = true;
    private OnCheckedChangeListener onCheckedChangeListener;
    private boolean isCheck=true;


    public MaterialAnimatedSwitch(Context context) {
        super(context);
        init();
    }

    public MaterialAnimatedSwitch(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public MaterialAnimatedSwitch(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void init() {
        margin = (int) getContext().getResources().getDimension(R.dimen.margin);
        initObservables();
        initPainters();
        if (!isCheck){
            actualState = MaterialAnimatedSwitchState.PRESS;
            setState(actualState);
        }else {
            actualState = MaterialAnimatedSwitchState.RELEASE;
            setState(actualState);
        }
//        actualState = MaterialAnimatedSwitchState.PRESS;
//        setState(actualState);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    private void initPainters() {
        basePainter = new BasePainter(baseColorRelease, baseColorPress, margin, ballMoveObservable);

        int width = Utils.dpToPx(53, getResources());
        int height = Utils.dpToPx(28, getResources());
        ballPainter = new BallPainter(ballColorRelease, ballColorPress, margin, ballFinishObservable,
                ballMoveObservable, getContext(),width,height);

        ballShadowPainter =
                new BallShadowPainter(ballShadowColor, ballShadowColor, margin, ballShadowColor,
                        ballFinishObservable, ballMoveObservable, getContext(),width,height);
//    iconPressPainter =
//        new IconPressPainter(getContext(), pressIcon, ballFinishObservable, ballMoveObservable,
//            margin);
//    iconReleasePainter =
//        new IconReleasePainter(getContext(), releaseIcon, ballFinishObservable, margin);
    }

    private void init(AttributeSet attrs) {
        TypedArray attributes =
                getContext().obtainStyledAttributes(attrs, R.styleable.materialAnimatedSwitch);
        initAttributes(attributes);
        init();
    }

    private void initAttributes(TypedArray attributes) {
        baseColorRelease = attributes.getColor(R.styleable.materialAnimatedSwitch_base_release_color,
                baseColorRelease);
        baseColorPress =
                attributes.getColor(R.styleable.materialAnimatedSwitch_base_press_color, baseColorPress);
        ballColorRelease = attributes.getColor(R.styleable.materialAnimatedSwitch_ball_release_color,
                ballColorRelease);
        ballColorPress =
                attributes.getColor(R.styleable.materialAnimatedSwitch_ball_press_color, ballColorPress);
        isCheck=attributes.getBoolean(R.styleable.materialAnimatedSwitch_isCheck,true);
//    pressIcon = BitmapFactory.decodeResource(getResources(),
//        attributes.getResourceId(R.styleable.materialAnimatedSwitch_icon_press,
//            R.drawable.tack_save_button_32_blue));
//    releaseIcon = BitmapFactory.decodeResource(getResources(),
//        attributes.getResourceId(R.styleable.materialAnimatedSwitch_icon_release,
//            R.drawable.tack_save_button_32_white));
    }

    private void initObservables() {
        ballFinishObservable = new BallFinishObservable();
        ballMoveObservable = new BallMoveObservable();
        ballFinishObservable.addObserver(new BallStateObserver());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = Utils.dpToPx(53, getResources());
        int height = Utils.dpToPx(28, getResources());
        setMeasuredDimension(width, height);
        basePainter.onSizeChanged(height, width);
        ballShadowPainter.onSizeChanged(height, width);
        ballPainter.onSizeChanged(height, width);
//    iconPressPainter.onSizeChanged(height, width);
//    iconReleasePainter.onSizeChanged(height, width);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        basePainter.draw(canvas);
        ballShadowPainter.draw(canvas);
        ballPainter.draw(canvas);
//    iconPressPainter.draw(canvas);
//    iconReleasePainter.draw(canvas);
        invalidate();

    }

    private void setState(MaterialAnimatedSwitchState materialAnimatedSwitchState) {
        basePainter.setState(materialAnimatedSwitchState);
        ballPainter.setState(materialAnimatedSwitchState);
        ballShadowPainter.setState(materialAnimatedSwitchState);
//    iconPressPainter.setState(materialAnimatedSwitchState);
//    iconReleasePainter.setState(materialAnimatedSwitchState);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                if (isClickable) {
                    doActionDown();
                }
                return true;
            default:
                return false;
        }
    }

    private void doActionDown() {
        if (actualState.equals(MaterialAnimatedSwitchState.RELEASE) || actualState.equals(
                MaterialAnimatedSwitchState.INIT) || actualState == null) {
            actualState = MaterialAnimatedSwitchState.PRESS;
            setState(actualState);
        } else {
            actualState = MaterialAnimatedSwitchState.RELEASE;
            setState(actualState);
        }
        playSoundEffect(SoundEffectConstants.CLICK);
    }

    public boolean isChecked() {
        return actualState.equals(MaterialAnimatedSwitchState.PRESS);
    }

    public void toggle() {
        if (isClickable) {
            doActionDown();
        }
    }

    /**
     * Avoid click when ball is still in movement
     * Call listener when state is updated
     */
    private class BallStateObserver implements Observer {

        @Override
        public void update(Observable observable, Object data) {
            BallFinishObservable ballFinishObservable = (BallFinishObservable) observable;
            isClickable = !ballFinishObservable.getState().equals(BallFinishObservable.BallState.MOVE);

            if (ballFinishObservable.getState().equals(BallFinishObservable.BallState.PRESS)) {
                if (onCheckedChangeListener != null) {
                    onCheckedChangeListener.onCheckedChanged(getId(), false);
                }
            } else if (ballFinishObservable.getState().equals(BallFinishObservable.BallState.RELEASE)) {
                if (onCheckedChangeListener != null) {
                    onCheckedChangeListener.onCheckedChanged(getId(), true);
                }
            }
        }
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener onCheckedChangeListener) {
        this.onCheckedChangeListener = onCheckedChangeListener;
    }

 /*   public void check(boolean value) {
        if (value) {
            if (actualState.equals(MaterialAnimatedSwitchState.PRESS)) {
                actualState = MaterialAnimatedSwitchState.RELEASE;
                setState(actualState);
            }
        } else {
            if (actualState.equals(MaterialAnimatedSwitchState.RELEASE)) {
                actualState = MaterialAnimatedSwitchState.PRESS;
                setState(actualState);
            }
        }
    }*/

    public void check(boolean value) {
        if (value) {
            if (actualState.equals(MaterialAnimatedSwitchState.RELEASE)) {
                actualState = MaterialAnimatedSwitchState.PRESS;
                setState(actualState);
            }
        } else {
            if (actualState.equals(MaterialAnimatedSwitchState.PRESS)) {
                actualState = MaterialAnimatedSwitchState.RELEASE;
                setState(actualState);
            }
        }
    }

    public interface OnCheckedChangeListener {

        void onCheckedChanged(int id, boolean isChecked);
    }
}
