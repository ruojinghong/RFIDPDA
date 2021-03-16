package com.bigoffs.rfid.mvp.presenter;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.Log;

import com.bigoffs.rfid.mvp.bean.BarcodeInfo;
import com.bigoffs.rfid.mvp.bean.BluetoothDevice;
import com.bigoffs.rfid.mvp.bean.ProductInfo;
import com.bigoffs.rfid.mvp.biz.IQueryBiz;
import com.bigoffs.rfid.mvp.biz.QueryBiz;
import com.bigoffs.rfid.mvp.view.IQueryView;
import com.bigoffs.rfid.network.CommonViewCommonResult;
import com.bigoffs.rfid.network.ICommonResult;
import com.bigoffs.rfid.network.ResultCallback;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import okhttp3.Call;
import zpSDK.zpSDK.zpBluetoothPrinter;

/**
 * @author okbuy-001
 * @desc
 * @time 2020/4/24 16:09
 */
public class QueryPresenter implements IQueryPresenter {


    /**
     * 打印模板一
     */
    public static final int PRINT_TYPE_ONE = 1;

    /**
     * 打印模板二
     */
    public static final int PRINT_TYPE_TWO = 2;

    /**
     * 打印模板三
     */
    public static final int PRINT_TYPE_THREE = 3;

    /**
     * 默认打印模板为1
     */
    private int printType = 1;

    /**
     * 如果状态为true则直接退出当前页
     */
    private boolean pageState = true;

    // Model
    private IQueryBiz mBiz;
    // View
    private IQueryView mView;

    private Context mContext;

    private BluetoothAdapter mBluetoothAdapter;

    /**
     * 打印模板的宽高
     */
    private int x = 600;
    private int y = 280;

    /**
     * 创建接口对象
     */
    private zpBluetoothPrinter mZpAPI;
    /**
     * 存储蓝牙设备列表
     */
    private List<BluetoothDevice> mBluetoothDeviceList;


    public QueryPresenter(Context context, IQueryView queryView) {
        mContext = context;
        mView = queryView;
        mBiz = new QueryBiz();
        mBluetoothDeviceList = new ArrayList<>();
        init();
    }

    @Override
    public void load() {

    }

    @Override
    public void query(boolean isRfid) {
        if (TextUtils.isEmpty(mView.getIncode().trim())) {
            mView.showNotice(mView.getChoose() + "不能为空！");
            return;
        }
        if (TextUtils.isEmpty(mView.getIncode().trim())) {
            mView.showNotice(mView.getChoose()+"不能为空！");
            return;
        }
        mView.showLoading("");
        switch (mView.getChoose()){
            case "条形码":
                mBiz.queryBarCode(mContext,mView.getIncode(),"QueryActivit",new CommonViewCommonResult(mView){
                    @Override
                    public void onSuccess(String result) {
                        super.onSuccess(result);
                        mView.setIncode(mView.getIncode());
                        Gson gson = new Gson();
                        List<BarcodeInfo> info = gson.fromJson(result,new TypeToken<List<BarcodeInfo>>(){}.getType());
                        mView.showBarcodeInfo(info);
                        if (isRfid)  queryRfid2Incode(mView.getIncode());

                    }

                    @Override
                    public void onFail(Call call, Exception e) {
                        super.onFail(call, e);
                        mView.hideLoading();
                        mView.setWrongIncode(mView.getIncode());
                        mView.clearInfo();
                        // 播放错误声
                        mView.playBeep();
                    }

                    @Override
                    public void onInterrupt(int code, String message) {
                        super.onInterrupt(code, message);
                    }
                });

                break;
            case "店内码":
                mBiz.queryProduct(mContext, mView.getIncode(), "QueryActivity", new CommonViewCommonResult(mView) {
                    @Override
                    public void onSuccess(String result) {
                        super.onSuccess(result);
                        mView.setIncode(mView.getIncode());
                        Gson gson = new Gson();
                        ProductInfo info = gson.fromJson(result, ProductInfo.class);
                        mView.showProductInfo(info);
                        if (isRfid)  queryRfid2Incode(mView.getIncode());
                    }

                    @Override
                    public void onInterrupt(int code, String message) {
                        super.onInterrupt(code, message);
                        mView.hideLoading();
                        mView.setWrongIncode(mView.getIncode());
                        mView.clearInfo();
                        // 播放错误声
                        mView.playBeep();
                    }
                });
                break;
            default:
                mView.hideLoading();
                break;

        }

    }

    @Override
    public void destory() {
        if(mZpAPI != null) {
            try{  mZpAPI.close();}catch (Exception e){}

        }
    }


    /**
     * 初始化打印控件
     */
    private void init() {
        //打印控件不为null,进行实例化
        if (mZpAPI == null) {
            mZpAPI = new zpBluetoothPrinter(mContext);
        }
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }




    /**
     * 打印标签
     *
     * @return 打印是否成功
     */
    @Override
    public boolean printLabel(ProductInfo info) {
        if (info == null) {
            mView.showNotice("请先查询货品");
            return false;
        } else {
            try {

                switch (printType){
                    case PRINT_TYPE_ONE:
                        return mZpAPI.drawBitmap(getPriceTagSpec(info.info.get(0).get("销售价"),info.info.get(12).get("吊牌价") ,info.info.get(9).get("品牌"), info.info.get(4).get("品类"), info.info.get(10).get("货号"), "1年", "合格","详见标签"), 0, 0, x, y, 0, 3);
                    case PRINT_TYPE_TWO:
                        return mZpAPI.drawBitmap(getPriceTagSpec(info.info.get(0).get("销售价")), 0, 0, 300, 70, 0, 1);
                    case PRINT_TYPE_THREE:
                        return mZpAPI.drawBitmap(getPriceTagSpecThree(info.info.get(0).get("销售价"),info.info.get(12).get("吊牌价") ,info.info.get(9).get("品牌"), info.info.get(4).get("品类"), info.info.get(10).get("货号"), "1年", "合格","详见标签"), 0, 0, x, y, 0, 3);
                    default:
                        mView.showNotice("请先选择模板");
                        return false;
                }

            }catch (Exception e){
                Log.e("打印失败",e.getMessage());
                mView.showNotice("打印失败,请重新连接打印机");
                return false;
            }
        }
    }

    /**
     * 连接打印机
     *
     * @return
     */

    @Override
    public boolean linkPrinter() {

        if (!mBluetoothAdapter.isEnabled()) {
            mView.openBluetooth();
            return false;
        }
        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }

        mBluetoothAdapter.startDiscovery();
//        mView.resgiterDeveiceReceive();


//
//        Set<android.bluetooth.BluetoothDevice> devices = mBluetoothAdapter.getBondedDevices();
//        for (int i = 0; i < devices.size(); i++) {
//            android.bluetooth.BluetoothDevice device = devices.iterator().next();
//        }
//
//
//        //初始化打印机设备列表
        mBluetoothDeviceList.clear();
        //获取已配对打印机，遍历添加到打印机列表中
        for (int i = 0; i < mZpAPI.getAllPrinters().size(); i++) {
            mBluetoothDeviceList.add(new BluetoothDevice(mZpAPI.getAllPrinters().get(i).GetName(), mZpAPI.getAllPrinters().get(i).Getmac()));
        }
        mView.showBuletoothDevicesDialog(mBluetoothDeviceList);


        return  true;

    }


    @Override
    public boolean link(String name, String address) {
        //连接打印机,连接之前确认是SDK所支持的打印机
        if (isSupported(name)) {
            //开启打印机,传入打印机MAC地址
            if (mZpAPI.openPrinterSync(address)) {
                //提示连接成功
                return true;
            } else {
                //提示连接失败
                return false;
            }

        } else {
            return false;
        }
    }

    @Override
    public void itemQuery(String incode) {
        setCurrentPage(false);
        mView.showLoading("");
        mBiz.queryProduct(mContext, incode, "QueryActivity", new CommonViewCommonResult(mView) {
            @Override
            public void onSuccess(String result) {


                super.onSuccess(result);
                mView.setIncode(mView.getIncode());
                Gson gson = new Gson();
                ProductInfo info = gson.fromJson(result, ProductInfo.class);
                mView.showProductInfo(info);
            }

            @Override
            public void onInterrupt(int code, String message) {
                super.onInterrupt(code, message);
                mView.hideLoading();
                mView.setWrongIncode(mView.getIncode());
                mView.clearInfo();
                // 播放错误声
                mView.playBeep();

            }
        });
    }

    @Override
    public boolean getCurrentPage() {
        return pageState;
    }

    @Override
    public void setCurrentPage(boolean b) {
        pageState = b;
    }

    @Override
    public void setPrintType(int type) {
        printType = type;
        if(type == 1){
            mView.showNotice("已设为正常模板");
        }else if(type == 2){
            mView.showNotice("已设为特价模板");
        }else if(type == 3){
            mView.showNotice("已设为空白模板");
        }
    }

    @Override
    public void queryRfid2Incode(String data) {
        mBiz.getIncodeFromRfid(mContext, data, "QueryActivity", new ICommonResult() {
            @Override
            public void onSuccess(String result) {
                mView.setIncode(result);
            }

            @Override
            public void onFail(Call call, Exception e) {

            }

            @Override
            public void onInterrupt(int code, String message) {

            }
        });
    }

    /**
     * 标准模板
     *
     * @return
     */
    private Bitmap getPriceTagSpec(String price, String oldPrice, String brand, String name, String shelf, String warranty, String level, String place) {
        /*
         *打印标签方法
         * 1.bitmap 需要打印的图像
         * 2.xx 标签x轴偏移（单位像素）
         * 3.yy 标签y轴偏移（单位像素）
         * 4.width 标签宽度（单位像素）
         * 5.height 标签高度（单位像素）
         * 6.Rotate 旋转角度（0,90,180,270）
         * 7.gotopaper 标签类型定位类型 0连续纸,1间隙纸,2 左侧黑标定位,3右侧黑标定位,4孔定位,5背部黑标定位
         */




        Bitmap bm = Bitmap.createBitmap(x, y, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bm);
        canvas.drawColor(Color.WHITE);
        Paint p = new Paint();
        p.setTypeface( Typeface.createFromAsset(mContext.getAssets(),"NotoSansCJK-Regular-1.otf"));
        p.setAntiAlias(true);
        p.setColor(Color.BLACK);
        p.setFakeBoldText(true);
        p.setTextSize(90);
        canvas.drawText("￥"+price, 40, 110, p);
        p.setColor(Color.BLACK);
        p.setTextSize(28);
        int margin = 28;
        canvas.drawText("￥"+oldPrice,160,28,p);
        p.setTextSize(20);
        canvas.drawText(brand, 110, 148 , p);
        canvas.drawText(name, 110, 148+margin , p);
        canvas.drawText(shelf, 110,  148+2*margin, p);
        canvas.drawText(warranty, 110, 148+3*margin, p);
        canvas.drawText(level, 225, 148+3*margin-1, p);
        canvas.drawText(place, 350, 148+3*margin-2, p);
        Bitmap ajioBm = Bitmap.createBitmap(180, 80, Bitmap.Config.ARGB_8888);
        Canvas ajioCanvas = new Canvas(ajioBm);
        ajioCanvas.drawColor(Color.BLACK);
        p.setColor(Color.WHITE);
        p.setTextSize(70);
        ajioCanvas.drawText(division(Integer.parseInt(price),Integer.parseInt(oldPrice)),5,65,p);
        canvas.drawBitmap(ajioBm,400,40,p);
        return bm;
    }



    /**
     * 打印价签模板三
     *空白模板
     * @return
     */
    private Bitmap getPriceTagSpecThree(String price,String oldPrice, String brand, String name, String shelf, String warranty, String level,String place) {
        /*
         *打印标签方法
         * 1.bitmap 需要打印的图像
         * 2.xx 标签x轴偏移（单位像素）
         * 3.yy 标签y轴偏移（单位像素）
         * 4.width 标签宽度（单位像素）
         * 5.height 标签高度（单位像素）
         * 6.Rotate 旋转角度（0,90,180,270）
         * 7.gotopaper 标签类型定位类型 0连续纸,1间隙纸,2 左侧黑标定位,3右侧黑标定位,4孔定位,5背部黑标定位
         */




        Bitmap bm = Bitmap.createBitmap(x, y, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bm);
        canvas.drawColor(Color.WHITE);
        Paint p = new Paint();
        p.setTypeface( Typeface.createFromAsset(mContext.getAssets(),"NotoSansCJK-Regular-1.otf"));
        p.setAntiAlias(true);
        p.setColor(Color.BLACK);
        p.setFakeBoldText(true);
        p.setTextSize(90);
        canvas.drawText("￥"+price, 40, 110, p);
        p.setColor(Color.BLACK);
        p.setTextSize(28);
        int margin = 28;
        canvas.drawText("吊牌价：",50,28,p);
        canvas.drawText("￥"+oldPrice,160,28,p);
        p.setTextSize(20);
        canvas.drawText("品牌："+brand, 50, 148 , p);
        canvas.drawText("品名："+name, 50, 148+margin , p);
        canvas.drawText("货号："+shelf, 50,  148+2*margin, p);
        canvas.drawText("等级："+level, 50, 148+3*margin, p);
        canvas.drawText("产地："+place, 165, 148+3*margin-1, p);
        Bitmap ajioBm = Bitmap.createBitmap(180, 80, Bitmap.Config.ARGB_8888);
        Canvas ajioCanvas = new Canvas(ajioBm);
        ajioCanvas.drawColor(Color.BLACK);
        p.setColor(Color.WHITE);
        p.setTextSize(70);
        ajioCanvas.drawText(division(Integer.parseInt(price),Integer.parseInt(oldPrice)),5,65,p);
        canvas.drawBitmap(ajioBm,400,40,p);
        return bm;
    }

    /**
     * 打印价签模板二
     * @param name
     * 特价模板
     * @return
     */
    private Bitmap getPriceTagSpec(String name) {
        /*
         *打印标签方法
         * 1.bitmap 需要打印的图像
         * 2.xx 标签x轴偏移（单位像素）
         * 3.yy 标签y轴偏移（单位像素）
         * 4.width 标签宽度（单位像素）
         * 5.height 标签高度（单位像素）
         * 6.Rotate 旋转角度（0,90,180,270）
         * 7.gotopaper 标签类型定位类型 0连续纸,1间隙纸,2 左侧黑标定位,3右侧黑标定位,4孔定位,5背部黑标定位
         */


        Bitmap bm = Bitmap.createBitmap(300, 60, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bm);
        canvas.drawColor(Color.WHITE);
        Paint p = new Paint();
        p.setTypeface( Typeface.createFromAsset(mContext.getAssets(),"NotoSansCJK-Regular-1.otf"));
        //        p.setTypeface(Typeface.MONOSPACE);
        p.setColor(Color.BLACK);
        p.setAntiAlias(true);
        p.setTextSize(28);
        p.setTextAlign(Paint.Align.CENTER);
        //        p.setFakeBoldText(true);
        canvas.drawText("折扣价:￥"+name, 122, 44, p);
        return bm;
    }




    /**
     * 判断给定的打印机名称是否是接口所支持的打印机,防止非SDK支持打印机调用出错
     *
     * @param printerName 打印机名称
     * @return 是否支持
     */
    public boolean isSupported(String printerName) {
        return Pattern.compile("^B3" + "_\\d{4}[L]?$").matcher(printerName).matches();
    }

    //整数相除 保留一位小数
    public  String division(int a ,int b){
        String result = "";
        float num =(float)a/b*10;

        DecimalFormat df = new DecimalFormat("0.0");

        result = df.format(num)+"折";

        return result;

    }

}
