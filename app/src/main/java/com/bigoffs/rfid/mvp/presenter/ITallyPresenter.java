package com.bigoffs.rfid.mvp.presenter;

/**
 * @author okbuy-001
 * @desc
 * @time 2018/12/11 10:42
 */
public interface ITallyPresenter {
    void queryInCode(String code);
    void queryShelfCode(String code);
    void clearAllItem();
    void moveShelf();
    void deleteFirstItem();
    String getCurrentShelf();
}
