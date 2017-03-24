package com.lyz.dewordhelper;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.lyz.dewordhelper.DB.Word;
import com.lyz.dewordhelper.DB.WordsAccess;
import com.lyz.dewordhelper.Dialog.InsertDialog;

import java.util.ArrayList;
import java.util.HashMap;

public class StockDetailActivity extends ListActivity {
    int unit;
    int book;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_stock_detail);
        unit = Integer.parseInt(getIntent().getStringExtra("Unit"));
        book = Integer.parseInt(getIntent().getStringExtra("Book"));
        initToolbar();
        ArrayList<HashMap<String, String>> wordList;
        String WHERE=" WHERE "+Word.Key_unit +" = "+ unit +" AND "+Word.Key_book+" = "+book;
        wordList=WordsAccess.getWordList(WHERE);
        SimpleAdapter mSchedule = new SimpleAdapter(this,
                wordList,
                R.layout.activity_stock_detail_item,
                new String[]{"All", "wordId"},
                new int[]{R.id.All, R.id.wordId});
        setListAdapter(mSchedule);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InsertDialog insertDialog=new InsertDialog(StockDetailActivity.this,book, unit);
                insertDialog.show();
            }
        });
    }
   @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        // TODO Auto-generated method stub
       super.onListItemClick(l, v, position, id);
       TextView idTV=(TextView)v.findViewById(R.id.wordId);
       String wordIdStr=idTV.getText().toString();
       Intent intent = new Intent(this,WordDetailActivity.class);
       intent.putExtra("WordIdStr",wordIdStr);
       this.startActivity(intent);
    }
    public void initToolbar() {
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView title = (TextView) findViewById(R.id.tv_title);
        title.setText("Einheit" + unit + ", Buch" + book);
    }
}