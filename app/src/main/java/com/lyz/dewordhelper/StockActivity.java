package com.lyz.dewordhelper;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.lyz.dewordhelper.Dialog.InsertDialog;

import java.util.ArrayList;
import java.util.HashMap;

public class StockActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("词库管理");
        ArrayList<HashMap<String, String>> myList = new ArrayList<HashMap<String, String>>();
        for (int i=1;i<=10;i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("Einheit",String.valueOf(i));
            map.put("Book",String.valueOf(1));
            myList.add(map);
        }
        for (int i=1;i<=10;i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("Einheit",String.valueOf(i));
            map.put("Book",String.valueOf(2));
            myList.add(map);
        }

        SimpleAdapter mSchedule = new SimpleAdapter(this,
                myList,
                R.layout.activity_stock_item,
                new String[]{"Einheit", "Book"},
                new int[]{R.id.einheit, R.id.book});
        setListAdapter(mSchedule);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InsertDialog insertDialog=new InsertDialog(StockActivity.this,0,0);
                insertDialog.show();
            }
        });
    }


    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        // TODO Auto-generated method stub
        super.onListItemClick(l, v, position, id);
        TextView einheitTV=(TextView)v.findViewById(R.id.einheit);
        TextView bookTV=(TextView)v.findViewById(R.id.book);
        String einheitStr=einheitTV.getText().toString();
        String bookStr=bookTV.getText().toString();
        Intent intent = new Intent(this,StockDetailActivity.class);
        intent.putExtra("Einheit",einheitStr);
        intent.putExtra("Book",bookStr);
        StockActivity.this.startActivity(intent);
    }
}


