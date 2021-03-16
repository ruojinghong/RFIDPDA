package com.bigoffs.rfid.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bigoffs.rfid.R;
import com.bigoffs.rfid.mvp.bean.PickingBarCode;


import java.util.List;

/**
 * Created by okbuy on 17-2-17.
 */

public class PickingBarCodeAdapter extends BaseAdapter {

    private Context mContext;

    private List<PickingBarCode.incode> mData;


    public PickingBarCodeAdapter(Context context, List<PickingBarCode.incode> data) {
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
            view = LayoutInflater.from(mContext).inflate(R.layout.item_picking_result, null);
            holder = new ViewHolder();
            holder.tvNumber = (TextView) view.findViewById(R.id.tv_number);
            holder.tvIncode = (TextView) view.findViewById(R.id.tv_incode);
            holder.tvShelfNumber = (TextView) view.findViewById(R.id.tv_shelf_number);
            holder.tvState  = (TextView) view.findViewById(R.id.tv_result);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        PickingBarCode.incode incode = mData.get(i);
                holder.tvNumber.setText((i + 1) + "");
                holder.tvIncode.setText(incode.incode);
                holder.tvShelfNumber.setText(incode.shelf);
                if(incode.status == 1){
                    holder.tvState.setText("可售");
                }else{
                    holder.tvState.setText("未知");
                }
                return view;

        


    }

    class ViewHolder {
        TextView tvNumber;
        TextView tvIncode;
        TextView tvShelfNumber;
        TextView tvState;
    }
}
