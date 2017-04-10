package com.optimumnano.quickcharge.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.optimumnano.quickcharge.R;

import java.lang.ref.SoftReference;


public class MenuItem1 extends RelativeLayout {
	private static final String tag = MenuItem1.class.getSimpleName();
	private SoftReference<Context> mContext = null;
	private TextView tvLeft,tvRight;
	private ImageView ivRight,ivLeft;
	private CharSequence tvLeftText,tvRightText;
	private int leftTextColor,rightTextColor;
	
	private boolean showivRight=false,showtvRight = false,showivLeft = false;
	private int ivLeftDrawable;

	public MenuItem1(Context context){
		this(context,null);
	}
	
	public MenuItem1(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = new SoftReference<Context>(context);
		LayoutInflater.from(context).inflate(R.layout.menu_item1, this, true);
		if (null != attrs) {
			if (null != mContext) {
				TypedArray a = mContext.get().obtainStyledAttributes(attrs, R.styleable.MenuItem1);
				for (int i = 0; i < a.getIndexCount(); i++) {
					int index = a.getIndex(i);
					switch (index) {
					case R.styleable.MenuItem1_tvLeftText:
						tvLeftText = a.getText(index);
						break;
					case R.styleable.MenuItem1_tvRightText:
						tvRightText = a.getText(index);
						break;
					case R.styleable.MenuItem1_showivRight:
						showivRight = a.getBoolean(index, false);
						break;
					case R.styleable.MenuItem1_showtvRight:
						showtvRight = a.getBoolean(index, false);
						break;
					case R.styleable.MenuItem1_showivLeft:
						showivLeft = a.getBoolean(index,false);
						break;
					case R.styleable.MenuItem1_ivLeftDrawable:
						ivLeftDrawable = a.getResourceId(R.styleable.MenuItem1_ivLeftDrawable,0);
						break;
					case R.styleable.MenuItem1_LeftTextColor:
						leftTextColor = a.getColor(index,0);
						break;
					case R.styleable.MenuItem1_rightTextColor:
						rightTextColor = a.getColor(index,0);
						break;
					}
				}
				a.recycle();
			}
		}
        initView();
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		tvLeft.setText(tvLeftText);
		tvRight.setText(tvRightText);
		if (showivRight) {
			ivRight.setVisibility(View.VISIBLE);
		}
		else {
			ivRight.setVisibility(View.GONE);
		}
		
		if (showtvRight) {
			tvRight.setVisibility(View.VISIBLE);
		}
		else {
			tvRight.setVisibility(View.GONE);
		}
		if (isShowivLeft()){
			ivLeft.setVisibility(View.VISIBLE);
			ivLeft.setImageResource(ivLeftDrawable);
		}
		else{
			ivLeft.setVisibility(View.GONE);
		}
		if (leftTextColor!=0){
			tvLeft.setTextColor(leftTextColor);
		}
		if (rightTextColor!=0){
			tvRight.setTextColor(rightTextColor);
		}
	}
	
	private void initView(){
		tvLeft = (TextView)findViewById(R.id.tvLeft);
		tvRight = (TextView)findViewById(R.id.tvRight);
		ivRight = (ImageView) findViewById(R.id.ivRight);
		ivLeft = (ImageView) findViewById(R.id.ivLeft);
	}
	/**
	 * 获取右边文本框内容
	 */
	public String getRightText(){
		return tvRight.getText().toString();
	}
	/**
	 * 给右边文本框赋值
	 * @param str
	 */
	public void setRightText(String str){
		tvRight.setText(str);
	}
	/**
	 * 给左边文本框赋值
	 * @param str
	 */
	public void setTvLeftText(String str){
		tvLeft.setText(str);
	}

	public void setIvLeftDrawable(int ivLeftDrawable) {
		ivLeft.setImageResource(ivLeftDrawable);
	}

	public CharSequence getTvLeftText() {
		return tvLeftText;
	}

	public boolean isShowivLeft() {
		return showivLeft;
	}

	public boolean isShowtvRight() {
		return showtvRight;
	}

	public boolean isShowivRight() {
		return showivRight;
	}

	public void setShowivLeft(boolean showivLeft) {
		this.showivLeft = showivLeft;
	}

	public void setShowtvRight(boolean showtvRight) {
		this.showtvRight = showtvRight;
	}

	public void setShowivRight(boolean showivRight) {
		this.showivRight = showivRight;
	}
}
