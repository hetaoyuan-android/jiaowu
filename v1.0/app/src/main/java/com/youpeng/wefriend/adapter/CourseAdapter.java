package com.youpeng.wefriend.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.youpeng.wefriend.R;
import com.youpeng.wefriend.activity.CourseDetailActivity;
import com.youpeng.wefriend.datacenter.SharedPreferencesManager;
import com.youpeng.wefriend.model.CourseBean;
import com.youpeng.wefriend.utils.LogUtils;

import java.util.List;

/**
 * Author Ideal
 * Date   2016/3/9
 * Desc
 */
public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.MyViewHolder> {

    private Activity mActivity;
    private List<CourseBean> mCourseBeanList;
    private int mWeekOffset = 0;

    public void setWeekOffset(int offset) {
        mWeekOffset = offset;
    }

    public CourseAdapter(Activity activity, List<CourseBean> beanList) {
        this.mActivity = activity;
        mCourseBeanList = beanList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_course, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final CourseBean courseBean = mCourseBeanList.get(position);

        if (courseBean.isHasCourse()) {
            int currentWeek = SharedPreferencesManager.getCurrentWeek() + mWeekOffset;

            holder.mBgView.setVisibility(View.VISIBLE);

            int recourseId = courseBean.getBgResourceId();

            //解析周数
            String courseWeek = courseBean.getCourseWeek();
            if (courseWeek.contains("双周")) {
                if (currentWeek % 2 == 0) {
                    if (courseWeek.contains("-")) {
                        int startSepIndex = courseWeek.indexOf("-");
                        String startStr = courseWeek.substring(0, startSepIndex);

                        int endSepIndex = courseWeek.indexOf("双周");
                        String endStr = courseWeek.substring(startSepIndex + 1, endSepIndex);

                        int startWeek = Integer.parseInt(startStr);
                        int endWeek = Integer.parseInt(endStr);

                        if (currentWeek < startWeek || currentWeek > endWeek) {
                            recourseId = R.drawable.course_hui_selector;
                        }
                    } else {
                        if (!courseWeek.contains(String.valueOf(currentWeek))) {
                            recourseId = R.drawable.course_hui_selector;
                        }
                    }
                } else {
                    recourseId = R.drawable.course_hui_selector;
                }

            } else if (courseWeek.contains("单周")) {

                if (currentWeek % 2 == 1) {
                    if (courseWeek.contains("-")) {
                        int startSepIndex = courseWeek.indexOf("-");
                        String startStr = courseWeek.substring(0, startSepIndex);

                        int endSepIndex = courseWeek.indexOf("单周");
                        String endStr = courseWeek.substring(startSepIndex + 1, endSepIndex);

                        int startWeek = Integer.parseInt(startStr);
                        int endWeek = Integer.parseInt(endStr);
                        if (currentWeek < startWeek || currentWeek > endWeek) {
                            recourseId = R.drawable.course_hui_selector;
                        }
                    } else {
                        if (!courseWeek.contains(String.valueOf(currentWeek))) {
                            recourseId = R.drawable.course_hui_selector;
                        }
                    }
                } else {
                    recourseId = R.drawable.course_hui_selector;
                }

            } else {
                // 1-12
                if (courseWeek.contains("-")) {
                    int startSepIndex = courseWeek.indexOf("-");
                    String startStr = courseWeek.substring(0, startSepIndex);

                    String endStr = courseWeek.substring(startSepIndex + 1, courseWeek.length());

                    int startWeek = Integer.parseInt(startStr);
                    int endWeek = Integer.parseInt(endStr);

                    if (currentWeek < startWeek || currentWeek > endWeek) {
                        recourseId = R.drawable.course_hui_selector;
                    }
                } else { // 5,6,7,,8,9
                    if (!courseWeek.contains(String.valueOf(currentWeek))) {
                        recourseId = R.drawable.course_hui_selector;
                    }
                }
            }

            holder.mBgView.setBackgroundResource(recourseId);
            holder.mTvContent.setText(mCourseBeanList.get(position).getCourseName());

            holder.mItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LogUtils.d("点击事件 添加");
                    Intent intent = new Intent(mActivity, CourseDetailActivity.class);
                    intent.putExtra("beanInfo", courseBean);
                    mActivity.startActivity(intent);
                }
            });
        } else {
            holder.mBgView.setVisibility(View.GONE);
        }

    }


    @Override
    public int getItemCount() {
        return mCourseBeanList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private View mItemView;
        private View mBgView;
        private TextView mTvContent;

        public MyViewHolder(View itemView) {
            super(itemView);
            mItemView = itemView;
            mTvContent = (TextView) itemView.findViewById(R.id.tv_content);
            mBgView = itemView.findViewById(R.id.rl_backgroud);
        }
    }
}
