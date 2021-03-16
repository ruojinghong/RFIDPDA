package com.bigoffs.rfid.util;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by okbuy on 17-3-10.
 */

public class ApkInstallReceiver {

    public static void installApk(Context context, long downloadId) {
        DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Intent installIntent = new Intent(Intent.ACTION_VIEW);
        Uri downloadFileUri = manager.getUriForDownloadedFile(downloadId);
        if (downloadFileUri != null) {
            installIntent.setDataAndType(downloadFileUri, "application/vnd.android.package-archive");
            installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(installIntent);
        }
    }
}
