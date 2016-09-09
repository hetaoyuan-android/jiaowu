package com.youpeng.wefriend.utils;

import android.content.Context;
import android.content.Intent;

import com.youpeng.wefriend.R;

/**
 * Author Ideal
 * Date   2016/3/14
 * Desc
 */
public class ShareUtils {
    /**
     * 分享
     */
    public static void share(Context context){
        share(context, context.getString(R.string.share_text));
    }

    /**
     * 分享
     */
    public static void share(Context context, String content){
        Intent intent=new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.share));
        intent.putExtra(Intent.EXTRA_TEXT, content);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(intent, context.getString(R.string.share)));
    }

}
