package com.bigoffs.rfid.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bigoffs.rfid.R;
import com.bigoffs.rfid.mvp.bean.TaskInfo;


import java.util.List;

/**
 * Created by okbuy on 17-2-21.
 */

public class TaskListAdapter extends BaseAdapter {

    public List<TaskInfo> mInfos;
    private Context mContext;

    public TaskListAdapter(Context context, List<TaskInfo> infos) {
        mContext = context;
        mInfos = infos;
    }

    public void addData(List<TaskInfo> infos) {
        mInfos.addAll(infos);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mInfos.size();
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
            view = LayoutInflater.from(mContext).inflate(R.layout.item_task_list, null);
            holder = new ViewHolder();
            holder.tvNumber = (TextView) view.findViewById(R.id.tv_number);
            holder.tvType = (TextView) view.findViewById(R.id.tv_type);
            holder.tvTime = (TextView) view.findViewById(R.id.tv_time);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        TaskInfo info = mInfos.get(i);
        holder.tvNumber.setText((i + 1) + "");
        holder.tvType.setText(info.typeName);
        holder.tvTime.setText(info.createTime);
        return view;
    }

    public class ViewHolder {
        TextView tvNumber;
        TextView tvType;
        TextView tvTime;
    }
}
