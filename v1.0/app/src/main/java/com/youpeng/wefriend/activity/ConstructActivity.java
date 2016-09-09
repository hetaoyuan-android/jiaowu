package com.youpeng.wefriend.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;
import com.youpeng.wefriend.R;

public class ConstructActivity extends AppCompatActivity {

    private ShimmerTextView mShimmerText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_construct);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        toolbar.setTitle("建设中");

        mShimmerText = (ShimmerTextView) findViewById(R.id.shimmer_tv);
        Shimmer shimmer = new Shimmer();
        shimmer.start(mShimmerText);
    }

}
