package com.bigoffs.rfid.ui.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bigoffs.rfid.R;
import com.bigoffs.rfid.persistence.util.SPUtils;
import com.bigoffs.rfid.service.IScanService;
import com.bigoffs.rfid.service.ScanServiceControl;
import com.bigoffs.rfid.ui.custom.SquareRelativeLayout;
import com.bigoffs.rfid.util.TakeStockTransaction;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author okbuy-001
 * @desc
 * @time 2020/4/23 16:41
 */
public class HomeActivity extends BaseActivity {
    @BindView(R.id.rl_query)
    SquareRelativeLayout mRlQuery;
    @BindView(R.id.rl_tally)
    SquareRelativeLayout mRlTally;
    @BindView(R.id.rl_inventory)
    SquareRelativeLayout mRlInventory;
    @BindView(R.id.rl_sign)
    SquareRelativeLayout mRlSign;
    @BindView(R.id.rl_storage)
    SquareRelativeLayout mRlStorage;
    @BindView(R.id.rl_pick)
    SquareRelativeLayout mRlPick;
    @BindView(R.id.rl_task)
    SquareRelativeLayout mRlTask;
    private boolean flag = false;
    private IScanService scanService = ScanServiceControl.getScanService();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        //初始化采集标签头
        startUHFWithCW();
        //初始化扫描条码头
//        openBarCodeReader();
        TakeStockTransaction.init(this);
    }

    @OnClick({R.id.rl_query, R.id.rl_tally, R.id.rl_inventory, R.id.rl_sign, R.id.rl_storage, R.id.rl_pick, R.id.rl_task})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_query:
                //查询界面
                startAnimActivity(QueryActivity.class);
                break;
            case R.id.rl_tally:
                //理货界面
                startAnimActivity(TallyActivity.class);
                break;
            case R.id.rl_inventory:
                //盘点
                startAnimActivity(InventoryListActivity.class);
                break;
            case R.id.rl_sign:
                //调拨签收
                startAnimActivity(SignActivity.class);
                break;
            case R.id.rl_storage:
                //入库
                startAnimActivity(GroundingActivity.class);
                break;
            case R.id.rl_pick:
                //拣货
                startAnimActivity(PickingListActivity.class);
                break;
            case R.id.rl_task:
                //任务
                startAnimActivity(TaskActivity.class);
                break;
        }
    }

    private void startUHFWithCW() {
        try {
            scanService.init(this);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        new InitTask().execute();
    }

    public class InitTask extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            return scanService.openReader();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (!result) {
                Toast.makeText(HomeActivity.this, "启动扫描头失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void openBarCodeReader() {

        SPUtils.put(this, "barCodeReaderMode", 2);

        if (flag) {
            boolean close = scanService.closeBarcodeReader();
            if (close) {
                flag = false;
            } else {
                showDialog("关闭条码扫描失败");
            }
        } else {

            boolean open = scanService.openBarcodeReader(this);
            if (open) {
                flag = true;
            } else {
                showDialog("开启条码扫描失败");
            }

        }
    }

}
