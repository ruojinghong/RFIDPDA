package com.bigoffs.rfid.mvp.view;

/**
 * @author okbuy-001
 * @desc
 * @time 2020/4/22 16:44
 */
public interface IDataFragmentView<T> extends ICommonView{
    void ShowData(T t);

    void startProgressDialog(String result);

    void stopProgressDialog(String result);

    void ShowToast(String text);
}
