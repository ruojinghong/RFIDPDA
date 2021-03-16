package com.bigoffs.rfid.persistence.util;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import com.bigoffs.rfid.R;


/**
 * 声音有关工具类
 */
public class PlaySoundPoolUtils {
    // SoundPool对象
    public static SoundPool mSoundPlayer = new SoundPool(10,
            AudioManager.STREAM_SYSTEM, 5);
    public static PlaySoundPoolUtils soundPlayUtils;
    // 上下文
    static Context mContext;

    /**
     * 初始化
     *
     * @param context
     */
    public static PlaySoundPoolUtils init(Context context) {
        if (soundPlayUtils == null) {
            soundPlayUtils = new PlaySoundPoolUtils();
        }

        // 初始化声音
        mContext = context;

        mSoundPlayer.load(mContext, R.raw.ok, 1);// 1
        mSoundPlayer.load(mContext, R.raw.didi, 1);//2
        mSoundPlayer.load(mContext, R.raw.msg, 1);// 3
        return soundPlayUtils;
    }

    /**
     * 播放声音
     *
     * @param soundID
     */
    public static void play(int soundID) {
        mSoundPlayer.play(soundID, 1, 1, 0, 0, 1);
    }
}