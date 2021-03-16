package com.bigoffs.rfid.mvp.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.bigoffs.rfid.GlobalCfg;
import com.bigoffs.rfid.MApplication;
import com.bigoffs.rfid.database.greendao.AllcationCaseDao;
import com.bigoffs.rfid.database.greendao.AllocationErrorDao;
import com.bigoffs.rfid.database.greendao.DaoSession;
import com.bigoffs.rfid.mvp.bean.Allocation;
import com.bigoffs.rfid.mvp.bean.AllocationError;
import com.bigoffs.rfid.mvp.bean.ProductInfo;
import com.bigoffs.rfid.mvp.bean.dao.AllcationCase;
import com.bigoffs.rfid.mvp.biz.ISignBiz;
import com.bigoffs.rfid.mvp.biz.SignBiz;
import com.bigoffs.rfid.mvp.view.ISignView;
import com.bigoffs.rfid.network.ICommonResult;
import com.bigoffs.rfid.network.PdaHttpUtil;
import com.bigoffs.rfid.util.CodeTypeUtils;
import com.bigoffs.rfid.util.DBOperator;
import com.bigoffs.rfid.util.LogUtil;
import com.bigoffs.rfid.util.SPUtil;
import com.bigoffs.rfid.util.TakeStockTransaction;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import org.greenrobot.greendao.Property;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * @author okbuy-001
 * @desc
 * @time 2020/4/29 17:36
 */
public class SignDetailPresenter {
    private ISignView<AllcationCase> mView;
    private ISignBiz mBiz;
    private Context mContext;
    private String type;
    private String code;
    private  DBOperator<AllcationCaseDao,AllcationCase> caseDBOperator ;
    private  DBOperator<AllocationErrorDao, AllocationError> caseErrorDBOperator ;
    public  String allocationId;

    public SignDetailPresenter(ISignView<AllcationCase> view, Context context,String type,String code){
            mView = view;
            mBiz = new SignBiz();
            mContext = context;
            this.type = type;
            this.code = code;
            caseDBOperator = TakeStockTransaction.getAllocationDBOperator();
            caseErrorDBOperator = TakeStockTransaction.getAllocationErrorDBOperator();
    }
    public void query(boolean isRefresh){
        mView.showLoading("加载数据中..");

        mBiz.getAllocation(mContext, type, code, "SignDetailActivity", new ICommonResult() {
            @Override
            public void onSuccess(String result) {
                dealDates(result,isRefresh);
            }

            @Override
            public void onFail(Call call, Exception e) {
                mView.close(e.toString());
            }

            @Override
            public void onInterrupt(int code, String message) {
                mView.close(message);
            }
        });
    }

    public void parseDate(AllcationCase allcationCase ){

    }

    public void queryRfid(String rfid){
        List<AllcationCase> cases = caseDBOperator.getItemByParameter(AllcationCaseDao.Properties.RfidCode,rfid);

        if(cases.size()==1){
            mView.add(cases.get(0).getIncode());
        }else{
            mView.addError(rfid);
        }

    }
    public void clearTable(){
        caseDBOperator.mCustomDao.deleteAll();
        caseErrorDBOperator.mCustomDao.deleteAll();
    }

    //部分签收
    public void partSign(List<String> legitimateList,List<AllocationError> errors) {
            mView.showLoading("上传中...");
            List<String> rfids = new ArrayList<>();
            for (AllocationError error:errors){
                if(TextUtils.isEmpty(error.getRfid())){
                        legitimateList.add(error.getIncode());
                }else{
                        rfids.add(error.getRfid());
                }
            }

          mBiz.partSign(mContext, new Gson().toJson(legitimateList),new Gson().toJson(rfids), allocationId,"SignDetailActivity", new ICommonResult() {
              @Override
              public void onSuccess(String result) {
                  mView.hideLoading();
                  mView.finishPartSign();
                  clearTable();
                  query(false);
              }

              @Override
              public void onFail(Call call, Exception e) {
                  mView.hideLoading();
                  mView.showNotice("签收失败:"+e.toString());
              }

              @Override
              public void onInterrupt(int code, String message) {
                  mView.hideLoading();
                  mView.showNotice("签收失败:"+message);
              }
          });

    }

    //签收调拨单
    public void signAllocation() {
        mView.showLoading("");
        mBiz.signAllocation(mContext, allocationId, "SignDetailActivity", new ICommonResult() {
            @Override
            public void onSuccess(String result) {
                mView.hideLoading();
                SPUtil.setCurrentAllocationId(mContext,"");
                clearTable();
                mView.finishAllocationSign();

            }

            @Override
            public void onFail(Call call, Exception e) {
                mView.hideLoading();
                mView.showNotice("签收失败:"+e.toString());
            }

            @Override
            public void onInterrupt(int code, String message) {
                mView.hideLoading();
                //该调拨单已经签收
                if(code == 1095){
                 SPUtil.setCurrentAllocationId(mContext,"");
                 clearTable();
                 mView.close("签收失败:"+message);
                }else{
                    mView.showNotice("签收失败:"+message);
                }
            }
        });

    }

    public void queryIncode(String toString) {
        List<AllcationCase> cases = caseDBOperator.getItemByParameter(AllcationCaseDao.Properties.Incode,toString);

        if(cases.size()>0){
            mView.add(cases.get(0).getIncode());
        }else{
            mView.addInccodeError(toString);
        }
    }

    public void fromRfidgetIncode(String rfid) {

       mBiz.getIncodeFromRfid(mContext, rfid, "SignDetailActivity", new ICommonResult() {
           @Override
           public void onSuccess(String result) {
                mView.setItemIncode(result);
           }

           @Override
           public void onFail(Call call, Exception e) {
               mView.setItemIncode("查询失败");
           }

           @Override
           public void onInterrupt(int code, String message) {
               mView.setItemIncode("查询失败");
           }
       });
    }

    public boolean errorListSave(AllocationError error, Property fieldName, String fieldValue){
        List<AllocationError> data = caseErrorDBOperator.getItemByParameter(fieldName,fieldValue);
        if(data.size() > 0){
            caseErrorDBOperator.insertData(data.get(0));
            return true;
        }
        return false;
    }

    void dealDates(String result, boolean isRefresh){
        Allocation allocation = Allocation.objectFromData(result);
        allocationId = allocation.getAllocationCode();
        String currentAllocationId = SPUtil.getCurrentAllocationId(mContext);
        if(TextUtils.isEmpty(currentAllocationId)){
            SPUtil.setCurrentAllocationId(mContext,allocationId+"");
        }else{

            if(!allocationId.equals(currentAllocationId)){
                    mView.close("之前的调拨单"+allocationId+"尚未签收");
                    return;
            }

        }
        mView.initData(allocation);
        new Thread(()->{
            for (Allocation.BoxInfoBean boxInfoBean : allocation.getBoxInfo()) {

                String boxCode = boxInfoBean.getBoxCode();
                for (Allocation.BoxInfoBean.NosignIncodesBean noSignIncodesBean : boxInfoBean.getNosignIncodes()) {
                    AllcationCase allcationCase = new AllcationCase(null,boxCode, false, noSignIncodesBean.getIncode(), noSignIncodesBean.getRfidCode(),false);

                    caseDBOperator.insertData(allcationCase);
                }
                for (Allocation.BoxInfoBean.SignIncodesBean signIncodesBean : boxInfoBean.getSignIncodes()) {

                    AllcationCase allcationCase = new AllcationCase(null,boxCode, true, signIncodesBean.getIncode(), signIncodesBean.getRfidCode(),false);

                    caseDBOperator.insertData(allcationCase);
                }


            }
            if(isRefresh) {
                List<String> data = new ArrayList<>();
                List<AllcationCase> allocations = caseDBOperator.getItemByParameter(AllcationCaseDao.Properties.Sign, "true");

                for (AllcationCase a : allocations) {
                    data.add(a.getRfidCode());

                }
                mView.refresh(data);
                List<String> errorString = new ArrayList<>();
                List<AllocationError> errors = caseErrorDBOperator.getAll();
                for (AllocationError a:errors){
                    errorString.add(a.getRfid());

                }
                mView.refresh(errorString);
                mView.setErrorList(errors);
            }

            mView.hideLoading();
        }).start();

    }

    public void saveError(AllocationError error){
        caseErrorDBOperator.insertData(error);
    }

    public void deleteError(AllocationError error) {
        caseErrorDBOperator.deleteData(error);
    }
}
