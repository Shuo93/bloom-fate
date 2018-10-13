package com.huawei.bloomfate.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RatingBar;

import com.huawei.bloomfate.R;

public class BioActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bio);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frag_bio, new BioFragment()).commit();
        }
    }
}
