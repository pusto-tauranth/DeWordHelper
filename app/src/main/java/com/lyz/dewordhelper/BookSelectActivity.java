package com.lyz.dewordhelper;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class BookSelectActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_select);
        initToolbar();
        TextView bookTXtv;
        //bookTXtv=(TextView)findViewById(R.id.booktx);
        //bookTXtv.setText("新编大学"+String.valueOf(getIntent().getStringExtra("Language")));
        ArrayList<HashMap<String, String>> myList = new ArrayList<HashMap<String, String>>();
        for (int i=1;i<=2;i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("Book",String.valueOf(i));
            map.put("WordsNum",String.valueOf(400));
            myList.add(map);
        }
        SimpleAdapter mSchedule = new SimpleAdapter(this,
                myList,
                R.layout.activity_book_select_item,
                new String[]{"Book", "WordsNum"},
                new int[]{R.id.book, R.id.words});
        setListAdapter(mSchedule);
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
        title.setText("选择书本");
    }

    public void onAll(View v){
        //Intent intent=new Intent(this,StockSelectActivity.class);
        //intent.putExtra("Book","All");
        Intent intent=new Intent(this,TrainingSettingsActivity.class);
        intent.putExtra("Unit","All");
        intent.putExtra("Book","All");
        startActivity(intent);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        // TODO Auto-generated method stub
        super.onListItemClick(l, v, position, id);
        TextView bookTV=(TextView)v.findViewById(R.id.book);
        String bookStr=bookTV.getText().toString();
        Intent intent = new Intent(this,StockSelectActivity.class);
        intent.putExtra("Book",bookStr);
        this.startActivity(intent);
    }
}