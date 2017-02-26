package com.lyz.dewordhelper;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.lyz.dewordhelper.DB.Word;
import com.lyz.dewordhelper.DB.WordsAccess;
import com.lyz.dewordhelper.Dialog.InsertDialog;

import java.util.ArrayList;
import java.util.HashMap;

public class StockDetailActivity extends ListActivity {
    int einheit;
    int book;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_stock_detail);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        einheit = Integer.parseInt(getIntent().getStringExtra("Einheit"));
        book = Integer.parseInt(getIntent().getStringExtra("Book"));
        toolbar.setTitle("Einheit"+einheit+", Buch"+book);
        ArrayList<HashMap<String, String>> myList;
        String WHERE=" WHERE "+Word.Key_einheit+" = "+einheit+" AND "+Word.Key_book+" = "+book;
        myList=WordsAccess.getWordList(WHERE);
        SimpleAdapter mSchedule = new SimpleAdapter(this,
                myList,
                R.layout.activity_stock_detail_item,
                new String[]{"All", "wordId"},
                new int[]{R.id.All, R.id.wordId});
        setListAdapter(mSchedule);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InsertDialog insertDialog=new InsertDialog(StockDetailActivity.this,book,einheit);
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
}