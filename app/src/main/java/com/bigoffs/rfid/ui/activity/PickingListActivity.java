package com.bigoffs.rfid.ui.activity;

import android.content.DialogInterface;
import android.os.Bundle;

import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bigoffs.rfid.R;
import com.bigoffs.rfid.mvp.bean.PickingList;
import com.bigoffs.rfid.mvp.biz.IPickingBiz;
import com.bigoffs.rfid.mvp.biz.PickingBiz;
import com.bigoffs.rfid.mvp.view.ICommonView;
import com.bigoffs.rfid.network.ICommonResult;
import com.bigoffs.rfid.ui.adapter.PickingListAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import okhttp3.Call;

/**
 * Created by okbuy on 17-3-25.
 */

public class PickingListActivity extends ScanBaseActivity implements ICommonView, PickingListAdapter.OnButtonClickListener {

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private RecyclerView mRecyclerView;

    private IPickingBiz mBiz;

    private List<PickingList> mData;

    private View mEmptyView;

    private TextView tvQuery;

    private EditText etIncode;

    private int lastVisibleItem;

    private PickingListAdapter adapter;

    private int page  = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mBiz = new PickingBiz();
        setContentView(R.layout.activity_picking_list);
        initViews();
    }

    private void initViews() {
        etIncode = (EditText) findViewById(R.id.et_incode);

        etIncode.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (event != null)
                    if (event.getAction() == KeyEvent.ACTION_UP) return true;
                if (actionId == EditorInfo.IME_ACTION_SEND || actionId == 0) {
                    if (etIncode.getText().toString().equals("")) {
                        showDialog("请输入标签~");
                    } else {

                        onReceiverData(etIncode.getText().toString());
                        etIncode.setText("");
                    }
                    return true;
                }
                return false;
            }
        });
        tvQuery = (TextView) findViewById(R.id.btn_commit);
        tvQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etIncode.getText().toString().equals("")){
                    showNotice("请输入拣货单号");
                }else{
                    query(etIncode.getText().toString());
                }
            }
        });
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData(false);
            }
        });

        mEmptyView = findViewById(R.id.tv_empty);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_picking_list);
        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        // 因为每个Item高度固定，所以设置这个选项可以提高性能
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        //上拉加载
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(final RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(mSwipeRefreshLayout.isRefreshing()) return;
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem == adapter.getItemCount() - 1) {
                    showLoading("");
                    page++;
                    mBiz.getPickingList(PickingListActivity.this, page,20,"PickingListActivity", new ICommonResult() {
                        @Override
                        public void onSuccess(String result) {
                            hideLoading();
                            mSwipeRefreshLayout.setRefreshing(false);
                            Gson gson = new Gson();
                            List<PickingList> list =  gson.fromJson(result, new TypeToken<List<PickingList>>() {
                            }.getType());
                            if(list.size() == 0){
                                showNotice("没有更多了");
                            }
                            mData.addAll(list);
                            adapter.notifyDataSetChanged();
                            if (mData.size() == 0) {
                                mEmptyView.setVisibility(View.VISIBLE);
                            } else {
                                mEmptyView.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onFail(Call call, Exception e) {
                            hideLoading();
                            mSwipeRefreshLayout.setRefreshing(false);
                        }

                        @Override
                        public void onInterrupt(int code, String message) {
                            hideLoading();
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                    });
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData(true);
    }

    private void getData(boolean showLoading) {
        if (showLoading) {
            showLoading("");
        }
        page = 1;
        mBiz.getPickingList(this, page,20,"PickingListActivity", new ICommonResult() {
            @Override
            public void onSuccess(String result) {
                hideLoading();
                mSwipeRefreshLayout.setRefreshing(false);
                Gson gson = new Gson();
                mData = gson.fromJson(result, new TypeToken<List<PickingList>>() {
                }.getType());
                adapter = new PickingListAdapter(mData, PickingListActivity.this);
                mRecyclerView.setAdapter(adapter);
                if (mData.size() == 0) {
                    mEmptyView.setVisibility(View.VISIBLE);
                } else {
                    mEmptyView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFail(Call call, Exception e) {
                hideLoading();
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onInterrupt(int code, String message) {
                hideLoading();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onButtonClick(View v, final int position) {
        new AlertDialog.Builder(this).setMessage("确定领取该拣货单？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                collectPickingList(mData.get(position).id+"");
            }
        }).setNegativeButton("取消",null).show();
    }

    // 领取拣货单
    private void collectPickingList(String code) {
        showLoading("");
        mBiz.collectPickingList(this, code, "PickingListActivity", new ICommonResult() {
            @Override
            public void onSuccess(String result) {
                hideLoading();
                int taskId = 0;
                String outstockCode  = "";
                try {
                    JSONObject object =  new JSONObject(result);
                    taskId = object.optInt("task_id");
                    outstockCode = object.optString("outstock_code");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                showNotice("领取成功");
                PickingDetailActivityNew.show(PickingListActivity.this, taskId,result);
                finish();
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

    void query(String code){
      showLoading("");
        page = 1;
            mBiz.queryPicking(this, code, "PickingListActivity", new ICommonResult() {
                @Override
                public void onSuccess(String result) {
                    Gson gson = new Gson();
                    mData = gson.fromJson(result, new TypeToken<List<PickingList>>() {
                    }.getType());
                    adapter = new PickingListAdapter(mData, PickingListActivity.this);
                    mRecyclerView.setAdapter(adapter);

                    if (mData.size() == 0) {
                        mEmptyView.setVisibility(View.VISIBLE);
                    } else {
                        mEmptyView.setVisibility(View.GONE);
                    }
                    hideLoading();
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


    public void onReceiverData(String data) {
         if (TextUtils.isEmpty(data.trim())) {
            showNotice("拣货单号不能为空！");
            return;
        } {
            query(data);
        }
    }


    protected void onScan(String barcode) {
        onReceiverData(barcode);
    }

    @Override
    public void OnFinish(String data) {

    }

    @Override
    public void ShowData(Object o) {

    }
}
