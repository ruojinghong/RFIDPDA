package com.bigoffs.rfid.ui.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.bigoffs.rfid.R;
import com.bigoffs.rfid.mvp.bean.PickingPreviewItem;
import com.bigoffs.rfid.mvp.biz.IPickingBiz;
import com.bigoffs.rfid.mvp.biz.PickingBiz;
import com.bigoffs.rfid.network.ICommonResult;
import com.bigoffs.rfid.ui.adapter.PickingPreviewAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import okhttp3.Call;

public class PickingPreviewActivity  extends BaseActivity {

    private RecyclerView mRecyclerView;
    private IPickingBiz mBiz = new PickingBiz();

    public static void show(Context context, int taskId) {
        Intent intent = new Intent(context, PickingPreviewActivity.class);
        intent.putExtra("taskId", taskId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_picking_preview);
        initViews();
        loadData();
    }

    private void initViews() {
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_picking_preview);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        // 因为每个Item高度固定，所以设置这个选项可以提高性能
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
    }

    private void loadData() {
        showLoading("");
        mBiz.getPickingPreviewList(this, getIntent().getIntExtra("taskId", 0), "PickingPreviewActivity", new ICommonResult() {
            @Override
            public void onSuccess(String result) {
                hideLoading();
                Gson gson = new Gson();
                List<PickingPreviewItem> data = gson.fromJson(result, new TypeToken<List<PickingPreviewItem>>() {
                }.getType());
                mRecyclerView.setAdapter(new PickingPreviewAdapter(data));
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
}
