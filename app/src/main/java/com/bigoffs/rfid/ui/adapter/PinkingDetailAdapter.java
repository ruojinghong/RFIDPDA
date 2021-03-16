package com.bigoffs.rfid.ui.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigoffs.rfid.R;
import com.bigoffs.rfid.mvp.bean.PickingDetailNew;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;


/**
 * @author okbuy-001
 * @desc
 * @time 2018/12/18 11:33
 */
public class PinkingDetailAdapter extends BaseExpandableListAdapter {

    private Dialog mDialog;
    private Context mContext;

    private PickingDetailNew data;

    public PinkingDetailAdapter(Context context, PickingDetailNew data){
        mContext = context;
        this.data = data;
        mDialog = new Dialog(mContext, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
    }

    @Override
    public int getGroupCount() {
        return data.pickList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return data.pickList.get(groupPosition).data.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return data.pickList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return  data.pickList.get(groupPosition).data.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder groupViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_expand_group_picking, parent, false);
            groupViewHolder = new GroupViewHolder();
            groupViewHolder.tvShelf = (TextView) convertView.findViewById(R.id.tv_shelf);
            groupViewHolder.tvNum = (TextView) convertView.findViewById(R.id.tv_num);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }
        groupViewHolder.tvShelf.setText(data.pickList.get(groupPosition).shelf);
        int count = 0;
        int itemCancel = 0;
        for(int i=0;i<data.pickList.get(groupPosition).data.size();i++){
            if(data.pickList.get(groupPosition).data.get(i).status == 2){
                count++;
            }else if(data.pickList.get(groupPosition).data.get(i).status == 3){
                itemCancel++;
            }
        }
        groupViewHolder.tvNum.setText(count+"/"+(data.pickList.get(groupPosition).data.size()-itemCancel));

        return convertView;

    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder childViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_expand_group_child_picking, parent, false);
            childViewHolder = new ChildViewHolder();
            childViewHolder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
            childViewHolder.tvBarCode = (TextView) convertView.findViewById(R.id.tv_barcode);
            childViewHolder.tvIncode = (TextView) convertView.findViewById(R.id.tv_incode);
            childViewHolder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
            childViewHolder.tvStatues = (TextView) convertView.findViewById(R.id.tv_status);
            childViewHolder.tvSize = (TextView) convertView.findViewById(R.id.tv_size);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }
        final ImageView iv = (ImageView) convertView.findViewById(R.id.iv_icon);
        Glide.with(mContext).load(data.pickList.get(groupPosition).data.get(childPosition).picUrl).placeholder(R.drawable.image_default).into(childViewHolder.ivIcon);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView i = new ImageView(mContext);
                i.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDialog.dismiss();
                    }
                });
                i.setImageDrawable(iv.getDrawable());
                mDialog.setContentView(i);
                mDialog.show();
            }
        });
        childViewHolder.tvBarCode.setText(data.pickList.get(groupPosition).data.get(childPosition).barcode);
        childViewHolder.tvIncode.setText(data.pickList.get(groupPosition).data.get(childPosition).incode);
        childViewHolder.tvName.setText(data.pickList.get(groupPosition).data.get(childPosition).brandName);
        childViewHolder.tvSize.setText(data.pickList.get(groupPosition).data.get(childPosition).size);
        if(data.pickList.get(groupPosition).data.get(childPosition).status == 2){
            //标记为绿色
            childViewHolder.tvStatues.setText("已捡");
            childViewHolder.tvStatues.setTextColor(Color.rgb(00,64,00));
            childViewHolder.tvBarCode.setTextColor(Color.rgb(00,64,00));
            childViewHolder.tvIncode.setTextColor(Color.rgb(00,64,00));
            childViewHolder.tvName.setTextColor(Color.rgb(00,64,00));
            childViewHolder.tvSize.setTextColor(Color.rgb(00,64,00));
        }else if(data.pickList.get(groupPosition).data.get(childPosition).status == 3){
            childViewHolder.tvStatues.setText("已取消");
            childViewHolder.tvStatues.setTextColor(Color.RED);
            childViewHolder.tvBarCode.setTextColor(Color.RED);
            childViewHolder.tvIncode.setTextColor(Color.RED);
            childViewHolder.tvName.setTextColor(Color.RED);
            childViewHolder.tvSize.setTextColor(Color.RED);
        }else {
            childViewHolder.tvStatues.setText("未拣");
            childViewHolder.tvStatues.setTextColor(Color.BLACK);
            childViewHolder.tvBarCode.setTextColor(Color.BLACK);
            childViewHolder.tvIncode.setTextColor(Color.BLACK);
            childViewHolder.tvName.setTextColor(Color.BLACK);
            childViewHolder.tvSize.setTextColor(Color.BLACK);
        }
        return convertView;

    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    static class GroupViewHolder {
        TextView tvShelf;
        TextView tvNum;
    }
    static class ChildViewHolder {
        ImageView ivIcon;
        TextView tvBarCode;
        TextView tvIncode; TextView tvName;
        TextView tvStatues; TextView tvSize;

    }

}
