package com.lyz.dewordhelper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ReportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        this.setTitle("训练报告");
    }
    public void onReturnClick(View v){
        finish();
    }
}
