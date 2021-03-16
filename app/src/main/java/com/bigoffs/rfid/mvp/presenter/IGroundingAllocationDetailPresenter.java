package com.bigoffs.rfid.mvp.presenter;

import com.bigoffs.rfid.mvp.bean.dao.GroundingAllocationDetailInfo;

import java.util.List;

/**
 * @author okbuy-001
 * @desc
 * @time 2020/5/15 18:07
 */
public interface IGroundingAllocationDetailPresenter {
    void queryInCode(String code);
    void queryBarCode(String code);
    void queryShelfCode(String code);
    void countNum(int num);
    void deleteItem(String code);
    void deleteFirstItem();
    void upData(String taskId);
    void upData(String incode, List<String> incodeList,List<GroundingAllocationDetailInfo> rfidList,String allocationCode);
    void queryHistory(String  allocationCode);
    void queryAll();

    void saveError(GroundingAllocationDetailInfo error);

    void deleteError(GroundingAllocationDetailInfo groundingDetailInfo);

    void clearTable();

    void queryRfid(String data);

    void fromRfidgetIncode(String rfid);
}
