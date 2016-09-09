package com.youpeng.wefriend.activity;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;

import com.youpeng.wefriend.R;
import com.youpeng.wefriend.adapter.CourseAdapter;
import com.youpeng.wefriend.adapter.WeekPreviewAdapter;
import com.youpeng.wefriend.database.CourseDao;
import com.youpeng.wefriend.datacenter.SharedPreferencesManager;
import com.youpeng.wefriend.model.CourseBean;
import com.youpeng.wefriend.netcenter.SimulationLogin;
import com.youpeng.wefriend.utils.LogUtils;
import com.youpeng.wefriend.utils.UIUtils;
import com.youpeng.wefriend.view.WeekDayView;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CourseActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int MSG_LOAD_COURSE = 0;
    private static final int MSG_REFRESH_COURSE = 1;
    private static final int MSG_COME_NORMAL_WEEK = 2;

    private List<CourseBean> mCourseBeans;

    private CourseHandler mHandler = new CourseHandler();
    private RecyclerView mRvCourse;
    private CourseAdapter mAdapter;

    private WeekDayView mWvMon;
    private WeekDayView mWvSun;
    private WeekDayView mWvSat;
    private WeekDayView mWvTue;
    private WeekDayView mWvWed;
    private WeekDayView mWvThu;
    private WeekDayView mWvFri;
    private TextView mTvWeekShow;
    private TextView mTvMouth;
    private ProgressBar mPbLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("课表");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        initView();
        initData();
    }

    private void initView() {
        mTvMouth = (TextView) findViewById(R.id.tv_mouth);

        mWvMon = (WeekDayView) findViewById(R.id.tv_mon);
        mWvTue = (WeekDayView) findViewById(R.id.tv_tue);
        mWvWed = (WeekDayView) findViewById(R.id.tv_wed);
        mWvThu = (WeekDayView) findViewById(R.id.tv_thu);
        mWvFri = (WeekDayView) findViewById(R.id.tv_fri);
        mWvSat = (WeekDayView) findViewById(R.id.tv_sat);
        mWvSun = (WeekDayView) findViewById(R.id.tv_sun);

        mRvCourse = (RecyclerView) findViewById(R.id.rv_course);
        mRvCourse.setLayoutManager(new GridLayoutManager(this, 7));

        findViewById(R.id.ll_week_set).setOnClickListener(this);
        mTvWeekShow = (TextView) findViewById(R.id.tv_week_show);

        mPbLoading = (ProgressBar) findViewById(R.id.pb_loading);
    }

    private void initData() {

        setDateAndWeek(0);

        new Thread(new Runnable() {
            @Override
            public void run() {
                CourseDao courseDao = new CourseDao();
                mCourseBeans = courseDao.loadAll();
                if (mCourseBeans == null || mCourseBeans.size() <= 0) {

                    mCourseBeans = SimulationLogin.getInstance().parseCourse();
                    if (SimulationLogin.STATUS_CODE == SimulationLogin.STATUS_OK) {

                        ////////////===========设置颜色===========////////////////////////
                        int[] bgArray = new int[]{R.drawable.course_bohelv_selector,
                                R.drawable.course_cheng_selector, R.drawable.course_cyan_selector,
                                R.drawable.course_fen_selector, R.drawable.course_huang_selector,
                                R.drawable.course_kafei_selector, R.drawable.course_lan_selector,
                                R.drawable.course_lv_selector, R.drawable.course_molan_selector,
                                R.drawable.course_pulan_selector, R.drawable.course_qing_selector,
                                R.drawable.course_tao_selector, R.drawable.course_tuhuang_selector,
                                R.drawable.course_zi_selector,
                        };

                        int bgNum = 0;
                        Map map = new HashMap();

                        for (int position = 0; position < mCourseBeans.size(); position++) {
                            CourseBean courseBean = mCourseBeans.get(position);
                            if (courseBean.isHasCourse()) {

                                boolean contain = map.containsKey(courseBean.getCourseId());
                                if (contain) {
                                    courseBean.setBgResourceId((Integer) map.get(courseBean.getCourseId()));
                                } else {
                                    int bgColor = bgArray[bgNum++];
                                    map.put(courseBean.getCourseId(), bgColor);
                                    courseBean.setBgResourceId(bgColor);
                                }
                            }
                        }
                        ////////////===========设置颜色==END=========////////////////////////
                        courseDao.addAll(mCourseBeans);
                    } else {
                        //网络请求出错
                    }
                }
                mHandler.sendEmptyMessage(MSG_LOAD_COURSE);

            }
        }).start();
    }

    private void setDateAndWeek(int weekOffset) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.WEEK_OF_YEAR, weekOffset);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int dayOfMouth = 0;

        if (weekOffset == 0) {
            switch (dayOfWeek) {
                case Calendar.MONDAY: {
                    //更新周数
                    calendar.setTimeInMillis(SharedPreferencesManager.getWeekTimestatmp());
                    int preDay = calendar.get(Calendar.DAY_OF_WEEK);

                    if (preDay != Calendar.MONDAY) {
                        SharedPreferencesManager.saveWeekTimestatmp(System.currentTimeMillis());
                        SharedPreferencesManager.saveCurrentWeek(SharedPreferencesManager.getCurrentWeek() + 1);
                    }

                    mWvMon.setCurSelectBg();
                    break;
                }
                case Calendar.TUESDAY: {
                    mWvTue.setCurSelectBg();
                    break;
                }
                case Calendar.WEDNESDAY: {
                    mWvWed.setCurSelectBg();
                    break;
                }
                case Calendar.THURSDAY: {
                    mWvThu.setCurSelectBg();
                    break;
                }
                case Calendar.FRIDAY: {
                    mWvFri.setCurSelectBg();
                    break;
                }
                case Calendar.SATURDAY: {
                    mWvSat.setCurSelectBg();
                    break;
                }
                case Calendar.SUNDAY: {
                    mWvSun.setCurSelectBg();
                    break;
                }
            }
            mTvWeekShow.setText("第 " + SharedPreferencesManager.getCurrentWeek() + " 周");
            mTvWeekShow.setTextColor(getResources().getColor(android.R.color.tertiary_text_light));
        } else {
            LogUtils.d("上一次的颜色" + mTvWeekShow.getCurrentTextColor() + "");
            mTvWeekShow.setTextColor(getResources().getColor(R.color.colorBrown));
            mTvWeekShow.setText("第 " + (SharedPreferencesManager.getCurrentWeek() + weekOffset) + " 周(非本周)");
        }

        //设置日期
        if (dayOfWeek == Calendar.SUNDAY) {
            dayOfWeek = 8;
        }
        Date data = new Date();

        calendar.setTime(data);
        calendar.add(Calendar.WEEK_OF_YEAR, weekOffset);
        calendar.add(Calendar.DAY_OF_YEAR, 2 - dayOfWeek);
        dayOfMouth = calendar.get(Calendar.DAY_OF_MONTH);
        int mouthOfYear = calendar.get(Calendar.MONTH);
        mTvMouth.setText(mouthOfYear + 1 + "月");
        mWvMon.setDay(String.valueOf(dayOfMouth));

        calendar.setTime(data);
        calendar.add(Calendar.WEEK_OF_YEAR, weekOffset);
        calendar.add(Calendar.DAY_OF_YEAR, 3 - dayOfWeek);
        dayOfMouth = calendar.get(Calendar.DAY_OF_MONTH);
        mWvTue.setDay(String.valueOf(dayOfMouth));

        calendar.setTime(data);
        calendar.add(Calendar.WEEK_OF_YEAR, weekOffset);
        calendar.add(Calendar.DAY_OF_YEAR, 4 - dayOfWeek);
        dayOfMouth = calendar.get(Calendar.DAY_OF_MONTH);
        mWvWed.setDay(String.valueOf(dayOfMouth));

        calendar.setTime(data);
        calendar.add(Calendar.WEEK_OF_YEAR, weekOffset);
        calendar.add(Calendar.DAY_OF_YEAR, 5 - dayOfWeek);
        dayOfMouth = calendar.get(Calendar.DAY_OF_MONTH);
        mWvThu.setDay(String.valueOf(dayOfMouth));

        calendar.setTime(data);
        calendar.add(Calendar.WEEK_OF_YEAR, weekOffset);
        calendar.add(Calendar.DAY_OF_YEAR, 6 - dayOfWeek);
        dayOfMouth = calendar.get(Calendar.DAY_OF_MONTH);
        mWvFri.setDay(String.valueOf(dayOfMouth));

        calendar.setTime(data);
        calendar.add(Calendar.WEEK_OF_YEAR, weekOffset);
        calendar.add(Calendar.DAY_OF_YEAR, 7 - dayOfWeek);
        dayOfMouth = calendar.get(Calendar.DAY_OF_MONTH);
        mWvSat.setDay(String.valueOf(dayOfMouth));

        calendar.setTime(data);
        calendar.add(Calendar.WEEK_OF_YEAR, weekOffset);
        calendar.add(Calendar.DAY_OF_YEAR, 8 - dayOfWeek);
        dayOfMouth = calendar.get(Calendar.DAY_OF_MONTH);
        mWvSun.setDay(String.valueOf(dayOfMouth));

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_week_set:
                setPreviewWeek(v);
                break;
        }
    }

    private void setPreviewWeek(View view) {
        View viewSetWeek = LayoutInflater.from(this).inflate(R.layout.view_setweek, null);

        RecyclerView rlWeekSelect = (RecyclerView) viewSetWeek.findViewById(R.id.rl_week_select);
        rlWeekSelect.setLayoutManager(new LinearLayoutManager(this));
        WeekPreviewAdapter previewAdapter = new WeekPreviewAdapter(this);

        rlWeekSelect.setAdapter(previewAdapter);

        final PopupWindow popupWindow = new PopupWindow(viewSetWeek, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.showAsDropDown(mTvWeekShow, -UIUtils.dp2pix(88), UIUtils.dp2pix(5));

        previewAdapter.setOnItemClickListener(new WeekPreviewAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(int postion) {
                int weekOffset = postion - SharedPreferencesManager.getCurrentWeek();
                setDateAndWeek(weekOffset);

                mAdapter.notifyDataSetChanged();
                mAdapter.setWeekOffset(weekOffset);
                popupWindow.dismiss();

                Message message = Message.obtain();
                message.what = MSG_COME_NORMAL_WEEK;
                boolean hasMessages = mHandler.hasMessages(MSG_COME_NORMAL_WEEK);
                if (hasMessages) {
                    mHandler.removeMessages(MSG_COME_NORMAL_WEEK);
                }
                mHandler.sendMessageDelayed(message, 5000);
            }
        });
    }

    @Override
    protected void onPause() {
        backWeekNormal();
        super.onPause();
    }

    private void backWeekNormal() {
        boolean hasMessages = mHandler.hasMessages(MSG_COME_NORMAL_WEEK);
        if (hasMessages) {
            mHandler.removeMessages(MSG_COME_NORMAL_WEEK);
            setDateAndWeek(0);
            mAdapter.setWeekOffset(0);
            mAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_course, menu);

        MenuItem item = menu.getItem(0);
        SpannableString s = new SpannableString("设置");
        s.setSpan(new ForegroundColorSpan(Color.BLUE), 0, s.length(), 0);
        item.setTitle(s);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            backWeekNormal();

            new DialogFragment(){
                DialogFragment mDialog;

                @Override
                public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
                    mDialog = this;
                    getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
                    View weekDialog = LayoutInflater.from(CourseActivity.this).inflate(R.layout.dialog_select_week, null);
                    final NumberPicker npWeek = (NumberPicker) weekDialog.findViewById(R.id.np_week);
                    npWeek.setMinValue(1);
                    npWeek.setMaxValue(25);
                    npWeek.setValue(SharedPreferencesManager.getCurrentWeek());
                    //设置为不可循环,不能编辑
                    npWeek.setDescendantFocusability(TimePicker.FOCUS_BLOCK_DESCENDANTS);
                    npWeek.setWrapSelectorWheel(false);

                    weekDialog.findViewById(R.id.tv_cancle).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mDialog.dismiss();
                        }
                    });

                    weekDialog.findViewById(R.id.tv_ok).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LogUtils.d(npWeek.getValue() + "");
                            mTvWeekShow.setText("第 " + npWeek.getValue() + " 周");
                            SharedPreferencesManager.saveCurrentWeek(npWeek.getValue());
                            SharedPreferencesManager.saveWeekTimestatmp(System.currentTimeMillis());

                            mAdapter.notifyDataSetChanged();

                            mDialog.dismiss();
                        }
                    });
                    return weekDialog;
                }
            }.show(getFragmentManager(), "weekdialog");

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class CourseHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_LOAD_COURSE: {
                    mAdapter = new CourseAdapter(CourseActivity.this, mCourseBeans);
                    mRvCourse.setAdapter(mAdapter);
                    mPbLoading.setVisibility(View.GONE);
                    break;
                }
                case MSG_COME_NORMAL_WEEK: {
                    setDateAndWeek(0);
                    mAdapter.setWeekOffset(0);
                    mAdapter.notifyDataSetChanged();
                    break;
                }
            }
        }
    }

}
