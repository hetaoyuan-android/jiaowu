package com.youpeng.wefriend.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youpeng.wefriend.R;
import com.youpeng.wefriend.datacenter.SharedPreferencesManager;
import com.youpeng.wefriend.utils.UIUtils;

/**
 * Author Ideal
 * Date   2016/3/13
 * Desc
 */
public class WeekPreviewAdapter extends RecyclerView.Adapter<WeekPreviewAdapter.MyViewHolder> {

    private OnItemClickListener mItemListener;
    private Context mContext;
    private int mCurrentWeek;
    int data[] = new int[25];

    public interface OnItemClickListener {
        public void OnItemClick(int postion);
    }

    public void setOnItemClickListener(OnItemClickListener itemListener) {
        mItemListener = itemListener;
    }

    public WeekPreviewAdapter(Context context) {
        mContext = context;
        mCurrentWeek = SharedPreferencesManager.getCurrentWeek();
        for (int i = 0; i < data.length; i++) {
            data[i] = i;
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout linearLayout = new LinearLayout(mContext);
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        TextView tvContent = new TextView(mContext);
        tvContent.setClickable(true);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //params.setMargins(0, UIUtils.dp2pix(5), 0,  UIUtils.dp2pix(5));
        //params.gravity = Gravity.CENTER;
        tvContent.setPadding(0, UIUtils.dp2pix(5), 0, UIUtils.dp2pix(5));
        //tvContent.setLayoutParams(params);

        linearLayout.addView(tvContent);
        return new MyViewHolder(linearLayout);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final int week = position + 1;
        TextView tvContent = holder.mTvContent;

        if (week == mCurrentWeek) {
            tvContent.setBackgroundResource(R.drawable.ic_current_week_bg_bule);
            tvContent.setText("第 " + week + " 周(本周)");
        } else {
            tvContent.setBackgroundDrawable(new BitmapDrawable());
            tvContent.setText("第 " + week + " 周");
        }

        tvContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemListener != null) {
                    mItemListener.OnItemClick(position + 1);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView mTvContent;

        public MyViewHolder(ViewGroup itemView) {
            super(itemView);
            mTvContent = (TextView) itemView.getChildAt(0);
        }
    }
}
