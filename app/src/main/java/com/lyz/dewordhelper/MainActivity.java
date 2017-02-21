package com.lyz.dewordhelper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

/*    <Button
        android:text="设置"
        android:onClick="onSettingsClick"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:id="@+id/button3"
        android:layout_weight="1"
        android:textSize="35sp"
        android:background="@android:color/darker_gray"/>*/