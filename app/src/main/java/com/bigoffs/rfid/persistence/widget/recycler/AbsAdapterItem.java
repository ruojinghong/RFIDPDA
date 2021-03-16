package com.bigoffs.rfid.persistence.widget.recycler;

import android.view.View;
import android.view.ViewGroup;

public abstract class AbsAdapterItem
{
	BaseRecyclerAdapter mAttachedAdapter;

	public BaseRecyclerAdapter getAttachedAdapter()
	{
		return mAttachedAdapter;
	}

	public abstract View onCreateView(ViewGroup parent, int position);

	public abstract void onBindView(BaseRecyclerAdapter.BaseViewHolder holder, View view, int position);

	public abstract void onViewAttachedToWindow(BaseRecyclerAdapter.BaseViewHolder holder, View view);

	public void onViewDetachedFromWindow(BaseRecyclerAdapter.BaseViewHolder holder, View view)
	{
	}

	public void onViewRecycled(BaseRecyclerAdapter.BaseViewHolder holder, View view)
	{
	}
}
