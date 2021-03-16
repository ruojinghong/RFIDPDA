package com.bigoffs.rfid.ui.activity;

import android.content.Context;
import android.database.sqlite.SQLiteException;

import com.bigoffs.rfid.mvp.biz.IUserBiz;
import com.bigoffs.rfid.mvp.biz.UserBiz;
import com.bigoffs.rfid.mvp.view.ICommonView;
import com.bigoffs.rfid.util.ToastUtil;
import com.bigoffs.rfid.util.UserManager;

/**
 * @author okbuy-001
 * @desc
 * @time 2020/4/24 15:34
 */
class LogoutPresenter implements BaseActivity.ILogoutPresenter {



        private Context mContext;
        // Model
        private IUserBiz mUserBiz;
        // View
        private ICommonView mView;

    public LogoutPresenter(Context context, ICommonView commonView) {
            mContext = context;
            mView = commonView;
            mUserBiz = new UserBiz();
        }

        @Override
        public void logout() {
            mUserBiz.logout(mContext);
            UserManager.logout(mContext);

            ToastUtil.showToast(mContext, "退出成功！");
            mView.goLoginActivity();
        }

}
