package com.lyz.dewordhelper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.lyz.dewordhelper.DB.WordsHelper;

public class MainActivity extends AppCompatActivity {

    public WordsHelper wordsHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wordsHelper=new WordsHelper(this);
        wordsHelper.openDatabase();
        wordsHelper.closeDatabase();
    }
    public void onTrainingClick(View v){
        Intent intent=new Intent(this,StockSelectActivity.class);
        startActivity(intent);
    }
    public void onStockClick(View v){
        Intent intent=new Intent(this,StockActivity.class);
        startActivity(intent);
    }
}