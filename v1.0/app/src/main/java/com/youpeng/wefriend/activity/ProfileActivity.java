package com.youpeng.wefriend.activity;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.youpeng.wefriend.R;
import com.youpeng.wefriend.datacenter.Constants;
import com.youpeng.wefriend.datacenter.FileManager;
import com.youpeng.wefriend.datacenter.SharedPreferencesManager;
import com.youpeng.wefriend.utils.BitmapUtil;
import com.youpeng.wefriend.utils.IntentUtils;
import com.youpeng.wefriend.utils.LogUtils;
import com.youpeng.wefriend.utils.UIUtils;

import java.io.File;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int MSG_IMAGE_COMPRESS = 0;

    private static final int TYPE_USER_SIGN = 0;
    private static final int TYPE_USER_NIKENAME = 1;
    private TextView mTvSign;
    private TextView mTvNickname;
    private ImageView mCivAvatar;

    private ProfileHanlder mHandler = new ProfileHanlder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("个人中心");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        initView();
        initData();
    }


    private void initData() {
        //设置用户名称
        String nickName = SharedPreferencesManager.getUserNikeName();
        if (!TextUtils.isEmpty(nickName)) {
            mTvNickname.setText(nickName);
        } else {
            mTvNickname.setText(SharedPreferencesManager.getUserName());
        }
        //设置个性签名
        mTvSign.setText(SharedPreferencesManager.getUserSign());

        //设置头像
        Bitmap avatarBitmap = BitmapFactory.decodeFile(FileManager.getAvatarFileName());
        if (null != avatarBitmap) {
            mCivAvatar.setImageBitmap(avatarBitmap);
        }
    }

    private void initView() {
        findViewById(R.id.rl_user_avatar).setOnClickListener(this);
        findViewById(R.id.rl_nick_name).setOnClickListener(this);
        findViewById(R.id.rl_user_sign).setOnClickListener(this);

        mTvNickname = (TextView) findViewById(R.id.tv_nick_name);
        mTvSign = (TextView) findViewById(R.id.tv_user_sign);
        mCivAvatar = (ImageView) findViewById(R.id.civ_user_avatar);
        findViewById(R.id.tv_logout).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_user_avatar: {
                updateAvatar();
                break;
            }
            case R.id.rl_nick_name: {
                updateSignOrNickname(TYPE_USER_NIKENAME);
                break;
            }
            case R.id.rl_user_sign: {
                updateSignOrNickname(TYPE_USER_SIGN);
                break;
            }
            case R.id.tv_logout: {
                logout();
                break;
            }
        }
    }

    public void logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("确定要退出登录吗?");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                intent.putExtra("logout", "logout");
                startActivity(intent);

                Intent finishMainIntent = new Intent(Constants.INTENT_FINISH_MAIN_ACTIIVTY);
                sendBroadcast(finishMainIntent);

                LogUtils.d("profile finish call  .....");
                finish();

            }
        });
        builder.create().show();
    }

    private void updateAvatar() {

        new DialogFragment() {
            DialogFragment mDialogFragment;

            @Override
            public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
                mDialogFragment = this;
                getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
                View view = inflater.inflate(R.layout.dialog_avatar_select, container);
                view.findViewById(R.id.tv_choose).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UIUtils.showShortToast("选择");
                        IntentUtils.gotoChoosePicture(ProfileActivity.this);
                        mDialogFragment.dismiss();
                    }
                });
                view.findViewById(R.id.tv_take).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UIUtils.showShortToast("拍照");
                        IntentUtils.gotoTakePicture(ProfileActivity.this);
                        mDialogFragment.dismiss();
                    }
                });
                view.findViewById(R.id.tv_cancle).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDialogFragment.dismiss();
                    }
                });
                return view;
            }

        }.show(getFragmentManager(), "AvatarSelect");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void updateSignOrNickname(final int what) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_input_layout, null);
        builder.setView(view);
        final EditText etContent = (EditText) view.findViewById(R.id.et_dialog_input);
        if (what == TYPE_USER_NIKENAME) {
            etContent.setText(SharedPreferencesManager.getUserNikeName());
        }
        if (what == TYPE_USER_SIGN) {
            etContent.setText(SharedPreferencesManager.getUserSign());
        }
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String inputText = etContent.getText().toString().trim();
                        if (TextUtils.isEmpty(inputText)) {
                            inputText = "";
                        }
                        if (what == TYPE_USER_NIKENAME) {
                            SharedPreferencesManager.saveUserNikeName(inputText);
                            mTvNickname.setText(inputText);
                        }
                        if (what == TYPE_USER_SIGN) {
                            SharedPreferencesManager.saveUserSign(inputText);
                            mTvSign.setText(inputText);
                        }
                    }
                }

        );
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }

        );
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            String imagePath = null;
            if (requestCode == IntentUtils.TAKE_PICTURE_REQUEST_CODE) {
                imagePath = FileManager.getAvatarFileName();
            } else if (requestCode == IntentUtils.CHOOSE_PICTURE_REQUEST_CODE) {
                Uri uri = data.getData();
                String[] proj = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(uri, proj, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    imagePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                    cursor.close();
                }
            }

            final String finalPath = imagePath;
            new Thread(new Runnable() {

                @Override
                public void run() {
                    BitmapUtil.compressImage(finalPath, FileManager.getAvatarFileName());
                    mHandler.sendEmptyMessage(MSG_IMAGE_COMPRESS);
                }
            }).start();
        }
    }

    class ProfileHanlder extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_IMAGE_COMPRESS: {
                    mCivAvatar.setImageBitmap(BitmapFactory.decodeFile(FileManager.getAvatarFileName()));
                    break;
                }
            }
        }
    }
}
