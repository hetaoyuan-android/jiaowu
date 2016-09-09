package com.youpeng.wefriend.model;

/**
 * Created by Ideal on 2016/2/23.
 */
public class ScoreBean {

    //网站同等结构
    private boolean isPass;
    private String  term;
    private String  courseNum;
    private String  courseName;
    private String  score;
    private String  credit;
    private String  classCount;
    private String  classCategory;
    private String  classCharecter;
    private String  examCharecter;
    private String  examMethod;

    //添加
    private String gradePoint;

    @Override
    public String toString() {
        return "ScoreBean{" +
                "isPass=" + isPass +
                ", term='" + term + '\'' +
                ", courseNum='" + courseNum + '\'' +
                ", courseName='" + courseName + '\'' +
                ", score='" + score + '\'' +
                ", credit='" + credit + '\'' +
                ", classCount='" + classCount + '\'' +
                ", classCategory='" + classCategory + '\'' +
                ", classCharecter='" + classCharecter + '\'' +
                ", gradePoint='" + gradePoint + '\'' +
                ", examCharecter='" + examCharecter + '\'' +
                ", examMethod='" + examMethod + '\'' +
                '}';
    }


    public String getGradePoint() {
        return gradePoint;
    }

    public void setGradePoint(String gradePoint) {
        this.gradePoint = gradePoint;
    }

    public String getExamCharecter() {
        return examCharecter;
    }

    public void setExamCharecter(String examCharecter) {
        this.examCharecter = examCharecter;
    }

    public String getClassCategory() {
        return classCategory;
    }

    public void setClassCategory(String classCategory) {
        this.classCategory = classCategory;
    }

    public boolean isPass() {
        return isPass;
    }

    public void setIsPass(boolean isPass) {
        this.isPass = isPass;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
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

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getClassCount() {
        return classCount;
    }

    public void setClassCount(String classCount) {
        this.classCount = classCount;
    }

    public String getClassCharecter() {
        return classCharecter;
    }

    public void setClassCharecter(String classCharecter) {
        this.classCharecter = classCharecter;
    }

    public String getExamMethod() {
        return examMethod;
    }

    public void setExamMethod(String examMethod) {
        this.examMethod = examMethod;
    }
}
