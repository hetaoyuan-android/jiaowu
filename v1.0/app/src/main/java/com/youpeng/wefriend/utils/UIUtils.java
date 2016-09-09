package com.youpeng.wefriend.utils;

import android.app.Application;
import android.widget.Toast;

import com.youpeng.wefriend.application.WeFriendApplication;

/**
 * Author Ideal
 * Date   2016/2/27
 * Desc
 */
public class UIUtils {

    private static Toast toast;

    public static void showLongToast(String text) {
        if (toast == null) {
            toast = Toast.makeText(WeFriendApplication.context, text, Toast.LENGTH_LONG);
        } else {
            toast.setText(text);
        }
        toast.show();

    }

    public static void showShortToast(String text) {
        if (toast == null) {
            toast = Toast.makeText(WeFriendApplication.context, text, Toast.LENGTH_SHORT);
        } else {
            toast.setText(text);
        }
        toast.show();
    }

    public static int dp2pix(int dp) {
        float scale = WeFriendApplication.context.getResources().getDisplayMetrics().density;
        return (int) (scale * dp + 0.5);
    }

    public static int pix2dp(int pix) {
        float scale = WeFriendApplication.context.getResources().getDisplayMetrics().density;
        return (int) (pix / scale + 0.5);
    }
}
