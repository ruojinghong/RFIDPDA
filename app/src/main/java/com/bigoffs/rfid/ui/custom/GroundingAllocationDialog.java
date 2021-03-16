package com.bigoffs.rfid.ui.custom;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bigoffs.rfid.R;
import com.bigoffs.rfid.database.greendao.GroundingDetailInfoDao;
import com.bigoffs.rfid.listener.ISignListener;
import com.bigoffs.rfid.listener.OnFinishListener;
import com.bigoffs.rfid.mvp.bean.AllocationError;
import com.bigoffs.rfid.mvp.bean.GroundingDetailInfo;
import com.bigoffs.rfid.mvp.bean.dao.GroundingAllocationDetailInfo;
import com.bigoffs.rfid.mvp.presenter.ScanPresenter;
import com.bigoffs.rfid.persistence.util.SoundUtils;

import java.util.List;

/**
 * @author okbuy-001
 * @desc
 * @time 2020/5/6 14:04
 */
public class GroundingAllocationDialog extends Dialog {
    private boolean flag;
    private ScanPresenter presenter;
    private Context mContext;
    private EditText etIncode;
    private ImageView imageView;
    private TextView tvBox;
    private TextView tvIncode;
    private Button btn;
    private int currentPoint = 0;
    /**
     * 当前RFID对应的店内码
     */
    private String currentIncode;

    private List<GroundingAllocationDetailInfo> rfidList;
    private ISignListener listener;
    /**
     * 决定删除完是否接着下一个
     */
    public boolean isNext;


    private GroundingAllocationDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        presenter.setAnotherListener(new OnFinishListener() {
            @Override
            public void OnFinish(String data) {

                if (isShowing()) {

                    String[] split = data.split("@");
                    String epc = rfidList.get(currentPoint).getRfid();
                    if (epc.equals(split[0])) {
                        if (split.length == 1) {
                            icon(0);
                        } else {
                            int rssi = Double.valueOf(split[1]).intValue();
                            icon(rssi);
                        }

                    }

                }

            }


        });
    }

    public void setListener(ISignListener listener) {
        this.listener = listener;
    }

    private void setIncode(String data) {
        if (currentIncode.equals(data)) {
            if (listener != null) {
                listener.deleteItem(currentPoint);
            }

        } else {
            etIncode.setText("");
            SoundUtils.play(2);
        }
        etIncode.setText("");
    }

    private void icon(int i) {
        switch (i) {
            case 0:
                imageView.setImageResource(R.drawable.icon1);
                SoundUtils.playByVolume(1, 0.17f);
                break;
            case 1:
                imageView.setImageResource(R.drawable.icon2);
                SoundUtils.playByVolume(1, 0.33f);
                break;
            case 2:
                imageView.setImageResource(R.drawable.icon3);
                SoundUtils.playByVolume(1, 0.5f);
                break;
            case 3:
                imageView.setImageResource(R.drawable.icon4);
                SoundUtils.playByVolume(1, 0.66f);
                break;
            case 4:
                imageView.setImageResource(R.drawable.icon5);
                SoundUtils.playByVolume(1, 0.78f);
                break;
            case 5:
                imageView.setImageResource(R.drawable.icon6);
                SoundUtils.playByVolume(1, 1);
                break;
        }
    }

    private void initView() {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.dialog_sign, null);
        etIncode = inflate.findViewById(R.id.et_incode);
        tvBox = inflate.findViewById(R.id.tv_box);
        tvIncode = inflate.findViewById(R.id.tv_incode);
        imageView = inflate.findViewById(R.id.animation1);
        btn = inflate.findViewById(R.id.btn_read_or_stop);
        etIncode.setShowSoftInputOnFocus(false);
        etIncode.setImeOptions(EditorInfo.IME_ACTION_SEND);
        etIncode.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (event != null)
                    if (event.getAction() == KeyEvent.ACTION_UP) return true;
                if (actionId == EditorInfo.IME_ACTION_SEND || actionId == 0) {
                    if (etIncode.getText().toString().equals("")) {

                    } else {
                        setIncode(etIncode.getText().toString());
                    }
                    return true;
                }
                return false;
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readOrClose();
            }
        });
        //将布局设置给Dialog
        setContentView(inflate);
        //获取当前Activity所在的窗体
        Window dialogWindow = getWindow();
        dialogWindow.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogWindow.setGravity(Gravity.BOTTOM);
        setCanceledOnTouchOutside(false);

    }

    public GroundingAllocationDialog(@NonNull Context context, int themeResId, ScanPresenter presenter, List<GroundingAllocationDetailInfo> s) {
        super(context, themeResId);
        this.presenter = presenter;
        mContext = context;
        rfidList = s;
    }


    @Override
    public void dismiss() {

        presenter.stopReadRfid();
        presenter.setReadDataModel(0);
        imageView.setImageResource(R.drawable.icon1);
        btn.setText("开始扫描");
        btn.setBackground(mContext.getDrawable(R.color.primary));
        super.dismiss();
    }

    @Override
    public void hide() {

        super.hide();
    }

    @Override
    public void show() {
        super.show();
        tvIncode.setText(currentIncode);
//        tvBox.setText(rfidList.get(currentPoint).getBoxCode());
    }

    public void show(List<GroundingAllocationDetailInfo> errList, int position, boolean next) {
        this.rfidList = errList;
        isNext = next;
        currentPoint = position;
        if (listener != null) {
            if(TextUtils.isEmpty(errList.get(position).getInCode())){
                listener.queryIncode(rfidList.get(position).getRfid());
            }else{
                setCurrenIncode(errList.get(position).getInCode());
            }

        }

    }


    public void readOrClose() {
        if (btn.getText().toString().equals("开始扫描")) {
            btn.setBackground(mContext.getDrawable(R.color.goods_del_bg));
            flag = true;

            presenter.setReadDataModel(1);
            presenter.startReadRfid();


            btn.setText("停止扫描");

        } else {
            flag = false;
            btn.setBackground(mContext.getDrawable(R.color.primary));
            presenter.stopReadRfid();
            imageView.setImageResource(R.drawable.icon1);
            btn.setText("开始扫描");
//            searchEpcInfo();
        }

    }

    public void setCurrenIncode(String incode) {
        currentIncode = incode;
        if(isShowing()){
            if(!incode.equals("查询失败")) {
                tvIncode.setText(currentIncode);
            }
//            tvBox.setText(rfidList.get(currentPoint).getBoxCode());

        }else{
            show();
        }

    }

    public void nextEpc() {
        if (rfidList == null) return;
        currentPoint++;
        if (currentPoint == rfidList.size()) {
            currentPoint = 0;
        }
    }

    //移动到第n个
    public void change(int i) {
         currentPoint = i;
        if(tvIncode != null) tvIncode.setText("加载中...");
        if(TextUtils.isEmpty(rfidList.get(currentPoint).getInCode())){
            listener.queryIncode(rfidList.get(i).getRfid());
        }else{
            setCurrenIncode(rfidList.get(i).getInCode());
        }

    }
}
