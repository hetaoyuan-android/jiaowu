package com.youpeng.wefriend.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.youpeng.wefriend.R;
import com.youpeng.wefriend.model.CourseBean;

import java.io.Serializable;

public class CourseDetailActivity extends AppCompatActivity {

    private TextView mTvTitle;

    private TextView mTvClassRoom;
    private TextView mTvTeacher;
    private TextView mTvClassName;
    private TextView mTvSection;
    private TextView mTvWeek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        initView();
        initData();
    }

    private void initData() {
        String[] weekdayChArray = new String[]{"周一", "周二", "周三", "周四", "周五", "周六", "周日"};

        Intent intent = getIntent();
        CourseBean courseBean = (CourseBean) intent.getSerializableExtra("beanInfo");

        mTvTitle.setText(courseBean.getCourseName());

        mTvClassRoom.setText(courseBean.getCourseRoom());
        mTvTeacher.setText(courseBean.getTeacherName());
        mTvClassName.setText(courseBean.getCourseName());

        mTvSection.setText(weekdayChArray[courseBean.getWeekday() - 1] + " " + courseBean.getSection() + "节");

        String textWeek = courseBean.getCourseWeek();
        if (textWeek.contains(",")) {
            String[] textArray = textWeek.split(",");
            mTvWeek.setText(textArray[0] + "-" + textArray[textArray.length - 1]);
        } else {
            mTvWeek.setText(textWeek);
        }

    }

    private void initView() {

        mTvTitle = (TextView) findViewById(R.id.tv_title);

        mTvClassRoom = (TextView) findViewById(R.id.tv_classroom);
        mTvTeacher = (TextView) findViewById(R.id.tv_teacher);
        mTvClassName = (TextView) findViewById(R.id.tv_coursename);
        mTvSection = (TextView) findViewById(R.id.tv_section);
        mTvWeek = (TextView) findViewById(R.id.tv_week);
    }
}
