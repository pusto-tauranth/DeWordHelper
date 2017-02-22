package com.lyz.dewordhelper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class StockActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);
        this.setTitle("词库管理");
    }
    public void onClick(View v){
        Intent intent=new Intent(this,StockDetailActivity.class);
        startActivity(intent);
    }
}
