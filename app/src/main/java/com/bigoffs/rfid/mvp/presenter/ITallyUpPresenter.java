package com.bigoffs.rfid.mvp.presenter;

/**
 * Created by okbuy on 17-2-20.
 */

public interface ITallyUpPresenter {
    void queryTaskInfo();
    boolean isTaskFinished();
    void queryUpIncode(String incode);
    boolean hasShelfNumber();
    void setShelfNumber(String shelfNumber);
    void clear();
    void remove();
    void up();
    void checkDetail();
    void checkDown();
    void checkUp();
    void checkRemain();
}
