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
import java.util.StringTokenizer;

public class StockDetailActivity extends ListActivity {
    String unitStr;
    String bookStr;
    String titleStr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_stock_detail);
        bookStr = getIntent().getStringExtra("Book");
        unitStr = getIntent().getStringExtra("Unit");
        initList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initList();
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

    public void initList(){
        if(bookStr.equals("Mark")){
            titleStr="收藏夹";
            initToolbar();
            ArrayList<HashMap<String, String>> wordList;
            String WHERE=" WHERE "+Word.Key_mark +" = "+ 1;
            wordList=WordsAccess.getWordList(WHERE);
            SimpleAdapter simpleAdapter = new SimpleAdapter(this,
                    wordList,
                    R.layout.activity_stock_detail_item,
                    new String[]{"All", "wordId"},
                    new int[]{R.id.All, R.id.wordId});
            setListAdapter(simpleAdapter);
            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    InsertDialog insertDialog=new InsertDialog(StockDetailActivity.this,-1,0);
                    insertDialog.show();
                }
            });
        }else{
            titleStr="Einheit" + unitStr + ", Buch" + bookStr;
            if(bookStr.equals("0")){
                titleStr="我添加的单词";
            }
            initToolbar();
            ArrayList<HashMap<String, String>> wordList;
            String WHERE=" WHERE "+Word.Key_unit +" = "+ unitStr +" AND "+Word.Key_book+" = "+bookStr;
            wordList=WordsAccess.getWordList(WHERE);
            SimpleAdapter simpleAdapter = new SimpleAdapter(this,
                    wordList,
                    R.layout.activity_stock_detail_item,
                    new String[]{"All", "wordId"},
                    new int[]{R.id.All, R.id.wordId});
            setListAdapter(simpleAdapter);
            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    InsertDialog insertDialog=new InsertDialog(StockDetailActivity.this,Integer.parseInt(bookStr),Integer.parseInt(unitStr));
                    insertDialog.show();
                }
            });
        }
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
        title.setText(titleStr);
    }
}