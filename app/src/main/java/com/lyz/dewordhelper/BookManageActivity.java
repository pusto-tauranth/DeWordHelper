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

public class BookManageActivity extends ListActivity {

    String Language = WordsAccess.getSettingValueByName("language");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_manage);
        initToolbar();
        initList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initList();
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

    public void initList(){
        ArrayList<HashMap<String, String>> stockList = new ArrayList<HashMap<String, String>>();
        if (Language.equals("德语")) {
            HashMap<String, String> mapMark = new HashMap<String, String>();
            mapMark.put("Book", "收藏夹");
            mapMark.put("WordNum", "总词量：" + WordsAccess.getWordTotal("German", " WHERE " + Word.Key_mark + "=" + 1));
            stockList.add(mapMark);
            HashMap<String, String> mapSelf = new HashMap<String, String>();
            mapSelf.put("Book", "我添加的单词");
            mapSelf.put("WordNum", "总词量：" + WordsAccess.getWordTotal("German", " WHERE " + Word.Key_unit + "=" + -10));
            stockList.add(mapSelf);
            for (int i = 1; i <= 3; i++) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("Book", "新编大学德语 " + i);
                map.put("WordNum", "总词量：" + WordsAccess.getWordTotal("German", " WHERE " + Word.Key_book + "=" + i));
                stockList.add(map);
            }
        }

        else{
            HashMap<String, String> mapMark = new HashMap<String, String>();
            mapMark.put("Book", "收藏夹");
            mapMark.put("WordNum", "总词量：" + WordsAccess.getWordTotal("French", " WHERE " + Word.Key_mark+ "=" + 1));
            stockList.add(mapMark);
            HashMap<String, String> mapSelf = new HashMap<String, String>();
            mapSelf.put("Book", "我添加的单词");
            mapSelf.put("WordNum", "总词量：" + WordsAccess.getWordTotal("French", " WHERE " + Word.Key_unit + "=" + -10));
            stockList.add(mapSelf);
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("Book", "Reflets走遍法国");
            map.put("WordNum", "总词量：" + WordsAccess.getWordTotal("French", " WHERE " + Word.Key_book+ "=" + 1));
            stockList.add(map);
        }
        SimpleAdapter mSchedule = new SimpleAdapter(this,
                stockList,
                R.layout.activity_stock_item,
                new String[]{"Book", "WordNum"},
                new int[]{R.id.stock, R.id.wordNum});
        setListAdapter(mSchedule);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InsertDialog insertDialog=new InsertDialog(BookManageActivity.this,-10,-10);
                insertDialog.show();
            }
        });
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        TextView bookTV=(TextView)v.findViewById(R.id.stock);
        if(position==0){
            Intent intent = new Intent(this,StockDetailActivity.class);
            intent.putExtra("Book","Mark");
            this.startActivity(intent);
        } else if(position==1){
            Intent intent = new Intent(this,StockDetailActivity.class);
            intent.putExtra("Book","-10");
            intent.putExtra("Unit","-10");
            this.startActivity(intent);
        }else{
            String bookStr=bookTV.getText().toString().substring(7);
            Intent intent = new Intent(this,UnitManageActivity.class);
            if(Language.equals("德语"))
            {intent.putExtra("Book",bookStr);}
            if(Language.equals("法语"))
            {intent.putExtra("Book",""+1);}
            this.startActivity(intent);
        }

    }
}
