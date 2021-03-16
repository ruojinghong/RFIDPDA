package com.bigoffs.rfid.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;


import androidx.annotation.Nullable;

import com.bigoffs.rfid.R;
import com.bigoffs.rfid.mvp.bean.InventoryInfoNew;
import com.bigoffs.rfid.mvp.bean.SerializeTallyProduct;
import com.bigoffs.rfid.ui.adapter.TallyDetailAdapter;
import com.bigoffs.rfid.ui.adapter.TallyDetailOnlyIncodeAdapter;

import java.util.List;

/**
 * Created by okbuy on 17-2-17.
 * 理货-货品明细页面
 */

public class TallyDetailActivity extends BaseActivity {

    private ListView mListView;

    private TextView tvTitle;

    private TextView tvCount;

    private TextView tv_2;
    private TextView tv_3;

    public static void show(Context context, List<InventoryInfoNew> data) {
        Intent intent = new Intent(context, TallyDetailActivity.class);
        intent.putExtra("data", new SerializeTallyProduct(data));
        context.startActivity(intent);
    }

    public static void show(Context context, List<InventoryInfoNew> data, String title) {
        Intent intent = new Intent(context, TallyDetailActivity.class);
        intent.putExtra("data", new SerializeTallyProduct(data));
        intent.putExtra("title", title);
        context.startActivity(intent);
    }

    public static void show(Context context, List<InventoryInfoNew> data, String title, boolean onlyIncode) {
        Intent intent = new Intent(context, TallyDetailActivity.class);
        intent.putExtra("data", new SerializeTallyProduct(data));
        intent.putExtra("title", title);
        intent.putExtra("onlyIncode", onlyIncode);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String title = getIntent().getStringExtra("title");
        if (!TextUtils.isEmpty(title)) {
            getSupportActionBar().setTitle(title);
        }
        setContentView(R.layout.activity_tally_detail);

        mListView = (ListView) findViewById(R.id.lv_tally_detail);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvCount = (TextView) findViewById(R.id.tv_count);
        tv_2 = (TextView) findViewById(R.id.tv_two);
        tv_3 = (TextView) findViewById(R.id.tv_shelf_number);
        tvTitle.setText(title+":");
        tvCount.setText(((SerializeTallyProduct)getIntent().getSerializableExtra("data")).data.size()+"");
        boolean onlyIncode = getIntent().getBooleanExtra("onlyIncode", false);
        if (onlyIncode) {
            findViewById(R.id.tv_state).setVisibility(View.GONE);
            mListView.setAdapter(new TallyDetailOnlyIncodeAdapter(this, ((SerializeTallyProduct) getIntent().getSerializableExtra("data")).data));
        } else {
            tv_2.setText("盘点货架号");
            tv_3.setText("正确货架号");
            mListView.setAdapter(new TallyDetailAdapter(this, ((SerializeTallyProduct) getIntent().getSerializableExtra("data")).data));
        }
    }
}
