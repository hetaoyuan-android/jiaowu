package com.youpeng.wefriend.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.youpeng.wefriend.R;

/**
 * Author Ideal
 * Date   2016/3/12
 * Desc
 */
public class WeekDayView extends FrameLayout {

    private String mWeekDayText;
    private TextView mTvDay;


    public WeekDayView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.WeekDayView);
        mWeekDayText = ta.getString(R.styleable.WeekDayView_weekday);
        ta.recycle();

        LayoutInflater.from(getContext()).inflate(R.layout.view_weekday, this, true);

        TextView tvWeekDay = (TextView) findViewById(R.id.tv_week);
        tvWeekDay.setText(mWeekDayText);
        mTvDay = (TextView) findViewById(R.id.tv_day);

    }


    public void setDay(String dayText) {
        mTvDay.setText(dayText);
    }

    public void setCurSelectBg(){
        this.setBackgroundResource(R.drawable.ic_week_cur_select_bg);
    }
}
