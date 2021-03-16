package com.bigoffs.rfid.mvp.presenter;

/**
 * @author okbuy-001
 * @desc
 * @time 2018/12/6 10:55
 */
public interface IGroundingPresenter {
    void query();
    void receive(int position, int type);
}
