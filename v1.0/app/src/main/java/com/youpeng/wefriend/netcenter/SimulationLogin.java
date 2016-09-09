package com.youpeng.wefriend.netcenter;


import android.text.TextUtils;

import com.youpeng.wefriend.datacenter.SharedPreferencesManager;
import com.youpeng.wefriend.model.CourseBean;
import com.youpeng.wefriend.model.ScoreBean;

import okhttp3.FormBody;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Ideal on 2016/2/25.
 */
public class SimulationLogin {

    private static final String TAG = "simulation";
    public static final int SERVER_BUSY = 1;
    public static final int PWD_ERR = 2;
    public static final int NET_ERR = 3;
    public static final int NET_USELESS = 4;
    public static final int DATAPARSE_ERR = 5;
    public static final int STATUS_OK = 6;

    private static SimulationLogin simulationLogin = new SimulationLogin();

    private SimulationLogin() {
    }

    public static int STATUS_CODE;
    private String mAccount;
    private String mPwd;

    public static SimulationLogin getInstance() {
        return simulationLogin;
    }

    private String login(OkHttpClient httpClient) {
        try {
            Request request = new Request.Builder()
                    .url(ConstStr.INDEX_URL)
                    .build();
            Response response = httpClient.newCall(request).execute();

            if (response.code() != 200) {
                STATUS_CODE = NET_ERR;
                return null;
            }
            String htmlContent = response.body().string();
            response.body().close();

            if (TextUtils.isEmpty(htmlContent)) {
                STATUS_CODE = NET_ERR;
                return null;
            }

            String viewStateValue = "";
            String eventValidationValue = "";
            String cmdokValue = "";
            Document document;
            try {
                document = Jsoup.parse(htmlContent);
                Element viewsSate = document.getElementById("__VIEWSTATE");
                Element eventValidation = document.getElementById("__EVENTVALIDATION");
                Element cmdok = document.getElementById("cmdok");


                viewStateValue = viewsSate.val();
                eventValidationValue = eventValidation.val();
                cmdokValue = cmdok.val();
            } catch (Exception e) {
                STATUS_CODE = DATAPARSE_ERR;
                e.printStackTrace();
                return null;
            }

            //第二次请求 拿到Cookie
            RequestBody requestBody = new FormBody.Builder()
                    .add(ConstStr.ACCOUNT_NAME, mAccount)
                    .add(ConstStr.PWD_NAME, mPwd)
                    .add("__VIEWSTATE", viewStateValue)
                    .add("__EVENTVALIDATION", eventValidationValue)
                    .add("cmdok", cmdokValue)
                    .build();

            request = new Request.Builder()
                    .url(ConstStr.LONGIN_URL)
                    .post(requestBody)
                    .build();

            response = httpClient.newCall(request).execute();

            if (response.code() != 200) {
                STATUS_CODE = NET_ERR;
                return null;
            }

            htmlContent = response.body().string();

            if (TextUtils.isEmpty(htmlContent)) {
                STATUS_CODE = NET_ERR;
                return null;
            }


            document = Jsoup.parse(htmlContent);
            Element loginError = document.getElementById("Lerror");

            if (loginError != null) {
                String loginErrorText = loginError.text();

                if (!TextUtils.isEmpty(loginErrorText)) {
                    if (loginErrorText.contains("用户名或密码错误")) {
                        STATUS_CODE = PWD_ERR;
                        return null;
                    }
                    if (loginErrorText.contains("忙")) {
                        STATUS_CODE = SERVER_BUSY;
                        return null;
                    }
                }
            }
            //第三次请求 登录
            request = new Request.Builder()
                    .url(ConstStr.CONTENT_URL)
                    .build();
            response = httpClient.newCall(request).execute();

            if (response.code() != 200) {
                STATUS_CODE = NET_ERR;
                return null;
            }
            htmlContent = response.body().string();
            document = Jsoup.parse(htmlContent);
            Element userElement = document.getElementById("users");

            String userName = userElement.text();
            return userName;

        } catch (Exception e) {
            STATUS_CODE = NET_ERR;
            e.printStackTrace();
            return null;
        }
    }

    public String spiderCourse() {
        String htmlContent = null;
        try {
            STATUS_CODE = STATUS_OK;

            CookieManager cookieManager = new CookieManager();
            cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
            OkHttpClient httpClient = new OkHttpClient.Builder()
                    .connectTimeout(50, TimeUnit.SECONDS)
                    .readTimeout(50, TimeUnit.SECONDS)
                    .cookieJar(new JavaNetCookieJar(cookieManager))
                    .build();

            String userName = login(httpClient);
            if (TextUtils.isEmpty(userName)) {
                return null;
            }

            //第一次请求,系统正在读取数据
            Request request = new Request.Builder().url(ConstStr.COURSE_URL + mAccount).build();
            Response response = httpClient.newCall(request).execute();
            if (response.code() != 200) {
                STATUS_CODE = NET_ERR;
                return null;
            }
            response.body().close();

            //Thread.sleep(60000);
            //拿到数据
            request = new Request.Builder().url(ConstStr.COURSE_CONTENT_URL).build();
            response = httpClient.newCall(request).execute();
            if (response.code() != 200) {
                STATUS_CODE = NET_ERR;
                return null;
            }
            htmlContent = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
            STATUS_CODE = NET_ERR;
            return null;
        }
        return htmlContent;
    }

    public String spiderScore() {
        String resultContent = null;
        STATUS_CODE = STATUS_OK;
        try {

            CookieManager cookieManager = new CookieManager();
            cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
            OkHttpClient httpClient = new OkHttpClient.Builder()
                    .connectTimeout(50, TimeUnit.SECONDS)
                    .readTimeout(50, TimeUnit.SECONDS)
                    .cookieJar(new JavaNetCookieJar(cookieManager))
                    .build();

            //login get cookie
            String userName = login(httpClient);
            if (TextUtils.isEmpty(userName)) {
                return null;
            }

            Request request = new Request.Builder().url(ConstStr.SCORE_URL + mAccount).build();
            Response response = httpClient.newCall(request).execute();


            String htmlContent = response.body().string();
            if (TextUtils.isEmpty(htmlContent)) {
                STATUS_CODE = NET_ERR;
                return null;
            }
            String viewStateValue = "";
            String eventValidationValue = "";
            try {


                Document document = Jsoup.parse(htmlContent);
                Element viewsSate = document.getElementById("__VIEWSTATE");
                Element eventValidation = document.getElementById("__EVENTVALIDATION");

                viewStateValue = viewsSate.val();
                eventValidationValue = eventValidation.val();
            } catch (Exception e) {
                STATUS_CODE = DATAPARSE_ERR;
                e.printStackTrace();
                return null;
            }

            RequestBody requestBody = new FormBody.Builder()
                    .add("btnSearch", "+%B2%E9+%D1%AF+")
                    .add("DropDownList1", "2015-2016-1")
                    .add("selSearch", "0")
                    .add("__VIEWSTATE", viewStateValue)
                    .add("__EVENTVALIDATION", eventValidationValue)
                    .build();

            request = new Request.Builder().
                    url(ConstStr.SCORE_URL + mAccount)
                    .post(requestBody)
                    .build();
            response = httpClient.newCall(request).execute();

            resultContent = response.body().string();
            if (TextUtils.isEmpty(resultContent)) {
                STATUS_CODE = NET_ERR;
                return null;
            }

        } catch (Exception e) {
            STATUS_CODE = NET_ERR;
            e.printStackTrace();
        }
        return resultContent;
    }

    public List<CourseBean> parseCourse() {
        this.mAccount = SharedPreferencesManager.getUserAccount();
        this.mPwd = SharedPreferencesManager.getUserPwd();

        String htmlContent = spiderCourse();
        if (TextUtils.isEmpty(htmlContent)) {
            return null;
        }
        List<CourseBean> courseBeanList = new ArrayList<>();

        try {
            Document document = Jsoup.parse(htmlContent);
            Element kbElement = document.getElementById("kb");

            Elements bodyContent = kbElement.getElementsByTag("tbody");
            Element bodyElement = bodyContent.get(0);

            Elements trElements = bodyElement.getElementsByTag("tr");

            for (int row = 0; row < trElements.size(); row++) {
                Element trElement = trElements.get(row);
                if (row == 0 || row == trElements.size() - 1) {
                    continue;
                }

                Elements tdElements = trElement.getElementsByTag("td");
                for (int column = 0; column < tdElements.size(); column++) {
                    if (column == 0) {
                        continue;
                    }
                    Element tdElement = tdElements.get(column);

                    CourseBean courseBean = new CourseBean();
                    Elements aElements = tdElement.getElementsByTag("a");
                    if (aElements.size() <= 0) {
                        courseBean.setHasCourse(false);
                        courseBeanList.add(courseBean);
                    } else {
                        //for (int i = 0; i < aElements.size(); i++) {
                        Element aElement = aElements.get(0);
                        String courseContent = aElement.attr("title");
                        String[] filed = courseContent.split("\n");

                        for (int num = 0; num < filed.length; num++) {
                            String[] value = filed[num].split("：");
                            switch (num) {
                                case 0: {
                                    courseBean.setCourseNum(value[1]);
                                    break;
                                }
                                case 1: {
                                    courseBean.setCourseId(value[1]);
                                    break;
                                }
                                case 2: {
                                    courseBean.setCourseName(value[1]);
                                    break;
                                }
                                case 3: {
                                    courseBean.setTeacherName(value[1]);
                                    break;
                                }
                                case 4: {
                                    courseBean.setStartTime(value[1]);
                                    break;
                                }
                                case 5: {
                                    courseBean.setCourseWeek(value[1]);
                                    break;
                                }
                                case 6: {
                                    courseBean.setCourseRoom(value[1]);
                                    break;
                                }
                                case 7: {
                                    courseBean.setCourseClass(value[1]);
                                    break;
                                }
                            }
                        }
                        courseBean.setWeekday(column);
                        courseBean.setSection((row * 2 - 1) + "-" + (row * 2));
                        courseBean.setHasCourse(true);
                        courseBeanList.add(courseBean);
                    }
                }
            }
        } catch (Exception e) {
            STATUS_CODE = DATAPARSE_ERR;
            e.printStackTrace();
            return null;
        }
        return courseBeanList;
    }

    public List<ScoreBean> parseScore() {
        this.mAccount = SharedPreferencesManager.getUserAccount();
        this.mPwd = SharedPreferencesManager.getUserPwd();

        String content = spiderScore();
        if (TextUtils.isEmpty(content)) {
            return null;
        }
        try {
            Document document = Jsoup.parse(content);
            Element table = document.getElementById("cjxx");
            List<ScoreBean> scoreBeanList = new ArrayList<>();

            Elements tbody = table.getElementsByTag("tbody");
            for (Element tbodyElement : tbody) {
                Elements trElements = tbodyElement.getElementsByTag("tr");

                for (Element trElement : trElements) {
                    ScoreBean scoreBean = new ScoreBean();

                    Elements tdElements = trElement.getElementsByTag("td");
                    for (int i = 0; i < tdElements.size(); i++) {
                        Element element = tdElements.get(i);
                        String text = element.text();
                        switch (i) {
                            case 0: {
                                if (text.equals("√")) {
                                    scoreBean.setIsPass(true);
                                } else {
                                    scoreBean.setIsPass(false);
                                }
                                break;
                            }
                            case 1: {
                                scoreBean.setTerm(text);
                                break;
                            }
                            case 2: {
                                scoreBean.setCourseNum(text);
                                break;
                            }
                            case 3: {
                                scoreBean.setCourseName(text);
                                break;
                            }
                            case 4: {
                                scoreBean.setScore(text);
                                break;
                            }
                            case 5: {
                                scoreBean.setCredit(text);
                                break;
                            }
                            case 6: {
                                scoreBean.setClassCount(text);
                                break;
                            }
                            case 7: {
                                scoreBean.setClassCharecter(text);
                                break;
                            }
                            case 8: {
                                scoreBean.setClassCategory(text);
                                break;
                            }
                            case 9: {
                                scoreBean.setExamCharecter(text);
                                break;
                            }
                            case 10: {
                                scoreBean.setExamMethod(text);
                                break;
                            }
                        }
                    }
                    scoreBeanList.add(scoreBean);
                }
            }
            return scoreBeanList;

        } catch (Exception e) {
            STATUS_CODE = DATAPARSE_ERR;
            e.printStackTrace();
            return null;
        }
    }

    public String userLogin(String account, String pwd) {
        this.mAccount = account;
        this.mPwd = pwd;

        STATUS_CODE = STATUS_OK;
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .connectTimeout(50, TimeUnit.SECONDS)
                .readTimeout(50, TimeUnit.SECONDS)
                .cookieJar(new JavaNetCookieJar(cookieManager))
                .build();

        String userName = login(httpClient);
        return userName;
    }

}

