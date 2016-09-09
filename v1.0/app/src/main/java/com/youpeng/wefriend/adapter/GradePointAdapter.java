package com.youpeng.wefriend.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.youpeng.wefriend.R;
import com.youpeng.wefriend.model.ScoreBean;

import java.util.List;

/**
 * Author Ideal
 * Date   2016/3/2
 * Desc
 */
public class GradePointAdapter extends RecyclerView.Adapter<GradePointAdapter.ViewHolder> {
    private List<ScoreBean> scoreBeanList;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTvClassName;
        public TextView mTvExamStatus;
        public TextView mTvScore;
        public TextView mTvGradePoint;

        public ViewHolder(View v) {
            super(v);
            mTvClassName= (TextView) v.findViewById(R.id.tv_coursename);
            mTvExamStatus= (TextView) v.findViewById(R.id.tv_exam_status);
            mTvScore= (TextView) v.findViewById(R.id.tv_score);
            mTvGradePoint= (TextView) v.findViewById(R.id.tv_gradepoint);
        }
    }

    public GradePointAdapter(List<ScoreBean> scoreBeanList) {
        this.scoreBeanList = scoreBeanList;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public GradePointAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_gradepoint, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTvClassName.setText(scoreBeanList.get(position).getCourseName());
        holder.mTvExamStatus.setText(scoreBeanList.get(position).getExamCharecter());
        holder.mTvScore.setText(scoreBeanList.get(position).getScore());
        holder.mTvGradePoint.setText(scoreBeanList.get(position).getGradePoint());

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return scoreBeanList.size();
    }
}
