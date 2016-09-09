package com.youpeng.wefriend.database;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Author Ideal
 * Date   2016/3/9
 * Desc
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "wefriend.db";
    public static final String TABLE_COURE = "tb_course";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_COURE +
                        " (_id integer primary key autoincrement, " +
                        " courseNum varchar(100), " +
                        " courseId varchar(100), " +
                        " courseName varchar(100), " +
                        " teacherName varchar(100), " +
                        " startTime varchar(100), " +
                        " courseTime varchar(100), " +
                        " courseRoom varchar(100), " +
                        " courseClass varchar(100), " +
                        " hasCourse integer, " +
                        " bgResourceId integer, " +
                        " weekday integer, " +
                        " section vachar(100) " +
                        " )"

        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
