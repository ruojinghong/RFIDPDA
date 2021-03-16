package com.bigoffs.rfid.persistence.widget.recycler;

import android.content.Context;

import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class GridRecyclerView extends RecyclerView
{
	private GridLayoutManager mLayoutManager;
	private MarginItemDecoration mItemDecoration;

	public GridRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		init();
	}

	public GridRecyclerView(Context context, @Nullable AttributeSet attrs)
	{
		super(context, attrs);
		init();
	}

	public GridRecyclerView(Context context)
	{
		super(context);
		init();
	}

	private void init()
	{
		mLayoutManager = new GridLayoutManager(getContext(), 2, LinearLayoutManager.VERTICAL, false);
		setLayoutManager(mLayoutManager);
		setItemAnimator(new DefaultItemAnimator());
		mItemDecoration = new MarginItemDecoration();
		addItemDecoration(mItemDecoration);
	}

	@Override
	public GridLayoutManager getLayoutManager()
	{
		return mLayoutManager;
	}

	public void setSpanCount(int spanCount)
	{
		mLayoutManager.setSpanCount(spanCount);
	}

	public void setSpanSizeLookup(GridLayoutManager.SpanSizeLookup spanSizeLookup)
	{
		mLayoutManager.setSpanSizeLookup(spanSizeLookup);
	}

	public void setOrientation(int orientation)
	{
		mLayoutManager.setOrientation(orientation);
	}

	public void setItemMargin(int margin)
	{
		mItemDecoration.setMargin(margin);
	}

	public void setItemMargin(int left, int top, int right, int bottom)
	{
		mItemDecoration.setMargin(left, top, right, bottom);
	}
}
