package com.lyz.dewordhelper;

import android.app.ListActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.lyz.dewordhelper.DB.WordsAccess;

import java.util.ArrayList;
import java.util.HashMap;

public class StockDetailActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_detail);



        /*WordsAccess access=new WordsAccess(this);
        ArrayList<HashMap<String,String>> wordList = access.getWordList();
        if(!wordList.isEmpty()){
            ListView lv=getListView();
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    TextView word_Id = (TextView)findViewById(R.id.word_Id);
                }
            });
        }*/
    }
    public void onClick(View v){
        finish();
    }
}
