package com.bigoffs.rfid.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bigoffs.rfid.GlobalCfg;
import com.bigoffs.rfid.R;
import com.bigoffs.rfid.mvp.presenter.ILoginPresenter;
import com.bigoffs.rfid.mvp.presenter.LoginPresenter;
import com.bigoffs.rfid.mvp.view.ILoginView;
import com.bigoffs.rfid.util.UserManager;

import java.util.List;

/**
 * @author okbuy-001
 * @desc
 * @time 2020/4/23 15:58
 */
public class LoginActivity extends BaseActivity implements ILoginView {



    public static final String TAG = "LoginActivity";
    private EditText mEtUserName;
    private EditText mEtPassword;
    private Button mBtnLogin;
    private TextView mTvVersion;

    private long mLastTimePressBack;

    private ILoginPresenter mPresenter = new LoginPresenter(this, this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
        setVersionInfo();
        mPresenter.checkLogin();
    }

    private void initViews() {
        mEtUserName = (EditText) findViewById(R.id.et_user_name);
        mEtPassword = (EditText) findViewById(R.id.et_password);
        mBtnLogin = (Button) findViewById(R.id.btn_login);
        mTvVersion = (TextView) findViewById(R.id.tv_version_code);
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.login();
            }
        });
        // 默认上一次登录的用户名
        mEtUserName.setText(UserManager.getLastUserName(this));
        mEtUserName.setSelection(mEtUserName.getText().length());
    }

    @Override
    public String getUserName() {
        return mEtUserName.getText().toString().trim();
    }

    @Override
    public String getPassword() {
        return mEtPassword.getText().toString();
    }

    @Override
    public void setVersionInfo() {
        mTvVersion.setText("v" + GlobalCfg.VERSION);
    }

    @Override
    public void goMainActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        long nowTime = System.currentTimeMillis();
        if (mLastTimePressBack == 0 || nowTime - mLastTimePressBack > 2000) {
            showNotice("再按一次退出程序");
            mLastTimePressBack = nowTime;
        } else {
            System.exit(0);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }





}
