package com.youpeng.wefriend.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.AsyncTask;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

import com.youpeng.wefriend.R;
import com.youpeng.wefriend.datacenter.Constants;
import com.youpeng.wefriend.datacenter.SharedPreferencesManager;
import com.youpeng.wefriend.netcenter.SimulationLogin;
import com.youpeng.wefriend.utils.LogUtils;
import com.youpeng.wefriend.utils.UIUtils;

public class LoginActivity extends AppCompatActivity {

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mActAccount;
    private EditText mEtPasswod;
    private View mProgressView;
    private View mLoginFormView;
    String account;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
     //   mActAccount = (AutoCompleteTextView) findViewById(R.id.act_account);
        mActAccount = (AutoCompleteTextView) findViewById(R.id.act_account);

        mEtPasswod = (EditText) findViewById(R.id.password);

        Button mEmailSignInButton = (Button) findViewById(R.id.btn_sign_in);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

       // mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.framelayout_progress);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mActAccount.setError(null);
        mEtPasswod.setError(null);

        // Store values at the time of the login attempt.
        account = mActAccount.getText().toString();
        password = mEtPasswod.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mEtPasswod.setError("密码不能为空");
            focusView = mEtPasswod;
            cancel = true;
        }

        // Check for a valid email address.
        if (account == null || account.length() != 9) {
            LogUtils.d("学号长度 " + account.length());

            mActAccount.setError("学号应该为九位");
            focusView = mActAccount;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(account, password);
            mAuthTask.execute((Void) null);
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    private void showProgress(final boolean show) {
        if (show) {
            mProgressView.setVisibility(View.VISIBLE);
        } else {
            mProgressView.setVisibility(View.GONE);
        }
    }


    private void addEmailsToAutoComplete(List<String> accountCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, accountCollection);

        mActAccount.setAdapter(adapter);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            Intent intent = new Intent(Constants.INTENT_FINISH_MAIN_ACTIIVTY);
            sendBroadcast(intent);
        }
        return super.onKeyDown(keyCode, event);
    }

    public class UserLoginTask extends AsyncTask<Void, Void, String> {

        private final String mAccount;
        private final String mPassword;

        UserLoginTask(String account, String password) {
            mAccount = account;
            mPassword = password;
        }

        @Override
        protected String doInBackground(Void... params) {
            String userName = SimulationLogin.getInstance().userLogin(mAccount, mPassword);

            //SystemClock.sleep(10000);
            LogUtils.d("用户名 " + userName);
            return userName;
        }

        @Override
        protected void onPostExecute(final String userName) {
            mAuthTask = null;
            showProgress(false);

            if (!TextUtils.isEmpty(userName)) {
                SharedPreferencesManager.saveUserName(userName);
                SharedPreferencesManager.saveUserAccount(account);
                SharedPreferencesManager.saveUserPwd(password);

                //send to server
                Intent intent = getIntent();
                String logout = intent.getStringExtra("logout");
                //从退出登录过来的
                if (!TextUtils.isEmpty(logout)) {
                    intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                finish();

            } else {
                switch (SimulationLogin.STATUS_CODE) {
                    case SimulationLogin.PWD_ERR: {
                        mEtPasswod.setError("密码错误");
                        mEtPasswod.requestFocus();
                        break;
                    }
                    case SimulationLogin.SERVER_BUSY: {
                        //服务器忙
                        UIUtils.showLongToast("服务器忙,请稍后重试");
                        break;
                    }
                    case SimulationLogin.NET_ERR: {
                        //网络超时
                        UIUtils.showLongToast("网络连接错误,请稍后重试");
                        break;
                    }
                }
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

