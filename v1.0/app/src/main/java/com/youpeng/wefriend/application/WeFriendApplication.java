package com.youpeng.wefriend.application;

import android.app.Application;
import android.content.Context;

/**
 * Author Ideal
 * Date   2016/2/26
 * Desc
 */
public class WeFriendApplication extends Application {

    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }
}
