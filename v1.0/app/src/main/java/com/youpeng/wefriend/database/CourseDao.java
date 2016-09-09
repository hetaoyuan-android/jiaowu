package com.youpeng.wefriend.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.youpeng.wefriend.application.WeFriendApplication;
import com.youpeng.wefriend.model.CourseBean;
import com.youpeng.wefriend.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Author Ideal
 * Date   2016/3/9
 * Desc
 */
public class CourseDao {

    private DatabaseHelper helper;

    public CourseDao() {
        helper = new DatabaseHelper(WeFriendApplication.context);
    }

    public boolean addAll(List<CourseBean> courseBeanList) {
        SQLiteDatabase db = helper.getReadableDatabase();

        for (CourseBean bean : courseBeanList) {
            boolean result = add(bean, db);
            if (!result) {
                db.close();
                return result;
            }
        }
        db.close();
        return true;
    }

    public boolean add(CourseBean bean, SQLiteDatabase db) {

        ContentValues contentValuse = new ContentValues();
        contentValuse.put("courseNum", bean.getCourseNum());
        contentValuse.put("courseId", bean.getCourseId());
        contentValuse.put("courseName", bean.getCourseName());
        contentValuse.put("teacherName", bean.getTeacherName());
        contentValuse.put("startTime", bean.getStartTime());
        contentValuse.put("courseTime", bean.getCourseWeek());
        contentValuse.put("courseRoom", bean.getCourseRoom());
        contentValuse.put("courseClass", bean.getCourseClass());

        contentValuse.put("hasCourse", bean.isHasCourse() ? 1 : 0);
        contentValuse.put("bgResourceId", bean.getBgResourceId());
        contentValuse.put("weekday", bean.getWeekday());
        contentValuse.put("section", bean.getSection());

        long rowId = db.insert(DatabaseHelper.TABLE_COURE, null, contentValuse);
        if (rowId == -1) {
            return false;
        } else {
            return true;
        }
    }

    public List<CourseBean> loadAll() {
        SQLiteDatabase db = helper.getReadableDatabase();
        List<CourseBean> courseBeans = new ArrayList<>();

        Cursor cursor = db.rawQuery("select * from " + DatabaseHelper.TABLE_COURE, null);
        while (cursor.moveToNext()) {
            CourseBean bean = new CourseBean();
            bean.setCourseNum(cursor.getString(1));
            bean.setCourseId(cursor.getString(2));
            bean.setCourseName(cursor.getString(3));
            bean.setTeacherName(cursor.getString(4));
            bean.setStartTime(cursor.getString(5));
            bean.setCourseWeek(cursor.getString(6));
            bean.setCourseRoom(cursor.getString(7));
            bean.setCourseClass(cursor.getString(8));

            bean.setHasCourse(cursor.getInt(9) == 1 ? true : false);
            bean.setBgResourceId(cursor.getInt(10));
            bean.setWeekday(cursor.getInt(11));
            bean.setSection(cursor.getString(12));

            courseBeans.add(bean);
        }
        cursor.close();
        db.close();
        LogUtils.d(courseBeans.toString());
        return courseBeans;
    }

    public int deleteAll() {
        SQLiteDatabase database = helper.getWritableDatabase();
        int count = database.delete(DatabaseHelper.TABLE_COURE, null, null);
        database.close();
        return count;
    }

    public void deteleTabale() {
        SQLiteDatabase database = helper.getWritableDatabase();
        database.execSQL("drop table " + DatabaseHelper.TABLE_COURE);
        database.close();
    }

}
