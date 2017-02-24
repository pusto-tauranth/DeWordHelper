package com.lyz.dewordhelper;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.lyz.dewordhelper.DB.Word;
import com.lyz.dewordhelper.DB.WordsAccess;

import java.util.ArrayList;
import java.util.HashMap;

public class StockDetailActivity extends ListActivity {
    Word word;
    int wordListInt;
    //String LIst;
    WordsAccess wordsAccess;
    //ArrayList<Word> einheit,allList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        //this.setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        String value = intent.getStringExtra("WordList");
        //String ID =intent.getStringExtra("ID");
        this.setTitle("词库"+"WordList");
        wordListInt = Integer.parseInt(value)+1;
        wordsAccess = new WordsAccess(this);
        ListView list = (ListView) findViewById(R.id.WordListView);
        ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
        for(int i=1;i<464;i++)
        {
            HashMap<String, String> map = new HashMap<String, String>();
            word=wordsAccess.getWordById(i);
            if(word.einheit!=wordListInt) continue;
            //ID=""+i;
            map.put("All",word.gender+"  "+word.word+"  "+word.pl+"  "+word.chn+"  "+word.einheit+" "+wordListInt);
            mylist.add(map);
        }
        SimpleAdapter mSchedule = new SimpleAdapter(this,
                mylist,
                R.layout.activity_stock_detail,
                new String[]{"All", "Word"},
                new int[]{R.id.All, R.id.Word});
        setListAdapter(mSchedule);
    }
   @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        // TODO Auto-generated method stub
        super.onListItemClick(l, v, position, id);

        Intent intent = new Intent();
        intent.setClass(this,WordDetailActivity.class);
        intent.putExtra("WordPosition", ""+position);
        Intent intent2 = getIntent();
        String value = intent2.getStringExtra("WordList");
        intent.putExtra("WordListLocation",value);
        this.startActivity(intent);

        System.out.println(position);
    }
}