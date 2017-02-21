package com.lyz.dewordhelper;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
    }
}
