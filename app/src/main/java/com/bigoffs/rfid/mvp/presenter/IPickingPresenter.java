package com.bigoffs.rfid.mvp.presenter;


import com.bigoffs.rfid.mvp.bean.PickingDetailNew;
import com.bigoffs.rfid.mvp.bean.PickingResult;
import com.bigoffs.rfid.mvp.bean.dao.ProductInfoCase;

import java.util.List;

/**
 * Created by okbuy on 17-3-30.
 */

public interface IPickingPresenter {
    void setTaskId(int taskId);

    void getPickDetail();

    void queryIncodeOrShelf(String incodeOrShelf);

    void getPickingInfo(String result);

    void checkLastPicking();

    void previewPickingList();

    void showFinishDialog();

    void finishPicking();
    //取消任务
    void cancleTask(String taskId, String outstockId);
    //替换店内码
    void changeIncode();

    void queryBarCode(int groupPosition, int childPosition);

    void initData(PickingDetailNew data);

    void query(String data);

    void pickup(ProductInfoCase infoCase);

    void queryIncode(String data);
}
