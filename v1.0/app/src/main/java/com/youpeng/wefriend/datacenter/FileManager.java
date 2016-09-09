package com.youpeng.wefriend.datacenter;

import android.content.Context;
import android.os.Environment;

import com.youpeng.wefriend.application.WeFriendApplication;
import com.youpeng.wefriend.utils.LogUtils;

import java.io.File;

/**
 * Author Ideal
 * Date   2016/3/15
 * Desc
 */
public class FileManager {

    public static final String AVATAR_FILENAME = "avatar.jpg";

    public static final String ROOT_PATH = "youpeng";
    public static final String IMAGE_PATH = "images";


    public static String getImagesDir() {
        return getDir(IMAGE_PATH);
    }

    public static String getAvatarFileName() {
        return getImagesDir() + File.separator + AVATAR_FILENAME;
    }

    public static String getDir(String targetPath) {
        String path;

        LogUtils.d("getDir :" + WeFriendApplication.context.getDir(targetPath, Context.MODE_PRIVATE).toString());

        if (isSDcardvailable()) {
            path = Environment.getExternalStorageDirectory().getAbsoluteFile() + "/" + ROOT_PATH
                    + "/" + targetPath;
        } else {
            // data/data/com.ideal.appmaket/cache/cache
            path = WeFriendApplication.context.getDir(targetPath, Context.MODE_PRIVATE).getAbsoluteFile().toString();
        }
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }


    public static boolean isSDcardvailable() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }
}
