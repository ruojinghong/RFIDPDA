package com.bigoffs.rfid.util;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;

import com.bigoffs.rfid.GlobalCfg;
import com.bigoffs.rfid.mvp.bean.VersionInfo;


/**
 * Created by okbuy on 17-3-8.
 */

public class UpdateManager {

    private Context mContext;

    private VersionInfo mVersionInfo;

    private IReceiveDataChange mReceiveDataChange;

    private long mId;

    public UpdateManager(Context context, VersionInfo info, IReceiveDataChange receiveDataChange) {
        mContext = context;
        mReceiveDataChange = receiveDataChange;
        mVersionInfo = info;
    }

    // 比较版本号，判断客户端是否需要更新
    private boolean needUpdate() {
        double versionNative = Double.parseDouble(GlobalCfg.VERSION);
        double versionNet = Double.parseDouble(mVersionInfo.version);
        return versionNative < versionNet;
    }

    public void check() {
        if (!needUpdate()) {
            return;
        } else {
            new AlertDialog.Builder(mContext).setTitle("有新版本").setMessage("请升级到最新版本，否则无法使用").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mReceiveDataChange.setReceiveDataChange(true);
                    download();
                }

            }).setCancelable(false).show();
        }
    }

    private void download() {
        DownloadManager manager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(mVersionInfo.url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        // 设置什么网络的情况下才可以下载更新，这里是wifi和流量都可以
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        // 设置通知栏显示方式
//        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);// 只在下载过程中显示通知，下载完成取消通知
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);// 下载过程中显示通知，下载完成通知不消失，提示安装
        request.setVisibleInDownloadsUi(true);// 设置该下载是否出现在系统的下载列表内
        // 设置文件的存储路径
//        request.setDestinationInExternalFilesDir(mContext, Environment.DIRECTORY_DOWNLOADS, "UmsPDA.apk");
        request.setDestinationInExternalPublicDir("PDA", "UmsPDA"+mVersionInfo.version+".apk");
        // 设置下载的文件类型
        request.setMimeType("application/vnd.android.package-archive");
        // 设置标题和描述
        request.setTitle("UmsPDA"+mVersionInfo.version);
        // 获取唯一id
        mId = manager.enqueue(request);
        SPUtil.storeDownloadId(mContext, mId);
        // 注册下载完成时的广播
//        mContext.registerReceiver(new ApkInstallReceiver(), new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

}
