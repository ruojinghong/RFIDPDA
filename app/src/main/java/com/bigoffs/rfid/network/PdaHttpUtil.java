package com.bigoffs.rfid.network;

import android.content.Context;

import com.bigoffs.rfid.GlobalCfg;
import com.bigoffs.rfid.mvp.bean.User;
import com.bigoffs.rfid.util.UserManager;

import com.zhy.http.okhttp.OkHttpUtils;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

/**
 * Created by okbuy on 17-2-7.
 */

public class PdaHttpUtil {
    public static void post(Context context, String url, Map<String, String> params, ICommonResult callback) {
        OkHttpUtils.post().url(url).params(getSignedParams(context, params)).build().execute(new ResultCallback(context, callback));
    }

    public static void post(Context context, String url, Map<String, String> params, String tag, ICommonResult callback, boolean domyself) {
        OkHttpUtils.post().url(url).params(getSignedParams(context, params)).tag(tag).build().execute(new ResultCallback(context, callback, domyself));
    }

    public static void postWithoutCallabck(Context context, String url, Map<String, String> params) {
        OkHttpUtils.post().url(url).params(getSignedParams(context, params)).build().execute(new NullCallback());
    }

    private static Map<String, String> getSignedParams(Context context, Map<String, String> params) {
        params.put("terminal_version", GlobalCfg.VERSION);
        params.put("timestamp", String.valueOf(System.currentTimeMillis())
                .substring(0, 10));
        params.put("device_type","pda");
        if (UserManager.isLogin(context)) {
            User user = UserManager.getUser(context);
            params.put("user_id", user.userId);
            params.put("access_token", user.accessToken);
        }
        String[] keys = params.keySet().toArray(new String[0]);
        Arrays.sort(keys);
        ArrayList<String> signPair = new ArrayList<>();

        for (String key : keys) {
            signPair.add(key + "=" + params.get(key));
        }
        String signContent = StringUtils.join(signPair, "&");
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(signContent.getBytes("UTF8"));
            final byte[] resultByte = messageDigest.digest();
            final String result = new String(Hex.encodeHex(resultByte));
            params.put("sign", result);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return params;
    }
}
