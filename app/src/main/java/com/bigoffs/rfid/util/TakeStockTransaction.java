package com.bigoffs.rfid.util;

import android.content.Context;

import com.bigoffs.rfid.MApplication;
import com.bigoffs.rfid.database.greendao.AllcationCaseDao;
import com.bigoffs.rfid.database.greendao.AllocationErrorDao;
import com.bigoffs.rfid.database.greendao.DaoSession;
import com.bigoffs.rfid.database.greendao.GroundingAllocationDetailInfoDao;
import com.bigoffs.rfid.database.greendao.GroundingDetailInfoDao;
import com.bigoffs.rfid.database.greendao.InventoryCaseDao;
import com.bigoffs.rfid.database.greendao.ProductInfoCaseDao;
import com.bigoffs.rfid.mvp.bean.AllocationError;
import com.bigoffs.rfid.mvp.bean.GroundingDetailInfo;
import com.bigoffs.rfid.mvp.bean.dao.AllcationCase;
import com.bigoffs.rfid.mvp.bean.dao.GroundingAllocationDetailInfo;
import com.bigoffs.rfid.mvp.bean.dao.InventoryCase;
import com.bigoffs.rfid.mvp.bean.dao.ProductInfoCase;

/**
 * @author okbuy-001
 * @desc
 * @time 2020/5/12 11:27
 */
public class TakeStockTransaction {

        public static DBOperator<AllcationCaseDao, AllcationCase> allocationDBOperator ;
        public static DBOperator<AllocationErrorDao, AllocationError> allocationErrorDBOperator ;
        public static DBOperator<GroundingDetailInfoDao, GroundingDetailInfo> groundingDetailInfo;
        public static DBOperator<GroundingAllocationDetailInfoDao, GroundingAllocationDetailInfo> groundingAllocationDetailInfo;
        public static DBOperator<InventoryCaseDao, InventoryCase> inventoryDBOperator;
        public static DBOperator<ProductInfoCaseDao, ProductInfoCase> productInfoDBOperator;
        public static DaoSession daoSession;

    public static void init(Context context) {
        if (daoSession == null) {
            daoSession = MApplication.getDaoSession(context);
        }
    }

    public static DBOperator<ProductInfoCaseDao, ProductInfoCase> getProductInfoDBOperator() {
        if (productInfoDBOperator == null) {
            productInfoDBOperator = DBOperator.getOperator(daoSession.getProductInfoCaseDao(), ProductInfoCase.class);
        }
        return productInfoDBOperator;
    }
    public static DBOperator<InventoryCaseDao, InventoryCase> getInventoryDBOperator() {
        if (inventoryDBOperator == null) {
            inventoryDBOperator = DBOperator.getOperator(daoSession.getInventoryCaseDao(), InventoryCase.class);
        }
        return inventoryDBOperator;
    }

    public static DBOperator<GroundingDetailInfoDao, GroundingDetailInfo> getGroundingDetailInforDBOperator() {
        if (groundingDetailInfo == null) {
            groundingDetailInfo = DBOperator.getOperator(daoSession.getGroundingDetailInfoDao(), GroundingDetailInfo.class);
        }
        return groundingDetailInfo;
    }
    public static DBOperator<GroundingAllocationDetailInfoDao, GroundingAllocationDetailInfo> getGroundingAllocationDetailInforDBOperator() {
        if (groundingAllocationDetailInfo == null) {
            groundingAllocationDetailInfo = DBOperator.getOperator(daoSession.getGroundingAllocationDetailInfoDao(), GroundingAllocationDetailInfo.class);
        }
        return groundingAllocationDetailInfo;
    }
    public static DBOperator<AllocationErrorDao, AllocationError> getAllocationErrorDBOperator() {
        if (allocationErrorDBOperator == null) {
            allocationErrorDBOperator = DBOperator.getOperator(daoSession.getAllocationErrorDao(), AllocationError.class);
        }
        return allocationErrorDBOperator;
    }

    public static DBOperator<AllcationCaseDao, AllcationCase> getAllocationDBOperator() {
        if (allocationDBOperator == null) {
            allocationDBOperator = DBOperator.getOperator(daoSession.getAllcationCaseDao(), AllcationCase.class);
        }
        return allocationDBOperator;
    }


}
