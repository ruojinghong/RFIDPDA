package com.bigoffs.rfid.ui.adapter;

import android.graphics.Color;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bigoffs.rfid.R;
import com.bigoffs.rfid.mvp.bean.GroundingDetailInfo;
import com.bigoffs.rfid.mvp.bean.dao.GroundingAllocationDetailInfo;


import java.util.List;

/**
 * @author okbuy-001
 * @desc
 * @time 2018/12/7 18:18
 */
public class GroundingAllocationDetailInfoAdapter extends RecyclerView.Adapter<GroundingAllocationDetailInfoAdapter.VH> {
    //私有属性
    private OnItemClickListener onItemClickListener = null;

    private List<GroundingAllocationDetailInfo> list;

    public GroundingAllocationDetailInfoAdapter(List<GroundingAllocationDetailInfo> list ){
        this.list = list;

    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grounding_allocation_detail,parent,false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        if("".equals(list.get(position).rfid)){
            holder.tvShelfCode.setText(list.get(position).inCode);
        }else{
            holder.tvShelfCode.setText(list.get(position).rfid);
        }
            holder.tvInCode.setText("不在该调拨单");

//        if (list.get(position).isExist){
//            holder.tvInCode.setTextColor(Color.BLACK);
//        }else {
//            holder.tvInCode.setTextColor(Color.RED);
//        }
//        if (list.get(position).isExist){
//            holder.tvShelfCode.setTextColor(Color.BLACK);
//        }else {
//            holder.tvShelfCode.setTextColor(Color.RED);
//        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(v, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class VH extends RecyclerView.ViewHolder{
        public final TextView tvInCode;
        public final TextView tvShelfCode;
        public VH(View v) {
            super(v);
            tvInCode = (TextView) v.findViewById(R.id.tv_right);
            tvShelfCode = (TextView) v.findViewById(R.id.tv_left);
        }
    }

    //回调接口
    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    //setter方法
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

}
