package com.bigoffs.rfid.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.bigoffs.rfid.R;
import com.bigoffs.rfid.mvp.bean.SkuQueryInfo;
import com.bigoffs.rfid.mvp.presenter.IQuerySkuPresenter;
import com.bigoffs.rfid.mvp.presenter.SkuQueryPresenter;
import com.bigoffs.rfid.mvp.view.ISkuQueryView;
import com.bigoffs.rfid.ui.adapter.BaseListAdapter;
import com.bigoffs.rfid.ui.adapter.SkuQueryAdapter;
import com.bigoffs.rfid.util.LogUtil;

import java.util.List;

public class SkuQueryActivity extends  BaseActivity implements ISkuQueryView {

    private IQuerySkuPresenter presenter = new SkuQueryPresenter(this,this);
    private ListView mListView;
    private TextView tvNon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_sku_query);
        initView();
        presenter.load(getIntent().getStringExtra("sku"));
    }

    private void initView() {
        mListView  = (ListView) findViewById(R.id.listview);
        tvNon = (TextView) findViewById(R.id.tv_no_info);
    }

    @Override
    public void setAdatper(List<SkuQueryInfo> data) {
        View headerView = LayoutInflater.from(this).inflate(R.layout.header_sku_query_info,null);
        mListView.addHeaderView(headerView);
        SkuQueryAdapter adapter = new SkuQueryAdapter(this,data);
        adapter.setOnInViewClickListener(R.id.tv_num, new BaseListAdapter.onInternalClickListener() {
            @Override
            public void OnClickListener(View parentV, View v, Integer position, Object values) {
                Intent intent = new Intent(SkuQueryActivity.this,FindActivity.class);
                intent.putExtra("rfid","");
                intent.putExtra("shelf",adapter.list.get(position).shelf);
                startAnimActivity(intent);
            }
        });
        mListView.setAdapter(adapter);

    }

    @Override
    public void showBlank() {
        mListView.setVisibility(View.GONE);
        tvNon.setText("暂无数据");
    }
}

