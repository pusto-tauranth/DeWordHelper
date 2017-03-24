package com.lyz.dewordhelper;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.lyz.dewordhelper.DB.Word;
import com.lyz.dewordhelper.DB.WordsAccess;

import java.util.ArrayList;
import java.util.HashMap;

public class BookManageActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_manage);
        initToolbar();
        ArrayList<HashMap<String, String>> stockList = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> mapFavorite = new HashMap<String, String>();
        mapFavorite.put("Book","收藏夹");
        mapFavorite.put("WordNum","总词量："+ WordsAccess.getWordTotal("German"," WHERE "+ Word.Key_book+"="+0));
        stockList.add(mapFavorite);
        for (int i=1;i<=2;i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("Book","新编大学德语 "+i);
            map.put("WordNum","总词量："+ WordsAccess.getWordTotal("German"," WHERE "+ Word.Key_book+"="+i));
            stockList.add(map);
        }
        SimpleAdapter mSchedule = new SimpleAdapter(this,
                stockList,
                R.layout.activity_stock_item,
                new String[]{"Book", "WordNum"},
                new int[]{R.id.stock, R.id.wordNum});
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
        title.setText("词库管理-书本");
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        // TODO Auto-generated method stub
        super.onListItemClick(l, v, position, id);
        TextView bookTV=(TextView)v.findViewById(R.id.stock);
        String bookStr;
        if(position==0){
            bookStr="0";
            Intent intent = new Intent(this,StockDetailActivity.class);
            intent.putExtra("Book",bookStr);
            intent.putExtra("Unit","0");
            this.startActivity(intent);
        }else{
            bookStr=bookTV.getText().toString().substring(7);
            Intent intent = new Intent(this,UnitManageActivity.class);
            intent.putExtra("Book",bookStr);
            this.startActivity(intent);
        }

    }
}
