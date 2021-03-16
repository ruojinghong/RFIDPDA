package com.bigoffs.rfid.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bigoffs.rfid.R;
import com.bigoffs.rfid.mvp.bean.InventoryInfoNew;


import java.util.List;

/**
 * Created by okbuy on 17-2-17.
 */

public class TallyDetailAdapter extends BaseAdapter {

    private Context mContext;

    private List<InventoryInfoNew> mData;

    public TallyDetailAdapter(Context context, List<InventoryInfoNew> data) {
        mContext = context;
        mData = data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup group) {
        ViewHolder holder = null;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_tally_detail, null);
            holder = new ViewHolder();
            holder.tvNumber = (TextView) view.findViewById(R.id.tv_number);
            holder.tvShelfNumber = (TextView) view.findViewById(R.id.tv_shelf);
            holder.tvTrueShelfNumber = (TextView) view.findViewById(R.id.tv_true_shelf);
            holder.tvIncode = (TextView) view.findViewById(R.id.tv_incode);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        InventoryInfoNew info = mData.get(i);
        holder.tvNumber.setText((i + 1) + "");
        holder.tvTrueShelfNumber.setText(info.oldShelf);
        holder.tvShelfNumber.setText(info.shelf);
        holder.tvIncode.setText(info.incode);
        return view;
    }

    class ViewHolder {
        TextView tvNumber;
        TextView tvShelfNumber;
        TextView tvTrueShelfNumber;
        TextView tvIncode;
    }
}
