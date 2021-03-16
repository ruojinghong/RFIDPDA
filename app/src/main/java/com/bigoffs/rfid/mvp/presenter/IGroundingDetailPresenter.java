package com.bigoffs.rfid.mvp.presenter;

import com.bigoffs.rfid.mvp.bean.GroundingDetailInfo;

public interface IGroundingDetailPresenter {

    void queryInCode(String code);
    void queryBarCode(String code);
    void queryShelfCode(String code);
    void countNum(int num);
    void deleteItem(String code);
    void deleteFirstItem();
    void upData(String taskId);
    void queryHistory();
    void queryAll();

    void saveError(GroundingDetailInfo error);

    void deleteError(GroundingDetailInfo groundingDetailInfo);
}
