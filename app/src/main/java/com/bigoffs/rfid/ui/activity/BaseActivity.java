package com.bigoffs.rfid.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PowerManager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bigoffs.rfid.MApplication;
import com.bigoffs.rfid.R;
import com.bigoffs.rfid.listener.OnFinishListener;
import com.bigoffs.rfid.mvp.view.ICommonView;
import com.bigoffs.rfid.persistence.util.ResourcesUtils;
import com.bigoffs.rfid.service.IScanService;
import com.bigoffs.rfid.service.ScanServiceControl;
import com.bigoffs.rfid.util.ToastUtil;
import com.bigoffs.rfid.util.UserManager;

/**
 * @author okbuy-001
 * @desc
 * @time 2020/4/22 14:17
 */
public class BaseActivity extends AppCompatActivity implements ICommonView {

    public MApplication mApplication;
    public ProgressDialog mypDialog;
    public String TAG = "test";
    public IScanService scanService = ScanServiceControl.getScanService();
    public Drawable redBackground;
    public Drawable commonBackground;
    public Drawable grayBackground;
    public AlertDialog.Builder builder;
    private ILogoutPresenter mLogoutPresenter;

    private boolean mShouldPlayBeep;
    private MediaPlayer mMediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApplication = MApplication.getInstance();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mypDialog = new ProgressDialog(BaseActivity.this);
        mypDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mypDialog.setCanceledOnTouchOutside(false);
        mypDialog.setCancelable(false);
        acquireWakeLock(this);
        SetScreenPorait(this);
        mLogoutPresenter = new LogoutPresenter(this, this);
        redBackground = ResourcesUtils.getDrawable(this, R.drawable.btn_click_red_havebackground);
        grayBackground = ResourcesUtils.getDrawable(this, R.drawable.btn_click_grey_havebackground);
        commonBackground = ResourcesUtils.getDrawable(this, R.drawable.button_common);
        builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.ic_lunch);
        builder.setTitle("提示!");
        scanService.setListener(new OnFinishListener() {
            @Override
            public void OnFinish(String data) {
                setData(data);
            }
        });

        //无title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //全屏
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public Toast mToast;

    public void ShowToast(final String text) {
        try {
            if (!TextUtils.isEmpty(text)) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        if (mToast == null) {
                            mToast = Toast.makeText(getApplicationContext(), text,
                                    Toast.LENGTH_LONG);
                        } else {
                            mToast.setText(text);
                        }
                        mToast.show();
                    }
                });

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ShowToast(final int resId) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (mToast == null) {
                    mToast = Toast.makeText(BaseActivity.this.getApplicationContext(), resId,
                            Toast.LENGTH_LONG);
                } else {
                    mToast.setText(resId);
                }
                mToast.show();
            }
        });
    }

    public void startAnimActivity(Class<?> cla) {
        this.startActivity(new Intent(this, cla));
    }

    public void startAnimActivity(Intent intent) {
        this.startActivity(intent);
    }

    /**
     * 判断用户在EditText中输入的信息是否完整
     *
     * @param editTexts
     * @return false 意味着用户输入的是完整的
     * true  意味着至少有一个EditText用户未输入内容
     */
    public boolean isEmpty(EditText... editTexts) {

        for (EditText et : editTexts) {
            if (TextUtils.isEmpty(et.getText().toString())) {

                String string = "请输入完整!";

                ShowToast(string);
                return true;
            }
        }

        return false;
    }

    public long firstPress;

    public void stopProgressDialog(final String result) {
        if (!mypDialog.isShowing()) return;
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mypDialog.cancel();
                    if (result != null) {
                        Toast.makeText(BaseActivity.this, result,
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startProgressDialog(final String text) {
        if (mypDialog.isShowing()) return;
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mypDialog.setMessage(text);
                    mypDialog.show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showDialog(final String result) {
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    builder.setMessage(result);
                    builder.setPositiveButton("确定", null);
                    builder.show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mypDialog.dismiss();
    }


    PowerManager.WakeLock wakeLock = null;

    @SuppressWarnings("deprecation")
    public void acquireWakeLock(Activity activity) {
        if (wakeLock == null) {
            PowerManager pm = (PowerManager) activity
                    .getSystemService(Context.POWER_SERVICE);
            wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, this
                    .getClass().getCanonicalName());
            wakeLock.acquire();
            // View.setKeepScreenOn(true);
        }
    }

    public void SetScreenPorait(Activity activity) {
        if (activity.getRequestedOrientation() != android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        }
    }

    public void setData(String data) {

    }

    @Override
    public void showLoading(String mssage) {
        startProgressDialog(mssage);
    }

    @Override
    public void hideLoading() {
        stopProgressDialog(null);
    }

    @Override
    public void goLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void showNotice(String msg) {
        ToastUtil.showToast(this, msg);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        if (UserManager.isLogin(this)) {
            menu.getItem(0).setTitle(UserManager.getUser(this).userName);
        }
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_profile:
                // do nothing
                break;
            case R.id.action_home:
                goHome();
                break;
            case R.id.action_task:
                goTask();
                break;
            case R.id.action_settings:
                goSetting();
                break;
            case R.id.action_logout:
                new AlertDialog.Builder(this).setMessage("确定要退出登录吗？").setNegativeButton("取消",null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mLogoutPresenter.logout();
                    }
                }).show();
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public interface ILogoutPresenter {
        void logout();
    }



    private void goHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    private void goSetting() {
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
    }

    private void goTask() {
        if (this instanceof TaskActivity) {
            return;
        }
        Intent intent = new Intent(this, TaskActivity.class);
        startActivity(intent);
    }

    protected void beep() {
        if (mShouldPlayBeep && mMediaPlayer != null) {
            mMediaPlayer.seekTo(0);
            mMediaPlayer.start();
        }
    }


}
