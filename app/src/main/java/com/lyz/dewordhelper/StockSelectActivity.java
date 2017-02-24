package com.lyz.dewordhelper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class StockSelectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_select);
        this.setTitle("选择词库");
    }
    public void onStock1Click(View v){
        Intent intent=new Intent(this,TrainingSettingsActivity.class);
        startActivity(intent);
        finish();
    }
}
