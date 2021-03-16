package com.bigoffs.rfid.ui.adapter;

import android.graphics.Color;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bigoffs.rfid.R;
import com.bigoffs.rfid.mvp.bean.GroundingDetailInfo;

import java.util.List;

/**
 * @author okbuy-001
 * @desc
 * @time 2018/12/7 18:18
 */
public class GroundingDetailInfoAdapter extends RecyclerView.Adapter<GroundingDetailInfoAdapter.VH> {


    private List<GroundingDetailInfo> list;

    public GroundingDetailInfoAdapter(List<GroundingDetailInfo> list ){
        this.list = list;

    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_three_rows,parent,false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
            holder.tvInCode.setText(list.get(position).inCode);
            holder.tvBarCode.setText(list.get(position).barCode);
            holder.tvShelfCode.setText(list.get(position).shelfCode);
            if (list.get(position).bCode){
                holder.tvBarCode.setTextColor(Color.BLACK);
            }else {
                holder.tvBarCode.setTextColor(Color.RED);
            }
        if (list.get(position).iCode){
            holder.tvInCode.setTextColor(Color.BLACK);
        }else {
            holder.tvInCode.setTextColor(Color.RED);
        }
        if (list.get(position).sCode){
            holder.tvShelfCode.setTextColor(Color.BLACK);
        }else {
            holder.tvShelfCode.setTextColor(Color.RED);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class VH extends RecyclerView.ViewHolder{
        public final TextView tvInCode;
        public final TextView tvBarCode;
        public final TextView tvShelfCode;
        public VH(View v) {
            super(v);
            tvInCode = (TextView) v.findViewById(R.id.tv_center);
            tvBarCode = (TextView) v.findViewById(R.id.tv_right);
            tvShelfCode = (TextView) v.findViewById(R.id.tv_left);
        }
    }

}
