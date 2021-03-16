package com.bigoffs.rfid.persistence.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Checkable;
import android.widget.FrameLayout;



public class ExtendFrameLayout extends FrameLayout implements ViewObservable.IViewObservable, Checkable
{
	ViewObservable mViewObservable = new ViewObservable(this);
	boolean mChecked = false;
	boolean mInterceptTouchEventToDownward = false;

	public ExtendFrameLayout(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	public ExtendFrameLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public ExtendFrameLayout(Context context)
	{
		super(context);
	}



	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		mViewObservable.notifyOnMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom)
	{
		super.onLayout(changed, left, top, right, bottom);
		mViewObservable.notifyOnLayout(changed, left, top, right, bottom);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh)
	{
		super.onSizeChanged(w, h, oldw, oldh);
		mViewObservable.notifyOnSizeChanged(w, h, oldw, oldh);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event)
	{
		boolean result = super.dispatchTouchEvent(event);
		if (!result)
		{
			result = super.onTouchEvent(event);
		}
		if (mInterceptTouchEventToDownward)
			return mInterceptTouchEventToDownward;
		else
			return result;
	}

	@Override
	protected void onDetachedFromWindow()
	{
		super.onDetachedFromWindow();
		mViewObservable.clear();
	}

	@Override
	public void setChecked(boolean checked)
	{
		mChecked = checked;
		super.setActivated(checked);
		int count = getChildCount();
		for (int i = 0; i < count; i++)
		{
			View child = getChildAt(i);
			if (child instanceof Checkable)
				((Checkable)child).setChecked(checked);
			else
				child.setActivated(checked);
		}
	}

	@Override
	public boolean isChecked()
	{
		return mChecked;
	}

	@Override
	public void toggle()
	{
		setChecked(!mChecked);
	}

	/**
	 * ����TouchEvent�¼����´���
	 * */
	public void setInterceptTouchEventToDownward(boolean intercept)
	{
		mInterceptTouchEventToDownward = intercept;
	}

	public boolean isInterceptTouchEventToDownward()
	{
		return mInterceptTouchEventToDownward;
	}

	@Override
	public void registerObserver(ViewObservable.OnViewObserver observer) {
		mViewObservable.registerObserver(observer);
	}

	@Override
	public void unregisterObserver(ViewObservable.OnViewObserver observer) {
		mViewObservable.unregisterObserver(observer);
	}
}
