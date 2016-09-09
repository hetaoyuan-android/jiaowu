package com.youpeng.wefriend.datacenter;

import android.content.Context;
import android.content.SharedPreferences;

import com.youpeng.wefriend.application.WeFriendApplication;

/**
 * Author Ideal
 * Date   2016/2/26
 * Desc
 */
public class SharedPreferencesManager {

    public interface FileName {
        public String userInfo = "user_info";
        public String eduInfo = "edu_info";
    }

    public static void clearSharedPreferences() {
        getSharedPreference(FileName.userInfo).edit().clear().commit();
        getSharedPreference(FileName.eduInfo).edit().clear().commit();
    }

    //用户相关信息
    public static SharedPreferences getSharedPreference(String fileName) {
        return WeFriendApplication.context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
    }

    public static void saveUserName(String username) {
        getSharedPreference(FileName.userInfo).edit().putString("user_name", username).commit();
    }

    public static String getUserName() {
        return getSharedPreference(FileName.userInfo).getString("user_name", null);
    }

    public static void saveUserAccount(String account) {
        getSharedPreference(FileName.userInfo).edit().putString("user_account", account).commit();
    }

    public static String getUserAccount() {
        return getSharedPreference(FileName.userInfo).getString("user_account", null);
    }

    public static void saveUserPwd(String pwd) {
        getSharedPreference(FileName.userInfo).edit().putString("user_pwd", pwd).commit();
    }

    public static String getUserPwd() {
        return getSharedPreference(FileName.userInfo).getString("user_pwd", null);
    }

    //nike_name
    public static void saveUserNikeName(String nikeName) {
        getSharedPreference(FileName.userInfo).edit().putString("user_nike_name", nikeName).commit();
    }

    public static String getUserNikeName() {
        return getSharedPreference(FileName.userInfo).getString("user_nike_name", null);
    }

    //sign
    public static void saveUserSign(String sign) {
        getSharedPreference(FileName.userInfo).edit().putString("user_sign", sign).commit();
    }

    public static String getUserSign() {
        return getSharedPreference(FileName.userInfo).getString("user_sign", "点击头像,可编辑个人资料");
    }


    //教务相关信息
    public static void saveScoreJson(String json) {
        getSharedPreference(FileName.eduInfo).edit().putString("score_json", json).commit();
    }

    public static String getScoreJson() {
        return getSharedPreference(FileName.eduInfo).getString("score_json", null);

    }

    public static void saveCurrentWeek(int week) {
        getSharedPreference(FileName.eduInfo).edit().putInt("current_week", week).commit();
    }

    public static int getCurrentWeek() {
        return getSharedPreference(FileName.eduInfo).getInt("current_week", 1);
    }

    public static void saveWeekTimestatmp(long timestamp) {
        getSharedPreference(FileName.eduInfo).edit().putLong("week_timestatmp", timestamp).commit();
    }

    public static long getWeekTimestatmp() {
        long timestamp = getSharedPreference(FileName.eduInfo).getLong("week_timestatmp", 0);
        if (timestamp == 0) {
            timestamp = System.currentTimeMillis();
            saveWeekTimestatmp(timestamp);
        }
        return timestamp;
    }
}
