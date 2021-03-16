package com.bigoffs.rfid.mvp.view;

/**
 * @author okbuy-001
 * @desc
 * @time 2020/4/23 16:22
 */
public interface ILoginView extends ICommonView{

    String getUserName();
    String getPassword();
    void setVersionInfo();
    void goMainActivity();

}
