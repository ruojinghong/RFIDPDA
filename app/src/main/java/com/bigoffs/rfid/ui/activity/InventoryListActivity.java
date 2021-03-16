package com.bigoffs.rfid.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bigoffs.rfid.R;
import com.bigoffs.rfid.mvp.bean.InventoryItemInfo;
import com.bigoffs.rfid.mvp.biz.InventoryBiz;
import com.bigoffs.rfid.network.ICommonResult;
import com.bigoffs.rfid.ui.adapter.BaseListAdapter;
import com.bigoffs.rfid.ui.adapter.ViewHolder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

public class InventoryListActivity extends BaseActivity {

    @BindView(R.id.tv_null)
    TextView mTvNull;
    private InventoryBiz mbiz;

    @BindView(R.id.lv)
    ListView mLv;
    private Context mContext;
    private static final String TAG = "InventoryListActivity";
    List<InventoryItemInfo> data = new ArrayList<>();
    private Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invertory_list);
        ButterKnife.bind(this);
        mContext = this;
        mbiz = new InventoryBiz();


        loadData();
    }

    private void loadData() {
        showLoading("");
        mbiz.getInventoryList(mContext, TAG, new ICommonResult() {
            @Override
            public void onSuccess(String result) {
                hideLoading();
                data = new Gson().fromJson(result, new TypeToken<List<InventoryItemInfo>>() {
                }.getType());
                adapter = new Adapter(mContext, data);
                adapter.setOnInViewClickListener(R.id.btn_in, new BaseListAdapter.onInternalClickListener() {
                    @Override
                    public void OnClickListener(View parentV, View v, Integer position, Object values) {
                        Intent it = new Intent(mContext, InventoryActivity.class);
                        it.putExtra("shelfs", data.get(position).getShelfArea());
                        it.putExtra("id", data.get(position).getTaskId());

                        startAnimActivity(it);
                    }
                });
                mLv.setAdapter(adapter);
                if (data.size() == 0) {
                    mTvNull.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFail(Call call, Exception e) {
                hideLoading();
            }

            @Override
            public void onInterrupt(int code, String message) {
                hideLoading();
            }
        });


    }

    class Adapter extends BaseListAdapter<InventoryItemInfo> {


        public Adapter(Context context, List<InventoryItemInfo> list) {
            super(context, list);
        }

        @Override
        public View bindView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_inventory_list, null);
            }

            InventoryItemInfo info = list.get(position);
            TextView tvId = ViewHolder.get(convertView, R.id.tv_id);
            TextView tvShelf = ViewHolder.get(convertView, R.id.tv_shelf);
            TextView tvTotalNum = ViewHolder.get(convertView, R.id.tv_total_num);
            TextView tvCreatTime = ViewHolder.get(convertView, R.id.tv_creat_time);
            ProgressBar progressBar = ViewHolder.get(convertView, R.id.progressBar);


            tvId.setText("ID:" + info.getTaskId());
            tvShelf.setText("货架位:" + info.getShelfArea());
            tvCreatTime.setText("创建时间：" + info.getCreateTime());


            return convertView;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
