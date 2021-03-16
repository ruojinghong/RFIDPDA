package com.bigoffs.rfid.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bigoffs.rfid.R;
import com.bigoffs.rfid.mvp.bean.FinishInventoryInfo;
import com.google.gson.Gson;


/**
 * Created by okbuy on 17-2-17.
 */

public class InventoryDetailAdapterNew extends BaseAdapter {

    private Context mContext;

    private FinishInventoryInfo mData;

    private int status;

    public InventoryDetailAdapterNew(Context context, String result , int status) {
        mContext = context;
        Gson gson = new Gson();
        mData = gson.fromJson(result,FinishInventoryInfo.class);
        this.status = status;
    }

    @Override
    public int getCount() {

        switch (status){
            case 5:
                return mData.dbInfos.size();

            case 2:
                return mData.moreInfos.size();

            case 3:
                return mData.loseInfos.size();

            case 4:
                return mData.wrongInfos.size();
                default:
                    return 0;
        }
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
            view = LayoutInflater.from(mContext).inflate(R.layout.item_inventory_detail, null);
            holder = new ViewHolder();
            holder.tvNumber = (TextView) view.findViewById(R.id.tv_number);
            holder.tvIncode = (TextView) view.findViewById(R.id.tv_incode);
            holder.tvShelfNumber = (TextView) view.findViewById(R.id.tv_shelf_number);
            holder.tvTrueShelfNumber  = (TextView) view.findViewById(R.id.tv_true_shelf_number);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }


        switch (status){
            case 5:
                FinishInventoryInfo.dbInfo info = mData.dbInfos.get(i);
                holder.tvNumber.setText((i + 1) + "");
                holder.tvIncode.setText(info.incode);
                holder.tvShelfNumber.setText(info.shelf);
                return view;
            case 2:
                FinishInventoryInfo.moreInfo moreInfo = mData.moreInfos.get(i);
                holder.tvNumber.setText((i + 1) + "");
                holder.tvIncode.setText(moreInfo.incode);
                holder.tvShelfNumber.setText(moreInfo.shelf);
                return view;

            case 3:
                FinishInventoryInfo.loseInfo loseInfo = mData.loseInfos.get(i);
                holder.tvNumber.setText((i + 1) + "");
                holder.tvIncode.setText(loseInfo.incode);
                holder.tvShelfNumber.setText(loseInfo.shelf);
                return view;

            case 4:
                FinishInventoryInfo.wrongInfo wrongInfo = mData.wrongInfos.get(i);
                holder.tvNumber.setText((i + 1) + "");
                holder.tvIncode.setText(wrongInfo.incode);
                holder.tvShelfNumber.setText(wrongInfo.shelf);
                holder.tvTrueShelfNumber.setText(wrongInfo.correctShelf);
                holder.tvTrueShelfNumber.setVisibility(View.VISIBLE);
                return view;

            default:
                return view;
        }


    }

    class ViewHolder {
        TextView tvNumber;
        TextView tvIncode;
        TextView tvShelfNumber;
        TextView tvTrueShelfNumber;
    }
}
