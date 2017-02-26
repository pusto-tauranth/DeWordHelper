package com.lyz.dewordhelper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class PlTrainingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_pl_training);
        initToolbar();
    }

    public void initToolbar(){
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView title=(TextView)findViewById(R.id.tv_title);
        title.setText("复数训练");//改成单词
    }

    public void onPlAClick(View v) {
        if (true) {
            Intent intent = new Intent(this, ReportActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
