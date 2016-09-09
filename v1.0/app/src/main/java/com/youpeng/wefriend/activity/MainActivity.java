package com.youpeng.wefriend.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.youpeng.wefriend.R;
import com.youpeng.wefriend.database.DatabaseHelper;
import com.youpeng.wefriend.datacenter.Constants;
import com.youpeng.wefriend.datacenter.FileManager;
import com.youpeng.wefriend.datacenter.SharedPreferencesManager;
import com.youpeng.wefriend.utils.IntentUtils;
import com.youpeng.wefriend.utils.LogUtils;
import com.youpeng.wefriend.utils.ShareUtils;
import com.youpeng.wefriend.utils.UIUtils;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private NavigationView mNavigationView;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        registerFinishBroadcast();

        String userAccount = SharedPreferencesManager.getUserAccount();
        String userPwd = SharedPreferencesManager.getUserPwd();

        if (TextUtils.isEmpty(userAccount) || TextUtils.isEmpty(userPwd)) {
            IntentUtils.startLoginActivity(this);
        }

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open,
                R.string.drawer_close);
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);


       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "待添加", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        findViewById(R.id.llayout_news).setOnClickListener(this);
        findViewById(R.id.llayout_course).setOnClickListener(this);
        findViewById(R.id.llayout_examinfo).setOnClickListener(this);
        findViewById(R.id.llayout_gradepoint).setOnClickListener(this);
        findViewById(R.id.llayout_teachevaluation).setOnClickListener(this);
        findViewById(R.id.llayout_globalquery).setOnClickListener(this);

    }

    private void registerFinishBroadcast() {
        IntentFilter filter = new IntentFilter(Constants.INTENT_FINISH_MAIN_ACTIIVTY);
        registerReceiver(mFinishMainActivityBR,filter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        setupDrawerContent();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mFinishMainActivityBR);
        super.onDestroy();
    }

    private void setupDrawerContent() {

        View header = mNavigationView.getHeaderView(0);

        TextView mTvUserName = (TextView) header.findViewById(R.id.tv_nick_name);
        TextView mTvUserSign = (TextView) header.findViewById(R.id.tv_user_sign);
        CircleImageView civProfile = (CircleImageView) header.findViewById(R.id.profile_image);

        String nickName = SharedPreferencesManager.getUserNikeName();
        if (!TextUtils.isEmpty(nickName)) {
            mTvUserName.setText(nickName);
        } else {
            mTvUserName.setText(SharedPreferencesManager.getUserName());
        }
        //设置个性签名
        mTvUserSign.setText(SharedPreferencesManager.getUserSign());

        //设置头像
        Bitmap avatarBitmap = BitmapFactory.decodeFile(FileManager.getAvatarFileName());
        if (null != avatarBitmap) {
            civProfile.setImageBitmap(avatarBitmap);
        }

        civProfile.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_profile: {
                        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                        startActivity(intent);
                        break;
                    }

                    case R.id.nav_logout: {
                        showExitDialog(item);
                        break;
                    }
                    case R.id.nav_share: {
                        ShareUtils.share(MainActivity.this);
                        break;
                    }
                }
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                //item.setChecked(true);
                return true;
            }
        });
    }

    private void showExitDialog(final MenuItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("确定要退出登录吗?");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                item.setChecked(false);
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                processUserExit();

                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.putExtra("logout", "logout");
                startActivity(intent);
                finish();
            }
        });
        builder.create().show();
    }

    private void processUserExit() {
        //删除sharedPreference
        SharedPreferencesManager.clearSharedPreferences();

        //删除数据库
        this.deleteDatabase(DatabaseHelper.DATABASE_NAME);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llayout_news:
                IntentUtils.startConstructActivity(this);
                break;
            case R.id.llayout_course:
                IntentUtils.startCourseActivity(this);
                break;
            case R.id.llayout_examinfo:
                IntentUtils.startConstructActivity(this);
                break;
            case R.id.llayout_gradepoint:
                IntentUtils.startGradePointActivity(this);
                break;
            case R.id.llayout_teachevaluation:
                IntentUtils.startConstructActivity(this);
                break;
            case R.id.llayout_globalquery:
                IntentUtils.startConstructActivity(this);
                break;
        }
    }

    private long mExitTime = 0;

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
            mDrawerLayout.closeDrawer(Gravity.LEFT);
        } else {
            if (System.currentTimeMillis() - mExitTime > 2000) {
                UIUtils.showLongToast("再按一次退出程序");
                mExitTime = System.currentTimeMillis();
            } else {
                finish();
            }
        }

    }

    private BroadcastReceiver mFinishMainActivityBR = new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {
            LogUtils.d("mian activity receive broadcast");
            MainActivity.this.finish();
        }
    };
}

