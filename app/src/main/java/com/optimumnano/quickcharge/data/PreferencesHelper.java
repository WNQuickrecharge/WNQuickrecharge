package com.optimumnano.quickcharge.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.optimumnano.quickcharge.bean.Location;
import com.optimumnano.quickcharge.bean.UserInfo;
import com.optimumnano.quickcharge.utils.JsonSerializer;


/**
 * Created by 秋平 on 2016/10/11.
 */

public class PreferencesHelper {

    private static final String SETTING = "setting";
    private static final String FIRST_TIME = "firstTime";
    private static final String ISLOGIN = "isLogin";
    private static final boolean FIRST_TIME_DEFAULT = true;
    private static final String PREF_KEY_SIGNED_IN_USER = "PREF_KEY_SIGNED_IN_USER";
    private final SharedPreferences mPref;

    public static final String LOCATION_CITY = "city";
    public static final String LOCATION = "location";
    public static final String USER_ROLE = "user_role";
    public static final String IS_OPEN_SHOW_ONLY_FREE = "is_open_show_free";
    public static final String KM = "km";
    public static final String KV = "kv";
    public static final String CAR_VIN="car_vin";


    public PreferencesHelper(Context context) {
        this.mPref = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE);
    }


    public boolean isLogined() {
        return mPref.getBoolean(ISLOGIN, false);
    }

    public void setIslogin(boolean islogin) {
        SharedPreferences.Editor editor = mPref.edit();
        editor.putBoolean(ISLOGIN, islogin);
        editor.apply();
    }

    public int showDistance() {
        return mPref.getInt(KM, 4);
    }

    public void setKV(int km) {
        SharedPreferences.Editor editor = mPref.edit();
        editor.putInt(KV, km);
        editor.apply();
    }

    public int showKV() {
        return mPref.getInt(KV, 120);
    }

    public void setShowDistance(int km) {
        SharedPreferences.Editor editor = mPref.edit();
        editor.putInt(KM, km);
        editor.apply();
    }

    public boolean isShowOnlyFree() {
        return mPref.getBoolean(IS_OPEN_SHOW_ONLY_FREE, true);
    }

    public void setIsShowOnlyFree(boolean isShowFree) {
        SharedPreferences.Editor editor = mPref.edit();
        editor.putBoolean(IS_OPEN_SHOW_ONLY_FREE, isShowFree);
        editor.apply();
    }


    private void setCity(String city) {
        SharedPreferences.Editor editor = mPref.edit();
        editor.putString(LOCATION_CITY, city);
        editor.apply();
    }

    public void setLocation(double lng, double lat) {
        Location location = new Location();
        location.lat = lat;
        location.lng = lng;

        SharedPreferences.Editor editor = mPref.edit();
        editor.putString(LOCATION, JsonSerializer.serialize(location));
        editor.apply();
    }

    public void setCarVin(String carVin) {
        SharedPreferences.Editor editor = mPref.edit();
        editor.putString(CAR_VIN, carVin);
        editor.apply();
    }

    public Location getLocation() {
        String location = mPref.getString(LOCATION, null);
        if (location != null)
            return JsonSerializer.deSerialize(location, Location.class);
        return null;
    }


    public String getCity() {
        return mPref.getString(LOCATION_CITY, null);
    }

    public String getCarVin(){return  mPref.getString(CAR_VIN, null);}

    public boolean updateCity(String city) {
        if (TextUtils.isEmpty(city) || city == "")
            return false;
        String city1 = getCity();
        if (city1 == null) {
            setCity(city);
            return true;
        } else {
            if (city1.equals(city)) {
                return false;
            } else {
                setCity(city);
                return true;
            }
        }
    }

    public boolean isFirstTime() {
        return mPref.getBoolean(FIRST_TIME, FIRST_TIME_DEFAULT);
    }

    public void saveFirstTime(boolean isFirst) {
        SharedPreferences.Editor editor = mPref.edit();
        editor.putBoolean(FIRST_TIME, isFirst);
        editor.apply();
    }

    public void clear() {
        mPref.edit().clear().apply();
    }


    public void putSignedInUser(UserInfo usr) {
        mPref.edit().putString(PREF_KEY_SIGNED_IN_USER, JsonSerializer.serialize(usr)).apply();
    }

    @Nullable
    public UserInfo getSignedInUser() {
        String ribotJson = mPref.getString(PREF_KEY_SIGNED_IN_USER, null);
        if (ribotJson == null) return null;
        return JsonSerializer.deSerialize(ribotJson, UserInfo.class);
    }


    public static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(SETTING, Context.MODE_PRIVATE);
    }


}
