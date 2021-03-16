package com.bigoffs.rfid.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.bigoffs.rfid.R;
import com.bigoffs.rfid.listener.ISignListener;
import com.bigoffs.rfid.listener.OnFinishListener;
import com.bigoffs.rfid.mvp.bean.Allocation;
import com.bigoffs.rfid.mvp.bean.AllocationError;
import com.bigoffs.rfid.mvp.bean.dao.AllcationCase;
import com.bigoffs.rfid.mvp.presenter.ScanPresenter;
import com.bigoffs.rfid.mvp.presenter.SignDetailPresenter;
import com.bigoffs.rfid.mvp.view.IDataFragmentView;
import com.bigoffs.rfid.mvp.view.ISignView;
import com.bigoffs.rfid.persistence.util.SoundUtils;
import com.bigoffs.rfid.ui.adapter.BaseListAdapter;
import com.bigoffs.rfid.ui.adapter.ViewHolder;
import com.bigoffs.rfid.ui.custom.SignDialog;
import com.bigoffs.rfid.ui.custom.SquareRelativeLayout;
import com.bigoffs.rfid.util.LogUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 调拨签收
 */
public class SignDetailActivity extends BaseActivity implements ISignView<AllcationCase>, IDataFragmentView<AllcationCase>,OnFinishListener{

    @BindView(R.id.tv_allacotion_id)
    TextView mTvAllacotionId;
    @BindView(R.id.tv_all_member)
    TextView mTvAllMember;
    @BindView(R.id.tv_all_box_member)
    TextView mTvAllBoxMember;
    @BindView(R.id.tv_sign_member)
    TextView mTvSignMember;
    @BindView(R.id.tv_all_sign_member)
    TextView mTvAllSignMember;
    @BindView(R.id.tv_content)
    TextView mTvContent;
    @BindView(R.id.rl_open_or_close)
    SquareRelativeLayout mRlOpenOrClose;
    @BindView(R.id.tv_should_sign_num)
    TextView mTvShouldSignNum;
    @BindView(R.id.tv_error_num)
    TextView mTvErrorNum;
    @BindView(R.id.lv_error)
    ListView mLvError;
    @BindView(R.id.tv_all_scan)
    TextView mTvAllScan;
    @BindView(R.id.tv_part_sign)
    TextView tvPartSign;
    @BindView(R.id.et_box)
    EditText mEtBox;
    @BindView(R.id.tv_clear_box)
    TextView mTvClearBox;
    @BindView(R.id.tv_deal)
    TextView mTvDeal;
    @BindView(R.id.tv_clear)
    TextView mTvClear;
    @BindView(R.id.tv_sign_allocation)
    TextView mTvSignAllocation;

    @BindView(R.id.ll_head)
    LinearLayout mLlHead;
    @BindView(R.id.ll_bottom)
    LinearLayout mLlBottom;
    @BindView(R.id.tv_box_id)
    TextView mTvBoxId;
    @BindView(R.id.ll_current_box)
    LinearLayout mLlCurrentBox;
    private String type;
    private String code;
    private SignDetailPresenter presenter;
    private ScanPresenter scanPresenter;

    private int countScanNum;
    //扫描到的合法的RFID
    private List<String> legitimateList;
    private SignDialog dialog;
    //当前箱号
    private String currentBox;
    private Adapter errorAdapter;
    private List<AllocationError> errorList;
    //统计一次签收扫描到的数据
    private int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_detail);
        ButterKnife.bind(this);
        type = getIntent().getStringExtra("type");
        code = getIntent().getStringExtra("code");
        mEtBox.setShowSoftInputOnFocus(false);
        legitimateList = new ArrayList<>();

        errorList = new ArrayList<>();
        errorAdapter = new Adapter(this, errorList);
        mLvError.setAdapter(errorAdapter);
        mLvError.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mTvContent.getText().equals("开始扫描")) {
                    dialog.show(errorList, position, false);
                } else {
                    ShowToast("请先关闭扫描");
                }
            }
        });




        mEtBox.setShowSoftInputOnFocus(false);
        mEtBox.setImeOptions(EditorInfo.IME_ACTION_SEND);
        mEtBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (event != null)
                    if (event.getAction() == KeyEvent.ACTION_UP) return true;
                if (actionId == EditorInfo.IME_ACTION_SEND || actionId == 0) {
                    if (mEtBox.getText().toString().equals("")) {
                        showDialog("请输入标签~");
                    } else {
                        //正则表达式 前两位为大写 + 8位数字 就是店内码
                        if ( Pattern.matches("^([A-Z]{2}[0-9]{8},)*[A-Z]{2}[0-9]{8}$", mEtBox.getText().toString())) {
                            presenter.queryIncode(mEtBox.getText().toString());
                        }else{
                            mLlCurrentBox.setVisibility(View.VISIBLE);
                            mTvBoxId.setText(mEtBox.getText().toString());
                        }
                        mEtBox.setText("");
                    }
                    return true;
                }
                return false;
            }
        });

        initScanPresenter();
        presenter = new SignDetailPresenter(this, this, type, code);
        presenter.query(true);

        dialog = new SignDialog(this, R.style.ActionSheetDialogStyle, scanPresenter, errorList);
        dialog.setListener(new ISignListener() {
            @Override
            public void deleteItem(int position) {
                presenter.deleteError(errorList.get(position));
                errorList.remove(position);

                errorAdapter.notifyDataSetChanged();
                 changeNum();
                if (dialog.isNext) {
                    //TODO 自动切换到下一个
                    LogUtil.i("--------------position=", "" + errorList.size());
                    if (errorList.size() > 0) {

                        dialog.change(0);
                    } else {
                        dialog.dismiss();
                    }


                } else {
                    dialog.dismiss();
                }


            }

            @Override
            public void setItemIncode(String incode) {
                dialog.setCurrenIncode(incode);
            }

            @Override
            public void queryIncode(String rfid) {
                presenter.fromRfidgetIncode(rfid);
            }


        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        scanPresenter.setReadDataModel(0);
        scanPresenter.setListenerProtectModel(this);
    }


    private void initScanPresenter() {
        scanPresenter = new ScanPresenter(this);
        scanPresenter.initData();
        scanPresenter.setReadDataModel(0);
        scanPresenter.setMode(1);
        scanPresenter.setCurrentSetting(ScanPresenter.Setting.stockRead);

    }

    @Override
    public void initData(Allocation allocation) {
        mTvAllMember.setText(allocation.getAllIncodeNumber() + "");
        mTvAllBoxMember.setText(allocation.getAllBoxNumber() + "");
        mTvSignMember.setText(allocation.getSignIncodeNumber() + "");
        mTvAllacotionId.setText(allocation.getAllocationCode()+"");
    }

    @Override
    protected void onPause() {
        super.onPause();
        scanPresenter.stopReadRfid();
    }

    @Override
    public void setSignNum(int num) {

    }

    @Override
    public void setSignedNum(int num) {

    }

    @Override
    public void openOrClose() {
        if (mTvContent.getText().toString().equals("停止扫描")) {
            scanPresenter.stopReadRfid();
            mTvAllSignMember.setText(legitimateList.size()+errorList.size() + "");
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

    @Override
    public void setReceiveNum(int num) {

    }

    @Override
    public void setErrorNum(int num) {

    }

    @Override
    public void showErrorList(AllcationCase signIncodesBean) {

    }

    @Override
    public void close(String s) {
        hideLoading();
        showNotice(s);
        finish();
    }

    @Override
    public void add(String rfid) {
        if(!dialog.isShowing()) {
            legitimateList.add(rfid);
            mTvAllSignMember.setText(legitimateList.size() + "");
            mTvAllScan.setText(legitimateList.size()+errorList.size() + "");
        }
    }

    @Override
    public void addError(String rfid) {

        for(int i = 0;i<errorList.size();i++){
            if(errorList.get(i).getRfid().equals(rfid)){
                SoundUtils.play(2);
                return;
            }
        }
        AllocationError error = new AllocationError(null,mTvBoxId.getText().toString(), rfid, "");
        errorList.add(error);
        presenter.saveError(error);
        errorAdapter.notifyDataSetChanged();
         changeNum();
        mTvAllScan.setText(legitimateList.size()+errorList.size() + "");
    }

    @Override
    public void finishPartSign() {
        errorList.clear();
        errorAdapter.notifyDataSetChanged();
        legitimateList.clear();
    }

    @Override
    public void finishAllocationSign() {
        Intent it = new Intent(SignDetailActivity.this,SignResultActivity.class);
        it.putExtra("code",presenter.allocationId);
        startAnimActivity(it);
        finish();
    }

    @Override
    public void setItemIncode(String incode) {
        dialog.setCurrenIncode(incode);
    }


    @Override
    public void addInccodeError(String toString) {

        for(int i = 0;i<errorList.size();i++){
            if(errorList.get(i).getIncode().equals(toString)){
                return;
            }
        }
        AllocationError error = new AllocationError(null,mTvBoxId.getText().toString()+"", "", toString);
        errorList.add(error);
        presenter.saveError(error);
        errorAdapter.notifyDataSetChanged();
        changeNum();
        mEtBox.setText("");
        new AlertDialog.Builder(SignDetailActivity.this).setTitle(toString).setMessage("店内码异常").setPositiveButton("剔除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                presenter.deleteError(errorList.get(errorList.size()-1));
                errorList.remove(errorList.size()-1);
                errorAdapter.notifyDataSetChanged();
                changeNum();
                mTvAllScan.setText(legitimateList.size()+errorList.size() + "");
            }
        }).setNegativeButton("确定",null).show();
        mTvAllScan.setText(legitimateList.size()+errorList.size() + "");
    }
    //初始化可以过滤扫描的数据
    @Override
    public void refresh(List<String> data) {
        scanPresenter.initMap(data);
    }

    @Override
    public List<AllocationError> getErrorList() {
        return errorList;
    }

    @Override
    public void setErrorList(List<AllocationError> errorList) {
        this.errorList.addAll(errorList);
        mLvError.post(new Runnable() {
            @Override
            public void run() {
          errorAdapter.notifyDataSetChanged();
            }
        });
    }


    @Override
    public void ShowData(AllcationCase allcationCase) {

    }

    @Override
    protected void onDestroy() {
        presenter.clearTable();
        super.onDestroy();

    }


    @OnClick({R.id.tv_sign_allocation, R.id.tv_clear_box, R.id.rl_open_or_close, R.id.tv_deal, R.id.tv_clear, R.id.tv_part_sign})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_clear_box:
                if (mTvContent.getText().toString().equals("停止扫描")) return;
                mEtBox.setText("");
                mLlCurrentBox.setVisibility(View.INVISIBLE);
                mTvBoxId.setText("");
                break;
            case R.id.rl_open_or_close:
                openOrClose();
                break;
            case R.id.tv_deal:
                if (mTvContent.getText().toString().equals("停止扫描")) return;
                deal();
                break;
            case R.id.tv_clear:
                if (mTvContent.getText().toString().equals("停止扫描")) return;

                new AlertDialog.Builder(this).setMessage(
                        "重新采集将清空当前全部数据，是否确认重新采集？").setNegativeButton("取消", null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        clear();
                    }
                }).show();

                break;
            case R.id.tv_part_sign:
                if (mTvContent.getText().toString().equals("停止扫描")) return;


                    new AlertDialog.Builder(this).setMessage(
                            "确定部分签收？").setNegativeButton("取消", null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            presenter.partSign(legitimateList,errorList);
                        }
                    }).show();


                break;
            case R.id.tv_sign_allocation:
                if (mTvContent.getText().toString().equals("停止扫描")) return;
                new AlertDialog.Builder(this).setMessage(
                        "确定完成签收？").setNegativeButton("取消", null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        presenter.signAllocation();
                    }
                }).show();
                break;
        }

    }

    private void deal() {
        if (errorList.size() > 0) {
            dialog.show(errorList, 0, true);
        }
    }

    private void clear() {
        errorList.clear();
        legitimateList.clear();
        errorAdapter.notifyDataSetChanged();
        scanPresenter.initData();
        mTvAllScan.setText("0");
        mEtBox.setText("");

       changeNum();
    }

    @Override
    public void OnFinish(String data) {
        if (!dialog.isShowing()) {
            SoundUtils.play(1);
            count++;

            presenter.queryRfid(data);
        }
    }


    class Adapter extends BaseListAdapter<AllocationError> {

        public Adapter(Context context, List<AllocationError> list) {
            super(context, list);
        }

        @Override
        public View bindView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_three_rows, null);
            }

            AllocationError rfid = list.get(position);
            TextView tvLeft = ViewHolder.get(convertView, R.id.tv_left);
            TextView tvCenter = ViewHolder.get(convertView, R.id.tv_center);
            TextView tvRight = ViewHolder.get(convertView, R.id.tv_right);
            tvRight.setTextColor(Color.RED);
            tvLeft.setText(rfid.getBoxCode());
            if (TextUtils.isEmpty(rfid.getIncode())) {
                tvCenter.setText(rfid.getRfid());
            } else {
                tvCenter.setText(rfid.getIncode());
            }

            tvRight.setText("不属于该调拨单");

            return convertView;
        }
    }

    @Override
    public void onBackPressed() {
        if(errorList.size()+legitimateList.size()>0){
          new AlertDialog.Builder(SignDetailActivity.this).setTitle("还有数据未签收，确定退出?").setPositiveButton("确定", new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                  finish();
              }
          }).setNegativeButton("取消",null).show();
        }else {
            super.onBackPressed();
        }
    }
    public void changeNum(){
              mTvErrorNum.setText("错误数量：" + errorList.size());
        mTvShouldSignNum.setText("已收数量："+legitimateList.size());
    }
}
