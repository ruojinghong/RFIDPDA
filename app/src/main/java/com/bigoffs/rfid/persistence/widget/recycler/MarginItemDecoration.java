package com.bigoffs.rfid.persistence.widget.recycler;

import android.graphics.Rect;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MarginItemDecoration extends RecyclerView.ItemDecoration
{
	private int mMarginLeft;
	private int mMarginTop;
	private int mMarginRight;
	private int mMarginBottom;

	public void setMargin(int margin)
	{
		mMarginLeft = margin;
		mMarginTop = margin;
		mMarginRight = margin;
		mMarginBottom = margin;
	}

	public void setMargin(int left, int top, int right, int bottom)
	{
		mMarginLeft = left;
		mMarginTop = top;
		mMarginRight = right;
		mMarginBottom = bottom;
	}

	@Override
	public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
		outRect.set(mMarginLeft, mMarginTop, mMarginRight, mMarginBottom);
		super.getItemOffsets(outRect, view, parent, state);
	}


}
