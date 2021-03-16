package com.bigoffs.rfid.ui.adapter;

import android.graphics.Color;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import com.bigoffs.rfid.R;
import com.bigoffs.rfid.mvp.bean.PickingList;

import java.util.List;

/**
 * Created by okbuy on 17-3-27.
 */

public class PickingListAdapter extends RecyclerView.Adapter<PickingListAdapter.ViewHolder> {

    private List<PickingList> mData;
    private OnButtonClickListener mListener;

    public PickingListAdapter(List<PickingList> data, OnButtonClickListener onButtonClickListener) {
        mData = data;
        mListener = onButtonClickListener;
    }

    @Override
    public PickingListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_picking_list, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(PickingListAdapter.ViewHolder holder, final int position) {
        PickingList data = mData.get(position);
        holder.tvCode.setText("单号：" + data.code);
        holder.tvTime.setText("时间：" + data.createTime);
        holder.tvNum.setText("数量：" + data.num);
        holder.tvType.setText("类型："+data.type);
        holder.tvRemark.setText("备注："+data.remark);
        if(data.typeId != 1){
            holder.tvType.setTextColor(Color.parseColor("#303F9F"));
        }else{
            holder.tvType.setTextColor(Color.parseColor("#000000"));
        }
        holder.btnCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (mListener != null) {
                    mListener.onButtonClick(v, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public interface OnButtonClickListener {
        void onButtonClick(View v, int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvCode;
        private TextView tvTime;
        private TextView tvNum;
        private TextView tvType;
        private TextView tvRemark;
        private Button btnCollect;

        public ViewHolder(View itemView) {
            super(itemView);
            tvCode = (TextView) itemView.findViewById(R.id.tv_picking_list_code);
            tvTime = (TextView) itemView.findViewById(R.id.tv_picking_list_time);
            tvNum = (TextView) itemView.findViewById(R.id.tv_picking_list_num);
            tvType = (TextView) itemView.findViewById(R.id.tv_picking_list_type);
            tvRemark = itemView.findViewById(R.id.tv_picking_list_remark);
            btnCollect = (Button) itemView.findViewById(R.id.btn_collect);
        }
    }
}
