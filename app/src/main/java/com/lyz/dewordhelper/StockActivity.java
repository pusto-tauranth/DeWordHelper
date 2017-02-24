package com.lyz.dewordhelper;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class StockActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_stock);
        //setContentView(R.layout.activity_main2);
        //DataAccess dataAccess = new DataAccess(this);
        // ArrayList<WordList> wordList = dataAccess.QueryList("BOOKID = '"+DataAccess.bookID+"'", null);
        ListView list = (ListView) findViewById(R.id.MyListView);
        ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < 6; i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("ItemTitle", "词库" + (i + 1));
            map.put("ItemText", "新编大学德语" + (i + 1));
            mylist.add(map);
        }

        SimpleAdapter mSchedule = new SimpleAdapter(this,
                mylist,
                R.layout.activity_stock,
                new String[]{"ItemTitle", "ItemText"},
                new int[]{R.id.ItemTitle, R.id.ItemText});
        setListAdapter(mSchedule);
    }


    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        // TODO Auto-generated method stub
        super.onListItemClick(l, v, position, id);

        Intent intent = new Intent();
        intent.setClass(StockActivity.this,StockDetailActivity.class);
        intent.putExtra("WordList", ""+position);

       // intent.putExtra("ID",id);
        StockActivity.this.startActivity(intent);

        System.out.println(position);
    }
}


