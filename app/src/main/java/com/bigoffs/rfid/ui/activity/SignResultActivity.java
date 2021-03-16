package com.bigoffs.rfid.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bigoffs.rfid.R;
import com.bigoffs.rfid.mvp.bean.Allocation;
import com.bigoffs.rfid.mvp.bean.SignResult;
import com.bigoffs.rfid.mvp.biz.ISignBiz;
import com.bigoffs.rfid.mvp.biz.SignBiz;
import com.bigoffs.rfid.network.ICommonResult;
import com.bigoffs.rfid.ui.adapter.BaseListAdapter;
import com.bigoffs.rfid.ui.adapter.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

public class SignResultActivity extends BaseActivity {

    @BindView(R.id.tv_allacotion_id)
    TextView mTvAllacotionId;
    @BindView(R.id.ll_total)
    LinearLayout mLlTotal;
    @BindView(R.id.ll_receive)
    LinearLayout mLlReceive;
    @BindView(R.id.tv_left)
    TextView mTvLeft;
    @BindView(R.id.tv_right)
    TextView mTvRight;
    @BindView(R.id.lv_head)
    LinearLayout mLvHead;
    @BindView(R.id.lv)
    ListView mLv;
    @BindView(R.id.total_num)
    TextView mTotalNum;
    @BindView(R.id.sign_num)
    TextView mSignNum;
    private Context mContext;
    private String code;
    private String type;
    private ISignBiz mBiz;
    public static final String TAG = "SignResultActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_result);
        ButterKnife.bind(this);
        code = getIntent().getStringExtra("code");
        mContext = this;
        mBiz = new SignBiz();
        loadData();
    }

    private void loadData() {
        showLoading("");
        mBiz.getAllocation(mContext, "调拨单", code, TAG, new ICommonResult() {
            @Override
            public void onSuccess(String result) {
                hideLoading();
                Allocation allocation = Allocation.objectFromData(result);
                initData(allocation);
            }

            @Override
            public void onFail(Call call, Exception e) {
                hideLoading();
                finish();
            }

            @Override
            public void onInterrupt(int code, String message) {
                hideLoading();
                finish();
            }
        });

    }

    private void initData(Allocation allocation) {
        mTvAllacotionId.setText("调拨单号：" + allocation.getAllocationCode());
        mTotalNum.setText("" + allocation.getAllIncodeNumber());
        mSignNum.setText(""+allocation.getSignIncodeNumber());
        List<SignResult> results = new ArrayList<>();
        for (Allocation.BoxInfoBean boxInfoBean : allocation.getBoxInfo()) {
            int nosign = 0;
            int sign = 0;
            String boxCode = boxInfoBean.getBoxCode();
            for (Allocation.BoxInfoBean.NosignIncodesBean noSignIncodesBean : boxInfoBean.getNosignIncodes()) {
                nosign++;
            }
            for (Allocation.BoxInfoBean.SignIncodesBean signIncodesBean : boxInfoBean.getSignIncodes()) {
                sign++;
            }
            results.add(new SignResult(boxCode, sign, sign + nosign));
            mLv.setAdapter(new SignResultAdapter(mContext, results));
        }

    }

    class SignResultAdapter extends BaseListAdapter<SignResult> {


        public SignResultAdapter(Context context, List<SignResult> list) {
            super(context, list);
        }

        @Override
        public View bindView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_two_rows, null);
            }
            SignResult result = list.get(position);
            TextView tvLeft = ViewHolder.get(convertView, R.id.tv_left);

            TextView tvRight = ViewHolder.get(convertView, R.id.tv_right);

            tvLeft.setText(result.box);
            tvRight.setText(result.receiveNum + "/" + result.totalNum);

            return convertView;
        }
    }
}
