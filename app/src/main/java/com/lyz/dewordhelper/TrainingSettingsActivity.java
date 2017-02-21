package com.lyz.dewordhelper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class TrainingSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_settings);
        this.setTitle("训练设置");
    }
    public void onGenderClick(View v){
        Intent intent=new Intent(this,GenderTrainingActivity.class);
        startActivity(intent);
        finish();
    }
    public void onPlClick(View v){
        Intent intent=new Intent(this,PlTrainingActivity.class);
        startActivity(intent);
        finish();
    }
}
