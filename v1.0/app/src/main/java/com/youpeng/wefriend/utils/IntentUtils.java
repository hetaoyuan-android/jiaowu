package com.youpeng.wefriend.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.youpeng.wefriend.activity.ConstructActivity;
import com.youpeng.wefriend.activity.CourseActivity;
import com.youpeng.wefriend.activity.GradePointActivity;
import com.youpeng.wefriend.activity.LoginActivity;
import com.youpeng.wefriend.datacenter.FileManager;

import java.io.File;

/**
 * Author Ideal
 * Date   2016/3/2
 * Desc
 */
public class IntentUtils {
    public static final int TAKE_PICTURE_REQUEST_CODE = 1;
    public static final int CHOOSE_PICTURE_REQUEST_CODE = 2;
    public static final int PROFILE_REQUEST_CODE = 3;


    public static void startLoginActivity(Activity activity) {
        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
    }

    public static void startGradePointActivity(Activity activity) {
        Intent intent = new Intent(activity, GradePointActivity.class);
        activity.startActivity(intent);
    }

    public static void startConstructActivity(Activity activity) {
        Intent intent = new Intent(activity, ConstructActivity.class);
        activity.startActivity(intent);
    }

    public static void startCourseActivity(Activity activity) {
        Intent intent = new Intent(activity, CourseActivity.class);
        activity.startActivity(intent);
    }

    //拍照
    public static void gotoTakePicture(Activity activity) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri uri = Uri.fromFile(new File(FileManager.getImagesDir(),FileManager.AVATAR_FILENAME));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        activity.startActivityForResult(intent, TAKE_PICTURE_REQUEST_CODE);
    }

    //从相册选择
    public static void gotoChoosePicture(Activity activity) {
        //Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        //intent.setType("image/*");
        Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activity.startActivityForResult(intent, CHOOSE_PICTURE_REQUEST_CODE);
    }
}
