package com.bigoffs.rfid.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigoffs.rfid.R;
import com.bigoffs.rfid.mvp.bean.GroundingBillList;


/**
 * @author okbuy-001
 * @desc
 * @time 2018/12/6 17:47
 */
public class GroundingListAdapter extends BaseAdapter {

    private Context mContext;

    private GroundingBillList data;
    private AdapterBtnCallBack callBack;

    public GroundingListAdapter(Context context , GroundingBillList data, AdapterBtnCallBack callBack){
            mContext = context;
            this.data = data;
            this.callBack =  callBack;
    }

    public interface AdapterBtnCallBack{
        public void ClickCallBack(int position, int type);
    }

    @Override
    public int getCount() {
        if (data.arrival!=null && data.arrival.size()>0){
            return data.arrival.size();
        }else{
            return data.allocation.size();
        }

    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ArrivalViewHolder holder = null;
        AllocationViewHolder allocationHolder = null;
        int type ;
        if(data.arrival == null){
            type =  1;
        }else{
            type = 0;
        }
        if(convertView == null){
            switch (type) {
                case 0:
                holder = new ArrivalViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_grounding_list, null);
                holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
                holder.tvArrivalNum = (TextView) convertView.findViewById(R.id.tv_arrival_num);
                holder.tvArrivalTime = (TextView) convertView.findViewById(R.id.tv_arrival_time);
                holder.tvBuyer = (TextView) convertView.findViewById(R.id.tv_buyer);
                holder.tvNote = (TextView) convertView.findViewById(R.id.tv_note);
                holder.tvNum = (TextView) convertView.findViewById(R.id.tv_num);
                holder.tvStatus = (TextView) convertView.findViewById(R.id.tv_status);
                holder.tvSupplier = (TextView) convertView.findViewById(R.id.tv_supplier);
                holder.llTitle = (LinearLayout) convertView.findViewById(R.id.ll_title);
                holder.btReceive = (Button) convertView.findViewById(R.id.bt_receive);
                convertView.setTag(holder);
                break;
                case 1:
                    allocationHolder = new AllocationViewHolder();
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.item_grounding_alloction_list,null);
                    allocationHolder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
                    allocationHolder.tvAllocationNum = (TextView) convertView.findViewById(R.id.tv_allocation_number);
                    allocationHolder.tvArrivalTime = (TextView) convertView.findViewById(R.id.tv_arrival_time);
                    allocationHolder.tvBuyer = (TextView) convertView.findViewById(R.id.tv_buyer);
                    allocationHolder.tvStatus = (TextView) convertView.findViewById(R.id.tv_status);
                    allocationHolder.tvNum = (TextView) convertView.findViewById(R.id.tv_into_num);
                    allocationHolder.tvStockName = (TextView) convertView.findViewById(R.id.tv_stock_name);
                    allocationHolder.tvNote = (TextView) convertView.findViewById(R.id.tv_note);
                    allocationHolder.btReceive = (Button) convertView.findViewById(R.id.bt_receive);
                    convertView.setTag(allocationHolder);
                    break;
            }
        }
            switch (type) {
                case 0:
                holder = (ArrivalViewHolder) convertView.getTag();
                holder.tvTitle.setText("到货单:" + data.arrival.get(position).arrivalCode);
                holder.tvStatus.setText("状态:" + data.arrival.get(position).status);
                holder.tvArrivalTime.setText("到货时间:" + data.arrival.get(position).arrival_time);
                holder.tvBuyer.setText("买手:" + data.arrival.get(position).buyer);
                holder.tvNum.setText("预约到货数量:" + data.arrival.get(position).count);
                holder.tvArrivalNum.setText("入库数量:" + data.arrival.get(position).instockCount);
                holder.tvSupplier.setText("供应商:" + data.arrival.get(position).supplier);
                holder.tvNote.setText("备注:" + data.arrival.get(position).note);
                holder.btReceive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callBack.ClickCallBack(position,0);
                    }
                });
                break;
                case 1:
                    allocationHolder = (AllocationViewHolder) convertView.getTag();
                                allocationHolder.tvTitle.setText("调拨单"+data.allocation.get(position).allocationCode);
                                allocationHolder.tvStatus.setText("状态:"+data.allocation.get(position).status);
                                allocationHolder.tvArrivalTime.setText("到货时间:"+data.allocation.get(position).arrivalTime);
                                allocationHolder.tvBuyer.setText("创建人:"+data.allocation.get(position).admin);
                                allocationHolder.tvAllocationNum.setText("预约到货数量:"+data.allocation.get(position).allocationNumber);
                                allocationHolder.tvStockName.setText("源仓:"+data.allocation.get(position).stockName);
                                allocationHolder.tvNum.setText("入库数量:"+data.allocation.get(position).intoNum);
                                allocationHolder.tvNote.setText("备注:"+data.allocation.get(position).remark);
                    allocationHolder.btReceive.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            callBack.ClickCallBack(position,1);
                        }
                    });

                    break;
            }
        return convertView;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if(data.arrival == null){
            return 1;
        }else{
            return 0;
        }
    }

    class ArrivalViewHolder{
        TextView tvTitle;
        TextView tvStatus;
        TextView tvArrivalTime;
        TextView tvBuyer;
        TextView tvNum;
        TextView tvArrivalNum;
        TextView tvSupplier;
        TextView tvNote;
        LinearLayout llTitle;
        Button btReceive;
    }
    class AllocationViewHolder{
        TextView tvTitle;
        TextView tvStatus;
        TextView tvArrivalTime;
        TextView tvBuyer;
        TextView tvNum;
        TextView tvAllocationNum;
        TextView tvStockName;
        TextView tvNote;
        Button btReceive;
    }
}
