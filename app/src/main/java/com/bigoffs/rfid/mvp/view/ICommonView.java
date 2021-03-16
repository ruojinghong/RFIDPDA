package com.bigoffs.rfid.mvp.view;

/**
 * @author okbuy-001
 * @desc
 * @time 2020/4/22 15:36
 */
public interface ICommonView {
    void showLoading(String message);
    void hideLoading();
    void goLoginActivity();
    void showNotice(String msg);
}
