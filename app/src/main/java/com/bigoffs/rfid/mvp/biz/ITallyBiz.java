package com.bigoffs.rfid.mvp.biz;

import android.content.Context;

import com.bigoffs.rfid.network.ICommonResult;


import java.util.List;

/**
 * Created by okbuy on 17-2-17.
 */

public interface ITallyBiz {
    // 理货下架查询店内码
    void queryDownIncode(Context context, String incode, String tag, ICommonResult callback);

    // 理货下架
    void down(Context context, List<String> incodes, String tag, ICommonResult callback);

    // 查询理货任务单信息
    void queryTaskInfo(Context context, int taskId, String tag, ICommonResult callback);

    // 理货上架查询店内码
    void queryUpIncode(Context context, String incode, int taskId, String tag, ICommonResult callback);

    // 理货上架
    void up(Context context, String shelfNumber, List<String> incodes, int taskId, String tag, ICommonResult callback);

    //理货扫描货架号
    void queryShelfcode(Context context, String shelf, String tag, ICommonResult callback);

    //理货扫描店内码
    void queryIncode(Context context, String inCode, String tag, ICommonResult callback);

    //转移货架接口
    void transferShelf(Context context, String shelfIncodes, String tag, ICommonResult callback);
    //新增转移货架接口
    void transferShelfNew(Context context, String shelf,String incodes,String rfids, String tag, ICommonResult callback);
}
