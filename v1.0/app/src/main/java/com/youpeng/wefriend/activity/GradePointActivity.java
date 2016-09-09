package com.youpeng.wefriend.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.youpeng.wefriend.R;
import com.youpeng.wefriend.adapter.GradePointAdapter;
import com.youpeng.wefriend.datacenter.SharedPreferencesManager;
import com.youpeng.wefriend.model.ScoreBean;
import com.youpeng.wefriend.netcenter.SimulationLogin;

import java.util.List;

public class GradePointActivity extends AppCompatActivity {

    private static final int MSG_LOAD_SCORE = 0;
    private static final int MSG_REFRESH_SCORE = 1;

    private RecyclerView mRvGradePoint;
    private GradePointAdapter mAdapter;
    private LinearLayout mProgressLayout;
    private View mProgressView;
    private TextView mTvInfo;
    //private Spinner mSpTerm;

    private List<ScoreBean> scoreBeans;

    Handler handler = new GradePointHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gradepoint);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("学分绩点");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        mProgressView = findViewById(R.id.pb_progress);
        mTvInfo = (TextView) findViewById(R.id.tv_info);
        mProgressLayout = (LinearLayout) findViewById(R.id.llayout_progress);

        mRvGradePoint = (RecyclerView) findViewById(R.id.recycler_grade);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRvGradePoint.setLayoutManager(layoutManager);


        /*mSpTerm = (Spinner) findViewById(R.id.sp_term);
        ArrayAdapter<CharSequence> spAdapter = ArrayAdapter.createFromResource(this,
                R.array.term_array, android.R.layout.simple_spinner_item);
        spAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpTerm.setAdapter(spAdapter);
        mSpTerm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] termArray = getResources().getStringArray(R.array.term_array);
                UIUtils.showLongToast(termArray[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

        String scoreJson = SharedPreferencesManager.getScoreJson();
        if (TextUtils.isEmpty(scoreJson)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    scoreBeans = SimulationLogin.getInstance().parseScore();
                    handler.sendEmptyMessage(MSG_LOAD_SCORE);
                }
            }).start();
        } else {
            mProgressLayout.setVisibility(View.GONE);
            Gson gson = new Gson();
            scoreBeans = gson.fromJson(scoreJson, new TypeToken<List<ScoreBean>>() {
            }.getType());
            mAdapter = new GradePointAdapter(scoreBeans);
            mRvGradePoint.setAdapter(mAdapter);
        }
    }

    public void refreshScore() {
        mProgressLayout.setVisibility(View.VISIBLE);
        mProgressView.setVisibility(View.VISIBLE);
        mTvInfo.setText("正在计算请稍后...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                scoreBeans = SimulationLogin.getInstance().parseScore();
                handler.sendEmptyMessage(MSG_REFRESH_SCORE);
            }
        }).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_gradepoint, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;

            case R.id.action_refresh:
                refreshScore();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }

    }

    private void setErrorInfo() {
        mProgressLayout.setVisibility(View.VISIBLE);
        mProgressView.setVisibility(View.GONE);
        switch (SimulationLogin.STATUS_CODE) {
            case SimulationLogin.NET_ERR: {
                mTvInfo.setText("网络错误,点击右上角刷新按钮");
                break;
            }
            case SimulationLogin.DATAPARSE_ERR: {
                mTvInfo.setText("数据解析错误,点击右上角刷新按钮");
                break;
            }
            case SimulationLogin.PWD_ERR: {
                mTvInfo.setText("密码错误,请退出重新登录");
                break;
            }
            case SimulationLogin.SERVER_BUSY: {
                mTvInfo.setText("服务器拉力过大,请稍后重试");
                break;
            }
        }
    }

    class GradePointHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_LOAD_SCORE: {
                    if (scoreBeans == null) {
                        setErrorInfo();
                    } else {
                        Gson gson = new Gson();
                        String scoreBeansJson = gson.toJson(scoreBeans);
                        SharedPreferencesManager.saveScoreJson(scoreBeansJson);

                        mProgressLayout.setVisibility(View.GONE);
                        mAdapter = new GradePointAdapter(scoreBeans);
                        mRvGradePoint.setAdapter(mAdapter);
                    }
                    break;
                }
                case MSG_REFRESH_SCORE: {
                    if (scoreBeans == null) {
                        setErrorInfo();
                    } else {
                        Gson gson = new Gson();
                        String scoreBeansJson = gson.toJson(scoreBeans);
                        SharedPreferencesManager.saveScoreJson(scoreBeansJson);

                        mProgressLayout.setVisibility(View.GONE);
                        mAdapter.notifyDataSetChanged();
                    }
                    break;
                }
            }
        }
    }

}
