package com.bigoffs.rfid.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bigoffs.rfid.R;
import com.bigoffs.rfid.mvp.bean.dao.InventoryCase;
import com.bigoffs.rfid.mvp.presenter.InventoryPresenter;
import com.bigoffs.rfid.mvp.view.IInventoryView;
import com.bigoffs.rfid.persistence.util.SoundUtils;
import com.bigoffs.rfid.ui.adapter.BaseListAdapter;
import com.bigoffs.rfid.ui.adapter.ViewHolder;
import com.bigoffs.rfid.ui.custom.SquareRelativeLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author okbuy-001
 * @desc
 * @time 2020/5/14 17:24
 */
public class InventoryActivity extends ScanBaseActivity implements IInventoryView {


    @BindView(R.id.tv_shelf_area)
    TextView mTvShelfArea;
    @BindView(R.id.tv_content)
    TextView mTvContent;
    @BindView(R.id.tv_all_scan)
    TextView mTvAllScan;
    @BindView(R.id.rl_open_or_close)
    SquareRelativeLayout mRlOpenOrClose;
    @BindView(R.id.tv_total_member)
    TextView mTvTotalMember;
    @BindView(R.id.tv_scan_member)
    TextView mTvScanMember;
    @BindView(R.id.ll_head)
    LinearLayout mLlHead;
    @BindView(R.id.tv_reset)
    TextView mTvReset;
    @BindView(R.id.tv_record)
    TextView mTvRecord;
    @BindView(R.id.ll_bottom)
    LinearLayout mLlBottom;
    @BindView(R.id.lv_error)
    ListView mLvError;
    private InventoryPresenter mPresenter ;
    private Adapter adapter;
    private Context mContext;
    private int scanNum = 0;
    private String taskId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);
        ButterKnife.bind(this);
        mContext = this;
        taskId = getIntent().getStringExtra("id");
        mPresenter = new InventoryPresenter(mContext,this,taskId+"");
        adapter = new Adapter(this,mPresenter.getList());
        mPresenter.loadData(taskId+"");
        mLvError.setAdapter(adapter);
        ((TextView)findViewById(R.id.title_left)).setText("货架号");

    }

    public static void show(Context context, String id) {
            Intent it  = new Intent(context,InventoryActivity.class);
            it.putExtra("id",id);
            context.startActivity(it);

    }

    @Override
    public void OnFinish(String data) {
        SoundUtils.play(1);
        mTvAllScan.setText(scanPresenter.getNum()+"");
        mPresenter.query(data);
    }

    @Override
    public void ShowData(Object o) {

    }



    @OnClick({R.id.rl_open_or_close, R.id.tv_reset, R.id.tv_record})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_open_or_close:
                openOrClose();
                break;
            case R.id.tv_reset:
                reset();
                break;
            case R.id.tv_record:
                updata();
                break;
        }
    }

    private void updata() {
        if (mTvContent.getText().toString().equals("停止扫描")) { openOrClose();}
        mPresenter.upLoad();
    }

    private void reset() {
        if (mTvContent.getText().toString().equals("停止扫描")) { openOrClose();}
        scanPresenter.initData();
        mTvAllScan.setText("0");
        mPresenter.reset();
        adapter.notifyDataSetChanged();
       mPresenter.clearTable();
       scanNum = 0;
        mPresenter.loadData(taskId+"");
    }

    @Override
    public void updataErrorList() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void initData(int size) {
            mLlHead.post(()->{
                mTvTotalMember.setText(size+"");
                hideLoading();


            });
    }

    @Override
    public void addScanNum() {
            scanNum++;
            mTvScanMember.setText(scanNum+"");
    }

    @Override
    public void finishTask(String result) {
        showNotice("盘点完成");
        InventoryDetailActivityNew.show(this, result);
        finish();
    }

    @Override
    public void setShelfArea(String shelfArea) {
        mTvShelfArea.setText(shelfArea);
    }


    class Adapter extends BaseListAdapter<InventoryCase> {

        public Adapter(Context context, List<InventoryCase> list) {
            super(context, list);
        }

        @Override
        public View bindView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_three_rows, null);
            }

            InventoryCase rfid = list.get(position);
            TextView tvLeft = ViewHolder.get(convertView, R.id.tv_left);
            TextView tvCenter = ViewHolder.get(convertView, R.id.tv_center);
            TextView tvRight = ViewHolder.get(convertView, R.id.tv_right);
            tvRight.setTextColor(Color.RED);
            tvLeft.setText(rfid.getShelfCode());
            if (TextUtils.isEmpty(rfid.getIncode())) {
                tvCenter.setText(rfid.getRfidCode());
            } else {
                tvCenter.setText(rfid.getIncode());
            }

            tvRight.setText("盘盈或串位");

            return convertView;
        }
    }

    public void openOrClose() {
        if (mTvContent.getText().toString().equals("停止扫描")) {
            scanPresenter.stopReadRfid();
            mRlOpenOrClose.setBackground(getDrawable(R.drawable.bg_circle));
//            mActivity.tbCommon.setVisibility(View.VISIBLE);

            mTvContent.setText("开始扫描");
        } else {


            scanPresenter.startReadRfid();

            mRlOpenOrClose.setBackground(getDrawable(R.drawable.bg_circle_red));
//                mActivity.tbCommon.setVisibility(View.INVISIBLE);
            mTvContent.setText("停止扫描");

        }

    }
}
