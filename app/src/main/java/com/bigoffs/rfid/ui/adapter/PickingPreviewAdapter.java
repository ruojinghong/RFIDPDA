package com.bigoffs.rfid.ui.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.bigoffs.rfid.R;
import com.bigoffs.rfid.mvp.bean.PickingPreviewItem;

import java.util.List;

/**
 * Created by okbuy on 17-4-1.
 */

public class PickingPreviewAdapter extends RecyclerView.Adapter<PickingPreviewAdapter.ViewHolder> {

    private List<PickingPreviewItem> mData;

    public PickingPreviewAdapter(List<PickingPreviewItem> data) {
        mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_picking_preview, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PickingPreviewItem item = mData.get(position);
        holder.tvOrderNumber.setText((position + 1) + "");
        holder.tvShelfNumber.setText(item.shelfNumber);
        holder.tvIncode.setText(item.incode);
        holder.tvNum.setText(item.finishNum + "/" + item.totalNum);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvOrderNumber;
        private TextView tvShelfNumber;
        private TextView tvIncode;
        private TextView tvNum;

        public ViewHolder(View itemView) {
            super(itemView);
            tvOrderNumber = (TextView) itemView.findViewById(R.id.tv_order_number);
            tvShelfNumber = (TextView) itemView.findViewById(R.id.tv_shelf_number);
            tvIncode = (TextView) itemView.findViewById(R.id.tv_incode);
            tvNum = (TextView) itemView.findViewById(R.id.tv_num);
        }
    }

}
