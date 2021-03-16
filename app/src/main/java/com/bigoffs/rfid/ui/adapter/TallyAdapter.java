package com.bigoffs.rfid.ui.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bigoffs.rfid.R;
import com.bigoffs.rfid.mvp.bean.TransferInfo;


import java.util.List;

/**
 * @author okbuy-001
 * @desc
 * @time 2018/12/11 17:51
 */
public class TallyAdapter extends RecyclerView.Adapter<TallyAdapter.VH> {


    private List<TransferInfo.FailIncodesBean> list;

    public TallyAdapter(List<TransferInfo.FailIncodesBean> list){
        this.list = list;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tally,parent,false);
        return new TallyAdapter.VH(v);
    }

    @Override
    public void onBindViewHolder(TallyAdapter.VH holder, int position) {
        holder.tvInCode.setText(list.get(position).getIncode());
        holder.tvShelfCode.setText(list.get(position).getRfidCode());
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
}