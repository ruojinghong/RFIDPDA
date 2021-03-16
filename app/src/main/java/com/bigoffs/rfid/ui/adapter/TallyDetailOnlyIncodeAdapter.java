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

public class TallyDetailOnlyIncodeAdapter extends BaseAdapter {

    private Context mContext;

    private List<InventoryInfoNew> mData;

    public TallyDetailOnlyIncodeAdapter(Context context, List<InventoryInfoNew> data) {
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
            view = LayoutInflater.from(mContext).inflate(R.layout.item_tally_detail_only_incode, null);
            holder = new ViewHolder();
            holder.tvNumber = (TextView) view.findViewById(R.id.tv_number);
            holder.tvIncode = (TextView) view.findViewById(R.id.tv_incode);
            holder.tvShelfNumber = (TextView) view.findViewById(R.id.tv_shelf_number);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        InventoryInfoNew info = mData.get(i);
        holder.tvNumber.setText((i + 1) + "");
        holder.tvIncode.setText(info.incode);
        if(info.status==2){
            holder.tvShelfNumber.setText(info.shelf);
        }else {
            holder.tvShelfNumber.setText(info.oldShelf);

        }
        return view;
    }

    class ViewHolder {
        TextView tvNumber;
        TextView tvIncode;
        TextView tvShelfNumber;
    }
}
