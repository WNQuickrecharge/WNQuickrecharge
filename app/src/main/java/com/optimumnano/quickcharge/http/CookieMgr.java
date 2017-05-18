package com.optimumnano.quickcharge.http;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Base64;

import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.Cookie;
import okhttp3.HttpUrl;

/**
 * Created by herry on 2017/5/3.
 */

public class CookieMgr {
    private static final String COOKIE_PREF_NAME = "cookie_pref";

    /**
     * key : encoded httpurl
     * </p>
     * value:cookie name&domain VS cookie
     * </p>
     * 每一个httpurl对应一个Cookie list
     */
    private Map<String, ConcurrentHashMap<String, Cookie>> cookieStore;

    private static CookieMgr INSTANCE = null;
    private Context context;
    private SharedPreferences mPrefs;

    public static CookieMgr getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (CookieMgr.class) {
                if (INSTANCE == null) {
                    INSTANCE = new CookieMgr(context);
                }
            }
        }
        return INSTANCE;
    }


    private CookieMgr(Context context) {
        this.context = context;
        cookieStore = new HashMap<String, ConcurrentHashMap<String, Cookie>>();
        mPrefs = context.getSharedPreferences(COOKIE_PREF_NAME, Context.MODE_PRIVATE);
        //collect info into memory
        collectCookieIntoMemory();
    }

    public void saveCookie(HttpUrl url, List<Cookie> cookieList) {
        if (cookieList == null || cookieList.isEmpty()) {
            return;
        }
        String key = encodeHttpUrl(url);
        for (Cookie cookie : cookieList) {
            //save to memory first
            String cookieKey = cookieKey(cookie);
            saveCookieToMemory(key, cookieKey, cookie);
            persistentCookie(key, cookieKey, cookie);
        }
    }

    public List<Cookie> getCookie(HttpUrl url) {
        String key = encodeHttpUrl(url);
        List<Cookie> ret = new ArrayList<Cookie>();
        boolean exist = cookieStore.containsKey(key);
        if (!exist) {
            return ret;//empty
        }
        ret.addAll(cookieStore.get(key).values());
        return ret;
    }

    public void clear() {
        cookieStore.clear();
        mPrefs.edit().clear();
    }

    protected String encodeHttpUrl(HttpUrl url) {
        String ret = "";
        String candidate = url.host() + "@" + url.port();
        ret = MD5Utils.getStringMD5(candidate);
        return ret;
    }


    protected String encodeCookie(String cookieJson) {
        String ret = "";
        try {
            ret = Base64.encodeToString(cookieJson.getBytes("utf-8"), Base64.DEFAULT);
        } catch (UnsupportedEncodingException e) {
            ret = cookieJson;
        }
        return ret;
    }

    protected String decodeCookie(String encodedCookie) {
        String ret = new String(Base64.decode(encodedCookie, Base64.DEFAULT));
        return ret;
    }

    /*唯一标识一个Cookie*/
    protected String cookieKey(Cookie cookie) {
        return cookie.name() + "@" + cookie.domain();
    }


    /**
     * format Cookie object to Json Object
     */
    protected String toJson(Cookie cookie) {
        Gson gson = new Gson();
        String ret = gson.toJson(cookie);
        return ret;
    }

    /**
     * format Json Object to Cookie object
     */

    protected Cookie toCookie(String json) {
        Gson gson = new Gson();
        Cookie c = gson.fromJson(json, Cookie.class);
        return c;
    }


    //private
    private void collectCookieIntoMemory() {
        Map<String, ?> map = mPrefs.getAll();
        for (Map.Entry<String, ?> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = (String) entry.getValue();
            String[] candidateKeys = TextUtils.split(value, ",");
            for (String condidateKey : candidateKeys) {
                String condidateValue = mPrefs.getString(condidateKey, null);
                if (condidateValue != null) {
                    if (!cookieStore.containsKey(key)) {
                        cookieStore.put(key, new ConcurrentHashMap<String, Cookie>());
                    }
                    cookieStore.get(key).put(value, toCookie(decodeCookie(condidateValue)));
                }
            }
        }
    }


    private void saveCookieToMemory(String key, String cookieKey, Cookie cookie) {
        if (cookieStore.containsKey(key)) {
            cookieStore.get(key).put(cookieKey, cookie);
        } else {
            ConcurrentHashMap<String, Cookie> data = new ConcurrentHashMap<String, Cookie>();
            data.put(cookieKey, cookie);
            cookieStore.put(key, data);
        }
    }

    private void persistentCookie(String key, String cookieKey, Cookie cookie) {
        SharedPreferences.Editor editor = mPrefs.edit();
        String cookieKeySet = TextUtils.join(",", cookieStore.get(key).keySet());
        editor.putString(key, cookieKeySet);
        editor.putString(cookieKey, encodeCookie(toJson(cookie)));
        editor.apply();
    }
}
