package com.bigoffs.rfid.mvp.presenter;

import com.bigoffs.rfid.mvp.bean.TallyProductInfo;

import java.util.List;

/**
 * Created by okbuy on 17-2-17.
 */

public interface ITallyDownPresenter {
    void query(String incode);
    void clear();
    void remove();
    void down();
    void goTallyDetail();
    List<TallyProductInfo> getData();
}
