package com.lyz.dewordhelper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.lyz.dewordhelper.DB.Word;
import com.lyz.dewordhelper.DB.WordsAccess;

import java.util.ArrayList;
import java.util.HashMap;

public class ReportActivity extends AppCompatActivity {

    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_report);
        initToolbar();

        initKeyPage();
    }

    public void onReturnClick(View v){
        finish();
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
        title.setText("训练报告");
    }

    public void initKeyPage() {
        lv = (ListView)findViewById(R.id.errorList);
        ArrayList<HashMap<String, String>> myList;
        String WHERE = " WHERE " + Word.Key_status + " = " + "-1"
                +" ORDER BY "+Word.Key_errortimes + " DESC ";
        myList = WordsAccess.getWordList(WHERE);
        WordsAccess.setAccuracy();
        WordsAccess.setErrorTimes();
        int newTrainingTimes=WordsAccess.statusReset();
        WordsAccess.setTrainingTimes(newTrainingTimes);

        SimpleAdapter listAdapter = new SimpleAdapter(this,
                myList,
                R.layout.activity_stock_detail_item,
                new String[]{"All", "wordId"},
                new int[]{R.id.All, R.id.wordId});
        lv.setAdapter(listAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView idTV = (TextView) view.findViewById(R.id.wordId);
                String wordIdStr = idTV.getText().toString();
                Intent intent = new Intent(ReportActivity.this, WordDetailActivity.class);
                intent.putExtra("WordIdStr", wordIdStr);
                startActivity(intent);
            }
        });
    }
}
