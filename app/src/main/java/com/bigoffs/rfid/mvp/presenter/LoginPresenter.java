package com.bigoffs.rfid.mvp.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.bigoffs.rfid.mvp.bean.User;
import com.bigoffs.rfid.mvp.biz.IUserBiz;
import com.bigoffs.rfid.mvp.biz.UserBiz;
import com.bigoffs.rfid.mvp.view.ILoginView;
import com.bigoffs.rfid.network.ICommonResult;
import com.bigoffs.rfid.ui.activity.LoginActivity;
import com.bigoffs.rfid.util.UserManager;
import com.google.gson.Gson;

import okhttp3.Call;

/**
 * @author okbuy-001
 * @desc
 * @time 2020/4/23 16:27
 */
public class LoginPresenter implements ILoginPresenter {
    // view
    private ILoginView mView;
    // model
    private IUserBiz mUserBiz;

    private Context mContext;

    public LoginPresenter(Context context, ILoginView loginView) {
        mContext = context;
        mView = loginView;
        mUserBiz = new UserBiz();
    }

    @Override
    public void checkLogin() {
        if (UserManager.isLogin(mContext)) {
            mView.goMainActivity();
        }
    }

    @Override
    public void login() {
        if (TextUtils.isEmpty(mView.getUserName()) || TextUtils.isEmpty(mView.getPassword())) {
            mView.showNotice("用户名和密码不能为空!");
            return;
        }
        mView.showLoading("");
        mUserBiz.login(mContext, mView.getUserName(), mView.getPassword(), LoginActivity.TAG, new ICommonResult() {
            @Override
            public void onSuccess(String result) {
                mView.hideLoading();
                mView.showNotice("登录成功！");
                // 存储用户名
                UserManager.saveLastUserName(mContext, mView.getUserName());
                Gson gson = new Gson();
                User user = gson.fromJson(result, User.class);
                UserManager.login(mContext, user);
                mView.goMainActivity();
            }

            @Override
            public void onFail(Call call, Exception e) {
                mView.hideLoading();
            }

            @Override
            public void onInterrupt(int code, String message) {
                mView.hideLoading();
            }
        });
    }
}
