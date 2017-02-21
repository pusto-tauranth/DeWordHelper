package com.lyz.dewordhelper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class GenderTrainingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gender_training);
        this.setTitle("词性训练");
    }
    public void onGenderAClick(View v){
        if(true){
            Intent intent=new Intent(this,ReportActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
