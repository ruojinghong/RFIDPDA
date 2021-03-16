package com.bigoffs.rfid.mvp.view;

import com.bigoffs.rfid.mvp.bean.SkuQueryInfo;

import java.util.List;

/**
 * @author okbuy-001
 * @desc
 * @time 2020/4/28 16:51
 */
public interface ISkuQueryView extends ICommonView{
    void setAdatper(List<SkuQueryInfo> list);

    void showBlank();
}
