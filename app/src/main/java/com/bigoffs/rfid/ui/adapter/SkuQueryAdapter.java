package com.bigoffs.rfid.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bigoffs.rfid.R;
import com.bigoffs.rfid.mvp.bean.SkuQueryInfo;

import java.util.List;

/**
 * @author okbuy-001
 * @desc
 * @time 2020/4/28 16:58
 */
public class SkuQueryAdapter extends BaseListAdapter<SkuQueryInfo> {



    public SkuQueryAdapter(Context context, List<SkuQueryInfo> list) {
        super(context, list);
    }





    @Override
    public View bindView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_query_sku, null);
        }

        SkuQueryInfo info = list.get(position);
        TextView tvShelf = ViewHolder.get(convertView,R.id.tv_shelf);
        TextView tvSize =  ViewHolder.get(convertView,R.id.tv_size) ;
        TextView tvNum =  ViewHolder.get(convertView,R.id.tv_num ) ;
        tvNum.setTextColor(Color.parseColor("#3F51B5"));
        tvShelf.setText(info.shelf);
        tvSize.setText(info.size);
        tvNum.setText(String.valueOf(info.incodeNum));

        return convertView;
    }


}
