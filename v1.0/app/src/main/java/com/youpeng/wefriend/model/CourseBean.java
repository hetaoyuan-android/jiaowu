package com.youpeng.wefriend.model;

import java.io.Serializable;

/**
 * Created by Ideal on 2016/2/23.
 */
public class CourseBean implements Serializable {

    /***********************************
     * 开课编号
     * 课程编码
     * 课程名称
     * 授课教师
     * 开课时间
     * 上课周次     格式 1-12
     * 开课地点
     * 上课班级
     * weekday     周几
     * section     1-2节
     ***********************************/
    private String courseNum;
    private String courseId;
    private String courseName;
    private String teacherName;
    private String startTime;
    private String courseWeek;
    private String courseRoom;
    private String courseClass;
    private int weekday;
    private String section;

    //添加
    private boolean hasCourse;


    @Override
    public String toString() {
        return "CourseBean{" +
                "courseNum='" + courseNum + '\'' +
                ", courseId='" + courseId + '\'' +
                ", courseName='" + courseName + '\'' +
                ", teacherName='" + teacherName + '\'' +
                ", startTime='" + startTime + '\'' +
                ", courseWeek='" + courseWeek + '\'' +
                ", courseRoom='" + courseRoom + '\'' +
                ", courseClass='" + courseClass + '\'' +
                ", weekday=" + weekday +
                ", section='" + section + '\'' +
                ", hasCourse=" + hasCourse +
                ", bgResourceId=" + bgResourceId +
                '}';
    }

    public int getWeekday() {
        return weekday;
    }

    public void setWeekday(int weekday) {
        this.weekday = weekday;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    private int bgResourceId;



    public int getBgResourceId() {
        return bgResourceId;
    }

    public void setBgResourceId(int bgResourceId) {
        this.bgResourceId = bgResourceId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public boolean isHasCourse() {
        return hasCourse;
    }

    public void setHasCourse(boolean hasCourse) {
        this.hasCourse = hasCourse;
    }

    public String getCourseNum() {
        return courseNum;
    }

    public void setCourseNum(String courseNum) {
        this.courseNum = courseNum;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getCourseWeek() {
        return courseWeek;
    }

    public void setCourseWeek(String courseWeek) {
        this.courseWeek = courseWeek;
    }

    public String getCourseRoom() {
        return courseRoom;
    }

    public void setCourseRoom(String courseRoom) {
        this.courseRoom = courseRoom;
    }

    public String getCourseClass() {
        return courseClass;
    }

    public void setCourseClass(String courseClass) {
        this.courseClass = courseClass;
    }
}
